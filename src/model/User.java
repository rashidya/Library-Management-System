package model;

public class User {
    private String id;
    private String name;
    private String contactNo;
    private String email;
    private String jobTitle;
    private String securityLevel;
    private String username;

    public User() {
    }

    public User(String id, String name, String contactNo, String email, String jobTitle, String securityLevel, String username) {
        this.id = id;
        this.name = name;
        this.contactNo = contactNo;
        this.email = email;
        this.jobTitle = jobTitle;
        this.securityLevel = securityLevel;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmail() {
        return email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public String getUsername() {
        return username;
    }
}
