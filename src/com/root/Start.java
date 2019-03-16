package com.root;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Start extends Application {

    private Functions functions = new Functions();
    public DatabaseSettings db = new DatabaseSettings();

    @Override
    public void start(Stage primaryStage) throws Exception{
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
            System.out.println("Database Configuration: \n" + db.toString());
            functions.clearInBuffer();
        } else {
            //No config file, prompt user to set database name
            Functions.setAlertMessage("Please set a database in settings");
            functions.loadStage("/notificationFrame/notification.fxml", "No database set!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
