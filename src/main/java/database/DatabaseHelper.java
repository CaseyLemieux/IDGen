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
import java.util.List;

//TODO: Rework the Database creation to allow the user to specify where they want to save the derby database on startup.


public class DatabaseHelper {

    private final String dbPath = "jdbc:derby:studentDB;create=true";
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private Connection connection;
    private Statement statement;
    private ExcelReader excelReader;
    private PDFAdapter pdfAdapter;
    private ArrayList<Student> students;
    private int numberOfStudents;
    public DatabaseHelper() {

        try {
            Class.forName(driver);
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
                student.setGradeLevel(returnSet.getString(7));
                student.setIdPic(returnSet.getBytes(8));
                students.add(student);
                numberOfStudents++;
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        excelReader = new ExcelReader();
        pdfAdapter = new PDFAdapter();
    }

    private void createStudentTable() {
        try {
            statement = connection.createStatement();
            String query = "CREATE TABLE STUDENTS( StudentID VARCHAR(10) NOT NULL, FirstName VARCHAR(25) NOT NULL, LastName VARCHAR(25) NOT NULL," +
                    " Email VARCHAR(50) NOT NULL, DisplayName VARCHAR(50), QrCode VARCHAR(200), GradeLevel VARCHAR(4), IdPic blob, PRIMARY KEY (StudentID))";
            statement.execute(query);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void addStudent(Student student) {
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO STUDENTS(StudentID, FirstName, LastName, Email, GradeLevel) " +
                    "VALUES('" + student.getStudentID() + "', '" + student.getFirstName().replaceAll("'", "''") + "', '" +
                    student.getLastName().replaceAll("'", "''") + "', '" + student.getEmail() +"', '" + student.getGradeLevel() +"')";
            statement.execute(query);
            statement.close();
            students.add(student);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    public int getNumberOfStudents(){
        return numberOfStudents;
    }

    public ArrayList<Student> getGradeLevel(String gradeLevel){
        ArrayList<Student> level = new ArrayList<>();
        for(Student student : students){
            if(student.getGradeLevel().equalsIgnoreCase(gradeLevel)){
                level.add(student);
            }
        }
        return level;
    }

    public void loadStudents(File studentsFile) {
        try (CSVReader reader = new CSVReader(new FileReader(studentsFile))) {
            reader.skip(1);
            List<String[]> students = reader.readAll();
            for(String[] row : students){
                Student student = new Student();
                student.setFirstName(row[0]);
                student.setLastName(row[1]);
                student.setStudentID(row[2]);
                student.setEmail(row[3]);
                student.setGradeLevel(row[4]);
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

    public void loadIDs(File focusFile){
        try(PDDocument doc = PDDocument.load(focusFile)){
            PDFTextStripper stripper = new PDFTextStripper();
            Splitter splitter = new Splitter();
            List<PDDocument> pages = splitter.split(doc);
            for(PDDocument page : pages){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                page.save(outputStream);
                byte[] bytes = outputStream.toByteArray();
                //System.out.println(Arrays.toString(bytes));
                String text = stripper.getText(page);
                String[] splitText = text.split("\\W+");
                String id = splitText[splitText.length - 1];
                System.out.println(id);
                for(Student student : students){
                    //System.out.println(id);
                    if(id.equalsIgnoreCase(student.getStudentID())){
                        student.setIdPic(bytes);
                        updateIDPic(student);
                        System.out.println(id + " " + student.getStudentID());
                    } else {
                        //System.out.println("Error in ID Parse");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void updateDisplayName(Student student){
        try{
            String query = "UPDATE STUDENTS SET DisplayName = ?" + "WHERE StudentID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, student.getDisplayName());
            statement.setString(2, student.getStudentID());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadQRCodes(File classLink) {
        ArrayList<Student> studentsQRData;
        excelReader.openFile(classLink);
        studentsQRData = excelReader.getStudentsQRData();
        for(Student qrStudent : studentsQRData){
            for(Student dbStudent : students){
                if(qrStudent.getEmail().equalsIgnoreCase(dbStudent.getEmail())){
                    dbStudent.setQrCode(qrStudent.getQrCode());
                    dbStudent.setDisplayName(qrStudent.getDisplayName());
                    updateQRCode(dbStudent);
                    updateDisplayName(dbStudent);
                }
            }
        }

    }
}
