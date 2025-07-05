/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import Entity.Automation;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import Entity.*;


public class mCourseFeeDD {
Connection con=null;
String OrderNumber="0";
ArrayList<CourseFeeData> DDList=new ArrayList<CourseFeeData>();
ArrayList<StudentData> StudentList=new ArrayList<StudentData>();
AdmissionYear Year=new AdmissionYear();
Automation Fee=new Automation();
public mCourseFeeDD()
{
     
    }
public String IsValidStudentofCentre(String PRN,String CentreId) throws SQLException
{
    if(CentreId==null || CentreId.equals("-1"))
    {
        return "Select Centre";
    }
        try
        {
            Connection con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select CollegeId from StudentPersonal where PRN=? ");
            st.setString(1, PRN);

            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getString(1).equals(CentreId))
                    return null;
                else
                    return "Student does not belong to selected Centre";

            }
            else
            {
                return "Invlid PRN";
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
            return "Could not connect to server";
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
}
public ArrayList<CourseFeeData> getDDListForUser(String Username) throws SQLException
{
        try {
             con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT DDId ,Branch,Bank,Amount,AmountType,DDNo,DDDate,D.CollegeId,CollegeName FROM DDMaster D ,CollegeMaster C where  D.CentreId=C.CollegeId and D.CollegeId in (select  CollegeId from CentreUserMap where UserName=? and EndDate is null) ");
            Query.setString(1, Username);;
            ResultSet Records = Query.executeQuery();
            CourseFeeData DD;
            while (Records.next()) {
                DD = new CourseFeeData();
                DD.DDId = Records.getInt(1);
                DD.Branch = Records.getString(2);
                DD.Bank = Records.getString(3);
                DD.Amount = Records.getString(4);
                DD.AmountType = Records.getString(5);
                DD.DDNo = Records.getString(6);
                DD.DDDate = Records.getString(7);
                DD.CentreId = Records.getInt(8);
                DD.CentreName = Records.getString(9);
                DDList.add(DD);
            }
            return DDList;
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean IsCurrentYearCourseFee(int DDId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT Date(EnteredDate) FROM `DEMS_db`.`DDMaster`where DDId=?");

            st.setInt(1, DDId);
            //st.setInt(2, AttendingYear);
            ResultSet rs=st.executeQuery();
            
            if(rs.next())
            {
               st=con.prepareStatement("select count(*) from AdmissionDates where \'"+ rs.getString(1)+"\' between Reg_StartDate and AdmissionClosedDate and AdmissionYear =(select max(AdmissionYear) from AdmissionDates)");
               ResultSet Record=st.executeQuery();

               if(Record.next())
               {
                   if(Record.getInt(1)>0)
                   {
                       return true;
                   }
                   return false;
               }
            }
            return false;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public boolean isValidApplicationNumber(String ApplicationNo,int AdmissionYear) throws SQLException
{
    Connection con = null;
        try
        {

            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select count(*) from AdmissionDates where"+ ApplicationNo+" between between ApplicationNoStart and ApplicationNoEnd and AdmissionYear=?");
            st.setInt(1, AdmissionYear);
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
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<CourseFeeData> getDDListForCentre(int Centre) throws SQLException
{
     try {
          con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT DDId ,Branch,Bank,Amount,AmountType,DDNo,DDDate,D.CollegeId,CollegeName FROM DDMaster D ,CollegeMaster C where  D.CentreId=C.CollegeId and D.CollegeId =? ");
            Query.setInt(1, Centre);
            ResultSet Records = Query.executeQuery();
            CourseFeeData DD;
            while (Records.next()) {
                DD = new CourseFeeData();
                DD.DDId = Records.getInt(1);
                DD.Branch = Records.getString(2);
                DD.Bank = Records.getString(3);
                DD.Amount = Records.getString(4);
                DD.AmountType = Records.getString(5);
                DD.DDNo = Records.getString(6);
                DD.DDDate = Records.getString(7);
                DD.CentreId = Records.getInt(8);
                DD.CentreName = Records.getString(9);
                DDList.add(DD);
            }
            return DDList;
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public int getStudentId(int ApplicationNo) throws SQLException
{
     try {
          con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT StudentId FROM StudentLoginMaster where QuestionId=?");
            Query.setInt(1, ApplicationNo);
            ResultSet Records = Query.executeQuery();

            while (Records.next()) {
            return    Records.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
     finally
     {
         if(con!=null)
         {
             con.close();
         }
     }
}
 public int getStudentAttendingYear(int StudentId) throws SQLException
 {
        Connection con=null;
        PreparedStatement st=null;
        try
        {
            con = new DBConnection().getConnection();
            st=con.prepareStatement("Select AttendingYear from StudentPersonal where StudentId=?");
            st.setInt(1,StudentId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentCourseFee.class.getName()).log(Level.SEVERE, null, ex);
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
  public int getPRNFromStudentId(String StudentId) throws SQLException
 {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select PRN from StudentPersonal where StudentId=?");
                st.setString(1,StudentId);
                 ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentCourseFee.class.getName()).log(Level.SEVERE, null, ex);
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
            if(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentCourseFee.class.getName()).log(Level.SEVERE, null, ex);
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
 public String SaveStudentCourseFeePRNStudents(HttpServletRequest request) throws IOException, SQLException
 {
          
          
        String DDNo = request.getParameter("DDNo");
        String BrName = request.getParameter("BrName");
        String CenterId=request.getParameter("CentreId");
        String BankName = request.getParameter("BankName");
        float Amount = Float.parseFloat(request.getParameter("Amount"));
        String ddDate = request.getParameter("ddDate");
        String AttYear=request.getParameter("Year");
         String amountType = request.getParameter("AmountType");
         int sum=0;
        Calendar now = Calendar.getInstance();
        int YearofAdmission = now.get(Calendar.YEAR);
        String DDNumber = null;
        DDNumber = DDNo.trim();
        boolean Update=false;
        
      if(request.getParameter("DDTYPE")!=null)
      {
          
          int BranchId=Integer.parseInt(request.getParameter("Course"));
          int TutionFeeid=getTutionFeeId(BranchId, Integer.parseInt(AttYear));
                  if (DDNumber != null && BrName != null && BankName != null && Amount != 0) {
           try {
               con = new DBConnection().getConnection();
               PreparedStatement st = con.prepareStatement("insert into DDMaster (Branch,Bank,Amount,AmountType,DDNo,DDDate,EnteredDate,CentreId) values(?,?,?,?,?,?,now(),?)");
               st.setString(1, BrName);
               st.setString(2, BankName);
               st.setFloat(3, Amount);
               st.setString(4, amountType);
               st.setString(5, DDNo);
               st.setString(6, convertDatetoyyyymmdd(ddDate));
               st.setInt(7,Integer.parseInt(request.getParameter("CentreId")));
               st.executeUpdate();
               
                st = con.prepareStatement("select DDId from DDMaster where Branch=? and DDNo=?  and Bank=? ");
                       st.setString(1, BrName);
                       st.setString(2, DDNo);
                       st.setString(3, BankName);
                       ResultSet Rs = st.executeQuery();
                       int DDId = 0;
                       if (Rs.next())
                       {
                           DDId = Rs.getInt(1);
                       }
                       con.close();
               if (request.getParameter("DDTYPE").equals("Bulk"))
               {
                    String[] Students = request.getParameterValues("StudentId");
                        if (Students.length > 0)
                   {
                            con = new DBConnection().getConnection();
                       st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,?)");
                       int i = 0;
                       while (i < Students.length)
                       {
                           st.setInt(1, Integer.parseInt(Students[i]));
                           st.setInt(2, TutionFeeid);
                           st.setInt(3, -1);
                           st.setInt(4, DDId);
                           st.setInt(5, getStudentAttendingYear(Integer.parseInt(Students[i])));
                           st.executeUpdate();
                           i++;
                          
                       }
                        con.close();
                   }

                   return "Saved";
               }
                else
               {

                           con = new DBConnection().getConnection();
                           st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,?)");
                           int StudentId=getStudentIdFromPRN(request.getParameter("PRN"));
                           st.setInt(1,StudentId);
                           st.setInt(2, getTutionFeeId(Integer.parseInt(getCourseIdForStudent(StudentId)), getStudentAttendingYear(StudentId)));
                           st.setInt(3, -1);
                           st.setInt(4, DDId);
                           st.setInt(5, getStudentAttendingYear(StudentId));
                           st.executeUpdate();
                           con.close();
                         return "Saved";
               }
                }
           catch (Exception ex) {
                   Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                    return ex.getMessage();
                }
            finally
        {
            if(con!=null)
            {
                con.close();
            }
        }

            }
                  else
                  {
              return "Error while Saving";
                  }

       }
      else
      {
            return "Error while Saving";
      }

    }
public String getCountryForCentre(int CentreId) throws SQLException
{
    String Country="India";
    String AmountType=null;
    try
    {
         con=new DBConnection().getConnection();
    PreparedStatement Query = con.prepareStatement("SELECT CollegeCountry FROM CollegeMaster where CollegeId=?");
            Query.setInt(1, CentreId);
            ResultSet Data = Query.executeQuery();
            if (Data.next())
            {
                Country = Data.getString(1);
            }
            if(Country.equals("India"))
            {
            AmountType="INR";
            }
            else
            {
                AmountType="USD";
            }
            return AmountType;

    }
    catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public int getStudentId_PRN(String PRN) throws SQLException
{
     try {
          con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT StudentId FROM StudentPersonal where PRN=?");
            Query.setString(1, PRN);
            ResultSet Records = Query.executeQuery();

            while (Records.next()) {
            return    Records.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
     finally
     {
         if(con!=null)
         {
             con.close();
         }
     }
}
public CourseFeeData getDDDetails(int DDId) throws SQLException
{
      try {
           con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT DDId ,Branch,Bank,Amount,AmountType,DDNo,DDDate,C.CollegeId,CollegeName FROM DDMaster D ,CollegeMaster C where  D.CentreId=C.CollegeId and D.DDId=?");
            Query.setInt(1, DDId);
            ResultSet Records = Query.executeQuery();
            CourseFeeData DD = null;
            while (Records.next()) {
                DD = new CourseFeeData();
                DD.DDId = Records.getInt(1);
                DD.Branch = Records.getString(2);
                DD.Bank = Records.getString(3);
                DD.Amount = Records.getString(4);
                DD.AmountType = Records.getString(5);
                DD.DDNo = Records.getString(6);
                DD.DDDate = Records.getString(7);
                DD.CentreId = Records.getInt(8);
                DD.CentreName = Records.getString(9);
             
            }

            Query=con.prepareStatement("SELECT SF.StudentId ,StudentName,QuestionId,C.CollegeId,CollegeName,BranchId,SP.PRN FROM StudentFeeMap SF,StudentPersonal SP,CollegeMaster C,StudentLoginMaster SL where  DDId= ? and SP.StudentId=SF.StudentId and SL.StudentId=SF.StudentId and C.CollegeId=SP.CollegeId");
            Query.setInt(1, DDId);
            Records=Query.executeQuery();
            StudentData Student;
            while (Records.next()) {
              Student=new StudentData();
              Student.StudentId=Records.getInt(1);
              Student.Name=Records.getString(2);
              Student.ApplicationNo=Records.getInt(3);
              Student.Centre=Records.getString(4);
              Student.Course=Records.getString(6);
              Student.PRN=Records.getString(7);
              StudentList.add(Student);
          }
            return DD;
        } 
      catch (SQLException ex)
      {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public Automation getAutomationFee()
{
    return Fee;
}
public CourseFeeData getDDForAutomationFee(int DDId) throws SQLException
{
    try {
           con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT DDId ,Branch,Bank,Amount,AmountType,DDNo,DDDate,C.CollegeId,CollegeName FROM DDMaster D ,CollegeMaster C where  D.CentreId=C.CollegeId and D.DDId=?");
            Query.setInt(1, DDId);
            ResultSet Records = Query.executeQuery();
            CourseFeeData DD = null;
            while (Records.next()) {
                DD = new CourseFeeData();
                DD.DDId = Records.getInt(1);
                DD.Branch = Records.getString(2);
                DD.Bank = Records.getString(3);
                DD.Amount = Records.getString(4);
                DD.AmountType = Records.getString(5);
                DD.DDNo = Records.getString(6);
                DD.DDDate = Records.getString(7);
                DD.CentreId = Records.getInt(8);
                DD.CentreName = Records.getString(9);
            }
            Query=con.prepareStatement("SELECT AttendingYear,AcademicYear,StudentsCount FROM `DEMS_db`.`CenterAutomationFee` where DDId=?");
            Query.setInt(1, DDId);
            ResultSet rs=Query.executeQuery();
            
            if(rs.next())
            {
                Fee.AcademicYear=rs.getInt(2);
                Fee.AttendingYear=rs.getInt(1);
                Fee.Count=rs.getInt(3);
            }
             return DD;
        }
      catch (SQLException ex)
      {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
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
public String AmountCheck(HttpServletRequest request) throws SQLException
{
    String amount=request.getParameter("Amount");
    String err=emptyCheck("DD Amount", amount);
    String AmountType=request.getParameter("AmountType");
    String AttYear=request.getParameter("Year");
    Float FeeAmount;
     PreparedStatement st=null;
     if(err==null)
     {
         if(request.getParameter("DDTYPE")!= null && request.getParameter("DDTYPE").equals("Bulk"))
         {
             int BranchId=Integer.parseInt(request.getParameter("Course"));
             String sid[]=request.getParameterValues("StudentId");
           try
           {
          Float DDAmt=Float.parseFloat(amount);
          con=new DBConnection().getConnection();
          if(AmountType.equals("INR"))
          {
          st=con.prepareStatement("select TutionFees_IND from TutionFeeMaster where BranchId=? and TutionfeeYear=? and Academic_Year=(select max(Academic_Year) from TutionFeeMaster)");
          }
          if(AmountType.equals("USD"))
          {
          st=con.prepareStatement("select TutionFees_USD from TutionFeeMaster where BranchId=? and TutionfeeYear=? and Academic_Year=(select max(Academic_Year) from TutionFeeMaster)");
          }
          st.setInt(1,BranchId);
          st.setInt(2, Integer.parseInt(AttYear));
          ResultSet record=st.executeQuery();
          if(record.next())
          {
              FeeAmount=sid.length * record.getFloat(1)/2;
              if(FeeAmount > DDAmt)
              {
                  return "Total Fee is :"+ FeeAmount;
              }
              else
              {
                  return null;
              }
          }
          else
          {
              return "The CourseFee for this Branch is not defined in the Data Base. Please Contact System Administration Team";
          }

           }
           catch(Exception e)
             {
               return "DD Amount is not valid";
             }
             finally
             {
                 if(con!=null)
                 {
                     con.close();
                 }
             }

         }
         else if(request.getParameter("DDTYPE")!= null && request.getParameter("DDTYPE").equals("Single"))
         {

             String ApplicationNo=request.getParameter("ApplicationNo");
           try
           {
          Float DDAmt=Float.parseFloat(amount);
          con=new DBConnection().getConnection();
          if(AmountType.equals("INR"))
          {
          st=con.prepareStatement("select TutionFees_IND from StudentLoginMaster l,StudentPersonal p, TutionFeeMaster t where t.BranchId=p.BranchId and l.StudentId=p.StudentId and l.QuestionId=?");
          }
          if(AmountType.equals("USD"))
          {
          st=con.prepareStatement("select TutionFees_USD from StudentLoginMaster l,StudentPersonal p, TutionFeeMaster t where t.BranchId=p.BranchId and l.StudentId=p.StudentId and l.QuestionId=?");
          }
          st.setString(1,ApplicationNo);
          ResultSet record=st.executeQuery();
          if(record.next())
          {
              FeeAmount=record.getFloat(1)/2;
              if(FeeAmount > DDAmt)
              {
                  return "Total Fee is :"+ FeeAmount;
              }
              else
              {
                  return null;
              }
          }
          else
          {
              return "The CourseFee for this Branch is not defined in the Data Base. Please Contact System Administration Team";
          }
           }
           catch(Exception e)
             {
               return "DD Amount is not valid";
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
     else
     {
         return err;
     }
return null;
}
public String AmountCheckForPRN(HttpServletRequest request) throws SQLException
{
    String amount=request.getParameter("Amount");
    String err=emptyCheck("DD Amount", amount);
    String AmountType=request.getParameter("AmountType");
    String AttendingYear=request.getParameter("Year");
    Float FeeAmount;
     PreparedStatement st=null;
     if(err==null)
     {
         if(request.getParameter("DDTYPE")!= null && request.getParameter("DDTYPE").equals("Bulk"))
         {
             int BranchId=Integer.parseInt(request.getParameter("Course"));
             String sid[]=request.getParameterValues("StudentId");
           try
           {
          Float DDAmt=Float.parseFloat(amount);
          Connection con=new DBConnection().getConnection();
          if(AmountType.equals("INR"))
          {
          st=con.prepareStatement("select TutionFees_IND from TutionFeeMaster where BranchId=? and TutionFeeYear=?");
          }
          if(AmountType.equals("USD"))
          {
          st=con.prepareStatement("select TutionFees_USD from TutionFeeMaster where BranchId=? and TutionFeeYear=?");
          }
          st.setInt(1,BranchId);
          st.setInt(2, Integer.parseInt(AttendingYear));
          ResultSet record=st.executeQuery();
          
          if(record.next())
          {
              FeeAmount=sid.length * record.getFloat(1)/2;
              if(FeeAmount > DDAmt)
              {
                  return "Total Fee is :"+ FeeAmount;
              }
              else
              {
                  return null;
              }
          }
          else
          {
              return "The CourseFee for this Branch is not defined in the Data Base. Please Contact System Administration Team";
          }

           }
           catch(Exception e)
             {
               return "DD Amount is not valid";
             }
             finally
             {
                 if(con!=null)
                 {
                     con.close();
                 }
             }

         }
         else if(request.getParameter("DDTYPE")!= null && request.getParameter("DDTYPE").equals("Single"))
         {
             Connection con=new DBConnection().getConnection();
             String PRN=request.getParameter("PRN");
           try
           {
          Float DDAmt=Float.parseFloat(amount);
          Connection Con=new DBConnection().getConnection();
          if(AmountType.equals("INR"))
          {
          st=con.prepareStatement("select TutionFees_IND from StudentPersonal p, TutionFeeMaster t where t.BranchId=p.BranchId and p.PRN=?");
          }
          if(AmountType.equals("USD"))
          {
          st=con.prepareStatement("select TutionFees_USD from StudentPersonal p, TutionFeeMaster t where t.BranchId=p.BranchId and p.PRN=?");
          }
          st.setString(1,PRN);
          ResultSet record=st.executeQuery();
          if(record.next())
          {
              FeeAmount=record.getFloat(1)/2;
              if(FeeAmount > DDAmt)
              {
                  return "Total Fee is :"+ FeeAmount;
              }
              else
              {
                  return null;
              }
          }
          else
          {
              return "The CourseFee for this Branch is not defined in the Data Base. Please Contact System Administration Team";
          }

           }
           catch(Exception e)
             {
               return "DD Amount is not valid";
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
     else
     {
         return err;
     }
return null;
}
public ArrayList<StudentData>getStudentList()
{
     return StudentList;
}
public String EnterCourseFee(String DDNo,String Bank,String Branch,String DDDate,float Amount,String AmountType ,int CentreId,int BranchId,ArrayList<StudentData> SeletedStudents) throws SQLException
{


 try
 {
 con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement Query = con.prepareStatement("insert into DDMaster(Branch,Bank,Amount,AmountType,DDNo,DDDate,EnteredDate,CentreId,OrderNo) values(?,?,?,?,?,?,sysdate(),?,?)");
            Query.setString(1, Branch);
            Query.setString(2,Bank );
            Query.setFloat(3,Amount);
            Query.setString(4,AmountType);
            Query.setString(5, DDNo);
            Query.setString(6, Registration.convertDatetoyyyymmdd(DDDate));
            Query.setInt(7,CentreId);
            Query.setString(8, OrderNumber);
           
            Query.executeUpdate();
            con.commit();
            Query=con.prepareStatement("SELECT DDId FROM DDMaster where Bank=? and Branch =? and DDNo=? and CentreId=?");
            Query.setString(1,Bank);
            Query.setString(2,Branch);
            Query.setString(3, DDNo);
            Query.setInt(4,CentreId);
            ResultSet Data=Query.executeQuery();
            int DDId=0;
            while (Data.next())
            {
                DDId=Data.getInt(1);
            }
            int TutionFeeId=0;
            Query=con.prepareStatement("SELECT TutionFeeId FROM TutionFeeMaster where BranchId=? and TutionFeeYear=1 and Academic_Year=(select max(Academic_Year) from TutionFeeMaster) ");
            Query.setInt(1,BranchId);
            //Query.setInt(2,Year.getCurrentAdmissionYear());
            Data=Query.executeQuery();
            while (Data.next()) {
                TutionFeeId=Data.getInt(1);
            }
            if(TutionFeeId<=0)
            {
                con.rollback();
                return "Tution Fee for this Branch is not Entered Contact MG University";
            }

            
            Query=con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,-1,sysdate(),?,1)");
            /*for (String Student : SeletedStudents) {
               Query.setInt(1,Integer.parseInt(Student));
               Query.setInt(2,TutionFeeId);
               Query.setInt(3, DDId);
               Query.executeUpdate();
            }*/
            int count=SeletedStudents.size();
            for(int i=0;i<count;i++)
            {
            StudentData student=new StudentData();
            student=SeletedStudents.get(i);
            Query.setInt(1,student.StudentId);
            Query.setInt(2,TutionFeeId);
            Query.setInt(3, DDId);
            Query.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
           con.rollback();

            return ex.getMessage();
        }finally{
            con.commit();
         con.setAutoCommit(true);

         if(con!=null)
         {
             con.close();
         }
          return "Saved";
        }

  }
public String isExamFeePaid(ArrayList<String> StudentList,int DDId) throws SQLException
    {
    String SIDs="";
                 for(int i=0;i<StudentList.size();i++)
                 {
                      if(i==0)
                      {
                          SIDs=StudentList.get(0);
                      }
                      else
                      {
                           SIDs=SIDs+","+StudentList.get(i);
                      }
                 }
    try{
         con=new DBConnection().getConnection();
        String Query="select QuestionId from StudentLoginMaster where StudentId in (SELECT  StudentId FROM StudentFeeMap where ExamFeeId>0 and StudentId in ("+SIDs+"))";
    if(DDId>1)
    {
    Query=Query+" and DDId!="+DDId;
    }
 PreparedStatement st = con.prepareStatement(Query);
 ResultSet rs= st.executeQuery();

                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with Application Number "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
            }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }

}
 public boolean SaveStudentCourseFee(CourseFeeData DD,ArrayList<StudentData> StudentList,int CentreId,int AttendingYear) throws IOException, SQLException {


        String DDNo = DD.DDNo;
        String BrName = DD.Branch;
        String BankName = DD.Bank;
        float Amount = Float.parseFloat(DD.Amount);
        String DDDate = DD.DDDate;
         String AmountType = DD.AmountType;
         int  DDId=DD.DDId;
         int sum=0;
        Calendar now = Calendar.getInstance();
        int YearofAdmission = now.get(Calendar.YEAR);
        String DDNumber = null;
        DDNumber = DDNo.trim();
        boolean Update=false;
        
        if(DDId!=0)
        {
            try {
                 con=new DBConnection().getConnection();
                PreparedStatement Query = con.prepareStatement("delete from StudentFeeMap where DDId=?");
                Query.setInt(1, DDId);
                Query.executeUpdate();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);

                con.close();
                 return false;
            }

        }
      if(StudentList!=null&&StudentList.size()>0)
      {
          int BranchId=getBranchId(StudentList.get(0).StudentId);
                  int TutionFeeid=getTutionFeeId(BranchId, 1);
                  if (DDNumber != null && BrName != null && BankName != null && Amount != 0) {
           try {
               con=new DBConnection().getConnection();
               PreparedStatement st = con.prepareStatement("Update  DDMaster set Branch=?,Bank=?,Amount=?,AmountType=?,DDNo=?,DDDate=?,CentreId=?,OrderNo=? where DDId=?");
               st.setString(1, BrName);
               st.setString(2, BankName);
               st.setFloat(3, Amount);
               st.setString(4, AmountType);
               st.setString(5, DDNo);
               st.setString(6, convertDatetoyyyymmdd(DDDate));
               st.setInt(7,CentreId);
               st.setString(8, OrderNumber);
               st.setInt(9, DDId);
               st.executeUpdate();
               st=con.prepareStatement("delete from StudentFeeMap where DDId=?");
               st.setInt(1,DDId);
               st.executeUpdate();
               con.close();
               if (StudentList.size()>1)
               {
                        con=new DBConnection().getConnection();
                       st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,?)");
                       int i = 0;
                       while (i < StudentList.size())
                       {
                           if(StudentList.get(i).isSelected)
                           {
                           st.setInt(1, StudentList.get(i).StudentId);
                           st.setInt(2,  TutionFeeid);
                           st.setInt(3,-1);
                           st.setInt(4, DDId);
                           st.setInt(5, AttendingYear);
                           st.executeUpdate();
                           
                           }

                           i++;
                       }
                       con.close();

                   return true;
               }
                else
               {
                   con=new DBConnection().getConnection();
                           st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,?)");
                           int StudentId=StudentList.get(0).StudentId;
                           TutionFeeid=getTutionFeeId(Integer.parseInt(getCourseIdForStudent(StudentId)), 1);
                           st.setInt(1,StudentId);
                           st.setInt(2, TutionFeeid);
                           st.setInt(3, -1);
                           st.setInt(4, DDId);
                           st.setInt(5, AttendingYear);
                           st.executeUpdate();
                           con.close();
                           return true;
               }

                } catch (Exception ex) {
                  Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
                  con.close();
                    return false;
                }
            }
                  else
                  {
              return false;
                  }

       }
      else
      {
            return false;
      }
    }
  public boolean SaveStudentExamFee(CourseFeeData DD,ArrayList<StudentData> StudentList,int CentreId) throws IOException, SQLException {


        String DDNo = DD.DDNo;
        String BrName = DD.Branch;
        String BankName = DD.Bank;
        float Amount = Float.parseFloat(DD.Amount);
        String DDDate = DD.DDDate;
         String AmountType = DD.AmountType;
         int  DDId=DD.DDId;
         int sum=0;
        Calendar now = Calendar.getInstance();
        int YearofAdmission = now.get(Calendar.YEAR);
        String DDNumber = null;
        DDNumber = DDNo.trim();
        boolean Update=false;
        if(DDId!=0)
        {
            try {
                 con=new DBConnection().getConnection();
                PreparedStatement Query = con.prepareStatement("delete from StudentFeeMap where DDId=?");
                Query.setInt(1, DDId);
                Query.executeUpdate();
            
            } catch (SQLException ex) {
                Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }
      if(StudentList!=null&&StudentList.size()>0)
      {
          int BranchId=getBranchId(StudentList.get(0).StudentId);
                  int ExamFeeid=getExamFeeId(BranchId);
                  if (DDNumber != null && BrName != null && BankName != null && Amount != 0) {
           try {

               PreparedStatement st = con.prepareStatement("Update  DDMaster set Branch=?,Bank=?,Amount=?,AmountType=?,DDNo=?,DDDate=?,CentreId=? where DDId=?");
               st.setString(1, BrName);
               st.setString(2, BankName);
               st.setFloat(3, Amount);
               st.setString(4, AmountType);
               st.setString(5, DDNo);
               st.setString(6, convertDatetoyyyymmdd(DDDate));
               st.setInt(7,CentreId);
               st.setInt(8, DDId);
               st.executeUpdate();
               st=con.prepareStatement("delete from StudentFeeMap where DDId=?");
               st.setInt(1,DDId);
               st.executeUpdate();
               if (StudentList.size()>1)
               {

                       st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId) values(?,?,?,sysdate(),?)");
                       int i = 0;
                       while (i < StudentList.size())
                       {
                           if(StudentList.get(i).isSelected)
                           {
                           st.setInt(1, StudentList.get(i).StudentId);
                           st.setInt(2, -1);
                           st.setInt(3, ExamFeeid);
                           st.setInt(4, DDId);
                           st.executeUpdate();
                           }
                           i++;
                       }


                   return true;
               }
                else
               {
                           st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId) values(?,?,?,sysdate(),?)");
                           int StudentId=StudentList.get(0).StudentId;
                           st.setInt(1,StudentId);
                           st.setInt(2, -1);
                           st.setInt(3, ExamFeeid);
                           st.setInt(4, DDId);
                           st.executeUpdate();
                           return true;
               }

                } catch (Exception ex) {
                  Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
                  else
                  {
              return false;
                  }

       }
      else
      {
            return false;
      }
    }
  public String  getCourseIdForStudent(int StudentId) throws SQLException
  {
       try {
            con=new DBConnection().getConnection();
            PreparedStatement Query;
            Query = con.prepareStatement("SELECT BranchId FROM StudentPersonal where StudentId=?");
            Query.setInt(1, StudentId);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {

                return Data.getString(1);

            }
             else
            {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
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
  private int  getBranchId(int StudentId) throws SQLException
  {
       try {
            con=new DBConnection().getConnection();
            PreparedStatement Query;
            Query = con.prepareStatement("SELECT BranchId FROM StudentPersonal where StudentId=?");
            Query.setInt(1, StudentId);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                return Data.getInt(1);
            }
             else
            {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
       finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
  }
int getTutionFeeId(int BranchId,int Year) throws SQLException
    {
        try {
             con=new DBConnection().getConnection();
            PreparedStatement Query;
            Query = con.prepareStatement("select TutionFeeId from TutionFeeMaster where BranchId=? and TutionFeeYear=?");
            Query.setInt(1, BranchId);
            Query.setInt(2, Year);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                return Data.getInt(1);
            }
             else
            {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
    }
int getExamFeeId(int BranchId) throws SQLException
    {
        try {
             con=new DBConnection().getConnection();
            PreparedStatement Query;
            Query = con.prepareStatement("select ExamFeeId from ExamFeeMaster where BranchId=? and To_Date is Null");
            Query.setInt(1, BranchId);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                return Data.getInt(1);
            }
             else
            {
                return -1;
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
    }
 public  String emptyCheck(String field,String str)
    {
       if(str==null)
           return field +" is Required";
        str=str.trim();
        if(str.isEmpty())
             return field +" is Required";

        else{

        return null;}
    }
   public String DDDateCheck(String DDDate) throws SQLException
   {
        String Error=emptyCheck("DDDate", DDDate);
       if(Error!=null)
       {
           return Error;
       }
        try {
             con=new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT DATEDIFF(?,sysdate())");
            ps.setString(1, convertDatetoyyyymmdd(DDDate));
            ResultSet Diff = ps.executeQuery();
            if (Diff.next()) {
                if (Diff.getInt(1) > 0) {
                    return "DD Date is Invalid ";
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
           return "Please Check Date format";

   }
//   public String feeCheck() throws SQLException
//   {
//        StudentCourseFee Fee=new StudentCourseFee();
//        float DDAmount=Float.parseFloat(request.getParameter("Amount"));
//        int BranchId=Integer.parseInt(request.getParameter("CourseApplied"));
//        int CentreId=Integer.parseInt(request.getSession().getAttribute("CenterId").toString());
//        if(request.getParameter("DDTYPE").equals("Single"))
//        {
//             if(DDAmount==Fee.getTutionFee(BranchId,1,CentreId))
//             {
//                 return null;
//             }
//             else
//             {
//                  return "Course Fee ("+Fee.getTutionFee(BranchId,1,CentreId)+") does not match with DD Amount";
//             }
//
//        }
//        else
//        {
//             String[] Stds=request.getParameterValues("StudentId");
//             if((Stds.length*Fee.getTutionFee(BranchId, 1, CentreId)) != DDAmount)
//             {
//                  return "Course Fee ("+Fee.getTu                 }
//                      else
//                      {
//                           SIDs=","+s[1];
//                      }
//                 }
//
//                PreparedStatement st = con.prepareStatement("select QuestionId from StudentLoginMaster where StudentId in (SELECT  StudentId FROM StudentFeeMap where StudentId in ("+SIDs+"))");
//                      ResultSet rs= st.executeQuery();
//                      if(rs.next())
//                      {
//                           return  "Fee is Already paid for student with Application No "+rs.getString(1);
//                      }
//                      else
//                      {
//                          return  null;
//                      }
//
//            } catch (SQLException ex) {
//
//                Logger.getLogger(CourseDDController.class.getName()).log(Level.SEVERE, null, ex);
//                return "Could Not Connect to Server";
//            }
//          }
//}tionFee(BranchId,1,CentreId)*Stds.length+") does not match with DD Amount";
//             }
//             return null;
//        }
//   }
//public String CheckApplicationId(String ApplicationId)
//{
//        try {
//            String Error = emptyCheck("Application No", ApplicationId);
//            if (Error != null) {
//                return Error;
//            }
//            PreparedStatement Data = con.prepareStatement("select CollegeId from StudentPersonal where StudentId=(select StudentId from StudentLoginMaster where Questionid=?)");
//           Data.setInt(1, Integer.parseInt(ApplicationId));
//            ResultSet Rs = Data.executeQuery();
//            if (Rs.next()) {
//                if (Rs.getInt(1) == CentreId) {
//                    return null;
//                }
//            }
//            return "Invalid Application Number";
//        } catch (SQLException ex) {
//
//            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
//               return "Some Error Related to  Application Number occurred ; please contact M G university SystemAdmin Dept";
//        }
//}
public String AmountCheck(String amount)
{
    String err=emptyCheck("DD Amount", amount);
     if(err==null)
     {
           try{
          Float.parseFloat(amount);
           }catch(NumberFormatException e)
             {
               return "DD Amount is not valid";
             }
           return null;
     }
     else
     {
         return err;
     }

}
public  String convertDatetoyyyymmdd(String date) {
        String dd = date.substring(0, 2);
        String mm = date.substring(3, 5);
        String yy = date.substring(6, 10);
        String ymd = yy + "-" + mm + "-" + dd;
        return ymd;
    }

 public  String convertDatetoddmmyyyy(String date) {
        String dd = date.substring(8, 10);
        String mm = date.substring(5, 7);
        String yy = date.substring(0, 4);
        String ymd = dd + "-" + mm + "-" + yy;
        return ymd;
    }
//public String CheckStudentDetails() throws SQLException
//{
//
//  String DDType=request.getParameter("DDTYPE");
//    if(DDType!=null&&DDType.equals("Bulk"))
//    {
//          if(request.getParameter("CourseApplied").equals("-1"))
//          {
//              return "No course selected";
//          }
//          else
//          {
//               if(request.getParameterValues("StudentId")==null||request.getParameterValues("StudentId").length<1)
//               {
//                   return "No Students selected";
//
//               }
//               else
//               {
//                    return  StudentIdCheck(request);
//
//               }
//
//          }
//    }
//    else if(DDType!=null&&DDType.equals("Single"))
//    {
//
//
//       String ApplicationNo= request.getParameter("ApplicationNo").trim();
//            int CollegeId=Integer.parseInt(request.getSession().getAttribute("CenterId").toString());
//            try{
//           if(ApplicationNo==null||Integer.parseInt(ApplicationNo)<100001)
//           {
//               return "Invalid Application Number";
//
//           }
//          else
//           {
//               return  ApplicationNoCheck(ApplicationNo,request.getSession().getAttribute("CenterId").toString());
//
//
//           }
//            }catch(NumberFormatException e )
//            { return  "Invalid Application Number";
//            }
//    }
//   return null;
//
//}
public   String ApplicationNoCheck(String  ApplicationNo,int Centre,String DDNo) throws SQLException
{
    con=new DBConnection().getConnection();
    int StudentId=0;
    int ddno=0;
     PreparedStatement st=con.prepareStatement("SELECT l.StudentId FROM StudentLoginMaster l,StudentPersonal p where QuestionId=? and l.StudentId=p.StudentId and CollegeId=?");
       try{
       st.setInt(1, Integer.parseInt(ApplicationNo));
       st.setInt(2, Centre);
        ResultSet Rs=st.executeQuery();
            if(Rs.next())
            {
                 StudentId=Rs.getInt(1);

                 ddno=Integer.parseInt(DDNo);
                 return StudentIdCheckforSingleDD(StudentId,ddno);
            }
            else
            {
                   return "Invalid Application No";
            }

       }catch(NumberFormatException e)
       {
             return "Invalid Application No";
       }

    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }

}
public   String StudentApplicationNoCheck(String  ApplicationNo,int Centre,String DDNo) throws SQLException
{
    int StudentId=0;
    int ddno=0;
    con=new DBConnection().getConnection();
     PreparedStatement st=con.prepareStatement("SELECT l.StudentId FROM StudentLoginMaster l,StudentPersonal p where QuestionId=? and l.StudentId=p.StudentId and CollegeId=?");
       try{
       st.setInt(1, Integer.parseInt(ApplicationNo));
       st.setInt(2, Centre);
       ResultSet Rs=st.executeQuery();
            if(Rs.next())
            {
                 StudentId=Rs.getInt(1);

                 ddno=Integer.parseInt(DDNo);
                 return StudentIdCheckforSingleDDEdit(StudentId,ddno);
            }
            else
            {
                   return "Invalid Application No";
            }

       }catch(NumberFormatException e)
       {
             return "Invalid Application No";
       }

    finally
            {
                if(con!=null)
                {
                    con.close();
                }
            }

}
public  String  StudentIdCheckforSingleDD(int Stu,int DDId) throws SQLException
{
con=new DBConnection().getConnection();
          if(Stu==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {


                PreparedStatement st = con.prepareStatement("select l.QuestionId from StudentLoginMaster l Inner Join StudentFeeMap f on l.StudentId=f.StudentId where f.StudentId=? and DDId!=? and f.TutionFeeId>0");

                st.setInt(1,Stu);
                st.setInt(2,DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with Application No "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public  String  StudentIdCheckforSingleDDEdit(int Stu,int DDId) throws SQLException
{
          con=new DBConnection().getConnection();
          if(Stu==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {
         
               
                PreparedStatement st = con.prepareStatement("select l.QuestionId from StudentLoginMaster l Inner Join StudentFeeMap f on l.StudentId=f.StudentId where f.StudentId=? and DDId!=? and f.TutionFeeId>0");
           
                st.setInt(1,Stu);
                st.setInt(2,DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with Application No "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public  String  StudentIdCheckEdit(String[] Stu,int DDId) throws SQLException
{
    con=new DBConnection().getConnection();

          if(Stu.length==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {

                String SIDs="";
                 for(int i=0;i<Stu.length;i++)
                 {
                      if(i==0)
                      {
                          SIDs=Stu[0];
                      }
                      else
                      {
                           SIDs=SIDs+","+Stu[i];
                      }
                 }

                PreparedStatement st = con.prepareStatement("select QuestionId from StudentLoginMaster where StudentId in (SELECT  StudentId FROM StudentFeeMap where  DDId != ? and StudentId in ("+SIDs+") and TutionFeeId>0) ");
                st.setInt(1, DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with Application No "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public  String  StudentIdCheck(String[] Stu,int DDId) throws SQLException
{
          con=new DBConnection().getConnection();
          if(Stu.length==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {
         
                String SIDs="";
                 for(int i=0;i<Stu.length;i++)
                 {
                      if(i==0)
                      {
                          SIDs=Stu[0];
                      }
                      else
                      {
                           SIDs=SIDs+","+Stu[i];
                      }
                 }

                PreparedStatement st = con.prepareStatement("select QuestionId from StudentLoginMaster where StudentId in (SELECT  StudentId FROM StudentFeeMap where  DDId != ? and StudentId in ("+SIDs+")and TutionFeeId>0) ");
                st.setInt(1, DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with Application No "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public  String  StudentIdCheckforSingleDDUsingPRN(int Stu,int DDId) throws SQLException
{
        con=new DBConnection().getConnection();
          if(Stu==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {


                PreparedStatement st = con.prepareStatement("select PRN from StudentPersonal where StudentId in (SELECT  StudentId FROM StudentFeeMap where  StudentId =? and DDId!=? )");

                st.setInt(1,Stu);
                st.setInt(2,DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with PRN "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public  String  StudentIdCheckUsingPRN(String[] Stu,int DDId) throws SQLException
{
            con=new DBConnection().getConnection();
          if(Stu.length==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {

                String SIDs="";
                 for(int i=0;i<Stu.length;i++)
                 {
                      if(i==0)
                      {
                          SIDs=Stu[0];
                      }
                      else
                      {
                           SIDs=SIDs+","+Stu[i];
                      }
                 }

                PreparedStatement st = con.prepareStatement("select PRN from StudentPersonal where StudentId in (SELECT  StudentId FROM StudentFeeMap where  DDId != ? and ExamFeeId>0 and StudentId in ("+SIDs+"))");
                st.setInt(1, DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with PRN "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public  boolean isAvailableDDNo(String ddNumber) throws SQLException
{
     try {
         con=new DBConnection().getConnection();
            int dd=0;
            PreparedStatement st = con.prepareStatement("select count(*) as count from DDMaster where DDNo= ?");
            st.setString(1, ddNumber);
            ResultSet rs = st.executeQuery();
            rs.next();

            if (rs.getInt("count") > 0) {
               return  false;
            }
            else
            {
              return  true;
            }

        } catch (SQLException ex) {

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
public  boolean isAvailableDDNoforDDId(String ddNumber,int DDId) throws SQLException
{

     try {

            int dd=0;
            con=new DBConnection().getConnection();

            PreparedStatement st = con.prepareStatement("select count(*) as count from DDMaster where DDNo= ? and DDId!=?");
            st.setString(1, ddNumber);
            st.setInt(2, DDId);
            ResultSet rs = st.executeQuery();
            rs.next();

            if (rs.getInt("count") > 0)
            {
               return  false;
            }
            else
            {
              return  true;
            }

        } catch (SQLException ex) {

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
public String UserPrivillegeCheck(int DDId,String User) throws SQLException
{
        try {
            con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("Select Count(*) from CentreUserMap where CollegeId =(SELECT CentreId  FROM DEMS_db.DDMaster where DDId=?) and UserName=? and EndDate is null");
            Query.setInt(1, DDId);
            Query.setString(2, User);
            ResultSet Rs = Query.executeQuery();
            if(Rs.next())
            {
                  if(Rs.getInt(1)==1)
                      return null;
            }
            return "Invalid DD or you do not have privillege to access this DD";
        } catch (SQLException ex) {
            Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
            return "Cann't Connect to Sever please try after some time";
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
         con.close();

}
public   String PRNNoCheck(String  PRN,int Centre,String DDId) throws SQLException
{
    int StudentId=0;
    int ddid=0;
    con=new DBConnection().getConnection();
     PreparedStatement st=con.prepareStatement("SELECT StudentId FROM StudentPersonal  where PRN=? and CollegeId=?");
       try{
       st.setString(1, PRN);
       st.setInt(2, Centre);
       ResultSet Rs=st.executeQuery();
            if(Rs.next())
            {
                 StudentId=Rs.getInt(1);

                 ddid=Integer.parseInt(DDId);
                 return StudentIdCheckforSingleDD_PRN(StudentId,ddid);
            }
            else
            {
                   return "Invalid PRN";
            }
       }catch(NumberFormatException e)
       {
             return "Invalid PRN";
       }

    finally
            {
                if(con!=null)
                {
                    con.close();
                }
            }

}
public  String  StudentIdCheckforSingleDD_PRN(int Stu,int DDId) throws SQLException
{

          if(Stu==0)
          {
               return "No Students Selected";
          }
          else
          {
            try {

                con=new DBConnection().getConnection();
                PreparedStatement st = con.prepareStatement("select PRN from StudentPersonal where StudentId in (SELECT  StudentId FROM StudentFeeMap where  StudentId =? and DDId!=? and ExamFeeId>0)");

                st.setInt(1,Stu);
                st.setInt(2,DDId);
                ResultSet rs= st.executeQuery();
                      if(rs.next())
                      {
                           return  "Fee is Already paid for student with PRN   "+rs.getString(1);
                      }
                      else
                      {
                          return  null;
                      }

            } catch (SQLException ex) {

                Logger.getLogger(mCourseFeeDD.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
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
public ArrayList<FeeData> getStudentFeeDetails(int StudentId) throws SQLException
    {
         Connection con=null;
         ArrayList<FeeData> FeeData=new ArrayList<FeeData>();
            try
            {
                con = new DBConnection().getConnection();
                PreparedStatement st=con.prepareStatement("SELECT d.DDNo,d.DDDate,d.Amount,d.AmountType,f.StudentAttendingYear,f.TutionFeeId,f.ExamFeeId FROM StudentFeeMap f inner join DDMaster d on d.DDId=f.DDId where f.StudentId=?");
                st.setInt(1,StudentId);
                ResultSet Rs=st.executeQuery();
                while(Rs.next())
                {
                    FeeData Fee=new FeeData();
                    String Amt=null;
                    Fee.DDNo=Rs.getString("DDNo");
                    if(Rs.getString("AmountType").equals("INR"))
                    {
                        Amt="Rs. ";
                    }
                    else
                    {
                        Amt="$ ";
                    }
                    Fee.Amount=Amt+ Rs.getString("Amount");
                    Fee.AttendingYear=Rs.getInt("StudentAttendingYear");
                    if(Rs.getInt("TutionFeeId")>0)
                    {
                        Fee.FeeType="Tution Fee";
                    }
                    else
                    {
                        Fee.FeeType="Exam Fee";
                    }
                    FeeData.add(Fee);
                }
                return FeeData;
            }
            catch (SQLException ex)
            {
                Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<DD> getDDForStudent(int StudentId) throws SQLException
{
    try {
            ArrayList<DD> StudentDD=new ArrayList<DD>();
            con=new DBConnection().getConnection();
            PreparedStatement Query;
            Query = con.prepareStatement("SELECT d.DDNo,d.Amount,Date_Format(DDDate,'%d-%m-%Y')  'DDDate',d.AmountType FROM `DEMS_db`.`StudentFeeMap` s inner join DDMaster d  on s.DDId=d.DDId where StudentId=? and TutionFeeId>0 order by s.StudentAttendingYear");
            Query.setInt(1, StudentId);
            ResultSet Data = Query.executeQuery();
            while (Data.next())
            {
                DD dd=new DD();
                dd.setAmount(Data.getFloat(2));
                dd.setDDDate(Data.getString(3));
                dd.setDDNumber(Data.getString(1));
                if(Data.getString(4).equals("INR"))
                dd.setAmountType("Rs.");
                else
                    dd.setAmountType("$");
                
                StudentDD.add(dd);
            }
            return StudentDD;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mCourseFee.class.getName()).log(Level.SEVERE, null, ex);
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
}



