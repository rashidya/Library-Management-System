package model;

public class UserAccount {
    private String username;
    private String password;
    private User user;


    public UserAccount() {
    }

    public UserAccount(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }
}
