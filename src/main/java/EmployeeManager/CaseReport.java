package EmployeeManager;

public class CaseReport {
    private String reportID;
    private String userId;
    private String userName;
    private String category;
    private String reportText;
    private String submitTime;
    private String members;
    private String singleScore;

    CaseReport(Object ReportID, Object UserID, Object UserName, Object Category, Object ReportText, Object SubmitTime, Object Members, Object SingleScore) {
        this.reportID = String.valueOf(ReportID);
        this.userId = String.valueOf(UserID);
        this.userName = String.valueOf(UserName);
        this.category = String.valueOf(Category);
        this.reportText = String.valueOf(ReportText);
        this.submitTime = String.valueOf(SubmitTime);
        this.members = String.valueOf(Members);
        this.singleScore = String.valueOf(SingleScore);
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

