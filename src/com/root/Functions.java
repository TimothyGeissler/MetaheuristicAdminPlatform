package com.root;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Functions {

    //Alert messages are routed through this class
    private static String alertMessage, username;
    public static void setAlertMessage(String alertMessageInput) {
        alertMessage = alertMessageInput;
    }
    public static String getAlertMessage() {
        return alertMessage;
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
        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.setResizable(false);
        //Set global CSS file
        scene.getStylesheets().add("/stylesheets/styles.css");
        stage.show();
    }

    public void changeScene(String path, String title, Stage stage) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource(path));
        Scene newScene = new Scene(loader);
        newScene.getStylesheets().add("/stylesheets/styles.css");
        stage.setTitle(title);
        stage.setScene(newScene);
    }
    //Scan query and return keyword to ID query
    public static String queryRouter(String query) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == ' ') {
                break;
            }
            temp.append(query.charAt(i));
        }
        return temp.toString().toUpperCase();
    }

    public static ArrayList<ArrayList<String>> selectQuery(String query, String[] column_names) {
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>(column_names.length);
        for (int a = 0; a < column_names.length; a++) {
            //Create arraylists within the arraylist
            arrayList.add(new ArrayList<>());
        }
        //.get(column).get(row)
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
    }

    public static void insdelQuery(String query) {
        Connection con;
        Statement st;
        try {
            System.out.println("LOCATION: @jdbc:mysql://localhost/" + db.getDatabase() + "\nQUERY: " + query);
            con = DriverManager.getConnection("jdbc:mysql://localhost/" + db.getDatabase(), "root", "mySQL2019");
            st = con.createStatement();
            //INSERT/DELETE query
            System.out.println("INSDEL query...");
            st.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Query Executed");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
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

    private int getOutBufferLength() {
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
