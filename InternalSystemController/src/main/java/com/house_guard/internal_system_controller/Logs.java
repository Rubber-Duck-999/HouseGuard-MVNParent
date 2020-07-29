package com.house_guard.internal_system_controller;

import javax.swing.*;

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class Logs {
    JFrame frame = new JFrame();
    Container container = new Container();
    JLabel timeFromLabel = new JLabel("Time from");
    JLabel timeToLabel = new JLabel("Time To");
    JLabel eventLabel = new JLabel("Logs Type");
    TimePicker timeFromPicker = new TimePicker();
    TimePicker timeToPicker = new TimePicker();
    JComboBox<String> eventCombo = new JComboBox<>(Constants.kEventTypes);
    JButton createButton = new JButton("GET LOGS");
    JButton resetButton = new JButton("RESET");
   
    Logs() {
        container = frame.getContentPane();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        frame.setTitle("House Guard Internal");
        frame.setVisible(false);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
    }

    public void setTimeClickers() {
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setColor(TimeArea.TimePickerTextValidTime, Color.blue);
        timeSettings.initialTime = LocalTime.now();
        timeFromPicker = new TimePicker(timeSettings);
        timeToPicker = new TimePicker(timeSettings);
    }
 
    public void setLayoutManager() {
        container.setLayout(null);
    }
 
    public void setLocationAndSize() {
        timeFromLabel.setBounds(50, 50, 100, 30);
        timeToLabel.setBounds(50, 120, 100, 30);
        eventLabel.setBounds(50, 190, 100, 30);
        timeFromPicker.setBounds(150, 50, 150, 30);
        timeToPicker.setBounds(150, 120, 150, 30);
        eventCombo.setBounds(150, 190, 150, 30);
        createButton.setBounds(50, 300, 100, 50);
        resetButton.setBounds(200, 300, 100, 50);
    }
 
    public void addComponentsToContainer() {
        container.add(timeFromLabel);
        container.add(timeToLabel);
        container.add(eventLabel);
        container.add(eventCombo);
        container.add(timeFromPicker);
        container.add(timeToPicker);
        container.add(createButton);
        container.add(resetButton);
    }
 
    public void addController(ActionListener listenerButtons) {
        createButton.setActionCommand("LOGS - CREATE");
        createButton.addActionListener(listenerButtons);;
        resetButton.setActionCommand("LOGS - RESET");
        resetButton.addActionListener(listenerButtons);
    }

    public LocalTime getTimeFrom() {
        return timeFromPicker.getTime();
    }

    public LocalTime getTimeTo() {
        return timeToPicker.getTime();
    }

    public String getEventType() {
        return (String) eventCombo.getSelectedItem();
    }

    public void changeView(boolean state) {
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

