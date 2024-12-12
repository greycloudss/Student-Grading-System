package com.example.demo1.Exporter;

import com.example.demo1.Student;
import javafx.scene.control.TableView;

import java.io.IOException;

public abstract class AbstractExport {
    protected String file;

    public AbstractExport(String file) {
        this.file = file;
    }

    public abstract void export(TableView<Student> table) throws IOException;

    protected String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }
}