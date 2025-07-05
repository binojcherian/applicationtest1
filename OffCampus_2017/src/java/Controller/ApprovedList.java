/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.CentreDetails;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;



/**
 *
 * @author HP
 */
public class ApprovedList
{
HttpServletRequest request;
HttpServletResponse response;
boolean Search=false;
boolean PostBack=false;
private  int BranchId;
private String CentreId;
private String Username;
private int Year;
  private ArrayList<StudentData> ApprovedList=null;
  private ArrayList<CourseData> Courses=null;
  CurrentAdmissionYear CurYear=new CurrentAdmissionYear();

  mStudentList List;
  String searchText;
    public ApprovedList(HttpServletRequest request,HttpServletResponse response) {
        try{
        this.request=request;
        this.response=response;
       Year=CurYear.getCurrentAdmissionYear();
       Username=request.getSession().getAttribute("UserName").toString();
       List=new mStudentList();
        if(request.getParameter("isPostBack")==null)
        {
            String UserName= request.getSession().getAttribute("UserName").toString();
            if(UserName!=null)
            ApprovedList=List.getApprovedStudentsOfUser(UserName);
        }else if(request.getParameter("Search")!=null&&request.getParameter("Search").equals("Search"))
        {
            Search=true;
            BranchId=Integer.parseInt(request.getParameter("CourseId"));
            CentreId=request.getParameter("CentreId");
            searchText=request.getParameter("SearchText");
            Year=Integer.parseInt(request.getParameter("AdmissionYear"));
            request.getSession().removeAttribute("BranchId");
            request.getSession().removeAttribute("CentreId");
            request.getSession().removeAttribute("AdmissionYear");
        }
       if(request.getParameter("iPageNo")!=null && (!request.getParameter("iPageNo").equals("0")))
       {
        Search=true;
           try{
               request.getSession().setAttribute("iPageNo",request.getParameter("iPageNo"));
               if(request.getParameter("cPageNo")!=null)
               {
                   request.getSession().setAttribute("cPageNo", request.getParameter("cPageNo"));
               }
               if(request.getSession().getAttribute("BranchId")!=null)
                   BranchId=Integer.parseInt(request.getSession().getAttribute("BranchId").toString());
               else
                   BranchId=-1;
           }catch(Exception ne)
           {
               BranchId=-1;
           }
           if(request.getSession().getAttribute("CentreId")!=null)
            CentreId=request.getSession().getAttribute("CentreId").toString();
           if(request.getSession().getAttribute("AdmissionYear")!=null)
            Year=Integer.parseInt(request.getSession().getAttribute("AdmissionYear").toString());
           else
               Year=CurYear.getCurrentAdmissionYear();
       }
        if(request.getParameter("AV")!=null)
       {
            Search=true;
            try
            {
                if(request.getSession().getAttribute("BranchId")!=null)
            BranchId=Integer.parseInt(request.getSession().getAttribute("BranchId").toString());
                if(request.getSession().getAttribute("CentreId")!=null)
            CentreId=request.getSession().getAttribute("CentreId").toString();
            Year=Integer.parseInt(request.getSession().getAttribute("AdmissionYear").toString());
           }
            catch(Exception Nfe)
            {
                Search=false;
           }
            //searchText=request.getParameter("SearchText");
       }
        }catch(Exception ex)
        {
           Logger.getLogger(ApprovedList.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    public int getCentreId()
    {
        if(CentreId==null)
            return -1;
        return Integer.parseInt(CentreId);
    }
public int getCourse()
    {
         if(BranchId==0)
             return -1;
         return  BranchId;
    }
    public String getQuery() throws IOException
    {
        try
        {
         String QueryStr = "select SQL_CALC_FOUND_ROWS SP.StudentId ,StudentName,CollegeName,DisplayName,QuestionId,IsMGUApproved,ifnull(PRN,' ') from StudentPersonal SP,StudentLoginMaster LM,CollegeMaster CM ,BranchMaster BM where ";
            QueryStr += " SP.IsCenterVerified=1 and isFinalSubmit=1 and IsMGUApproved=1 and SP.CollegeId=CM.CollegeId and BM.BranchId=SP.BranchId and LM.StudentId=SP.StudentId and SP.StudentId not in (select StudentId from ReAdmission)";
          if(Search||request.getParameter("iPageNo")!=null)
        {
            boolean AppNoSearch=false;
           if(searchText!=null&& (!searchText.equals(""))&&(!searchText.isEmpty()))
           {
                try{
               int AppNo=Integer.parseInt(searchText);
               if(AppNo>100000)
               QueryStr+=" and QuestionId="+searchText+" ";
               AppNoSearch=true;
               }catch(Exception NE)
                {

                }
               if(!AppNoSearch)
               {
                    QueryStr+=" and  StudentName like '%"+searchText+"%'";
               }
                if(Search)
                request.getSession().setAttribute("SearchText", searchText);
           }


           if(BranchId!=0 && BranchId!=-1 &&!AppNoSearch)
           {
               QueryStr+=" and SP.BranchId="+BranchId+" ";
               if(Search)
               request.getSession().setAttribute("BranchId", new Integer(BranchId));
           }

           if(CentreId!=null&&!CentreId.equals("-1")&&!AppNoSearch)
           {
                QueryStr+=" and SP.CollegeId="+CentreId+" ";
                if(Search)
                request.getSession().setAttribute("CentreId", CentreId);
           }
            if(Search)
           {
                QueryStr+=" and SP.JoiningYear="+Year+" ";
                if(Search)
                request.getSession().setAttribute("AdmissionYear", Year);
           }

            return QueryStr+" order by SP.CollegeId,StudentName";
        }
        else
        {
        QueryStr+=" and SP.JoiningYear="+CurYear.getCurrentAdmissionYear()+" ";
        return QueryStr+" order by SP.CollegeId,StudentName";
        }
        }
        catch(Exception ex)
        {
            Logger.getLogger(ApprovedList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
    public String getStatus(int status)
    {
        if(status==1)
            return "Approved";
       if(status==-1)
            return "Rejected";
            return "Waiting";
    }
    public int getCourseCount()
    {
        if(Courses==null)
            return 0;
        return Courses.size();
    }
    public boolean IsCentreTransfered(int StudentId) throws SQLException
    {
        model.mStudentList stu=new  model.mStudentList();
        return stu.IsCentreTransfered(StudentId);
    }
      public boolean IsDisContinueStudent(int StudentId) throws SQLException
    {
        model.mStudentList stu=new  model.mStudentList();
        return stu.IsDisContinued(StudentId);
    }
public int getAdmissionYear()
{
    return Year;
}
public  ArrayList<CourseData > getCourses() throws SQLException
    {
    return List.getAllBranchs();
    }
public ArrayList<CentreDetails> getCentres() throws SQLException
    {
     return  List.getCentres();
    }
public void close() throws SQLException
    {
    List.close();
}




}
