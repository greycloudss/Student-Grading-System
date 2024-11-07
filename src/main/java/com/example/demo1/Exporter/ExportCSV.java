package com.example.demo1.Exporter;
import com.example.demo1.Exporter.Export.Export;
import com.example.demo1.Student;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExportCSV implements Export {

    @Override
    public void export(TableView<Student> table, String file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (TableColumn<Student, ?> column : table.getColumns()) {
                writer.write(column.getText() + ",");
            }
            writer.newLine();

            for (Student student : table.getItems()) {
                for (TableColumn<Student, ?> column : table.getColumns()) {
                    Object cellData = column.getCellData(student);
                    writer.write((cellData == null ? "" : cellData.toString()) + ",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}