package com.example.studystation.databaseClasses;

public class SubjectName {

    String urlOfImage;
    String nameOfSubject;

    public SubjectName() {
    }

    public SubjectName(String urlOfImage, String nameOfSubject) {
        this.urlOfImage = urlOfImage;
        this.nameOfSubject = nameOfSubject;
    }

    public String getUrlOfImage() {
        return urlOfImage;
    }

    public void setUrlOfImage(String urlOfImage) {
        this.urlOfImage = urlOfImage;
    }

    public String getNameOfSubject() {
        return nameOfSubject;
    }

    public void setNameOfSubject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }
}
