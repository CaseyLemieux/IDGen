package database;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import excel.ExcelReader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import pdf.PDFAdapter;
import student.Student;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseHelper {

    private final String dbPath = "jdbc:derby:studentDB;create=true";
    private Connection connection;
    private Statement statement;
    private ExcelReader excelReader;
    private PDFAdapter pdfAdapter;
    private ArrayList<Student> students;
    private int numberOfStudets;
    public DatabaseHelper() {

        try {
            connection = DriverManager.getConnection(dbPath);
            ResultSet resultSet = connection.getMetaData().getTables(null, "APP", "STUDENTS", null);
            if (!resultSet.next()) {
                createStudentTable();
            } else {
                //Do nothing we don't need to create the table
                System.out.println("Already Exists");
            }
            this.students = new ArrayList<>();
            String query = "SELECT * FROM STUDENTS";
            statement = connection.createStatement();
            ResultSet returnSet = statement.executeQuery(query);
            while(returnSet.next()){
                Student student = new Student();
                student.setStudentID(returnSet.getString(1));
                student.setFirstName(returnSet.getString(2));
                student.setLastName(returnSet.getString(3));
                student.setEmail(returnSet.getString(4));
                student.setDisplayName(returnSet.getString(5));
                student.setQrCode(returnSet.getString(6));
                student.setIdPic(returnSet.getBytes(7));
                students.add(student);
                numberOfStudets++;
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
            String query = "CREATE TABLE STUDENTS( StudentID VARCHAR(10) NOT NULL, FirstName VARCHAR(25) NOT NULL, LastName VARCHAR(25) NOT NULL," +
                    " Email VARCHAR(50) NOT NULL, DisplayName VARCHAR(50), QrCode VARCHAR(200), IdPic blob, PRIMARY KEY (StudentID))";
            statement.execute(query);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void addStudent(Student student) {
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO STUDENTS(StudentID, FirstName, LastName, Email) " +
                    "VALUES('" + student.getStudentID() + "', '" + student.getFirstName().replaceAll("'", "''") + "', '" + student.getLastName().replaceAll("'", "''") + "', '" + student.getEmail() +"')";
            statement.execute(query);
            statement.close();
            System.out.println("Student Added");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    private void updateQRCode(Student student){
        try {
            String query = "UPDATE STUDENTS SET QrCode = ? " + "WHERE StudentID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, student.getQrCode());
            statement.setString(2, student.getStudentID());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateIDPic(Student student){
        try{
            String query = "UPDATE STUDENTS SET IdPic = ? " + "WHERE StudentID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBytes(1, student.getIdPic());
            statement.setString(2, student.getStudentID());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadStudents(File studentsFile) {
        try (CSVReader reader = new CSVReader(new FileReader(studentsFile))) {
            reader.skip(1);
            List<String[]> students = reader.readAll();
            for(String[] row : students){
                Student student = new Student();
                student.setFirstName(row[0]);
                student.setLastName(row[1]);
                student.setStudentID(row[3]);
                student.setEmail(row[4]);
                addStudent(student);
                //System.out.println(student.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfStudets(){
        return numberOfStudets;
    }

    public void loadIDs(File focusFile){
        try(PDDocument doc = PDDocument.load(focusFile)){
            PDFTextStripper stripper = new PDFTextStripper();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Splitter splitter = new Splitter();
            List<PDDocument> pages = splitter.split(doc);
            int num = 1;
            for(PDDocument page : pages){
                page.save(outputStream);
                byte[] bytes = outputStream.toByteArray();
                System.out.println(Arrays.toString(bytes));
                String text = stripper.getText(page);
                String[] splitText = text.split("\\W+");
                String id = splitText[splitText.length - 1];

                for(Student student : students){
                    //System.out.println(id);
                    if(id.equals(student.getStudentID())){
                        student.setIdPic(bytes);
                        updateIDPic(student);
                        //System.out.println("Added ID " + num);
                        num++;
                    } else {
                        //System.out.println("Error in ID Parse");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadQRCodes(File classLink) {
        ArrayList<Student> studentsQRData;
        excelReader.openFile(classLink);
        studentsQRData = excelReader.getStudentsQRData();
        for(Student qrStudent : studentsQRData){
            for(Student dbStudent : students){
                if(qrStudent.getEmail().equals(dbStudent.getEmail())){
                    dbStudent.setQrCode(qrStudent.getQrCode());
                    updateQRCode(dbStudent);
                }
            }
        }

    }
}
