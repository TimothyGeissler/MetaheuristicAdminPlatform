package settingsFrame;

import com.jfoenix.controls.*;
import com.root.Functions;
import com.sun.webkit.WebPage;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mainFrame.MainController;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName.*;

public class SettingsController2 implements Initializable {

    private Functions functions = new Functions();
    private String realPassword;
    private StringBuilder hiddenPassword = new StringBuilder();

    //Store data about spreadsheets
    private String [] urls = new String[15];

    // observable list is a listenable element, used to save table data
    private ObservableList<String> usersObservableList = FXCollections.observableArrayList();

    //Tables data for tableView
    //private ObservableList<String> tablesObservableList = FXCollections.observableArrayList();
    //private SimpleStringProperty tablesProperty;

    @FXML
    private TabPane settingsTabPane;

    @FXML
    private Tab aboutTab;

    @FXML
    private Tab timetablesTab;

    @FXML
    private Tab acctTab;

    @FXML
    private Tab sqlTab;

    @FXML
    private Label confirmLabel;

    @FXML
    private Button noDelAcctButton;

    @FXML
    private Button yesDelAcctButton;

    @FXML ListView<String> usersListview;

    @FXML
    private WebView helpWebView;

    @FXML
    private Pane helpArrowPane;

    @FXML
    private Pane helpHidingPane;

    @FXML
    private FontAwesomeIcon helpExpandIcon;

    @FXML
    private AnchorPane mainSettingsAnchorpane;

    @FXML
    private FontAwesomeIcon filterDownArrow;

    @FXML
    private FontAwesomeIcon filterUpArrow;

    @FXML
    private TextField helpSearchField;

    @FXML
    private ImageView aboutImageView;

    @FXML
    private Pane aboutAnchorPane;

    @FXML
    private Label versionLabel1;

    @FXML
    private Label versionLabel2;

    @FXML
    private Label versionLabel3;

    @FXML
    private JFXToggleButton darkmodeToggle;

    @FXML
    private Label settingsHeaderLabel;

    @FXML
    private Label settingsFooterLabel;

    @FXML
    private AnchorPane helpScrollAnchorPane;

    @FXML
    private AnchorPane timetablesAnchorPane;

    @FXML
    private ImageView timetableSettingsImageView;

    @FXML
    private Pane u1Pane;

    @FXML
    private Circle whiteCircle1;

    @FXML
    private Circle whiteCircle2;

    @FXML
    private Circle whiteCircle3;

    @FXML
    private Circle whiteCircle4;

    @FXML
    private Circle whiteCircle5;

    @FXML
    private FontAwesomeIcon deleteIcon1;

    @FXML
    private FontAwesomeIcon deleteIcon2;

    @FXML
    private FontAwesomeIcon deleteIcon3;

    @FXML
    private FontAwesomeIcon deleteIcon4;

    @FXML
    private FontAwesomeIcon deleteIcon5;

    @FXML
    private Label gradeLabel1;

    @FXML
    private JFXTextField gradeTxtfield1;

    @FXML
    private Pane l2Pane;

    @FXML
    private Label gradeLabel2;

    @FXML
    private JFXTextField gradeTxtfield2;

    @FXML
    private Pane u2Pane;

    @FXML
    private Label gradeLabel3;

    @FXML
    private JFXTextField gradeTxtfield3;

    @FXML
    private Pane l3Pane;

    @FXML
    private Label gradeLabel4;

    @FXML
    private JFXTextField gradeTxtfield4;

    @FXML
    private Pane u3Pane;

    @FXML
    private Label gradeLabel5;

    @FXML
    private JFXTextField gradeTxtfield5;


    @FXML
    private Tab helpCenterTab;


    @FXML
    private Label filepathLabel;

    @FXML
    private AnchorPane accountAnchorPane;

    @FXML
    private JFXTextField newUsernameTxtField;

    @FXML
    private JFXPasswordField newPasswordTxtField;

    @FXML
    private JFXPasswordField newPasswordTxtField2;

    @FXML
    private FontAwesomeIcon settingsicon3;

    @FXML
    private FontAwesomeIcon settingsIcon4;

    @FXML
    private FontAwesomeIcon settingsIcon5;

    @FXML
    private Label usernameLabel;

    @FXML
    private FontAwesomeIcon settingsIcon1;

    @FXML
    private FontAwesomeIcon settingsIcon2;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button showPasswordButton;

    @FXML
    private Button helpSearchExpandButton;

    @FXML
    private FontAwesomeIcon eyeballIcon;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button saveAcctButton;

    @FXML
    private JFXCheckBox autoUpdateCheckBox;

    @FXML
    private Label finalgradeTitle;

    @FXML
    private JFXSlider finalGradeSlider;

    @FXML
    private Label finalGradeLabel;

    @FXML
    private AnchorPane sqlAnchorPane;

    @FXML
    private FontAwesomeIcon runIcon;

    @FXML
    private JFXTextArea outputTxtArea;

    @FXML
    private JFXTextArea editorTxtArea;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Button settingsBackButton;

    @FXML
    private FontAwesomeIcon savedIcon;

    @FXML
    private FontAwesomeIcon settingsBackIcon;


    private Pane [] panes = {u1Pane, l2Pane, u2Pane, l3Pane, u3Pane};
    private String [] paneNames = {"u1Pane", "l2Pane", "u2Pane", "l3Pane", "u3Pane"};
    private Circle [] circles = {whiteCircle1, whiteCircle2, whiteCircle3, whiteCircle4, whiteCircle5};

    @FXML
    void handleActions(ActionEvent event) throws Exception{
        if (event.getSource() == saveAcctButton) {
            System.out.println("Save new details...");
            if (newUsernameTxtField.getText().equals("") && !newPasswordError()) {
                //only change password
                String update_password = "UPDATE users_table SET Password = '" + newPasswordTxtField.getText() + "' WHERE Username = '" + functions.getUsername() + "';";
                Functions.query(update_password);
                System.out.println("Updated password -> " + update_password);
                passwordLabel.setText(newPasswordTxtField.getText());
                newPasswordTxtField.clear();
                newPasswordTxtField2.clear();
                savedIconTimer();
            } else if (newPasswordTxtField.getText().equals("") && newPasswordTxtField2.getText().equals("") && !newUsernameTxtField.getText().equals("")) {
                //only change username
                String update_username = "UPDATE users_table SET Username = '" + newUsernameTxtField.getText() + "' WHERE Username = '" + functions.getUsername() + "';";
                Functions.query(update_username);
                System.out.println("Updated username -> " + update_username);
                functions.setUsername(newUsernameTxtField.getText());
                usernameLabel.setText(newUsernameTxtField.getText());
                newUsernameTxtField.clear();
                savedIconTimer();
            } else if (!newPasswordError()) {
                String update_all = "UPDATE users_table SET Username = '" + newUsernameTxtField.getText() + "', Password = '" + newPasswordTxtField2.getText() + "' WHERE Username = '" + functions.getUsername() + "';";
                Functions.query(update_all);
                System.out.println("Updated password & username -> " + update_all);
                usernameLabel.setText(newUsernameTxtField.getText());
                passwordLabel.setText(newPasswordTxtField.getText());
                newPasswordTxtField.clear();
                newPasswordTxtField2.clear();
                newUsernameTxtField.clear();
                savedIconTimer();
            }
        } else if (event.getSource() == settingsBackButton) {
            System.out.println("Exit settings");
            functions.loadStage("/mainFrame/main.fxml", "Home Screen - User: " + functions.getUsername());
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else if (event.getSource() == deleteAccountButton) {
            System.out.println("Delete Account after prompt");
            //change UI
            deleteAccountButton.setVisible(false);
            yesDelAcctButton.setVisible(true);
            noDelAcctButton.setVisible(true);
            confirmLabel.setVisible(true);
        } else if (event.getSource() == noDelAcctButton){
            //change UI
            deleteAccountButton.setVisible(true);
            yesDelAcctButton.setVisible(false);
            noDelAcctButton.setVisible(false);
            confirmLabel.setVisible(false);
            System.out.println("cancelled delete");
        } else if (event.getSource() == yesDelAcctButton){
            //change UI
            deleteAccountButton.setVisible(true);
            yesDelAcctButton.setVisible(false);
            noDelAcctButton.setVisible(false);
            confirmLabel.setVisible(false);
            //get user to delete
            String user = usersListview.getSelectionModel().getSelectedItem();
            System.out.println("user to delete: " + user);
            //delete from calculated_timetables
            String delete_timetables = "DELETE FROM calculated_timetables WHERE TeacherID LIKE '" + user + "_';";
            Functions.query(delete_timetables);
            System.out.println("Deleted from calculated_timetables -> " + delete_timetables);

            //delete from notes_table
            //String delete_notes = "DELETE FROM notes_table INNER JOIN student_teacher ON notes_table.StudentID = student_teacher.StudentID WHERE student_teacher.TeacherID = '" + user + "';";
            String delete_notes = "DELETE FROM notes_table WHERE StudentID IN (SELECT StudentID FROM student_teacher WHERE TeacherID = '" + user + "');";
            Functions.query(delete_notes);
            System.out.println("Deleted from notes_table -> " + delete_notes);

            //delete from attendance_table
            //String delete_attendance = "DELETE FROM attendance_table INNER JOIN student_teacher ON attendance_table.StudentID = student_teacher.StudentID WHERE student_teacher.TeacherID = '" + user + "';";
            String delete_attendance = "DELETE FROM attendance_table WHERE StudentID IN (SELECT StudentID FROM student_teacher WHERE TeacherID = '" + user + "');";
            Functions.query(delete_attendance);
            System.out.println("Deleted from attendance_table -> " + delete_attendance);

            //delete from students_table
            //String delete_students = "DELETE FROM students_table INNER JOIN student_teacher ON students_table.StudentID = student_teacher.StudentID WHERE student_teacher.TeacherID = '" + user + "';";
            String delete_students = "DELETE FROM students_table WHERE StudentID IN (SELECT StudentID FROM student_teacher WHERE TeacherID = '" + user + "');";
            Functions.query(delete_students);
            System.out.println("Deleted from students_table -> " + delete_students);

            //delete from student_teacher
            String delete_st = "DELETE FROM student_teacher WHERE TeacherID = '" + user + "';";
            Functions.query(delete_st);
            System.out.println("Deleted from student_teacher -> " + delete_st);

            //delete from users_table
            String delete_users = "DELETE FROM users_table WHERE Username = '" + user + "';";
            Functions.query(delete_users);
            System.out.println("Deleted from users_table -> " + delete_users);
            //update list
            usersObservableList.remove(user);
            usersListview.setItems(usersObservableList);
        } else if (event.getSource() == autoUpdateCheckBox) {
            System.out.println("Checkbox: " + autoUpdateCheckBox.isSelected());
            //Save state of checkbox
            if (!functions.fileExists("checkbox.txt")) {
                //Create file
                functions.createTxtFile("checkbox.txt");
            }
            //File exists, update values
            //Add date, to compare to in order to check if NYs Eve has passed since last login
            //Synchronize date w/ system timezone
            Date currentDate = new Date();
            LocalDate currentlocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            functions.addOutBuffer(autoUpdateCheckBox.isSelected() + "\n" + currentlocalDate);
            functions.printFile("checkbox.txt");

            //Only enable slider if checkbox is checked
            if (!autoUpdateCheckBox.isSelected()) {
                finalGradeSlider.setDisable(true);
                //Set label color to gray
                finalGradeLabel.setTextFill(Color.web("#d8d8d8"));
                finalgradeTitle.setTextFill(Color.web("#d8d8d8"));
            } else {
                //Undo changes
                finalGradeSlider.setDisable(false);
                finalGradeLabel.setTextFill(Color.web("#131e38"));
                finalgradeTitle.setTextFill(Color.web("#131e38"));
            }
        } else if (event.getSource() == helpSearchExpandButton) {
            if (helpExpandIcon.getIconName().equals("SEARCH")) {
                helpSearchField.setVisible(true);
                helpHidingPane.setVisible(true);
                //helpSearchExpandButton.setVisible(false);
                helpExpandIcon.setIcon(ARROW_RIGHT);
                helpArrowPane.setVisible(true);
                if (!helpSearchField.getText().equals("")) {
                    filterDownArrow.setVisible(true);
                    filterUpArrow.setVisible(true);
                }
            } else if (helpExpandIcon.getIconName().equals("ARROW_RIGHT")) {
                helpSearchField.setVisible(false);
                filterDownArrow.setVisible(false);
                filterUpArrow.setVisible(false);
                helpHidingPane.setVisible(false);
                //helpSearchExpandButton.setVisible(false);
                helpExpandIcon.setIcon(SEARCH);
                helpArrowPane.setVisible(false);
            }
        }
    }

    private void savedIconTimer() {
        savedIcon.setVisible(true);
        //set timer
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        savedIcon.setVisible(false);
                    }
                },
                5000
        );
    }

    private boolean newPasswordError() {
        String errorMessage = "";
        boolean error = true;
        if (newPasswordTxtField.getText().length() < 8) {
            //Too short
            errorMessage = errorMessage + "Password is too short\n";
            System.out.println("Too short");
        }
        if (!newPasswordTxtField.getText().equals(newPasswordTxtField2.getText())) {
            //Do not match
            errorMessage = errorMessage + "Passwords do not match\n";
            System.out.println("Passwords do not match");
        }
        if (errorMessage.equals("")) {
            //No problems
            System.out.println("Can save new info");
            error = false;
        } else {
            //Display alert window
            Functions.setAlertMessage(errorMessage);
            try {
                functions.loadStage("/alertFrame/alert.fxml", "Error");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return error;
    }

    @FXML
    void handleMousePress(MouseEvent event) {
        if (event.getSource() == showPasswordButton) {
            System.out.println("Mousepress on view button...\nNew Text -> " + getRealPassword());
            passwordLabel.setText(getRealPassword());
        }
    }

    @FXML
    void handleMouseRelease(MouseEvent event) {
        if (event.getSource() == showPasswordButton) {
            System.out.println("Mouserelease on view button...\nNew text -> " + hiddenPassword.toString());
            passwordLabel.setText(hiddenPassword.toString());
        } else if (event.getSource() == finalGradeSlider) {
            //Save value
            String sliderPosition = String.valueOf((int)finalGradeSlider.getValue());
            if (functions.fileExists("checkbox.txt")) {
                functions.scanFile("checkbox.txt");
                //Scan lines 0 & 1 and place in outbuffer for new file
                functions.addOutBuffer(functions.getInBuffer(0));
                functions.addOutBuffer(functions.getInBuffer(1));
                functions.addOutBuffer(sliderPosition);
                //push outbuffer to file
                functions.printFile("checkbox.txt");
            }
            System.out.println("MouseRelease: " + finalGradeSlider + ", value: " + sliderPosition);
        }
    }

    /*
    @FXML
    void onDragDetected(MouseEvent event) {
        System.out.println("Drag detected @: " + event.getSource());
        finalGradeLabel.setText((int)finalGradeSlider.getValue()+ "");
    }*/

    @FXML
    void onDragOver(DragEvent event) {
        if (event.getGestureSource() != u1Pane && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    void onDragDropped(DragEvent event) throws Exception {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles() && functions.getUsername().equals("admin")) {
            String URL = dragboard.getFiles().toString().substring(1, dragboard.getFiles().toString().length() - 1);
            if (URL.substring(URL.length() - 5).equals(".xlsx")){
                Pane source = (Pane) event.getSource();
                String substring = source.toString().substring(8, source.toString().length() - 1);

                System.out.println("--Files received: " + URL + "\n--Source: " + source);
                //Modify URL, must have 2 \\ for every \
                String doubleURL = URL.replace("\\", "\\\\");
                System.out.println("New URL to be sent to SQL: " + doubleURL);
                int paneNumber = getPaneNo(source);
                //Save to SQL
                String insQuery = "INSERT INTO timetable_urls (Class, URL) VALUES ('" + paneNumber + "', '" + doubleURL + "');";
                System.out.println(insQuery);
                Functions.query(insQuery);
                //Update array urls
                setURL(doubleURL, paneNumber);
                //Update UI
                //String iconKey = iconNames[classNo];
                //System.out.println("IconKey: " + iconKey + "\nIcon to update: " + icons[classNo].getId());//iconMap.get(iconKey).getId());
                //iconMap.get(iconKey).setIcon(CHECK_CIRCLE);
                System.out.println("Pane to update: " + source);
                //get child icon from pane
                FontAwesomeIcon deleteIcon = getChildIconFromPane(source);
                Circle deleteCircle = getChildCircleFromPane(source);
                deleteIcon.setVisible(true);
                deleteCircle.setVisible(true);
                //Update pane appearance
                source.setStyle("-fx-background-color: #42f486; -fx-background-radius: 6;");
                success = true;
                //Update label color
                Label childLabel = getChildLabel(source);
                childLabel.setTextFill(Paint.valueOf("#00152d"));
                //Read xlsx file
                ArrayList<ArrayList<String>> spreadsheet = Functions.readSpreadsheet(URL);
                //Flatten 2D array into 1D list + ClassNo.
                ArrayList<String> flatSpreadsheet = functions.flattenArrayList(spreadsheet);
                //Concat ArrayList into String
                String concatSpreadsheet = functions.concatArrayList(flatSpreadsheet);
                //Save to SQL table academic_table
                //String [] cols = {"L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12", "L13", "L14", "L15", "L16", "L17", "L18", "L19", "L20", "L21", "L22", "L23", "L24", "L25", "L26", "L27", "L28", "L29", "L30"};
                String cols = "L1, L2, L3, L4, L5, L6, L7, L8, L9, L10, L11, L12, L13, L14, L15, L16, L17, L18, L19, L20, L21, L22, L23, L24, L25, L26, L27, L28, L29, L30";
                String sql = "INSERT INTO academic_timetables (ClassNumber," + cols + ") VALUES (" + paneNumber + ", " + concatSpreadsheet + "\");";
                System.out.println("Flattened SQL: " + sql);
                Functions.query(sql);
            } else {
                System.out.println("Invalid .xlsx file!");
                Functions.setAlertMessage("Not a valid Excel file!");
                functions.loadStage("/alertFrame/alert.fxml", "Error");
            }
        } else {
            System.out.println("Cannot import, user not admin");
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    void onMouseEnteredPane(MouseEvent event) {
        //Display filepath of pane in label while mouse hovers over it
        Pane currentPane = (Pane) event.getSource();
        int paneNo = getPaneNo(currentPane);
        //Get url
        //System.out.println("Hover URL: " + getURL(paneNo));
        String currentUrl = getURL(paneNo);
        filepathLabel.setText(currentUrl);
        System.out.println("Mouse hover: " + currentPane.getId() + ", pane #" + paneNo + " -> URL: " + currentUrl);
    }

    @FXML
    void onMouseExitedPane(MouseEvent event) {
        //clear label
        filepathLabel.setText("");
    }

    //Double click pane to change class name (teacher initial)
    @FXML
    void onMouseClickedPane(MouseEvent event) {
        if(event.getButton().equals(MouseButton.SECONDARY)) {// && event.getClickCount() == 2){
            if (functions.getUsername().equals("admin")) {
                Pane source = (Pane) event.getSource();
                System.out.println("RightClick: " + source);
                //Get child label & TF from pane
                Label childLabel = getChildLabel(source);
                childLabel.setVisible(false);
                JFXTextField txtfield = getChildJFXTxtField(source);
                txtfield.setVisible(true);
                //give focus
                txtfield.requestFocus();
                //childTF.setEditable(true);
            } else {
                System.out.println("Cannot modify labels, user is not admin");
            }

        }
    }

    public JFXTextField getChildJFXTxtField(Pane source) {
        ObservableList<Node> children = source.getChildrenUnmodifiable();
        JFXTextField childTF = new JFXTextField();
        for (Node node: children) {
            System.out.println("Node: " + node);
            if (node instanceof JFXTextField) {
                //System.out.println("Node " + node.getId() + " found in pane " + source + "...");
                childTF = (JFXTextField) node;
            }
        }
        return childTF;
    }

    public Label getChildLabel(Pane source) {
        ObservableList<Node> children = source.getChildrenUnmodifiable();
        Label childLabel = new Label();
        for (Node node: children) {
            System.out.println("Node: " + node);
            if (node instanceof Label) {
                childLabel = (Label) node;
            }
        }
        return childLabel;
    }


    @FXML
    void onBackIconClicked(MouseEvent event) throws Exception {
        System.out.println("Back icon clicked!");
        //Transition to main window
        Scene currentScene = ((Node) (event.getSource())).getScene();
        //functions.animateSceneTransition("/mainFrame/main.fxml", currentScene, "down");
        functions.loadStage("/mainFrame/main.fxml", "Home Screen - User: " + functions.getUsername());
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private String selectQuery(String query) {
        Connection c = null;
        Statement stmt = null;
        StringBuilder selectResult = new StringBuilder();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timetablerSQLite.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            //"SELECT * FROM users_table;"
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            //Print cols as headers
            for (int i = 1; i <= cols; i++) {
                if (i > 1) {
                    selectResult.append(", ");
                    System.out.print(",  ");
                }
                selectResult.append(rsmd.getColumnName(i));
                System.out.print(rsmd.getColumnName(i));
            }
            selectResult.append("\n");
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    if (i > 1) {
                        selectResult.append(", ");
                        System.out.print(",  ");
                    }
                    String columnValue = rs.getString(i);
                    selectResult.append(columnValue);
                    System.out.print(columnValue);
                }
                selectResult.append("\n");
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.print("\n");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            selectResult = new StringBuilder("Error in SQLite syntax!");
        }
        return selectResult.toString();
    }

    @FXML
    void onMouseClicked(MouseEvent event) {
        if (event.getSource() == runIcon) {
            if (functions.getUsername().equals("admin")) {
                System.out.println("Run button clicked!");
                //Get SQL string
                String query = editorTxtArea.getText();
                System.out.println("Query: " + query);
                //Check if query is select
                String queryType = query.substring(0, 6).toUpperCase();
                if (queryType.equals("SELECT")) {
                    System.out.println("SELECT query...");
                    //Use custom method to return string to display in console
                    String result = selectQuery(query);
                    //Clear
                    outputTxtArea.clear();
                    System.out.println("Console cleared...");
                    outputTxtArea.setText(result);
                    System.out.println("Console updated: " + result);
                } //else if (queryType.equals("INSERT") || queryType.equals("DELETE")) {

            /*} else {
                System.out.println("Other type of query...");
                queryType = "OTHER";
            }
            System.out.println("INSERT query...");
            //Use custom method to return string to display in console
            String result = query(query, queryType);
            //Clear
            outputTxtArea.clear();
            System.out.println("Console cleared...");
            outputTxtArea.setText(result);
            System.out.println("Console updated: " + result);*/
            }
        } else if (event.getSource() == filterDownArrow) {
            searchHelp(helpSearchField.getText(), true);
        } else if (event.getSource() == filterUpArrow) {
            searchHelp(helpSearchField.getText(), false);
        } else if (event.getSource() == usersListview) {
            try {
                String seletedUser = usersListview.getSelectionModel().getSelectedItem();
                System.out.println("selected: " + seletedUser);
                deleteAccountButton.setDisable(false);
            } catch (Exception ex) {
                System.out.println("List ordering exception caught!");
            }
        }

        /*else if (event.getSource() == expandIcon){
            boolean tableViewVisible = tablesTableView.isVisible();
            FontAwesomeIcon icon = (FontAwesomeIcon) event.getSource();
            if (tableViewVisible) {
                //Is visible, make invisible
                tablesTableView.setVisible(false);
                System.out.println("TableView visible: false");
                icon.setIcon(LEVEL_DOWN);
            } else {
                tablesTableView.setVisible(true);
                System.out.println("TableView visible: true");
                //Change icon
                icon.setIcon(LEVEL_UP);
            }
            System.out.println("Icon set: " + icon.getIconName());
        }*/ else {
            //Delete URL
            //Pane source = (Pane) event.getSource();
            //int paneNumber = getPaneNo(source);
            //Get child icon
            FontAwesomeIcon deleteIcon = (FontAwesomeIcon) event.getSource();
            deleteIcon.setScaleX(1.05);
            deleteIcon.setScaleY(1.05);
            System.out.println("MOUSECLICK on: " + deleteIcon);
            boolean isVisible = deleteIcon.isVisible();
            //System.out.println("MOUSE CLICK on pane: " + source + ", " + paneNumber);
            //String sourceStyle = source.getStyle();
            //if (sourceStyle.equals("-fx-background-color: #42f486")) {
            if (isVisible) {
                //Is already green, can delete respective url
                System.out.println("Is green, can delete URL");
                Pane parentPane = (Pane) deleteIcon.getParent();
                parentPane.setStyle("-fx-background-color: #d8d8d8;  -fx-background-radius: 6;");
                //Get pane number
                int paneNumber = getPaneNo(parentPane);
                //Change color and visibility
                deleteIcon.setVisible(false);
                circles[paneNumber].setVisible(false);
                System.out.println("Pane number:" + paneNumber);
                //Remove entry from SQL
                String removeURL = "DELETE FROM timetable_urls WHERE Class = " + paneNumber + ";";
                System.out.println("SQL query to remove URL: " + removeURL);
                Functions.query(removeURL);
                //Check with user before deleting
            /*
            Functions.setAlertMessage("Are you sure you would like to delete this file link?");
            functions.loadStage("/optionDialogFrame/optiondialog.fxml", "Confirm");
            Parent loader = FXMLLoader.load(getClass().getResource("/optionDialogFrame/optiondialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Confirm");
            Scene scene = new Scene(loader);
            stage.setScene(scene);
            stage.setResizable(false);
            //Set global CSS file
            scene.getStylesheets().add("/stylesheets/styles.css");
            //Get all nodes in option dialog window
            ArrayList<Node> nodes = getAllNodes(loader);
            //interate and find buttons
            Button noButton = new Button();
            Button yesButton = new Button();
            //Use atomic so other thread can see bool value
            AtomicBoolean answer = new AtomicBoolean(false);
            for (Node node: nodes) {
                System.out.println("Node IDs: " + node.getId());
                if (node.getId().equals("noButton")) {
                    noButton = (Button) node;
                } else if (node.getId().equals("yesButton")) {
                    yesButton = (Button) node;
                }
            }
            noButton.setOnAction(event1 -> {
                System.out.println("Declined");
                //already initialized as false
                ((Node) (event.getSource())).getScene().getWindow().hide();
            });
            yesButton.setOnAction(event2 -> {
                System.out.println("Accepted");
                answer.set(true);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            });
            System.out.println("Answer: " + answer);*/
            } else {
                //Has no url
                System.out.println("Not green, no URL to delete");
            }
        }
    }

    private FontAwesomeIcon getChildIconFromPane(Pane source) {
        //Get respective icon from pane children
        ObservableList<Node> children = source.getChildrenUnmodifiable();
        FontAwesomeIcon deleteIcon = new FontAwesomeIcon();
        System.out.println("Getting child icon from parent pane: " + source.getId());
        for (Node node: children) {
            System.out.println("Node: " + node);
            if (node instanceof FontAwesomeIcon) {
                System.out.println("Node " + node.getId() + " found in pane " + source + "...");
                deleteIcon = (FontAwesomeIcon) node;
            }
        }
        return deleteIcon;
    }

    private Circle getChildCircleFromPane(Pane source) {
        //Get child circle from pane
        ObservableList<Node> children = source.getChildrenUnmodifiable();
        Circle circle = new Circle();
        System.out.println("Getting child circle from parent pane: " + source.getId());
        for (Node node: children) {
            System.out.println("Node: " + node);
            if (node instanceof Circle) {
                System.out.println("Node " + node.getId() + " found in pane " + source + "...");
                circle = (Circle) node;
            }
        }
        return circle;
    }

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
    void txtFlowKeypress(KeyEvent event) {
        System.out.println("TxtFlow keypress");
    }

    private int getPaneNo(Pane source) {
        //Get class no. from source
        String substring = source.toString().substring(8, source.toString().length() - 1);
        int classNo = 0;
        for (int i = 0; i < panes.length; i++) {
            //System.out.println("Compare: " + paneNames[i] + " -- " + substring);
            if (paneNames[i].equals(substring)) {
                //System.out.println("Class No. " + i + "\n");
                classNo = i;
                break;
            }
        }
        return classNo;
    }

    @FXML
    void onToggleChanged(ActionEvent event) throws Exception {
        if (event.getSource() == darkmodeToggle) {
            System.out.println("Darkmode toggle changed: " + darkmodeToggle.isSelected());
            //Update in functions variable
            functions.setIsDark(darkmodeToggle.isSelected());
            //Save in txt file
            functions.addOutBuffer(darkmodeToggle.isSelected() + "");
            if (!functions.fileExists("darkmode.txt")) {
                functions.createTxtFile("darkmode.txt");
            }
            functions.printFile("darkmode.txt");
            //Apply changes to current window
            //Close main window & settings window
            /*Stage stage1 = new Stage();
            Scene mainWindowScene = new Scene(FXMLLoader.load(getClass().getResource("/mainFrame/main.fxml")));
            stage1.setScene(mainWindowScene);
            mainWindowScene.getWindow().hide();*/
            //Close main window


            /*Stage stage2 = new Stage();
            Scene settingsWindowScene = new Scene(FXMLLoader.load(getClass().getResource("/settingsFrame/settings2.fxml")));
            stage2.setScene(settingsWindowScene);
            settingsWindowScene.getWindow().hide();*/

            //Close settings window to refresh
            ((Node) (event.getSource())).getScene().getWindow().hide();
            //Load window back w/ new theme
            functions.loadStage("/settingsFrame/settings3.fxml", "Settings");
            //functions.loadStage("/alertFrame/alert.fxml", "Restart");
        }
    }

    //Create method, because Settings window will need to be able to update in real time
    private void setDarkMode() throws Exception {
        //String aboutImageViewURL, darkmodeToggleColor, darkmodeToggleLineColor, textColor, anchorPaneColor, tabPaneColor,
                //tabStyle;
        /*if (isDark) {
            scene.getStylesheets().remove("/stylesheets/styles.css");
            System.out.println("CSS Removed: styles.css");
            scene.getStylesheets().add("/stylesheets/darkmode.css");
            System.out.println("Darkmode CSS added");
            /*
            //Darkmode changes
            aboutImageViewURL = "/assets/darkmode/dark_blue_vector.jpg";
            darkmodeToggleColor = "#00E97A";
            darkmodeToggleLineColor = "#00ce6c";
            anchorPaneColor = "-fx-background-color: #00060c";
            tabPaneColor = "-fx-background-color: #000811";
            tabStyle = "-fx-background-color: #000811; -fx-text-fill: #697c8e; -fx-border-color: white; #00060c";
            textColor = "#697c8e";*/
        /*} else {
            scene.getStylesheets().remove("/stylesheets/darkmode.css");
            System.out.println("CSS Removed: darkmode.css");
            scene.getStylesheets().add("/stylesheets/styles.css");
            System.out.println("Styles CSS added");
            /*
            //Lightmode changes
            aboutImageViewURL = "/assets/lightmode/big-blue-vector.jpg";
            darkmodeToggleColor = "#7ae7eb";
            darkmodeToggleLineColor = "#77c2bb";
            anchorPaneColor = "-fx-background-color: #ffffff";
            tabPaneColor = "-fx-background-color: derive(gray, 99%);";
            tabStyle = "-fx-background-color: derive(gray, 99%); -fx-text-fill: #131e38; -fx-border-color: white;";
            textColor = "#131e38";*/
        /*}*/
        /*
            //Apply changes
            aboutAnchorPane.setStyle(anchorPaneColor);
            aboutImageView.setImage(new Image(aboutImageViewURL));
            darkmodeToggle.setToggleColor(Paint.valueOf(darkmodeToggleColor));
            darkmodeToggle.setToggleLineColor(Paint.valueOf(darkmodeToggleLineColor));
            darkmodeToggle.setTextFill(Paint.valueOf(textColor));
            settingsTabPane.setStyle(tabPaneColor);
            mainSettingsAnchorpane.setStyle(anchorPaneColor);

            Tab [] tabArray = {settingsTab1, settingsTab2, settingsTab3, settingsTab4};
            for (Tab tab: tabArray) {
                tab.setStyle(tabStyle);
            }

            Label [] labelArray = {versionLabel1, versionLabel2, versionLabel3};
            for (Label label: labelArray) {
                label.setTextFill(Paint.valueOf(textColor));
            }

            versionLabel1.setTextFill(Paint.valueOf(textColor));*/
    }

    private void onKeyPress() {
        //System.out.println("Text being entered..");

    }

    private void setRealPassword(String input) {
        realPassword = input;
    }

    private String getRealPassword() {
        return realPassword;
    }

    private void searchHelp(String searchKey, boolean forward) {
        System.out.println("Searching help documentation...\nKey: " + searchKey);
        //Search function
        WebEngine engine = helpWebView.getEngine();
        try {
            Field pageField = engine.getClass().getDeclaredField("page");
            pageField.setAccessible(true);
            WebPage page = (WebPage) pageField.get(engine);
            page.find(searchKey, forward, true, false);
        } catch(Exception e) {
            System.out.println("could not access help doc html page...");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //disable if restricted startup = true
        System.out.println("Restricted Mode: " + functions.isRestrictedHelpCenter());
        if (functions.isRestrictedHelpCenter()) {
            aboutTab.setDisable(true);
            timetablesTab.setDisable(true);
            acctTab.setDisable(true);
            sqlTab.setDisable(true);
            //set selected tab
            SingleSelectionModel<Tab> tabModel = settingsTabPane.getSelectionModel();
            tabModel.select(helpCenterTab);
            webviewStartup();
            //set back button listener
            settingsBackButton.setOnAction(event -> {
                try {
                    functions.loadStage("/loginFrame/login.fxml", "Login");
                    functions.setRestrictedHelpCenter(false);
                    //close window
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            //normal initialization
            //Set values on label placeholders & hide password field
            String[] columns = {"Username", "Password"};
            //Get username from functions class
            String username = functions.getUsername();
            //Pull data from database
            ArrayList<ArrayList<String>> query_result = Functions.select("SELECT * FROM users_table WHERE Username = '" + username + "';", columns);
            String usernameSQL = query_result.get(0).get(0);
            //Use public variable so it can be accessed by other methods
            setRealPassword(query_result.get(1).get(0));
            System.out.println("Username: " + usernameSQL + "\nPassword: " + getRealPassword());
            usernameLabel.setText(usernameSQL);
            //Hide password w/ asterisks
            for (int i = 0; i < getRealPassword().length(); i++) {
                hiddenPassword.append('•');
            }
            passwordLabel.setText(hiddenPassword.toString());

            //Set state of checkbox
            if (functions.fileExists("checkbox.txt")) {
                functions.scanFile("checkbox.txt");
                boolean checkBoxState = Boolean.parseBoolean(functions.getInBuffer(0));
                System.out.println("Checkbox state: " + checkBoxState);
                autoUpdateCheckBox.setSelected(checkBoxState);
            }

            //Disable new username field if admin is logged in, admin username is used as the identifier
            if (functions.getUsername().equals("admin")) {
                newUsernameTxtField.setText("admin");
                newUsernameTxtField.setDisable(true);
            }

            //Disable access to the following if not admin
            if (!functions.getUsername().equals("admin")) {
                autoUpdateCheckBox.setDisable(true);
                finalGradeSlider.setDisable(true);
            }

            //Get timetable urls from SQL table
            String [] cols = {"Class", "URL"};
            Pane [] paneArray = {u1Pane, l2Pane, u2Pane, l3Pane, u3Pane};
            JFXTextField [] txtFields = {gradeTxtfield1, gradeTxtfield2, gradeTxtfield3, gradeTxtfield4, gradeTxtfield5};
            ArrayList<ArrayList<String>> timetableUrls = Functions.select("SELECT * FROM timetable_urls", cols);
            //ArrayList result cannot be empty
            if (!timetableUrls.get(0).isEmpty()) {
                //Update UI
                System.out.println("Query result size: (c,r) " + timetableUrls.size() + ", " + timetableUrls.get(0).size());
                for (int i = 0; i < timetableUrls.get(0).size(); i++) {
                    //Get pane to be updated
                    int classNo = Integer.parseInt(timetableUrls.get(0).get(i));
                    Pane pane = paneArray[classNo];
                    JFXTextField txtField = txtFields[classNo];
                    System.out.println("ClassNo: " + classNo + " refers to pane: " + pane);
                    //Make green
                    pane.setStyle("-fx-background-color: #42f486; -fx-background-radius: 6;");
                    System.out.println("Pane: " + pane + " style set: -fx-background-color: #42f486; -fx-background-radius: 6;");
                    //Get Icon, make visible
                    System.out.println("Set pane no. " + classNo + " to enabled...");
                    FontAwesomeIcon icon = getChildIconFromPane(pane);
                    Circle circle = getChildCircleFromPane(pane);
                    icon.setVisible(true);
                    circle.setVisible(true);
                    System.out.println("Icon: " + icon + " setVisible: " + icon.isVisible());
                    //Update label color
                    Label childLabel = getChildLabel(pane);
                    childLabel.setTextFill(Paint.valueOf("#00152d"));

                    //Store to array in memory
                    setURL(timetableUrls.get(1).get(i), Integer.parseInt(timetableUrls.get(0).get(i)));
                }
                //Debugging
                printURLarray();
            } else {
                System.out.println("SQL table:timetable_urls - " + timetableUrls.get(0).isEmpty());
            }

            filepathLabel.setText("");

            //Get class names for labels and TextFields
            //JFXTextField [] txtFields = {gradeTxtfield1, gradeTxtfield2, gradeTxtfield3, gradeTxtfield4, gradeTxtfield5};
            Label [] labels = {gradeLabel1, gradeLabel2, gradeLabel3, gradeLabel4, gradeLabel5};
            String [] cols_classnames = {"ClassNo", "ClassName"};
            String getClassNames = "SELECT * FROM class_names";
            System.out.println("Retrieving class names: " + getClassNames);
            ArrayList<ArrayList<String>> classNames = Functions.select(getClassNames, cols_classnames);

            for (int i = 0; i < labels.length; i++) {
                String name = classNames.get(1).get(i);
                //Safeguard if SQL table order is incorrect
                int itemNo = Integer.parseInt(classNames.get(0).get(i));
                txtFields[itemNo].setText(name);
                labels[itemNo].setText(name);
            }

            //Assign listeners to Txtfields
            for (JFXTextField txtField : txtFields) {
            /*txtFields[i].focusedProperty().addListener((obs, oldVal, newVal) ->
                    System.out.println(newVal ? "Focused" : "Unfocused"));*/
                txtField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                    if (newPropertyValue) {
                        System.out.println("Textfield on focus");
                    } else {
                        System.out.println("Textfield out focus");
                        String newText = txtField.getText();
                        System.out.println("Text to save: " + newText);

                        Pane parentPane = (Pane) txtField.getParent();
                        int classNumber = getPaneNo(parentPane);
                        System.out.println("ClassNumber to update: " + classNumber);
                        //Save to classNames.txt
                        String sql = "UPDATE class_names SET ClassName = '" + newText + "' WHERE ClassNo = " + classNumber + ";";
                        System.out.println("UPDATE query: " + sql);
                        Functions.query(sql);
                        //Make txtfield invisible, label visible
                        Label childLabel = getChildLabel(parentPane);
                        JFXTextField childTxtfield = getChildJFXTxtField(parentPane);
                        childLabel.setVisible(true);
                        childTxtfield.setVisible(false);
                        System.out.println("txtfield hidden, label showing...");
                        //set new text as label text
                        childLabel.setText(newText);
                        System.out.println("ChildLabel: " + childLabel + " text updated.");
                    }
                });
            }

            webviewStartup();

            //Load value of max grade and initialize slider & label
            if (functions.fileExists("checkbox.txt")) {
                functions.scanFile("checkbox.txt");
                System.out.println("File length of checkbox.txt: " + functions.getInBufferLength());
                if (functions.getInBufferLength() == 3) {
                    //Contains values already
                    String finalgrade = functions.getInBuffer(2);
                    System.out.println("Value found for FinalGrade: " + finalgrade);
                    finalGradeLabel.setText(finalgrade);
                    finalGradeSlider.setValue(Double.parseDouble(finalgrade));
                } else {
                    //No value found
                    System.out.println("No value found for final grade");
                    finalGradeSlider.setValue(7.0);
                    finalGradeLabel.setText("7");
                    //Save default to txt file
                    functions.addOutBuffer(functions.getInBuffer(0) + "\n" + functions.getInBuffer(1) + "\n7");
                    functions.printFile("checkbox.txt");
                }
            } else {
                System.out.println("Defaults set, checkbox.txt does not exist... ");
                //file does not exist, set to default Grade 7
                finalGradeSlider.setValue(7.0);
                finalGradeLabel.setText("7");
            }
            //Disable slider if checkbox is not selected
            if (!autoUpdateCheckBox.isSelected()) {
                finalGradeSlider.setDisable(true);
                //Set label color to gray
                finalGradeLabel.setTextFill(Color.web("#d8d8d8"));
                finalgradeTitle.setTextFill(Color.web("#d8d8d8"));
            }
            //Slider listener, change label value according to slider value (lambda for fun)
            finalGradeSlider.valueProperty().addListener((ov, oldValue, newValue) -> finalGradeLabel.setText(Math.round(newValue.doubleValue()) + ""));

            //Initialize position of darkmode toggle
            if (functions.getIsDark()) {
                darkmodeToggle.setSelected(true);
            } else {
                darkmodeToggle.setSelected(false);
            }
            System.out.println("Darkmode Toggle init position: " + darkmodeToggle.isSelected());

        /*
        //Slider listener to save value when focus is lost
        finalGradeSlider.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                System.out.println("Focus Gained");
            }
            else {
                System.out.println("Focus Lost");
            }
        });*/
            //Set image
            if (!functions.getIsDark()) {
                aboutImageView.setImage(new Image("/assets/lightmode/big-blue-vector.jpg"));
                System.out.println("Settings imageView set (lightmode)");
                timetableSettingsImageView.setImage(new Image("/assets/lightmode/lightmode-widescreen.jpg"));
            }

            //Set listener for SQL editor
            editorTxtArea.textProperty().addListener((observable, oldValue, newValue) -> {
                onKeyPress();
            });

        /*
        //Init database tableView
        System.out.println("Initializing DB tableView");
        //Get data
        String [] tables = {db.getStudentTable(), db.getTeacherTable(), db.getStudentTeacherTable(), db.getAcademicTable(), db.getPersonalisedTable(), db.getTimetableUrls()};
        for (int i = 0; i < 6; i++) {
            System.out.println("Adding table: " + tables[i]);
            tablesTableView.getItems().add(tables[i]);
        }
        tablesColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));*/

            //Init TreeView of database
            System.out.println("Init treeview...");
            TreeViewHelper tvh = new TreeViewHelper();
            ArrayList<TreeItem<String>> tables = tvh.getTables();
            TreeItem<String> rootItem = new TreeItem<>("SQLite System Database");
            rootItem.getChildren().addAll(tables);
            treeView.setRoot(rootItem);
            System.out.println("TreeView initialized");



            if (!functions.getUsername().equals("admin")) {
                //disable advanced SQL for ordinary users
                editorTxtArea.setText("Only the administrator can run SQL queries.");
                editorTxtArea.setDisable(true);
                runIcon.setDisable(true);
            }

            //Init users listview
            if (functions.getUsername().equals("admin")) {
                String getUsers = "SELECT Username FROM users_table";
                String[] users_cols = {"Username"};
                ArrayList<ArrayList<String>> usersList = Functions.select(getUsers, users_cols);
                for (int i = 0; i < usersList.get(0).size(); i++) {
                    usersObservableList.add(usersList.get(0).get(i));
                    System.out.println("added user: " + usersList.get(0).get(i));
                }
                usersListview.getItems().addAll(usersObservableList);
            } else {
                usersObservableList.add("Only the Administrator can delete users.");
                usersListview.getItems().addAll(usersObservableList);
                usersListview.setDisable(true);
                deleteAccountButton.setDisable(true);
            }
            deleteAccountButton.setDisable(true);

            //set normal back button listener
            settingsBackButton.setOnAction(event -> {
                try {
                    functions.loadStage("/mainFrame/main.fxml", "Home Screen - User: " + username);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setURL(String URL, int index) {
        urls[index] = URL;
    }

    public String getURL(int index) {
        return urls[index];
    }

    public void printURLarray(){
        for (int i = 0; i < urls.length; i++) {
            System.out.println("Url " + i + ": " + urls[i]);
        }
    }

    private void webviewStartup() {
        //Init webview
        WebEngine engine = helpWebView.getEngine();
        URL helpFile = getClass().getResource("/WebViewHelp/main.html");
        if (functions.getIsDark()) {
            helpFile = getClass().getResource("/WebViewHelp/main-dark.html");
        }
        engine.load(helpFile.toExternalForm());

        //Init search UI
        helpSearchField.setVisible(false);
        filterUpArrow.setVisible(false);
        filterDownArrow.setVisible(false);
        helpHidingPane.setVisible(false);
        helpArrowPane.setVisible(false);

        helpSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchHelp(newValue, true);
            if (!newValue.equals("")) {
                filterUpArrow.setVisible(true);
                filterDownArrow.setVisible(true);
            } else {
                filterDownArrow.setVisible(false);
                filterUpArrow.setVisible(false);
            }
        });
    }

    private void dragDropPrompt() {
        String errorMsg = "Drag and drop timetables to the respective box to add it to the program";
        Functions.setAlertMessage(errorMsg);
        System.out.println("ALERT MSG: " + errorMsg);
        try {
            functions.loadStage("/alertFrame/alert.fxml", "Error");
        } catch (Exception e) {
            System.out.println("****Error displaying error message: " + errorMsg);
        }
    }
}