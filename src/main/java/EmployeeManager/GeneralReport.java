package EmployeeManager;

import java.util.Map;

import static EmployeeManager.Server.path;

public class GeneralReport {
    private String reportID;
    private String userID;
    private String userName;
    private String category;
    private String reportText;
    private String submitTime;
    private String reportPath;

    static String[] Attrs={"reportID","userID","userName","category","reportText","submitTime","reportPath"};

    GeneralReport(){}

    GeneralReport(Map<String, Object> argv)
    {
        this.reportID = argv.get("reportID").toString();
        this.userID =argv.get("userID").toString();
        this.userName = argv.get("userName").toString();
        this.category = argv.get("category").toString();
        this.reportText = argv.get("reportText").toString();
        this.submitTime = argv.get("submitTime").toString();
        this.reportPath = argv.get("reportPath").toString().substring(path.length());
    }
    /*
    GeneralReport(Object ReportID, Object UserID, Object UserName, Object Category,
                  Object ReportText, Object SubmitTime, Object reportPath) {
        this.reportID = String.valueOf(ReportID);
        this.userId = String.valueOf(UserID);
        this.userName = String.valueOf(UserName);
        this.category = String.valueOf(Category);
        this.reportText = String.valueOf(ReportText);
        this.submitTime = String.valueOf(SubmitTime);
        this.reportPath = String.valueOf(reportPath);
    }*/

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
