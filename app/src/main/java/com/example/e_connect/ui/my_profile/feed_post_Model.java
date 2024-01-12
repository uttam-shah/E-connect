package com.example.e_connect.ui.my_profile;

public class feed_post_Model {
    private String imageUrl;
    private String subtitle;
    private String userName;
    private long postTime;
    private int likeCount;
    private int comments;

    public feed_post_Model() {
        // Default constructor required for Firebase
    }

    public feed_post_Model(String imageUrl, String subtitle, String userName, long postTime, int likeCount, int comments) {
        this.imageUrl = imageUrl;
        this.subtitle = subtitle;
        this.userName = userName;
        this.postTime = postTime;
        this.likeCount = likeCount;
        this.comments = comments;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }


}
