<%--
    Document   : MarkEntryForPRN
    Created on : Oct 7, 2011, 3:10:15 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<%
if(request.getSession().getAttribute("UserName")==null)
{
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
    String PrivilegeMessage=null;
    LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=33)
    {
        PrivilegeMessage="YOU ARE NOT PERMITTED TO ACCESS THIS PAGE";
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
    String StudentSubjectMarkId;
    String SubjectBranchId=null;

    boolean Edit=false;
    if(request.getParameter("StudentSubjectMarkId")!=null)
        {
            Edit=true;
            StudentSubjectMarkId=request.getParameter("StudentSubjectMarkId");
        }
%>
<html>
     
    <head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/MarkEntry.js"></script>
                <script type="text/javascript" src="./js/IPAddress.js"></script>
            

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
	    </head><body onload="ViewSubjectTotalCountDetailsNew();">
	<!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<jsp:include page="Header_Assistant.jsp"/>
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
            <form id="MarkEntryForPRN" name="MarkEntryForPRN" action="MarkEntryForPRN.jsp" method="post" >

<%
MarkEntryPRNBckUp mark=new MarkEntryPRNBckUp(request, response);
int CountMark=mark.TotalRecordsEnteredByUser();

%>
                <input type="hidden" name="Count" id="Count" value="<%=mark.TotalRecordsEnteredByUser() %>" >
                <input type="hidden" name="Edit" id="Edit" value="<%=Edit %>" >
                 <input type="hidden" id="showalert" name="showalert">
                <input type="hidden" name="StudentSubjectMarkId" id="StudentSubjectMarkId" value="<%=request.getParameter("StudentSubjectMarkId") %>" >
<center>
                    <table  border="1" width="90%" style="border-style: groove" cellspacing="0" cellpadding="3">
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" colspan="3"> <b> <font color="#a02a28">Mark Entry</font></b></td>
                    </tr>
                    <tr>
                        <td>
                    <fieldset><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                        <center>
                        <table>
                       <tr>
                        <td class="textblack">Select Exam</td>
                        <td align="left">
                            <select name="Exam" id="Exam" style="width: 250px">
                                <option value="-1">.....Select.....</option>
                                <% ArrayList<ExamData> ExamList=mark.getExams();
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
                                <td class="textblack">Course</td>
                                <td>
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
                        <%if(mark.BranchError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.BranchError()%></span> </td>
                        <% } %>

                            </tr>


                            <tr>
                        <td class="textblack">Select Syllabus</td>
                        <td>
                            <div id="Acdemic_Year">
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
                        <td class="textblack">Select Year/Semester</td>
                        <td>
                            <div id="YearOrSem">
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
                                <td class="textblack">Subject</td>
                                <td>
                                     <div id="SubjectList"><!--onchange="ViewSubjectTotalCountDetails();"-->
                                         <select name="Subject" id="Subject" style="width: 270px" onchange="ViewSubjectTotalCountDetailsNew();">
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

                            </select>
                                 </div>
                        </td>
                        <%if(mark.SubjectError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.SubjectError()%></span> </td>
                        <% } %>
                            </tr>
                            <tr>
                                <td class="textblack">PRN</td>
                                <td>
                                    <input type="text" name="PRN" id="PRN" value="<%=mark.getPRN() %>" style="width: 215px" maxlength="13">
                                </td>
                                <%if(mark.PRNError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.PRNError()%></span> </td>
                        <% } %>
                            </tr>
                            <tr>
                                <td class="textblack">External Mark</td>
                                <td>
                                    <input type="text" name="Mark" id="Mark" value="<%=mark.getMark() %>" style="width: 115px" maxlength="5" onkeypress="return chkNum(event);" onblur="CalculateInternalNew();" ><div id="Status"></div>
                                </td>
                                <%if(mark.MarkError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.MarkError()%></span> </td>
                        <% } %>
                            </tr>
                            <tr>
                                <td class="textblack">Internal Mark</td>
                                <td>
                                    <input type="text" name="InternalMark" id="InternalMark" readonly="true" value="<%=mark.getInternalMark() %>" style="width: 115px" maxlength="2" onkeypress="return chkNumForInt(event);">
                                    <input type="Submit" name="btnEdit" id="btnEdit" value="Edit"  style="visibility:hidden" onclick="return EnableEditForInternal();">
                                    <input type="hidden" name="InternalEdit" id="InternalEdit" value="">
                                </td>
                            </tr>
                            <%-- Practicalmark --%>
                            <tr>
                                <td class="textblack">Practical Mark</td>
                                <td>
                                    <input type="text" name="PracticalMark" id="PracticalMark" readonly="true" value="<%=mark.getInternalMark() %>" style="width: 115px" maxlength="2" onkeypress="return chkNumForInt(event);">
                                    <input type="Submit" name="btnEdit" id="btnEdit" value="Edit"  style="visibility:hidden" onclick="return EnableEditForPractical();">
                                    <input type="hidden" name="PracticalEdit" id="PracticalEdit" value="">
                                </td>
                            </tr>
                            <tr><td class="textblack">Remarks</td>
                                                                        <td><textarea name="Remarks" rows="2" cols="35"  ><%=mark.getRemarks() %></textarea>
                                                                        </td>
                                                                        <%if(mark.RemarksError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=mark.RemarksError()%></span> </td>
                        <% } %>
                                                                    </tr>
                            <tr>

                            <tr>
                                <td class="textblack" colspan="2" align="right">
                                    <%if(mark.getIsEdit()){ %>
                                    <input type="submit" name="Submit" id="Submit" value="Update">
                                    <% }else {%>

                                    <input type="submit" name="Submit" id="Submit"  value="Submit" <%if(CountMark >=20)
                                                                                                    {%>disabled="true"
                                                                                                      <input type="hidden" id="showalert" name="showalert" value="1">
                                                                                                    <% }%> ><% } %>
                                    <input type="submit" name="Submit" id="Submit" value="Reset" onclick="return ResetAll();">
                                </td>
                            </tr>

                        <%if( mark.getIssaved()!=null) {%>
                        <td colspan="3" align="right"><span style="color:#a02a28; font-size: small"><b><%=mark.getIssaved() %></b></span> </td>
                        <% } %>
                    </tr>
                        </table>
                        </center>
                    </fieldset></td>

                        <td valign="top">
                            <fieldset><legend><span style="color:#a02a28; font-size: small"><b> Subject Details</b></span></legend>
                                <div id="SubjectCount">
                                    <%
                                        model.Absentees absent=new model.Absentees();
                                        AbsenteesEntryBckUp abs=new AbsenteesEntryBckUp(request, response);
                                        int TotalCount=0,AbsCount=0,MalCount=0,AbsEntered=0,MalWithOutFee=0,MalEntered=0,MarkEntered=0,AbsWithOutFee=0,Unconfirmed=0,Pending=0;

                                        if(request.getParameter("Subject")!=null && (!request.getParameter("Subject").equals("-1")))
                                        {

                                            TotalCount=abs.getTotalCountOfStudentsForSubject();
                                            AbsCount=abs.getTotalCountOfAbsenteeForSubject();
                                            MalCount=abs.getTotalCountOfMalPracticeForSubject();
                                            AbsEntered=abs.getTotalCountOfAbsenteesEntered();
                                            AbsWithOutFee=mark.getTotalAbsenteesEnteredForSubjectWithoutFee();
                                            MalEntered=abs.getTotalCountOfMalPracticeEntered();
                                            MarkEntered=abs.getTotalCountOfMarkEnteredForSubject();
                                            MalWithOutFee=mark.getTotalMalpracteesEnteredForSubjectWithoutFee();
                                            //SubjectBranchId=absent.getStudentSubjectBranchId(request.getParameter("Exam"), absent.getStudentIdFromPRN(request.getParameter("PRN")), request.getParameter("Subject"), Integer.parseInt(request.getParameter("YearSem")));
                                            //Unconfirmed=absent.getTotalCountOfUnconfirmedStudents(SubjectBranchId);
                                            //Pending=absent.getTotalCountOfUnconfirmedStudentsPending(SubjectBranchId);
                                            Pending=TotalCount-(MarkEntered+AbsEntered);
                                            String SubjectName=mark.getSubjectNameFromSubjectId();
                                        }
                                    %>
                                <table width="100%" >
                                 <tr>
                                     <td colspan="3" class="textblack" bgcolor="#d4c7a4" align="center" height="25px" width="100%"><b>Count</b></td>
                                        </tr>

                                        <tr>
                                        <table border="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">
                                         <tr>
                                            <th class="textblack">Item</th>
                                            <th class="textblack">Total</th>
                                            <th class="textblack">Entered</th>
                                        </tr>
                                        <tr>
                                            <td class="textblack"> Regisration:</td>
                                            <td class="textblack"><%if(request.getParameter("Subject")!=null){ %><a href="SubjectWisePRN.jsp?SubjectBranchId=<%=request.getParameter("Subject")%>&Course=<%=request.getParameter("Course")%>"> <%=TotalCount %></a><% }else{%><%=TotalCount %><% } %></td>
                                            <td class="textblack"><%=MarkEntered %></td>

                                        </tr>
                                        <%--tr>
                                            <td class="textblack">Marks To be Entered:</td>
                                            <td class="textblack"><%=(TotalCount-AbsCount) %></td>
                                        </tr--%>
                                        <tr>
                                             <td class="textblack">Absentees (Registered)</td>
                                             <td class="textblack"><%=AbsCount %></td>
                                             <td class="textblack"><%=AbsEntered-AbsWithOutFee %></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">Absentees (Not Registered)</td>
                                             <td class="textblack"><%--AbsCount --%></td>
                                             <td class="textblack"><%=AbsWithOutFee %></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">MalPractice (Registered) </td>
                                             <td class="textblack"><%=MalCount %></td>
                                             <td class="textblack"><%=MalEntered-MalWithOutFee%></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">MalPractice (Not Registered) </td>
                                             <td class="textblack"><%--MalCount --%></td>
                                             <td class="textblack"><%=MalWithOutFee %></td>
                                        </tr>

                                        <!--
                                        <tr>
                                             <td class="textblack">Withheld (Aproved)</td>
                                             <td class="textblack">_</td>
                                             <td class="textblack"><%=Unconfirmed %></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">Withheld (Pending for Approval)</td>
                                             <td class="textblack">_</td>
                                             <td class="textblack"><%=Pending %></td>
                                        </tr>
                                        -->
                                         <tr>
                                             <td class="textblack">Mark Entry Pending</td>
                                             <td class="textblack" style="text-align: center;">-</td>
                                             <td class="textblack" style="color:#a02a28;font-weight:bold;text-align: center;"><%=Pending %></td>
                                        </tr>
                                        </table>
                                        </tr>
                                    </table>
                                    </div>
                            </fieldset>
                        </td>
                    </tr>
                    <tr><td colspan="3">
                    <fieldset ><legend><span style="color:#a02a28; font-size: small"><b> List of Mark Entered</b></span></legend>
                         <%
                    ArrayList<StudentSubjectMark> StudentList=new ArrayList<StudentSubjectMark>();
                    StudentList=mark.getMarksEnteredByUser();
                    int Count= StudentList.size();i=0;
                    if(Count==0)
                        {%>

                   <center>
                       <table>
                            <%if( mark.getIsSendForApproval()!=null) {%>

                        <tr align="right"><span style="color:#a02a28; font-size: small"><b><%=mark.getIsSendForApproval() %></b></span> </tr>
                        <% } %><% else{ %>
                          <tr class="textblack"  align="center"><span style="color:#a02a28; font-size: small"><b>No Marks to Display</b></span></tr>
                       <% } %>
                       </table>
                   </center>

                        <% }else{
                           %>
                   <table border="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">
                    <tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>Sl No</th>
                         <th>PRN</th>
                         <th>StudentName</th>
                         <th>Subject</th>
                         <th>External</th>
                         <th>Internal</th>
                          <th>Practical</th>
                         <th>Total</th>
                         <th>Edit</th>
                    </tr>
                    <% while(i<Count)
                        {
                        StudentSubjectMark stu=StudentList.get(i);
                    %>

                    <tr>
                        <td class="textblack"><%=i+1%></td>
                        <td class="textblack"><%=stu.PRN %></td>
                        <td class="textblack"><%=stu.StudentName  %></td>
                        <td class="textblack"><%=stu.SubjectName %></td>
                        <td class="textblack"><%=stu.ExternalMark %></td>
                        <td class="textblack"><%=stu.InternalMark %></td>
                        <td class="textblack"><%=stu.InternalMark %></td>
                        <td class="textblack"><%=Double.parseDouble(stu.ExternalMark)+ Double.parseDouble(stu.InternalMark)%></td>
                        <td class="textblack"><a href="MarkEntryForPRN.jsp?StudentSubjectMarkId=<%=stu.StudentSubjectMarkId %>">Edit</a> </td>

                    </tr>
                    <input type="hidden" name="StudentMarkId" id="StudentMarkId" value="<%=stu.StudentSubjectMarkId %>">
                    <% i++;}}%>
                    <% if(Count>0)
                        {%>
                     <tr>
                        <td colspan="9" align="CENTER">
                            <input type="submit" name="submit" name="submit" value="Send for Approval">
                        </td>
                    </tr>
                    <% }%>

                </table>
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
