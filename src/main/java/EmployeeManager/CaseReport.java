package EmployeeManager;

import java.util.Map;

import static EmployeeManager.Server.path;

public class CaseReport {
    private String reportID;
    private String userID;
    private String userName;
    private String category;
    private String reportText;
    private String submitTime;
    private String members;
    private String singleScore;
    private String reportPath;

    static String[] Attrs = {"reportID", "userID", "userName", "category", "reportText", "submitTime", "members", "singleScore", "reportPath"};

    CaseReport(Map<String, Object> argv)
    {
        this.reportID = argv.get("reportID").toString();
        this.userID =argv.get("userID").toString();
        this.userName = argv.get("userName").toString();
        this.category = argv.get("category").toString();
        this.reportText = argv.get("reportText").toString();
        this.submitTime = argv.get("submitTime").toString();
        this.members = argv.get("members").toString();
        this.singleScore = argv.get("singleScore").toString();
        this.reportPath = argv.get("reportPath").toString().substring(path.length());
    }
    /*
    CaseReport(Object ReportID, Object UserID, Object UserName, Object Category, Object ReportText,
               Object SubmitTime, Object Members, Object SingleScore, Object reportPath) {
        this.reportID = String.valueOf(ReportID);
        this.userId = String.valueOf(UserID);
        this.userName = String.valueOf(UserName);
        this.category = String.valueOf(Category);
        this.reportText = String.valueOf(ReportText);
        this.submitTime = String.valueOf(SubmitTime);
        this.members = String.valueOf(Members);
        this.singleScore = String.valueOf(SingleScore);
        this.reportPath = String.valueOf(reportPath);
    }
    */
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

