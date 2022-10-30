package com.company;

import com.company.ui.MainFrame;

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
    public static void reLanch(){
        fmain.setVisible(false);
        fmain.dispose();
        fmain = new MainFrame();
        fmain.setVisible(true);
    }
    public static  void saveInvoices(){
        fmain.leftPanel.saveInvoices();
        fmain.rightPanel.saveInvoices();
    }

}
