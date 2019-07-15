package EmployeeManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lsh on 21/02/2018.
 */
public class Util {

    public static String getRepeatQMark(int n, int m) {
        char[] s = new char[n * 2 * m + 3 * n + n];
        int p = 0;
        for (int i = 1; i <= n; i++) {
            if (i > 1) s[p++] = ',';
            s[p++] = '(';
            for (int j = 1; j <= m; j++) {
                if (j > 1) s[p++] = ',';
                s[p++] = '?';
            }
            s[p++] = ')';
        }
        return String.valueOf(s, 0, p);
    }

    public static int getTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String currentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static int getWeekday() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (weekday < 0) weekday += 7;
        return weekday;
    }

    public static String truncSecond(String datetime) {
        int n = datetime.length(), cnt = 0, p = -1;
        for (int i = 0; i < n; i++)
            if (datetime.charAt(i) == ':') {
                cnt += 1;
                p = i;
            }
        if (cnt == 2) return datetime.substring(0, p);
        else return datetime;
    }

    public static int getHour(String time) {
        int p = time.indexOf(":");
        if (p == -1) return -1;
        return Integer.parseInt(time.substring(0, p));
    }

    public static int getHour() {
        String datetime = currentTime();
        return getHour(datetime.substring(datetime.indexOf(" ") + 1));
    }

    public static long dateToStamp(String datetime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(datetime);
        return date.getTime();
    }

    public static String stampToDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        return format.format(date);
    }

    public static String AddOneDay(String date) {
        try {
            String datetime = stampToDate(dateToStamp(date + " 12:00:00") + 86400000);
            return datetime.substring(0, datetime.indexOf(' '));
        } catch (Exception e) {
            return null;
        }
    }

    private static double Radius = 6371;

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        lat1 = Math.toRadians(lat1);
        lng1 = Math.toRadians(lng1);
        lat2 = Math.toRadians(lat2);
        lng2 = Math.toRadians(lng2);
        double C = Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng1 - lng2) + Math.sin(lat1) * Math.sin(lat2);
        return Radius * Math.acos(C);
    }
}
