package com.example.studystation.databaseClasses;

import java.util.List;

public class DocList {

    String department;
    String section;
    String subject;
    String type;
    List <DocCardViewDetails> docCardViewDetailsList;

    public DocList() {
    }

    public DocList(String department, String section, String subject, String type, List<DocCardViewDetails> docCardViewDetailsList) {
        this.department = department;
        this.section = section;
        this.subject = subject;
        this.type = type;
        this.docCardViewDetailsList = docCardViewDetailsList;
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

    public List<DocCardViewDetails> getDocCardViewDetailsList() {
        return docCardViewDetailsList;
    }

    public void setDocCardViewDetailsList(List<DocCardViewDetails> docCardViewDetailsList) {
        this.docCardViewDetailsList = docCardViewDetailsList;
    }
}
