/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

/**
 *
 * @author Shailesh
 */
public class InvoiceDetails {

    public final String invoiceNo;
    public final String invoiceDate;
    public final String termOfPay;
    public final String modeOfTransport;

    public InvoiceDetails(String invoiceNo, String invoiceDate, String termOfPay, String modeOfTransport) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.termOfPay = termOfPay;
        this.modeOfTransport = modeOfTransport;
    }
}
