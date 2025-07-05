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

/**
 *
 * @author HP
 */
public class PRNListforCentres {
Connection con;
 private ArrayList<String> StudentId=new ArrayList<String>();
    private ArrayList<String> StudentName=new ArrayList<String>();
    private ArrayList<String> PRN=new ArrayList<String>();
    private ArrayList<StudentData> StudentList=new ArrayList<StudentData>();

    public PRNListforCentres() throws SQLException
    {
        
    }

     
      public ArrayList<StudentData> getStudentList()
      {
          return StudentList;
      }
public ArrayList<StudentData> getStudentDetailsOfCentres(String CenterId) throws SQLException
    {
    con=new DBConnection().getConnection();
        try {
            String QueryStr = "select SQL_CALC_FOUND_ROWS SP.StudentId ,StudentName,SP.CollegeId, b.DisplayName,PRN from StudentPersonal SP,BranchMaster b where SP.isFinalSubmit=1 and IsMGUApproved=1 and  b.BranchId=SP.BranchId and CollegeId=? order by PRN ";
           
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, CenterId);

            ResultSet Records = Query.executeQuery();
            ArrayList<StudentData> StudentList = new ArrayList<StudentData>();
            StudentData Student = null;
            while (Records.next()) {
                Student = new StudentData();
                Student.StudentId = Records.getInt(1);
                Student.Name = Records.getString(2);              
                Student.Course = Records.getString(4);
                Student.PRN = Records.getString(5);
                StudentList.add(Student);
                System.out.println(Student.Name);
            }
            return StudentList;
        } catch (SQLException ex) {
            Logger.getLogger(PRNListforCentres.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<CourseData> getBranchs(String CentreId) throws SQLException
    {

     ArrayList<CourseData> CourseList=new ArrayList<CourseData>();
        try {
            con=new DBConnection().getConnection();
            String QueryStr = "select Distinct DisplayName,C.BranchId from BranchMaster B,CollegeBranchMap C where B.BranchId=C.BranchId and C.CollegeId=? ";
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, CentreId);
            ResultSet Records = Query.executeQuery();
            CourseData Data = null;
            while (Records.next()) {
                Data = new CourseData();
                Data.BranchId = Records.getInt(2);
                Data.BranchName = Records.getString(1);
                CourseList.add(Data);
                System.out.println(Data.BranchName);
            }
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(PRNListforCentres.class.getName()).log(Level.SEVERE, null, ex);
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

public ArrayList<CourseData> getBComMBABranchs(String CentreId) throws SQLException
    {

     ArrayList<CourseData> CourseList=new ArrayList<CourseData>();
        try {
            con=new DBConnection().getConnection();
            String QueryStr = "select Distinct DisplayName,C.BranchId from BranchMaster B,CollegeBranchMap C where B.BranchId=C.BranchId and C.CollegeId=? and B.BranchId in(7,8,21) order by B.DisplayName ";
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, CentreId);
            ResultSet Records = Query.executeQuery();
            CourseData Data = null;
            while (Records.next()) {
                Data = new CourseData();
                Data.BranchId = Records.getInt(2);
                Data.BranchName = Records.getString(1);
                CourseList.add(Data);
                System.out.println(Data.BranchName);
            }
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(PRNListforCentres.class.getName()).log(Level.SEVERE, null, ex);
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

 public void close() throws SQLException
    {
if(con!=null&&(!con.isClosed()))
{
    con.close();
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
    public String getPRN(int index)
    {
        return PRN.get(index);
    }
public boolean  contains(String SId)
    {
        return StudentId.contains(SId);
    }



public ArrayList<StudentData> getStudentsOfCourse(int CourseId) throws SQLException
    {

    try {
        con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select StudentId,StudentName,PRN from StudentPersonal where JoiningYear= (SELECT max(JoiningYear) FROM StudentPersonal) and IsMGUApproved=1 and PRN is not null and BranchId=? order by CollegeId");
            Query.setInt(1, CourseId);
            ResultSet Records = Query.executeQuery();
          //  ArrayList<StudentData> StudentList=new ArrayList<StudentData>();
            StudentData student;
            while (Records.next()) {
                student = new StudentData();
                student.StudentId = Records.getInt(1);
                student.Name = Records.getString(2);
                student.PRN = Records.getString(3);
                StudentList.add(student);
            }
            return StudentList;
        } catch (SQLException ex) {
            Logger.getLogger(PRNListforCentres.class.getName()).log(Level.SEVERE, null, ex);
            return  null;
        }
    finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
   }

public ArrayList<CourseData> getSubBranch(String BranchId) throws SQLException
{
    ArrayList<CourseData> SubBranch=new ArrayList<CourseData>();
     Connection con =new DBConnection().getConnection();
     try
    {
    PreparedStatement st=con.prepareStatement("Select SubBranchId,SubBranchName,DisplayName from SubBranchMaster where BranchId=? ");
    st.setString(1, BranchId);

    ResultSet Rs=st.executeQuery();
    while(Rs.next())
    {
        CourseData SubBr=new CourseData();
        SubBr.BranchId=Rs.getInt(1);
        SubBr.BranchName=Rs.getString(2);
        SubBranch.add(SubBr);
    }
    return SubBranch;
     }
     catch(SQLException ex)
        {
             Logger.getLogger(PRNListforCentres.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return  null;
        }
        finally
        {
            con.close();
        }
}

public ArrayList<CourseData> getOptionalSubjects(String BranchId) throws SQLException
{
    ArrayList<CourseData> SubBranch=new ArrayList<CourseData>();
     Connection con =new DBConnection().getConnection();
     try
    {
    PreparedStatement st=con.prepareStatement("SELECT b.SubjectBranchId,s.SubjectName from SubjectBranchMaster b,SubjectMaster s where b.SubjectId=s.SubjectId and b.IsElective=1 and b.IsOptionalSubject=1 and b.BranchId=? ");
    st.setString(1, BranchId);

    ResultSet Rs=st.executeQuery();
    while(Rs.next())
    {
        CourseData SubBr=new CourseData();
        SubBr.BranchId=Rs.getInt(1);
        SubBr.BranchName=Rs.getString(2);
        SubBranch.add(SubBr);
    }
    return SubBranch;
     }
     catch(SQLException ex)
        {
             Logger.getLogger(PRNListforCentres.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return  null;
        }
        finally
        {
            con.close();
        }
}

//public int getCountOfApproved
}
