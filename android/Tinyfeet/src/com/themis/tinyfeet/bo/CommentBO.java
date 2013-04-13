package com.themis.tinyfeet.bo;

import com.themis.tinyfeet.proto.Tfeet;
import com.themis.tinyfeet.proto.User;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-13
 * Time: 下午5:15
 * To change this template use File | Settings | File Templates.
 */
public class CommentBO {
    private String userName;
    private String userId;
    private String userAvatarUrl;
    private String text;
    private long createdAt;

    public Tfeet.Comment bo2ProtoBuf(){
        Tfeet.Comment.Builder comment = Tfeet.Comment.newBuilder();
        User.UserSimple.Builder user = User.UserSimple.newBuilder();
        user.setUname(this.getUserName());
        user.setUid(this.getUserId());
        user.setProfileImageUrl(this.getUserAvatarUrl());
        comment.setUserSimple(user);
        comment.setText(this.getText());
        comment.setCreatedAt(this.getCreatedAt());
        return comment.build();
    }

    public void protoBuf2BO(Tfeet.Comment comment){
        this.setUserId(comment.getUserSimple().getUid());
        this.setUserName(comment.getUserSimple().getUname());
        this.setUserAvatarUrl(comment.getUserSimple().getProfileImageUrl());
        this.setText(comment.getText());
        this.setCreatedAt(comment.getCreatedAt());
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
