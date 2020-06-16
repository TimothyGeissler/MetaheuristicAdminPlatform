package com.root;

import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {

    private Functions functions = new Functions();

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
