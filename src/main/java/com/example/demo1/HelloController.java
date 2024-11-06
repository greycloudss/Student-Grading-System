package com.example.demo1;

import com.example.demo1.Exporter.StudentExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HelloController {
    @FXML
    public ComboBox<String> GroupSel;
    @FXML
    public ComboBox<String> DateSel;
    @FXML
    public ComboBox<String> SortSel;
    @FXML
    public ComboBox<String> howSel;
    @FXML
    public CheckBox grFilter;
    @FXML
    public TextField toGrade;
    @FXML
    public TextField frmGrade;
    @FXML
    public RadioButton CSVile;
    @FXML
    public RadioButton PDFile;
    @FXML
    public TableView<Student> table;
    @FXML
    public TableColumn<Student, String> firstNameCol;
    @FXML
    public TableColumn<Student, String> lastNameCol;
    @FXML
    public TableColumn<Student, String> groupCol;
    @FXML
    public TableColumn<Student, String> studentIDCol;

    ObservableList<Student> students;

    @FXML
    private void initialize() {
        grFilter.setSelected(false);
        toGrade.setDisable(true);
        frmGrade.setDisable(true);

        DateSel.setOnAction(event -> updateTable());

        students = FXCollections.observableArrayList();

        studentIDCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));

        updateTable();

        GroupSel.getItems().add("All");
        SortSel.getItems().addAll("Default", "Group", "Name", "Student ID");
        howSel.getItems().addAll("Ascending", "Descending");

        GroupSel.setOnAction(event -> filterAndSortTable());
        SortSel.setOnAction(event -> filterAndSortTable());
        howSel.setOnAction(event -> filterAndSortTable());
    }

    private void updateTable() {
        table.getColumns().removeIf(col -> col.getText().startsWith("Day"));

        students.clear();
        if (DateSel.getValue() != null) {
            students.addAll(parseCSV(DateSel.getValue() + ".csv"));
        }
        if (!students.isEmpty() && students.getFirst().days() != null) {
            Pair<Integer, Character>[] days = students.getFirst().getDays();
            int i = 0, n = days.length;

            try {
                if (!frmGrade.getText().isEmpty() && !toGrade.getText().isEmpty()) {
                    int frm = Integer.parseInt(frmGrade.getText());
                    int to = Integer.parseInt(toGrade.getText());

                    if (frm >= 0 && frm < to && to <= days.length) {
                        i = frm - 1;
                        n = to;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            for (; i < n; i++) {
                TableColumn<Student, String> gradeColumn = createGradeColumn(i);
                table.getColumns().add(gradeColumn);
            }
        }
        table.setItems(FXCollections.observableArrayList(students));
    }

    public void updateMonths() {
        DateSel.getItems().clear();
        String[] mon = dirParser();

        if (mon != null) {
            for (String s : mon)
                DateSel.getItems().add(s);
        }
    }

    private TableColumn<Student, String> createGradeColumn(int i) {
        final int index = i;
        TableColumn<Student, String> gradeColumn = new TableColumn<>("Day " + (i + 1));
        gradeColumn.setCellValueFactory(cellData -> {
            Pair<Integer, Character>[] grades = cellData.getValue().days();
            if (index < grades.length && grades[index] != null) {
                Pair<Integer, Character> grade = grades[index];
                return new SimpleStringProperty(grade.getKey() + "" + grade.getValue());
            } else {
                return new SimpleStringProperty("");
            }
        });
        return gradeColumn;
    }

    private String[] dirParser() {
        File currentDirectory = new File(".");
        File[] filesList = currentDirectory.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (filesList != null) {
            for (File file : filesList) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    fileNames.add(file.getName().replace(".csv", ""));
                }
            }
        }
        return fileNames.toArray(new String[0]);
    }

    private ArrayList<Student> parseCSV(String file) {
        ArrayList<Student> students = new ArrayList<>();
        HashSet<String> groups = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String studentId = parts[0].trim();
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String group = parts[3].trim();

                    Pair<Integer, Character>[] days = new Pair[parts.length - 4];
                    for (int i = 4; i < parts.length; i++) {
                        days[i - 4] = toGradePair(parts[i].trim());
                    }
                    students.add(new Student(studentId, firstName, lastName, group, days));

                    if (!groups.contains(group)) {
                        groups.add(group);
                        GroupSel.getItems().add(group);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    Pair<Integer, Character> toGradePair(String grade) {
        int i = 0;
        while (i < grade.length() && Character.isDigit(grade.charAt(i))) {
            i++;
        }
        int numericPart = i > 0 ? Integer.parseInt(grade.substring(0, i)) : -1;
        char charPart = (i < grade.length()) ? grade.charAt(i) : ' ';
        return new Pair<>(numericPart, charPart);
    }

    private void filterAndSortTable() {
        ObservableList<Student> filteredList = FXCollections.observableArrayList(students);

        String selectedGroup = GroupSel.getSelectionModel().getSelectedItem();
        if (selectedGroup != null && !selectedGroup.equals("All")) {
            filteredList.removeIf(student -> !student.group().equals(selectedGroup));
        }

        String sortCriteria = SortSel.getSelectionModel().getSelectedItem();
        boolean ascending = "Ascending".equals(howSel.getSelectionModel().getSelectedItem());

        filteredList.sort((s1, s2) -> {
            int result = 0;
            if ("Group".equals(sortCriteria)) {
                result = s1.group().compareTo(s2.group());
            } else if ("Name".equals(sortCriteria)) {
                result = s1.getFirstName().compareTo(s2.getFirstName());
            } else if ("Student ID".equals(sortCriteria)) {
                result = s1.getStudentId().compareTo(s2.getStudentId());
            }
            return ascending ? result : -result;
        });

        table.setItems(filteredList);
    }

    public void onExitBTN() {
        System.exit(0);
    }

    public void exactSearch() {
        updateTable();
    }

    public void onExportBtn() {
        ObservableList<Student> filteredData = table.getItems();
        List<Student> students = new ArrayList<>(filteredData);

        StudentExporter exporter = new StudentExporter(students);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Export File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File file = fileChooser.showSaveDialog(table.getScene().getWindow());

        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (filePath.endsWith(".csv")) {
                exporter.exportToCSV(filePath);
            } else if (filePath.endsWith(".pdf")) {
                exporter.exportToPDF(filePath);
            }
        }
    }

    public boolean cb_listener() {
        if (grFilter.isSelected()) {
            toGrade.setDisable(false);
            frmGrade.setDisable(false);
        } else {
            toGrade.setDisable(true);
            frmGrade.setDisable(true);
        }
        return grFilter.isSelected();
    }

    public void cb_filter() {
        howSel.setVisible(true);
    }

    public void handleRadioBtnCSV() {
        if (CSVile.isSelected())
            PDFile.setSelected(false);
    }

    public void handleRadioBtnPDF() {
        if (PDFile.isSelected())
            CSVile.setSelected(false);
    }
}
