package com.example.demo1.Exporter;
import com.example.demo1.Student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class ExportCSV implements Export {
    protected List<Student> students;

    public ExportCSV(List<Student> students) {
        this.students = students;
    }

    @Override
    public void export(String fileName) {
        exportToCSV(fileName);
    }

    public void exportToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Student ID,First Name,Last Name,Group\n");

            for (Student student : students) {
                writer.write(student.getStudentId() + "," +
                        student.getFirstName() + "," +
                        student.getLastName() + "," +
                        student.group() + "\n");
            }
        } catch (IOException ignored) {}
    }
}
