package com.example.demo1.Exporter;

import com.example.demo1.Exporter.Export.Export;
import com.example.demo1.Student;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.FileNotFoundException;

public class ExportPDF implements Export {

    @Override
    public void export(TableView<Student> table, String file) {
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Table pdfTable = new Table(table.getColumns().size());

            for (TableColumn<Student, ?> column : table.getColumns()) {
                pdfTable.addCell(column.getText());
            }

            for (Student student : table.getItems()) {
                for (TableColumn<Student, ?> column : table.getColumns()) {
                    Object cellData = column.getCellData(student);
                    pdfTable.addCell(cellData == null ? "" : cellData.toString());
                }
            }

            document.add(pdfTable);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}