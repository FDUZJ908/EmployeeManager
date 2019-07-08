package EmployeeManager;

import EmployeeManager.admin.model.Privilege;
import EmployeeManager.cls.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import static EmployeeManager.Util.getRepeatQMark;
import static EmployeeManager.Util.getTimestamp;

@Component
public class Server {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public JdbcTemplate jdbcTemplate;

    Map<String, AccessToken> tokenList = new HashMap<String, AccessToken>();

    @Value("${web.upload-path}")
    private String path;


    public int insertMap(Map<String, Object> map, String table) throws Exception {
        StringBuffer cols = new StringBuffer();
        Object[] args = new Object[map.size()];
        int m = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (m > 0) cols.append(',');
            cols.append(entry.getKey());
            args[m++] = entry.getValue();
        }
        String sql = "INSERT INTO " + table + " ( " + cols + " ) VALUES" + getRepeatQMark(1, m);
        return jdbcTemplate.update(sql, args);
    }

    public String getAccessToken(String corpsecret, boolean newToken) {
        int time = getTimestamp();

        if (!newToken && tokenList.containsKey(corpsecret) && time < tokenList.get(corpsecret).expireTime) {
            return tokenList.get(corpsecret).value;
        }
        HTTPRequest http = new HTTPRequest();
        try {
            JSONObject jsonObject = http.sendGET("https://qyapi.weixin.qq.com/cgi-bin/gettoken?" +
                    "corpid=" + Variable.corpid + "&corpsecret=" + corpsecret);
            String value = jsonObject.getString("access_token");
            int expireTime = time + jsonObject.getInt("expires_in") / 4 * 3;
            tokenList.put(corpsecret, new AccessToken(value, expireTime));
            return value;
        } catch (Exception e) {
            return "failure";
        }
    }

    public boolean updateUser(String userID, String corpsecret) {
        String token = getAccessToken(corpsecret, false);
        HTTPRequest http = new HTTPRequest();
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token + "&userid=" + userID;
            JSONObject jsonObject = http.sendGET(url);
            userID = jsonObject.getString("userid");
            String userName = jsonObject.getString("name");
            String gender = jsonObject.getString("gender");
            String tel = jsonObject.getString("mobile");
            String email = jsonObject.getString("email");
            String sql = "UPDATE user SET userID=?,gender=?,tel=?,email=? WHERE userName=?";
            int ret = jdbcTemplate.update(sql, userID, gender, tel, email, userName);
            return ret > 0;
        } catch (Exception e) {
            return false;
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

    public boolean isUser(String userID) {
        if (userID == "failure") return false;
        String sql = "select * from user where userID=? limit 1";
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, userID);
        if (res.size() == 0) return updateUser(userID, Variable.submitSecret);
        return true;
    }

    public String id2name(String ids, String delimiter) {
        if (ids.length() == 0) return "";
        String[] argvs = ids.split(delimiter);
        String sql = "SELECT userName FROM user WHERE userID IN " + getRepeatQMark(1, argvs.length);
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, argvs);
        if (res == null) return "";

        StringBuffer names = new StringBuffer();
        int i = 0;
        for (Map<String, Object> map : res) {
            if (i > 0) names.append("," + map.get("userName").toString());
            else names.append(map.get("userName").toString());
            i++;
        }
        return names.toString();
    }

    public String name2id(String names, String delimiter) {
        if (names.length() == 0) return "";
        String[] argvs = names.split(",");
        String sql = "SELECT userID FROM user WHERE userName IN " + getRepeatQMark(1, argvs.length);
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, argvs);
        if (res == null) return "";

        StringBuffer ids = new StringBuffer();
        int i = 0;
        for (Map<String, Object> map : res) {
            if (i > 0) ids.append(delimiter + map.get("userID").toString());
            else ids.append(map.get("userID").toString());
            i++;
        }
        return ids.toString();
    }

    public String id2name(String ids) {
        return id2name(ids, ",");
    }

    public String name2id(String names) {
        return name2id(names, ",");
    }

    public String getUserName(String userId) {
        String dIDSql = "select userName from user where userID=? limit 1";
        List<Map<String, Object>> userNameCursor;
        try {
            userNameCursor = jdbcTemplate.queryForList(dIDSql, userId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        String userName = "";
        for (Map<String, Object> map : userNameCursor) {
            userName = map.get("userName").toString();
            break;
        }
        return userName;
    }

    public List<Map<String, Object>> getDepartment(String userId) {
        String dIDSql = "select dID,dName from department where userID=?";
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, userId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        return department;
    }

    public List<Privilege> getAllPriviledges() {
        return jdbcTemplate.query("SELECT * FROM privilege", BeanPropertyRowMapper.newInstance(Privilege.class));
    }

    public String getUsersByPriviledges(Integer[] privileges) {
        if (privileges == null || privileges.length == 0) return "";
        String sql = "SELECT userID FROM user WHERE privilege IN " + getRepeatQMark(1, privileges.length);
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, privileges);
        StringBuffer userIDs = new StringBuffer();
        int i = 0;
        for (Map<String, Object> user : users) {
            if (i > 0) userIDs.append("|");
            userIDs.append(user.get("userID").toString());
            i++;
        }
        return userIDs.toString();
    }

    public int getUserPrivilege(String userId) {
        String dIDSql = "select privilege from user where userID=? limit 1";
        List<Map<String, Object>> userNameCursor;
        try {
            userNameCursor = jdbcTemplate.queryForList(dIDSql, userId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0;
        }
        int userPrivilege = 0;
        for (Map<String, Object> map : userNameCursor) {
            userPrivilege = Integer.parseInt(map.get("privilege").toString());
        }
        return userPrivilege;
    }

    public int getIntSysVar(String varName) {
        int sysVar = 0;
        String sysVarSql = "select value from sysVar where varName= ? limit 1";
        List<Map<String, Object>> sysVarCursor;
        try {
            sysVarCursor = jdbcTemplate.queryForList(sysVarSql, varName);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0;
        }
        for (Map<String, Object> map : sysVarCursor) {
            sysVar = Integer.parseInt(map.get("value").toString());
        }
        return sysVar;
    }

    public String getStringSysVar(String varName) {
        String sysVar = "";
        String sysVarSql = "select string from sysVar where varName= ? limit 1";
        List<Map<String, Object>> sysVarCursor;
        try {
            sysVarCursor = jdbcTemplate.queryForList(sysVarSql, varName);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return "";
        }
        for (Map<String, Object> map : sysVarCursor) {
            sysVar = map.get("string").toString();
        }
        return sysVar;
    }

    public int getLeaderScoreLimit(int userPrivilege) {
        String scoreLimitSql = "select leaderScoreLimit from privilege where privilege = ?";
        List<Map<String, Object>> scoreLimitCursor;
        try {
            scoreLimitCursor = jdbcTemplate.queryForList(scoreLimitSql, userPrivilege);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0;
        }
        int scoreLimit = 0;
        for (Map<String, Object> map : scoreLimitCursor) {
            scoreLimit = Integer.parseInt(map.get("leaderScoreLimit").toString());
        }
        return scoreLimit;
    }

    public int getLeaderPostLimit(String userID) {
        String sql = "select leaderPostLimit from privilege,user where userID=? and user.privilege = privilege.privilege";
        List<Map<String, Object>> scoreLimitCursor;
        try {
            scoreLimitCursor = jdbcTemplate.queryForList(sql, userID);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0;
        }
        int postLimit = 0;
        for (Map<String, Object> map : scoreLimitCursor) {
            postLimit = Integer.parseInt(map.get("leaderPostLimit").toString());
        }
        return postLimit;
    }

    public List<User> getLeader(String UserId, List<Map<String, Object>> department) {
        Object dID = "";
        List<User> users = new ArrayList<User>();
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID from department natural join user  where isLeader=1 and dID=?";
            try {
                dLeader = jdbcTemplate.queryForList(leaderSql, dID);
            } catch (Exception e) {
                return null;
            }
            for (Map<String, Object> leader : dLeader) {
                users.add(new User(leader.get("userName"), leader.get("userID")));
            }
        }
        return users;
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
                logger.info(e.getMessage());
                return false;
            } catch (IOException e) {
                logger.info(e.getMessage());
                return false;
            }
        }
        return true;
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
                logger.info(e.getMessage());
            }
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
                logger.info(e.getMessage());
            }
            if (memberCursor.isEmpty()) {
                errorUser.add(member[i]);
            }
        }
        return errorUser;
    }


    public Map<String, Object> getUndealedGeneralReport(String reportID) {
        String sql = "SELECT * FROM undealedGeneralReport WHERE reportID=" + reportID + " LIMIT 1";
        List<Map<String, Object>> generalReport;
        try {
            generalReport = jdbcTemplate.queryForList(sql);
            return generalReport.get(0);
        } catch (Exception e) {
            logger.info(e.getMessage());
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
            logger.info(e.getMessage());
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
            logger.info(e.getMessage());
            return null;
        }
        for (Map<String, Object> map : generalReport) {
            latestGeneralReport = map.get("submitTime").toString();
            break;
        }
        return latestGeneralReport;
    }

    /*
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
    */

    public List<String> getAllUsers() {
        List<Map<String, Object>> list;
        try {
            list = jdbcTemplate.queryForList("select userName from user");
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        List<String> nameList = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            nameList.add(map.get("userName").toString());
        }
        return nameList;
    }

    public List<String> getAllDepartmentUsers(String userId, int userPrivilege) {
        List<String> AllUsers = new ArrayList<String>();
        String dIDSql = "select dID from department where userID=? and isLeader=1 order by dID asc";
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, userId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

        List<Map<String, Object>> allUsers;
        String leaderSql;
        for (Map<String, Object> map : department) {
            Object dID[] = {map.get("dID").toString(), Integer.toString(userPrivilege)};
            leaderSql = "select distinct userName from department natural join user  where dID=? and privilege<? order by userName";
            try {
                allUsers = jdbcTemplate.queryForList(leaderSql, dID);
            } catch (Exception e) {
                logger.info(e.getMessage());
                return null;
            }
            for (Map<String, Object> user : allUsers) {
                if (!AllUsers.contains(user.get("userName").toString()))
                    AllUsers.add(user.get("userName").toString());
            }
        }
        return AllUsers;
    }

    public List<DepartmentLeader> getUserDepartmentLeader(String userId) {
        int privilege = getUserPrivilege(userId);
        List<DepartmentLeader> DLeader = new ArrayList<DepartmentLeader>();
        String dIDSql = "select dID,dName from department where userID=? order by dName";
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, userId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

        Object dID = "";
        List<Map<String, Object>> dLeader;
        String leaderSql;
        for (Map<String, Object> map : department) {
            dID = map.get("dID");
            leaderSql = "select userName,userID,dName from department natural join user  " +
                    "where isLeader=1 and dID=? and privilege > ? order by convert(userName using gbk) asc";
            try {
                dLeader = jdbcTemplate.queryForList(leaderSql, dID, privilege);
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
        int caseReportCheckLimit = getIntSysVar("caseReportCheckLimit");
        caseReportCheckLimit = caseReportCheckLimit - 1;
        int limitPrivilege = 0;
        if (userPrivilege > caseReportCheckLimit)
            limitPrivilege = userPrivilege;
        else
            limitPrivilege = caseReportCheckLimit;
        String dIDSql = "select userName from user where privilege > ? order by convert(userName using gbk) asc";
        List<Map<String, Object>> department;
        try {
            department = jdbcTemplate.queryForList(dIDSql, limitPrivilege);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

        for (Map<String, Object> leader : department) {
            String DLeader_temp = leader.get("userName").toString();
            DLeader.add(DLeader_temp);
        }
        return DLeader;
    }

    public int award(String users, int score) throws Exception {
        if (users.length() == 0)
            return 0;
        String[] userids = users.split(",");
        String sql = "UPDATE user SET s_score=s_score + " + score + " WHERE userID IN " + getRepeatQMark(1, userids.length);
        return jdbcTemplate.update(sql, userids);
    }

    public QRCode getQRCode(String userID) {
        String sql = "SELECT * FROM QRCode WHERE s_time<=NOW() AND NOW()<e_time AND managers LIKE '%" + userID + "%' LIMIT 1";
        try {
            QRCode qrCode = jdbcTemplate.queryForObject(sql, new Mapper<QRCode>(QRCode.class));
            return qrCode;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public QRCode getQRCode(int QRID) {
        String sql = "SELECT * FROM QRCode WHERE QRID=" + String.valueOf(QRID);
        try {
            QRCode qrCode = jdbcTemplate.queryForObject(sql, new Mapper<QRCode>(QRCode.class));
            return qrCode;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /*
    public void updateQRCodeCheckins(int QRID, String userID) {
        String sql = "UPDATE QRCode SET checkins = ( " +
                " CASE " +
                " WHEN checkins = '' OR checkins IS NULL THEN ? " +
                " ELSE concat(checkins, ?) " +
                " END " +
                " ) WHERE QRID=?";
        jdbcTemplate.update(sql, userID, "," + userID, QRID);
    }
    */

    public int updateQRCodeCheckins(int QRID, String userID) {
        try {
            String sql = "INSERT INTO QRCheckin(QRID,userID,checkTime) VALUES(?,?,NOW())";
            jdbcTemplate.update(sql, QRID, userID);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

/*
    public synchronized QRCode getQRCode(String userID) {
        String time = currentTime();
        int QRID = -1;
        Set<Integer> keys = Variable.QRCodes.keySet();
        for (int key : keys) {
            QRCode qrCode = Variable.QRCodes.get(key);
            if (time.compareTo(qrCode.e_time) > 0) Variable.QRCodes.remove(key);
            else if (qrCode.managers.contains(userID)) {
                QRID = key;
                break;
            }
        }
        if (QRID != -1) return Variable.QRCodes.get(QRID);
        String sql = "SELECT * FROM QRCode WHERE s_time<=NOW() AND NOW()<e_time AND managers LIKE '%" + userID + "%' LIMIT 1";
        try {
            QRCode qrCode = jdbcTemplate.queryForObject(sql, new Mapper<QRCode>(QRCode.class));
            Variable.QRCodes.put(qrCode.QRID, qrCode);
            return qrCode;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
*/

    public void sendMessage(String members, String content, boolean byName, int agentID) throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = getAccessToken(Variable.corpsecret.get(agentID), false);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;

        String userIDs = byName ? name2id(members, "|") : members;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", userIDs);
        jsonObject.put("msgtype", "text");
        jsonObject.put("agentid", agentID);
        JSONObject text = new JSONObject();
        text.put("content", content);
        jsonObject.put("text", text);

        jsonObject = httpReq.sendPost(url, jsonObject);
    }


    public Boolean base64ToImg(String srcURLFile, String avatarURL) {
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
            avatarURL = path + "/" + avatarURL;
            File file = new File(avatarURL);

            if (file.exists()) {
                file.delete();
            }

            OutputStream out = new FileOutputStream(avatarURL);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return true;
    }

    public Boolean imgSub(String avatarURL, String avatarURLSub, String suffix, int x, int y, int w, int h) {

        avatarURL = path + "/" + avatarURL;
        avatarURLSub = path + "/" + avatarURLSub;

        try {
            FileInputStream is = null;
            ImageInputStream iis = null;

            File file = new File(avatarURLSub);

            if (file.exists()) {
                file.delete();
            }

            is = new FileInputStream(avatarURL);

            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);
            ImageReader reader = it.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, w, h);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, suffix, new File(avatarURLSub));
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public List<ReportType> getReportType() {
        List<ReportType> reportType = new ArrayList<ReportType>();
        List<Map<String, Object>> reportTypeCursor;
        String reportTypeSql = "select typeName , typeValue from reportType";
        try {
            reportTypeCursor = jdbcTemplate.queryForList(reportTypeSql);
        } catch (Exception e) {
            return null;
        }

        for (Map<String, Object> map : reportTypeCursor) {
            ReportType reportType_temp = new ReportType(map.get("typeName").toString(), map.get("typeValue").toString());
            reportType.add(reportType_temp);
        }
        return reportType;
    }

    public int getTypeValue(String typeName) {
        Map<String, Object> res;
        String sql = "select typeValue from reportType where typeName=? LIMIT 1";
        Object args[] = new Object[]{typeName};
        try {
            res = jdbcTemplate.queryForMap(sql, args);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0;
        }
        return Integer.parseInt(res.get("typeValue").toString());
    }

    public void updateIdScoreMap(String ids, Integer score, Map<String, Integer> IdScore) {
        for (String id : ids.split(",")) {
            if (id.equals("")) continue;
            Integer curScore = IdScore.get(id);
            curScore += score;
            IdScore.put(id, curScore);
        }
    }
}
