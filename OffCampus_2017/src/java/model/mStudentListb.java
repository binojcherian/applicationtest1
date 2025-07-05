/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;


import Entity.*;
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
public class mStudentListb {
Connection con=null;
 private ArrayList<String> StudentId=new ArrayList<String>();
    private ArrayList<String> StudentName=new ArrayList<String>();
    private ArrayList<String> ApplicationNo=new ArrayList<String>();
    private ArrayList<StudentData> StudentList=new ArrayList<StudentData>();
 
    public mStudentListb()
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     finally
        {
            if(con!=null)
                con.close();
        }
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
             ps=connection.prepareStatement("SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName FROM ExamRegistrationMaster e inner join"
                     + " SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId` inner join SubjectMaster sm on sm.SubjectId=s.SubjectId"
                     + " WHERE e.`StudentId` =? AND e.`ExamId` =?  and CurrentYearSem <= ? order by CurrentYearSem ");
                  ps.setInt(1, StudentId);
                  ps.setInt(2, examId);
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                  ps.setInt(3, 2);
                 } else {
                     ps.setInt(3, 1);
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
                    subjectBranchs.add(sb);
                }
              return subjectBranchs;
            }
            else
                return new ArrayList<SubjectBranch>();
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
              if((BranchId==21 && Year==2) || (BranchId==17 && Year==2) )
              {
                  Query="SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName, s.SubBranchId FROM ExamRegistrationMaster e inner join SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId`inner join SubjectMaster sm on sm.SubjectId=s.SubjectId  WHERE e.`StudentId` =? AND e.`ExamId` =? and s.SubjectBranchId in ("+getSubjectBranchIds(BranchId, connection, SemOrYear,Year) +") ";
              }
              else
              {
                Query="SELECT  CurrentYearSem, s.`SubjectBranchId`, s.`SubjectId`,sm.SubjectName ,s.SubBranchId  FROM ExamRegistrationMaster e inner join SubjectBranchMaster s on  e.`SubjectBranchId`= s.`SubjectBranchId`and s.IsElective=1 inner join SubjectMaster sm on sm.SubjectId=s.SubjectId  WHERE e.`StudentId` =? AND e.`ExamId` =? and s.SubjectBranchId in ("+getSubjectBranchIds(BranchId, connection, SemOrYear,Year) +") ";
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }
  public ArrayList<SubjectBranch> getRegisteredPapers(int StudentId,int BranchId,Connection connection,int Year)
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
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
            }
            else if(SemOrYear==0&&AttendingYear==1)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and (CurrentYearSem=1 or  CurrentYearSem=2 )";
            }else if(SemOrYear==0&&AttendingYear==2)
            {
                Query="select SubjectBranchId from SubjectBranchMaster where BranchId=? and ( CurrentYearSem=3 or CurrentYearSem=4 )";
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
    }
    
    public ArrayList<SubjectBranch> getSupplyOrImprovementPapers(int StudentId,int BranchId) throws SQLException
    {
         ArrayList<SubjectBranch> subjectBranchs = new ArrayList<SubjectBranch>();
        Connection connection = new DBConnection().getConnection();
        try {
            PreparedStatement st=connection.prepareStatement("select count(*) from ReAdmission where StudentId=? and AttendingYear=2 and JoiningYear=2011" );
            st.setInt(1, StudentId);
            ResultSet re=st.executeQuery();
            while(re.next())
            {
                if(re.getInt(1)>0)
                {
                    if(BranchId==7 || BranchId==8)
                    {
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and CurrentYearSem=1");
                    }
                    else
                    {
                        st=connection.prepareStatement("SELECT  sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM  SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE sb.`BranchId`=? and (CurrentYearSem=1 or CurrentYearSem=2)");
                    }
                    st.setInt(1, BranchId);
                    ResultSet result=st.executeQuery();
                    while(result.next())
                    {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(result.getInt(1));
                    subject.setSubjectId(result.getInt(2));
                    subject.setSubjectName(result.getString(3));
                    subjectBranchs.add(sb);
                    }
                }
                else
                {
                    PreparedStatement ps = connection.prepareStatement("Select ExamId from (SELECT * FROM ExamMaster E order by   AcadamicYear desc ,StartDate desc limit 2) a limit 1,1 ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int ExamId = rs.getInt(1);
                //first pick supply subjects
                ps = connection.prepareStatement("SELECT  S.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` FROM StudentSubjectMark S inner join SubjectBranchMaster sb on S.`SubjectBranchId`=sb.`SubjectBranchId`  inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId  WHERE S.`StudentId`=? and (S.`IsAbsent`=1 or  S.`IsPassed`=0) and ExamId=? and S.IsValid=1");
                ps.setInt(1, StudentId);
                ps.setInt(2, ExamId);
                ResultSet SubjectsDate = ps.executeQuery();
                while (SubjectsDate.next()) {
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(SubjectsDate.getInt(1));
                    subject.setSubjectId(SubjectsDate.getInt(2));
                    subject.setSubjectName(SubjectsDate.getString(3));
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
                if (BranchId == 7 || BranchId == 8) {
                    subjectBranchs.addAll(getBcomPassedPart1And2Papers(StudentId, BranchId, connection));
                }

            }
                }
            }



        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);

        }
        finally
        {
            connection.close();
            return subjectBranchs;
        }

    }

    public ArrayList<SubjectBranch> getBcomPassedPart1And2Papers(int StudentId,int BranchId,Connection connection) throws SQLException
    {
        PreparedStatement statement=connection.prepareStatement("SELECT sb.`SubjectBranchId`, sm.`SubjectId`, sm.`SubjectName` ,ss.StudentId "
                + "FROM SubjectBranchMaster sb inner join SubjectMaster sm on sb.SubjectId=sm.SubjectId inner join StudentSubjectMark ss on "
                + "sb.`SubjectBranchId`=ss.`SubjectBranchId` and Ispassed=1  WHERE sb.`BranchId` =? AND sb.`PartNo`!='II' AND CurrentYearSem=1 And ss.StudentId=? and ss.IsValid=1");
            statement.setInt(1, BranchId);
            statement.setInt(2, StudentId);
            ResultSet rs=statement.executeQuery();
            ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
             while (rs.next()) {
             SubjectExam subject=new SubjectExam();
             SubjectBranch subjectBranch=new SubjectBranch();
             subjectBranch.setSubject(subject);
             subjectBranch.setSubjectBranchId(rs.getInt(1));
             subject.setSubjectId(rs.getInt(2));
             subject.setSubjectName(rs.getString(3));
             list.add(subjectBranch);
                }
            return list;
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
              sb.setSubject(subject);
              sb.setSubjectBranchId(SubResultSet.getInt(1));
              subject.setSubjectId(SubResultSet.getInt(2));
              subject.setSubjectName(SubResultSet.getString(3));
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
               SubjectExam sub=new SubjectExam();
               subjectBranch.setSubject(sub);
               subjectBranch.setSubjectBranchId(rs.getInt(7));
               sub.setSubjectId(rs.getInt(3));
               sub.setSubjectName(rs.getString(2));
               subjects.add(subjectBranch);
            }
           return subjects;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
    }


   public int UnconfirmedStudentsCount(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT IsConfirmed,count(*) FROM StudentExamFeeStatus"
               + " where CollegeId=? and BranchId=? and AttendingYear=? and IsConfirmed=0");
       
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
               + " where CollegeId=? and BranchId=? and AttendingYear=? and IsConfirmed=1");

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
       public int MguApprovedStudentsCount(int BranchId,int CollegeId,int AttendingYear, Connection connection) throws SQLException
    {
       PreparedStatement statement=connection.prepareStatement("SELECT IsConfirmed,count(*) FROM StudentExamFeeStatus"
               + " where CollegeId=? and BranchId=? and AttendingYear=? and IsMguApproved=1");

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
               + " where CollegeId=? and BranchId=? and AttendingYear=? ");
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
               + "not in (SELECT StudentId FROM StudentExamFeeStatus where CollegeId=? and BranchId=?) ");
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

      public mStudentListb(int  CentreId,int BranchId,int JoiningYear) throws SQLException
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
                System.out.println(Student.Name);
            }
            return StudentList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            PreparedStatement Query = con.prepareStatement("select U.CollegeId,CollegeName from CentreUserMap U,CollegeMaster M  where UserName=? and U.CollegeId=M.CollegeId and EndDate is null");
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex1);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
                System.out.println(Data.BranchName);
            }
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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


public ArrayList<CentreDetails> getCentres() throws SQLException
{
              ArrayList<CentreDetails> CentresList=new ArrayList<CentreDetails>();
              try {
                  con=new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("select CollegeId,CollegeName from CollegeMaster order by CollegeName ");

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
                Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex1);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex1);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(mStudentListb.class.getName()).log(Level.SEVERE, null, ex);
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
//public int getCountOfApproved

public static  void main(String s[]) throws SQLException
    {
       // ArrayList<Student> supplyOrImprovementPapers =
                new mStudentListb().getSupplyOrImprovementPapers(6629, 7);
                Connection con = new DBConnection().getConnection();
                new mStudentListb().getRegisteredOptionalPapers(8093, 7, con, 1);
    }
}
