package EmployeeManager.admin.model;

/**
 * Created by 11437 on 2017/10/14.
 */
public class Privilege{
    private String pid;
    private String privilege;
    private String weekday;
    private String pushTime;
    private String leaderPostLimit;
    private String leaderScoreLimit;

    public Privilege(){}
    public Privilege(String pid,String privilege,String weekday,String pushTime,String leaderPostLimit,String leaderScoreLimit){
        this.pid=pid;
        this.privilege=privilege;
        this.weekday=weekday;
        this.pushTime=pushTime;
        this.leaderPostLimit=leaderPostLimit;
        this.leaderScoreLimit=leaderScoreLimit;
    }

    public String getPid(){return pid;}
    public String getPrivilege(){return privilege;}
    public String getWeekday(){return weekday;}
    public String getPushTime(){return pushTime;}
    public String getLeaderPostLimit(){return  leaderPostLimit;}
    public String getLeaderScoreLimit(){return leaderScoreLimit;}

    public void setPid(String pid){this.pid=pid;}
    public void setPrivilege(String privilege){this.privilege=privilege;}
    public void setWeekday(String weekday){this.weekday=weekday;}
    public void setPushTime(String pushTime){this.pushTime=pushTime;}
    public void setLeaderPostLimit(String leaderPostLimit){this.leaderPostLimit=leaderPostLimit;}
    public void setLeaderScoreLimit(String leaderScoreLimit){this.leaderScoreLimit=leaderScoreLimit;}

}
