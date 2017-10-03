package EmployeeManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static EmployeeManager.Server.*;

@Controller
public class BackgroundController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Server server;
    Map<String, Checkin> checkins = new HashMap<String, Checkin>();
    Set<String> reported = new HashSet<String>();
    int QRTimeout = 30 * 60 * 1000;
    @Value("${web.upload-path}")
    private String path;

    @RequestMapping("/GeneralReport")
    public String GeneralReport(@RequestParam("state") String STATE,
                                @RequestParam("code") String code,
                                Model model) {
        String UserId = server.getUserId(code, submitSecret);
        logger.info("Request GeneralReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum","00");
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
    public String GeneralReportPost(@RequestParam("content") String content,
                                    @RequestParam("type") String type,
                                    @RequestParam("leader") String leader,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("UserId") String UserId,
                                    Model model) {
        logger.info("Post GeneralReport: " + UserId); //log
        if (reported.contains(UserId)) {
            model.addAttribute("errorNum", "05");
            return "failure";
        }

        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            model.addAttribute("errorNum", "02");
            return "failure";
        }
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";
        String sqlMessage = "'" + UserId + "','" + leader + "'," + type + ",'" + content + "','" + pathCurrent + "','" +
                currentTime + "'";
        server.jdbcTemplate.update("insert into undealedGeneralReport " +
                "(userID,leaderName,category,reportText,reportPath,submitTime) values(" + sqlMessage + ")");

        reported.add(UserId);
        try {
            server.sendMessage(leader, "您有一份新报告需要审批，可进入 报告审批 查看。", true, approvalAgentID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("successNum", "00");
        return "success";
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
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);

        model.addAttribute("UserId", UserId);
        model.addAttribute("userName", userName);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("list", DLeaders);
        return "CaseReport";
    }

    @PostMapping(value = "/CaseReport")
    public String CaseReportPost(@RequestParam("members") String members,
                                 @RequestParam("UserId") String UserId,
                                 @RequestParam("content") String content,
                                 @RequestParam("type") String type,
                                 @RequestParam("score_type") String score_type,
                                 @RequestParam("score") String score,
                                 @RequestParam("leader") String leader,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {
        logger.info("Post CaseReport: " + UserId); //log

        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent))
            return "failure";
        if (file.getOriginalFilename().equals(""))
            pathCurrent = "";
        String sqlMessage = "'" + UserId + "','" + leader + "','" + members + "'," + type + ",'" + content + "','" +
                pathCurrent + "'," + score + ",'" + currentTime + "'," + score_type;
        server.jdbcTemplate.update("insert into undealedCaseReport " +
                "(userID,leaderName,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");

        try {
            server.sendMessage(leader, "您有一份新报告需要审批，可进入 报告审批 查看。", true, approvalAgentID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("successNum", "01");
        return "success";
    }

    @RequestMapping("/LeadershipReport")
    public String Leadership(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request LeadershipReport: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum","00");
            return "failure";
        }
        String userName = server.getUserName(UserId);
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("UserId", UserId);
        model.addAttribute("userName", userName);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("list", DLeaders);
        return "LeadershipReport";
    }

    @PostMapping(value = "/LeadershipReport")
    public String LeadershipPost(@RequestParam("members") String members,
                                 @RequestParam("UserId") String UserId,
                                 @RequestParam("content") String content,
                                 @RequestParam("type") String type,
                                 @RequestParam("score_type") String score_type,
                                 @RequestParam("score") String score,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {
        logger.info("Post LeadershipReport: " + UserId); //log

        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (!server.saveFile(file, pathCurrent)) {
            model.addAttribute("errorNum","02");
            return "failure";
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

        model.addAttribute("successNum", "02");
        return "success";
    }


    //slected_type 为设置button点击后颜色准备
    @RequestMapping("/RankingList")
    public String RankingList(@RequestParam("code") String CODE,
                              @RequestParam("state") String STATE,
                              Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request RankingList: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum", "00");
            return "failure";
        }

        String sql = "select userName,s_score,avatarURL from user order by s_score desc";
        List<Map<String, Object>> list;
        try {
            list = server.jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            return e.getMessage();
        }
        List<User> users = new ArrayList<User>();
        int rank = 1;
        for (Map<String, Object> map : list) {
            User user_temp = new User(map.get("userName"), map.get("s_score"), rank++, map.get("avatarURL"));
            users.add(user_temp);
        }
        model.addAttribute("list", users);
        model.addAttribute("selected_type", 3);

        return "RankingList";
    }

    //积分排行大致如下，需要增加显示更多信息（照片等）
    @PostMapping(value = "/RankingList")
    public String RankingListPost(@RequestParam("button") String type,
                                  Model model) {
        logger.info("Post RankingList: " + type); //log

        if (type.equals("总排行")) {
            String sql = "select userName,s_score,avatarURL from user order by s_score desc";
            List<Map<String, Object>> list;
            try {
                list = server.jdbcTemplate.queryForList(sql);
            } catch (Exception e) {
                return e.getMessage();
            }
            List<User> users = new ArrayList<User>();
            int rank = 1;
            for (Map<String, Object> map : list) {
                User user_temp = new User(map.get("userName"), map.get("s_score"), rank++, map.get("avatarURL"));
                users.add(user_temp);
            }
            model.addAttribute("selected_type", 3);
            model.addAttribute("list", users);
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
            String sql = "select userName,s_score,avatarURL from user where position=" + selectedType + " order by s_score desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sql);
            } catch (Exception e) {
                return e.getMessage();
            }
            List<User> users = new ArrayList<User>();
            int rank = 1;
            for (Map<String, Object> map : list) {
                User user_temp = new User(map.get("userName"), map.get("s_score"), rank++, map.get("avatarURL"));
                users.add(user_temp);
            }
            model.addAttribute("list", users);
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

        String getGeneralReport = "select reportID,userID,category,reportText,submitTime,userName,reportPath " +
                "from undealedGeneralReport natural join user where leaderName=(select userName from user " +
                "where userID=?)";

        String getCaseReport = "select reportID,userID,category,reportText,submitTime,members,singleScore,userName,reportPath " +
                "from undealedCaseReport natural join user " +
                "where leaderName=(select userName from user where userID=?)";

        Object args[] = new Object[]{UserId};
        List<Map<String, Object>> generalReport;
        List<Map<String, Object>> caseReport;
        try {
            generalReport = server.jdbcTemplate.queryForList(getGeneralReport, args);
        } catch (Exception e) {
            return e.getMessage();
        }
        try {
            caseReport = server.jdbcTemplate.queryForList(getCaseReport, args);
        } catch (Exception e) {
            return e.getMessage();
        }
        List<GeneralReport> list1 = new ArrayList<GeneralReport>();
        List<CaseReport> list2 = new ArrayList<CaseReport>();
        for (Map<String, Object> map : generalReport) {
            String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
            GeneralReport list1_temp = new GeneralReport(map.get("reportID"), map.get("userID"), map.get("userName"),
                    map.get("category"), map.get("reportText"), map.get("submitTime"), pathtmp);
            list1.add(list1_temp);
        }
        for (Map<String, Object> map : caseReport) {
            String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
            CaseReport list2_temp = new CaseReport(map.get("reportID"), map.get("userID"), map.get("userName"),
                    map.get("category"), map.get("reportText"), map.get("submitTime"), map.get("members"),
                    map.get("singleScore"), pathtmp);
            list2.add(list2_temp);
        }
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);
        return "ReportApproval";
    }

    @PostMapping("/ReportApproval")
    @ResponseBody
    public ReportApprovalAjax ReportApprovalPost(
            @RequestBody ReportApprovalAjax ajax,
                Model model) {
        String check1 = ajax.getCheck1();
        String check2 = ajax.getCheck2();
        logger.info("post ReportApproval: check1-:" + check1 + "; check2-" + check2); //log

        String[] reports1 = check1.split(",");
        String[] reports2 = check2.split(",");
        String reportStatus=ajax.getReportStatus();
        String reportComment = ajax.getReportComment();

        String updateSql = "";
        String checkTime = server.currentTime();

        logger.info("'"+check1+"'");
        logger.info("'"+check2+"'");

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
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ajax.setCheckResponse("一般报告审批失败!");
                }


                try {
                    server.sendMessage(generalReport.get("userID").toString(), "您有一份报告已被审批，可进入 我的报告 查看。", false, reportAgentID);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (!check2.isEmpty()) {
            logger.info(String.valueOf(reports2.length));
            for (int i = 0; i < reports2.length; i++) {
                Map<String, Object> caseReport = server.getUndealedCaseReport(reports2[i]);
                if (caseReport == null) continue;
                String sqlMessage = "";
                int singleScore;
                int scoreType;

                if (reportStatus.equals("1")) {
                    logger.info(caseReport.get("scoreType").toString());
                    if (caseReport.get("scoreType").toString().equals("1")) {
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
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ajax.setCheckResponse("个案报告审批失败!");
                }

                try {
                    server.sendMessage(caseReport.get("userID").toString(), "您有一份报告已被审批，可进入 我的报告 查看。", false, reportAgentID);
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
        model.addAttribute("UserID", UserId);
        model.addAttribute("UserName", UserName);
        return "HistoryReport";
    }

    //添加查询与统计
    @PostMapping(value = "/HistoryReport")
    public String HistoryReportPost(@RequestParam("button") String type,
                                    /*@RequestParam("search") String search,*/
                                    @RequestParam("UserID") String UserId,
                                    @RequestParam("UserName") String UserName,
                                    Model model) {
        logger.info("Post HistoryReport: " + UserId); //log

        if (type.equals("我的提交(未审批)")) {
            String sqlGeneralReport = "select reportID,leaderName,category,reportText,submitTime,reportPath " +
                    "from undealedGeneralReport where userID =? order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            Object args[] = new Object[]{UserId};
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {

                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserId, UserName, map.get("submitTime"),
                        "", map.get("category"), map.get("reportText"), "", "","", "", map.
                        get("leaderName"), "", 10, pathtmp);
                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select reportID, leaderName, category, reportText, submitTime, singleScore, scoreType, members, reportPath " +
                    "from undealedCaseReport where userID = ? order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }
            for (Map<String, Object> map : listCaseReport) {

                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserId, UserName, map.get("submitTime"),
                        "", map.get("category"), map.get("reportText"), "",map.get("singleScore"), map.get("scoreType"),
                        "", map.get("leaderName"), map.get("members"), 11, pathtmp);
                reports.add(report_temp);
            }

            model.addAttribute("UserID", UserId);
            model.addAttribute("UserName", UserName);
            model.addAttribute("list", reports);
            model.addAttribute("selected_type", 2);
            return "HistoryReport";
        }

        if (type.equals("我的提交(已审批)")) {
             /* generalReport
            * */
            String sqlGeneralReport = "select reportID,leaderName,category,reportText,submitTime,checkTime,isPass,comment,reportPath " +
                    "from generalReport where userID =? order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            Object args[] = new Object[]{UserId};
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {

                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserId, UserName, map.get("submitTime"),
                        map.get("checkTime"), map.get("category"), map.get("reportText"), map.get("isPass"),"", 1,
                        map.get("comment"), map.get("leaderName"), "", 0, pathtmp);

                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select reportID, leaderName, category, reportText, submitTime, checkTime, isPass, " +
                    "comment, singleScore, scoreType, members, reportPath " +
                    "from caseReport where userID = ? order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }
            for (Map<String, Object> map : listCaseReport) {

                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserId, UserName, map.get("submitTime"),
                        map.get("checkTime"), map.get("category"), map.get("reportText"), map.get("isPass"), map.get("singleScore"),map.get("scoreType"),
                        map.get("comment"), map.get("leaderName"), map.get("members"), 1, pathtmp);
                reports.add(report_temp);
            }

            /* leaderReport
            * */
            String sqlLeaderReport = "select reportID,category, reportText, submitTime, singleScore, scoreType, reportPath " +
                    "from leaderReport where userID = ? order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlLeaderReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }
            for (Map<String, Object> map : listLeaderReport) {
                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserId, "", map.get("submitTime"), "", map.get("category"),
                        map.get("reportText"), "",map.get("singleScore"), map.get("scoreType"), "", "", "", 2, pathtmp);
                reports.add(report_temp);
            }
            model.addAttribute("UserID", UserId);
            model.addAttribute("UserName", UserName);
            model.addAttribute("list", reports);
            model.addAttribute("selected_type", 1);
            return "HistoryReport";
        }
        if (type.equals("我的审批")) {
            String sqlGeneralReport = "select reportID, userID,category,reportText,submitTime,checkTime,isPass,comment,reportPath " +
                    "from generalReport where leaderName = ? order by submitTime desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Object args[] = new Object[]{UserName};/* leader's name*/
            try {
                list = server.jdbcTemplate.queryForList(sqlGeneralReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }

            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : list) {
                String posterName = server.getUserName(map.get("userID").toString());
                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), map.get("userID"), posterName, map.get("submitTime"), map.get("checkTime"), map.get("category")
                        , map.get("reportText"), map.get("isPass"), "",1, map.get("comment"), UserName, "", 0, pathtmp);
                reports.add(report_temp);
            }

            String sqlCaseReport = "select reportID, userID, category, reportText, submitTime,checkTime, isPass, comment, " +
                    "singleScore, scoreType,members, reportPath " +
                    "from caseReport where leaderName = ? order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport, args);
            } catch (Exception e) {
                return e.getMessage();
            }
            for (Map<String, Object> map : listCaseReport) {
                String posterName = server.getUserName(map.get("userID").toString());
                String pathtmp = map.get("reportPath").toString().replaceAll("/root", "");
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), map.get("userID"), posterName, map.get("submitTime"), map.get("checkTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), map.get("singleScore"),
                        map.get("scoreType"), map.get("comment"), UserName, map.get("members"), 1, pathtmp);
                reports.add(report_temp);
            }
            model.addAttribute("UserID", UserId);
            model.addAttribute("UserName", UserName);
            model.addAttribute("list", reports);
            model.addAttribute("selected_type", 3);
            return "HistoryReport";
        }
        model.addAttribute("errorNum","01" );
        return "failure";

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
        reported.clear();
        logger.info("Refresh succeed!"); //log
        return "Refresh succeed!";
    }

    @RequestMapping("/QRCode")
    public String QRCode(@RequestParam("code") String CODE,
                         @RequestParam("state") String REFRESH,
                         Model model) {
        String UserId = server.getUserId(CODE, submitSecret);
        logger.info("Request QRCode: " + UserId); //log
        if (!server.isUser(UserId)) {
            model.addAttribute("errorNum","00");
            return "failure";
        }
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
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("creator", UserId);
        model.addAttribute("CODE", CODE);
        return "QRCode";
    }

    @RequestMapping("/redirectQR")
    public String redirectQR(@RequestParam("timestamp") String timestamp,
                             @RequestParam("creator") String creator,
                             Model model) {
        logger.info("Request redirectQR: " + creator + "-" + timestamp); //log

        model.addAttribute("state", creator + "-" + timestamp);
        return "redirectQR";
    }

    @RequestMapping("/checkin")
    public String checkin(@RequestParam("code") String CODE,
                          @RequestParam("state") String STATE,
                          Model model) {
        logger.info("Request checkin: " + STATE); //log

        int p = STATE.indexOf("-");
        String creator = STATE.substring(0, p);
        long timestamp = Long.parseLong(STATE.substring(p + 1));
        if (!checkins.containsKey(creator) || checkins.get(creator).getTimestamp() != timestamp)  // invalid QRcode
        {
            model.addAttribute("errorNum", "03");
            return "failure";
        }
        if (System.currentTimeMillis() - timestamp > QRTimeout)  // invalid QRcode
        {
            model.addAttribute("errorNum", "03");
            return "failure";
        }
        String userID = server.getUserId(CODE, submitSecret);
        Checkin checkin = checkins.get(creator);
        if (checkin.getUsers().contains(userID))
        {
            model.addAttribute("errorNum" , "04");
            return "failure";// avoid scanning QRcode repeatly
        }
        server.award(userID, 2);
        checkin.add(userID);
        model.addAttribute("userID", userID);
        return "checkinSuccess";
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
