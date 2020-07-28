package com.example.studystation.databaseClasses;

import java.util.List;

public class SubjectList {

    String department;
    List <SubjectName> subjectNameList;

    public SubjectList() {
    }

    public SubjectList(String department, List<SubjectName> subjectNameList) {
        this.department = department;
        this.subjectNameList = subjectNameList;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<SubjectName> getSubjectNameList() {
        return subjectNameList;
    }

    public void setSubjectNameList(List<SubjectName> subjectNameList) {
        this.subjectNameList = subjectNameList;
    }
}
