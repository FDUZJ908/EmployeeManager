package EmployeeManager;

public class HistoryReport {

    private String userID;
    private String userName;
    private String submitTime;
    private String category;
    private String reportText;
    private String isPass;
    private String score;
    private String scoreType;
    private String comment;
    private String leaderName;

    public HistoryReport(Object userID, Object userName, Object submitTime, Object category, Object reportText,
                         Object isPass, Object scoreType, Object comment, Object leaderName) {

        int scoreTemp = 0;
        String categoryTmp = "";
        int categoryTemp = Integer.parseInt(String.valueOf(category));
        switch (categoryTemp){
            default:
                scoreTemp = 0;
                categoryTmp = "";
                break;
            case 0:
                scoreTemp = 2;
                categoryTmp = "日常工作";
                break;
            case 1:
                scoreTemp = 2;
                categoryTmp = "领导交办";
                break;
            case 2:
                scoreTemp = 2;
                categoryTmp = "阶段汇总";
                break;
            case 3:
                scoreTemp = 1;
                categoryTmp = "考勤签到";
                break;
            case 4:
                scoreTemp = 2;
                categoryTmp = "急难险重";
                break;
            case 5:
                scoreTemp = 2;
                categoryTmp = "其他工作";
                break;
        }
        if (scoreType == "1")
            scoreTemp = -scoreTemp;


        if (String.valueOf(isPass) == "null")
            isPass = "";
        else
            if (String.valueOf(isPass) == "true")
                isPass = "通过";
            else
                isPass = "未通过";

        if (String.valueOf(comment) == "null")
            comment = "";
        else
            comment = String.valueOf(comment);

        this.userID = String.valueOf(userID);
        this.userName = String.valueOf(userName);
        this.submitTime = String.valueOf(submitTime);
        this.category = String.valueOf(categoryTmp);
        this.reportText = String.valueOf(reportText);
        this.isPass = String.valueOf(isPass);
        this.score = String.valueOf(scoreTemp);
        this.scoreType = String.valueOf(scoreType);
        this.comment = String.valueOf(comment);
        this.leaderName = String.valueOf(leaderName);
    }

    public void setScore(String score) {
        this.score = score;
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

    public String getCategory() {
        return category;
    }

    public String getReportText() {
        return reportText;
    }

    public String getIsPass() {
        return isPass;
    }

    public String getScore() {
        return score;
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


}
