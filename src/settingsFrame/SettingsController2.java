package settingsFrame;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.root.DatabaseSettings;
import com.root.Functions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import optionDialogFrame.OptionDialogController;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsController2 implements Initializable {

    private Functions functions = new Functions();
    private String realPassword;
    private StringBuilder hiddenPassword = new StringBuilder();
    //private Map<String, FontAwesomeIcon> iconMap = new HashMap<>();

    @FXML
    private Pane u1a_pane;

    @FXML
    private Label U1CPane;

    @FXML
    private Pane u1b_pane;

    @FXML
    private Label U1APane;

    @FXML
    private Pane u1c_pane;

    @FXML
    private Pane l2d_pane;

    @FXML
    private Label U1CPane1;

    @FXML
    private Pane l2e_pane;

    @FXML
    private Label U1CPane11;

    @FXML
    private Pane l2f_pane;

    @FXML
    private Label U1CPane12;

    @FXML
    private Pane u2g_pane;

    @FXML
    private Label U1CPane13;

    @FXML
    private Pane u2h_pane;

    @FXML
    private Label U1CPane14;

    @FXML
    private Pane u2i_pane;

    @FXML
    private Label U1CPane15;

    @FXML
    private Pane l3j_pane;

    @FXML
    private Label U1CPane16;

    @FXML
    private Pane l3k_pane;

    @FXML
    private Label U1CPane17;

    @FXML
    private Pane l3l_pane;

    @FXML
    private Label U1CPane18;

    @FXML
    private Pane u3m_pane;

    @FXML
    private Label U1CPane181;

    @FXML
    private Pane u3n_pane;

    @FXML
    private Label U1CPane1811;

    @FXML
    private Pane u3o_pane;

    @FXML
    private Label U1CPane1812;

    @FXML
    private JFXTextField newUsernameTxtField;

    @FXML
    private JFXPasswordField newPasswordTxtField;

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

    private DatabaseSettings db = new DatabaseSettings();
    private Pane [] panes = {u1a_pane, u1b_pane, u1c_pane, l2d_pane, l2e_pane, l2f_pane, u2g_pane, u2h_pane, u2i_pane, l3j_pane, l3k_pane, l3l_pane, u3m_pane, u3n_pane, u3o_pane};
    private String [] iconNames = {"u1a_icon", "u1b_icon", "u1c_icon", "l2d_icon", "l2e_icon", "l2f_icon", "u2g_icon", "u2H_icon", "u2i_icon", "l3j_icon", "l3k_icon", "l3l_icon", "u3m_icon", "u3n_icon", "u3o_icon"};
    private String [] paneNames = {"u1a_pane", "u1b_pane", "u1c_pane", "l2d_pane", "l2e_pane", "l2f_pane", "u2g_pane", "u2H_pane", "u2i_pane", "l3j_pane", "l3k_pane", "l3l_pane", "u3m_pane", "u3n_pane", "u3o_pane"};

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
                Functions.insdelQuery("");
            } else {
                //Display alert window
                Functions.setAlertMessage(errorMessage);
                functions.loadStage("/alertFrame/alert.fxml", "Error");
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

    @FXML
    void onDragOver(DragEvent event) {
        if (event.getGestureSource() != U1APane && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    void onDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard .hasFiles()) {
            Pane source = (Pane) event.getSource();
            String substring = source.toString().substring(8, source.toString().length() - 1);
            String URL = dragboard.getFiles().toString();
            System.out.println("--Files received: " + URL + "\n--Source: " + source);
            //Modify URL, must have 2 \\ for every \
            URL = URL.replace("\\", "\\\\");
            System.out.println("New URL to be sent to SQL: " + URL);
            int paneNumber = getPaneNo(source);
            //Save to SQL
            String insQuery = "INSERT INTO " + db.getTimetableUrls() + " (Class, URL) VALUES ('" + paneNumber + "', '" + URL.substring(1, URL.length() - 1) + "');";
            System.out.println(insQuery);
            Functions.insdelQuery(insQuery);
            //Update UI
            //String iconKey = iconNames[classNo];
            //System.out.println("IconKey: " + iconKey + "\nIcon to update: " + icons[classNo].getId());//iconMap.get(iconKey).getId());
            //iconMap.get(iconKey).setIcon(CHECK_CIRCLE);
            System.out.println("Pane to update: " + source);
            //get child icon from pane
            FontAwesomeIcon deleteIcon = getChildIconFromPane(source);
            deleteIcon.setVisible(true);
            //Update pane appearance
            source.setStyle("-fx-background-color: #42f486");
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    void onMouseClicked(MouseEvent event) throws Exception {
        //Delete URL
        //Pane source = (Pane) event.getSource();
        //int paneNumber = getPaneNo(source);
        //Get child icon
        FontAwesomeIcon deleteIcon = (FontAwesomeIcon) event.getSource();
        deleteIcon.setScaleX(1.05);
        deleteIcon.setScaleY(1.05);
        System.out.println("MOUSECLICK on: " + deleteIcon);
        boolean isVisible = deleteIcon.isVisible();
        //System.out.println("MOUSE CLICK on pane: " + source + ", " + paneNumber);
        //String sourceStyle = source.getStyle();
        //if (sourceStyle.equals("-fx-background-color: #42f486")) {
        if (isVisible) {
            //Is already green, can delete respective url
            System.out.println("Is green, can delete URL");
            //Change color and visibility
            deleteIcon.setVisible(false);
            Pane parentPane = (Pane) deleteIcon.getParent();
            parentPane.setStyle("-fx-background-color: #d8d8d8");
            //Get pane number
            int paneNumber = getPaneNo(parentPane);
            System.out.println("Pane number:" + paneNumber);
            //Remove entry from SQL
            String removeURL = "DELETE FROM " + db.getTimetableUrls() + " WHERE Class = " + paneNumber + ";";
            System.out.println("SQL query to remove URL: " + removeURL);
            Functions.insdelQuery(removeURL);
            //Check with user before deleting
            /*
            Functions.setAlertMessage("Are you sure you would like to delete this file link?");
            functions.loadStage("/optionDialogFrame/optiondialog.fxml", "Confirm");
            Parent loader = FXMLLoader.load(getClass().getResource("/optionDialogFrame/optiondialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Confirm");
            Scene scene = new Scene(loader);
            stage.setScene(scene);
            stage.setResizable(false);
            //Set global CSS file
            scene.getStylesheets().add("/stylesheets/styles.css");
            //Get all nodes in option dialog window
            ArrayList<Node> nodes = getAllNodes(loader);
            //interate and find buttons
            Button noButton = new Button();
            Button yesButton = new Button();
            //Use atomic so other thread can see bool value
            AtomicBoolean answer = new AtomicBoolean(false);
            for (Node node: nodes) {
                System.out.println("Node IDs: " + node.getId());
                if (node.getId().equals("noButton")) {
                    noButton = (Button) node;
                } else if (node.getId().equals("yesButton")) {
                    yesButton = (Button) node;
                }
            }
            noButton.setOnAction(event1 -> {
                System.out.println("Declined");
                //already initialized as false
                ((Node) (event.getSource())).getScene().getWindow().hide();
            });
            yesButton.setOnAction(event2 -> {
                System.out.println("Accepted");
                answer.set(true);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            });
            System.out.println("Answer: " + answer);*/
        } else {
            //Has no url
            System.out.println("Not green, no URL to delete");
        }
    }

    private FontAwesomeIcon getChildIconFromPane(Pane source) {
        //Get respective icon from pane children
        ObservableList<Node> children = source.getChildrenUnmodifiable();
        FontAwesomeIcon deleteIcon = new FontAwesomeIcon();
        for (Node node: children) {
            System.out.println("Node: " + node);
            if (node instanceof FontAwesomeIcon) {
                System.out.println("Node " + node.getId() + " found in pane " + source + "...");
                deleteIcon = (FontAwesomeIcon) node;
            }
        }
        return deleteIcon;
    }

    @FXML
    void onMouseEntered(MouseEvent event) {
        FontAwesomeIcon source = (FontAwesomeIcon) event.getSource();
        source.setScaleX(1.1);
        source.setScaleY(1.1);
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        FontAwesomeIcon source = (FontAwesomeIcon) event.getSource();
        source.setScaleY(1);
        source.setScaleX(1);
    }

/*
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            System.out.println("Node: " + node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }*/

    private int getPaneNo(Pane source) {
        //Get class no. from source
        String substring = source.toString().substring(8, source.toString().length() - 1);
        int classNo = 0;
        System.out.println("Iterating: " + panes.length);
        for (int i = 0; i < panes.length; i++) {
            System.out.println("Compare: " + paneNames[i] + " -- " + substring);
            if (paneNames[i].equals(substring)) {
                System.out.println("Class No. " + i);
                classNo = i;
                break;
            }
        }
        return classNo;
    }

    private void setRealPassword(String input) {
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
            boolean checkBoxState = Boolean.parseBoolean(functions.getInBuffer(0));
            System.out.println("Checkbox state: " + checkBoxState);
            autoUpdateCheckBox.setSelected(checkBoxState);
        }

        //Disable new username field if admin is logged in, admin username is used as the identifier
        if (functions.getUsername().equals("admin")) {
            newUsernameTxtField.setText("admin");
            newUsernameTxtField.setDisable(true);
        }
        //Get timetable urls from SQL table
        String [] cols = {"Class", "URL"};
        ArrayList<ArrayList<String>> timetableUrls = Functions.selectQuery("SELECT * FROM timetable_urls", cols);
        Pane [] paneArray = {u1a_pane, u1b_pane, u1c_pane, l2d_pane, l2e_pane, l2f_pane, u2g_pane, u2h_pane, u2i_pane, l3j_pane, l3k_pane, l3l_pane, u3m_pane, u3n_pane, u3o_pane};
        //ArrayList result cannot be empty
        if (!timetableUrls.get(0).isEmpty()) {
            //Update UI
            System.out.println("Query result size: (c,r) " + timetableUrls.size() + ", " + timetableUrls.get(0).size());
            for (int i = 0; i < timetableUrls.get(0).size(); i++) {
                //Get pane to be updated
                int classNo = Integer.parseInt(timetableUrls.get(0).get(i));
                Pane pane = paneArray[classNo];
                System.out.println("ClassNo: " + classNo + " refers to pane: " + pane);
                //Make green
                pane.setStyle("-fx-background-color: #42f486");
                System.out.println("Pane: " + pane + " style set: -fx-background-color: #42f486");
                //Get Icon, make visible
                FontAwesomeIcon icon = getChildIconFromPane(pane);
                icon.setVisible(true);
                System.out.println("Icon: " + icon + " setVisible: " + icon.isVisible());
            }
        } else {
            System.out.println("SQL table:" + db.getTimetableUrls() + " - " + timetableUrls.get(0).isEmpty());
        }

    }

    private void dragDropPrompt() {
        String errorMsg = "Drag and drop timetables to the respective box to add it to the program";
        Functions.setAlertMessage(errorMsg);
        System.out.println("ALERT MSG: " + errorMsg);
        try {
            functions.loadStage("/alertFrame/alert.fxml", "Error");
        } catch (Exception e) {
            System.out.println("****Error displaying error message: " + errorMsg);
        }
    }
}