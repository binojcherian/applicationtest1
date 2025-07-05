/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnection {

    public Connection getConnection() 
    {
        try
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
            } 
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
       // Connection con = DriverManager.getConnection("jdbc:mysql://220.226.4.16:3306/DEMS_db", "dems", "dems");
        //Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dems_result", "demsresult", "demsresult");
     // Connection con = DriverManager.getConnection("jdbc:mysql://10.33.1.40:3306/dems_result", "demsresult", "demsresult@)!)");
       // Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dems_db2017", "root", "");
      //Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEMS_db", "dbuserdems", "db_user_dems@)!)");
   //Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEMS_db", "dbuserdems", "db_user_dems@)!)");// 30062022
  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dems_db", "root", "123456");//04/07/2022
// Connection con = DriverManager.getConnection("jdbc:mysql://10.33.1.40/DEMS_db", "dbuserdemscamp","db_user_demscamp@)!@");
         //Connection con = DriverManager.getConnection("jdbc:mysql://10.33.1.55:3306/DEMS_db", "dems", "dems");
         //Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEMS_db","dbuserdems","db_user_dems@)!)");
            con.setAutoCommit(true);
            return con;
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
public static void main(String s[]) throws SQLException
{
   Connection con=  new DBConnection().getConnection();
   PreparedStatement p= con.prepareStatement("select * from StudentLoginMaster");
   ResultSet Data= p.executeQuery();
   while(Data.next())
   {
       System.out.print(Data.getString(1));
   }
}
}
