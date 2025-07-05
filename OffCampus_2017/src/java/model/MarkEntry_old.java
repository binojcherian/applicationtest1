/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * sunandha
 */

package model;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import java.lang.Math;
import Entity.*;
import com.sun.net.httpserver.HttpContext;
import java.net.InetAddress;


public class MarkEntry_old
{

public ArrayList<StudentSubject> getAllSubjectsofStudent(String PRN,String YearSem) throws SQLException
{
     ArrayList<StudentSubject> StudentList=new ArrayList<StudentSubject>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select sp.StudentId,sp.StudentName,sp.BranchId,s.SubjectName,sb.SubjectBranchId from StudentPersonal sp,SubjectMaster s, SubjectBranchMaster sb,StudentSubjectBranchMap ss where sp.StudentId=ss.StudentId and s.SubjectId=sb.SubjectId and ss.SubjectBranchId=sb.SubjectBranchId and sb.CurrentYearSem=? and sp.PRN=?");
            st.setString(1, YearSem);
            st.setString(2, PRN);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubject student=new StudentSubject();
                student.StudentId=rs.getString("StudentId");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public String getBranchNameFromBranchId(String BranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select BranchName from BranchMaster where BranchId=?");
            st.setString(1, BranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public String getExamNameFromExamId(String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select ExamName from ExamMaster where ExamId=?");
            st.setString(1, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int getCountOfRecordsToBeVerifiedBySO(String UserName) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join AssistantSOMapping a on a.User_Asst=l.UserName and a.EndDate is null and l.Privilege=33 and a.User_SO=? and m.IsSOVerified=-1 and IsAssistantVerified=1 and l.IsEdited=0");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int TotalRecordsVerifiedByUser(String User_Asst,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join MguClientLogin mc on mc.UserName=l.UserName and mc.EndDate is null and mc.Privilege=33 and mc.Username=? and m.ExamId=? and m.IsAssistantVerified=1 and l.IsEdited=0 ");
            st.setString(1, User_Asst);
            st.setString(2, ExamId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int TotalRecordsVerifiedByUserForSOPanel(String User_Asst) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId and m.ExamId=7 inner join MguClientLogin mc on mc.UserName=l.UserName and mc.EndDate is null and mc.Privilege=33 and mc.Username=?  and m.IsAssistantVerified=1 and l.IsEdited=0 ");
            st.setString(1, User_Asst);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public String getStudentSubjectBranchId(String ExamId,int StudentId,String SubjectId,String CurrentYearSem)throws SQLException
{
    Connection con=null;
    try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select sb.SubjectBranchId from SubjectBranchMaster as sb,ExamRegistrationMaster as er where sb.SubjectBranchId=er.SubjectBranchId and sb.SubjectId=? and er.ExamId=? and er.StudentId=? and sb.CurrentYearSem=?");
            st.setString(1, SubjectId);
            st.setString(2, ExamId);
            st.setInt(3, StudentId);
            st.setString(4, CurrentYearSem);
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
                return rs.getString(1);
            }
           return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsStudentAbsentForSubject(int StudentId,String ExamId,String BranchId,String SubjectId,String Sem,String AcdemicYear) throws SQLException
{
     Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark as sm inner join SubjectBranchMaster as sb "+
            " on sm.SubjectBranchId=sb.SubjectBranchId where sm.StudentId=? and sm.ExamId=? "+
            " and sb.BranchId=? and sb.SubjectId=? and sb.CurrentYearSem=? and sb.AcademicYear=? "+
            " and sm.IsValid=1 and sm.IsAbsent=1");
            st.setInt(1, StudentId);
            st.setString(2, ExamId);
            st.setString(3, BranchId);
            st.setString(4, SubjectId);
            st.setString(5, Sem);
            st.setString(6, AcdemicYear);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsStudentAbsentForSubject(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
     Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsValid=1 and IsAbsent=1");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsStudentUnconfirmedForSubject(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
     Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsValid=1 and TempUnconfirmed=1");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsStudentExamFeePaid(int StudentId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from ExamRegistrationMaster where StudentId=? and ExamId=? ");
            st.setInt(1, StudentId);
            st.setString(2, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsStudentMarkAlreadyEntered(int StudentId,String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark as sm inner join SubjectBranchMaster as sb "+
            " on sm.SubjectBranchId=sb.SubjectBranchId where sm.StudentId=? and sb.SubjectId=? and sm.ExamId=? "+
            " and sb.BranchId=? and sb.CurrentYearSem=? and sb.AcademicYear=? "+
            " and sm.IsValid=1 and sm.IsAbsent=0 and sm.IsMalPractice=0 and sm.TempUnconfirmed=0");
            st.setInt(1, StudentId);
            st.setString(2, SubjectId);
            st.setString(3, ExamId);
            st.setString(4, BranchId);
            st.setString(5, Sem);
            st.setString(6, AcdemicYear);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsStudentMarkAlreadyEntered(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsValid=1 and IsAbsent=0 and IsMalPractice=0 and TempUnconfirmed=0");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsStudentMarkAlreadyEnteredForEdit(int StudentId,String SubjectBranchId,String ExamId,String StudentSubjectMarkId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsValid=1 and IsAbsent=0 and IsMalPractice=0 and TempUnconfirmed=0 and StudentSubjectMarkId!=?");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.setString(3, ExamId);
            st.setString(4, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public int getMaximumMarkForSubject(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select ExternalMax,TotalMark,HasInternal from SubjectBranchMaster where SubjectBranchId=?");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt("ExternalMax")>0)
                {
                    return rs.getInt("ExternalMax");
                }
                else
                {
                    return rs.getInt("TotalMark");
                }
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int getTotalMarkForSubject(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select ExternalMax,TotalMark,HasInternal from SubjectBranchMaster where SubjectBranchId=?");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {

                    return rs.getInt("TotalMark");

            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsResultWithHeldForStudent(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsValid=1 and IsMalPractice=1");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public String getStudentSubjectBranchId(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select StudentSubjectMarkId from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsValid=1");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public String getExamIdFromStudentMark(String StudentSubjectMarkId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select ExamId from StudentSubjectMark where StudentSubjectMarkId=?");
            st.setString(1, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean SaveStudentMarkForSubject(int StudentId,String ExamId,String SubjectBranchId,double ExternalMark,Integer InternalMark,double PracticalMark,String Remarks) throws SQLException
{
    Connection con=null;
    PreparedStatement st=null;
    int Status=IsStudentFailedForSubject(ExternalMark,Double.parseDouble(InternalMark.toString()),PracticalMark,SubjectBranchId);
        try
        {
           con = new DBConnection().getConnection();
           if(IsResultWithHeldForStudent(StudentId, SubjectBranchId, ExamId))
           {
               st=con.prepareStatement("Update StudentSubjectMark set ExternalMark=?,InternalMark=?,PracticalMark=?,IsValid=1,IsPassed=? where StudentSubjectMarkId=?");
               st.setDouble(1,ExternalMark);
               st.setInt(2, InternalMark);
               st.setDouble(1,PracticalMark);
               st.setInt(3, Status);
               st.setString(4, getStudentSubjectBranchId(StudentId, SubjectBranchId, ExamId));
           }
           else
           {
                st=con.prepareStatement("insert into StudentSubjectMark (StudentId,ExamId,SubjectBranchId,ExternalMark,InternalMark,PracticalMark,Remarks,IsValid,IsPassed) values(?,?,?,?,?,?,?,1,?)");
                st.setInt(1,StudentId);
                st.setString(2, ExamId);
                st.setString(3, SubjectBranchId);
                st.setDouble(4,ExternalMark);
                st.setInt(5, InternalMark);
                st.setDouble(6,PracticalMark);
                st.setString(7, Remarks);
                st.setInt(8, Status);
           }
            
            st.execute();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean SaveMarkEntryLog(int StudentId,String SubjectBranchId,String ExamId,int Edit,HttpServletRequest request) throws SQLException, UnknownHostException
{
    
String IpAddress=request.getRemoteAddr();
String StudentSubjectMarkId=getStudentSubjectBranchId(StudentId, SubjectBranchId, ExamId);
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,33,?,?,sysdate(),?)");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, StudentSubjectMarkId);
    st.setInt(4, Edit);
    st.execute();
    return true;
}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean UpdateMarkofStudent(int StudentId,String ExamId,String SubjectBranchId,double ExternalMark,Integer InternalMark,double PracticalMark,String Remarks,String StudentSubjectMarkId) throws SQLException
{
    Connection con=null;
    PreparedStatement st=null;
    int Status=IsStudentFailedForSubject(ExternalMark,Double.parseDouble(InternalMark.toString()),PracticalMark,SubjectBranchId);
        try
        {
           con = new DBConnection().getConnection();
                st=con.prepareStatement("update StudentSubjectMark set StudentId=?,ExamId=?,SubjectBranchId=?,ExternalMark=?,InternalMark=?,PracticalMark=?,Remarks=?,IsPassed=? where StudentSubjectMarkId=? ");
                st.setInt(1,StudentId);
                st.setString(2, ExamId);
                st.setString(3, SubjectBranchId);
                st.setDouble(4,ExternalMark);
                st.setInt(5, InternalMark);
                st.setDouble(6,PracticalMark);
                st.setString(7, Remarks);
                st.setInt(8, Status);
                st.setString(9, StudentSubjectMarkId);

            st.execute();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<StudentSubjectMark> getMarksEnteredByUser(String UserName) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s,MarkEntryLog m where sp.StudentId=sm.StudentId and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.StudentSubjectMarkId=m.StudentSubjectMarkId and m.Privilege=33 and m.IsEdited=0 and m.UserName=? and sm.IsAssistantVerified=0 and IsValid=1 and IsAbsent!=1 and sm.ExamId=7 order by StudentSubjectMarkId desc");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.PracticalMark=rs.getString("PracticalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public int TotalRecordsEnteredByUser(String UserName) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join MguClientLogin mc on mc.UserName=l.UserName and mc.EndDate is null and mc.Privilege=33 and mc.Username=? and m.IsAssistantVerified=0 and l.IsEdited=0 ");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int TotalRecordsEnteredByAR(String UserName) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join MguClientLogin mc on mc.UserName=l.UserName and mc.EndDate is null and mc.Privilege=31 and mc.Username=? and m.IsAssistantVerified=0 and l.IsEdited=0 ");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int TotalRecordsVerifiedByUser(String User_Asst) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join MguClientLogin mc on mc.UserName=l.UserName and mc.EndDate is null and mc.Privilege=33 and mc.Username=? and m.IsAssistantVerified=1 and l.IsEdited=0 ");
            st.setString(1, User_Asst);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalCountOfRecordsToBeVerifiedBySO(String UserName) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId and m.ExamId=11 inner join AssistantSOMapping a on a.User_Asst=l.UserName and a.EndDate is null and l.Privilege=33 and a.User_SO=? and m.IsSOVerified=0 and IsAssistantVerified=1 and l.IsEdited=0");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int getTotalCountOfRecordsToBeVerifiedBySORevaluation(String UserName) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join AssistantSOMapping a on a.User_Asst=l.UserName and a.EndDate is null and l.Privilege=33 and a.User_SO=? and  m.forRevaluationSO=1 and IsAssistantVerified=1 and l.IsEdited=0");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int getTotalCountOfRecordsToBeVerifiedBySOPostCorrection(String UserName) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId inner join AssistantSOMapping a on a.User_Asst=l.UserName and a.EndDate is null and l.Privilege=33 and a.User_SO=? and  m.forPostCorrectionSO=1 and IsAssistantVerified=1 and l.IsEdited=0");
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int getTotalCountOfRecordsToBeVerifiedByARPostCorrection() throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId and l.Privilege=31  and   m.IsARVerifiedPostCorrection=1 and forPostCorrectionSO=0 ");
         
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public int getTotalCountOfRecordsToBeVerifiedByRevaluation() throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join MarkEntryLog l on m.StudentSubjectMarkId=l.StudentSubjectMarkId and l.Privilege=31  and   m.IsARVerifiedRevaluation=1 and forRevaluationSO=0 ");

            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public StudentSubjectMark getSelectedStudentMark(String StudentSubjectBranchId) throws SQLException
{
     Connection con=null;
     StudentSubjectMark student=new StudentSubjectMark();
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select s.SubjectId,sb.AcademicYear,sp.StudentName,sp.PRN,sp.BranchId,s.SubjectName,sb.SubjectBranchId,sb.CurrentYearSem,sm.ExamId,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId,sm.Remarks from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s where sp.StudentId=sm.StudentId and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.IsAssistantVerified=0 and sm.StudentSubjectMarkId=? and IsValid=1");
            st.setString(1, StudentSubjectBranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {

                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.AcdemicYear=rs.getString("AcademicYear");
                student.SubjectId=rs.getString("SubjectId");
                student.Sem=rs.getString("CurrentYearSem");
                student.ExamId=rs.getString("ExamId");
                student.InternalMark=rs.getString("InternalMark");
                 student.PracticalMark=rs.getString("PracticalMark");
                student.BranchId=rs.getString("BranchId");
                student.Remarks=rs.getString("Remarks");
           }
            return student;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public StudentSubjectMark getSelectedStudentMarkWithMarkId(String StudentSubjectMarkId) throws SQLException
{
     Connection con=null;
     StudentSubjectMark student=new StudentSubjectMark();
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select s.SubjectId,sb.AcademicYear,sp.StudentName,sp.PRN,sp.BranchId,s.SubjectName,sb.SubjectBranchId,sb.CurrentYearSem,sm.ExamId,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId,sm.Remarks from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s where sp.StudentId=sm.StudentId and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.IsAssistantVerified=1 and sm.StudentSubjectMarkId=? and IsValid=1");
            st.setString(1, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {

                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.AcdemicYear=rs.getString("AcademicYear");
                student.SubjectId=rs.getString("SubjectId");
                student.Sem=rs.getString("CurrentYearSem");
                student.ExamId=rs.getString("ExamId");
                student.InternalMark=rs.getString("InternalMark");
                student.PracticalMark=rs.getString("PracticalMark");
                student.BranchId=rs.getString("BranchId");
                student.Remarks=rs.getString("Remarks");
           }
            return student;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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


public boolean SendMarksForApproval(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsAssistantVerified=1 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<StudentSubjectMark> getRecordsToBeVerifiedBySO(String Query,String UserName) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.PracticalMark=rs.getString("PracticalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<StudentSubjectMark> getRecordsforCorrectionAdmin(String Query) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(Query);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                if(rs.getString("IsAbsent").equals("1")){
                    student.ExternalMark="";
                    student.InternalMark="";
                    student.PracticalMark="";
                    student.ClassMod="";
                    student.TourReportMark="";
                    student.ExternalMod="";
                    student.InternalMod="";
                    student.gracemarkExt="";
                    student.gracemarkInt="";
                }else{
                    student.ExternalMark=rs.getString("ExternalMark");
                    student.InternalMark=rs.getString("InternalMark");
                    student.PracticalMark=rs.getString("PracticalMark");
                    student.ClassMod=rs.getString("ClassModeration");
                    student.TourReportMark=rs.getString("TourReportMark");
                    student.ExternalMod=rs.getString("ModerationExt");
                    student.InternalMod=rs.getString("ModerationInt");
                     student.gracemarkExt=rs.getString("ExternalGraceMark");
                    student.gracemarkInt=rs.getString("InternalGraceMark");
                }
                student.CurrentYearSem=rs.getString("CurrentYearSem");
                student.PaperNo=rs.getString("PaperNo");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.ExamId=rs.getString("ExamId");
                student.ExamName=rs.getString("ExamName");
                student.StudentId=rs.getString("StudentId");
                student.IsAbsent=rs.getString("IsAbsent");
                  student.IsPracticalAbsent=rs.getString("IsPracticalAbsent");
                student.IsPassed=rs.getString("IsPassed");
                student.IsPostCorrected=rs.getString("IsPostCorrected");
                student.IsWithheld=rs.getString("IsWithheld");
                student.IsMalPractice=rs.getString("IsMalPractice");
                student.IsReValuated=rs.getString("IsRevaluated");
                student.Remarks=rs.getString("ReMarks");
                student.MarkStatus=rs.getString("MarkStatus");



                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public StudentSubjectMark getRecordsforCorrectionAdminbyId(String StudentSubjectMarkId) throws SQLException
{
    StudentSubjectMark StudentList=new StudentSubjectMark();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,sp.PRN,s.SubjectName,sm.ExamId," +
                    " sm.StudentId,sm.IsValid,sm.IsPassed,sm.MarkStatus,sm.ReMarks,sm.IsWithheld,sm.IsAbsent,sm.IsMalPractice,sm.IsReValuated," +
                    " sm.IsPostCorrected,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.ModerationExt,sm.ModerationInt,sm.ClassModeration," +
                    " sm.PracticalMark,sm.TourReportMark,sm.StudentSubjectMarkId from StudentSubjectMark sm " +
                    " inner join StudentPersonal sp on sm.StudentId=sp.StudentId inner join  SubjectBranchMaster sb on sm.SubjectBranchId=sb.SubjectBranchId" +
                    " inner join  SubjectMaster s on sb.SubjectId=s.SubjectId" +
                    " where sm.IsSOVerified=1 and sm.IsValid=1 and sm.StudentSubjectMarkId=? ");
            st.setString(1, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();
             StudentSubjectMark student=new StudentSubjectMark();
            while(rs.next())
            {
                
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.PracticalMark=rs.getString("PracticalMark");
                student.ClassMod=rs.getString("ClassModeration");
                student.TourReportMark=rs.getString("TourReportMark");
                student.ExternalMod=rs.getString("ModerationExt");
                student.InternalMod=rs.getString("ModerationInt");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.ExamId=rs.getString("ExamId");
                student.StudentId=rs.getString("StudentId");
                student.IsAbsent=rs.getString("IsAbsent");
                student.IsPassed=rs.getString("IsPassed");
                student.IsPostCorrected=rs.getString("IsPostCorrected");
                student.IsWithheld=rs.getString("IsWithheld");
                student.IsMalPractice=rs.getString("IsMalPractice");
                student.IsReValuated=rs.getString("IsRevaluated");
                student.Remarks=rs.getString("ReMarks");
                student.IsValid=rs.getString("IsValid");
                student.MarkStatus=rs.getString("MarkStatus");
            
            }
            return student;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<StudentSubjectMark> getPostCorrectionBySO(String Query,String UserName) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.ExternalMod=rs.getString("ModerationExt");
                student.InternalMod=rs.getString("ModerationInt");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
}public ArrayList<StudentSubjectMark> getRevaluationBySO(String Query,String UserName) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.ExternalMod=rs.getString("ModerationExt");
                student.InternalMod=rs.getString("ModerationInt");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsStudentUnconfirmedForSubject(int StudentId,String ExamId,String BranchId,String SubjectId,String Sem,String AcdemicYear) throws SQLException
{
     Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark as sm inner join "
            + " SubjectBranchMaster as sb on sm.SubjectBranchId=sb.SubjectBranchId where sm.StudentId=? "
            + " and sm.ExamId=? and sb.BranchId=? and sb.SubjectId=? and sb.CurrentYearSem=? and "
            + " sb.AcademicYear=? and sm.IsValid=1 and sm.TempUnconfirmed=1");
            st.setInt(1, StudentId);
            st.setString(2, ExamId);
            st.setString(3, BranchId);
            st.setString(4, SubjectId);
            st.setString(5, Sem);
            st.setString(6, AcdemicYear);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsStudentRegisteredThisSubject(int StudentId,String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement(" select sb.SubjectBranchId from SubjectBranchMaster as sb,ExamRegistrationMaster as er"
                    + " where sb.SubjectBranchId=er.SubjectBranchId and sb.SubjectId=? and er.ExamId=? and sb.BranchId=?"
                    + " and er.StudentId=? and sb.CurrentYearSem=? and sb.AcademicYear=?");
            st.setString(1, SubjectId);
            st.setString(2, ExamId);
            st.setString(3, BranchId);
            st.setInt(4, StudentId);
            st.setString(5, Sem);
            st.setString(6, AcdemicYear);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<StudentSubjectMark> getPostCorrectionByAR(String Query) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(Query);
          
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");
                student.ExternalMod=rs.getString("ModerationExt");
                student.InternalMod=rs.getString("ModerationInt");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<StudentSubjectMark> getRecordsApprovedBySO(String Query,String UserName) throws SQLException
{
    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, UserName);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                StudentSubjectMark student=new StudentSubjectMark();
                student.PRN=rs.getString("PRN");
                student.StudentName=rs.getString("StudentName");
                student.SubjectBranchId=rs.getString("SubjectBranchId");
                student.SubjectName=rs.getString("SubjectName");
                student.ExternalMark=rs.getString("ExternalMark");
                student.InternalMark=rs.getString("InternalMark");
                student.StudentSubjectMarkId=rs.getString("StudentSubjectMarkId");

                StudentList.add(student);
            }
            return StudentList;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean ApproveStudentsMark(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsSOVerified=1 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean forRevaluationStudentsMark(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsRevaluated=1,forRevaluationSO=1,IsARVerifiedRevaluation=1 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean forPostCorrectionStudentsMark(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsPostCorrected=1,forPostCorrectionSO=1,IsARVerifiedPostCorrection=1 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean InsertStudentsOldMark_Revaluation(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("INSERT INTO `DEMS_db`.`StudentSubjectMark_Old`(`StudentSubjectMarkId`,`ExamId`,`StudentId`," +
                   "`SubjectBranchId`,`ExternalMark`,`InternalMark`,`PracticalMark`,`TourReportMark`,`ModerationExt`,`IsAbsent`," +
                   "`IsMalPractice`,`IsRevaluated`,`IsWithheld`,`IsPassed`,`Isvalid`,`Remarks`,`ModerationInt`,`MarkStatus`,`ClassModeration`," +
                   "`IsPostCorrected`,ModifiedDate )VALUES (?,?,?,?,?,?,?,?,?,?,?,1,?,?,?,?,?,?,?,?,sysdate()) ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.setString(2, Sid.ExamId);
              st.setString(3, Sid.StudentId);
              st.setString(4, Sid.SubjectBranchId);
              st.setString(5, Sid.ExternalMark);
              st.setString(6, Sid.InternalMark);
              st.setString(7, Sid.PracticalMark);
              st.setString(8, Sid.TourReportMark);
              st.setString(9, Sid.ExternalMod);
              st.setString(10, Sid.IsAbsent);
              st.setString(11, Sid.IsMalPractice);          
              st.setString(12, Sid.IsWithheld);
              st.setString(13, Sid.IsPassed);
              st.setString(14, Sid.IsValid);
              st.setString(15, Sid.Remarks);
              st.setString(16, Sid.InternalMod);
              st.setString(17, Sid.MarkStatus);
              st.setString(18, Sid.ClassMod);
              st.setString(19, Sid.IsPostCorrected);
              st.execute();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean InsertStudentsOldMark_PostCorrection(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("INSERT INTO `DEMS_db`.`StudentSubjectMark_Old`(`StudentSubjectMarkId`,`ExamId`,`StudentId`," +
                   "`SubjectBranchId`,`ExternalMark`,`InternalMark`,`PracticalMark`,`TourReportMark`,`ModerationExt`,`IsAbsent`," +
                   "`IsMalPractice`,`IsRevaluated`,`IsWithheld`,`IsPassed`,`Isvalid`,`Remarks`,`ModerationInt`,`MarkStatus`,`ClassModeration`," +
                   "`IsPostCorrected`,ModifiedDate )VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,sysdate()) ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.setString(2, Sid.ExamId);
              st.setString(3, Sid.StudentId);
              st.setString(4, Sid.SubjectBranchId);
              st.setString(5, Sid.ExternalMark);
              st.setString(6, Sid.InternalMark);
              st.setString(7, Sid.PracticalMark);
              st.setString(8, Sid.TourReportMark);
              st.setString(9, Sid.ExternalMod);
              st.setString(10, Sid.IsAbsent);
              st.setString(11, Sid.IsMalPractice);
              st.setString(12,Sid.IsReValuated);
              st.setString(13, Sid.IsWithheld);
              st.setString(14, Sid.IsPassed);
              st.setString(15, Sid.IsValid);
              st.setString(16, Sid.Remarks);
              st.setString(17, Sid.InternalMod);
              st.setString(18, Sid.MarkStatus);
              st.setString(19, Sid.ClassMod);

              st.execute();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean ApproveStudentsMarkBySORevaluation(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set forRevaluationSO=0 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean ApproveStudentsMarkByARRevaluation(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsARVerifiedRevaluation=0 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean ApproveStudentsMarkBySOPostCorrection(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set forPostCorrectionSO=0 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean ApproveStudentsMarkByARPostCorrection(ArrayList<StudentSubjectMark> stuMark) throws SQLException
{
    Connection con=null;
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsARVerifiedPostCorrection=0 where StudentSubjectMarkId=? ");
           int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public String getSubjectBranchIdfromStudentSubjectMarkId(String StudentSubjectMarkId) throws SQLException
{
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select SubjectBranchId from StudentSubjectMark where StudentSubjectMarkId=?" );
            st.setString(1, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
    public String MarkCorrection(double external, double internal, String StudentSubjectMarkId) throws SQLException {
        String SubjectBranchId = getSubjectBranchIdfromStudentSubjectMarkId(StudentSubjectMarkId);
        Connection con = null;

        try {
            if (external == -1) {
                return "Enter Mark";
            } else {

                int MaxMark = getMaximumMarkForSubject(SubjectBranchId);
                int TotalMark = getTotalMarkForSubject(SubjectBranchId);
                if (MaxMark == 0) {
                    if (external < 0) {
                        return "Invalid Mark Entered";
                    } else {
                        return null;
                    }
                } else {
                    if (external < 0 || external > MaxMark) {
                        return "Invalid Mark Entered";
                    } else {
                        //if (external + internal < 0 || external + internal > TotalMark) {
                           // return "Invalid Mark Entered";
                        //} else {
                            return null;
                        //}
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (con != null) {
                con.close();
            }
        }

    }
public boolean MarkUpdationBySO(double external,double internal,double Practical,String StudentSubjectMarkId) throws SQLException
{
    Absentees abs=new Absentees();
    Connection con=null;
  
    int Status=IsStudentFailedForSubject(external,internal,Practical,getSubjectBranchIdfromStudentSubjectMarkId(StudentSubjectMarkId));
   
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set ExternalMark=?,InternalMark=?,PracticalMark=?,IsPassed=? where StudentSubjectMarkId=? ");
           st.setDouble(1, external);
           st.setDouble(2, internal);
           st.setDouble(3, Practical);
           st.setInt(4, Status);
           st.setString(5, StudentSubjectMarkId);
           st.executeUpdate();
           return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean SaveMarkEditLogForSO(String StudentSubjectMarkId,String ExamId,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,32,?,?,sysdate(),1)");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, StudentSubjectMarkId);
    st.execute();
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkEditLogForAR(String StudentSubjectMarkId,String ExamId,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,31,?,?,sysdate(),1)");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, StudentSubjectMarkId);
    st.execute();
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkApprovalLogForSO(ArrayList<StudentSubjectMark> stuMark,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,32,?,?,sysdate(),4)");
     int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, User);
              st.setString(2, IpAddress);
              st.setString(3, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkApprovalLogForSOPostCorrection(ArrayList<StudentSubjectMark> stuMark,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,32,?,?,sysdate(),7)");
     int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, User);
              st.setString(2, IpAddress);
              st.setString(3, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkApprovalLogForSORevaluation(ArrayList<StudentSubjectMark> stuMark,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,32,?,?,sysdate(),14)");
     int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, User);
              st.setString(2, IpAddress);
              st.setString(3, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkApprovalLogForARPostCorrection(ArrayList<StudentSubjectMark> stuMark,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,31,?,?,sysdate(),8)");
     int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, User);
              st.setString(2, IpAddress);
              st.setString(3, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkApprovalLogForARRevaluation(ArrayList<StudentSubjectMark> stuMark,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,31,?,?,sysdate(),13)");
     int size=stuMark.size(),i=0;
           while(i<size)
           {
              StudentSubjectMark Sid=stuMark.get(i);
              st.setString(1, User);
              st.setString(2, IpAddress);
              st.setString(3, Sid.StudentSubjectMarkId);
              st.executeUpdate();
              i++;
           }
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkLogForSOPostCorrection(String StudentSubjectMarkId,String ExamId,int IsEditvalue,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,32,?,?,sysdate(),?)");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, StudentSubjectMarkId);
    st.setInt(4, IsEditvalue);
    st.execute();
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean SaveMarkLogForARPostCorrection(String StudentSubjectMarkId,String ExamId,int IsEditvalue,HttpServletRequest request) throws SQLException
{
String IpAddress=request.getRemoteAddr();
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,31,?,?,sysdate(),?)");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, StudentSubjectMarkId);
    st.setInt(4, IsEditvalue);
    st.execute();
    return true;

}
catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean MarkUpdationBySOPostCorrection(double external,double internal, double practical,double modExt,double modInt,String StudentSubjectMarkId) throws SQLException
{
    Absentees abs=new Absentees();
    Connection con=null;
    int Status=IsStudentFailedForSubjectwitMod(external+modExt,internal+modInt,practical,getSubjectBranchIdfromStudentSubjectMarkId(StudentSubjectMarkId));
    try
        {
           con = new DBConnection().getConnection();
           PreparedStatement st=con.prepareStatement("update StudentSubjectMark set ExternalMark=?,InternalMark=?,PracticalMark=?,ModerationExt=?,ModerationInt=?,IsPassed=? where StudentSubjectMarkId=? ");
           st.setDouble(1, external);
           st.setDouble(2, internal);
            st.setDouble(3, practical);
             st.setDouble(4,modExt );
             st.setDouble(5,modInt );
           st.setInt(6, Status);
           st.setString(7, StudentSubjectMarkId);
           if(st.executeUpdate()==0)
           return false;
           else
               return  true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public long CalculateInternal(double ExternalMark,String SubjectBranchId) throws SQLException
{
    Connection con=null;
    long Internal;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select InternalEvaluationValue from SubjectBranchMaster where SubjectBranchid=?");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                Internal=Math.round(ExternalMark*rs.getDouble("InternalEvaluationValue"));
            }
            else
            {
                Internal=0;
            }
            return Internal;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public long CalculateInternal(double ExternalMark,String SubjectId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
    long Internal;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select InternalEvaluationValue from SubjectBranchMaster "+
            " where SubjectId=? and BranchId=? and CurrentYearSem=? and AcademicYear=? limit 0,1");
            st.setString(1, SubjectId);
            st.setString(2, BranchId);
            st.setString(3, Sem);
            st.setString(4, AcdemicYear);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                Internal=Math.round(ExternalMark*rs.getDouble("InternalEvaluationValue"));
            }
            else
            {
                Internal=0;
            }
            return Internal;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsSubjectHasNoInternalCalculation(String SubjectId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select HasNoInternalCalculation from SubjectBranchMaster "+
            " where SubjectId=? and BranchId=? and CurrentYearSem=? and AcademicYear=? limit 0,1 ");
            st.setString(1, SubjectId);
            st.setString(2, BranchId);
            st.setString(3, Sem);
            st.setString(4, AcdemicYear);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public boolean IsSubjectHasInternal(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select hasInternal from SubjectBranchMaster where SubjectBranchId=? ");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsSubjectHasExternal(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select HasNoExternal from SubjectBranchMaster where SubjectBranchId=? ");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsSubjectHasPractical(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select HasPractical from SubjectBranchMaster where SubjectBranchId=? ");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsSubjectHasNoInternalCalculation(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select hasNoInternalCalculation from SubjectBranchMaster where SubjectBranchId=? ");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public int IsStudentFailedForSubject(double External,double Internal,double Practical,String SubjectBranchId) throws SQLException
{
    Connection con=null;
    double Total=External+Internal+Practical;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select ExternalMin,PassMark,InternalMin from SubjectBranchMaster where SubjectBranchId=?");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getDouble("ExternalMin")!=0)
                {
                    if(rs.getDouble("ExternalMin") > External)
                    {
                        return 0;
                    }
                    else if(rs.getDouble("InternalMin")!=0)
                    {
                        if(rs.getDouble("InternalMin")>Internal)
                        {
                            return 0;
                        }
                        else if(rs.getDouble("PassMark") > Total)
                        {
                            return 0;
                        }
                    }
                }
                else
                {
                    if(rs.getDouble("PassMark") > Total)
                        {
                            return 0;
                        }
                }
                
                
                return 1;
            }
             return 1;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
}
public int IsStudentFailedForSubjectwitMod(double External,double Internal, double practical,String SubjectBranchId) throws SQLException
{
    Connection con=null;
    double Total=External+Internal+practical;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select ExternalMin,PassMark,InternalMin from SubjectBranchMaster where SubjectBranchId=?");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getDouble("ExternalMin")!=0)
                {
                    if(rs.getDouble("ExternalMin") > External)
                    {
                        return 0;
                    }
                    else if(rs.getDouble("InternalMin")!=0)
                    {
                        if(rs.getDouble("InternalMin")>Internal)
                        {
                            return 0;
                        }
                        else if(rs.getDouble("PassMark") > Total)
                        {
                            return 0;
                        }
                    }
                }
                else
                {
                    if(rs.getDouble("PassMark") > Total)
                        {
                            return 0;
                        }
                }


                return 1;
            }
             return 1;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
}
public boolean IsMarkVerificationCompletedForBranch(String BranchId,String Sem,String ExamId) throws SQLException
{
    Absentees abs=new Absentees();
    Connection con=null;
    int TotalReg=getTotalCountOfRegistrationForCourse(BranchId,Sem,ExamId)-(getTotalCountOfAbsenteesForCourse(BranchId, Sem, ExamId)-getTotalAbsenteesEnteredWithoutFee(BranchId, Sem, ExamId));
        try
        {
            if(BranchId.equals("-1"))
            {
                return false;
            }
            else
            {
                con=new DBConnection().getConnection();
                PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark m inner join StudentPersonal s on m.StudentId=s.StudentId and s.Branchid=? where  m.ExamId=? and m.IsAssistantVerified=1 and m.IsSOVerified=1 ");
                st.setString(1, BranchId);
                st.setString(2, ExamId);
                ResultSet rs=st.executeQuery();
                if(rs.next())
                {
                    if(rs.getInt(1) == TotalReg)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                return false;
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalCountOfRegistrationForCourse(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(e.StudentId) from ExamRegistrationMaster e inner join StudentPersonal s on s.StudentId=e.StudentId  inner join SubjectBranchMaster b on b.SubjectBranchId=e.SubjectBranchId and b.BranchId=? and CurrentYearSem=? where e.ExamId=? ");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalCountOfStudentsForCourse(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(distinct e.StudentId) from ExamRegistrationMaster e inner join StudentPersonal s on s.StudentId=e.StudentId  inner join SubjectBranchMaster b on b.SubjectBranchId=e.SubjectBranchId and b.BranchId=? and CurrentYearSem=? where e.ExamId=? ");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalPassForCourse(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM StudentPassDetails p inner join StudentPersonal s on  s.StudentId=p.StudentId and s.BranchId=? where p.YearOrSem=? and p.ExamId=?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalCountOfAbsenteesForCourse(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT sum(Count) FROM `DEMS_db`.`ExamAbsenteeMalPracticeCount` c inner join SubjectBranchMaster b on  b.SubjectBranchId=c.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where c.ExamId=? and c.IsAbsentOrMal=1");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalAbsenteesEnteredWithoutFee(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentSubjectMark` c inner join SubjectBranchMaster b on  b.SubjectBranchId=c.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where c.ExamId=? and IsAbsent=1 and StudentId not in (select StudentId from StudentFeeMap where ExamFeeId>0)");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getTotalAbsenteesEnteredForSubjectWithoutFee(String SubjectBranchId,int Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentSubjectMark` c inner join SubjectBranchMaster b on  b.SubjectBranchId=c.SubjectBranchId and c.SubjectBranchId=? and b.CurrentYearSem=? where c.ExamId=? and IsAbsent=1 and not exists (select StudentId from StudentFeeMap f where f.StudentId=c.StudentId and ExamFeeId>0");
            st.setString(1,SubjectBranchId);
            st.setInt(2, Sem);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<ResultStatistics> getStatisticsForCourse(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
    ArrayList<ResultStatistics> Stat=new ArrayList<ResultStatistics>();
    Absentees abs=new Absentees();
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT sb.SubjectBranchId,sb.SubjectCode,s.SubjectName,count(distinct StudentId) as Pass from SubjectBranchMaster sb inner join SubjectMaster s on s.SubjectId=sb.SubjectId inner join StudentSubjectMark m on sb.SubjectBranchId=m.SubjectBranchId and IsPassed=1  and m.ExamId=? where sb.BranchId=? and sb.CurrentYearSem=?  group by sb.SubjectBranchId");
            st.setString(1, ExamId);
            st.setString(2, BranchId);
            st.setString(3, Sem);
             //and IsSOVerified=1
            ResultSet Subject=st.executeQuery();
            while(Subject.next())
            {
                ResultStatistics Result=new ResultStatistics();
                Result.SubjectBranchId=Subject.getString("SubjectBranchId");
                Result.SubjectCode=Subject.getString("SubjectCode");
                Result.SubjectName=Subject.getString("SubjectName");
                Result.TotalPass=Subject.getInt("Pass");
                int TotalReg=abs.getTotalCountOfStudentsForSubject(Subject.getString("SubjectBranchId"), ExamId)-abs.getTotalCountOfAbsenteeForSubject(Subject.getString("SubjectBranchId"), ExamId);
                Result.TotalReg=TotalReg;
                Result.PassPercentage=Math.round((Subject.getDouble("Pass")/TotalReg)*100);
                Stat.add(Result);

                int SemTotal=getSemesterTotalForCourse(BranchId,Sem);

                st=con.prepareStatement("select count(studentId) from StudentSubjectMark where sum(ExternalMark+InternalMark)>=?");
            }
            return Stat;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<ResultStatistics> getStatisticsForCourseMCA(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
    ArrayList<ResultStatistics> Stat=new ArrayList<ResultStatistics>();
    Absentees abs=new Absentees();
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT sb.SubjectBranchId,sb.SubjectCode,s.SubjectName,count(distinct StudentId) as Pass from SubjectBranchMaster sb inner join SubjectMaster s on s.SubjectId=sb.SubjectId inner join StudentSubjectMark m on sb.SubjectBranchId=m.SubjectBranchId  where sb.BranchId=? and sb.CurrentYearSem=? and m.ExamId=? group by sb.SubjectBranchId");
            st.setString(1, BranchId);
            st.setString(2, Sem);
             st.setString(3, ExamId);
            ResultSet Subject=st.executeQuery();
            while(Subject.next())
            {
                ResultStatistics Result=new ResultStatistics();
                Result.SubjectBranchId=Subject.getString("SubjectBranchId");
                Result.SubjectCode=Subject.getString("SubjectCode");
                Result.SubjectName=Subject.getString("SubjectName");
                Result.TotalPass=Subject.getInt("Pass");
                int TotalReg=abs.getTotalCountOfStudentsForSubject(Subject.getString("SubjectBranchId"), ExamId)-abs.getTotalCountOfAbsenteeForSubject(Subject.getString("SubjectBranchId"), ExamId);
                Result.TotalReg=TotalReg;
                Result.PassPercentage=Math.round((Subject.getDouble("Pass")/TotalReg)*100);
                Stat.add(Result);

                int SemTotal=getSemesterTotalForCourse(BranchId,Sem);

                st=con.prepareStatement("select count(studentId) from StudentSubjectMark where sum(ExternalMark+InternalMark)>=?");
            }
            return Stat;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getCountofStudentsAboveEightyPercentage(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM StudentPassDetails p inner join StudentPersonal s on  s.StudentId=p.StudentId and s.BranchId=? where p.YearOrSem=? and p.ExamId=? and p.TotalMark>?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            st.setDouble(4, getSemesterTotalForCourse(BranchId, Sem)*0.8);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getCountofStudentsAboveSeventyFivePercentage(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM StudentPassDetails p inner join StudentPersonal s on  s.StudentId=p.StudentId and s.BranchId=? where p.YearOrSem=? and p.ExamId=? and p.TotalMark>?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            st.setDouble(4, getSemesterTotalForCourse(BranchId, Sem)*0.75);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getCountofStudentsAboveSixtyPercentage(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM StudentPassDetails p inner join StudentPersonal s on  s.StudentId=p.StudentId and s.BranchId=? where p.YearOrSem=? and p.ExamId=? and p.TotalMark>?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            st.setDouble(4, getSemesterTotalForCourse(BranchId, Sem)*0.6);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getCountofStudentsAboveFiftyPercentage(String BranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM StudentPassDetails p inner join StudentPersonal s on  s.StudentId=p.StudentId and s.BranchId=? where p.YearOrSem=? and p.ExamId=? and p.TotalMark>?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setString(3, ExamId);
            st.setDouble(4, getSemesterTotalForCourse(BranchId, Sem)*0.5);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int getSemesterTotalForCourse(String BranchId,String Sem) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT sum(TotalMark) FROM SubjectBranchMaster where BranchId=? and CurrentYearSem=?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public int getSemesterTotalForCourseMCA(String BranchId,String Sem,int year) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT sum(TotalMark) FROM SubjectBranchMaster where BranchId=? and CurrentYearSem=? and AcademicYear=?");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setInt(3, year);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<StatisticsWithModeration> getStatisticsForBranchWithModeration(int From,int To, String BranchId,String Sem,String ExamId) throws SQLException
{
    ArrayList<StatisticsWithModeration> Statistics=new ArrayList<StatisticsWithModeration>();
    Connection con=null;int i=From;
    int Count=0,Reg=getTotalCountOfStudentsForCourse(BranchId, Sem, ExamId);
    int passbeforemod=getTotalPassForCourse(BranchId, Sem, ExamId);
    int totalpass=Reg-passbeforemod;
    try
    {
        while(i<=To)
        {
            int j=0;
            String Query="select distinct m.StudentId,sum((m.ExternalMark+m.InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  and b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and m.IsPassed=0 and m.IsValid=1 where m.IsAbsent!=1  group by StudentId  ";
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement(Query);
            st.setString(1, BranchId);
            st.setString(2,Sem);
            st.setString(3, ExamId);
            ResultSet Rs=st.executeQuery();
            StatisticsWithModeration stat=new StatisticsWithModeration();
            int Pass=getTotalPassForCourse(BranchId, Sem, ExamId);
            while(Rs.next())
            {
                if(Rs.getInt("Mark")+i>=0)
                {
                    j++;
                }
                stat.AppliedModeration=i;
                stat.TotalPass=j;
                stat.PassPercentage=(stat.TotalPass*100)/Reg;
                stat.TotalFail=totalpass-stat.TotalPass;
            }
            Statistics.add(stat);
            i++;
        }
        return Statistics; 
    }
    catch (SQLException ex)
    {
        Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<StatisticsWithModeration> getStatisticsForBranchWithModeration_Blisc(int From,int To, String BranchId,String Sem,String ExamId) throws SQLException
{
    ArrayList<StatisticsWithModeration> Statistics=new ArrayList<StatisticsWithModeration>();
    Connection con=null;int i=From;
    int Count=0,Reg=getTotalCountOfStudentsForCourse(BranchId, Sem, ExamId);
    int passbeforemod=getTotalPassForCourse(BranchId, Sem, ExamId);
    int totalpass=Reg-passbeforemod;
    try
    {
        while(i<=To)
        {
            int j=0;
            String Query="select distinct m.StudentId,sum((m.ExternalMark+m.InternalMark)) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  and b.BranchId=? and b.CurrentYearSem=? and m.ExamId=?  and m.IsValid=1 where m.IsAbsent!=1  group by StudentId  ";
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement(Query);
            st.setString(1, BranchId);
            st.setString(2,Sem);
            st.setString(3, ExamId);
            ResultSet Rs=st.executeQuery();
            StatisticsWithModeration stat=new StatisticsWithModeration();
            int Pass=getTotalPassForCourse(BranchId, Sem, ExamId);

            while(Rs.next())
            {
                int Student=Rs.getInt("StudentId");
                if(Rs.getInt("Mark")+i>=200)
                {
                    String Query1="select distinct m.StudentId,sum((m.ExternalMark+m.InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  and b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and m.IsPassed=0 and m.IsValid=1 where m.IsAbsent!=1 and m.StudentId=?  group by StudentId  ";
            con=new DBConnection().getConnection();
            PreparedStatement st1=con.prepareStatement(Query1);
            st1.setString(1, BranchId);
            st1.setString(2,Sem);
            st1.setString(3, ExamId);
            st1.setInt(4, Student);
            ResultSet Rs1=st1.executeQuery();
            while(Rs1.next())
            {
                if(Rs1.getInt("Mark")+i>=0)
                    j++;

                }}
                stat.AppliedModeration=i;
                stat.TotalPass=j;
                stat.PassPercentage=(stat.TotalPass*100)/Reg;
                stat.TotalFail=totalpass-stat.TotalPass;
            }
            Statistics.add(stat);
            i++;
        }
        return Statistics;
    }
    catch (SQLException ex)
    {
        Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<StatisticsWithModeration> getStatisticsForSubjectPassWithModeration(int From,int To, String BranchId,String Sem,String ExamId, String SubjectBranchId) throws SQLException
{
    ArrayList<StatisticsWithModeration> Statistics=new ArrayList<StatisticsWithModeration>();
    Connection con=null;int i=From;
    Absentees abs=new Absentees();
    boolean flag=false;
    int Count=0,Reg=getTotalCountOfStudentsForCourse(BranchId, Sem, ExamId);
    try
    {
        con=new DBConnection().getConnection();
        while(i<=To)
        {
            String Query="select distinct StudentId,sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m "
                    + " inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=?"
                    + "  and b.SubjectBranchId=? where m.ExamId=? and m.IsPassed=0  "
                    + " and StudentId not in(select StudentId from StudentSubjectMark where IsAbsent=1) group by StudentId ";
            
            PreparedStatement st=con.prepareStatement(Query);
            st.setString(1, BranchId);
            st.setString(2,Sem);
            st.setString(3, SubjectBranchId);
            st.setString(4, ExamId);
            ResultSet Rs=st.executeQuery();
            StatisticsWithModeration stat=new StatisticsWithModeration();
            int Pass=getTotalPassForCourse(BranchId, Sem, ExamId);
            int SubjectPass=TotalPassForSubject(SubjectBranchId, ExamId);
            while(Rs.next())
            {
                flag=true;
                if(Rs.getInt("Mark")+i>=0)
                {
                    Pass++;
                    SubjectPass++;
                }
                stat.AppliedModeration=i;
                stat.TotalPass=Pass;
                stat.PassPercentage=(stat.TotalPass*100)/Reg;
                stat.TotalFail=Reg-stat.TotalPass;
                stat.SubjectBranchId=SubjectBranchId;
                
                stat.TotalSubjectPass=SubjectPass;
            }
            if(flag==true){
            Statistics.add(stat);}
            i++;
            
        }
        return Statistics;
    }
    catch (SQLException ex)
    {
        Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public int TotalPassForSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM StudentSubjectMark where SubjectBranchId=? and ExamId=? and IsPassed=1");
            st.setString(1,SubjectBranchId);
            st.setString(2, ExamId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
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

public String getStudentSubjectMarkIdsEnteredByUser(String Assistant) throws SQLException
{
    Connection con=null;
    String MarkId=" ";
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select StudentSubjectMarkId from MarkEntryLog where IsEdited=0 and UserName=?");
            st.setString(1, Assistant);
            ResultSet rs=st.executeQuery();
            
            while(rs.next())
            {
                MarkId+=rs.getString("StudentSubjectMarkId")+",";
            }
String Mark=MarkId.substring(0,MarkId.length()-1);
            return Mark;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
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