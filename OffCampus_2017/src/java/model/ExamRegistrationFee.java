/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mgu
 */
public class ExamRegistrationFee
{

    HttpServletRequest request;
    HttpServletResponse response;
    String CenterId = null;
    PrintWriter out = null;
    int DDId = 0;
    int AttendingYear=0;
    ArrayList<SelectedStudent> DDStudentsList=null;
    Hashtable<String, String> CourseFee = new Hashtable<String, String>();
    AdmissionYear CurYear=new AdmissionYear();

   
    public ExamRegistrationFee(HttpServletRequest request, HttpServletResponse response) throws SQLException
    {
        this.request = request;
        this.response = response;
        CenterId = (String) request.getSession().getAttribute("CenterId");
        if(request.getParameter("AttendingYear")!=null && request.getParameter("AttendingYear").equals("-1"))
        {
            AttendingYear=Integer.parseInt(request.getParameter("AttendingYear"));
        }
    }

    public int getYear()
    {
        return AttendingYear;
    }
    public int getAutomationFeeId(int Year) throws SQLException
    {
        Connection con=null;
        try
        {
        con=new DBConnection().getConnection();
        PreparedStatement st=con.prepareStatement("select FeeMasterId from FeeMaster where FeeItem='AutomationFee' and AttendingYear=?");
        st.setInt(1, Year);
        ResultSet rs=st.executeQuery();
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
    public String getCountryForCentre(int CentreId) throws SQLException
    {
    String Country="India";
    String AmountType=null;
    Connection con=null;
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
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean SaveExamRegistrationFee(String DDNo,String DDDate,String Bank,String Branch,String CourseId,String CentreId,String Amount,int Year, String AmountType) throws SQLException
    {
        Connection con=null;
        try
        {
        con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement Query = con.prepareStatement("insert into DDMaster(Branch,Bank,Amount,AmountType,DDNo,DDDate,EnteredDate,CentreId,OrderNo) values(?,?,?,?,?,?,sysdate(),?,0)");
            Query.setString(1, Branch);
            Query.setString(2,Bank );
            Query.setString(3,Amount);
            Query.setString(4,AmountType);
            Query.setString(5, DDNo);
            Query.setString(6, DDDate );
            Query.setString(7,CentreId);
            //Query.setString(8, OrderNumber);

            Query.executeUpdate();
            
            Query=con.prepareStatement("SELECT DDId FROM DDMaster where Bank=? and Branch =? and DDNo=? and CentreId=?");
            Query.setString(1,Bank);
            Query.setString(2,Branch);
            Query.setString(3, DDNo);
            Query.setString(4,CentreId);
            ResultSet Data=Query.executeQuery();
            int DDId=0;
            while (Data.next())
            {
                DDId=Data.getInt(1);
            }
            

            Query=con.prepareStatement("insert into  CourseDDMap (Courseid,Semoryear,Entereddate,DDId,ExamId) values(?,?,now(),?,(select max(ExamId) from ExamMaster))");
            Query.setString(1,CourseId);
            Query.setInt(2,Year);                  
            Query.setInt(3,DDId);
            Query.execute();
            con.commit();
            return true;

        }
        catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
           con.rollback();

            return false;
        }finally{
            con.commit();
         con.setAutoCommit(true);

         if(con!=null)
         {
             con.close();
         }
        }
    }

    public boolean ApproveDD(String DDId) throws SQLException
    {
        Connection con=null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("Update CourseDDMap set IsMguApproved=1,IsConfirmed=1 where DDId=?");
            ps.setString(1, DDId);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
    public Hashtable getCourseFee(int DDId) throws IOException, SQLException
    {
        Connection con=null;
        try {
           con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Branch,Bank,Amount ,AmountType,DDNo,DDDate,CentreId FROM DDMaster where DDId=?");
            ps.setInt(1, DDId);
            ResultSet Data = ps.executeQuery();
            if (Data.next())
            {
                CourseFee.put("Branch", Data.getString(1));
                CourseFee.put("Bank", Data.getString(2));
                CourseFee.put("Amount", Data.getString(3));
                CourseFee.put("AmountType", Data.getString(4));
                CourseFee.put("DDNo", Data.getString(5));
                CourseFee.put("DDDate", convertDatetoddmmyyyy(Data.getString(6)));
                CourseFee.put("CentreId", Data.getString(7));
                
            }
            ps=con.prepareStatement("select StudentName,QuestionId,CollegeId,BranchId from StudentLoginMaster l, StudentPersonal p,StudentFeeMap f where DDId=? and l.StudentId=f.StudentId and p.StudentId=f.StudentId");
            ps.setInt(1, DDId);
            ResultSet data=ps.executeQuery();
            int count=0;
             while(data.next())
             {
                 count++;
                 if(count>1)
                 {
                     break;
                 }
                 CourseFee.put("StudentName", data.getString(1));
                 CourseFee.put("ApplicationNo", data.getString(2));
                 CourseFee.put("CentreId", data.getString(3));
                 CourseFee.put("BranchId", data.getString(4));
             }
            if(count>1)
            {
                
                CourseFee.put("Bulk", "Yes");
             // getDDStudentsDetails(CourseFee.get("CentreId"),CourseFee.get("BranchId"),DDId);
               // getDDStudentsDetails(""+74,""+14,9);
            }
            else
            {
                CourseFee.put("Bulk","No");
            }
             
            return CourseFee;
        } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
//public get
    public boolean SaveStudentCourseFee() throws IOException, SQLException
    {
         Connection con = new DBConnection().getConnection();
        String DDNo = request.getParameter("DDNo");
        String BrName = request.getParameter("BrName");
        String BankName = request.getParameter("BankName");
        float Amount = Float.parseFloat(request.getParameter("Amount"));
        String ddDate = request.getParameter("ddDate");
         String amountType = request.getParameter("AmountType");
         int sum=0;
        Calendar now = Calendar.getInstance();
        int YearofAdmission = now.get(Calendar.YEAR);
        String DDNumber = null;
        DDNumber = DDNo.trim();
        boolean Update=false;
        if(request.getParameter("DDId")!=null)
        {
            try
            {
                PreparedStatement Query = con.prepareStatement("delete from StudentFeeMap where DDId=?");
                Query.setInt(1, Integer.parseInt(request.getParameter("DDId")));
                Query.executeUpdate();
                Query = con.prepareStatement("delete from DDMaster where DDId=?");
                Query.setInt(1, Integer.parseInt(request.getParameter("DDId")));
                Query.executeUpdate();
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            

        }
      if(request.getParameter("DDTYPE")!=null)
      {
          int BranchId=Integer.parseInt(request.getParameter("CourseApplied"));
          int TutionFeeid=getTutionFeeId(Integer.parseInt(CenterId),BranchId, 1);
                  if (DDNumber != null && BrName != null && BankName != null && Amount != 0) {
           try {
               PreparedStatement st = con.prepareStatement("insert into DDMaster (Branch,Bank,Amount,AmountType,DDNo,DDDate,EnteredDate,CentreId,OrderNo) values(?,?,?,?,?,?,now(),?,0)");
               st.setString(1, BrName);
               st.setString(2, BankName);
               st.setFloat(3, Amount);
               st.setString(4, amountType);
               st.setString(5, DDNo);
               st.setString(6, convertDatetoyyyymmdd(ddDate));
               st.setInt(7,Integer.parseInt(request.getSession().getAttribute("CenterId").toString()));
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
               if (request.getParameter("DDTYPE").equals("Bulk"))
               {
                    String[] Students = request.getParameterValues("StudentId");
                        if (Students.length > 0)
                   {
                       st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,1)");
                       int i = 0;
                       while (i < Students.length)
                       {
                           st.setInt(1, Integer.parseInt(Students[i]));
                           st.setInt(2, TutionFeeid);
                           st.setInt(3, -1);
                           st.setInt(4, DDId);
                           st.executeUpdate();
                           i++;
                       }
                   }
                  
                   return true;
               }
                else
               {
                           int StudentId=getStudentId(Integer.parseInt(request.getParameter("ApplicationNo")),Integer.parseInt(CenterId));
                           TutionFeeid=getTutionFeeId(Integer.parseInt(CenterId),Integer.parseInt(getCourseIdForStudent(StudentId)),1);
                           st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,1)");
                           
                           st.setInt(1,StudentId);
                           st.setInt(2,TutionFeeid);
                           st.setInt(3,-1);
                           st.setInt(4, DDId);
                           st.executeUpdate();
                         return true;
               }      
                }
           catch (Exception ex) {
                   response.getWriter().print(ex);

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
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
     public boolean SaveStudentCourseFeePRNStudents() throws IOException, SQLException
    {
         Connection con = new DBConnection().getConnection();
        String DDNo = request.getParameter("DDNo");
        String BrName = request.getParameter("BrName");
        String BankName = request.getParameter("BankName");
        float Amount = Float.parseFloat(request.getParameter("Amount"));
        String ddDate = request.getParameter("ddDate");
         String amountType = request.getParameter("AmountType");
         int sum=0;
        Calendar now = Calendar.getInstance();
        int YearofAdmission = now.get(Calendar.YEAR);
        String DDNumber = null;
        DDNumber = DDNo.trim();
        boolean Update=false;
        if(request.getParameter("DDId")!=null)
        {
            try
            {

                PreparedStatement Query = con.prepareStatement("delete from StudentFeeMap where DDId=?");
                Query.setInt(1, Integer.parseInt(request.getParameter("DDId")));
                Query.executeUpdate();
                Query = con.prepareStatement("delete from DDMaster where DDId=?");
                Query.setInt(1, Integer.parseInt(request.getParameter("DDId")));
                Query.executeUpdate();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }


        }
      if(request.getParameter("DDTYPE")!=null)
      {
          int BranchId=Integer.parseInt(request.getParameter("CourseApplied"));
          int TutionFeeid=getTutionFeeId(Integer.parseInt(CenterId),BranchId, 1);
                  if (DDNumber != null && BrName != null && BankName != null && Amount != 0) {
           try {
               PreparedStatement st = con.prepareStatement("insert into DDMaster (Branch,Bank,Amount,AmountType,DDNo,DDDate,EnteredDate,CentreId,OrderNo) values(?,?,?,?,?,?,now(),?,0)");
               st.setString(1, BrName);
               st.setString(2, BankName);
               st.setFloat(3, Amount);
               st.setString(4, amountType);
               st.setString(5, DDNo);
               st.setString(6, convertDatetoyyyymmdd(ddDate));
               st.setInt(7,Integer.parseInt(request.getSession().getAttribute("CenterId").toString()));
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
               if (request.getParameter("DDTYPE").equals("Bulk"))
               {
                    String[] Students = request.getParameterValues("StudentId");
                        if (Students.length > 0)
                   {
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
                   }

                   return true;
               }
                else
               {
                           st = con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId,StudentAttendingYear) values(?,?,?,sysdate(),?,?)");
                           int StudentId=getStudentIdFromPRN(request.getParameter("PRN"));
                           st.setInt(1,StudentId);
                           st.setInt(2, getTutionFeeId(Integer.parseInt(CenterId),Integer.parseInt(getCourseIdForStudent(StudentId)), getStudentAttendingYear(StudentId)));
                           st.setInt(3, -1);
                           st.setInt(4, DDId);
                           st.setInt(5, getStudentAttendingYear(StudentId));
                           st.executeUpdate();
                         return true;
               }
                }
           catch (Exception ex) {
                   response.getWriter().print(ex);

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

    public boolean SaveStudentAttendingYear() throws SQLException
    {
        Connection con=null;
        try
        {
              con = new DBConnection().getConnection();
             if (request.getParameter("DDTYPE").equals("Single"))
               {
            
            PreparedStatement st=con.prepareStatement("Update StudentPersonal set AttendingYear=1 where StudentId=?");
            st.setInt(1, getStudentId(Integer.parseInt(request.getParameter("ApplicationNo")),Integer.parseInt(CenterId)));

            st.executeUpdate();
            return true;
             }
             else
             {
                  String[] Students = request.getParameterValues("StudentId");
                   if (Students.length > 0)
                   {
             PreparedStatement st=con.prepareStatement("Update StudentPersonal set AttendingYear=1 where StudentId=?");
              int i = 0;
                       while (i < Students.length)
                       {
                           st.setInt(1, Integer.parseInt(Students[i]));
                            st.executeUpdate();
                            i++;
                       }

                       return true;
                   }
             }
              return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
    public String  getCourseIdForStudent(int StudentId) throws SQLException
  {
        Connection con=null;
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
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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
    public static String convertDatetoyyyymmdd(String date) {
        String dd = date.substring(0, 2);
        String mm = date.substring(3, 5);
        String yy = date.substring(6, 10);
        String ymd = yy + "-" + mm + "-" + dd;
        return ymd;
    }


    public static String convertDatetoddmmyyyy(String date) {
        String dd = date.substring(8, 10);
        String mm = date.substring(5, 7);
        String yy = date.substring(0, 4);
        String ymd = dd + "-" + mm + "-" + yy;
        return ymd;
    }

      public boolean deleteDD(String ddId) throws SQLException, IOException {
               Connection con = null;
          try {
   
        con = new DBConnection().getConnection();
         DDId=Integer.parseInt(ddId);
        PreparedStatement st = con.prepareStatement("delete from StudentFeeMap where DDId=?");
        st.setInt(1,DDId);
        st.executeUpdate();
        st = con.prepareStatement("Delete from DDMaster where DDId=?");
        st.setInt(1,DDId);
        st.executeUpdate();
        
        return true;
          } catch (SQLException ex) {

            return false;
        }
           finally{
               con.close();
           }
      }
public boolean deleteDDForReAdmission(String ddId) throws SQLException, IOException
 {  Connection con = null;
           try {
      
        con = new DBConnection().getConnection();
        DDId=Integer.parseInt(ddId);
        PreparedStatement st = con.prepareStatement("delete from AdmissionFees where DDId=?");
        st.setInt(1,DDId);
        st.executeUpdate();
        st = con.prepareStatement("Delete from DDMaster where DDId=?");
        st.setInt(1,DDId);
        st.executeUpdate();
        con.close();
        return true;
          } catch (SQLException ex) {

            return false;
        }
    finally{
        con.close();
    }
      }
 public  boolean isAvailableDDNo(String ddNumber,String DDId) throws SQLException
    {
     Connection con=null;
     if(DDId!=null)
         return true;
     try {
             con = new DBConnection().getConnection();
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
 
public float getTutionFee(int BranchId,int Year,int CentreId) throws SQLException
    {
    Connection con=null;
        try {
            con = new DBConnection().getConnection();
            float IndFee,OutSideFee,TutionFeeId;
            PreparedStatement Query;
            Query = con.prepareStatement("SELECT TutionFeeId,TutionFees_IND,TutionFees_USD FROM TutionFeeMaster where BranchId=? and TutionFeeYear=?");
            Query.setInt(1, BranchId);
            Query.setInt(2, Year);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                TutionFeeId=Data.getInt(1);
                IndFee=Data.getInt(2);
                OutSideFee=Data.getFloat(3);

            }
             else
            {
                return -1;
            }
            Query =con.prepareStatement("SELECT CollegeCountry,DistrictId FROM CollegeMaster where CollegeId=?");
            Query.setInt(1, CentreId);
            Data=Query.executeQuery();
              if(Data.next())
              {
                    if(Data.getString(1).equals("India"))
                    {
                        return IndFee;
                    }
                    else
                    {
                         return TutionFeeId;
                    }
              }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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

int getTutionFeeId(int CentreId,int BranchId,int Year) throws SQLException
    {
    Connection con=null;
        try {
            con = new DBConnection().getConnection();
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
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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

public int getStudentId(int ApplicationNo ,int CentreId) throws SQLException
    {
    Connection con=null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select l.StudentId from StudentLoginMaster l,StudentPersonal p  where  QuestionId= ? and CollegeId=? and l.StudentId=p.StudentId ");
            Query.setInt(1, ApplicationNo);
            Query.setInt(2,Integer.parseInt(CenterId));
            ResultSet Records = Query.executeQuery();
            if (Records.next()) {
                return Records.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
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

 public Boolean isCentreVerificationOver(int StudentId) throws SQLException
 {
            int JoiningYear=new AdmissionYear().getCurrentAdmissionYear();
            Connection con = null;
            con = new DBConnection().getConnection();
        try
        {
            PreparedStatement Query=con.prepareStatement("select JoiningYear from StudentPersonal where StudentId=? ");
            Query.setInt(1, StudentId);
            ResultSet Record=Query.executeQuery();
            Record.next();
            JoiningYear=Record.getInt(1);
             Calendar now = Calendar.getInstance();
             //int currentYear = now.get(Calendar.YEAR);
            Query = con.prepareStatement("select Center_Ver_EndDate from AdmissionDates where AdmissionYear=? ");
            Query.setInt(1,JoiningYear);
            ResultSet rs = Query.executeQuery();
            rs.next();
            Date Course_Sel_EndDate = rs.getDate("Center_Ver_EndDate");
            con.close();
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(Course_Sel_EndDate);
            endDate.add(Calendar.DATE, 1);

            Calendar currentDate = Calendar.getInstance();

            if (endDate.before(currentDate))
            {
                
                return true;
            } 
            else
            {
                return false;
            }
        } 
        catch (SQLException ex)
        {
            if(con!=null)
                con.close();

            System.out.print(ex);
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

 public boolean IsCentreVerificationOverForCentre(String CollegeId,String BranchId) throws SQLException
 {
     Connection con = null;
        try
        {
            con=new DBConnection().getConnection();
            int JoiningYear = new AdmissionYear().getCurrentAdmissionYear();
            PreparedStatement st=con.prepareStatement("select TIME_TO_SEC(timediff(now(),CentreVerificationEndDate)) from DateExtensionForCentres where AdmissionYear=? and CollegeId=? and (BranchId=? or BranchId=-1)");
            st.setInt(1, JoiningYear);
            st.setString(2,CollegeId);
            st.setString(3, BranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt(1)<0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
         finally
        {
            if(con!=null)
            {
                con.close();
            }
        }

 }
public Boolean isReAdmissionOver() throws SQLException
{
            AdmissionYear CurYear=new AdmissionYear();
            Connection con = null;
            con = new DBConnection().getConnection();
        try {
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            PreparedStatement Query = con.prepareStatement("select ReAdmissionEndDate from AdmissionDates where AdmissionYear=? ");
            Query.setInt(1,CurYear.getCurrentAdmissionYear());
            ResultSet rs = Query.executeQuery();
            rs.next();
            Date Course_Sel_EndDate = rs.getDate("ReAdmissionEndDate");
            con.close();
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(Course_Sel_EndDate);
            endDate.add(Calendar.DATE, 1);

            Calendar currentDate = Calendar.getInstance();

            if (endDate.before(currentDate)) {

                return true;
            } else {

                return false;
            }

        } catch (SQLException ex) {
            if(con!=null)
                con.close();

            System.out.print(ex);
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

public void getDDStudentsDetails(String CentreId, String BranchId) throws SQLException, IOException
 {
       Connection con=null;
       try
       {
     StudentApplicationList List=new StudentApplicationList(Integer.parseInt(CentreId), Integer.parseInt(BranchId));
     DDStudentsList=new ArrayList<SelectedStudent>();
       SelectedStudent SS;
          int i=0;
        while (i<List.getCount())
        {
            SS=new SelectedStudent();
            SS.StudentId=Integer.parseInt(List.getStudentId(i));
            SS.ApplicationId=Integer.parseInt(List.getApplication(i));
            SS.StudentName=List.getStudentName(i);
            SS.isSelected=false;
            DDStudentsList.add(SS);
            i++;
        }
       
         
              if(request.getParameter("DDTYPE").equals("Bulk")&&request.getParameterValues("StudentId")!=null)
              {
           String studs[]=request.getParameterValues("StudentId");
           i=0;int j=0;
           while(i<studs.length)
           {
                 j=0;
                 while(j<DDStudentsList.size())
                 {
                     if(DDStudentsList.get(j).StudentId==Integer.parseInt(studs[i]))
                     {
                         DDStudentsList.get(j).isSelected=true;
                     }
                     j++;
                 }
                 i++;
           }
        }
       }
       catch (SQLException ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
        
    }

public void getDDStudentsDetailsPRN(String CentreId, String BranchId,int Year) throws SQLException, IOException
 {
       Connection con=null;
       try
       {
     StudentListPRN List=new StudentListPRN(Integer.parseInt(CentreId), Integer.parseInt(BranchId),Year);
     DDStudentsList=new ArrayList<SelectedStudent>();
       SelectedStudent SS;
          int i=0;
        while (i<List.getCount())
        {
            SS=new SelectedStudent();
            SS.StudentId=Integer.parseInt(List.getStudentId(i));
            SS.ApplicationId=Integer.parseInt(List.getApplication(i));
            SS.StudentName=List.getStudentName(i);
            SS.PRN=List.getPRN(i);
            SS.isSelected=false;
            DDStudentsList.add(SS);
            i++;
        }


              if(request.getParameter("DDTYPE").equals("Bulk")&&request.getParameterValues("StudentId")!=null)
              {
           String studs[]=request.getParameterValues("StudentId");
           i=0;int j=0;
           while(i<studs.length)
           {
                 j=0;
                 while(j<DDStudentsList.size())
                 {
                     if(DDStudentsList.get(j).StudentId==Integer.parseInt(studs[i]))
                     {
                         DDStudentsList.get(j).isSelected=true;
                     }
                     j++;
                 }
                 i++;
           }
        }
       }
       catch (Exception ex) {
            Logger.getLogger(ExamRegistrationFee.class.getName()).log(Level.SEVERE, null, ex);

        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }

    }

public ArrayList<SelectedStudent> getStudentList()
{
    return  DDStudentsList;
}
public static void main(String a[]) throws SQLException, IOException
{
   
}
}
