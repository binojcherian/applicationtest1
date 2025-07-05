<%--
    Document   : ResultStatistics
    Created on : Dec 3, 2011, 12:26:29 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<% if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
Statistics stat=new Statistics(request, response);
%>
<html><head>

		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/Statistics.js"></script>
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
	    </head><body onload="ChangeModerationType()">
	<!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<jsp:include page="Header_SO.jsp"/>
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
            <center>
                <form id="ResultStatistics" name="ResultStatistics" action="ResultStatisticsforMCA.jsp" method="post">
                    <table  border="1" width="95%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Result Statistics</font></b></td>
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
                                                <% ArrayList<ExamData> ExamList=stat.getExams();
                                                            int i=1;
                                                            int count=ExamList.size();
                                                            if(count>0){
                                                                    while(i<=count)
                                                                        {
                                                                        ExamData exam=ExamList.get(i-1);
                                                            %>
                                                           <option <%if(stat.getExam().equals(exam.ExamId)) { out.print("selected");}  %>
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
                                                            <% ArrayList<Entity.CourseData> CourseList=stat.getBranchList();
                                                            i=0;
                                                            count=CourseList.size();

                                                                    while(i<count)
                                                                        {
                                                                        Entity.CourseData Course=CourseList.get(i);
                                                            %>
                                                            <option <%if(Integer.parseInt(stat.getBranch())==Course.BranchId) { out.print("selected");} %>
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
                                                    count=stat.getMaxYearOrSemForCourse();
                                                    i=1;
                                                    while(i<=count)
                                                        {%>
                                                         <option <%if(Integer.parseInt(stat.getSemYear())==i){ out.print("selected");}  %> value=<%=i%>><%=i %></option>
                                                        </option>
                                                                <% i++;}%>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td align="center" >
                                            <input type="Submit" name="Submit" id="Submit" value="Submit">
                                        </td></tr>
                                    <tr>
                                        <td colspan="4">
                                    <div id="MarkStatistics">
<%

    ArrayList<ResultStatistics> Result=stat.getStatisticsForCourse();
    int Count=Result.size();i=0;
    if(Count!=0){%>
    <table BORDER="1" style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack" width="100%">
        <tr align="center">
             <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Statistics of Tabulated Marks for the Conduct of Pass Board Meeting</font></b></td>
        </tr>
        <tr>
            <td align="center">
       <center>
        <table width="100%" cellspacing="0" cellpadding="3" class="textblack">
            <tr >
                <td height="20px" class="textblack">Number of Candidates : <b> <%=stat.getTotalCountOfStudentsForCourse() %></b></td>
                <td height="20px" class="textblack"> Number of Candidates Passed :<b> <%=stat.getTotalPassForCourse() %></b></td>
            </tr>
            <tr>
                <td height="20px"  class="textblack">Number of Candidates above 80% : <b><%=stat.getCountofStudentsAboveEightyPercentage() %></b></td>
                <td height="20px"  class="textblack">Number of Candidates above 75% : <b><%=stat.getCountofStudentsAboveSeventyFivePercentage() %></b></td>
            </tr>
            <tr>
                <td height="20px" class="textblack">Number of Candidates above 60% :<b><%=stat.getCountofStudentsAboveSixtyPercentage() %></b></td>
                <td height="20px" class="textblack">Number of Candidates above 50% :<b><%=stat.getCountofStudentsAboveFiftyPercentage() %></b></td>
            </tr>
        </table>
       </center>
                </td>
        </tr>
        <tr>
            <td>
                <center>
    <table BORDER="1"  style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack">
        <tr  bgcolor="#ddcccc" align="center">
            <th class="textblack">SlNo</th>
            <th class="textblack">Subject Code</th>
            <th class="textblack">Subject Name</th>
            <th class="textblack">Total Registration</th>
            <th class="textblack">Total Pass</th>
            <th class="textblack">Pass %</th>
        </tr>
    <% while(i<Count){
        ResultStatistics ReStat=Result.get(i);
                                        %>
                                        <tr>
                                            <td class="textblack"><%=i+1%></td>
                                            <td class="textblack"><%=ReStat.SubjectCode %></td>
                                            <td class="textblack"><%=ReStat.SubjectName %></td>
                                            <td class="textblack"><%=ReStat.TotalReg %></td>
                                            <td class="textblack"><%=ReStat.TotalPass %></td>
                                            <td class="textblack"><%=ReStat.PassPercentage %></td>
                                        </tr>
                                    <% i++;}}%>
                                    </table></center></td></tr>
                                    <tr>
                                        <td align="center">
                                        <table    cellspacing="0" cellpadding="3" class="textblack">
                                            <tr>
                                                <td class="textblack" align="center">
                                                    <input type="radio" name="ModerationType" id="ModerationType" <%if(stat.IsBranchModeration()){out.print("checked");}%> value="0" onclick="ChangeModerationType()" >Semester Moderation
                                                    <input type="radio" name="ModerationType" id="ModerationType" <%if(!stat.IsBranchModeration()){out.print("checked");}%> value="1" onclick="ChangeModerationType()">Subject Moderation
                                                </td>
                                            </tr>
                                        </table>
                                        </td>
                                    </tr>
                                    <tr >
                                        <td>
                                            <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Prepare For Moderation</b></span></legend>
                                              <div id="BranchWiseModeration" >
                                                <table>

                                                    <tr id="SubjectList" <%if(stat.IsBranchModeration()){out.print("style=\"display: none\"");} %>>
                                                                <td class="textblack">Select Subject</td>
                                                                <td>

                                                                      <select name="Subject" id="Subject" style="width: 250px" onchange="getStatusOfAbsEntry();">
                                                                         <option value="-1">.....Select.....</option>
                                                                                    <% ArrayList<Subject> SubList=stat.getSubjectsForCourse();
                                                                                    i=0;
                                                                                    if(SubList!=null)
                                                                                        {
                                                                                    count=SubList.size();
                                                                                    if(count!=0)
                                                                                        {
                                                                                            while(i<count)
                                                                                                {
                                                                                                Subject sub=SubList.get(i);
                                                                                    %>
                                                                                   <option <%if(stat.getSubject().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                                                        value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                                                                    <%i++; }}}
                                                                                    %>
                                                                         %>
                                                                    </select>

                                                                </td>
                                                             </tr>
                                                    <tr>
                                                        <td class="textblack">Enter Moderation Range: From
                                                            <input type="text" id="ModerationFrom" name="ModerationFrom" value="<%=stat.getModerationFrom() %>" maxlength="2" style="width: 100px" onkeypress="return chkNumForInt(event);">
                                                        </td>
                                                        <td class="textblack">To
                                                            <input type="text" id="ModerationTo" name="ModerationTo" value="<%=stat.getModerationTo() %>" maxlength="2" style="width: 100px" onkeypress="return chkNumForInt(event);">
                                                        </td>
                                                        <td class="textblack">
                                                            <input type="submit" name="Calculate" id="Calculate" value="Calculate" onclick="return ChechSubject(this.form);">
                                                        </td>
                                                    </tr>
                                                    <tr id="BranchModeration" <%if(!stat.IsBranchModeration()){out.print("style=\"display: none\"");} %>>
                                                        <td>
                                                        <%
                                                        ArrayList<StatisticsWithModeration> Statistics =stat.getStatisticsForBranchWithModeration();
                                                        Count=Statistics.size();i=0;
                                                        if(Count>0)
                                                            {%>
                                                    <table id="BranchM" style="border-style: groove" cellspacing="0" cellpadding="3" width="100%" border="1">
                                                        <tr bgcolor="#ddcccc">
                                                            <th class="textblack" >SlNo</th>
                                                            <th class="textblack" >Applied Moderation</th>
                                                            <th class="textblack" >Total Pass</th>
                                                            <th class="textblack" >Failed</th>
                                                            <th class="textblack" >Pass Percentage</th>

                                                        </tr>
                                                       <% while(i<Count)
                                                            {
                                                               StatisticsWithModeration StatResult=Statistics.get(i);
                                                                %>
                                                        <tr>
                                                            <td class="textblack"><%=i+1 %></td>
                                                            <td class="textblack"><%=StatResult.AppliedModeration %></td>
                                                            <td class="textblack"><%=StatResult.TotalPass %></td>
                                                            <td class="textblack"><%=StatResult.TotalFail %></td>
                                                            <td class="textblack"><%=StatResult.PassPercentage %></td>
                                                        </tr>
                                                            <% i++;}%>
                                                    </table>
                                                            <% }%>
                                                        </td>
                                                        </tr>
                                                         <tr id="SubjectModeration" <%if(stat.IsBranchModeration()){out.print("style=\"display: none\"");} %>>
                                                             <table id="SubjectM" style="border-style: groove" cellspacing="0" cellpadding="3" width="100%" border="1" >
                                                             <%
                                                                ArrayList<StatisticsWithModeration> SubjectStatistics =stat.getStatisticsForSubjectWithModeration();
                                                                Count=SubjectStatistics.size();i=0;
                                                                if(Count>0)
                                                            {%>

                                                        <tr bgcolor="#ddcccc">
                                                            <th class="textblack" width="20%">SlNo</th>
                                                            <th class="textblack" width="20%">Applied Moderation</th>
                                                            <th class="textblack" width="20%">Total Pass</th>
                                                            <!--<th class="textblack" width="20%">Failed</th>
                                                            <th class="textblack" width="20%">Pass Percentage</th>-->
                                                            <th class="textblack" width="20%">Total Pass:<%=stat.getSubjectName() %></th>
                                                        </tr>
                                                         <% while(i<Count)
                                                            {
                                                               StatisticsWithModeration StatResult=SubjectStatistics.get(i);
                                                                %>
                                                        <tr>
                                                            <td class="textblack"><%=i+1 %></td>
                                                            <td class="textblack"><%=StatResult.AppliedModeration %></td>
                                                            <td class="textblack"><%=StatResult.TotalPass %></td>
                                                           <!-- <td class="textblack"><%=StatResult.TotalFail %></td>
                                                            <td class="textblack"><%=StatResult.PassPercentage %></td>-->
                                                            <td class="textblack"><%=StatResult.TotalSubjectPass %></td>
                                                        </tr>
                                                            <% i++;}%>
                                                        </table><% }%>
                                                        </tr>
                                                </table>
                                            </div>
                                            </fieldset>
                                        </td>
                                    </tr>

                                </table>


                                    </div>

                                        </td>
                                    </tr>

                                </table>
                            </fieldset>

                        </td></tr>
                    </table>
                </form>
            </center>
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
