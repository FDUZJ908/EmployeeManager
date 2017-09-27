package EmployeeManager;

import java.util.HashSet;
import java.util.Set;

public class Checkin {
    private long timestamp;
    private Set<String> checkinMember;

    public Checkin(long timeStamp) {
        timestamp = timeStamp;
        checkinMember=new HashSet<String>();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Set<String> getCheckinMember() {
        return checkinMember;
    }

    public void addCheckinMember(String userID) {
        this.checkinMember.add(userID);
    }

    public void deleteCheckinMember() {
        checkinMember.clear();
    }
}
