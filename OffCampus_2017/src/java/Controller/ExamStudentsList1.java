/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.BranchList;
import Entity.SubjectBranch;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ExamRegistration;

public class ExamStudentsList1
{

    HttpServletRequest request;
    HttpServletResponse response;
    String BranchId = null;
    String yearApplied = null;
    String joinYear = null;
    boolean Search = false;
    String Query = null;
    String CenterId = null;
    ExamRegistration registration = new ExamRegistration();
    ArrayList<ArrayList<SubjectBranch>> branchOptionals = new ArrayList<ArrayList<SubjectBranch>>();
    ArrayList<Integer> studentList = new ArrayList<Integer>();
    ArrayList<SubjectBranch> branchSupplys = new ArrayList<SubjectBranch>();
    //branchSupplys=supplyList.getSupplyOrImprovementPapers(sid, branchId);
    String errorMessage = null;
    boolean redirected=false;
    public ExamStudentsList1(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        this.request = request;
        this.response = response;
        CenterId = (String) request.getSession().getAttribute("CenterId");
        BranchId = (String) request.getSession().getAttribute("BranchId");
        yearApplied = (String) request.getSession().getAttribute("yearApplied");
        joinYear=(String) request.getSession().getAttribute("joinYear");
        if (request.getParameter("year_applied") != null) {
            yearApplied = request.getParameter("year_applied");

        }
        if (request.getParameter("course_applied") != null) {
            BranchId = request.getParameter("course_applied");

        }
      if (request.getParameter("join_year") != null) {
            joinYear = request.getParameter("join_year");

        }
        if (request.getParameter("search_button") != null && request.getParameter("search_button").equals("Search")) {
            Search = true;

            if (BranchId != null && yearApplied != null) {
                branchOptionals = new BranchList().getOptionalForBranch(Integer.parseInt(BranchId), Integer.parseInt(yearApplied));
                studentList=new ExamRegistration().OptionSelectedStudentsList(BranchId);
            }

            request.getSession().setAttribute("BranchId", BranchId);
            request.getSession().setAttribute("yearApplied", yearApplied);
            request.getSession().setAttribute("joinYear", joinYear);

        } else if(request.getParameter("search_button") != null && request.getParameter("search_button").equals("ok")){
           
            if (BranchId != null && yearApplied != null) {
                branchOptionals = new BranchList().getOptionalForBranch(Integer.parseInt(BranchId), Integer.parseInt(yearApplied));
                studentList=new ExamRegistration().OptionSelectedStudentsList(BranchId);
            }
}
          /**/

        if (request.getParameter("submit") != null && request.getParameter("submit").equals("Register")) {


            String regstdStudents[] = request.getParameterValues("studentId");
            registration.deleteExamRegister(BranchId, CenterId,yearApplied);
            if (regstdStudents != null && regstdStudents.length != 0) {
                try {
                    
                    boolean insertOk=false;
                    for (int i = 0; i < regstdStudents.length; i++) {

                        if (registration.insertExamRegister(BranchId, CenterId, regstdStudents[i], yearApplied)) {
                          insertOk=true;
                        } else {
                            errorMessage = "Some error happened during Exam Registration";
                        }
                    }
                      if(insertOk)
                      {
                         redirected=true;
                         response.sendRedirect("ExamConfirmation.jsp");
                      }
                } catch (Exception ex) {

                    Logger.getLogger(ExamStudentsList1.class.getName()).log(Level.SEVERE, null, ex);

                }
            }else{
           redirected=true;
           response.sendRedirect("ExamConfirmation.jsp");
            }
        }

    }

    //Need to modify
    public String getSearchQueryForHallTicket() throws SQLException {
        Query = "";
        if (Search) {
            if (BranchId != null && !BranchId.equals("-1") && !BranchId.equals("-2")) {
                Query += " and s.BranchId=" + BranchId;
                request.getSession().setAttribute("BranchId", BranchId);
            }
            if (yearApplied != null && !yearApplied.equals("-1")) {
                if (yearApplied.equals("1")) {
                    Query += " and JoiningYear=2011 ";
                }
                Query += " and s.AttendingYear=" + yearApplied;
            }
        } else{
            if (BranchId != null && yearApplied != null)
             {
                if(yearApplied.equals("1"))
                 Query += " and s.BranchId="+ BranchId+ " and s.JoiningYear=2011 and s.AttendingYear=" + yearApplied;
                else
                 Query += " and s.BranchId="+ BranchId+ "  and s.AttendingYear=" + yearApplied;
             }
        }
        return Query;
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

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String BranchId) {
        this.BranchId = BranchId;
    }

    public String getCenterId() {
        return CenterId;
    }

    public void setCenterId(String CenterId) {
        this.CenterId = CenterId;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String Query) {
        this.Query = Query;
    }

    public boolean isSearch() {
        return Search;
    }

    public void setSearch(boolean Search) {
        this.Search = Search;
    }

    public ArrayList<SubjectBranch> getBranchSupplys() {
        return branchSupplys;
    }

    public void setBranchSupplys(ArrayList<SubjectBranch> branchSupplys) {
        this.branchSupplys = branchSupplys;
    }

    public String getJoinYear() {
        return joinYear;
    }

    public void setJoinYear(String joinYear) {
        this.joinYear = joinYear;
    }

    public ExamRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(ExamRegistration registration) {
        this.registration = registration;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    
}
