/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import database.DatabaseManagement;
import static invoice.OffsetConstants.CONSIGNEEDETAILSY;
import static invoice.OffsetConstants.CUSTOMERDETAILSY;
import static invoice.OffsetConstants.GSTDETAILSX;
import static invoice.OffsetConstants.GSTDETAILSY;
import static invoice.OffsetConstants.INVOICENOX;
import static invoice.OffsetConstants.INVOICENOY;
import static invoice.OffsetConstants.PRODUCTNAMEY;
import static invoice.OffsetConstants.STARTX;
import static invoice.OffsetConstants.TOTALAMOUNTX;
import static invoice.OffsetConstants.TOTALAMOUNTY;
import static invoice.OffsetConstants.VEHICLENOX;
import static invoice.OffsetConstants.VEHICLENOY;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import static invoice.OffsetConstants.TOTALAMOUNTWORDSY;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jains
 */
public class Invoice {

    private final double IGST;

    public Invoice(String path, double CGST, double SGST, double IGST) {
        this.path = path;
        this.CGST = CGST;
        this.SGST = SGST;
        this.IGST = IGST;
        new File(path).mkdirs();
        init();
    }

    //Initialization
    private void init() {
        try {
            invoiceNo = "MMC-" + getNextInvoiceNo();
            file = new File(this.path + "\\Invoice" + invoiceNo + ".pdf");
            InputStream is = getClass().getResourceAsStream("/resources/InvoiceTemplate.pdf");
            File f = File.createTempFile("ivc", "invoice");
            FileOutputStream fos = new FileOutputStream(f);
            int i = 0;
            while ((i = is.read()) != -1) {
                fos.write(i);
            }
            loadPDFFile(f);
            CGSTAmount = 0;
            SGSTAmount = 0;
            totalPriceExcludingGST = 0;
            totalTax = 0;
        } catch (IOException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public void writeInvoiceDetails(InvoiceDetails id) {
        if (!this.invoiceNo.equals(id.invoiceNo)) {
            this.invoiceNo = id.invoiceNo;
            this.file = new File(this.path + "\\Invoice" + invoiceNo + ".pdf");
        }
        try {
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 11);
            contentStream.setLeading(28);

            contentStream.beginText();

            contentStream.newLineAtOffset(INVOICENOX, INVOICENOY);
            contentStream.showText(id.invoiceNo);

            contentStream.newLine();
            contentStream.showText(id.modeOfTransport);

            contentStream.endText();

            contentStream.beginText();

            contentStream.newLineAtOffset(INVOICENOX + 140, INVOICENOY);
            contentStream.showText(id.invoiceDate);

            contentStream.newLine();
            contentStream.showText(id.termOfPay);

            contentStream.endText();
        } catch (Exception e) {
            System.out.println(e + " in writeInvoiceDetails");
        }
    }

    public void writeVehicleDetails(String vehicleNo) {
        try {
            contentStream.beginText();

            contentStream.newLineAtOffset(VEHICLENOX, VEHICLENOY);
            contentStream.showText(vehicleNo);

            contentStream.endText();
        } catch (IOException ex) {
            System.out.println(vehicleNo + "in writeVehicleDetails");
        }
    }

    public void writeProductDetails(List<Product> products) {
        int srNo = 0;
        try {
            this.products = products;
            contentStream.setLeading(15);
            for (Product p : products) {
                contentStream.beginText();
                contentStream.newLineAtOffset(STARTX, PRODUCTNAMEY);
                srNo++;
                if (srNo > 1) {
                    for (int i = 0; i < srNo - 1; i++) {
                        contentStream.newLine();
                    }
                }
                contentStream.showText(srNo + "");

                contentStream.newLineAtOffset(25, 0);
                contentStream.showText(p.productName);

                contentStream.newLineAtOffset(245, 0);
                contentStream.showText(p.hsn);

                contentStream.newLineAtOffset(65, 0);
                contentStream.showText(p.quantity + "");

                contentStream.newLineAtOffset(60, 0);
                contentStream.showText(p.rate + "");

                contentStream.newLineAtOffset(55, 0);
                contentStream.showText(p.per);

                contentStream.newLineAtOffset(40, 0);
                double amountExcludingGST = calculateAndGetTotalAmount(p.rate, p.quantity);
                totalPriceExcludingGST += amountExcludingGST;
                contentStream.showText(roundUpto2Dec(amountExcludingGST) + " Rs");

                contentStream.endText();
            }
            totalTax = calculateAndGetTax(totalPriceExcludingGST, CGST) + calculateAndGetTax(totalPriceExcludingGST, SGST) + calculateAndGetTax(totalPriceExcludingGST, IGST);
            writeGSTDetails();
            writeTotalAmount(getTotalPriceIncludingGST());
            writeGSTBreakUp();
        } catch (IOException ex) {
            System.out.println("invoice.Invoice.writeProductDetails()" + ex);
        }
    }

    private String roundUpto2Dec(double val) {
        return String.format("%.2f", val);
    }

    private void writeGSTDetails() {
        try {
            contentStream.beginText();

            contentStream.newLineAtOffset(GSTDETAILSX, GSTDETAILSY);
            contentStream.showText(roundUpto2Dec(totalPriceExcludingGST) + " Rs");

            contentStream.newLineAtOffset(0, -18);
            contentStream.showText(roundUpto2Dec(calculateAndGetTax(totalPriceExcludingGST, CGST)));

            contentStream.newLineAtOffset(0, -18);
            contentStream.showText(roundUpto2Dec(calculateAndGetTax(totalPriceExcludingGST, SGST)));
            
            contentStream.newLineAtOffset(0, -18);
            contentStream.showText(roundUpto2Dec(calculateAndGetTax(totalPriceExcludingGST, IGST)) + "");

            contentStream.endText();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Some Error Occured" + e);
        }
    }
    
    public void writeConsigneeDetails(String name, String address, String GSTNo) {
        try {
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 11);
            contentStream.setLeading(12);
            contentStream.beginText();

            contentStream.newLineAtOffset(STARTX, CONSIGNEEDETAILSY);
            contentStream.showText(name);
            contentStream.newLine();
            address = address.replaceAll("\t", " ");
            String breakAddress = address;
            int breakPoint = 0;
            while (true) {

                int subStringLength = address.substring(breakPoint, address.length()).length();
                breakAddress = subStringLength <= 100 ? address.substring(breakPoint, address.length()) : address.substring(breakPoint, breakPoint + 100);
                contentStream.showText(breakAddress);
                contentStream.newLine();
                breakPoint += 100;
                if (breakAddress.length() < 100) {
                    break;
                }
            }
            contentStream.newLineAtOffset(0, -12);
            contentStream.showText("GST NO: " + GSTNo);

            contentStream.endText();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "ex" + ex);
        }

    }

    public void writeCustomerDetails(String name, String address, String GSTNo) {
        try {
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
            contentStream.setLeading(12);
            contentStream.beginText();

            contentStream.newLineAtOffset(STARTX, CUSTOMERDETAILSY);
            contentStream.showText(name);
            contentStream.newLine();
            address = address.replaceAll("\t", " ");
            String breakAddress = address;
            int breakPoint = 0;
            while (true) {
                int subStringLength = address.substring(breakPoint, address.length()).length();
                breakAddress = subStringLength <= 100 ? address.substring(breakPoint, address.length()) : address.substring(breakPoint, breakPoint + 100);
                contentStream.showText(breakAddress);
                contentStream.newLine();
                breakPoint += 100;
                if (breakAddress.length() < 100) {
                    break;
                }
            }
            contentStream.newLineAtOffset(0, -6);
            contentStream.showText("GST NO: " + GSTNo);

            contentStream.endText();
        } catch (IOException ex) {
            System.out.println("ex" + ex);
        }

    }

    private void writeTotalAmount(double totalAmount) {
        try {
            contentStream.beginText();
            contentStream.newLineAtOffset(TOTALAMOUNTX, TOTALAMOUNTY);
            contentStream.showText(roundUpto2Dec(totalAmount) + " Rs");
            contentStream.endText();
            writeTotalAmountInWords(totalAmount);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "writeTotalAmount" + ex);
        }
    }

    private void writeTotalAmountInWords(double price) {
        try {
            String priceInWords = NumberToWord.convert(roundUpto2Dec(price));
            contentStream.beginText();
            contentStream.newLineAtOffset(STARTX, 280);
            contentStream.showText(priceInWords);
            contentStream.endText();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void writeGSTBreakUp() {
        try {

            contentStream.beginText();

            contentStream.newLineAtOffset(STARTX, TOTALAMOUNTWORDSY);
            contentStream.showText(roundUpto2Dec(totalPriceExcludingGST) + " Rs");

            contentStream.newLineAtOffset(110, 0);
            contentStream.showText(CGST + "%");

            CGSTAmount = calculateAndGetTax(totalPriceExcludingGST, CGST);
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText(roundUpto2Dec(CGSTAmount) + " Rs");

            contentStream.newLineAtOffset(70, 0);
            contentStream.showText(SGST + "%");

            SGSTAmount = calculateAndGetTax(totalPriceExcludingGST, SGST);
            contentStream.newLineAtOffset(42, 0);
            contentStream.showText(roundUpto2Dec(SGSTAmount) + " Rs");

            contentStream.newLineAtOffset(80, 0);
            contentStream.showText(IGST + "%");

            IGSTAmount = calculateAndGetTax(totalPriceExcludingGST, IGST);
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText(roundUpto2Dec(IGSTAmount) + " Rs");

            contentStream.newLineAtOffset(85, 0);
            contentStream.showText(roundUpto2Dec(CGSTAmount + SGSTAmount + IGSTAmount) + " Rs");

            contentStream.endText();

            writeTotalTaxInWords((CGSTAmount + SGSTAmount + IGSTAmount));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void writeTotalTaxInWords(double taxValue) {
        try {
            String taxInWords = NumberToWord.convert(roundUpto2Dec(taxValue));
            contentStream.beginText();
            contentStream.newLineAtOffset(STARTX, 190);
            contentStream.showText(taxInWords);
            contentStream.endText();
        } catch (Exception e) {
        }
    }

    public double calculateAndGetTotalAmount(double rate, double quantity) {
        return (rate * quantity);
    }

    public void setPath(String path) {
        this.path = path;
    }

    // Getters and Setters
    public double getCGSTAmount() {
        return CGSTAmount;
    }

    public double getSGSTAmount() {
        return SGSTAmount;
    }

    public double getTotalPriceExcludingGST() {
        return totalPriceExcludingGST;
    }

    public double getTotalPriceIncludingGST() {
        return totalPriceExcludingGST + totalTax;
    }

    private double calculateAndGetTax(double totalAmount, double percent) {
        double tax = 0;
        tax = (totalAmount * percent) / 100;
        return tax;
    }

    @Override
    public String toString() {
        return "Invoice{" + "path=" + path + ", invoiceNo=" + invoiceNo + '}';
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public static int getNextInvoiceNo() {
        try {
            ResultSet rs = DatabaseManagement.select("SELECT * FROM invoice");
            if (rs.last()) {
                return rs.getInt("invoiceno");
            } else {
                return 0;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "in getNextInvoiceNo");
        }
        return -1;
    }

    //PDF Writing
    private void loadPDFFile(File file) {
        try {
            templatePDF = PDDocument.load(file);
            contentStream = new PDPageContentStream(templatePDF, templatePDF.getPage(0), PDPageContentStream.AppendMode.APPEND, false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e + " in loadPDFFile");
        }
    }

    public void saveAndClose() {
        try {
            contentStream.close();
            templatePDF.save(file);
            templatePDF.close();
        } catch (IOException e) {
            System.out.println(e + " in saveAndClose");
        }
    }

    private PDDocument templatePDF = null;
    private PDPageContentStream contentStream = null;
    private String path = null;
    private String invoiceNo = "MMC-";
    private List<Product> products = null;
    private File file = null;
    private double CGSTAmount, SGSTAmount, IGSTAmount;
    private double totalPriceExcludingGST;
    private double totalTax;
    private String hsn = null;
    private final double CGST, SGST;
}
