<%-- 
    Document   : Header_Assistant
    Created on : Oct 29, 2011, 12:12:29 PM
    Author     : user
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<div>

    <tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr>
    <td><img src="images/spacer.gif" alt="" height="20" width="1"></td>
  </tr>
</tbody></table>
</td></tr>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">

    <td height="7" width="10" bgcolor="#ffffff"><img src="images/spacer.gif" alt=""  ></td>
    <td height="7" width="100%" bgcolor="#a02a28"><img src="images/spacer.gif" alt=""  ></td>
  </tr>
</tbody></table>
</td></tr>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
    <td width="90%" align="center" valign="baseline" bgcolor="#ffffff">
     <div class="headerlogo" align="left">
                    <img src="images/mgulogo1.jpg" alt="" border="0">
            </div><div id="headercaption" align="left">
                        <h1>MG University  Distance Education</h1>
               </div>
</td>
<td bgcolor="#ffffff" align="right" class="textblack"> Welcome <%=request.getSession().getAttribute("UserName").toString() %></td>
  </tr>
</tbody></table>
</td></tr>
<tr align="right" ><td align="right"  style="color:#6c4303"><a href="InstructionAssistant.jsp" >Home</a>  | <a href="Examregistration.jsp" >Exam Registration</a>  |  <a href="MarkEntryForPRN.jsp" >Exam Management</a> |  <a href="Support.jsp" >Support</a> | <a href="/OffCampus/Logout" >Logout</a>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
    <td height="7" width="100%" bgcolor="#a02a28"><img src="images/spacer.gif" alt=""></td>
  </tr>
</tbody></table>
</td></tr>
<tr><td align="center" valign="top">
    <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
    <td height="10" width="100%" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="759"></td>
  </tr>
</tbody></table>
</td></tr></div>