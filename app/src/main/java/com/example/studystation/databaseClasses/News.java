package com.example.studystation.databaseClasses;

public class News {

    String heading;
    String imageUrl;
    String type;
    String link;

    public News() {
    }

    public News(String heading, String imageUrl, String type, String link) {
        this.heading = heading;
        this.imageUrl = imageUrl;
        this.type = type;
        this.link = link;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
