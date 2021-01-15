package com.house_guard.user_panel;

import java.awt.BorderLayout;
import com.house_guard.Common.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MonitorView
{
    private JFrame frame;
    private JLabel monitorState;
    private JButton ONButton, OFFButton, HIDEButton, CHANGEButton;

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
        HIDEButton = new JButton("HIDE");
        CHANGEButton = new JButton("Change Pin");
        buttonPanel.add(ONButton);
        buttonPanel.add(OFFButton);
        buttonPanel.add(HIDEButton);
        buttonPanel.add(CHANGEButton);
        buttonPanel.setLayout(new GridLayout(4,4));
        frame.add(buttonPanel, BorderLayout.CENTER);
    }

    private void setFieldPanel() {
        JPanel fieldPanel = new JPanel();
        monitorState = new JLabel("", SwingConstants.CENTER);
        fieldPanel.add(monitorState);
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
    }

    private void setButtonFont()
    {
        Font buttonFont = new Font("Calibri", Font.BOLD, 34);
        ONButton.setFont(buttonFont);
        ONButton.setForeground(Color.GREEN);
        OFFButton.setFont(buttonFont);
        OFFButton.setForeground(Color.RED);
        HIDEButton.setFont(buttonFont);
        HIDEButton.setForeground(Color.BLUE);
        CHANGEButton.setFont(buttonFont);
        CHANGEButton.setForeground(Color.BLACK);
    }

    public void close()
    {
        frame.dispose();
    }

    public void hide()
    {
        frame.setVisible(false);
    }

    public void show()
    {
        frame.setVisible(true);
    }

    public < E > void setMonitorState(E v, Color colour)
    {
        monitorState.setText("" + v);
        monitorState.setForeground(colour);
    }

    public void addController(ActionListener listenerButtons)
    {
        ONButton.setActionCommand(Types.ON);
        OFFButton.setActionCommand(Types.OFF);
        ONButton.addActionListener(listenerButtons);
        OFFButton.addActionListener(listenerButtons);
        HIDEButton.setActionCommand("HIDE");
        HIDEButton.addActionListener(listenerButtons);
        CHANGEButton.setActionCommand("CHANGE");
        CHANGEButton.addActionListener(listenerButtons);
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
