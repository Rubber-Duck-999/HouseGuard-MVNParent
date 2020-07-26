package com.house_guard.internal_system_controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View {
    JFrame frame = new JFrame();
    Container container = new Container();
    JLabel ipLabel = new JLabel("IP ADDRESS");
    JLabel portLabel = new JLabel("PORT");
    JLabel passwordLabel = new JLabel("GUID");
    JTextField ipTextField = new JTextField();
    JTextField portTextField = new JTextField();
    JPasswordField guidField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show guid");
   
    View() {
        container = frame.getContentPane();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        frame.setTitle("House Guard Internal");
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
    }
 
    public void setLayoutManager() {
        container.setLayout(null);
    }
 
    public void setLocationAndSize() {
        ipLabel.setBounds(50, 50, 100, 30);
        portLabel.setBounds(50, 120, 100, 30);
        passwordLabel.setBounds(50, 190, 100, 30);
        ipTextField.setBounds(150, 50, 150, 30);
        portTextField.setBounds(150, 120, 150, 30);
        guidField.setBounds(150, 190, 150, 30);
        showPassword.setBounds(150, 230, 150, 25);
        loginButton.setBounds(50, 300, 100, 50);
        resetButton.setBounds(200, 300, 100, 50);
    }
 
    public void addComponentsToContainer() {
        container.add(ipLabel);
        container.add(portLabel);
        container.add(passwordLabel);
        container.add(ipTextField);
        container.add(portTextField);
        container.add(guidField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }
 
    public void addController(ActionListener listenerButtons) {
        loginButton.setActionCommand("LOGIN");
        loginButton.addActionListener(listenerButtons);
        showPassword.setActionCommand("SHOW");
        showPassword.addActionListener(listenerButtons);
        resetButton.setActionCommand("RESET");
        resetButton.addActionListener(listenerButtons);
    }

    public String getIpText() {
        return ipTextField.getText();
    }

    public String getPortText() {
        return portTextField.getText();
    }

    public void setUserText(String text) {
        ipTextField.setText("");
    }

    public void setPasswd(String text) {
        guidField.setText("");
    }

    public String getPasswd() {
        return String.valueOf(guidField.getPassword());
    }

    public void showPasswd() {
        if (showPassword.isSelected()) {
            guidField.setEchoChar((char) 0);
        } else {
            guidField.setEchoChar('*');
        }
    }

    public void changeLogin(boolean state) {
        frame.setVisible(state);
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayPassMessage(String message) {
        JOptionPane.showMessageDialog(frame, "Pass: " + message, "Pass", JOptionPane.INFORMATION_MESSAGE);
    }

    public void close() {
        frame.dispose();
    }

    public static class CloseListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            e.getWindow().setVisible(false);
            System.exit(0);
        }
    }
}