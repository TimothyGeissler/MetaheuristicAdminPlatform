package settingsFrame;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SettingsController2 implements Initializable {

    Functions functions = new Functions();
    private String realPassword;
    private StringBuilder hiddenPassword = new StringBuilder();

    @FXML
    private JFXPasswordField newPasswordTxtField;

    @FXML
    private JFXTextField newUsernameTxtField;

    @FXML
    private JFXPasswordField newPasswordTxtField2;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button showPasswordButton;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button saveAcctButton;

    @FXML
    private JFXCheckBox autoUpdateCheckBox;

    @FXML
    void handleActions(ActionEvent event) throws Exception {
        if (event.getSource() == saveAcctButton) {
            System.out.println("Save new details...");
            String errorMessage = "";
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
                //run query
                functions.insdelQuery("");
            } else {
                //Display alert window
                Functions.setAlertMessage(errorMessage);
                functions.loadStage("/notificationFrame/notification.fxml", "Error");
            }
        } else if (event.getSource() == deleteAccountButton) {
            System.out.println("Delete Account after prompt");
        } else if (event.getSource() == autoUpdateCheckBox) {
            System.out.println("Checkbox: " + autoUpdateCheckBox.isSelected());
            //Save state of checkbox
            if (!functions.fileExists("checkbox.txt")) {
                //Create file
                functions.createTxtFile("checkbox.txt");
            }
            //File exists, update values
            functions.addOutBuffer(autoUpdateCheckBox.isSelected() + "");
            functions.printFile("checkbox.txt");
        }
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
        }
    }

    public void setRealPassword(String input) {
        realPassword = input;
    }

    private String getRealPassword() {
        return realPassword;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set values on label placeholders & hide password field
        String[] columns = {"UserID", "Username", "Password"};
        //Get username from functions class
        String username = functions.getUsername();
        //Pull data from database
        ArrayList<ArrayList<String>> query_result = Functions.selectQuery("SELECT * FROM users_table WHERE Username = '" + username + "';", columns);
        String usernameSQL = query_result.get(1).get(0);
        //Use public variable so it can be accessed by other methods
        setRealPassword(query_result.get(2).get(0));
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
            Boolean checkBoxState = Boolean.valueOf(functions.getInBuffer(0));
            System.out.println("Checkbox state: " + checkBoxState);
            autoUpdateCheckBox.setSelected(checkBoxState);
        }

        //Disable new username field if admin is logged in, admin username is used as the identifier
        if (functions.getUsername().equals("admin")) {
            newUsernameTxtField.setText("admin");
            newUsernameTxtField.setDisable(true);
        }

    }
}