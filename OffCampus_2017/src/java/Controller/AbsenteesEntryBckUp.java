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

public class AbsenteesEntryBckUp
{
HttpServletRequest request;
HttpServletResponse response;
String ExamId="-1",BranchId="-1",AcdemicYear="-1",Sem="-1",SubBranchId="-1",SubjectId="-1",SubjectBranchId="-1",AcademicYearSearch="-1",SubBranchSearch="-1",YearSemSearch="-1",PRN=null,BranchSearch="-1",PRNSearch=null,SubjectSearch="-1",ExamError=null,BranchError=null,AcdemicYearError=null,SubBranchError=null,SubjectError=null,PRNError=null,SemError=null;
int IsAbsent=0,MalPractice=0;
int AbsentSearch=-1;
boolean IsSaved=false,Search=false;
Absentees absent=new Absentees();
MarkEntry_old mark =new MarkEntry_old();

public AbsenteesEntryBckUp(HttpServletRequest request,HttpServletResponse response) throws SQLException
{
    this.request=request;
    this.response=response;
    if(request.getParameter("Exam")!=null)
    {
        ExamId=request.getParameter("Exam");
    }
    if(request.getParameter("Course")!=null)
    {
        BranchId=request.getParameter("Course");
    }
     if(request.getParameter("CourseSearch")!=null)
    {
        BranchSearch=request.getParameter("CourseSearch");
    }
     if(request.getParameter("SubjectSearch")!=null)
    {
        SubjectSearch=request.getParameter("SubjectSearch");
    }
    if(request.getParameter("Subject")!=null)
    {
        //SubjectBranchId=request.getParameter("Subject");
        SubjectId=request.getParameter("Subject");
         //SubjectBranchId=absent.getStudentSubjectBranchId(ExamId,absent.getStudentIdFromPRN(PRN),SubjectId,Sem);
    }
  
    if(request.getParameter("PRN")!=null)
    {
        PRN=request.getParameter("PRN");
    }
    if(request.getParameter("YearSem")!=null)
    {
        Sem=request.getParameter("YearSem");
    }
    if(request.getParameter("YearSemSearch")!=null)
    {
        YearSemSearch=request.getParameter("YearSemSearch");
    }
    if(request.getParameter("AcdemicYear")!=null)
    {
        AcdemicYear=request.getParameter("AcdemicYear");
    }
    if(request.getParameter("AcdemicYearSearch")!=null)
    {
        AcademicYearSearch=request.getParameter("AcdemicYearSearch");
    }
      
    if(request.getParameter("Absent")!=null)
    {
        if(request.getParameter("Absent").equals("Absent"))
        IsAbsent=1;
        if(request.getParameter("Absent").equals("MalPractice"))
        MalPractice=1;
    }
    
    if(request.getParameter("Search")!=null && request.getParameter("Search").equals("Search"))
    {
        Search=true;
         request.getSession().removeAttribute("BranchSearch");
         request.getSession().removeAttribute("SubjectSearch");
         request.getSession().removeAttribute("PRNSearch");
         request.getSession().removeAttribute("AbsentSearch");
         request.getSession().removeAttribute("iPageNo");
         request.getSession().removeAttribute("cPageNo");
         request.getSession().removeAttribute("SubBranchSearch");
         request.getSession().removeAttribute("YearSemSearch");
         request.getSession().removeAttribute("AcademicYearSearch");
         
    if(request.getParameter("CourseSearch")!=null)
    {
        BranchSearch=request.getParameter("CourseSearch");
        request.getSession().setAttribute("BranchSearch", BranchSearch);
    }
    if(request.getParameter("SubjectSearch")!=null)
    {
        SubjectSearch=request.getParameter("SubjectSearch");
        request.getSession().setAttribute("SubjectSearch", SubjectSearch);
    }
    if(request.getParameter("PRNSearch")!=null)
    {
        PRNSearch=request.getParameter("PRNSearch");
        request.getSession().setAttribute("PRNSearch", PRNSearch);
    }
    if(request.getParameter("AbsentSearch")!=null)
    {
        if(request.getParameter("AbsentSearch").equals("Absent"))
        AbsentSearch=1;
        if(request.getParameter("AbsentSearch").equals("MalPractice"))
        AbsentSearch=0;
        request.getSession().setAttribute("AbsentSearch", AbsentSearch);
    }
    }

   

    if(request.getParameter("YearSemSearch")!=null)
    {
        YearSemSearch=request.getParameter("YearSemSearch");
        request.getSession().setAttribute("YearSemSearch", YearSemSearch);
    }

    if(request.getParameter("AcdemicYearSearch")!=null)
    {
        AcademicYearSearch=request.getParameter("AcdemicYearSearch");
        request.getSession().setAttribute("AcademicYearSearch",  AcademicYearSearch);
    }

    if(request.getParameter("iPageNo")!=null && (!request.getParameter("iPageNo").equals("0")))
    {
        Search=true;
        request.getSession().setAttribute("iPageNo",request.getParameter("iPageNo"));
        if(request.getParameter("cPageNo")!=null)
        {
            request.getSession().setAttribute("cPageNo", request.getParameter("cPageNo"));
        }
        if(request.getSession().getAttribute("BranchSearch")!=null)
        {
            BranchSearch=request.getSession().getAttribute("BranchSearch").toString();
        }
        if(request.getSession().getAttribute("SubjectSearch")!=null)
        {
            SubjectSearch=request.getSession().getAttribute("SubjectSearch").toString();
        }
        if(request.getSession().getAttribute("PRNSearch")!=null)
        {
            PRNSearch=request.getSession().getAttribute("PRNSearch").toString();
        }
        if(request.getSession().getAttribute("AbsentSearch")!=null)
        {
            AbsentSearch=Integer.parseInt(request.getSession().getAttribute("AbsentSearch").toString());
        }
       
        if(request.getSession().getAttribute("YearSemSearch")!=null)
        {
            YearSemSearch=request.getSession().getAttribute("YearSemSearch").toString();
        }
        if(request.getSession().getAttribute("AcademicYearSearch")!=null)
        {
            AcademicYearSearch=request.getSession().getAttribute("AcademicYearSearch").toString();
        }
    }
    if(request.getParameter("AV")!=null)
       {
            Search=true;
                if(request.getSession().getAttribute("BranchSearch")!=null)
                {
                    BranchSearch=request.getSession().getAttribute("BranchSearch").toString();
                }
                if(request.getSession().getAttribute("SubjectSearch")!=null)
                {
                    SubjectSearch=request.getSession().getAttribute("SubjectSearch").toString();
                }
                if(request.getSession().getAttribute("PRNSearch")!=null)
                {
                    PRNSearch=request.getSession().getAttribute("PRNSearch").toString();
                }
                if(request.getSession().getAttribute("AbsentSearch")!=null)
                {
                    AbsentSearch=Integer.parseInt(request.getSession().getAttribute("AbsentSearch").toString());
                }
                 if(request.getSession().getAttribute("SubBranchSearch")!=null)
                {
                    SubBranchSearch=request.getSession().getAttribute("SubBranchSearch").toString();
                }

                if(request.getSession().getAttribute("YearSemSearch")!=null)
                {
                    YearSemSearch=request.getSession().getAttribute("YearSemSearch").toString();
                }
                 if(request.getSession().getAttribute("AcademicYearSearch")!=null)
                {
                   AcademicYearSearch=request.getSession().getAttribute("AcademicYearSearch").toString();
                }
       }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Submit"))
    {
       
        ExamError=getExamError();
        if(ExamError==null)
        {
            BranchError=getBranchError();
            if(BranchError==null)
            {
                SemError=getSemError();
                if(SemError==null)
                {
                     SubjectError=getSubjectError();
                       
                             SubjectError=getSubjectError();
                             if(SubjectError==null)
                             {
                                    SubjectBranchId=absent.getStudentSubjectBranchId(ExamId,absent.getStudentIdFromPRN(PRN),SubjectId,Sem);
                              PRNError=getPRNError();
                              if(PRNError==null)
                              {
                         
                                if(absent.SaveAbsentees(PRN, SubjectBranchId, ExamId, IsAbsent, MalPractice))
                                {
                                    if(absent.SaveAbsenteesMalPracticeLog(absent.getStudentIdFromPRN(PRN), SubjectBranchId, ExamId, request, IsAbsent, MalPractice))
                                    {
                                        IsSaved=true;
                                        request.getSession().removeAttribute("BranchSearch");
                                        request.getSession().removeAttribute("SubjectSearch");
                                        request.getSession().removeAttribute("PRNSearch");
                                        request.getSession().removeAttribute("AbsentSearch");
                                        request.getSession().removeAttribute("iPageNo");
                                        request.getSession().removeAttribute("cPageNo");
                                        request.getSession().removeAttribute("SubBranchSearch");
                                        request.getSession().removeAttribute("YearSemSearch");
                                        request.getSession().removeAttribute("AcademicYearSearch");

                                    }
                                }
                  
                }
            }
            else
            {
                SubjectError=getSubjectError();
                if(SubjectError==null)
                {
                 PRNError=getPRNError();
                if(PRNError==null)
                {
                    if(absent.SaveAbsentees(PRN, SubjectBranchId, ExamId, IsAbsent, MalPractice))
                    {
                     if(absent.SaveAbsenteesMalPracticeLog(absent.getStudentIdFromPRN(PRN), SubjectBranchId, ExamId, request, IsAbsent, MalPractice))
                     {
                       IsSaved=true;
                       request.getSession().removeAttribute("BranchSearch");
                       request.getSession().removeAttribute("SubjectSearch");
                       request.getSession().removeAttribute("PRNSearch");
                       request.getSession().removeAttribute("AbsentSearch");
                       request.getSession().removeAttribute("iPageNo");
                       request.getSession().removeAttribute("cPageNo");

                     }
                   }
                }
              }
            }
          }
        }
      }
   }

    
    if(request.getParameter("Search")==null && request.getParameter("AV")==null && request.getParameter("iPageNo")==null)
    {
        
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

public int getAbsentSearch()
{
    return AbsentSearch;
}

public String getQuery()
{
   String Query="select SQL_CALC_FOUND_ROWS sp.StudentId,sm.StudentSubjectMarkId,sp.StudentName,sp.PRN,c.CollegeName,b.DisplayName,sm.IsAbsent,sm.IsMalPractice,s.SubjectName,sm.SubjectBranchId from StudentPersonal sp,StudentSubjectMark sm,CollegeMaster c,BranchMaster b,SubjectMaster s,SubjectBranchMaster sb,MarkEntryLog ml where sp.StudentId=sm.StudentId and sm.SubjectBranchId=sb.SubjectBranchId and sb.SubjectId=s.SubjectId and sp.BranchId=b.Branchid and c.CollegeId=sp.CollegeId and sm.IsValid=1 and (sm.IsAbsent=1 or sm.IsMalPractice=1) and ml.StudentSubjectMarkId=sm.StudentSubjectMarkId and ml.UserName=? and sm.ExamId=7 ";

   if(Search)
   {
       if(BranchSearch !=null && (!BranchSearch.equals("-1")) )
       {
           Query+=" and sp.BranchId="+BranchSearch;
           
       }
       if(SubjectSearch !=null && (!SubjectSearch.equals("-1")) )
       {
           Query+=" and sb.SubjectId="+SubjectSearch;
       }
       if(PRNSearch !=null && (!PRNSearch.trim().isEmpty()) )
       {
           Query+=" and sp.PRN='"+PRNSearch+"'";
       }
       if(AbsentSearch!=-1 && AbsentSearch==1)
       {
           Query+= " and sm.IsAbsent=1 ";
       }
       if(AbsentSearch!=-1 && AbsentSearch==0)
       {
           Query+= " and sm.IsMalPractice=1 ";
       }
      /* if(SubBranchSearch !=null && (! SubBranchSearch.equals("-1")) )
       {
           Query+=" and sb.SubBranchId="+ SubBranchSearch;
       }*/
       if(YearSemSearch !=null && (!YearSemSearch.equals("-1")) )
       {
           Query+=" and sb.CurrentYearSem="+ YearSemSearch;
       }
        if(AcademicYearSearch !=null && (!AcademicYearSearch.equals("-1")) )
       {
           Query+=" and sb.AcademicYear="+ AcademicYearSearch;
       }
   }
   return Query;
}
public String getPRNSearch()
{
    if(PRNSearch==null)
    {
        return "";
    }
    return PRNSearch;
}
public String getCourseSearch()
{
    return BranchSearch;
}
public String getSubjectSearch()
{
    return SubjectSearch;
}
public String getSubBranchSearch()
{
    return SubBranchSearch;
}
public String getSemYearSearch()
{
    return YearSemSearch;
}
public String getAcdemicYearSearch()
{
    return AcademicYearSearch;
}


public String ExamError()
{
    return ExamError;
}
public String SubBranchError()
{
    return SubBranchError;
}
public String BranchError()
{
    return BranchError;
}
public String SemError()
{
    return SemError;
}

public String AcdemicYearError()
{
    return AcdemicYearError;
}

public String SubjectError()
{
    return SubjectError;
}
public String PRNError()
{
    return PRNError;
}

public String getIssaved()
{
    if(IsSaved)
    {
        return "Saved Successfully";
    }
    return null;
}
public String getSemYear()
{
   
    return Sem;
}
public String getExam()
{
    return ExamId;
}
public String getBranch()
{
    return BranchId;
}
public String getSubject()
{
    return SubjectId;
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
public String getSubBranchError()
{
    if(SubBranchId.equals("-1"))
    {
        return "Select Sub Branch";
    }
    return null;
}
public String getSemError()
{
    if(Sem.equals("-1"))
    {
        return "Select Semester/Year";
    }
    return null;
}
public String getExamError()
{
    if(ExamId.equals("-1"))
    {
        return "Select Exam";
    }
    return null;
}
public String getBranchError()
{
    if(BranchId.equals("-1"))
    {
        return "Select Course";
    }
    return null;
}
public String getSubjectError()
{
   // if(SubjectBranchId.equals("-1"))
    //{
   //     return "Select Subject";
   // }
    if(SubjectId.equals("-1"))
    {
        return "Select Subject";
    }
    return null;
}
public String getAcademicYear()
{
    if(AcdemicYear==null)
        return "-1";
    return AcdemicYear;
}

public String getSubBranch()
{
     if(SubBranchId==null)
        return "-1";
    return SubBranchId;
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
    else if(absent.IsMarkEnteredForStudent(absent.getStudentIdFromPRN(PRN), SubjectBranchId, ExamId) )
    {
        return "Mark Already entered for this student for this Subject";
          
    }
    else if(absent.IsAbsenteeAlreadyEntered(absent.getStudentIdFromPRN(PRN), SubjectBranchId, ExamId))
    {
        return "Student is already marked as Absent for this Subject";
    }
    else if(absent.IsMalPracticeAlreadyEntered(absent.getStudentIdFromPRN(PRN), SubjectBranchId, ExamId))
    {
        return "Student is already marked for Mal Practice for this Subject";
    }
     else if(mark.IsStudentUnconfirmedForSubject(absent.getStudentIdFromPRN(PRN), SubjectBranchId, ExamId) )
    {
     return "Student is marked as withheld for this subject";
    }
    else
    {
        if(absent.IsStudentBelongToCourse(PRN, BranchId)==false)
        {
            return "Student Does not belong to Selected Course";
        }
        else if(absent.IsStudentBelongToSubject(absent.getStudentIdFromPRN(PRN), ExamId, SubjectBranchId)==false)
        {
            return "Student Does not Register for this Subject";
        }
        /*else if((BranchId.equals("21") && (Sem==3 || Sem==4)) || (BranchId.equals("17") && (Sem==4)))
        {
            if(absent.IsStudentBelongToElective(absent.getStudentIdFromPRN(PRN), ExamId, SubBranchId,BranchId,Sem,SubjectBranchId)==false)
            {
                return "Student Does not belong to Selected Sub Branch";
            }
            
        }*/
        return null;
    }
}
public ArrayList<Entity.CourseData> getBranchList() throws SQLException
{
    return absent.getBranchList();
}
public ArrayList<ExamData> getExams() throws SQLException
{
    return absent.getExams();
}
public int getMaxYearOrSemForCourse() throws SQLException
{
    return absent.getMaxYearOrSemForCourse(BranchId);
}
public ArrayList<Subject> getSubjectsForCourse() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForBranch(BranchId,Sem,AcdemicYear);
}
public ArrayList<StudentSubject> getAllAbsentMalPracticeStudents() throws SQLException
{
    return absent.getAllAbsentMalPracticeStudents();
}
public StudentSubject getAbsentMalPracticeDetails(String StudentId) throws SQLException
{
    return absent.getAbsentMalPracticeDetails(StudentId);
}

public ArrayList<Subject> getSubjectsSubBranch() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubBranch(BranchId,Sem);
}

public ArrayList<Subject> getSubjectsForSubBranch() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForSubBranch(BranchId,SubBranchId,Sem,AcdemicYear);
}

public boolean DeleteStudentAbsentDetails(String StudentSubjectMarkId) throws SQLException
{
    return absent.DeleteStudentAbsentDetails(StudentSubjectMarkId);
}
public String getAbsCountError() throws SQLException
{
    if(IsAbsent==1)
    {
        if(getTotalCountOfAbsenteeForSubject()==0)
        {
            return "Total number of absentees for this subject is not entered";
        }
        else if(getTotalCountOfAbsenteeForSubject()==getTotalCountOfAbsenteesEntered())
        {
            return "Absentees maximum count reached ";
        }
    }
    if(MalPractice==1)
    {
        if(getTotalCountOfMalPracticeForSubject()==0)
        {
            return "Total number of mal practice students for this subject is not entered";
        }
        else if(getTotalCountOfMalPracticeForSubject()==getTotalCountOfMalPracticeEntered())
        {
            return "Mal practice students maximum count reached";
        }
    }
    return null;
}
public int getTotalCountOfAbsenteeForSubject() throws SQLException
{
    return absent.getTotalCountOfAbsenteeForSubject(SubjectId, ExamId,BranchId,Sem,AcdemicYear);
}
public int getTotalCountOfAbsenteesEntered() throws SQLException
{
    return absent.getTotalCountOfAbsenteesEntered(SubjectId, ExamId,BranchId,Sem,AcdemicYear);
}
public int getTotalCountOfStudentsForSubject() throws SQLException
{
    return absent.getTotalCountOfStudentsForSubject(SubjectId, ExamId,BranchId,Sem,AcdemicYear);
}
public int getTotalCountOfMalPracticeEntered() throws SQLException
{
   return absent.getTotalCountOfMalPracticeEntered(SubjectId,ExamId,BranchId,Sem,AcdemicYear);
}
public int getTotalCountOfMalPracticeForSubject() throws SQLException
{
     return absent.getTotalCountOfMalPracticeForSubject(SubjectId, ExamId,BranchId,Sem,AcdemicYear);
}
public ArrayList<Subject> getAllSubjectsForCourse() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getAllSubjectsForBranch(BranchId);
}

public ArrayList<Subject> getAllSubjectsForSearch() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getAllSubjectsForBranch(BranchSearch);
}

public int getCurrentExamId() throws SQLException
{
    return absent.getCurrentExamId();
}
public int getTotalCountOfMarkEnteredForSubject() throws SQLException
{
    return absent.getTotalCountOfMarkEnteredForSubject(SubjectId, ExamId,BranchId,Sem,AcdemicYear);
}

public ArrayList<Subject> getSubjectsForCourseAcdemicYear() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForBranch(BranchId,Sem,AcdemicYear);
}

public static void main(String args[])
{
    
}

}
