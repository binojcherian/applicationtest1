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

public class AbsenteesCount
{
HttpServletRequest request;
HttpServletResponse response;
String ExamId="-1",BranchId="-1",SubjectBranchId="-1", Sem="-1",BranchSearch="-1",PRNSearch=null,SubjectSearch="-1",ExamError=null,BranchError=null,SubjectError=null,SemError=null,CountError=null,ExamAbsentMalPracticeId="-1";
Integer IsAbsentOrMal=-1,AbsentSearch=-1;
Integer Count=-1;
boolean IsSaved=false,IsEdit=false,Search=false;

Absentees absent=new Absentees();

public AbsenteesCount(HttpServletRequest request,HttpServletResponse response) throws SQLException
{
    this.request=request;
    this.response=response;
    if(request.getParameter("Exam")!=null && (!request.getParameter("Exam").equals("-1")))
    {
        ExamId=request.getParameter("Exam");
    }
    if(request.getParameter("Course")!=null && (!request.getParameter("Course").equals("-1")))
    {
        BranchId=request.getParameter("Course");
    }
    if(request.getParameter("Subject")!=null  && (!request.getParameter("Subject").equals("-1")))
    {
        SubjectBranchId=request.getParameter("Subject");
    }
    if(request.getParameter("Count")!=null && (!request.getParameter("Count").isEmpty()))
    {
        Count=Integer.parseInt(request.getParameter("Count"));
    }
    if(request.getParameter("YearSem")!=null  && (!request.getParameter("YearSem").equals("-1")))
    {
        Sem=request.getParameter("YearSem");
    }
    if(request.getParameter("Absent")!=null )
    {
        if(request.getParameter("Absent").equals("Absent"))
        IsAbsentOrMal=1;
        if(request.getParameter("Absent").equals("MalPractice"))
        IsAbsentOrMal=2;
    }
    if(request.getParameter("Search")!=null && request.getParameter("Search").equals("Search"))
    {
        Search=true;
        IsEdit=false;
        request.getSession().removeAttribute("BranchSearch");
        request.getSession().removeAttribute("SubjectSearch");
        request.getSession().removeAttribute("AbsentSearch");
        request.getSession().removeAttribute("iPageNo");
        request.getSession().removeAttribute("cPageNo");
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
    if(request.getParameter("AbsentSearch")!=null)
    {
        if(request.getParameter("AbsentSearch").equals("Absent"))
        AbsentSearch=1;
        if(request.getParameter("AbsentSearch").equals("MalPractice"))
        AbsentSearch=0;
        request.getSession().setAttribute("AbsentSearch", AbsentSearch);
    }
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
               
                if(request.getSession().getAttribute("AbsentSearch")!=null)
                {
                    AbsentSearch=Integer.parseInt(request.getSession().getAttribute("AbsentSearch").toString());
                }
    }
    if(request.getParameter("ExamAbsenteeMalPracticeId")!=null)
    {
        ExamAbsentMalPracticeId=request.getParameter("ExamAbsenteeMalPracticeId");
        if(request.getParameter("Submit")==null && request.getParameter("Search")==null)
        {
        IsEdit=true;
        FillFromDB();
        }
        Search=true;

                if(request.getSession().getAttribute("BranchSearch")!=null)
                {
                    BranchSearch=request.getSession().getAttribute("BranchSearch").toString();
                }
                if(request.getSession().getAttribute("SubjectSearch")!=null)
                {
                    SubjectSearch=request.getSession().getAttribute("SubjectSearch").toString();
                }

                if(request.getSession().getAttribute("AbsentSearch")!=null)
                {
                    AbsentSearch=Integer.parseInt(request.getSession().getAttribute("AbsentSearch").toString());
                }
        
    }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Submit") )
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
                    if(SubjectError==null)
                    {
                        CountError=getCountError();
                        if(CountError==null)
                        { 
                                IsSaved=absent.SaveAbsenteesMalPracticeCount(ExamId, SubjectBranchId, IsAbsentOrMal, Count);
                                Count=-1;
                                IsEdit=false;
                                request.getSession().removeAttribute("BranchSearch");
                                request.getSession().removeAttribute("SubjectSearch");
                                request.getSession().removeAttribute("AbsentSearch");
                                request.getSession().removeAttribute("iPageNo");
                                request.getSession().removeAttribute("cPageNo");
                        }
                    }
                }
            }
        }
    }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Update"))
    {
        IsEdit=true;
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
                    if(SubjectError==null)
                    {
                        CountError=getCountError();
                        if(CountError==null)
                        {
                           IsSaved=absent.UpdateAbsenteesmalPracticeCount(ExamId, SubjectBranchId, IsAbsentOrMal, Count, ExamAbsentMalPracticeId);
                           Search=true;
                           IsEdit=false;
                           ExamId="-1";
                           SubjectBranchId="-1";
                           Sem="-1";
                           BranchId="-1";
                           Count=-1;
                        }
                    }
                }
            }
        }
     }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Reset"))
    {
        request.getSession().removeAttribute("BranchSearch");
        request.getSession().removeAttribute("SubjectSearch");
        request.getSession().removeAttribute("AbsentSearch");
        request.getSession().removeAttribute("iPageNo");
        request.getSession().removeAttribute("cPageNo");
        BranchSearch="-1";
        SubjectSearch="-1";
        AbsentSearch=-1;
    }
}

public boolean getIsEdit()
{
    return IsEdit;
}
public String getQuery()
{
    String Query="select SQL_CALC_FOUND_ROWS b.DisplayName,sb.SubjectBranchId,s.SubjectName,e.IsAbsentOrMal,e.Count,e.ExamAbsenteeMalPracticeId from BranchMaster b,SubjectBranchMaster sb,SubjectMaster s,ExamAbsenteeMalPracticeCount e where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.SubjectBranchId=e.SubjectBranchId ";

     if(Search)
        {
       if(BranchSearch !=null && (!BranchSearch.equals("-1")) )
       {
           Query+=" and sb.BranchId="+BranchSearch;

       }
       if(SubjectSearch !=null && (!SubjectSearch.equals("-1")) )
       {
           Query+=" and sb.SubjectBranchId="+SubjectSearch;
       }
       if(AbsentSearch!=-1 && AbsentSearch==1)
       {
           Query+= " and e.IsAbsentOrMal=1 ";
       }
       if(AbsentSearch!=-1 && AbsentSearch==0)
       {
           Query+= " and e.IsAbsentOrMal=2 ";
       }
   }
    return Query;
}
public int getAbsentSearch()
{
    return AbsentSearch;
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
public String ExamError()
{
    
    return ExamError;
}
public String BranchError()
{
    return BranchError;
}
public String SemError()
{
    return SemError;
}
public String SubjectError()
{
    return SubjectError;
}
public String CountError()
{
    return CountError;
}
public int getIsAbsentOrMal()
{
    return IsAbsentOrMal;
}
private void FillFromDB() throws SQLException
{
    AbsenteeCount AbsCount=absent.getAbsenteeMalPracticeCountofId(ExamAbsentMalPracticeId);
    ExamId=AbsCount.ExamId;
    BranchId=AbsCount.BranchId;
    Sem=AbsCount.YearSem;
    SubjectBranchId=AbsCount.SubjectBranchId;
    Count=AbsCount.Count;
    IsAbsentOrMal=AbsCount.IsAbsentOrMal;
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
    if(ExamId==null)
        return "-1";
    return ExamId;
}
public String getBranch()
{
    if(BranchId==null)
        return "-1";
    return BranchId;
}
public String getSubject()
{
    if(SubjectBranchId==null)
        return "-1";
    return SubjectBranchId;
}
public String getCount()
{
    if(Count==-1)
    {
      return "";
    }
    else
      return Count.toString();
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
    if(SubjectBranchId.equals("-1"))
    {
        return "Select Subject";
    }
    return null;
}
public String getCountError() throws SQLException
{
    if(Count==-1)
    {
        return "Enter Count";
    }
    else if(absent.IsCountAlreadyEntered(ExamId, SubjectBranchId, IsAbsentOrMal, ExamAbsentMalPracticeId))
    {
        return "Count Already entered for this Subject";
    }
    else if(Count>absent.getTotalStudentsForCourse(BranchId, ExamId))
    {
        return "Count entered is greater than total number of students for course";
    }
    else if(IsAbsentOrMal==1 && absent.getTotalCountOfAbsenteesEntered(SubjectBranchId) > Count)
    {
        return "The total number of absentees entered is greater than the specified Count";
    }
    else if(IsAbsentOrMal==2 && absent.getTotalCountOfMalPracticeEntered(SubjectBranchId) > Count)
    {
        return "The total number of Mal Practice entered is greater than the specified Count";
    }

   return null;
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
    return sub.getSubjectsForBranch(BranchId, Sem);
}
public ArrayList<Subject> getAllSubjectsForSearch() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getAllSubjectsForBranch(BranchSearch);
}
}
