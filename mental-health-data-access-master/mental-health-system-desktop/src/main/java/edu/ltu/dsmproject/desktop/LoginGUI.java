package edu.ltu.dsmproject.desktop;

import edu.ltu.dsmproject.dataaccess.dao.UserDatabaseAccessObject;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginGUI{
    private static JFrame frame = new JFrame("User Credentials");
    private static final int MAIN_FRAME_WIDTH = 250;
    private static final int MAIN_FRAME_HEIGHT = 380;
    public JPanel panelMain;
    private JTextField userNameTextField;
    private JTextField passwordTextField;
    private JTextArea invalidLabel;
    private String permissionLevel;

    public LoginGUI() {
        JButton loginButton = new JButton("Login");
        JButton guestLoginButton = new JButton("Guest");
        userNameTextField = new JTextField(7);
        userNameTextField.setText("Username");
        userNameTextField.setForeground(Color.GRAY);

        passwordTextField = new JPasswordField(7);
        passwordTextField.setText("Password");
        passwordTextField.setForeground(Color.GRAY);
        JLabel instructionsLabel = new JLabel();
        instructionsLabel.setText("Please enter your username and password");

        invalidLabel = new JTextArea();
        invalidLabel.setText("The username or password you have entered  is invalid. Please try again.");
        invalidLabel.setForeground(Color.red);
        invalidLabel.setVisible(false);


        panelMain = new JPanel(new GridLayout(7, 1));
        panelMain.add(invalidLabel);
        panelMain.add(instructionsLabel);
        panelMain.add(userNameTextField);
        panelMain.add(passwordTextField);
        panelMain.add(loginButton);
        panelMain.add(guestLoginButton);
        frame.add(panelMain, BorderLayout.CENTER);
        panelMain.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);

        DesktopGUI gui = new DesktopGUI();

        // highlights the whole text in the text field when clicked on
        userNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(() -> userNameTextField.selectAll());
            }
        });

        // highlights the whole text in the text field when clicked on
        passwordTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(() -> passwordTextField.selectAll());
            }
        });

        loginButton.addActionListener(e -> {
            try {
                if(!UserDatabaseAccessObject.getInstance().checkUserCredentials(userNameTextField.getText(), passwordTextField.getText())) {
                    failedLogin();
                }
                else {
                    permissionLevel = UserDatabaseAccessObject.getInstance().permissionLevelOfUser;
                    frame.setVisible(false);
                    frame.dispose();
                    gui.permissionLevel = permissionLevel;
                    gui.runMainFrame();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        guestLoginButton.addActionListener(e -> {
            permissionLevel = "Guest";
            frame.setVisible(false);
            frame.dispose();
            gui.permissionLevel = permissionLevel;
            gui.runMainFrame();
                }

        );

    }
    public void setupPanel() {
        frame.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    }
    public void failedLogin() {
        invalidLabel.setVisible(true);
        JOptionPane.showMessageDialog(null, "Invalid UserName or password");
    }
}

