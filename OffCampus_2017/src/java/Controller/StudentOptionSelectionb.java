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
import Entity.CentreDetails;
import Entity.SubjectBranch;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;import java.sql.Connection;

public class StudentOptionSelectionb {

    HttpServletRequest request;
    HttpServletResponse response;
    Integer StudentId;
    String Stream = "-1", BranchId = "-1", SubBranchId = "-1", SubjectError = null, Message = null,year;
    String optionalId[],supplyList[];
    OptionalAndStreamSelection Option = new OptionalAndStreamSelection();
    ExamRegistration reg=new ExamRegistration();
    public StudentOptionSelectionb(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        this.request = request;
        this.response = response;
        if (request.getSession().getAttribute("StudentId") != null) {
            StudentId = Integer.parseInt(request.getSession().getAttribute("StudentId").toString());
        }
        if (request.getSession().getAttribute("BranchId") != null) {
            BranchId = request.getSession().getAttribute("BranchId").toString();
        } if (request.getSession().getAttribute("yearApplied") != null) {
           year = request.getSession().getAttribute("yearApplied").toString();
        }
        
        
        supplyList=request.getParameterValues("supplyList");

        if (request.getParameter("SubmitOption") != null && request.getParameter("SubmitOption").equals("Submit")) {

            Connection con=null;
            con=new DBConnection().getConnection();
            ExamRegistration optionDel=new ExamRegistration();
            optionDel.deleteOptionalPapers(StudentId,Integer.parseInt(BranchId),Integer.parseInt(year));
            optionDel.deleteSupplyOrImprovementOrOptionalPapers(StudentId,Integer.parseInt(BranchId),Integer.parseInt(year));
           


            if (BranchId.equals("21") || BranchId.equals("17")) {
                SubBranchId = request.getParameter("SubBranch");
                ArrayList<SubjectBranch> studentOptionals = new mSubjectb().getAllSubjectsForMBAOrMCOM_3AND4_SEM(Integer.parseInt(BranchId), Integer.parseInt(SubBranchId));
                for (int i = 0; i < studentOptionals.size(); i++) {
                     Option.SaveOptionalSubjectMBAorMcom(StudentId, SubBranchId,studentOptionals.get(i).getSubjectBranchId());
                     String SubjectBranchId=studentOptionals.get(i).getSubjectBranchId()+"";
                     Option.SaveOptionaltoExamRegistration(StudentId,SubjectBranchId );

                     
                }
                
                optionDel.SaveExamLog("OptionEdited"+StudentId, request);
                Message = "Option Saved Succesfully";
            } else {
                optionalId = request.getParameterValues("Optional");
                if (optionalId != null && optionalId.length != 0) {
                    try {
                        for (int i = 0; i < optionalId.length; i++) {

                            
                            Option.SaveOptionalSubject(StudentId, optionalId[i]);
                            Option.SaveOptionaltoExamRegistration(StudentId, optionalId[i]);// Saving to ExamRegistartionmaster
                                                       
                        }
                        optionDel.SaveExamLog("OptionEdited"+StudentId, request);
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            }
            if(!(BranchId.equals("21")  && year.equals("2")))
                    {
                        reg.InsertAllSubjectsOfStudent(BranchId, StudentId.toString(), year, con);
                    }
            
            // supply saving 
            
            if (supplyList != null && supplyList.length != 0) {
                    try {
                        for (int i = 0; i < supplyList.length; i++) {

                            Option.SaveSupplementarySubject(StudentId, supplyList[i],"N");
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
           Option.SaveOptionalselectionStatus(StudentId,BranchId);
            try {
                response.sendRedirect("Examregistration.jsp?search_button=ok");
            } catch (IOException ex) {
                Logger.getLogger(StudentOptionSelectionb.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    public String getMessage() {
        return Message;
    }

    public String getSubjectError() {
        if (SubBranchId.equals("-1")) {
            return "Please Select the Option";
        }
        return null;
    }

    public CourseData getOptionalSubjectOfStudent(int StudentId) throws SQLException {
        return Option.getOptionalSubjectOfStudent(StudentId);
    }
}
