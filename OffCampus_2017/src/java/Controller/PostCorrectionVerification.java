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

public class PostCorrectionVerification
{
    HttpServletRequest request;
    HttpServletResponse response;
    String UserName=null;
    String BranchId="-1",StudentSubjectMarkId="",BranchName="",Sem="-1",InternalEroor=null,ExamName="",StudentName="",SubjectName="",ExamId="-1",SubjectId="-1",SemError=null,Id=null,AcdemicYear="-1",AcdemicYearError=null,SubjectBranchId="-1",Error=null,ExamError=null,Query=null,PRN=null,Assistant="-1",BranchError=null,SubjectError=null,PRNError=null,MarkError=null;
    boolean IsVerified=false,Search=false;
  
   Double external= -1.0,internal= -1.0,externalmod=0.0,internalmod=0.0,practical=0.0;
    MarkEntry_old mark=new MarkEntry_old();
    ArrayList<StudentSubjectMark> Studentmark;
    Absentees absent=new Absentees();
    private String ExternalError=null;

    public PostCorrectionVerification(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException
    {
        this.request=request;
        this.response=response;
    if(request.getParameter("Exam")!=null)
    {
        ExamId=request.getParameter("Exam");
         
    }
     if(request.getParameter("AcdemicYear")!=null)
    {
        AcdemicYear=request.getParameter("AcdemicYear");
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
    if(request.getParameter("YearSem")!=null)
    {
        Sem=request.getParameter("YearSem");
    }
    if(request.getParameter("external")!=null && (!request.getParameter("external").isEmpty()))
    {
        external=Double.parseDouble(request.getParameter("external"));
    }
        if(request.getParameter("internal")!=null && (!request.getParameter("internal").isEmpty()))
    {
        internal=Double.parseDouble(request.getParameter("internal"));
    }
           if(request.getParameter("practicalMark")!=null && (!request.getParameter("practicalMark").isEmpty()))
    {
        practical=Double.parseDouble(request.getParameter("practicalMark"));
    }
        if(request.getParameter("externalmod")!=null && (!request.getParameter("externalmod").isEmpty()))
    {
        externalmod=Double.parseDouble(request.getParameter("externalmod"));
    }
             if(request.getParameter("internalmod")!=null && (!request.getParameter("internalmod").isEmpty()))
    {
        internalmod=Double.parseDouble(request.getParameter("internalmod"));
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
         if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Approve"))
        {
            Studentmark=new ArrayList<StudentSubjectMark>();
            StudentSubjectMark Student;
            if(request.getParameterValues("Select")!=null)
                {
                String Stu[]=request.getParameterValues("Select");
                 for (String SId : Stu)
                 {
                    Student=new StudentSubjectMark();
                    Student.StudentSubjectMarkId=SId;
                    Studentmark.add(Student);
                 }
                 if(mark.ApproveStudentsMarkBySOPostCorrection(Studentmark))
                 {
                     if(mark.SaveMarkApprovalLogForSOPostCorrection(Studentmark, request))
                     {
                         IsVerified=true;
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
           practical=Double.parseDouble(Student.PracticalMark);
           SubjectId=Student.SubjectId;
           StudentName=Student.StudentName;
           SubjectName=Student.SubjectName;
           BranchName=mark.getBranchNameFromBranchId(Student.BranchId);
           ExamName=mark.getExamNameFromExamId(Student.ExamId);
           Search=true;
       }

        if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Verify Mark"))
        {

             if(request.getParameter("Select")!=null)
                {
                    external=0.0;
                    internal=0.0;
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
                    practical=Double.parseDouble(request.getParameter("PracticalMark"));
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
    if(Sem.equals("-1"))
    return Sem.toString();
    else
        return "-1";
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

public int getCountOfRecordsVerifiedByAssistant() throws SQLException
{
    return mark.TotalRecordsVerifiedByUser(Assistant);
}
public int getCountOfRecordsVerifiedBySO() throws SQLException
{
    return mark.getCountOfRecordsToBeVerifiedBySO(request.getSession().getAttribute("UserName").toString());
}


public int getCountOfRecordsVerifiedByAssistantinSoPanel() throws SQLException
{
    return mark.TotalRecordsVerifiedByUserForSOPanel(Assistant);
}
public String getQuery()
{
    Query="select distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.StudentSubjectMarkId from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s,MarkEntryLog m,AssistantSOMapping asm " ;
         Query+="where sp.StudentId=sm.StudentId and sm.IsSOVerified=0 and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.StudentSubjectMarkId=m.StudentSubjectMarkId and m.UserName=asm.User_Asst and asm.User_SO=? and asm.EndDate is null and sm.IsAssistantVerified=1 and IsValid=1 ";
         if(Search)
         {
             if(BranchId!=null && (!BranchId.equals("-1")))
             {
                 Query+= " and sp.BranchId="+BranchId;
             }
             if(SubjectBranchId!=null && (!SubjectBranchId.equals("-1")) )
             {
                 Query+=" and sm.SubjectBranchId="+SubjectBranchId;
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
public String forPostCorrection()
{
    Query="SELECT p.StudentName,p.PRN,b.SubjectBranchId,s.SubjectName ,m.ExternalMark, " +
" m.InternalMark,m.ModerationExt,m.ModerationInt,m.StudentSubjectMarkId FROM StudentSubjectMark m  " +
" inner join StudentPersonal p on p.StudentId=m.StudentId " +
" inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId " +
" inner join UserCourseMapping u on u.BranchId=b.BranchId and  u.UserName=? " +
" inner join SubjectMaster s " +
" on s.SubjectId=b.SubjectId " +
" where  m.forPostCorrectionSO=1 and m.IsValid=1 and u.EndDate is null and  m.IsAssistantVerified=1 and m.IsSOVerified=1 ";
         //if(Search)
        // {
             if(ExamId!=null && (!ExamId.equals("-1")))
             {
                 Query+=" and m.ExamId="+ExamId;
             }
             if(BranchId!=null && (!BranchId.equals("-1")))
             {
                 Query+= " and p.BranchId="+BranchId;
             }
              if(SubjectId!=null && (!SubjectId.equals("-1")) )
             {
                 Query+=" and b.SubjectId="+SubjectId;
             }
             if(Sem!=null && (!Sem.equals("-1")) )
             {
                 Query+=" and b.CurrentYearSem="+Sem;
             }
              if(AcdemicYear!=null && (!AcdemicYear.equals("-1")) )
             {
                 Query+=" and b.AcademicYear="+AcdemicYear;
             }
             if(PRN !=null && (!PRN.isEmpty()))
             {
                 Query+=" and p.PRN='"+PRN+"'";
             }
             
         //}
         Query+=" order by b.SubjectBranchId,p.PRN ";
    return Query;
}

public String getApprovedQuery() throws SQLException
{
    Query="Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.StudentSubjectMarkId from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s,MarkEntryLog m,AssistantSOMapping asm" ;
         Query+=" where sp.StudentId=sm.StudentId and sm.IsSOVerified=1 and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.StudentSubjectMarkId=m.StudentSubjectMarkId and m.Privilege=32 and m.IsEdited=4 and m.UserName=?  ";
         if(Search)
         {
             if(BranchId!=null && (!BranchId.equals("-1")))
             {
                 Query+= " and sp.BranchId="+BranchId;
             }
             if(SubjectBranchId!=null && (!SubjectBranchId.equals("-1")) )
             {
                 Query+=" and sm.SubjectBranchId="+SubjectBranchId;
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


public ArrayList<StudentSubjectMark> getMarksApprovedBySO() throws SQLException
{
    return mark.getRecordsToBeVerifiedBySO(getApprovedQuery(), request.getSession().getAttribute("UserName").toString());
}
public ArrayList<StudentSubjectMark> getMarksToBeVerifiedBySO() throws SQLException
{
    return mark.getRecordsToBeVerifiedBySO(getQuery(), request.getSession().getAttribute("UserName").toString());
}
public ArrayList<StudentSubjectMark> getPostCorrectionBySO() throws SQLException
{
    return mark.getPostCorrectionBySO(forPostCorrection(), request.getSession().getAttribute("UserName").toString());
}
public int getTotalCountOfRecordsToBeVerifiedBySO() throws SQLException
{
    return mark.getTotalCountOfRecordsToBeVerifiedBySO(request.getSession().getAttribute("UserName").toString());
}
public int getTotalCountOfRecordsToBeVerifiedBySOPostCorrection() throws SQLException
{
    return mark.getTotalCountOfRecordsToBeVerifiedBySOPostCorrection(request.getSession().getAttribute("UserName").toString());
}
public boolean UpdateMarksBySO(String StudentSubjectMarkId) throws SQLException
{
    if(mark.MarkUpdationBySO(external, internal,practical,  StudentSubjectMarkId))
    {
        if(mark.SaveMarkEditLogForSO(StudentSubjectMarkId, mark.getExamIdFromStudentMark(StudentSubjectMarkId),  request))
        return true;
    }
    return false;
}
    public boolean UpdateMarksBySOPostCorrection(String StudentSubjectMarkId) throws SQLException {
        MarkError = mark.MarkCorrection(external + externalmod, internal + internalmod, StudentSubjectMarkId);
       if (MarkError == null) {
            if (mark.MarkUpdationBySOPostCorrection(external, internal,practical, externalmod, internalmod, StudentSubjectMarkId)) {
                int isEditValue = 9;
                if (mark.SaveMarkLogForSOPostCorrection(StudentSubjectMarkId, mark.getExamIdFromStudentMark(StudentSubjectMarkId), isEditValue, request)) {
                    return true;
                }
            }

       }
        return false;
    }
   /* public String MarkError()
{
    return MarkError;
}*/
public ArrayList<Subject> getSubjectsForCourseAcdemicYear() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForBranch(BranchId,Sem,AcdemicYear);
}
}
