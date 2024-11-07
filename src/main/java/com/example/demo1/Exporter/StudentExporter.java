package com.example.demo1.Exporter;

import com.example.demo1.Student;
import javafx.scene.control.TableView;

public class StudentExporter {
    private final ExportCSV csvExporter;
    private final ExportPDF pdfExporter;

    public StudentExporter() {
        this.csvExporter = new ExportCSV();
        this.pdfExporter = new ExportPDF();
    }

    public void exportToCSV(TableView<Student> table, String file) {
        csvExporter.export(table, file);
    }

    public void exportToPDF(TableView<Student> table, String file) {
        pdfExporter.export(table, file);
    }
}
