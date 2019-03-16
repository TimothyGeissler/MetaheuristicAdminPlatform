package loginFrame;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.root.Functions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.events.EventException;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Functions functions = new Functions();

    @FXML
    FontAwesomeIcon loginGearIcon;
    @FXML
    Button loginGearButton;
    @FXML
    Button loginButton;
    @FXML
    Button signupButton;
    @FXML
    JFXTextField usernameField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    private FontAwesomeIcon usernameIcon;
    @FXML
    private FontAwesomeIcon passwordIcon;
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private Button powerOffButton;


    @FXML
    private void handleButtonClicks(ActionEvent mouseEvent) throws Exception {
        if (mouseEvent.getSource() == loginButton) {
            //Pass through mouse event to allow for close of login screen if login is successful
            if (LoginProcedure()) {
                //Successful Login, close login window
                ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
            }
        } else if (mouseEvent.getSource() == signupButton) {
            //Route to singup form
            functions.loadStage("/signupFrame/signup.fxml", "Signup Form");
        } else if (mouseEvent.getSource() == loginGearButton) {
            System.out.println("Login gear clicked...");
            //Load database settings window
            functions.loadStage("/loginsettingsFrame/loginsettings.fxml", "Database Settings");
        } else if (mouseEvent.getSource() == powerOffButton) {
            //Close app
            System.out.println("Close Program...");
            System.exit(0);
        }
    }


    //To be triggered if user clicks Login or presses enter
    private boolean LoginProcedure() throws Exception {
        boolean successful = false;
        System.out.println("Login");
        //Check login credentials with SQL users database
        String username = usernameField.getText(), password = passwordField.getText(); //Get details from form
        //Fields to check
        String[] columns = {"UserID", "Username", "Password"};
        //Pull data from database
        ArrayList<ArrayList<String>> query_result = Functions.selectQuery("SELECT * FROM users_table WHERE Username = '" + username + "' AND Password = '" + password + "';", columns);
        //Correct details will trigger if statement, invalid details will be caught by the Exception handler
        //.get(column).get(row)
        try {
            if (query_result.get(1).get(0).equals(username) && query_result.get(2).get(0).equals(password)) {
                functions.loadStage("/mainFrame/main.fxml", "Home Screen - User: " + username);
                //Set global username variable
                functions.setUsername(username);
                successful = true;
            }
        } catch (Exception ex) {
            System.out.println("Data not found");
            //show alert dialog
            Functions.setAlertMessage("Invalid login details");
            functions.loadStage("/notificationFrame/notification.fxml", "Error");
            successful = false;
        }
        return successful;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Login Frame initialized...");

    }

    public void keyPressed(javafx.scene.input.KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            System.out.println("ENTER KeyPress Detected!!!");
            if (LoginProcedure()) {
                //Successful Login, close login window
                ((Node) (keyEvent.getSource())).getScene().getWindow().hide();
            }
        }
    }
}
