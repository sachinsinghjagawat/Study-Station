package com.example.studystation.databaseClasses;

import java.util.List;

public class User {

    String email;
    String department;
    String section;
    List <Favourite> favouriteList;
    List <Note> noteList;

    public User() {
    }

    public User(String email, String department, String section, List<Favourite> favouriteList, List<Note> noteList) {
        this.email = email;
        this.department = department;
        this.section = section;
        this.favouriteList = favouriteList;
        this.noteList = noteList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Favourite> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<Favourite> favouriteList) {
        this.favouriteList = favouriteList;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
