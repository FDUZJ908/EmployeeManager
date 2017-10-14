package EmployeeManager;

import EmployeeManager.cls.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import sun.misc.BASE64Decoder;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import static EmployeeManager.cls.User.userAttrs;
import static EmployeeManager.cls.User.userKeys;

@Component
public class Server {

    static final String corpid = "wx7635106ee1705d6d";
    static final String submitSecret = "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY";
    static final String contactSecret = "eRepbi6SM0Qk6DbHsbO0NqhKWEhL4_CtFLt-UKO_P5k";
    static final String reportSecret = "x0VeHuzpagYuJmrymzJayHmN3vrzgtcISA2Q_8qhLG0";
    /*static final String approvalSecret = "5nFQW5WCF1Gxk7Ht6crCkACsUhqP1DkGk7Fsvg8W67E";
    static final String reportSecret = "xVBKh9GKuDGd_nJp8TBRzWzWBHkIVDFPjxewNKhBEzA";
    static final int approvalAgentID = 1000004;
    static final int reportAgentID = 1000005;*/
    static final int reportAgentID = 1000017;


    static final Map<Integer, String> corpsecret = new HashMap<Integer, String>() {{
        put(reportAgentID, reportSecret);
        //put(approvalAgentID, approvalSecret);
        //put(reportAgentID, reportSecret);
    }};

    @Autowired
    public JdbcTemplate jdbcTemplate;

    Map<String, AccessToken> tokenList = new HashMap<String, AccessToken>();

    @Value("${web.upload-path}")
    private String path;

    public String getAccessToken(String corpsecret, boolean newToken) {
        int time = (int) (System.currentTimeMillis() / 1000);

        if (!newToken && tokenList.containsKey(corpsecret) && time < tokenList.get(corpsecret).expireTime) {
            return tokenList.get(corpsecret).value;
        }
        HTTPRequest http = new HTTPRequest();
        try {
            JSONObject jsonObject = http.sendGET("https://qyapi.weixin.qq.com/cgi-bin/gettoken?" +
                    "corpid=" + corpid + "&corpsecret=" + corpsecret);
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
        String sql = "select * from user where userID=? limit 1";
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, new Object[]{UserId});
        return res.size() > 0;
    }

    /*
    public boolean isUser(String UserId) {
        String token = getAccessToken(submitSecret, false);
        HTTPRequest http = new HTTPRequest();
        int errcode;
        String errmsg;
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token + "&userid=" + UserId;
            JSONObject jsonObject = http.sendGET(url);
            errcode = jsonObject.getInt("errcode");
            errmsg = jsonObject.getString("errmsg");
        } catch (Exception e) {
            return false;
        }
        return errcode == 0 && errmsg.equals("ok");
    }
    */

    public List<Map<String, Object>> getDepartment(String userId) {
        String dIDSql = "select dID,dName from department where userID=?";
        Object args[] = new Object[]{userId};
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return department;
    }

    public String getUserName(String userId) {

        String dIDSql = "select userName from user where userID=? limit 1";
        Object args[] = new Object[]{userId};
        List<Map<String, Object>> userNameCursor;
        try {
            userNameCursor = jdbcTemplate.queryForList(dIDSql, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        String userName = "";
        for (Map<String, Object> map : userNameCursor) {
            userName = map.get("userName").toString();
            break;
        }
        return userName;
    }

    public int getUserPrivilege(String userId) {

        String dIDSql = "select privilege from user where userID=? limit 1";
        Object args[] = new Object[]{userId};
        List<Map<String, Object>> userNameCursor;
        try {
            userNameCursor = jdbcTemplate.queryForList(dIDSql, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
        int userPrivilege = 0;
        for (Map<String, Object> map : userNameCursor) {
            userPrivilege = Integer.parseInt(map.get("privilege").toString());
        }
        return userPrivilege;
    }

    public int getLeaderScoreLimit (int userPrivilege) {
        String scoreLimitSql = "select leaderScoreLimit from privilege where privilege = ?";
        Object args[] = new Object[]{userPrivilege};
        List<Map<String, Object>> scoreLimitCursor;
        try {
            scoreLimitCursor = jdbcTemplate.queryForList(scoreLimitSql, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
        int scoreLimit = 0;
        for (Map<String, Object> map : scoreLimitCursor) {
            scoreLimit = Integer.parseInt(map.get("leaderScoreLimit").toString());
        }
        return scoreLimit;
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
                users.add(new User(leader.get("userName"), leader.get("userID")));
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
            pathCurrent = path + "/" + pathCurrent;
            File f = new File(pathCurrent);
            try {
                if (!f.exists())
                    f.createNewFile();
                file.transferTo(f);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
            try {
                dir.mkdir();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }

    public Map<String, Object> getUndealedGeneralReport(String reportID) {
        String sql = "SELECT * FROM undealedGeneralReport WHERE reportID=" + reportID + " LIMIT 1";
        List<Map<String, Object>> generalReport;
        try {
            generalReport = jdbcTemplate.queryForList(sql);
            return generalReport.get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getUndealedCaseReport(String reportID) {
        String sql = "SELECT * FROM undealedCaseReport WHERE reportID=" + reportID + " LIMIT 1";
        List<Map<String, Object>> caseReport;
        try {
            caseReport = jdbcTemplate.queryForList(sql);
            return caseReport.get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getLatestGeneralReport(String userID) {
        String sql = "select submitTime from generalReport where userID=" + "'" + userID + "'" + "order by submitTime desc";
        List<Map<String, Object>> generalReport;
        String latestGeneralReport = "";
        try {
            generalReport = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        for (Map<String, Object> map : generalReport) {
            latestGeneralReport = map.get("submitTime").toString();
            break;
        }
        return latestGeneralReport;
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
            System.out.println(e.getMessage());
            return null;
        }
        List<String> allUsers = new ArrayList<String>();
        for (Map<String, Object> map : allUsersCursor) {
            String user_temp = map.get("userName").toString();
            allUsers.add(user_temp);
        }
        return allUsers;
    }

    public List<String> getAllDepartmentUsers(String userId, int userPrivilege) {
        List<String> AllUsers = new ArrayList<String>();
        String dIDSql = "select dID from department where userID=? order by dID asc";
        Object args1[] = new Object[]{userId};
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, args1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        Object dID = "";
        List<Map<String, Object>> allUsers;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName , privilege from department natural join user  where dID=?  order by convert(userName using gbk) asc";
            Object args2[] = new Object[]{dID};
            try {
                allUsers = jdbcTemplate.queryForList(leaderSql, args2);
            } catch (Exception e) {
                return null;
            }
            for (Map<String, Object> user : allUsers) {
                String User = user.get("userName").toString();
                if(!AllUsers.contains(User) && Integer.parseInt(user.get("privilege").toString()) < userPrivilege)
                        AllUsers.add(User);
            }
        }
        return AllUsers;
    }

    public List<DepartmentLeader> getUserDepartmentLeader(String userId) {
        List<DepartmentLeader> DLeader = new ArrayList<DepartmentLeader>();
        String dIDSql = "select dID,dName from department where userID=? order by convert(dName using gbk) asc";
        Object args1[] = new Object[]{userId};
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, args1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        Object dID = "";
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID,dName from department natural join user  where isLeader=1 and dID=? order by convert(userName using gbk) asc";
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



    public List<String> getLeaderInCase(String userId) {
        List<String> DLeader = new ArrayList<String>();
        int userPrivilege = getUserPrivilege(userId);
        String dIDSql = "select userName from user where privilege > ?   order by  convert(userName using gbk) asc";
        Object args1[] = new Object[]{userPrivilege};
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, args1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

            for (Map<String, Object> leader : department) {
                String DLeader_temp = leader.get("userName").toString();
                DLeader.add(DLeader_temp);
            }
        return DLeader;
    }


    public Map<Integer, String> syncDepartment() throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = getAccessToken(contactSecret, true);
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
        String token = getAccessToken(contactSecret, true);
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
                else {
                    Object argv = user.get(userKeys[j]);
                    if (argv.getClass() == String.class) argv = argv.toString().replace(" ", "");
                    args[argc++] = argv;
                }
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

    public void award(String userid, int score) {
        String updatesql = "UPDATE user set s_score=s_score + " + score + " where userID=?";
        Object args[] = new Object[]{userid};
        try {
            jdbcTemplate.update(updatesql, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                System.out.println(e.getMessage());
            }
            if (!memberCursor.isEmpty()) {
                for (Map<String, Object> map : memberCursor) {
                    IDs = IDs + map.get("userID").toString() + "|";
                }
            } else {
                System.out.println("成员不存在");
            }
        }
        if (IDs.length() > 0)
            IDs = IDs.substring(0, IDs.length() - 1); //****
        return IDs;
    }

    public void sendMessage(String members, String content, boolean byName, int agentID) throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = getAccessToken(corpsecret.get(agentID), false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;

        String userIDs = byName ? name2id(members) : members;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", userIDs);
        jsonObject.put("msgtype", "text");
        jsonObject.put("agentid", agentID);
        JSONObject text = new JSONObject();
        text.put("content", content);
        jsonObject.put("text", text);

        jsonObject = httpReq.sendPost(url, jsonObject);
    }


    public Boolean base64ToImg (String srcURLFile ,String avatarURL) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(srcURLFile);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成图片
                avatarURL = path + "/" + "CaiYu.png";

            OutputStream out = new FileOutputStream("CaiYu.png");
            out.write(b);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean imgSub (String avatarURL,String avatarURLSub,String suffix, int x , int y , int w , int h) {
        try {
            FileInputStream is = null ;
            ImageInputStream iis =null ;
            avatarURL = path + "/" + avatarURL;
            avatarURLSub = path + "/" + avatarURLSub;
            is = new FileInputStream(avatarURL);

            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);
            ImageReader reader = it.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(is);

            reader.setInput(iis,true) ;


            ImageReadParam param = reader.getDefaultReadParam();


            Rectangle rect = new Rectangle(x,y,w,h);


            param.setSourceRegion(rect);

            BufferedImage bi = reader.read(0,param);

            ImageIO.write(bi, suffix, new File(avatarURLSub));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
