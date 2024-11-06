package com.example.demo1;

import javafx.util.Pair;

public class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private String group;
    private Pair<Integer, Character>[] days;

    public Student(String studentId, String firstName, String lastName, String group, Pair<Integer, Character>[] days) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.days = days;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Pair<Integer, Character>[] getDays() {
        return days;
    }

    public void setDays(Pair<Integer, Character>[] days) {
        this.days = days;
    }

    public Pair<Integer, Character>[] days() {
        return days;
    }

    public String group() {
        return group;
    }
}
