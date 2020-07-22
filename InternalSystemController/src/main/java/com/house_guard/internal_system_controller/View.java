package com.house_guard.internal_system_controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View
{
    JFrame frame = new JFrame();
    Container container = new Container();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("GUID");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show guid");
   
    View() {
        container = frame.getContentPane();
        setLayoutManager();
        setLocationAndSize();
        setButtonDesign();
        addComponentsToContainer();
        frame.setTitle("House Guard Internal");
        frame.setVisible(true);
        frame.setSize(500, 300);
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
    }
 
    public void setLayoutManager() {
        container.setLayout(null);
    }

    private void setButtonDesign() {
        loginButton.setBackground(Color.DARK_GRAY);
            // these next two lines do the magic..
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
    }
 
    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
    }
 
    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }
 
    public void addController(ActionListener listenerButtons)
    {
        loginButton.setActionCommand("LOGIN");
        loginButton.addActionListener(listenerButtons);
        showPassword.setActionCommand("SHOW");
        showPassword.addActionListener(listenerButtons);
        resetButton.setActionCommand("RESET");
        resetButton.addActionListener(listenerButtons);
    }

    public String getUserText() {
        return userTextField.getText();
    }

    public void setUserText(String text) {
        userTextField.setText("");
    }

    public void setPasswd(String text) {
        passwordField.setText("");
    }

    public String getPasswd() {
        return String.valueOf(passwordField.getPassword());
    }

    public void showPasswd() {
        if (showPassword.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }

    public void displayErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(frame, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayPassMessage(String message)
    {
        JOptionPane.showMessageDialog(frame, "Pass: " + message, "Pass", JOptionPane.INFORMATION_MESSAGE);
    }

    public void close()
    {
        frame.dispose();
    }

    public static class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            e.getWindow().setVisible(false);
            System.exit(0);
        }
    }
}
