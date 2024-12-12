package com.example.demo1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;

public class Student {
    private StringProperty studentId;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty group;
    private ObjectProperty<Pair<Integer, Character>[]> days;

    public Student(String studentId, String firstName, String lastName, String group, Pair<Integer, Character>[] days) {
        this.studentId = new SimpleStringProperty(studentId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.group = new SimpleStringProperty(group);
        this.days = new SimpleObjectProperty<>(days);
    }

    public String getStudentId() {
        return studentId.get();
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public StringProperty studentIdProperty() {
        return studentId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getGroup() {
        return group.get();
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    public StringProperty groupProperty() {
        return group;
    }

    public Pair<Integer, Character>[] getDays() {
        return days.get();
    }

    public void setDays(Pair<Integer, Character>[] days) {
        this.days.set(days);
    }

    public ObjectProperty<Pair<Integer, Character>[]> daysProperty() {
        return days;
    }

    public void addDay(Pair<Integer, Character> day) {
        Pair<Integer, Character>[] tmp = new Pair[days.get().length + 1];
        System.arraycopy(days.get(), 0, tmp, 0, days.get().length);
        tmp[tmp.length - 1] = day;
        days.set(tmp);
    }
}
