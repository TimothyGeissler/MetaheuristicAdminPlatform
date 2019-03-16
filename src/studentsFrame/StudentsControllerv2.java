package studentsFrame;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.root.DatabaseSettings;
import com.root.Functions;
import com.root.students;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentsControllerv2 implements Initializable {

    private Functions functions = new Functions();
    private DatabaseSettings db = new DatabaseSettings();

    // observable list is a listenable element, used to save table data
    private ObservableList<students> studentsObservableList = FXCollections.observableArrayList();

    @FXML
    private TableView<students> studentsTable;
    @FXML
    private TableColumn<students, String> studentID_column;
    @FXML
    private TableColumn<students, String> firstname_column;
    @FXML
    private TableColumn<students, String> surname_column;
    @FXML
    private TableColumn<students, String> class_column;
    @FXML
    private TableColumn<students, String> teacher_column;
    @FXML
    private JFXTextField studentIDTxtField;
    @FXML
    private JFXTextField firstnameTxtField;
    @FXML
    private JFXTextField surnameTxtField;
    @FXML
    private JFXTextField teacherTxtField;
    @FXML
    private JFXTextField classTxtField;
    @FXML
    private Button addStudentButton;
    @FXML
    private Button deleteStudentButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button editStudentButton;
    @FXML
    private Button searchButton;
    @FXML
    private JFXTextField searchTxtField;
    @FXML
    private JFXComboBox<String> fieldSearchList;


    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == closeButton) {
            //close window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else if (event.getSource() == addStudentButton) {
            //Check if user is admin (null-safe)
            if (Objects.equals(functions.getUsername(), "admin")) {
                System.out.println("Can change details - admin");
                String StudentID = studentIDTxtField.getText(), firstname = firstnameTxtField.getText(), surname = surnameTxtField.getText(), StudentClass = classTxtField.getText(), teacher = teacherTxtField.getText();
                //Add students obj to observablelist & set to tableview
                studentsObservableList.add(new students(StudentID, firstname, surname, StudentClass, teacher));
                System.out.println(studentsObservableList.toString());
                studentsTable.setItems(studentsObservableList);
                //Save student-teacher relationship in student-teacher table
                Functions.insdelQuery("INSERT INTO " + db.getStudentTeacherTable() + "(TeacherID, StudentID) VALUES ('" + teacher + "','" + StudentID + "');");
                System.out.println("Success");
                //Save information in students table
                Functions.insdelQuery("INSERT INTO " + db.getStudentTable() + " (StudentID, StudentName, StudentSurname, StudentClass) VALUES ('" + StudentID + "','" + firstname + "','" + surname + "','" + StudentClass + "');");
                System.out.println("Success");
            } else {
                //Is not admin
                System.out.println("Not admin - cannot change students");
                Functions.setAlertMessage("Only the Admin can change student data!");
                functions.loadStage("/notificationFrame/notification.fxml", "Error");
            }
            //fetch & add students to SQL DB
        } else if (event.getSource() == deleteStudentButton) {
            //Check if user is admin
            if (Objects.equals(functions.getUsername(), "admin")) {
                //Delete student
                int rowIndex = studentsTable.getSelectionModel().getSelectedIndex();
                //Get studentID of student to delete
                String deleteStudentID = studentsTable.getSelectionModel().getSelectedItem().getStudentID();
                System.out.println("Index: " + rowIndex);
                //Delete from ObservableList
                studentsObservableList.remove(rowIndex);
                //Delete from SQL database
                Functions.insdelQuery("DELETE student_teacher, students_table FROM students_table INNER JOIN student_teacher " +
                        "ON students_table.StudentID = student_teacher.StudentID WHERE students_table.StudentID = " + deleteStudentID + ";");
            } else {
                //Is not admin
                System.out.println("Not admin - cannot change students");
                Functions.setAlertMessage("Only the Admin can change student data!");
                functions.loadStage("/notificationFrame/notification.fxml", "Error");
            }
            //delete selected row from DB
        } else if (event.getSource() == editStudentButton) {
            int rowIndex = studentsTable.getSelectionModel().getSelectedIndex();
            //Delete from sql
            Functions.insdelQuery("DELETE student_teacher, students_table FROM students_table INNER JOIN student_teacher " +
                    "ON students_table.StudentID = student_teacher.StudentID WHERE students_table.StudentID = " + studentsObservableList.get(rowIndex).getStudentID() + ";");
            studentsObservableList.remove(rowIndex);
            students updateStudent = new students(studentIDTxtField.getText(), firstnameTxtField.getText(), surnameTxtField.getText(), classTxtField.getText(), teacherTxtField.getText());
            studentsObservableList.add(updateStudent);
            //Save update to DB
            //Save student-teacher relationship in student-teacher table
            Functions.insdelQuery("INSERT INTO " + db.getStudentTeacherTable() + "(TeacherID, StudentID) VALUES ('" + teacherTxtField.getText() + "','" + studentIDTxtField.getText() + "');");
            System.out.println("Success");
            //Save information in students table
            Functions.insdelQuery("INSERT INTO " + db.getStudentTable() + " (StudentID, StudentName, StudentSurname, StudentClass) VALUES ('" +studentIDTxtField.getText() + "','" + firstnameTxtField.getText() + "','" + surnameTxtField.getText() + "','" + classTxtField.getText() + "');");
            System.out.println("Success");
        } else if (event.getSource() == searchButton) {
            //ensure that the search column has been defined in the dropdown list
            String searchColumn = fieldSearchList.getValue();

            if (searchColumn != null) {
                //Can search
                String searchText = searchTxtField.getText();
                System.out.println("To search: " + searchText + ", @ " + searchColumn);
                //Iterate through ObservableList
                ArrayList resultIndex = null;
                for (int i = 0; i < studentsObservableList.size(); i++) {
                    //Run if statement for each potential value of searchColumn
                    if ((searchColumn.equals("StudentID") && studentsObservableList.get(i).getStudentID().equals(searchText)) ||
                            (searchColumn.equals("Firstname") && studentsObservableList.get(i).getFirstname().equals(searchText)) ||
                            (searchColumn.equals("Surname") && studentsObservableList.get(i).getSurname().equals(searchText)) ||
                            (searchColumn.equals("Class") && studentsObservableList.get(i).getClassA().equals(searchText)) ||
                            (searchColumn.equals("Teacher") && studentsObservableList.get(i).getTeacher().equals(searchText))) {
                        resultIndex.add(i);
                        System.out.println("Result Index: " + resultIndex);
                    }
                }
                studentsTable.getSelectionModel().selectIndices(1,3);
            } else {
                //User needs to set column to search by
                System.out.println("Need to specify column to search by! - 'Search Column: " + searchColumn + "'");
                //Display alert dialog
                Functions.setAlertMessage("Please select a column to search by in the drop-down list!");
                functions.loadStage("notificationFrame/notification.fxml", "Cannot Search");
            }
        }
    }

    @FXML
    void handleMouseClicks(MouseEvent event) {
        try {
            students selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
            System.out.println("Selected row: " + selectedStudent.toString() +
                    "\nRow: " + studentsTable.getSelectionModel().getSelectedIndex());
            //Insert values of selected row into txtfields
            studentIDTxtField.setText(selectedStudent.getStudentID());
            firstnameTxtField.setText(selectedStudent.getFirstname());
            surnameTxtField.setText(selectedStudent.getSurname());
            classTxtField.setText(selectedStudent.getClassA());
            teacherTxtField.setText(selectedStudent.getTeacher());
        } catch (Exception ex) {
            System.out.println("Table ordering exception caught");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Init studentsWindow.fxml...");
        //Init cols w/ property value factory using lambda expression
        //Implementing the callback directly means the compiler will check for correct method name
        studentID_column.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty());
        firstname_column.setCellValueFactory(cellData -> cellData.getValue().firstnameProperty());
        surname_column.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        class_column.setCellValueFactory(cellData -> cellData.getValue().classProperty());
        teacher_column.setCellValueFactory(cellData -> cellData.getValue().teacherProperty());
        //Inner join student and student_teacher tables
        String students_table = db.getStudentTable(), student_teacher = db.getStudentTeacherTable();
        System.out.println("students_table = " + students_table + "\nstudent_teacher = " + student_teacher);
        String [] columns = {"StudentID", "StudentName", "StudentSurname", "StudentClass", "TeacherID", "StudentID"};
        String studentsListQuery = "SELECT " +  students_table + ".StudentID, " + students_table + ".StudentName, " +
                students_table + ".StudentSurname, " + students_table + ".StudentClass, " + student_teacher + ".TeacherID FROM " + students_table + " INNER JOIN " + student_teacher +
                " ON " + students_table + ".StudentID = " + student_teacher + ".StudentID;";
        ArrayList<ArrayList<String>> queryResult = Functions.selectQuery(studentsListQuery, columns);
        //Load list of students if present
        //String [] studentsTableColumns = {"StudentID", "StudentName", "StudentSurname", "StudentClass"};
        //String [] stud_teachTableColumns = {"TeacherID", "StudentID"};
        //ArrayList<ArrayList<String>> studentsResult = Functions.selectQuery("SELECT * FROM students_table", studentsTableColumns);
        //ArrayList<ArrayList<String>> stud_teachResult = Functions.selectQuery("SELECT * FROM student_teacher", stud_teachTableColumns);
        //System.out.println("ArrayList<ArrayList<String>> Students results: " + studentsResult.toString() + "\nArrayList<ArrayList<String>> StudentsTeacher results: " + stud_teachResult.toString());
        System.out.println("SQL Result: " + queryResult.toString());
        //Add data to observableList
        //.get(column).get(row)
        for (int i = 0; i < queryResult.get(0).size(); i++) {
            String studentIdresult = queryResult.get(0).get(i), studentNameResult = queryResult.get(1).get(i), studentSurnameResult = queryResult.get(2).get(i),
            studentClassResult = queryResult.get(3).get(i), studentTeacherResult = queryResult.get(4).get(i);
            studentsObservableList.add(new students(studentIdresult, studentNameResult, studentSurnameResult, studentClassResult, studentTeacherResult));
            System.out.println("ObservableList appended: " + studentsObservableList.toString());
        }
        studentsTable.setItems(studentsObservableList);
        //System.out.println(studentsObservableList.toString());
        //studentsTable.setItems(studentsObservableList);
        //Init comboBox values
        fieldSearchList.getItems().addAll("StudentID", "Firstname", "Surname", "Class", "Grade", "Teacher");
    }
}
