package signupFrame;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class SignupController {

    Functions functions = new Functions();

    @FXML
    private Button signupButton;

    @FXML
    private Button cancelButton;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == signupButton) {
            //Signup button clicked, read information from text fields
            System.out.println("Signup clicked");
            String username = usernameField.getText(), password = passwordField.getText();
            System.out.println("Username: " + username + "\nPassword: " + password);
            //Check if username is already present
            //Columns to return
            String [] columns = {"Username"};
            ArrayList<ArrayList<String>> queryResult = Functions.selectQuery("SELECT * FROM users_table WHERE Username = '" + username + "';", columns);
            String errorMsg = "";
            if (queryResult.size() > 1) {
                //query has returned results, there is a matching username
                System.out.println("Username already present...");
                errorMsg = errorMsg + "There is already a user with this username\n";
                functions.loadStage("/notificationFrame/notification.fxml", "Error");
            } if (password.length() < 8) {
                //New username, may be added to database
                System.out.println("Password not long enough");
                //Show alert dialog
                errorMsg = errorMsg + "Password is not long enough\n";
                functions.loadStage("/notificationFrame/notification.fxml", "Error");
            } if (queryResult.size() == 0 && password.length() > 7) {
                //New username & password long enough, may be added to database
                System.out.println("Unique username, password long enough - may be inserted");
                Functions.insdelQuery("INSERT INTO users_table (Username, Password) VALUES (" + username + ", " + password + ");");
                System.out.println("Details saved");
            } else {
                Functions.setAlertMessage(errorMsg);
                System.out.println("ALERT MSG: " + errorMsg);
                functions.loadStage("/notificationFrame/notification.fxml", "Error");
            }
            /*
            try {
                ArrayList<ArrayList<String>> queryResult = Functions.selectQuery("SELECT * FROM users_table WHERE Username = '" + username + "';", columns);
                //Use null-safe comparison
                if (Objects.equals(queryResult.get(0).get(0), username)) {
                    //Pre-existing username
                    System.out.println("Username already present...");
                    //Show alert dialog
                    Functions.setAlertMessage("There is already a user with this username");
                    functions.loadStage("/notificationFrame/notification.fxml", "Error");
                } else  if (password.length() >= 8){
                    //New username, may be added to database
                    System.out.println("Unique username, password long enough - may be inserted");
                    Functions.insdelQuery("INSERT INTO users_table (Username, Password) VALUES (" + username + ", " + password + ");");
                    System.out.println("Details saved");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in select query, Exception caught");
            }*/
        } else if (event.getSource() == cancelButton) {
            //Close window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

}
