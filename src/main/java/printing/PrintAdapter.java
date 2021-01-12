package printing;

import student.Student;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

public class PrintAdapter implements Printable {
    private Student student;
    private ArrayList<Student> students;

    public PrintAdapter(Student student){
        this.student = student;
    }

    public PrintAdapter(ArrayList<Student> students){
        this.students = students;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}
