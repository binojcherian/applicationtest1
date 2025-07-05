<%-- 
    Document   : ResetPassword
    Created on : Oct 31, 2011, 3:14:09 PM
    Author     : user
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%if(request.getSession().getAttribute("UserName")==null){
 response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}else
    {
boolean PostBack = false;
boolean isReset=false;
 login login=new login();
       login.setUserName(request.getParameter("UserName"));
String message=null;
String OldpasError=null,NewPasError=null,ConfmPasError=null;
if ((request.getParameter("isPostBack") != null) && request.getParameter("isPostBack").equals("Yes")) {
                            PostBack = true;
                        }
                        if (PostBack) {
                         LoginCheck validation=new LoginCheck();
                         OldpasError=validation.Password(request.getParameter("oldPaswd"));
                         if(OldpasError==null){
                             NewPasError=validation.newPassword(request.getParameter("newPaswd"));
                             if(NewPasError==null){
                                ConfmPasError=validation.ConfirmPassword(request.getParameter("newPaswd"), request.getParameter("confmPaswd")) ;
                       if(ConfmPasError==null){ login.setUserName((String) request.getSession().getAttribute("UserName"));
                        String oldPasswd=request.getParameter("oldPaswd");
                        if(oldPasswd.equals(login.getPassword()))
                        {
                        //login.resetPaswd(request);
                           login.setPassword(request.getParameter("newPaswd"));
                        isReset=true;
                        message="Your Password has been changed";
                        }
                       else{
                       message="Your current password is wrong";}
                        }}}}
%>
<html><head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/Absentee.js"></script>
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
<jsp:include page="Header_Home.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">
<jsp:include page="Navigation_Home.jsp"/>
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
    <form action="ResetPassword.jsp" name="ResetPassword" method="post">
    <input type="hidden" name="isPostBack" value="Yes"/>

        <table  border="1" width="90%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Change Password</font></b></td>
                    </tr>
                    <tr>
                        <td align="center">
                            <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                         <table>
              <tr>
                <td class="textblack">
                    Current Password
                </td>
                <td>
                    <input type="password"  name="oldPaswd" maxlength="20" value="<%if(PostBack&& !isReset){out.print(request.getParameter("oldPaswd"));}%>" >
                </td>
                <% if(OldpasError!=null ) {%>
                                         <td colspan="" class="textblack">  <span  class="Error" style="color:red">*<%=OldpasError %>  </span></td>

                                   <%}%>
            </tr>
            <tr>
                <td class="textblack">
                    New Password
                </td>
                <td>
                    <input type="password"  name="newPaswd" maxlength="20" >

                </td>
        <% if(NewPasError!=null ) {%>      <td colspan="" class="textblack">  <span  class="Error" style="color:red">*<%=NewPasError%>  </span></td>


                                   <%}else{%>
                                         <td class="textblack"><font color="green" size="small">* Password must be minimum 6 characters and must contain special characters.</font></td><%}%>
        </tr>
            <tr>
                <td class="textblack">Confirm New Password</td>
                <td><input type="password" name="confmPaswd" maxlength="20">
                </td>
                 <% if(ConfmPasError!=null ) {%>


                                         <td colspan="" class="textblack">  <span  class="Error" style="color:red">*<%=ConfmPasError%>  </span></td>

                                   <%}%>

            </tr>
           <tr>&emsp;</tr>
            <tr>
                <td>&emsp;</td>
                <td align="center" >  <input  type="submit" value="Save" onclick="return ValidateAccount()">
                    <input  type="reset" value="Cancel" >  </td>

            </tr>
            <%if(message!=null) {%>
             <span  class="textblack" style="color:red">*<%=message%>  </span>

                                   <%}%>
        </table></fieldset>
                        </td></tr>
        </table>

       
   
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

