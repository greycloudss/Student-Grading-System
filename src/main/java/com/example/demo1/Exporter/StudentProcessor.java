package com.example.demo1.Exporter;

import com.example.demo1.Exporter.StudentDataProcessor;
import com.example.demo1.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class StudentProcessor implements StudentDataProcessor {
    @Override
    public ObservableList<Student> filterByGroup(ObservableList<Student> students, String group) {
        if (group == null || group.equalsIgnoreCase("All")) {
            return students;
        }
        ObservableList<Student> filtered = FXCollections.observableArrayList();
        for (Student student : students) {
            if (group.equals(student.getGroup())) {
                filtered.add(student);
            }
        }
        return filtered;
    }

    @Override
    public ObservableList<Student> sortByName(ObservableList<Student> students, boolean ascending) {
        ObservableList<Student> sorted = FXCollections.observableArrayList(students);
        Comparator<Student> comparator = Comparator.comparing(Student::getFirstName)
                .thenComparing(Student::getLastName);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        FXCollections.sort(sorted, comparator);
        return sorted;
    }

    @Override
    public ObservableList<Student> sortById(ObservableList<Student> students, boolean ascending) {
        ObservableList<Student> sorted = FXCollections.observableArrayList(students);
        Comparator<Student> comparator = Comparator.comparing(Student::getStudentId);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        FXCollections.sort(sorted, comparator);
        return sorted;
    }
}
