package EmployeeManager;

import java.util.Map;

public class User {
    static public String[] userKeys = {"userid", "gender", "avatar", "name", "position", "mobile", "email"};
    static public String[] userAttrs = {"userID", "gender", "avatarURL", "userName", "duty", "tel", "email"};
    static public String[] Attrs = {"userName", "s_score", "avatarURL"};

    private String userName;
    private String s_score;
    private String avatarURL;

    User(){}

    User(Map<String, Object> argv) {
        userName = argv.get("userName").toString();
        s_score = argv.get("s_score").toString();
        avatarURL = argv.get("avatarURL").toString();
    }
/*
    User(Object userName, Object Score, int rank, Object avatarURL) {
        this.username = String.valueOf(userName);
        this.score = String.valueOf(Score);
        this.rank = String.valueOf(rank);
        this.avatarURL = String.valueOf(avatarURL);
    }*/

    User(Object userName, Object Score) {
        this.userName = String.valueOf(userName);
        this.s_score = String.valueOf(Score);
    }

    public String getAvatarURL() {
        return avatarURL;
    }
    
    public String getUsername() {
        return userName;
    }

    public String getScore() {
        return s_score;
    }
}

