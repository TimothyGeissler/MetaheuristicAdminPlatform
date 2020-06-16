package mainFrame;

import com.root.Functions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import studentsFrame.StudentsControllerv2;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    Functions functions = new Functions();

    @FXML
    private Button dashboardButton;

    @FXML
    private ImageView mainImageView;

    @FXML
    private Button timetableButton;

    @FXML
    private Button studentsButton;

    @FXML
    private Button atimetableButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private FontAwesomeIcon updateInfo;

    @FXML
    StackPane stackPane;

    @FXML
    AnchorPane mainAnchorPane;

    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        Scene currentScene = ((Node) (event.getSource())).getScene();
        if (event.getSource() == dashboardButton) {
            System.out.println("Dashboard");
            //Route to dashboard
            //animator.animateInto("/dashboardFrame/dashboard.fxml", "My Dashboard", dashboardButton.getScene(), mainAnchorPane, stackPane);
            functions.loadStage("/dashboardFrame/dashboard2.fxml", "My Dashboard");
        } else if (event.getSource() == timetableButton) {
            System.out.println("Timetable button");
            functions.loadStage("/timetableFrame/timetable2.fxml", "My Timetable");
        } else if (event.getSource() == studentsButton) {
            System.out.println("Students button");
            functions.loadStage("/studentsFrame/students3.fxml", "My Students");
        } else if (event.getSource() == atimetableButton) {
            System.out.println("Academic timetable button");
            functions.loadStage("/academicTimetableFrame/academic_timetable.fxml", "Academic Timetable");
        } else if (event.getSource() == settingsButton) {
            functions.loadStage("/settingsFrame/settings3.fxml", "Settings");
            //functions.animateSceneTransition("/settingsFrame/settings2.fxml", currentScene, "up");
            System.out.println("Settings button");
        } else if (event.getSource() == logoutButton) {
            System.out.println("Logout button");
            functions.loadStage("/loginFrame/login.fxml", "Login");
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }


    @FXML
    void onMouseClicked(MouseEvent event) throws Exception {
        if (event.getSource() == updateInfo) {
            System.out.println("Info button clicked, show infoDialogBox");
            //Show info dialog
            Functions.setIsInfoMessage(true);
            Functions.setAlertMessage("The grade of each student has been updated automatically, students that have left school have been removed.");
            functions.loadStage("/alertFrame/alert.fxml", "Auto-Update Successful!");
        }
    }

    //Scale animations
    @FXML
    void onMouseEntered(MouseEvent event) {
        updateInfo.setScaleX(1.1);
        updateInfo.setScaleY(1.1);
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        updateInfo.setScaleX(1);
        updateInfo.setScaleY(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Home Screen initialized...");
        //Check if auto-update feature is on
        if (functions.fileExists("checkbox.txt")) {
            functions.scanFile("checkbox.txt");
            boolean isEnabled = Boolean.parseBoolean(functions.getInBuffer(0));
            System.out.println("Auto-Update feature enabled: " + isEnabled);
            if (isEnabled) {
                //Check if data needs to be updated by comparing saved date to current date
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                //Syncronize date w/ system timezone
                Date currentDate = new Date();
                LocalDate currentlocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate oldDate = LocalDate.parse(functions.getInBuffer(1));
                System.out.println("Current date: " + currentlocalDate + ", last login: " + oldDate); //2016/11/16 12:08:43
                //Compare dates
                if (oldDate.getYear() != currentlocalDate.getYear()) {
                    //Different years, can update data
                    System.out.println("Different years, can update data...");
                    //Get studentIDs of students to delete
                    String [] cols = {"StudentID"};
                    ArrayList<ArrayList<String>> results = Functions.select("SELECT StudentID FROM students_table WHERE StudentGrade = " + functions.getInBuffer(2), cols);
                    //Delete where grade = final grade from student teacher @ student table
                    for (int i = 0; i < results.get(0).size(); i++) {
                        //Use pre-existing method from StudentsController
                        StudentsControllerv2 students = new StudentsControllerv2();
                        students.deleteFromBothTables(results.get(0).get(i));
                    }
                    //Update grade info
                    String updateGrade = "UPDATE students_table SET StudentGrade = StudentGrade + 1;";
                    System.out.println("Update grade: " + updateGrade);
                    Functions.query(updateGrade);
                    //Notify user that update has taken place
                    updateInfo.setVisible(true);
                } else {
                    //Same year
                    System.out.println("Same year, nothing to change. Update date in checkbox.txt");
                }
                //regardless, update date
                functions.addOutBuffer("true");
                functions.addOutBuffer(currentlocalDate + "");
                //include final grade slider position if already present
                if (functions.getInBufferLength() == 3) {
                    functions.addOutBuffer(functions.getInBuffer(2));
                }
                functions.printFile("checkbox.txt");

            } else {
                //Nothing to be done
                System.out.println("Student data does not need to be updated");
            }
        } else {
            Date currentDate = new Date();
            LocalDate currentlocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("Creating checkbox.txt w/ date: " + currentlocalDate);
            //No checkbox.txt file, create one
            functions.createTxtFile("checkbox.txt");
            //Store login date on 2nd line
            functions.addOutBuffer("\n" + currentlocalDate);
            functions.printFile("checkbox.txt");
        }
        //Set to lightmode picture
        if (!functions.getIsDark()) {
            mainImageView.setImage(new Image("/assets/lightmode/lightmode-widescreen.jpg"));

            System.out.println("Set main wallpaper (light): /assets/lightmode/big-blue-vector.jpg");
        }
    }
}
