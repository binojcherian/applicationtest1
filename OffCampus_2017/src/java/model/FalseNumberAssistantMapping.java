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
import java.io.IOException;
import java.util.ArrayList;

public class FalseNumberAssistantMapping
{
public boolean AssignFalseNoToAssistant(String Assistant,String From,String To,String Count) throws SQLException
{
    Connection con=null;

        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("INSERT INTO FalseNumberAssistantMapping(User_Asst,FalseNo_From,FalseNo_To,Count,EnteredDate) values(?,?,?,?,sysdate())");
            st.setString(1, Assistant);
            st.setString(2, From);
            st.setString(3, To);
            st.setString(4, Count);
            st.execute();
            return true;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(FalseNumberAssistantMapping.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
