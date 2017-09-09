package EmployeeManager;

/**
 * Created by lsh on 05/09/2017.
 */
public class AccessToken {
    String value;
    int expireTime;

    AccessToken(String v, int t) {
        value = v;
        expireTime = t;
    }
}
