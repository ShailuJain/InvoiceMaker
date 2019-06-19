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
public class ConsigneeTable {
    private static final String TABLE_NAME = "consignees";
    public static int getConsigneeId(String GSTNo){
        try {
            int cId = 0;
            ResultSet rs = DatabaseManagement.select(TABLE_NAME, "WHERE consignee_gstno = '" + GSTNo + "'", "consignee_id");
            if(rs.next())
                return rs.getInt("consignee_id");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getConsigneeId of ConsigneeTable");
        }
        return -1;
    }
    public static String getConsigneeName(int consigneeId){
        try {
            ResultSet rs = DatabaseManagement.select(TABLE_NAME,"WHERE consignee_id = " + consigneeId,"consignee_name");
            if(rs.next())
                return rs.getString("consignee_name");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getConsigneeName of ConsigneeTable");
        }
        return null;
    }
    public static String getGSTNo(int consigneeId){
        try {
            ResultSet rs = DatabaseManagement.select(TABLE_NAME,"WHERE consignee_id = " + consigneeId,"consignee_gstno");
            if(rs.next())
                return rs.getString("consignee_gstno");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getConsigneeEmail of ConsigneeTable");
        }
        return null;
    }
    public static int getTotalConsignees(){
        try {
            ResultSet rs = DatabaseManagement.select(TABLE_NAME,null, (String) null);
            int customerCount = 0;
            rs.last();
            customerCount = rs.getRow();
            return customerCount;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in getTotalConsignees " + e);
        }
        return -1;
    }
    public static boolean isConsigneeExist(String GSTNo){
        ResultSet rs = DatabaseManagement.select(TABLE_NAME,"WHERE consignee_gstno = '" + GSTNo + "'",null);
        if(rs!=null){
            try {
                if(rs.next())
                    return true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error in isConsigneeExist " + e);
            }
        }
        return false;
    }
    public static void insert(String value1,String value2,String value3){
        try {
            String sql = "INSERT into " + TABLE_NAME + "(consignee_name,consignee_address,consignee_gstno) VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,value1);
            preparedStatement.setString(2,value2);
            preparedStatement.setString(3,value3);
            preparedStatement.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in insert of ConsigneeTable" + e.getMessage());
        }
    }
    public static void update(int consigneeId,String value1,String value2, String value3){
        try {
            String sql = "UPDATE " + TABLE_NAME + " SET consignee_name = ?, consignee_address = ?, consignee_gstno = ? WHERE consignee_id = ?";
            System.out.print(consigneeId);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,value1);
            preparedStatement.setString(2,value2);
            preparedStatement.setString(3,value3);
            preparedStatement.setInt(4,consigneeId);
            preparedStatement.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in update of ConsigneeTable");
        }
    }
    public static void delete(int customerId){
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE consignee_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,customerId);
            preparedStatement.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in delete of ConsigneeTable");
        }
    }
    private static Connection conn = MySqlConnect.connectDB();
}
