/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mgu
 */
public class CenterVerification {

    public boolean VerifyApplication(HttpServletRequest request, int status) throws SQLException {
        Connection con = null;
        try {
            Integer i = (Integer) request.getSession().getAttribute("StudentId");
            int sid = i.intValue();
            int Year;
            Calendar cal=Calendar.getInstance();
            Year=cal.get(Calendar.YEAR);
            String remarks = request.getParameter("Remarks");
            String migration=request.getParameter("Migration");
            con = new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement QueryPersonal;

            QueryPersonal = con.prepareStatement("update StudentPersonal set IsMGUApproved=?,Remarks=?  where StudentId=?");
            QueryPersonal.setInt(1, status);
            QueryPersonal.setString(2, remarks);
            QueryPersonal.setInt(3, sid);
            QueryPersonal.executeUpdate();
 
           /* String PRN=generatePRN(sid,con,Year);
              if(PRN!=null)
                 {
                  QueryPersonal=con.prepareStatement("update StudentPersonal set PRN=? where StudentId=?");
                  QueryPersonal.setString(1, PRN);
                  QueryPersonal.setInt(2, sid);
                  QueryPersonal.executeUpdate();
                  }
       */
        if(migration!=null)
            {
                  PreparedStatement   QueryMigration;
            QueryMigration=con.prepareStatement("update StudentDocumentMaster set DocumentName=? where StudentId=? and DocumentId=5");
            QueryMigration.setString(1, migration);
            QueryMigration.setInt(2, sid);
            QueryMigration.executeUpdate();
            }
            con.commit();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
               // PreparedStatement QueryPersonal=con.prepareStatement("Unlock tables");
           // QueryPersonal.execute();

            con.rollback();
            con.setAutoCommit(true);
                con.close();
            }
            return false;
        }
    }
     public boolean VerifyApplicationCentreNotVerified(HttpServletRequest request, int status) throws SQLException {
        Connection con = null;
        try {
            Integer i = (Integer) request.getSession().getAttribute("StudentId");
            int sid = i.intValue();
            int Year;
            Calendar cal=Calendar.getInstance();
            Year=cal.get(Calendar.YEAR);
            String remarks = request.getParameter("Remarks");
            String migration=request.getParameter("Migration");
            con = new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement QueryPersonal;

            QueryPersonal = con.prepareStatement("update StudentPersonal set IsMGUApproved=?,Remarks=?,IsCenterVerified=?  where StudentId=?");
            QueryPersonal.setInt(1, status);
            QueryPersonal.setString(2, remarks);
            QueryPersonal.setInt(3, status);
            QueryPersonal.setInt(4, sid);
            QueryPersonal.executeUpdate();

           /* String PRN=generatePRN(sid,con,Year);
              if(PRN!=null)
                 {
                  QueryPersonal=con.prepareStatement("update StudentPersonal set PRN=? where StudentId=?");
                  QueryPersonal.setString(1, PRN);
                  QueryPersonal.setInt(2, sid);
                  QueryPersonal.executeUpdate();
                  }
       */
        if(migration!=null)
            {
                  PreparedStatement   QueryMigration;
            QueryMigration=con.prepareStatement("update StudentDocumentMaster set DocumentName=? where StudentId=? and DocumentId=5");
            QueryMigration.setString(1, migration);
            QueryMigration.setInt(2, sid);
            QueryMigration.executeUpdate();
            }
            con.commit();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
               // PreparedStatement QueryPersonal=con.prepareStatement("Unlock tables");
           // QueryPersonal.execute();

            con.rollback();
            con.setAutoCommit(true);
                con.close();
            }
            return false;
        }
    }
    public boolean VerifyReAdmission(HttpServletRequest request, int status) throws SQLException
    {
        Connection con = null;
        try {
            Integer i = (Integer) request.getSession().getAttribute("StudentId");
            int sid = i.intValue();
            String remarks = request.getParameter("Remarks");
            con = new DBConnection().getConnection();
            PreparedStatement QueryPersonal=con.prepareStatement("update ReAdmission set IsMguApproved=? where StudentId=?");
            QueryPersonal.setInt(1, status);
            QueryPersonal.setInt(2, sid);
            QueryPersonal.executeUpdate();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
                con.close();
            }
            return false;
        }
    }
      public boolean RejectApplication(HttpServletRequest request, int status) throws SQLException {
        Connection con = null;
        try {
            Integer i = (Integer) request.getSession().getAttribute("StudentId");
            int sid = i.intValue();
            int Year;
            Calendar cal=Calendar.getInstance();
            Year=cal.get(Calendar.YEAR);
            String remarks = request.getParameter("Remarks");
            String migration=request.getParameter("Migration");
            con = new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement QueryPersonal;

            QueryPersonal = con.prepareStatement("update StudentPersonal set IsMGUApproved=?,Remarks=?  where StudentId=?");
            QueryPersonal.setInt(1, status);
            QueryPersonal.setString(2, remarks);
            QueryPersonal.setInt(3, sid);
            QueryPersonal.executeUpdate();



        if(migration!=null)
            {
                  PreparedStatement   QueryMigration;
            QueryMigration=con.prepareStatement("update StudentDocumentMaster set DocumentName=? where StudentId=? and DocumentId=5");
            QueryMigration.setString(1, migration);
            QueryMigration.setInt(2, sid);
            QueryMigration.executeUpdate();
            }
            con.commit();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
               // PreparedStatement QueryPersonal=con.prepareStatement("Unlock tables");
           // QueryPersonal.execute();

            con.rollback();
            con.setAutoCommit(true);
                con.close();
            }
            return false;
        }
    }
public boolean IsStudentValid(HttpServletRequest request) throws SQLException
    {
    Connection con = null;
        try {
            
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select count(*) from StudentPersonal where StudentId=? and CollegeId In(select CollegeId from CentreUserMap where UserName=? and EndDate is null )");
            if(request.getParameter("StudentId")!=null)
            {
            Query.setInt(1, Integer.parseInt(request.getParameter("StudentId")));
            }
          else
            {
                Query.setInt(1, Integer.parseInt(request.getSession().getAttribute("StudentId").toString()));
            }
            Query.setString(2, request.getSession().getAttribute("User").toString());
            ResultSet Data = Query.executeQuery();
            if(Data.next())
            {
                 if(Data.getInt(1)==1)
                     return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }

    }
public String getApplicantsStatusForReAdmission(HttpServletRequest request) throws SQLException {
       Connection con = null;
        try {
            con = new DBConnection().getConnection();
            String ApplicantStatus = "Waiting";
            PreparedStatement query = con.prepareStatement("select IsCentreVerified from ReAdmission where StudentId=?");
            if(request.getParameter("StudentId")!=null)
            {
                query.setInt(1, Integer.parseInt(request.getParameter("StudentId")));
            }
          else
            {
                query.setInt(1, Integer.parseInt(request.getSession().getAttribute("StudentId").toString()));
            }
            ResultSet Data = query.executeQuery();
            Data.next();
            String status = Data.getString("IsCentreVerified");
            if (status != null) {
                if (status.equals("0")) {
                    ApplicantStatus = "Rejected";
                } else if (status.equals("1")) {
                    ApplicantStatus = "Verified";
                } else {
                    ApplicantStatus = "Waiting";
                }
            }    con.close();
            return ApplicantStatus;

        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
                con.close();
            }
            return "";
        }
    }
public boolean IsMGUApproved(HttpServletRequest request) throws SQLException
    {
    Connection con = null;
        try {
            
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT count(*) FROM StudentPersonal where IsMGUApproved=1 and StudentId=?");
            if(request.getParameter("StudentId")!=null)
            {
            Query.setInt(1, Integer.parseInt(request.getParameter("StudentId")));
            }
          else
            {
                Query.setInt(1, Integer.parseInt(request.getSession().getAttribute("StudentId").toString()));
            }
           
            ResultSet Data = Query.executeQuery();
            if(Data.next())
            {
                 if(Data.getInt(1)==1)
                     return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }

    }
public boolean IsMGUApprovedForReAdmission(HttpServletRequest request) throws SQLException
    {
    Connection con = null;
        try {

            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT count(*) FROM ReAdmission where IsMGUApproved=1 and StudentId=?");
            if(request.getParameter("StudentId")!=null)
            {
            Query.setInt(1, Integer.parseInt(request.getParameter("StudentId")));
            }
          else
            {
                Query.setInt(1, Integer.parseInt(request.getSession().getAttribute("StudentId").toString()));
            }

            ResultSet Data = Query.executeQuery();
            if(Data.next())
            {
                 if(Data.getInt(1)==1)
                     return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }

    }
    public String getApplicantsStatus(int StudentId) throws SQLException {
       Connection con = null;
        try {
            con = new DBConnection().getConnection();
            String ApplicantStatus = "Waiting";
            PreparedStatement query = con.prepareStatement("select IsMGUApproved from StudentPersonal where StudentId=?");
            query.setInt(1, StudentId);
            ResultSet Data = query.executeQuery();
            Data.next();
            String status = Data.getString(1);
            if (status != null) {
                if (status.equals("-1"))
                {
                    ApplicantStatus = "Rejected";
                } else if (status.equals("1")) {
                    ApplicantStatus = "Verified";
                } else {
                    ApplicantStatus = "Waiting";
                }
            }    con.close();
            return ApplicantStatus;

        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
                con.close();
            }
            return "";
        }
    }

    public String getRemarks(int StudentId) throws SQLException {
         Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select Remarks from StudentPersonal where StudentId=?");
            query.setInt(1, StudentId);
            ResultSet Data = query.executeQuery();
             String remarks=null;
           if(Data.next())
           remarks = Data.getString("Remarks");
           // response.getWriter.print("****"+remarks);
            con.close();
            if(remarks!=null)
            return remarks;
            else
                return "";

        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
           if (con != null) {
                con.close();
            } return "";
        }

    }
     public String getMigration(int StudentId) throws SQLException {
         Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select DocumentName from StudentDocumentMaster where StudentId=? and DocumentId=5");
            query.setInt(1, StudentId);
            ResultSet Data = query.executeQuery();
                String migraiondetails=null;
            if(Data.next())
                  migraiondetails= Data.getString("DocumentName");
           // response.getWriter.print("****"+remarks);
            con.close();
            if(migraiondetails!=null)
            return migraiondetails;
            else
                return "";

        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
           if (con != null) {
                con.close();
            } return "";
        }

    }

  private String  generatePRN(int sid,Connection con,int year) throws SQLException  {
        PreparedStatement st;
      try {

        //    st = con.prepareStatement("Lock tables StudentPersonal write,PRNStatus AS p write,CollegeMaster AS c write");
         //  st.execute();
            st = con.prepareStatement("SELECT CollegeCode,c.CollegeId,BranchId,LastStudentNo from PRNStatus p,CollegeMaster c where Year=? and p.BranchId=" + "(select BranchId from StudentPersonal where StudentId =?) and p.CollegeId=(select CollegeId from StudentPersonal where StudentId=? )  and c.CollegeId=p.CollegeId");
            st.setInt(1, year);
            st.setInt(2, sid);
            st.setInt(3, sid);
            ResultSet Data = st.executeQuery();
            String PRN = "IN09707";
            String CourseCode;
            String col;
            String colid;
            int count = 0;
            while (Data.next()) {
                count++;
                col=Data.getString(1);
                if(col.length()< 4)
                {
                    colid = "0" + Data.getString(1);
                }  else {
                    colid = Data.getString(1);
                }
                PRN = PRN + colid;
                


                if (Data.getInt(3) < 10) {
                    CourseCode = "0" + Data.getString(3);
                }  else {
                    CourseCode = Data.getString(3);
                }
                int StudentNo = Data.getInt(4) + 1;
                PRN = PRN + CourseCode + new Integer(year).toString().substring(2, 4) + new Integer(StudentNo).toString();
                st = con.prepareStatement("update  PRNStatus set LastStudentNo=LastStudentNo+1 where year=? and CollegeId = ? and BranchId=?");
                st.setInt(1, year);
                st.setInt(2, Data.getInt(2));
                st.setInt(3, Data.getInt(3));
                st.executeUpdate();
           //     st = con.prepareStatement("Unlock tables");
             //   st.execute();
                return PRN;
            }
            if (count == 0) {
                st = con.prepareStatement("select c.CollegeId,BranchId,CollegeCode from StudentPersonal ,CollegeMaster c where StudentId=? and StudentPersonal.CollegeId=c.CollegeId");
                st.setInt(1, sid);
                Data = st.executeQuery();
                int CollegeId = 0;
                int BranchId = 0;

                if (Data.next()) {
                    CollegeId = Data.getInt(1);

                    BranchId = Data.getInt(2);
                    PRN = PRN + Data.getString(3);
                }
                st = con.prepareStatement("insert into PRNStatus values(?,?,?,?)");
                st.setInt(1, 10001);
                st.setInt(2, year);
                st.setInt(3, CollegeId);
                st.setInt(4, BranchId);
                st.executeUpdate();

               

                if (BranchId < 10) {
                    CourseCode = "0" + new Integer(BranchId).toString();
                }  else {
                    CourseCode = new Integer(BranchId).toString();
                }
                PRN += CourseCode + new Integer(year).toString().substring(2, 4) + "10001";
                return PRN;
            }
            //st = con.prepareStatement("Unlock tables");
            //st.execute();
           return null;
        } catch (SQLException ex) {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
           //st = con.prepareStatement("Unlock tables");
            //st.execute();
            return null;
        }
    }

  public boolean EditRemarksofApprovedStudents(String Remarks,int StudentId) throws SQLException
  {
      Connection con = null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("Update StudentPersonal set Remarks=? where StudentId=?");
            st.setString(1, Remarks);
            st.setInt(2, StudentId);
            st.execute();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
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

  public boolean ChangeReAdmissionStatus(int StudentId) throws SQLException
{
    Connection con=null;
    try
    {
        con=new DBConnection().getConnection();
        PreparedStatement st=con.prepareStatement("Update StudentDiscontinueDetails set ReAdmissionStatus=1 where StudentId=?");
        st.setInt(1, StudentId);
        st.execute();

        st=con.prepareStatement("Update StudentPersonal set IsDisContinue=0 where StudentId=?");
        st.setInt(1, StudentId);
        return true;
    }
    catch (SQLException ex)
    {
        Logger.getLogger(CenterVerification.class.getName()).log(Level.SEVERE, null, ex);
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

    public static void main(String s[])
    {
         Calendar cal=Calendar.getInstance();
         String y=new Integer(cal.get(Calendar.YEAR)).toString().substring(2, 4);
            System.out.print(y);
    }
 
}

