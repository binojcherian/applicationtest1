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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mgu
 */
public class QualificationDetails {
    AdmissionYear CurYear=new AdmissionYear();
    HttpServletRequest request;
    HttpServletResponse response;
    PrintWriter out = null;
    int StudentId = 0;
    Hashtable<String, String> Qualification = new Hashtable<String, String>();
    Hashtable<String,String> College=new Hashtable<String, String>();
    Hashtable<String,String> CourseNames=new Hashtable<String, String>();
    public QualificationDetails(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

    }

    public String getCourse(String BranchId) throws IOException, SQLException
    {
        Connection con =null;
        try {
             con = new DBConnection().getConnection();
             PreparedStatement Branchquery = con.prepareStatement("select DisplayName from BranchMaster where BranchId=?");
             Branchquery.setInt(1, Integer.parseInt(BranchId));
             ResultSet branch = Branchquery.executeQuery();
             branch.next();
             String DisplayName=branch.getString("DisplayName");
             con.close();
             return DisplayName;
        } catch (SQLException ex) {
            response.getWriter().print(ex);
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
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

   public String getBranchName(int BranchId) throws IOException, SQLException
   {
       Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement Branchquery = con.prepareStatement("select BranchName,CourseId from BranchMaster where BranchId=?");
            Branchquery.setInt(1,BranchId);
            ResultSet branch = Branchquery.executeQuery();
            branch.next();
            String BranchName = branch.getString("BranchName");
            con.close();
            return BranchName;
        } catch (SQLException ex) {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
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
   
     public String getCourseName(int BranchId) throws IOException{
        try {
            Connection con = new DBConnection().getConnection();
             PreparedStatement Branchquery = con.prepareStatement("select CourseId from BranchMaster where BranchId=?");
             Branchquery.setInt(1,BranchId);
             ResultSet branch = Branchquery.executeQuery();
             branch.next();

             int courseId=branch.getInt("CourseId");

             PreparedStatement Coursequery = con.prepareStatement("select CourseCode from CourseMaster where CourseId=?");
             Coursequery.setInt(1,courseId);
             ResultSet course = Coursequery.executeQuery();
             course.next();
             String CourseCode = course.getString("CourseCode");
             con.close();
             return CourseCode;
        } catch (SQLException ex) {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
}
    public String getCenter(String CenterId){

        try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select CollegeName from CollegeMaster where CollegeId=?");
            query.setInt(1, Integer.parseInt(CenterId));
            ResultSet Data = query.executeQuery();
            Data.next();
              String College=Data.getString("CollegeName");
              con.close();
              return College;
        } catch (SQLException ex) {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
           return null;
        }}

    public boolean saveQualification() throws IOException, SQLException {
        Connection con = null;
        StudentId = (Integer) request.getSession().getAttribute("StudentId");
        Boolean secUpdate = false;
        Boolean hisecUpdate = false;
        Boolean degUpdate = false;
        try {
            con = new DBConnection().getConnection();
           // con.setAutoCommit(false);
           // con.rollback();
            PreparedStatement st = con.prepareStatement("select ExamName from EducationMaster where StudentId=?");
            st.setInt(1, StudentId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                if (rs.getString("ExamName").equals("10th")) {
                    secUpdate = true;
                } else if (rs.getString("ExamName").equals("12th")) {
                    hisecUpdate = true;
                } else if (rs.getString("ExamName").equals("Degree")) {
                    degUpdate = true;
                }
            }
            saveCourseapplied(con);
            saveCenterDetails(con);
            saveSecEducation(con, secUpdate);
            saveHiSecEducation(con, hisecUpdate);
            String BranchId = request.getParameter("course_applied");

            if (BranchId != null) {
                      if(checkMasters(BranchId))
                        saveDegree(con, degUpdate);
                    }
         
            con.close();
            return true;
        } catch (SQLException ex) {
         
            if (con != null) {
                con.close();
            }
            return false;

        }
    }
public int getDurationForBranch(String BranchId) throws SQLException
{
    Connection con=null;
        try
        {

            con = new DBConnection().getConnection();
            PreparedStatement Branchquery = con.prepareStatement("select Duration from BranchMaster where BranchId=?");
            Branchquery.setString(1,BranchId);
            ResultSet branch = Branchquery.executeQuery();
            if(branch.next())
            {
                return branch.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
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
    public void saveCourseapplied(Connection con) throws IOException, SQLException {
        try {

            PreparedStatement Query = con.prepareStatement("update StudentPersonal set BranchId=? where StudentId=?");
            Query.setString(1, request.getParameter("course_applied"));
            Query.setInt(2, StudentId);
            response.getWriter().print(request.getParameter("course_applied")+Query);
            Query.executeUpdate();
         
        } catch (SQLException ex) {
            
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

      public void saveCenterDetails(Connection con) throws IOException, SQLException {
        try {
                      

            PreparedStatement Query = con.prepareStatement("update StudentPersonal set CollegeId=? where StudentId=?");
            Query.setString(1, request.getParameter("centerOpt1"));
            Query.setInt(2, StudentId);
            Query.executeUpdate();

        } catch (SQLException ex) {
            response.getWriter().print(ex);
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void saveSecEducation(Connection con, Boolean isUpdate) throws IOException, SQLException 
    {
        
          response.getWriter().print("save sec");

        String sec_board = request.getParameter("sec_board");
        String sec_school = request.getParameter("sec_school");
        String sec_passdate = request.getParameter("sec_passdate");
        String sec_subject = request.getParameter("sec_subject");
        String sec_regstno = request.getParameter("sec_regstno");
        String sec_marks = request.getParameter("sec_marks");
        String sec_outof = request.getParameter("sec_outof");
      
        if (isUpdate) {
            try {

                String Query = "update EducationMaster set BoardOfUniversity=?,DateofPassing=?,RegistrationNo=?,Marks=?,OutOf=?,Specialization=?,CollegeName=? where StudentId=? and ExamName=?";
                PreparedStatement ps = con.prepareStatement(Query);
                ps.setString(1, sec_board);
                ps.setInt(2,  Integer.parseInt(sec_passdate));
                ps.setString(3, sec_regstno);
                ps.setString(4, (sec_marks));
                ps.setString(5, sec_outof);
                ps.setString(6, sec_subject);
                ps.setString(7, sec_school);
                ps.setInt(8, StudentId);
                ps.setString(9, "10th");
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                String Query = "INSERT INTO EducationMaster(StudentId,ExamName,BoardOfUniversity,DateofPassing,RegistrationNo,Marks,OutOf,CertificateNo,Specialization,CollegeName)  VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(Query);
                ps.setInt(1, StudentId);
                ps.setString(2, "10th");
                ps.setString(3, sec_board);
                ps.setInt(4, Integer.parseInt(sec_passdate));
                ps.setString(5, sec_regstno);
                ps.setString(6, sec_marks);
                ps.setString(7, sec_outof);
                ps.setInt(8, 5555);
                ps.setString(9, sec_subject);
                ps.setString(10, sec_school);
                ps.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
     /*if(request.getParameter("Grade").equals("1"))
        {
          response.getWriter().print("Grade save");
              try {
                  PreparedStatement ps=con.prepareStatement("delete from SSLCMaster where Studentid=?");
                          ps.setInt(1, StudentId);
                  ps.executeUpdate();
                   String Query= " insert into SSLCMaster (StudentId,SubjectCode,Subject,Grade) values(?,?,?,?)";
                ps = con.prepareStatement(Query);
                ps.setInt(1, StudentId);
                ps.setString(2, "FLPart1");
                ps.setString(3, request.getParameter("FLPart1Name"));
                ps.setString(4, request.getParameter("FLPart1Grade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "FLPart2");
                ps.setString(3, request.getParameter("FLPart2Name"));
                ps.setString(4, request.getParameter("FLPart2Grade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "SL");
                ps.setString(3, request.getParameter("SLName"));
                ps.setString(4, request.getParameter("SLGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "TL");
                ps.setString(3, request.getParameter("TLName"));
                ps.setString(4, request.getParameter("TLGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "Physics");
                ps.setString(3, "Physics");
                ps.setString(4, request.getParameter("PhysicsGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "Mathematics");
                ps.setString(3, "Mathematics");
                ps.setString(4, request.getParameter("MathematicsGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "Chemistry");
                ps.setString(3, "Chemistry");
                ps.setString(4, request.getParameter("ChemistryGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "IT");
                ps.setString(3, "Information Technology");
                ps.setString(4, request.getParameter("ITGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "SS");
                ps.setString(3, "Social Science");
                ps.setString(4, request.getParameter("SSGrade"));
                ps.executeUpdate();
                ps.setInt(1, StudentId);
                ps.setString(2, "Biology");
                ps.setString(3,"Biology");
                ps.setString(4, request.getParameter("BiologyGrade"));
                ps.executeUpdate();
                                
            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         }*/
        
         
    }

    public void saveHiSecEducation(Connection con, Boolean isUpdate) throws IOException, SQLException {


        String hisec_board = request.getParameter("hisec_board");
        String hisec_school = request.getParameter("hisec_school");
        String hisec_passdate = request.getParameter("hisec_passdate");
        String hisec_subject = request.getParameter("hisec_subject");
        String hisec_regstno = request.getParameter("hisec_regstno");
        String hisec_marks = request.getParameter("hisec_marks");
        String hisec_outof = request.getParameter("hisec_outof");
        if (isUpdate) {
            try {
                String Query = "update EducationMaster set BoardOfUniversity=?,DateofPassing=?,RegistrationNo=?,Marks=?,OutOf=?,Specialization=?,CollegeName=? where StudentId=? and ExamName=?";
                PreparedStatement ps = con.prepareStatement(Query);
                ps.setString(1, hisec_board);
                ps.setInt(2, Integer.parseInt(hisec_passdate));
                ps.setString(3, hisec_regstno);
                ps.setString(4, hisec_marks);
                ps.setString(5, hisec_outof);
                ps.setString(6, hisec_subject);
                ps.setString(7, hisec_school);
                ps.setInt(8, StudentId);
                ps.setString(9, "12th");
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        } else {
            try {
                String Query = "INSERT INTO EducationMaster(StudentId,ExamName,BoardOfUniversity,DateofPassing,RegistrationNo,Marks,OutOf,CertificateNo,Specialization,CollegeName)  VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(Query);
                ps.setInt(1, StudentId);
                ps.setString(2, "12th");
                ps.setString(3, hisec_board);
                ps.setInt(4, Integer.parseInt(hisec_passdate));
                ps.setString(5, hisec_regstno);
                ps.setString(6,hisec_marks);
                ps.setString(7, hisec_outof);
                ps.setInt(8, 6666);
                ps.setString(9, hisec_subject);
                ps.setString(10, hisec_school);


                ps.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        }

    }

    public void saveDegree(Connection con, Boolean isUpdate) throws IOException, SQLException {

        String degree_university = request.getParameter("degree_university");
        String degree_college = request.getParameter("degree_college");
        String degree_passdate = request.getParameter("degree_passdate");
        String degree_subject = request.getParameter("degree_subject");
        String degree_regstno = request.getParameter("degree_regstno");
        String degree_marks = request.getParameter("degree_marks");
        String degree_outof = request.getParameter("degree_outof");

        if (isUpdate) {
            try {
                String Query = "update EducationMaster set BoardOfUniversity=?,DateofPassing=?,RegistrationNo=?,Marks=?,OutOf=?,Specialization=?,CollegeName=? where StudentId=? and ExamName=? ";
                PreparedStatement ps = con.prepareStatement(Query);
                ps.setString(1, degree_university);
                ps.setInt(2, Integer.parseInt(degree_passdate));
                ps.setString(3, degree_regstno);
                ps.setString(4, degree_marks);
                ps.setString(5, degree_outof);
                ps.setString(6, degree_subject);
                ps.setString(7, degree_college);
                ps.setInt(8, StudentId);
                ps.setString(9, "Degree");
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
             

        } else {
            try {
                String Query = "INSERT INTO EducationMaster(StudentId,ExamName,BoardOfUniversity,DateofPassing,RegistrationNo,Marks,OutOf,CertificateNo,Specialization,CollegeName)  VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(Query);
                ps.setInt(1, StudentId);
                ps.setString(2, "Degree");
                ps.setString(3, degree_university);
                ps.setInt(4, Integer.parseInt(degree_passdate));
                ps.setString(5, degree_regstno);
                ps.setString(6,degree_marks);
                ps.setString(7, degree_outof);
                ps.setInt(8, 6666);
                ps.setString(9, degree_subject);
                ps.setString(10, degree_college);
                ps.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        }
    }

  public boolean Execute() throws SQLException
    {
      Connection con = null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select CollegeName,CollegeId from CollegeMaster ");

            ResultSet Records = query.executeQuery();
            while (Records.next()) {
                 College.put(Records.getString(2), Records.getString(1));


            }
             return true;
        } catch (SQLException ex) {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
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

public Set ListCourseNames() throws SQLException{
    Connection con1 =null;
        try {
            con1 = new DBConnection().getConnection();
           
                PreparedStatement st3 = con1.prepareStatement("select BranchId,DisplayName from BranchMaster where CourseId in(select CourseId from CourseMaster) order by DisplayName");
                //st3.setInt(1, rs.getInt(1));
                ResultSet Records = st3.executeQuery();
                while (Records.next())
                { 
                    CourseNames.put(Records.getString(1),Records.getString(2));   
                }
            
             Set CourseList=CourseNames.entrySet();
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
         finally
        {
            if(con1!=null)
            {
                con1.close();
            }
        }
}

public boolean isRegistrationDateOverForBranch(String BranchId) throws SQLException
{
     Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select datediff(sysdate(),RegistrationEndDate) from RegistrationBranchMaster where BranchId=? and Year=?");
            st.setString(1, BranchId);
            st.setInt(2, CurYear.getCurrentAdmissionYear());
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
                if(rs.getInt(1)>=0)
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

        }
        catch (SQLException ex)
        {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
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


    public Hashtable getQualificationDetails(int StudentId) throws IOException {
        this.StudentId = StudentId;
        if (FillQualificationDetails()) {
            return Qualification;
        }
        return null;
    }
    private boolean FillQualificationDetails() throws IOException {
        try {
            out = response.getWriter();
            Connection con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select ExamName,BoardOfUniversity,DateofPassing,RegistrationNo,Marks,OutOf,CertificateNo,Specialization,CollegeName"
                    + " from EducationMaster where StudentId=?");
            query.setInt(1, StudentId);
            ResultSet data = query.executeQuery();
            while (data.next()) {
                if (data.getString("ExamName").equals("10th")) {

                    Qualification.put("sec_board", data.getString("BoardOfUniversity"));
                    Qualification.put("sec_school", data.getString("CollegeName"));
                    Qualification.put("sec_passdate",  new Integer(data.getInt("DateofPassing")).toString());
                    Qualification.put("sec_subject", data.getString("Specialization"));
                    Qualification.put("sec_regstno", data.getString("RegistrationNo"));
                    Qualification.put("sec_marks", data.getString("Marks"));
                    Qualification.put("sec_outof", data.getString("OutOf"));
                   
                   /* if(data.getString("OutOf")!=null&&data.getString("OutOf").equals("Passed"))
                    {
                           PreparedStatement GradeQuery=con.prepareStatement("SELECT SubjectCode,Subject,Grade FROM DEMS_db.SSLCMaster where StudentId=?");
                           GradeQuery.setInt(1, StudentId);
                           ResultSet Rs=GradeQuery.executeQuery();
                           while(Rs.next())
                           {
                                if(Rs.getString(1).equals("FLPart1"))
                                {
                                     Qualification.put("FLPart1Name",Rs.getString(2) );
                                     Qualification.put("FLPart1Grade", Rs.getString(3));
                                }
                                if(Rs.getString(1).equals("FLPart2"))
                                {
                                     Qualification.put("FLPart2Name", Rs.getString(2));
                                     Qualification.put("FLPart2Grade", Rs.getString(3));
                                }
                               if(Rs.getString(1).equals("SL"))
                               {

                                Qualification.put("SLName",Rs.getString(2));
                                Qualification.put("SLGrade",Rs.getString(3));
                               }

                              if(Rs.getString(1).equals("TL"))
                              {
                                 Qualification.put("TLName",Rs.getString(2));
                                 Qualification.put("TLGrade",Rs.getString(3));
                              }
                           if(Rs.getString(1).equals("Physics"))
                          {
                                 Qualification.put("PhysicsGrade",Rs.getString(3));
                             }
                           if(Rs.getString(1).equals("Mathematics"))
                           {
                                  Qualification.put("MathematicsGrade",Rs.getString(3));
                            }
                          if(Rs.getString(1).equals("Chemistry"))
                          {
                                Qualification.put("ChemistryGrade", Rs.getString(3));
                           }

                           if(Rs.getString(1).equals("IT"))
                          {
                               Qualification.put("ITGrade", Rs.getString(3));
                          }
                          if(Rs.getString(1).equals("SS"))
                          {
                              Qualification.put("SSGrade",Rs.getString(3));
                         }
                         if(Rs.getString(1).equals("Biology"))
                              {
                            Qualification.put("BiologyGrade",Rs.getString(3));
                             }

                           }
                          Qualification.put("Grade", "1");
                    }
                    else
                    {
                         Qualification.put("Grade", "0");
                    }*/

                } else if (data.getString("ExamName").equals("12th")) {

                    Qualification.put("hisec_board", data.getString("BoardOfUniversity"));
                    Qualification.put("hisec_school", data.getString("CollegeName"));
                    Qualification.put("hisec_passdate", new Integer(data.getInt("DateofPassing")).toString());
                    Qualification.put("hisec_subject", data.getString("Specialization"));
                    Qualification.put("hisec_regstno", data.getString("RegistrationNo"));
                    Qualification.put("hisec_marks", data.getString("Marks"));
                    Qualification.put("hisec_outof", data.getString("OutOf"));

                } else if (data.getString("ExamName").equals("Degree")) {

                    Qualification.put("degree_university", data.getString("BoardOfUniversity"));
                    Qualification.put("degree_college", data.getString("CollegeName"));
                    Qualification.put("degree_passdate", new Integer(data.getInt("DateofPassing")).toString());
                    Qualification.put("degree_subject", data.getString("Specialization"));
                    Qualification.put("degree_regstno", data.getString("RegistrationNo"));
                    Qualification.put("degree_marks",  data.getString("Marks"));
                    Qualification.put("degree_outof", data.getString("OutOf"));
                }

                PreparedStatement query1 = con.prepareStatement("select CollegeId,BranchId from StudentPersonal where StudentId=?");
                query1.setInt(1, StudentId);
                ResultSet data1 = query1.executeQuery();
                while (data1.next()) {
                    if(data1.getString(1)!=null){
                    Qualification.put("CenterId", data1.getString(1));
                    Qualification.put("BranchId", data1.getString(2));}
                }

            }
            con.close();
            return true;
        } catch (SQLException ex) {
            response.getWriter().print(ex);

            return false;
        }
    }
public boolean checkMasters(String BranchId) throws SQLException{
     Connection con =null;
            try {
                con = new DBConnection().getConnection();
                Boolean isMasters = false;
                PreparedStatement st = con.prepareStatement("select BranchCode,CourseId from BranchMaster where BranchId=?");
                st.setInt(1, Integer.parseInt(BranchId));
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    if (rs.getString("BranchCode") != null) {
                        PreparedStatement stmt = con.prepareStatement("select BasicQualification from CourseMaster where CourseId=?");
                        stmt.setInt(1, rs.getInt("CourseId"));
                        ResultSet record = stmt.executeQuery();
                        while (record.next()) {
                            isMasters = record.getString("BasicQualification").equals("Degree");
                           
                        }
                    }
                }
                 return isMasters;
            } catch (SQLException ex) {

                Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
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





   public static void main(String[] args) throws Exception
    {


      QualificationDetails d1=new QualificationDetails(null, null);
      d1.ListCourseNames();
      //System.out.print(d1.CourseNames);
     
     Set s1=d1.CourseNames.entrySet();
     for(Object y:s1){
         Map.Entry<String ,String > o=(Map.Entry<String,String >)y;
         System.out.print(o.getKey());
         System.out.print(o.getValue());
     }
               
}
 public void SaveCourseCenterDetailsToReAdmission(Connection con)
            throws IOException, SQLException {
        try {

            PreparedStatement Query = con.prepareStatement("update ReAdmission set BranchId=?,CollegeId=? where StudentId=?");
            Query.setString(1, request.getParameter("course_applied"));
            Query.setString(2, request.getParameter("centerOpt1"));
            Query.setInt(3, StudentId);
            //response.getWriter().print(request.getParameter("course_applied")+Query);
            Query.executeUpdate();

        } catch (SQLException ex) {

            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
 public boolean saveQualificationForReAdmission() throws IOException, SQLException {
        Connection con = null;
        StudentId = (Integer) request.getSession().getAttribute("StudentId");
        Boolean secUpdate = false;
        Boolean hisecUpdate = false;
        Boolean degUpdate = false;
        try {
            con = new DBConnection().getConnection();


            PreparedStatement st = con.prepareStatement("select ExamName from EducationMaster where StudentId=?");
            st.setInt(1, StudentId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                if (rs.getString("ExamName").equals("10th")) {
                    secUpdate = true;
                } else if (rs.getString("ExamName").equals("12th")) {
                    hisecUpdate = true;
                } else if (rs.getString("ExamName").equals("Degree")) {
                    degUpdate = true;
                }
            }
            saveCourseapplied(con);
             SaveStudentAttendingYear(StudentId,request.getParameter("AttYear"));
            saveCenterDetails(con);
            SaveCourseCenterDetailsToReAdmission(con);
            saveSecEducation(con, secUpdate);
            saveHiSecEducation(con, hisecUpdate);
            String BranchId = request.getParameter("course_applied");

            if (BranchId != null) {
                      if(checkMasters(BranchId))
                        saveDegree(con, degUpdate);
                    }

            con.close();
            return true;
        } catch (SQLException ex) {

            if (con != null) {
                con.close();

            }
            return false;

        }

    }
  public void SaveStudentAttendingYear(int StudentId,String AttendingYear) throws SQLException
    {
         Connection con=null;
        try
        {
              con = new DBConnection().getConnection();
              PreparedStatement st=con.prepareStatement("Update StudentPersonal set AttendingYear=? where StudentId=?");
              st.setString(1, AttendingYear);
              st.setInt(2, StudentId);
              st.execute();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(QualificationDetails.class.getName()).log(Level.SEVERE, null, ex);

        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }
  public Hashtable getQualificationDetailsForReAdmission(int StudentId) throws IOException {
        this.StudentId = StudentId;
        if (FillQualificationDetailsForReAdmission()) {
            return Qualification;
        }
        return null;
    }

    private boolean FillQualificationDetailsForReAdmission() throws IOException {
        try {
            out = response.getWriter();
            Connection con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select ExamName,BoardOfUniversity,DateofPassing,RegistrationNo,Marks,OutOf,CertificateNo,Specialization,CollegeName"
                    + " from EducationMaster where StudentId=?");
            query.setInt(1, StudentId);
            ResultSet data = query.executeQuery();
            while (data.next()) {
                if (data.getString("ExamName").equals("10th")) {

                    Qualification.put("sec_board", data.getString("BoardOfUniversity"));
                    Qualification.put("sec_school", data.getString("CollegeName"));
                    Qualification.put("sec_passdate",  new Integer(data.getInt("DateofPassing")).toString());
                    Qualification.put("sec_subject", data.getString("Specialization"));
                    Qualification.put("sec_regstno", data.getString("RegistrationNo"));
                    Qualification.put("sec_marks", data.getString("Marks"));
                    Qualification.put("sec_outof", data.getString("OutOf"));

                    if(data.getString("OutOf")!=null&&data.getString("OutOf").equals("Passed"))
                    {

                          Qualification.put("Grade", "1");
                    }
                    else
                    {
                         Qualification.put("Grade", "0");
                    }

                } else if (data.getString("ExamName").equals("12th")) {

                    Qualification.put("hisec_board", data.getString("BoardOfUniversity"));
                    Qualification.put("hisec_school", data.getString("CollegeName"));
                    Qualification.put("hisec_passdate", new Integer(data.getInt("DateofPassing")).toString());
                    Qualification.put("hisec_subject", data.getString("Specialization"));
                    Qualification.put("hisec_regstno", data.getString("RegistrationNo"));
                    Qualification.put("hisec_marks", data.getString("Marks"));
                    Qualification.put("hisec_outof", data.getString("OutOf"));

                } else if (data.getString("ExamName").equals("Degree")) {

                    Qualification.put("degree_university", data.getString("BoardOfUniversity"));
                    Qualification.put("degree_college", data.getString("CollegeName"));
                    Qualification.put("degree_passdate", new Integer(data.getInt("DateofPassing")).toString());
                    Qualification.put("degree_subject", data.getString("Specialization"));
                    Qualification.put("degree_regstno", data.getString("RegistrationNo"));
                    Qualification.put("degree_marks",  data.getString("Marks"));
                    Qualification.put("degree_outof", data.getString("OutOf"));
                }

                PreparedStatement query1 = con.prepareStatement("select CollegeId,BranchId from StudentPersonal where StudentId=?");
                query1.setInt(1, StudentId);
                ResultSet data1 = query1.executeQuery();
                while (data1.next()) {
                    if(data1.getString(1)!=null){
                    Qualification.put("CenterId", data1.getString(1));
                    Qualification.put("BranchId", data1.getString(2));}
                }

            }
            con.close();
            return true;
        } catch (SQLException ex) {
            response.getWriter().print(ex);

            return false;
        }
    }

}
