package optionDialogFrame;

import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionDialogController implements Initializable {

    @FXML
    private Label alertMessageLabel;

    @FXML
    private Button noButton;

    @FXML
    private Button yesButton;

    private boolean ans;

    @FXML
    void handleButtonClicks(ActionEvent event) {
        if (event.getSource() == yesButton) {
            System.out.println("Accepted");
            ans = true;
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else {
            System.out.println("Declined");
            ans = false;
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    public boolean getAnswer() {
        return ans;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Notification window initialized");
        alertMessageLabel.setText(Functions.getAlertMessage());
    }
}
