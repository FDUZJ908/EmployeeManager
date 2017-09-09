package EmployeeManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static  EmployeeManager.Server.PASecret;

@Controller
public class BackgroundController {

    //static final Logger log = LoggerFactory.getLogger(Application.class);

    Server server=new Server();

    //@Autowired
    //private JdbcTemplate jdbcTemplate;

    // @Value("${web.upload-path}")
    //String path;

    /*
    public String getUserId(String CODE, String corpsecret) {
        String token;
        String UserId;
        HttpsGet http = new HttpsGet();
        try {
            JSONObject jsonObject = http.sendGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx7635106ee1705d6d&corpsecret=" + corpsecret);
            token = jsonObject.get("access_token").toString();
        } catch (Exception e) {
            log.info(e.toString());
            return "failure";
        }
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + token + "&code=" + CODE;
            JSONObject jsonObject = http.sendGet(url);
            UserId = jsonObject.get("UserId").toString();
        } catch (Exception e) {
            log.info(e.toString());
            return "failure";
        }
        return UserId;
    }
    */
    /*
    public boolean isUser(String UserId) {
        String isUserSql = "select userName from user where userid=" + UserId;
        List<Map<String, Object>> isUser;
        try {
            isUser = jdbcTemplate.queryForList(isUserSql);
        } catch (Exception e) {
            return false;
        }
        if (isUser.isEmpty()) {
            log.info("NotUser");
            return false;
        }
        return true;
    }
    */
/*

    public List<Map<String, Object>> getDepartment(String UserId) {
        String dIDSql = "select dID,dName from department where userid=" + UserId;
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql);
        } catch (Exception e) {
            return null;
        }
        return department;
    }

    public List<User> getLeader(String UserId, List<Map<String, Object>> department) {
        Object dID = "";
        List<User> users = new ArrayList<User>();
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID from department natural join user  where isLeader=1 and dID=\"" + dID + '"';
            try {
                dLeader = jdbcTemplate.queryForList(leaderSql);
            } catch (Exception e) {
                return null;
            }
            for (Map<String, Object> leader : dLeader) {
                User user_temp = new User(leader.get("userName"), leader.get("userID"));
                users.add(user_temp);
            }
        }
        return users;
    }
*/
/*
    public String currentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    public boolean saveFile(MultipartFile file, String pathCurrent) {
        if (!file.isEmpty()) {
            try {
                pathCurrent = path + file.getOriginalFilename();
                BufferedOutputStream bufferedOutputStream = null;
                FileOutputStream fileOutputStream = null;
                File f = new File(pathCurrent);
                fileOutputStream = new FileOutputStream(pathCurrent);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                bufferedOutputStream.write(file.getBytes());
                bufferedOutputStream.flush();
                bufferedOutputStream.close();


            } catch (FileNotFoundException e) {
                log.info("FileNotFound");
                return false;
            } catch (IOException e) {
                log.info("IOException");
                return false;
            }
        }
        return true;
    }*/
/*

    public List<String> checkMember(String members) {
        //分解队伍成员
        String[] member = members.split(" ");
        List<Map<String, Object>> memberCursor = new ArrayList<Map<String, Object>>();
        String memberSql = "";
        List<String> errorUser = new ArrayList<String>();
        //检验队伍成员
        for (int i = 0; i < member.length; i++) {
            memberSql = "select * from user where userName=\"" + member[i] + "\"";
            try {
                memberCursor = jdbcTemplate.queryForList(memberSql);
            } catch (Exception e) {
                ;
            }
            if (memberCursor.isEmpty()) {
                errorUser.add(member[i]);
            }
        }
        return errorUser;
    }
*/

    @RequestMapping("/GeneralReport")
    //@RequestParam("state") String STATE,
    public String GeneralReport(@RequestParam("code") String code,Model model) {
        System.out.println(code);
        String UserId = server.getUserId(code, PASecret);
        System.out.println(UserId);
        if (server.isUser(UserId) == false)
            return "failure";
        /*List<Map<String, Object>> Departments = server.getDepartment(UserId);
        if (Departments == null)
            return "failure";
        List<User> Leaders = getLeader(UserId, Departments);*/
        model.addAttribute("UserId", UserId);
        /*model.addAttribute("Leaders", Leaders);
        model.addAttribute("Departments", Departments);*/
        return "GeneralReport";
    }
/*
    @PostMapping(value = "/GeneralReport")
    public String GeneralReportPost(@RequestParam("content") String content,
                                    @RequestParam("type") String type,
                                    @RequestParam("leader") String leader,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("UserId") String UserId) {
        log.info("Enter GeneralReport");
        log.info("UserId:" + UserId);
        String pathCurrent = path + file.getOriginalFilename();
        String sqlMessage = UserId + ",'" + leader + "'," + type + ",'" + content + "','" + pathCurrent + "','" + currentTime() + "'";
        jdbcTemplate.update("insert into generalreport (userID,leaderName,category,reportText,reportPath,submitTime) values(" + sqlMessage + ")");
        if (saveFile(file, pathCurrent) == false)
            return "failure";
        return "success";
    }*/
/*

    @RequestMapping("/CaseReport")
    public String CaseReport(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {
        String UserId = getUserId(CODE, "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY");
        if (isUser(UserId) == false)
            return "failure";
        List<Map<String, Object>> Departments = getDepartment(UserId);
        if (Departments == null)
            return "failure";
        List<User> Leaders = getLeader(UserId, Departments);
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
        List<String> errorUser = checkMember(members);
        if (!errorUser.isEmpty()) {
            model.addAttribute("errorUser", errorUser);
            return "CaseReport";
        }
        String pathCurrent = path + file.getOriginalFilename();
        String sqlMessage = UserId + ",'" + leader + "','" + members + "'," + type + ",'" + content + "','" + pathCurrent + "'," + score + ",'" + currentTime() + "'," + score_type;
        jdbcTemplate.update("insert into casereport (userID,leaderName,members,category,reportText,reportPath,singleScore,submitTime,scoreType) values(" + sqlMessage + ")");
        if (saveFile(file, pathCurrent) == false)
            return "failure";
        return "success";
    }

    @RequestMapping("/LeadershipReport")
    public String Leadership(@RequestParam("code") String CODE,
                             @RequestParam("state") String STATE,
                             Model model) {
        String UserId = getUserId(CODE, "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY");
        if (isUser(UserId) == false)
            return "failure";
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
        List<String> errorUser = checkMember(members);
        if (!errorUser.isEmpty()) {
            model.addAttribute("errorUser", errorUser);
            return "LeadershipReport";
        }
        String pathCurrent = path + file.getOriginalFilename();
        String sqlMessage = UserId + ",'" + members + "'," + type + ",'" + content + "','" + pathCurrent + "'," + score + ",'" + currentTime() + "'," + score_type;
        jdbcTemplate.update("insert into leaderreport (userID,members,category,reportText,reportPath,singleScore,submitTime,scoreType) values(" + sqlMessage + ")");
        if (saveFile(file, pathCurrent) == false)
            return "failure";
        return "success";
    }

    @RequestMapping("/RankingList")
    public String RankingList(Model model) {
        String sql = "select userName,s_score from user order by s_score desc";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = jdbcTemplate.queryForList(sql);
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

    //积分排行大致如下，需要增加显示更多信息（照片等）
    @PostMapping(value = "/RankingList")
    public String RankingListPost(@RequestParam("button") String type, Model model) {
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
            list = jdbcTemplate.queryForList(sql);
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

    @RequestMapping("/ReportApproval")
    //@RequestParam("code") String CODE,
    //@RequestParam("state") String STATE,
    public String ReportApproval(
            Model model) {
        //String UserId = getUserId(CODE, "5nFQW5WCF1Gxk7Ht6crCkACsUhqP1DkGk7Fsvg8W67E");
        String UserId = "3";
        if (isUser(UserId) == false)
            return "failure";
        String getGeneralReport = "select reportID,userID,category,reportText,submitTime,userName " +
                "from GeneralReport natural join user where leaderName=(select userName from user where userID=" + UserId + ")";
        String getCaseReport = "select reportID,userID,category,reportText,submitTime,members,singleScore,userName " +
                "from CaseReport natural join user where leaderName=(select userName from user where userID=" + UserId + ")";
        List<Map<String, Object>> generalReport = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> caseReport = new ArrayList<Map<String, Object>>();
        try {
            generalReport = jdbcTemplate.queryForList(getGeneralReport);
        } catch (Exception e) {
            return e.toString();
        }
        try {
            caseReport = jdbcTemplate.queryForList(getCaseReport);
        } catch (Exception e) {
            return e.toString();
        }
        List<GeneralReport> list1 = new ArrayList<GeneralReport>();
        List<CaseReport> list2 = new ArrayList<CaseReport>();
        for (Map<String, Object> map : generalReport) {
            GeneralReport list1_temp = new GeneralReport(map.get("reportID"), map.get("userID"), map.get("userName"), map.get("category"), map.get("reportText"), map.get("submitTime"));
            list1.add(list1_temp);
        }
        for (Map<String, Object> map : caseReport) {
            CaseReport list2_temp = new CaseReport(map.get("reportID"), map.get("userID"), map.get("userName"), map.get("category"), map.get("reportText"), map.get("submitTime"), map.get("members"), map.get("singleScore"));
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
        String updateSql = "";
        reportComment = '"' + reportComment + '"';
        String checkTime = "'" + currentTime() + "'";
        System.out.println(reports2[0]);
        if (!check1.isEmpty()) {
            for (int i = 0; i < reports1.length; i++) {
                updateSql = "update generalreport set ispass=" + reportStatus + " , comment=" + reportComment + ",checkTime=" + checkTime + " where reportID=" + reports1[i];
                jdbcTemplate.update(updateSql);
                //updateSql = "delete from undealedgeneralreport where reportID=" + reports1[i];
                //jdbcTemplate.update(updateSql);
            }
        }
        if (!check2.isEmpty()) {
            for (int i = 0; i < reports2.length; i++) {
                updateSql = "update casereport set ispass=" + reportStatus + " , comment=" + reportComment + ",checkTime=" + checkTime + " where reportID=" + reports2[i];
                jdbcTemplate.update(updateSql);
                //updateSql = "delete from undealedcasereport where reportID=" + reports2[i];
                //jdbcTemplate.update(updateSql);
            }
        }
        return "ReportApproval";
    }

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
