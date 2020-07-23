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
   
    public Menu() {
        container = frame.getContentPane();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        frame.setTitle("House Guard Menu");
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
 
    public void setLayoutManager() {
        container.setLayout(null);
    }
 
    public void setLocationAndSize() {
        logsButton.setBounds(200, 50, 150, 100);
        usersButton.setBounds(200, 150, 150, 100);
        devicesButton.setBounds(200, 250, 150, 100);
        settingsButton.setBounds(200, 350, 150, 100);
    }
 
    public void addComponentsToContainer() {
        container.add(logsButton);
        container.add(usersButton);
        container.add(devicesButton);
        container.add(settingsButton);       
    }
 
    public void addController(ActionListener listenerButtons)
    {
        logsButton.setActionCommand("LOGS");
        logsButton.addActionListener(listenerButtons);
        usersButton.setActionCommand("USERS");
        usersButton.addActionListener(listenerButtons);
        devicesButton.setActionCommand("DEVICES");
        devicesButton.addActionListener(listenerButtons);
        settingsButton.setActionCommand("SETTINGS");
        settingsButton.addActionListener(listenerButtons);
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
            e.getWindow().dispose();
        }
    }
}
