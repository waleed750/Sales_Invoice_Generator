package com.company;

import com.company.model.InvoiceModel;
import com.company.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static MainFrame fmain;
    public static<T> byte[] convertModelsToBytes(List<T> models){

        String text= "";
        for (var value: models) {

            text = text + value.toString();
        }
      /*  JOptionPane.showMessageDialog(null, text);*/
        return  text.getBytes() ;
    }
    public static void main(String[] args) {

        fmain = new MainFrame();
        fmain.setVisible(true);
    }
    public static void relanch(){
        fmain.setVisible(false);
        fmain.dispose();
        fmain = new MainFrame();
        fmain.setVisible(true);
    }
    public static  void saveInvoices(){
        fmain.leftPanel.saveInvoices();
        fmain.rightPanel.saveInvoices();
        relanch();
    }

}
