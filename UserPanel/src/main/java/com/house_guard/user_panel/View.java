package com.house_guard.user_panel;

import java.awt.BorderLayout;
import com.house_guard.user_panel.Types;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class View
{
    private JFrame frame;
    private JLabel[] digits;
    private JButton[] buttons;
    private JButton enter;

    public View()
    {
        frame = new JFrame("User Panel");
        JPanel buttonPanel = new JPanel();
        JPanel fieldPanel = new JPanel();

        buttons = new JButton[12];
        String[] label = {Types.ONE, Types.TWO, Types.THREE,
                          Types.FOUR, Types.FIVE, Types.SIX,
                          Types.SEVEN, Types.EIGHT, Types.NINE,
                          Types.CLEAR, Types.ZERO, Types.BACK};
        buttonPanel.setLayout(new GridLayout(4,3));
        Font buttonFont = new Font("Calibri", Font.BOLD, 40);

        for (int i = 0; i < 12; i++) {
            buttons[i] = new JButton(label[i]);
            buttonPanel.add(buttons[i]);
            buttons[i].setFont(buttonFont);
        }

        enter = new JButton("Enter");
        enter.setFont(new Font("Calibri", Font.BOLD, 40));
        
        digits = new JLabel[4];

        fieldPanel.setLayout(new GridLayout(1,2));
        buttonFont = new Font("Calibri", Font.BOLD, 128);
        for (int i = 3; i > -1; i--) {
            digits[i] = new JLabel("", SwingConstants.CENTER);
            fieldPanel.add(digits[i]);
            digits[i].setFont(buttonFont);
        }

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

    public < E > void setDigits(String[] v)
    {
        for (int i = 0; i < 4; i++) {
            digits[i].setText("" + v[i]);
        }
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
