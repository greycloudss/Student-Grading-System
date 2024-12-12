package com.example.demo1.Exporter;

import com.example.demo1.Student;
import javafx.collections.ObservableList;

public interface StudentDataProcessor {
    ObservableList<Student> filterByGroup(ObservableList<Student> students, String group);
    ObservableList<Student> sortByName(ObservableList<Student> students, boolean ascending);
    ObservableList<Student> sortById(ObservableList<Student> students, boolean ascending);
}