package EmployeeManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsh on 23/01/2018.
 */
public class Variable {
    public static final String corpid = "wx7635106ee1705d6d";

    public static final int AgentID = 1000020;
    //public static final int reportAgentID = 1000017;
    //public static final int submitAgentID = 1000002;

    public static final String contactSecret = "eRepbi6SM0Qk6DbHsbO0NqhKWEhL4_CtFLt-UKO_P5k";
    //public static final String reportSecret = "x0VeHuzpagYuJmrymzJayHmN3vrzgtcISA2Q_8qhLG0";
    //public static final String submitSecret = "oJHX1ituPjdSKDFaHVitOWqw-t9u16aG1UEZsn7cBA8";
    //public static final String reportSecret = "oJHX1ituPjdSKDFaHVitOWqw-t9u16aG1UEZsn7cBA8";
    public static final String Secret = "oJHX1ituPjdSKDFaHVitOWqw-t9u16aG1UEZsn7cBA8";
    public static final String callbackToken = "NmPyvUOxDwTFwQ5BHJJadPD02";
    public static final String callbackEncodingAESKey = "2JYiuUvPlweUkNVrTcBCy4QL6VDUraSmxryDxaCryCG";


    public static final Map<Integer, String> corpsecret = new HashMap<>() {{
        //put(reportAgentID, reportSecret);
        //put(submitAgentID, submitSecret);
        put(AgentID, Secret);
    }};

    public static final String[] Weekdays = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};

    public static int QRCodeDistLimitKM = 1;
    public static int maxGeneralReportCount = 1;
    public static int caseReportEntryLimit = 1;
    public static int caseReportCheckLimit = 1;
    public static Map<String, Integer> generalCount = new HashMap<>();
    public static Map<String, Integer> leaderCount = new HashMap<>();

    public static String mesgToLeader = "您有一份新报告需要审批，可进入 报告查询-审批报告 查看。";
    public static String mesgToSubordinate = "您有一份报告已被审批，可进入 报告查询-我的报告 查看。";
    public static String mesgForReport = "企业微信提醒您提交今日报告，如已提交请忽略本条消息。";
}
