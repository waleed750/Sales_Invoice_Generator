package com.company.model;

public class InvoiceItemModel /*implements Comparable*/ {
    public int No ;
    public String itemName ;
    public int itemPrice;
    public int count ;
    public int itemTotal;

    public InvoiceItemModel(int no, String itemName, int itemPrice, int count) {
        No = no;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.itemTotal = itemPrice * count;
    }

    @Override
    public String toString() {
        return No + "," + itemName + "," + itemPrice + "," + count + "," + itemTotal +"\n";
    }

    /*@Override
    public int compareTo(Object o) {
        InvoiceItemModel l = (InvoiceItemModel) o;
        if(this.No < l.No){
            return 0;
        }else{
            return  1;
        }
    }*/
}
