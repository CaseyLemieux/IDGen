package driver;



import database.DatabaseHelper;
import student.Student;
import tablemodel.StudentTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Driver extends JFrame {

    private JButton btnClasslinkFile;
    private JButton btnFocusFile;
    private JButton btnStudentFile;
    private JTextField txtClasslink;
    private JTextField txtFocusFile;
    private JTextField txtStudentFile;
    private JPanel topPanel;
    private JPanel tablePanel;
    private JPanel bottomPanel;
    private StudentTableModel tableModel;
    private final JFileChooser fileChooser;
    private File classLinkFile;
    private File focusFile;
    private File studentsFile;
    private final DatabaseHelper databaseHelper;
    private ArrayList<Student> students;


    JFrame frame;
    Driver(){
        fileChooser = new JFileChooser();
        databaseHelper = new DatabaseHelper();
        students = databaseHelper.getStudents();
        initTopPanel();
        initTablePanel();
        initBottomPanel();
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setTitle("Student ID Generator");
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    private void initTopPanel(){
        //Create panel, labels, and text field.
        topPanel = new JPanel();
        JLabel lblClasslink = new JLabel("ClassLink QR Code File:");
        JLabel lblFocus = new JLabel("Focus ID PDF File");
        txtClasslink = new JTextField(30);
        txtFocusFile = new JTextField(30);

        //Create buttons and add their onClick Actions
        btnClasslinkFile = new JButton("Browse");
        btnClasslinkFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e);
                tableModel.tableUpdated();
            }
        });
        btnFocusFile = new JButton("Browse");
        btnFocusFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e);
                tableModel.tableUpdated();
            }
        });
        //Add all of the componets to the topPanel
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.add(lblClasslink);
        topPanel.add(txtClasslink);
        topPanel.add(btnClasslinkFile);
        topPanel.add(lblFocus);
        topPanel.add(txtFocusFile);
        topPanel.add(btnFocusFile);
    }

    private void initTablePanel(){
        tableModel = new StudentTableModel(students);
        JTable studentTable = new JTable(tableModel);
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.getTableHeader().setResizingAllowed(false);
        studentTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        tablePanel = new JPanel(new GridLayout());
        tablePanel.add(scrollPane);
    }

    private void initBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        JButton btnExportGrade = new JButton("Export Grade Level");
        JButton btnExportSelected = new JButton("Export Selected Student");
        JButton btnloadStudents = new JButton("Load Students");
        btnStudentFile = new JButton("Browse");
        btnStudentFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e);
            }
        });
        btnloadStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               databaseHelper.loadStudents(studentsFile);
               tableModel.tableUpdated();
            }
        });
        btnExportSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JLabel lblNumberOfStudents = new JLabel("Total Students: ");
        JLabel lblTotalStudents = new JLabel(databaseHelper.getNumberOfStudets() + " ");
        JLabel lblStudentFile = new JLabel("Student Info File: ");
        txtStudentFile = new JTextField(30);
        bottomPanel.add(lblNumberOfStudents);
        bottomPanel.add(lblTotalStudents);
        bottomPanel.add(lblStudentFile);
        bottomPanel.add(txtStudentFile);
        bottomPanel.add(btnStudentFile);
        //bottomPanel.add(Box.createGlue());
        //JPanel buttonPanel = new JPanel();
        bottomPanel.add(btnloadStudents);
        bottomPanel.add(btnExportSelected);
        bottomPanel.add(btnExportGrade);
        //bottomPanel.add(buttonPanel);

    }

    private void chooseFile(ActionEvent event){
        if(event.getSource() == btnClasslinkFile){
            int value = fileChooser.showOpenDialog(Driver.this);
            if(value == JFileChooser.APPROVE_OPTION){
                classLinkFile = fileChooser.getSelectedFile();
                txtClasslink.setText(classLinkFile.getAbsolutePath());
                databaseHelper.loadQRCodes(classLinkFile);
            }
        }
        else if(event.getSource() == btnFocusFile){
            int value = fileChooser.showOpenDialog(Driver.this);
            if(value == JFileChooser.APPROVE_OPTION){
                focusFile = fileChooser.getSelectedFile();
                txtFocusFile.setText(focusFile.getAbsolutePath());
                databaseHelper.loadIDs(focusFile);
            }
        }
        else if(event.getSource() == btnStudentFile){
            int value = fileChooser.showOpenDialog(Driver.this);
            if(value == JFileChooser.APPROVE_OPTION){
                studentsFile = fileChooser.getSelectedFile();
                txtStudentFile.setText(studentsFile.getAbsolutePath());
            }
        }
    }


    public static void main(String[] args){
        Driver driver = new Driver();
        driver.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent event){
                if(JOptionPane.showConfirmDialog(driver, "Are you sure you want to exit?", "Close Window?"
                        , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
        //new DatabaseHelper();
    }
}
