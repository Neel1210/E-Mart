/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ProductsPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author 91747
 */
public class OrderDAO {
    public static String getNextOrderId()throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select max(order_id) from orders");
        rs.next();
        String ordId = rs.getString(1);
        if(ordId==null)
        {
            return "O-101";
        }
        int ordno = Integer.parseInt(ordId.substring(2));
        ordno = ordno + 1;
        return "O-"+ordno;
    }
    public static boolean addOrder(ArrayList<ProductsPojo> al,String ordId)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into orders values(?,?,?,?)");
        int count = 0;
        for(ProductsPojo p : al)
        {
            ps.setString(1, ordId);
            ps.setString(2, p.getProductId());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, UserProfile.getUserid());
            count = count+ps.executeUpdate();
        }
        return count==al.size();
    }
    public static ArrayList<String> getAllOrderId()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select distinct order_id from orders");
        ArrayList<String> orderid = new ArrayList<String>();
        while (rs.next())
        {
            orderid.add(rs.getString(1));
        }
        return orderid;
    }
    
    public static ArrayList<ProductsPojo> getOrderDetails(String s)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from orders where order_id = ?");
        ps.setString(1, s);
        ResultSet rs = ps.executeQuery();
        ArrayList<ProductsPojo> orders = new ArrayList<ProductsPojo>();
        while (rs.next())
        {
            ProductsPojo p = ProductsDAO.getProductDetail(rs.getString(2));
            p.setQuantity(rs.getInt(3));
            orders.add(p);
        }
        return orders;
    }
}
