package EmployeeManager.admin.model;

/**
 * Created by 11437 on 2017/10/14.
 */
public class Privilege {
    private int privilege;
    private int weekday;
    private String pushTime;
    private String leaderPostLimit;
    private String leaderScoreLimit;

    public Privilege() {
    }

    public Privilege(int privilege, int weekday, String pushTime, String leaderPostLimit, String leaderScoreLimit) {
        this.privilege = privilege;
        this.weekday = weekday;
        this.pushTime = pushTime;
        this.leaderPostLimit = leaderPostLimit;
        this.leaderScoreLimit = leaderScoreLimit;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getLeaderPostLimit() {
        return leaderPostLimit;
    }

    public void setLeaderPostLimit(String leaderPostLimit) {
        this.leaderPostLimit = leaderPostLimit;
    }

    public String getLeaderScoreLimit() {
        return leaderScoreLimit;
    }

    public void setLeaderScoreLimit(String leaderScoreLimit) {
        this.leaderScoreLimit = leaderScoreLimit;
    }

    public String getWeekdays() {
        String re = "";
        int x = this.weekday;
        for (int i = 0; i < 7; i++) {
            if ((x & (1 << i)) > 0) {
                switch (i) {
                    case 0:
                        re += ",星期一";
                        break;
                    case 1:
                        re += ",星期二";
                        break;
                    case 2:
                        re += ",星期三";
                        break;
                    case 3:
                        re += ",星期四";
                        break;
                    case 4:
                        re += ",星期五";
                        break;
                    case 5:
                        re += ",星期六";
                        break;
                    case 6:
                        re += ",星期天";
                        break;
                }
            }
        }
        if (re.length() > 0) re = re.substring(1, re.length());
        return re;
    }

}
