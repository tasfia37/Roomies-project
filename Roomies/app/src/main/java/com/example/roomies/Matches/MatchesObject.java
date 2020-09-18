package com.example.roomies.Matches;

public class MatchesObject {

    private String userId;
    private String name;
    private String profileImageUrl;
    private String userSex;
    private String Department;
    private String Batch;

    public MatchesObject(String userId ,String name, String profileImageUrl,String userSex,String Department, String Batch) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.userSex = userSex;
        this.Department = Department;
        this.Batch = Batch;
    }
    public String getDepartment() {
        return Department;
    }
    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getBatch() {
        return Batch;
    }
    public void setBatch(String Batch) {
        this.Batch = Batch;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
