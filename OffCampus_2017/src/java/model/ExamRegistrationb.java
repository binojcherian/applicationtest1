/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;


import Entity.Amount;
import Entity.Subject;
import Entity.SubjectBranch;
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
public class ExamRegistrationb {
    ArrayList<String> BranchId,BranchCode ,BranchName,BranchDisplayName;
    ArrayList<CourseData> Branches;
    String SearchFilter,SearchBranch,SearchCode;

    HttpServletRequest request;
    HttpServletResponse response;



    public boolean insertExamRegister(String  BranchId,String CollegeId, String StudentId,String year) throws SQLException
    {
         Connection con = null;
         try {
           
            con = new DBConnection().getConnection();
            PreparedStatement st1=con.prepareStatement("SELECT count(*) FROM DEMS_db.StudentExamFeeStatus S WHERE S.`StudentId`=?");
            st1.setString(1, StudentId);
            ResultSet rs=st1.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==0)
                {
                    PreparedStatement st = con.prepareStatement("insert into StudentExamFeeStatus (StudentId,CollegeId,BranchId,Amount,FeeType,isConfirmed,AttendingYear,registerTime,IsMguApproved) values(?,?,?,?,?,?,?,sysdate(),1)");
                    st.setInt(1, Integer.parseInt(StudentId));
                    st.setInt(2, Integer.parseInt(CollegeId));
                    st.setInt(3, Integer.parseInt(BranchId));
                    mExamFeeNew examFeeNew=new mExamFeeNew();
                    Amount amount=examFeeNew.getTotalExamFeeForStudent(Integer.parseInt(StudentId), Integer.parseInt(CollegeId), Integer.parseInt(BranchId),Integer.parseInt(year), con);
                    st.setInt(4, (int)amount.getAmt());
                    st.setString(5, amount.getAmountType());
                    st.setInt(6, 1);
                    st.setInt(7, Integer.parseInt(year));
                    st.execute();
                    if(!(BranchId.equals("21")  && year.equals("2")))
                    {
                        InsertAllSubjectsOfStudent(BranchId, StudentId, year, con);
                    }                   
                }
                else
                {
                    PreparedStatement st=con.prepareStatement("update StudentExamFeeStatus set IsMguApproved=1,isConfirmed=1 where StudentId=?");
                    st.setInt(1, Integer.parseInt(StudentId));
                    st.executeUpdate();
                }
            }
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
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
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return  0;
        }
        finally
        {
            con.close();
        }
    }
    public void InsertAllSubjectsOfStudent(String  BranchId, String StudentId,String year,Connection con) throws SQLException
    {
        PreparedStatement st1=null;
        int SemOrYear=getYearOrSem(Integer.parseInt(BranchId));
        if(SemOrYear==0 && year.equals("1"))
        {
            st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or CurrentYearSem=2) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or CurrentYearSem=2)) and IsOptionalSubject=0");
            st1.setString(1, BranchId);
            st1.setString(2, BranchId);
        }
        else if(SemOrYear==0 && year.equals("2"))
        {
            st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4)) and IsOptionalSubject=0");
            st1.setString(1, BranchId);
            st1.setString(2, BranchId);
        }
        else
        {
             st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=?  and IsElective=0 and IsOptionalSubject=0");
             st1.setString(1, BranchId);
             st1.setString(2, year);
        }
        ResultSet Subject=st1.executeQuery();
        while(Subject.next())
        {
            PreparedStatement st=con.prepareStatement("select count(*) from ExamRegistrationMaster where StudentId=? and SubjectBranchId=? and ExamId=7");
            st.setString(1, StudentId);
            st.setString(2, Subject.getString("SubjectBranchId"));
            ResultSet result=st.executeQuery();
            while(result.next())
            {
                if(result.getInt(1)==0)
                {
                    PreparedStatement st2=con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId) values(?,?,2)");
                    st2.setString(1, StudentId);
                    st2.setString(2, Subject.getString("SubjectBranchId"));
                    st2.execute();
                }
            }
            PreparedStatement stBr=con.prepareStatement("select count(*) from StudentSubjectBranchMap where StudentId=? and SubjectBranchId=? ");
            stBr.setString(1, StudentId);
            stBr.setString(2, Subject.getString("SubjectBranchId"));
            ResultSet result1=stBr.executeQuery();
            while(result1.next())
            {
                if(result1.getInt(1)==0)
                {
                    PreparedStatement st2=con.prepareStatement("insert into StudentSubjectBranchMap (StudentId,SubjectBranchId) values(?,?)");
                    st2.setString(1, StudentId);
                    st2.setString(2, Subject.getString("SubjectBranchId"));
                    st2.execute();
                }
            }
        }
    }
    
    
public String getClientIP(HttpServletRequest request)
{
    String ipAddress = null;
    ipAddress = request.getHeader("X-FORWARDED-FOR");
    if(ipAddress == null)
    {
        ipAddress = request.getRemoteAddr();
    }
    return ipAddress;
}
public boolean SaveExamLog(String Action,HttpServletRequest request) throws SQLException
{

String IpAddress=getClientIP(request);

String User=request.getSession().getAttribute("UserName").toString();
Connection con=null;
try
{
    con=new DBConnection().getConnection();
    PreparedStatement st=con.prepareStatement("insert into ExamRegistrationLog (UserName,IPAddress,Action,ModifiedDate) values(?,?,?,sysdate())");
    st.setString(1, User);
    st.setString(2, IpAddress);
    st.setString(3, Action);
    st.execute();
    return true;
}
catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
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

   public boolean deleteExamRegister(String  BranchId,String CollegeId,String  AttendingYear) throws SQLException
    {
         Connection con = null;
         try {

            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("delete from StudentExamFeeStatus where CollegeId=? and BranchId=? and AttendingYear=? and  IsConfirmed!=1");

            st.setInt(1, Integer.parseInt(CollegeId));
            st.setInt(2, Integer.parseInt(BranchId));
            st.setString(3,AttendingYear );
            st.execute();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
        }

    }

  
 public ArrayList<Integer> OptionSelectedStudentsList(String BranchId)
    {
         Connection con=null ;
            ArrayList<Integer> StudentList=new ArrayList<Integer>();
      try {
           con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT StudentId from StudentoptionSelected  where OptionsupplySelection=1  and BranchId=?");
            st.setString(1, BranchId);
            ResultSet Rs = st.executeQuery();
            while (Rs.next()) {

                StudentList.add(Rs.getInt(1));
            }
                 Rs.close();
                 con.close();
          return  StudentList;

           } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return StudentList;
        }

    }
//need to modify
public ArrayList<Integer> ExamRegstrnConfirmedStudentsList(String BranchId,String CentreId,String year)
    {
            Connection con=null ;
            ArrayList<Integer> StudentList=new ArrayList<Integer>();
      try {
           con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT StudentId from  StudentExamFeeStatus  where CollegeId  and BranchId=? and AttendingYear=?");
              st.setString(1,CentreId);
              st.setString(1, BranchId);
              st.setString(1, BranchId);
              st.setString(1, BranchId);

            ResultSet Rs = st.executeQuery();
            while (Rs.next()) {

                StudentList.add(Rs.getInt(1));
            }
                 Rs.close();
                 con.close();
          return  StudentList;

           } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return StudentList;
        }

    }

//need to modify
public boolean isExamRegstrnConfirmedStudent(int StudentId,int year) throws SQLException
    {
         Connection con=null ;
            ArrayList<Integer> StudentList=new ArrayList<Integer>();
      try {
           con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT count(*) from  StudentExamFeeStatus  where StudentId=? and AttendingYear=? and IsConfirmed=1");
              st.setInt(1,StudentId);
              st.setInt(2, year);         
            ResultSet Rs = st.executeQuery();
             while(Rs.next())
            {
                if(Rs.getInt(1)>0)
                {
                    return true;
                }
            }
            return false;
                

           } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex);
            return false;
 
        }finally
        {
            if(con!=null)
            {
                con.close();
            }
        }

    }

public boolean isExamRegstredStudent(int StudentId,int year) throws SQLException
    {
         Connection con=null ;
            ArrayList<Integer> StudentList=new ArrayList<Integer>();
      try {
           con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT count(*) from  StudentExamFeeStatus  where StudentId=? and AttendingYear=? ");
              st.setInt(1,StudentId);
              st.setInt(2, year);
            ResultSet Rs = st.executeQuery();
             while(Rs.next())
                {
                    if(Rs.getInt(1)>0)
                    {
                        return true;
                    }
                }
            return false;


           } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationb.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }finally
        {
            if(con!=null)
            {
                con.close();
            }
        }

    }



public boolean deleteSupplyOrImprovementOrOptionalPapers(int StudentId,int BranchId,int Year) throws SQLException
    {   Connection con=null ;
        try {
           
            int examId=4;
            con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
            PreparedStatement st = con.prepareStatement("Delete FROM ExamRegistrationMaster where StudentId =? AND ExamId =? ");
                  st.setInt(1, StudentId);
                  st.setInt(2, examId);
           st.executeUpdate();
              con.close();
              return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            if(con!=null)
                con.close();
            return false;
        }
         
    }
// Deleting from SubjectbRanchMap;
public boolean deleteOptionalPapers(int StudentId,int BranchId,int Year) throws SQLException
    {   Connection con=null ;
        try {

            con = new DBConnection().getConnection();
            ArrayList<SubjectBranch>  regstdOptionals=new ArrayList<SubjectBranch>() ;
            regstdOptionals=new mStudentListb().getRegisteredOptionalPapers(StudentId, BranchId, con, Year);
            for(int k=0;k< regstdOptionals.size();k++){
            PreparedStatement st = con.prepareStatement("Delete FROM StudentSubjectBranchMap where StudentId =? AND SubjectBranchId=?  ");
                  st.setInt(1, StudentId);
                  st.setInt(2, regstdOptionals.get(k).getSubjectBranchId());
            st.executeUpdate();
            }  con.close();
              return true;

        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            if(con!=null)
                con.close();
            return false;
        }

    }



 public static void main(String[] args) throws Exception {


      ExamRegistrationb b1=new ExamRegistrationb();
      //System.out.println(b1.isExamRegstrnConfirmedStudent(8490,1));
    //b1.insertExamRegister("35", "10", "6");
      Connection con = new DBConnection().getConnection();
          b1.deleteOptionalPapers(3779, 21, 2);
             //   b1.getRegisteredOptionalPapers1(213, 21, con, 2);

    }



}
