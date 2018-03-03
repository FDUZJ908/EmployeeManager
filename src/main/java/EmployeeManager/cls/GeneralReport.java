package EmployeeManager.cls;

import java.util.Map;

public class GeneralReport {
    private String reportID;
    private String userID;
    private String userName;
    private String category;
    private String singleScore;
    private String reportText;

    public String getSingleScore() {
        return singleScore;
    }

    private String submitTime;
    private String reportPath;

    public static String[] Attrs={"reportID","userID","userName","category","typeValue","reportText","submitTime","reportPath"};

    public GeneralReport(){}

    public GeneralReport(Map<String, Object> argv)
    {
        this.reportID = argv.get("reportID").toString();
        this.userID =argv.get("userID").toString();
        this.userName = argv.get("userName").toString();
        this.category = argv.get("category").toString();
        this.singleScore = argv.get("typeValue").toString();
        this.reportText = argv.get("reportText").toString();
        this.submitTime = argv.get("submitTime").toString();
        this.reportPath = "https://shiftlin.top:8443/" + argv.get("reportPath").toString();
        this.submitTime = this.submitTime.substring(0 , this.submitTime.lastIndexOf(":"));
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
