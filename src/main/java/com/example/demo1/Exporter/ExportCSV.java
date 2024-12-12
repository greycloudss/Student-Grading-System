package com.example.demo1.Exporter;
import com.example.demo1.Student;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class ExportCSV extends AbstractExport {
    public ExportCSV(String file) {
        super(file);
    }

    @Override
    public void export(TableView<Student> table) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sanitizeFileName(file)))) {
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
        }
    }
}

