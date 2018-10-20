package database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author jains
 */
public class CustomerTable {
    
    public static int getCustomerId(String GSTNo){
        try {
            int cId = 0;
            ResultSet rs = DatabaseManagement.select("customer", "WHERE gstno = '" + GSTNo + "'", "customerid");
            if(rs.next())
                return rs.getInt("customerid");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getCustomerId of CustomerTable");
        }
        return -1;
    }
    public static String getCustomerName(int categoryId){
        try {
            ResultSet rs = DatabaseManagement.select("customer","WHERE categoryid = " + categoryId,"customername");
            if(rs.next())
                return rs.getString("customername");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getCustomerName of CustomerTable");
        }
        return null;
    }
    public static String getCustomerPhone(int categoryId){
        try {
            ResultSet rs = DatabaseManagement.select("customer","WHERE categoryid = " + categoryId,"phoneno");
            if(rs.next())
                return rs.getString("phoneno");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getCustomerPhone of CustomerTable");
        }
        return null;
    }
    public static String getGSTNo(int categoryId){
        try {
            ResultSet rs = DatabaseManagement.select("customer","WHERE categoryid = " + categoryId,"gstno");
            if(rs.next())
                return rs.getString("gstno");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getCustomerEmail of CustomerTable");
        }
        return null;
    }
    public static int getTotalCustomers(){
        try {
            ResultSet rs = DatabaseManagement.select("customer",null, (String) null);
            int customerCount = 0;
            rs.last();
            customerCount = rs.getRow();
            return customerCount;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in getTotalCustomers " + e);
        }
        return -1;
    }
    public static boolean isCustomerExist(String GSTNo){
        ResultSet rs = DatabaseManagement.select("customer","WHERE gstno = '" + GSTNo + "'",null);
        if(rs!=null){
            try {
                if(rs.next())
                    return true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error in isCustomerExist " + e);
            }
        }
        return false;
    }
    public static void insert(String value1,String value2,String value3){
        try {
            String sql = "INSERT into customer(customername,address,gstno) VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,value1);
            preparedStatement.setString(2,value2);
            preparedStatement.setString(3,value3);
            preparedStatement.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in insert of CustomerTable");
        }
    }
    public static void update(int customerId,String value1,String value2, String value3){
        try {
            String sql = "UPDATE customer SET customername = ?, address = ?, gstno = ? WHERE customerid = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,value1);
            preparedStatement.setString(2,value2);
            preparedStatement.setString(3,value3);
            preparedStatement.setInt(4,customerId);
            preparedStatement.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in update of CustomerTable");
        }
    }
    public static void delete(int customerId){
        try {
            String sql = "DELETE FROM customer WHERE customerid = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,customerId);
            preparedStatement.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in delete of CustomerTable");
        }
    }
    private static Connection conn = MySqlConnect.connectDB();
}
