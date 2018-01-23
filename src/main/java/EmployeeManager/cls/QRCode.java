package EmployeeManager.cls;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lsh on 14/10/2017.
 */
public class QRCode {
    public static String[] Attrs = {"QRID", "s_time", "e_time", "token", "managers", "value"};
    public int QRID;
    public String s_time;
    public String e_time;
    public int token;
    public String managers;
    public int value;
    public Set<String> checkins;

    public QRCode() {
    }

    public QRCode(Map<String, Object> argv) {
        QRID = (Integer) (argv.get("QRID"));
        s_time = argv.get("s_time").toString();
        e_time = argv.get("e_time").toString();
        token = (Integer) (argv.get("token"));
        managers = argv.get("managers").toString();
        value = (Integer) (argv.get("value"));

        checkins = new HashSet<String>();
    }

    public synchronized void Add(String userID) {
        checkins.add(userID);
    }

}
