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
import Entity.*;

public class TabulationRegister
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
            PreparedStatement st = con.prepareStatement("SELECT ExamId FROM ExamMaster where AcadamicYear=2019");
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

    public int getTotalPapersForSemester(String BranchId,int Sem,int StudentId,Connection con)
    {
        try
        {
            PreparedStatement st = con.prepareStatement("select count(distinct m.SubjectBranchId) from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where m.StudentId=? and m.ExamId=? and m.IsValid=1");
            st.setString(1,BranchId);
            st.setInt(2, Sem);
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

    public ArrayList<Entity.StudentData> getStudentsOfCourse(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName,sbm.SubBranchName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId left join SubBranchMaster sbm on sbm.SubBranchId=sb.SubBranchId where b.BranchId="+BranchId+" and e.ExamId="+ExamId +" and sb.CurrentYearSem ="+Sem ;
            //  +" and c.CollegeId  not in (96,93,81,88,71,73,74,108,75,62,110,38,42,82,76,115,80,66,46,48,89,91,90,84,95,86,63,123,133,141,146,152,167)";
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+="  order by c.CollegeName,p.PRN  ";
             //System.out.println("$$$$"+Query);
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
                 Student.SubBranchName=rs7.getString("SubBranchName");
                 System.out.println("$$$$"+Student.StudentName);
                 
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
    
    public ArrayList<Entity.StudentData> getStudentsOfCourseConsolidate(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName,sbm.SubBranchName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId inner join tempExamconsolidate2015 t on t.StudentId=p.StudentId inner join SubBranchMaster sbm on sbm.SubBranchId=sb.SubBranchId where b.BranchId="+BranchId+" and e.ExamId="+ExamId+" and p.StudentId=9753" ;
            //  +" and c.CollegeId  not in (96,93,81,88,71,73,74,108,75,62,110,38,42,82,76,115,80,66,46,48,89,91,90,84,95,86,63,123,133,141,146,152,167)";
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+="  order by c.CollegeName,p.PRN  ";
             System.out.println("$$$$"+Query);
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
                 Student.SubBranchName=rs7.getString("SubBranchName");
                 System.out.println("$$$$"+Student.StudentName);
                 
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
    
     public ArrayList<Entity.StudentData> getStudentsOfCourseConsolidateLLM(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId inner join tempExamconsolidate2015 t on t.StudentId=p.StudentId  where b.BranchId="+BranchId+" and e.ExamId="+ExamId  ;
            //  +" and c.CollegeId  not in (96,93,81,88,71,73,74,108,75,62,110,38,42,82,76,115,80,66,46,48,89,91,90,84,95,86,63,123,133,141,146,152,167)";
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+="  order by c.CollegeName,p.PRN  ";
             //System.out.println("$$$$"+Query);
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
                // Student.SubBranchName=rs7.getString("SubBranchName");
                 System.out.println("$$$$"+Student.StudentName);
                 
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
    
    public ArrayList<Entity.StudentData> getStudentsOfCourse1(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName,sbm.SubBranchName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId left join SubBranchMaster sbm on sbm.SubBranchId=sb.SubBranchId where b.BranchId="+BranchId+" and e.ExamId="+ExamId +" and sb.CurrentYearSem in (1,2)"  ;
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+=" order by c.CollegeName,p.PRN  ";
             System.out.println("$$$$"+Query);
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
                 Student.SubBranchName=rs7.getString("SubBranchName");
                 System.out.println("$$$$"+Student.StudentName);
                 
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
    
    
    
    public ArrayList<Entity.StudentData> getStudentsOfCourse_Bcom2013(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName, "
                    + " c.CollegeId,c.CollegeName,sbm.SubBranchName from ExamRegistrationMaster e "
                    + " inner join StudentPersonal p on p.StudentId=e.StudentId "
                    + " inner join StudentExamFeeStatus es "
                    + " on es.StudentId=e.StudentId and e.ExamId=es.ExamId "
                    + " inner join BranchMaster b "
                    + " on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId "
                    + " inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId "
                    + " left join SubBranchMaster sbm on sbm.SubBranchId=sb.SubBranchId "
                    + " where b.BranchId="+BranchId+" "
                    + " and es.IsMGUApproved=1 and  sb.SubjectBranchId in(61,207,62,63,100) and e.ExamId="+ExamId ; 
                    
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+=" order by p.CollegeId,p.PRN ";
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
                 Student.SubBranchName=rs7.getString("SubBranchName");
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
    
    
     public ArrayList<Entity.StudentData> getStudentsOfCourse_BcomPartIII(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
           // String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId where p.CourseCompletedStatus=1 and p.AttendingYear=3  and sb.PartNo='PART-III' and  b.BranchId="+BranchId+" and e.ExamId="+ExamId ;
             String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName from StudentExamFeeStatus e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId  where  p.AttendingYear=3  and  b.BranchId="+BranchId+" and e.ExamId="+ExamId ;
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+="  order by p.PRN  ";
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
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
 
     public ArrayList<Entity.StudentData> getStudentsOfCourse_BcomPartII(String ExamId,String BranchId,String CentreId,String Sem) throws SQLException
    {
        ArrayList<Entity.StudentData> StudentList=new ArrayList<Entity.StudentData>();
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
           // String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName from ExamRegistrationMaster e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId inner join SubjectBranchMaster sb on sb.SubjectBranchId=e.SubjectBranchId where p.CourseCompletedStatus=1 and p.AttendingYear=3  and sb.PartNo='PART-III' and  b.BranchId="+BranchId+" and e.ExamId="+ExamId ;
             String Query="SELECT distinct p.StudentId,p.StudentName,p.PRN,b.BranchId,b.DisplayName,c.CollegeId,c.CollegeName from StudentExamFeeStatus e inner join StudentPersonal p on p.StudentId=e.StudentId inner join BranchMaster b on p.BranchId=b.BranchId inner join CollegeMaster c on p.CollegeId=c.CollegeId "
                     + " inner join ExamRegistrationMaster em on em.ExamId=e.ExamId and e.StudentId=em.StudentId inner join SubjectBranchMaster sb on sb.SubjectBranchId=em.SubjectBranchId "
                     + " where  p.AttendingYear=3 and sb.PartNo='PART-III' and  b.BranchId="+BranchId+" and e.ExamId="+ExamId ;
            if(!CentreId.equals("-1"))
            {
                Query+=" and c.CollegeId="+CentreId;
            }

            Query+=" order by p.PRN  ";
            PreparedStatement st7=con.prepareStatement(Query);
            ResultSet rs7=st7.executeQuery();
            while(rs7.next())
            {
                Entity.StudentData Student=new Entity.StudentData();
                Student.StudentId=rs7.getString("StudentId");
                Student.StudentName=rs7.getString("StudentName");
                Student.PRN=rs7.getString("PRN");
                Student.BranchId=rs7.getString("BranchId");
                Student.BranchName=rs7.getString("DisplayName");
                Student.CollegeId=rs7.getString("CollegeId");
                Student.CollegeName=rs7.getString("CollegeName");
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
            PreparedStatement st8 = con.prepareStatement("SELECT count(*)  FROM StudentExamWithheldDetails where StudentId=? and ExamId=? and Status='MalPractice' and EndDate is null");
            st8.setString(1, StudentId);
            st8.setString(2, ExamId);
            ResultSet Rs8=st8.executeQuery();

            if(Rs8.next())
            {
               if(Rs8.getInt(1)>0)
               {
                   return "Withheld";
               }
               else
               {
                    st8 = con.prepareStatement("SELECT count(*)  FROM StudentExamWithheldDetails where StudentId=? and ExamId=? and Status='Unconfirmed' and EndDate is null");
                    st8.setString(1, StudentId);
                    st8.setString(2, ExamId);
                    ResultSet Rs9=st8.executeQuery();
                    while(Rs9.next())
                    {
                        if(Rs9.getInt(1)>0)
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

    public ArrayList<TabulationData> getStudentMarkDetails(String StudentId,String ExamId,int Sem,Connection con)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark,m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,b.SubjectBranchId,m.IsAbsent FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and b.CurrentYearSem=? and  m.IsValid=1 order by b.PartNo, b.PaperNo ");
            st.setString(1, StudentId);
            st.setString(2, ExamId);
            st.setInt(3, Sem);
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

     public ArrayList<TabulationData> getStudentMarkDetails_old(String StudentId,String Sem,Connection con)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            PreparedStatement st = con.prepareStatement("SELECT m.ExamId, m.StudentId,b.ExternalMin,b.HasPractical,b.HasNoExternal,b.InternalMin,b.IsAddedWithTotal,b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark,m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration,m.ExternalGraceMark,m.InternalGraceMark  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId   where m.StudentId=? and b.CurrentYearSem=? and m.MarkStatus=1 and  m.IsValid=1  order by  b.PaperNo ");
            st.setString(1, StudentId);

            st.setString(2, Sem);

            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs.getString("ExamId");
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
                if(Rs.getString("ExternalGraceMark").equals("0"))
                {
                    Tab.ExternalGraceMark=" ";
                }
                else
                {
                     Tab.ExternalGraceMark=Rs.getString("ExternalGraceMark");
                }
                if(Rs.getString("InternalGraceMark").equals("0"))
                {
                    Tab.InternalGraceMark=" ";
                }
                else
                {
                     Tab.InternalGraceMark=Rs.getString("InternalGraceMark");
                }
                if(Rs.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
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
                    if(Rs.getString("HasNoExternal").equals("1"))
                    {
                        Tab.ExternalMark=" ";
                    }else{
                    Tab.ExternalMark=Rs.getString("ExternalMark");}
                    if(Rs.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs.getString("ModerationExt");
                    }
                     if(Rs.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs.getString("ClassModeration");
                    }
                    if(Rs.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs.getString("ModerationInt");
                    }
                

                   /* if(Rs.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {*/
                        Tab.InternalMark=Rs.getString("InternalMark");
                   // }
                    Tab.TotalMark="";
                    SemTotal+=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration")+Rs.getInt("ExternalGraceMark")+Rs.getInt("InternalGraceMark");

                    if(Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ExternalGraceMark")>=Rs.getInt("ExternalMin") && Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")+Rs.getInt("InternalGraceMark")>=Rs.getInt("InternalMin") && Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")+Rs.getInt("ExternalGraceMark")+Rs.getInt("InternalGraceMark")>=Rs.getInt("PassMark"))
                    {
                        //int stTotal=Integer.parseInt(getSemTotalForStudent_Old(StudentId, Sem, con));
                        int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration")+Rs.getInt("ExternalGraceMark")+Rs.getInt("InternalGraceMark");
                        int passMark=Rs.getInt("PassMark");
                        //for llm
                      
                        /*int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark=Rs.getInt("PMark");
                       if(stTotal<175 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}*/
                         if (smark<passMark){
                        Tab.Status="Failed";}
                       else{Tab.Status="Passed";}
                        Integer Total=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration")+Rs.getInt("ExternalGraceMark")+Rs.getInt("InternalGraceMark");
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
     
      public ArrayList<TabulationData> getStudentMarkDetails_LLM(String StudentId,String Sem,Connection con)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            PreparedStatement st = con.prepareStatement("SELECT m.ExamId,m.IsPassed, m.StudentId,b.ExternalMin,b.HasPractical,b.HasNoExternal,b.InternalMin,b.IsAddedWithTotal,b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark,m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration ,m.ExternalGraceMark,m.InternalGraceMark,m.IsPassed FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId   where m.StudentId=? and b.CurrentYearSem=? and m.MarkStatus=1 and  m.IsValid=1 and m.hasAdditional=0 order by  b.PaperNo ");
            st.setString(1, StudentId);

            st.setString(2, Sem);

            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs.getString("ExamId");
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

if(Rs.getString("PMark").equals("0"))
                {
                    Tab.PMark=" ";
                }
                else
                {
                     Tab.PMark=Rs.getString("PMark");
                }
if(Rs.getString("ExternalGraceMark").equals("0"))
                {
                    Tab.ExternalGraceMark=" ";
                }
                else
                {
                     Tab.ExternalGraceMark=Rs.getString("ExternalGraceMark");
                }
if(Rs.getString("InternalGraceMark").equals("0"))
                {
                    Tab.InternalGraceMark=" ";
                }
                else
                {
                     Tab.InternalGraceMark=Rs.getString("InternalGraceMark");
                }
                if(Rs.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
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
                    if(Rs.getString("HasNoExternal").equals("1"))
                    {
                        Tab.ExternalMark=" ";
                    }else{
                    Tab.ExternalMark=Rs.getString("ExternalMark");}
                    if(Rs.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs.getString("ModerationExt");
                    }
                     if(Rs.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs.getString("ClassModeration");
                    }
                    if(Rs.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs.getString("ModerationInt");
                    }
                

                   /* if(Rs.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {*/
                        Tab.InternalMark=Rs.getString("InternalMark");
                   // }
                    Tab.TotalMark="";
                    SemTotal+=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration");

                    if(Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")>=Rs.getInt("ExternalMin") && Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("InternalMin") && Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("PassMark"))
                    {
                        int stTotal=Integer.parseInt(getSemTotalForStudent_Old(StudentId, Sem, con));
                        int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration");
                        int passMark=Rs.getInt("PassMark");
                        //for llm
                      
                       int smark1=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark1=Rs.getInt("PMark");
                       if(stTotal<200 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}
                         if (Rs.getInt("IsPassed")==0){
                        Tab.Status="Failed";}
                       else{Tab.Status="Passed";}
                        Integer Total=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration");
                        Tab.GrandTotal+=Total;
                        Tab.TotalMark=Total.toString();
                        if(Rs.getInt("IsPassed")==0)
                        {  Tab.TotalMark=" ";}
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
         public ArrayList<TabulationData> getStudentMarkDetails_BTSPract(String StudentId,String Sem,Connection con)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            PreparedStatement st = con.prepareStatement("SELECT m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.HasInternal,b.HasPractical,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark,m.InternalMark,m.PracticalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.IsPracticalAbsent,m.ClassModeration FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and b.CurrentYearSem=?   and  m.IsValid=1 and m.MarkStatus=1 order by  b.PaperNo ");
            st.setString(1, StudentId);

            st.setString(2, Sem);

            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs.getString("ExamId");
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
                    Tab.PracticalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
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
                     if(Rs.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs.getString("ClassModeration");
                    }
                    if(Rs.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs.getString("ModerationInt");
                    }
            if(Rs.getString("HasPractical").equals("0"))
                {
                    Tab.PracticalMark=" ";
                }
                else
                {
                    if(Rs.getInt("IsPracticalAbsent")==1){
                         Tab.PracticalMark="A";
                          Tab.Status="Absent";
                    }else{
                     Tab.PracticalMark=Rs.getString("PracticalMark");}
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
                    SemTotal+=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("PracticalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration");

                    if(Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")>=Rs.getInt("ExternalMin") && Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("InternalMin") && Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("PracticalMark")+Rs.getInt("ModerationInt")>=Rs.getInt("PassMark"))
                    {
                        int stTotal=Integer.parseInt(getSemTotalForStudent_OldBTS(StudentId, Sem, con));
                        int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("InternalMark")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration")+Rs.getInt("PracticalMark");
                        int passMark=Rs.getInt("PassMark");
                        //for llm

                        /*int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark=Rs.getInt("PMark");
                       if(stTotal<175 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}*/
                         if (smark<passMark){
                        Tab.Status="Failed";}
                       else{
                             if(Rs.getInt("IsPracticalAbsent")==1){
                         Tab.Status="Absent";}else{
                             Tab.Status="Passed";}}
                        Integer Total=Rs.getInt("ExternalMark")+Rs.getInt("InternalMark")+Rs.getInt("ModerationExt")+Rs.getInt("ModerationInt")+Rs.getInt("ClassModeration")+Rs.getInt("PracticalMark");
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
      public ArrayList<TabulationData> getStudentMarkDetails_Bcom(String StudentId)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        Connection con6=null;
        try
        {
              con6 = new DBConnection().getConnection();
            PreparedStatement st1 = con6.prepareStatement("select * from " +
                    "(SELECT b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal, " +
                    "b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, " +
                    "m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total ," +
                    "s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM " +
                    "StudentSubjectMark m " +
                    "inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId " +
                    "inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=?" +
                    "and m.IsValid=1 and m.MarkStatus=1 and PartNo='PART-I' " +
                    "Union " +
                    " SELECT b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal," +
                    " b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, " +
                    "m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , " +
                    "s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM " +
                    " StudentSubjectMark m " +
                    " inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  " +
                    " inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? " +
                    " and m.IsValid=1 and m.MarkStatus=1 and ExamId=7 and PartNo!='PART-I' ) r order by PaperNo");
            st1.setString(1, StudentId);
st1.setString(2, StudentId);

            ResultSet Rs1=st1.executeQuery();
            while(Rs1.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs1.getString("ExamId");
                Tab.StudentId=Rs1.getString("StudentId");
                Tab.SubjectBranchId=Rs1.getString("SubjectBranchId");
                Tab.SubjectName=Rs1.getString("SubjectName");
                if(Rs1.getString("ExternalMin").equals("0"))
                {
                    Tab.ExtMin=" ";
                }
                else
                {
                     Tab.ExtMin=Rs1.getString("ExternalMin");
                }
                if(Rs1.getString("InternalMin").equals("0"))
                {
                    Tab.IntMin=" ";
                }
                else
                {
                     Tab.IntMin=Rs1.getString("InternalMin");
                }
                if(Rs1.getString("PassMark").equals("0"))
                {
                    Tab.PassMark=" ";
                }
                else
                {
                     Tab.PassMark=Rs1.getString("PassMark");
                }


                if(Rs1.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
                    SemTotal=0;
                    if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
                    Tab.ExternalMark=Rs1.getString("ExternalMark");
                    if(Rs1.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs1.getString("ModerationExt");
                    }
                     if(Rs1.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs1.getString("ClassModeration");
                    }
                    if(Rs1.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs1.getString("ModerationInt");
                    }


                    if(Rs1.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {
                        Tab.InternalMark=Rs1.getString("InternalMark");
                    }
                    Tab.TotalMark="";
                    SemTotal+=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");

                    if(Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")>=Rs1.getInt("ExternalMin") && Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("InternalMin") && Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("PassMark"))
                    {
                        //int stTotal=Integer.parseInt(getSemTotalForStudent_Bcom(StudentId));
                        int smark=Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        int passMark=Rs1.getInt("PassMark");
                        //for llm

                        /*int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark=Rs.getInt("PMark");
                       if(stTotal<175 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}*/
                         if (smark<passMark){
                        Tab.Status="Failed";}
                       else{Tab.Status="Passed";}
                        Integer Total=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        Tab.GrandTotal+=Total;
                        Tab.TotalMark=Total.toString();
                    }
                    else
                    {
                        Tab.Status="Failed";
                        Tab.TotalMark=" ";
                        if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally{
            if(con6!=null)
            {
                try {
                    con6.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
      
      public ArrayList<TabulationData> getStudentMarkDetails_Bcom2013(String StudentId)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        Connection con6=null;
        try
        {
              con6 = new DBConnection().getConnection();
            PreparedStatement st1 = con6.prepareStatement("select * from "
                    + " (SELECT b.CurrentYearSem,b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal, "
                    + "  b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, "
                    + " m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , "
                    + " s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM "
                    + " StudentSubjectMark m "
                    + " inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? "
                    + " and m.IsValid=1 and m.MarkStatus=1 and PartNo='PART-I' "
                    + " Union "
                    + " SELECT b.CurrentYearSem,b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal, "
                    + " b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, "
                    + " m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , "
                    + " s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM "
                    + " StudentSubjectMark m "
                    + " inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? "
                    + " and m.IsValid=1 and m.MarkStatus=1 and b.CurrentYearSem=1 and PartNo='PART-II' "
                    
                    + " Union "
                    + " SELECT b.CurrentYearSem,b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal, "
                    + " b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, "
                    + " m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , "
                    + " s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM "
                    + " StudentSubjectMark m "
                    + " inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId "
                    + " inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? "
                    + " and m.IsValid=1 and  m.MarkStatus=1 and PartNo='PART-III'"
                    + " ) r "
                    + " order by PaperNo");
            st1.setString(1, StudentId);
st1.setString(2, StudentId);
st1.setString(3, StudentId);
            ResultSet Rs1=st1.executeQuery();
            while(Rs1.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs1.getString("ExamId");
                Tab.StudentId=Rs1.getString("StudentId");
                Tab.SubjectBranchId=Rs1.getString("SubjectBranchId");
                Tab.SubjectName=Rs1.getString("SubjectName");
                if(Rs1.getString("ExternalMin").equals("0"))
                {
                    Tab.ExtMin=" ";
                }
                else
                {
                     Tab.ExtMin=Rs1.getString("ExternalMin");
                }
                if(Rs1.getString("InternalMin").equals("0"))
                {
                    Tab.IntMin=" ";
                }
                else
                {
                     Tab.IntMin=Rs1.getString("InternalMin");
                }
                if(Rs1.getString("PassMark").equals("0"))
                {
                    Tab.PassMark=" ";
                }
                else
                {
                     Tab.PassMark=Rs1.getString("PassMark");
                }


                if(Rs1.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
                    SemTotal=0;
                    if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
                    Tab.ExternalMark=Rs1.getString("ExternalMark");
                    if(Rs1.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs1.getString("ModerationExt");
                    }
                     if(Rs1.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs1.getString("ClassModeration");
                    }
                    if(Rs1.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs1.getString("ModerationInt");
                    }


                    if(Rs1.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {
                        Tab.InternalMark=Rs1.getString("InternalMark");
                    }
                    Tab.TotalMark="";
                    SemTotal+=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");

                    if(Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")>=Rs1.getInt("ExternalMin") && Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("InternalMin") && Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("PassMark"))
                    {
                        //int stTotal=Integer.parseInt(getSemTotalForStudent_Bcom(StudentId));
                        int smark=Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        int passMark=Rs1.getInt("PassMark");
                        //for llm

                        /*int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark=Rs.getInt("PMark");
                       if(stTotal<175 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}*/
                         if (smark<passMark){
                        Tab.Status="Failed";}
                       else{Tab.Status="Passed";}
                        Integer Total=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        Tab.GrandTotal+=Total;
                        Tab.TotalMark=Total.toString();
                    }
                    else
                    {
                        Tab.Status="Failed";
                        Tab.TotalMark=" ";
                        if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally{
            if(con6!=null)
            {
                try {
                    con6.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
      
       public ArrayList<TabulationData> getStudentMarkDetails_BcomIII(String StudentId)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        Connection con6=null;
        try
        {
              con6 = new DBConnection().getConnection();
            PreparedStatement st1 = con6.prepareStatement(" SELECT b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal, " +
                    "b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, " +
                    "m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total ," +
                    "s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM " +
                    "StudentSubjectMark m " +
                    "inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId " +
                    "inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=?" +
                    "and m.IsValid=1 and m.MarkStatus=1 and PartNo='PART-III' " +
                    
                    " order by b.CurrentYearSem,b.PaperNo");
            st1.setString(1, StudentId);


            ResultSet Rs1=st1.executeQuery();
            while(Rs1.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs1.getString("ExamId");
                Tab.StudentId=Rs1.getString("StudentId");
                Tab.SubjectBranchId=Rs1.getString("SubjectBranchId");
                Tab.SubjectName=Rs1.getString("SubjectName");
                if(Rs1.getString("ExternalMin").equals("0"))
                {
                    Tab.ExtMin=" ";
                }
                else
                {
                     Tab.ExtMin=Rs1.getString("ExternalMin");
                }
                if(Rs1.getString("InternalMin").equals("0"))
                {
                    Tab.IntMin=" ";
                }
                else
                {
                     Tab.IntMin=Rs1.getString("InternalMin");
                }
                if(Rs1.getString("PassMark").equals("0"))
                {
                    Tab.PassMark=" ";
                }
                else
                {
                     Tab.PassMark=Rs1.getString("PassMark");
                }


                if(Rs1.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
                    SemTotal=0;
                    if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
                    Tab.ExternalMark=Rs1.getString("ExternalMark");
                    if(Rs1.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs1.getString("ModerationExt");
                    }
                     if(Rs1.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs1.getString("ClassModeration");
                    }
                    if(Rs1.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs1.getString("ModerationInt");
                    }


                    if(Rs1.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {
                        Tab.InternalMark=Rs1.getString("InternalMark");
                    }
                    Tab.TotalMark="";
                    SemTotal+=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");

                    if(Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")>=Rs1.getInt("ExternalMin") && Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("InternalMin") && Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("PassMark"))
                    {
                        //int stTotal=Integer.parseInt(getSemTotalForStudent_Bcom(StudentId));
                        int smark=Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        int passMark=Rs1.getInt("PassMark");
                        //for llm

                        /*int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark=Rs.getInt("PMark");
                       if(stTotal<175 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}*/
                         if (smark<passMark){
                        Tab.Status="Failed";}
                       else{Tab.Status="Passed";}
                        Integer Total=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        Tab.GrandTotal+=Total;
                        Tab.TotalMark=Total.toString();
                    }
                    else
                    {
                        Tab.Status="Failed";
                        Tab.TotalMark=" ";
                        if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally{
            if(con6!=null)
            {
                try {
                    con6.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
       public ArrayList<TabulationData> getStudentMarkDetails_BcomPartII(String StudentId)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int SemTotal=0;
        Boolean flag=true,EngFail=false;
        Connection con6=null;
        try
        {
              con6 = new DBConnection().getConnection();
            PreparedStatement st1 = con6.prepareStatement(" SELECT b.PaperNo,m.ExamId, m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal, " +
                    "b.HasInternal,b.PartNo,b.PassMark,b.TotalMark/2 as PMark, m.ExternalMark, " +
                    "m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total ," +
                    "s.SubjectName,b.SubjectBranchId,m.IsAbsent,m.ClassModeration FROM " +
                    "StudentSubjectMark m " +
                    "inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId " +
                    "inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=?" +
                    "and m.IsValid=1 and m.MarkStatus=1  and PartNo='PART-II' " +
                    
                    " order by b.CurrentYearSem,b.PaperNo");
            st1.setString(1, StudentId);


            ResultSet Rs1=st1.executeQuery();
            while(Rs1.next())
            {
                TabulationData Tab=new TabulationData();
                Tab.oldExam=Rs1.getString("ExamId");
                Tab.StudentId=Rs1.getString("StudentId");
                Tab.SubjectBranchId=Rs1.getString("SubjectBranchId");
                Tab.SubjectName=Rs1.getString("SubjectName");
                if(Rs1.getString("ExternalMin").equals("0"))
                {
                    Tab.ExtMin=" ";
                }
                else
                {
                     Tab.ExtMin=Rs1.getString("ExternalMin");
                }
                if(Rs1.getString("InternalMin").equals("0"))
                {
                    Tab.IntMin=" ";
                }
                else
                {
                     Tab.IntMin=Rs1.getString("InternalMin");
                }
                if(Rs1.getString("PassMark").equals("0"))
                {
                    Tab.PassMark=" ";
                }
                else
                {
                     Tab.PassMark=Rs1.getString("PassMark");
                }


                if(Rs1.getInt("IsAbsent")==1)
                {
                    Tab.ExternalMark="A";
                    Tab.ModerationExt=" ";
                    Tab.ModerationInt=" ";
                    Tab.InternalMark=" ";
                    Tab.TotalMark=" ";
                    Tab.Status="Absent";
                   Tab.ClassModeration=" ";
                    SemTotal=0;
                    if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
                    Tab.ExternalMark=Rs1.getString("ExternalMark");
                    if(Rs1.getString("ModerationExt").equals("0"))
                    {
                        Tab.ModerationExt=" ";
                    }
                    else
                    {
                        Tab.ModerationExt=Rs1.getString("ModerationExt");
                    }
                     if(Rs1.getString("ClassModeration").equals("0"))
                    {
                        Tab.ClassModeration=" ";
                    }
                    else
                    {
                        Tab.ClassModeration=Rs1.getString("ClassModeration");
                    }
                    if(Rs1.getString("ModerationInt").equals("0"))
                    {
                        Tab.ModerationInt=" ";
                    }
                    else
                    {
                        Tab.ModerationInt=Rs1.getString("ModerationInt");
                    }


                    if(Rs1.getString("HasInternal").equals("0"))
                    {
                        Tab.InternalMark=" ";
                    }
                    else
                    {
                        Tab.InternalMark=Rs1.getString("InternalMark");
                    }
                    Tab.TotalMark="";
                    SemTotal+=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");

                    if(Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")>=Rs1.getInt("ExternalMin") && Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("InternalMin") && Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")>=Rs1.getInt("PassMark"))
                    {
                        //int stTotal=Integer.parseInt(getSemTotalForStudent_Bcom(StudentId));
                        int smark=Rs1.getInt("ExternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        int passMark=Rs1.getInt("PassMark");
                        //for llm

                        /*int smark=Rs.getInt("ExternalMark")+Rs.getInt("ModerationExt");
                        int passMark=Rs.getInt("PMark");
                       if(stTotal<175 && smark<passMark){
                        Tab.Status="Failed";}
                        else{Tab.Status="Passed";}*/
                         if (smark<passMark){
                        Tab.Status="Failed";}
                       else{Tab.Status="Passed";}
                        Integer Total=Rs1.getInt("ExternalMark")+Rs1.getInt("InternalMark")+Rs1.getInt("ModerationExt")+Rs1.getInt("ModerationInt")+Rs1.getInt("ClassModeration");
                        Tab.GrandTotal+=Total;
                        Tab.TotalMark=Total.toString();
                    }
                    else
                    {
                        Tab.Status="Failed";
                        Tab.TotalMark=" ";
                        if(Rs1.getInt("SubjectBranchId")!=57 && Rs1.getInt("SubjectBranchId")!=51 && Rs1.getInt("SubjectBranchId")!=160 && Rs1.getInt("SubjectBranchId")!=154)
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally{
            if(con6!=null)
            {
                try {
                    con6.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
 public ArrayList<TabulationData> getStudentTRDetails_BcomPartIII(String ExamId,String Sem,Connection con,String CourseId, String CentreId)
    {
        ArrayList<TabulationData> StudentMark=new ArrayList<TabulationData>();
        int PrevStudentId=-1;
        Integer SemTotal=0;
        Boolean flag=true,EngFail=false;
        try
        {
            String Query="SELECT p.StudentId,p.StudentName,p.PRN,c.CollegeName,b.DisplayName,sb.CurrentYearSem,s.SubjectName,sb.ExternalMin,sb.InternalMin,sb.IsAddedWithTotal,sb.HasInternal,sb.PartNo,sb.PassMark,m.ExternalMark,m.InternalMark,m.ModerationExt,m.ModerationInt,m.ExternalMark+m.InternalMark as total , s.SubjectName,sb.SubjectBranchId,m.IsAbsent,m.IsMalPractice FROM `DEMS_db`.`StudentSubjectMark` m inner join StudentPersonal p on p.StudentId=m.StudentId  inner join CollegeMaster c on c.CollegeId=p.CollegeId inner join BranchMaster b on b.BranchId=p.BranchId inner join SubjectBranchMaster sb on sb.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on s.SubjectId=sb.SubjectId where m.IsValid=1 and p.BranchId=?  and m.MarkStatus=1 and sb.PartNo='PART-III'  and m.StudentId=2116  ";
            if(!CentreId.equals("-1"))
            {
                Query+=" and p.CollegeId="+CentreId;
            }
            Query+=" order by c.CollegeName,b.DisplayName,p.PRN,sb.CurrentYearSem,sb.PartNo,sb.PaperNo";
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, CourseId);
            
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
            Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
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
            PreparedStatement st=con.prepareStatement("select distinct sum(TotalMark) from SubjectBranchMaster where Branchid=? and CurrentYearSem=? group by SubBranchId");
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

    public String getSemTotalForStudent(String StudentId,String ExamId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.ExamId=?");
            st.setInt(1, Sem);
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
     public int getSemTotalForStudent_BLiSc(int StudentId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.MarkStatus=1");
            st.setInt(1, Sem);
            st.setInt(2, StudentId);
         
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
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
    }
  public String getSemTotalForStudent_Old(String StudentId,String Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.MarkStatus=1 and s.IsValid=1");
            st.setString(1, Sem);
            st.setString(2, StudentId);

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
    public String getSemTotalForStudent_OldBTS(String StudentId,String Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt+s.PracticalMark) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.MarkStatus=1 and s.IsValid=1");
            st.setString(1, Sem);
            st.setString(2, StudentId);

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
  public String getSemTotalForStudent_Bcom(String StudentId) throws SQLException
    {Connection con1=null;
         try
        {  con1 = new DBConnection().getConnection();
            PreparedStatement st2 = con1.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId  and s.StudentId=? where m.IsAddedWithTotal=1 and s.MarkStatus=1 and s.IsValid=1");

            st2.setString(1, StudentId);

            ResultSet Rs5=st2.executeQuery();
            while(Rs5.next())
            {
                return Rs5.getString("Total");
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
        if(con1!=null)
        {
                try {
                    con1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
         
    }
    public int getTotalForEnglish(String StudentId,String ExamId,Connection con)
    {
         try
        {
            PreparedStatement st = con.prepareStatement("select sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId where m.StudentId=? and b.PartNo='Part-I'  and m.IsValid=1 and m.MarkStatus=1");
            st.setString(1, StudentId);
         
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
 public int getTotalForEnglishBcom(String StudentId,String ExamId)
    {Connection con3=null;
         try
        {  con3 = new DBConnection().getConnection();
            PreparedStatement st5 = con3.prepareStatement("select sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId where m.StudentId=? and b.PartNo='PART-I'  and m.IsValid=1 and m.MarkStatus=1");
            st5.setString(1, StudentId);
            ResultSet Rs5=st5.executeQuery();
            while(Rs5.next())
            {
                return Rs5.getInt("TotalMark");
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
        if(con3!=null)
        {
                try {
                    con3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        }
       
       
    }
 public int getSyllabusofSubject(String SubjectBranchId)
    {Connection con3=null;
         try
        {  con3 = new DBConnection().getConnection();
            PreparedStatement st5 = con3.prepareStatement("SELECT AcademicYear FROM SubjectBranchMaster where SubjectBranchId=?");
            st5.setString(1, SubjectBranchId);
            ResultSet Rs5=st5.executeQuery();
            while(Rs5.next())
            {
                return Rs5.getInt("AcademicYear");
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
        if(con3!=null)
        {
                try {
                    con3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        }
       
       
    }
    public boolean IsStudentPassedForEnglish(String StudentId,String ExamId,Connection con)
    {
        boolean flag=true;
        try
        {
            PreparedStatement st = con.prepareStatement("select sum(m.ExternalMark+m.InternalMark+m.ModerationExt+m.ModerationInt) as TotalMark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId  where  m.StudentId=? and m.MarkStatus=1 and m.IsValid=1  and b.PartNo='Part-I' and (b.CurrentYearSem=1 or b.CurrentYearSem=2)  and m.StudentId not in( select distinct StudentId  from StudentSubjectMark s inner join SubjectBranchMaster b1 on s.SubjectBranchId=b1.SubjectBranchId  where (s.IsAbsent=1  or s.IsMalPractice=1) and s.MarkStatus=1 and s.IsValid=1 and b1.PartNo='Part-I' and (b1.CurrentYearSem=1 or b1.CurrentYearSem=2))");
            st.setString(1, StudentId);
           
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                if(Rs.getInt("TotalMark")<40)
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
    
    public boolean IsStudentPassedForEnglishBcom(String StudentId,String ExamId)
    {
        boolean flag=true;
        Connection con4=null;
        try
        {
  con4 = new DBConnection().getConnection();
            PreparedStatement st4 = con4.prepareStatement("select sum(m.ExternalMark+m.InternalMark+m.ModerationExt+m.ModerationInt) as TotalMark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId  where  m.StudentId=? and m.MarkStatus=1 and m.IsValid=1 and b.PartNo='PART-I'  and m.StudentId not in( select distinct StudentId  from StudentSubjectMark s inner join SubjectBranchMaster b1 on s.SubjectBranchId=b1.SubjectBranchId  where (s.IsAbsent=1  or s.IsMalPractice=1) and s.MarkStatus=1 and s.IsValid=1 and b1.PartNo='PART-I')");
            st4.setString(1, StudentId);
            ResultSet Rs4=st4.executeQuery();
            while(Rs4.next())
            {
                if(Rs4.getInt("TotalMark")<70)
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
         finally
    {
        if(con4!=null)
        {
                try {
                    con4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    }

      public boolean IsStudentRalForEnglishBcom(String StudentId,String ExamId)
    {
        boolean flag=true;
        Connection con4=null;
        try
        {
  con4 = new DBConnection().getConnection();
            PreparedStatement st4 = con4.prepareStatement("SELECT * FROM `DEMS_db`.`StudentExamWithheldDetails` where StudentId=? and EndDate is null");
            st4.setString(1, StudentId);
            ResultSet Rs4=st4.executeQuery();
            while(Rs4.next())
            {
                
                    flag=false;
              
            }
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
         finally
    {
        if(con4!=null)
        {
                try {
                    con4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TabulationRegister.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    }
    //NewMethod Added on 1/1/13
     public boolean IsStudentPassedForSemester(int StudentId,String Sem,String BranchId,Connection con)
    {
        boolean flag=true;
        int semester=Integer.parseInt(Sem);
        try {
            
            int ExamId=getCurrentExamId(con);
            if(semester>2 || semester<2)
                    {ExamId=10;}
            if(BranchId.equals("13")||BranchId.equals("18")||BranchId.equals("26") )
            {
              
                if(semester==4){
                    
                      if(getSemTotalForStudent_BLiSc( StudentId,semester, con)>=getTotalPassMarkForBLiSc(BranchId, Sem, con))
                {
                    
                    PreparedStatement st=con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and m.MarkStatus=1  and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    //PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    st.setInt(1, StudentId);
                    st.setInt(2,semester);
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
                }else if(semester==3){
                
                if(getSemTotalForStudent_BLiSc( StudentId,semester, con)>=275)
                {
                    
                    PreparedStatement st=con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and m.MarkStatus=1  and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    //PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    st.setInt(1, StudentId);
                    st.setInt(2,semester);
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
            }}
            else
            {
                //System.out.println("amrita");
                PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.HasPractical,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,m.PracticalMark,m.IsPracticalAbsent,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal,m.ModerationExt  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and b.CurrentYearSem=? and m.MarkStatus=1 order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                st.setInt(1, StudentId);
              //  st.setInt(2, ExamId);
                st.setInt(2,semester);
                ResultSet rs=st.executeQuery();
                while(rs.next())
                {
                     if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithGrandTotal")==1)
                    {
                        flag=false;
                    }
                     else if(rs.getInt("IsPracticalAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                   // else if((rs.getInt("ExternalMark")+rs.getInt("ModerationExt"))>=rs.getInt("ExternalMin") )
                    else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") )
                    {
                        if(rs.getInt("HasPractical")==1){
                             if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark")+ rs.getInt("PracticalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                    if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }else{
                                 if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark")+ rs.getInt("PracticalMark") < rs.getInt("PassMark")){
                                     {
                                         flag=false;
                                     }
                                 }
                             }
                        }
                        else
                        {
                            if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark") +rs.getInt("ModerationExt")>= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                 if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                     if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                            
                        }
                       
                       
                    }
                    else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                }
            }
            System.out.println(StudentId+"-"+flag+"-"+Sem);
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }
public boolean IsStudentPassedForSemesterLLM(int StudentId,String Sem,String BranchId,Connection con)
    {
        boolean flag=true;
        int semester=Integer.parseInt(Sem);
        try {
            int ExamId=getCurrentExamId(con);
            if(semester>2 || semester<4)
                    {ExamId=7;}
            if(BranchId.equals("13")||BranchId.equals("18")||BranchId.equals("26"))
            {
                
                if(semester==2){
                if(getSemTotalForStudent_BLiSc( StudentId,semester, con)>=getTotalPassMarkForBLiSc(BranchId, Sem, con))
                {
                    
                    PreparedStatement st=con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,m.ModerationExt,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal,b.HasPractical FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and m.MarkStatus=1  and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    //PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    st.setInt(1, StudentId);
                    st.setInt(2,semester);
                    ResultSet rs=st.executeQuery();
                   while(rs.next())
                {
                     if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithGrandTotal")==1)
                    {
                        flag=false;
                    }
                     else if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                    else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") )
                    {
                        if(rs.getInt("HasPractical")==1){
                             if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark")+ rs.getInt("PracticalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                    if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }
                        }
                        else
                        {
                            if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark") +rs.getInt("ModerationExt")>= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                 if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                     if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                            
                        }
                       
                       
                    }
                    else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
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
            }else if(semester==3){
                int r1=getSemTotalForStudent_BLiSc( StudentId,semester, con);
             if(r1>=275)
                {
                    
                    PreparedStatement st=con.prepareStatement("SELECT b.HasPractical,m.IsPassed,m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and m.MarkStatus=1  and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    //PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    st.setInt(1, StudentId);
                    st.setInt(2,semester);
                    ResultSet rs=st.executeQuery();
                   while(rs.next())
                {
                     if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithGrandTotal")==1)
                    {
                        flag=false;
                    }
                     else if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                     if(rs.getInt("IsPassed")==0)
 
                     {
                         flag=false;
                     }
                    else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") )
                    {
                        if(rs.getInt("HasPractical")==1){
                             if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark")+ rs.getInt("PracticalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                    if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }
                        }
                        else
                        {
                            if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                 if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                     if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                            
                        }
                       
                       
                    }
                    else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                }

                }
            else
            {
                PreparedStatement st = con.prepareStatement("SELECT m.IsPassed,m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.HasPractical,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,m.PracticalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal,m.ModerationExt  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and b.CurrentYearSem=? and m.MarkStatus=1 order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                st.setInt(1, StudentId);
              //  st.setInt(2, ExamId);
                st.setInt(2,semester);
                ResultSet rs=st.executeQuery();
                while(rs.next())
                {
                     if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithGrandTotal")==1)
                    {
                        flag=false;
                    }
                     else if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                    else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") )
                    {
                        if(rs.getInt("HasPractical")==1){
                             if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark")+ rs.getInt("PracticalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                    if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }
                        }
                        else
                        {
                            if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                 if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                     if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                            
                        }
                       
                       
                    }
                    else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                }
            }}}
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }
public boolean IsStudentPassedForSemesterLLM_Semester(int StudentId,String Sem,String BranchId,Connection con)
    {
        boolean flag=true;
         int semester=Integer.parseInt(Sem);
        try {
            int ExamId=getCurrentExamId(con);
            if(semester>1 || semester<5)
                    {ExamId=7;}
            if(BranchId.equals("13")||BranchId.equals("18")||BranchId.equals("26"))
            {
                
                if(semester==1||semester==2||semester==3||semester==4){
                if(getSemTotalForStudent_BLiSc( StudentId,semester, con)>=getTotalPassMarkForBLiSc(BranchId, Sem, con))
                {
                    
                    PreparedStatement st=con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.IsAddedWithGrandTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,m.ModerationExt,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal,b.HasPractical,m.IsPassed FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1 and m.MarkStatus=1  and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    //PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.ExamId=? and m.IsValid=1 and b.CurrentYearSem=? order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                    st.setInt(1, StudentId);
                    st.setInt(2,semester);
                    ResultSet rs=st.executeQuery();
                   while(rs.next())
                {
                     if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithGrandTotal")==1)
                    {
                        flag=false;
                    }
                     else if(rs.getInt("IsAbsent")==1 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                     else if(rs.getInt("IsPassed")==0 && rs.getInt("IsAddedWithTotal")==1)
                    {
                        flag=false;
                    }
                    else if(rs.getInt("ExternalMark")>=rs.getInt("ExternalMin") )
                    {
                        if(rs.getInt("HasPractical")==1){
                             if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark")+ rs.getInt("PracticalMark") >= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                    if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }
                        }
                        else
                        {
                            if(  rs.getInt("ExternalMark") + rs.getInt("InternalMark") +rs.getInt("ModerationExt")>= rs.getInt("PassMark")){
                                 if(rs.getInt("HasInternal")==1)
                            {
                                 if(rs.getInt("InternalMark")>=rs.getInt("InternalMin"))
                                {

                                }
                                else
                                {
                                     if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                                }
                        }
                                 
                             }else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
                                    {
                                    flag=false;
                                    }
                    }
                            
                        }
                       
                       
                    }
                    else
                    {
                         if(rs.getInt("IsAddedWithGrandTotal")==1)
                                    {
                                    flag=false;
                                    }
                                   else if(rs.getInt("IsAddedWithTotal")==1)
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
            }}
            System.out.println(StudentId+"-%%%%%%%%%%%%%%%%%%%%%%%%%-"+Sem+"-"+flag);
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
           PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt+s.ClassModeration+s.PracticalMark+s.ExternalGraceMark+s.InternalGraceMark) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithGrandTotal=1 and s.hasAdditional=0 and s.MarkStatus=1 and s.IsValid=1");
            //PreparedStatement st = con.prepareStatement("SELECT TotalMark  as Total from  DEMS_db.StudentPassDetails where YearOrSem=? and StudentId=?");
           
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
    public String getSubjectSemTotal(int StudentId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt+s.ClassModeration+s.PracticalMark) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithGrandTotal=1 and s.MarkStatus=1");
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
     public boolean IsStudentPassedForBcomPartIII(int StudentId,int Sem,String BranchId,Connection con)
    {
        boolean flag=true;
        try {
            int ExamId=getCurrentExamId(con);
           
           
                PreparedStatement st = con.prepareStatement("SELECT m.StudentId,b.ExternalMin,b.InternalMin,b.IsAddedWithTotal,b.CurrentYearSem,b.TotalMark,b.HasInternal,b.PartNo,b.PassMark,m.ExternalMark+m.ModerationExt as ExternalMark,ifnull(m.InternalMark,0)+m.ModerationInt as InternalMark,s.SubjectName,b.SubjectBranchId,m.IsAbsent,b.ExternalMax,b.InternalMax,b.IsAddedWithTotal  FROM StudentSubjectMark m inner Join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.PartNo='PART-III' inner join SubjectMaster s on b.SubjectId=s.SubjectId where m.StudentId=? and m.IsValid=1  and m.MarkStatus=1 order by b.CurrentYearSem,b.PartNo,b.PaperNo");
                st.setInt(1, StudentId);
              //  st.setInt(2, ExamId);
                //st.setInt(2,Sem);
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
            
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }
      public String getSemTotalforBcomPartIII(int StudentId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt+s.ClassModeration) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and m.PartNo='PART-III' and s.StudentId=? where m.IsAddedWithTotal=1 and s.MarkStatus=1 and s.IsValid=1");
            //st.setInt(1, Sem);
            st.setInt(1, StudentId);
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
        public boolean IsBettermentStudent(int StudentId,String ExamId,Connection con)
    {
        boolean flag=false;
        try
        {
            PreparedStatement st = con.prepareStatement("select StudentId from BettermentDetails where ExamId=? and StudentId=?");
            st.setString(1, ExamId);
            st.setInt(2, StudentId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {        
                    flag=true;              
            }
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }
           public boolean IsAditionalDegreeStudent(int StudentId,String ExamId,Connection con)
    {
        boolean flag=false;
        try
        {
            PreparedStatement st = con.prepareStatement("select StudentId from AdditionalDegreeRegistration where  StudentId=?");
           // st.setString(1, ExamId);
            st.setInt(1, StudentId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {        
                    flag=true;              
            }
            return flag;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        }
    }
   
 public String getSemmaxExam(int StudentId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT max(ExamId) exam FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithGrandTotal=1 and s.MarkStatus=1 and s.IsValid=1");
            st.setInt(1, Sem);
            st.setInt(2, StudentId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                return Rs.getString("exam");
            }
            return " ";
        }
        catch (SQLException ex)
        {
            Logger.getLogger(UserCreation.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }

    }
   /* public static void main(String arg[])
    {
        
        
         Connection con = new DBConnection().getConnection();
         
         
         TabulationRegister tb=new TabulationRegister();
         tb.getStudentTRDetails_BcomPartIII("3",2, con,"7","-1");
    }*/
      
}
