package EmployeeManager;

/**
 * Created by 11437 on 2017/7/25.
 */
public class User {
    private String username;
    private String score;

    User(Object userName, Object Score) {
        this.username = String.valueOf(userName);
        this.score = String.valueOf(Score);
    }

    public String getUsername() {
        return username;
    }

    public String getScore() {
        return score;
    }
}

