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


/**
 *
 * @author mgu
 */
public class ForExamConfirmation {

    ArrayList<StudentsForExam> Students;
    HttpServletRequest request;
    HttpServletResponse response;
 
    public boolean ConfirmExamRegistration(HttpServletRequest request,String BranchId,String CentreId,Connection con,String AttendingYear)
    {
       // ArrayList<String> Students=request.getParameterValues("SelectedStudents");
        StudentData Student;
        ArrayList<StudentData> StudentList=new ArrayList<StudentData>();
        if(request.getParameterValues("SelectedStudents")!=null)
                {
                String Stu[]=request.getParameterValues("SelectedStudents");
                 for (String SId : Stu) {
                    Student=new StudentData();
                    Student.StudentId=Integer.parseInt(SId);
                    StudentList.add(Student);
                 }
        }
        try
        {
            int Count=StudentList.size();
            PreparedStatement st = con.prepareStatement("Update StudentExamFeeStatus set IsConfirmed=1 where Studentid=? ");
            for(int i=0;i<Count;i++)
            {
                StudentData Stu=StudentList.get(i);
                st.setInt(1, Stu.StudentId);
                st.executeUpdate();
            }
            st=con.prepareStatement("update CourseDDMap set IsConfirmed =1  where CourseId=? and semoryear=? and ExamId=7 and DDId in (Select DDId from DDMaster where CentreId=?)");
            st.setString(1, BranchId);
            st.setString(2, AttendingYear);
            st.setString(3, CentreId);
            st.executeUpdate();
            return true;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(ForExamConfirmation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String getCentreName(String CentreId, Connection con)
    {
        try
        {
            PreparedStatement st = con.prepareStatement("select CollegeName from CollegeMaster where CollegeId=?");
            st.setString(1, CentreId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getString("CollegeName");
            }
            return " ";

        }
        catch (SQLException ex)
        {
            Logger.getLogger(ForExamConfirmation.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }

    }

    public String getCourseName(String BranchId, Connection con)
    {
        try
        {
            PreparedStatement st = con.prepareStatement("select DisplayName from BranchMaster where BranchId=?");
            st.setString(1, BranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getString("DisplayName");
            }
            return " ";

        }
        catch (SQLException ex)
        {
            Logger.getLogger(ForExamConfirmation.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }

    }
 public String getExamName(String ExamId, Connection con)
    {
        try
        {
            PreparedStatement st = con.prepareStatement("select ExamName from ExamMaster where ExamId=?");
            st.setString(1, ExamId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getString("ExamName");
            }
            return " ";

        }
        catch (SQLException ex)
        {
            Logger.getLogger(ForExamConfirmation.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }

    }
    public ArrayList<StudentsForExam> StudentList(String AttendingYear, String BranchId,String CentreId,Connection con)
    {

         Students=new ArrayList<StudentsForExam>();
      try {

            PreparedStatement st = con.prepareStatement("SELECT ifnull(p.PRN,''),p.StudentName,p.StudentId  FROM StudentExamFeeStatus s inner join StudentPersonal p on p.StudentId=s.StudentId where p.AttendingYear=? and p.CollegeId=? and p.BranchId=?");
            st.setString(1, AttendingYear);
            st.setString(2, CentreId);
            st.setString(3, BranchId);
            ResultSet Rs = st.executeQuery();

            StudentsForExam stud=null;
            while (Rs.next())
            {
                stud=new StudentsForExam();
                stud.PRN=Rs.getString(1);
                stud.StudentName=Rs.getString(2);
                stud.StudentId=Rs.getInt("StudentId");
                Students.add(stud);
            }
                 Rs.close();

          return  Students;

           } catch (SQLException ex) {
            Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Students;
        }

    }

    public ArrayList<StudentsForExam> ConfirmedStudentList(String AttendingYear, String BranchId,String CentreId,Connection con)
    {

         Students=new ArrayList<StudentsForExam>();
      try {

            PreparedStatement st = con.prepareStatement("SELECT ifnull(p.PRN,''),p.StudentName,p.StudentId  FROM StudentExamFeeStatus s inner join StudentPersonal p on p.StudentId=s.StudentId where p.AttendingYear=? and p.CollegeId=? and p.BranchId=? and s.IsConfirmed=1");
            st.setString(1, AttendingYear);
            st.setString(2, CentreId);
            st.setString(3, BranchId);
            ResultSet Rs = st.executeQuery();

            StudentsForExam stud=null;
            while (Rs.next())
            {
                stud=new StudentsForExam();
                stud.PRN=Rs.getString(1);
                stud.StudentName=Rs.getString(2);
                stud.StudentId=Rs.getInt("StudentId");
                Students.add(stud);
            }
                 Rs.close();

          return  Students;

           } catch (SQLException ex) {
            Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Students;
        }

    }

    public ArrayList<Entity.CourseData> getSubBranch(String BranchId,String YearorSem) throws SQLException
    {
        ArrayList<Entity.CourseData> SubBranch=new ArrayList<Entity.CourseData>();
         Connection con =new DBConnection().getConnection();
         try
        {
        PreparedStatement st=con.prepareStatement("Select SubBranchId,SubBranchName from SubBranchMaster where BranchId=? and StartingSem <=? ");
        st.setString(1, BranchId);
        st.setString(2, YearorSem);

        ResultSet Rs=st.executeQuery();
        while(Rs.next())
        {
            Entity.CourseData SubBr=new Entity.CourseData();
            SubBr.BranchId=Rs.getInt(1);
            SubBr.BranchName=Rs.getString(2);
            SubBranch.add(SubBr);
        }
        return SubBranch;
         }
         catch(SQLException ex)
            {
                 Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
                con.close();
                return  null;
            }
            finally
            {
                con.close();
            }
    }

    
    public ArrayList<StudentsForExam> MguApprovedStudentList(String AttendingYear, String BranchId,String CentreId,String ExamId,String studenttype,Connection con)
    {

         Students=new ArrayList<StudentsForExam>();
      try {

            PreparedStatement st = con.prepareStatement("SELECT ifnull(p.PRN,''),p.StudentName,p.StudentId  FROM StudentExamFeeStatus s inner join StudentPersonal p on p.StudentId=s.StudentId where s.AttendingYear=? and s.ExamCentre=? and s.BranchId=? and s.IsMguApproved=1  and ExamId =? and s.IsSupplementaryExam=? order by PRN");
            
            st.setString(1, AttendingYear);
            st.setString(2, CentreId);
            st.setString(3, BranchId);
             st.setString(4, ExamId);
             st.setString(5,studenttype);
             
            
            ResultSet Rs = st.executeQuery();

            StudentsForExam stud=null;
            while (Rs.next())
            {
                stud=new StudentsForExam();
                stud.PRN=Rs.getString(1);
                stud.StudentName=Rs.getString(2);
                stud.StudentId=Rs.getInt("StudentId");
                Students.add(stud);
            }
                // Rs.close();

          return  Students;

           } catch (SQLException ex) {
            Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Students;
        }

    }
 public ArrayList<StudentsForExam> MguApprovedStudentList_CourseCompleted(String AttendingYear, String BranchId,String CentreId,Connection con)
    {

         Students=new ArrayList<StudentsForExam>();
      try {

            PreparedStatement st = con.prepareStatement("SELECT ifnull(p.PRN,''),p.StudentName,p.StudentId  FROM StudentExamFeeStatus s inner join StudentPersonal p on p.StudentId=s.StudentId where s.AttendingYear=? and s.ExamCentre=? and s.BranchId=? and s.IsMguApproved=1 and p.CourseCompletedStatus=1 and ExamId =(select max(ExamId) from ExamRegistrationMaster) order by PRN");
            st.setString(1, AttendingYear);
            st.setString(2, CentreId);
            st.setString(3, BranchId);
            
            System.out.println("@@@@@"+st);
            ResultSet Rs = st.executeQuery();

            StudentsForExam stud=null;
            while (Rs.next())
            {
                stud=new StudentsForExam();
                stud.PRN=Rs.getString(1);
                stud.StudentName=Rs.getString(2);
                stud.StudentId=Rs.getInt("StudentId");
                Students.add(stud);
            }
                // Rs.close();

          return  Students;

           } catch (SQLException ex) {
            Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Students;
        }

    }

public ArrayList<StudentsForExam> MguApprovedStudentListForSubject(String SubjectBranchId,String CentreId,Connection con)
    {

         Students=new ArrayList<StudentsForExam>();
      try {

            PreparedStatement st = con.prepareStatement("SELECT ifnull(p.PRN,''),p.StudentName,p.StudentId  FROM StudentExamFeeStatus s inner join StudentPersonal p on p.StudentId=s.StudentId inner join ExamRegistrationMaster e on e.StudentId=s.StudentId and e.SubjectBranchId=? and e.ExamId=7  where s.IsMguApproved=1 and s.CollegeId=? order by PRN");
            st.setString(1, SubjectBranchId);
            st.setString(2, CentreId);
            ResultSet Rs = st.executeQuery();

            StudentsForExam stud=null;
            while (Rs.next())
            {
                stud=new StudentsForExam();
                stud.PRN=Rs.getString(1);
                stud.StudentName=Rs.getString(2);
                stud.StudentId=Rs.getInt("StudentId");
                Students.add(stud);
            }
                 Rs.close();

          return  Students;

           } catch (SQLException ex) {
            Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Students;
        }

    }

}