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

public class MarkVerificationPostCorrectionAR
{
    HttpServletRequest request;
    HttpServletResponse response;
    String UserName=null;
    String BranchId="-1",Id=null,SubjectBranchId="-1",Query=null,PRN=null,Assistant="-1",BranchError=null,SubjectError=null,PRNError=null,SO="-1";
    boolean IsVerified=false,Search=false;
    Double external= -1.0,internal= -1.0,externalmod=0.0,internalmod=0.0,Practical=-1.0;
    MarkEntry_old mark=new MarkEntry_old();
    ArrayList<StudentSubjectMark> Studentmark;
    Absentees absent=new Absentees();

    public MarkVerificationPostCorrectionAR(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
        this.request=request;
        this.response=response;
         if(request.getParameter("Course")!=null)
    {
        BranchId=request.getParameter("Course");
        request.getSession().setAttribute("BranchId", BranchId);
    }
    if(request.getParameter("Subject")!=null)
    {
        SubjectBranchId=request.getParameter("Subject");
        request.getSession().setAttribute("SubjectBranchId", SubjectBranchId);
    }
        if(request.getParameter("SO")!=null)
        {
            SO=request.getParameter("SO");
            request.getSession().setAttribute("SO", SO);
        }
    if(request.getParameter("PRN")!=null)
    {
        PRN=request.getParameter("PRN");
        request.getSession().setAttribute("PRN", PRN);
    }
    if(request.getParameter("external")!=null && (!request.getParameter("external").isEmpty()))
    {
        external=Double.parseDouble(request.getParameter("external"));
    }
        if(request.getParameter("internal")!=null && (!request.getParameter("internal").isEmpty()))
    {
        internal=Double.parseDouble(request.getParameter("internal"));
    }
            if(request.getParameter("PracticalMark")!=null && (!request.getParameter("PracticalMark").isEmpty()))
    {
        Practical=Double.parseDouble(request.getParameter("PracticalMark"));
    }
             if(request.getParameter("internal")!=null && (!request.getParameter("internal").isEmpty()))
    {
        externalmod=Double.parseDouble(request.getParameter("externalmod"));
    }
             if(request.getParameter("internal")!=null && (!request.getParameter("internal").isEmpty()))
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
                    SubjectBranchId=request.getSession().getAttribute("SubjectBranchId").toString();
                }

                if(request.getSession().getAttribute("PRN")!=null)
                {
                    PRN=request.getSession().getAttribute("PRN").toString();
                }
        if(request.getSession().getAttribute("SO")!=null)
                {
                    SO=request.getSession().getAttribute("SO").toString();
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
                 if(mark.ApproveStudentsMarkByARPostCorrection(Studentmark))
                 {
                     if(mark.SaveMarkApprovalLogForARPostCorrection(Studentmark, request))
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
public String PRNError()
{
    return PRNError;
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
    return SubjectBranchId;
}
public String getSO()
{
    return SO;
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
public ArrayList<User>getAllSO() throws SQLException
{
    UserCreation user=new UserCreation();
    return user.getSectionOfficers();
}
public int getCountOfRecordsVerifiedBySO() throws SQLException
{
    return mark.TotalRecordsEnteredByUser(SO);
}


public String forPostCorrectionAR()
{
    Query="select distinct sp.StudentName,sp.PRN,s.SubjectName,sb.SubjectBranchId ,sm.ExternalMark,sm.InternalMark,sm.PracticalMark,sm.ModerationExt,sm.ModerationInt,sm.StudentSubjectMarkId from StudentPersonal sp,StudentSubjectMark sm,SubjectBranchMaster sb,SubjectMaster s,MarkEntryLog m " ;
         Query+="where sp.StudentId=sm.StudentId and sm.IsARVerifiedPostCorrection=1 and sm.SubjectBranchId=sb.SubjectBranchId and s.SubjectId=sb.SubjectId and sm.StudentSubjectMarkId=m.StudentSubjectMarkId  and sm.IsAssistantVerified=1 and sm.IsSOVerified=1 and IsValid=1  and sm.forPostCorrectionSO=0  ";
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
             if(SO!=null && (!SO.equals("-1")))
             {
                 Query+=" and m.Privilege=32 and m.UserName='"+SO+"'";
             }
         }
         Query+=" order by sb.SubjectBranchId,sp.PRN limit 0,30 ";
    return Query;
}




public ArrayList<StudentSubjectMark> getPostCorrectionByAR() throws SQLException
{
    return mark.getPostCorrectionByAR(forPostCorrectionAR());
}
public int getTotalCountOfRecordsToBeVerifiedByAR() throws SQLException
{
    return mark.getTotalCountOfRecordsToBeVerifiedByARPostCorrection();
}

public boolean UpdateMarksByAR(String StudentSubjectMarkId) throws SQLException
{
    if(mark.MarkUpdationBySO(external, internal, Practical,StudentSubjectMarkId))
    {
        if(mark.SaveMarkEditLogForAR(StudentSubjectMarkId, mark.getExamIdFromStudentMark(StudentSubjectMarkId),  request))
        return true;
    }
    return false;
}

public boolean UpdateMarksByARPostCorrection(String StudentSubjectMarkId) throws SQLException
{
    if(mark.MarkUpdationBySOPostCorrection(external, internal,Practical,externalmod,internalmod, StudentSubjectMarkId))
    {
        int isEditValue=10;
        if(mark.SaveMarkLogForARPostCorrection(StudentSubjectMarkId, mark.getExamIdFromStudentMark(StudentSubjectMarkId),isEditValue,  request))
        return true;
    }
    return false;
}

}
