

package model;


import Entity.Amount;
import Entity.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class mExamFee {
Connection con;
String LastDate;
String LastDateWithFine;
float FineInRs;
float FineInUSD;
    public mExamFee()  {
       
            con = new DBConnection().getConnection();

      
    }
    public void  getFineAndLastDate(int AcademicYear) throws SQLException
    {
         PreparedStatement Query= con.prepareStatement("SELECT Distinct LastDate,LastDateWithFine,FineInRs,FineInUSD FROM FeeDateMaster where AcademicYear=? ");
         Query.setInt(1, AcademicYear);
         ResultSet Data=Query.executeQuery();
         if(Data.next())
         {
             LastDate=Data.getString(1);
             LastDateWithFine=Data.getString(2);
             FineInRs=Data.getFloat(3);
             FineInUSD=Data.getFloat(4);
         }
    }
     public Boolean isLastDateOver() throws SQLException {

            Connection con = null;
            con = new DBConnection().getConnection();
        try
        {
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            PreparedStatement Query = con.prepareStatement("SELECT Distinct LastDate FROM FeeDateMaster where AcademicYear=1");
           // Query.setInt(1,currentYear);
            ResultSet rs = Query.executeQuery();
            rs.next();
            Date Course_Sel_EndDate = rs.getDate(1);
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
        } catch (SQLException ex) {
            if(con!=null)
                con.close();
             Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    public  Amount getExamFee(String Branch,String Centre)
    {
        Amount amount=new Amount();
        try {
            String Country = "India";
            PreparedStatement Query = con.prepareStatement("SELECT CollegeCountry FROM CollegeMaster where CollegeId=?");
            Query.setInt(1, Integer.parseInt(Centre));
            ResultSet Data = Query.executeQuery();
          if (Data.next()) {
                Country = Data.getString(1);
            }
            Data.close();
            if (Country.equals("India")) {
                Query = con.prepareStatement("SELECT TotalFeeInRs FROM ExamFeeMaster where BranchId=? and AcademicYear=1");
                amount.setAmountType("INR");
            } else {
                Query = con.prepareStatement("SELECT TotalFeeInUSD FROM ExamFeeMaster where BranchId=? and AcademicYear=1");
            amount.setAmountType("USD");
            }
            Query.setInt(1, Integer.parseInt(Branch));
            Data = Query.executeQuery();
            if (Data.next()) {
                amount.setAmt(Data.getFloat(1));
                return amount ;
            }
            amount.setAmt(-2);
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
            amount.setAmt(-1);
            return amount ;
        }
    }
    public Amount getExamFeeForStudent(String PRN,String Centre)
    {
        Amount amount=new Amount();
       try {
            String Country = "India";
            PreparedStatement Query = con.prepareStatement("SELECT CollegeCountry FROM CollegeMaster where CollegeId=?");
            Query.setInt(1, Integer.parseInt(Centre));
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                Country = Data.getString(1);

            }
            Data.close();
            if (Country.equals("India")) {             
                Query = con.prepareStatement("SELECT TotalFeeInRs FROM ExamFeeMaster where BranchId=(select BranchId from StudentPersonal where PRN=? ) and AcademicYear=1");
                 amount.setAmountType("INR");
            } else {               
                Query = con.prepareStatement("SELECT TotalFeeInUSD FROM ExamFeeMaster where BranchId=(select BranchId from StudentPersonal where PRN=? ) and AcademicYear=1");
            amount.setAmountType("USD");
            }
             Query.setString(1, PRN);
            Data = Query.executeQuery();
            if (Data.next()) {
                amount.setAmt(Data.getFloat(1));
                return amount ;
            }
            amount.setAmt(-2);
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
            amount.setAmt(-1);
            return amount;
        }

    }
  public String getLastDate()
    {
      return LastDate;
  }
  public String  isValidPRN(String PRN,int Centre) throws SQLException
    {
        try {
            con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT StudentId FROM StudentPersonal where PRN=? and CollegeId=? ");
            Query.setString(1, PRN);
            Query.setInt(2, Centre);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                return Data.getString(1);
            } else {
                return null;
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
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
  
  public String EnterExamFee(int DDNo,String Bank,String Branch,String DDDate,float Amount,String AmountType ,int CentreId,int BranchId,ArrayList<String> SeletedStudents) throws SQLException
    {
        try {

            con.setAutoCommit(false);
            PreparedStatement Query = con.prepareStatement("insert into DDMaster(Branch,Bank,Amount,AmountType,DDNo,DDDate,EnteredDate,CentreId) values(?,?,?,?,?,?,sysdate(),?)");
            Query.setString(1, Branch);
            Query.setString(2,Bank );
            Query.setFloat(3,Amount);
            Query.setString(4,AmountType);
            Query.setInt(5, DDNo);
            Query.setString(6, Registration.convertDatetoyyyymmdd(DDDate));
            Query.setInt(7,CentreId);
            Query.executeUpdate();
            con.commit();
            Query=con.prepareStatement("SELECT DDId FROM DDMaster where Bank=? and Branch =? and DDNo=? and CentreId=?");
            Query.setString(1,Bank);
            Query.setString(2,Branch);
            Query.setInt(3, DDNo);
            Query.setInt(4,CentreId);
            ResultSet Data=Query.executeQuery();
            int DDId=0;
            while (Data.next()) {
                DDId=Data.getInt(1);
            }
            int ExamFeeId=0;
            Query=con.prepareStatement("SELECT ExamFeeId FROM ExamFeeMaster where BranchId=? and AcademicYear=1 and To_Date is null");
            Query.setInt(1,BranchId);
            Data=Query.executeQuery();
            while (Data.next()) {
                ExamFeeId=Data.getInt(1);
            }
            if(ExamFeeId==0)
            {
                con.rollback();
                return "Exam Fee for this Branch is not Entered Contact MG University";
            }
            Query=con.prepareStatement("insert into StudentFeeMap (StudentId,TutionFeeId,ExamFeeId,PaymentDate,DDId) values(?,-1,?,sysdate(),?)");
            for (String Student : SeletedStudents) {
               Query.setInt(1,Integer.parseInt(Student));
               Query.setInt(2,ExamFeeId);
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
        }
return null;
  }
public String isExamFeePaid(ArrayList<String> StudentList,int DDId)
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
        String Query="select PRN from StudentPersonal where StudentId in (SELECT  StudentId FROM StudentFeeMap where ExamFeeId>0 and StudentId in ("+SIDs+"))";
    if(DDId>1)
    {
    Query=Query+" and DDId!="+DDId;
    }
 PreparedStatement st = con.prepareStatement(Query);
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

                Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
                return "Could Not Connect to Server";
            }

}
public  boolean isAvailableDDNo(String DDNo)
    {

     try {

            PreparedStatement st = con.prepareStatement("select count(*) as count from DDMaster where DDNo= ?");
            st.setString(1, DDNo);
            ResultSet rs = st.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
               return  true;
            }
            else
            {
              return  false;
            }

        } catch (SQLException ex) {

            return true;
        }
    }
    public String CheckStudentFee(String StdId)
{
    try {
            PreparedStatement st = con.prepareStatement("SELECT count(*) FROM StudentFeeMap where ExamFeeId >0 and StudentId=?");
            try{
            st.setInt(1, Integer.parseInt(StdId));
            }catch(NumberFormatException NFE)
            {
                return "Invalid Student";
            }
            ResultSet Rs = st.executeQuery();
            int count = 0;
            if (Rs.next()) {
                count = Rs.getInt(1);
                if (count >0) {
                    return "Fee Already paid for this Student";
                }
                else
                    return null;
            }
            return "Invalid Student ";
        } catch (SQLException ex) {
            Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
            return "Could Not Connect to Server";
        }
}
public Student getStudent(String PRN)
    {
    try {
            PreparedStatement st = con.prepareStatement(" select StudentId , PRN , StudentName , BranchId from StudentPersonal where PRN =?");
            st.setString(1,PRN);
            ResultSet Rs = st.executeQuery();
            int count = 0;
            Student student = null;
            while (Rs.next()) {
          student=new Student();
          student.setStudentId(Rs.getInt(1));
          student.setPRN(Rs.getString(2));
          student.setName(Rs.getString(3));
          student.setBranch(Rs.getInt(4));
            }
            return student;
        } catch (SQLException ex) {
            Logger.getLogger(mExamFee.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
}
public String getLastDateWithFine()
    {
      return LastDateWithFine;
  }
  public float getFineInRs()
    {
      return FineInRs;
  }
  public float  getFineInUSD()
    {
      return  FineInUSD;
  }
}
