package EmployeeManager;

import EmployeeManager.cls.QRCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsh on 23/01/2018.
 */
public class Variable {
    public static final String corpid = "wx7635106ee1705d6d";
    public static final String submitSecret = "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY";
    public static final String contactSecret = "eRepbi6SM0Qk6DbHsbO0NqhKWEhL4_CtFLt-UKO_P5k";
    public static final String reportSecret = "x0VeHuzpagYuJmrymzJayHmN3vrzgtcISA2Q_8qhLG0";
    /*static final String approvalSecret = "5nFQW5WCF1Gxk7Ht6crCkACsUhqP1DkGk7Fsvg8W67E";
    static final String reportSecret = "xVBKh9GKuDGd_nJp8TBRzWzWBHkIVDFPjxewNKhBEzA";
    static final int approvalAgentID = 1000004;
    static final int reportAgentID = 1000005;*/
    public static final int reportAgentID = 1000017;
    public static final Map<Integer, String> corpsecret = new HashMap<Integer, String>() {{
        put(reportAgentID, reportSecret);
        //put(approvalAgentID, approvalSecret);
        //put(reportAgentID, reportSecret);
    }};

    public static int maxReportCount = 0;
    public static Map<Integer, QRCode> QRCodes = new HashMap<Integer, QRCode>();
    public static Map<String, Integer> reported = new HashMap<String, Integer>();
}
