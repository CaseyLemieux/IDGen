package tablemodel;

import student.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StudentTableModel extends AbstractTableModel {
    private String[] columnNames = {"Student ID", "Username", "Email", "Display Name", "QR Code"};
    private ArrayList<Student> students;

    public int getRowCount() {
        return 0;
    }

    public int getColumnCount() {
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
