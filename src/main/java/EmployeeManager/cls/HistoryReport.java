package EmployeeManager.cls;

import java.util.Map;

public class HistoryReport {

    public static String[] Attrs = {"reportID", "userID", "userName", "type", "category", "reportText",
            "submitTime", "checkTime", "isPass", "members", "comment", "singleScore", "scoreType", "leaderName", "reportPath"};
    public static int GENERAL = 0x0001;
    public static int CASE = 0x0010;
    public static int LEADER = 0x0100;
    public static int APPROVED = 0x1000;
    private String reportID;
    private String userID;
    private String userName;
    private String type;
    private String typeName;
    private String category;
    private String reportText;
    private String submitTime;
    private String checkTime;
    private String isPass;
    private String members;
    private String comment;
    private String singleScore;
    private String scoreType;
    private String leaderName;
    private String reportPath;

    public HistoryReport() {
    }

    public HistoryReport(Map<String, Object> argv) {
        reportID = argv.get("reportID").toString();
        userID = argv.get("userID").toString();
        userName = argv.get("userName").toString();

        type = argv.get("type").toString();
        category = argv.get("category").toString();

        int typeNum = Integer.parseInt(type);

        reportText = argv.get("reportText").toString();
        submitTime = Util.truncSecond(argv.get("submitTime").toString());//-5
        checkTime = Util.truncSecond(argv.get("checkTime").toString());//

        if ((typeNum & GENERAL) > 0) {
            typeName = "一般报告";
        }

        if ((typeNum & CASE) > 0) {
            typeName = "个案报告";
        }

        if ((typeNum & LEADER) > 0) {
            isPass = "通过";
            typeName = "领导批示";
        } else if ((typeNum & APPROVED) > 0) {
            isPass = (argv.get("isPass").toString().equals("true")) ? "通过" : "不通过";
        } else isPass = "待审批";

        members = argv.get("members").toString();
        if (members.length() == 0)
            members = "无";

        comment = argv.get("comment").toString();
        if (comment.length() == 0)
            comment = "无";

        singleScore = argv.get("singleScore").toString();

        if ((typeNum & (CASE | LEADER)) > 0) {
            scoreType = argv.get("scoreType").toString();
            if (scoreType.equals("false"))
                singleScore = "-" + singleScore;
        }

        leaderName = argv.get("leaderName").toString();
        reportPath = "https://shiftlin.top:8443/" + argv.get("reportPath").toString();
    }


    public String getReportPath() {
        return reportPath;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String id) {
        reportID = id;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public String getCategory() {
        return category;
    }


    public String getReportText() {
        return reportText;
    }

    public String getIsPass() {
        return isPass;
    }

    public String getSingleScore() {
        return singleScore;
    }

    public String getScoreType() {
        return scoreType;
    }

    public String getComment() {
        return comment;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getMembers() {
        return members;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
