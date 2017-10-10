package EmployeeManager.cls;

/**
 * Created by lsh on 05/09/2017.
 */
public class AccessToken {
    public String value;
    public int expireTime;

    public AccessToken(String v, int t) {
        value = v;
        expireTime = t;
    }
}
