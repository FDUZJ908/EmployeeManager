package EmployeeManager.cls;

public class ReportType {
    private String TypeName;
    private String TypeValue;

    public ReportType() {

    }

    public ReportType(String typeName, String typeValue) {
        TypeName = typeName;
        TypeValue = typeValue;
    }

    public String getTypeName() {
        return TypeName;
    }

    public String getTypeValue() {
        return TypeValue;
    }
}
