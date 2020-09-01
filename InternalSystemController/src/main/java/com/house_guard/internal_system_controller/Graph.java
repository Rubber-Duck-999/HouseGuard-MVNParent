package com.house_guard.internal_system_controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane; 
import javax.swing.JTable; 

public class Graph {

    JFrame f; 
    // Table 
    JTable j; 
    private String[] columnNames = { "Index", "Type", "Time Sent" };


    // Constructor 
    Graph(String[][] array) 
    { 
        // Frame initiallization 
        f = new JFrame(); 
  
        // Frame Title 
        f.setTitle("Type: ");
  
        // Initializing the JTable 
        j = new JTable(array, columnNames); 
        j.setBounds(30, 40, 200, 300); 
  
        // adding it to JScrollPane 
        JScrollPane sp = new JScrollPane(j); 
        f.add(sp); 
        // Frame Size 
        f.setSize(500, 200); 
        // Frame Visible = true 
        f.setVisible(true); 
    }
}

