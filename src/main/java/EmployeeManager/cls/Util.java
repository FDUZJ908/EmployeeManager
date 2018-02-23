package EmployeeManager.cls;

/**
 * Created by lsh on 21/02/2018.
 */
public class Util {

    public static  String getRepeatQMark(int n, int m) {
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
}
