package com.example.demo1;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class HelloController {
    File[] Student_files;



    // First field of the 2nd dimension is lastname; 2nd field is first name;
    String[][] Student_names;

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
    public RadioButton PDFile; // haha

    String curPath = System.getProperty("user.dir") + "\\students";


    @FXML
    public TableView table;


    @FXML
    private final AnimationTimer animTm = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateStudents();
        }
    };

    @FXML
    private void initialize() {
        if (!grFilter.isSelected()) {
            frmGrade.setDisable(true);
            toGrade.setDisable(true);
        }
        System.out.println(curPath);
        animTm.start();

        SortSel.getItems().addAll("Group", "Name", "Student ID");

        howSel.getItems().addAll("Ascending", "Descending");
        howSel.getItems().set(1, "Descending");
    }

    private void updateStudents() {
        File dir = new File(curPath);

        if (!dir.exists())
            dir.mkdir();

        Student_files = dir.listFiles();
        Student_names = cropper(Student_files);
    }

    public void onExitBTN() {
        System.exit(0);
    }

    public void exactSearch() {
        GroupSel.getItems();
        DateSel.getItems();
        SortSel.getItems();
        howSel.getItems();
    }

    public void onExportBtn() {
        /*
        *
        *   TBD: add exporting of file
        *   Formats: PDF, CSV
        *
        */
    }


    private String[][] cropper(File[] files) {
        String[][] trimmed = new String[files.length][4];
        //StudentID-LastName_firstName
        //First 2 letters of the StudentID is the group the student is in

        for (int i = 0; i < files.length; i++) {

            try {
                String temp = files[i].getName().substring(files[i].getName().indexOf("-") + 1, files[i].getName().indexOf("."));

                trimmed[i][0] = files[i].getName().substring(0, files[i].getName().indexOf("-"));

                trimmed[i][1] = temp.substring(0, temp.indexOf("_"));

                trimmed[i][2] = temp.substring(temp.indexOf("_") + 1);

                trimmed[i][3] = trimmed[i][0].substring(0, 2);
            } catch (Exception e) {}
        }


        return trimmed;
    }






    //----------------------------------------------------------------- useless button logic handling and such -----------------------------------------------------------------\\

    public void cb_listener() {
        if (grFilter.isSelected()) {
            toGrade.setDisable(false);
            frmGrade.setDisable(false);
        } else {
            toGrade.setDisable(true);
            frmGrade.setDisable(true);
        }
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