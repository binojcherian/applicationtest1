/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.*;
import Entity.*;
import java.io.IOException;

public class MarkVerification
{
    HttpServletRequest request;
    HttpServletResponse response;
    String UserName=null;
    String BranchId="-1",StudentSubjectMarkId="",Sem="-1",BranchName="",InternalEroor=null,ExamName="",StudentName="",SubjectName="",ExamId="-1",SubjectId="-1",SemError=null,Id=null,AcdemicYear="-1",AcdemicYearError=null,SubjectBranchId="-1",Error=null,ExamError=null,Query=null,PRN=null,Assistant="-1",BranchError=null,SubjectError=null,PRNError=null,PracticalError=null,InternalError=null;
    boolean IsVerified=false,Search=false;
    
    Double external= -1.0,internal= -1.0,Practical= -1.0;
    MarkEntry_old mark=new MarkEntry_old();
    ArrayList<StudentSubjectMark> Studentmark;
    Absentees absent=new Absentees();
    private String ExternalError=null;

    public MarkVerification(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException
    {
        this.request=request;
        this.response=response;
    if(request.getParameter("Exam")!=null)
    {
        ExamId=request.getParameter("Exam");
          request.getSession().setAttribute("ExamId", ExamId);
    }
     if(request.getParameter("AcdemicYear")!=null)
    {
        AcdemicYear=request.getParameter("AcdemicYear");
        request.getSession().setAttribute("AcdemicYear", AcdemicYear);
    }
         if(request.getParameter("Course")!=null)
    {
        BranchId=request.getParameter("Course");
        request.getSession().setAttribute("BranchId", BranchId);
    }
    if(request.getParameter("Subject")!=null)
    {
        SubjectBranchId=request.getParameter("Subject");
        SubjectId=request.getParameter("Subject");
        request.getSession().setAttribute("SubjectBranchId", SubjectBranchId);
    }
        if(request.getParameter("Assistant")!=null)
        {
            Assistant=request.getParameter("Assistant");
            request.getSession().setAttribute("Assistant", Assistant);
        }
    if(request.getParameter("PRN")!=null)
    {
        PRN=request.getParameter("PRN");
        request.getSession().setAttribute("PRN", PRN);
    }
    if(request.getParameter("YearSem")!=null  )
    {
        Sem=request.getParameter("YearSem");
         request.getSession().setAttribute("YearSem", Sem);
    }
    if(request.getParameter("external")!=null && (!request.getParameter("external").isEmpty()))
    {
        external=Double.parseDouble(request.getParameter("external"));
    }
        if(request.getParameter("internal")!=null && (!request.getParameter("internal").isEmpty()))
    {
        internal=Double.parseDouble(request.getParameter("internal"));
    }
            if(request.getParameter("practical")!=null && (!request.getParameter("practical").isEmpty()))
    {
        Practical=Double.parseDouble(request.getParameter("practical"));
    }
         if(request.getParameter("iPageNo")!=null && (!request.getParameter("iPageNo").equals("0")))
    {
        Search=true;
        request.getSession().setAttribute("iPageNo",request.getParameter("iPageNo"));
               if(request.getParameter("cPageNo")!=null)
               {
                   request.getSession().setAttribute("cPageNo", request.getParameter("cPageNo"));
               }
                if(request.getSession().getAttribute("BranchId")!=null)
                {
                    BranchId=request.getSession().getAttribute("BranchId").toString();
                }
                if(request.getSession().getAttribute("SubjectBranchId")!=null)
                {
                    SubjectId=request.getSession().getAttribute("SubjectBranchId").toString();
                    SubjectBranchId=request.getSession().getAttribute("SubjectBranchId").toString();
                }
            if(request.getSession().getAttribute("ExamId")!=null)
                {
                    ExamId=request.getSession().getAttribute("ExamId").toString();
                }
            if(request.getSession().getAttribute("AcdemicYear")!=null)
                {
                    AcdemicYear=request.getSession().getAttribute("AcdemicYear").toString();
                }
            if(request.getSession().getAttribute("YearSem")!=null)
                {
                    Sem=request.getSession().getAttribute("YearSem").toString();
                }
                if(request.getSession().getAttribute("PRN")!=null)
                {
                    PRN=request.getSession().getAttribute("PRN").toString();
                }
        if(request.getSession().getAttribute("Assistant")!=null)
                {
                    Assistant=request.getSession().getAttribute("Assistant").toString();
                }
    }
         if(request.getParameter("Search")!=null && request.getParameter("Search").equals("Search"))
        {
            Search=true;
        }
        if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("ForReValuation"))
        {
            Studentmark=new ArrayList<StudentSubjectMark>();
            StudentSubjectMark Student;
            if(request.getParameterValues("Select")!=null)
                {
                String Stu[]=request.getParameterValues("Select");
                 for (String SId : Stu)
                 {
                    Student=new StudentSubjectMark();                   
                    Student=mark.getRecordsforCorrectionAdminbyId(SId);
                    Studentmark.add(Student);
                 }
                 if(mark.InsertStudentsOldMark_Revaluation(Studentmark))
                 { if(mark.forRevaluationStudentsMark(Studentmark))
                 {                 
                         IsVerified=true;  
                         Error="For Revaluation done Succssfully";
                   }
                 }
            }
            Search=true;
        }
         if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("ForPostCorrection"))
        {
            Studentmark=new ArrayList<StudentSubjectMark>();
            StudentSubjectMark Student;
            if(request.getParameterValues("Select")!=null)
                {
                String Stu[]=request.getParameterValues("Select");
                 for (String SId : Stu)
                 {
                    Student=new StudentSubjectMark();
                    Student=mark.getRecordsforCorrectionAdminbyId(SId);
                    Studentmark.add(Student);
                 }
                 if(mark.InsertStudentsOldMark_PostCorrection(Studentmark))
                 { if(mark.forPostCorrectionStudentsMark(Studentmark))
                 {
                         IsVerified=true;
                         Error="For PostCorrection done Succssfully";
                   }
                 }
            }
            Search=true;
        }
       if(request.getParameter("SubmitEdit")!=null && request.getParameter("SubmitEdit").equals("Submit"))
       {
           Search=true;
       }
        if(request.getParameter("StudentSubjectMarkId")!=null && (!request.getParameter("StudentSubjectMarkId").isEmpty()))
       {
           StudentSubjectMark Student;
           String markId=request.getParameter("StudentSubjectMarkId");
           Student=new StudentSubjectMark();
           Student=mark.getSelectedStudentMarkWithMarkId(markId);
           ExamId=Student.ExamId;
           BranchId=Student.BranchId;
           Sem=Student.Sem;
           AcdemicYear=Student.AcdemicYear;
           StudentSubjectMarkId=Student.StudentSubjectMarkId;
           SubjectBranchId=Student.SubjectBranchId;
           PRN=Student.PRN;
           external=Double.parseDouble(Student.ExternalMark);
           internal=Double.parseDouble(Student.InternalMark);
           Practical=Double.parseDouble(Student.PracticalMark);
           SubjectId=Student.SubjectId;
           StudentName=Student.StudentName;
           SubjectName=Student.SubjectName;
           BranchName=mark.getBranchNameFromBranchId(Student.BranchId);
           ExamName=mark.getExamNameFromExamId(Student.ExamId);
           Search=true;
       }

        if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Approve"))
        {

             if(request.getParameter("Select")!=null)
                {
                    external=0.0;
                    internal=0.0;
                    Practical=0.0;
                    Studentmark=new ArrayList<StudentSubjectMark>();
                    StudentSubjectMark Student,Stud;
                    String Stu=request.getParameter("Select");
                    String markId=request.getParameter("StudentSubjectMarkId");
                    Student=new StudentSubjectMark();
                    Student=mark.getSelectedStudentMarkWithMarkId(markId);
                    String studList[]=Stu.split(",");

                    if(request.getParameter("Mark")!=null && (!request.getParameter("Mark").equals("")))
                    {
                    external=Double.parseDouble(request.getParameter("Mark"));
                    }

                    if(request.getParameter("InternalMark")!=null && (!request.getParameter("InternalMark").equals("")))
                    {
                    internal=Double.parseDouble(request.getParameter("InternalMark"));
                    }
                   if(request.getParameter("PracticalMark")!=null && (!request.getParameter("PracticalMark").equals("")))
                    {
                    Practical=Double.parseDouble(request.getParameter("PracticalMark"));
                    }
                    ExternalError=getExternalMarkError();
                    if(ExternalError==null)
                    {
                        InternalEroor=getInternalMarkError();
                      if(InternalEroor==null)
                     {

                        if((external==Double.parseDouble(Student.ExternalMark))&& (internal==Double.parseDouble(Student.InternalMark)))
                       {
                            for (String SId : studList)
                            {
                             Student=new StudentSubjectMark();
                              Student.StudentSubjectMarkId=SId;
                              Studentmark.add(Student);
                          }
                          if(mark.ApproveStudentsMark(Studentmark))
                          {
                              if(mark.SaveMarkApprovalLogForSO(Studentmark, request))
                              {
                                   IsVerified=true;
                                   Error="Mark Verification done Succssfully";
                                   //response.sendRedirect("MarkVerificationSO.jsp");
                               }
                          }
                      }
                         else
                         {
                            Error="Wrong Mark is entered , Please check the Marks";
                         IsVerified=false;
                         }
                    }
                  }

               }
              Search=true;
        }
        if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Cancel"))
        {
            //response.sendRedirect("MarkVerificationSO.jsp");
        }

    }
    public String getiPageNo()
    {
        if(request.getParameter("AV")==null)
        {
           return request.getParameter("iPageNo");
        }
        else
        {
             if(request.getSession().getAttribute("iPageNo")!=null)
             {
                 return request.getSession().getAttribute("iPageNo").toString();
             }
             return  null;
        }
    }
    public String getcPageNo()
    {
        if(request.getParameter("AV")==null)
        {
           return request.getParameter("cPageNo");
        }
        else
        {
             if(request.getSession().getAttribute("cPageNo")!=null)
             {
                 return request.getSession().getAttribute("cPageNo").toString();
             }
             return  null;
        }
    }
public String BranchError()
{
    return BranchError;
}
public String SubjectError()
{
    return SubjectError;
}
public String getExternalMarkError() throws SQLException
{
    if(external==-1.0 )
    {
        return "Enter External Mark";
    }
     return null;
}
public String getInternalMarkError() throws SQLException
{
    if(internal==-1.0 )
    {
        return "Enter Internal Mark";
    }
     return null;
}
public String getPracticalMarkError() throws SQLException
{
    if(Practical==-1.0 )
    {
        return "Enter Practical Mark";
    }
     return null;
}
public String PRNError()
{
    return PRNError;
}

public String SemError()
{
    return SemError;
}
public String getExam()
{
    return ExamId;
}
public String getError()
{
    return Error;
}
public String getBranchName()
{
    return BranchName;
}
public String getStudentName()
{
    return StudentName;
}
public String getExamName()
{
    return ExamName;
}
public String AcdemicYearError()
{
    return AcdemicYearError;
}
public String getAcademicYear()
{
    if(AcdemicYear==null)
        return "-1";
    return AcdemicYear;
}
public String getSemYear()
{
   if( !Sem.equals("-1"))
    return Sem;
    else
        return "-1";
}
public String InternalError()
{
    return InternalError;
}
public String PracticalError()
{
    return PracticalError;
}
public String getBranch()
{
//    if(request.getSession().getAttribute("BranchId")!=null)
//    return request.getSession().getAttribute("BranchId").toString();
//   else
        return BranchId;
}
public String getSubject()
{
    return SubjectId;
}
public String getSubjectName()
{
    return SubjectName;
}
public Double getExternalMark()
{
    return external;
}
public Double getInternalMark()
{
    return internal;
}
public String getAssistant()
{
    return Assistant;
}
public String ExamError()
{
    return ExamError;
}
public String getPRN()
{
    if(PRN==null)
    {
      return "";
    }
    else
      return PRN;
}
public String getPRNError() throws SQLException
{
    if(PRN==null ||PRN.isEmpty())
    {
        return "Enter PRN";
    }
    else if(PRN.length()<12)
    {
        return "Invalid PRN";
    }
    else
    {
        return null;
    }
}
public ArrayList<ExamData> getExams() throws SQLException
{
    return absent.getExams();
}
public ArrayList<ExamData> getAllExams() throws SQLException
{
    return absent.getAllExams();
}
public int getMaxYearOrSemForCourse() throws SQLException
{
    return absent.getMaxYearOrSemForCourse(BranchId);
}
public ArrayList<Subject> getSubjectsForCourse() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getAllSubjectsForBranch(BranchId);
}
public ArrayList<StudentSubjectMark> getMarksEnteredByUser() throws SQLException
{
    return mark.getMarksEnteredByUser(request.getSession().getAttribute("UserName").toString());
}
public ArrayList<Entity.CourseData> getBranchList() throws SQLException
{
    return absent.getBranchList();
}
public ArrayList<User>getAllAssistantsofSO() throws SQLException
{
    UserCreation user=new UserCreation();
    return user.getAllAssistantsofSO(request.getSession().getAttribute("UserName").toString());
}
public int getCountOfRecordsVerifiedBySO() throws SQLException
{
    return mark.getCountOfRecordsToBeVerifiedBySO(request.getSession().getAttribute("UserName").toString());
}

public int getCountOfRecordsVerifiedByAssistant() throws SQLException
{
    return mark.TotalRecordsVerifiedByUser(Assistant,ExamId);
}
public int getCountOfRecordsVerifiedByAssistantinSoPanel() throws SQLException
{
    return mark.TotalRecordsVerifiedByUserForSOPanel(Assistant);
}
public String getQuery()
{
         //Query="select distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s,MarkEntryLog m,AssistantSOMapping asm " ;
        // Query+="where sp.StudentId=sm.StudentId and sm.IsSOVerified=0 and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.StudentSubjectMarkId=m.StudentSubjectMarkId and m.UserName=asm.User_Asst and asm.User_SO=? and asm.EndDate is null and sm.IsAssistantVerified=1 and IsValid=1 ";
         Query="select distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId , sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId from StudentSubjectMark sm " +
" inner join StudentPersonal sp on sp.StudentId=sm.StudentId inner join SubjectBranchMaster sb on sm.SubjectBranchId=sb.SubjectBranchId inner join SubjectMaster s on s.SubjectId=sb.SubjectId inner join MarkEntryLog m on sm.StudentSubjectMarkId=m.StudentSubjectMarkId inner join AssistantSOMapping asm on m.UserName=asm.User_Asst " +
" where sm.IsSOVerified=0 and asm.User_SO=? and asm.EndDate is null and sm.IsAssistantVerified=1 and IsValid=1";
    if(Search)
         {
             if(ExamId!=null && (!ExamId.equals("-1")))
             {
                 Query+=" and sm.ExamId="+ExamId;
             }
             if(BranchId!=null && (!BranchId.equals("-1")))
             {
                 Query+= " and sp.BranchId="+BranchId;
             }
             if(SubjectId!=null && (!SubjectId.equals("-1")) )
             {
                 Query+=" and sb.SubjectId="+SubjectId;
             }
             if(Sem!=null && (!Sem.equals("-1") ))
             {
                 Query+=" and sb.CurrentYearSem="+Sem;
             }
              if(AcdemicYear!=null && (!AcdemicYear.equals("-1")) )
             {
                 Query+=" and sb.AcademicYear="+AcdemicYear;
             }
             if(PRN !=null && (!PRN.isEmpty()))
             {
                 Query+=" and sp.PRN='"+PRN+"'";
             }
             if(Assistant!=null && (!Assistant.equals("-1")))
             {
                 Query+=" and m.Privilege=33 and m.UserName='"+Assistant+"'";
             }

         }
         Query+=" order by sb.SubjectBranchId,sp.PRN limit 0,30 ";
    return Query;
}

public String getApprovedQuery() throws SQLException
{
    //Query="Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s,MarkEntryLog m,AssistantSOMapping asm" ;
         //Query+=" where sp.StudentId=sm.StudentId and sm.IsSOVerified=1 and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.StudentSubjectMarkId=m.StudentSubjectMarkId and m.Privilege=32 and m.IsEdited=4 and m.UserName=?  ";
        Query="Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.StudentSubjectMarkId from StudentSubjectMark sm inner join StudentPersonal sp on sm.StudentId=sp.StudentId inner join SubjectBranchMaster sb on sb.SubjectBranchId=sm.SubjectBranchId inner join SubjectMaster s on s.SubjectId=sb.SubjectId " +
" inner join MarkEntryLog m on sm.StudentSubjectMarkId=m.StudentSubjectMarkId where  sm.IsSOVerified=1 and m.Privilege=32 and m.IsEdited=4 and  sm.IsValid=1 and m.UserName=? ";
    if(Search||request.getParameter("iPageNo")!=null)
         {
             if(ExamId!=null && (!ExamId.equals("-1")))
             {
                 Query+=" and sm.ExamId="+ExamId;
             }
             if(BranchId!=null && (!BranchId.equals("-1")))
             {
                 Query+= " and sp.BranchId="+BranchId;
             }
              if(SubjectId!=null && (!SubjectId.equals("-1")) )
             {
                 Query+=" and sb.SubjectId="+SubjectId;
             }
             if(Sem!=null && !Sem.equals("-1") )
             {
                 Query+=" and sb.CurrentYearSem="+Sem;
             }
              if(AcdemicYear!=null && (!AcdemicYear.equals("-1")) )
             {
                 Query+=" and sb.AcademicYear="+AcdemicYear;
             }
             if(PRN !=null && (!PRN.isEmpty()))
             {
                 Query+=" and sp.PRN='"+PRN+"'";
             }
             if(Assistant!=null && (!Assistant.equals("-1")))
             {
                 Query+=" and sm.StudentSubjectMarkId in ("+mark.getStudentSubjectMarkIdsEnteredByUser(Assistant) +")";
             }
         }
         Query+=" order by sb.SubjectBranchId,sp.PRN ";
    return Query;
}
public String getApprovedQueryforCorrectionAdmin() throws SQLException
{
    

         if(Search)
         {
             Query="Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,SUBSTRING_INDEX(em.ExamName,'-',-1) as ExamName ,sb.CurrentYearSem,sb.PaperNo,sp.PRN,s.SubjectName,sm.ExamId,sm.StudentId,sm.IsValid,sm.IsPassed,sm.MarkStatus,sm.ReMarks,sm.IsWithheld,"
            + " sm.IsAbsent,sm.IsPracticalAbsent,sm.IsMalPractice,sm.IsReValuated,sm.IsPostCorrected,sb.SubjectBranchId ,sm.ExternalMark,"
            + " sm.InternalMark,sm.ModerationExt,sm.ModerationInt,sm.ClassModeration,sm.PracticalMark,sm.TourReportMark,sm.StudentSubjectMarkId ,sm.ExternalGraceMark,sm.InternalGraceMark "
            + " from StudentSubjectMark sm inner join StudentPersonal sp on sm.StudentId=sp.StudentId inner join  SubjectBranchMaster sb "
            + " on sm.SubjectBranchId=sb.SubjectBranchId inner join  SubjectMaster s on sb.SubjectId=s.SubjectId "
            + "inner join ExamMaster em on em.ExamId=sm.ExamId" ;
         Query+=" where sm.IsValid=1   ";
         
             if(ExamId!=null && (!ExamId.equals("-1")))
             {
                 Query+=" and sm.ExamId="+ExamId;
             }
             if(BranchId!=null && (!BranchId.equals("-1")))
             {
                 Query+= " and sp.BranchId="+BranchId;
             }
              if(SubjectId!=null && (!SubjectId.equals("-1")) )
             {
                 Query+=" and sb.SubjectId="+SubjectId;
             }
             if(Sem!=null && !Sem.equals("-1"))
             {
                 Query+=" and sb.CurrentYearSem="+Sem;
             }
              if(AcdemicYear!=null && (!AcdemicYear.equals("-1")) )
             {
                 Query+=" and sb.AcademicYear="+AcdemicYear;
             }
             if(PRN !=null && (!PRN.isEmpty()))
             {
                 Query+=" and sp.PRN='"+PRN+"'";
             }
             if(Assistant!=null && (!Assistant.equals("-1")))
             {
                 Query+=" and sm.StudentSubjectMarkId in ("+mark.getStudentSubjectMarkIdsEnteredByUser(Assistant) +")";
             }
             Query+="  order by sp.PRN,sb.CurrentYearSem,sb.PaperNo,sm.SubjectBranchId,sm.ExamId  ";
        
         
         }
          return Query;
    
}


public boolean getSearch()
    {
    return Search;
}
public ArrayList<StudentSubjectMark> getMarksApprovedBySO() throws SQLException
{
    return mark.getRecordsToBeVerifiedBySO(getApprovedQuery(), request.getSession().getAttribute("UserName").toString());
}
public ArrayList<StudentSubjectMark> getMarksToBeVerifiedBySO() throws SQLException
{
    return mark.getRecordsToBeVerifiedBySO(getQuery(), request.getSession().getAttribute("UserName").toString());
}
public ArrayList<StudentSubjectMark> getMarksForCorrectionAdmin() throws SQLException
{
    return mark.getRecordsforCorrectionAdmin(getApprovedQueryforCorrectionAdmin());
}
public int getTotalCountOfRecordsToBeVerifiedBySO() throws SQLException
{
    return mark.getTotalCountOfRecordsToBeVerifiedBySO(request.getSession().getAttribute("UserName").toString());
}
public boolean UpdateMarksBySO(String StudentSubjectMarkId) throws SQLException
{
    this.StudentSubjectMarkId=StudentSubjectMarkId;
if(IsSubjectHasPractical(StudentSubjectMarkId)){
               
    if(mark.MarkUpdationBySO(external, internal, Practical,StudentSubjectMarkId))
    {
        if(mark.SaveMarkEditLogForSO(StudentSubjectMarkId, mark.getExamIdFromStudentMark(StudentSubjectMarkId),  request))
        return true;
    }}
else{
    Practical=0.0;
   if(mark.MarkUpdationBySO(external, internal, Practical,StudentSubjectMarkId))
    {
        if(mark.SaveMarkEditLogForSO(StudentSubjectMarkId, mark.getExamIdFromStudentMark(StudentSubjectMarkId),  request))
        return true;
    } 
}
    return false;
}
public boolean IsSubjectHasPractical(String StudentSubjectMarkId) throws SQLException
{
    SubjectBranchId=mark.getSubjectBranchIdfromStudentSubjectMarkId(StudentSubjectMarkId);
    return mark.IsSubjectHasPractical(SubjectBranchId);
}

public boolean IsSubjectHasInternal(String StudentSubjectMarkId) throws SQLException
{
    SubjectBranchId=mark.getSubjectBranchIdfromStudentSubjectMarkId(StudentSubjectMarkId);
    return mark.IsSubjectHasInternal(SubjectBranchId);
}
public ArrayList<Subject> getSubjectsForCourseAcdemicYear() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForBranch(BranchId,Sem,AcdemicYear);
}
}
