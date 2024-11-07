package com.example.demo1.Exporter.Export;

import com.example.demo1.Student;
import javafx.scene.control.TableView;

import java.io.IOException;

public interface Export {
    void export(TableView<Student> table, String file) throws IOException;
}
