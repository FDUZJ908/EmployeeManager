package EmployeeManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static EmployeeManager.User.*;

/**
 * Created by lsh on 04/09/2017.
 */
@Component
public class Server {

    static final String corpid = "wx7635106ee1705d6d";
    static final String PASecret = "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY";
    static final String SYNSecret = "eRepbi6SM0Qk6DbHsbO0NqhKWEhL4_CtFLt-UKO_P5k";

    @Autowired
    public JdbcTemplate jdbcTemplate;

    Map<String, AccessToken> tokenList = new HashMap<String, AccessToken>();

    @Value("${web.upload-path}")
    private String path;


    public String getAccessToken(String corpsecret, boolean newToken) {
        int time = (int) (System.currentTimeMillis() / 1000);
        System.out.println("cor1:" + tokenList.containsKey(corpsecret));
        System.out.println("Time" + time);
        if (!newToken && tokenList.containsKey(corpsecret) && time < tokenList.get(corpsecret).expireTime) {
            System.out.println("true");
            return tokenList.get(corpsecret).value;
        }
        HTTPRequest http = new HTTPRequest();
        try {
            JSONObject jsonObject = http.sendGET("https://qyapi.weixin.qq.com/cgi-bin/gettoken?" +
                    "corpid=" + corpid + "&corpsecret=" + corpsecret);
            String value = jsonObject.getString("access_token");
            System.out.println("tokenlsh:" + jsonObject.toString());
            int expireTime = time + jsonObject.getInt("expires_in") / 4 * 3;
            System.out.println("cs:" + corpsecret);
            tokenList.put(corpsecret, new AccessToken(value, expireTime));
            System.out.println("cor2:" + tokenList.containsKey(corpsecret));
            return value;
        } catch (Exception e) {
            return "failure";
        }
    }

    public String getUserId(String code, String corpsecret) {
        String UserId;
        String token = getAccessToken(corpsecret, false);
        System.out.println(token);
        HTTPRequest http = new HTTPRequest();
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + token + "&code=" + code;
            JSONObject jsonObject = http.sendGET(url);
            UserId = jsonObject.getString("UserId");
        } catch (Exception e) {
            return "failure";
        }
        return UserId;
    }

    public boolean isUser(String UserId) {
        String token = getAccessToken(PASecret, false);
        HTTPRequest http = new HTTPRequest();
        int errcode;
        String errmsg;
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token + "&userid=" + UserId;
            JSONObject jsonObject = http.sendGET(url);
            errcode = jsonObject.getInt("errcode");
            errmsg = jsonObject.getString("errmsg");

            /*
            System.out.println("****************************\n");
            System.out.println(UserId);
            System.out.println(jsonObject.toString());
            System.out.println("\n****************************");
            */
        } catch (Exception e) {
            return false;
        }
        return errcode == 0 && errmsg.equals("ok");
    }

    public List<Map<String, Object>> getDepartment(String userId) {

        String dIDSql = "select dID,dName from department where userID=?";
        Object args[] = new Object[]{userId};
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, args);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return department;
    }

    public String getUserName(String userId) {

        String dIDSql = "select userName from user where userID=?";
        Object args[] = new Object[]{userId};
        List<Map<String, Object>> userNameCursor;
        try {
            userNameCursor = jdbcTemplate.queryForList(dIDSql, args);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        String userName = "";
        for (Map<String, Object> map : userNameCursor) {
            userName = map.get("userName").toString();
            break;
        }
        return userName;
    }

    public List<User> getLeader(String UserId, List<Map<String, Object>> department) {
        Object dID = "";
        List<User> users = new ArrayList<User>();
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID from department natural join user  where isLeader=1 and dID=?";
            Object args[] = new Object[]{dID};
            try {
                dLeader = jdbcTemplate.queryForList(leaderSql, args);
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
            memberSql = "select * from user where userName=?";
            Object args[] = new Object[]{member[i]};
            try {
                memberCursor = jdbcTemplate.queryForList(memberSql, args);
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

    /*public void getReports(List<HistoryReport> reports,
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
    }*/

    public List<String> getAllUsers() {
        String sql = "select userName from user";
        List<Map<String, Object>> allUsersCursor;
        try {
            allUsersCursor = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        List<String> allUsers = new ArrayList<String>();
        for (Map<String, Object> map : allUsersCursor) {
            String user_temp = map.get("userName").toString();
            allUsers.add(user_temp);
        }
        return allUsers;
    }

    public List<DepartmentLeader> getUserDepartmentLeader(String userId) {
        List<DepartmentLeader> DLeader = new ArrayList<DepartmentLeader>();
        String dIDSql = "select dID,dName from department where userID=?";
        Object args1[] = new Object[]{userId};
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, args1);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

        Object dID = "";
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID,dName from department natural join user  where isLeader=1 and dID=?";
            Object args2[] = new Object[]{dID};
            try {
                dLeader = jdbcTemplate.queryForList(leaderSql, args2);
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
        return DLeader;
    }

    public Map<Integer, String> syncDepartment() throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = getAccessToken(SYNSecret, true);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + token;
        JSONObject jsonRes = httpReq.sendGET(url);
        if (!jsonRes.getString("errmsg").equals("ok") || jsonRes.getInt("errcode") != 0) {
            return null;
        }

        Map<Integer, String> departName = new HashMap<Integer, String>();
        JSONArray departList = jsonRes.getJSONArray("department");
        int departNum = departList.length();
        for (int i = 0; i < departNum; i++) {
            JSONObject depart = departList.getJSONObject(i);
            int dID = depart.getInt("id");
            String dName = depart.getString("name");
            String sql = "select * from department where dID=" + dID + " and dName='" + dName + "' limit 1";
            List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
            if (res.isEmpty()) {
                jdbcTemplate.update("update department set dName='" + dName + "' where dID=" + dID);
            }
            departName.put(dID, dName);
        }
        return departName;
    }

    public void syncUser(Map<Integer, String> departName) throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = getAccessToken(SYNSecret, true);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + token + "&department_id=1&fetch_child=1";
        JSONObject jsonRes = httpReq.sendGET(url);
        if (!jsonRes.getString("errmsg").equals("ok") || jsonRes.getInt("errcode") != 0) {
            return;
        }

        JSONArray userList = jsonRes.getJSONArray("userlist");
        int userNum = userList.length(), keyNum = userKeys.length, argc = 0;
        Object[] args = new Object[userNum * keyNum];
        String values = "values";
        for (int i = 0; i < userNum; i++) {
            JSONObject user = userList.getJSONObject(i);
            if (i > 0) values += ",(";
            else values += "(";
            for (int j = 0; j < keyNum; j++) {
                if (!user.has(userKeys[j])) args[argc++] = null;
                else args[argc++] = user.get(userKeys[j]);
                if (j > 0) values += ",?";
                else values += "?";
            }
            values += ")";
        }
        String sql = "insert into user(";
        String update = " on duplicate key update ";
        for (int i = 0; i < keyNum; i++) {
            if (i > 0) {
                sql += ",";
                update += ",";
            }
            sql += userAttrs[i];
            update += userAttrs[i] + "=values(" + userAttrs[i] + ")";
        }
        sql += ") " + values + update;
        jdbcTemplate.update(sql, args);

        for (int i = 0; i < userNum; i++) {
            JSONObject user = userList.getJSONObject(i);
            String userID = user.getString("userid");
            Set<Integer> dIDs_sql = new HashSet<Integer>();
            sql = "select dID from department where userID='" + userID + "'";
            List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> map : res) {
                dIDs_sql.add(Integer.parseInt(map.get("dID").toString()));
            }
            Set<Integer> dIDs_json = new HashSet<Integer>();
            JSONArray departs = user.getJSONArray("department");
            int departNum = departs.length();
            for (int j = 0; j < departNum; j++) {
                dIDs_json.add(departs.getInt(j));
            }
            for (int dID : dIDs_json) {
                if (!dIDs_sql.contains(dID)) {
                    sql = "insert into department(dID,userID,dName) values(?,?,?)";
                    args = new Object[]{dID, userID, departName.get(dID)};
                    jdbcTemplate.update(sql, args);
                } else dIDs_sql.remove(dID);
            }
            for (int dID : dIDs_sql) {
                sql = "delete from department where userID='" + userID + "' and dID=" + dID;
                jdbcTemplate.update(sql);
            }
        }
    }





























































    public String name2id(String names) {
        String IDs = "";
        String[] namelist = names.split(",");

        List<Map<String, Object>> memberCursor = new ArrayList<Map<String, Object>>();
        String memberSql = "";

        for (int i = 0; i < namelist.length; i++) {
            memberSql = "select userID from user where userName=?";
            Object args[] = new Object[]{namelist[i]};
            try {
                memberCursor = jdbcTemplate.queryForList(memberSql, args);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            if (!memberCursor.isEmpty()) {
                for (Map<String, Object> map : memberCursor) {
                    IDs = IDs + map.get("userID").toString() + "|";
                }
            } else {
                System.out.println("成员不存在");
            }
        }
        return IDs;
    }


    public void postMessageToUser(String members, String text) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        String users = name2id(members);

        jsonObject.put("touser", users);
    }


}
