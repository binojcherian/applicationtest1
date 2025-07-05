/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DBConnection;
import model.ExamRegistration;
import model.ExamRegistrationFee;

public class CentreExamFee
{
    String CentreId,Bank,Branch,DDDate,DDNo,Amount,ErrorMsg=null,DDNoError=null,DDDateError=null,BankError=null,BranchError=null,YearError=null,AmountError=null,AmountType,CourseId;
    int Year,Count;
    boolean isSaved=false,Search=false;
    HttpServletRequest request;
    HttpServletResponse response;
    CurrentAdmissionYear CurYear=new CurrentAdmissionYear();
    
  public CentreExamFee(HttpServletRequest request,HttpServletResponse response) throws SQLException, ParseException
    {
      ExamRegistrationFee Fee=new ExamRegistrationFee(request, response);
      ExamRegistration reg=new ExamRegistration();
      this.request=request;
      this.response=response;
//      if(request.getSession().getAttribute("yearApplied")!=null )
//      {
//            Year=Integer.parseInt(request.getSession().getAttribute("yearApplied").toString());;
//      }
        //CourseId=request.getSession().getAttribute("BranchId").toString();
           if(request.getParameter("CentreId")!=null)
        {
            CentreId = request.getParameter("CentreId");
            request.getSession().setAttribute("CentreId", CentreId);
        }

        if (request.getParameter("year_applied") != null) {
            Year = Integer.parseInt(request.getParameter("year_applied"));
            request.getSession().setAttribute("Year", Year);

        }
        if (request.getParameter("course_applied") != null) {
            CourseId = request.getParameter("course_applied");
            request.getSession().setAttribute("CourseId", CourseId);

        }
      if(request.getParameter("search_button")!=null && request.getParameter("search_button").equals("Search"))
      {
          if(CentreId.equals("-1") || CentreId==null)
          {
            ErrorMsg="Select Centre";
          }
         else
          {
              if(Year==-1 )
              {
                  ErrorMsg="Select Year";
              }
              else
              {
                  if(CourseId==null || CourseId.equals("-1"))
                  {
                      ErrorMsg="Select Course";
                  }
                  else
                  {
                      Search=true;
                  }
              }
          }
      }
        if(request.getParameter("Save")!=null && request.getParameter("Save").equals("Submit"))
        {
            
        //CentreId=request.getParameter("CentreId");
        Bank=request.getParameter("BankName");
        Branch=request.getParameter("BrName");
        DDDate=request.getParameter("ddDate");
        DDNo=request.getParameter("DDNo");
        Amount=request.getParameter("Amount");
        AmountType=request.getParameter("AmountType");
            DDNoError=DDNoCheck();
            if(DDNoError==null)
            {
                DDDateError=DDDateCheck();
                if(DDDateError==null)
                {
                    BankError=BankCheck();
                    if(BankError==null)
                    {
                        BranchError=BranchCheck();
                        if(BranchError==null)
                        {                             
                                    AmountError=AmountTypeCheck();
                                    if(AmountError==null)
                                    {
                                    DDNoError=isFeeAlreadyPaid();
                                    if(DDNoError==null)
                                    {
                                     isSaved=Fee.SaveExamRegistrationFee(DDNo, convertDatetoyyyymmdd(DDDate), Bank, Branch, CourseId, CentreId, Amount, Year, AmountType);
                                     reg.SaveExamLog("DDAdded"+DDNo, request);
                                     Search=true;
                                     if(request.getSession().getAttribute("CentreId")!=null)
                                     {
                                         CentreId=request.getSession().getAttribute("CentreId").toString();
                                     }
                                     if(request.getSession().getAttribute("Year")!=null)
                                     {
                                         Year=Integer.parseInt(request.getSession().getAttribute("Year").toString());
                                     }
                                     if(request.getSession().getAttribute("CourseId")!=null)
                                     {
                                         CourseId=request.getSession().getAttribute("CourseId").toString();
                                     }
                                    
                                    }
                            }
                                    
                                
                            
                        }
                    }
                }
            }
        }
      if(request.getParameter("AV")!=null && request.getParameter("AV").equals("1"))
      {
         Search=true;
         if(request.getSession().getAttribute("CentreId")!=null)
         {
             CentreId=request.getSession().getAttribute("CentreId").toString();
         }
         if(request.getSession().getAttribute("Year")!=null)
         {
             Year=Integer.parseInt(request.getSession().getAttribute("Year").toString());
         }
         if(request.getSession().getAttribute("CourseId")!=null)
         {
             CourseId=request.getSession().getAttribute("CourseId").toString();
         }
      }
      if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Approve Selected DDs"))
      {
           String regstdDD[] = request.getParameterValues("Select");

            if (regstdDD != null && regstdDD.length != 0) {
                try {
//                    registration.deleteExamRegister(BranchId, CenterId,yearApplied);
//                      boolean insertOk=false;
                    for (int i = 0; i < regstdDD.length; i++) {

                        if (Fee.ApproveDD(regstdDD[i])) {
                          //insertOk=true;
                            reg.SaveExamLog("ApprovedDD"+regstdDD[i], request);

                    } else {
                            ErrorMsg = "Some error happened during Process";
                        }
                    }
//                      if(insertOk)
//                      {
//                         redirected=true;
//                         response.sendRedirect("CentreExamFee.jsp");
//                      }
                } catch (Exception ex) {

                    Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
                                    Search=true;
                                     if(request.getSession().getAttribute("CentreId")!=null)
                                     {
                                         CentreId=request.getSession().getAttribute("CentreId").toString();
                                     }
                                     if(request.getSession().getAttribute("Year")!=null)
                                     {
                                         Year=Integer.parseInt(request.getSession().getAttribute("Year").toString());
                                     }
                                     if(request.getSession().getAttribute("CourseId")!=null)
                                     {
                                         CourseId=request.getSession().getAttribute("CourseId").toString();
                                     }
      }
    }


  public String AmountTypeCheck()throws SQLException
 {
      ExamRegistrationFee Fee=new ExamRegistrationFee(request, response);
     String Type=Fee.getCountryForCentre(Integer.parseInt(CentreId));
     if(!Type.equals(AmountType))
     {
         if(Type.equals("INR"))
         {
         return "Amount Type should be INR";
         }
         if(Type.equals("USD"))
         {
             return "Amount Type should be USD";
         }
         return null;

     }

return null;

    }
  public boolean getSearch()
    {
    return Search;
}
public boolean isCenterVerificationOver() throws SQLException
{
    Connection con=null;

        try {
               con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("select datediff(Center_Ver_EndDate,sysdate()) from AdmissionDates where AdmissionYear=?");
            ps.setInt(1, CurYear.getCurrentAdmissionYear());
            ResultSet Diff = ps.executeQuery();
            if (Diff.next())
            {
                if (Diff.getInt(1) < 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;

        } catch (SQLException ex) {
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       finally
        {
            con.close();
        }

}
public boolean IsExamnFeeDateOver(String DDDate) throws SQLException
{
Connection con=null;
        try {
               con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("select datediff(EndDate,?) from DDDate");
            ps.setString(1, DDDate);
            ResultSet Diff = ps.executeQuery();
            if (Diff.next())
            {
                if (Diff.getInt(1) >= 0)
                {
                    return true;
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

        } catch (SQLException ex) {
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       finally
        {
            con.close();
        }
}
  public String isFeeAlreadyPaid() throws SQLException
  {
      Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select count(*) from DDMaster where DDNo=? ");
            st.setString(1, DDNo);
           
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return "DD is Already Entered";
                }
                return null;
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
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

  public String getCourseApplied() {
        if (CourseId == null) {
            return "-1";
        } else {
            return CourseId;
        }
    }
    public String getCenterId()
    {
        if (CentreId == null) {
            return "-1";
        } else {
            return CentreId;
        }
    }

    public int getYearApplied() {
        
            return Year;
        
    }
  public String getQuery() throws SQLException
  {
      String Query="SELECT distinct SQL_CALC_FOUND_ROWS DDNo,Bank,Branch,DDDate,Amount,d.DDId FROM DDMaster d INNER JOIN CourseDDMap a on a.DDId=d.DDId where a.ExamId=(select max(ExamId) from CourseDDMap)";
      
      return Query;
  }
  public String getisSaved()
  {
      if(isSaved==true)
      {
          Bank="";Branch="";DDDate="";DDNo="";Amount="";
          return "DD  Entered Successfully";
      }
      return null;
  }
  public String getDDNoError()
  {
      return DDNoError;
  }
  public String getDDDateError()
  {
      return DDDateError;
  }
  public String getBankError()
  {
      return BankError;
  }
  public String getBranchError()
  {
      return BranchError;
  }
  public String getYearError()
  {
      return YearError;
  }
  public String getAmountError()
  {
      return AmountError;
  }
  public String getDDNo()
  {
      if(DDNo==null)
      {
          return "";
      }
      return DDNo;
  }
  public String getDDDate()
  {
      if(DDDate==null)
      {
          return "";
      }
      return DDDate;
  }
  public String getBank()
  {
      if(Bank==null)
      {
          return "";
      }
      return Bank;
  }
  public String getBranch()
  {
      if(Branch==null)
      {
          return "";
      }
      return Branch;
  }
  public int getYear()
  {
      return Year;
  }

  
  public int getCount()
  {
      return Count;
  }
  public String getAmount()
  {
      if(Amount==null)
      {
          return "";
      }
      return Amount;
  }
  public String getAmountType()
  {

      return AmountType;
  }
 public String DDDateCheck() throws ParseException, SQLException
 {
         Connection con=null;
        String Error=emptyCheck("DDDate", DDDate);
//       if(Error!=null)
//       {
           return Error;
//       }
//        try {
//               con = new DBConnection().getConnection();
//            PreparedStatement ps = con.prepareStatement("select datediff(EndDate,?),EndDate from DDDate");
//            ps.setString(1, convertDatetoyyyymmdd(DDDate));
//            ResultSet Diff = ps.executeQuery();
//            if (Diff.next())
//            {
//                if (Diff.getInt(1) < 0)
//                {
//                    return "DD Date is Invalid (DD should be taken before "+convertDatetoddmmyyyy(Diff.getString(2))+")" ;
//                }
//                else
//                {
//                    return null;
//                }
//            }
//            return "Please Check Date format";
//
//        } catch (SQLException ex) {
//            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
//            return "Please Check Date format";
//        }
//       finally
//        {
//            con.close();
//        }

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
 public String BankCheck()
 {
    String Error= emptyCheck("Bank", Bank);
     if(Error==null)
     {
         if(Bank.length()<3)
         {
             return "Invalid Bank Name";
         }

     }
     return Error;
}
 public String BranchCheck()
 {
    String Error= emptyCheck("Branch", Branch);
     if(Error==null)
     {
         if(Branch.length()<3)
         {
             return "Invalid Branch Name";
         }

     }
     return Error;
}
 public  String convertDatetoyyyymmdd(String date)
 {
        String dd = date.substring(0, 2);
        String mm = date.substring(3, 5);
        String yy = date.substring(6, 10);
        String ymd = yy + "-" + mm + "-" + dd;
        return ymd;
    }
 public static String convertDatetoddmmyyyy(String date)
 {
        String dd = date.substring(8, 10);
        String mm = date.substring(5, 7);
        String yy = date.substring(0, 4);
        String ymd = dd + "-" + mm + "-" + yy;
        return ymd;
    }
 public String DDNoCheck() throws SQLException
 {
     String Error= emptyCheck("DDNo", DDNo);
     if(Error==null)
     {
         if(DDNo.length()<6)
         {
             return "Invalid DD No";
         }
         else
         {
             if(isAvailableDDNo()==false)
             {
                 return "DDNo Already exists";
             }
             return null;
         }

     }
     return Error;
}


 public String getCountryForCentre(String CentreId) throws SQLException
 {
    String Country="India";
    Connection con=null;
    String AmountType=null;
    try
    {
         con=new DBConnection().getConnection();
    PreparedStatement Query = con.prepareStatement("SELECT CollegeCountry FROM CollegeMaster where CollegeId=?");
            Query.setString(1, CentreId);
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
    catch (SQLException ex)
    {
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
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
 public int getStudentsCount(String CentreId,int Year) throws SQLException
 {
     CurrentAdmissionYear CurYear=new CurrentAdmissionYear();
     Connection con=null;
     PreparedStatement st=null;
        try
        {
            con = new DBConnection().getConnection();
            if(Year==1)
            {
            st =con.prepareStatement("SELECT count(*) FROM StudentPersonal where CollegeId=? and IsCenterVerified=1 and JoiningYear=?");
            st.setString(1, CentreId);
            st.setInt(2,CurYear.getCurrentAdmissionYear());
            }
            else
            {
            st=con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentPersonal` where CollegeId=? and AttendingYear=?");
            st.setString(1, CentreId);
            st.setInt(2, Year);
            }
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
            else
                return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CentreExamFee.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }

    }
 public  boolean isAvailableDDNo() throws SQLException
 {
     Connection con=null;

     try {
             con = new DBConnection().getConnection();
            int dd=0;

            PreparedStatement st = con.prepareStatement("select count(*) as count from DDMaster where DDNo= ?");
            st.setString(1, DDNo);
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

        } 
     catch (SQLException ex)
     {
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
}
