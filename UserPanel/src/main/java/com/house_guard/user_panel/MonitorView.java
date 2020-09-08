package com.house_guard.user_panel;

import java.awt.BorderLayout;
import com.house_guard.Common.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.logging.Logger;

public class MonitorView
{
    private JFrame frame;
    private JLabel monitorState, timeLabel, userLabel;
    private JButton ONButton, OFFButton;

    public MonitorView()
    {
        frame = new JFrame("Alarm State");
        setButtonPanel();
        setFieldPanel();
        setButtonFont();
        setLabelFont();
    }

    private void setButtonPanel() {
        JPanel buttonPanel = new JPanel();
        ONButton = new JButton(Types.ON);
        OFFButton = new JButton(Types.OFF);
        buttonPanel.add(ONButton);
        buttonPanel.add(OFFButton);
        buttonPanel.setLayout(new GridLayout(2,4));
        frame.add(buttonPanel, BorderLayout.CENTER);
    }

    private void setFieldPanel() {
        JPanel fieldPanel = new JPanel();
        monitorState = new JLabel("", SwingConstants.CENTER);
        timeLabel = new JLabel("", SwingConstants.LEFT);
        userLabel = new JLabel("", SwingConstants.CENTER);
        fieldPanel.add(timeLabel);
        fieldPanel.add(monitorState);
        fieldPanel.add(userLabel);
        fieldPanel.setLayout(new GridLayout(1,3));
        frame.add(fieldPanel, BorderLayout.NORTH);
    }

    public void setMonitor()
    {
        GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        device.setFullScreenWindow(frame);
    }

    private void setLabelFont()
    {
        Font buttonFont = new Font("Calibri", Font.BOLD, 104);
        monitorState.setFont(buttonFont);
        timeLabel.setFont(new Font("Calibri", Font.ITALIC, 12));
        userLabel.setFont(new Font("Calibri", Font.ITALIC, 12));
    }

    private void setButtonFont()
    {
        Font buttonFont = new Font("Calibri", Font.BOLD, 40);
        ONButton.setFont(buttonFont);
        ONButton.setForeground(Color.GREEN);
        OFFButton.setFont(buttonFont);
        OFFButton.setForeground(Color.RED);
    }

    public void close()
    {
        frame.dispose();
    }

    public < E > void setMonitorState(E v, Color colour)
    {
        monitorState.setText("" + v);
        monitorState.setForeground(colour);
    }

    public < E > void setTimeLabel(E timeChanged, E timeNow)
    {
        timeLabel.setText("Time: " + timeNow + ", Last: " + timeChanged);
    }

    public < E > void setUser(E user)
    {
        userLabel.setText("Last User to set Alarm: " + user);
    }

    public void addController(ActionListener listenerButtons)
    {
        ONButton.setActionCommand(Types.ON);
        OFFButton.setActionCommand(Types.OFF);
        ONButton.addActionListener(listenerButtons);
        OFFButton.addActionListener(listenerButtons);
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
