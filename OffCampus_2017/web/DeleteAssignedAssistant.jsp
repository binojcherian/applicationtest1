<%-- 
    Document   : DeleteAssignedAssistant
    Created on : Oct 3, 2011, 11:22:44 AM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.User,model.*" %>

<%
if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
if(request.getParameter("Id")==null || request.getParameter("Id").isEmpty())
    {
response.sendRedirect("AssistantToSOMapping.jsp");
}
LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=31)
    {
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
AssistantToSO Map=new AssistantToSO(request, response);
String Id=request.getParameter("Id");
User User=new User();
User=Map.getAssignedAssistantsgetAssigedAssistantRowToDelete(Id);
String Assistant=User.UserName;
String SO=User.Name;
if(request.getParameter("No")!=null && request.getParameter("No").equals("No"))
    {
    response.sendRedirect("AssistantToSOMapping.jsp?AV=1");
    }
if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Yes"))
    {
    if(Map.RemoveAssistantFromSO(request.getParameter("Id")))
        {
        response.sendRedirect("AssistantToSOMapping.jsp?AV=1");
        }
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html><head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
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
<jsp:include page="header.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">

        <jsp:include page="Navigation_ExamAR.jsp"/>

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
            <form id="DeleteAssignedAssistant" name="DeleteAssignedAssistant" action="DeleteAssignedAssistant.jsp" method="post">
                <input type="hidden" id="Id" name="Id" value="<%=Id %>"
                <center>
                    <table  border="1" width="80%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Delete User</font></b></td>
                    </tr>

                    <tr><td colspan="2">
                  <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Delete User</b></span></legend>
                   <table>

                    <tr>
                        <td class="textblack">User Name: SO</td>
                        <td>
                            <input type="text" id="SO" name="SO" readonly="true" value="<%=SO %>" maxlength="30" style="width:215 px">
                        </td>
                    </tr>
                     <tr>

                        <td class="textblack">User Name: Assistant</td>
                        <td>
                            <input type="text" id="Assistant" name="Assistant" readonly="true" value="<%=Assistant %>" maxlength="30" style="width:215 px">
                        </td>
                    </tr>
                    <tr>
                        <td><font color="#a02a28">Do you want to delete this user?</font></td>
                        <td>
                        <input type="submit" name="submit" id="submit" value="Yes">
                        <input type="submit" name="No" id="No" value="No">
                    </td>
                    </tr>
                </table>
              </fieldset>
                  </td></tr>
                </table>
                     </center>
                <br>
                <br>
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
