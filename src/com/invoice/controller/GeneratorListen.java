
package com.invoice.controller;

import com.invoice.model.InvoiceHeader;
import com.invoice.model.InvoiceHeaderTableModel;
import com.invoice.model.InvoiceLine;
import com.invoice.model.InvoiceLineTableModel;
import com.invoice.view.GeneratorFrame;
import com.invoice.view.InvoiceHeaderDialog;
import com.invoice.view.InvoiceLineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



public class GeneratorListen implements ActionListener{
    private GeneratorFrame frame;
    private InvoiceHeaderDialog headerDialog;
    private InvoiceLineDialog lineDialog;
    
    public GeneratorListen(GeneratorFrame frame){
        this.frame = frame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Load Files":
                loadFiles();
                break;
            
            case "Save Files":
                saveFiles();
                break;
                
            case "Create New Invoice":
                createInvoice();
                break;
            
            case "Delete Invoice":
                deleteInvoice();
                break;
             
            case "Create New Line":
                createLine();
                break;
                
            case "Delete Line":
                deleteLine();
                break;
                
            case "newInvoiceOKBtn":
                NewInvoiceOkBtn();
                break;
                
            case "newInvoiceCancelBtn":
                cancelNewInvoiceBtn();
                break;
            case "newLineCancelBtn" :
                cancelNewLineBtn();
                break;
                
            case "newLineOKBtn" :
                NewLineOkBtn();
                break;
        }
    }

    private void loadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        
        try {
        
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            File headerFile = fileChooser.getSelectedFile();
            Path headerPath =Paths.get(headerFile.getAbsolutePath());
            List<String> headerLines = Files.readAllLines(headerPath);
             
            ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
            for (String headerLine : headerLines){
                    String [] arr = headerLine.split(",");
                    String str1 = arr[0];
                    String str2 = arr[1];
                    String str3 = arr[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = GeneratorFrame.dateFormat.parse(str2);
                    InvoiceHeader header = new InvoiceHeader(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
            }
            frame.setInvoicesArray(invoiceHeaders);
            result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File lineFile = fileChooser.getSelectedFile();
                Path linePath = Paths.get(lineFile.getAbsolutePath());
                List<String> lineLines = Files.readAllLines(linePath);
                ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
                for (String lineLine:lineLines){
                    String[] arr = lineLine.split(",");
                    String str1 = arr[0];  // invoice no
                    String str2 = arr[1];  // invoice item
                    String str3 = arr[2];  // item price
                    String str4 = arr[3];  // item count
                    int invCode = Integer.parseInt(str1);
                    double price = Double.parseDouble(str3);
                    int count = Integer.parseInt(str4);
                    InvoiceHeader inv = frame.getInvObject(invCode);
                    InvoiceLine line = new InvoiceLine(str2, price, count, inv);
                    inv.getLines().add(line);
                }
            }
            InvoiceHeaderTableModel headerTableModel = new InvoiceHeaderTableModel(invoiceHeaders);
            frame.setHeaderTableModel(headerTableModel);
            frame.getInvoiceHeaderTable().setModel(headerTableModel);
                       
        }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFiles() {
       ArrayList<InvoiceHeader> invoicesArr = frame.getInvoicesArray();
       JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showSaveDialog(frame);
       if (result == JFileChooser.APPROVE_OPTION){
           File hFile = fileChooser.getSelectedFile();
           FileWriter hFileWriter = new FileWriter(hFile);
           String hString = "";
           String lString = "";
           for (InvoiceHeader invoiceHeader : invoicesArr){
             hString += invoiceHeader.toString();
             hString += "\n";
             for (InvoiceLine invoiceLine :invoiceHeader.getLines() ){
                 lString += invoiceLine.toString();
                 lString += "\n";
             }
           }
           hString = hString.substring(0, hString.length()-1);
           lString = lString.substring(0, lString.length()-1);
           result = fileChooser.showSaveDialog(frame);
           File lineFile = fileChooser.getSelectedFile();
           FileWriter lFileWriter = new FileWriter(lineFile);
           hFileWriter.write(hString);
           lFileWriter.write(lString);
           hFileWriter.close();
           lFileWriter.close();
        }
       }catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
       
    }

    private void createInvoice() {
        headerDialog = new InvoiceHeaderDialog(frame);
        headerDialog.setVisible(true);
        
    }

    private void deleteInvoice() {
        
        int selectedInvIndex = frame.getInvoiceHeaderTable().getSelectedRow();
        if (selectedInvIndex != -1){
            frame.getInvoicesArray().remove(selectedInvIndex);
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getInvoiceLineTable().setModel(new InvoiceLineTableModel(null));
            frame.setLinesArray(null);
            frame.getCustomerNameLbl().setText("");
            frame.getInvoiceNoLbl().setText("");
            frame.getInvoiceTotalLbl().setText("");
            frame.getInvoiceDateLbl().setText("");
            
        }
        
    }

    private void createLine() {
        lineDialog = new InvoiceLineDialog(frame);
        lineDialog.setVisible(true);
        
    }

    private void deleteLine() {
     int lineIndex = frame.getInvoiceLineTable().getSelectedRow();
     int invoiceIndex = frame.getInvoiceHeaderTable().getSelectedRow();
     if (lineIndex != -1){
         frame.getLinesArray().remove(lineIndex);
         InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel)frame.getInvoiceLineTable().getModel();
         lineTableModel.fireTableDataChanged();
         frame.getInvoiceTotalLbl().setText("" +frame.getInvoicesArray().get(invoiceIndex).getInvoiceTotal());
         frame.getHeaderTableModel().fireTableDataChanged();
         frame.getInvoiceHeaderTable().setRowSelectionInterval(invoiceIndex, invoiceIndex);
     }
        
    }
    
    
    private void cancelNewInvoiceBtn() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
        
    }
    private void NewInvoiceOkBtn() {
        headerDialog.setVisible(false);
        
        String cstName = headerDialog.getCustNameField().getText();
        String dte =headerDialog.getInvDateField().getText();
        Date date = new Date();
        try {
            date = GeneratorFrame.dateFormat.parse(dte);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(frame,"parse date error , put today date","wrong date format", JOptionPane.ERROR_MESSAGE);
        }
        
        int invNum = 0;
        for (InvoiceHeader inv: frame.getInvoicesArray() ){
            if (inv.getNo()> invNum ) invNum = inv.getNo();
        }
        invNum++;
        InvoiceHeader invhead = new InvoiceHeader(invNum, cstName, date);
        frame.getInvoicesArray().add(invhead);
        frame.getHeaderTableModel().fireTableDataChanged();
        
        
        headerDialog.dispose();
        headerDialog = null;
        
    }

    private void cancelNewLineBtn() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void NewLineOkBtn() {
        lineDialog.setVisible(false);
        
        String name = lineDialog.getItemNameField().getText();
        String s1 = lineDialog.getItemCountField().getText();
        String s2 = lineDialog.getItemPriceField().getText();
        int count = 1;
        double price = 1;
        try {
            count = Integer.parseInt(s1);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Wrong Number","Invalid Format", JOptionPane.ERROR_MESSAGE);
        }
        try {
            price = Double.parseDouble(s2);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Wrong Number","Invalid Format", JOptionPane.ERROR_MESSAGE);
        }
        int selectedHeader = frame.getInvoiceHeaderTable().getSelectedRow();
        if (selectedHeader != -1){
            InvoiceHeader invHeader = frame.getInvoicesArray().get(selectedHeader);
            InvoiceLine line = new InvoiceLine(name, price, count, invHeader);
            frame.getLinesArray().add(line);
            InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel)frame.getInvoiceLineTable().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
        }
        frame.getInvoiceHeaderTable().setRowSelectionInterval(selectedHeader,selectedHeader);
        
        lineDialog.dispose();
        lineDialog = null;
    }

    
    
}
