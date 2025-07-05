<%-- 
    Document   : CountAbsenteesAndMalPractice
    Created on : Nov 4, 2011, 11:48:09 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>

<%
int TotalCountAbs=0,TotalCountMal=0,AbsEntered=0,MalEntered=0;
int flagAbs=-1,flagMal=-2;
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
if(request.getParameter("Subject")!=null && request.getParameter("Exam")!=null)
    {
    TotalCountAbs=absentee.getTotalCountOfAbsenteeForSubject();
    TotalCountMal=absentee.getTotalCountOfMalPracticeForSubject();
    AbsEntered=absentee.getTotalCountOfAbsenteesEntered();
    MalEntered=absentee.getTotalCountOfMalPracticeEntered();
    }
%>

<fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Status</b></span></legend>
<font  style="font-size: small" color="#a02a28">Absentees  : <%=AbsEntered %>/<%=TotalCountAbs %> entered
<br>
MalPractice: <%=MalEntered %>/<%=TotalCountMal %> entered</font>
</fieldset>
