package EmployeeManager;

public class ReportApprovalAjax {
    private String reportStatus;
    private String reportComment;
    private String check1;
    private String check2;
    private String checkResponse;
    private String checkNum;

    public ReportApprovalAjax(String reportStatus, String reportComment, String check1, String check2, String checkResponse, String checkNum) {
        this.reportStatus = reportStatus;
        this.reportComment = reportComment;
        this.check1 = check1;
        this.check2 = check2;
        this.checkResponse = checkResponse;
        this.checkNum = checkNum;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public ReportApprovalAjax () {


    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getCheckResponse() {
        return checkResponse;
    }

    public void setCheckResponse(String checkResponse) {
        this.checkResponse = checkResponse;
    }

    public String getReportComment() {
        return reportComment;

    }

    public void setReportComment(String reportComment) {
        this.reportComment = reportComment;
    }

    public String getCheck1() {
        return check1;
    }

    public void setCheck1(String check1) {
        this.check1 = check1;
    }

    public String getCheck2() {
        return check2;
    }

    public void setCheck2(String check2) {
        this.check2 = check2;
    }
}
