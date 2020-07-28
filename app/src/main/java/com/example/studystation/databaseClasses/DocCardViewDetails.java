package com.example.studystation.databaseClasses;

public class DocCardViewDetails {

    String nameOfPdf;
    String nameOfUser;
    double rating;
    String urlOfImage;
    String urlOfPdf;

    public DocCardViewDetails() {
    }

    public DocCardViewDetails(String nameOfPdf, String nameOfUser, double rating, String urlOfImage, String urlOfPdf) {
        this.nameOfPdf = nameOfPdf;
        this.nameOfUser = nameOfUser;
        this.rating = rating;
        this.urlOfImage = urlOfImage;
        this.urlOfPdf = urlOfPdf;
    }

    public String getNameOfPdf() {
        return nameOfPdf;
    }

    public void setNameOfPdf(String nameOfPdf) {
        this.nameOfPdf = nameOfPdf;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrlOfImage() {
        return urlOfImage;
    }

    public void setUrlOfImage(String urlOfImage) {
        this.urlOfImage = urlOfImage;
    }

    public String getUrlOfPdf() {
        return urlOfPdf;
    }

    public void setUrlOfPdf(String urlOfPdf) {
        this.urlOfPdf = urlOfPdf;
    }
}
