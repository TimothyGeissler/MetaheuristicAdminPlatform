package dashboardFrame;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label noOfStudentsLabel;

    @FXML
    private Label noOfLessonsLabel;

    @FXML
    private Label hoursWeekLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set values for data labels and chart at startup
        noOfStudentsLabel.setText("16");
        noOfLessonsLabel.setText("33");
        hoursWeekLabel.setText("72");
    }
}
