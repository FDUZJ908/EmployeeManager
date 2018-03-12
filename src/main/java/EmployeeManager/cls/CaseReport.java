package EmployeeManager.cls;

import java.util.Map;

public class CaseReport {
    public static String[] Attrs = {"reportID", "userID", "userName", "category", "reportText", "submitTime", "members", "singleScore", "reportPath"};

    private String reportID;
    private String userID;
    private String userName;
    private String category;
    private String reportText;
    private String submitTime;
    private String members;
    private String singleScore;
    private String reportPath;

    public CaseReport() {
    }

    public CaseReport(Map<String, Object> argv) {
        this.reportID = argv.get("reportID").toString();
        this.userID = argv.get("userID").toString();
        this.userName = argv.get("userName").toString();
        this.category = argv.get("category").toString();
        this.reportText = argv.get("reportText").toString();
        this.submitTime = argv.get("submitTime").toString();
        this.members = argv.get("members").toString();
        this.singleScore = argv.get("singleScore").toString();
        if (!argv.get("reportPath").toString().isEmpty()) {
            this.reportPath = "/" + argv.get("reportPath").toString();
        }
        else{
            this.reportPath = argv.get("reportPath").toString();
        }
        this.submitTime = this.submitTime.substring(0 , this.submitTime.lastIndexOf(":"));
        if (argv.get("members").toString() == "") {
            this.members = "无";
        }
    }

    public String getUserId() {
        return userID;
    }

    public String getReportPath() {
        return reportPath;
    }

    public String getCategory() {
        return category;
    }

    public String getReportText() {
        return reportText;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public String getReportID() {
        return reportID;
    }

    public String getSingleScore() {
        return singleScore;
    }

    public String getUserName() {
        return userName;
    }

    public String getMembers() {
        return members;
    }

}

