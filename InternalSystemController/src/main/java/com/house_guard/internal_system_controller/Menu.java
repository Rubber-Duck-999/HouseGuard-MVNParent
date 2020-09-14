package com.house_guard.internal_system_controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
 
public class Menu {
     
    JFrame frame = new JFrame();
    Container container = new Container();
    JButton logsButton = new JButton("LOGS");
    JButton usersButton = new JButton("USERS");
    JButton devicesButton = new JButton("DEVICES");
    JButton settingsButton = new JButton("SETTINGS");   
    JButton logoutButton = new JButton("LOGOUT"); 
   
    public Menu() {
        container = frame.getContentPane();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        frame.setTitle("House Guard Menu");
        frame.setVisible(false);
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
 
    public void setLayoutManager() {
        container.setLayout(null);
    }
 
    public void setLocationAndSize() {
        logsButton.setBounds(100, 50, 300, 90);
        usersButton.setBounds(100, 150, 300, 90);
        devicesButton.setBounds(100, 250, 300, 90);
        settingsButton.setBounds(100, 350, 300, 90);
        logoutButton.setBounds(100, 450, 300, 90);
    }
 
    public void addComponentsToContainer() {
        container.add(logsButton);
        container.add(usersButton);
        container.add(devicesButton);
        container.add(settingsButton);     
        container.add(logoutButton);  
    }
 
    public void addController(ActionListener listenerButtons) {
        logsButton.setActionCommand("LOGS");
        logsButton.addActionListener(listenerButtons);
        usersButton.setActionCommand("USERS");
        usersButton.addActionListener(listenerButtons);
        devicesButton.setActionCommand("DEVICES");
        devicesButton.addActionListener(listenerButtons);
        settingsButton.setActionCommand("SETTINGS");
        settingsButton.addActionListener(listenerButtons);
        logoutButton.setActionCommand("LOGOUT");
        logoutButton.addActionListener(listenerButtons);      
    }

    public void changeView(boolean state) {
        frame.setVisible(state);
    }

    public void close() {
        frame.dispose();
    }

    public static class CloseListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }
    }
}
