package com.example.studystation.databaseClasses;

import java.util.List;

public class MotivationList {

    List<Motivation> motivationContent;

    public MotivationList() {
    }

    public MotivationList(List<Motivation> motivationContent) {
        this.motivationContent = motivationContent;
    }

    public List<Motivation> getMotivationContent() {
        return motivationContent;
    }

    public void setMotivationContent(List<Motivation> motivationContent) {
        this.motivationContent = motivationContent;
    }
}
