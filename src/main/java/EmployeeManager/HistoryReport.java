package EmployeeManager;

import javax.validation.constraints.Null;

public class HistoryReport {

    private String reportID;
    private String userID;
    private String userName;
    private String submitTime;
    private String checkTime;
    private String category;
    private String reportText;
    private String isPass;
    private String singleScore;
    private String scoreType;
    private String comment;
    private String leaderName;
    private String members;
    private String type;
    private String reportPath;

    public HistoryReport(Object reportID,Object userID, Object userName, Object submitTime, Object checkTime,
                         Object category, Object reportText, Object isPass, Object scoreType, Object comment,
                         Object leaderName, Object members, Object type, Object reportPath) {

        int scoreTemp = 0;
        String categoryTmp = "";
        int categoryTemp = Integer.parseInt(String.valueOf(category));
        switch (categoryTemp) {
            default:
                scoreTemp = 0;
                categoryTmp = "";
                break;
            case 1:
                scoreTemp = 1;
                categoryTmp = "日常工作";
                break;
            case 2:
                scoreTemp = 2;
                categoryTmp = "领导交办";
                break;
            case 3:
                scoreTemp = 2;
                categoryTmp = "阶段汇总";
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

        if (Integer.parseInt(String.valueOf(type))>= 10)
            isPass = "待审批";
        else
        {
            if (String.valueOf(isPass).equals("true"))
                isPass = "通过";
            if (String.valueOf(isPass).equals("false"))
                isPass = "不通过";
        }

        if (String.valueOf(comment) == "")
            comment = "无";

        if(members.toString() == "")
            members = "无";

        if(leaderName.toString() == "")
            leaderName = "无";

        submitTime = submitTime.toString().substring(0 , submitTime.toString().length()-5);

        if(checkTime.toString() != "")
            checkTime = checkTime.toString().substring(0 , checkTime.toString().length()-5);

        this.reportID = String.valueOf(reportID);
        this.userID = String.valueOf(userID);
        this.userName = String.valueOf(userName);
        this.submitTime = String.valueOf(submitTime);
        this.checkTime = String.valueOf(checkTime);
        this.category = String.valueOf(categoryTmp);
        this.reportText = String.valueOf(reportText);
        this.isPass = String.valueOf(isPass);
        this.singleScore = String.valueOf(scoreTemp);
        this.scoreType = String.valueOf(scoreType);
        this.comment = String.valueOf(comment);
        this.leaderName = String.valueOf(leaderName);
        this.members = String.valueOf(members);
        this.type = String.valueOf(type);
        this.reportPath = String.valueOf(reportPath);
    }

    /*public void setScore(String score) {
        this.singleScore = score;
    }
*/

    public String getReportPath() {
        return reportPath;
    }

    public String getReportID() {return reportID;}

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public String getCheckTime() { return checkTime; }

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

    public String getType() {return type;}
}
