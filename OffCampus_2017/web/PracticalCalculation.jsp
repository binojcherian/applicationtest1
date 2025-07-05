<%--
    Document   : InternalCalculation
    Created on : Oct 28, 2011, 4:38:18 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>

<%
MarkEntryPRNBckUp mark=new MarkEntryPRNBckUp(request, response);
long Practical=0;
if(mark.IsSubjectHasPractical())
    {
       Practical=-3;
        
    }

%>
<%=Practical%>