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
        System.out.println(server.jdbcTemplate == null);

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
        System.out.println("Enter GeneralReport");
        System.out.println("UserId:" + UserId);
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

        if (reportStatus.equals("true"))
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
                for (Map<String, Object> map : caseReport) {
                    String scoreType = "";
                    if (map.get("scoreType").toString().equals("true"))
                        scoreType = "1";
                    else
                        scoreType = "0";
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
        return "ReportApproval";
    }
    /*
    @RequestMapping("/HistoryReport")
    public String HistoryReport() {
        return "HistoryReport";
    }

    //添加查询与统计
    @PostMapping(value = "/HistoryReport")
    public String HistoryReportPost() {
        return "success";
    }
*/
}
