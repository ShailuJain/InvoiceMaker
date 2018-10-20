/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Shailesh
 */
public class InvoiceTable {
    public static void insert(String customerName,int totalAmount){
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO invoice(tocustomer,totalamount) VALUES(?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, totalAmount);
            preparedStatement.execute();
        } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Some Error in insert InvoiceTable: " + e); 
        }
    }
    private static final Connection conn = MySqlConnect.connectDB();
}
