package com.softwareTest.timeline.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.softwareTest.timeline.Utility.JsonVisibilityLevel;

import java.util.Date;

public class UserInfo {
    private Integer userId;

    private String username;

    private String displayName;

    private String userPassword;

    private String salt;

    private Date lastLogin;

    public UserInfo(Integer userId, String username, String displayName, String userPassword, String salt, Date lastLogin) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.userPassword = userPassword;
        this.salt = salt;
        this.lastLogin = lastLogin;
    }

    public UserInfo() {
        super();
    }

    @JsonView(JsonVisibilityLevel.AbstractView.class)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonView(JsonVisibilityLevel.NormalView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @JsonView(JsonVisibilityLevel.NormalView.class)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    @JsonView(JsonVisibilityLevel.DetailedView.class)
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    @JsonView(JsonVisibilityLevel.DetailedView.class)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    @JsonView(JsonVisibilityLevel.BasicView.class)
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}