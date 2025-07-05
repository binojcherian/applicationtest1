/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.SubjectBranch;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.*;

public class StudentOptionSelection {

    HttpServletRequest request;
    HttpServletResponse response;
    int StudentId;
    String examCentre;
    String Stream = "-1", BranchId = "-1", SubBranchId = "-1", SubjectError = null, Message = null,year;
    String optionalId[],supplyList[],improveList[],cbcssimproveList[];  String CenterId = null,cbcssimproveListcnt=null;
    
    OptionalAndStreamSelection Option = new OptionalAndStreamSelection();

    public StudentOptionSelection(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        this.request = request;
        this.response = response;
        if (request.getSession().getAttribute("StudentId") != null) {
            StudentId = Integer.parseInt(request.getSession().getAttribute("StudentId").toString());
        }
        if (request.getSession().getAttribute("BranchId") != null) {
            BranchId = request.getSession().getAttribute("BranchId").toString();
        
        
        } 
        if(request.getSession().getAttribute("CenterId")!=null)
        {
            CenterId = request.getSession().getAttribute("CenterId").toString();
            
        }if (request.getSession().getAttribute("yearApplied") != null) {
           year = request.getSession().getAttribute("yearApplied").toString();
        }
        
        if (request.getParameter("examCentre") != null) {
           examCentre = request.getParameter("examCentre");
        }
        supplyList=request.getParameterValues("supplyList");
       //  improveList=request.getParameterValues("ImproveList");
        Connection con=null;
        con=new DBConnection().getConnection();
        ExamRegistration optionDel=new ExamRegistration();

        if (request.getParameter("SubmitOption") != null && request.getParameter("SubmitOption").equals("Submit")) {

                  
            optionDel.deleteOptionalPapers(StudentId,Integer.parseInt(BranchId),Integer.parseInt(year));
            optionDel.deleteSupplyOrImprovementOrOptionalPapers(StudentId,Integer.parseInt(BranchId),Integer.parseInt(year));
           


            if (BranchId.equals("21") || BranchId.equals("17") ) {
                SubBranchId = request.getParameter("SubBranch");
                 if(SubBranchId!=null){
                ArrayList<SubjectBranch> studentOptionals = new mSubject().getAllSubjectsForMBAOrMCOM_3AND4_SEM(Integer.parseInt(BranchId), Integer.parseInt(SubBranchId));
                for (int i = 0; i < studentOptionals.size(); i++) {
                     Option.SaveOptionalSubjectMBAorMcom(StudentId, SubBranchId,studentOptionals.get(i).getSubjectBranchId());
                     String SubjectBranchId=studentOptionals.get(i).getSubjectBranchId()+"";
                     Option.SaveOptionaltoExamRegistration(StudentId,SubjectBranchId);

                }}
                Message = "Option Saved Succesfully";
            } else if (BranchId.equals("26")) {
                SubBranchId = request.getParameter("SubBranch");
                 if(SubBranchId!=null){
                ArrayList<SubjectBranch> studentOptionals = new mSubject().getAllSubjectsForLLM_1AND2_SEM(Integer.parseInt(BranchId), Integer.parseInt(SubBranchId));
                for (int i = 0; i < studentOptionals.size(); i++) {
                     Option.SaveOptionalSubjectMBAorMcom(StudentId, SubBranchId,studentOptionals.get(i).getSubjectBranchId());
                     String SubjectBranchId=studentOptionals.get(i).getSubjectBranchId()+"";
                     Option.SaveOptionaltoExamRegistration(StudentId,SubjectBranchId);

                }}
                Message = "Option Saved Succesfully";
            } else if (BranchId.equals("18")) {
                SubBranchId = request.getParameter("SubBranch");
                 if(SubBranchId!=null){
                ArrayList<SubjectBranch> studentOptionals = new mSubject().getAllSubjectsForLLM_3rdYEAR(Integer.parseInt(BranchId), Integer.parseInt(SubBranchId));
                for (int i = 0; i < studentOptionals.size(); i++) {
                     Option.SaveOptionalSubjectMBAorMcom(StudentId, SubBranchId,studentOptionals.get(i).getSubjectBranchId());
                     String SubjectBranchId=studentOptionals.get(i).getSubjectBranchId()+"";
                     Option.SaveOptionaltoExamRegistration(StudentId,SubjectBranchId);

                }}
                Message = "Option Saved Succesfully";
            } 
            
            //chnage for semester course 
            else if(BranchId.equals("24") || BranchId.equals("25")) {
                optionalId = request.getParameterValues("Optional");
                String optional1 = null,optional2 = null;
                if (optionalId != null && optionalId.length != 0) {
                    try {
                        for (int i = 0; i < optionalId.length; i++) {

                            if(optionalId[i].equals("634") ||optionalId[i].equals("648") ){
                              optional1="634" ;
                              optional2="648" ;}
                            else if(optionalId[i].equals("635") ||optionalId[i].equals("651") ){
                              optional1="635" ;
                              optional2="651" ;}
                           else if(optionalId[i].equals("636") ||optionalId[i].equals("649") ){
                              optional1="636" ;
                              optional2="649" ;}
                             else if(optionalId[i].equals("637") ||optionalId[i].equals("650") ){
                              optional1="637" ;
                              optional2="650" ;}
                            
                            else if(optionalId[i].equals("638") ||optionalId[i].equals("653") ){
                              optional1="638" ;
                              optional2="653" ;}
                             else if(optionalId[i].equals("639") ||optionalId[i].equals("657") ){
                              optional1="639" ;
                              optional2="657" ;}
                            Option.SaveOptionalSubject(StudentId, optional1);
                           // Option.SaveOptionalSubject(StudentId, optional2);
                            Option.SaveOptionaltoExamRegistration(StudentId,optional1);
                            Option.SaveOptionaltoExamRegistration(StudentId,optional2);
                             // Saving to ExamRegistartionmaster
                            
                           
                            
                            
                            
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            }
            
            else {
                optionalId = request.getParameterValues("Optional");
                if (optionalId != null && optionalId.length != 0) {
                    try {
                        for (int i = 0; i < optionalId.length; i++) {

                            
                            Option.SaveOptionalSubject(StudentId, optionalId[i]);
                            Option.SaveOptionaltoExamRegistration(StudentId, optionalId[i]);// Saving to ExamRegistartionmaster

                            
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            }
            
            // supply saving 
           String  examType="N";
            if (supplyList != null && supplyList.length != 0) {
                    try {
                        for (int i = 0; i < supplyList.length; i++) {
                         String sbId=supplyList[i]; 
                        
                         String typeT ="N";
                         String typeP ="N";
                         //Bcom CBCSS -- Specify papper type(Theory /Practical] for supply.
                         if(sbId.equals("803") || sbId.equals("812") || sbId.equals("958") || sbId.equals("963")){
                            typeT= request.getParameter("supplyPaperTypeT"+supplyList[i]);
                            typeP = request.getParameter("supplyPaperTypeP"+supplyList[i]);
                            if((typeT!=null && typeT.equals("T")) && (typeP!=null && typeP.equals("P"))   ){
                                examType="B";
                            }else if((typeT!=null && typeT.equals("T"))){
                                 examType="T";
                            }else if((typeP!=null && typeP.equals("P"))){
                                examType="P";
                            }
                         }
                            Option.SaveSupplementarySubject(StudentId, supplyList[i],examType);
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            
              //Improvement saving
            
               //Improvement saving
            if (!(BranchId.equals("7") || BranchId.equals("8"))) {
            int semCompleted= 0;
            String examTypeforImp   ="N";
            int attendingYear=Integer.parseInt(year);
            semCompleted = (attendingYear-1)*2;
            for (int sem=semCompleted;sem>0;sem--){
                
             improveList=request.getParameterValues("ImproveList"+sem);
             if (improveList != null && improveList.length != 0) {
                  boolean isImproveCountLimit=false;
                 if (improveList.length > 2) {
                     
                         isImproveCountLimit = true;
                    
                 }
                     
                 if(!isImproveCountLimit ){   
                    try {
                        for (int i = 0; i < improveList.length; i++) {
                             String impId=improveList[i]; 
   
                         //Bcom CBCSS -- Specify papper type(Theory /Practical] for supply.
                         if(impId.equals("803") || impId.equals("812") || impId.equals("958") || impId.equals("963")){
  
                                examTypeforImp="T";                          
                         } 

                            Option.SaveSupplementarySubject(StudentId, improveList[i],examTypeforImp);
                        }
                        
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }}else  {
                     Message="You can select only two improvement papers.";
                 }
                }}
            }else{ improveList=request.getParameterValues("ImproveList");
             if (improveList != null && improveList.length != 0) {
                
                     
                 
                    try {
                        for (int i = 0; i < improveList.length; i++) {

                            Option.SaveSupplementarySubject(StudentId, improveList[i],"N");
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }}
             
            
           
            
            
            
           Option.SaveOptionalselectionStatus(StudentId,BranchId);
            try {

                 Option.RevertMguApproval(StudentId);

                response.sendRedirect("Examregistration.jsp?search_button=ok");
            } catch (IOException ex) {
                Logger.getLogger(StudentOptionSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        // For Course Completed Students.
        
        else if (request.getParameter("SubmitOption") != null && request.getParameter("SubmitOption").equals("Register")) {

           ExamRegistration registration = new ExamRegistration();
           //registration.deleteExamRegister(BranchId, BranchId, Stream);
            year=request.getParameter("year");
            optionDel.deleteSupplyOrImprovementOrOptionalPapers(StudentId,Integer.parseInt(BranchId));

            // supply saving 
            String  examType="N"; 
            if (supplyList != null && supplyList.length != 0) {
                    try {
                        for (int i = 0; i < supplyList.length; i++) {
                            
                         String sbId=supplyList[i]; 
                         String typeT ="N";
                         String typeP ="N";
                         //Bcom CBCSS -- Specify papper type(Theory /Practical] for supply.
                         if(sbId.equals("803") || sbId.equals("812") || sbId.equals("958") || sbId.equals("963")){
                            typeT= request.getParameter("supplyPaperTypeT"+supplyList[i]);
                            typeP = request.getParameter("supplyPaperTypeP"+supplyList[i]);
                            if((typeT!=null && typeT.equals("T")) && (typeP!=null && typeP.equals("P"))   ){
                                examType="B";
                            }else if((typeT!=null && typeT.equals("T"))){
                                 examType="T";
                            }else if((typeP!=null && typeP.equals("P"))){
                                examType="P";
                            }
                         }
                            Option.SaveSupplementarySubjectForCourseCompleted(StudentId, supplyList[i],examCentre,examType);
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
             //CBCSS course completed
            
            
            
            if ((BranchId.equals("24") || BranchId.equals("25")||BranchId.equals("27") || BranchId.equals("28")|| BranchId.equals("29") || BranchId.equals("30") || BranchId.equals("31"))) {
        int semCompleted= 0;
            int attendingYear=Integer.parseInt(year);
            semCompleted = (attendingYear)*2;
            for (int sem=semCompleted;sem>0;sem--){
                
             cbcssimproveList=request.getParameterValues("cbcssImproveList"+sem);
             cbcssimproveListcnt=request.getParameter("hidcbcssImproveList"+sem);
            
             if(cbcssimproveListcnt!=null){
                 int cnt=Integer.parseInt(cbcssimproveListcnt);
             
             if (cbcssimproveList != null && cbcssimproveList.length != 0) {
                  boolean isImproveCountLimit=false;
                 if (cbcssimproveList.length == cnt) {
                     
                         isImproveCountLimit = true;
                    
                 }
                     
                 if(isImproveCountLimit ){   
                    try {
                        for (int i = 0; i < cbcssimproveList.length; i++) {
                            String sbId=cbcssimproveList[i];
                           String examType1="N";
                           //improve both theory and practical paper  of following subjects -- Bcom CBCSS 
                            if(sbId.equals("803") || sbId.equals("812") || sbId.equals("958") || sbId.equals("963")){
                                examType1="B";
                            }
                            Option.SaveSupplementarySubject(StudentId, cbcssimproveList[i],examType1);
                        }
                    } catch (Exception ex) {

                        Logger.getLogger(ExamStudentsList.class.getName()).log(Level.SEVERE, null, ex);

                    }}else  {
                    
                     Message="You have to select all the papers for semester improvement .";
                    //  response.sendRedirect("SupplySelection.jsp?StudentId="+StudentId);
                
                 }
                } }}
             Message="Option Saved Succesfully.";
            }
          
            try {
                registration.insertExamRegisterForCourseCompleted(BranchId,CenterId,StudentId,examCentre );
                response.sendRedirect("SupplyRegistration.jsp?search_button=ok");
            } catch (IOException ex) {
                Logger.getLogger(StudentOptionSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if(con!=null)
        con.close();
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
