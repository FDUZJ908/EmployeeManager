package EmployeeManager;

import EmployeeManager.cls.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static EmployeeManager.Server.reportAgentID;
import static EmployeeManager.Server.submitSecret;

@Controller
public class BackgroundController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Server server;

    String mesgToLeader = "您有一份新报告需要审批，可进入 报告查询-审批报告 查看。";
    String mesgToSubordinate = "您有一份报告已被审批，可进入 报告查询-我的报告 查看。";

    @RequestMapping("/GeneralReport")
    public String GeneralReport(@RequestParam("state") String STATE,
                                @RequestParam("code") String code,
                                Model model) {
        String UserId = server.getUserId(code, submitSecret);
        logger.info("Request GeneralReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        // Department-Leader-LeaderID
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);
        String userName = server.getUserName(UserId);

        model.addAttribute("userName", userName);
        model.addAttribute("UserId", UserId);
        model.addAttribute("list", DLeaders);
        return "GeneralReport";
    }

    @PostMapping(value = "/GeneralReport")
    @ResponseBody
    public ResponseMsg GeneralReportPost(@RequestParam("content") String content,
                                         @RequestParam("type") String type,
                                         @RequestParam("leader") String leader,
                                         @RequestParam("file") MultipartFile file,
                                         @RequestParam("UserId") String UserId,
                                         Model model) {
        logger.info("Post GeneralReport: " + UserId); //log
        ResponseMsg responseMsg = new ResponseMsg("", ""); // create ajax response
        if(!server.reported.containsKey(UserId))
            server.reported.put(UserId,server.maxReportCount);
        int remaining=server.reported.get(UserId);
        if (remaining<=0) {
            responseMsg.setMsg("超过每天提交次数上限！请明天再提交。");
            responseMsg.setNum("0");
            return responseMsg;
        }

        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            responseMsg.setMsg("文件上传失败！");
            responseMsg.setNum("0");
            return responseMsg;
        }
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";
        String sqlMessage = "'" + UserId + "','" + leader + "'," + type + ",'" + content + "','" + pathCurrent + "','" +
                currentTime + "'";
        server.jdbcTemplate.update("insert into undealedGeneralReport " +
                "(userID,leaderName,category,reportText,reportPath,submitTime) values(" + sqlMessage + ")");

        server.reported.put(UserId,--remaining);
        try {
            server.sendMessage(leader, mesgToLeader, true, reportAgentID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        responseMsg.setMsg("一般报告提交成功！");
        responseMsg.setNum("1");
        return responseMsg;
    }

    @RequestMapping("/CaseReport")
    public String CaseReport(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request CaseReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        String userName = server.getUserName(UserId);
        List<String> AllUsers = server.getAllUsers();
        // Department-Leader-LeaderID
        List<String> DLeaders = server.getLeaderInCase(UserId);

        model.addAttribute("UserId", UserId);
        model.addAttribute("userName", userName);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("list", DLeaders);
        return "CaseReport";
    }

    @PostMapping(value = "/CaseReport")
    @ResponseBody
    public ResponseMsg CaseReportPost(@RequestParam("members") String members,
                                      @RequestParam("UserId") String UserId,
                                      @RequestParam("content") String content,
                                      @RequestParam("type") String type,
                                      @RequestParam("score_type") String score_type,
                                      @RequestParam("score") String score,
                                      @RequestParam("leader") String leader,
                                      @RequestParam("file") MultipartFile file,
                                      Model model) {
        logger.info("Post CaseReport: " + UserId); //log
        ResponseMsg responseMsg = new ResponseMsg("", ""); // create ajax response

        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            responseMsg.setMsg("文件上传失败！");
            responseMsg.setNum("0");
            return responseMsg;
        }
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";
        String sqlMessage = "'" + UserId + "','" + leader + "','" + members + "'," + type + ",'" + content + "','" +
                pathCurrent + "'," + score + ",'" + currentTime + "'," + score_type;
        server.jdbcTemplate.update("insert into undealedCaseReport " +
                "(userID,leaderName,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");

        try {
            server.sendMessage(leader, mesgToLeader, true, reportAgentID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        responseMsg.setMsg("个案报告提交成功！");
        responseMsg.setNum("1");
        return responseMsg;
    }

    @RequestMapping("/LeadershipReport")
    public String Leadership(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request LeadershipReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }
        String userName = server.getUserName(UserId);
        int userPrivilege = server.getUserPrivilege(UserId);
        int scoreLimit = server.getLeaderScoreLimit(userPrivilege);
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);
        List<String> AllUsers = server.getAllDepartmentUsers(UserId, userPrivilege);
        model.addAttribute("UserId", UserId);
        model.addAttribute("userName", userName);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("list", DLeaders);
        model.addAttribute("scoreLimit", scoreLimit);
        return "LeadershipReport";
    }

    @PostMapping(value = "/LeadershipReport")
    @ResponseBody
    public ResponseMsg LeadershipPost(@RequestParam("members") String members,
                                      @RequestParam("UserId") String UserId,
                                      @RequestParam("content") String content,
                                      @RequestParam("type") String type,
                                      @RequestParam("score_type") String score_type,
                                      @RequestParam("score") String score,
                                      @RequestParam("file") MultipartFile file,
                                      Model model) {
        logger.info("Post LeadershipReport: " + UserId); //log
        ResponseMsg responseMsg = new ResponseMsg("", "");// create ajax response
        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            responseMsg.setMsg("文件上传失败！");
            responseMsg.setNum("0");
            return responseMsg;
        }
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";
        String sqlMessage = "'" + UserId + "','" + members + "'," + type + ",'" + content + "','" + pathCurrent + "'," +
                score + ",'" + server.currentTime() + "'," + score_type;
        server.jdbcTemplate.update("insert into leaderReport " +
                "(userID,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");

        int singleScore = Integer.parseInt(score);
        if (score_type.equals("0"))
            singleScore = -singleScore;

        String sqlLeaderScore = "UPDATE user set s_score=s_score + " + singleScore + " where userID=?";
        Object args[] = new Object[]{UserId};
        server.jdbcTemplate.update(sqlLeaderScore, args);
        responseMsg.setMsg("领导报告提交成功！");
        responseMsg.setNum("1");
        return responseMsg;
    }


    //slected_type 为设置button点击后颜色准备
    @RequestMapping("/RankingList")
    public String RankingList(@RequestParam("state") String STATE,
                              Model model) {
        logger.info("Request RankingList"); //log

        String sql = "select userName,s_score,avatarURL,duty,title from user order by s_score desc";
        List<User> users = server.jdbcTemplate.query(sql, new Mapper<User>(User.class));

        model.addAttribute("list", users);
        model.addAttribute("selected_type", 3);
        if (STATE.equals("PC")) return "Rank";
        return "RankingList";
    }

    @PostMapping(value = "/RankingList")
    public String RankingListPost(@RequestParam("button") String type,
                                  @RequestParam("state") String STATE,
                                  Model model) {
        logger.info("Post RankingList: " + type); //log

        if (type.equals("总排行")) {
            String sql = "select userName,s_score,avatarURL,duty,title from user order by s_score desc";
            List<User> users = server.jdbcTemplate.query(sql, new Mapper<User>((User.class)));
            model.addAttribute("selected_type", 3);
            model.addAttribute("list", users);
            if (STATE.equals("PC")) return "Rank";
            return "RankingList";
        } else {
            String selectedType;
            if (type.equals("领导干部")) {
                selectedType = "3";
                model.addAttribute("selected_type", 1);
            } else if (type.equals("中层干部")) {
                selectedType = "2";
                model.addAttribute("selected_type", 2);
            } else if (type.equals("一般干部")) {
                selectedType = "1";
                model.addAttribute("selected_type", 4);
            } else {
                selectedType = "0";
                model.addAttribute("selected_type", 5);
            }
            String sql = "select userName,s_score,avatarURL,duty,title from user where position=" + selectedType + " order by s_score desc";
            List<User> users = server.jdbcTemplate.query(sql, new Mapper<User>((User.class)));
            model.addAttribute("list", users);
            if (STATE.equals("PC")) return "Rank";
            return "RankingList";
        }
    }

    @RequestMapping("/ReportApproval")
    public String ReportApproval(@RequestParam("code") String CODE,
                                 @RequestParam("state") String STATE,
                                 Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request ReportApproval: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        String sqlGen = "select reportID,userID,category,reportText,submitTime,userName,reportPath " +
                "from undealedGeneralReport natural join user where leaderName=(select userName from user " +
                "where userID=?)";

        String sqlCase = "select reportID,userID,category,reportText,submitTime,members,singleScore,userName,reportPath " +
                "from undealedCaseReport natural join user " +
                "where leaderName=(select userName from user where userID=?)";
        List<GeneralReport> listGen = server.jdbcTemplate.query(sqlGen, new Object[]{UserId}, new Mapper<GeneralReport>(GeneralReport.class));
        List<CaseReport> listCase = server.jdbcTemplate.query(sqlCase, new Object[]{UserId}, new Mapper<CaseReport>(CaseReport.class));
        model.addAttribute("list1", listGen);
        model.addAttribute("list2", listCase);
        return "ReportApproval";
    }

    @PostMapping("/ReportApproval")
    @ResponseBody
    public ReportApprovalAjax ReportApprovalPost(@RequestBody ReportApprovalAjax ajax,
                                                 Model model) {
        String check1 = ajax.getCheck1();
        String check2 = ajax.getCheck2();
        String[] reports1 = check1.split(",");
        String[] reports2 = check2.split(",");
        String reportStatus = ajax.getReportStatus();
        String reportComment = ajax.getReportComment();

        String updateSql = "";
        String checkTime = server.currentTime();

        logger.info("'" + check1 + "'");
        logger.info("'" + check2 + "'");

        if (!check1.isEmpty()) {
            for (int i = 0; i < reports1.length; i++) {
                Map<String, Object> generalReport = server.getUndealedGeneralReport(reports1[i]);
                if (generalReport == null) continue;
                String sqlMessage = "";
                int singleScore;

                sqlMessage = "'" + generalReport.get("userID").toString() + "'" +
                        ",'" + generalReport.get("leaderName").toString() + "'," +
                        generalReport.get("category").toString() +
                        ",'" + generalReport.get("reportText").toString() + "'" +
                        ",'" + generalReport.get("reportPath").toString() + "'" +
                        ",'" + generalReport.get("submitTime").toString() + "'" +
                        ",'" + checkTime + "'," +
                        reportStatus +
                        ",'" + reportComment + "'";
                if (generalReport.get("category").toString().equals("1"))
                    singleScore = 1;
                else
                    singleScore = 2;
                if (reportStatus.equals("0"))
                    singleScore = 0;

                try {
                    updateSql = "UPDATE user set s_score=s_score + " + singleScore +
                            " where userID=?";
                    Object args[] = new Object[]{generalReport.get("userID").toString()};
                    server.jdbcTemplate.update(updateSql, args);

                    updateSql = "insert into generalReport " +
                            "(userID,leaderName,category,reportText,reportPath,submitTime,checkTime,isPass,comment) " +
                            "values(" + sqlMessage + ")";
                    server.jdbcTemplate.update(updateSql);
                    updateSql = "delete from undealedGeneralReport where reportID=" + reports1[i];
                    server.jdbcTemplate.update(updateSql);
                    ajax.setCheckResponse("审批成功!");
                    ajax.setCheckNum("1");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ajax.setCheckResponse("审批失败!");
                    ajax.setCheckNum("0");
                    return ajax;
                }

                try {
                    server.sendMessage(generalReport.get("userID").toString(), mesgToSubordinate, false, reportAgentID);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (!check2.isEmpty()) {
            for (int i = 0; i < reports2.length; i++) {
                Map<String, Object> caseReport = server.getUndealedCaseReport(reports2[i]);
                if (caseReport == null) continue;
                String sqlMessage = "";
                int singleScore;
                int scoreType;

                if (reportStatus.equals("1")) {
                    if (caseReport.get("scoreType").toString().equals("true")) {
                        singleScore = Integer.parseInt(caseReport.get("singleScore").toString());
                    } else
                        singleScore = -Integer.parseInt(caseReport.get("singleScore").toString());
                } else {
                    singleScore = 0;
                }
                sqlMessage = "'" + caseReport.get("userID").toString() + "'" +
                        ",'" + caseReport.get("leaderName").toString() + "'" +
                        ",'" + caseReport.get("members").toString() + "'," +
                        caseReport.get("category").toString() +
                        ",'" + caseReport.get("reportText").toString() + "'" +
                        ",'" + caseReport.get("reportPath").toString() + "'" +
                        "," + caseReport.get("scoreType").toString() + "," +
                        caseReport.get("singleScore").toString() +
                        ",'" + caseReport.get("submitTime").toString() + "'" +
                        ",'" + checkTime + "'," +
                        reportStatus +
                        ",'" + reportComment + "'";
                if (!caseReport.get("members").toString().isEmpty()) {
                    String[] member = caseReport.get("members").toString().split(",");

                    for (int j = 0; j < member.length; j++) {
                        updateSql = "update user set s_score = s_score + " + singleScore +
                                " where userName=?";
                        Object args1[] = new Object[]{member[j]};
                        server.jdbcTemplate.update(updateSql, args1);
                    }
                }

                try {
                    updateSql = "update user set s_score = s_score + " + singleScore +
                            " where userID=?";
                    Object args2[] = new Object[]{caseReport.get("userID").toString()};
                    server.jdbcTemplate.update(updateSql, args2);

                    updateSql = "insert into caseReport " +
                            "(userID,leaderName,members,category,reportText,reportPath,scoreType," +
                            "singleScore,submitTime,checkTime,isPass,comment) " +
                            "values(" + sqlMessage + ")";
                    server.jdbcTemplate.update(updateSql);
                    updateSql = "delete from undealedCaseReport where reportID=" + reports2[i];
                    server.jdbcTemplate.update(updateSql);
                    ajax.setCheckResponse("审批成功!");
                    ajax.setCheckNum("1");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ajax.setCheckResponse("审批失败!");
                    ajax.setCheckNum("0");
                    return ajax;
                }

                try {
                    server.sendMessage(caseReport.get("userID").toString(), mesgToSubordinate, false, reportAgentID);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return ajax;
    }

    @RequestMapping("/HistoryReport")
    public String HistoryReport(@RequestParam("code") String CODE,
                                @RequestParam("state") String STATE,
                                Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request HistoryReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        String UserName = server.getUserName(UserId);
        Map<String, Object> defValue = new HashMap<String, Object>();
        defValue.put("userName", UserName);
        defValue.put("userID", UserId);

        String sqlGen = "select reportID,leaderName,category,reportText,submitTime,checkTime,isPass,comment,reportPath " +
                "from generalReport where userID =? order by submitTime desc";
        Object args[] = new Object[]{UserId};
        defValue.put("type", HistoryReport.GENERAL | HistoryReport.APPROVED);
        List<HistoryReport> reports = server.jdbcTemplate.query(sqlGen, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));

        String sqlCase = "select reportID, leaderName, category, reportText, submitTime, checkTime, isPass, " +
                "comment, singleScore, scoreType, members, reportPath " +
                "from caseReport where userID = ? order by submitTime desc";
        defValue.put("type", HistoryReport.CASE | HistoryReport.APPROVED);
        reports.addAll(server.jdbcTemplate.query(sqlCase, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));

        String sqlLeader = "select reportID,category, reportText, submitTime, singleScore, scoreType, reportPath " +
                "from leaderReport where userID = ? order by submitTime desc";
        defValue.put("type", HistoryReport.LEADER);
        reports.addAll(server.jdbcTemplate.query(sqlLeader, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));

        Collections.sort(reports, new Comparator<HistoryReport>() {
            public int compare(HistoryReport o1, HistoryReport o2) {
                return o2.getSubmitTime().compareTo(o1.getSubmitTime());
            }
        });
        model.addAttribute("UserID", UserId);
        model.addAttribute("UserName", UserName);
        model.addAttribute("list", reports);
        model.addAttribute("selected_type", 1); //进入页面时我的提交(已审批)
        return "HistoryReport";
    }

    //添加查询与统计
    @PostMapping(value = "/HistoryReport") //case: Username,type general: case,//scoretype,singlescore,scoretype
    public String HistoryReportPost(@RequestParam("button") String type,
                                    /*@RequestParam("search") String search,*/
                                    @RequestParam("UserID") String UserId,
                                    @RequestParam("UserName") String UserName,
                                    Model model) {
        logger.info("Post HistoryReport: " + UserId); //log

        Map<String, Object> defValue = new HashMap<String, Object>();
        defValue.put("userName", UserName);
        defValue.put("userID", UserId);
        int selectType = 0;
        List<HistoryReport> reports = new ArrayList<HistoryReport>();

        if (type.equals("我的提交(未审批)")) {
            String sqlGen = "select reportID,leaderName,category,reportText,submitTime,reportPath " +
                    "from undealedGeneralReport where userID =? order by submitTime desc";
            Object args[] = new Object[]{UserId};
            defValue.put("type", HistoryReport.GENERAL);
            reports = server.jdbcTemplate.query(sqlGen, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));
            /* caseReport
            * */
            String sqlCase = "select reportID, leaderName, category, reportText, submitTime, singleScore, scoreType, members, reportPath " +
                    "from undealedCaseReport where userID = ? order by submitTime desc";
            defValue.put("type", HistoryReport.CASE);
            reports.addAll(server.jdbcTemplate.query(sqlCase, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            selectType = 2;

        } else if (type.equals("我的提交(已审批)")) {
             /* generalReport
            * */
            String sqlGen = "select reportID,leaderName,category,reportText,submitTime,checkTime,isPass,comment,reportPath " +
                    "from generalReport where userID =? order by submitTime desc";
            Object args[] = new Object[]{UserId};
            defValue.put("type", HistoryReport.GENERAL | HistoryReport.APPROVED);
            reports = server.jdbcTemplate.query(sqlGen, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));

            /* caseReport
            * */
            String sqlCase = "select reportID, leaderName, category, reportText, submitTime, checkTime, isPass, " +
                    "comment, singleScore, scoreType, members, reportPath " +
                    "from caseReport where userID = ? order by submitTime desc";
            defValue.put("type", HistoryReport.CASE | HistoryReport.APPROVED);
            reports.addAll(server.jdbcTemplate.query(sqlCase, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            /* leaderReport
            * */
            String sqlLeader = "select reportID,category, reportText, submitTime, singleScore, scoreType, reportPath " +
                    "from leaderReport where userID = ? order by submitTime desc";
            defValue.put("type", HistoryReport.LEADER);
            reports.addAll(server.jdbcTemplate.query(sqlLeader, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            selectType = 1;
        } else if (type.equals("我的审批")) {
            defValue.put("leaderName", UserName);

            String sqlGen = "select reportID, userID, userName, category,reportText,submitTime,checkTime,isPass,comment,reportPath " +
                    "from generalReport natural join user where leaderName = ? order by submitTime desc";
            Object args[] = new Object[]{UserName};
            defValue.put("type", HistoryReport.GENERAL | HistoryReport.APPROVED);
            reports = server.jdbcTemplate.query(sqlGen, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));

            String sqlCase = "select reportID, userID, userName, category, reportText, submitTime,checkTime, isPass, comment, " +
                    "singleScore, scoreType,members, reportPath " +
                    "from caseReport natural join user where leaderName = ? order by submitTime desc";
            defValue.put("type", HistoryReport.CASE | HistoryReport.APPROVED);
            reports.addAll(server.jdbcTemplate.query(sqlCase, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            selectType = 3;
        }
        if (selectType == 0) {
            model.addAttribute("errorNum", "01");
            return "failure";
        }

        Collections.sort(reports, new Comparator<HistoryReport>() {
            public int compare(HistoryReport o1, HistoryReport o2) {
                return o2.getSubmitTime().compareTo(o1.getSubmitTime());
            }
        });

        model.addAttribute("UserID", UserId);
        model.addAttribute("UserName", UserName);
        model.addAttribute("list", reports);
        model.addAttribute("selected_type", selectType);
        return "HistoryReport";
    }

    @RequestMapping("/Synchronizer")
    @ResponseBody
    public String Synchronizer() throws Exception {
        logger.info("Synchronization starts!"); //log
        server.syncUser(server.syncDepartment());
        logger.info("Synchronization succeed!"); //log
        return "Synchronization succeed!";
    }

    @RequestMapping("/Refresh")
    @ResponseBody
    public String Refresh() throws Exception {
        logger.info("Refresh starts!"); //log
        server.reported.clear();
        logger.info("Refresh succeed!"); //log
        return "Refresh succeed!";
    }

    @RequestMapping("/QRCode")
    public String QRCode(@RequestParam("code") String CODE,
                         @RequestParam("state") String REFRESH,
                         Model model) {
        String userID = server.getUserId(CODE, submitSecret);
        logger.info("Request QRCode: " + userID); //log
        if (!server.isUser(userID)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        QRCode qrCode = server.getQRCode(userID);
        if (qrCode == null) {
            model.addAttribute("errorNum", "03");
            return "failure";
        }
        /*
        long timestamp = System.currentTimeMillis();
        if (!checkins.containsKey(UserId)) {
            checkins.put(UserId, new Checkin(timestamp));
        } else {
            Checkin checkin = checkins.get(UserId);
            if (REFRESH.equals("true") || timestamp - checkin.getTimestamp() > QRTimeout) {
                checkin.clear();
                checkin.setTimestamp(timestamp);
            }
            timestamp = checkin.getTimestamp();
        }
        */
        model.addAttribute("qrid", qrCode.QRID);
        model.addAttribute("creator", userID);
        return "QRCode";
    }

    @RequestMapping("/RedirectQR")
    public String RedirectQR(@RequestParam("qrid") String QRID,
                             @RequestParam("creator") String creator,
                             Model model) {
        logger.info("Request redirectQR: " + QRID); //log

        model.addAttribute("state", QRID + "-" + creator);
        return "RedirectQR";
    }

    @RequestMapping("/Checkin")
    public String Checkin(@RequestParam("code") String CODE,
                          @RequestParam("state") String STATE,
                          Model model) {
        String userID = server.getUserId(CODE, submitSecret);
        logger.info("Request checkin: " + userID + " - " + STATE); //log
        if (!server.isUser(userID)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        int p = STATE.indexOf("-");
        int QRID = Integer.parseInt(STATE.substring(0, p));
        String creator = STATE.substring(p + 1);
        QRCode qrCode = server.QRCodes.get(QRID);
        String time = server.currentTime();

        if (qrCode == null || qrCode.flag.length() > 0
                || qrCode.s_time.compareTo(time) > 0 || time.compareTo(qrCode.e_time) > 0
                || !qrCode.QREntry.contains(creator)) {
            model.addAttribute("errorNum", "01");
            return "failure";
        }


        if (qrCode.checkins.contains(userID)) {
            model.addAttribute("errorNum", "02");
            return "failure";// avoid scanning QRcode repeatly
        }
        if (server.award(userID, qrCode.value) > 0) qrCode.checkins.add(userID);
        else {
            model.addAttribute("errorNum", "-1");
            return "failure";
        }
        //timestamp = Long.parseLong(STATE.substring(p + 1));

       /*
        int p = STATE.indexOf("-");
        String creator = STATE.substring(0, p);
        long timestamp = Long.parseLong(STATE.substring(p + 1));
        if (!checkins.containsKey(creator) || checkins.get(creator).getTimestamp() != timestamp)  // invalid QRcode
        {
            model.addAttribute("errorNum", "01");
            return "failure";
        }
        if (System.currentTimeMillis() - timestamp > QRTimeout)  // invalid QRcode
        {
            model.addAttribute("errorNum", "01");
            return "failure";
        }
        Checkin checkin = checkins.get(creator);
        if (checkin.getUsers().contains(userID)) {
            model.addAttribute("errorNum", "02");
            return "failure";// avoid scanning QRcode repeatly
        }*/

        return "success";
    }

    @RequestMapping("/UploadAvatar")
    public String UploadAvatar(@RequestParam("code") String CODE,
                               @RequestParam("state") String STATE,
                               Model model) {
        String userID = server.getUserId(CODE, submitSecret);
        logger.info("Request UploadAvatar: " + userID); //log
        if (!server.isUser(userID)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        String sql = "select avatarURL from user where userID=? limit 1";
        Map<String, Object> result = server.jdbcTemplate.queryForMap(sql, userID);
        String avatarURL = (result == null) ? "" : result.get("avatarURL").toString();
        model.addAttribute("userID", userID);
        model.addAttribute("avatarURL", avatarURL);
        return "UploadAvatar";
    }

    @PostMapping("/UploadAvatar")
    @ResponseBody
    public ResponseMsg UploadAvatar(@RequestParam("userID") String userID,
                                    @RequestParam("x") String x,
                                    @RequestParam("y") String y,
                                    @RequestParam("w") String w,
                                    @RequestParam("h") String h,
                                    @RequestParam("srcURL") String srcURL,
                                    @RequestParam("file") MultipartFile file) {
        logger.info("Post UploadAvatar: " + userID); //log

        String filename = file.getOriginalFilename();
        String suffix = "png";
        String avatarURL = userID + "/" + userID + "." + suffix;
        String avatarURLSub = userID + "/" + userID + "sub." + suffix;
        String srcURLFile = srcURL.substring(srcURL.indexOf(",") + 1);

        server.base64ToImg(srcURLFile, avatarURL);


        if (server.imgSub(avatarURL, avatarURLSub, suffix, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(w), Integer.parseInt(h))) {
            String sql = "update user set avatarURL=? where userID=?";
            server.jdbcTemplate.update(sql, avatarURLSub, userID);
            return new ResponseMsg("1", avatarURLSub);
        }
        return new ResponseMsg("0", srcURL);
    }


    /*
    @RequestMapping("/Reservation")
    public String Reservation(@RequestParam("code") String CODE,
                              @RequestParam("state") String STATE,
                              Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        if (server.isUser(UserId) == false)
            return "failure";
        String UserName = server.getUserName(UserId);
        model.addAttribute("UserID", UserId);
        model.addAttribute("UserName", UserName);
        return "Reservation";
    }

    @PostMapping(value = "/Reservation")
    public String Reservation(@RequestParam("type") String type,
                              @RequestParam("members") String members,
                              @RequestParam("suits") String suits,
                              @RequestParam("UserID") String userID) {

        //type = 0 午餐
        //type = 1 晚餐


        String time = server.currentTime();
        String sql = "'"+userID+"',"+members+","+suits+",'"+time+"',"+type;
        server.jdbcTemplate.update("insert into reservation " +
                "(userID,members,suits,time,type) values(" + sql + ")");
        return "success";
    }


    @RequestMapping("/checkOrder")
    public String checkOder(Model model) {
        int lunchmembers;
        int lunchsuits;
        int dinnermembers;
        int dinnersuits;
        return "checkOrder";
    }

    */
}
