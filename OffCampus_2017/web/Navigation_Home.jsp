<%-- 
    Document   : Navigation_Home
    Created on : Oct 31, 2011, 3:46:50 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
if(request.getSession().getAttribute("UserName")==null){
 response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}
LoginController login=new LoginController(request, response);
String InstructionPage="";
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role==31)
    {

    InstructionPage="ARInstruction.jsp";
    }
if(Role==32)
    {

    InstructionPage="SOInstruction.jsp";
    }
if(Role==33)
    {
    
    InstructionPage="InstructionAssistant.jsp";
    }
if(Role==35)
    {
    
    InstructionPage="AdminInstruction.jsp";
    }
%>
<div >
    <table width="250" border="0" cellpadding="0" cellspacing="0">
        <tbody>
            <tr>
          <td colspan="2"><img src="images/br.gif" alt=""></td>
        </tr>
        <tr valign="top" align="left">
          <td width="20"><img src="images/arrow.gif" alt="" height="17" width="13"></td>
          <td valign="middle" width="230" align="left"><span class="textorange"><a href="<%=InstructionPage %>">Instruction</a></span></td>
        </tr>
        <tr>
          <td colspan="2"><img src="images/br.gif" alt=""></td>
        </tr>
        <%if(Role==31){ %>
        <tr valign="top" align="left">
          <td width="20"><img src="images/arrow.gif" alt="" height="17" width="13"></td>
          <td valign="middle" width="230" align="left"><span class="textorange"><a href="ARStatusReport.jsp">Status</a></span></td>
        </tr>
        <tr>
          <td colspan="2"><img src="images/br.gif" alt=""></td>
        </tr><% }%>
        <tr valign="top" align="left">
          <td width="20"><img src="images/arrow.gif" alt="" height="17" width="13"></td>
          <td valign="middle" width="230" align="left"><span class="textorange"><a href="ResetPassword.jsp">Change Password</a></span></td>
        </tr>
        <tr>
          <td colspan="2"><img src="images/br.gif" alt=""></td>
        </tr>



	<tr>
	  <td colspan="2">
	  <br>
	  </td>
	</tr>
	<tr>
	 <td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;

	</td>
	</tr>
      </tbody></table>
</div>

