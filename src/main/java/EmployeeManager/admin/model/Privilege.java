package EmployeeManager.admin.model;

import EmployeeManager.Variable;

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
        StringBuffer res = new StringBuffer();
        int x = this.weekday;
        for (int i = 0; i < 7; i++) {
            if ((x & (1 << i)) > 0) {
                res.append("," + Variable.Weekdays[i]);
            }
        }
        if (res.length() > 0) return res.substring(1, res.length());
        else return res.toString();
    }

}
