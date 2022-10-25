package com.company.ui;

import com.company.Main;
import com.company.data.CompnentNames;
import com.company.ui.leftPanel.LeftPanel;
import com.company.ui.rightPanel.RightPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainFrame extends JFrame implements ActionListener {

    public LeftPanel leftPanel ;
    public RightPanel rightPanel;
    public  static int lselectedIndex = -1 ;
    public  static int rselectedIndex = -1 ;



    private  static String leftCurrentPath = Paths.get("src/com/company/data/","InvoiceHeader.csv").toString() ;
    private static String rightCurrentPath = Paths.get("src/com/company/data/","InvoiceLine.csv").toString();


   private JMenuBar menuBar;
   private JMenu fileMenu;
   private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;

       /* String file1 = "InvoiceHeader.csv";
        Path path1 = Paths.get("src/com/company/data/",file1);


        String file2 = ;
        Path path2 = Paths.get("src/com/company/data/",file2);*/

    public MainFrame(){

        rightPanel = new RightPanel(rightCurrentPath);
        leftPanel = new LeftPanel(rightPanel , leftCurrentPath);



        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        loadMenuItem = new JMenuItem(CompnentNames.LoadFile);
        saveMenuItem = new JMenuItem(CompnentNames.SaveFile);

        saveMenuItem.addActionListener(this);
        saveMenuItem.setActionCommand(CompnentNames.SaveFile);

        loadMenuItem.addActionListener(this);
        loadMenuItem.setActionCommand(CompnentNames.LoadFile);

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
    public MainFrame(Path path1 , Path path2){


    }

    public static void refresh(JFrame f){
        f.invalidate();
        f.validate();
        f.repaint();
        System.out.println("Referesh done");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()){
                case CompnentNames.LoadFile:
                    loadFile();
                    System.out.println("Current Paht : "+leftCurrentPath);
                  /*  loadFile(rightCurrentPath);*/
                    System.out.println("Current Paht : "+rightCurrentPath);
                    Main.reLanch();
                    break;
                case CompnentNames.SaveFile :
                    saveFile();
                    break;

            }
    }

    void loadFile (){

        String newleftPath = "";
        String newrightPath = "";

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter fi = new FileNameExtensionFilter("ExcelFiles",
                ".csv");
        fc.addChoosableFileFilter(fi);
        if(fc.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
            newleftPath = new String(fc.getSelectedFile().getPath());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(leftCurrentPath);
                int size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            return;
        }
        fc = new JFileChooser();
        fi = new FileNameExtensionFilter("ExcelFiles",
                ".csv");
        fc.addChoosableFileFilter(fi);
        if(fc.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
            newrightPath = new String(fc.getSelectedFile().getPath());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(leftCurrentPath);
                int size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            return;
        }
        leftCurrentPath = newleftPath;
        rightCurrentPath = newrightPath;
    }

    void saveFile(){
        JFileChooser fc = new JFileChooser();
        /*fc.setCurrentDirectory(new java.io.File("."));*/
/*
        fc.setDialogTitle(choosertitle);
*/
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        boolean created = false;
        String filePath = "";
        FileOutputStream fos = null;
        if(fc.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
            filePath = fc.getSelectedFile().getPath();
            System.out.println("File Path " + filePath);
            File f1 = new File(filePath+"/InvoiceHeader.csv");

            try {
                if(!f1.exists()){
                    f1.createNewFile();
                }
                File f2 = new File(filePath+"/InvoiceLine.csv");
                if(!f2.exists()){
                    f2.createNewFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            //Save Files
            leftPanel.saveInvoices(filePath+"/InvoiceHeader.csv");
            rightPanel.saveInvoices(filePath+"/InvoiceLine.csv");
            JOptionPane.showMessageDialog(null,"Data Saved Successfully to "+filePath);
        }
    }

   /* void changeCurrentPath(String path1 , ){
        leftCurrentPath= path1.toString();
        rightCurrentPath = path2.toString();
    }*/
}
