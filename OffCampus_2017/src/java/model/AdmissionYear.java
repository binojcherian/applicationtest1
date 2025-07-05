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
import java.util.ArrayList;

public class AdmissionYear
{
    
public int getCurrentAdmissionYear() throws SQLException
{
    Connection con=null;
    try
    {
        con=new DBConnection().getConnection();
        PreparedStatement st=con.prepareStatement("select AdmissionYear from AdmissionDates order by AdmissionYear desc limit 1 ");
        ResultSet Rs=st.executeQuery();
        while(Rs.next())
        {
            return Rs.getInt(1);
        }
        return 0;
    }
    catch (SQLException ex) {
            if(con!=null)
                con.close();
             Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
            finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
}

public ArrayList<Integer> getAdmissionYears() throws SQLException
{
    ArrayList<Integer> Years=new ArrayList<Integer>();
    Connection con=null;
    try
    {
        con=new DBConnection().getConnection();
        PreparedStatement st=con.prepareStatement("select AdmissionYear from AdmissionDates order by AdmissionYear desc limit 5 ");
        ResultSet Rs=st.executeQuery();
        while(Rs.next())
        {
            Years.add(Rs.getInt(1));
        }
        return Years;
    }
    catch (SQLException ex) {
            if(con!=null)
                con.close();
             Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsAdmissionDateOver() throws SQLException
{
    int CurYear=getCurrentAdmissionYear();
    Connection Con=null;
    try
    {
        Con=new DBConnection().getConnection();
        PreparedStatement ps=Con.prepareStatement("SELECT DATEDIFF(Reg_EndDate,SysDate()) from AdmissionDates  where Admissionyear=? ");
        ps.setInt(1, CurYear);
        ResultSet RS=ps.executeQuery();
        while (RS.next())
         {
           if(RS.getInt(1)<0)
            {
                return true;
            }
        }
        return false;
    }
    catch (SQLException ex)
    {
            if(Con!=null)
                Con.close();
             Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
            finally
        {
            if(Con!=null)
            {
                Con.close();
            }
        }

}

public String IsAdmissionDatesOverForApplicationNumber(String ApplicationNo) throws SQLException
{
    Connection con=null;
        try
        {
            int CurYear = getCurrentAdmissionYear();
            con=new DBConnection().getConnection();
            PreparedStatement ps=con.prepareStatement("select DATEDIFF(RegistrationEndDate,SysDate()) from ApplicationCenterMap  where "+ApplicationNo+" between App_From and App_To and Year=?");
            ps.setInt(1, CurYear);
        ResultSet RS=ps.executeQuery();
        while (RS.next())
         {
           if(RS.getInt(1)<0)
            {
                return "Can not create account, Last Date Over";
            }
        }
        return null;
    }
    catch (SQLException ex)
    {
            if(con!=null)
                con.close();
             Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
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

public String IsReAdmissionDatesOver() throws SQLException
{
    Connection con=null;
        try
        {
            int CurYear = getCurrentAdmissionYear();
            con=new DBConnection().getConnection();
            PreparedStatement ps=con.prepareStatement("select DATEDIFF(ReAdmissionEndDate,SysDate()) from AdmissionDates where  AdmissionYear=?");
            ps.setInt(1, CurYear);
        ResultSet RS=ps.executeQuery();
        while (RS.next())
         {
           if(RS.getInt(1)<0)
            {
                return "Re Admission last Date Over";
            }
        }
        return null;
    }
    catch (SQLException ex)
    {
            if(con!=null)
                con.close();
             Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
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

public int getStudentAttendingYear(int StudentId) throws SQLException
{
        Connection con=null;
        PreparedStatement st=null;
        try
        {
            con = new DBConnection().getConnection();
            st=con.prepareStatement("Select AttendingYear from StudentPersonal where StudentId=?");
            st.setInt(1,StudentId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

public int getStudentAdmissionYear(int StudentId) throws SQLException
{
        Connection con=null;
        PreparedStatement st=null;
        try
        {
            con = new DBConnection().getConnection();
            st=con.prepareStatement("Select JoiningYear from StudentPersonal where StudentId=?");
            st.setInt(1,StudentId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AdmissionYear.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
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
