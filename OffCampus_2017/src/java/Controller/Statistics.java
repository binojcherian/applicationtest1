/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * sunandha
 */

package Controller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.*;
import Entity.*;

public class Statistics
{
HttpServletRequest request;
HttpServletResponse response;
String ExamId="-1",BranchId="-1",SubjectBranchId="-1",Sem="-1";
Integer ModerationFrom=0,ModerationTo=0;
boolean IsBranchModeration=true;
Absentees absent=new Absentees();
MarkEntry mark=new MarkEntry();
StudentSemesterTotal SemTotal=new StudentSemesterTotal();
public Statistics(HttpServletRequest request,HttpServletResponse response) throws SQLException
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
    if(request.getParameter("Subject")!=null)
    {
        SubjectBranchId=request.getParameter("Subject");
    }
    if(request.getParameter("YearSem")!=null)
    {
        Sem=request.getParameter("YearSem");
    }
    if(request.getParameter("ModerationFrom")!=null && (!request.getParameter("ModerationFrom").isEmpty()))
    {
        ModerationFrom=Integer.parseInt(request.getParameter("ModerationFrom"));
    }
    if(request.getParameter("ModerationTo")!=null && (!request.getParameter("ModerationTo").isEmpty()))
    {
        ModerationTo=Integer.parseInt(request.getParameter("ModerationTo"));
    }
    
    if(request.getParameter("ModerationType")!=null)
    {
        if(request.getParameter("ModerationType").equals("0"))
        {
            IsBranchModeration=true;
        }
        else
        {
            IsBranchModeration=false;
        }
    }
    
}

public String getSemYear()
{
    return Sem;
}

public String getExam()
{
    return ExamId;
}

public boolean IsBranchModeration()
{
    return IsBranchModeration;
}
public String getBranch()
{
    return BranchId;
}

public String getSubject()
{
    return SubjectBranchId;
}

public String getModerationFrom()
{
    if(ModerationFrom==0)
        return "";
    return ModerationFrom.toString();
}

public String getModerationTo()
{
    if(ModerationTo==0)
        return "";
    return ModerationTo.toString();
}

public String getSubjectError()
{
    if(SubjectBranchId.equals("-1"))
    {
        return "Select Subject";
    }
    return null;
}
public String getSubjectName() throws SQLException
{
    return absent.getSubjectNameFromSubjectBranchId(SubjectBranchId);
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

public boolean IsMarkVerificationCompletedForBranch() throws SQLException
{
    return mark.IsMarkVerificationCompletedForBranch(BranchId, Sem,ExamId);
}

public ArrayList<ResultStatistics> getStatisticsForCourse() throws SQLException
{
    if((!BranchId.equals("-1")) && (!Sem.equals("-1") )&& (!ExamId.equals("-1")))
    {
    System.out.println("BranchId "+BranchId+","+Sem+","+ExamId);
    SemTotal.DeleteStudentSemesterTotal();
    SemTotal.SaveStudentSemesterTotal_1(Integer.parseInt(BranchId), Sem);
    }
    //return mark.getStatisticsForCourse(BranchId, Sem, ExamId);
    return mark.getStatisticsForCourse_MBA(BranchId, Sem, ExamId);
}

public int getTotalCountOfRegistrationForCourse() throws SQLException
{
    return mark.getTotalCountOfRegistrationForCourse(BranchId, Sem, ExamId);
}

public int getTotalCountOfStudentsForCourse() throws SQLException
{
    return mark.getTotalCountOfStudentsForCourse(BranchId, Sem, ExamId);
}

public int getTotalPassForCourse() throws SQLException
{
    return mark.getTotalPassForCourse(BranchId, Sem, ExamId);
}

public int getCountofStudentsAboveEightyPercentage() throws SQLException
{
    return mark.getCountofStudentsAboveEightyPercentage(BranchId, Sem, ExamId);
}

public int getCountofStudentsAboveSeventyFivePercentage() throws SQLException
{
    return mark.getCountofStudentsAboveSeventyFivePercentage(BranchId, Sem, ExamId);
}

public int getCountofStudentsAboveSixtyPercentage() throws SQLException
{
    return mark.getCountofStudentsAboveSixtyPercentage(BranchId, Sem, ExamId);
}

public int getCountofStudentsAboveFiftyPercentage() throws SQLException
{
    return mark.getCountofStudentsAboveFiftyPercentage(BranchId, Sem, ExamId);
}

public ArrayList<StatisticsWithModeration> getStatisticsForBranchWithModeration() throws SQLException
{
    return mark.getStatisticsForBranchWithModeration(ModerationFrom, ModerationTo, BranchId, Sem, ExamId);    
}
/*public ArrayList<StatisticsWithModeration> getStatisticsForBranchWithModeration() throws SQLException
{
    return mark.getStatisticsForBranchWithModeration_Blisc(ModerationFrom, ModerationTo, BranchId, Sem, ExamId);
}*/
public ArrayList<StatisticsWithModeration> getStatisticsForSubjectWithModeration() throws SQLException
{
   String SubjectIdforstat=mark.getSubjectIdfromSubjectBranchId(SubjectBranchId);
  //  return mark.getStatisticsForSubjectPassWithModeration(ModerationFrom, ModerationTo, BranchId, Sem, ExamId, SubjectBranchId);
    return mark.getStatisticsForSubjectPassWithModeration_forMBA(ModerationFrom, ModerationTo, BranchId, Sem, ExamId, SubjectIdforstat);
}

/*public ArrayList<Subject> getSubjectsForCourse() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForBranch(BranchId, Sem);
}*/
public ArrayList<Subject> getSubjectsForCourse() throws SQLException
{
    Subjects sub=new Subjects();
    //return sub.getSubjectsForBranchMBA(BranchId, Sem);
    return sub.getSubjectsForBranch_MBASubjectwise(BranchId, Sem);
}
}
