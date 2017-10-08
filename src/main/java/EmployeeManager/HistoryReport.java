package EmployeeManager;

import java.util.Map;

public class HistoryReport {

    static String[] Attrs = {"reportID", "userID", "userName", "type", "category", "reportText", "submitTime", "checkTime", "isPass",
            "members", "comment", "singleScore", "scoreType", "leaderName", "reportPath"};
    static int GENERAL = 0x0001;
    static int CASE = 0x0010;
    static int LEADER = 0x0100;
    static int APPROVED = 0x1000;
    private String reportID;
    private String userID;
    private String userName;
    private String type;
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

    public HistoryReport(){}

    public HistoryReport(Map<String, Object> argv) {
        reportID = argv.get("reportID").toString();
        userID = argv.get("userID").toString();
        userName = argv.get("userName").toString();
        type = argv.get("type").toString();
        int typeNum = Integer.parseInt(type);
        int categoryNum = Integer.parseInt(argv.get("category").toString());
        switch (categoryNum) {
            default:
                singleScore = "0";
                category = "";
                break;
            case 1:
                singleScore = "1";
                category = "日常工作";
                break;
            case 2:
                singleScore = "2";
                category = "领导交办";
                break;
            case 3:
                singleScore = "2";
                category = "阶段汇总";
                break;
            case 4:
                singleScore = "2";
                category = "急难险重";
                break;
            case 5:
                singleScore = "2";
                category = "其他工作";
                break;
        }
        reportText = argv.get("reportText").toString();
        submitTime = argv.get("submitTime").toString();//-5
        checkTime = argv.get("checkTime").toString();//

        submitTime = submitTime.substring(0 , submitTime.lastIndexOf(":"));

        if ((typeNum & APPROVED) > 0)
            isPass = (argv.get("isPass").toString().equals("true")) ? "通过" : "不通过";
        else isPass = "待审批";

        members = argv.get("members").toString();
        if (members.length() == 0)
            members = "无";

        comment = argv.get("comment").toString();
        if (comment.length() == 0)
            comment = "无";

        if ((typeNum & (CASE | LEADER)) > 0) {
            singleScore = argv.get("singleScore").toString();
            scoreType = argv.get("scoreType").toString();
            if (scoreType.equals("false"))
                singleScore = "-" + singleScore;
        }

        leaderName = argv.get("leaderName").toString();
        reportPath = argv.get("reportPath").toString();
    }


    public String getReportPath() {
        return reportPath;
    }

    public String getReportID() {
        return reportID;
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
}
