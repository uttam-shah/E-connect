package com.example.e_connect.upload_post;

public class NotesPostModel {
    private String user_name;
    private String pdfUrl;
    private String imageUrl;
    private String stream;
    private String subject;
    private String description;
    private long timestamp;

    // Empty constructor for Firebase
    public NotesPostModel() {
    }

    public NotesPostModel(String user_name, String pdfUrl, String imageUrl, String stream, String subject, String description, long timestamp) {
        this.user_name = user_name;
        this.pdfUrl = pdfUrl;
        this.imageUrl = imageUrl;
        this.stream = stream;
        this.subject = subject;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


