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

        oneButton   = new JButton("1");
        twoButton   = new JButton("2");
        threeButton = new JButton("3");

        fourButton  = new JButton("4");
        fiveButton   = new JButton("5");
        sixButton   = new JButton("6");

        sevenButton = new JButton("7");
        eightButton = new JButton("8");
        nineButton  = new JButton("9");

        clearButton = new JButton("X");
        zeroButton = new JButton("0");
        backButton = new JButton("<");

        buttonPanel.add(oneButton);
        buttonPanel.add(twoButton);
        buttonPanel.add(threeButton);
        
        buttonPanel.add(fourButton);
        buttonPanel.add(fiveButton);
        buttonPanel.add(sixButton);

        buttonPanel.add(sevenButton);
        buttonPanel.add(eightButton);
        buttonPanel.add(nineButton);

        buttonPanel.add(clearButton);
        buttonPanel.add(zeroButton);
        buttonPanel.add(backButton);

        enter = new JButton("Enter");
        enter.setFont(new Font("Calibri", Font.BOLD, 40));
        buttonPanel.setLayout(new GridLayout(4,3));

        setButtonFont();
        setLabelFont();

        fieldPanel.setLayout(new GridLayout(1,2));
        fieldPanel.add(passFieldDigit4);
        fieldPanel.add(passFieldDigit3);
        fieldPanel.add(passFieldDigit2);
        fieldPanel.add(passFieldDigit1);

        frame.add(fieldPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(enter, BorderLayout.SOUTH);

        GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        //frame.setSize(1280,980);
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

    private void setButtonFont()
    {
        buttonFont = new Font("Calibri", Font.BOLD, 40);
        oneButton.setFont(buttonFont);
        twoButton.setFont(buttonFont);
        threeButton.setFont(buttonFont);

        fourButton.setFont(buttonFont);
        fiveButton.setFont(buttonFont);
        sixButton.setFont(buttonFont);

        sevenButton.setFont(buttonFont);
        eightButton.setFont(buttonFont);
        nineButton.setFont(buttonFont);

        clearButton.setFont(buttonFont);
        zeroButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
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
        oneButton.setActionCommand(Types.Actions.ONE.name());
        oneButton.addActionListener(listenerButtons);
        twoButton.setActionCommand(Types.Actions.TWO.name());
        twoButton.addActionListener(listenerButtons);
        threeButton.setActionCommand(Types.Actions.THREE.name());
        threeButton.addActionListener(listenerButtons);

        fourButton.setActionCommand(Types.Actions.FOUR.name());
        fourButton.addActionListener(listenerButtons);
        fiveButton.setActionCommand(Types.Actions.FIVE.name());
        fiveButton.addActionListener(listenerButtons);
        sixButton.setActionCommand(Types.Actions.SIX.name());
        sixButton.addActionListener(listenerButtons);

        sevenButton.setActionCommand(Types.Actions.SEVEN.name());
        sevenButton.addActionListener(listenerButtons);
        eightButton.setActionCommand(Types.Actions.EIGHT.name());
        eightButton.addActionListener(listenerButtons);
        nineButton.setActionCommand(Types.Actions.NINE.name());
        nineButton.addActionListener(listenerButtons);

        clearButton.setActionCommand(Types.Actions.CLEAR.name());
        clearButton.addActionListener(listenerButtons);
        zeroButton.setActionCommand(Types.Actions.ZERO.name());
        zeroButton.addActionListener(listenerButtons);
        backButton.setActionCommand(Types.Actions.BACK.name());
        backButton.addActionListener(listenerButtons);

        enter.setActionCommand(Types.Actions.ENTER.name());
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

    public static class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            e.getWindow().setVisible(false);
            System.exit(0);
        }
    }
}
