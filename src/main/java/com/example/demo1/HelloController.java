package com.example.demo1;

import com.example.demo1.Exporter.StudentExporter;
import com.example.demo1.Exporter.StudentProcessor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

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


    @FXML
    public TextField AP_SID;
    @FXML
    public TextField AP_AT;
    @FXML
    public TextField AP_GR;
    @FXML
    public TextField AP_D;
    @FXML
    public TextField AP_GP;
    @FXML
    public CheckBox  AP_CB;
    @FXML
    public Text      AP_GR_T;
    @FXML
    public Text      AP_AT_T;
    @FXML
    public Text      AP_D_T;
    @FXML
    public Text      AP_GP_T;
    @FXML
    public Text      AP_SID_T;
    @FXML
    public Button    AP_BTN;

    private final StudentProcessor processor = new StudentProcessor();
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    private void setupToggleSortSelListener() {
        SortSel.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if ("Default".equals(newValue) || "Name".equals(newValue) || "Student ID".equals(newValue)) {
                    howSel.setVisible(true);
                } else {
                    howSel.setVisible(false);
                }
            }
        });
    }

    @FXML
    private void initialize() {
        grFilter.setSelected(false);
        toGrade.setDisable(true);
        frmGrade.setDisable(true);

        AP_CB.setSelected(false);
        AP_listener();

        DateSel.setOnAction(event -> updateTable());

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
        TableView<String> a;
        setupToggleSortSelListener();
    }

    public void updateTable() {
        table.getColumns().removeIf(col -> col.getText().startsWith("Day"));

        students.clear();
        if (DateSel.getValue() != null) {
            students.addAll(parseCSV(DateSel.getValue() + ".csv"));
        }
        if (!students.isEmpty() && students.getFirst().getDays() != null) {
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
        filterAndSortTable();
    }

    public void updateMonths() {
        DateSel.getItems().clear();
        String[] mon = dirParser();

        if (mon != null)
            for (String s : mon)
                DateSel.getItems().add(s);
    }

    private TableColumn<Student, String> createGradeColumn(int i) {
        final int index = i;
        TableColumn<Student, String> gradeColumn = new TableColumn<>("Day " + (i + 1));
        gradeColumn.setCellValueFactory(cellData -> {
            Pair<Integer, Character>[] grades = cellData.getValue().getDays();
            if (index < grades.length && grades[index] != null) {
                Pair<Integer, Character> grade = grades[index];
                return new SimpleStringProperty(grade.getKey() + " " + grade.getValue());
            } else {
                return new SimpleStringProperty(" ");
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

        while (i < grade.length() && Character.isDigit(grade.charAt(i)))
            i++;

        int numericPart = i > 0 ? Integer.parseInt(grade.substring(0, i)) : -1;
        char charPart = (i < grade.length()) ? grade.charAt(i) : ' ';
        return new Pair<>(numericPart, charPart);
    }

    private void filterAndSortTable() {
        ObservableList<Student> filteredList = processor.filterByGroup(students, GroupSel.getSelectionModel().getSelectedItem());

        String sortCriteria = SortSel.getSelectionModel().getSelectedItem();
        boolean ascending = "Ascending".equals(howSel.getSelectionModel().getSelectedItem());

        if ("Name".equals(sortCriteria)) {
            filteredList = processor.sortByName(filteredList, ascending);
        } else if ("Student ID".equals(sortCriteria)) {
            filteredList = processor.sortById(filteredList, ascending);
        }

        table.setItems(filteredList);
    }

    public void onExitBTN() {
        System.exit(0);
    }

    public void exactSearch() {
        updateTable();
    }

    public void onExportBtn() {
        StudentExporter exporter = new StudentExporter();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Export File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File file = fileChooser.showSaveDialog(table.getScene().getWindow());

        if (file != null) {
            exporter.export(table, file.getAbsolutePath());
        }
    }


    public void AP_ExactChange() {
        try {
            String sanitizedInput = AP_SID.getText().trim().replaceAll("[^0-9]", "");
            int inputStudentId = Integer.parseInt(sanitizedInput);

            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                int studentId = Integer.parseInt(student.getStudentId().trim().replaceAll("[^0-9]", ""));
                if (studentId == inputStudentId) {
                    if (!AP_GP.getText().isEmpty()) {
                        student.setGroup(AP_GP.getText().trim());
                    }

                    if (!AP_D.getText().isEmpty() && Integer.parseInt(AP_D.getText().trim()) > 0 &&
                            Integer.parseInt(AP_D.getText().trim()) - 1 <= 31) {
                        int dayInt = Integer.parseInt(AP_D.getText().trim()) - 1;
                        int mark = AP_GR.getText().isEmpty() ? 0 : Integer.parseInt(AP_GR.getText().trim());
                        char attendance = AP_AT.getText().isEmpty() ? ' ' : AP_AT.getText().trim().charAt(0);

                        Pair<Integer, Character>[] days = student.getDays();
                        if (days.length > dayInt) {
                            days[dayInt] = new Pair<>(mark, attendance);
                        } else {
                            student.addDay(new Pair<>(mark, attendance));
                        }
                        student.setDays(days);
                    }

                    students.set(i, student);
                    break;
                }
            }

            table.setItems(students);
            table.refresh();
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean AP_listener() {
        if (AP_CB.isSelected()) {
            AP_SID.setVisible(true);
            AP_AT.setVisible(true);
            AP_GR.setVisible(true);
            AP_D.setVisible(true);
            AP_GP.setVisible(true);
            AP_GR_T.setVisible(true);
            AP_AT_T.setVisible(true);
            AP_D_T.setVisible(true);
            AP_GP_T.setVisible(true);
            AP_SID_T.setVisible(true);
            AP_BTN.setVisible(true);

        } else {
            AP_SID.setVisible(false);
            AP_AT.setVisible(false);
            AP_GR.setVisible(false);
            AP_D.setVisible(false);
            AP_GP.setVisible(false);
            AP_GR_T.setVisible(false);
            AP_AT_T.setVisible(false);
            AP_D_T.setVisible(false);
            AP_GP_T.setVisible(false);
            AP_SID_T.setVisible(false);
            AP_BTN.setVisible(false);
        }
        return AP_CB.isSelected();
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
