<%-- 
    Document   : TabulationRegister
    Created on : Mar 7, 2012, 11:40:15 AM
    Author     : user
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>

<%
if(request.getSession().getAttribute("UserName")==null)
{
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
    String PrivilegeMessage=null;
    LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=33)
    {
        PrivilegeMessage="YOU ARE NOT PERMITTED TO ACCESS THIS PAGE";
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
Tabulation mark=new Tabulation(request, response);
 %>
<html><head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/Tabulation.js"></script>
		<style type="text/css">
<!--
body {
        background-image:  url();
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;
        background-color: #FFFFFF;
}
-->
        </style>
<link rel="Stylesheet" href="Style/style.css" type="text/css">



	    <style type="text/css">
<!--
.style1 {color: #0099CC}
-->
        </style>
	    </head><body>
	<!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<jsp:include page="Header_Assistant.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
       <td width="200" bgcolor="#e8dec3">
              <jsp:include page="Navigation_ExamAssistant.jsp"/>
       </td>
    <td width="20" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="20"></td>
    <td width="90%">
      <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
      <tbody><tr>
        <td colspan="5" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="609"></td>
      </tr>
      <tr>
        <td width="16" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="18" width="8"></td>
	<td>
<div class="textblack" ></div></td>
        <td bgcolor="#ffffff">&nbsp;
            <%-- Content --%>
            <center>
                <form name="TabulationRegister" id="TabulationRegister" action="TabulationRegister.jsp" method="post" style="min-height: 300px">
                <table  border="1" width="90%" style="border-style: groove; min-height: 500px;" cellspacing="0" cellpadding="3" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" colspan="3"> <b> <font color="#a02a28">Tabulation Register</font></b></td>
                    </tr>
                    <tr style="height: 150px;">
                        <td>
                            <fieldset style="height: 100px"><legend><span style="color:#a02a28; font-size: small; "><b>Please Enter</b></span></legend>
                        <center>
                            <table width="90%" cellspacing="4">
                            <tr>
                        <td class="textblack">Exam</td>
                        <td align="left">
                            <select name="Exam" id="Exam" style="width: 350px">
                                <option value="-1">.....Select.....</option>
                                <% ArrayList<ExamData> ExamList=mark.getExams();
                                            int i=1;
                                            int count=ExamList.size();
                                            if(count>0){
                                                    while(i<=count)
                                                        {
                                                        ExamData exam=ExamList.get(i-1);
                                            %>
                                           <option <%if(mark.getExam().equals(exam.ExamId)) { out.print("selected");}  %>
                                                value="<%=exam.ExamId %>"><%=exam.ExamName %></option>

                                            <%i++; }}
                                            %>
                            </select>
                        </td>
                         <td class="textblack">Course</td>
                            <td>
                              <select name="Course" id="Course" style="width: 250px" onchange="ChangeCentreList();">
                                <option value="-1">.....Select.....</option>
                                            <% ArrayList<Entity.CourseData> CourseList=mark.getBranchList();
                                            i=0;
                                            count=CourseList.size();

                                                    while(i<count)
                                                        {
                                                        Entity.CourseData Course=CourseList.get(i);
                                            %>
                                            <option <%if(Integer.parseInt(mark.getBranch())== Course.BranchId)
                                           { out.print("selected");}%>
                                                value="<%=Course.BranchId %>"><%=Course.BranchName %></option>

                                            <%i++; }
                                            %>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">
                            Centre
                        </td>
                        <td class="textblack">
                            <div id="CentreList">
                                <select id="Centre" Name="Centre" style="width: 350px">
                                    <option value="-1">.....Select.....</option>
                                    <% ArrayList<Centre> CentreList=mark.getAllCentresForCourse();
                                            i=0;
                                            count=CentreList.size();

                                                    while(i<count)
                                                        {
                                                        Centre centre=CentreList.get(i);
                                            %>
                                            <option  value="<%=centre.CentreId %>"><%=centre.CentreName %></option>

                                            <%i++; }
                                            %>
                                </select>
                            </div>
                        </td>
                            <td class="textblack">Year/Semester</td>
                        <td>
                            <div id="YearOrSem">
                                <select  name="YearSem" id="YearSem" style="width: 115px" onchange="ChangeSubjectList();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                    count=mark.getMaxYearOrSemForCourse();
                                    i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(Integer.parseInt(mark.getSemYear())==i) { out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++;}%>
                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <%if(mark.ExamError()!=null) {%>
                        <td colspan="4"><span style="color:#a02a28; font-size: small"><%=mark.ExamError()%></span> </td>
                        <% } %>
                        <%if(mark.BranchError()!=null) {%>
                        <td colspan="4"><span style="color:#a02a28; font-size: small"><%=mark.BranchError()%></span> </td>
                        <% } %>
                        <%if(mark.SemError()!=null) {%>
                        <td colspan="4"><span style="color:#a02a28; font-size: small"><%=mark.SemError()%></span> </td>
                        <% } %>
                    </tr>
                    <tr>
                        <td colspan="3" align="right">
                            <input type="submit" name="Submit" id="Submit" value="Submit">
                        </td>
                    </tr>

                        </table>
                                </center>
                    </fieldset>
                        </td>
                    </tr>
                </table>
                                </form>
            </center>
         <%-- Content --%>
        </td>
      </tr>
    </tbody></table></td>
  </tr>
  <TR> <TD><br/></TD></TR>
</tbody></table>
</td></tr>
</tbody></table>
</body></html>
<jsp:include page="footer.jsp"/><% }%>
