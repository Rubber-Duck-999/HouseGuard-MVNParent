package com;

import java.awt.BorderLayout;
import com.house_guard.Common.*;
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

public class View
{
    private JFrame frame;
    private JPanel buttonPanel, fieldPanel;
    private JLabel passFieldDigit1, passFieldDigit2, passFieldDigit3, passFieldDigit4;
    private JButton oneButton, twoButton, threeButton;
    private JButton fourButton, fiveButton, sixButton;
    private JButton sevenButton, eightButton, nineButton;
    private JButton clearButton, zeroButton, backButton;
    private JButton[] buttons;
    private JButton enter;
    private Font buttonFont;

    public View()
    {
        frame = new JFrame("User Panel");
        buttonPanel = new JPanel();
        fieldPanel = new JPanel();

        passFieldDigit1 = new JLabel("", SwingConstants.CENTER);
        passFieldDigit2 = new JLabel("", SwingConstants.CENTER);
        passFieldDigit3 = new JLabel("", SwingConstants.CENTER);
        passFieldDigit4 = new JLabel("", SwingConstants.CENTER);

        buttons = new JButton[12];
        String[] label = {Types.ONE, Types.TWO, Types.THREE,
                          Types.FOUR, Types.FIVE, Types.SIX,
                          Types.SEVEN, Types.EIGHT, Types.NINE,
                          Types.CLEAR, Types.ZERO, Types.BACK};
        buttonPanel.setLayout(new GridLayout(4,3));
        buttonFont = new Font("Calibri", Font.BOLD, 40);

        for (int i = 0; i < 12; i++) {
            buttons[i] = new JButton(label[i]);
            buttonPanel.add(buttons[i]);
            buttons[i].setFont(buttonFont);
        }

        enter = new JButton("Enter");
        enter.setFont(new Font("Calibri", Font.BOLD, 40));
        
        setLabelFont();

        fieldPanel.setLayout(new GridLayout(1,2));
        fieldPanel.add(passFieldDigit4);
        fieldPanel.add(passFieldDigit3);
        fieldPanel.add(passFieldDigit2);
        fieldPanel.add(passFieldDigit1);

        frame.add(fieldPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(enter, BorderLayout.SOUTH);

        setView();
    }

    public void setView()
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
        buttonFont = new Font("Calibri", Font.BOLD, 128);
        passFieldDigit1.setFont(buttonFont);
        passFieldDigit2.setFont(buttonFont);
        passFieldDigit3.setFont(buttonFont);
        passFieldDigit4.setFont(buttonFont);
    }

    public < E > void setDigits(String[] v)
    {
        passFieldDigit1.setText("" + v[0]);
        passFieldDigit2.setText("" + v[1]);
        passFieldDigit3.setText("" + v[2]);
        passFieldDigit4.setText("" + v[3]);
    }

    public void addController(ActionListener listenerButtons)
    {
        String[] actions = {Types.ONE, Types.TWO, Types.THREE,
            Types.FOUR, Types.FIVE, Types.SIX,
            Types.SEVEN, Types.EIGHT, Types.NINE,
            Types.CLEAR, Types.ZERO, Types.BACK};
        for (int i = 0; i < 12; i++) {
            buttons[i].addActionListener(listenerButtons);
            buttons[i].setActionCommand(actions[i]);
        }

        enter.setActionCommand(Types.ENTER);
        enter.addActionListener(listenerButtons);
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
