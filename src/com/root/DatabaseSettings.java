package com.root;

public class DatabaseSettings {
    private static String database, studentTable, teacherTable, studentTeacherTable, academicTable, personalisedTable, timetable_urls;

    @Override
    public String toString() {
        return "DatabaseSettings{" +
                "database='" + database + '\'' +
                ", studentTable='" + studentTable + '\'' +
                ", teacherTable='" + teacherTable + '\'' +
                ", studentTeacherTable='" + studentTeacherTable + '\'' +
                ", academicTable='" + academicTable + '\'' +
                ", personalisedTable='" + personalisedTable + '\'' +
                ", timetable_urls='" + timetable_urls + '\'' +
                '}';
    }

    public String getTimetableUrls() {
        return timetable_urls;
    }

    //Reference to class rather than class instance (this.timetable_urls)
    public void setTimetableUrls(String timetable_urls) {
        DatabaseSettings.timetable_urls = timetable_urls;
    }

    public void setDatabase(String database) {
        DatabaseSettings.database = database;
    }

    public void setStudentTable(String studentTable) {
        DatabaseSettings.studentTable = studentTable;
    }

    public void setTeacherTable(String teacherTable) {
        DatabaseSettings.teacherTable = teacherTable;
    }

    public void setStudentTeacherTable(String studentTeacherTable) {
        DatabaseSettings.studentTeacherTable = studentTeacherTable;
    }

    public void setAcademicTable(String academicTable) {
        DatabaseSettings.academicTable = academicTable;
    }

    public void setPersonalisedTable(String personalisedTable) {
        DatabaseSettings.personalisedTable = personalisedTable;
    }

    public String getDatabase() {
        return database;
    }

    public String getStudentTable() {
        return studentTable;
    }

    public String getTeacherTable() {
        return teacherTable;
    }

    public String getStudentTeacherTable() {
        return studentTeacherTable;
    }

    public String getAcademicTable() {
        return academicTable;
    }

    public String getPersonalisedTable() {
        return personalisedTable;
    }
}
