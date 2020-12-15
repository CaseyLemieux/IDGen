package database;

import student.Student;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHelper {

    final String dbPath = "jdbc:derby:studentDB;create=true";
    Connection connection;
    Statement statement;

    public DatabaseHelper(){

        try {
            //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            //DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            connection = DriverManager.getConnection(dbPath);
            ResultSet resultSet = connection.getMetaData().getTables(null,"APP", "STUDENTS",null);
            if(!resultSet.next()){
               createStudentTable();
            } else{
                //Do nothing we don't need to create the table
                System.out.println("Already Exists");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createStudentTable(){
        try{
            statement = connection.createStatement();
            String query = "CREATE TABLE STUDENTS( StudentID INT NOT NULL, Username VARCHAR(50) NOT NULL," +
                    " Email VARCHAR(50) NOT NULL, DisplayName VARCHAR(50) NOT NULL, QrCode VARCHAR(100) NOT NULL, PRIMARY KEY (StudentID))";
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addStudent(Student student){

    }

    public ArrayList<Student> getStudents(){
        ArrayList<Student> students = new ArrayList<>();
        return students;
    }
}
