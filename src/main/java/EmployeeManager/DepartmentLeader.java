package EmployeeManager;

public class DepartmentLeader {
    private String department;
    private String leader;
    private String leaderID;

    public DepartmentLeader(Object department, Object leader, Object leaderID) {
        this.department = department.toString();
        this.leader = leader.toString();
        this.leaderID = leaderID.toString();
    }

    public String getLeaderID() {
        return leaderID;
    }

    public String getDepartment() {
        return department;
    }

    public String getLeader() {
        return leader;
    }
}
