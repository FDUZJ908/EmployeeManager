package EmployeeManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static EmployeeManager.cls.User.userAttrs;
import static EmployeeManager.cls.User.userKeys;

/**
 * Created by lsh on 03/03/2018.
 */
public class SyncService {

    @Autowired
    Server server;

    public Map<Integer, String> syncDepartment() throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = server.getAccessToken(Variable.contactSecret, true);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + token;
        JSONObject jsonRes = httpReq.sendGET(url);
        if (!jsonRes.getString("errmsg").equals("ok") || jsonRes.getInt("errcode") != 0) {
            return null;
        }

        Map<Integer, String> departName = new HashMap<>();
        JSONArray departList = jsonRes.getJSONArray("department");
        int departNum = departList.length();
        for (int i = 0; i < departNum; i++) {
            JSONObject depart = departList.getJSONObject(i);
            int dID = depart.getInt("id");
            String dName = depart.getString("name");
            String sql = "select * from department where dID=" + dID + " and dName='" + dName + "' limit 1";
            List<Map<String, Object>> res = server.jdbcTemplate.queryForList(sql);
            if (res.isEmpty()) {
                server.jdbcTemplate.update("update department set dName='" + dName + "' where dID=" + dID);
            }
            departName.put(dID, dName);
        }
        return departName;
    }

    public void syncUser(Map<Integer, String> departName) throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = server.getAccessToken(Variable.contactSecret, true);
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
        server.jdbcTemplate.update(sql, args);

        for (int i = 0; i < userNum; i++) {
            JSONObject user = userList.getJSONObject(i);
            String userID = user.getString("userid");
            Set<Integer> dIDs_sql = new HashSet<Integer>();
            sql = "select dID from department where userID='" + userID + "'";
            List<Map<String, Object>> res = server.jdbcTemplate.queryForList(sql);
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
                    server.jdbcTemplate.update(sql, args);
                } else dIDs_sql.remove(dID);
            }
            for (int dID : dIDs_sql) {
                sql = "delete from department where userID='" + userID + "' and dID=" + dID;
                server.jdbcTemplate.update(sql);
            }
        }
    }
}
