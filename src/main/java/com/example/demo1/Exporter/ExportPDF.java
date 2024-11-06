package com.example.demo1.Exporter;

import com.example.demo1.Student;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import java.io.FileNotFoundException;
import java.util.List;

public abstract class ExportPDF implements Export {
    protected List<Student> students;

    public ExportPDF(List<Student> students) {
        this.students = students;
    }

    @Override
    public void export(String fileName) {
        exportToPDF(fileName);
    }

    public void exportToPDF(String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Table table = new Table(4);
            table.addCell("Student ID");
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("Group");

            for (Student student : students) {
                table.addCell(student.getStudentId());
                table.addCell(student.getFirstName());
                table.addCell(student.getLastName());
                table.addCell(student.group());
            }

            document.add(table);
            document.close();
        } catch (FileNotFoundException ignored) {}
    }
}
