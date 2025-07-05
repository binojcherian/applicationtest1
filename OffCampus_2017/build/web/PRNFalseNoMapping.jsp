<%-- 
    Document   : PRNFalseNoMapping
    Created on : Sep 27, 2011, 2:16:07 PM
    Author     : mgu
--%>

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
            <form id="PRNFalseNoMapping" name="PRNFalseNoMapping" action="PRNFalseNoMapping.jsp" method="post">
               
                <center>
                    <table  border="1" width="70%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Assign False No To PRN</font></b></td>
                    </tr>
                    <tr> <td>
                    <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                        <table>
                            <tr>
                  <td align="left">
                   <table>
                    <tr>
                        <td class="textblack">Select Exam</td>
                        <td>
                            <select name="Exam" id="Exam" style="width: 215px">
                                <option value="-1">.....Select.....</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Select Course</td>
                        <td>
                            <select name="Course" id="Course" style="width: 215px">
                                 <option value="-1">.....Select.....</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Select Subject</td>
                        <td>
                            <select name="Subject" id="Subject" style="width: 215px">
                                 <option value="-1">.....Select.....</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Enter PRN</td>
                        <td>
                            <input type="text" name="PRN" id="PRN" style="width: 215px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Enter False No</td>
                        <td>
                            <input type="text" name="FalseNo" id="FalseNo" style="width: 215px">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="submit" name="Submit" name="Submit" value="Assign">
                            <input type="submit" name="Clear" name="Clear" value="Reset">
                        </td>
                    </tr>
                </table>
               </td>
                                
                                <td valign="top" align="right" >
                                    
                                    <table border="1" >
                                        <tr>
                                            <td colspan="2" class="textblack"><b>List of False Numbers For User</b></td>
                                        </tr>
                                        <tr class="textblack" bgcolor="#ddcccc" align="center">
                                            <th>FalseNo From</th>
                                            <th>FalseNo To</th>
                                        </tr>
                                        <tr>
                                            <td class="textblack">12300</td>
                                             <td class="textblack">12400</td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">56400</td>
                                             <td class="textblack">56450</td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">75740</td>
                                             <td class="textblack">75770</td>
                                        </tr>
                                        <tr  class="textblack" >
                                            <td class="textblack" colspan="2"><b>Remaining False No to Enter:400</b></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                    <br>
                    <fieldset ><legend><span style="color:#a02a28; font-size: small"><b> False Number List</b></span></legend>
                    <table border="1" width="100%">
                    <tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>Sl No</th>
                         <th>PRN</th>
                         <th>False No</th>
                         <th>Delete </th>
                    </tr>
                    <tr>
                        <td class="textblack">1</td>
                        <td class="textblack">TR05071010988</td>
                        <td class="textblack">12345</td>
                        <td class="textblack"><a href="PRNFalseNoMapping.jsp">Delete</a> </td>
                    </tr>
                    <tr>
                        <td class="textblack">2</td>
                        <td class="textblack">TM02211011475</td>
                        <td class="textblack">56465</td>
                          <td class="textblack"><a href="PRNFalseNoMapping.jsp">Delete</a> </td>
                    </tr>
                    <tr>
                        <td class="textblack">3</td>
                        <td class="textblack">ER05211010704</td>
                        <td class="textblack">75745</td>
                          <td class="textblack"><a href="PRNFalseNoMapping.jsp">Delete</a> </td>
                    </tr>
                     <tr>
                        <td colspan="4" align="CENTER">
                            <input type="submit" name="Submit" name="Submit" value="Send to Approval">
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
<jsp:include page="footer.jsp"/>
