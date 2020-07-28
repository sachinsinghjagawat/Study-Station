package com.example.studystation.databaseClasses;

import java.util.List;

public class Question {

    String question;
    List<String> ansList ;

    public Question() {
    }

    public Question(String question, List<String> ansList) {
        this.question = question;
        this.ansList = ansList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnsList() {
        return ansList;
    }

    public void setAnsList(List<String> ansList) {
        this.ansList = ansList;
    }
}
