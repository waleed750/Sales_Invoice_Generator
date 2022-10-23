package com.company.ui.rightPanel;

import com.company.Main;
import com.company.data.CompnentNames;
import com.company.exceptions.DateFormatException;
import com.company.model.InvoiceItemModel;
import com.company.model.InvoiceModel;
import com.company.ui.MainFrame;
import com.company.ui.leftPanel.LeftPanel;
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RightPanel extends JComponent implements ActionListener {


    int invnumber = 0;
    private JLabel invoiceNumber = new JLabel("Invoice Number ");
    private JLabel invoiceInt = new JLabel("   "+invnumber);

    private JLabel invoiceDate = new JLabel("Invoice Date           ");
    private JTextField invoiceDateField = new JTextField();
    private JLabel customerName = new JLabel("Customer Name  ") ;
    private JTextField customerNameField = new JTextField();
    private JLabel invoiceTotal = new JLabel("Invoice Total        ");
    private JLabel invoiceTotalNumber = new JLabel("0");

    private JButton saveButton = new JButton(CompnentNames.SaveBtn);

    private JButton cancelButton = new JButton(CompnentNames.CancelBtn);

   /* private JLabel invoiceItems;*/

    private JTable invoiceItemTable ;

    List<InvoiceItemModel> invoiceItems ;
    List<InvoiceItemModel> currentInvoiceItems ;
    public  static int selectedIndex = -1 ;

    String[] cols2 = {"No." , "Item Name" , "Item Price" , "Count" , "Item Total"};
    public RightPanel(){
        //Right Panel Starts -------------------------

        getInvoiceItems(new InvoiceModel(1 , "sda" , "sd" , 0));
        getCurrentInvoiceItems(new InvoiceModel(1 , "sda" , "sd" , 0));
        JPanel panel = new JPanel();
        Border blackline = BorderFactory.createTitledBorder("Invoice Items" );


        panel.add(new JScrollPane(invoiceItemTable));
        panel.setBorder(blackline);
//------------------------------------------------------------

        add(invoiceNumber);
        add(invoiceInt);

        invoiceDateField = new JTextField(30);
        customerNameField = new JTextField(30);

        customerNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("FocusGained");

            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("FocusLost");
                    if(customerNameField.getText().length() < 3){
                       customerNameField.requestFocus();
                       JOptionPane.showMessageDialog(null,"You have to enter name more than 3 characters");
                    }else{
                        Main.fmain.leftPanel.updateRow(customerNameField.getText() , invoiceDateField.getText());
                    }
            }
        });

        customerNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(customerNameField.getText().length() < 3){
                    customerNameField.requestFocus();
                }else{
                    Main.fmain.leftPanel.updateRow(customerNameField.getText() , invoiceDateField.getText());
                }
            }
        });

        invoiceDateField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    validateFormat();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JPanel panel1 = new JPanel();

        panel1.setLayout(new BoxLayout(panel1 , BoxLayout.X_AXIS));



        panel1.add(invoiceDate);
        panel1.add(invoiceDateField);
        JPanel panel3 = new JPanel();

        panel3.setLayout(new BoxLayout(panel3 , BoxLayout.Y_AXIS));
        panel3.add(panel1);
        add(panel3);


        add(customerName);
        add(customerNameField);

        add(invoiceTotal);
        add(invoiceTotalNumber);


        add(panel);

        //Save and Cancel

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        saveButton.addActionListener(this);
        saveButton.setActionCommand(CompnentNames.SaveBtn);
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand(CompnentNames.CancelBtn);

        bottomPanel.add(saveButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        bottomPanel.add(cancelButton);
        add(bottomPanel);

        invoiceItemTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){

                    int row = invoiceItemTable.getSelectedRow();
                    //To stop editing in cell
                    if(invoiceItemTable.isEditing()){
                        invoiceItemTable.getCellEditor().stopCellEditing();
                    }

                    updateTable(row );
                }
            }
        });
        invoiceItemTable.addMouseListener(new MouseAdapter() {
            @Override
              public void mouseClicked(MouseEvent e) {
             selectedIndex = invoiceItemTable.getSelectedRow() < 0 ? 0 : invoiceItemTable.getSelectedRow();


             MainFrame.lselectedIndex = selectedIndex;
         }
          });
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setVisible(true);
        //Right Panel Ends ----------------------------
    }
    public  void getInvoiceItems(InvoiceModel model){

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
                invoiceItems = new ArrayList<>();
                invnumber = 0 ;
                int total = 0;
                for (var line: lines.split("\n")) {
                    //Split the row in columns

                    var tempLine = line.split(",");
                    System.out.println(tempLine[2]);

                    int id = Integer.parseInt(tempLine[0].replaceAll("\\s+",""));
                    var temp = new InvoiceItemModel(
                    id,//No
                    tempLine[1],//Item name
                    Integer.parseInt(tempLine[2].replaceAll("\\s+","")),
                    Integer.parseInt(tempLine[3].replaceAll("\\s+",""))
                    );
                    invnumber += temp.count;
                    total += temp.itemTotal;
                    invoiceItems.add(temp);
                }

                invoiceItems.stream().sorted();


            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public  void getCurrentInvoiceItems(InvoiceModel model){
        currentInvoiceItems= new ArrayList<>();
        invnumber = 0 ;
        for (InvoiceItemModel invoiceItem : invoiceItems) {
            if(model.No == invoiceItem.No){
                currentInvoiceItems.add(invoiceItem);
                invnumber++;
            }
        }

        invoiceInt.setText(invnumber+"");
        invoiceTotalNumber.setText(model.total+"");
        customerNameField.setText(model.customer);
        invoiceDateField.setText(model.date);

        System.out.println("Number of invoices "+ invnumber);
                /*if(invoiceItemTable!= null)
                    invoiceItemTable.setModel(twoDime(invoiceItems));*/

        if(invoiceItemTable != null){
            /* RemoveAll();*/
            TableModel df = new DefaultTableModel(twoDime(currentInvoiceItems), cols2);
            invoiceItemTable.setModel(df);
        }
        if(invoiceItemTable == null){
            TableModel df = new DefaultTableModel(twoDime(currentInvoiceItems), cols2);
            invoiceItemTable = new JTable(df);
        }

    }
    String [][] twoDime(List<InvoiceItemModel> rows){
        String [][] temp = new String[rows.size()][5];
        for (int i = 0; i < rows.size(); i++) {
            temp[i][0] = rows.get(i).No + "";
            temp[i][1] = rows.get(i).itemName;
            temp[i][2] = rows.get(i).itemPrice +"";
            temp[i][3] = rows.get(i).count+"";
            temp[i][4] = rows.get(i).itemTotal +"";
            System.out.println(rows.get(i).toString());
        }
        return  temp;
    }

    void RemoveAll(){
        DefaultTableModel tbmodel = (DefaultTableModel) invoiceItemTable.getModel();
        for (int i = 0; i < invoiceItemTable.getRowCount(); i++) {
            tbmodel.removeRow(i);
        }
    }
    public void addNewInvoiceItem(InvoiceModel model){
        DefaultTableModel m = (DefaultTableModel)invoiceItemTable.getModel();

        m.addRow(  new Object[]{model.No+"", " ", ""+0 , 0+"" , ""+0});

        var itemModel = new InvoiceItemModel(model.No , "" , 0, 0 );

        currentInvoiceItems.add(itemModel);

       /* invoiceItems.add(itemModel);
        invoiceItems.stream().sorted();*/

    }

    public void saveInvoices(){

        String file = "InvoiceLine.csv";
        Path path = Paths.get("src/com/company/data/",file);
        FileOutputStream fos = null;
        try {
            byte[] b = Main.convertModelsToBytes(invoiceItems);

            fos = new FileOutputStream(String.valueOf(path));

            fos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
           JOptionPane.showMessageDialog(null , e.toString(),"Warning",JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null , e.toString(),"Warning",JOptionPane.ERROR_MESSAGE);
            System.out.println("Couldn't save Item");
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void updateTable(int row){



        var No = Integer.parseInt(String.valueOf(invoiceItemTable.getValueAt(invoiceItemTable.getRowCount()-1 ,0)));
        var itemName = String.valueOf(invoiceItemTable.getValueAt(row ,1));
        var itemPrice = Integer.parseInt( String.valueOf(invoiceItemTable.getValueAt(row ,2)));

        var count = Integer.parseInt(String.valueOf(invoiceItemTable.getValueAt(row ,3)));
        var newValue = new InvoiceItemModel(No , itemName ,itemPrice, count);

        System.out.println("New Value : "+newValue.itemTotal);
        currentInvoiceItems.set(row ,newValue);
        invoiceItems.add(newValue);
        invoiceItems.sort(new Comparator<InvoiceItemModel>() {
            @Override
            public int compare(InvoiceItemModel lhs, InvoiceItemModel rhs) {
                return Integer.compare(lhs.No, rhs.No);
            }
        });
        invoiceItemTable.setValueAt(newValue.itemTotal+"" , row ,invoiceItemTable.getColumnCount()-1 );
       /* switch (column){
            case 0 :
                oldvalue.No = Integer.parseInt(String.valueOf(invoiceItemTable.getValueAt(row ,column)));
                invoiceData.set(row , oldvalue);
                break;
            case 1 :
                oldvalue.date = String.valueOf(invoiceItemTable.getValueAt(row ,column));
                invoiceData.set(row , oldvalue) ;
                break;
            case 2 :
                invoiceData.get(row).customer = String.valueOf(invoiceItemTable.getValueAt(row ,column));
                break;
            case 3 :
                invoiceData.get(row).total = Integer.parseInt(String.valueOf(invoiceItemTable.getValueAt(row ,column)));
        }*/

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Save" :
                try {
                    validateFormat();
                    Main.saveInvoices();
                    System.out.println("Save triggered");
                } catch (Exception ex) {

                }finally {
                    break;
                }
            case "Cancel":
                Main.relanch();
                System.out.println("cancel triggered");
                break;
        }
    }
    public void deleteInvoice(InvoiceModel model){

        if(currentInvoiceItems.size() > 0 )
            currentInvoiceItems.clear();
        for (int i = 0; i < invoiceItems.size(); i++) {
            var value = invoiceItems.get(i);
            if(value.No == model.No){
                invoiceItems.remove(i);
            }
        }
        /*for (var value :invoiceItems) {
            if(value.No == model.No){
                System.out.println("Model Item : "+invoiceItems.get(i).toString());
                invoiceItems.remove(i);
            }
            i++;

        }*/

        JOptionPane.showMessageDialog(null , "Row Deleted");
    }
    void validateFormat()throws  Exception{
        if(invoiceDateField.getText().matches("^[0-3]?[0-9].[0-3]?[0-9].(?:[0-9]{2})?[0-9]{2}")){

        }else{
            invoiceDateField.requestFocus();
            throw new DateFormatException();
        }
    }
}
