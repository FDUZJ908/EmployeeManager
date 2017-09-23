package EmployeeManager;

import java.util.List;

public class checkinRecord {
    private String LeaderID;
    private String TimeStamp;
    private List<String> checkinMember;

    public checkinRecord(String leaderID, String timeStamp) {
        LeaderID = leaderID;
        TimeStamp = timeStamp;
    }

    public String getLeaderID() {
        return LeaderID;
    }

    public void setLeaderID(String leaderID) {
        LeaderID = leaderID;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public List<String> getCheckinMember() {
        return checkinMember;
    }

    public void setCheckinMember(List<String> checkinMember) {
        this.checkinMember = checkinMember;
    }

    public void deleteCheckinMember() {
        for(int i=0;i<this.checkinMember.size();){
            this.checkinMember.remove(i);
        }
    }
}
