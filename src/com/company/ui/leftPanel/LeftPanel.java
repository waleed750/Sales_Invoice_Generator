package com.company.ui.leftPanel;


import com.company.EventListener;
import com.company.Main;
import com.company.data.CompnentNames;
import com.company.exceptions.WrongFileFormatException;
import com.company.model.InvoiceModel;
import com.company.ui.MainFrame;
import com.company.ui.rightPanel.RightPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LeftPanel extends JComponent {

    private JTable invoiceTable ;
    private String [] cols = {"No." , "Date" , "Customer" , "Total" };
    public  static  int selectedIndex = -1 ;
    private JButton CreateNewInvoice ;
    private JButton DeleteInvoice ;
    private JLabel invoiceLabel;
    public  List<InvoiceModel> invoiceData;

    public static String currentPath ="";
    RightPanel rightPanel;
    public LeftPanel( RightPanel rightPanel , String path){
        /*invoiceTable = new JTable(data , cols);*/
        currentPath = path ;
        this.rightPanel = rightPanel;
        //Methods on Initiating
        if(!currentPath.equals(""))
        getInvoiceData();

        //Panel mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedIndex = -1;
                invoiceTable.getSelectionModel().clearSelection();
                System.out.println("Selectedindex : "+ selectedIndex +"SelectedRow : "+invoiceTable.getSelectedRow() );
            }
        });
        /*invoiceLabel = new JLabel("InvoiceLabels");*/
        Border blackline = BorderFactory.createTitledBorder("Invoice Tables" );

        //Get data to table

        if(invoiceData != null)
        rightPanel.getCurrentInvoiceItems(invoiceData.get(0));


        //Table Listeners
        if(invoiceTable == null){
            TableModel tb = new DefaultTableModel(null,cols){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return  false;
                }
            };
            invoiceTable = new JTable(tb);
        }
        invoiceTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){

                    int row = invoiceTable.getSelectedRow();
                    //To stop editing in cell
                    if(invoiceTable.isEditing()){
                        invoiceTable.getCellEditor().stopCellEditing();
                    }

                    updateTable(row );
                }
            }
        });
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                triggerMouseListener();
                /*JOptionPane.showMessageDialog(null , ""+selectedIndex);
*/
            }


        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(invoiceTable));
        panel.setBorder(blackline);


        EventListener el = new EventListener();

        CreateNewInvoice = new JButton(CompnentNames.CreateNewInvoice);
        CreateNewInvoice.setActionCommand(CompnentNames.CreateNewInvoice);
        CreateNewInvoice.addActionListener(el);


        DeleteInvoice = new JButton(CompnentNames.DeleteInvoice);
        DeleteInvoice.setActionCommand(CompnentNames.DeleteInvoice);
        DeleteInvoice.addActionListener(el);



        //add table invoice Details
        add(panel);

        //Create and Delete
        add(CreateNewInvoice);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(DeleteInvoice);



        setLayout(new FlowLayout());
        setVisible(true);


    }



    public void addNewInvoice() {
            DefaultTableModel m = (DefaultTableModel)invoiceTable.getModel();
            var lastNo = invoiceData.get(invoiceData.size()-1).No + 1;
            m.addRow(  new Object[]{lastNo+"", " ", " " , 0+""});
            invoiceData.add(new InvoiceModel(lastNo , "" , "" , 0));
        /*}else{
            rightPanel.addNewInvoiceItem(invoiceData.get(selectedIndex));
        }*/
    }

    void getInvoiceData(){
        /*String file = "InvoiceHeader.csv";
        Path path = Paths.get("src/com/company/data/",file);*/
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(String.valueOf(currentPath));
            int size = fis.available();
            byte[] b = new byte[size];
            fis.read(b);
            String text = new  String (b);
            String [] rows = text.split("\n");
            invoiceData = new InvoiceModel().fromCsv(rows);

            getTotal();
            if(invoiceTable != null){
                TableModel tb = new DefaultTableModel(twoDime(invoiceData),cols){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return  false;
                    }
                };
                invoiceTable.setModel(tb);
            }else{
                TableModel tb = new DefaultTableModel(twoDime(invoiceData),cols){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return  false;
                    }
                };
                invoiceTable = new JTable(tb);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }  finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    String [][] twoDime(List<InvoiceModel> rows){
        String [][] temp = new String[rows.size()][4];
        for (int i = 0; i < rows.size(); i++) {
            temp[i][0] = rows.get(i).No + "";
            temp[i][1] = rows.get(i).date;
            temp[i][2] = rows.get(i).customer;
            temp[i][3] = rows.get(i).total +"";

        }
        return  temp;
    }
    void getTotal(){
        String file = "InvoiceLine.csv";
        Path path = Paths.get("src/com/company/data/",file);
        FileInputStream fis = null;
        List<Integer> totals = new ArrayList<>();
        try {
            fis = new FileInputStream(String.valueOf(path));
            int size = fis.available();
            byte[] b = new byte[size];
            fis.read(b);
            String lines = new String(b);
            for (var line: lines.split("\n")) {

                //Split the row in columns
                var tempLine = line.split(",");
                //get total price of the item

                //tempLine[3].replaceAll("\\s+","") => To replace all white spaces
                var temptotal = Integer.parseInt(tempLine[2].replaceAll("\\s+","") ) * Integer.parseInt(tempLine[3].replaceAll("\\s+",""));
                //add total to the customer total
                try{
                    invoiceData.get(Integer.parseInt(tempLine[0].replaceAll("\\s+",""))-1).total += temptotal;
                }catch (IndexOutOfBoundsException EX ){
                    System.out.println("Incex Out Of Bounds : "+ EX.getMessage());
                }

            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
              if(fis != null){
                  fis.close();
              }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void updateTable(int row){



        var No = Integer.parseInt(String.valueOf(invoiceTable.getValueAt(invoiceTable.getRowCount()-1 ,0)));
        var date = String.valueOf(invoiceTable.getValueAt(row ,1));
        var customer =  String.valueOf(invoiceTable.getValueAt(row ,2));

        var total = Integer.parseInt(String.valueOf(invoiceTable.getValueAt(row ,3)));
        var newValue = new InvoiceModel(No , date ,customer, total);
        invoiceData.set(row ,newValue );


    }

    public void deleteInvoice(){
        int dialogButton = JOptionPane.YES_NO_OPTION;

        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to Delete?","Warning" ,dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            int row = invoiceTable.getSelectedRow();
            if(row < 0 &&  RightPanel.selectedIndex < 0){
                JOptionPane.showMessageDialog(null , "No Invoice Selected");
                return;
            }else if(RightPanel.selectedIndex >= 0){
                /*   rightPanel.*/
            }
            DefaultTableModel d = (DefaultTableModel)invoiceTable.getModel() ;
            rightPanel.deleteInvoice(invoiceData.get(row));
            d.removeRow(row);
            invoiceData.remove(row);

            /*  JOptionPane.showMessageDialog(null , "Row Deleted");*/
        /*invoiceData.forEach((value)->{
            JOptionPane.showMessageDialog(null , value.toString());
        });*/
            if(invoiceTable.getRowCount() >= 0){
                selectedIndex = 0 ;
                triggerMouseListener();
            }
            else
                selectedIndex = -1 ;
        }else{
            return;
        }

    }

    public void saveInvoices(){
        /*String file = "InvoiceHeader.csv";
        Path path = Paths.get("src/com/company/data/",file);*/
        FileOutputStream fos = null;
        try {
            byte[] b = Main.convertModelsToBytes(invoiceData);

            fos = new FileOutputStream(String.valueOf(currentPath));

            fos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't save ");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't save ");
        }finally {
            try {
                fos.close();
                System.out.println("Saved Successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void saveInvoices(String path){

        FileOutputStream fos = null;
        try {
            byte[] b = Main.convertModelsToBytes(invoiceData);

            fos = new FileOutputStream(String.valueOf(path));

            fos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't save ");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't save ");
        }finally {
            try {
                fos.close();
                System.out.println("Saved Successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    void triggerMouseListener(){
        selectedIndex = invoiceTable.getSelectedRow() < 0 ? 0 : invoiceTable.getSelectedRow() ;


        MainFrame.lselectedIndex = selectedIndex;
        /*MainFrame.lselectedIndex = selectedIndex;MainFrame.rselectedIndex = -1 ;*/
        try{
            rightPanel.getCurrentInvoiceItems(invoiceData.get(selectedIndex));
        }catch (Exception error){
            System.out.println(error.toString()+"");
        }
    }
    public void updateRow(String name , String date){
        DefaultTableModel m = (DefaultTableModel)invoiceTable.getModel() ;
        selectedIndex = Math.max(selectedIndex, 0);
        var data = invoiceData.get(selectedIndex);
        data.customer = name;
        System.out.println("Date "+ date);
        data.date = date;
        invoiceData.set(selectedIndex , data);
        TableModel tb = new DefaultTableModel(twoDime(invoiceData),cols){
            @Override
            public boolean isCellEditable(int row, int column) {
                return  false;
            }
        };
        invoiceTable = new JTable(tb);
    }
    public static String getFileExtension(String fullName) {

        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
