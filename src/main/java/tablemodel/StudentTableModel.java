package tablemodel;

import student.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StudentTableModel extends AbstractTableModel {

    private String[] columnNames = {"Student ID", "Username", "Email", "Display Name", "Grade", "QR Code"};
    private ArrayList<Student> students;

    public StudentTableModel(ArrayList<Student> students){
        this.students = students;
    }

    public int getRowCount() {
        return students.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object object = null;
        if(columnIndex == 0){
            //Student ID
            object = students.get(rowIndex).getStudentID();
        }
        else if(columnIndex == 1){
            //Username
            object = students.get(rowIndex).getUserName();
        }
        else if(columnIndex == 2){
            //Email
            object = students.get(rowIndex).getEmail();
        }
        else if(columnIndex == 3){
            //Display Name
            object = students.get(rowIndex).getDisplayName();
        }
        else if(columnIndex == 4){
            //Grade
            object = students.get(rowIndex).getGrade();
        }
        else if(columnIndex == 5){
            //QR Code
            object = students.get(rowIndex).getQrCode();
        }
        return object;
    }

    public String getColumnName(int col){
        return columnNames[col];
    }
}
