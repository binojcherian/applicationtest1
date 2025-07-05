/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import Entity.DD;
import Entity.Student;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HP
 */
public class mDD {

     public int getTotalDDAmountForCurrentExam(int Branch,int AttendingYear,int College,Connection con) throws SQLException
    {
         PreparedStatement Query=con.prepareStatement("SELECT  Sum(Amount) FROM DDMaster d INNER JOIN CourseDDMap a on a.DDId=d.DDId   where a.ExamId=(select max(ExamId) from CourseDDMap) "
                 + "AND CourseId=? AND CentreId =? AND Semoryear=?");
         Query.setInt(1, Branch);
         Query.setInt(2,College );
         Query.setInt(3, AttendingYear);
         ResultSet rs=Query.executeQuery();
         if(rs.next())
             return rs.getInt(1);
         else
             return 0;
    }

     public DD getDDWithOutFine(int Branch,int AttendingYear,int College,Connection con)
     {
         DD dd=new DD();
        try
        {
            PreparedStatement st = con.prepareStatement("select sum(Amount),count(DDNo) from DDMaster d inner join CourseDDMap c on c.DDId=d.DDId and c.CourseId=? and c.Semoryear=? where d.CentreId=? and d.DDDate < '2012-04-03'");
            st.setInt(1, Branch);
            st.setInt(2, AttendingYear);
            st.setInt(3,College);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                dd.Count=rs.getInt(2);
                dd.setAmount(rs.getFloat(1));
            }
                    return dd;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mDD.class.getName()).log(Level.SEVERE, null, ex);
            return dd;
        }

         
     }
     public DD getDDWithFine(int Branch,int AttendingYear,int College,Connection con)
     {
         DD dd=new DD();
        try
        {
            PreparedStatement st = con.prepareStatement("select sum(Amount),count(DDNo) from DDMaster d inner join CourseDDMap c on c.DDId=d.DDId and c.CourseId=? and c.Semoryear=? where d.CentreId=? and d.DDDate > '2012-04-03' and d.DDDate < '2012-04-07'");
            st.setInt(1, Branch);
            st.setInt(2, AttendingYear);
            st.setInt(3,College);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                dd.Count=rs.getInt(2);
                dd.setAmount(rs.getFloat(1));
            }
                    return dd;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mDD.class.getName()).log(Level.SEVERE, null, ex);
            return dd;
        }


     }
    public ArrayList<DD> getDDsForCurrentExam(int Branch,int AttendingYear,int College,Connection con) throws SQLException
    {
         PreparedStatement Query=con.prepareStatement("SELECT   DDNo,Bank,Branch,DDDate,Amount,AmountType,d.DDId,IsConfirmed,IsMguApproved FROM DDMaster d INNER JOIN CourseDDMap a on a.DDId=d.DDId "
                 + "where a.ExamId=(select max(ExamId) from CourseDDMap) AND CourseId=? AND CentreId =? AND Semoryear=?");
         Query.setInt(1, Branch);
         Query.setInt(2,College );
         Query.setInt(3, AttendingYear);
         ResultSet rs=Query.executeQuery();
         ArrayList<DD> dds=new ArrayList<DD>();
         while (rs.next()) {
          DD dd= new DD();
          dd.setDDNo(rs.getString(1));
          dd.setBank(rs.getString(2));
          dd.setBranch(rs.getString(3));
          dd.setDDDate(rs.getString(4));
          dd.setAmount(rs.getFloat(5));
          dd.setAmountType(rs.getString(6));
          dd.setDDId(rs.getInt(7));
          if(rs.getInt(8)==0)
          {
              dd.Status="Centre Not Confirmed";
              dd.setConfirmed(false);
          }else if(rs.getInt(8)==1)
          {
              dd.setConfirmed(true);
              dd.Status="Centre Confirmed";
          }
          if(rs.getInt(9)==1)
          {
              dd.Status="MguApproved";
              dd.setMguApproved(true);
          }
          else if(rs.getInt(9)==0)
          {
              dd.setMguApproved(false);
          }
         dds.add(dd);
        }
      return dds;
    }

        public ArrayList<DD> getConfirmedDDsForCurrentExam(int Branch,int AttendingYear,int College,Connection con) throws SQLException
    {
         PreparedStatement Query=con.prepareStatement("SELECT   DDNo,Bank,Branch,DDDate,Amount,AmountType,d.DDId,IsConfirmed FROM DDMaster d INNER JOIN CourseDDMap a on a.DDId=d.DDId "
                 + "where a.ExamId=(select max(ExamId) from CourseDDMap) AND CourseId=? AND CentreId =? AND Semoryear=? AND IsConfirmed=1 ");
         Query.setInt(1, Branch);
         Query.setInt(2,College );
         Query.setInt(3, AttendingYear);
         ResultSet rs=Query.executeQuery();
         ArrayList<DD> dds=new ArrayList<DD>();
         while (rs.next()) {
          DD dd= new DD();
          dd.setDDNo(rs.getString(1));
          dd.setBank(rs.getString(2));
          dd.setBranch(rs.getString(3));
          dd.setDDDate(rs.getString(4));
          dd.setAmount(rs.getFloat(5));
          dd.setAmountType(rs.getString(6));
          dd.setDDId(rs.getInt(7));
          if(rs.getInt(8)==0)
          {
              dd.setConfirmed(false);
          }else if(rs.getInt(8)==1)
              dd.setConfirmed(true);
         dds.add(dd);
        }
      return dds;
    }

        public int getConfirmedDDsCount(int Branch,int AttendingYear,int College,Connection con)
        {
        try {
            PreparedStatement Query = con.prepareStatement("SELECT   count(d.DDId) FROM DDMaster d INNER JOIN CourseDDMap a on a.DDId=d.DDId where a.ExamId=(select max(ExamId) from CourseDDMap) AND CourseId=? AND CentreId =? AND Semoryear=? AND IsConfirmed=1 ");
            Query.setInt(1, Branch);
            Query.setInt(2, College);
            Query.setInt(3, AttendingYear);
            ResultSet rs = Query.executeQuery();
            while(rs.next())
            {
                return(rs.getInt(1));
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mDD.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        }
         public int getMguApprovedDDsCount(int Branch,int AttendingYear,int College,Connection con)
        {
        try {
            PreparedStatement Query = con.prepareStatement("SELECT   count(d.DDId) FROM DDMaster d INNER JOIN CourseDDMap a on a.DDId=d.DDId where a.ExamId=(select max(ExamId) from CourseDDMap) AND CourseId=? AND CentreId =? AND Semoryear=? AND IsMguApproved=1 ");
            Query.setInt(1, Branch);
            Query.setInt(2, College);
            Query.setInt(3, AttendingYear);
            ResultSet rs = Query.executeQuery();
            while(rs.next())
            {
                return(rs.getInt(1));
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mDD.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        }
public boolean deleteDD(int DDId,HttpServletRequest request) throws SQLException, IOException
      {
          Connection Con = null;
          ExamRegistration reg=new ExamRegistration();
           try
           {
        Con = new DBConnection().getConnection();
        PreparedStatement st = Con.prepareStatement("Delete from DDMaster where DDId=?");
        st.setInt(1,DDId);
        st.executeUpdate();
        st=Con.prepareStatement("Delete FROM CourseDDMap WHERE DDId=? ");
        st.setInt(1,DDId);
        st.executeUpdate();
        reg.SaveExamLog("DDDeleted"+request.getParameter("DDNo"), request);
        Con.close();
        return true;
          }
           catch (SQLException ex)
           {
               return false;
           }
           finally
           {
               if(Con!=null)
               {
                   Con.close();
               }
           }

      }

    public DD getExamDDetails(int DDId,Connection con) throws SQLException
{
            PreparedStatement Query = con.prepareStatement("SELECT DDId ,Branch,Bank,Amount,AmountType,DDNo,DDDate,C.CollegeId,CollegeName FROM DDMaster D ,CollegeMaster C where  D.CentreId=C.CollegeId and D.DDId=?");
            Query.setInt(1, DDId);
            ResultSet Records = Query.executeQuery();
            DD dd = null;
            while (Records.next()) {
               dd=new DD();
                dd.setDDId(Records.getInt(1));
                dd.setBranch(Records.getString(2));
                dd.setBank(Records.getString(3));
                dd.setAmount ( Records.getFloat(4));
                dd.setAmountType (Records.getString(5));
                dd.setDDNo (Records.getString(6));
                dd.setDDDate (Records.getString(7));
                dd.setCentreId (Records.getInt(8));
                dd.setCentreName (Records.getString(9));
    }
            return dd;
    }
      
public DD getDDDetails(int DDId,Connection con) throws SQLException
{
      try {
            PreparedStatement Query = con.prepareStatement("SELECT DDId ,Branch,Bank,Amount,AmountType,DDNo,DDDate,C.CollegeId,CollegeName FROM DDMaster D ,CollegeMaster C where  D.CentreId=C.CollegeId and D.DDId=?");
            Query.setInt(1, DDId);
            ResultSet Records = Query.executeQuery();
            DD dd = null;
            while (Records.next()) {
               dd=new DD();
                dd.setDDId(Records.getInt(1));
                dd.setBranch(Records.getString(2));
                dd.setBank(Records.getString(3));
                dd.setAmount ( Records.getFloat(4));
                dd.setAmountType (Records.getString(5));
                dd.setDDNo (Records.getString(6));
                dd.setDDDate (Records.getString(7));
                dd.setCentreId (Records.getInt(8));
                dd.setCentreName (Records.getString(9));
            }

            Query=con.prepareStatement("SELECT SF.StudentId ,StudentName,QuestionId,C.CollegeId,CollegeName,BranchId,PRN FROM StudentFeeMap SF,StudentPersonal SP,CollegeMaster C,StudentLoginMaster SL where  DDId= ? and SP.StudentId=SF.StudentId and SL.StudentId=SF.StudentId and C.CollegeId=SP.CollegeId");
            Query.setInt(1, DDId);
            Records=Query.executeQuery();
            Student Student;
            while (Records.next()) {
              Student=new Student();
              Student.setStudentId(Records.getInt(1));
              Student.setName(Records.getString(2));
               Student.setCentreId(Records.getInt(4));
              Student.setBranch(Records.getInt(6));
              Student.setPRN(Records.getString(7));
            dd.addStudent(Student);
          }
            return dd;
        } catch (SQLException ex) {
            Logger.getLogger(mDD.class.getName()).log(Level.SEVERE, null, ex);
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
public static  void main(String a[]) throws SQLException
    {
        new mDD().getDDsForCurrentExam(21, 1, 76, new DBConnection().getConnection());
    }
}
