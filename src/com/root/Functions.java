package com.root;

import de.jensd.fx.glyphs.GlyphIconName;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Functions {

    //Alert messages are routed through this class
    private static String alertMessage, username;
    private static boolean isInfo;
    public static void setAlertMessage(String alertMessageInput) {
        alertMessage = alertMessageInput;
    }
    //Modify alert dialog to display info rather than alert
    public static void setIsInfoMessage(boolean info) { isInfo = info; }
    public static boolean getIsInfoMessage() { return isInfo; }
    public static String getAlertMessage() {
        return alertMessage;
    }
    private static boolean isDark;
    public void setIsDark(boolean input) {
        isDark = input;
    }
    public boolean getIsDark() {
        return isDark;
    }
    private static DatabaseSettings db = new DatabaseSettings();
    private static ArrayList<String> inBuffer = new ArrayList<>();
    private static ArrayList<String> outBuffer = new ArrayList<>();
    private static ArrayList<String> timetables = new ArrayList<>();
    //Make database name a variable in case it is changed

    public void loadStage(String path, String title) throws Exception {
        Parent loader = FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        stage.setTitle(title);
        //stage.getIcons().add(new Image("icon2.png"));
        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.setResizable(false);
        //Set global CSS file
        String cssURL;
        if (isDark) {
            cssURL = "/stylesheets/darkmode.css";
            System.out.println("Loading " + path + " in dark mode...");
        } else {
            cssURL = "/stylesheets/lightmode.css";
            System.out.println("Loading " + path + " in light mode...");
        }
        scene.getStylesheets().add(cssURL);
        stage.show();
    }

    public void animateSceneTransition(String newSceneURL, Scene currentScene, String direction) throws IOException {
        //Load scene 2
        Parent loader = FXMLLoader.load(getClass().getResource(newSceneURL));

        //Load second scene just outside of window
        if (direction.equals("up")) {
            loader.translateYProperty().set(currentScene.getHeight());
        } else if (direction.equals("down")) {
            loader.translateYProperty().set(currentScene.getHeight() * -1);
        }
        System.out.println("Animating " + newSceneURL + " in direction: " + direction);
        //Get parentContainer StackPane
        StackPane parentContainer = (StackPane) currentScene.getRoot();
        System.out.println("Parent StackPane: " + parentContainer.getId());
        //Get current AnchorPane
        System.out.println("Current Scene: " + currentScene.getRoot().getId());
        AnchorPane currentAnchorPane = new AnchorPane();
        System.out.println("Nodes in current Scene: ");
        ObservableList<Node> children = ((StackPane) currentScene.getRoot()).getChildren();
        for (Node child: children) {
            System.out.println("\t-> " + child.getId());
            if (child instanceof AnchorPane) {
                currentAnchorPane = (AnchorPane) child;
            }
        }
        System.out.println("Current AnchorPane: " + currentAnchorPane.getId());

        //add new scene to parent container
        parentContainer.getChildren().add(loader);
        System.out.println("Added loader to parentContainer...");
        //Animation
        Timeline timeline = new Timeline();
        //EASE_IN = speed up, EASE_OUT = slow down
        KeyValue kv = new KeyValue(loader.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        //Remove current scene
        AnchorPane finalCurrentAnchorPane = currentAnchorPane;
        timeline.setOnFinished(event1 -> parentContainer.getChildren().remove(finalCurrentAnchorPane));
        timeline.play();
        System.out.println("Animated transition finished!");
    }

    public void changeScene(String path, String title, Stage stage) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource(path));
        Scene newScene = new Scene(loader);
        newScene.getStylesheets().add("/stylesheets/darkmode.css");
        stage.setTitle(title);
        stage.setScene(newScene);
    }

    /*public static ArrayList<ArrayList<String>> selectQuery(String query, String[] column_names) {
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>(column_names.length);
        for (int a = 0; a < column_names.length; a++) {
            //Create arraylists within the arraylist
            arrayList.add(new ArrayList<>());
        }
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            System.out.println("LOCATION: jdbc:mysql://localhost/" + db.getDatabase() + "\nQUERY: " + query);
            con = DriverManager.getConnection("jdbc:mysql://localhost/" + db.getDatabase(), "root", "mySQL2019");
            st = con.createStatement();
            System.out.print("\nSelect Query Results:" + "\n\t-->");
            rs = st.executeQuery(query);
            //Iterate through rows of rs object and save to 2D array
            while (rs.next()) {
                for (int i = 0; i < column_names.length; i++) {
                    String cell = rs.getString(column_names[i]);
                    //For debugging
                    if (cell.equals("")) {
                        cell = "null";
                    }

                    System.out.print("\t" + cell);
                    arrayList.get(i).add(cell);
                }
            }
            System.out.print("\n\n");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return arrayList;
    }*/

    //.get(column).get(row)
    public static ArrayList<ArrayList<String>> select(String query, String [] column_names) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>(column_names.length);
        for (int a = 0; a < column_names.length; a++) {
            //Create arraylists within the arraylist
            arrayList.add(new ArrayList<>());
        }
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + db.getDatabase());
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            //"SELECT * FROM users_table;"
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                for (int i = 0; i < column_names.length; i++) {
                    String cell = rs.getString(column_names[i]);
                    //For debugging
                    if (cell.equals("")) {
                        cell = "null";
                    }
                    System.out.print("\t" + cell);
                    arrayList.get(i).add(cell);
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.print("\n");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return arrayList;
    }

    public static void query(String query) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + db.getDatabase());
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            //String sql = "INSERT INTO users_table (Username, Password) VALUES ('timdeegee', 'password');";
            stmt.executeUpdate(query);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static ArrayList<ArrayList<String>> readSpreadsheet(String path) throws IOException, InvalidFormatException {
        System.out.println("Scanning spreadsheet: " + path);
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (int a = 0; a < 7; a++) {
            //Create arraylists within the arraylist
            result.add(new ArrayList<>());
        }
        //Create a workbook
        Workbook workbook = WorkbookFactory.create(new File(path));
        //Get no. of sheets
        System.out.println("Sheets: " + workbook.getNumberOfSheets());
        //Lamba expression
        System.out.println("Using Lambda expression");
        workbook.forEach(sheet -> {
            System.out.println(" --> " + sheet.getSheetName());
        });

        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        //Use array as final in order to work with lamba expression
        final int[] rows = {0};
        DataFormatter dataFormatter = new DataFormatter();
        System.out.println("\n\nIterating with lambda expression\n");
        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
                result.get(rows[0]).add(cellValue);
            });
            System.out.println();
            ++rows[0];
        });

        // Closing the workbook
        workbook.close();
        return result;
    }

    /*public static void insdelQuery(String query) {
        Connection con;
        Statement st;
        try {
            System.out.println("LOCATION: @jdbc:mysql://localhost/" + db.getDatabase() + "\nQUERY: " + query);
            con = DriverManager.getConnection("jdbc:mysql://localhost/" + db.getDatabase(), "root", "Password");
            st = con.createStatement();
            //INSERT/DELETE query
            System.out.println("INSDEL query...");
            st.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Query Executed");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }*/

    public ArrayList<String> flattenArrayList(ArrayList<ArrayList<String>> input) {
        System.out.println("\nFlattening ArrayList...");
        ArrayList<String> result = new ArrayList<>();
        //.get(column).get(row)
        //System.out.println("Flattening ArrayList...\nDimensions: [" + input.get(0).size());
        for (int a = 0; a < 6; a++) {
            for (int b = 0; b < 5; b++) {
                result.add(input.get(a).get(b));
                System.out.print(input.get(a).get(b) + ", ");
            }
        }
        System.out.println();
        return result;
    }

    public String concatArrayList(ArrayList<String> input) {
        String concat = "";
        for (int i = 0; i < input.size(); i++) {
            concat = concat + "\"" + input.get(i) + "\", ";
        }
        //cut last 3 chars
        concat = concat.substring(0, concat.length() - 3);
        System.out.println("Concatenated: " + concat);
        return concat;
    }

    public void createTxtFile(String filepath) {
        try (PrintWriter outFile = new PrintWriter(new FileWriter(filepath))) {
            outFile.print("");
            System.out.println(filepath + " created successfully");
        } catch (IOException ex) {
            System.out.println("Error creating " + filepath);
        }
    }
    public void copyFile(String filename, String newFilename) {
        createTxtFile(newFilename);
        scanFile(filename);
        for (int i = 0; i < getInBufferLength(); i++) {
            addOutBuffer(getInBuffer(i));
        }
        printFile(newFilename);
        System.out.println(filename + " copied to " + newFilename);
    }

    public void mkDir(String dirName) {
        File file = new File(dirName);
        file.mkdir();
        System.out.println("Directory created");
    }

    public void delDir(String dirName) {
        File file = new File(dirName);
        file.delete();
    }

    public String rootFilepath() {
        File file = new File("");
        String temp = "";
        try {
            temp = file.getCanonicalPath();
        } catch (IOException ex) {
            ex.getStackTrace();
        }
        System.out.println("Root filepath: " + temp);
        return temp;
    }

    public void deleteFile(String filename) {
        File file = new File(filename);
        file.delete();
        System.out.println(filename + " deleted");
    }

    public boolean fileExists(String filename) {
        boolean exists;
        try {
            Scanner scan = new Scanner(new File(filename));
            exists = true;
            scan.close();
        } catch (FileNotFoundException ex) {
            exists = false;
        }
        System.out.println(filename + " exists: " + exists + "...");
        return exists;
    }

    public void scanFile(String filename) {
        try {
            Scanner scan = new Scanner(new File(filename));
            clearInBuffer();
            int i = 0;
            while (scan.hasNext()) {
                String line = scan.nextLine();
                addInBuffer(line);
                i++;
            }
            System.out.println(filename + " scanned successfully, " + i + " iterations");
            scan.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Faliure scanning " + filename);
        }
    }

    public void printFile(String filename) {
        try (PrintWriter outFile = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < getOutBufferLength(); i++) {
                outFile.println(getOutBuffer(i));
            }
            clearOutBuffer();
            System.out.println("outBuffer succesfully written & cleared");
        } catch (IOException ex) {
            System.out.println("Faliure saving to file " + filename);
        }
    }

    public String getInBuffer(int index) {
        return inBuffer.get(index);
    }

    private void addInBuffer(String input) {
        inBuffer.add(input);
    }

    public int getOutBufferLength() {
        return outBuffer.size();
    }

    public int getInBufferLength() {
        return inBuffer.size();
    }

    private void clearOutBuffer() {
        outBuffer.clear();
    }

    void clearInBuffer() {
        inBuffer.clear();
    }

    public static String getTimetable(int index) {
        return timetables.get(index);
    }

    public static void addTimetable(String timetable) {
        timetables.add(timetable);
    }

    public int getTimetablesLength() {
        return timetables.size();
    }

    private String getOutBuffer(int index) {
        return outBuffer.get(index);
    }

    public void addOutBuffer(String input) {
        outBuffer.add(input);
    }

    public String getUsername() { return username; }

    public void setUsername(String inputUsername) { username = inputUsername; }
}
