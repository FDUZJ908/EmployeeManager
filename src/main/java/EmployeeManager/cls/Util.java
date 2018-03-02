package EmployeeManager.cls;

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

    public static int getWeekday() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (weekday < 0) weekday += 7;
        return weekday;
    }

    public static String currentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    public static String truncSecond(String datetime) {
        int p = datetime.lastIndexOf(":");
        return datetime.substring(0, p);
    }
}
