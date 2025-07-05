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
public class ExamRegistration {
    ArrayList<String> BranchId,BranchCode ,BranchName,BranchDisplayName;
    ArrayList<CourseData> Branches;
    String SearchFilter,SearchBranch,SearchCode;

    HttpServletRequest request;
    HttpServletResponse response;



    public boolean insertExamRegister(String  BranchId,String CollegeId, String StudentId,String year) throws SQLException
    {
         Connection con = null;
         boolean isInsertallSubjects=true;
         if(BranchId.equals("21") && year.equals("2") )
                    {
                       isInsertallSubjects=false;
                    }
//         else if(BranchId.equals("17") && year.equals("2") )
//                    {
//                       isInsertallSubjects=false;
//                    } 
         else if(BranchId.equals("18") && year.equals("3") )
                    {
                       isInsertallSubjects=false;
                    }
//          else if(BranchId.equals("26") && year.equals("1") )
//                    {
//                       isInsertallSubjects=false;
//                    }
         else if(BranchId.equals("18") && year.equals("3") )
                    {
                       isInsertallSubjects=false;
                    }
         
         try {
           
            con = new DBConnection().getConnection();
            PreparedStatement st1=con.prepareStatement("SELECT count(*) FROM DEMS_db.StudentExamFeeStatus S WHERE S.`StudentId`=? and examId=(SELECT max(ExamId) FROM ExamMaster E) ");
            st1.setString(1, StudentId);
            ResultSet rs=st1.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==0)
                {
                    PreparedStatement st = con.prepareStatement("insert into StudentExamFeeStatus (StudentId,CollegeId,BranchId,Amount,FeeType,isConfirmed,AttendingYear,IsMguApproved,ExamCentre,registerTime,examId) values(?,?,?,?,?,?,?,?,?,sysdate(),(SELECT max(ExamId) FROM ExamMaster E))");
                    st.setInt(1, Integer.parseInt(StudentId));
                    st.setInt(2, Integer.parseInt(CollegeId));
                    st.setInt(3, Integer.parseInt(BranchId));
                    //mExamFeeNew examFeeNew=new mExamFeeNew();
                    // Amount amount=examFeeNew.getTotalExamFeeForStudent(Integer.parseInt(StudentId), Integer.parseInt(CollegeId), Integer.parseInt(BranchId),Integer.parseInt(year), con);
                    st.setInt(4, 0);
                    st.setString(5,"INR");
                    st.setInt(6, 0);
                    st.setInt(7, Integer.parseInt(year));
                     st.setInt(8, 1);
                      st.setInt(9, Integer.parseInt(CollegeId));
                    st.execute();
                    if(isInsertallSubjects)
                    {
                       InsertAllSubjectsOfStudent(BranchId, StudentId, year, con);
                    }
                }
                 else
                {
                    PreparedStatement st=con.prepareStatement("update StudentExamFeeStatus set IsMguApproved=1,isConfirmed=1,ExamCentre=? where StudentId=? and examId=(SELECT max(ExamId) FROM ExamMaster E)");
                     st.setInt(1, Integer.parseInt(CollegeId));
                    st.setInt(2, Integer.parseInt(StudentId));
                    st.executeUpdate();
                    
                    // For ensuring the subject addition
                    if(isInsertallSubjects)
                    {
                       InsertAllSubjectsOfStudent(BranchId, StudentId, year, con);
                    }
                }
            }
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
        }

    }

     public boolean insertExamRegister(String  BranchId,String CollegeId, String StudentId) throws SQLException
    {
         Connection con = null;


         try {

            con = new DBConnection().getConnection();
                int attyear=0;
            attyear=getStudentAttendingYear(Integer.parseInt(StudentId));
            PreparedStatement st1=con.prepareStatement("SELECT count(*) FROM DEMS_db.StudentExamFeeStatus S WHERE S.`StudentId`=? and examId=(SELECT max(ExamId) FROM ExamMaster E) ");
            st1.setString(1, StudentId);
            ResultSet rs=st1.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==0)
                {
                    PreparedStatement st = con.prepareStatement("insert into StudentExamFeeStatus (StudentId,CollegeId,BranchId,Amount,FeeType,isConfirmed,IsMguApproved,ExamCentre,AttendingYear,registerTime,examId) values(?,?,?,?,?,?,?,?,?,sysdate(),(SELECT max(ExamId) FROM ExamMaster E))");
                    st.setInt(1, Integer.parseInt(StudentId));
                    st.setInt(2, Integer.parseInt(CollegeId));
                    st.setInt(3, Integer.parseInt(BranchId));
                    //mExamFeeNew examFeeNew=new mExamFeeNew();
                    // Amount amount=examFeeNew.getTotalExamFeeForStudent(Integer.parseInt(StudentId), Integer.parseInt(CollegeId), Integer.parseInt(BranchId),Integer.parseInt(year), con);
                    st.setInt(4, 0);
                    st.setString(5,"INR");
                    st.setInt(6, 1);
                    st.setInt(7, 1);
                     st.setInt(8, Integer.parseInt(CollegeId));
                     st.setInt(9, attyear);
                    st.execute();

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
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
        }

    }
      public boolean insertExamRegisterSupply(String  BranchId,String CollegeId, String StudentId) throws SQLException
    {
         Connection con = null;


         try {

            con = new DBConnection().getConnection();
            int attyear=0;
            attyear=getStudentAttendingYear(Integer.parseInt(StudentId));
            PreparedStatement st1=con.prepareStatement("SELECT count(*) FROM DEMS_db.StudentExamFeeStatus S WHERE S.`StudentId`=? and examId=(SELECT max(ExamId) FROM ExamMaster E) ");
            st1.setString(1, StudentId);
            ResultSet rs=st1.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==0)
                {
                    PreparedStatement st = con.prepareStatement("insert into StudentExamFeeStatus (StudentId,CollegeId,BranchId,Amount,FeeType,isConfirmed,IsMguApproved,IsSupplementaryExam,ExamCentre,AttendingYear,registerTime,examId) values(?,?,?,?,?,?,?,?,?,?,sysdate(),(SELECT max(ExamId) FROM ExamMaster E))");
                    st.setInt(1, Integer.parseInt(StudentId));
                    st.setInt(2, Integer.parseInt(CollegeId));
                    st.setInt(3, Integer.parseInt(BranchId));
                    //mExamFeeNew examFeeNew=new mExamFeeNew();
                    // Amount amount=examFeeNew.getTotalExamFeeForStudent(Integer.parseInt(StudentId), Integer.parseInt(CollegeId), Integer.parseInt(BranchId),Integer.parseInt(year), con);
                    st.setInt(4, 0);
                    st.setString(5,"INR");
                    st.setInt(6, 1);
                    st.setInt(7, 1);
                    st.setInt(8, 1);
                     st.setInt(9, Integer.parseInt(CollegeId));
                      st.setInt(10, attyear);
                    st.execute();

                }
                 else
                {
                    PreparedStatement st=con.prepareStatement("update StudentExamFeeStatus set IsMguApproved=1,isConfirmed=1 ,IsSupplementaryExam=1 where StudentId=? and ExamId=(SELECT max(ExamId) FROM ExamMaster E)");
                    st.setInt(1, Integer.parseInt(StudentId));
                    st.executeUpdate();
                    
                }
            }
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
        }

    }
  public boolean updateExamCentreVenue(String  BranchId,String CollegeId, String StudentId) throws SQLException
     {
         Connection con = null;
         try {
            con = new DBConnection().getConnection();

                    PreparedStatement st=con.prepareStatement("update StudentExamFeeStatus set ExamVenue=? where StudentId=?");
                     st.setString(1, CollegeId);
                    st.setInt(2, Integer.parseInt(StudentId));
                    st.executeUpdate();
                  con.close();
                    return true;
                }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
        }

     }
    public int getStudentAttendingYear(int StudentId) throws SQLException
    {
        Connection con =new DBConnection().getConnection();
    CourseData Course=new CourseData();
    try
    {
        PreparedStatement st=con.prepareStatement("SELECT AttendingYear  FROM StudentPersonal where StudentId=?");
        st.setInt(1, StudentId);
        ResultSet Rs=st.executeQuery();

        while(Rs.next())
        {
            return Rs.getInt("AttendingYear");
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
      if(BranchId!=null && BranchId.equals("-1"))
         {
             BranchId=getStudentBranch(StudentId);
         }
        
        
        if(BranchId!=null && !BranchId.equals("-1")){
        
        PreparedStatement st1=null;
        int SemOrYear=getYearOrSem(Integer.parseInt(BranchId));
        if(SemOrYear==0 && year.equals("1"))
        {
            if(BranchId.equals("26"))//LLM correction first semester
             {
            st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=1  and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and CurrentYearSem=1 ) and IsOptionalSubject=0");
             }else
            {
                st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or CurrentYearSem=2) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or CurrentYearSem=2)) and IsOptionalSubject=0");
            
            }st1.setString(1, BranchId);
            st1.setString(2, BranchId);
        }
        else if(SemOrYear==0 && year.equals("2"))
        {
            
             //For Handling Mcom 3rd Semester 
            
             if(BranchId.equals("17"))
             {
                 st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=3  and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and CurrentYearSem=3 ) and IsOptionalSubject=0");
             }
             //LLM 3rd semester need to check 2nd sem elective choosen
             else if(BranchId.equals("26"))
             {
             st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4)) and IsOptionalSubject=0 and SubBranchId=(SELECT distinct SubBranchId FROM StudentSubjectBranchMap WHERE `StudentId`=? and SubBranchId is not null)");
             st1.setString(3, StudentId);
             }
             else{
            
            st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=3 or CurrentYearSem=4)) and IsOptionalSubject=0");
             }
             st1.setString(1, BranchId);
            st1.setString(2, BranchId);
        }
        else if(SemOrYear==0 && year.equals("3"))
        {
            st1=con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=5 or CurrentYearSem=6) and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and (CurrentYearSem=5 or CurrentYearSem=6)) and IsOptionalSubject=0");
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
            PreparedStatement st=con.prepareStatement("select count(*) from ExamRegistrationMaster where StudentId=? and SubjectBranchId=? and ExamId=(select max(ExamId) from ExamMaster)");
            st.setString(1, StudentId);
            st.setString(2, Subject.getString("SubjectBranchId"));
            ResultSet result=st.executeQuery();
            while(result.next())
            {
                if(result.getInt(1)==0)
                {
                    PreparedStatement st2=con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId,RegistrationType) values(?,?,(select max(ExamId) from ExamMaster),0)");
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
    }
    
    public String getStudentBranch(String StudentId ) throws SQLException
{
    boolean isPassed = false;
     Connection con=null ;
     String BranchId = null;
    
     try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT S.`BranchId` FROM StudentPersonal S where S.StudentId=?");
            st.setString(1,StudentId);
           
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
            BranchId= rs.getString("BranchId") ;

            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
          
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
             return BranchId;
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
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
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
            PreparedStatement st = con.prepareStatement("SELECT StudentId from StudentoptionSelected  where OptionsupplySelection=1  and BranchId=? and examId=(SELECT max(ExamId) FROM ExamMaster E)");
            st.setString(1, BranchId);
            ResultSet Rs = st.executeQuery();
            while (Rs.next()) {

                StudentList.add(Rs.getInt(1));
            }
                 Rs.close();
                 con.close();
          return  StudentList;

           } catch (SQLException ex) {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex1);
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
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex1);
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
            PreparedStatement st = con.prepareStatement("SELECT count(*) from  StudentExamFeeStatus  where StudentId=? and AttendingYear=? and examId=(SELECT max(ExamId) FROM ExamMaster E) and IsConfirmed=1");
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
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
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
           
            PreparedStatement st1 = con.prepareStatement("Delete FROM StudentoptionSelected where StudentId =? AND ExamId =? ");
                  st1.setInt(1, StudentId);
                  st1.setInt(2, examId);
                  st1.executeUpdate();
              con.close();
              return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            if(con!=null)
                con.close();
            return false;
        }
         
    }


public boolean deleteSupplyOrImprovementOrOptionalPapers(int StudentId,int BranchId) throws SQLException
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
            regstdOptionals=new mStudentList().getRegisteredOptionalPapers(StudentId, BranchId, con, Year);
            for(int k=0;k< regstdOptionals.size();k++){
            PreparedStatement st = con.prepareStatement("Delete FROM StudentSubjectBranchMap where StudentId =? AND SubjectBranchId=? ");
                  st.setInt(1, StudentId);
                  st.setInt(2, regstdOptionals.get(k).getSubjectBranchId());
            st.executeUpdate();
            }  con.close();
              return true;

        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            if(con!=null)
                con.close();
            return false;
        }

    }

public boolean checkAndInsertAllStudentsSubjectAddedtoExam(int examId) throws SQLException
{
    Connection con=null ;
         try {

            con = new DBConnection().getConnection();
            PreparedStatement st1=con.prepareStatement("SELECT S.StudentId,S.BranchId,S.AttendingYear  FROM DEMS_db.StudentExamFeeStatus S "
                    + "WHERE S.IsConfirmed=1 and  S.examId=? and S.StudentId not in (Select StudentId from ExamRegistrationMaster E where E.examId=? and E.RegistrationType=0 ) ");
            st1.setInt(1,examId);
            st1.setInt(2,examId);
            ResultSet rs=st1.executeQuery();
            while(rs.next())
            {
               
                       InsertAllSubjectsOfStudent(rs.getString(2),rs.getString(1),rs.getString(3), con);
                 
                
            }
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
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

//For Course Completed

 public boolean insertExamRegisterForCourseCompleted(String  BranchId,String CollegeId, int StudentId,String examCentre) throws SQLException
    {
         Connection con = null;
         try {
           
            con = new DBConnection().getConnection();
            int attyear=0;
            attyear=getStudentAttendingYear(StudentId);
            PreparedStatement st1=con.prepareStatement("SELECT count(*) FROM DEMS_db.StudentExamFeeStatus S WHERE S.`StudentId`=? and examId=(SELECT max(ExamId) FROM ExamMaster E) ");
            st1.setInt(1, StudentId);
            ResultSet rs=st1.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)==0)
                {
                    PreparedStatement st = con.prepareStatement("insert into StudentExamFeeStatus (StudentId,CollegeId,BranchId,Amount,FeeType,isConfirmed,AttendingYear,registerTime,examId,ExamCentre) values(?,?,?,?,?,?,?,sysdate(),(SELECT max(ExamId) FROM ExamMaster E),?)");
                    st.setInt(1, StudentId);
                    st.setInt(2, Integer.parseInt(CollegeId));
                    st.setInt(3, Integer.parseInt(BranchId));
                    //mExamFeeNew examFeeNew=new mExamFeeNew();
                    // Amount amount=examFeeNew.getTotalExamFeeForStudent(Integer.parseInt(StudentId), Integer.parseInt(CollegeId), Integer.parseInt(BranchId),Integer.parseInt(year), con);
                    st.setInt(4, 0);
                    st.setString(5,"INR");
                    st.setInt(6, 1);
                    st.setInt(7, attyear);
                    st.setString(8,examCentre);                    
                    st.execute();
                   
                }
                else
                {
                    PreparedStatement st=con.prepareStatement("update StudentExamFeeStatus set ExamCentre=?,IsSupplementaryExam=? where StudentId=? and examId=(SELECT max(ExamId) FROM ExamMaster E) ");
                    st.setString(1,examCentre);
                    st.setInt(2, 1);
                    st.setInt(3,StudentId);
                    st.executeUpdate();
                }
               
            }
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistration.class.getName()).log(Level.SEVERE, null, ex);
                if(con!=null)
                con.close();
            return false;
        }

    }

 public static void main(String[] args) throws Exception {


      ExamRegistration b1=new ExamRegistration();
      b1.checkAndInsertAllStudentsSubjectAddedtoExam(3);
      //System.out.println(b1.isExamRegstrnConfirmedStudent(8490,1));
    //b1.insertExamRegister("35", "10", "6");
     // Connection con = new DBConnection().getConnection();
         // b1.deleteOptionalPapers(3779, 21, 2);
             //   b1.getRegisteredOptionalPapers1(213, 21, con, 2);

    }



}
