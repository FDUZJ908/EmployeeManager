package EmployeeManager.admin.model;

/**
 * Created by 11437 on 2017/10/15.
 */
public class Depart {
    private String did;
    private String dname;
    private String userid;
    private String username;
    private int privilege;
    private int isleader;
    private int selected = 0;

    public Depart() {
    }

    public Depart(String did, String userid, String username, String dname, int isleader) {
        this.did = did;
        this.userid = userid;
        this.username = username;
        this.dname = dname;
        this.isleader = isleader;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public int getIsleader() {
        return isleader;
    }

    public void setIsleader(int isleader) {
        this.isleader = isleader;
    }

    public String getIsleader_() {
        return (this.isleader == 0) ? "否" : "是";
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
