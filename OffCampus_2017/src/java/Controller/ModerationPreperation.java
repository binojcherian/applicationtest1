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

public class ModerationPreperation
{
    HttpServletRequest request;
    HttpServletResponse response;
    String ExamId="-1",BranchId="-1",SubjectBranchId="-1",Sem="-1";
   
    MarkEntry_old mark=new MarkEntry_old();
    Absentees absent=new Absentees();
    public ModerationPreperation(HttpServletRequest request,HttpServletResponse response)
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
    return SubjectBranchId;
}

public String getSubjectError()
{
    if(SubjectBranchId.equals("-1"))
    {
        return "Select Subject";
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

public boolean IsMarkVerificationCompletedForBranch() throws SQLException
{
    return mark.IsMarkVerificationCompletedForBranch(BranchId, Sem,ExamId);
}

public ArrayList<Subject> getSubjectsForCourse() throws SQLException
{
    Subjects sub=new Subjects();
    return sub.getSubjectsForBranch(BranchId, Sem);
}
}
