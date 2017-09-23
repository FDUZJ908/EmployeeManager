package EmployeeManager;

import java.util.HashSet;
import java.util.Set;

public class Checkin {
    private String timestamp;
    private Set<String> checkinMember;

    public Checkin(String timeStamp) {
        timestamp = timeStamp;
        checkinMember=new HashSet<String>();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Set<String> getCheckinMember() {
        return checkinMember;
    }

    public void setCheckinMember(Set<String> checkinMember) {
        this.checkinMember = checkinMember;
    }

    public void deleteCheckinMember() {
        checkinMember.clear();
    }
}
