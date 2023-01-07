/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ReceptionistPojo;
import emart.pojo.UserPojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 91747
 */
public class ReceptionistDAO {
    public static List<ReceptionistPojo> getAllReceptionists()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select users.empid,empname,userid,job,salary from users,employees where usertype='Receptionist' and users.empid = employees.empid");
        ArrayList<ReceptionistPojo> recpList = new ArrayList<>();
        while(rs.next())
        {
            ReceptionistPojo recp = new ReceptionistPojo();
            recp.setEmpid(rs.getString(1));
            recp.setEmpname(rs.getString(2));
            recp.setUserid(rs.getString(3));
            recp.setJob(rs.getString(4));
            recp.setSalary(rs.getDouble(5));
            recpList.add(recp);
        }
        return recpList;
    }
    public static Map<String,String> getAllRecpId() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select userid,username from users where usertype='Receptionist' order by userid");
        HashMap <String,String> receptionistList=new HashMap<>();
        while(rs.next())
        {
            String id=rs.getString(1);
            String name=rs.getString(2);
            receptionistList.put(id,name);
        }
        return receptionistList;
    }
    public static boolean updatePassword(String userid,String pwd)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update users set password=? where userid=?");
        ps.setString(1, pwd);
        ps.setString(2, userid);
        return ps.executeUpdate()==1;
    }
    public static List<String> getAllReceptionistUserId()throws SQLException
    {
        Connection con=DBConnection.getConnection();
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select userid from users where usertype='Receptionist' order by userid");
        List <String> receptionistList=new ArrayList<>();
        while(rs.next())
        {
            String id=rs.getString(1);
            receptionistList.add(id);
        }
        return receptionistList;
    }
    public static boolean deleteReceptionist(String userid)throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("delete from users where userid=?");
        ps.setString(1, userid);
        return ps.executeUpdate()==1;
        
    }
    public static boolean addReceptionist(UserPojo user)throws SQLException
    {
        Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("Insert into users values(?,?,?,?,?)");
        ps.setString(1,user.getUserid());
        ps.setString(2, user.getEmpid());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getUsertype());
        ps.setString(5, user.getUsername());
        int result=ps.executeUpdate();
        return result==1;
        
    }
    public static Map<String,String> getNonRegisteredReceptionists() throws SQLException
    {
        Connection con=DBConnection.getConnection();
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select empid,empname from employees where job='Receptionst' and empid not in (select empid from users where usertype='Receptionst')");
        HashMap <String,String> receptionistList=new HashMap<>();
        while(rs.next())
        {
            String id=rs.getString(1);
            String name=rs.getString(2);
            receptionistList.put(id,name);
        }
        return receptionistList;
    }
}
