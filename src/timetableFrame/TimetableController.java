package timetableFrame;

import CalculateTimetable.DistributedPseudoRandom;
import CalculateTimetable.PseudoRandom;
import CalculateTimetable.PseudoRandom2;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.root.DatabaseSettings;
import com.root.Functions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableController implements Initializable {

    Functions functions = new Functions();
    DatabaseSettings db = new DatabaseSettings();

    @FXML
    private Button clearButton;

    @FXML
    private JFXComboBox<String> studentsList;

    @FXML
    private FontAwesomeIcon closeIcon;

    @FXML
    private Button exportButton;

    @FXML
    private Button calculateButton;

    @FXML
    private JFXComboBox<String> teacherSelector;

    @FXML
    private JFXComboBox<String> weekSelector;

    @FXML
    private GridPane timetableGridPane;

    @FXML
    void onMouseEntered(MouseEvent event) {
        ((FontAwesomeIcon) event.getSource()).setScaleX(1.1);
        ((FontAwesomeIcon) event.getSource()).setScaleY(1.1);
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        ((FontAwesomeIcon) event.getSource()).setScaleY(1);
        ((FontAwesomeIcon) event.getSource()).setScaleX(1);
    }


    @FXML
    void onMouseClicked(MouseEvent event) throws Exception {
        if (event.getSource() == closeIcon) {
            System.out.println("Close timetableFrame");
            functions.loadStage("/mainFrame/main.fxml", "Home Screen - User: " + functions.getUsername());
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }


    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == exportButton) {
            System.out.println("Export timetable");
            ArrayList<String> studentTimetable = calculateStudentTimetable();
            exportAsPDF(studentTimetable);
        } else if (event.getSource() == calculateButton) {
            System.out.println("Calculate timetables");
            //Check that all academic timetables have been imported
            String getAcademicTimetables = "SELECT * FROM " + db.getAcademicTable() + ";";
            String [] cols = {"L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12", "L13", "L14", "L15", "L16", "L17", "L18", "L19", "L20", "L21", "L22", "L23", "L24", "L25", "L26", "L27", "L28", "L29", "L30"};
            System.out.println("Retrieving academic timetables: " + getAcademicTimetables);
            ArrayList<ArrayList<String>> academicTimetable = Functions.select(getAcademicTimetables, cols);
            if (academicTimetable.get(0).size() < 5) {
                System.out.println("Academic timetables missing! Import all timetables (" + academicTimetable.get(0).size() + " found)");
                //Some timetables are missing, display notification
                Functions.setAlertMessage("Not all academic timetables have been imported! Use the settings window to save timetables for each class.");
                functions.loadStage("/alertFrame/alert.fxml", "Cannot calculate timetables!");
            } else {
                //For each week
            calculateTeacherTimetable();
            }
        } else if (event.getSource() == clearButton){
            System.out.println("Clearing gridpane...");
            clearGridPane();
        }
    }

    private void calculateTeacherTimetable() throws Exception {
        //Get students of teacher selected
        if (!teacherSelector.getSelectionModel().isEmpty()) {
            //int week = weekSelector.getSelectionModel().getSelectedIndex() + 1;
            String teacher = teacherSelector.getSelectionModel().getSelectedItem();
            System.out.println("Calculating timetable for " + teacher);
            //Check if timetable already exists
            if (timetableExists(teacher)) {
                //Clear GridPane
                    /*try {                                                                    ---clearing grid to display timetable
                        timetableGridPane.getChildren().clear();
                        System.out.println("GridPane cleared successfully!");
                        //Repopulate with headers
                        Label [] newLabels = new Label[11];
                        String[] labelText = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Period 1", "Period 2", "Period 3", "Period 4", "Period 5", "Period 6"};
                        for (int i = 0; i < 11; i++) {
                            newLabels[i] = new Label();
                            newLabels[i].setText(labelText[i]);
                            newLabels[i].setId(labelText[i] + "Label");
                            newLabels[i].setFont(new Font("Century Gothic", 15));
                            newLabels[i].setTextFill(Paint.valueOf("#ffffff"));
                            newLabels[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                            newLabels[i].setAlignment(Pos.CENTER);
                            if (i < 5) {
                                //Add days
                                timetableGridPane.add(newLabels[i], i + 1, 0);
                            } else {
                                //Add periods
                                timetableGridPane.add(newLabels[i], 0, i - 4);
                            }
                        }
                        System.out.println("New labels added");
                    } catch (Exception ex) {
                        System.out.println("GridPane already clear...");
                    }*/
                //Delete pre-existing SQL entry
                String deleteQuery = "DELETE FROM " + db.getPersonalisedTable() + " WHERE TeacherID LIKE '" + teacher + "_';";
                System.out.println("Delete timetable: " + deleteQuery);
                Functions.query(deleteQuery);
            } else {
                System.out.println("No timetable to overwrite...Proceed");
            }

            String getStudents = "SELECT StudentID FROM " + db.getStudentTeacherTable() + " WHERE TeacherID = '" + teacher + "'";
            System.out.println("Get student: " + getStudents);
            String [] teacherCols = {"StudentID"};
            ArrayList<ArrayList<String>> students = Functions.select(getStudents, teacherCols);
            int studentCount = students.get(0).size();
            System.out.println("Number of students: " + studentCount);

            //Use genetic algorithm to optimise lesson distribution
            //DistributedPseudoRandom dpr = new DistributedPseudoRandom();
            //ArrayList<Integer> bestTimetable = dpr.calculateGeneticDistTimetable(studentCount);
            PseudoRandom2 ps2 = new PseudoRandom2();
            ArrayList<ArrayList<Integer>> bestTimetable = ps2.calculateSixWeekTimetable(studentCount);
            for (int week = 0; week < 6; week++) {
                System.out.println("Skeleton timetable @ week " + week + " : " + bestTimetable.get(week).toString());
            }
            //Save timetable
            saveCalculatedTimetable(teacher, bestTimetable);
            //Display timetable if week is selected
            if (!weekSelector.getSelectionModel().isEmpty()) {
                int selectedWeek = weekSelector.getSelectionModel().getSelectedIndex();
                System.out.println("Week: " + selectedWeek + " selected, displaying timetable");
                //Get timetable from SQL (@ teacher + weekNo)
                ArrayList<ArrayList<String>> timetableToDisplay = getTimetable(teacher, selectedWeek);
                displayCalculatedTimetable(timetableToDisplay);
            }
            //Check if teacher an
                /*
                //Convert int[] into ArrayList<Arraylist<String>>
                //init skeleton timetable (Array of 2D Arrays)
                ArrayList<ArrayList<ArrayList<String>>> skeleton = new ArrayList<>();
                for (int weekNo = 0; weekNo <= 5; weekNo++) {
                    for (int a = 0; a < 5; a++) {
                        skeleton.add(new ArrayList<>());
                        for (int b = 0; b < 6; b++) {
                            skeleton.get(weekNo).get(a).add(bestTimetable.get((a * 6) + b) + "");
                        }
                    }
                }
                //Get corresponding name
                String getStudentname = "SELECT " + db.getStudentTable() + ".StudentName, " + db.getStudentTable() + ".StudentSurname, " +
                        db.getStudentTeacherTable() + ".StudentID FROM " + db.getStudentTeacherTable() + " INNER JOIN " + db.getStudentTable() +
                        " ON " + db.getStudentTeacherTable() + ".StudentID = " + db.getStudentTable() + ".StudentID;";
                System.out.println("Get corresponding student names: " + getStudentname);
                String [] joinSelectCols = {"StudentName", "StudentSurname", "StudentID"};
                ArrayList<ArrayList<String>> studentNameResults = Functions.select(getStudentname, joinSelectCols);
                String [] names = new String[studentCount];
                String [] timetableOutput = new String[30];

                ArrayList<Label> labelList = new ArrayList<>();
                int index = 0, realIndex = 0;
                for (int day = 0; day < 5; day++) {
                    for (int period = 0; period < 6; period++) {
                        int key = Integer.parseInt(skeleton.get(day).get(period));
                        if (key > 0) {
                            for (int i = 0; i < studentNameResults.get(0).size(); i++) {
                                if (studentNameResults.get(2).get(i).equals(students.get(0).get(key - 1))) {
                                    names[key - 1] = studentNameResults.get(0).get(i).charAt(0) + " " +  studentNameResults.get(1).get(i);
                                }
                            }
                            System.out.println(students.get(0).get(key - 1) + " : " + names[key - 1] + " day " + day + " period: " + period + " key: " + key);
                            //Make labels to display name on timetable
                            labelList.add(new Label());
                            labelList.get(index).setId("label" + index);
                            labelList.get(index).setText(names[key - 1]);
                            labelList.get(index).setFont(new Font("Century Gothic", 15));
                            //Make items grow to fill gridPane cell
                            labelList.get(index).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                            //Center labels
                            labelList.get(index).setAlignment(Pos.CENTER);
                            timetableGridPane.setAlignment(Pos.CENTER);
                            //Add labelList @ index to gridPane
                            timetableGridPane.add(labelList.get(index), day + 1, period + 1);
                            //System.out.println("Added label: " + labelList.get(index).getId() + " @ (" + (day + 1) + ";" + (period + 1) + ")");
                            index++;
                            //Add to timetable out
                            timetableOutput[realIndex] = names[key - 1];
                        } else {
                            //Add to timetable out
                            timetableOutput[realIndex] = " ";
                        }
                        realIndex++;
                    }
                    System.out.println();
                }*/
        }else {
            //Show error, admin must select a teacher
            Functions.setAlertMessage("Please select a teacher to calculate a timetable for.");
            functions.loadStage("/alertFrame/alert.fxml", "Cannot calculate timetable");
        }
    }

    private boolean timetableExists(String teacherName) {
        ArrayList<ArrayList<String>> timetable = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            timetable.add(new ArrayList<>());
        }

        boolean exists = false;
        for (int week = 0; week < 6; week++) {
            if (timetable.get(week).size() > 0) {
                exists = true;
            }
        }
        return exists;
    }

    private ArrayList<ArrayList<String>> decodeTimetable(ArrayList<ArrayList<String>> skeletonTimetable) {
        System.out.println("Skeleton timetable size: (" + skeletonTimetable.size() + " x " + skeletonTimetable.get(0).size() + ")");
        for (ArrayList<String> list: skeletonTimetable) {
            System.out.println("Skeleton timetable: " + list.toString());
        }
        //Convert timetable from numerical skeleton to names
        ArrayList<ArrayList<String>> decoded = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            decoded.add(new ArrayList<>());
        }
        //Get students of teacher
        String teacher = teacherSelector.getSelectionModel().getSelectedItem();
        String getStudents = "SELECT * FROM " + db.getStudentTeacherTable() + " INNER JOIN " + db.getStudentTable()
                + " ON " + db.getStudentTeacherTable() + ".StudentID = " + db.getStudentTable() + ".StudentID WHERE " +
                db.getStudentTeacherTable() + ".TeacherID = '" + teacher + "';";
        System.out.println("Get students of teacher " + teacher + ": " + getStudents);
        String [] cols = {"StudentID", "TeacherID", "StudentID", "StudentName", "StudentSurname", "StudentClass", "StudentGrade"};
        ArrayList<ArrayList<String>> studentsResult = Functions.select(getStudents, cols);
        //Create hashmap of initials of students
        //Start count @ 1 because 0 = free period
        int numStudents = studentsResult.get(0).size(), count = 1;
        String [] initials = new String[numStudents];
        HashMap<Integer, String> initialsHM = new HashMap<>();
        //Add 0 as free period
        initialsHM.put(0, " ");
        System.out.println("Initial HashMap: " + initialsHM.toString());
        for (int a = 0; a < numStudents; a++) {
            String temp = studentsResult.get(3).get(a).charAt(0) + " " + studentsResult.get(4).get(a);
            initials[count] = temp;
            //Add to HashMap (start at 1)
            initialsHM.put(a + 1, temp);
            System.out.println("Initials: " + temp);
        }
        System.out.println("Final HashMap: " + initialsHM.toString());
        //Use HashMap to decode
        for (int week = 0; week < 6; week++) {
            for (int i = 0; i < skeletonTimetable.get(week).size(); i++) {
                //Number ID of student in slot
                int num = Integer.parseInt(skeletonTimetable.get(week).get(i));
                System.out.print(num + ", ");
                decoded.get(week).add(initialsHM.get(num));
            }
            System.out.println("\nDecoded timetable: " + decoded.get(week).toString());
        }
        return decoded;
    }

    private void saveCalculatedTimetable(String teacher, ArrayList<ArrayList<Integer>> timetable) {
        ArrayList<ArrayList<String>> stringTimetable = new ArrayList<>();
        //Convert Integer to String
        for (int week = 0; week < 6; week++) {
            List<String> tempTimetable = timetable.get(week).stream().map(
                    Object::toString).collect(Collectors.toList()
            );
            stringTimetable.add((ArrayList<String>) tempTimetable);
        }

        //raisedTimetable.add((ArrayList<String>) timetableString);
        /*raisedTimetable.add(new ArrayList<>());
        for (int i = 0; i < timetable.size(); i++) {
            raisedTimetable.get(0).add(timetable.get(i) + "");
        }**/
        ArrayList<ArrayList<String>> decodedTimetable = decodeTimetable(stringTimetable);

        for (int week = 0; week < 6; week++) {
            String data = "";
            for (String lesson: decodedTimetable.get(week)) {
                data = data + "'" + lesson + "', ";
            }
            data = data.substring(0, data.length() - 2);
            System.out.println("Data to save: " + data);
            //Insert week to differentiate timetables for each week (composite key)
            String insertQuery = "INSERT INTO " + db.getPersonalisedTable() + " VALUES ('" + teacher + week + "', " + data + ");";
            System.out.println("TO INSERT: " + insertQuery);
            Functions.query(insertQuery);
        }
    }

    private ArrayList<ArrayList<String>> getTimetable(String selectedTeacher, int week) {
        //Get timetables for all weeks
        String [] cols = {"L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12", "L13", "L14", "L15", "L16", "L17", "L18", "L19", "L20", "L21", "L22", "L23", "L24", "L25", "L26", "L27", "L28", "L29", "L30"};
        String getTimetable = "SELECT * FROM " + db.getPersonalisedTable() + " WHERE TeacherID = '" + selectedTeacher + (week - 1) + "';";
        System.out.println("Getting timetable of: " + selectedTeacher + " -> " + getTimetable);
        ArrayList<ArrayList<String>> timetableResult = Functions.select(getTimetable, cols);
        return timetableResult;
    }

    private void displayCalculatedTimetable(ArrayList<ArrayList<String>> timetable) {
        try {
            //Clear pane
            clearGridPane();
            System.out.println("Timetable to print: " + timetable.toString() + ", size: (" + timetable.size() + " x " + timetable.get(0).size() + ")");
            //Save decoded names rather than numbers
            ArrayList<Label> labels = new ArrayList<>();
            int index = 0;
            for (int day = 0; day < 5; day++) {
                for (int period = 0; period < 6; period++) {
                    //Make labels to display name on timetable
                    labels.add(new Label());
                    labels.get(index).setText(timetable.get(index).get(0));
                    labels.get(index).setFont(new Font("Century Gothic", 15));
                    labels.get(index).setAlignment(Pos.CENTER);
                    timetableGridPane.setAlignment(Pos.CENTER);
                    labels.get(index).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    timetableGridPane.add(labels.get(index), day + 1, period + 1);
                    index++;
                }
            }
        } catch (Exception ex) {
            System.out.println("No teacher selected!\n");
            ex.printStackTrace();
        }
    }

    private Label getLabelFromGridPane(GridPane gridPane, int col, int row) {
        //Simplifies clear method, JFX has no (row,col) system to get children
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                //Assume GridPane contains Labels
                return (Label) node;
            }
        }
        return null;
    }

    private void clearGridPane() {
        try {
            timetableGridPane.getChildren().clear();
            System.out.println("Grid line visible: " + timetableGridPane.isGridLinesVisible());
            System.out.println("GridPane cleared successfully!");
            //Repopulate with headers
            Label [] newLabels = new Label[11];
            String[] labelText = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Period 1", "Period 2", "Period 3", "Period 4", "Period 5", "Period 6"};
            for (int i = 0; i < 11; i++) {
                newLabels[i] = new Label();
                newLabels[i].setText(labelText[i]);
                newLabels[i].setId("timetableLabel");
                newLabels[i].setFont(new Font("Century Gothic", 16));
                //newLabels[i].setTextFill(Paint.valueOf("#ffffff"));
                newLabels[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                newLabels[i].setAlignment(Pos.CENTER);
                if (i < 5) {
                    //Add days
                    timetableGridPane.add(newLabels[i], i + 1, 0);
                } else {
                    //Add periods
                    timetableGridPane.add(newLabels[i], 0, i - 4);
                }
            }
            //timetableGridPane.setGridLinesVisible(true);
            System.out.println("Grid line visible: " + timetableGridPane.isGridLinesVisible());
            System.out.println("New labels added");
        } catch (Exception ex) {
            System.out.println("GridPane already clear...");
        }
    }

    @FXML
    void handleListSelection(ActionEvent event) {
        if (event.getSource() == studentsList) {
            //Calculate timetable for student selected
            String selectedStudent = studentsList.getSelectionModel().getSelectedItem();
            System.out.println("Student selected: " + selectedStudent);
            //System.out.println(allListsSelected());
        } else if (event.getSource() == teacherSelector) {
            String selectedTeacher = teacherSelector.getSelectionModel().getSelectedItem();
            System.out.println("Teacher selected: " + selectedTeacher);
            //Init studentList, get students of teacher
            boolean isEmpty = setStudentsList(selectedTeacher);
            if (isEmpty) {
                System.out.println("No students to display in studentList");
            } else {
                System.out.println("Students Set...");
            }
            //loadTimetable();
            //Load respective timetable if found
            /*String selectedTeacher = teacherSelector.getSelectionModel().getSelectedItem();
            ArrayList<ArrayList<String>> timetableResult = getTimetable(selectedTeacher);
            if (timetableExists(selectedTeacher)) {
                System.out.println("Timetable present for " + selectedTeacher);
                fetchAndDisplayCalculatedTimetable(timetableResult);
            } else {
                System.out.println(selectedTeacher + " does not have a timetable to load...");
            }*/
        } else if (event.getSource() == weekSelector) {
            int selectedWeek = weekSelector.getSelectionModel().getSelectedIndex() + 1;
            System.out.println("Week selected: " + selectedWeek);
            if (!teacherSelector.getSelectionModel().isEmpty()) {
                //load and display
                ArrayList<ArrayList<String>> timetable = getTimetable(teacherSelector.getSelectionModel().getSelectedItem(), selectedWeek);
                displayCalculatedTimetable(timetable);
            } else {
                System.out.println("No teacher selected, cannot display timetable");
            }
        }
    }

    private boolean setStudentsList(String teacher) {
        //returns if studentslist is empty
        boolean isEmpty = false;
        //Clear list
        studentsList.getItems().clear();
        String [] columns = {"StudentName", "StudentSurname"};
        String students_table = db.getStudentTable(), student_teacher = db.getStudentTeacherTable(), selectQuery;
        selectQuery = "SELECT StudentName, StudentSurname FROM " + students_table  + " INNER JOIN " +
                student_teacher  + " ON " + student_teacher + ".StudentID = " + students_table + ".StudentID WHERE " + student_teacher + ".TeacherID = '" + teacher + "';";
        System.out.println("Set students of " + teacher + ": " + selectQuery);
        ArrayList<ArrayList<String>> queryResult = Functions.select(selectQuery, columns);
        System.out.println("SQL SELECT Result - > " + queryResult);
        if (queryResult.size() == 1) {
            System.out.println("No students found...Add students");
            isEmpty = true;
        } else {
            for (int i = 0; i < queryResult.get(0).size(); i++) {
                System.out.println("Adding student no. " + i);
                //add name Steve and surname Jobs = S Jobs
                studentsList.getItems().add(queryResult.get(0).get(i).substring(0, 1) + " " + queryResult.get(1).get(i));
            }
            System.out.println("Done.");
            //Load URLS from SQL
        }
        return isEmpty;
    }

    private ArrayList<String> calculateStudentTimetable() throws FileNotFoundException, DocumentException {
        ArrayList<String> result = new ArrayList<>();
        //Get current teacher
        if (!teacherSelector.getSelectionModel().isEmpty()) {
            String teacher = teacherSelector.getSelectionModel().getSelectedItem(), student = studentsList.getSelectionModel().getSelectedItem();
            System.out.println("Generating " + teacher + "'s timetable for " + student);
            //result.add(student + "'s lessons with " + teacher + ":\n");
            //Make Array for days
            String [] mapDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            //Get timetables of teacher
            for (int week = 1; week <= 6; week++) {
                ArrayList<ArrayList<String>> masterTimetable = getTimetable(teacher, week);
                //Get index of when the student has a lesson
                for (int i = 0; i < masterTimetable.size(); i++) {
                    if (masterTimetable.get(i).get(0).equals(student)) {
                        int dayNum = i / 6, period = i % 6;
                        String day = mapDays[dayNum];
                        //Get grade of student
                        String getGrade = "SELECT StudentGrade FROM " + db.getStudentTable() + " WHERE (substr(StudentName, -1 * length(StudentName), 1) || ' ' || StudentSurname) = '" + student + "';";
                        System.out.println("Getting grade: " + getGrade);
                        String [] gradeCols = {"StudentGrade"};
                        int grade = Integer.parseInt(Functions.select(getGrade, gradeCols).get(0).get(0));
                        System.out.println(student + " is grade: " + grade);
                        //Get academic timetable of that grade
                        String getAcademicTimetable = "SELECT * FROM " + db.getAcademicTable() + " WHERE ClassNumber = " + (grade - 1) + ";";
                        String [] academicCols = {"L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12", "L13", "L14", "L15", "L16", "L17", "L18", "L19", "L20", "L21", "L22", "L23", "L24", "L25", "L26", "L27", "L28", "L29", "L30"};
                        ArrayList<ArrayList<String>> academicTimetable = Functions.select(getAcademicTimetable, academicCols);
                        //Get lesson being missed
                        String lessonMissed = academicTimetable.get(i).get(0);
                        System.out.println("To miss: " + lessonMissed);
                        //Print results
                        String line = "Week " + week + " on " + day + " in period " + (period + 1) + " during " + lessonMissed;
                        result.add(line);
                    }
                }
            }
            System.out.println("Printout for student: " + student + "...");
            for (String lesson: result) {
                System.out.println(lesson);
            }
        } else {
            System.out.println("Teacher not selected, select a teacher!!");
        }
        return result;
    }

    private void exportAsPDF(ArrayList<String> data) throws Exception {
        String teacher = teacherSelector.getSelectionModel().getSelectedItem(), student = studentsList.getSelectionModel().getSelectedItem();
        //Home directory
        File home = FileSystemView.getFileSystemView().getHomeDirectory();
        String filepath = home.getAbsolutePath() + File.separator + student + "_timetable.pdf";
        System.out.println("Home directory: " + home.getAbsolutePath() + "\nFilepath to save: " + filepath);
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));

        //Custom fonts
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 17, BaseColor.BLACK);
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);

        doc.open();
        doc.addCreationDate();
        doc.addCreator("Timetabler by T.Geissler");
        doc.add(new Paragraph(student + "'s lessons with " + teacher + ":", headerFont));
        for (String line: data) {
            doc.add(new Paragraph(" -- " + line, font));
        }
        doc.close();
        writer.close();
        System.out.println("Finished writing PDF");
        //Alert user that file is saved
        Functions.setIsInfoMessage(true);
        Functions.setAlertMessage("Timetable for " + student + " has been saved in your Desktop folder");
        functions.loadStage("/alertFrame/alert.fxml", "PDF Saved!");
    }


    /*private void loadTimetable() {
        if (allListsSelected()) {
            //Proceed
            String selectedTeacher = teacherSelector.getSelectionModel().getSelectedItem();
            //Week selected (1 - 6)
            int week = weekSelector.getSelectionModel().getSelectedIndex() + 1;
            System.out.println("Week " + week + " selected");
            //Load respective timetable if found
            ArrayList<ArrayList<String>> timetableResult = getTimetable(selectedTeacher, week);
            if (timetableExists(selectedTeacher, week)) {
                System.out.println("Timetable present for " + selectedTeacher);
                fetchAndDisplayCalculatedTimetable(timetableResult);
            } else {
                System.out.println(selectedTeacher + " does not have a timetable to load...");
            }
        }
    }*/

    private boolean allListsSelected() {
        boolean areSelected = true;
        if (teacherSelector.getSelectionModel().isEmpty() || weekSelector.getSelectionModel().isEmpty()) {
            areSelected = false;
            System.out.println("Teacher and Week selectors selected: " + areSelected);
        }
        return areSelected;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Dark mode
        /*
        if (functions.getIsDark()) {
            System.out.println("Dark mode modifications made...");
            timetableGridPane.setGridLinesVisible(true);
        } else {
            timetableGridPane.setGridLinesVisible(true);
            System.out.println("Light mode modifications made...");
        }*/

        System.out.println("TimetableFrame Initialised");
        //Set StudentList text

        //Set values for teacher list, display only if admin is logged in
        if (functions.getUsername().equals("admin")) {
            teacherSelector.setVisible(true);
            String getTeachers = "SELECT DISTINCT TeacherID FROM " + db.getStudentTeacherTable() + ";";
            String [] teacherCols = {"TeacherID"};
            System.out.println("Init teacherDropdown menu... " + getTeachers);
            ArrayList<ArrayList<String>> teachers = Functions.select(getTeachers, teacherCols);
            for (int i = 0; i < teachers.get(0).size(); i++) {
                teacherSelector.getItems().addAll(teachers.get(0).get(i));
                System.out.println("Adding teacher: " + teachers.get(0).get(i));
            }
        }
        //Initialize values for weekSelector
        System.out.println("Initialize weekSelector: ");
        for (int i = 1; i <= 6; i++) {
            weekSelector.getItems().add("Week " + i);
            System.out.println("Added: " + weekSelector.getItems().get(i - 1));
        }
    }
}
