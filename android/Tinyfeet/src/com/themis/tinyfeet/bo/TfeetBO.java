package com.themis.tinyfeet.bo;

import com.themis.tinyfeet.proto.Tfeet;
import com.themis.tinyfeet.proto.User;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-13
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public class TfeetBO {
    private long tfId;
    private String userName;
    private String userId;
    private String userAvatarUrl;
    private String picUrl;
    private String text;
    private double latitude;
    private double longitude;
    private long createdAt;
    private int season;
    private int dayNight;
    private long likedCount;
    private long commentCount;

    public Tfeet.TFeet bo2Protobuf(){
        Tfeet.TFeet.Builder tfeet =  Tfeet.TFeet.newBuilder();
        tfeet.setTfid(this.getTfId());
        tfeet.setPicUrl(this.getPicUrl());
        tfeet.setText(this.getText());
        tfeet.setCreatedAt(this.getCreatedAt());

        User.UserSimple.Builder user = User.UserSimple.newBuilder();
        user.setUid(this.getUserId());
        user.setUname(this.getUserName());
        user.setProfileImageUrl(this.getUserAvatarUrl());
        tfeet.setUser(user);

        Tfeet.Geo.Builder geo = Tfeet.Geo.newBuilder();
        geo.setLat(this.getLatitude());
        geo.setLng(this.getLongitude());
        tfeet.setGeo(geo);

        tfeet.setSeason(Tfeet.Season.valueOf(this.getSeason()));
        tfeet.setDaynight(Tfeet.DayNight.valueOf(this.getDayNight()));
        return tfeet.build();
    }

    public void protobuf2BO(Tfeet.TFeet tfeet){
        this.setTfId(tfeet.getTfid());
        this.setPicUrl(tfeet.getPicUrl());
        this.setText(tfeet.getText());
        this.setCreatedAt(tfeet.getCreatedAt());
        this.setUserId(tfeet.getUser().getUid());
        this.setUserName(tfeet.getUser().getUname());
        this.setUserAvatarUrl(tfeet.getUser().getProfileImageUrl());
        this.setLatitude(tfeet.getGeo().getLat());
        this.setLongitude(tfeet.getGeo().getLng());
        this.setSeason(tfeet.getSeason().getNumber());
        this.setDayNight(tfeet.getDaynight().getNumber());
        this.setLikedCount(tfeet.getLikedCount());
        this.setCommentCount(tfeet.getCommentCount());
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getDayNight() {
        return dayNight;
    }

    public void setDayNight(int dayNight) {
        this.dayNight = dayNight;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(long likedCount) {
        this.likedCount = likedCount;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTfId() {
        return tfId;
    }

    public void setTfId(long tfId) {
        this.tfId = tfId;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
