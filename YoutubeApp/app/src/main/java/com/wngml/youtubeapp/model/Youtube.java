package com.wngml.youtubeapp.model;

public class Youtube {
    public String title;
    public String description;
    public String imgUrl;
    public String videoId;

    public Youtube(String title, String description, String imgUrl, String videoId) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.videoId = videoId;
    }
}
