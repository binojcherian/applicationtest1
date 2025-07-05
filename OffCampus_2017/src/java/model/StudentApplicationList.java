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


public class StudentApplicationList
{
    private ArrayList<String> StudentId=new ArrayList<String>();
    private ArrayList<String> StudentName=new ArrayList<String>();
    private ArrayList<String> ApplicationNo=new ArrayList<String>();
    private ArrayList<String>  PRN=new ArrayList<String>();
    AdmissionYear Year=new AdmissionYear();
public StudentApplicationList(int  CentreId,int BranchId) throws SQLException
    {
         Connection con=null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT SP.StudentId,SP.StudentName,SL.QuestionId FROM StudentLoginMaster SL,StudentPersonal SP where SP.StudentId=SL.StudentId and SP.IsFinalSubmit=1 and SP.JoiningYear=? and SP.CollegeId=? and SP.BranchId=? order by SL.QuestionId");
             ps.setInt(1, Year.getCurrentAdmissionYear());
            ps.setInt(2, CentreId);
            ps.setInt(3, BranchId);
            ResultSet Data = ps.executeQuery();
            while (Data.next()) {
                StudentId.add(Data.getString(1));
                StudentName.add(Data.getString(2));
                ApplicationNo.add(Data.getString(3));
                //PRN.add(Data.getString(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(StudentApplicationList.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean  contains(String SId)
    {
        return StudentId.contains(SId);
    }

}
