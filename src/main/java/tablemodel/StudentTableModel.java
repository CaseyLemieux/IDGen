package tablemodel;

import student.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StudentTableModel extends AbstractTableModel {

    private String[] columnNames = {"Student ID", "First Name", "Last Name", "Email", "Display Name", "QR Code", "ID Bytes"};
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

    public void tableUpdated(){
        this.fireTableStructureChanged();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object object = null;
        if(columnIndex == 0){
            //Student ID
            object = students.get(rowIndex).getStudentID();
        }
        else if(columnIndex == 1){
            //First Name
            object = students.get(rowIndex).getFirstName();
        }
        else if(columnIndex == 2){
            //Last Name
            object = students.get(rowIndex).getLastName();
        }
        else if(columnIndex == 3){
            //Email
            object = students.get(rowIndex).getEmail();
        }
        else if(columnIndex == 4){
            //Display Name
            object = students.get(rowIndex).getDisplayName();
        }
        else if(columnIndex == 5){
            //QR Code
            object = students.get(rowIndex).getQrCode();
        }
        else if(columnIndex == 6){
            //ID Bytes
            object = students.get(rowIndex).getIdPic();
        }
        return object;
    }

    public String getColumnName(int col){
        return columnNames[col];
    }
}
