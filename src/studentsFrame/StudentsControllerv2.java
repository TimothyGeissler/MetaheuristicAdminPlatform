package studentsFrame;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.root.DatabaseSettings;
import com.root.Functions;
import com.root.students;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class StudentsControllerv2 implements Initializable {

    private Functions functions = new Functions();
    private DatabaseSettings db = new DatabaseSettings();

    // observable list is a listenable element, used to save table data
    private ObservableList<students> studentsObservableList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane studentsAnchorPane;

    @FXML
    private TableView<students> studentsTable;

    @FXML
    private TableColumn<students, String> studentID_column;

    @FXML
    private TableColumn<students, String> firstname_column;

    @FXML
    private TableColumn<students, String> surname_column;

    @FXML
    private TableColumn<students, String> grade_column;

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
    private JFXTextField gradeTxtField;

    @FXML
    private JFXTextField classTxtField;

    @FXML
    private JFXTextField teacherTxtField;

    @FXML
    private JFXTextField searchTxtField;

    @FXML
    private Button addStudentButton;

    @FXML
    private Button deleteStudentButton;

    @FXML
    private Button closeButton;

    @FXML
    private FontAwesomeIcon backIcon;

    @FXML
    private Button editStudentButton;

    @FXML
    private Button searchButton;

    @FXML
    private FontAwesomeIcon searchIcon;

    @FXML
    private JFXComboBox<String> fieldSearchList;

    @FXML
    private Button clearButton;

    @FXML
    private FontAwesomeIcon clearIcon;


    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == closeButton) {
            //close window
            ((Node) (event.getSource())).getScene().getWindow().hide();
            functions.loadStage("/mainFrame/main.fxml", "Home Screen - User: " + functions.getUsername());
        } else if (event.getSource() == addStudentButton) {
            //Check if user is admin (null-safe)
            if (Objects.equals(functions.getUsername(), "admin")) {
                System.out.println("Can change details - admin");
                String StudentID = studentIDTxtField.getText(), firstname = firstnameTxtField.getText(), surname = surnameTxtField.getText(), StudentClass = classTxtField.getText(), grade = gradeTxtField.getText(), teacher = teacherTxtField.getText();
                //Make sure grade is an integer (to work with auto-update function)
                try {
                    Integer.parseInt(grade);
                    System.out.println(grade + " can be parsed as int...");
                    //Add students obj to observablelist & set to tableview
                    studentsObservableList.add(new students(StudentID, firstname, surname, StudentClass, grade, teacher));
                    System.out.println(studentsObservableList.toString());
                    studentsTable.setItems(studentsObservableList);
                    //Save student-teacher relationship in student-teacher table
                    String sql = "INSERT INTO " + db.getStudentTeacherTable() + " (TeacherID, StudentID) VALUES ('" + teacher + "','" + StudentID + "');";
                    System.out.println("Insert student: " + sql);
                    Functions.query(sql);
                    System.out.println("Success");
                    //Save information in students table
                    Functions.query("INSERT INTO " + db.getStudentTable() + " (StudentID, StudentName, StudentSurname, StudentClass, StudentGrade) VALUES ('" + StudentID + "','" + firstname + "','" + surname + "','" + StudentClass + "','" + grade + "');");
                    System.out.println("Success");
                } catch (Exception ex) {
                    //parseInt failed
                    System.out.println(grade + " cannot be parsed as int!");
                    Functions.setAlertMessage("The grade field can only contain numbers!");
                    functions.loadStage("/alertFrame/alert.fxml", "Error");
                }
            } else {
                //Is not admin
                System.out.println("Not admin - cannot change students");
                Functions.setAlertMessage("Only the Admin can change student data!");
                functions.loadStage("/alertFrame/alert.fxml", "Error");
            }
            //fetch & add students to SQL DB
        } else if (event.getSource() == deleteStudentButton) {
            //Check if user is admin
            if (functions.getUsername().equals("admin")) {
                //Delete student
                int rowIndex = studentsTable.getSelectionModel().getSelectedIndex();
                //Get studentID of student to delete
                String deleteStudentID = studentsTable.getSelectionModel().getSelectedItem().getStudentID();
                System.out.println("Index: " + rowIndex);
                //Delete from SQL database (mySQL)
                //Functions.query("DELETE student_teacher, students_table FROM students_table INNER JOIN student_teacher " + "ON students_table.StudentID = student_teacher.StudentID WHERE students_table.StudentID = " + deleteStudentID + ";");
                deleteFromBothTables(rowIndex);
                //Delete from ObservableList
                studentsObservableList.remove(rowIndex);
            } else {
                //Is not admin
                System.out.println("Not admin - cannot change students");
                Functions.setAlertMessage("Only the Admin can change student data!");
                functions.loadStage("/alertFrame/alert.fxml", "Error");
            }
            //delete selected row from DB
        } else if (event.getSource() == editStudentButton) {
            int rowIndex = studentsTable.getSelectionModel().getSelectedIndex();
            //Delete from sql
            //String mySQLdelQuery = "DELETE student_teacher, students_table FROM students_table INNER JOIN student_teacher " + "ON students_table.StudentID = student_teacher.StudentID WHERE students_table.StudentID = " + studentsObservableList.get(rowIndex).getStudentID() + ";";
            //SQLite can't inner join w/ delete
            deleteFromBothTables(Integer.parseInt(studentsObservableList.get(rowIndex).getStudentID()));
            studentsObservableList.remove(rowIndex);
            students updateStudent = new students(studentIDTxtField.getText(), firstnameTxtField.getText(), surnameTxtField.getText(), classTxtField.getText(), gradeTxtField.getText(), teacherTxtField.getText());
            studentsObservableList.add(updateStudent);
            //Save update to DB
            //Save student-teacher relationship in student-teacher table
            String sqlinsert1 = "INSERT INTO " + db.getStudentTeacherTable() + " (TeacherID, StudentID) VALUES ('" + teacherTxtField.getText() + "','" + studentIDTxtField.getText() + "');";
            System.out.println("Edit query 1: " + sqlinsert1);
            Functions.query(sqlinsert1);
            System.out.println("Success");
            //Save information in students table
            String sqlinsert2 = "INSERT INTO " + db.getStudentTable() + " (StudentID, StudentName, StudentSurname, StudentClass, StudentGrade) VALUES ('" +studentIDTxtField.getText() + "','" + firstnameTxtField.getText() + "','" + surnameTxtField.getText() + "','" + classTxtField.getText() + "','" + gradeTxtField.getText() + "');";
            System.out.println("Edit query 2: " + sqlinsert2);
            Functions.query(sqlinsert2);
            System.out.println("Success");

        } else if (event.getSource() == searchButton) {
            //ensure that the search column has been defined in the dropdown list
            String searchColumn = fieldSearchList.getValue();

            if (searchColumn != null) {
                //Can search
                String searchText = searchTxtField.getText();
                System.out.println("To search: " + searchText + ", @ " + searchColumn);
                //Iterate through ObservableList
                ArrayList<students> resultIndex = new ArrayList<>();
                for (int i = 0; i < studentsObservableList.size(); i++) {
                    //Run if statement for each potential value of searchColumn
                    if ((searchColumn.equals("StudentID") && studentsObservableList.get(i).getStudentID().equals(searchText)) ||
                            (searchColumn.equals("Firstname") && studentsObservableList.get(i).getFirstname().equals(searchText)) ||
                            (searchColumn.equals("Surname") && studentsObservableList.get(i).getSurname().equals(searchText)) ||
                            (searchColumn.equals("Class") && studentsObservableList.get(i).getClassA().equals(searchText)) ||
                            (searchColumn.equals("Teacher") && studentsObservableList.get(i).getTeacher().equals(searchText)) ||
                            searchColumn.equals("Grade") && studentsObservableList.get(i).getGrade().equals(searchText)) {
                        resultIndex.add(studentsObservableList.get(i));
                        System.out.println("Result Index: " + resultIndex);
                    }
                }
                //studentsTable.getSelectionModel().selectIndices(1,3);
            } else {
                //User needs to set column to search by
                System.out.println("Need to specify column to search by! - 'Search Column: " + searchColumn + "'");
                //Display alert dialog
                Functions.setAlertMessage("Please select a column to search by in the drop-down list!");
                functions.loadStage("alertFrame/alert.fxml", "Cannot Search");
            }
        } else if (event.getSource() == clearButton) {
            //Clear txtfields
            System.out.println("cls all txtfields...");
            studentIDTxtField.clear();
            firstnameTxtField.clear();
            surnameTxtField.clear();
            teacherTxtField.clear();
            gradeTxtField.clear();
            classTxtField.clear();
        }
    }

    //Create separate method, code is used in DELETE and EDIT buttons
    public void deleteFromBothTables(int studentID) {
        String SQLite = "DELETE FROM " + db.getStudentTeacherTable() + " WHERE StudentID = " + studentID + ";", SQLite2 = "DELETE FROM " + db.getStudentTable() + " WHERE StudentID = " + studentID + ";";
        System.out.println("DELETE query 1: " + SQLite + "\nDELETE query 2: " + SQLite2);
        Functions.query(SQLite);
        Functions.query(SQLite2);
        //String deletequery = "DELETE FROM students_table WHERE StudentGrade = " + studentsObservableList.get(rowIndex).getStudentID() + ";";
        //Functions.query(deletequery);
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
            gradeTxtField.setText(selectedStudent.getGrade());
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
        grade_column.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        teacher_column.setCellValueFactory(cellData -> cellData.getValue().teacherProperty());
        //Inner join student and student_teacher tables
        String students_table = db.getStudentTable(), student_teacher = db.getStudentTeacherTable();
        System.out.println("students_table = " + students_table + "\nstudent_teacher = " + student_teacher);
        String [] columns = {"StudentID", "StudentName", "StudentSurname", "StudentClass", "StudentGrade", "TeacherID", "StudentID"};
        String studentsListQuery = "SELECT " +  students_table + ".StudentID, " + students_table + ".StudentName, " +
                students_table + ".StudentSurname, " + students_table + ".StudentClass, " + students_table + ".StudentGrade, " +
                student_teacher + ".TeacherID FROM " + students_table + " INNER JOIN " + student_teacher + " ON " + students_table +
                ".StudentID = " + student_teacher + ".StudentID;";
        ArrayList<ArrayList<String>> queryResult = Functions.select(studentsListQuery, columns);
        System.out.println("SQL Result: " + queryResult.toString());
        //Add data to observableList
        //.get(column).get(row)
        for (int i = 0; i < queryResult.get(0).size(); i++) {
            String studentIdresult = queryResult.get(0).get(i), studentNameResult = queryResult.get(1).get(i), studentSurnameResult = queryResult.get(2).get(i),
            studentClassResult = queryResult.get(3).get(i), studentGradeResult = queryResult.get(4).get(i), studentTeacherResult = queryResult.get(5).get(i);
            studentsObservableList.add(new students(studentIdresult, studentNameResult, studentSurnameResult, studentClassResult, studentGradeResult, studentTeacherResult));
            System.out.println("ObservableList appended: " + studentsObservableList.toString());
        }
        studentsTable.setItems(studentsObservableList);
        //Init comboBox values
        fieldSearchList.getItems().addAll("StudentID", "Firstname", "Surname", "Class", "Grade", "Teacher");
        //Remove horizontal scrollbar

    }
}
