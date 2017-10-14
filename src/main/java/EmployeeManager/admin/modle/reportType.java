package EmployeeManager.admin.modle;
public class reportType {
    private String name;
    private String value;
    private String remark;

    public reportType(Object name, Object value, Object remark) {
        this.name = name.toString();
        this.value = value.toString();
        this.remark = remark.toString();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getRemark() {
        return remark;
    }
}
