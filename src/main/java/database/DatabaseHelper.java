package database;

import excel.ExcelReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Row;
import pdf.PDFAdapter;
import student.Student;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


public class DatabaseHelper {

    private final String dbPath = "jdbc:derby:studentDB;create=true";
    private Connection connection;
    private Statement statement;
    private ExcelReader excelReader;
    private PDFAdapter pdfAdapter;

    public DatabaseHelper() {

        try {
            //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            //DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            connection = DriverManager.getConnection(dbPath);
            ResultSet resultSet = connection.getMetaData().getTables(null, "APP", "STUDENTS", null);
            if (!resultSet.next()) {
                createStudentTable();
            } else {
                //Do nothing we don't need to create the table
                System.out.println("Already Exists");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        excelReader = new ExcelReader();
        pdfAdapter = new PDFAdapter();
    }

    private void createStudentTable() {
        try {
            statement = connection.createStatement();
            String query = "CREATE TABLE STUDENTS( StudentID INT NOT NULL, Username VARCHAR(50) NOT NULL," +
                    " Email VARCHAR(50) NOT NULL, DisplayName VARCHAR(50) NOT NULL, QrCode VARCHAR(100), PRIMARY KEY (StudentID))";
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void addStudent(Student student) {

    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        return students;
    }

    public void loadStudents(File classLink, File focus) {
        ArrayList<Student> students = new ArrayList<>();
        excelReader.openFile(classLink);
        students = excelReader.getStudents();
        try(PDDocument doc = PDDocument.load(focus)){
            PDFTextStripper stripper = new PDFTextStripper();
            for(int i = 0; i < doc.getNumberOfPages(); ++i){
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String text = stripper.getText(doc);
                String[] splitText = text.split("\\W+");
                System.out.println(Arrays.toString(splitText));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
