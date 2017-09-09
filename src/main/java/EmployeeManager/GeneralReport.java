package EmployeeManager;

/**
 * Created by 11437 on 2017/8/4.
 */
public class GeneralReport {
    private String reportID;
    private String userId;
    private String userName;
    private String category;
    private String reportText;
    private String submitTime;

    GeneralReport(Object ReportID, Object UserID, Object UserName, Object Category, Object ReportText, Object SubmitTime) {
        this.reportID = String.valueOf(ReportID);
        this.userId = String.valueOf(UserID);
        this.userName = String.valueOf(UserName);
        this.category = String.valueOf(Category);
        this.reportText = String.valueOf(ReportText);
        this.submitTime = String.valueOf(SubmitTime);
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
