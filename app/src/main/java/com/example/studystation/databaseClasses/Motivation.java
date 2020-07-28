package com.example.studystation.databaseClasses;

import java.util.List;

public class Motivation {

    String imageUrl;
    double like;
    String content;
    String externalLink;
    List<String> users;

    public Motivation() {
    }

    public Motivation(String imageUrl, double like, String content, String externalLink , List<String> users) {
        this.imageUrl = imageUrl;
        this.like = like;
        this.content = content;
        this.externalLink = externalLink;
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLike() {
        return like;
    }

    public void setLike(double like) {
        this.like = like;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }
}
