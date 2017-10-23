package EmployeeManager.admin.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by 11437 on 2017/10/13.
 */
public class Employee {
   private String userid;
   private String username;
   private String duty;
   private String position;
   private String title;
   private String privilege;
   private String status;
   private String tel;
   private String gender;
   private String email;

   public Employee(){}

   public Employee(String userid,String username,String duty,String position,String title,String privilege,String status,String tel,String gender,String email){
       this.userid=userid;
       this.username=username;
       this.duty=duty;
       this.position=position;
       this.title=title;
       this.privilege=privilege;
       this.status=status;
       this.tel=tel;
       this.gender=gender;
       this.email=email;
   }

   public String getUserID(){
       return userid;
   }

   public void setUserid(String userid){
       this.userid=userid;
   }

   public String getUsername(){
       return username;
    }

    public void setUsername(String username) {
       this.username=username;
   }

    public String getDuty(){
       return duty;
    }

    public void setDuty(String duty){
        this.duty=duty;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position=position;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getPrivilege(){
        return privilege;
    }

    public void setPrivilege(String privilege){
        this.privilege=privilege;
    }

    public String getStatus(){ return status;}

    public void setStatus(String status){this.status=status;}

    public String getTel(){return tel;}

    public void setTel(String tel){this.tel=tel;}

    public String getGender(){return gender;}

    public void setGender(String gender){this.gender=gender;}

    public String getEmail(){return email;}

    public void setEmail(String email){this.email=email;}
}
