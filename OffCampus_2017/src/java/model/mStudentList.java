/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;


import Entity.CentreDetails;
import Entity.ExamVenueDetails;
import Entity.Student;
import Entity.Subject;
import Entity.SubjectBranch;
import Entity.SubjectExam;
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
public class mStudentList {
Connection con=null;
 private ArrayList<String> StudentId=new ArrayList<String>();
    private ArrayList<String> StudentName=new ArrayList<String>();
    private ArrayList<String> ApplicationNo=new ArrayList<String>();
    private ArrayList<StudentData> StudentList=new ArrayList<StudentData>();
 
    public mStudentList()
    {
        
    }

   public String getStudentWithheldDetails(int StudentId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            int ExamId=1;
            PreparedStatement st = con.prepareStatement("SELECT Status FROM StudentExamWithheldDetails where StudentId=? and ExamId=? and  EndDate is null");
            st.setInt(1, StudentId);
            st.setInt(2, ExamId);
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
               if(Rs.getString("Status").equals("MalPractice"))
               {
                   return "Result Withheld.";
               }else if(Rs.getString("Status").equals("Unconfirmed"))
               {
                  return "Result Announced Later.";
               }
               
               
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
    }
 
  public ArrayList<StudentsForExam> MguApprovedStudentListForSubject(String SubjectBranchId,String CentreId,Connection con)
    {

        ArrayList<StudentsForExam> Students=new ArrayList<StudentsForExam>();
      try {

            PreparedStatement st = con.prepareStatement("SELECT ifnull(p.PRN,''),p.StudentName,p.StudentId  FROM StudentExamFeeStatus s inner join StudentPersonal p on p.StudentId=s.StudentId inner join ExamRegistrationMaster e on e.StudentId=s.StudentId and e.SubjectBranchId=? and e.ExamId=(select max(ExamId) from ExamMaster)  where s.IsMguApproved=1 and s.CollegeId=? order by PRN");
            st.setString(1, SubjectBranchId);
            st.setString(2, CentreId);
            ResultSet Rs = st.executeQuery();

            StudentsForExam stud=null;
            while (Rs.next())
            {
                stud=new StudentsForExam();
                stud.PRN=Rs.getString(1);
                stud.StudentName=Rs.getString(2);
                stud.StudentId=Rs.getInt("StudentId");
                Students.add(stud);
            }
                 Rs.close();

          return  Students;

           } catch (SQLException ex) {
            Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(StudentsForExam.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Students;
        }

    }
            
public int getMaxYearOrSemForCourse(String CourseId) throws SQLException
{
    Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT max(CurrentYearSem) FROM SubjectBranchMaster where BranchId=?");
            st.setString(1, CourseId);
            ResultSet Rs=st.executeQuery();

            if(Rs.next())
            {
                return Rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
}
public ArrayList<SubjectBranch> getRegisteredSupplyOrImprovementPapers_nominalroll(int StudentId,int BranchId,Connection connection,int Year,String ExamId)
    {
        try {
            if(Year>1)
            {
            int examId=4;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
                  
            ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
             PreparedStatement st = connection.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? ");
             st.setInt(1, BranchId);
         
              rs = st.executeQuery();
            ps=connection.prepareStatement("SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName,e.ExamType FROM ExamRegistrationMaster e inner join"
                     + " SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId` inner join SubjectMaster sm on sm.SubjectId=s.SubjectId"
                     + " WHERE e.`StudentId` =? AND e.`ExamId` =? AND e.RegistrationType=1  and CurrentYearSem <= ? order by CurrentYearSem ");
                  ps.setInt(1, StudentId);
                  ps.setString(2, ExamId);
                 
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                  ps.setInt(3, (Year+1));
                 } else {
                     ps.setInt(3, Year);
                }
                 }
                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    sb.setSubject(subject);
                    sb.setSemOrYear(SubjectsDate.getInt(1));
                    sb.setSubjectBranchId(SubjectsDate.getInt(2));
                    subject.setSubjectId(SubjectsDate.getInt(3));
                    subject.setSubjectName(SubjectsDate.getString(4));
                    subject.setPaperType(SubjectsDate.getString(5));
                    subjectBranchs.add(sb);
                }
              return subjectBranchs;
            }
            else
                return new ArrayList<SubjectBranch>();
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }
public ArrayList<SubjectBranch> getRegisteredSupplyOrImprovementPapers(int StudentId,int BranchId,Connection connection,int Year)
    {
        try {
            if(Year>1)
            {
            int examId=4;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
                  
            ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
             PreparedStatement st = connection.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? ");
             st.setInt(1, BranchId);
         
              rs = st.executeQuery();
            ps=connection.prepareStatement("SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName,e.ExamType FROM ExamRegistrationMaster e inner join"
                     + " SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId` inner join SubjectMaster sm on sm.SubjectId=s.SubjectId"
                     + " WHERE e.`StudentId` =? AND e.`ExamId` =? AND e.RegistrationType=1  and CurrentYearSem <= ? order by CurrentYearSem ");
                  ps.setInt(1, StudentId);
                  ps.setInt(2, examId);
                 
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                  ps.setInt(3, (Year+1));
                 } else {
                     ps.setInt(3, Year);
                }
                 }
                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    sb.setSubject(subject);
                    sb.setSemOrYear(SubjectsDate.getInt(1));
                    sb.setSubjectBranchId(SubjectsDate.getInt(2));
                    sb.setPaperType(SubjectsDate.getString(5));
                    subject.setSubjectId(SubjectsDate.getInt(3));
                    subject.setSubjectName(SubjectsDate.getString(4));
                   
                    subjectBranchs.add(sb);
                }
              return subjectBranchs;
            }
            else
                return new ArrayList<SubjectBranch>();
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

public ArrayList<SubjectBranch> getRegisteredSupplyOrImprovementPapersForCourseComplted(int StudentId,int BranchId,Connection connection)
    {
        try {

            int examId=12;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
            ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
            
             ps=connection.prepareStatement("SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName,e.ExamType FROM ExamRegistrationMaster e inner join"
                     + " SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId` inner join SubjectMaster sm on sm.SubjectId=s.SubjectId"
                     + " WHERE e.`StudentId` =? AND e.`ExamId` =? AND e.RegistrationType=1  order by CurrentYearSem ");
                  ps.setInt(1, StudentId);
                  ps.setInt(2, examId);


                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    sb.setSubject(subject);
                    sb.setSemOrYear(SubjectsDate.getInt(1));
                    sb.setSubjectBranchId(SubjectsDate.getInt(2));           
                    subject.setSubjectId(SubjectsDate.getInt(3));
                    subject.setSubjectName(SubjectsDate.getString(4));
                    sb.setPaperType(SubjectsDate.getString(5));
                    subjectBranchs.add(sb);
                }
              return subjectBranchs;

        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }


 public ArrayList<SubjectBranch> getRegisteredOptionalPapers(int StudentId,int BranchId,Connection connection,int Year)
    {
        try {
    int SemOrYear=0;
            int examId=4;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
            ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
             PreparedStatement st = connection.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? ");
             st.setInt(1, BranchId);
              rs = st.executeQuery();
              if (rs.next())
                  {
                      SemOrYear=rs.getInt(1);
                  }
              String Query="";
              if((BranchId==21 && Year==2) ||  (BranchId==17 && Year==2) || (BranchId==26 && Year==1) || (BranchId==18 && Year==3) )
              {
                  Query="SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName, s.SubBranchId FROM ExamRegistrationMaster e inner join SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId`inner join SubjectMaster sm on sm.SubjectId=s.SubjectId  WHERE e.`StudentId` =? AND e.`ExamId` =? and s.SubjectBranchId in ("+getSubjectBranchIds(BranchId, connection, SemOrYear,Year) +") ";
              }
              else
              {
                Query="SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName ,s.SubBranchId  FROM ExamRegistrationMaster e inner join SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId`and (s.IsElective=1 or s.IsOptionalSubject=1)  inner join SubjectMaster sm on sm.SubjectId=s.SubjectId  WHERE e.`StudentId` =? AND e.`ExamId` =? and s.SubjectBranchId in ("+getSubjectBranchIds(BranchId, connection, SemOrYear,Year) +") ";
              }
              ps=connection.prepareStatement(Query);
                  ps.setInt(1, StudentId);
                  ps.setInt(2, examId);

                    ResultSet SubjectsDate = ps.executeQuery();
                    while (SubjectsDate.next())
                    {
                        SubjectBranch sb = new SubjectBranch();
                        SubjectExam subject = new SubjectExam();
                        sb.setSubject(subject);
                        sb.setSemOrYear(SubjectsDate.getInt(1));
                        sb.setSubjectBranchId(SubjectsDate.getInt(2));
                        subject.setSubjectId(SubjectsDate.getInt(3));
                        subject.setSubjectName(SubjectsDate.getString(4));
                        sb.setSubBranchId(SubjectsDate.getInt(5));
                        subjectBranchs.add(sb);
                    }
              return subjectBranchs;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }
  public int getRegisteredExamCentre(int StudentId,int BranchId,Connection connection)
    { int examcentre=0;
        try {
            

            int examId=4;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();

                  String Query="SELECT  ExamCentre FROM ExamRegistrationMaster e  WHERE e.`StudentId` =? AND e.`ExamId` =? ";

              ps=connection.prepareStatement(Query);
                  ps.setInt(1, StudentId);
                  ps.setInt(2, examId);

                    ResultSet SubjectsData = ps.executeQuery();
                    while (SubjectsData.next())
                    {
                                                examcentre=SubjectsData.getInt(1);
                       
                    }
              return examcentre;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return examcentre;
    }

 private String getSubjectBranchIds(int BranchId,Connection con,int SemOrYear,int AttendingYear)
    {
        String SubjectBranchId="";
        String Query="";
        try
        {
            if(SemOrYear==1&&AttendingYear==1)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=1";
            }else if(SemOrYear==1&&AttendingYear==2)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=2";
            }else if(SemOrYear==1&&AttendingYear==3)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and CurrentYearSem=3";
            }
            else if(SemOrYear==0&&AttendingYear==1)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or  CurrentYearSem=2 )";
            }else if(SemOrYear==0&&AttendingYear==2)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and ( CurrentYearSem=3 or CurrentYearSem=4 )";
            }else if(SemOrYear==0&&AttendingYear==3)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and ( CurrentYearSem=5 or CurrentYearSem=6 )";
            }
            PreparedStatement st=con.prepareStatement(Query);
            st.setInt(1, BranchId);
       
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                SubjectBranchId+=rs.getString("SubjectBranchId")+",";
            }
            String Mark="0";
            if(SubjectBranchId.length()>0)
            {
             Mark=SubjectBranchId.substring(0,SubjectBranchId.length()-1);
            }
            return Mark;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
    }
    
  public ArrayList<SubjectBranch> getSupplyOrImprovementPapers(int StudentId,int BranchId,int joinYear,int yearApplied) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        boolean isReadmission=false;
        boolean isRegistered=false;// ReAdmssion
        int readmtdYear = 0;int notionalYear=0;
        try {
            
            if(yearApplied>1){
           PreparedStatement st=connection.prepareStatement("select count(*),AttendingYear from ReAdmission where StudentId=? " );
            
            st.setInt(1, StudentId);
           // st.setInt(2, joinYear);
            //st.setInt(3, yearApplied);
            ResultSet re=st.executeQuery();
            while(re.next())
            {
                if(re.getInt(1)>0)
                {
                    isReadmission=true;
                    readmtdYear=re.getInt(2);
                }                  
                
                    PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc ) a limit 1,3 ");
                    ResultSet rs = ps.executeQuery();

                 while (rs.next()) {
                int ExamId = rs.getInt(1);
                //first pick supply subjects
                //For Handling CBCSS 
                if(BranchId==24 || BranchId==25||  BranchId==27|| BranchId==28|| BranchId==29|| BranchId==30 || BranchId==31 || BranchId==32 )
                { 
                    
                    ps=connection.prepareStatement("SELECT  S.`stud_paper_id`, sm.`SubjectId`, sm.`SubjectName`,S.theory_ext_grade_point,S.theory_int_grade_point,S.practical_ext_grade_point,S.practical_int_grade_point FROM DEMS_CAMP.exam_papers_consolidated_all S inner join SubjectBranchMaster sb on S.`stud_paper_id`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE  S.`student_id`=? and S.`Result`='F' ");
                    //ps =connection.prepareStatement("SELECT  S.`subject_branch_id`, sm.`SubjectId`, sm.`SubjectName`,S.theory_ext_grade,S.theory_int_grade,S.practical_ext_grade,S.practical_int_grade FROM DEMS_CAMP.exam_papers_lowersemesters S inner join SubjectBranchMaster sb on S.`subject_branch_id`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE  S.`student_id`=? and (S.`present_absent`='A' or  S.`course_result`='F') and S.`subject_branch_id` not in  (select f.`subject_branch_id` from  DEMS_CAMP.exam_papers_lowersemesters f where   f.`course_result`='P' and f.`student_id`=?)  group by S.`subject_branch_id`" );
                    ps.setInt(1, StudentId);
                    //ps.setInt(2, StudentId);
                    
                    
                    
                    }
                else{
                    ps = connection.prepareStatement("SELECT  S.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM StudentSubjectMark S inner join SubjectBranchMaster sb on S.`SubjectBranchId`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE S.`StudentId`=? and (S.`IsAbsent`=1 or  S.`IsPassed`=0) and S.`MarkStatus`=1 and  ExamId=? and S.IsValid=1");
                        ps.setInt(1, StudentId);
                        ps.setInt(2, ExamId);}
                
                ResultSet SubjectsDate = ps.executeQuery();
                System.out.println("supply"+ps);
                while (SubjectsDate.next()) {
                    isRegistered=true;
                    String supplyType="N";//For Bcom CBCSS
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    int subjectBranchId=SubjectsDate.getInt(1);
                   
                    if(subjectBranchId==803 || subjectBranchId==812 ||subjectBranchId==958 || subjectBranchId==963 ){
                        float try_ext=0;
                        float try_int=0;
                        float pract_ext=0;
                        float pract_int=0;
                         if(SubjectsDate.getString(4)!=null){
                         
                        try_ext=Float.valueOf(SubjectsDate.getString(4));}
                         
                         if(SubjectsDate.getString(5)!=null){
                        try_int=Float.valueOf(SubjectsDate.getString(5));}
                         
                         if(SubjectsDate.getString(6)!=null){
                         pract_ext=Float.valueOf(SubjectsDate.getString(6));}
                         if(SubjectsDate.getString(7)!=null){
                        pract_int=Float.valueOf(SubjectsDate.getString(7));}
                       
                        if(((try_ext<0.50) ||( try_int<0.50))&&((pract_ext<0.50) ||(pract_int<0.50) )){
                            supplyType="B";
                         }else if((try_ext<0.50) ||( try_int<0.50)){
                            supplyType="T";
                         }else if((pract_ext<0.50) ||(pract_int<0.50) ) {
                            supplyType="P"; 
                         }
                    }
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
                    subject.setPaperType(supplyType);
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                }

                

                if (BranchId == 13) {
                    //for BLSi if  not sem pass take 50 or <50 subjects
                    int semPassMark = getTotalPassMarkForBLiSc(BranchId + "", 1, connection);
                    int studentTotal = getSemTotalForStudent(StudentId + "", ExamId + "", 1, connection);
                    if (studentTotal < semPassMark) {
                        subjectBranchs.addAll(getlessThan50AndGreaterThan40Subjects(StudentId, 1, ExamId, connection));
                    }
                     semPassMark = getTotalPassMarkForBLiSc(BranchId + "", 2, connection);
                     studentTotal = getSemTotalForStudent(StudentId + "", ExamId + "", 2, connection);
                    if (studentTotal < semPassMark) {
                        subjectBranchs.addAll(getlessThan50AndGreaterThan40Subjects(StudentId, 2, ExamId, connection));
                    }
                }
                

            }
//                 //Improvement Option Bcom
//            if (BranchId == 7 || BranchId == 8) {
//                    subjectBranchs.addAll(getBcomPassedPart1And2Papers(StudentId, BranchId, connection,yearApplied));
//                }
//            if (BranchId == 24 || BranchId == 25 || BranchId == 26|| BranchId == 27|| BranchId == 28|| BranchId == 29|| BranchId == 30 || BranchId == 31) {
//                    subjectBranchs.addAll(getCBCSSPassedPapersForImprovemnt(StudentId, BranchId, connection,yearApplied));
//                }
            
            // ReAdmssion

                if(isReadmission && readmtdYear!=1){
                     String supplyType="N";//For Bcom CBCSS
                    int readmtedSem=(yearApplied*2)-1;

                 if(BranchId==7 || BranchId==8)
                    {
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem <="+(readmtdYear-1)+"");
                    }
                 else if(BranchId==21)
                 {
                         st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+readmtedSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+" and CurrentYearSem="+readmtedSem+ ")");
                 }
                    else
                    {
                        
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+readmtedSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+"  and CurrentYearSem<"+readmtedSem+")");
                    }
                    st.setInt(1, BranchId);
                   System.out.println("read"+st);
                    ResultSet result=st.executeQuery();
                    while(result.next())
                    {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    subject.setSubjectId(result.getInt(2));
                    subject.setSubjectName(result.getString(3));
                     subject.setPaperType(supplyType);
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(result.getInt(1));
                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                    }
                     }
                ////
                
            }
            
            
           //Checking Notional Registration 
            PreparedStatement st1=connection.prepareStatement("select count(*),AttendingYear from NotionalRegistration where StudentId=? " );
            st1.setInt(1, StudentId);
            //st.setInt(2, joinYear);
            //st.setInt(3, yearApplied);
            ResultSet re1=st1.executeQuery();
            while(re1.next())
            {
                if(re1.getInt(1)>0)
                {
                     notionalYear=re1.getInt(2);                   
                     int notionalSem=(yearApplied*2)-1;

                 if(BranchId==7 || BranchId==8)
                    {
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem <="+(notionalYear-1)+"");
                    }
                 else if(BranchId==21)
                 {
                         st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+notionalSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+" and CurrentYearSem="+notionalSem+ ")");
                         
                 }
                    else
                    {
                        
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+notionalSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+"  and CurrentYearSem<"+notionalSem+")");
                    }
                    st.setInt(1, BranchId);
                     String supplyType="N";//For Bcom CBCSS
                    ResultSet result=st.executeQuery();
                    while(result.next())
                    {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    subject.setSubjectId(result.getInt(2));
                    subject.setSubjectName(result.getString(3));
                    subject.setPaperType(supplyType);
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(result.getInt(1));
                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                    }
                    
                }                 
            }
            
            
                    }
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }
        
    }

 
   public ArrayList<SubjectBranch> getSupplyOrImprovementPapersOld(int StudentId,int BranchId,int joinYear,int yearApplied) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        boolean isReadmission=false;
        boolean isRegistered=false;// Radmssion
        int readmtdYear = 0;int notionalYear=0;
        try {
            
            if(yearApplied>1){
            PreparedStatement st=connection.prepareStatement("select count(*),AttendingYear from ReAdmission where StudentId=? " );
            st.setInt(1, StudentId);
            //st.setInt(2, joinYear);
            //st.setInt(3, yearApplied);
            ResultSet re=st.executeQuery();
            while(re.next())
            {
                if(re.getInt(1)>0)
                {
                    isReadmission=true;
                    readmtdYear=re.getInt(2);
                }                  
                
                    PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc ) a limit 1,3 ");
                    ResultSet rs = ps.executeQuery();

                 while (rs.next()) {
                int ExamId = rs.getInt(1);
                //first pick supply subjects
                ps = connection.prepareStatement("SELECT  S.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM StudentSubjectMark S inner join SubjectBranchMaster sb on S.`SubjectBranchId`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE S.`StudentId`=? and (S.`IsAbsent`=1 or  S.`IsPassed`=0) and ExamId=? and S.IsValid=1");
                ps.setInt(1, StudentId);
                ps.setInt(2, ExamId);
                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                    isRegistered=true;
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                }

                

                if (BranchId == 13) {
                    //for BLSi if  not sem pass take 50 or <50 subjects
                    int semPassMark = getTotalPassMarkForBLiSc(BranchId + "", 1, connection);
                    int studentTotal = getSemTotalForStudent(StudentId + "", ExamId + "", 1, connection);
                    if (studentTotal < semPassMark) {
                        subjectBranchs.addAll(getlessThan50AndGreaterThan40Subjects(StudentId, 1, ExamId, connection));
                    }
                     semPassMark = getTotalPassMarkForBLiSc(BranchId + "", 2, connection);
                     studentTotal = getSemTotalForStudent(StudentId + "", ExamId + "", 2, connection);
                    if (studentTotal < semPassMark) {
                        subjectBranchs.addAll(getlessThan50AndGreaterThan40Subjects(StudentId, 2, ExamId, connection));
                    }
                }
                

            }
            if (BranchId == 7 || BranchId == 8) {
                    subjectBranchs.addAll(getBcomPassedPart1And2Papers(StudentId, BranchId, connection,yearApplied));
                }
            
            // ReAdmssion

                if(isReadmission && readmtdYear!=1){
                    int readmtedSem=(yearApplied*2)-1;

                 if(BranchId==7 || BranchId==8)
                    {
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem <="+(readmtdYear-1)+"");
                    }
                 else if(BranchId==21)
                 {
                         st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+readmtedSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+" and CurrentYearSem="+readmtedSem+ ")");
                 }
                    else
                    {
                        
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+readmtedSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+"  and CurrentYearSem<"+readmtedSem+")");
                    }
                    st.setInt(1, BranchId);
                    
                    ResultSet result=st.executeQuery();
                    while(result.next())
                    {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    subject.setSubjectId(result.getInt(2));
                    subject.setSubjectName(result.getString(3));
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(result.getInt(1));
                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                    }
                     }
                ////
                
            }
            
            
           //Checking Notional Registration 
            PreparedStatement st1=connection.prepareStatement("select count(*),AttendingYear from NotionalRegistration where StudentId=? " );
            st1.setInt(1, StudentId);
            //st.setInt(2, joinYear);
            //st.setInt(3, yearApplied);
            ResultSet re1=st1.executeQuery();
            while(re1.next())
            {
                if(re1.getInt(1)>0)
                {
                     notionalYear=re1.getInt(2);                   
                     int notionalSem=(yearApplied*2)-1;

                 if(BranchId==7 || BranchId==8)
                    {
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem <="+(notionalYear-1)+"");
                    }
                 else if(BranchId==21)
                 {
                         st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+notionalSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+" and CurrentYearSem="+notionalSem+ ")");
                 }
                    else
                    {
                        
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem<"+notionalSem+" and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId="+BranchId+"  and CurrentYearSem<"+notionalSem+")");
                    }
                    st.setInt(1, BranchId);
                    
                    ResultSet result=st.executeQuery();
                    while(result.next())
                    {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    subject.setSubjectId(result.getInt(2));
                    subject.setSubjectName(result.getString(3));
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(result.getInt(1));
                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                    }
                    
                }                 
            }
            
            
                    }
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }
        
    }

    public ArrayList<SubjectBranch> getBcomPassedPart1And2Papers(int StudentId,int BranchId,Connection connection,int currentYear) 
    {   ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
    int examId=0;
        try {
            PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc ) a limit 1,1 ");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                examId = result.getInt(1);}
        
        PreparedStatement statement=connection.prepareStatement("SELECT sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` ,ss.StudentId "
                + "FROM SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId inner join StudentSubjectMark ss on "
                + "sb.`SubjectBranchId`=ss.`SubjectBranchId` and Ispassed=1 AND MarkStatus=1 WHERE sb.`BranchId` =? AND sb.`PartNo`!='PART-II'  AND sb.`PartNo`!='PART-I' AND CurrentYearSem=? And ss.StudentId=? and "
                + " ExamId=? and ss.IsValid=1");
            statement.setInt(1, BranchId);
            statement.setInt(2, (currentYear-1));//previous year for improvement
            statement.setInt(3, StudentId);
            statement.setInt(4, examId);
            ResultSet rs=statement.executeQuery();
           
             while (rs.next()) {
             SubjectExam subject=new SubjectExam();
             SubjectBranch subjectBranch=new SubjectBranch();
             
             subject.setSubjectId(rs.getInt(2));
             subject.setSubjectName(rs.getString(3));
             subjectBranch.setSubject(subject);
             subjectBranch.setSubjectBranchId(rs.getInt(1));
             list.add(subjectBranch);
                }
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }finally{return list;}
    }
    public ArrayList<SubjectBranch> getlessThan50AndGreaterThan40Subjects(int StudentId,int sem,int ExamId,Connection  connection) throws SQLException
    {
          PreparedStatement statement=connection.prepareStatement("SELECT sb.`SubjectBranchId` ,sm.SubjectId,sm.SubjectName FROM StudentSubjectMark ss"
                  + " inner join SubjectBranchMaster sb on sb.`SubjectBranchId`=ss.`SubjectBranchId` inner join SubjectMaster sm on "
                  + "sb.SubjectId=sm.SubjectId where  ss.StudentId=? and ExamId=? and ss.TotalMark<50 and ss.TotalMark>40   and sb.CurrentYearSem=? and ss.IsValid=1");
          statement.setInt(1, StudentId);
          statement.setInt(2, ExamId);
          statement.setInt(3, sem);
           ResultSet SubResultSet= statement.executeQuery();
           ArrayList<SubjectBranch>  subjectBranchs=new ArrayList<SubjectBranch>();
          while (SubResultSet.next()) {
              SubjectBranch sb=new SubjectBranch();
              SubjectExam subject=new SubjectExam();
              
              subject.setSubjectId(SubResultSet.getInt(2));
              subject.setSubjectName(SubResultSet.getString(3));
              sb.setSubject(subject);
              sb.setSubjectBranchId(SubResultSet.getInt(1));
                subjectBranchs.add(sb);
        }
           return subjectBranchs;
    }

   
   public ArrayList<SubjectBranch> getSubjectsForBranch(String BranchId,int Semester,Connection con) throws SQLException
{
     ArrayList<SubjectBranch> subjects=new ArrayList<SubjectBranch>();
   
        try
        {
          
            PreparedStatement st = con.prepareStatement("SELECT s.SubjectId,s.Subjectname,s.SubjectCode,b.DisplayName,b.SemorYear,sb.CurrentYearSem,sb.SubjectBranchId FROM SubjectBranchMaster sb,SubjectMaster s,BranchMaster b where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.BranchId=? and AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=?) and CurrentYearSem=? ");
            st.setString(1, BranchId);
            st.setString(2, BranchId);
            st.setInt(3, Semester);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
               SubjectBranch subjectBranch=new SubjectBranch();
               SubjectExam sub=new SubjectExam();               subjectBranch.setSubject(sub);
               subjectBranch.setSubjectBranchId(rs.getInt(7));
               sub.setSubjectId(rs.getInt(3));
               sub.setSubjectName(rs.getString(2));
               subjects.add(subjectBranch);
            }
           return subjects;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public int getTotalPassMarkForBLiSc(String BranchId,int YearSem,Connection con)
    {
         try
        {
            PreparedStatement st=con.prepareStatement("select sum(TotalMark) from SubjectBranchMaster where Branchid=? and CurrentYearSem=?");
            st.setString(1, BranchId);
            st.setInt(2, YearSem);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1)/2;
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }



    public int getSemTotalForStudent(String StudentId,String ExamId,int Sem,Connection con) throws SQLException
    {
         try
        {
            PreparedStatement st = con.prepareStatement("SELECT sum(s.InternalMark+s.ExternalMark+s.ModerationExt+s.ModerationInt) as Total FROM StudentSubjectMark s inner join SubjectBranchMaster m on m.SubjectBranchId=s.SubjectBranchId and CurrentYearSem=? and s.StudentId=? where m.IsAddedWithTotal=1 and s.ExamId=?");
            st.setInt(1, Sem);
            st.setString(2, StudentId);
            st.setString(3, ExamId);
            ResultSet Rs=st.executeQuery();
            while(Rs.next())
            {
                return Rs.getInt("Total");
            }
            return -1;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
    }


   public int UnconfirmedStudentsCount(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT IsConfirmed,count(*) FROM StudentExamFeeStatus"
               + " where ExamCentre=? and BranchId=? and AttendingYear=? and IsConfirmed=0");
       
       int Unconfirmed=0;
       statement.setInt(1, CollegeId);
       statement.setInt(2, BranchId);
       statement.setInt(3, AttendingYear);
       ResultSet rs=statement.executeQuery();
        if (rs.next()) {
            
            
                Unconfirmed=rs.getInt(2);
         
        }

       return Unconfirmed;
    }

      public int ConfirmedStudentsCount(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT IsConfirmed,count(*) FROM StudentExamFeeStatus"
               + " where ExamCentre=? and BranchId=? and AttendingYear=? and IsConfirmed=1 and examId=(SELECT max(ExamId) FROM ExamMaster E)");

       int confirmed=0;
       statement.setInt(1, CollegeId);
       statement.setInt(2, BranchId);
       statement.setInt(3, AttendingYear);
       ResultSet rs=statement.executeQuery();
        if (rs.next()) {
                confirmed=rs.getInt(2);
        }
       return confirmed;
    }
  public int RegisteredStudentsCount(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT IsConfirmed,count(*) FROM StudentExamFeeStatus"
               + " where ExamCentre=? and BranchId=? and AttendingYear=? and examId=(SELECT max(ExamId) FROM ExamMaster E)");
       int regCount=0;
       statement.setInt(1, CollegeId);
       statement.setInt(2, BranchId);
       statement.setInt(3, AttendingYear);
       ResultSet rs=statement.executeQuery();
        if (rs.next()) {
                regCount=rs.getInt(2);
        }
       return regCount;
    }

public ArrayList<Student> UnRegisteredStudents(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT s.`StudentId`, s.`StudentName`, s.`PRN` FROM StudentPersonal s "
               + "WHERE s.`IsMGUApproved`=1 AND s.`JoiningYear`=? AND s.`AttendingYear`=? AND s.`CollegeId`=? AND s.`BranchId`=? And StudentId "
               + "not in (SELECT StudentId FROM StudentExamFeeStatus where CollegeId=? and BranchId=? and examId=(SELECT max(ExamId) FROM ExamMaster E)) ");
       if(AttendingYear==1)
           statement.setInt(1, 2011);
       else
           statement.setInt(1, 2010);
       statement.setInt(2, AttendingYear);
       statement.setInt(3, CollegeId);
       statement.setInt(4, BranchId);
       statement.setInt(5, CollegeId);
       statement.setInt(6, BranchId);
       ResultSet rs=statement.executeQuery();
       ArrayList<Student> students=new ArrayList<Student>();
        while (rs.next()) {
                Student  student=new Student();
                student.setStudentId(rs.getInt(1));
                student.setName(rs.getString(2));
                student.setPRN(rs.getString(3));
               students.add(student);
        }
       return students;
    }

      public mStudentList(int  CentreId,int BranchId,int JoiningYear) throws SQLException
    {
          AdmissionYear Year=new AdmissionYear();
  
        try {
            con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("select l.StudentId,StudentName,QuestionId from StudentPersonal p,StudentLoginMaster l  where CollegeId=? and BranchId=? and p.StudentId=l.StudentId and p.JoiningYear=? order by StudentName ");
            ps.setInt(1, CentreId);
            ps.setInt(2, BranchId);
            ps.setInt(3, JoiningYear);
            ResultSet Data = ps.executeQuery();
            StudentData Student;
            while (Data.next()) {
                Student=new StudentData();
                Student.StudentId=Data.getInt(1);
                Student.Name=Data.getString(2);
                Student.ApplicationNo=Data.getInt(3);
                StudentId.add(Data.getString(1));
                StudentName.add(Data.getString(2));
                ApplicationNo.add(Data.getString(3));
                StudentList.add(Student);
            }

        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
    }


      
          public ArrayList<StudentData> getStudentsListForCourseInCentre(int  CentreId,int BranchId) throws SQLException
    {
          StudentList.clear();
        try {
            con = new DBConnection().getConnection();
            PreparedStatement ps = con.prepareStatement("select StudentId,StudentName,PRN from StudentPersonal p where CollegeId=? and BranchId=? and IsMguApproved=1  order by StudentName ");
            ps.setInt(1, CentreId);
            ps.setInt(2, BranchId);
            ResultSet Data = ps.executeQuery();
            StudentData Student;
            while (Data.next()) {
                Student=new StudentData();
                Student.StudentId=Data.getInt(1);
                Student.Name=Data.getString(2);
                Student.PRN=Data.getString(3);
                 StudentList.add(Student);
            }
            Data.close();

        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
        return StudentList;
    }
      public ArrayList<StudentData> getStudentList()
      {
          return StudentList;
      }
public ArrayList<StudentData> getStudentDetailsOfUser(String UserName) throws SQLException
    {

        try {
            con=new DBConnection().getConnection();
            String QueryStr = "select SP.StudentId ,StudentName,CollegeName,DisplayName,QuestionId from StudentPersonal SP,StudentLoginMaster LM,CollegeMaster CM ,BranchMaster BM where ";
            QueryStr += "SP.CollegeId in (select  CollegeId from CentreUserMap where UserName=? and EndDate is null)" + " and isFinalSubmit=1 and IsCenterVerified !=0 and SP.CollegeId=CM.CollegeId and BM.BranchId=SP.BranchId and LM.StudentId=SP.StudentId";
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, UserName);
        
            ResultSet Records = Query.executeQuery();
            ArrayList<StudentData> StudentList = new ArrayList<StudentData>();
            StudentData Student = null;
            while (Records.next()) {
                Student = new StudentData();
                Student.StudentId = Records.getInt(1);
                Student.Name = Records.getString(2);
                Student.Centre = Records.getString(3);
                Student.Course = Records.getString(4);
                Student.ApplicationNo = Records.getInt(5);
                StudentList.add(Student);
                
            }
            return StudentList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<CourseData> getBranchs(String Username) throws SQLException
    {

     ArrayList<CourseData> CourseList=new ArrayList<CourseData>();
        try {
            con=new DBConnection().getConnection();
            String QueryStr = "select Distinct DisplayName,C.BranchId from BranchMaster B,CollegeBranchMap C where B.BranchId=C.BranchId and C.CollegeId in (SELECT CollegeId FROM CentreUserMap where UserName=? and EndDate is null)";
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, Username);
            ResultSet Records = Query.executeQuery();
            CourseData Data = null;
            while (Records.next()) {
                Data = new CourseData();
                Data.BranchId = Records.getInt(2);
                Data.BranchName = Records.getString(1);
                CourseList.add(Data);
                
            }
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<CentreDetails> getCentres(String Username) throws SQLException
{
              ArrayList<CentreDetails> CentresList=new ArrayList<CentreDetails>();
              try {
                  con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select U.CollegeId,CollegeName from CentreUserMap U,CollegeMaster M  where  U.CollegeId=M.CollegeId and EndDate is null");
            Query.setString(1, Username);
            ResultSet Records = Query.executeQuery();
    
            CentreDetails Centre;
            while (Records.next()) {
                Centre=new CentreDetails();
                Centre.CentreId=Records.getInt(1);
                Centre.CentreName=Records.getString(2);
                CentresList.add(Centre);
            }
            con.close();
            return CentresList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex1);
            }
            finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
            return CentresList;
        }
}
 public void close() throws SQLException
    {
if(con!=null&&(!con.isClosed()))
{
    con.close();
}
}
  public int getCount()
    {
         return StudentId.size();
    }
    public String getStudentId(int index)
    {
         return StudentId.get(index);
    }
    public String getStudentName(int index)
    {
         return StudentName.get(index);
    }
    public String getApplication(int index)
    {
        return ApplicationNo.get(index);
    }
    public boolean  contains(String SId)
    {
        return StudentId.contains(SId);
    }
    public String getExamCentreName(String CentreId) throws SQLException
    {
         try
         {
                con=new DBConnection().getConnection();
                PreparedStatement st=con.prepareStatement("select e.ExamCentreName from ExamCentre2011 e inner join ExamCentreMap m on m.ExamCentreId=e.ExamCentreId and m.CentreId=? and m.ExamId=8");
                st.setString(1,CentreId);
                ResultSet rs=st.executeQuery();
                if(rs.next())
                {
                    return rs.getString("ExamCentreName");
                }
                return "";
         }
         catch (SQLException ex)
         {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return "";
         }
         finally
         {
             if(con!=null)
             {
                  con.close();
             }
         }
    }
    public ArrayList<Entity.CourseData> getSubBranch(String BranchId,String YearorSem) throws SQLException
    {
        ArrayList<Entity.CourseData> SubBranch=new ArrayList<Entity.CourseData>();
         Connection con =new DBConnection().getConnection();
         try
        {
        PreparedStatement st=con.prepareStatement("Select SubBranchId,SubBranchName from SubBranchMaster where BranchId=? and StartingSem <=? ");
        st.setString(1, BranchId);
        st.setString(2, YearorSem);

        ResultSet Rs=st.executeQuery();
        while(Rs.next())
        {
            Entity.CourseData SubBr=new Entity.CourseData();
            SubBr.BranchId=Rs.getInt(1);
            SubBr.BranchName=Rs.getString(2);
            SubBranch.add(SubBr);
        }
        return SubBranch;
         }
         catch(SQLException ex)
            {
                 Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
                con.close();
                return  null;
            }
            finally
            {
                con.close();
            }
    }
    public ArrayList<StudentData> getRejectedStudentsOfUser(String UserName) throws SQLException
    {
        try {
            con=new DBConnection().getConnection();
            String QueryStr = "select SP.StudentId ,StudentName,CollegeName,DisplayName,QuestionId from StudentPersonal SP,StudentLoginMaster LM,CollegeMaster CM ,BranchMaster BM where ";
            QueryStr += "SP.CollegeId in (select  CollegeId from CentreUserMap where UserName=? and EndDate is null)" + " and isFinalSubmit=1 and IsMGUApproved=-1 and SP.CollegeId=CM.CollegeId and BM.BranchId=SP.BranchId and LM.StudentId=SP.StudentId";
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, UserName);

            ResultSet Records = Query.executeQuery();
            ArrayList<StudentData> RejectedStudentList = new ArrayList<StudentData>();
            StudentData Student = null;
            while (Records.next()) {
                Student = new StudentData();
                Student.StudentId = Records.getInt(1);
                Student.Name = Records.getString(2);
                Student.Centre = Records.getString(3);
                Student.Course = Records.getString(4);
                Student.ApplicationNo = Records.getInt(5);
                RejectedStudentList.add(Student);
                System.out.println(Student.Name);
            }
            return RejectedStudentList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
    public ArrayList<StudentData> getApprovedStudentsOfUser(String UserName) throws SQLException
    {
        try {
            con=new DBConnection().getConnection();
            String QueryStr = "select SP.StudentId ,StudentName,CollegeName,DisplayName,QuestionId from StudentPersonal SP,StudentLoginMaster LM,CollegeMaster CM ,BranchMaster BM where ";
            QueryStr += "SP.CollegeId in (select  CollegeId from CentreUserMap where UserName=? and EndDate is null)" + " and isFinalSubmit=1 and IsMGUApproved=1 and SP.CollegeId=CM.CollegeId and BM.BranchId=SP.BranchId and LM.StudentId=SP.StudentId";
            PreparedStatement Query = con.prepareStatement(QueryStr);
            Query.setString(1, UserName);

            ResultSet Records = Query.executeQuery();
            ArrayList<StudentData> ApprovedStudentList = new ArrayList<StudentData>();
            StudentData Student = null;
            while (Records.next()) {
                Student = new StudentData();
                Student.StudentId = Records.getInt(1);
                Student.Name = Records.getString(2);
                Student.Centre = Records.getString(3);
                Student.Course = Records.getString(4);
                Student.ApplicationNo = Records.getInt(5);
                ApprovedStudentList.add(Student);
                System.out.println(Student.Name);
            }
            return ApprovedStudentList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
public ArrayList<StudentData> getStudentsOfCourse(int CourseId) throws SQLException
    {

    try {
        con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select StudentId,StudentName,PRN from StudentPersonal where JoiningYear= (SELECT max(JoiningYear) FROM StudentPersonal) and IsMGUApproved=1 and PRN is not null and BranchId=? order by CollegeId");
            Query.setInt(1, CourseId);
            ResultSet Records = Query.executeQuery();
          //  ArrayList<StudentData> StudentList=new ArrayList<StudentData>();
            StudentData student;
            while (Records.next()) {
                student = new StudentData();
                student.StudentId = Records.getInt(1);
                student.Name = Records.getString(2);
                student.PRN = Records.getString(3);
                StudentList.add(student);
            }
            return StudentList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return  null;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
   }
public int getCountOfApprovedStudentsWithoutPRNInCurrentYear(int CourseId) throws SQLException
    {
        try {
            con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select count(*) from StudentPersonal where JoiningYear= (SELECT max(JoiningYear) FROM StudentPersonal) and IsMGUApproved=1 and BranchId=?");
            Query.setInt(1, CourseId);
            ResultSet Records=Query.executeQuery();
            if(Records.next())
                return Records.getInt(1);
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
}
public int getUnconfirmedStudentCountInCurrentYear(int CourseId) throws SQLException
    {
        try {
            con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT count(*) FROM StudentPersonal where ISMGUApproved is null and IsCenterVerified =1 and BranchId= ? and JoiningYear = (select max(JoiningYear) from StudentPersonal)");
            Query.setInt(1, CourseId);
            ResultSet Records=Query.executeQuery();
            if(Records.next())
                return Records.getInt(1);
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
}
public int IsMguApproved(int StudentId) throws SQLException
{
        try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select IsMguApproved from StudentPersonal where StudentId=? ");
            st.setInt(1,StudentId);
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);

            }
        }
        //public int getCountOfApproved
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
        return 0;
}

 public int MguApprovedStudentsCount(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT IsConfirmed,count(*) FROM StudentExamFeeStatus"
               + " where CollegeId=? and BranchId=? and AttendingYear=? and IsMguApproved=1 and examId=(SELECT max(ExamId) FROM ExamMaster E)");

       int confirmed=0;
       statement.setInt(1, CollegeId);
       statement.setInt(2, BranchId);
       statement.setInt(3, AttendingYear);
       ResultSet rs=statement.executeQuery();
        if (rs.next()) {
                confirmed=rs.getInt(2);
        }
       return confirmed;
    }
public ArrayList<CentreDetails> getCentres() throws SQLException
{
              ArrayList<CentreDetails> CentresList=new ArrayList<CentreDetails>();
              try {
                  con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select c.CollegeId,CollegeName from CollegeMaster c where "
                    + "  c.CollegeId not in (select CollegeId from  CentreBlockDetails b where b.IsBlocked=1) order by CollegeName");

            ResultSet Records = Query.executeQuery();

            CentreDetails Centre;
            while (Records.next()) {
                Centre=new CentreDetails();
                Centre.CentreId=Records.getInt(1);
                Centre.CentreName=Records.getString(2);
                CentresList.add(Centre);
            }
            con.close();
            return CentresList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex1);
            }
            finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
            return CentresList;
        }
}
public ArrayList<ExamVenueDetails> getExamVenue() throws SQLException
{
              ArrayList<ExamVenueDetails> VenueList=new ArrayList<ExamVenueDetails>();
              try {
                  con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT ExamCentreId,ExamCentreName FROM `DEMS_db`.`ExamCentre2011`  where ExamId=8 order by ExamCentreName ");

            ResultSet Records = Query.executeQuery();

            ExamVenueDetails venue;
            while (Records.next()) {
                venue=new ExamVenueDetails();
                venue.ExamCentreId=Records.getInt(1);
                venue.ExamCentreName=Records.getString(2);
                VenueList.add(venue);
            }
            con.close();
            return VenueList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex1);
            }
            finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
            return VenueList;
        }
}
  public boolean IsCentreTransfered(int StudentId) throws SQLException
   {
       Connection con=null;

        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentCentreTransferMaster` where StudentId=?");
            st.setInt(1, StudentId);

            ResultSet Rs=st.executeQuery();
            if(Rs.next())
            {
                if(Rs.getInt(1)>0)
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
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
        public boolean IsDisContinued(int StudentId) throws SQLException
   {
       Connection con=null;

        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT count(*) FROM `DEMS_db`.`StudentPersonal` where IsDisContinue=1 and StudentId=?");
            st.setInt(1, StudentId);

            ResultSet Rs=st.executeQuery();
            if(Rs.next())
            {
                if(Rs.getInt(1)>0)
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
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
      public  ArrayList<CourseData> getAllBranchs() throws SQLException
    {
     ArrayList<CourseData> CourseList=new ArrayList<CourseData>();
        try {
            con=new DBConnection().getConnection();

            PreparedStatement Query = con.prepareStatement("select Distinct DisplayName,BranchId from BranchMaster");

            ResultSet Records = Query.executeQuery();
            CourseData Data ;
            while (Records.next()) {
                Data = new CourseData();
                Data.BranchId = Records.getInt(2);
                Data.BranchName = Records.getString(1);
                CourseList.add(Data);
            }
            con.close();
          return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex1);
            }
            finally
        {
             if(con!=null)
             {
                  con.close();
             }
        }
         return CourseList;
        }

    }

public boolean isAlredaySubjectAdded(ArrayList<SubjectBranch> Subjectlist, SubjectBranch subject)
{
    boolean checked = false;
    for (int j = 0; j < Subjectlist.size(); j++) {
        SubjectBranch subjBranch ;
        subjBranch = Subjectlist.get(j);


        if (subjBranch.getSubjectBranchId() != 0 && subjBranch.getSubjectBranchId() == subject.getSubjectBranchId()) {
            checked = true;
        }
    }
    return checked;

}


public boolean isAlredayPassed(SubjectBranch subject,int StudentId ) throws SQLException
{
    boolean isPassed = false;
    
     try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM StudentSubjectMark S  WHERE S.`StudentId`=? and SubjectBranchId=? and S.`IsPassed`=1 and S.Isvalid=1 AND S.`MarkStatus`=1 ");
            st.setInt(1,StudentId);
            st.setInt(2,subject.getSubjectBranchId());
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
              isPassed=true;  

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
             return isPassed;
        }
        
    
   
    

}
 public ArrayList<SubjectBranch> getRegisteredPapers(int StudentId,int BranchId,Connection connection,int Year,String ExamId)
  {
       try {
    int SemOrYear=0;
            int examId=4;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
            ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
             PreparedStatement st = connection.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? ");
             st.setInt(1, BranchId);
              rs = st.executeQuery();
              if (rs.next())
                  {
                      SemOrYear=rs.getInt(1);
                  }
              String Query="";

                Query="SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName ,s.SubBranchId  FROM ExamRegistrationMaster e inner join SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId` inner join SubjectMaster sm on sm.SubjectId=s.SubjectId  WHERE e.`StudentId` =? AND e.`ExamId` =? and s.SubjectBranchId in ("+getSubjectBranchIds(BranchId, connection, SemOrYear,Year) +") order by s.PartNo,s.PaperNo ";

              ps=connection.prepareStatement(Query);
                  ps.setInt(1, StudentId);
                  ps.setString(2, ExamId);
                 
                    ResultSet SubjectsDate = ps.executeQuery();
                    while (SubjectsDate.next())
                    {
                        SubjectBranch sb = new SubjectBranch();
                        SubjectExam subject = new SubjectExam();
                        sb.setSubject(subject);
                        sb.setSemOrYear(SubjectsDate.getInt(1));
                        sb.setSubjectBranchId(SubjectsDate.getInt(2));
                        subject.setSubjectId(SubjectsDate.getInt(3));
                        subject.setSubjectName(SubjectsDate.getString(4));
                        sb.setSubBranchId(SubjectsDate.getInt(5));
                        subjectBranchs.add(sb);
                    }
              return subjectBranchs;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
  }
//public int getCountOfApproved
 public ArrayList<SubjectBranch> getRegisteredPapers_CourseCompleted(int StudentId,int BranchId,Connection connection,int Year)
  {
       try {
    int SemOrYear=0;
            int examId=4;
            PreparedStatement ps = connection.prepareStatement("SELECT max(ExamId) FROM ExamMaster E");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                examId=rs.getInt(1);
            rs.close();
            ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
             PreparedStatement st = connection.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? ");
             st.setInt(1, BranchId);
              rs = st.executeQuery();
              if (rs.next())
                  {
                      SemOrYear=rs.getInt(1);
                  }
              String Query="";

                Query="SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName ,s.SubBranchId,e.ExamType  FROM ExamRegistrationMaster e inner join SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId` inner join SubjectMaster sm on sm.SubjectId=s.SubjectId  WHERE e.`StudentId` =? AND e.`ExamId` =?  order by s.PartNo,s.PaperNo ";

              ps=connection.prepareStatement(Query);
                  ps.setInt(1, StudentId);
                  ps.setInt(2, examId);

                    ResultSet SubjectsDate = ps.executeQuery();
                    while (SubjectsDate.next())
                    {
                        SubjectBranch sb = new SubjectBranch();
                        SubjectExam subject = new SubjectExam();
                        sb.setSubject(subject);
                        sb.setSemOrYear(SubjectsDate.getInt(1));
                        sb.setSubjectBranchId(SubjectsDate.getInt(2));
                        subject.setSubjectId(SubjectsDate.getInt(3));
                        subject.setSubjectName(SubjectsDate.getString(4));
                        sb.setSubBranchId(SubjectsDate.getInt(5));
                        subject.setPaperType(SubjectsDate.getString(6));
                        subjectBranchs.add(sb);
                    }
              return subjectBranchs;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
  }

 // For Course Completed Students
 public ArrayList<SubjectBranch> getSupplyOrImprovementPapersForCourseCompleted(int StudentId,int BranchId) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        try {
            
                    PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc ) a limit 1,5 ");
                    ResultSet rs = ps.executeQuery();

                 while (rs.next()) {
                int ExamId = rs.getInt(1);
                //first pick supply subjects
                
                ps = connection.prepareStatement("SELECT  S.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM StudentSubjectMark S inner join SubjectBranchMaster sb on S.`SubjectBranchId`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE S.`StudentId`=? and (S.`IsAbsent`=1 or  S.`IsPassed`=0) AND S.MarkStatus=1 and ExamId=? and S.IsValid=1");
                ps.setInt(1, StudentId);
                ps.setInt(2, ExamId);
               // System.out.println(ps);
                ResultSet SubjectsDate = ps.executeQuery();
                
                while (SubjectsDate.next()) {
                   
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                }
              

            }
                
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }
        
    }
 //get CBCSS course completed supply students
  public ArrayList<SubjectBranch> getSupplyForCourseCompleted(int StudentId,int BranchId) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        try {
            
                  
                PreparedStatement  ps=connection.prepareStatement("SELECT  S.`stud_paper_id`, sm.`SubjectId`, sm.`SubjectName` ,S.theory_ext_grade_point,S.theory_int_grade_point,S.practical_ext_grade_point,S.practical_int_grade_point  FROM DEMS_CAMP.exam_papers_consolidated_all S inner join SubjectBranchMaster sb on S.`stud_paper_id`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE  S.`student_id`=? and  S.`Result`='F'");
                 //PreparedStatement ps =connection.prepareStatement("SELECT  S.`subject_branch_id`, sm.`SubjectId`, sm.`SubjectName`,S.theory_ext_grade,S.theory_int_grade,S.practical_ext_grade,S.practical_int_grade FROM DEMS_CAMP.exam_papers_lowersemesters S inner join SubjectBranchMaster sb on S.`subject_branch_id`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE  S.`student_id`=? and (S.`present_absent`='A' or  S.`course_result`='F') and S.`subject_branch_id` not in  (select f.`subject_branch_id` from  DEMS_CAMP.exam_papers_lowersemesters f where   f.`course_result`='P' and f.`student_id`=?)  group by S.`subject_branch_id`" );
                 ps.setInt(1, StudentId);
                // ps.setInt(2, StudentId);
                 ResultSet SubjectsDate = ps.executeQuery();
               
                 while (SubjectsDate.next()) {
                    String supplyType="N";//For Bcom CBCSS
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    int subjectBranchId=SubjectsDate.getInt(1);
                         if(subjectBranchId==803 || subjectBranchId==812 || subjectBranchId==958 || subjectBranchId==963 ){
                        float try_ext=0;
                        float try_int=0;
                        float pract_ext=0;
                        float pract_int=0;
                         if(SubjectsDate.getString(4)!=null){
                         
                        try_ext=Float.valueOf(SubjectsDate.getString(4));}
                         
                         if(SubjectsDate.getString(5)!=null){
                        try_int=Float.valueOf(SubjectsDate.getString(5));}
                         
                         if(SubjectsDate.getString(6)!=null){
                         pract_ext=Float.valueOf(SubjectsDate.getString(6));}
                         if(SubjectsDate.getString(7)!=null){
                        pract_int=Float.valueOf(SubjectsDate.getString(7));}
                       
                        if(((try_ext<0.50) ||( try_int<0.50))&&((pract_ext<0.50) ||(pract_int<0.50) )){
                            supplyType="B";
                         }else if((try_ext<0.50) ||( try_int<0.50)){
                            supplyType="T";
                         }else if((pract_ext<0.50) ||(pract_int<0.50) ) {
                            supplyType="P"; 
                         }
                         }
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
                    subject.setPaperType(supplyType);
                    sb.setSubject(subject); 
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                }
              

            }
           
         catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }
        
    }
  //Get CBCSS course completed students with gade point 'D'--- course faild students 
  public ArrayList<SubjectBranch> getSupplyOrImprovementPapersForCourseCompletedCBCSS(int StudentId,int BranchId) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        try {
            
                PreparedStatement  ps=connection.prepareStatement("SELECT bm.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM DEMS_CAMP.programe_result_final e inner join dems_db.StudentPersonal p on e.studentid=p.studentid inner join SubjectBranchMaster bm   on bm.branchid=p.branchid inner join SubjectMaster sm on bm.SubjectId=sm.SubjectId where  coursecompletedStatus=1  and bm.hasPractical!=1 and CGPA_grade='D' and P.studentid=?; ");
                    ps.setInt(1, StudentId);
                   
                    
               
                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                   
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
                    
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                }
              
      
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }
        
    }
  
  public ArrayList<SubjectBranch> getCBCSSCourseCompleredPapersForImprovemntSemesterwise(int StudentId,int BranchId,int sem) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        try {
            
              PreparedStatement    ps=connection.prepareStatement("SELECT bm.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName`,bm.CurrentYearSem " +
                                        " FROM DEMS_CAMP.programme_result_consolidated_forsemesterimprove e " +
                                        " inner join DEMS_db.StudentPersonal p " +
                                        "  on e.student_id=p.studentid inner join SubjectBranchMaster bm " +
                                        "   on bm.branchid=p.branchid inner join SubjectMaster sm on bm.SubjectId=sm.SubjectId " +
                                        "inner join " +
                                        "    DEMS_CAMP.semester_result_consolidated_final r on e.student_id=r.student_id and e.branchId=r.branchId " +
                                        "and r.semester=bm.CurrentYearSem " +
                       " inner join DEMS_CAMP.exam_papers_consolidated_final f on f.student_id=e.student_id " +
                      " and f.stud_paper_id=bm.SubjectBranchId " +
                                        "   where " +
                                        "bm.hasPractical!=1 and  e.Result='F' and p.CourseCompletedStatus=1 " +
                                        "and r.Result='P' and e.student_id=? and bm.CurrentYearSem=? ");
       
               ps.setInt(1, StudentId);
               ps.setInt(2, sem);
    
                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                   
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
                    subject.setCurrentYearSem(SubjectsDate.getInt(4));
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));                   
                    if(!isAlredaySubjectAdded(subjectBranchs,sb) && !isAlredayPassed(sb, StudentId))
                         subjectBranchs.add(sb);
                }
              
                    
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }
        
    }
   public boolean UpdateExamRemarks(String Remarks,int StudentId) throws SQLException
  {
      Connection con = null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("Update StudentExamFeeStatus set Remarks=? where StudentId=? and ExamId=8");
            st.setString(1, Remarks);
            st.setInt(2, StudentId);
            st.execute();
            
            return true;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
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
   
      public String getExamRemarks(int StudentId) throws SQLException {
         Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select Remarks from StudentExamFeeStatus where StudentId=? and ExamId=8");
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
 
      
      public ArrayList<SubjectBranch> getImprovementPapers(int StudentId,int BranchId,int yearApplied) throws SQLException
    {
        ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        boolean isReadmission=false;
        boolean isRegistered=false;// ReAdmssion
        int readmtdYear = 0;int notionalYear=0;
       
            
            
                 //Improvement Option Bcom
            if (BranchId == 7 || BranchId == 8) {
                    subjectBranchs.addAll(getBcomPassedPart1And2Papers(StudentId, BranchId, connection,yearApplied));
                }
            if (BranchId == 24 || BranchId == 25 ||  BranchId == 27|| BranchId == 28|| BranchId == 29|| BranchId == 30 || BranchId == 31 || BranchId == 32) {
                    subjectBranchs.addAll(getCBCSSPassedPapersForImprovemnt(StudentId, BranchId, connection,yearApplied));
                }
           connection.close();
           return subjectBranchs;
        
    }
       public ArrayList<SubjectBranch> getCBCSSPassedPapersForImprovemnt(int StudentId,int BranchId,Connection connection,int currentYear) 
    {   ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
        int examId=0;
        try {
            PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc ) a limit 1,1 ");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                examId = result.getInt(1);}
        
            PreparedStatement statement=connection.prepareStatement("SELECT  S.`stud_paper_id`, sm.`SubjectId`, sm.`SubjectName`,S.`student_id` FROM DEMS_CAMP.exam_papers_consolidated_all S inner join SubjectBranchMaster sb on S.`stud_paper_id`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE  S.`student_id`=? and Exam_Id=? and  S.`Result`='P' ");
                        
            statement.setInt(1, StudentId);  
            statement.setInt(2, examId);
            ResultSet rs=statement.executeQuery();
           
             while (rs.next()) {
             SubjectExam subject = new SubjectExam();
             SubjectBranch subjectBranch=new SubjectBranch();
             
             subject.setSubjectId(rs.getInt(2));
             subject.setSubjectName(rs.getString(3));
             subjectBranch.setSubject(subject);
             subjectBranch.setSubjectBranchId(rs.getInt(1));
             list.add(subjectBranch);
                }
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }finally{return list;}
    }
     
     

      public boolean isAlredayPassedForCBCSS(SubjectBranch subject,int StudentId ) throws SQLException
{
    boolean isPassed = false;
    
     try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(" SELECT  * FROM exam_papers S  WHERE  S.`student_id`=? and S.`subject_branch_id`=? and  S.`present_absent`='P' and  S.`course_result`='P' ");
                 
            st.setInt(1,StudentId);
            st.setInt(2,subject.getSubjectBranchId());
            ResultSet rs=st.executeQuery();

            if(rs.next())
            {
              isPassed=true;  

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
             return isPassed;
        }
        
    
   
    

}
      
      
      public ArrayList<SubjectBranch> getCBCSSPassedPapersForImprovemntSemesterwise(int StudentId,int BranchId,int currentSem) throws SQLException 
    {   ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        int examId=0;
        try {
            PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc ) a limit 1,1 ");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                examId = result.getInt(1);}
        
            PreparedStatement statement=connection.prepareStatement("SELECT  S.`stud_paper_id`, sm.`SubjectId`, sm.`SubjectName`,S.`student_id` FROM DEMS_CAMP.exam_papers_consolidated_all S inner join SubjectBranchMaster sb on S.`stud_paper_id`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  inner join ExamRegistrationMaster e on  sb.`SubjectBranchId`=e.`SubjectBranchId`  and e.studentid=? and e.ExamId=?  WHERE  S.`student_id`=? and Exam_Id=? and   S.`Result`='P' and S.Semester=? and e.RegistrationType=0 ");
                        
             statement.setInt(1, StudentId);  
            statement.setInt(2, examId);
            statement.setInt(3, StudentId);  
            statement.setInt(4, examId);
            statement.setInt(5, currentSem);
            ResultSet rs=statement.executeQuery();
            
           
           
             while (rs.next()) {
             SubjectExam subject = new SubjectExam();
             SubjectBranch subjectBranch=new SubjectBranch();
             
             subject.setSubjectId(rs.getInt(2));
             subject.setSubjectName(rs.getString(3));
             subjectBranch.setSubject(subject);
             subjectBranch.setSubjectBranchId(rs.getInt(1));
             list.add(subjectBranch);
                }
             connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(mStudentList.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(connection!=null){ connection.close();}
            return list;}
    }
public static  void main(String s[]) throws SQLException
    {
       // ArrayList<Student> supplyOrImprovementPapers =
                new mStudentList().getSupplyOrImprovementPapers(219, 7,2010,3);
                Connection con = new DBConnection().getConnection();
                new mStudentList().getRegisteredOptionalPapers(8093, 7, con, 1);
    }
}