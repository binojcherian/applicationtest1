<%-- 
    Document   : MarkVerificationSO
    Created on : Sep 29, 2011, 2:23:23 PM
    Author     : mgu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
String studList=null;
if(request.getSession().getAttribute("UserName")==null)
{
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}
else
{
     LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=32)
    {
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
if(request.getParameterValues("Select")!=null)
                {
                    studList=request.getParameter("Select");
                   // out.println(request.getParameter("Select")+"hh");
                    
               }
%>
<html><head>
		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/MarkEntry.js"></script>
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
<table width="100%%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">

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
<div class="textblack"></div>	</td>
        <td bgcolor="#ffffff">&nbsp;
            <%-- Content --%>
<%
String StudentSubjectMarkId;
MarkVerification mark=new MarkVerification(request, response);
StudentSubjectMarkId=request.getParameter("StudentSubjectMarkId");
%>
            <form id="VerificationSO" name="VerificationSO" action="VerificationSO.jsp" method="post">

                <center>
                    <table  border="1" width="100%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" colspan="3" > <b> <font color="#a02a28"> Mark Verification By SO</font></b></td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset><legend><span style="color:#a02a28; font-size: small"><b> List of Marks</b></span></legend>
                                <center>
                                    <fieldset style="width:90%"><legend><span style="color:#a02a28; font-size: small"><b>Search</b></span></legend>
                                        <table width="100%">
                                           
                         <tr>
                        <td class="textblack" align="left">Exam</td>
                        <td align="left"><%=mark.getExamName()%> <input type="hidden" id="Select" name="Select" value="<%=studList%>" />
                        <input type="hidden" name="Exam" id="Exam" value="<%=mark.getExam()%>"/>
                        </td>
                        <td><span style="color:#a02a28; font-size: small"></span>
                            <input type="hidden" name="StudentSubjectMarkId" id="StudentSubjectMarkId" value="<%=StudentSubjectMarkId%>"/>
                        </td>
                        </tr>
                                        
                       <tr>
                       <td class="textblack" align="left">Course</td>
                       <td align="left"><%=mark.getBranchName()%>
                       <input type="hidden" name="Course" id="Course" value="<%=mark.getBranch()%>"/>
                       </td>
                       <td class="textblack" align="left" >PRN</td>
                       <td class="textblack" align="left"><%=mark.getPRN()%>
                       <input type="hidden" name="PRN" id="PRN" value="<%=mark.getPRN()%>"/>
                       </td>
                       </tr>
                       
                       <tr>
                       <td class="textblack" align="left">Acdemic Year</td>
                       <td align="left"><%=mark.getAcademicYear()%>
                       <input type="hidden" name="AcdemicYear" id="AcdemicYear" value="<%=mark.getAcademicYear()%>"/>
                       </td>
                      </tr>
                       <tr>
                       <td class="textblack" align="left">Student Name</td>
                       <td class="textblack" align="left"><%=mark.getStudentName()%></td>
                       </tr>
                       <tr>
                       <td class="textblack" align="left">Year/Semester</td>
                       <td align="left"><%=mark.getSemYear()%>
                       <input type="hidden" name="YearSem" id="YearSem" value="<%=mark.getSemYear()%>"/>
                       </td>
                       <td><span style="color:#a02a28; font-size: small"></span> </td>
                       </tr>
                       <tr>
                       <td class="textblack" align="left">Subject</td>
                       <td colspan="3" align="left"><%=mark.getSubjectName()%> 
                        <input type="hidden" name="Subject" id="Subject" value="<%=mark.getSubject()%>"/>
                       </td>     
                       </tr>
                       <tr>
                       <td class="textblack" align="left">External Mark</td>
                       <td align="left"><input type="text" maxlength="5" id="Mark" name="Mark"  value="" style="width:100px;" onkeypress="return chkNum(event);" onblur="CalculateInternalNew();" ></td>
                       <td class="textblack" align="left">Internal Mark</td>
                       <td align="left"><input type="text" maxlength="5" id="InternalMark" name="InternalMark" value=""  style="width:100px;" onkeypress="return chkNum(event);"></td>
                       </tr>
                       <tr><td colspan="3"><%if(mark.getExternalMarkError()!=null){%><span style="color:#a02a28; font-size: small"><%=mark.getExternalMarkError()%><br/> </span><%}%> 
                           <%if(mark.getInternalMarkError()!=null){%><span style="color:#a02a28; font-size: small"><%=mark.getInternalMarkError()%><br/> </span><%}%>
                           </td></tr>
                        <tr>
                            <td class="textblack" colspan="2"><%if(mark.getError()!=null){%><span style="color:#a02a28; font-size: small"><%=mark.getError()%> </span><%}%></td>
                        </tr>
                        <tr><td>&nbsp;</td></tr>
                        <tr>
                        <td colspan="2" align="center">
                            <input type="submit" name="Submit" id="Submit" value="Approve" >
                        </td>
                        <td colspan="2" align="center">
                            <input type="submit" name="Submit" id="Close" value="Back to Verification Page" onclick="refreshParent();">
                        </td>
                    </tr>
                    </table> </center>
                    </fieldset>  <br>
                    <br>
                <br>
                                </center>
            </form>

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

