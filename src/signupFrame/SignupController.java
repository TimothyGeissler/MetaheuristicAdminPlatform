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

public class SignupController {

    Functions functions = new Functions();

    @FXML
    private Button signupButton;

    @FXML
    private Button closeButton;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    void handleButtonClicks(ActionEvent event) {
        if (event.getSource() == signupButton) {
            //Signup button clicked, read information from text fields
            System.out.println("Signup clicked");
            String username = usernameField.getText(), password = passwordField.getText();
            //Check if username is already present
            //Columns to return
            String [] columns = {"Username"};
            //Rows to return
            int rows = 1;
            try {
                ArrayList<ArrayList<String>> queryResult = functions.selectQuery("SELECT * FROM users_table WHERE Username = '" + username + "';", columns);
                if (queryResult.get(0).get(0) == username) {
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
            }
        } else if (event.getSource() == "closeButton") {
            //Close window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

}
