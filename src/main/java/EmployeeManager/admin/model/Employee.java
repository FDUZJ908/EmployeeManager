package EmployeeManager.admin.model;


/**
 * Created by 11437 on 2017/10/13.
 */
public class Employee {
    private String userid;
    private String username;
    private String duty;
    private String position;
    private String title;
    private int privilege;
    private String status;
    private String tel;
    private String gender;
    private String email;
    private int selected = 0;

    public Employee() {
    }

    public Employee(String userid, String username, String duty, String position, String title, int privilege, String status, String tel, String gender, String email) {
        this.userid = userid;
        this.username = username;
        this.duty = duty;
        this.position = position;
        this.title = title;
        this.privilege = privilege;
        this.status = status;
        this.tel = tel;
        this.gender = gender;
        this.email = email;
    }

    public String getUserID() {
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

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender_() {
        if (this.gender == null)
            return "未知";
        if (this.gender.equals("1"))
            return "男";
        else if (this.gender.equals("2"))
            return "女";
        return "未知";
    }

    public String getStatus_() {
        if (this.status == null)
            return "";
        else if (this.status.equals("0"))
            return "在职";
        else if (this.status.equals("1"))
            return "退休";
        else if (this.status.equals("2"))
            return "退职";
        else if (this.status.equals("3"))
            return "开除";
        else if (this.status.equals("4"))
            return "离任";
        else return "";
    }

    public void setStatus_(String status_) {
        switch (status_) {
            case "在职":
                this.status = "0";
                break;
            case "退休":
                this.status = "1";
                break;
            case "退职":
                this.status = "2";
                break;
            case "开除":
                this.status = "3";
                break;
            case "离任":
                this.status = "4";
                break;
            default:
                this.status = "0";
                break;
        }
    }

    public String getPosition_() {
        if (this.position == null)
            return "";
        if (this.position.equals("0"))
            return "村级干部";
        if (this.position.equals("1"))
            return "一般干部";
        if (this.position.equals("2"))
            return "中层干部";
        if (this.position.equals("3"))
            return "领导干部";
        return "";
    }

    public void setPosition_(String position_) {
        switch (position_) {
            case "村级干部":
                this.position = "0";
                break;
            case "一般干部":
                this.position = "1";
                break;
            case "中层干部":
                this.position = "2";
                break;
            case "领导干部":
                this.position = "3";
                break;
            default:
                this.position = null;
                break;
        }
    }

    public String getTitle_() {
        if (this.title == null)
            return "";
        if (this.title.equals("0"))
            return "村级";
        if (this.title.equals("1"))
            return "科员";
        if (this.title.equals("2"))
            return "股级";
        if (this.title.equals("3"))
            return "科级";
        return "";
    }

    public void setTitle_(String title_) {
        switch (title_) {
            case "村级":
                this.title = "0";
                break;
            case "科员":
                this.title = "1";
                break;
            case "股级":
                this.title = "2";
                break;
            case "科级":
                this.title = "3";
                break;
            default:
                this.title = null;
                break;
        }
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
