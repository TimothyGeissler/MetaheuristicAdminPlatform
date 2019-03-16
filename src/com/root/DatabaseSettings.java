package com.root;

public class DatabaseSettings {
    private static String database, studentTable, teacherTable, studentTeacherTable, academicTable, personalisedTable;

    @Override
    public String toString() {
        return "DatabaseSettings{" +
                "database='" + database + '\'' +
                ", studentTable='" + studentTable + '\'' +
                ", teacherTable='" + teacherTable + '\'' +
                ", studentTeacherTable='" + studentTeacherTable + '\'' +
                ", academicTable='" + academicTable + '\'' +
                ", personalisedTable='" + personalisedTable + '\'' +
                '}';
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setStudentTable(String studentTable) {
        this.studentTable = studentTable;
    }

    public void setTeacherTable(String teacherTable) {
        this.teacherTable = teacherTable;
    }

    public void setStudentTeacherTable(String studentTeacherTable) {
        this.studentTeacherTable = studentTeacherTable;
    }

    public void setAcademicTable(String academicTable) {
        this.academicTable = academicTable;
    }

    public void setPersonalisedTable(String personalisedTable) {
        this.personalisedTable = personalisedTable;
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
