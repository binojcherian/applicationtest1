/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.ExamData;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.BranchList;
import Entity.SubjectBranch;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Absentees;
import model.ExamRegistration;
import model.MarkEntry_old;

public class ExamStudentsList {

    HttpServletRequest request;
    HttpServletResponse response;
    String BranchId = null;
    String yearApplied = null;
    boolean Search = false;
    String Query = null;
    String CenterId = null;
    String ExamId = null,ExamName="",ExamError=null;
     
    ExamRegistration registration = new ExamRegistration();
    Absentees absent=new Absentees();
    MarkEntry_old mark=new MarkEntry_old();
    
    ArrayList<ArrayList<SubjectBranch>> branchOptionals = new ArrayList<ArrayList<SubjectBranch>>();
    ArrayList<Integer> studentList = new ArrayList<Integer>();
    ArrayList<SubjectBranch> branchSupplys = new ArrayList<SubjectBranch>();
    //branchSupplys=supplyList.getSupplyOrImprovementPapers(sid, branchId);
    String errorMessage = null;
    boolean redirected=false;
    String joinYear = null;
    public ExamStudentsList(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        
        this.request = request;
        this.response = response;
         
         if(request.getParameter("ExamId")!=null)
        {
            ExamId = request.getParameter("ExamId");
            request.getSession().setAttribute("ExamId", ExamId);
        }
          
        if(request.getParameter("CentreId")!=null)
        {
            CenterId = request.getParameter("CentreId");
            request.getSession().setAttribute("CenterId", CenterId);
        }
       
        if (request.getParameter("year_applied") != null) {
            yearApplied = request.getParameter("year_applied");
            request.getSession().setAttribute("year", yearApplied);

        }
        if (request.getParameter("course_applied") != null) {
            BranchId = request.getParameter("course_applied");
            request.getSession().setAttribute("BranchId", BranchId);
        }
         if (request.getParameter("join_year") != null) {
            joinYear = request.getParameter("join_year");
            request.getSession().setAttribute("joinYear", joinYear);

        }
        if (request.getParameter("search_button") != null && request.getParameter("search_button").equals("Search")) {
            /*  if(ExamId==null || ExamId.equals("-1"))
            {
                errorMessage="Select Exam";
            }
            else*/
            if(CenterId==null || CenterId.equals("-1"))
            {
                errorMessage="Select Centre";
            }
            else
            {
                if(yearApplied==null || yearApplied.equals("-1"))
                {
                    errorMessage="Select Attending Year";
                }
                else
                {
                    if(BranchId==null || BranchId.equals("-1"))
                    {
                        errorMessage="Select Course";
                    }
                    else
                    {
                        Search = true;
                    }
                }
            }
            

            if (BranchId != null && yearApplied != null) {
                branchOptionals = new BranchList().getOptionalForBranch(Integer.parseInt(BranchId), Integer.parseInt(yearApplied));
                studentList=new ExamRegistration().OptionSelectedStudentsList(BranchId);
            } 
            request.getSession().setAttribute("BranchId", BranchId);
            request.getSession().setAttribute("yearApplied", yearApplied);
             
        }
        else if(request.getParameter("search_button") != null && request.getParameter("search_button").equals("ok"))
        {
            Search=true;
            if(request.getSession().getAttribute("ExamId")!=null)
            {
                ExamId=request.getSession().getAttribute("ExamId").toString();
             
             ExamName=mark.getExamNameFromExamId(ExamId);
            
            }
            if(request.getSession().getAttribute("CenterId")!=null)
            {
                CenterId=request.getSession().getAttribute("CenterId").toString();
            }
            if(request.getSession().getAttribute("year")!=null)
            {
                yearApplied=request.getSession().getAttribute("year").toString();
            }
            if(request.getSession().getAttribute("BranchId")!=null)
            {
                BranchId=request.getSession().getAttribute("BranchId").toString();
            }
            if (BranchId != null && yearApplied != null)
            {
                branchOptionals = new BranchList().getOptionalForBranch(Integer.parseInt(BranchId), Integer.parseInt(yearApplied));
                studentList=new ExamRegistration().OptionSelectedStudentsList(BranchId);
            }
        }
          /* */

        if (request.getParameter("submit") != null && request.getParameter("submit").equals("Approve")) {


            String regstdStudents[] = request.getParameterValues("Select");

            if (regstdStudents != null && regstdStudents.length != 0) {
                try {
//                    registration.deleteExamRegister(BranchId, CenterId,yearApplied);
//                      boolean insertOk=false;
                    for (int i = 0; i < regstdStudents.length; i++) {

                        if (registration.insertExamRegister(BranchId, CenterId, regstdStudents[i], yearApplied)) {
                          //insertOk=true;
                            registration.SaveExamLog("Approved"+regstdStudents[i], request);
                            
                    } 
                        else
                        {
                            errorMessage = "Some error happened during Process";
                        }
                    }
//                      if(insertOk)
//                      {
//                         redirected=true;
//                         response.sendRedirect("CentreExamFee.jsp");
//                      }
                } catch (Exception ex) {

                    Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
                            Search=true;
                            if(request.getSession().getAttribute("CenterId")!=null)
                            {
                                CenterId=request.getSession().getAttribute("CenterId").toString();
                            }
                            if(request.getSession().getAttribute("year")!=null)
                            {
                                yearApplied=request.getSession().getAttribute("year").toString();
                            }
                            if(request.getSession().getAttribute("BranchId")!=null)
                            {
                                BranchId=request.getSession().getAttribute("BranchId").toString();
                            }
                            
                             if (BranchId != null && yearApplied != null) {
                branchOptionals = new BranchList().getOptionalForBranch(Integer.parseInt(BranchId), Integer.parseInt(yearApplied));
                studentList=new ExamRegistration().OptionSelectedStudentsList(BranchId);
            } 
        }

    }

public String getSearchQuery() throws SQLException {
        Query = "";
        if (Search) {
            if (BranchId != null && !BranchId.equals("-1") && !BranchId.equals("-2")) {
                Query += " and StudentPersonal.BranchId=" + BranchId;
                request.getSession().setAttribute("BranchId", BranchId);
            }
            if (yearApplied != null && !yearApplied.equals("-1")) {

                   // Query += " and JoiningYear="+joinYear;

                    Query += " and StudentPersonal.CourseCompletedStatus!=1 and StudentPersonal.IsDisContinue!=1  and StudentPersonal.AttendingYear=" + yearApplied;
            }
        } else{
            if (BranchId != null && yearApplied != null)
             {

                // Query += " and StudentPersonal.BranchId="+ BranchId+ " and StudentPersonal.JoiningYear="+joinYear+"  and StudentPersonal.AttendingYear=" + yearApplied;

                   Query += " and StudentPersonal.CourseCompletedStatus!=1 and StudentPersonal.IsDisContinue!=1  and StudentPersonal.BranchId="+ BranchId+ "  and StudentPersonal.AttendingYear=" + yearApplied;

             }
        }
        return Query;
    }

public String getSearchQueryForCourseCompltd() throws SQLException {
        Query = "";
        if (Search) {
            if (BranchId != null && !BranchId.equals("-1") && !BranchId.equals("-2")) {
                Query += " and StudentPersonal.BranchId=" + BranchId;
                request.getSession().setAttribute("BranchId", BranchId);
            }

                    Query += " and StudentPersonal.CourseCompletedStatus=1 and StudentPersonal.IsDisContinue!=1 and StudentPersonal.BranchId="+ BranchId+ " ";
                    } else{
            if (BranchId != null )
             {
                  Query += " and StudentPersonal.CourseCompletedStatus=1 and StudentPersonal.IsDisContinue!=1  and StudentPersonal.BranchId="+ BranchId+ " ";

             }
        }
        return Query;
    }



    public ArrayList<ArrayList<SubjectBranch>> getBranchOptionals() {
        return branchOptionals;
    }

    public void setBranchOptionals(ArrayList<ArrayList<SubjectBranch>> branchOptionals) {
        this.branchOptionals = branchOptionals;
    }

    public String getCourseApplied() {
        if (BranchId == null) {
            return "-1";
        } else {
            return BranchId;
        }
    }
    public String getCenterId()
    {
        if (CenterId == null) {
            return "-1";
        } else {
            return CenterId;
        }
    }
    public String getExamId()
    {
        if (ExamId == null) {
            return "-1";
        } else {
            return ExamId;
        }
    }
    public String getError()
    {
        return errorMessage;
    }

    public String getExamName()
{
    return ExamName;
}
    public String getYearApplied() {
        if (yearApplied == null) {
            return "";
        } else {
            return yearApplied;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
public boolean getSearch()
    {
    return Search;
}
    public ArrayList<Integer> getStudentList() {
        studentList=new ExamRegistration().OptionSelectedStudentsList(BranchId);
        return studentList;
    }

    public boolean isRedirected() {
        return redirected;
    }

    public void setRedirected(boolean redirected) {
        this.redirected = redirected;
    }

    public ArrayList<ExamData> getExams() throws SQLException
{
    return absent.getExams();
}
public ArrayList<ExamData> getAllExams() throws SQLException
{
    return absent.getAllExams();
}
public String ExamError()
{
    return ExamError;
}
public String getExam()
{
    return ExamId;
}
}
