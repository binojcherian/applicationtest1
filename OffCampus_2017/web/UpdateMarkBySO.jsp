<%-- 
    Document   : UpdateMarkBySO
    Created on : Oct 28, 2011, 12:17:28 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>

<%
    
MarkVerification mark=new MarkVerification(request, response);
MarkEntryPRNBckUp mkpb=new MarkEntryPRNBckUp(request, response);
String InternalError=null,PracticalError=null;
String StudentMarkId=request.getParameter("Select");
String External=request.getParameter("external");
String internal=request.getParameter("internal");
String practical=request.getParameter("practical");

boolean IsUpdated=false;
if(mark.IsSubjectHasInternal(StudentMarkId)){
     InternalError=mark.getInternalMarkError();
       if(InternalError==null){
           if(mark.IsSubjectHasPractical(StudentMarkId)){
                   PracticalError=mark.getPracticalMarkError();
                        if(PracticalError==null)
                            {
                             IsUpdated=mark.UpdateMarksBySO(StudentMarkId);
                               }}else{
                                    IsUpdated=mark.UpdateMarksBySO(StudentMarkId);
                                    }
                                   
                                }
          }else if(mark.IsSubjectHasPractical(StudentMarkId)){
                   PracticalError=mark.getPracticalMarkError();
                        if(PracticalError==null)
                            {
                             IsUpdated=mark.UpdateMarksBySO(StudentMarkId);
                               }
          } else
          {
               IsUpdated=mark.UpdateMarksBySO(StudentMarkId);
          }

request.getSession().setAttribute("Error", PracticalError);
request.getSession().setAttribute("Error1", InternalError);
%>
<%=IsUpdated %>