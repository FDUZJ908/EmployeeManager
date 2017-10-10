package EmployeeManager.cls;

import java.util.HashSet;
import java.util.Set;

public class Checkin {
    private long timestamp;
    private Set<String> users;

    public Checkin() {
        timestamp = System.currentTimeMillis();
        users = new HashSet<String>();
        users.clear();
    }

    public Checkin(long timeStamp) {
        timestamp = timeStamp;
        users = new HashSet<String>();
        users.clear();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void add(String userID) {
        this.users.add(userID);
    }

    public void clear() {
        users.clear();
    }

}
