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
    private JLabel monitorState, timeLabel;
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
        timeLabel = new JLabel("", SwingConstants.CENTER);
        fieldPanel.add(monitorState);
        fieldPanel.add(timeLabel);
        fieldPanel.setLayout(new GridLayout(2,2));
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
        Font buttonFont = new Font("Calibri", Font.BOLD, 128);
        monitorState.setFont(buttonFont);
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
        timeLabel.setText("Current Time: " + timeNow + ", Last Changed: " + timeChanged);
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
