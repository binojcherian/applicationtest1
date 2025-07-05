<%-- 
    Document   : ModerationPreperation
    Created on : Dec 9, 2011, 12:17:39 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>

<% if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
    String PrivilegeMessage=null;
    ModerationPreperation moderation=new ModerationPreperation(request, response);
    LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=31)
    {
        PrivilegeMessage="YOU ARE NOT PERMITTED TO ACCESS THIS PAGE";
    }
%>
<html><head>
		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/Moderation.js"></script>
                <script type="text/javascript" src="./js/Absentee.js"></script>
		<style type="text/css">
<!--
body
{
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
        
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0" >
<tbody>
<jsp:include page="Header_Home.jsp"/>
<tr><td>
        <% if(PrivilegeMessage!=null){ %>
        <div align="left" style="min-height: 400px" >
                 <MARQUEE  direction="left" scrollamount="6" width="50%" loop="-1">   <b style="color:#FF0000; font-size:18px; font-family:Georgia, 'Times New Roman', Times, serif; font-weight:bold;"><%=PrivilegeMessage%></b>
                 </MARQUEE></div> <%}else{%>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200"  bgcolor="#e8dec3">
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
<div class="textblack"></div>
        </td>
        <td bgcolor="#ffffff">&nbsp;
         <%-- Content --%>
         <center>
             <form name="ModerationPreperation" id="ModerationPreperation" action="ModerationPreperation.jsp" method="post">
                 
                 <table  BORDER="1"  style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack" width="95%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Moderation Preperation</font></b></td>
                    </tr>
                    <tr>
                        <td align="center">
                            <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                                <table>
                                    <tr>
                                        <td class="textblack">Select Exam</td>
                                        <td>
                                            <select name="Exam" id="Exam" style="width: 300px">
                                                <option value="-1">.....Select.....</option>
                                                <% ArrayList<ExamData> ExamList=moderation.getExams();
                                                            int i=1;
                                                            int count=ExamList.size();
                                                            if(count>0){
                                                                    while(i<=count)
                                                                        {
                                                                        ExamData exam=ExamList.get(i-1);
                                                            %>
                                                           <option <%if(moderation.getExam().equals(exam.ExamId)) { out.print("selected");}  %>
                                                                value="<%=exam.ExamId %>"><%=exam.ExamName %></option>

                                                            <%i++; }}
                                                            %>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textblack">Select Course</td>
                                        <td>
                                            <select name="Course" id="Course" style="width: 250px" onchange="ChangeYearOrSem();">
                                                <option value="-1">.....Select.....</option>
                                                            <% ArrayList<CourseData> CourseList=moderation.getBranchList();
                                                            i=0;
                                                            count=CourseList.size();

                                                                    while(i<count)
                                                                        {
                                                                        CourseData Course=CourseList.get(i);
                                                            %>
                                                            <option <%if(Integer.parseInt(moderation.getBranch())==Course.BranchId) { out.print("selected");} %>
                                                                value="<%=Course.BranchId %>"><%=Course.BranchName %></option>

                                                            <%i++; }
                                                            %>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textblack">Select Year/Semester</td>
                                        <td>
                                            <div id="YearOrSem">
                                                <select  name="YearSem" id="YearSem" style="width: 115px" >
                                                    <option value="-1">.....Select.....</option>
                                                    <%
                                                    count=moderation.getMaxYearOrSemForCourse();
                                                    i=1;
                                                    while(i<=count)
                                                        {%>
                                                        <option <%if(moderation.getSemYear()==i){ out.print("selected");}  %> value=<%=i%>><%=i %></option>
                                                                <% i++;}%>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td align="center" >
                                            <input type="Submit" name="Submit" id="Submit" value="Search">
                                        </td>
                                    </tr>
                                </table>
                                                <%
                                                    if(moderation.IsMarkVerificationCompletedForBranch())
                                                    {
                                                    ArrayList<Subject> SubList=moderation.getSubjectsForCourse();
                                                    int Count=SubList.size();i=0;
                                                    if(Count>0)
                                                        {%>
                                                        <center>
                                             <table border="1"  style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack" width="80%">
                                                        <tr  bgcolor="#ddcccc" align="center">
                                                            <th class="textblack" align="left">Select</th>
                                                            <th class="textblack" align="left">Subject Name</th>
                                                        </tr>
                                                        <%while(i<Count)
                                                            {
                                                            Subject Sub=SubList.get(i);
                                                %>
                                                            <tr>
                                                                <td class="textblack"  align="left"><input type="checkbox" name="Select"id="Select" Value="<%=Sub.SubjectBranchId %>" > </td>
                                                                <td class="textblack"><%=Sub.SubjectName %></td>
                                                            </tr> 
                                               <%i++;}%>
                                               <tr>
                                                   <td class="textblack" colspan="2" align="left">
                                                       <input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="return SelectAllRows(this.form) "><b>Select All</b>
                                                   </td>
                                               </tr>
                                             </table>
                                               <table width="80%">
                                                    <tr>
                                                       <td class="textblack">Total Moderation to be Applied</td>
                                                       <td>
                                                           <input type="text" name="TotalModeration" id="TotalModeration" value="" maxlength="2" style="width: 100px" onkeypress="return chkNumberOnly(event);">
                                                       </td>
                                                   </tr>
                                                   <tr>
                                                       <td class="textblack">Maximum Moderation allowed for a Subject</td>
                                                       <td>
                                                           <input type="text" name="SubjectMax" id="SubjectMax" value="" maxlength="2" style="width: 100px" onkeypress="return chkNumberOnly(event);">
                                                       </td>
                                                   </tr>
                                                   <tr>
                                                       <td class="textblack" colspan="2" align="right">
                                                           <input type="submit" name="Add" id="Add" value="Add">
                                                       </td>
                                                   </tr>
                                               </table>
                                                        </center></fieldset></td></tr>

                                             <tr><td><center>
                                               <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Define the Rule Set</b></span></legend>
                                               <table width="80%">
                                                  
                                                   <tr>
                                                   <td align="left" class="textblack" >
                                                       <input type="checkbox" name="ForDistinction" id="ForDistinction" value="1" onclick="return EnableClassModeration(this.form)">
                                                        Moderation/Marks To be applied for improving the percentage of of  students with Distinction
                                                   </td>
                                                   <td>
                                                       <input type="text" name="Distinction" id="Distinction" value="" maxlength="2" style="width: 60px" readonly="true" onkeypress="return chkNumberOnly(event);">
                                                   </td>
                                               </tr>
                                               <tr>
                                                   <td align="left" class="textblack" >
                                                       <input type="checkbox" name="ForFirst" id="ForFirst" value="1" onclick="return EnableClassModeration(this.form)">
                                                        Moderation/Marks To be applied for improving the percentage of of  students with First Class
                                                   </td>
                                                   <td>
                                                       <input type="text" name="First" id="First" value="" maxlength="2" style="width: 60px" readonly="true" onkeypress="return chkNumberOnly(event);">
                                                   </td>
                                               </tr>
                                               <tr>
                                                   <td align="left" class="textblack" >
                                                       <input type="checkbox" name="ForSecond" id="ForSecond" value="1" onclick="return EnableClassModeration(this.form)">
                                                        Moderation/Marks To be applied for improving the percentage of  students with Second class
                                                   </td>
                                                   <td>
                                                       <input type="text" name="Second" id="Second" value="" maxlength="2" style="width: 60px" readonly="true" onkeypress="return chkNumberOnly(event);">
                                                   </td>
                                               </tr>
                                                   <tr>
                                                       <td colspan="2">
                                                   <table style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack" width="100%" align="center">
                                                       <tr>
                                                            <td colspan="2" class="textblack">
                                                                   <input type="radio" name="Class" id="Class" Value="SemPass">Moderation can be applid only if student passes for Semester
                                                            </td>
                                                        </tr>
                                                       <tr>
                                                       <td  class="textblack">
                                                           <input type="radio" name="Class"id="Class" checked="true" Value="SubjectPass">Moderation can be applid for Subject Pass
                                                       </td>
                                                       </tr>
                                                     
                                                   </table>
                                                   </td>
                                                   </tr>
                                                   <tr>
                                                       <td class="textblack">
                                                            <input type="checkbox" name="SeperateMinimum" id="SeperateMinimum" value="1">Moderation to be affected on External as well as Internal marks
                                                       </td>
                                                   </tr>
                                                   <tr>
                                                       <td class="textblack">
                                                            <input type="checkbox" name="InternalChange" id="InternalChange" value="1">Internal to be changed when external mark is changed by the effect of moderation
                                                       </td>
                                                   </tr>
                                                   <tr>
                                                       <td align="center" colspan="2">
                                                       <input type="Submit" name="SaveModeration" id="SaveModeration" value="Save Moderation">
                                                       </td>
                                                   </tr>
                                               </table>
                                                   
                                                   </fieldset>
                                              
                                                    <%}}else
                                        {if(request.getParameter("YearSem")!=null){%>
                                        <center>
                        <table><tr>
                            <td></td>
                            <td colspan="" class="textblack"><span  class="Error" style="color:red">*Mark Verification has not yet Completed for this Course* </span></td>
                            </tr><br/></table> </center><% } }%>
                            </center>
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
</tbody></table><% }%>
</td></tr>
</tbody></table>
</body></html>
<jsp:include page="footer.jsp"/><% }%>
