package EmployeeManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static EmployeeManager.Server.PASecret;

@Controller
public class BackgroundController {

    @Autowired
    Server server;

    @Value("${web.upload-path}")
    private String path;

    @RequestMapping("/GeneralReport")
    public String GeneralReport(//@RequestParam("state") String STATE,
                                //@RequestParam("code") String code,
                                Model model) {

        // 本地测试
        /*
        System.out.println(code);
        String UserId = server.getUserId(code, PASecret);
        System.out.println(UserId);
        if (server.isUser(UserId) == false)
            return "failure";
        */

        String UserId = "1";
        List<Map<String, Object>> Departments = server.getDepartment(UserId);
        if (Departments == null)
            return "failure";
        List<User> Leaders = server.getLeader(UserId, Departments);
        model.addAttribute("UserId", UserId);
        model.addAttribute("Leaders", Leaders);
        model.addAttribute("Departments", Departments);
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
        server.jdbcTemplate.update("insert into undealedgeneralreport " +
                "(userID,leaderName,category,reportText,reportPath,submitTime) values(" + sqlMessage + ")");
        return "success";
    }


    @RequestMapping("/CaseReport")
    public String CaseReport(//@RequestParam("code") String CODE,
                             //@RequestParam("state") String STATE,
                             Model model) {
        /*
        String UserId = server.getUserId(CODE, "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY");
        if (server.isUser(UserId) == false)
            return "failure";
        */

        String UserId = "1";
        List<Map<String, Object>> Departments = server.getDepartment(UserId);
        if (Departments == null)
            return "failure";
        List<User> Leaders = server.getLeader(UserId, Departments);
        model.addAttribute("UserId", UserId);
        model.addAttribute("Leaders", Leaders);
        model.addAttribute("Departments", Departments);
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
        List<String> errorUser = server.checkMember(members);
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
        }
        String currentTime = server.currentTime();
        String currentFileName = server.currentFileName(currentTime, file.getOriginalFilename());
        server.mkDir(UserId);
        String pathCurrent = path + UserId + "/" + currentFileName;
        if (server.saveFile(file, pathCurrent) == false)
            return "failure";
        String sqlMessage = UserId + ",'" + leader + "','" + members + "'," + type + ",'" + content + "','" +
                pathCurrent + "'," + score + ",'" + currentTime + "'," + score_type;
        server.jdbcTemplate.update("insert into undealedcasereport " +
                "(userID,leaderName,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");
        return "success";
    }

    @RequestMapping("/LeadershipReport")
    public String Leadership(//@RequestParam("code") String CODE,
                             //@RequestParam("state") String STATE,
                             Model model) {
/*
        String UserId = getUserId(CODE, "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY");
        if (isUser(UserId) == false)
            return "failure";
*/
        String UserId = "1";
        model.addAttribute("UserId", UserId);
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
        server.jdbcTemplate.update("insert into leaderreport " +
                "(userID,members,category,reportText,reportPath,singleScore,submitTime,scoreType) " +
                "values(" + sqlMessage + ")");
        return "success";
    }

    @RequestMapping("/RankingList")
    public String RankingList(Model model) {
        return "RankingList";
    }

    //积分排行大致如下，需要增加显示更多信息（照片等）
    @PostMapping(value = "/RankingList")
    public String RankingListPost(@RequestParam("button") String type, Model model) {

        if (type.equals("所有干部")) {
            String sql = "select userName,s_score from user order by s_score desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sql);
            } catch (Exception e) {
                return e.toString();
            }
            List<User> users = new ArrayList<User>();
            for (Map<String, Object> map : list) {
                User user_temp = new User(map.get("userName"), map.get("s_score"));
                users.add(user_temp);
            }
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
            String sql = "select userName,s_score from user where position=" + selectedType + " order by s_score desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sql);
            } catch (Exception e) {
                return e.toString();
            }
            List<User> users = new ArrayList<User>();
            for (Map<String, Object> map : list) {
                User user_temp = new User(map.get("userName"), map.get("s_score"));
                users.add(user_temp);
            }
            model.addAttribute("list", users);
            return "RankingList";
        }
    }

    @RequestMapping("/ReportApproval")
    public String ReportApproval(//@RequestParam("code") String CODE,
                                 //@RequestParam("state") String STATE,
                                 Model model) {
        /*
        String UserId = getUserId(CODE, "5nFQW5WCF1Gxk7Ht6crCkACsUhqP1DkGk7Fsvg8W67E");
        if (server.isUser(UserId) == false)
            return "failure";
        */

        String UserId = "1";

        String getGeneralReport = "select reportID,userID,category,reportText,submitTime,userName " +
                "from undealedgeneralreport natural join user where leaderName=(select userName from user " +
                "where userID=" + UserId + ")";
        String getCaseReport = "select reportID,userID,category,reportText,submitTime,members,singleScore,userName " +
                "from undealedcasereport natural join user " +
                "where leaderName=(select userName from user where userID=" + UserId + ")";
        List<Map<String, Object>> generalReport;
        List<Map<String, Object>> caseReport;
        try {
            generalReport = server.jdbcTemplate.queryForList(getGeneralReport);
        } catch (Exception e) {
            return e.toString();
        }
        try {
            caseReport = server.jdbcTemplate.queryForList(getCaseReport);
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
                    sqlMessage = map.get("userID").toString() +
                            ",'" + map.get("leaderName").toString() + "'," +
                            map.get("category").toString() +
                            ",'" + map.get("reportText").toString() + "'" +
                            ",'" + map.get("reportPath").toString() + "'" +
                            ",'" + map.get("submitTime").toString() + "'" +
                            ",'" + checkTime + "'," +
                            reportStatus +
                            ",'" + reportComment + "'";
                    if(map.get("category").toString().equals("1"))
                        singleScore =  1;
                    else
                        singleScore =  2;
                    if(reportStatus.equals("0"))
                        singleScore = 0;

                    updateSql = "UPDATE user set s_score=s_score + " + singleScore + " where userID=" + map.get("userID").toString();
                    server.jdbcTemplate.update(updateSql);
                }

                updateSql = "insert into generalreport " +
                        "(userID,leaderName,category,reportText,reportPath,submitTime,checkTime,isPass,comment) " +
                        "values(" + sqlMessage + ")";
                server.jdbcTemplate.update(updateSql);
                updateSql = "delete from undealedgeneralreport where reportID=" + reports1[i];
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
                    }
                    else {
                        scoreType = "0";
                        singleScore = -Integer.parseInt(map.get("singleScore").toString());

                    }
                    sqlMessage = map.get("userID").toString() +
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
                    if(!map.get("members").toString().isEmpty()) {
                        String[] member = map.get("members").toString().split(",");

                        for (int j = 0; j < member.length; j++) {
                            updateSql = "update user set s_score = s_score + " + singleScore + " where userID=" + member[j];
                            server.jdbcTemplate.update(updateSql);
                        }
                    }
                }
                updateSql = "insert into casereport " +
                        "(userID,leaderName,members,category,reportText,reportPath,scoreType," +
                        "singleScore,submitTime,checkTime,isPass,comment) " +
                        "values(" + sqlMessage + ")";
                server.jdbcTemplate.update(updateSql);
                updateSql = "delete from undealedcasereport where reportID=" + reports2[i];
                server.jdbcTemplate.update(updateSql);
            }
        }
        return "success";
    }

    @RequestMapping("/HistoryReport")
    public String HistoryReport(//@RequestParam("code") String CODE,
                                //@RequestParam("state") String STATE,
                                Model model) {
        String UserID = "1";
        String UserName = "潘";
        model.addAttribute("UserID", UserID);
        model.addAttribute("UserName",UserName);
        return "HistoryReport";
    }

    //添加查询与统计
    @PostMapping(value = "/HistoryReport")
    public String HistoryReportPost(@RequestParam("button") String type,
                                    @RequestParam("search") String search,
                                    @RequestParam("UserID") String UserID,
                                    @RequestParam("UserName") String UserName,
                                    Model model) {

        if (type.equals("日常工作") || type.equals("领导交办") || type.equals("阶段汇总") ||
                type.equals("考勤签到") || type.equals("急难险重") || type.equals("其他工作")) {
            String sqlSearchGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport " +
                    "where (userID = '" + UserID + "' or " +
                    "leaderName = '" + UserName + "') " +
                    " order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlSearchGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }

            String sqlSearchCaseReport = "select leaderName,category,reportText,submitTime,isPass,comment,singleScore,scoreType " +
                    "from casereport " +
                    "where (userID = '" + UserID + "' or " +
                    "leaderName = '" + UserName + "') " +
                    " order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlSearchCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);

            String sqlSearchLeaaderReport = "select category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from leaderreport where userID = '" + UserID + "' " +
                    " order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlSearchLeaaderReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listLeaderReport, UserID, UserName);

            for (int i = 0; i < reports.size(); i++) {
                HistoryReport rpt = reports.get(i);
                if (!type.equals(rpt.getCategory())) {
                    reports.remove(i);
                    i--;
                }
            }

            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }


        if (type.equals("搜索")) {
            search = "%" + search.toLowerCase() + "%";
            String sqlSearchGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport " +
                    "where (userID = '" + UserID + "' or " +
                    "leaderName = '" + UserName + "') and " +
                    "(lower(reportText) like '" + search + "' or " +
                    "lower(comment) like '" + search + "' or " +
                    "lower(submitTime) like '" + search + "' or " +
                    "lower(leaderName) like '" + search + "') " +
                    " order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlSearchGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }

            String sqlSearchCaseReport = "select leaderName,category,reportText,submitTime,isPass,comment,singleScore,scoreType " +
                    "from casereport " +
                    "where (userID = '" + UserID + "' or " +
                    "leaderName = '" + UserName + "') and " +
                    "(lower(category) like '" + search + "' or " +
                    "lower(reportText) like '" + search + "' or " +
                    "lower(comment) like '" + search + "' or " +
                    "lower(submitTime) like '" + search + "' or " +
                    "lower(leaderName) like '" + search + "') " +
                    " order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlSearchCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);

            String sqlSearchLeaaderReport = "select category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from leaderreport where userID = '" + UserID + "' and (" +
                    "lower(reportText) like '" + search + "' or " +
                    "lower(submitTime) like '" + search + "' or " +
                    "lower(comment) like '" + search + "') " +
                    " order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlSearchLeaaderReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listLeaderReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }


        if (type.equals("我提交的报告")) {
            /* generalReport
            * */
            String sqlGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport where userID = '" + UserID + "' order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from casereport where userID = '" + UserID + "' order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);

            /* leaderReport
            * */
            String sqlLeaderReport = "select category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from leaderreport where userID = '" + UserID + "' order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlLeaderReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listLeaderReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }

        if (type.equals("我提交的已通过的报告")) {
            /* generalReport
            * */
            String sqlGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport where userID = '" + UserID + "' and isPass = '1' order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from casereport where userID = '" + UserID + "' and isPass = '1' order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);

            /* leaderReport
            * */
            String sqlLeaderReport = "select category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from leaderreport where userID = '" + UserID + "' and isPass = '1' order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlLeaderReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listLeaderReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }


        if (type.equals("我提交的未通过的报告")) {
            /* generalReport
            * */
            String sqlGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport where userID = '" + UserID + "' and isPass = '0' order by submitTime desc";
            List<Map<String, Object>> listGeneralReport = new ArrayList<Map<String, Object>>();
            try {
                listGeneralReport = server.jdbcTemplate.queryForList(sqlGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : listGeneralReport) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }

            /* caseReport
            * */
            String sqlCaseReport = "select leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from casereport where userID = '" + UserID + "' and isPass = '0' order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);

            /* leaderReport
            * */
            String sqlLeaderReport = "select category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from leaderreport where userID = '" + UserID + "' and isPass = '0' order by submitTime desc";
            List<Map<String, Object>> listLeaderReport = new ArrayList<Map<String, Object>>();
            try {
                listLeaderReport = server.jdbcTemplate.queryForList(sqlLeaderReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listLeaderReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }


        if (type.equals("我审批的报告")) {
            String sqlGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport where leaderName = '" + UserName + "' order by submitTime desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sqlGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : list) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }
            String sqlCaseReport = "select leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from casereport where leaderName = '" + UserName + "' order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }

        if (type.equals("我审批的已通过的报告")) {
            String sqlGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport where leaderName = '" + UserName + "' and isPass = '1' order by submitTime desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sqlGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : list) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }
            String sqlCaseReport = "select leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from casereport where leaderName = '" + UserName + "' and isPass = '1' order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }

        if (type.equals("我审批的未通过的报告")) {
            String sqlGeneralReport = "select leaderName,category,reportText,submitTime,isPass,comment " +
                    "from generalreport where leaderName = '" + UserName + "' and isPass = '0' order by submitTime desc";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                list = server.jdbcTemplate.queryForList(sqlGeneralReport);
            } catch (Exception e) {
                return e.toString();
            }
            List<HistoryReport> reports = new ArrayList<HistoryReport>();
            for (Map<String, Object> map : list) {
                HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                        map.get("reportText"), map.get("isPass"), 0, map.get("comment"), map.get("leaderName"));
                reports.add(report_temp);
            }
            String sqlCaseReport = "select leaderName, category, reportText, submitTime, isPass, comment, singleScore, scoreType " +
                    "from casereport where leaderName = '" + UserName + "' and isPass = '0' order by submitTime desc";
            List<Map<String, Object>> listCaseReport = new ArrayList<Map<String, Object>>();
            try {
                listCaseReport = server.jdbcTemplate.queryForList(sqlCaseReport);
            } catch (Exception e) {
                return e.toString();
            }
            server.getReports(reports, listCaseReport, UserID, UserName);
            model.addAttribute("UserID", UserID);
            model.addAttribute("UserName",UserName);
            model.addAttribute("list", reports);
            return "HistoryReport";
        }

        return "failure";
    }


}
