package EmployeeManager.admin.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by 11437 on 2017/10/14.
 */
public class Score {
    private String userid;
    private String username;
    private String s_score;
    @Max(value=1000,message ="超过最大数值")
    @Min(value=0,message = "设定不正确")
    private String f_score;

    public Score(){}
    public Score(String userid,String username,String s_score,String f_score){
        this.userid=userid;
        this.username=username;
        this.s_score=s_score;
        this.f_score=f_score;
    }

    public String getUserid(){return userid;}
    public String getUsername(){return username;}
    public String getS_score(){return s_score;}
    public String getF_score(){return f_score;}

    public void setUserid(String userid){this.userid=userid;}
    public void setUsername(String username){this.username=username;}
    public void setS_score(String s_score){this.s_score=s_score;}
    public void setF_score(String f_score){this.f_score=f_score;}
}
