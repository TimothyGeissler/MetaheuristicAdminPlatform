package alertFrame;

import com.root.Functions;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphIconName;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName.INFO_CIRCLE;

public class AlertController implements Initializable {

    Functions functions = new Functions();

    @FXML
    private FontAwesomeIcon headerIcon;

    @FXML
    private Label alertMessageLabel;

    @FXML
    private Button closeNotificationButton;

    @FXML
    void handleButtonClicks(ActionEvent event) {
        if (event.getSource() == closeNotificationButton) {
            //Close notification window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Notification window initialized");
        alertMessageLabel.setText(Functions.getAlertMessage());
        System.out.println("Is info dialog: " + Functions.getIsInfoMessage());
        if (Functions.getIsInfoMessage()) {
            //Is info dialog, modify UI
            headerIcon.setIcon(INFO_CIRCLE);
            headerIcon.setFill(Paint.valueOf("#00e9a3"));
        }
        //Set value for isInfoDialog to default
        Functions.setIsInfoMessage(false);
    }
}
