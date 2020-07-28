package com.example.studystation.databaseClasses;

public class Favourite {
    String title;
    String url;

    public Favourite() {
    }

    public Favourite(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
