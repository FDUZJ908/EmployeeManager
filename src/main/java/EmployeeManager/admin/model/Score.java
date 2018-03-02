package EmployeeManager.admin.model;

/**
 * Created by 11437 on 2017/10/14.
 */
public class Score {
    private String userid;
    private String username;
    private int s_score;
    private int f_score;

    public Score(){}
    public Score(String userid,String username,int s_score,int f_score){
        this.userid=userid;
        this.username=username;
        this.s_score=s_score;
        this.f_score=f_score;
    }

    public String getUserid(){return userid;}
    public String getUsername(){return username;}
    public int getS_score(){return s_score;}
    public int getF_score(){return f_score;}

    public void setUserid(String userid){this.userid=userid;}
    public void setUsername(String username){this.username=username;}
    public void setS_score(int s_score){this.s_score=s_score;}
    public void setF_score(int f_score){this.f_score=f_score;}
}
