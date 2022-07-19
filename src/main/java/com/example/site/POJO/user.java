package com.example.site.POJO;

public class user {
    private int uid;
    private String username;
    private String pwd;
    private String email;
    private String homeUrl;
    private String screenName;
    private int created;
    private int activated;
    private int logged;
    private String groupName;

    @Override
    public String toString() {
        return "user{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", email='" + email + '\'' +
                ", homeUrl='" + homeUrl + '\'' +
                ", screenName='" + screenName + '\'' +
                ", created=" + created +
                ", activated=" + activated +
                ", logged=" + logged +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public int getLogged() {
        return logged;
    }

    public void setLogged(int logged) {
        this.logged = logged;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public user() {
    }

    public user(int uid, String username, String pwd, String email, String homeUrl, String screenName, int created, int activated, int logged, String groupName) {
        this.uid = uid;
        this.username = username;
        this.pwd = pwd;
        this.email = email;
        this.homeUrl = homeUrl;
        this.screenName = screenName;
        this.created = created;
        this.activated = activated;
        this.logged = logged;
        this.groupName = groupName;
    }
}
