/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StudentListPRN
{
    private ArrayList<String> StudentId=new ArrayList<String>();
    private ArrayList<String> StudentName=new ArrayList<String>();
    private ArrayList<String> ApplicationNo=new ArrayList<String>();
    private ArrayList<String>  PRN=new ArrayList<String>();
   public StudentListPRN(int  CentreId,int BranchId,int Year) throws SQLException
    {
         Connection con=null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("select l.StudentId,StudentName,QuestionId,PRN from StudentPersonal p,StudentLoginMaster l where Collegeid=? and p.AttendingYear=? and Branchid=? and PRN is not null and p.StudentId=l.StudentId order by PRN");
            ps.setInt(1, CentreId);
            ps.setInt(2, Year);
            ps.setInt(3, BranchId);
            ResultSet Data = ps.executeQuery();
            while (Data.next()) {
                StudentId.add(Data.getString(1));
                StudentName.add(Data.getString(2));
                ApplicationNo.add(Data.getString(3));
                PRN.add(Data.getString(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(StudentListPRN.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
    }
    public int getCount()
    {
         return StudentId.size();
    }
    public String getStudentId(int index)
    {
         return StudentId.get(index);
    }
    public String getStudentName(int index)
    {
         return StudentName.get(index);
    }
    public String getApplication(int index)
    {
        return ApplicationNo.get(index);
    }
      public String getPRN(int index)
    {
        return PRN.get(index);
    }
public boolean  contains(String SId)
    {
        return StudentId.contains(SId);
    }

}

