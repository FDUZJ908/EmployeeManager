package EmployeeManager.admin.model;

import EmployeeManager.Variable;

/**
 * Created by 11437 on 2018/2/13.
 */
public class WeekDay {
    private int id;
    private String name;

    public WeekDay() {
    }

    public WeekDay(int id) {
        this.id = id;
        this.name = Variable.Weekdays[id];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
