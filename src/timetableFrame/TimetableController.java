package timetableFrame;

import com.jfoenix.controls.JFXComboBox;
import com.root.DatabaseSettings;
import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TimetableController implements Initializable {

    Functions functions = new Functions();
    DatabaseSettings db = new DatabaseSettings();

    @FXML
    private JFXComboBox<String> studentsList;

    @FXML
    private Label lessonLabel;

    @FXML
    private Label lessonLabel1;

    @FXML
    private Label lessonLabel2;

    @FXML
    private Label lessonLabel3;

    @FXML
    private Label lessonLabel4;

    @FXML
    private Label lessonLabel5;

    @FXML
    private Label lessonLabel6;

    @FXML
    private Label lessonLabel7;

    @FXML
    private Label lessonLabel8;

    @FXML
    private Label lessonLabel9;

    @FXML
    private Label lessonLabel10;

    @FXML
    private Label lessonLabel11;

    @FXML
    private Label lessonLabel12;

    @FXML
    private Label lessonLabel13;

    @FXML
    private Label lessonLabel14;

    @FXML
    private Label lessonLabel15;

    @FXML
    private Label lessonLabel16;

    @FXML
    private Label lessonLabel17;

    @FXML
    private Label lessonLabel18;

    @FXML
    private Label lessonLabel19;

    @FXML
    private Label lessonLabel20;

    @FXML
    private Label lessonLabel21;

    @FXML
    private Label lessonLabel22;

    @FXML
    private Label lessonLabel23;

    @FXML
    private Label lessonLabel24;

    @FXML
    private Label lessonLabel25;

    @FXML
    private Label lessonLabel26;

    @FXML
    private Label lessonLabel27;

    @FXML
    private Label lessonLabel28;

    @FXML
    private Label lessonLabel29;

    @FXML
    private Button closeButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button calculateButton;


    @FXML
    void handleButtonClicks(ActionEvent event) {
        if (event.getSource() == closeButton) {
            System.out.println("Close timetableFrame");
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else if (event.getSource() == exportButton) {
            System.out.println("Export timetable");
        } else if (event.getSource() == calculateButton) {
            System.out.println("Calculate timetable");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TimetableFrame Initialised");
        //Set dropdown menu text
        ArrayList<String> studentList = new ArrayList<>();
        String [] columns = {"StudentName"};
        String students_table = db.getStudentTable(), student_teacher = db.getStudentTeacherTable();
        String selectQuery = "SELECT " + students_table + ".StudentName FROM " + students_table + " INNER JOIN " +
                student_teacher + " ON " + student_teacher + ".StudentID = " + students_table + ".StudentID WHERE " +
                student_teacher + ".TeacherID = '" + functions.getUsername() + "';";
        System.out.println("students_table = " + students_table + "\nstudent_teacher = " + student_teacher);
        ArrayList<ArrayList<String>> queryResult = Functions.selectQuery(selectQuery, columns);
        System.out.println("SQL SELECT Result - > " + queryResult);
        if (queryResult.size() == 1) {
            System.out.println("No students found...Add students");
        } else {
            System.out.println("Iterating through SQL result ArrayList...");
            for (int i = 0; i < queryResult.size(); i++) {
                studentList.add(queryResult.get(0).get(i));
            }
            //Load URLS from SQL
        }
    }
}
