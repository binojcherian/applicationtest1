<%-- 
    Document   : InternalCalculation
    Created on : Oct 28, 2011, 4:38:18 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>

<%
MarkEntryPRNBckUp mark=new MarkEntryPRNBckUp(request, response);
long Internal=0;
if(mark.IsSubjectHasNoInternalCalculation())
    {
        Internal=-1;
        if(mark.IsSubjectHasInternal())
            {
            Internal=-2;
            }
    }
else
    {
    if(request.getParameter("Mark")!=null)
        {
        Internal=mark.CalculateInternal();
        }
    }
%>
<%=Internal%>