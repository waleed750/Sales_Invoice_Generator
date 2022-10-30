package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Create New Invoice" :
                Main.fmain.leftPanel.addNewInvoice();
                JOptionPane.showMessageDialog(null , "new ");
                break;
            case "Delete Invoice":
                Main.fmain.leftPanel.deleteInvoice();
                break;
            case "Save" :
                try {
                    Main.fmain.rightPanel.validateFormat();
                    Main.fmain.rightPanel.addNewInvoiceItem();
                    Main.saveInvoices();
                    System.out.println("Save triggered");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }finally {
                    break;
                }
            case "Cancel":
                Main.reLanch();
                System.out.println("cancel triggered");
                break;
        }
    }
}
