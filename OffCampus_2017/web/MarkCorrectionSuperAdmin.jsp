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
if(Role!=35 )
    {
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
%>
<html><head>
		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/ExamRegistration.js"></script>
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
    <jsp:include page="HeaderAdmin.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">

              <jsp:include page="NavigationAdminSysAdmin.jsp"/>

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
MarkVerification mark=new MarkVerification(request, response);
%>
            <form id="MarkCorrectionSuperAdmin" name="MarkCorrectionSuperAdmin" action="MarkCorrectionSuperAdmin.jsp" method="post">

                <center>
                    <table  border="1" width="98%" >
                    <tr  align="center">
                        <td style="height: 20px;background-color:#d9bf79;column-span: 3 " > <b> <font color="#a02a28"> MarkCorrectionSuperAdmin</font></b></td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset><legend><span style="color:#a02a28; font-size: small"><b> List of Marks</b></span></legend>
                                <center>
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
                       <%if(mark.getError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.getError()%></span> </td>
                        <% } %>
                        </tr>

                                <tr>
                                <td class="textblack" align="left">Course</td>
                                <td align="left">
                              <select name="Course" id="Course" style="width: 250px" onchange="ChangeYearOrSemForMarkEntry();">
                                <option value="-1">.....Select.....</option>
                                            <% ArrayList<CourseData> CourseList=mark.getBranchList();
                                            i=0;
                                            count=CourseList.size();

                                                    while(i<count)
                                                        {
                                                        CourseData Course=CourseList.get(i);
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
                                    <option value="2012" <%if(mark.getAcademicYear().equals("2012")){out.print("selected");}%>>2012</option>
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
                                        <option <%if(Integer.parseInt(mark.getSemYear())==i) { out.print("selected");}  %> value=<%=i %><%=i %></option>
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
                                            <option <%if(Integer.parseInt(mark.getSubject())==Integer.parseInt(sub.SubjectId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                
                            </select>
                                 </div>
                                </td>
                                        <td class="textblack" align="left" >
                                            Assistant
                                        </td >
                                        <td class="textblack" align="left">
                                            <select name="Assistant" id="Assistant" style="width: 215px" >
                                            <option value="-1">.....Select.....</option>
                                            <% ArrayList<User> UserList=mark.getAllAssistantsofSO();
                                            i=0;
                                            if(UserList!=null)
                                                {
                                            count=UserList.size();
                                            if(count!=0)
                                                {
                                                    while(i<count)
                                                        {
                                                        User us=UserList.get(i);
                                            %>
                                           <option <%if(mark.getAssistant().equals(us.UserName)) { out.print("selected");}%>
                                                value="<%=us.UserName %>"><%=us.Name %></option>

                                            <%i++; }}}
                                            %>
                                            </select>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td colspan="4" align="right">
                                            <input type="submit" name="Search"  value="Search">
                                        </td>
                                    </tr>

                                </table>
                                    </fieldset>
                                    <br>
                                    <center>
                                        <font class="textblack" color="#a02a28"><b>
                                                <%if(request.getParameter("Assistant")!=null && (!request.getParameter("Assistant").equals("-1"))){ %>
                                                Total Records Verified by <%= request.getParameter("Assistant")%>: <%=mark.getCountOfRecordsVerifiedByAssistantinSoPanel() %><% } else{%>
                                                <div id="recordCount"></div><% }%>
                                            </b></font>
                                        <br>
                                         <%
                                                if(mark.getSearch()){
                    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
                    StudentList=mark.getMarksForCorrectionAdmin();
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
                     <table border="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">
                        <!-- <tr class="textblack"  align="center"><span style="color:#a02a28; font-size: small"><b>Total number of Records to be verified : <%=mark.getTotalCountOfRecordsToBeVerifiedBySO() %></b></span></tr>-->
                    <tr class="textblack" bgcolor="#e3cd93" align="center">
                         <th>Select</th>
                         <th>Sl No</th>
                         <th>PRN</th>
                         <th>StudentName</th>
                         <!--<th>StSubMarkId</th>-->
                        
                         <th>Sem</th>
                         
                         <th>Exam</th>
                         <!--<th>MarkStatus</th>-->
                         <th>Paper</th>
                         <th>Subject</th>
                         <th>External</th>
                         <th>Internal</th>
                         <th>Practical</th>
                         <th>TourReport</th>
                         <th>ExtMod</th>
                         <th>IntMod</th>
                         <th>ClassMod</th>
                         <th>Total </th>
                        <th>Absent </th>
                        <th>Pract Absent </th>
                    </tr>
                    <% while(i<Count)
                        {
                        StudentSubjectMark stu=StudentList.get(i);
                    %>
                    <tr id="MarkList" name="MarkList" <%if(stu.IsAbsent .equals("1")){ %> bgcolor="#dedede"<%}if(stu.IsReValuated .equals("1")){ %> bgcolor="#bf9"<%}if(stu.IsPostCorrected .equals("1")){ %> bgcolor="#eb9"<%} if(stu.MarkStatus  .equals("1")){ %> bgcolor="#f7f7f7"<%}%>>
                        <td class="textblack" align="left"><input type="checkbox" name="Select" id="Select" Value="<%=stu.StudentSubjectMarkId %>"> </td>
                        <td class="textblack" align="left"><%=i+1%></td>
                        <td class="textblack" align="left"><%=stu.PRN %></td>
                        <td class="textblack" align="left"><%=stu.StudentName  %></td>
                        <!--<td class="textblack" align="left"><%=stu.StudentSubjectMarkId %></td>-->
                        <td class="textblack" align="left"><%=stu.CurrentYearSem %></td>
                        
                        <td class="textblack" align="left"><%=stu.ExamName %></td>
                        <td class="textblack" align="left"><%=stu.PaperNo%></td>
                         <!--<td class="textblack" align="left"><%=stu.MarkStatus %></td>-->
                        <td class="textblack" align="left"><%=stu.SubjectName %></td>
                        <% if(stu.IsAbsent.equals("1")){%>
                        

                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="external" name="external" value="<%=stu.ExternalMark %>" readonly="true"  style="width:90%;" </td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="internal" name="internal" value="<%=stu.InternalMark %>" readonly="true"  style="width:90%;" ></td>
                         <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="Practical" name="Practical" value="<%=stu.PracticalMark %>" readonly="true"  style="width:90%;" </td>
                          <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="TourReport" name="TourReport" value="<%=stu.TourReportMark %>" readonly="true"  style="width:90%;" ></td>
                           <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="ExtMod" name="ExtMod" value="<%=stu.ExternalMod %>" readonly="true"  style="width:90%;" ></td>
                            <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="IntMod" name="IntMod" value="<%=stu.InternalMod %>" readonly="true"  style="width:90%;" ></td>
                             <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="ClassMod" name="ClassMod" value="<%=stu.ClassMod %>" readonly="true"  style="width:90%;" ></td>

                        <td class="textblack" align="left"> </td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="Absent" name="Absent" value="<%=stu.IsAbsent %>" readonly="true"  style="width:90%;" ></td>
                       <%}else{%>
                                 <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="external" name="external" value="<%=stu.ExternalMark %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);" onblur="CalculateInternalForVerification(<%=i%>);"></td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="internal" name="internal" value="<%=stu.InternalMark %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);"></td>
                         <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="Practical" name="Practical" value="<%=stu.PracticalMark %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);"></td>
                          <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="TourReport" name="TourReport" value="<%=stu.TourReportMark %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);"></td>
                           <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="ExtMod" name="ExtMod" value="<%=stu.ExternalMod %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);"></td>
                            <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="IntMod" name="IntMod" value="<%=stu.InternalMod %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);"></td>
                             <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="ClassMod" name="ClassMod" value="<%=stu.ClassMod %>" readonly="true"  style="width:90%;" onkeypress="return chkNum(event);"></td>

                        <td class="textblack" align="left"><%=Double.parseDouble(stu.ExternalMark)+ Double.parseDouble(stu.InternalMark)+ Double.parseDouble(stu.ExternalMod)+ Double.parseDouble(stu.InternalMod)+ Double.parseDouble(stu.ClassMod)+ Double.parseDouble(stu.PracticalMark)+ Double.parseDouble(stu.TourReportMark)%></td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="Absent" name="Absent" value="<%=stu.IsAbsent %>" readonly="true"  style="width:90%;" ></td>
                        <td class="textblack" style="width:60px;"><input type="text" maxlength="5" id="PractAbsent" name="PractAbsent" value="<%=stu.IsPracticalAbsent %>" readonly="true"  style="width:90%;" ></td>
                      <% }%>
                    </tr>
                    <input type="hidden" id="SubjectBranchId" name="SubjectBranchId" value="<%=stu.SubjectBranchId %>">
                    <% i++;}%>
                     <tr>
                       <td colspan="18"class="textblack" align="left"><input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="SelectAllRows(this.form);"><b>SelectAll</b> </td>
                    </tr>
                    <tr>
                        <td colspan="9" align="right">
                            <input type="submit" name="Submit" name="Submit" value="ForReValuation" >
                        </td>
                          <td colspan="9" align="right">
                            <input type="submit" name="Submit" name="Submit" value="ForPostCorrection" >
                        </td>
                    </tr>
                    <% }}%>
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

