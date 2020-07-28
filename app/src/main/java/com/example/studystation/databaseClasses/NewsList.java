package com.example.studystation.databaseClasses;

import java.util.List;

public class NewsList {

    List<News> newsList;

    public NewsList() {
    }

    public NewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
