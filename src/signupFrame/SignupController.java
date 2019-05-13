package signupFrame;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class SignupController {

    Functions functions = new Functions();

    @FXML
    private Button realSignupButton;

    @FXML
    private Button cancelButton;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    void handleButtonClicks(ActionEvent event) throws Exception {
        if (event.getSource() == realSignupButton) {
            //Signup button clicked, read information from text fields
            System.out.println("Signup clicked");
            String username = usernameField.getText(), password = passwordField.getText();
            System.out.println("Username: " + username + "\nPassword: " + password);
            //Check if username is already present
            //Columns to return
            String [] columns = {"Username"};
            ArrayList<ArrayList<String>> queryResult = Functions.select("SELECT * FROM users_table WHERE Username = '" + username + "';", columns);
            String errorMsg = "";
            System.out.println("queryResult.isEmpty() = " + queryResult.get(0).isEmpty() + "\npassword.length() = " + password.length());
            if (!queryResult.get(0).isEmpty()) {
                //query has returned results, there is a matching username
                System.out.println("Username already present...");
                errorMsg = errorMsg + "There is already a user with this username\n";
            } if (password.length() < 8) {
                //New username, may be added to database
                System.out.println("Password not long enough");
                //Show alert dialog
                errorMsg = errorMsg + "Password is not long enough\n";
            } if (queryResult.get(0).isEmpty() && password.length() > 7) {
                //New username & password long enough, may be added to database
                System.out.println("Unique username, password long enough - may be inserted");
                Functions.query("INSERT INTO users_table (Username, Password) VALUES ('" + username + "', '" + password + "');");
                System.out.println("Details saved");
                //Exit
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } else {
                Functions.setAlertMessage(errorMsg);
                System.out.println("ALERT MSG: " + errorMsg);
                functions.loadStage("/alertFrame/alert.fxml", "Error");
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
                    functions.loadStage("/alertFrame/alert.fxml", "Error");
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
