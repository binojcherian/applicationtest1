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

public class StudentSubjects
{

public void InsertIntoStudentSubjectBranchMapBCom(int AttendingYear) throws Exception
{
    Connection con=null;
        try 
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT StudentId,StudentName,BranchId FROM `DEMS_db`.`StudentPersonal`where (BranchId =7 or BranchId=8) and AttendingYear=? and PRN is not null where StudentId in (select StudentId from StudentExamFeeStatus) ");
            st.setInt(1, AttendingYear);
            ResultSet Student=st.executeQuery();
            while (Student.next())
            {
                PreparedStatement st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=?  and IsOptionalSubject=0");
                st1.setString(1, Student.getString("BranchId"));
                st.setInt(2, AttendingYear);
                ResultSet Subject=st1.executeQuery();
                while(Subject.next())
                {
                    PreparedStatement stm=con.prepareStatement("select count(*) from ExamRegistrationMaster where StudentId=? and SubjectBranchId=? and ExamId=7");
                    stm.setString(1, Student.getString("StudentId"));
                    stm.setString(2, Subject.getString("SubjectBranchId"));
                    ResultSet result=stm.executeQuery();
                    while(result.next())
                    {
                        if(result.getInt(1)==0)
                        {
                            PreparedStatement st2=con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId) values(?,?,2)");
                            st2.setString(1, Student.getString("StudentId"));
                            st2.setString(2, Subject.getString("SubjectBranchId"));
                            st2.execute();
                        }
                    }
                    PreparedStatement stBr=con.prepareStatement("select count(*) from StudentSubjectBranchMap where StudentId=? and SubjectBranchId=? ");
                    stBr.setString(1, Student.getString("StudentId"));
                    stBr.setString(2, Subject.getString("SubjectBranchId"));
                    ResultSet result1=stBr.executeQuery();
                    while(result.next())
                    {
                        if(result1.getInt(1)==0)
                        {
                            PreparedStatement st2=con.prepareStatement("insert into StudentSubjectBranchMap (StudentId,SubjectBranchId) values(?,?)");
                            st2.setString(1, Student.getString("StudentId"));
                            st2.setString(2, Subject.getString("SubjectBranchId"));
                            st2.execute();
                        }
                    }
                    
                }
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(StudentSubjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    finally
    {
        con.close();
    }
}

 public int getYearOrSem(int CourseId) throws SQLException
    {
        Connection con =new DBConnection().getConnection();
    CourseData Course=new CourseData();
    try
    {
        PreparedStatement st=con.prepareStatement("select SemorYear,LastYearorSem from BranchMaster where BranchId=?");
        st.setInt(1, CourseId);
        ResultSet Rs=st.executeQuery();

        while(Rs.next())
        {
            return Rs.getInt("SemorYear");
        }
        return 0;
    }
    catch(SQLException ex)
        {
            Logger.getLogger(StudentSubjects.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return  0;
        }
        finally
        {
            con.close();
        }
    }
 
public void InsertAllSubjectsOfStudent(String BranchId,String year) throws SQLException
    {
        Connection con = null;
        try 
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT StudentId,StudentName,BranchId,AttendingYear FROM `DEMS_db`.`StudentPersonal`where BranchId=? and AttendingYear=? and PRN is not null and StudentId in (select StudentId from StudentExamFeeStatus) ");
            st.setString(1, BranchId);
            st.setString(2, year);
            ResultSet Student=st.executeQuery();
            while (Student.next())
            {
                 PreparedStatement st1 = null;
                int SemOrYear = getYearOrSem(Integer.parseInt(Student.getString("BranchId")));
                if (SemOrYear == 0 && Student.getString("AttendingYear").equals("1")) {
                    st1 = con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or CurrentYearSem=2) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or CurrentYearSem=2)) and IsOptionalSubject=0");
                    st1.setString(1, Student.getString("BranchId"));
                     st1.setString(2, Student.getString("BranchId"));
                } else if (SemOrYear == 0 && Student.getString("AttendingYear").equals("2")) {
                    st1 = con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4)) and IsOptionalSubject=0");
                    st1.setString(1, Student.getString("BranchId"));
                     st1.setString(2, Student.getString("BranchId"));
                } else {
                    st1 = con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=?  and IsOptionalSubject=0");
                    st1.setString(1, Student.getString("BranchId"));
                    st1.setString(2, Student.getString("AttendingYear"));
                }
                ResultSet Subject = st1.executeQuery();
                while (Subject.next())
                {
                    PreparedStatement stSub = con.prepareStatement("select count(*) from ExamRegistrationMaster where StudentId=? and SubjectBranchId=? and ExamId=7");
                    stSub.setString(1, Student.getString("StudentId"));
                    stSub.setString(2, Subject.getString("SubjectBranchId"));
                    ResultSet result = stSub.executeQuery();
                    while (result.next())
                    {
                        if (result.getInt(1) == 0)
                        {
                            PreparedStatement st2 = con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId) values(?,?,2)");
                            st2.setString(1, Student.getString("StudentId"));
                            st2.setString(2, Subject.getString("SubjectBranchId"));
                            st2.execute();
                        }
                    }
                    PreparedStatement stBr = con.prepareStatement("select count(*) from StudentSubjectBranchMap where StudentId=? and SubjectBranchId=? ");
                    stBr.setString(1, Student.getString("StudentId"));
                    stBr.setString(2, Subject.getString("SubjectBranchId"));
                    ResultSet result1 = stBr.executeQuery();
                    while (result1.next()) {
                        if (result1.getInt(1) == 0) {
                            PreparedStatement st2 = con.prepareStatement("insert into StudentSubjectBranchMap (StudentId,SubjectBranchId) values(?,?)");
                            st2.setString(1, Student.getString("StudentId"));
                            st2.setString(2, Subject.getString("SubjectBranchId"));
                            st2.execute();
                        }
                    }
                }
            }           
        }
        catch (Exception ex)
        {
            Logger.getLogger(StudentSubjects.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
        }
        finally
        {
            con.close();
        }
    }

public static void main(String[] args) throws Exception
    {
    StudentSubjects stu=new StudentSubjects();
    //stu.InsertIntoStudentSubjectBranchMapBCom();
    stu.InsertAllSubjectsOfStudent("18","2");
    }
}
