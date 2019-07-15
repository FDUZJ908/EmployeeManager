package EmployeeManager.cls;

import java.util.Map;

public class User {
    static public String[] userKeys = {"userid", "gender", "avatar", "name", "position", "mobile", "email"};
    static public String[] userAttrs = {"userID", "gender", "avatarURL", "userName", "duty", "tel", "email"};
    static public String[] Attrs = {"userName", "s_score", "avatarURL", "duty", "title"};

    private String userName;
    private String s_score;
    private String avatarURL;
    private String duty;
    private String title;

    public User() {
    }

    public User(Map<String, Object> argv) {
        userName = argv.get("userName").toString();
        s_score = argv.get("s_score").toString();
        avatarURL = argv.get("avatarURL").toString();
        if (avatarURL.length()>4 && !avatarURL.substring(0, 4).equals("http"))
            avatarURL = "/" + avatarURL;
        duty = argv.get("duty").toString();
        title = argv.get("title").toString();
        if (title.length() > 0) {
            int titleNum = Integer.parseInt(title);
            switch (titleNum) {
                case 0:
                    title = "村级";
                    break;
                case 1:
                    title = "科员";
                    break;
                case 2:
                    title = "股级";
                    break;
                case 3:
                    title = "科级";
                    break;
            }
        }
    }

    public User(Object userName, Object Score) {
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

    public String getDuty() {
        return duty;
    }

    public String getTitle() {
        return title;
    }
}

