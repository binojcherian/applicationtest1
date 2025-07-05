/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messages
{
    Connection con=null;
    public String getMessageForCentre() throws SQLException
    {
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select MessageText from Messages where ForCentre=1");
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

    public String getMessageForStudent() throws SQLException
    {
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select MessageText from Messages where ForStudent=1");
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

    public String getMessageForStudentDocument() throws SQLException
    {
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select MessageText from Messages where ForStudent=0 and ForCentre=0");
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }


}
