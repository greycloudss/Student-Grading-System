package com.example.demo1.Exporter;

import com.example.demo1.Student;

import java.util.List;

public class StudentExporter {
    private final ExportCSV csvExporter;
    private final ExportPDF pdfExporter;

    public StudentExporter(List<Student> students) {
        this.csvExporter = new ExportCSV(students) {};
        this.pdfExporter = new ExportPDF(students) {};
    }

    public void exportToCSV(String fileName) {
        csvExporter.exportToCSV(fileName);
    }

    public void exportToPDF(String fileName) {
        pdfExporter.exportToPDF(fileName);
    }
}
