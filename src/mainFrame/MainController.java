package mainFrame;

import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    Functions functions = new Functions();

    @FXML
    private Button dashboardButton;

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
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == dashboardButton) {
            System.out.println("Dashboard");
            //Route to dashboard
            functions.loadStage("/dashboardFrame/dashboard.fxml", "My Dashboard");
        } else if (event.getSource() == timetableButton) {
            System.out.println("Timetable button");
            functions.loadStage("/timetableFrame/timetable.fxml", "My Timetable");
        } else if (event.getSource() == studentsButton) {
            System.out.println("Students button");
            functions.loadStage("/studentsFrame/studentsv2.fxml", "My Students");
        } else if (event.getSource() == atimetableButton) {
            System.out.println("Academic timetable button");
            functions.loadStage("/academicTimetableFrame/academic_timetable.fxml", "Academic Timetable");
        } else if (event.getSource() == settingsButton) {
            functions.loadStage("/settingsFrame/settings2.fxml", "Settings");
            System.out.println("Settings button");
        } else if (event.getSource() == logoutButton) {
            System.out.println("Logout button");
            functions.loadStage("/loginFrame/login.fxml", "Login");
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Home Screen initialized...");
    }
}
