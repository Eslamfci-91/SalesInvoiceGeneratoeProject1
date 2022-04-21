/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * an/*d open the template in the editor.
 */
package com.invoice.model;

import com.invoice.view.GeneratorFrame;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Eslam
 */
public class InvoiceHeaderTableModel extends AbstractTableModel{
    
    private ArrayList<InvoiceHeader> invoicesArray;
    private String[] columns = {"ID","Date","Customer Name","Total"};

    public InvoiceHeaderTableModel(ArrayList<InvoiceHeader> invoicesArray) {
        this.invoicesArray = invoicesArray;
    }
    
    
    @Override
    public int getRowCount() {
        return invoicesArray.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader inv = invoicesArray.get(rowIndex);
        switch(columnIndex){
            case 0: 
                return inv.getNo();
            case 1: 
                return GeneratorFrame.dateFormat.format(inv.getInvDate());
            case 2:
                return inv.getCustomer();
            case 3: 
                return inv.getInvoiceTotal();
            
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        
        return columns[column];
        
    }
    
    
    
}
