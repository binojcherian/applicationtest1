<%--
    Document   : UpdateMarkBySO
    Created on : Oct 28, 2011, 12:17:28 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>

<%
RevaluationVerification mark=new RevaluationVerification(request, response);

String StudentMarkId=request.getParameter("Select");
String External=request.getParameter("external");
String internal=request.getParameter("internal");
String modExt=request.getParameter("externalmod");
String modInt=request.getParameter("internalmod");
boolean IsUpdated=false;
if(External!=null && internal!=null && modExt!=null && modInt!=null){
IsUpdated=mark.UpdateMarksBySORevaluation(StudentMarkId);}

%>
<%=IsUpdated%>