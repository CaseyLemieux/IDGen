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
    private JButton btnExportSelected;
    private JButton btnExportGrade;
    private JButton btnloadStudents;
    private JTextField txtClasslink;
    private JTextField txtFocusFile;
    private JLabel lblClasslink;
    private JLabel lblFocus;
    private JLabel lblTotalStudents;
    private JLabel lblNumberOfStudents;
    private JPanel topPanel;
    private JPanel tablePanel;
    private JPanel bottomPanel;
    private JTable studentTable;
    private JFileChooser fileChooser;
    private File classLinkFile;
    private File focusFile;
    private DatabaseHelper databaseHelper;
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
        lblClasslink = new JLabel("ClassLink QR Code File:");
        lblFocus = new JLabel("Focus ID PDF File");
        txtClasslink = new JTextField(30);
        txtFocusFile = new JTextField(30);

        //Create buttons and add their onClick Actions
        btnClasslinkFile = new JButton("Browse");
        btnClasslinkFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e);
            }
        });
        btnFocusFile = new JButton("Browse");
        btnFocusFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e);
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
        StudentTableModel tableModel = new StudentTableModel(students);
        studentTable = new JTable(tableModel);
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
        btnExportGrade = new JButton("Export Grade Level");
        btnExportSelected = new JButton("Export Selected Student");
        btnloadStudents = new JButton("Load Students");
        btnloadStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudents();
            }
        });
        lblNumberOfStudents = new JLabel("Total Students: ");
        lblTotalStudents = new JLabel("9 ");
        bottomPanel.add(lblNumberOfStudents);
        bottomPanel.add(lblTotalStudents);
        bottomPanel.add(Box.createGlue());
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
            }
        }
        else if(event.getSource() == btnFocusFile){
            int value = fileChooser.showOpenDialog(Driver.this);
            if(value == JFileChooser.APPROVE_OPTION){
                focusFile = fileChooser.getSelectedFile();
                txtFocusFile.setText(focusFile.getAbsolutePath());
            }
        }
    }

    private void exportSelected(){

    }

    private void exportGrade(){

    }

    private void loadStudents(){
        databaseHelper.loadStudents(classLinkFile, focusFile);
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
        new DatabaseHelper();
    }
}
