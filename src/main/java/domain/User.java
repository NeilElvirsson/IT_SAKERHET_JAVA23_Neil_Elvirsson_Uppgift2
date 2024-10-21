package domain;

public class User {

    private String userName;
    private String password;

    public User(String userName) {

        this.userName = userName;
    }
    public User(String username, String password) {
        this.userName = username;
        this.password = password;
    }
    public String getPassword() {

        return this.password;
    }

    public String getUserName() {

        return this.userName;
    }

}
