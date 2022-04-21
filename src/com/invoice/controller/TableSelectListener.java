/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invoice.controller;

import com.invoice.model.InvoiceHeader;
import com.invoice.model.InvoiceLine;
import com.invoice.model.InvoiceLineTableModel;
import com.invoice.view.GeneratorFrame;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sila
 */
public class TableSelectListener implements ListSelectionListener{
    
    private GeneratorFrame frame ;

    public TableSelectListener(GeneratorFrame frame) {
        this.frame = frame;
    }
    

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedInvIndex = frame.getInvoiceHeaderTable().getSelectedRow();
        System.out.println("Invoice Selected : " + selectedInvIndex);
        if (selectedInvIndex != -1){
            
        InvoiceHeader selectedInvoice = frame.getInvoicesArray().get(selectedInvIndex);
        ArrayList<InvoiceLine> lines = selectedInvoice.getLines();
        InvoiceLineTableModel lineTableModel = new InvoiceLineTableModel(lines);
        frame.setLinesArray(lines);
        frame.getInvoiceLineTable().setModel(lineTableModel);
        frame.getCustomerNameLbl().setText(selectedInvoice.getCustomer());
        frame.getInvoiceNoLbl().setText("" +selectedInvoice.getNo());
        frame.getInvoiceTotalLbl().setText(""+ selectedInvoice.getInvoiceTotal());
        frame.getInvoiceDateLbl().setText( GeneratorFrame.dateFormat.format(selectedInvoice.getInvDate()));
        }
    }
 
    
}
