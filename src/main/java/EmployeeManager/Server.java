package EmployeeManager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lsh on 04/09/2017.
 */
@Component
public class Server {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Value("${web.upload-path}")
    private String path;

    static final String PASecret = "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY";
    Map<String, AccessToken> tokenList = new HashMap<String, AccessToken>();

    public String getAccessToken(String corpsecret, boolean newToken) {
        int time = (int) (System.currentTimeMillis() / 1000);
        if (!newToken && tokenList.containsKey(corpsecret) && tokenList.get(corpsecret).expireTime < time) {
            return tokenList.get(corpsecret).value;
        }
        HttpsGet http = new HttpsGet();
        try {
            JSONObject jsonObject = http.sendGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?" +
                    "corpid=wx7635106ee1705d6d&corpsecret=" + corpsecret);
            String value = jsonObject.getString("access_token");
            int expireTime = time + jsonObject.getInt("expires_in") / 4 * 3;
            tokenList.put(corpsecret, new AccessToken(value, expireTime));
            return value;
        } catch (Exception e) {
            return "failure";
        }
    }

    public String getUserId(String code, String corpsecret) {
        String UserId;
        String token = getAccessToken(corpsecret, false);
        HttpsGet http = new HttpsGet();
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + token + "&code=" + code;
            JSONObject jsonObject = http.sendGet(url);
            UserId = jsonObject.getString("UserId");
        } catch (Exception e) {
            return "failure";
        }
        return UserId;
    }

    public boolean isUser(String UserId) {
        String token = getAccessToken(PASecret, false);
        HttpsGet http = new HttpsGet();
        int errcode;
        String errmsg;
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token + "&userid=" + UserId;
            JSONObject jsonObject = http.sendGet(url);
            errcode = jsonObject.getInt("errcode");
            errmsg = jsonObject.getString("errmsg");
        } catch (Exception e) {
            return false;
        }
        return errcode == 0 && errmsg.equals("ok");
    }

    public List<Map<String, Object>> getDepartment(String UserId) {

        String dIDSql = "select dID,dName from department where userID=" + UserId;

        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql);
        } catch (Exception e) {
            System.out.println(e.toString());
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
            leaderSql = "select userName,userID from department natural join user  where isLeader=1 and dID=\"" +
                    dID + '"';
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

    public String currentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    public boolean saveFile(MultipartFile file, String pathCurrent) {
        if (!file.isEmpty()) {

            File f = new File(pathCurrent);
            try {
                if (!f.exists())
                    f.createNewFile();
                file.transferTo(f);
            } catch (FileNotFoundException e) {
                System.out.println(e.toString());
                return false;
            } catch (IOException e) {
                System.out.println(e.toString());
                return false;
            }
        }
        return true;
    }

    public List<String> checkMember(String members) {
        //分解队伍成员
        String[] member = members.split(",");
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

    public String currentFileName(String currentTime, String originalFileName) {
        return currentTime.replace(" ", "-").replace(":", "-") + "_" +
                originalFileName.replace(" ", "%20");
    }

    public void mkDir(String UserId) {
        File dir = new File(path + UserId);
        if (!dir.exists())
            dir.mkdir();
    }

    public List<Map<String, Object>> getUndealedGeneralReport(String reportID) {
        String sql = "SELECT * FROM undealedgeneralreport WHERE reportID=" + reportID;
        List<Map<String, Object>> generalReport;
        try {
            generalReport = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return generalReport;
    }

    public List<Map<String, Object>> getUndealedCaseReport(String reportID) {
        String sql = "SELECT * FROM undealedcasereport WHERE reportID=" + reportID;
        List<Map<String, Object>> caseReport;
        try {
            caseReport = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return caseReport;
    }

    public void getReports(List<HistoryReport> reports,
                           List<Map<String, Object>> listCaseReport,
                           String UserID,
                           String UserName) {
        for (Map<String, Object> map : listCaseReport) {
            HistoryReport report_temp = new HistoryReport(UserID, UserName, map.get("submitTime"), map.get("category"),
                    map.get("reportText"), map.get("isPass"), map.get("scoreType"), map.get("comment"), map.get("leaderName"));
            if (String.valueOf(map.get("scoreType")) == "true")
                report_temp.setScore(String.valueOf(Integer.parseInt(String.valueOf(map.get("singleScore")))));
            else
                report_temp.setScore(String.valueOf(-Integer.parseInt(String.valueOf(map.get("singleScore")))));
            reports.add(report_temp);
        }
    }

    public List<String> getAllUsers(){
        String sql = "select userName from user";
        List<Map<String,Object>> allUsersCursor;
        try {
            allUsersCursor = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        List<String> allUsers = new ArrayList<String>();
        for(Map<String,Object>map: allUsersCursor ){
            String user_temp = new String(map.get("userName").toString());
            allUsers.add(user_temp);
        }
        return allUsers;
    }

    public List<DepartmentLeader> getUserDepartmentLeader(String UserId) {
        List<DepartmentLeader> DLeader = new ArrayList<DepartmentLeader>();
        String dIDSql = "select dID,dName from department where userID=" + UserId;
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

        Object dID = "";
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID,dName from department natural join user  where isLeader=1 and dID=\"" +
                    dID + '"';
            try {
                dLeader = jdbcTemplate.queryForList(leaderSql);
            } catch (Exception e) {
                return null;
            }
            for (Map<String, Object> leader : dLeader) {
                DepartmentLeader DLeader_temp = new DepartmentLeader(
                        leader.get("dName"),
                        leader.get("userName"),
                        leader.get("userID"));
                DLeader.add(DLeader_temp);
            }
        }


        return  DLeader;
    }
}
