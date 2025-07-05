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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.*;
public class Subjects
{
public ArrayList<Subject> getSubjectsForBranch(String BranchId,String Semester) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT s.SubjectId,s.Subjectname,s.SubjectCode,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId,TotalMark,PassMark FROM SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=? and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=?) and CurrentYearSem=?  group by s.SubjectId  order by sb.CurrentYearSem,sb.PartNo,sb.PaperNo");
            st.setString(1, BranchId);
            st.setString(2, BranchId);
            st.setString(3, Semester);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectBranchId=rs.getString(7);
                sub.SubjectCode=rs.getString(3);
                sub.SubjectName=rs.getString(2);
                sub.MaxMark=rs.getString("TotalMark");
                sub.MinMark=rs.getString("PassMark");
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<Subject> getSubjectsForBranchMBA(String BranchId,String Semester) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT s.SubjectId,s.Subjectname,s.SubjectCode,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId,TotalMark,PassMark FROM SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=?  and CurrentYearSem=?    order by sb.AcademicYear,sb.CurrentYearSem,sb.PartNo,sb.PaperNo");
            st.setString(1, BranchId);
          
            st.setString(2, Semester);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectBranchId=rs.getString(7);
                sub.SubjectCode=rs.getString(3);
                sub.SubjectName=rs.getString(2);
                sub.MaxMark=rs.getString("TotalMark");
                sub.MinMark=rs.getString("PassMark");
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<Subject> getSubjectsForBranch_MBASubjectwise(String BranchId,String Semester) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT s.SubjectId,s.Subjectname,s.SubjectCode,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId,TotalMark,PassMark FROM SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=?  and CurrentYearSem=?  group by s.SubjectId  order by sb.AcademicYear,sb.CurrentYearSem,sb.PartNo,sb.PaperNo");
            st.setString(1, BranchId);
          
            st.setString(2, Semester);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectBranchId=rs.getString(7);
                sub.SubjectId=rs.getString(1);
                sub.SubjectCode=rs.getString(3);
                sub.SubjectName=rs.getString(2);
                sub.MaxMark=rs.getString("TotalMark");
                sub.MinMark=rs.getString("PassMark");
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<Subject> getSubjectsForBranch(String BranchId,String Semester,String AcademicYear) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        { //Query Changed by Yadu 10-09-2012
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT s.SubjectId,s.Subjectname,s.SubjectCode,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId,TotalMark,PassMark FROM SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=? and AcademicYear=? and CurrentYearSem=? group by s.SubjectId order by sb.CurrentYearSem,sb.PartNo,sb.PaperNo");
            st.setString(1, BranchId);
            st.setString(2, AcademicYear);
            st.setString(3, Semester);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectId=rs.getString(1);
                sub.SubjectBranchId=rs.getString(7);
                sub.SubjectCode=rs.getString(3);
                sub.SubjectName=rs.getString(2);
                sub.MaxMark=rs.getString("TotalMark");
                sub.MinMark=rs.getString("PassMark");
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<Subject> getAllSubjectsForBranch(String BranchId) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT s.SubjectId,s.Subjectname,s.SubjectCode,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId FROM SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=? and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=?)  order by sb.CurrentYearSem,sb.PaperNo");
            st.setString(1, BranchId);
            st.setString(2, BranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectBranchId=rs.getString(7);
                sub.SubjectCode=rs.getString(3);
                sub.SubjectName=rs.getString(2);
                sub.YearSem=rs.getString(6);
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<Subject> getAllSubjectsForNominalRoll(String BranchId,String Semester,String SubBranch) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
     PreparedStatement st=null;
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            if(SubBranch!=null && (!SubBranch.equals("-1")) && (!SubBranch.isEmpty()))
            {
                st = con.prepareStatement("SELECT s.SubjectId,s.SubjectName,su.SubBranchId,su.DisplayName,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId,sb.AcademicYear FROM  SubjectBranchMaster sb,SubjectMaster s,SubBranchMaster su,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.SubBranchId=su.SubBranchId and sb.BranchId=? and CurrentYearSem=? and su.SubBranchId=? order by sb.AcademicYear,sb.PartNo, sb.PaperNo");
                st.setString(1, BranchId);
                st.setString(2, Semester);
                st.setString(3, SubBranch);
            }
            else
            {
                st = con.prepareStatement("SELECT s.SubjectId,s.SubjectName,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId,sb.AcademicYear FROM  SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=? and CurrentYearSem=? order by sb.AcademicYear,sb.PartNo, sb.PaperNo");
                st.setString(1, BranchId);
                st.setString(2, Semester);
            }
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectBranchId=rs.getString("SubjectBranchId");
                sub.SubjectName=rs.getString("SubjectName");
                sub.semOryear=rs.getInt("SemorYear");
                sub.YearSem=rs.getString("CurrentYearSem");
                sub.AcademicYear=rs.getInt("AcademicYear");
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<Subject> getSubjectsForSubBranch(String BranchId,String SubBranchId,String Semester,String AcdemicYear) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT bm.SubjectBranchId,sb.SubBranchId,sb.BranchId,sb.StartingSem,sb.DisplayName, s.SubjectId,s.SubjectName FROM SubjectBranchMaster bm inner join SubjectMaster s on s.SUbjectId=bm.SubjectId inner join SubBranchMaster sb on sb.SubBranchId=bm.SubBranchId  and sb.SubBranchId= ? inner join BranchMaster b on b.BranchId=sb.BranchId and sb.BranchId=? and bm.CurrentYearSem=? and bm.AcademicYear=?");
            st.setString(1, SubBranchId);
            st.setString(2, BranchId);
            st.setString(3, Semester);
            st.setString(4,AcdemicYear);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubjectBranchId=rs.getString(1);
                sub.SubBranchId=rs.getString(2);
                sub.BranchId=rs.getString(3);
                sub.StartingSem=rs.getString(4);
                sub.DisplaySubBranchName=rs.getString(5);
                sub.SubjectId=rs.getString(6);
                sub.SubjectName=rs.getString(7);
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<Subject> getSubBranch(String BranchId,String Semester) throws SQLException
{
     ArrayList<Subject> subject=new ArrayList<Subject>();
    Connection con=null;
        try
        { //Query Changed by Yadu 10-09-2012
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("Select SubBranchId,SubBranchName from SubBranchMaster as sb where BranchId=? and StartingSem <=? ");
            st.setString(1, BranchId);
            st.setString(2, Semester);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Subject sub=new Subject();
                sub.SubBranchId=rs.getString(1);
                sub.DisplaySubBranchName=rs.getString(2);
                subject.add(sub);
            }
           return subject;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
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


public String getSubBranchName(String SubBranchId,Connection con)
{
        try
        {
            PreparedStatement st = con.prepareStatement("select SubBranchName from SubBranchMaster where SubBranchId=?");
            st.setString(1, SubBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString("SubBranchName");
            }
            return "";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Subjects.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

}
}

