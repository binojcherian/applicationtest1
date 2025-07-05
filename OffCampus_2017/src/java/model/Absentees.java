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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.*;

public class Absentees
{

public ArrayList<Entity.CourseData> getBranchList() throws SQLException
{
    Connection con=null;
    try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT BranchId ,DisplayName FROM BranchMaster where BranchId not in (24,25,27,28,29,30,31) order by DisplayName");

            ResultSet Rs = st.executeQuery();
            Entity.CourseData Course=null;
            ArrayList<Entity.CourseData> Branches=new ArrayList<Entity.CourseData>();
            while (Rs.next()) {
                Course=new Entity.CourseData();
                Course.BranchId=Rs.getInt(1);
                Course.BranchName=Rs.getString(2);
                Branches.add(Course);
            }

           return  Branches;

           } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }
    finally{
        if(con!=null)
                 con.close();
             }

}
public ArrayList<Entity.CourseData> getAllBranchList() throws SQLException
{
    Connection con=null;
    try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT BranchId ,DisplayName FROM BranchMaster   order by DisplayName");

            ResultSet Rs = st.executeQuery();
            Entity.CourseData Course=null;
            ArrayList<Entity.CourseData> Branches=new ArrayList<Entity.CourseData>();
            while (Rs.next()) {
                Course=new Entity.CourseData();
                Course.BranchId=Rs.getInt(1);
                Course.BranchName=Rs.getString(2);
                Branches.add(Course);
            }

           return  Branches;

           } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }
    finally{
        if(con!=null)
                 con.close();
             }

}
public ArrayList<Entity.CourseData> getBranchListForMBA() throws SQLException
{
    Connection con=null;
    try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT BranchId ,DisplayName FROM BranchMaster where BranchId=21 order by DisplayName ");

            ResultSet Rs = st.executeQuery();
            Entity.CourseData Course=null;
            ArrayList<Entity.CourseData> Branches=new ArrayList<Entity.CourseData>();
            while (Rs.next()) {
                Course=new Entity.CourseData();
                Course.BranchId=Rs.getInt(1);
                Course.BranchName=Rs.getString(2);
                Branches.add(Course);
            }

           return  Branches;

           } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }
    finally{
        if(con!=null)
                 con.close();
             }

}
public ArrayList<ExamData> getExams() throws SQLException
{
    Connection con=null;
    try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT ExamId ,ExamName FROM ExamMaster where ExamId=5");
            ResultSet Rs = st.executeQuery();
            ArrayList<ExamData> ExamDat=new ArrayList<ExamData>();
            while (Rs.next()) {
               ExamData exam=new ExamData();
                exam.ExamId=Rs.getString(1);
                exam.ExamName=Rs.getString(2);
                ExamDat.add(exam);
            }

           return  ExamDat;

           } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }
    finally{
        if(con!=null)
                 con.close();
             }

}
public ArrayList<ExamData> getAllExams() throws SQLException
{
    Connection con=null;
    try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM `DEMS_db`.`ExamMaster` order by AcadamicYear desc");
            ResultSet Rs = st.executeQuery();
            ArrayList<ExamData> ExamDat=new ArrayList<ExamData>();
            while (Rs.next()) {
               ExamData exam=new ExamData();
                exam.ExamId=Rs.getString(1);
                exam.ExamName=Rs.getString(2);
                ExamDat.add(exam);
            }

           return  ExamDat;

           } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }
    finally{
        if(con!=null)
                 con.close();
             }

}
public int getCurrentExamId() throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT ExamId FROM ExamMaster order by ExamId limit 1");
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                return Rs.getInt(1);
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
                con.close();
        }
}

public int getMaxYearOrSemForCourse(String CourseId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT max(CurrentYearSem) FROM SubjectBranchMaster where BranchId=?");
            st.setString(1, CourseId);
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                return Rs.getInt(1);
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
                con.close();
        }
}

public boolean IsStudentBelongToCourse(String PRN,String BranchId) throws SQLException
{
     Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentPersonal where PRN=? and BranchId=? ");
            st.setString(1, PRN);
            st.setString(2, BranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                return false;
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
}

public boolean IsStudentBelongToSubject(int StudentId,String ExamId,String SubjectBranchId) throws SQLException
{
     Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(*) from ExamRegistrationMaster as er where StudentId=? and SubjectBranchId=? and ExamId=? ");
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
                return false;
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
}

public boolean IsStudentBelongToElective(int StudentId,String ExamId,String SubBranchId,String BranchId,int CurrentSem,String SubjectBranchId) throws SQLException
{
     Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM `ExamRegistrationMaster` as er,`SubjectBranchMaster` as sm where er.SubjectBranchId=sm.SubjectBranchId and er.ExamId=? and sm.BranchId=? and SubBranchId=? and CurrentYearSem=? and er.StudentId=? and sm.SubjectBranchId=?");
            st.setString(1, ExamId);
            st.setString(2, BranchId);
            st.setString(3, SubBranchId);
            st.setInt(4, CurrentSem);
            st.setInt(5, StudentId);
            st.setString(6, SubjectBranchId);

            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                return false;
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
}

public int getTotalAbsenteesEnteredForSubjectWithoutFee(String SubjectBranchId,String Sem,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentSubjectMark` c inner join SubjectBranchMaster b on  b.SubjectBranchId=c.SubjectBranchId and c.SubjectBranchId=? and b.CurrentYearSem=? where c.ExamId=? and IsAbsent=1 and IsValid=1 and not exists (select StudentId from StudentFeeMap f where f.StudentId=c.StudentId and ExamFeeId>0)");
            st.setString(1,SubjectBranchId);
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

public int getTotalAbsenteesEnteredForSubjectWithoutFee(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentSubjectMark` c inner join "+
            " SubjectBranchMaster b on  b.SubjectBranchId=c.SubjectBranchId and b.SubjectId=? and b.BranchId=? "+
            " and b.AcademicYear=? and b.CurrentYearSem=? "+
            " where c.ExamId=? and IsAbsent=1 and IsValid=1 and not exists "+
            " (select StudentId from ExamRegistrationMaster f inner join "+
            " SubjectBranchMaster b on  b.SubjectBranchId=f.SubjectBranchId and b.SubjectId=? and b.BranchId=? "+
           " and b.AcademicYear=? and b.CurrentYearSem=?  where f.StudentId=f.StudentId and f.ExamId=?)");
            st.setString(1,SubjectId);
            st.setString(2,BranchId);
            st.setString(3,AcdemicYear);
            st.setString(4, Sem);
            st.setString(5, ExamId);
            st.setString(6,SubjectId);
            st.setString(7,BranchId);
            st.setString(8,AcdemicYear);
            st.setString(9, Sem);
            st.setString(10, ExamId);
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

public int getTotalMalpracteesEnteredForSubjectWithoutFee(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentSubjectMark` c inner join "+
            " SubjectBranchMaster b on  b.SubjectBranchId=c.SubjectBranchId and b.SubjectId=? and b.BranchId=? "+
            " and b.AcademicYear=? and b.CurrentYearSem=? "+
            " where c.ExamId=? and IsMalPractice=1 and IsValid=1 and not exists "+
             " (select StudentId from ExamRegistrationMaster f inner join "+
            " SubjectBranchMaster b on  b.SubjectBranchId=f.SubjectBranchId and b.SubjectId=? and b.BranchId=? "+
           " and b.AcademicYear=? and b.CurrentYearSem=?  where f.StudentId=f.StudentId and f.ExamId=?)");
            st.setString(1,SubjectId);
            st.setString(2,BranchId);
            st.setString(3,AcdemicYear);
            st.setString(4, Sem);
            st.setString(5, ExamId);
            st.setString(6,SubjectId);
            st.setString(7,BranchId);
            st.setString(8,AcdemicYear);
            st.setString(9, Sem);
            st.setString(10, ExamId);
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

public int getStudentIdFromPRN(String PRN) throws SQLException
{
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select StudentId from StudentPersonal where PRN=?");
                st.setString(1,PRN);
                 ResultSet rs=st.executeQuery();
            while(rs.next())
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

public int getStudentIdFromStudentSubjectMarkId(String StudentSubjectMarkId) throws SQLException
{
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select StudentId from StudentSubjectMark where StudentSubjectMarkId=?");
                st.setString(1,StudentSubjectMarkId);
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

public boolean SaveMalPractice(int StudentId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into StudentExamWithheldDetails (Studentid,ExamId,ModifiedDate) values(?,?,sysdate())");
            st.setInt(1,StudentId);
            st.setString(2, ExamId);
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

public boolean SaveAbsentees(String PRN,String SubjectBranchId,String ExamId,int IsAbsent,int MalPractice) throws SQLException
{
    boolean flag=false;
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            if(IsAbsent==1)
            {
                PreparedStatement st=con.prepareStatement("insert into StudentSubjectMark(StudentId,SubjectBranchId,ExamId,IsAbsent,IsValid) values(?,?,?,?,1)");
                st.setInt(1, getStudentIdFromPRN(PRN));
                st.setString(2, SubjectBranchId);
                st.setString(3, ExamId);
                st.setInt(4, IsAbsent);
                st.execute();
                flag=true;
            }
            if(MalPractice==1)
            {
                if(IsMarkEnteredForStudent(getStudentIdFromPRN(PRN), SubjectBranchId, ExamId))
                {
                     PreparedStatement st1=con.prepareStatement("Update StudentSubjectMark set IsMalPractice=1 where StudentId=? and SubjectBranchId=? and ExamId=?");
                     st1.setInt(1, getStudentIdFromPRN(PRN));
                     st1.setString(2, SubjectBranchId);
                     st1.setString(3, ExamId);
                     st1.execute();
                }
                else
                {
                    PreparedStatement st1=con.prepareStatement("insert into StudentSubjectMark(StudentId,SubjectBranchId,ExamId,IsMalPractice,IsValid) values(?,?,?,?,1)");
                    st1.setInt(1, getStudentIdFromPRN(PRN));
                    st1.setString(2, SubjectBranchId);
                    st1.setString(3, ExamId);
                    st1.setInt(4, MalPractice);
                    st1.execute();
                }
              if(SaveMalPractice(getStudentIdFromPRN(PRN), ExamId))
              {
                  flag=true;
              }
              else
              {
                    flag=false;
              }
            }
            if(flag==true)
            {
                return true;
            }
            else
                return false;


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

public ArrayList<StudentSubject> getAllAbsentMalPracticeStudents() throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select sp.StudentId,sm.StudentSubjectMarkId,sp.StudentName,sp.PRN,c.CollegeName,b.DisplayName,sm.IsAbsent,sm.IsMalPractice,s.SubjectName,sm.SubjectBranchId from StudentPersonal sp,StudentSubjectMark sm,CollegeMaster c,BranchMaster b,SubjectMaster s,SubjectBranchMaster sb where sp.StudentId=sm.StudentId and sm.SubjectBranchId=sb.SubjectBranchId and sb.SubjectId=s.SubjectId and sp.BranchId=b.Branchid and c.CollegeId=sp.CollegeId and sm.IsValid=1");
            ResultSet rs=st.executeQuery();
            ArrayList<StudentSubject> student=new ArrayList<StudentSubject>();
            while(rs.next())
            {
                StudentSubject stuSubject=new StudentSubject();
                stuSubject.BranchName=rs.getString("DisplayName");
                stuSubject.CollegeName=rs.getString("CollegeName");
                stuSubject.PRN=rs.getString("PRN");
                stuSubject.StudentId=rs.getString("StudentSubjectMarkId");
                stuSubject.StudentName=rs.getString("StudentName");
                stuSubject.SubjectName=rs.getString("SubjectName");
                if(rs.getString("IsAbsent").equals("1"))
                {
                    stuSubject.Status="Absent";
                }
                if(rs.getString("IsMalPractice").equals("1"))
                {
                    stuSubject.Status="MalPractice";
                }
                student.add(stuSubject);
            }
            return student;
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

public AbsenteeCount getAbsenteeMalPracticeCountofId(String ExamAbsenteeMalPracticeId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select sb.BranchId,sb.CurrentYearSem,sb.SubjectBranchId,e.IsAbsentOrMal,e.Count,ExamAbsenteeMalPracticeId,e.ExamId from SubjectBranchMaster sb,ExamAbsenteeMalPracticeCount e where sb.SubjectBranchId=e.SubjectBranchId and ExamAbsenteeMalPracticeId=?");
            st.setString(1, ExamAbsenteeMalPracticeId);
            ResultSet rs=st.executeQuery();
            AbsenteeCount ACount=new AbsenteeCount();
            if(rs.next())
            {
            ACount.SubjectBranchId=rs.getString("SubjectBranchId");
            ACount.BranchId=rs.getString("BranchId");
            ACount.ExamId=rs.getString("ExamId");
            ACount.YearSem=rs.getString("CurrentYearSem");
            ACount.Count=rs.getInt("Count");
            ACount.IsAbsentOrMal=rs.getInt("IsAbsentOrMal");

            }
            return ACount;
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

public StudentSubject getAbsentMalPracticeDetails(String StudentSubjectMarkId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select sp.StudentId,sp.StudentName,sp.PRN,c.CollegeName,b.DisplayName,sm.IsAbsent,sm.IsMalPractice,s.SubjectName,sm.SubjectBranchId from StudentPersonal sp,StudentSubjectMark sm,CollegeMaster c,BranchMaster b,SubjectMaster s,SubjectBranchMaster sb where sp.StudentId=sm.StudentId and sm.SubjectBranchId=sb.SubjectBranchId and sb.SubjectId=s.SubjectId and sp.BranchId=b.Branchid and c.CollegeId=sp.CollegeId and sm.IsValid=1 and sm.StudentSubjectMarkId=?");
            st.setString(1, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();
             StudentSubject stuSubject=new StudentSubject();
            while(rs.next())
            {
                stuSubject.BranchName=rs.getString("DisplayName");
                stuSubject.CollegeName=rs.getString("CollegeName");
                stuSubject.PRN=rs.getString("PRN");
                stuSubject.StudentId=rs.getString("StudentId");
                stuSubject.StudentName=rs.getString("StudentName");
                stuSubject.SubjectName=rs.getString("SubjectName");
                if(rs.getString("IsAbsent").equals("1"))
                {
                    stuSubject.Status="Absent";
                }
                if(rs.getString("IsMalPractice").equals("1"))
                {
                    stuSubject.Status="MalPractice";
                }

            }
            return stuSubject;
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

public boolean DeleteStudentAbsentDetails(String StudentSubjectMarkId) throws SQLException
{
    boolean flag=false;
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement st=con.prepareStatement("Update StudentSubjectMark set IsValid=0,MarkStatus=0 where StudentSubjectMarkId=? ");
            st.setString(1, StudentSubjectMarkId);
            st.executeUpdate();
            flag=true;
            con.commit();

            st=con.prepareStatement(" select StudentId,ExamId from StudentSubjectMark where StudentSubjectMarkId=? and IsMalPractice=1 ");
            st.setString(1, StudentSubjectMarkId);
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
            flag=false;
            PreparedStatement st1=con.prepareStatement("Update StudentExamWithheldDetails set EndDate=sysdate() where StudentId=? and ExamId=? ");
            st1.setString(1, rs.getString("StudentId"));
            st1.setString(2, rs.getString("ExamId"));
            st1.executeUpdate();
            flag=true;
            con.commit();
            }
            if(flag==true)
            {
                con.setAutoCommit(true);
                return true;
            }
            else
                return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
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

public boolean IsMarkEnteredForStudent(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
     Connection con=null;
         boolean returnValue=true;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement selectQuery=con.prepareStatement("Select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and ExternalMark is not null and IsValid=1  and IsAbsent!=1");
            selectQuery.setInt(1, StudentId);
            selectQuery.setString(2, SubjectBranchId);
            selectQuery.setString(3, ExamId);
            ResultSet Data=selectQuery.executeQuery();
        
            if(Data.next())
            {
                if(Data.getInt(1)>0)
                {
                    returnValue=true;
                }else
                    returnValue=false;
            
            }else
            {
               returnValue=false;
            }
            
          
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
          
        }
        finally
        {
            if(con!=null&&!con.isClosed())
            {
                con.close();
            }
        }

         return  returnValue;
}

public boolean IsAbsenteeAlreadyEntered(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsAbsent=1 and IsValid=1");
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
                return false;
            }
            return false;

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

public boolean IsMalPracticeAlreadyEntered(int StudentId,String SubjectBranchId,String ExamId) throws SQLException
{
Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(*) from StudentSubjectMark where StudentId=? and SubjectBranchId=? and ExamId=? and IsMalPractice=1 and IsValid=1");
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
                return false;
            }
            return false;

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

public boolean IsCountAlreadyEntered(String ExamId,String SubjectBranchId,int IsAbsentOrMal,String ExamAbsentMalPracticeId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(*) from ExamAbsenteeMalPracticeCount where ExamId=? and SubjectBranchId=? and IsAbsentOrMal=? and ExamAbsenteeMalPracticeId !=?  ");
            st.setString(1, ExamId);
            st.setString(2, SubjectBranchId);
            st.setInt(3, IsAbsentOrMal);
            st.setString(4, ExamAbsentMalPracticeId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
                return false;
            }
            return false;

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

public boolean SaveAbsenteesMalPracticeCount(String ExamId,String SubjectBranchId,int IsAbsentOrMal,int Count) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into ExamAbsenteeMalPracticeCount (ExamId,SubjectBranchId,IsAbsentOrMal,Count,ModifiedDate) values(?,?,?,?,sysdate())");
            st.setString(1, ExamId);
            st.setString(2, SubjectBranchId);
            st.setInt(3, IsAbsentOrMal);
            st.setInt(4, Count);
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

public boolean UpdateAbsenteesmalPracticeCount(String ExamId,String SubjectBranchId,int IsAbsentOrMal,int Count,String ExamAbsentMalPracticeId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("update ExamAbsenteeMalPracticeCount set ExamId=?,SubjectBranchId=?,IsAbsentOrMal=?,Count=? where ExamAbsenteeMalPracticeId=?");
            st.setString(1, ExamId);
            st.setString(2, SubjectBranchId);
            st.setInt(3, IsAbsentOrMal);
            st.setInt(4, Count);
            st.setString(5, ExamAbsentMalPracticeId);
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

public boolean SaveAbsenteesMalPracticeLog(int StudentId,String SubjectBranchId,String ExamId,HttpServletRequest request,int IsAbsent,int MalPractice) throws SQLException
{
MarkEntry_old mark=new MarkEntry_old();
int IsEdited=0;
String IpAddress=request.getRemoteAddr();
String StudentSubjectMarkId=mark.getStudentSubjectBranchId(StudentId, SubjectBranchId, ExamId);
String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
if(IsAbsent==1)
{
    IsEdited=2;
}
if(MalPractice==1)
{
    IsEdited=3;
}
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into MarkEntryLog (UserName,Privilege,IPAddress,StudentSubjectMarkId,EnteredDate,IsEdited) values(?,32,?,?,sysdate(),?)");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, StudentSubjectMarkId);
    st.setInt(4, IsEdited);
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

public int getTotalCountOfAbsenteeForSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select Count from ExamAbsenteeMalPracticeCount where SubjectBranchId=? and ExamId=? and IsAbsentOrMal=1");
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
public int getTotalCountOfAbsenteeForSubject_MBA(String SubjectId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select Count(e.SubjectBranchId) from ExamAbsenteeMalPracticeCount e"
                    + " inner join SubjectBranchMaster b on e.SubjectBranchId=b.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId"
                    + " where s.SubjectId=? and ExamId=? and IsAbsentOrMal=1");
            st.setString(1,SubjectId);
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


public int getTotalCountOfAbsenteeForSubject(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select sum(ea.Count) from SubjectBranchMaster as sm inner join ExamAbsenteeMalPracticeCount as ea on sm.SubjectBranchId=ea.SubjectBranchId where sm.SubjectId=? and ea.ExamId=? and sm.BranchId=? and sm.CurrentYearSem=? and sm.AcademicYear=? and IsAbsentOrMal=1");
            st.setString(1,SubjectId);
            st.setString(2,ExamId);
            st.setString(3,BranchId);
            st.setString(4, Sem);
            st.setString(5,AcdemicYear);
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

public int getTotalCountOfAbsenteesEntered(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where IsAbsent=1 and IsValid=1 and SubjectBranchId=? and ExamId=7");
            st.setString(1, SubjectBranchId);
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

public int getTotalCountOfAbsenteesEntered(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark as sm inner join "+
            " SubjectBranchMaster as sb on sm.SubjectBranchId=sb.SubjectBranchId where sm.IsAbsent=1 and sm.IsValid=1 "+
            " and sb.SubjectId=? and sm.ExamId=? and sb.BranchId=? and sb.CurrentYearSem=? and sb.AcademicYear=?");
            st.setString(1, SubjectId);
            st.setString(2, ExamId);
            st.setString(3, BranchId);
            st.setString(4, Sem);
            st.setString(5, AcdemicYear);
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



public int getTotalCountOfUnconfirmedStudents(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where TempUnconfirmed=1 and UnConfirmedSOVerified=1 and IsValid=1 and SubjectBranchId=? and ExamId=7");
            st.setString(1, SubjectBranchId);
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

public int getTotalCountOfUnconfirmedStudentsPending(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark where TempUnconfirmed=1 and UnConfirmedSOVerified=0 and IsValid=1 and SubjectBranchId=? and ExamId=(select ExamId from ExamMaster order by ExamId limit 1)");
            st.setString(1, SubjectBranchId);
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

public int getTotalStudentsForCourse(String BranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentPersonal p inner join ExamMaster e on e.AcadamicYear=p.JoiningYear and e.ExamId=?  where p.BranchId=? and p.PRN is not null  ");
            st.setString(1, ExamId);
            st.setString(2, BranchId);
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

public String getSubjectNameFromSubjectBranchId(String SubjectBranchId) throws SQLException
{
     Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select s.SubjectName from SubjectMaster s,SubjectBranchMaster sb where sb.SubjectId=s.SubjectId and sb.SubjectBranchId=? ");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return "";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
}

public String getSubjectNameFromSubjectId(String SubjectId) throws SQLException
{
     Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select SubjectName from SubjectMaster where SubjectId=?");
            st.setString(1, SubjectId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return "";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
}

public Subject getBranchFromSubjectBranchId(String SubjectBranchId) throws SQLException
{
     Connection con=null;
        try
        {
            Subject sub=new Subject();
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select SubjectBranchId,BranchId,CurrentYearSem from SubjectBranchMaster where Subject"
                    + "Id=? ");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {

                sub.BranchId=rs.getString("BranchId");
                sub.YearSem=rs.getString("CurrentYearSem");
                sub.SubjectBranchId=rs.getString("SubjectBranchId");

            }
            return sub;
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

public int getTotalCountOfStudentsForSubject_MBA(String SubjectId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from ExamRegistrationMaster e "
             + " inner join SubjectBranchMaster b on e.SubjectBranchId=b.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId"
                    + " where s.SubjectId=? and ExamId=?");
            st.setString(1, SubjectId);
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
public int getTotalCountOfStudentsForSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from ExamRegistrationMaster where SubjectBranchId=? and ExamId=?");
            st.setString(1, SubjectBranchId);
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
public int getTotalCountOfStudentsForPassedSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from StudentSubjectMark where SubjectBranchId=? and ExamId=? and IsValid=1 and IsPassed=1");
            st.setString(1, SubjectBranchId);
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
public int getTotalCountOfStudentsForPassedSubject_MBA(String SubjectId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from StudentSubjectMark m "
                    + " inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId"
                    + " where s.SubjectId=? and ExamId=? and IsValid=1 and IsPassed=1");
            st.setString(1, SubjectId);
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
public int getTotalCountOfStudentsForAbsentSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from StudentSubjectMark where SubjectBranchId=? and ExamId=? and IsValid=1 and IsAbsent=1");
            st.setString(1, SubjectBranchId);
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
public int getTotalCountOfStudentsForAbsentSubject_MBA(String SubjectId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from StudentSubjectMark m  "
                       + " inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId "
                    + " where s.SubjectId=? and ExamId=? and IsValid=1 and IsAbsent=1");
            st.setString(1, SubjectId);
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
public int getTotalCountOfStudentsForSubject(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(StudentId) from ExamRegistrationMaster as er inner join SubjectBranchMaster as sm on er.SubjectBranchId=sm.SubjectBranchId where sm.SubjectId=? and er.ExamId=? and sm.BranchId=? and sm.CurrentYearSem=? and sm.AcademicYear=?");
            st.setString(1, SubjectId);
            st.setString(2, ExamId);
            st.setString(3, BranchId);
            st.setString(4, Sem);
            st.setString(5, AcdemicYear);
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

public int getTotalCountOfMalPracticeEntered(String SubjectBranchId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark sm where IsMalPractice=1 and IsValid=1 and SubjectBranchId=? and ExamId=7");
            st.setString(1, SubjectBranchId);
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

public int getTotalCountOfMalPracticeEntered(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from StudentSubjectMark as sm "+
            " inner join SubjectBranchMaster as sb "+
            " on sm.SubjectBranchId=sb.SubjectBranchId "+
            " where IsMalPractice=1 and IsValid=1 "+
            " and sb.SubjectId=? and sm.ExamId=? and sb.BranchId=? "+
            " and sb.CurrentYearSem=? and sb.AcademicYear=?");
            st.setString(1, SubjectId);
            st.setString(2, ExamId);
            st.setString(3, BranchId);
            st.setString(4, Sem);
            st.setString(5, AcdemicYear);
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


public int getTotalCountOfMalPracticeForSubject(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select ea.Count from SubjectBranchMaster as sm inner join ExamAbsenteeMalPracticeCount as ea on sm.SubjectBranchId=ea.SubjectBranchId where sm.SubjectId=? and ea.ExamId=? and sm.BranchId=? and sm.CurrentYearSem=? and sm.AcademicYear=? and IsAbsentOrMal=2");
                st.setString(1,SubjectId);
                st.setString(2, ExamId);
                st.setString(3, BranchId);
                st.setString(4, Sem);
                st.setString(5, AcdemicYear);
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

public int getTotalCountOfMalPracticeForSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select Count from ExamAbsenteeMalPracticeCount where SubjectBranchId=? and ExamId=? and IsAbsentOrMal=2");
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

public int getTotalCountOfMarkEnteredForSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select Count(*) from StudentSubjectMark where SubjectBranchId=? and ExamId=? and IsValid=1 and IsMalPractice!=1 and IsAbsent!=1 and ExternalMark is not null");
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

public int getTotalCountOfMarkEnteredForSubject(String SubjectId,String ExamId,String BranchId,String Sem,String AcdemicYear) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select Count(*) from StudentSubjectMark as sm "+
            " inner join SubjectBranchMaster as sb "+
            " on sm.SubjectBranchId=sb.SubjectBranchId "+
            " where sb.SubjectId=? and sm.ExamId=? and sb.BranchId=? and sb.CurrentYearSem=? and sb.AcademicYear=? "+
            " and IsValid=1 and IsMalPractice!=1 and IsAbsent!=1 "+
            " and ExternalMark is not null");
            st.setString(1,SubjectId);
            st.setString(2,ExamId);
            st.setString(3,BranchId);
            st.setString(4,Sem);
            st.setString(5,AcdemicYear);
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

public int getTotalCountOfMarkVerifiedForSubject(String SubjectBranchId,String ExamId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select Count(*) from StudentSubjectMark where SubjectBranchId=? and ExamId=? and ExternalMark is not null and IsValid=1 and IsSOVerified=1 and IsMalPractice=0");
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

public boolean MarkTemporaryAbsentee(String SubjectBranchId,ArrayList<String>Student,HttpServletRequest request) throws SQLException
{
    MarkEntry_old mark=new MarkEntry_old();
    Connection con=null;
    int length=Student.size(),i=0;
    Integer ExamId=getCurrentExamId();
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into StudentSubjectMark (ExamId,StudentId,SubjectBranchId,TempUnconfirmed,IsValid) values(?,?,?,1,1)");
            while(i<length)
            {
                String stu=Student.get(i);
                st.setInt(1, ExamId);
                st.setString(2, stu);
                st.setString(3, SubjectBranchId);
                st.execute();
                i++;
                SaveMarkEntryLog(stu, SubjectBranchId, ExamId.toString(), 5, request);
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

public boolean ApproveUnconfirmedBySO(ArrayList<String> StudentSubjectMarkId,HttpServletRequest request) throws SQLException
{
    MarkEntry_old mark=new MarkEntry_old();
    Connection con=null;
    int length=StudentSubjectMarkId.size(),i=0;
    Integer ExamId=getCurrentExamId();
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("update StudentSubjectMark set UnConfirmedSOVerified=1 where StudentSubjectMarkId=?");
            while(i<length)
            {
                String stu=StudentSubjectMarkId.get(i);
                st.setString(1, stu);
                st.execute();
                i++;
                SaveLog(stu, ExamId.toString(), 6, request);

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

public boolean SaveUnconfirmedWithheldDetails(ArrayList<String> StudentSubjectMarkId) throws SQLException
{
    Connection con=null;
    int length=StudentSubjectMarkId.size(),i=0;
    Integer ExamId=getCurrentExamId();
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into StudentExamWithheldDetails (StudentId,ExamId,ModifiedDate,Status) values(?,?,sysdate(),'Unconfirmed')");
            while(i<length)
            {
                String stu=StudentSubjectMarkId.get(i);
                st.setInt(1, getStudentIdFromStudentSubjectMarkId(stu));
                st.setInt(2, ExamId);
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

public boolean RejectAbsenteeBySO(ArrayList<String> StudentSubjectMarkId) throws SQLException
{
    MarkEntry_old mark=new MarkEntry_old();
    Connection con=null;
    int length=StudentSubjectMarkId.size(),i=0;
    Integer ExamId=getCurrentExamId();
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("update StudentSubjectMark set IsValid=0 where StudentSubjectMarkId=?");
            while(i<length)
            {
                String stu=StudentSubjectMarkId.get(i);
                st.setString(1, stu);
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

public boolean SaveMarkEntryLog(String StudentId,String SubjectBranchId,String ExamId,int Edit,HttpServletRequest request) throws SQLException
{
    MarkEntry_old abs=new MarkEntry_old();
    String IpAddress=request.getRemoteAddr();
    String StudentSubjectMarkId=abs.getStudentSubjectBranchId(Integer.parseInt(StudentId), SubjectBranchId, ExamId);
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

public void SaveLog(String StudentSubjectMarkId,String ExamId,int Edit,HttpServletRequest request ) throws SQLException
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
        st.setInt(4, Edit);
        st.execute();

    }
    catch (SQLException ex)
    {
        Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

    }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
}
public static void main(String args[])
{

}
}
