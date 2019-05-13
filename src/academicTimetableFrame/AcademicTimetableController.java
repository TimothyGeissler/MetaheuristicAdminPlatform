package academicTimetableFrame;

import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AcademicTimetableController implements Initializable {

    public Functions functions;

    @FXML
    private AnchorPane academicTimetableAnchorPane;

    @FXML
    private Tab u1Tab;

    @FXML
    private AnchorPane u1AnchorPane;

    @FXML
    private GridPane u1GridPane;

    @FXML
    private Tab l2Tab;

    @FXML
    private AnchorPane l2AnchorPane;

    @FXML
    private GridPane l2GridPane;

    @FXML
    private Tab u2Tab;

    @FXML
    private AnchorPane u2AnchorPane;

    @FXML
    private GridPane u2GridPane;

    @FXML
    private Tab l3Tab;

    @FXML
    private AnchorPane l3AnchorPane;

    @FXML
    private GridPane l3GridPane;

    @FXML
    private Tab u3Tab;

    @FXML
    private AnchorPane u3AnchorPane;

    @FXML
    private GridPane u3GridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Label> labelList = new ArrayList<>();
        GridPane [] gridpanes = {u1GridPane, l2GridPane, u2GridPane, l3GridPane, u3GridPane};
        String [] cols = {"L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12", "L13", "L14", "L15", "L16", "L17", "L18", "L19", "L20", "L21", "L22", "L23", "L24", "L25", "L26", "L27", "L28", "L29", "L30"};
        //Assign values to labels from SQL: academic_timetables
        int index = 0;
        for (int z = 0; z < 5; z++) {
            String sql = "SELECT * FROM academic_timetables WHERE ClassNumber = " + z;
            ArrayList<ArrayList<String>> temp = Functions.select(sql, cols);
            System.out.println("ArrayList.get(0).size() @ z = " + z + ": " + temp.get(0).size());
                //Fill into labels
                int min = 30 * z, max = 15 * ((z * 2) + 2), count = 0;
                System.out.println("Ranges: \n" + min + " < x < " + max);

                int row_counter = 1, col_counter = 1;
                for (int x = min; x < max; x++) {
                    //Add labels to labelList
                    labelList.add(new Label());
                    labelList.get(index).setId("label" + index);
                    labelList.get(index).setText("label" + index);
                    labelList.get(index).setFont(new Font("Century Gothic", 13));

                    if (col_counter % 6 == 0) {
                        row_counter++;
                    }
                    //reset rows
                    if (row_counter == 7) {
                        row_counter = 1;
                    }
                    //reset cols
                    if (col_counter == 6) {
                        col_counter = 1;
                    }

                    //Make items grow to fill gridpane cell
                    labelList.get(index).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    //Center labels
                    labelList.get(index).setAlignment(Pos.CENTER);
                    gridpanes[z].setAlignment(Pos.CENTER);
                    //Add labellist @ index to gridpane @ index
                    System.out.println("LabelList @: " + index);
                    gridpanes[z].add(labelList.get(index), col_counter, row_counter);
                    index++;
                    //Give labels values from SQL result if present
                    if (temp.get(0).size() > 0) {
                        labelList.get(x).setText(temp.get(count).get(0));
                        System.out.println(temp.get(count).get(0));
                        count++;
                    } else {
                        //fill with blank values
                        labelList.get(x).setText("");
                    }
                    col_counter++;
                }
            //}
        }

        //Map tabs to array using HashMap and alphabet iterator to generate names
        //System.out.println("Generating tab strings");
        Tab [] tabNames = {u1Tab, l2Tab, u2Tab, l3Tab, u3Tab};
        String [] cols_tabnames = {"ClassName"};
        String sql = "SELECT ClassName FROM class_names";
        System.out.println(sql);
        ArrayList<ArrayList<String>> results = Functions.select(sql, cols_tabnames);

        for (int i = 0; i < results.get(0).size(); i++) {
            String tempName = results.get(0).get(i);
            System.out.print("Name of Class retrieved: " + tempName + ", lastIndexOf(' ') = " + tempName.lastIndexOf(' '));
            if (tempName.lastIndexOf(' ') == 7) {
                //Uses format 'Upper 3' + teacher initials
                System.out.println(" - Use standard format");
                String concat = standardize("Class " + tempName.substring(tempName.lastIndexOf(' ')));
                System.out.println("Standardized: '" + concat + "', Len: " + concat.length());
                tabNames[i].setText(concat);
            } else {
                System.out.println(" - Uses other format");
                String standardized = standardize(tempName);
                System.out.println("Standardized: '" + standardized  + "', Len: " + standardized .length());
                tabNames[i].setText(standardized);
            }
        }
     }
    //Standardize all string to 17 chars long for equal tab size
    private String standardize(String input) {
        int inputleng = input.length();
        for (int i = 0; i < (17 - inputleng) / 2; i++) {
            input = " " + input;
        }
        for (int j = 0; j < (17 - inputleng) / 2; j++) {
            input = input + " ";
        }
        //Cut all down to 16 chars in case division by 2 is rounded
        if (input.length() == 17) {
            input = input.substring(1);
        } else if (input.length() == 15) {
            input = input + " ";
        }
        return input;
    }
}
