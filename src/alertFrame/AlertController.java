package alertFrame;

import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AlertController implements Initializable {

    @FXML
    private AnchorPane notificationAnchorPane;

    @FXML
    private Label alertMessageLabel;

    @FXML
    private Button closeButton;

    @FXML
    void handleButtonClicks(ActionEvent event) {
        if (event.getSource() == closeButton) {
            //Close notification window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Notification window initialized");
        alertMessageLabel.setText(Functions.getAlertMessage());
    }
}
