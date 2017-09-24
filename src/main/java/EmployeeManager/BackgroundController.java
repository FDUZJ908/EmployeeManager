package EmployeeManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static EmployeeManager.Server.PASecret;

@Controller
public class BackgroundController {

    @Autowired
    Server server;
    Map<String, Checkin> checkins = new HashMap<String, Checkin>();
    @Value("${web.upload-path}")
    private String path;

    @RequestMapping("/GeneralReport")
    public String GeneralReport(@RequestParam("state") String STATE,
                                @RequestParam("code") String code,
                                Model model) {
        //System.out.println(code);
        String UserId = server.getUserId(code, PASecret);
        //System.out.println(UserId);
        if (server.isUser(UserId) == false)
            return "failure";

       /* List<Map<String, Object>> Departments = server.getDepartment(UserId);
        if (Departments == null)
            return "failure";
        List<User> Leaders = server.getLeader(UserId, Departments);*/

        // Department-Leader-LeaderID
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);

        model.addAttribute("UserId", UserId);
        model.addAttribute("list", DLeaders);
        return "GeneralReport";
    }

    @PostMapping(value = "/GeneralReport")
    public String GeneralReportPost(@RequestParam("content") String content,
                                    @RequestParam("type") String type,
                                    @RequestParam("leader") String leader,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("UserId") String UserId) {
        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (server.saveFile(file, pathCurrent) == false)
            return "failure";
        String sqlMessage = UserId + ",'" + leader + "'," + type + ",'" + content + "','" + pathCurrent + "','" +
                currentTime + "'";
        server.jdbcTemplate.update("insert into undealedGeneralReport " +
                "(userID,leaderName,category,reportText,reportPath,submitTime) values(" + sqlMessage + ")");
        return "success";
    }

    @RequestMapping("/CaseReport")
    public String CaseReport(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {

        String UserId = server.getUserId(CODE, PASecret);
        if (server.isUser(UserId) == false)
            return "failure";
        List<String> AllUsers = server.getAllUsers();
        List<Map<String, Object>> Departments = server.getDepartment(UserId);
        if (Departments == null)
            return "failure";
        List<User> Leaders = server.getLeader(UserId, Departments);

        // Department-Leader-LeaderID
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);

        model.addAttribute("UserId", UserId);
        model.addAttribute("Leaders", Leaders);
        model.addAttribute("Departments", Departments);
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
       /* List<String> errorUser = server.checkMember(members);
        if (!errorUser.isEmpty()) {
            model.addAttribute("errorUser", errorUser);
            List<Map<String, Object>> Departments = server.getDepartment(UserId);
            if (Departments == null)
                return "failure";
            List<User> Leaders = server.getLeader(UserId, Departments);
            model.addAttribute("UserId", UserId);
            model.addAttribute("Leaders", Leaders);
            model.addAttribute("Departments", Departments);
            return "CaseReport";
        }*/
        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (server.saveFile(file, pathCurrent) == false)
            return "failure";
        String sqlMessage = "'"+UserId + "','" + leader + "','" + members + "'," + type + ",'" + content + "','" +
                pathCurrent + "'," + score + ",'" + currentTime + "'," + score_type;
        server.jdbcTemplate.update("insert into undealedCaseReport " +
                "(userID,leaderName,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");
        return "success";
    }

    @RequestMapping("/LeadershipReport")
    public String Leadership(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {

        String UserId = server.getUserId(CODE, PASecret);
        if (server.isUser(UserId) == false)
            return "failure";
        List<DepartmentLeader> DLeaders = server.getUserDepartmentLeader(UserId);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("UserId", UserId);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("list",DLeaders);
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
        //判断是否领导自己提交?
        List<String> errorUser = server.checkMember(members);
        if (!errorUser.isEmpty()) {
            model.addAttribute("errorUser", errorUser);
            model.addAttribute("UserId", UserId);
            return "LeadershipReport";
        }
        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (server.saveFile(file, pathCurrent) == false)
            return "failure";
        String sqlMessage = UserId + ",'" + members + "'," + type + ",'" + content + "','" + pathCurrent + "'," +
                score + ",'" + server.currentTime() + "'," + score_type;
        server.jdbcTemplate.update("insert into leaderReport " +
                "(userID,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");
        return "success";
    }

    @RequestMapping("/RankingList")
    public String RankingList(@RequestParam("code") String CODE,
                              @RequestParam("state") String STATE,
                              Model model) {
        String UserId = server.getUserId(CODE, PASecret);
        if (server.isUser(UserId) == false)
            return "failure";
        return "RankingList";
    }

    //积分排行大致如下，需要增加显示更多信息（照片等）
    @PostMapping(value = "/RankingList")
    public String RankingListPost(@RequestParam("button") String type, Model model) {

        if (type.equals("总排行")) {
            String sql = "select userName,s_score,avatarURL from user order by s_score desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sql);
            } catch (Exception e) {
                return e.toString();
            }
            List<User> users = new ArrayList<User>();
            int rank = 1;
            for (Map<String, Object> map : list) {
                User user_temp = new User(map.get("userName"), map.get("s_score"), rank++, map.get("avatarURL"));
                users.add(user_temp);
            }
            model.addAttribute("selected_type",type);
            model.addAttribute("list", users);
            return "RankingList";
        } else {
            String selectedType = new String();
            if (type.equals("领导干部"))
                selectedType = "3";
            else if (type.equals("中层干部"))
                selectedType = "2";
            else if (type.equals("一般干部"))
                selectedType = "1";
            else selectedType = "0";
            String sql = "select userName,s_score,avatarURL from user where position=" + selectedType + " order by s_score desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sql);
            } catch (Exception e) {
                return e.toString();
            }
            List<User> users = new ArrayList<User>();
            int rank = 1;
            for (Map<String, Object> map : list) {
                User user_temp = new User(map.get("userName"), map.get("s_score"), rank++, map.get("avatarURL"));
                users.add(user_temp);
            }
            model.addAttribute("selected_type",type);
            model.addAttribute("list", users);
            return "RankingList";
        }
    }

    @RequestMapping("/ReportApproval")
    public String ReportApproval(@RequestParam("code") String CODE,
                                 @RequestParam("state") String STATE,
                                 Model model) {

        String UserId = server.getUserId(CODE, PASecret);
        if (server.isUser(UserId) == false)
            return "failure";

        String getGeneralReport = "select reportID,userID,category,reportText,submitTime,userName " +
                "from undealedGeneralReport natural join user where leaderName=(select userName from user " +
                "where userID=?)";

        String getCaseReport = "select reportID,userID,category,reportText,submitTime,members,singleScore,userName " +
                "from undealedCaseReport natural join user " +
                "where leaderName=(select userName from user where userID=?)";

        Object args[] = new Object[]{UserId};
        List<Map<String, Object>> generalReport;
        List<Map<String, Object>> caseReport;
        try {
            generalReport = server.jdbcTemplate.queryForList(getGeneralReport, args);
        } catch (Exception e) {
            return e.toString();
        }
        try {
            caseReport = server.jdbcTemplate.queryForList(getCaseReport, args);
        } catch (Exception e) {
            return e.toString();
        }
        List<GeneralReport> list1 = new ArrayList<GeneralReport>();
        List<CaseReport> list2 = new ArrayList<CaseReport>();
        for (Map<String, Object> map : generalReport) {
            GeneralReport list1_temp = new GeneralReport(map.get("reportID"), map.get("userID"), map.get("userName"),
                    map.get("category"), map.get("reportText"), map.get("submitTime"));
            list1.add(list1_temp);
        }
        for (Map<String, Object> map : caseReport) {
            CaseReport list2_temp = new CaseReport(map.get("reportID"), map.get("userID"), map.get("userName"),
                    map.get("category"), map.get("reportText"), map.get("submitTime"), map.get("members"),
                    map.get("singleScore"));
            list2.add(list2_temp);
        }
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);
        return "ReportApproval";
    }

    @PostMapping("/ReportApproval")
    public String ReportApprovalPost(@RequestParam("reportStatus") String reportStatus,
                                     @RequestParam("reportComment") String reportComment,
                                     @RequestParam("check1") String check1,
                                     @RequestParam("check2") String check2) {
        String[] reports1 = check1.split(",");
        String[] reports2 = check2.split(",");

        if (reportStatus.equals("1"))
            reportStatus = "1";
        else
            reportStatus = "0";

        String updateSql = "";
        String checkTime = server.currentTime();

        if (!check1.isEmpty()) {
            for (int i = 0; i < reports1.length; i++) {
                List<Map<String, Object>> generalReport;
                generalReport = server.getUndealedGeneralReport(reports1[i]);
                String sqlMessage = "";
                int singleScore;
                for (Map<String, Object> map : generalReport) {
                    sqlMessage = "'" + map.get("userID").toString() + "'" +
                            ",'" + map.get("leaderName").toString() + "'," +
                            map.get("category").toString() +
                            ",'" + map.get("reportText").toString() + "'" +
                            ",'" + map.get("reportPath").toString() + "'" +
                            ",'" + map.get("submitTime").toString() + "'" +
                            ",'" + checkTime + "'," +
                            reportStatus +
                            ",'" + reportComment + "'";
                    if (map.get("category").toString().equals("1"))
                        singleScore = 1;
                    else
                        singleScore = 2;
                    if (reportStatus.equals("0"))
                        singleScore = 0;

                    updateSql = "UPDATE user set s_score=s_score + " + singleScore +
                            " where userID=?";
                    Object args[] = new Object[]{map.get("userID").toString()};

                    server.jdbcTemplate.update(updateSql, args);
                }

                updateSql = "insert into generalReport " +
                        "(userID,leaderName,category,reportText,reportPath,submitTime,checkTime,isPass,comment) " +
                        "values(" + sqlMessage + ")";
                server.jdbcTemplate.update(updateSql);
                updateSql = "delete from undealedGeneralReport where reportID=" + reports1[i];
                server.jdbcTemplate.update(updateSql);


            }
        }
        if (!check2.isEmpty()) {
            for (int i = 0; i < reports2.length; i++) {
                List<Map<String, Object>> caseReport;
                caseReport = server.getUndealedCaseReport(reports2[i]);
                String sqlMessage = "";
                int singleScore;
                for (Map<String, Object> map : caseReport) {
                    String scoreType = "";
                    if (reportStatus.equals("1")) {
                        scoreType = "1";
                        singleScore = Integer.parseInt(map.get("singleScore").toString());
                    } else {
                        scoreType = "0";
                        singleScore = -Integer.parseInt(map.get("singleScore").toString());

                    }
                    sqlMessage = "'" + map.get("userID").toString() + "'" +
                            ",'" + map.get("leaderName").toString() + "'" +
                            ",'" + map.get("members").toString() + "'," +
                            map.get("category").toString() +
                            ",'" + map.get("reportText").toString() + "'" +
                            ",'" + map.get("reportPath").toString() + "'" +
                            ",'" + scoreType + "'," +
                            map.get("singleScore").toString() +
                            ",'" + map.get("submitTime").toString() + "'" +
                            ",'" + checkTime + "'," +
                            reportStatus +
                            ",'" + reportComment + "'";
                    System.out.print(map.get("members").toString());
                    if (!map.get("members").toString().isEmpty()) {
                        String[] member = map.get("members").toString().split(",");

                        for (int j = 0; j < member.length; j++) {
                            updateSql = "update user set s_score = s_score + " + singleScore +
                                    " where userName=?";
                            Object args1[] = new Object[]{member[j]};
                            server.jdbcTemplate.update(updateSql, args1);
                        }
                    }

                    updateSql = "update user set s_score = s_score + " + singleScore +
                            " where userID=?";
                    Object args2[] = new Object[]{map.get("userID").toString()};
                    server.jdbcTemplate.update(updateSql, args2);
                }
                updateSql = "insert into caseReport " +
                        "(userID,leaderName,members,category,reportText,reportPath,scoreType," +
                        "singleScore,submitTime,checkTime,isPass,comment) " +
                        "values(" + sqlMessage + ")";
                server.jdbcTemplate.update(updateSql);
                updateSql = "delete from undealedCaseReport where reportID=" + reports2[i];
                server.jdbcTemplate.update(updateSql);
            }
        }
        return "success";
    }

    @RequestMapping("/HistoryReport")
    public String HistoryReport(@RequestParam("code") String CODE,
                                @RequestParam("state") String STATE,
                                Model model) {
        String UserId = server.getUserId(CODE, PASecret);
        if (server.isUser(UserId) == false)
            return "failure";
        String UserName = server.getUserName(UserId);
        model.addAttribute("UserID", UserId);
        model.addAttribute("UserName", UserName);
        return "HistoryReport";
    }

    //添加查询与统计
    @PostMapping(value = "/HistoryReport")
    public String HistoryReportPost(@RequestParam("button") String type,
                                    /*@RequestParam("search") String search,*/
                                    @RequestParam("UserID") String UserID,
                                    @RequestParam("UserName") String UserName,
                                    Model model) {
        if (type.equals("我的提交(未审批)")) {
            String sqlGeneralReport = "select reportID,leaderName,category,reportText,submitTime" +
                    "from undealedGeneralReport where userID =? order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            Object args[] = new Object[]{UserID};
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport, args);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), "", "", "", map.get("leaderName"), "", 10);
                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select reportID, leaderName, category, reportText, submitTime, singleScore, scoreType, members " +
                    "from undealedCaseReport where userID = ? order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport, args);
            } catch (Exception e) {
                return e.toString();
            }
            for (Map<String, Object> map : listCaseReport) {
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), "", map.get("scoreType"), "", map.get("leaderName"), map.get("members"), 11);
                reports.add(report_temp);
            }

            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName", UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }

        if (type.equals("我的提交(已审批)")) {
             /* generalReport
            * */
            String sqlGeneralReport = "select reportID,leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalReport where userID =? order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            Object args[] = new Object[]{UserID};
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport, args);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 1, map.get("comment"), map.get("leaderName"), "", 0);
                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select reportID, leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType, members " +
                    "from caseReport where userID = ? order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport, args);
            } catch (Exception e) {
                return e.toString();
            }
            for (Map<String, Object> map : listCaseReport) {
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), map.get("scoreType"), map.get("comment"), map.get("leaderName"), map.get("members"), 1);
                reports.add(report_temp);
            }

            /* leaderReport
            * */
            String sqlLeaderReport = "select reportID,category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from leaderReport where userID = ? order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlLeaderReport, args);
            } catch (Exception e) {
                return e.toString();
            }
            for (Map<String, Object> map : listLeaderReport) {
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), map.get("scoreType"), map.get("comment"), map.get("leaderName"), map.get("members"), 2);
                reports.add(report_temp);
            }
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName", UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }
        if (type.equals("我的审批")) {
            String sqlGeneralReport = "select reportID, userID,category,reportText,submitTime,isPass,comment " +
                    "from generalReport where leaderName = ? order by submitTime desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Object args[] = new Object[]{UserName};/* leader's name*/
            try {
                list = server.jdbcTemplate.queryForList(sqlGeneralReport, args);
            } catch (Exception e) {
                return e.toString();
            }

            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : list) {
                String posterName = server.getUserName(map.get("userID").toString());
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), map.get("userID"), posterName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"), "", 0);
                reports.add(report_temp);
            }

            String sqlCaseReport = "select reportID, userID, category, reportText, submitTime, isPass, comment, singleScore, scoreType,members " +
                    "from caseReport where leaderName = ? order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport, args);
            } catch (Exception e) {
                return e.toString();
            }
            for (Map<String, Object> map : listCaseReport) {
                String posterName = server.getUserName(map.get("userID").toString());
                HistoryReport report_temp = new HistoryReport(map.get("reportID"), map.get("userID"), posterName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), map.get("scoreType"), map.get("comment"), map.get("leaderName"), map.get("members"), 1);
                reports.add(report_temp);
            }
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName", UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }
        return "failure";

    }

    @RequestMapping("/Synchronizer")
    @ResponseBody
    public String Synchronizer() throws Exception {

        server.syncUser(server.syncDepartment());

        return "Synchronization succeed!";
    }

    @RequestMapping("/QRCode")
    public String QRCode(@RequestParam("code") String CODE,
                         Model model) {
        String UserID = server.getUserId(CODE, PASecret);
        String timestamp = Long.toString(System.currentTimeMillis());
        if (checkins.containsKey(UserID)) {
            Checkin checkin = checkins.get(UserID);
            checkin.deleteCheckinMember();
            checkin.setTimestamp(timestamp);
        } else {
            if (server.isUser(UserID)) {
                Checkin checkin = new Checkin(timestamp);
                checkins.put(UserID, checkin);
            } else
                return "failure";
        }
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("creator", UserID);
        return "QRCode";
    }

    @RequestMapping("/redirectQR")
    public String redirectQR(@RequestParam("timestamp") String timestamp,
                             @RequestParam("creator") String creator,
                             Model model) {
        System.out.println(timestamp);
        if (!checkins.containsKey(creator) || !checkins.get(creator).getTimestamp().equals(timestamp)) return "failure";
        System.out.println("creator:"+creator);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("creator", creator);
        return "redirectQR";
    }

    @RequestMapping("/checkin")
    public String checkin(@RequestParam("code") String CODE,
                          @RequestParam("state") String STATE,
                          Model model) {
        System.out.println(CODE);
        String userID = server.getUserId(CODE, PASecret);
        server.award(userID,2);
        System.out.println("checkin:" + userID);
        if (checkins.get(STATE).getCheckinMember().contains(userID)) return "failure";
        model.addAttribute("userID", userID);
        return "checkinSuccess";
    }
}
