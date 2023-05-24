package com.juity.blog.DTO.cond;

public class userCond {
    private String username;
    private String pwd;

    public userCond(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.pwd;
    }

    public void setPassword(String password) {
        this.pwd = password;
    }
}
