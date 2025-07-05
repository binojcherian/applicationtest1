<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*" %>
<%
LoginController login=new LoginController(request, response);
int Role=0;String ErrorMsg=null;
if(request.getParameter("Submit")!=null)
    {
        Role=login.getRole();
        if(login.getMessage()!=null)
            {
            ErrorMsg=login.getMessage();
            }
        else
            {
        if(Role==0)
            {
            ErrorMsg="Invalid UserName/Password entered";
            }
        else
            {
            request.getSession().setAttribute("UserName", request.getParameter("UserName"));
            if(Role==1)
            {
            response.sendRedirect("ARInstruction.jsp");
            }
            if(Role==2)
                {
                response.sendRedirect("SOInstruction.jsp");
                }
            if(Role==3)
                {
                response.sendRedirect("InstructionAssistant.jsp");
                }
             if(Role==5)
                {
                response.sendRedirect("AdminInstruction.jsp");
                }
            }
        }
    }

%>
        <html><head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
            
		<link href="Style_New/style.css" rel="stylesheet" type="text/css">
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
<link rel="Stylesheet" href="Style_New/style.css" type="text/css">



	    <style type="text/css">
<!--
.style1 {color: #0099CC}
-->
        </style>
	    </head><body>
	<!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
    <jsp:include page="HeaderLogin.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
    <td width="200">


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
        <td bgcolor="#ffffff">&nbsp;  <%-- Content --%>
            <center><br>
                                                                    <br><br>
                                                                    <br><br>


                                                            <form action="LoginPage.jsp" name="LoginPage" method="post" style="max-width: 350px;background-color:#e8dec3;height: 130px">

                                                                <table style=" font-family: Tahoma, Geneva, sans-serif;font-size: small ">
                                                                    <%if(ErrorMsg!=null) {%>

                                                                    <tr>
                                                                        <td colspan="2"  >
                                                                            <font color="red" ><%=ErrorMsg%></font>
                                                                        </td>
                                                                    </tr>
<% }%>
                                                                    <tr>
                                                                    <br>
                                                                        <td>
                                                                            Username
                                                                        </td>
                                                                        <td>
                                                                            <input type="text"  name="UserName" maxlength="20"  >
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td> Password</td>
                                                                        <td><input type="password" name="Password" maxlength="20">
                                                                        </td>
                                                                    </tr>

                                                                    <tr>

                                                                        <td colspan="2" align="right" >  <input  type="submit" id="Submit" name="Submit" value="Login &raquo; &raquo;" > </td>
                                                                    </tr>

                                                                    <tr>


		</tr>
                                                                </table>
                                                            </form>
                                                                        <br>
                                                                    <br><br>
                                                                    <br>
                                                        </center>

         <%-- Content --%>
        </td>
      </tr>
    </tbody></table></td>
  </tr>
</tbody></table>
</td></tr>
</tbody></table>
</body></html>
<jsp:include page="footer.jsp"/>