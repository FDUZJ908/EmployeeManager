package EmployeeManager.admin.model;

/**
 * Created by 11437 on 2018/2/13.
 */
public class WeekDay {
    private String id;
    private String name;

    public WeekDay() {
    }

    public WeekDay(String id) {
        this.id = id;
        switch (id) {
            case "1":
                this.name = "星期一";
                break;
            case "2":
                this.name = "星期二";
                break;
            case "3":
                this.name = "星期三";
                break;
            case "4":
                this.name = "星期四";
                break;
            case "5":
                this.name = "星期五";
                break;
            case "6":
                this.name = "星期六";
                break;
            case "7":
                this.name = "星期天";
                break;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
