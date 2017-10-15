package EmployeeManager.admin.model;

public class report {
    private String id;
    private String content;
    private String category;
    private String type;
    private String score;
    private String submitter;
    private String leader;
    private String pass;
    private String members;
    private String submitTime;
    private String comment;
    private String checkTime;

    public report(String id, String content, String category, String type, String score,
                  String submitter, String leader, String pass, String members, String submitTime,
                  String comment, String checkTime) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.type = type;
        this.score = score;
        this.submitter = submitter;
        this.leader = leader;
        this.pass = pass;
        this.members = members;
        this.submitTime = submitTime;
        this.comment = comment;
        this.checkTime = checkTime;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getScore() {
        return score;
    }

    public String getSubmitter() {
        return submitter;
    }

    public String getLeader() {
        return leader;
    }

    public String getPass() {
        return pass;
    }

    public String getMembers() {
        return members;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public String getComment() {
        return comment;
    }

    public String getCheckTime() {
        return checkTime;
    }
}
