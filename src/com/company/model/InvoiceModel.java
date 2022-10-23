package com.company.model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvoiceModel {
    public int No;
    public String date ;
    public String customer ;
    public int total;

    public  InvoiceModel(){

    }
    public InvoiceModel(int no, String date, String customer, int total) {
        No = no;
        this.date = date;
        this.customer = customer;
        this.total = total;
    }
    public List<InvoiceModel> fromCsv(String[] data){
        List<InvoiceModel> models = new ArrayList<>();
        int i = 0 ;
        for(var row : data){
            String[] feild = row.split(",");

            models.add(new InvoiceModel(Integer.parseInt(feild[0]) , feild[1] , feild[2] , 0));
           /* No = Integer.parseInt(feild[0]);
            date = feild[1];
            customer = feild[2];*/
            i++;
        }

       return models;
    }

    @Override
    public String toString() {
        return  No + "," + date  + "," + customer  + "," + total +"\n";
    }


}
