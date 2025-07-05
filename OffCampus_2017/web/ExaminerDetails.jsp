<%-- 
    Document   : ExaminerDetails
    Created on : Sep 29, 2011, 4:27:02 PM
    Author     : mgu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
<div class="textblack"></div>	</td>
        <td bgcolor="#ffffff">&nbsp;
            <%-- Content --%>
            <form id="index" name="index" action="index.jsp" method="post">

                <center>
                    <table  border="1" width="80%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Enter Examiner Details</font></b></td>
                    </tr>
                    <tr> <td align="center">
                    <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                <table>

                    <tr>
                        <td class="textblack">Examiner Code</td>
                        <td>
                            <input type="text" name="Code" id="Code" style="width: 215px"  maxlingth="6">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Examiner Name</td>
                        <td>
                            <input type="text" name="Name" id="Name" style="width: 215px"  maxlingth="60">
                        </td>
                    </tr>
                    
                    <tr>
                        <td class="textblack">College Name</td>
                        <td>
                            <input type="text" name="College" id="College" style="width: 215px"  maxlingth="60">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Designation</td>
                        <td>
                            <input type="text" name="Designation" id="Designation" style="width: 215px"  maxlingth="30">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Phone Number</td>
                        <td>
                            <input type="text" name="Phone" id="Phone" style="width: 215px" maxlingth="15">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="submit" name="Submit" name="Submit" value="Submit">
                        </td>
                    </tr>
                </table>
                    </fieldset>
                             <BR>
                   <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Examiner's List</b></span></legend>
                    <table border="1" width="100%">
                    <tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>Sl No</th>
                         <th>Code</th>
                         <th>ExaminerName</th>
                         <th>College</th>
                         <th>Designation</th>
                         <th>Delete </th>
                    </tr>
                    <tr>
                        <td class="textblack">1</td>
                        <td class="textblack">Ex102</td>
                        <td class="textblack">Maria Abraham</td>
                        <td class="textblack">CMS College, Kottayam</td>
                        <td class="textblack">Assistant Professor</td>
                        <td class="textblack"><a href="PRNFalseNoMapping.jsp">Delete</a> </td>
                    </tr>
                    <tr>
                         <td class="textblack">1</td>
                        <td class="textblack">Ex123</td>
                        <td class="textblack">Asha Susan Joy</td>
                        <td class="textblack">U C College, Aluva</td>
                        <td class="textblack">Senior Lecturer</td>
                        <td class="textblack"><a href="PRNFalseNoMapping.jsp">Delete</a> </td>
                    </tr>
                    <tr>
                        <td class="textblack">1</td>
                        <td class="textblack">Ex112</td>
                        <td class="textblack">Abraham Korah</td>
                        <td class="textblack">CMS College, Kottayam</td>
                        <td class="textblack">Lecturer</td>
                        <td class="textblack"><a href="PRNFalseNoMapping.jsp">Delete</a> </td>
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
<jsp:include page="footer.jsp"/>