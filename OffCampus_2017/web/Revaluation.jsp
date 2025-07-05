<%--
    Document   : MarkVerificationSO
    Created on : Sep 29, 2011, 2:23:23 PM
    Author     : mgu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
if(request.getSession().getAttribute("UserName")==null)
{
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}
else
{
     LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=32)
    {
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
%>
<html><head>
		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/MarkEntry.js"></script>
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
<%
RevaluationVerification mark=new RevaluationVerification(request, response);
%>
            <form id="Revaluation" name="Revaluation" action="Revaluation.jsp" method="post">

                <center>
                    <table  border="1" width="98%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" colspan="3" > <b> <font color="#a02a28"> ReValuation By SO</font></b></td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset style="width:90%"><legend><span style="color:#a02a28; font-size: small"><b>Search</b></span></legend>
                                        <table width="100%">

                                        <tr></tr>
                                            <tr>
                        <td class="textblack" align="left">Select Exam</td>
                        <td align="left">
                            <select name="Exam" id="Exam" style="width: 250px">
                                <option value="-1">.....Select.....</option>
                                <% ArrayList<ExamData> ExamList=mark.getAllExams();
                                            int i=1;
                                            int count=ExamList.size();
                                            if(count>0){
                                                    while(i<=count)
                                                        {
                                                        ExamData exam=ExamList.get(i-1);
                                            %>
                                           <option <%if(mark.getExam().equals(exam.ExamId)) { out.print("selected");}  %>
                                                value="<%=exam.ExamId %>"><%=exam.ExamName %></option>

                                            <%i++; }}
                                            %>
                            </select>
                        </td>
                        <%if(mark.ExamError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.ExamError()%></span> </td>
                        <% } %>
                        </tr>

                                <tr>
                                <td class="textblack" align="left">Course</td>
                                <td align="left">
                              <select name="Course" id="Course" style="width: 250px" onchange="ChangeYearOrSemForMarkEntry();">
                                <option value="-1">.....Select.....</option>
                                            <% ArrayList<Entity.CourseData> CourseList=mark.getBranchList();
                                            i=0;
                                            count=CourseList.size();

                                                    while(i<count)
                                                        {
                                                       Entity.CourseData Course=CourseList.get(i);
                                            %>
                                            <option <%if(Integer.parseInt(mark.getBranch())== Course.BranchId)
                                           { out.print("selected");}%>
                                                value="<%=Course.BranchId %>"><%=Course.BranchName %></option>

                                            <%i++; }
                                            %>

                            </select>
                        </td>
                                <td class="textblack" align="left">
                                            PRN
                                        </td>
                                        <td class="textblack" align="left">
                                            <input type="text" name="PRN" id="PRN" value="<%=mark.getPRN() %>" style="width: 215px" maxlength="13">
                                        </td>
                            </tr>

                              <tr>
                        <td class="textblack" align="left">Select Syllabus</td>
                        <td align="left">
                            <div id="Acdemic_Year" align="left">
                                <select  name="AcdemicYear" id="AcdemicYear" style="width: 115px">
                                    <option value="-1">.....Select.....</option>
                                    <option value="2010" <%if(mark.getAcademicYear().equals("2010")){out.print("selected");}%>>2010</option>
                                    <option value="2011" <%if(mark.getAcademicYear().equals("2011")){out.print("selected");}%>>2011</option>
                                </select>
                            </div>
                        </td>
                        <%if(mark.AcdemicYearError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.AcdemicYearError()%></span> </td>
                        <% } %>
                    </tr>
                       <tr>
                        <td class="textblack" align="left">Select Year/Semester</td>
                        <td align="left">
                            <div id="YearOrSem" align="left">
                                <select  name="YearSem" id="YearSem" style="width: 115px" onchange="ChangeSubjectListNew();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                    count=mark.getMaxYearOrSemForCourse();
                                    i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(Integer.parseInt(mark.getSemYear())==i) { out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++;}%>
                                </select>
                            </div>
                        </td>
                        <%if(mark.SemError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.SemError()%></span> </td>
                        <% } %>
                    </tr>

                                    <tr>

                                        <td class="textblack" align="left">Subject</td>
                                <td align="left">
                                    <div id="SubjectList" align="left">
                                    <select name="Subject" id="Subject" style="width: 270px">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubList=mark.getSubjectsForCourseAcdemicYear();
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
                                           <option <%if(mark.getSubject().equals(sub.SubjectId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>
                                </td>
                                        


                                     
                                        <td colspan="4" align="right">
                                            <input type="submit" name="Search" name="Search" value="Search">
                                        </td>
                                    </tr>
                                </table>
                                    </fieldset>
                                    <br>
                                    <center>
                                     
                                        <br>
                                         <%
                    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
                    StudentList=mark.getRevaluationBySO();
                    int Count= StudentList.size();i=0;
                    if(Count==0)
                        {%>
                        <center>
                       <table>
                          <tr class="textblack"  align="center"><span style="color:#a02a28; font-size: small"><b>No Marks to Display</b></span></tr>
                       </table>
                   </center>

                        <% }else{
                           %>
                     <table border="1" width="98%" style="border-style: groove" cellspacing="0" cellpadding="3">
                         <tr class="textblack"  align="center"><span style="color:#a02a28; font-size: small"><b>Total number of Records to be verified : <%=mark.getTotalCountOfRecordsToBeVerifiedBySORevaluation() %></b></span></tr>
                    <tr class="textblack" bgcolor="#ded4b8" align="center">
                         <th>Select</th>
                         <th>Sl No</th>
                         <th>PRN</th>
                         <th>StudentName</th>
                         <th>Subject</th>
                         <th>External</th>
                         <th>Internal</th>
                          <th>Ext-Mod</th>
                         <th>Int_Mod</th>
                         <th>Total </th>
                         <th>Edit </th>
                    </tr>
                    <% while(i<Count)
                        {
                        StudentSubjectMark stu=StudentList.get(i);
                    %>
                    <tr id="MarkList" name="MarkList">
                         <td class="textblack" align="left"><input type="checkbox" name="Select"id="Select" Value="<%=stu.StudentSubjectMarkId %>"> </td>
                        <td class="textblack" align="left"><%=i+1%></td>
                        <td class="textblack" align="left"><%=stu.PRN %></td>
                        <td class="textblack" align="left"><%=stu.StudentName  %></td>
                        <td class="textblack" align="left"><%=stu.SubjectName %></td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="external" name="external" value="<%=stu.ExternalMark %>" readonly="true"  style="width:100%;" onkeypress="return chkNum(event);" onblur="CalculateInternalForVerificationRevaluation(<%=i%>);"></td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="internal" name="internal" value="<%=stu.InternalMark %>" readonly="true"  style="width:100%;" onkeypress="return chkNum(event);"></td>
                         <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="externalmod" name="externalmod" value="<%=stu.ExternalMod %>" readonly="true"  style="width:100%;" onkeypress="return chkNum(event);"></td>
                         <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="internalmod" name="internalmod" value="<%=stu.InternalMod %>" readonly="true"  style="width:100%;" onkeypress="return chkNum(event);"></td>
                         <td class="textblack" align="left"><%=Double.parseDouble(stu.ExternalMark)+ Double.parseDouble(stu.InternalMark)+Double.parseDouble(stu.ExternalMod)+Double.parseDouble(stu.InternalMod)%></td>
                         <td class="textblack" align="left"><input type="button" name="SubmitEdit" id="SubmitEdit" value="Edit" onclick="return OpenTextForEditRevaluation(<%=i%>);"/> </td>
                    </tr>
                    <input type="hidden" id="SubjectBranchId" name="SubjectBranchId" value="<%=stu.SubjectBranchId %>">
                    <% i++;}%>
                     <tr>
                         <td colspan="11"class="textblack" align="left"><input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="SelectAllRowsRevaluation(this.form);"><b>SelectAll</b> </td>
                    </tr>
                    <tr>
                        <td colspan="11" align="right">
                            <input type="submit" name="Submit" name="Submit" value="Approve">
                        </td>
                    </tr>
                    <% }%>
                </table> </center>
                                    </center>
                            </fieldset>
                        </td>

                    </tr>

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

