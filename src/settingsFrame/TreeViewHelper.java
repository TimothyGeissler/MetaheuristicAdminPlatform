package settingsFrame;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;

public class TreeViewHelper {
    //package-private
    TreeViewHelper() {
    }

    public ArrayList<TreeItem<String>> getTables() {
        ArrayList<TreeItem<String>> tables = new ArrayList<TreeItem<String>>();
        TreeItem<String> studentTable = new TreeItem<>("students_table");
        studentTable.getChildren().addAll(getStudentFields());
        TreeItem<String> academicTable = new TreeItem<>("academic_timetables");
        academicTable.getChildren().addAll(getAcademicFields());
        TreeItem<String> calculatedTable = new TreeItem<>("calculated_timetables");
        calculatedTable.getChildren().addAll(getCalculatedFields());
        TreeItem<String> studTeachTable = new TreeItem<>("student_teacher");
        studTeachTable.getChildren().addAll(getStudTeachFields());
        TreeItem<String> classURLsTable = new TreeItem<>("timetable_urls");
        classURLsTable.getChildren().addAll(getURLsFields());
        TreeItem<String> usersTable = new TreeItem<>("users_table");
        usersTable.getChildren().addAll(getUsersFields());

        tables.add(academicTable);
        tables.add(studentTable);
        tables.add(calculatedTable);
        tables.add(studTeachTable);
        tables.add(classURLsTable);
        tables.add(usersTable);
        return tables;
    }

    //creates an ArrayList of TreeItems (StudentTable Fields)
    private ArrayList<TreeItem<String>> getStudentFields() {
        ArrayList<TreeItem<String>> studentTable = new ArrayList<TreeItem<String>>();

        //Insert key symbol for PK
        TreeItem<String> studID = new TreeItem<>("\uD83D\uDD11StudentID");
        TreeItem<String> studName = new TreeItem<>("StudentName");
        TreeItem<String> studSurname = new TreeItem<>("StudentSurname");
        TreeItem<String> studClass = new TreeItem<>("StudentClass");
        TreeItem<String> studGrade = new TreeItem<>("StudentGrade");

        studentTable.add(studID);
        studentTable.add(studName);
        studentTable.add(studSurname);
        studentTable.add(studClass);
        studentTable.add(studGrade);

        return studentTable;
    }

    //academic timetable fields
    private ArrayList<TreeItem<String>> getAcademicFields() {
        ArrayList<TreeItem<String>> academicTable = new ArrayList<TreeItem<String>>();

        TreeItem<String> classNo = new TreeItem<>("\uD83D\uDD11ClassNumber");
        TreeItem<String> lesson1 = new TreeItem<>("L1");
        //to skip L2-L29
        TreeItem<String> downArrow = new TreeItem<>("↓");
        TreeItem<String> lesson30 = new TreeItem<>("L30");

        academicTable.add(classNo);
        academicTable.add(lesson1);
        academicTable.add(downArrow);
        academicTable.add(lesson30);

        return academicTable;
    }

    //calculated timetable fields
    private ArrayList<TreeItem<String>> getCalculatedFields() {
        ArrayList<TreeItem<String>> calculatedTable = new ArrayList<TreeItem<String>>();

        TreeItem<String> teachID = new TreeItem<>("\uD83D\uDD11TeacherID");
        TreeItem<String> lesson1 = new TreeItem<>("L1");
        //to skip L2-L29
        TreeItem<String> downArrow = new TreeItem<>("↓");
        TreeItem<String> lesson30 = new TreeItem<>("L30");

        calculatedTable.add(teachID);
        calculatedTable.add(lesson1);
        calculatedTable.add(downArrow);
        calculatedTable.add(lesson30);

        return calculatedTable;
    }

    //student teacher fields
    private ArrayList<TreeItem<String>> getStudTeachFields() {
        ArrayList<TreeItem<String>> studTeachTable = new ArrayList<TreeItem<String>>();

        TreeItem<String> studID = new TreeItem<>("\uD83D\uDD11StudentID");
        TreeItem<String> teachID = new TreeItem<>("\uD83D\uDD11TeacherID");

        studTeachTable.add(studID);
        studTeachTable.add(teachID);

        return studTeachTable;
    }

    //timetableUrls
    private ArrayList<TreeItem<String>> getURLsFields() {
        ArrayList<TreeItem<String>> urlsTable = new ArrayList<TreeItem<String>>();

        TreeItem<String> classNo = new TreeItem<>("\uD83D\uDD11Class");
        TreeItem<String> url = new TreeItem<>("URL");

        urlsTable.add(classNo);
        urlsTable.add(url);

        return urlsTable;
    }

    //users table
    private ArrayList<TreeItem<String>> getUsersFields() {
        ArrayList<TreeItem<String>> usersTable = new ArrayList<>();

        TreeItem<String> username = new TreeItem<>("\uD83D\uDD11Username");
        TreeItem<String> pass = new TreeItem<>("\uD83D\uDD11Password");

        usersTable.add(username);
        usersTable.add(pass);

        return usersTable;
    }
}
