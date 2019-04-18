package loginsettingsFrame;

import com.jfoenix.controls.JFXTextField;
import com.root.DatabaseSettings;
import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginSettingsController implements Initializable {

    private Functions functions = new Functions();
    public DatabaseSettings db = new DatabaseSettings();

    @FXML
    private JFXTextField databaseTxtField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;
    @FXML
    private JFXTextField studentsTableTxtField;
    @FXML
    private JFXTextField teachersTableTxtField;
    @FXML
    private JFXTextField teacherstudentTableTxtField;
    @FXML
    private JFXTextField academicTableTxtField;
    @FXML
    private JFXTextField personalisedTableTxtField;
    @FXML
    private JFXTextField timetableurlsTxtField;

    @FXML
    void handleButtonClicks(ActionEvent event) {
        if (event.getSource() == saveButton) {
            System.out.println("Save database info");
            if (!functions.fileExists("config.txt")) {
                //No pre-existing database config, create file
                System.out.println("Config.txt not found...");
                functions.createTxtFile("config.txt");
            }
            //Check no values are empty

            //Save data in array & add to global variable in functions
            String[] databaseInfo = new String[7];
            databaseInfo[0] = databaseTxtField.getText();
            db.setDatabase(databaseInfo[0]);
            databaseInfo[1] = studentsTableTxtField.getText();
            db.setStudentTable(databaseInfo[1]);
            databaseInfo[2] = teachersTableTxtField.getText();
            db.setTeacherTable(databaseInfo[2]);
            databaseInfo[3] = teacherstudentTableTxtField.getText();
            db.setStudentTeacherTable(databaseInfo[3]);
            databaseInfo[4] = academicTableTxtField.getText();
            db.setAcademicTable(databaseInfo[4]);
            databaseInfo[5] = personalisedTableTxtField.getText();
            db.setPersonalisedTable(databaseInfo[5]);
            databaseInfo[6] = timetableurlsTxtField.getText();
            db.setTimetableUrls(databaseInfo[6]);
            System.out.println(db.toString());
            //Iterate through array to pass into outBuffer
            for (String info: databaseInfo) {
                functions.addOutBuffer(info);
            }
            //Overwrite data
            functions.printFile("config.txt");
            functions.scanFile("config.txt");
            //Close
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else if (event.getSource() == closeButton) {
            //Close
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DB settings window initialized...");
        //Load existing DB info from functions class if file already exists
        if (functions.fileExists("config.txt")) {
            String databaseName = db.getDatabase(), studentsTable = db.getStudentTable(), teachersTable = db.getTeacherTable(), teacherStudentTable = db.getStudentTeacherTable(), academicTable = db.getAcademicTable(), personalisedTable = db.getPersonalisedTable(), timetableUrls = db.getTimetableUrls();
            System.out.println(databaseName + "\n" + studentsTable + "\n" + teachersTable + "\n" + teacherStudentTable + "\n" + academicTable + "\n" + personalisedTable + "\n" + timetableUrls);
            functions.scanFile("config.txt");
            databaseTxtField.setText(databaseName);
            studentsTableTxtField.setText(studentsTable);
            teachersTableTxtField.setText(teachersTable);
            teacherstudentTableTxtField.setText(teacherStudentTable);
            academicTableTxtField.setText(academicTable);
            personalisedTableTxtField.setText(personalisedTable);
            timetableurlsTxtField.setText(timetableUrls);
        }
    }
}
