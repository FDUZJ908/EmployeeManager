package EmployeeManager.admin.model;

/**
 * Created by 11437 on 2017/10/15.
 */
public class Depart {
    private String did;
    private String userid;
    private String username;
    private String dname;
    private String isleader;

    public Depart(){}
    public Depart(String did,String userid,String username,String dname,String isleader){
        this.did=did;
        this.userid=userid;
        this.username=username;
        this.dname=dname;
        this.isleader=isleader;
    }

    public String getDid(){return did;}
    public String getUserid(){return userid;}
    public String getUsername(){return username;}
    public String getDname(){return dname;}
    public String getIsleader(){return isleader;}

    public void setDid(String did){this.did=did;}
    public void setUserid(String userid){this.userid=userid;}
    public void setUsername(String username){this.username=username;}
    public void setDname(String dname){this.dname=dname;}
    public void setIsleader(String isleader){this.isleader=isleader;}

    public String getIsleader_(){
        if(this.isleader.equals("0"))
            return "否";
        else
            return "是";
    }
}
