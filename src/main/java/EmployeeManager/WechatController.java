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

@Controller
@RequestMapping("/wechat")
public class WechatController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Server server;


    @GetMapping("/GeneralReport")
    public String GeneralReport(@RequestParam("state") String STATE,
                                @RequestParam("code") String code,
                                // @RequestParam(value = "password", required = false) String password,
                                Model model) {
        String UserId = server.getUserId(code, Variable.Secret);
        logger.info("Request GeneralReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }
        List<ReportType> typelist = server.getReportType();
        // Department-Leader-LeaderID
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);
        String userName = server.getUserName(UserId);

        model.addAttribute("userName", userName);
        model.addAttribute("UserId", UserId);
        model.addAttribute("list", DLeaders);
        model.addAttribute("typelist", typelist);
        model.addAttribute("minReportWordCount", Variable.minReportWordCount);
        return "templates/GeneralReport";
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
        int count = Variable.generalCount.getOrDefault(UserId, 0);
        if (count >= Variable.maxGeneralReportCount) {
            return new ResponseMsg("0", "超过每天提交次数上限！请明天再提交。");
        }

        String currentTime = Util.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            return new ResponseMsg("0", "文件上传失败！");
        }
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";

        Map<String, Object> generalReport = new HashMap<>();

        generalReport.put("userID", UserId);
        generalReport.put("leaderName", leader);
        generalReport.put("category", type);
        generalReport.put("reportText", content);
        generalReport.put("reportPath", pathCurrent);
        generalReport.put("submitTime", currentTime);
        generalReport.put("singleScore", server.getTypeValue(type));

        try {
            server.insertMap(generalReport, "undealedGeneralReport");
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseMsg("0", "一般报告提交失败！");
        }

        Variable.generalCount.put(UserId, ++count);
        try {
            server.sendMessage(leader, Variable.mesgToLeader, true, Variable.AgentID);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return new ResponseMsg("1", "一般报告提交成功！");
    }

    @GetMapping("/CaseReport")
    public String CaseReport(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {
        String UserId = server.getUserId(CODE, Variable.Secret);
        logger.info("Request CaseReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }
        int userPrivilege = server.getUserPrivilege(UserId);
        if (userPrivilege < server.getIntSysVar("caseReportEntryLimit")) {
            model.addAttribute("errorNum", "04");
            return "templates/failure";
        }

        List<ReportType> reportTypes = server.getReportType();
        String userName = server.getUserName(UserId);
        List<String> AllUsers = server.getAllUsers();
        // Department-Leader-LeaderID
        List<String> DLeaders = server.getLeaderInCase(UserId);

        model.addAttribute("UserId", UserId);
        model.addAttribute("userName", userName);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("list", DLeaders);
        model.addAttribute("reportTypes", reportTypes);
        model.addAttribute("minReportWordCount", Variable.minReportWordCount);
        return "templates/CaseReport";
    }

    @PostMapping(value = "/CaseReport")
    @ResponseBody
    public ResponseMsg CaseReportPost(@RequestParam("members") String members,
                                      @RequestParam("UserId") String UserId,
                                      @RequestParam("content") String content,
                                      @RequestParam("type") String type,
                                      @RequestParam("score_type") int scoreType,
                                      @RequestParam("score") int score,
                                      @RequestParam("leader") String leader,
                                      @RequestParam("file") MultipartFile file,
                                      Model model) {
        logger.info("Post CaseReport: " + UserId); //log

        String currentTime = Util.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent))
            return new ResponseMsg("0", "文件上传失败！");
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";

        Map<String, Object> caseReport = new HashMap<>();
        caseReport.put("userID", UserId);
        caseReport.put("leaderName", leader);
        caseReport.put("members", members);
        caseReport.put("category", type);
        caseReport.put("reportText", content);
        caseReport.put("reportPath", pathCurrent);
        caseReport.put("submitTime", currentTime);
        caseReport.put("singleScore", score);
        caseReport.put("scoreType", scoreType);

        try {
            server.insertMap(caseReport, "undealedCaseReport");
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseMsg("0", "个案报告提交失败！");
        }

        try {
            server.sendMessage(leader, Variable.mesgToLeader, true, Variable.AgentID);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return new ResponseMsg("1", "个案报告提交成功！");
    }

    @GetMapping("/LeaderReport")
    public String LeaderReport(@RequestParam("code") String CODE,
                               @RequestParam("state") String STATE,
                               Model model) {
        String UserId = server.getUserId(CODE, Variable.Secret);
        logger.info("Request LeaderReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }
        int userPrivilege = server.getUserPrivilege(UserId);
        if (userPrivilege < server.getIntSysVar("leaderReportEntryLimit")) {
            model.addAttribute("errorNum", "04");
            return "templates/failure";
        }

        List<ReportType> reportType = server.getReportType();
        String userName = server.getUserName(UserId);
        int scoreLimit = server.getLeaderScoreLimit(userPrivilege);
        List<String> AllUsers = server.getAllDepartmentUsers(UserId, userPrivilege);
        model.addAttribute("UserId", UserId);
        model.addAttribute("userName", userName);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("scoreLimit", scoreLimit);
        model.addAttribute("reportType", reportType);
        model.addAttribute("minReportWordCount", Variable.minReportWordCount);
        return "templates/LeaderReport";
    }

    @PostMapping(value = "/LeaderReport")
    @ResponseBody
    public ResponseMsg LeaderPost(@RequestParam("members") String members,
                                  @RequestParam("UserId") String UserId,
                                  @RequestParam("content") String content,
                                  @RequestParam("type") String type,
                                  @RequestParam("score_type") int scoreType,
                                  @RequestParam("score") int score,
                                  @RequestParam("file") MultipartFile file,
                                  Model model) {
        logger.info("Post LeaderReport: " + UserId); //log

        if (!Variable.leaderCount.containsKey(UserId))
            Variable.leaderCount.put(UserId, 0);
        int count = Variable.leaderCount.get(UserId);
        if (count >= server.getLeaderPostLimit(UserId)) {
            return new ResponseMsg("0", "超过每天提交次数上限！请明天再提交。");
        }
        Variable.leaderCount.put(UserId, ++count);

        String currentTime = Util.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            return new ResponseMsg("0", "文件上传失败！");
        }
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";

        Map<String, Object> caseReport = new HashMap<>();
        caseReport.put("userID", UserId);
        caseReport.put("members", members);
        caseReport.put("category", type);
        caseReport.put("reportText", content);
        caseReport.put("reportPath", pathCurrent);
        caseReport.put("singleScore", score);
        caseReport.put("scoreType", scoreType);
        caseReport.put("submitTime", currentTime);

        try {
            server.insertMap(caseReport, "leaderReport");
            if (scoreType == 0) score = -score;
            server.award(server.name2id(members, ","), score);
        } catch (Exception e) {
            return new ResponseMsg("0", "领导批办提交失败！");
        }

        return new ResponseMsg("1", "领导批办提交成功！");
    }

    //slected_type 为设置button点击后颜色准备
    @GetMapping("/RankingList")
    public String RankingList(@RequestParam("state") String STATE,
                              Model model) {
        logger.info("Request RankingList"); //log


        //"3"为领导干部
        String sql = "select userName,s_score+f_score as s_score,avatarURL,duty,title from user where position=1 order by s_score desc";

        List<User> users = server.jdbcTemplate.query(sql, new Mapper<>(User.class));
        model.addAttribute("list", users);
        model.addAttribute("selected_type", 4);

        if (STATE.equals("PC")) return "templates/RankingListPC";
        return "templates/RankingList";
    }

    @PostMapping(value = "/RankingList")
    public String RankingListPost(@RequestParam("button") String type,
                                  @RequestParam("state") String STATE,
                                  @RequestParam("userInput") String userInput,
                                  Model model) {
        logger.info("Post RankingList: " + type); //log

        String sql = "select userName,s_score+f_score as s_score,avatarURL,duty,title from user order by s_score desc";
        if (!userInput.equals("")) {
            List<User> users = server.jdbcTemplate.query(sql, new Mapper<User>((User.class)));
            List<User> usersSearch = new ArrayList<>();
            String target = "";
            for (User user : users) {
                target = user.getDuty() + user.getUsername() + user.getScore() + user.getTitle();
                if (target.contains(userInput))
                    usersSearch.add(user);
            }
            model.addAttribute("list", usersSearch);
            model.addAttribute("userInput", userInput);
            return "templates/RankingList";
        }
        if (type.equals("总排行")) {
            List<User> users = server.jdbcTemplate.query(sql, new Mapper<User>((User.class)));
            model.addAttribute("selected_type", 3);
            model.addAttribute("list", users);
            if (STATE.equals("PC")) return "templates/RankingListPC";
            return "templates/RankingList";
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
            sql = "select userName,s_score+f_score as s_score,avatarURL,duty,title from user  where position=" + selectedType + " order by s_score desc";
            List<User> users = server.jdbcTemplate.query(sql, new Mapper<User>(User.class));
            model.addAttribute("list", users);
            if (STATE.equals("PC")) return "templates/RankingListPC";
            return "templates/RankingList";
        }
    }

    @GetMapping("/ReportApproval")
    public String ReportApproval(@RequestParam("code") String CODE,
                                 @RequestParam("state") String STATE,
                                 Model model) {
        String UserId = server.getUserId(CODE, Variable.Secret);
        logger.info("Request ReportApproval: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }

        String sqlGen = "select reportID,userID,category,singleScore,reportText,submitTime,userName,reportPath " +
                "from undealedGeneralReport natural join user " +
                "where leaderName=(select userName from user where userID=?)";

        String sqlCase = "select reportID,userID,category,reportText,submitTime,members,singleScore,userName,reportPath " +
                "from undealedCaseReport natural join user " +
                "where leaderName=(select userName from user where userID=?)";
        List<GeneralReport> listGen = server.jdbcTemplate.query(sqlGen, new Object[]{UserId}, new Mapper<GeneralReport>(GeneralReport.class));
        List<CaseReport> listCase = server.jdbcTemplate.query(sqlCase, new Object[]{UserId}, new Mapper<CaseReport>(CaseReport.class));
        model.addAttribute("list1", listGen);
        model.addAttribute("list2", listCase);
        return "templates/ReportApproval";
    }

    @PostMapping("/ReportApproval")
    @ResponseBody
    public ReportApprovalAjax ReportApprovalPost(@RequestBody ReportApprovalAjax ajax,
                                                 Model model) {
        String check1 = ajax.getCheck1(); //Gen
        String check2 = ajax.getCheck2(); //Case
        String[] reports1 = check1.split(",");
        String[] reports2 = check2.split(",");
        String reportStatus = ajax.getReportStatus();
        String reportComment = ajax.getReportComment();

        String updateSql = "";
        String checkTime = Util.currentTime();

        logger.info("check1: '" + check1 + "'");
        logger.info("check2: '" + check2 + "'");

        if (!check1.isEmpty()) {
            for (int i = 0; i < reports1.length; i++) {
                Map<String, Object> generalReport = server.getUndealedGeneralReport(reports1[i]);
                if (generalReport == null) continue;
                String userID = generalReport.get("userID").toString();

                generalReport.put("checkTime", checkTime);
                generalReport.put("isPass", Integer.valueOf(reportStatus));
                generalReport.put("comment", reportComment);

                int singleScore = (int) generalReport.get("singleScore");
                try {
                    server.award(userID, singleScore);
                    server.insertMap(generalReport, "generalReport");

                    updateSql = "delete from undealedGeneralReport where reportID=" + reports1[i];
                    server.jdbcTemplate.update(updateSql);
                    ajax.setCheckResponse("审批成功!");
                    ajax.setCheckNum("1");
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    ajax.setCheckResponse("审批失败!");
                    ajax.setCheckNum("0");
                    return ajax;
                }

                try {
                    server.sendMessage(userID, Variable.mesgToSubordinate, false, Variable.AgentID);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }
        if (!check2.isEmpty()) {
            for (int i = 0; i < reports2.length; i++) {
                Map<String, Object> caseReport = server.getUndealedCaseReport(reports2[i]);
                if (caseReport == null) continue;

                String userID = caseReport.get("userID").toString();

                int singleScore = 0;
                if (reportStatus.equals("1")) {
                    if ((Boolean) caseReport.get("scoreType")) {
                        singleScore = (int) caseReport.get("singleScore");
                    } else
                        singleScore = -(int) caseReport.get("singleScore");
                }

                caseReport.put("checkTime", checkTime);
                caseReport.put("isPass", Integer.valueOf(reportStatus));
                caseReport.put("comment", reportComment);
                try {
                    server.award(userID + "," + server.name2id(caseReport.get("members").toString(), ","), singleScore);
                    server.insertMap(caseReport, "caseReport");

                    updateSql = "delete from undealedCaseReport where reportID=" + reports2[i];
                    server.jdbcTemplate.update(updateSql);
                    ajax.setCheckResponse("审批成功!");
                    ajax.setCheckNum("1");
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    ajax.setCheckResponse("审批失败!");
                    ajax.setCheckNum("0");
                    return ajax;
                }

                try {
                    server.sendMessage(userID, Variable.mesgToSubordinate, false, Variable.AgentID);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return ajax;
    }

    @GetMapping("/HistoryReport")
    public String HistoryReport(@RequestParam("code") String CODE,
                                @RequestParam("state") String STATE,
                                Model model) {
        String UserId = server.getUserId(CODE, Variable.Secret);
        logger.info("Request HistoryReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }

        String UserName = server.getUserName(UserId);
        Map<String, Object> defValue = new HashMap<String, Object>();
        defValue.put("userName", UserName);
        defValue.put("userID", UserId);

        String sqlGen = "select reportID,leaderName,category,singleScore,reportText,submitTime,checkTime,isPass,comment,reportPath " +
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
        return "templates/HistoryReport";
    }

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
            String sqlGen = "select reportID,leaderName,category,reportText,submitTime,singleScore,reportPath " +
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
            String sqlGen = "select reportID,leaderName,category,singleScore,reportText,submitTime,checkTime,isPass,comment,reportPath " +
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
            String sqlLeader = "select reportID,category, reportText, submitTime, singleScore, scoreType, members, reportPath " +
                    "from leaderReport where userID = ? order by submitTime desc";
            defValue.put("type", HistoryReport.LEADER);
            reports.addAll(server.jdbcTemplate.query(sqlLeader, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            selectType = 1;
        } else if (type.equals("我的审批")) {
            defValue.put("leaderName", UserName);

            String sqlGen = "select reportID, userID, userName, category, singleScore,reportText,submitTime,checkTime,isPass,comment,reportPath " +
                    "from generalReport natural join user  where leaderName = ? order by submitTime desc";
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
            return "templates/failure";
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
        return "templates/HistoryReport";
    }

    @GetMapping("/QRCode")
    public String QRCode(@RequestParam("code") String CODE,
                         @RequestParam("state") String REFRESH,
                         Model model) {
        String userID = server.getUserId(CODE, Variable.Secret);
        logger.info("Request QRCode: " + userID); //log
        if (!server.isUser(userID)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }

        QRCode qrCode = server.getQRCode(userID);
        if (qrCode == null) {
            model.addAttribute("errorNum", "03");
            return "templates/failure";
        }

        model.addAttribute("qrid", qrCode.QRID);
        model.addAttribute("token", qrCode.token);
        model.addAttribute("manager", userID);
        return "templates/QRCode";
    }

    @GetMapping("/RedirectQR")
    public String RedirectQR(@RequestParam("qrid") String QRID,
                             @RequestParam("token") String token,
                             @RequestParam("manager") String manager,
                             @RequestParam("manalatlng") String manalatlng,
                             Model model) {
        logger.info("Request redirectQR: " + QRID); //log

        model.addAttribute("state", QRID + "-" + token);
        model.addAttribute("manager", manager);
        model.addAttribute("manalatlng", manalatlng);
        return "templates/RedirectQR";
    }

    @RequestMapping("/Checkin")
    public String Checkin(@RequestParam("code") String CODE,
                          @RequestParam("state") String STATE,
                          Model model) {
        String userID = server.getUserId(CODE, Variable.Secret);
        logger.info("Request checkin: " + userID + " / " + STATE); //log
        if (!server.isUser(userID)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }

        String[] args = STATE.split("-");
        String manager = args[2];
        double lat1 = Double.parseDouble(args[3]);
        double lng1 = Double.parseDouble(args[4]);
        double lat2 = Double.parseDouble(args[5]);
        double lng2 = Double.parseDouble(args[6]);

        int QRID = Integer.parseInt(args[0]), token = Integer.parseInt(args[1]);
        QRCode qrCode = server.getQRCode(QRID);
        String time = Util.currentTime();

        if (qrCode == null || qrCode.token != token || qrCode.s_time.compareTo(time) > 0 || time.compareTo(qrCode.e_time) > 0) {
            model.addAttribute("errorNum", "01");
            return "templates/failure";
        }

        if (Util.getDistance(lat1, lng1, lat2, lng2) > Variable.QRCodeDistLimitKM) {
            model.addAttribute("errorNum", "07");
            return "templates/failure";
        }

        Map<String, Object> qrCheckin = new TreeMap<>();
        qrCheckin.put("QRID", QRID);
        qrCheckin.put("userID", userID);
        qrCheckin.put("checkTime", Util.currentTime());
        qrCheckin.put("manager", manager);
        qrCheckin.put("userLat", lat1);
        qrCheckin.put("userLng", lng1);
        qrCheckin.put("manaLat", lat2);
        qrCheckin.put("manaLng", lng2);
        try {
            server.insertMap(qrCheckin, "QRCheckin");
        } catch (Exception e) {
            model.addAttribute("errorNum", "02");
            return "templates/failure"; // avoid repeatedly scanning QR code
        }
        try {
            server.award(userID, qrCode.value);
        } catch (Exception e) {
            model.addAttribute("errorNum", "-1");
            return "templates/failure";
        }

        return "templates/success";
    }

    @GetMapping("/UploadAvatar")
    public String UploadAvatar(@RequestParam("code") String CODE,
                               @RequestParam("state") String STATE,
                               Model model) {
        String userID = server.getUserId(CODE, Variable.Secret);
        logger.info("Request UploadAvatar: " + userID); //log
        if (!server.isUser(userID)) {
            model.addAttribute("errorNum", "00");
            return "templates/failure";
        }
        Random ranparam = new Random();

        String sql = "select avatarURL from user where userID=? limit 1";
        Map<String, Object> result = server.jdbcTemplate.queryForMap(sql, userID);
        String avatarURL = (result.size() == 0) ? "" : result.get("avatarURL").toString();
        if (avatarURL.length() > 4 && !avatarURL.substring(0, 4).equals("http"))
            avatarURL = "/" + avatarURL;

        model.addAttribute("userID", userID);
        model.addAttribute("avatarURL", avatarURL + "?" + ranparam);
        return "templates/UploadAvatar";
    }

    @PostMapping("/UploadAvatar")
    @ResponseBody
    public ResponseMsg UploadAvatar(@RequestParam("userID") String userID,
                                    @RequestParam("choose") String choose,
                                    @RequestParam("x") String x,
                                    @RequestParam("y") String y,
                                    @RequestParam("w") String w,
                                    @RequestParam("h") String h,
                                    @RequestParam("srcURL") String srcURL
    ) {
        logger.info("Post UploadAvatar: " + userID); //log
        String suffix = "jpeg";
        String avatarURL = userID + "/" + userID + "." + suffix;
        String avatarURLSub = userID + "/" + userID + "Sub" + Util.getTimestamp() + "." + suffix;
        String srcURLFile = srcURL.substring(srcURL.indexOf(",") + 1);

        server.mkDir(userID);
        if (server.base64ToImg(srcURLFile, avatarURL)) {
            if (server.imgSub(avatarURL, avatarURLSub, suffix, Integer.parseInt(x), Integer.parseInt(y), 640, 640)) {
                String sql = "update user set avatarURL=? where userID=?";
                server.jdbcTemplate.update(sql, avatarURLSub, userID);
                return new ResponseMsg("1", "/" + avatarURLSub + "?seed=1");
            }
        }
        return new ResponseMsg("0", srcURL);
    }
}
