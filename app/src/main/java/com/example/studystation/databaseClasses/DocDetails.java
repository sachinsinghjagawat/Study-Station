package com.example.studystation.databaseClasses;

import java.util.List;

public class DocDetails {

    String department;
    String section;
    String subject;
    String type;
    String nameOfPdf;
    String nameOfUser;
    double sumRating;
    double totalRating;
    String urlOfPdf;
    List <Question> discussion;
    List<String> users;

    public DocDetails() {
    }

    public DocDetails(String department, String section, String subject
            , String type, String nameOfPdf, String nameOfUser
            , double sumRating, double totalRating
            , String urlOfPdf, List<Question> discussion , List<String> users) {
        this.department = department;
        this.section = section;
        this.subject = subject;
        this.type = type;
        this.nameOfPdf = nameOfPdf;
        this.nameOfUser = nameOfUser;
        this.sumRating = sumRating;
        this.totalRating = totalRating;
        this.urlOfPdf = urlOfPdf;
        this.discussion = discussion;
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getSumRating() {
        return sumRating;
    }

    public void setSumRating(double sumRating) {
        this.sumRating = sumRating;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public String getUrlOfPdf() {
        return urlOfPdf;
    }

    public void setUrlOfPdf(String urlOfPdf) {
        this.urlOfPdf = urlOfPdf;
    }

    public List<Question> getDiscussion() {
        return discussion;
    }

    public void setDiscussion(List<Question> discussion) {
        this.discussion = discussion;
    }
}
