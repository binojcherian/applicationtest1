/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentSemesterTotal
{
   /* public void SaveStudentSemesterTotal() throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select m.StudentId,sum(m.ExternalMark+InternalMark) as Total,m.ExamId,b.CurrentYearSem from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId where StudentId not in( select distinct StudentId from StudentSubjectMark where (IsAbsent=1 or IsPassed=0 or IsMalPractice=1) and IsValid=1) group by m.StudentId");
            ResultSet Rs=st.executeQuery();

            while(Rs.next())
            {
                st=con.prepareStatement("INSERT INTO StudentPassDetails (StudentId,ExamId,YearOrSem,TotalMark,EnteredDate,LastModifiedDate) values(?,?,?,?,sysdate(),sysdate())");
                st.setInt(1, Rs.getInt("StudentId"));
                st.setString(2, Rs.getString("ExamId"));
                st.setInt(3, Rs.getInt("CurrentYearSem"));
                st.setInt(4, Rs.getInt("Total"));
                st.execute();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentSemesterTotal.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            con.close();
        }
    }
*/
   /*  public void SaveStudentSemesterTotal() throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select m.StudentId,sum(m.ExternalMark+m.InternalMark) as Total,m.ExamId,b.CurrentYearSem from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and m.ExamId=1 and b.CurrentYearSem=1 and b.Branchid=17 where m.StudentId not in( select distinct StudentId from StudentSubjectMark s inner join SubjectBranchMaster b1  on s.SubjectBranchId=b1.SubjectBranchId and s.ExamId=1 and b1.CurrentYearSem=1 and b1.Branchid=17 where (s.IsAbsent=1 or s.IsPassed=0 or s.IsMalPractice=1) and IsValid=1) group by m.StudentId");
            ResultSet Rs=st.executeQuery();

            while(Rs.next())
            {
                st=con.prepareStatement("INSERT INTO StudentPassDetails (StudentId,ExamId,YearOrSem,TotalMark,EnteredDate,LastModifiedDate) values(?,?,?,?,sysdate(),sysdate())");
                st.setInt(1, Rs.getInt("StudentId"));
                st.setString(2, Rs.getString("ExamId"));
                st.setInt(3, Rs.getInt("CurrentYearSem"));
                st.setInt(4, Rs.getInt("Total"));
                st.execute();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentSemesterTotal.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            con.close();
        }
    }*/
    public void SaveStudentSemesterTotal() throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select m.StudentId,sum(m.ExternalMark+m.InternalMark+m.ModerationExt+m.ClassModeration+m.PracticalMark+m.ModerationInt) as Total,"
                    + " (select max(ExamId) from ExamRegistrationMaster where StudentId =m.StudentId) as ExamId, b.CurrentYearSem,b.BranchId from StudentSubjectMark m "
                    + " inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and "
                    + " b.CurrentYearSem=1 and b.Branchid=26 "
                    + " where MarkStatus=1 and IsValid=1 and b.IsAddedWithTotal=1 "
                    + " and m.StudentId not in( select distinct StudentId  from StudentSubjectMark s "
                    + " inner join SubjectBranchMaster b1  on s.SubjectBranchId=b1.SubjectBranchId "
                    + " and b1.CurrentYearSem=2 and b1.BranchId=21 and b1.IsAddedWithTotal=1 "
                    + " where (s.IsAbsent=1 or s.IsPassed=0 or s.IsMalPractice=1 ) and MarkStatus=1 and IsValid=1) "
                    + " group by m.StudentId");
            ResultSet Rs=st.executeQuery();

            while(Rs.next())
            {
                st=con.prepareStatement("INSERT INTO StudentPassDetails (StudentId,ExamId,YearOrSem,TotalMark,EnteredDate,LastModifiedDate,BranchId) values(?,?,?,?,sysdate(),sysdate(),?)");
                st.setInt(1, Rs.getInt("StudentId"));
                st.setString(2, Rs.getString("ExamId"));
                st.setInt(3, Rs.getInt("CurrentYearSem"));
                st.setInt(4, Rs.getInt("Total"));
                st.setInt(5, Rs.getInt("BranchId"));
                st.execute();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentSemesterTotal.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            con.close();
        }
    }
     public void SaveStudentSemesterTotal_1(int b,String yr) throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select m.StudentId,sum(m.ExternalMark+m.InternalMark+m.ModerationExt+m.ClassModeration+m.PracticalMark+m.ModerationInt) as Total,"
                    + " (select max(ExamId) from ExamRegistrationMaster e1 "
                    + " inner join SubjectBranchMaster b2 on e1.SubjectBranchId=b2.SubjectBranchId"
                    + " where StudentId =m.StudentId and b2.CurrentYearSem="+yr+" and b2.BranchId="+b+" ) as ExamId, b.CurrentYearSem,b.BranchId from StudentSubjectMark m "
                    + " inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and "
                    + " b.CurrentYearSem="+yr+" and b.Branchid="+b+" "
                    + " where MarkStatus=1 and IsValid=1 and b.IsAddedWithTotal=1 "
                    + " and m.StudentId not in( select distinct StudentId  from StudentSubjectMark s "
                    + " inner join SubjectBranchMaster b1  on s.SubjectBranchId=b1.SubjectBranchId "
                    + " and b1.CurrentYearSem="+yr+" and b1.BranchId="+b+" and b1.IsAddedWithTotal=1 "
                    + " where (s.IsAbsent=1 or s.IsPassed=0 or s.IsMalPractice=1 ) and MarkStatus=1 and IsValid=1) "
                    + " group by m.StudentId");
            ResultSet Rs=st.executeQuery();

            while(Rs.next())
            {
                st=con.prepareStatement("INSERT INTO StudentPassDetails (StudentId,ExamId,YearOrSem,TotalMark,EnteredDate,LastModifiedDate,BranchId) values(?,?,?,?,sysdate(),sysdate(),?)");
                st.setInt(1, Rs.getInt("StudentId"));
                st.setString(2, Rs.getString("ExamId"));
                st.setInt(3, Rs.getInt("CurrentYearSem"));
                st.setInt(4, Rs.getInt("Total"));
                st.setInt(5, Rs.getInt("BranchId"));
                st.execute();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentSemesterTotal.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            con.close();
        }
    }
     public int  DeleteStudentSemesterTotal() throws SQLException
    {
        int s=0;
        Connection con=null;
        DBConnection dbobj=new DBConnection();
        try
        {
             con=dbobj.getConnection();
             PreparedStatement st=con.prepareStatement("delete from studentpassdetails");
             st.execute();
            /*Statement st=con.createStatement();
           s=st.executeUpdate("delete from studentpassdetails"); 
           System.out.println("s: "+s);*/
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentSemesterTotal.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            con.close();
        }
        return s;
    }
     
    
    public static void main(String[] args) throws Exception
    {
        //StudentSemesterTotal SemTotal=new StudentSemesterTotal();
      //  SemTotal.SaveStudentSemesterTotal_1(6,6);
    }
}
