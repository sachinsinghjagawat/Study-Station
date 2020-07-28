package com.example.studystation.databaseClasses;

import java.util.List;

public class Note {
    String title;
    String content;
    String url;

    public Note() {
    }

    public Note(String title, String content , String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
