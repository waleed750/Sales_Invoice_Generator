package com.company.ui;

import com.company.data.CompnentNames;
import com.company.ui.leftPanel.LeftPanel;
import com.company.ui.rightPanel.RightPanel;
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame implements ActionListener {

    public LeftPanel leftPanel ;
    public RightPanel rightPanel;
    public  static int lselectedIndex = -1 ;
    public  static int rselectedIndex = -1 ;

   private JMenuBar menuBar;
   private JMenu fileMenu;
   private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;


    public MainFrame(){
        rightPanel = new RightPanel();
        leftPanel = new LeftPanel(rightPanel );
       /* JPanel lpanel = new JPanel();
        lpanel.add(leftPanel);
        lpanel.setLayout(new BoxLayout(lpanel , BoxLayout.Y_AXIS));
        add(lpanel);*/

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        loadMenuItem = new JMenuItem(CompnentNames.LoadFile);
        saveMenuItem = new JMenuItem(CompnentNames.SaveFile);

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        add(leftPanel);


        add(rightPanel);




        setLocation(200 , 50);
        setSize(1000 , 670);
        setResizable(false);


        setLayout(new GridLayout(1, 2));


        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void refresh(JFrame f){
        f.invalidate();
        f.validate();
        f.repaint();
        System.out.println("Referesh done");
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }



}
