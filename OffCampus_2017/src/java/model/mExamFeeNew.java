/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import Entity.Amount;
import Entity.SubjectBranch;
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
public class mExamFeeNew {

      public Amount getTotalExamFeeForStudent(int StudentId,int College,int Branch, int AttendingYear, Connection connection) throws SQLException
    {
        Amount amount=new Amount();
       try {
            String Country = "India";
            PreparedStatement Query = connection.prepareStatement("SELECT CollegeCountry FROM CollegeMaster where CollegeId=?");
            Query.setInt(1, College);
            ResultSet Data = Query.executeQuery();
            if (Data.next()) {
                Country = Data.getString(1);
            }
            Data.close();

              Query=connection.prepareStatement("SELECT e.`TotalFeeInRs`, e.`TotalFeeInUSD` FROM ExamFeeMaster_New e WHERE e.`BranchId`=? AND e.`AcademicYear`=?");
              Query.setInt(1, Branch);
              Query.setInt(2, AttendingYear);
              ResultSet rs=Query.executeQuery();
              while (rs.next()) {
                   if (Country.equals("India")) {
                       amount.setAmt(rs.getFloat(1));
                       amount.setAmountType("INR");
                   }
                   else
                   {
                       amount.setAmountType("USD");
                       amount.setAmt(rs.getFloat(2));
                   }
               }
       
            if(AttendingYear==2)
            {
             mStudentList studentList=new mStudentList();
             ArrayList<SubjectBranch> subjects=studentList.getRegisteredSupplyOrImprovementPapers(StudentId, Branch, connection,AttendingYear);
             int SupplyPapersCount=subjects.size();
             int SemCount=SupplyPapersCount;
              if(SupplyPapersCount>1)
              {
                  //This is Simple trick for Counting no of different Sems when Sem Count is <=2 Should be modified next time
                  if(subjects.get(0).getSemOrYear()!=subjects.get(SupplyPapersCount-1).getSemOrYear())
                      SemCount=2;
                  else
                      SemCount =1;
              }
             PreparedStatement ps=connection.prepareStatement("SELECT e.`ExamFeePerPaperInRs`,e.`CVFeePerSemInRs`,e.`MarkListPerSemInRs` ,"
             + " e.`ExamFeePerPaperInUSD`,e.`CVFeePerSemInUSD`, e.`MarkListPerSemInUSD` FROM ExamFeeMaster_New e WHERE e.`AcademicYear`=? AND e.`BranchId`=?");
             ps.setInt(1, AttendingYear-1);
             ps.setInt(2, Branch);
             ResultSet FeeDetails=ps.executeQuery();
             float SupplyTotalPaperFee=0,TotalCvFee=0,TotalMarkListFee=0;
             if(FeeDetails.next())
             {
                 if(Country.equals("India"))
                 {
                     SupplyTotalPaperFee=SupplyPapersCount*FeeDetails.getFloat(1);
                     TotalCvFee=SemCount*FeeDetails.getFloat(2);
                     TotalMarkListFee=SemCount*FeeDetails.getFloat(3);
                 }
                 else
                 {
                     SupplyTotalPaperFee=SupplyPapersCount*FeeDetails.getFloat(4);
                     TotalCvFee=SemCount*FeeDetails.getFloat(5);
                     TotalMarkListFee=SemCount*FeeDetails.getFloat(6);
                 }
                 amount.setAmt(amount.getAmt()+SupplyTotalPaperFee+TotalCvFee+TotalMarkListFee);
             }
            }
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(mExamFeeNew.class.getName()).log(Level.SEVERE, null, ex);
            amount.setAmt(-1);
            return amount;
        }
       

    }

        public Amount getTotalExamFeeForCentre(int College,int Branch, int AttendingYear, Connection connection) throws SQLException
    {
        Amount amount=new Amount();
       try {

            PreparedStatement Query=connection.prepareStatement("SELECT sum(Amount),FeeType FROM `DEMS_db`.`StudentExamFeeStatus` where BranchId=? and CollegeId=? and AttendingYear=?");
              Query.setInt(1, Branch);
              Query.setInt(2, College);
              Query.setInt(3, AttendingYear);
              ResultSet rs=Query.executeQuery();
              while (rs.next()) {

                       amount.setAmt(rs.getFloat(1));
                       amount.setAmountType(rs.getString(2));
               }
              return amount;

        } catch (SQLException ex) {
            Logger.getLogger(mExamFeeNew.class.getName()).log(Level.SEVERE, null, ex);
            amount.setAmt(-1);
            return amount;
        }


    }


   public String getExamFeePendingorNot(int Branch,int AttendingYear,int College,Connection con) throws SQLException
    {

     Amount amt=new Amount();
    amt=getTotalExamFeeForCentre(College,Branch,AttendingYear, con);
    float studentcoursefeetotal=amt.getAmt();
    mDD dd=new mDD();
    float DDFeetotal=dd.getTotalDDAmountForCurrentExam(Branch, AttendingYear, College, con);
       if((DDFeetotal-studentcoursefeetotal)< 0)
       {
           return "Fee pending";
       }
       else
       {
        return null;
       }

    }
   
          //public getSupplies


public static void main(String a[]) throws SQLException
        {
    new mExamFeeNew().getTotalExamFeeForStudent(11565, 76, 21, 2, new DBConnection().getConnection());
}
}
