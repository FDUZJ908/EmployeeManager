package EmployeeManager.admin.model;

import EmployeeManager.Util;

import java.util.Map;

public class QRChecker {
    public static String[] Attrs = {"QRID", "userID", "userName", "checkTime", "manager", "manaLat", "manaLng", "userLat", "userLng"};
    public int QRID;
    public String userID;
    public String userName;
    public String checkTime;
    public String manager;
    public String manaLatLng;
    public String userLatLng;
    public double distance;

    public QRChecker() {
    }

    public QRChecker(Map<String, Object> argv) {
        QRID = (Integer) (argv.get("QRID"));
        userID = argv.get("userID").toString();
        userName = argv.get("userName").toString();
        checkTime = argv.get("checkTime").toString();
        manager = argv.get("manager").toString();
        double manaLat = (double) argv.get("manaLat"), manaLng = (double) argv.get("manaLng");
        double userLat = (double) argv.get("userLat"), userLng = (double) argv.get("userLng");
        manaLatLng = manaLat + ", " + manaLng;
        userLatLng = userLat + ", " + userLng;
        distance = Util.getDistance(userLat, userLng, manaLat, manaLng);
    }

    public QRChecker(int QRID, String userID, String userName, String checkTime, String manager, String manaLatLong, String userLatLong) {
        this.QRID = QRID;
        this.userID = userID;
        this.userName = userName;
        this.checkTime = checkTime;
        this.manager = manager;
        this.manaLatLng = manaLatLong;
        this.userLatLng = userLatLong;
    }
}
