package com.example.e_connect.upload_post;

public class QueryPostModel {
    private String title;
    private String text;
    private long timestamp;  // Time of the post
    private int likes;
    private int comments;

    public QueryPostModel() {
        // Default constructor required for calls to DataSnapshot.getValue(QueryPostModel.class)
    }

    public QueryPostModel(String title, String text, long timestamp, int likes, int comments) {
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
        this.likes = likes;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }
}
