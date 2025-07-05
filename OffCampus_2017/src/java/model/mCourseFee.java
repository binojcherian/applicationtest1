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
import java.util.Hashtable;


public class mCourseFee {
Connection con=null;
private  ArrayList<String> DocId =new ArrayList<String>();
     private  ArrayList<String> DocName=new ArrayList<String>();
     private ArrayList<String> DocPath=new ArrayList<String>();
     private Hashtable<String ,DocData> Table=new Hashtable<String, DocData>();
     private String CourseFeeDetails=null;
     private boolean iSTutionFeeEntered,iSTutionFeePaid;
    private String Branch,Bank,Amount,AmountType,DDNo,DDDate;
    public mCourseFee(int StudentId) throws SQLException {
        con=new DBConnection().getConnection();
        PreparedStatement Query ;
              String InIndia,Overseas;
              Query=con.prepareStatement("SELECT TutionFeeId,TutionFees_IND,TutionFees_USD FROM TutionFeeMaster where TutionFeeYear=1 and  BranchId=(select BranchId from StudentPersonal where StudentId=?)");
              Query.setInt(1,StudentId);
            ResultSet  Records=Query.executeQuery();
              int TutionFeeId=0;
               if(Records.next())
               {
                   iSTutionFeeEntered=true;
                   TutionFeeId=Records.getInt(1);
                   CourseFeeDetails="Course  In India="+Records.getString(2)+ "Rs     <br>Overseas Fee $"+Records.getString(3);
                  
               }
             System.out.print("Tution Fee "+TutionFeeId);
              if(iSTutionFeeEntered)
              {
              Query=con.prepareStatement("SELECT Branch,Bank,Amount,AmountType,DDNo,DDDate FROM DDMaster where DDId= (select DDId from StudentFeeMap where TutionFeeId= ? and StudentId=?)");
              Query.setInt(1,TutionFeeId);
              Query.setInt(2, StudentId);
              Records=Query.executeQuery();
                  if(Records.next())
                  {
                   iSTutionFeePaid=true;
                   Branch=Records.getString(1);
                   Bank=Records.getString(2);
                   Amount=Records.getString(3);
                   AmountType=Records.getString(4);
                   DDNo=Records.getString(5);
                   DDDate=Records.getString(6);
               
                  }
              con.close();
              }

    }

public void close() throws SQLException
{
     if(con!=null&&(!con.isClosed()))
         con.close();
}
   public boolean  IsFeePaid()
{
     return iSTutionFeePaid;
}
public String getBranch()
{
     return Branch;
}
public String getBank()
{
     return Bank;
}
public String getDDNo()
{
     return DDNo;
}
public String getDDAmount()
{
    return Amount;
}
public String getAmountType()
{
     return AmountType;
}
        public String getDDDate()
        {
             return DDDate;
        }
        public static void main(String s[]) throws SQLException
        {
             mCourseFee CF=new mCourseFee(627);
        }
}
