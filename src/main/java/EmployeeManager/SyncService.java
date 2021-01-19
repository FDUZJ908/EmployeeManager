package EmployeeManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static EmployeeManager.cls.User.userAttrs;
import static EmployeeManager.cls.User.userKeys;

/**
 * Created by lsh on 03/03/2018.
 */
@Component
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

        List<Map<String, Object>> departs = new ArrayList<>();
        Map<Integer, String> department = new TreeMap<>();
        JSONArray departList = jsonRes.getJSONArray("department");
        int departNum = departList.length();
        for (int i = 0; i < departNum; i++) {
            JSONObject depart = departList.getJSONObject(i);
            Map<String, Object> departMap = new TreeMap<>();
            int dID = depart.getInt("id");
            String dName = depart.getString("name");
            departMap.put("dID", dID);
            departMap.put("dName", dName);
            departs.add(departMap);
            department.put(dID, dName);
        }

        departs.sort(new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int a = (int) o1.get("dID"), b = (int) o2.get("dID");
                if (a == b) return 0;
                if (a < b) return -1;
                else return 1;
            }
        });

        // server.jdbcTemplate.update("DELETE FROM ministry"); //foreign key cascade with department table
        server.insertMapList(departs, "ministry", true);
        return department;
    }

    public void syncUser(Map<Integer, String> department) throws Exception {
        HTTPRequest httpReq = new HTTPRequest();
        String token = server.getAccessToken(Variable.contactSecret, true);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + token + "&department_id=1&fetch_child=1";
        JSONObject jsonRes = httpReq.sendGET(url);
        if (!jsonRes.getString("errmsg").equals("ok") || jsonRes.getInt("errcode") != 0) {
            return;
        }

        List<Map<String, Object>> users = new ArrayList<>();
        List<Map<String, Object>> userDeps = new ArrayList<>();
        JSONArray userList = jsonRes.getJSONArray("userlist");
        int userNum = userList.length(), keyNum = userKeys.length;
        for (int i = 0; i < userNum; i++) {
            JSONObject user = userList.getJSONObject(i);
            Map<String, Object> userMap = new TreeMap<>();
            for (int j = 0; j < keyNum; j++)
                userMap.put(userAttrs[j], user.getString(userKeys[j]));
            users.add(userMap);

            JSONArray depList = user.getJSONArray("department");
            JSONArray isLeaderList = user.getJSONArray("is_leader_in_dept");
            for (int j = 0; j < depList.length(); j++) {
                Map<String, Object> userDepMap = new TreeMap<>();
                int dID = depList.getInt(j);
                userDepMap.put("dID", dID);
                userDepMap.put("userID", user.getString("userid"));
                userDepMap.put("dName", department.get(dID));
                userDepMap.put("isLeader", isLeaderList.getInt(j));
                userDeps.add(userDepMap);
            }
        }

        // server.jdbcTemplate.update("DELETE FROM user"); //foreign key cascade with department table
        server.insertMapList(users, "user", true);
        server.insertMapList(userDeps, "department", true);
    }
}
