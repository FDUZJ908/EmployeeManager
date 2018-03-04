package EmployeeManager.cls;

import java.util.Map;

public class GeneralReport {
    public static String[] Attrs = {"reportID", "userID", "userName", "category", "singleScore", "reportText", "submitTime", "reportPath"};

    private String reportID;
    private String userID;
    private String userName;
    private String category;
    private String singleScore;
    private String reportText;
    private String submitTime;
    private String reportPath;

    public GeneralReport() {
    }

    public GeneralReport(Map<String, Object> argv) {
        this.reportID = argv.get("reportID").toString();
        this.userID = argv.get("userID").toString();
        this.userName = argv.get("userName").toString();
        this.category = argv.get("category").toString();
        this.singleScore = argv.get("singleScore").toString();
        this.reportText = argv.get("reportText").toString();
        this.submitTime = argv.get("submitTime").toString();
        this.reportPath = "/" + argv.get("reportPath").toString();
        this.submitTime = this.submitTime.substring(0, this.submitTime.lastIndexOf(":"));
    }

    public String getSingleScore() {
        return singleScore;
    }

    public String getUserId() {
        return userID;
    }

    public String getReportPath() {
        return reportPath;
    }

    public String getUserName() {
        return userName;
    }

    public String getReportID() {
        return reportID;
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
}
