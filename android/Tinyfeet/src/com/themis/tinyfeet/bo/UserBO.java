package com.themis.tinyfeet.bo;

import com.themis.tinyfeet.proto.User;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-13
 * Time: 下午7:53
 * To change this template use File | Settings | File Templates.
 */
public class UserBO {
    private String uid;
    private String userName;
    private String userAvatarUrl;
    private String email;
    private String gender;
    private String location;
    private long likedCount;
    private long addedCount;
    private long createdAt;

    public User.UserDetail bo2ProtoBuf(){
        User.UserDetail.Builder userDetail = User.UserDetail.newBuilder();
        userDetail.setUid(this.getUid());
        userDetail.setUname(this.getUserName());
        userDetail.setProfileImageUrl(this.getUserAvatarUrl());
        userDetail.setEmail(this.getEmail());
        userDetail.setGender(this.getGender());
        userDetail.setLocation(this.getLocation());
        userDetail.setLikedCount(this.getLikedCount());
        userDetail.setAddedCount(this.getAddedCount());
        userDetail.setCreatedAt(this.getCreatedAt());
        return userDetail.build();
    }

    public void protoBuf2BO(User.UserDetail userDetail){
        this.setUid(userDetail.getUid());
        this.setUserName(userDetail.getUname());
        this.setUserAvatarUrl(userDetail.getProfileImageUrl());
        this.setEmail(userDetail.getEmail());
        this.setGender(userDetail.getGender());
        this.setLocation(userDetail.getLocation());
        this.setLikedCount(userDetail.getLikedCount());
        this.setAddedCount(userDetail.getAddedCount());
        this.setCreatedAt(userDetail.getCreatedAt());
    }

    public long getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(long addedCount) {
        this.addedCount = addedCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(long likedCount) {
        this.likedCount = likedCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
