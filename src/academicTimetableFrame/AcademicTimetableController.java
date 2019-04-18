package academicTimetableFrame;

import com.root.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AcademicTimetableController implements Initializable {

    public Functions functions;

    @FXML
    private Accordion mainAccordion;
    @FXML
    private Tab tabA;
    @FXML
    private GridPane U1AGridPane;
    @FXML
    private Tab tabB;
    @FXML
    private GridPane U1BGridPane;
    @FXML
    private Tab tabC;
    @FXML
    private GridPane U1CGridPane;
    @FXML
    private Tab tabD;
    @FXML
    private GridPane L2DGridPane;
    @FXML
    private Tab tabE;
    @FXML
    private GridPane L2EGridPane;
    @FXML
    private Tab tabF;
    @FXML
    private GridPane L2FGridPane;
    @FXML
    private Tab tabG;
    @FXML
    private GridPane U2GGridPane;
    @FXML
    private Tab tabH;
    @FXML
    private GridPane U2HGridPane;
    @FXML
    private Tab tabI;
    @FXML
    private GridPane U2IGridPane;
    @FXML
    private Tab tabJ;
    @FXML
    private GridPane L3JGridPane;
    @FXML
    private Tab tabK;
    @FXML
    private GridPane L3KGridPane;
    @FXML
    private Tab tabL;
    @FXML
    private GridPane L3LGridPane;
    @FXML
    private Tab tabM;
    @FXML
    private GridPane U3MGridPane;
    @FXML
    private Tab tabN;
    @FXML
    private GridPane U3NGridPane;
    @FXML
    private Tab tabO;
    @FXML
    private GridPane U3OGridPane;

    //To map String of tab names to Tab FXML object
    private Map<String, Tab> map = new HashMap<String, Tab>();
    private Map<String, GridPane> gridPaneMap = new HashMap<String, GridPane>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Label> labelList = new ArrayList<>();
        int row_counter = 1, col_counter = 1;
        for (int i = 0; i < 450; i++) {
            labelList.add(new Label());
            labelList.get(i).setId("label" + i);
            labelList.get(i).setText("label" + i);
            //Iterate row_counter for each row
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
            if (i < 30) {
                U1AGridPane.setAlignment(Pos.CENTER);
                U1AGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 29 && i < 60) {
                U1BGridPane.setAlignment(Pos.CENTER);
                U1BGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 59 && i < 90) {
                U1CGridPane.setAlignment(Pos.CENTER);
                U1CGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 89 && i < 120) {
                L2DGridPane.setAlignment(Pos.CENTER);
                L2DGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 119 && i < 150) {
                L2EGridPane.setAlignment(Pos.CENTER);
                L2EGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 149 && i < 180) {
                L2FGridPane.setAlignment(Pos.CENTER);
                L2FGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 179 && i < 210) {
                U2GGridPane.setAlignment(Pos.CENTER);
                U2GGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 209 && i < 240) {
                U2HGridPane.setAlignment(Pos.CENTER);
                U2HGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 239 && i < 270) {
                U2IGridPane.setAlignment(Pos.CENTER);
                U2IGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 269 && i < 300) {
                L3JGridPane.setAlignment(Pos.CENTER);
                L3JGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 299 && i < 330) {
                L3KGridPane.setAlignment(Pos.CENTER);
                L3KGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 329 && i < 360) {
                L3LGridPane.setAlignment(Pos.CENTER);
                L3LGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 359 && i < 390) {
                U3MGridPane.setAlignment(Pos.CENTER);
                U3MGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 389 && i < 420) {
                U3NGridPane.setAlignment(Pos.CENTER);
                U3NGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            if (i > 419) {
                U3OGridPane.setAlignment(Pos.CENTER);
                U3OGridPane.add(labelList.get(i), col_counter, row_counter);
            }
            col_counter++;
        }


        //Map tabs to array using HashMap and alphabet iterator to generate names
        //System.out.println("Generating tab strings");
        String [] tabNames = new String[16];
        for (char alph = 'A'; alph <='O'; alph ++) {
            System.out.println("Iteration: " + ((int) alph - 65) + " --> " + "tab" + alph);
            tabNames[(int) alph - 65] = "tab" + alph;
        }
        /*
        ArrayList<Tab> tabArrayList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            map.put(tabNames[i], new Tab());
            tabArrayList.add(map.get(tabNames[i]));
            tabArrayList.get(i).setText("tab-" + i);
            System.out.println("Generated: " + tabArrayList.get(i).getText());
        }
        System.out.println("Academic Timetable init...");
        //Load tab names
        for (int i = 0; i < 15; i++) {
            map.get(tabNames[i]).setText("tab" + i);
        }
        if (functions.fileExists("class_config.txt")) {
            System.out.println("/class_config.txt found");
            functions.scanFile("class_config.txt");
            for (int i = 0; i < 16; i++) {
                //tabNames.add(functions.getInBuffer(i));
                //tabArrayList.add(new Tab());
                //tabArrayList.get(i).setText(functions.getInBuffer(i));
            }

        }*/
    }
}
