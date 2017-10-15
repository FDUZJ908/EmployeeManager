package EmployeeManager.admin.model;

import java.util.Map;

public class reportType {
    private String name;
    private String value;
    private String remark;

    public reportType() {
    }

    public static String[] Attrs = {"typeName", "typeValue", "typeRemark"};

    public reportType(Map<String, Object> argv) {
        this.name = argv.get("typeName").toString();
        this.value = argv.get("typeValue").toString();
        this.remark = argv.get("typeRemark").toString();
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
