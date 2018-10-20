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
public class Product{

       String productName;
       String hsn;
       double quantity;
       double rate;
       String per;

        public Product(String productName,String hsn,double quantity,double rate,String per) {
            this.productName = productName;
            this.hsn = hsn;
            this.quantity = quantity;
            this.rate = rate;
            this.per = per;
        }
    }