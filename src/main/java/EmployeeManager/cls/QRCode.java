package EmployeeManager.cls;

import java.util.Map;

/**
 * Created by lsh on 14/10/2017.
 */
public class QRCode {
    public static String[] Attrs = {"QRID", "s_time", "e_time", "token", "managers", "value", "checkins"};
    public int QRID;
    public String s_time;
    public String e_time;
    public int token;
    public String managers;
    public int value;
    public String checkins;

    public QRCode() {
    }

    public QRCode(Map<String, Object> argv) {
        QRID = (Integer) (argv.get("QRID"));
        s_time = Util.truncSecond(argv.get("s_time").toString());
        e_time = Util.truncSecond(argv.get("e_time").toString());
        token = (Integer) (argv.get("token"));
        managers = argv.get("managers").toString();
        value = (Integer) (argv.get("value"));
        checkins = argv.get("checkins").toString();
    }

    public QRCode(int QRID, String s_time, String e_time, int token, String managers, int value, String checkins) {
        this.QRID = QRID;
        this.s_time = s_time;
        this.e_time = e_time;
        this.token = token;
        this.managers = managers;
        this.value = value;
        this.checkins = checkins;
    }
}
