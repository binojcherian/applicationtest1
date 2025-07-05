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

public class AbsenteesCountBckUp
{
HttpServletRequest request;
HttpServletResponse response;
String ExamId="-1",BranchId="-1",AcdemicYear="-1",Sem="-1",SubBranchError=null,SubjectBranchId="-1",SubBranchId="-1",AcademicYearSearch="-1",SubBranchSearch="-1",YearSemSearch="-1",BranchSearch="-1",PRNSearch=null,SubjectSearch="-1",ExamError=null,BranchError=null,AcdemicYearError=null,SubjectError=null,SemError=null,CountError=null,ExamAbsentMalPracticeId="-1";
Integer IsAbsentOrMal=-1,AbsentSearch=-1;
Integer Count=-1;
boolean IsSaved=false,IsEdit=false,Search=false;

AbsenteesBckUp absent=new AbsenteesBckUp();

public AbsenteesCountBckUp(HttpServletRequest request,HttpServletResponse response) throws SQLException
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
     if(request.getParameter("CourseSearch")!=null && (!request.getParameter("CourseSearch").equals("-1")))
    {
        BranchId=request.getParameter("CourseSearch");
    }
    if(request.getParameter("AcdemicYear")!=null && (!request.getParameter("AcdemicYear").equals("-1")))
    {
        AcdemicYear=request.getParameter("AcdemicYear");
    }
    if(request.getParameter("AcdemicYearSearch")!=null && (!request.getParameter("AcdemicYearSearch").equals("-1")))
    {
        AcdemicYear=request.getParameter("AcdemicYearSearch");
    }
    if(request.getParameter("Subject")!=null  && (!request.getParameter("Subject").equals("-1")))
    {
        SubjectBranchId=request.getParameter("Subject");
    }
    if(request.getParameter("SubBranch")!=null  && (!request.getParameter("SubBranch").equals("-1")))
    {
        SubBranchId=request.getParameter("SubBranch");
    }
     if(request.getParameter("SubBranchSearch")!=null  && (!request.getParameter("SubBranchSearch").equals("-1")))
    {
        SubBranchId=request.getParameter("SubBranchSearch");
    }
    if(request.getParameter("Count")!=null && (!request.getParameter("Count").isEmpty()))
    {
        Count=Integer.parseInt(request.getParameter("Count"));
    }
    if(request.getParameter("YearSem")!=null  && (!request.getParameter("YearSem").equals("-1")))
    {
        Sem=request.getParameter("YearSem");
    }
     if(request.getParameter("YearSemSearch")!=null && (!request.getParameter("YearSemSearch").equals("-1")))
    {
        YearSemSearch=request.getParameter("YearSemSearch");
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
    if(request.getParameter("AbsentSearch")!=null)
    {
        if(request.getParameter("AbsentSearch").equals("Absent"))
        AbsentSearch=1;
        if(request.getParameter("AbsentSearch").equals("MalPractice"))
        AbsentSearch=0;
        request.getSession().setAttribute("AbsentSearch", AbsentSearch);
    }
    
    if(request.getParameter("SubBranchSearch")!=null)
    {
        SubBranchSearch=request.getParameter("SubBranchSearch");
        request.getSession().setAttribute("SubBranchSearch", SubBranchSearch);
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
        
                if(request.getSession().getAttribute("AcademicYearSearch")!=null)
                {
                   AcademicYearSearch=request.getSession().getAttribute("AcademicYearSearch").toString();
                }

                if(request.getSession().getAttribute("SubjectSearch")!=null)
                {
                    SubjectSearch=request.getSession().getAttribute("SubjectSearch").toString();
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
                          
 
    }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Submit") )
    {
       // IsSaved=absent.UpdateAbsenteesmalPracticeCount(ExamId, SubjectBranchId, IsAbsentOrMal, Count, ExamAbsentMalPracticeId);
        ExamError=getExamError();
        if(ExamError==null)
        {
            BranchError=getBranchError();
            if(BranchError==null)
            {
               AcdemicYearError=getAcdemicYearError();
               if(AcdemicYearError==null)
               {
                SemError=getSemError();
                if(SemError==null)
                {
                    SubjectError=getSubjectError();
                     if(BranchId.equals("21") && ( Sem.equals("3") || Sem.equals("4")) || (BranchId.equals("17") && Sem.equals("4")))
                      {
                             SubBranchError=getSubBranchError();
                             if(SubBranchError==null)
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
                                     request.getSession().removeAttribute("SubBranchSearch");
                                     request.getSession().removeAttribute("YearSemSearch");
                                     request.getSession().removeAttribute("AcademicYearSearch");

                                  }
                                }
                              }

                     }
                     else if(SubjectError==null)
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
                                    request.getSession().removeAttribute("SubBranchSearch");
                                    request.getSession().removeAttribute("YearSemSearch");
                                    request.getSession().removeAttribute("AcademicYearSearch");

                          }
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
               AcdemicYearError=getAcdemicYearError();
               if(AcdemicYearError==null)
               {
                SemError=getSemError();
                if(SemError==null)
                {
                    SubjectError=getSubjectError();
                     if(BranchId.equals("21") && ( Sem.equals("3") || Sem.equals("4") ) || (BranchId.equals("17") && Sem.equals("4")))
                      {
                             SubBranchError=getSubBranchError();
                             if(SubBranchError==null)
                             {
                               SubjectError=getSubjectError();
                               if(SubjectError==null)
                               {
                                CountError=getCountError();
                                if(CountError==null)
                                {
                                     IsSaved=absent.UpdateAbsenteesmalPracticeCount(ExamId, SubjectBranchId, IsAbsentOrMal, Count, ExamAbsentMalPracticeId);
                                     Count=-1;
                                     IsEdit=false;
                                     request.getSession().removeAttribute("BranchSearch");
                                     request.getSession().removeAttribute("SubjectSearch");
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
                     else if(SubjectError==null)
                    {
                        CountError=getCountError();
                        if(CountError==null)
                        {

                                    IsSaved=absent.UpdateAbsenteesmalPracticeCount(ExamId, SubjectBranchId, IsAbsentOrMal, Count, ExamAbsentMalPracticeId);
                                    Count=-1;
                                    IsEdit=false;
                                    request.getSession().removeAttribute("BranchSearch");
                                    request.getSession().removeAttribute("SubjectSearch");
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
        request.getSession().removeAttribute("SubBranchSearch");
        request.getSession().removeAttribute("YearSemSearch");
        request.getSession().removeAttribute("AcademicYearSearch");
        BranchSearch="-1";
        SubjectSearch="-1";
        AbsentSearch=-1;
        SubBranchSearch="-1";
        YearSemSearch="-1";
        AcdemicYear="-1";
        AcademicYearSearch="-1";
    }
}

public boolean getIsEdit()
{
    return IsEdit;
}
public String getQuery()
{
    String Query="select SQL_CALC_FOUND_ROWS b.DisplayName,sb.SubjectBranchId,s.SubjectName,e.IsAbsentOrMal,e.Count,e.ExamAbsenteeMalPracticeId from BranchMaster b,SubjectBranchMaster sb,SubjectMaster s,ExamAbsenteeMalPracticeCount e where b.BranchId=sb.BranchId and s.SubjectId=sb.SubjectId and sb.SubjectBranchId=e.SubjectBranchId and e.ExamId=7";

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
       if(SubBranchSearch !=null && (! SubBranchSearch.equals("-1")) )
       {
           Query+=" and sb.SubBranchId="+ SubBranchSearch;
       }
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
public String SubBranchError()
{
    return SubBranchError;
}
public String AcdemicYearError()
{
    return AcdemicYearError;
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
    AcdemicYear=AbsCount.AcademicYear;
    Sem=AbsCount.YearSem;
    SubjectBranchId=AbsCount.SubjectBranchId;
    SubBranchId=AbsCount.SubBranchId;
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

public int getSemYearSearch()
{
    return Integer.parseInt(YearSemSearch);
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
public String getSubBranchSearch()
{
    return SubBranchSearch;
}
public String getAcademicYear()
{
    if(AcdemicYear==null)
        return "-1";
    return AcdemicYear;
}

public String getAcademicYearSearch()
{
    if(AcademicYearSearch==null)
        return "-1";
    return AcademicYearSearch;
}

public String getSubBranch()
{
     if(SubBranchId==null)
        return "-1";
    return SubBranchId;
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

public String getAcdemicYearError()
{
    if(AcdemicYear.equals("-1"))
    {
        return "Select Acdemic Year";
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
public String getSubBranchError()
{
    if(SubBranchId.equals("-1"))
    {
        return "Select Sub Branch";
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
    else if(Count>absent.getTotalStudentsForExamRegistered(SubjectBranchId, ExamId))
    {
        return "Count entered is greater than total number of students Registered for Exam";
    }
    else if(IsAbsentOrMal==1 && absent.getTotalCountOfAbsenteesEntered(SubjectBranchId,ExamId) > Count)
    {
        return "The total number of absentees entered is greater than the specified Count";
    }
    else if(IsAbsentOrMal==2 && absent.getTotalCountOfMalPracticeEntered(SubjectBranchId,ExamId) > Count)
    {
        return "The total number of Mal Practice entered is greater than the specified Count";
    }
    else if((absent.getTotalCountOfAbsenteesEntered(SubjectBranchId,ExamId)+ absent.getTotalCountOfMalPracticeEntered(SubjectBranchId,ExamId))>absent.getTotalStudentsForExamRegistered(SubjectBranchId, ExamId))
    {
         return "The total number of Mal Practice/Absent entered is greater than the specified Count";
    }
      else if((absent.getTotalCountOfAbsentees(SubjectBranchId,ExamId)+ absent.getTotalCountOfMalPractice(SubjectBranchId,ExamId))>absent.getTotalStudentsForExamRegistered(SubjectBranchId, ExamId))
    {
         return "The total number of Mal Practice/Absent Count entered is greater than the specified Count";
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
    return sub.getSubjectsForBranch(BranchId, Sem,AcdemicYear);
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

public ArrayList<Subject> getAllSubjectsForSearch() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getAllSubjectsForBranch(BranchSearch);
}
}
