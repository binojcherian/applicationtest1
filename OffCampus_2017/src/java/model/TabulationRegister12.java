/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

public class TabulationRegister12
{
    public ArrayList<StudentData> Student=new ArrayList<StudentData>();

    public ArrayList<Centre> getAllCentresForCourse(String BranchId) throws SQLException
    {
        ArrayList<Centre> CentreList=new ArrayList<Centre>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("SELECT c.CollegeName,b.CollegeId from CollegeBranchMap b inner join CollegeMaster c on c.CollegeId=b.CollegeId and b.BranchId=? where b.IsDeleted=0 order by c.CollegeName");
            st.setString(1, BranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Centre list=new Centre();
                list.CentreId=rs.getString("CollegeId");
                list.CentreName=rs.getString("CollegeName");

                CentreList.add(list);
            }
            return CentreList;
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

    public int getCurrentExamId(Connection con) throws SQLException
    {
        try
        {
            PreparedStatement st = con.prepareStatement("SELECT ExamId FROM ExamMaster where AcadamicYear=2010");
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                return Rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int getTotalPapersForSemester(String BranchId,String Sem,int StudentId,Connection con)
    {
        try
        {
            PreparedStatement st = con.prepareStatement("select count(distinct m.SubjectBranchId) from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where m.StudentId=? and m.ExamId=? and m.IsValid=1");
            st.setString(1,BranchId);
            st.setString(2, Sem);
            st.setInt(3, StudentId);
            st.setInt(4, getCurrentExamId(con));
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                if(BranchId.equals("7") ||BranchId.equals("8"))
                {
                    return Rs.getInt(1)+1;
                }
                else
                {
                    return Rs.getInt(1);
                }
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public String YearOrSem(String CourseId) throws SQLException
    {
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT SemorYear FROM BranchMaster where BranchId=?");
            st.setString(1, CourseId);
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                if(Rs.getInt(1)==1)
                {
                return "Year" ;
                }
                if(Rs.getInt(1)==0)
                {
                return "Semester" ;
                }
            }
            return "";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
     finally
        {
            if(con!=null)
                con.close();
        }
}



    public ArrayList<Entity.StudentData> getStudentsOfCourse(String ExamId,String BranchId,String CentreId) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId where b.BranchId="+BranchId+" and e.ExamId="+ExamId;
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }
            Query+=" order by p.PRN";
            PreparedStatement st=con.prepareStatement(Query);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs.getString("StudentId");
                Student.StudentName=rs.getString("StudentName");
                Student.PRN=rs.getString("PRN");
                Student.BranchId=rs.getString("BranchId");
                Student.BranchName=rs.getString("DisplayName");
                Student.CollegeId=rs.getString("CollegeId");
                Student.CollegeName=rs.getString("CollegeName");
                StudentList.add(Student);
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

    public String getExamName(String ExamId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT ExamName  FROM ExamMaster where ExamId=?");
            st.setString(1, ExamId);
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                return Rs.getString("ExamName");
            }
            return " ";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
     finally
        {
            if(con!=null)
                con.close();
        }
    }

    public String getStudentWithheldDetails(String StudentId,String ExamId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT count(*)  FROM StudentExamWithheldDetails where StudentId=? and ExamId=? and Status='MalPractice' and EndDate is null");
            st.setString(1, StudentId);
            st.setString(2, ExamId);
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
               if(Rs.getInt(1)>0)
               {
                   return "W";
               }
               else
               {
                    st = con.prepareStatement("SELECT count(*)  FROM StudentExamWithheldDetails where StudentId=? and ExamId=? and Status='Unconfirmed' and EndDate is null");
                    st.setString(1, StudentId);
                    st.setString(2, ExamId);
                    ResultSet Rs1=st.executeQuery();
                    while(Rs1.next())
                    {
                        if(Rs1.getInt(1)>0)
                        {
                            return "RAL";
                        }
                    }
               }
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
    }

    public ArrayList<TabulationData> getStudentMarkDetails(String StudentId,String ExamId,String Sem,Connection con)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark,m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,b.SubjectBranchId,m.IsAbsent FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and b.CurrentYearSem=? and  m.IsValid=1 order by b.PartNo, b.PaperNo ");
            st.setString(1, StudentId);
            st.setString(2, ExamId);
            st.setString(3, Sem);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.StudentId=Rs.getString("StudentId");
                Tab.SubjectBranchId=Rs.getString("SubjectBranchId");
                Tab.SubjectName=Rs.getString("SubjectName");
                if(Rs.getString("ExternalMin").equals("0"))
                {
                    Tab.ExtMin=" ";
                }
                else
                {
                     Tab.ExtMin=Rs.getString("ExternalMin");
                }
                if(Rs.getString("InternalMin").equals("0"))
                {
                    Tab.IntMin=" ";
                }
                else
                {
                     Tab.IntMin=Rs.getString("InternalMin");
                }
                if(Rs.getString("PassMark").equals("0"))
                {
                    Tab.PassMark=" ";
                }
                else
                {
                     Tab.PassMark=Rs.getString("PassMark");
                }


                if(Rs.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                    SemTotal=0;
                    if(Rs.getInt("SubjectBranchId")!=57 && Rs.getInt("SubjectBranchId")!=51 && Rs.getInt("SubjectBranchId")!=160 && Rs.getInt("SubjectBranchId")!=154)
                    {
                    flag=false;
                    }
                    else
                    {
                        EngFail=true;
                    }
                }
                else
                {
                    Tab.ExternalMark=Rs.getString("ExternalMark");
                    if(Rs.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs.getString("ModerationExt");
                    }
                    if(Rs.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs.getString("ModerationInt");
                    }


                    if(Rs.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {
                        Tab.InternalMark=Rs.getString("InternalMark");
                    }
                    Tab.TotalMark="";
                    SemTotal+=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt");
                    if(Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")>=Rs.getInt("ExternalMin") && Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("InternalMin") && Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("PassMark"))
                    {
                        Tab.Status="Passed";
                        Integer Total=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt");
                        Tab.GrandTotal+=Total;
                        Tab.TotalMark=Total.toString();
                    }
                    else
                    {
                        Tab.Status="Failed";
                        Tab.TotalMark=" ";
                        if(Rs.getInt("SubjectBranchId")!=57 && Rs.getInt("SubjectBranchId")!=51 && Rs.getInt("SubjectBranchId")!=160 && Rs.getInt("SubjectBranchId")!=154)
                        {
                            flag=false;
                        }
                        else
                        {
                            EngFail=true;
                        }
                    }
                    Tab.flag=flag;
                    Tab.IsFailedForEnglish=EngFail;
                }

                StudentMark.add(Tab);
            }
            return StudentMark;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(TabulationRegister12.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ArrayList<TabulationData> getStudentTRDetails(String ExamId,String Sem,Connection con,String CourseId, String CentreId)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int PrevStudentId=-1;
        Integer SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            String Query="SELECT p.StudentId,p.StudentName,p.PRN,c.CollegeName,b.DisplayName,sb.CurrentYearSem,s.SubjectName,sb.ExternalMin,sb.InternalMin,sb.IsAddedWithTotal,sb.HasInternal,sb.PartNo,sb.PassMark,m.ExternalMark,m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,sb.SubjectBranchId,m.IsAbsent,m.IsMalPractice FROM `DEMS_db`.`StudentSubjectMark` m inner join StudentPersonal p on p.StudentId=m.StudentId inner join ExamRegistrationMaster em on em.StudentId=p.StudentId and em.SubjectBranchId=m.SubjectBranchId and em.ExamId=1 inner join CollegeMaster c on c.CollegeId=p.CollegeId inner join BranchMaster b on b.BranchId=p.BranchId inner join SubjectBranchMaster sb on sb.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on s.SubjectId=sb.SubjectId where m.IsValid=1 and p.BranchId=?  and sb.CurrentYearSem=? ";
            if(!CentreId.equals("-1"))
            {
                Query+=" and p.CollegeId="+CentreId;
            }
            Query+=" order by c.CollegeName,b.DisplayName,p.PRN,sb.CurrentYearSem,sb.PartNo,sb.PaperNo";
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, CourseId);
            st.setString(2, Sem);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                if(Rs.getInt("StudentId")!=PrevStudentId)
                {
                SemTotal=0;
                flag=true;
                }
                StudentData stu=new StudentData();
                TabulationData Tab=new TabulationData();
                stu.StudentId=Rs.getInt("StudentId");
                stu.PRN=Rs.getString("PRN");
                stu.Name=Rs.getString("StudentName");
stu.Centre=Rs.getString("CollegeName");
stu.Course=Rs.getString("DisplayName");
                Tab.StudentId=Rs.getString("StudentId");
                Tab.PRN=Rs.getString("PRN");
                Tab.SubjectBranchId=Rs.getString("SubjectBranchId");
                Tab.SubjectName=Rs.getString("SubjectName");
                if(Rs.getString("ExternalMin").equals("0"))
                {
                    Tab.ExtMin=" ";
                }
                else
                {
                     Tab.ExtMin=Rs.getString("ExternalMin");
                }
                if(Rs.getString("InternalMin").equals("0"))
                {
                    Tab.IntMin=" ";
                }
                else
                {
                     Tab.IntMin=Rs.getString("InternalMin");
                }
                if(Rs.getString("PassMark").equals("0"))
                {
                    Tab.PassMark=" ";
                }
                else
                {
                     Tab.PassMark=Rs.getString("PassMark");
                }
                if(Rs.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                    SemTotal=0;
                    if(Rs.getInt("SubjectBranchId")!=57 && Rs.getInt("SubjectBranchId")!=51 && Rs.getInt("SubjectBranchId")!=160 && Rs.getInt("SubjectBranchId")!=154)
                    {
                        flag=false;
                    }
                    else
                    {
                        EngFail=true;
                    }
                }
                else if(Rs.getInt("IsMalPractice")==1)
                {
                    Tab.ExternalMark="W";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="WithHeld";
                    SemTotal=0;
                    if(Rs.getInt("SubjectBranchId")!=57 && Rs.getInt("SubjectBranchId")!=51 && Rs.getInt("SubjectBranchId")!=160 && Rs.getInt("SubjectBranchId")!=154)
                    {
                        flag=false;
                    }
                    else
                    {
                        EngFail=true;
                    }
                }
                else
                {
                    Tab.ExternalMark=Rs.getString("ExternalMark");
                    if(Rs.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs.getString("ModerationExt");
                    }
                    if(Rs.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs.getString("ModerationInt");
                    }


                    if(Rs.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {
                        Tab.InternalMark=Rs.getString("InternalMark");
                    }
                    Tab.TotalMark="";
                    SemTotal+=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt");
                    if(Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")>=Rs.getInt("ExternalMin") && Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("InternalMin") && Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("PassMark"))
                    {
                        Tab.Status="Passed";
                        Integer Total=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt");
                        //SemTotal+=Total;

                        Tab.TotalMark=Total.toString();
                    }
                    else
                    {
                        Tab.Status="Failed";
                        Tab.TotalMark=" ";
                        if(Rs.getInt("SubjectBranchId")!=57 && Rs.getInt("SubjectBranchId")!=51 && Rs.getInt("SubjectBranchId")!=160 && Rs.getInt("SubjectBranchId")!=154)
                        {
                            flag=false;
                        }
                        else
                        {
                            EngFail=true;
                        }
                    }
                    Tab.GrandTotal=SemTotal.toString();
                    Tab.IsFailedForEnglish=EngFail;
                }
                Tab.flag=flag;
                if(Rs.getInt("StudentId")!=PrevStudentId)
                {
                   Student.add(stu);
                }
                StudentMark.add(Tab);
                 PrevStudentId=Rs.getInt("StudentId");
            }
            return StudentMark;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(TabulationRegister12.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ArrayList<StudentData> getStudents()
    {
        return Student;
    }

    public int getTotalPassMarkForBLiSc(String BranchId,String YearSem,Connection con)
    {
        try
        {
            PreparedStatement st=con.prepareStatement("select sum(TotalMark) from SubjectBranchMaster where Branchid=? and CurrentYearSem=?");
            st.setString(1, BranchId);
            st.setString(2, YearSem);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1)/2;
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }


    public String getSemTotalForStudent(String StudentId,String ExamId,String Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.ExamId=?");
            st.setString(1, Sem);
            st.setString(2, StudentId);
            st.setString(3, ExamId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                return Rs.getString("Total");
            }
            return " ";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
    }

    public int getTotalForEnglish(String StudentId,String ExamId,Connection con)
    {
         try
        {
            PreparedStatement st = con.prepareStatement("select sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId where m.StudentId=? and b.PaperNo=1  and m.IsValid=1 and m.ExamId=?");
            st.setString(1, StudentId);
            st.setString(2, ExamId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                return Rs.getInt("TotalMark");
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public boolean IsStudentPassedForEnglish(String StudentId,String ExamId,Connection con)
    {
        boolean flag=true;
        try
        {
            PreparedStatement st = con.prepareStatement("select sum((ExternalMark+InternalMark+ModerationExt+ModerationInt)-b.PassMark) as TotalMark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId where m.StudentId=? and b.PaperNo=1  and m.IsValid=1 and m.ExamId=?");
            st.setString(1, StudentId);
            st.setString(2, ExamId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                if(Rs.getInt("TotalMark")<0)
                {
                    flag=false;
                }
            }
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }

    //NewMethod Added on 1/1/13
    public boolean IsStudentPassedForSemester(int StudentId,String Sem,String BranchId,Connection con)
    {
        boolean flag=true;
        try {
            int ExamId=getCurrentExamId(con);
            if(BranchId.equals("13)"))
            {
                if(getTotalPapersForSemester(BranchId, Sem, StudentId, con)>=getTotalPassMarkForBLiSc(BranchId, Sem, con))
                {
                    PreparedStatement st=con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and m.MarkStatus=1 and m.MarkStatus=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    //PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    st.setInt(1, StudentId);
                    st.setString(2,Sem);
                    ResultSet rs=st.executeQuery();
                    while(rs.next())
                    {
                        if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                        {
                            flag=false;
                        }
                        else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") && rs.getInt("ExternalMark")+rs.getInt("InternalMark")>=rs.getInt("PassMark"))
                        {
                            if(rs.getInt("HasInternal")==1)
                                {
                                    if(rs.getInt("InternalMark")>rs.getInt("InternalMin"))
                                    {

                                    }
                                    else
                                    {
                                        if(rs.getInt("IsAddedWithTotal")==1)
                                        {
                                        flag=false;
                                        }
                                    }
                            }
                        }
                        else
                        {
                            if(rs.getInt("IsAddedWithTotal")==1)
                                        {
                                        flag=false;
                                        }
                        }
                    }

                }
                else
                {
                    flag=false;
                }
            }
            else
            {
                PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                st.setInt(1, StudentId);
                st.setInt(2, ExamId);
                st.setString(3,Sem);
                ResultSet rs=st.executeQuery();
                while(rs.next())
                {
                    if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                    else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") && rs.getInt("ExternalMark")+rs.getInt("InternalMark")>=rs.getInt("PassMark"))
                    {
                        if(rs.getInt("HasInternal")==1)
                            {
                                if(rs.getInt("InternalMark")>rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                    if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                    }
                    else
                    {
                        if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                }
            }
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }

    //NewMethod Added on 1/1/13
    public String getSemTotal(int StudentId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.MarkStatus=1");
            st.setInt(1, Sem);
            st.setInt(2, StudentId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                return Rs.getString("Total");
            }
            return " ";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }

    }
}