package driver;



import database.DatabaseHelper;

import javax.swing.*;

public class Driver extends JFrame {

    JButton btnClasslinkFile;
    JButton btnFocusFile;
    JTextField txtClasslink;
    JTextField txtFocusFile;
    JLabel lblClasslink;
    JLabel lblFocus;
    JPanel topPanel;
    JPanel tablePanel;
    JPanel bottomPanel;



    JFrame frame;
    Driver(){
        topPanel = new JPanel();
        txtClasslink = new JTextField(25);
        txtFocusFile = new JTextField(25);
        btnClasslinkFile = new JButton("Browse");
        btnFocusFile = new JButton("Browse");
        lblClasslink = new JLabel("ClassLink QR Code File:");
        lblFocus = new JLabel("Focus ID PDF File");
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.add(lblClasslink);
        topPanel.add(txtClasslink);
        topPanel.add(btnClasslinkFile);
        topPanel.add(lblFocus);
        topPanel.add(txtFocusFile);
        topPanel.add(btnFocusFile);
        add(topPanel);

        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setVisible(true);
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
