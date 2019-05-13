package com.root;

import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {

    private Functions functions = new Functions();
    public DatabaseSettings db = new DatabaseSettings();

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Get dark mode settings from txt file
        if (functions.fileExists("darkmode.txt")) {
            functions.scanFile("darkmode.txt");
            System.out.println("Dark Mode: " + functions.getInBuffer(0));
            try {
                boolean darkmode = Boolean.parseBoolean(functions.getInBuffer(0));
                functions.setIsDark(darkmode);
                System.out.println("Configuration successfully scanned...");
            } catch (Exception ex) {
                System.out.println("Error reading darkmode.txt, setting to default setting...");
                functions.addOutBuffer("false");
                functions.printFile("darkmode.txt");
            }
        }
        //Load login window
        functions.loadStage("/loginFrame/login.fxml", "Login");
        //Scan config file for database name at startup
        if (functions.fileExists("config.txt")) {
            functions.scanFile("config.txt");
            db.setDatabase(functions.getInBuffer(0));
            db.setStudentTable(functions.getInBuffer(1));
            db.setTeacherTable(functions.getInBuffer(2));
            db.setStudentTeacherTable(functions.getInBuffer(3));
            db.setAcademicTable(functions.getInBuffer(4));
            db.setPersonalisedTable(functions.getInBuffer(5));
            db.setTimetableUrls(functions.getInBuffer(6));
            System.out.println("Database Configuration: \n" + db.toString());
            functions.clearInBuffer();
        } else {
            //No config file, prompt user to set database name
            Functions.setAlertMessage("Please set a database in settings");
            functions.loadStage("/alertFrame/alert.fxml", "No database set!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
