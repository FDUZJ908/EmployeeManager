package EmployeeManager;

/**
 * Created by 11437 on 2017/7/25.
 */
public class User {
    private String username;
    private String score;
    private String rank;
    private String avatarURL;

    User(Object userName, Object Score, int rank, Object avatarURL) {
        this.username = String.valueOf(userName);
        this.score = String.valueOf(Score);
        this.rank = String.valueOf(rank);
        this.avatarURL = String.valueOf(avatarURL);
    }
    User(Object userName, Object Score) {
        this.username = String.valueOf(userName);
        this.score = String.valueOf(Score);
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public String getScore() {
        return score;
    }
}

