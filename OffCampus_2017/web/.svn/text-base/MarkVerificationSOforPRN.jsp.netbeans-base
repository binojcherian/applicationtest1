<%-- 
    Document   : MarkVerificationSOforPRN
    Created on : Oct 10, 2011, 11:21:54 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="Controller.*,java.util.ArrayList,Entity.*" %>
<%
if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
%>
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

        <jsp:include page="Navigation_ExamSO.jsp"/>

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
            <form id="MarkVerificationSOforPRN" name="MarkVerificationSOforPRN" action="MarkVerificationSOforPRN.jsp" method="post">

                <center>
                    <table  border="1" width="70%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Mark Verification</font></b></td>
                    </tr>
                    <tr> <td>
                    <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Verify</b></span></legend>
                        <table>
                            <tr>
                                <td align="left">
                                        <table>

                    <tr>

                    </tr>
                    <tr  class="textblack"  align="left">
                        <td class="textblack" colspan="6"  height="25px" align="left" style="size: large"><b>Course: MSc. IT </b></td>
                    </tr>
                    <tr  class="textblack"  align="center">
                        <td class="textblack" colspan="6" bgcolor="#ded4b8" height="25px" align="center"><b>Semester:1</b></td>
                    </tr>
                    <tr>
                        <td class="textblack">MATHEMATICAL FOUNDATION OF INFORMATION TECHNOLOGY:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">OBJECT ORIENTED PROGRAMMING WITH C++:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">DATA COMMUNICATION:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">DATA STRUCTURES & ALGORITHMS:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                     <tr>
                        <td class="textblack">PROGRAMMING IN C++ (LAB):</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>

                    <tr  class="textblack"  align="center">
                        <td class="textblack" colspan="6" bgcolor="#ded4b8" height="25px" align="center"><b>Semester:2</b></td>
                    </tr>
                    <tr>
                        <td class="textblack">SYSTEM SOFTWARE AND OPERATING SYSTEMS:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">COMPUTER COMMUNICATION AND NETWORK ARCHITECTURE:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">SOFTWARE ENGINEERING:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">RDBMS AND ORACLE:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>
                     <tr>
                        <td class="textblack">DOS, WINDOWS, LINUX & SEMINAR:</td>
                        <td>
                            <input type="text" name="mark" id="mark" style="width: 100px">
                        </td>
                    </tr>


                    
                </table>
                                </td>


                            </tr>
                        </table>
                        <tr>
                        <td  align="right">
                            <input type="submit" name="Submit" name="Submit" value="Approve">
                            <input type="submit" name="Clear" name="Clear" value="SendForCorrection">
                        </td>
                    </tr>
                    </fieldset>
                         
                    <br>
                    
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

