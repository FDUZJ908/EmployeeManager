package EmployeeManager.admin.model;

import java.util.Map;

public class QRCheckers {
    public static String[] Attrs = {"QRID", "userID", "userName", "checkTime"};
    public int QRID;
    public String userID;
    public String userName;
    public String checkTime;

    public QRCheckers() {
    }

    public QRCheckers(Map<String, Object> argv) {
        QRID = (Integer) (argv.get("QRID"));
        userID = argv.get("userID").toString();
        userName = argv.get("userName").toString();
        checkTime = argv.get("checkTime").toString();
    }

    public QRCheckers(int QRID, String userID, String userName, String checkTime) {
        this.QRID = QRID;
        this.userID = userID;
        this.userName = userName;
        this.checkTime = checkTime;
    }
}
