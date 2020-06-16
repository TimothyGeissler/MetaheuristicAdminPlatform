package studentsFrame;

import com.jfoenix.controls.JFXTextField;
import com.root.Functions;
import com.root.Student;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentsControllerv2 implements Initializable {

    private Functions functions = new Functions();

    // observable list is a listenable element, used to save table data
    private ObservableList<Student> studentObservableList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane studentsAnchorPane;

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private TableColumn<Student, String> studentID_column;

    @FXML
    private TableColumn<Student, String> firstname_column;

    @FXML
    private TableColumn<Student, String> surname_column;

    @FXML
    private TableColumn<Student, String> grade_column;

    @FXML
    private TableColumn<Student, String> class_column;

    @FXML
    private TableColumn<Student, String> teacher_column;

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
    private FontAwesomeIcon studentsBackIcon;

    @FXML
    private Button editStudentButton;

    @FXML
    private Button clearButton;

    @FXML
    private FontAwesomeIcon clearStudentsIcon;


    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == closeButton) {
            //close window
            functions.closeWindow(event.getSource());
        } else if (event.getSource() == addStudentButton) {
            //Check if user is admin (null-safe)
            /*if (Objects.equals(functions.getUsername(), "admin")) {

            } else {
                //Is not admin
                System.out.println("Not admin - cannot change students");
                Functions.setAlertMessage("Only the Admin can change student data!");
                functions.loadStage("/alertFrame/alert.fxml", "Error");
            }
            System.out.println("Can change details - admin");*/
            String StudentID = studentIDTxtField.getText(), firstname = firstnameTxtField.getText(), surname = surnameTxtField.getText(), StudentClass = classTxtField.getText(), grade = gradeTxtField.getText(), teacher = teacherTxtField.getText();
            //Make sure grade is an integer (to work with auto-update function)
            try {
                Integer.parseInt(grade);
                System.out.println(grade + " can be parsed as int...");
                //Add students obj to observablelist & set to tableview
                studentObservableList.add(new Student(StudentID, firstname, surname, StudentClass, grade, teacher));
                System.out.println(studentObservableList.toString());
                studentsTable.setItems(studentObservableList);
                //Save student-teacher relationship in student-teacher table
                String sql = "INSERT INTO student_teacher (TeacherID, StudentID) VALUES ('" + teacher + "','" + StudentID + "');";
                System.out.println("Insert student: " + sql);
                Functions.query(sql);
                System.out.println("Success");
                //Save information in students table
                Functions.query("INSERT INTO students_table (StudentID, StudentName, StudentSurname, StudentClass, StudentGrade) VALUES ('" + StudentID + "','" + firstname + "','" + surname + "','" + StudentClass + "','" + grade + "');");
                System.out.println("Success");
                //insert into attendance table
                String attendance_insert = "INSERT INTO attendance_table (StudentID, week1, week2, week3, week4, week5, week6) VALUES ('" + StudentID + "', '0', '0', '0', '0', '0', '0');";
                System.out.println("INSERT attendance_table: " + attendance_insert);
                Functions.query(attendance_insert);
                //insert into notes table
                String notes_insert = "INSERT INTO notes_table (StudentID, note) VALUES ('" + StudentID + "', '');";
                System.out.println("INSERT notes_table: " + notes_insert);
                Functions.query(notes_insert);
            } catch (Exception ex) {
                //parseInt failed
                System.out.println(grade + " cannot be parsed as int!");
                Functions.setAlertMessage("The grade field can only contain numbers!");
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
                deleteFromBothTables(deleteStudentID);
                //Delete from ObservableList
                studentObservableList.remove(rowIndex);
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

            deleteFromBothTables(studentObservableList.get(rowIndex).getStudentID());
            studentObservableList.remove(rowIndex);
            Student updateStudent = new Student(studentIDTxtField.getText(), firstnameTxtField.getText(), surnameTxtField.getText(), classTxtField.getText(), gradeTxtField.getText(), teacherTxtField.getText());
            studentObservableList.add(updateStudent);
            //Save update to DB
            //Save student-teacher relationship in student-teacher table
            String sqlinsert1 = "INSERT INTO student_teacher (TeacherID, StudentID) VALUES ('" + teacherTxtField.getText() + "','" + studentIDTxtField.getText() + "');";
            System.out.println("Edit query 1: " + sqlinsert1);
            Functions.query(sqlinsert1);
            System.out.println("Success");
            //Save information in students table
            String sqlinsert2 = "INSERT INTO students_table (StudentID, StudentName, StudentSurname, StudentClass, StudentGrade) VALUES ('" +studentIDTxtField.getText() + "','" + firstnameTxtField.getText() + "','" + surnameTxtField.getText() + "','" + classTxtField.getText() + "','" + gradeTxtField.getText() + "');";
            System.out.println("Edit query 2: " + sqlinsert2);
            Functions.query(sqlinsert2);
            System.out.println("Success");
            //Save new info in attendance_table
            String attendance_insert = "INSERT INTO attendance_table (StudentID, week1, week2, week3, week4, week5, week6) VALUES ('" + studentIDTxtField.getText() + "', '0', '0', '0', '0', '0', '0');";
            System.out.println("Insert 3: " + attendance_insert);
            Functions.query(attendance_insert);
            //update notes_table
            String notes_insert = "INSERT INTO notes_table (StudentID, note) VALUES ('" + studentIDTxtField.getText() + "', '');";
            System.out.println("Insert 4: " + notes_insert);
            Functions.query(notes_insert);

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
    public void deleteFromBothTables(String studentID) {
        String SQLite = "DELETE FROM student_teacher WHERE StudentID = " + studentID + ";", SQLite2 = "DELETE FROM students_table WHERE StudentID = " + studentID + ";";
        //delete from attendance table
        String attendance_del = "DELETE FROM attendance_table WHERE StudentID = " + studentID + ";";
        //delete from notes_table
        String notes_del = "DELETE FROM notes_table WHERE StudentID = " + studentID + ";";
        System.out.println("DELETE query 1: " + SQLite + "\nDELETE query 2: " + SQLite2 + "\nDELETE query 3: " + attendance_del + "\nDELETE query 4: " + notes_del);
        Functions.query(SQLite);
        Functions.query(SQLite2);
        Functions.query(attendance_del);
        Functions.query(notes_del);
        //String deletequery = "DELETE FROM students_table WHERE StudentGrade = " + studentsObservableList.get(rowIndex).getStudentID() + ";";
        //Functions.query(deletequery);
    }

    @FXML
    void handleMouseClicks(MouseEvent event) {
        try {
            Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
            System.out.println("Selected row: " + selectedStudent.toString() +
                    "\nRow: " + studentsTable.getSelectionModel().getSelectedIndex());
            //Insert values of selected row into txtfields
            studentIDTxtField.setText(selectedStudent.getStudentID());
            firstnameTxtField.setText(selectedStudent.getFirstname());
            surnameTxtField.setText(selectedStudent.getSurname());
            classTxtField.setText(selectedStudent.getClassA());
            gradeTxtField.setText(selectedStudent.getGrade());
            teacherTxtField.setText(selectedStudent.getTeacher());

            //editing privileges
            if (selectedStudent.getTeacher().equals(functions.getUsername()) || functions.getUsername().equals("admin")) {
                //can modify
                deleteStudentButton.setDisable(false);
                editStudentButton.setDisable(false);
            } else {
                deleteStudentButton.setDisable(true);
                editStudentButton.setDisable(true);
            }
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
        String students_table = "students_table", student_teacher = "student_teacher";
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
            studentObservableList.add(new Student(studentIdresult, studentNameResult, studentSurnameResult, studentClassResult, studentGradeResult, studentTeacherResult));
            System.out.println("ObservableList appended: " + studentObservableList.toString());
        }
        studentsTable.setItems(studentObservableList);
        //Remove horizontal scrollbar

        //FilteredList of searching
        FilteredList<Student> filteredStudents = new FilteredList<>(studentObservableList, p -> true);
        searchTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredStudents.setPredicate(student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getFirstname().toLowerCase().contains(lowerCaseFilter) || student.getSurname().toLowerCase().contains(lowerCaseFilter) ||
                student.getGrade().toLowerCase().contains(lowerCaseFilter) || student.getStudentID().toLowerCase().contains(lowerCaseFilter) ||
                student.getClassA().toLowerCase().contains(lowerCaseFilter) || student.getTeacher().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches field value.
                }
                return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Student> sortedStudents = new SortedList<>(filteredStudents);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedStudents.comparatorProperty().bind(studentsTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        studentsTable.setItems(sortedStudents);

        //Init admin privileges
        if (!functions.getUsername().equals("admin")) {
             deleteStudentButton.setDisable(true);
             editStudentButton.setDisable(true);
             //init teacher text field
             teacherTxtField.setText(functions.getUsername());
             teacherTxtField.setDisable(true);
        }

     }
}
