package com.example.demo1.Exporter;

import com.example.demo1.Student;
import javafx.scene.control.TableView;

public class StudentExporter {

    public void export(TableView<Student> table, String file) {
        AbstractExport exporter;

        if (file.endsWith(".csv")) {
            exporter = new ExportCSV(file);
        } else if (file.endsWith(".pdf")) {
            exporter = new ExportPDF(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Use .csv or .pdf");
        }

        try {
            exporter.export(table);
        } catch (Exception e) {
            throw new RuntimeException("Export failed: " + e.getMessage(), e);
        }
    }
}
