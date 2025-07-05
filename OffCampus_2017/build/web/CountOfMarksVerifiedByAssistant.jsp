<%-- 
    Document   : CountOfMarksVerifiedByAssistant
    Created on : Oct 27, 2011, 10:33:27 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="Controller.*,java.util.ArrayList,Entity.*" %>


<%
String Assistant=request.getParameter("Assistant");
MarkVerification mark=new MarkVerification(request, response);

int Count=mark.getCountOfRecordsVerifiedByAssistant();
if(!Assistant.equals("-1"))
    {
%>

    Total Records Verified by <%= Assistant%>: <%=Count %>

<% }%>