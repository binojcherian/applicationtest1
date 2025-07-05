<%--
    Document   : AbsenteesCount
    Created on : Nov 3, 2011, 2:34:51 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp"  import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<% if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
AbsenteesCountBckUp absentee=new AbsenteesCountBckUp(request, response);
LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=31)
    {
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
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
<div class="textblack"></div>	</td>
        <td bgcolor="#ffffff">&nbsp;
            <%-- Content --%>
            <center>
                <form id="AbsenteesCount" name="AbsenteesCount" action="AbsenteesCount.jsp" method="post">
                    <input type="hidden"  name="ExamAbsenteeMalPracticeId" id="ExamAbsenteeMalPracticeId" value="<%=request.getParameter("ExamAbsenteeMalPracticeId") %>"
                     <center>
                    <table  border="1" width="97%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Enter Absentees/MalPractice Count</font></b></td>
                    </tr>
                    <tr> <td align="center">
                    <fieldset style="width:95%"><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                        <table>
                     <tr>
                        <td class="textblack">Select Exam</td>
                        <td>
                            <select name="Exam" id="Exam" style="width: 300px">
                                <option value="-1">.....Select.....</option>
                                <% ArrayList<ExamData> ExamList=absentee.getExams();
                                            int i=1;
                                            int count=ExamList.size();
                                            if(count>0){
                                                    while(i<=count)
                                                        {
                                                        ExamData exam=ExamList.get(i-1);
                                            %>
                                           <option <%if(absentee.getExam().equals(exam.ExamId)) { out.print("selected");}  %>
                                                value="<%=exam.ExamId %>"><%=exam.ExamName %></option>

                                            <%i++; }}
                                            %>
                            </select>
                        </td>
                        <%if(absentee.ExamError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.ExamError()%></span> </td>
                        <% } %>

                    </tr>
                    <tr>
                        <td class="textblack">Select Course</td>
                        <td>
                            <select name="Course" id="Course" style="width: 250px" onchange="ChangeYearOrSemForCount();">
                                <option value="-1">.....Select.....</option>
                                            <% ArrayList<Entity.CourseData> CourseList=absentee.getBranchList();
                                            i=0;
                                            count=CourseList.size();

                                                    while(i<count)
                                                        {
                                                        Entity.CourseData Course=CourseList.get(i);
                                            %>
                                            <option <%if(Integer.parseInt(absentee.getBranch())==Course.BranchId) { out.print("selected");} %>
                                                value="<%=Course.BranchId %>"><%=Course.BranchName %></option>

                                            <%i++; }
                                            %>

                            </select>
                        </td>
                        <%if(absentee.BranchError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.BranchError()%></span> </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td class="textblack">Select Syllabus</td>
                        <td>
                            <div id="Acdemic_Year">
                                <select  name="AcdemicYear" id="AcdemicYear" style="width: 115px">
                                    <option value="-1">.....Select.....</option>
                                    <option value="2010" <%if(absentee.getAcademicYear().equals("2010")){out.print("selected");}%>>2010</option>
                                    <option value="2011" <%if(absentee.getAcademicYear().equals("2011")){out.print("selected");}%>>2011</option>
                                    <option value="2011" <%if(absentee.getAcademicYear().equals("2012")){out.print("selected");}%>>2012</option>
                                </select>
                            </div>
                        </td>
                        <%if(absentee.AcdemicYearError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.AcdemicYearError()%></span> </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td class="textblack">Select Year/Semester</td>
                        <td>
                            <div id="YearOrSem">
                                <select  name="YearSem" id="YearSem" style="width: 115px" onchange="redirectfunction();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                    count=absentee.getMaxYearOrSemForCourse();
                                    i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(absentee.getSemYear().equals("i")){ out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++;}%>
                                </select>
                            </div>
                        </td>
                        <%if(absentee.SemError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.SemError()%></span> </td>
                        <% } %>
                    </tr>
                    <%
                    String val;
                    if((absentee.getBranch().equals("21")) && (absentee.getSemYear().equals("3") || absentee.getSemYear().equals("4")))
                        {
                            val="visibility: visible;";
                        }
                    else if((absentee.getBranch().equals("17")) && (absentee.getSemYear().equals("4")))
                        {
                            val="visibility: visible;";
                        }
                    else
                        {
                            val="visibility: collapse;";

                        }
                    %>
                    <tr id="SubBranchDet" style="<%=val%>">
                        <td class="textblack">Select Sub Branch</td>
                        <td>
                            <div id="SubBranchList">
                                <select name="SubBranch" id="SubBranch" style="width: 350px;"  onchange="ViewAllSubBranchList();">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubBranchList=absentee.getSubjectsSubBranch();
                                            i=0;
                                            if(SubBranchList!=null)
                                                {
                                            count=SubBranchList.size();
                                            if(count!=0)
                                                {
                                                    while(i<count)
                                                        {
                                                        Subject sub=SubBranchList.get(i);
                                            %>
                                           <option <%if(absentee.getSubBranch().equals(sub.SubBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubBranchId %>"><%=sub.DisplaySubBranchName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>
                        </td>
                        <%if(absentee.SubBranchError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.SubBranchError()%></span> </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td class="textblack">Select Subject</td>
                        <td>
                            <div id="SubjectList">
                            <select name="Subject" id="Subject" style="width: 250px">
                                 <option value="-1">.....Select.....</option>
                                            <%
                                             ArrayList<Subject> SubList;
                                            if((absentee.getBranch().equals("21")) && (absentee.getSemYear().equals("3") || absentee.getSemYear().equals("4")))
                                            {
                                                SubList=absentee.getSubjectsForSubBranch();
                                            }
                                            else if((absentee.getBranch().equals("17")) && (absentee.getSemYear().equals("4")))
                                            {
                                                SubList=absentee.getSubjectsForSubBranch();
                                            }
                                            else
                                            {
                                                SubList=absentee.getSubjectsForCourse();

                                            }

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
                                           <option <%if(absentee.getSubject().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>
                        </td>
                        <%if(absentee.SubjectError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.SubjectError()%></span> </td>
                        <% } %>
                    </tr>
                     <tr>
                        <td class="textblack">
                            <input type="radio" name="Absent" id="Absent" <%if(absentee.getIsAbsentOrMal()==1 || absentee.getIsAbsentOrMal()==-1){%>checked="true"<% } %> value="Absent">Is Absent
                        </td>
                         <td class="textblack">
                            <input type="radio" name="Absent" id="Absent" <%if(absentee.getIsAbsentOrMal()==2){%>checked="true"<% } %> value="MalPractice">Is MalPractice
                        </td>
                    </tr>
                     <tr>
                        <td class="textblack">Enter Count</td>
                        <td>
                            <input type="text" name="Count" id="Count" value="<%=absentee.getCount() %>" maxlength="5" style="width: 115px" onkeypress="return chkNumForInt(event)">
                        </td>
                        <%if(absentee.CountError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=absentee.CountError()%></span> </td>
                        <% } %>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                             <%if(absentee.getIsEdit()){ %>
                                    <input type="submit" name="Submit" id="Submit" value="Update">
                                    <% }else {%>
                                    <input type="submit" name="Submit" id="Submit" value="Submit"><% }%>
                            <input type="submit" name="Submit" id="Submit" value="Reset" onclick="ResetForAbsenteeCount();">
                        </td>
                    </tr>
                    <tr>

                        <%if(absentee.getIssaved()!=null) {%>
                        <td colspan="3" align="right"><span style="color:#a02a28; font-size: small"><b><%=absentee.getIssaved() %></b></span> </td>
                        <% } %>
                    </tr>

                        </table>
                    </fieldset>
                    <br>
                      <fieldset style="width:95%"><legend><span style="color:#a02a28; font-size: small"><b>Absent/MalPractice List</b></span></legend>
                              <fieldset style="width:95%"><legend><span style="color:#a02a28; font-size: small"><b>Search</b></span></legend>
                                <table>
                                <tr>
                                         <td class="textblack">Course</td>
                                <td><!--ViewAllSubjectListForSearch();-->
                                    <select name="CourseSearch" id="CourseSearch" style="width: 260px" onchange="ChangeYearOrSemForSearch();">
                                <option value="-1">.....Select.....</option>
                                           <% ArrayList<Entity.CourseData> BranchList=absentee.getBranchList();
                                            i=0;
                                            count=BranchList.size();

                                                    while(i<count)
                                                        {
                                                        Entity.CourseData Course=BranchList.get(i);
                                            %>
                                            <option <%if(Integer.parseInt(absentee.getCourseSearch())==Course.BranchId) { out.print("selected");} %>
                                                value="<%=Course.BranchId %>"><%=Course.BranchName %></option>

                                            <%i++; }
                                            %>

                            </select>
                                </td>
                                <td class="textblack">
                                    <input type="radio" name="AbsentSearch" id="AbsentSearch"  value="Absent" >Absent
                                         <input type="radio" name="AbsentSearch" id="AbsentSearch"  value="MalPractice" >MalPractice
                                </td>
                                    </tr>
                    <tr>
                        <td class="textblack">Select Acdemic Year</td>
                        <td>
                            <div id="Acdemic_Year">
                                <select  name="AcdemicYearSearch" id="AcdemicYearSearch" style="width: 115px">
                                    <option value="-1">.....Select.....</option>
                                    <option value="2010" <%if(absentee.getAcademicYearSearch().equals("2010")){out.print("selected");}%>>2010</option>
                                    <option value="2011" <%if(absentee.getAcademicYearSearch().equals("2011")){out.print("selected");}%>>2011</option>
                                    <option value="2012" <%if(absentee.getAcademicYear().equals("2012")){out.print("selected");}%>>2012</option>
                                </select>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td class="textblack">Select Year/Semester</td>
                        <td>
                            <div id="YearOrSemSearch">
                                <select  name="YearSemSearch" id="YearSemSearch" style="width: 115px" onchange="redirectFunctionForSearch();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                    count=absentee.getMaxYearOrSemForCourse();
                                    i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(absentee.getSemYearSearch()==i){ out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++;}%>
                                </select>
                            </div>
                        </td>
                     </tr>
                     <%
                    String value;
                    if((absentee.getCourseSearch().equals("21")) && (absentee.getSemYearSearch()==3 || absentee.getSemYearSearch()==4))
                        {
                            value="visibility: visible;";
                        }
                    else if((absentee.getCourseSearch().equals("17")) && (absentee.getSemYearSearch()==4))
                        {
                            value="visibility: visible;";
                        }
                    else
                        {
                            value="visibility: collapse;";

                        }
                    %>

                    <tr id="SubBranchDetSearch" style="<%=value%>">
                        <td class="textblack">Select Sub Branch</td>
                        <td>
                            <div id="SubBranchListSearch">
                            <select name="SubBranchSearch" id="SubBranchSearch" style="width: 350px" onchange="ViewAllSubBranchListForSearch();">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubBranchList1=absentee.getSubjectsSubBranch();
                                            i=0;
                                            if(SubBranchList1!=null)
                                                {
                                            count=SubBranchList1.size();
                                            if(count!=0)
                                                {
                                                    while(i<count)
                                                        {
                                                        Subject sub=SubBranchList1.get(i);
                                            %>
                                           <option <%if(absentee.getSubBranchSearch().equals(sub.SubBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubBranchId %>"><%=sub.DisplaySubBranchName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>
                        </td>
                       </tr>
                            <tr>
                                <td class="textblack">Subject</td>
                                <td>
                                    <div id="SubjectListSearch">
                                    <select name="SubjectSearch" id="SubjectSearch" style="width: 260px">
                                            <option value="-1">.....Select.....</option>
                                            <%
                                            //ArrayList<Subject> SubjectList=absentee.getAllSubjectsForSearch();
                                             ArrayList<Subject> SubjectList;
                                            if((absentee.getCourseSearch().equals("21")) && (absentee.getSemYearSearch()==3 || absentee.getSemYearSearch()==4))
                                            {
                                                SubjectList=absentee.getSubjectsForSubBranch();
                                            }
                                            else if((absentee.getCourseSearch().equals("17")) && (absentee.getSemYearSearch()==4))
                                            {
                                                SubjectList=absentee.getSubjectsForSubBranch();
                                            }
                                            else
                                            {
                                                SubjectList=absentee.getSubjectsForCourse();
                                                //SubjectList=absentee.getAllSubjectsForSearch();
                                            }
                                           i=0;
                                           if(SubjectList!=null)
                                               {
                                           count=SubjectList.size();
                                            if(count!=0)
                                                {
                                                    while(i<count)
                                                        {
                                                        Subject sub=SubjectList.get(i);
                                            %>
                                           <option <%if(absentee.getSubjectSearch().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>
                                </td>
                                     <td colspan="2" align="left"  class="textblack">

                                         <input type="submit" name="Search" name="Search" value="Search">
                                     </td>
                                    </tr>
                                </table>
                            </fieldset>

                            <%!
                        public int nullIntconv(String str) {
                            int conv = 0;
                            if (str == null) {
                                str = "0";
                            } else if ((str.trim()).equals("null")) {
                                str = "0";
                            } else if (str.equals("")) {
                                str = "0";
                            }
                            try {
                                conv = Integer.parseInt(str);
                            } catch (Exception e) {
                            }
                            return conv;
                        }
                    %>
                    <%
                                Connection con = new DBConnection().getConnection();
                                String message=null;
                                ResultSet rsPagination = null;
                                ResultSet rsRowCnt = null;

                                PreparedStatement psPagination = null;
                                PreparedStatement psRowCnt = null;

                                int iShowRows = 30;  // Number of records show on per page
                                int iTotalSearchRecords = 20;  // Number of pages index shown

                                int iTotalRows = nullIntconv(request.getParameter("iTotalRows"));
                                int iTotalPages = nullIntconv(request.getParameter("iTotalPages"));
                                int iPageNo = nullIntconv(absentee.getiPageNo());
                                int cPageNo = nullIntconv(absentee.getcPageNo());

                                int iStartResultNo = 0;
                                int iEndResultNo = 0;

                                if(request.getParameter("Search")!=null)
                                    {
                                    iPageNo=0;
                                    }
                                if(iPageNo!=0)
                                iPageNo = Math.abs((iPageNo - 1) * iShowRows);

                                try
                                {
                                    String sqlPagination = absentee.getQuery()+" order by ExamAbsenteeMalPracticeId desc limit " + iPageNo + "," + iShowRows + "";

                                    psPagination = con.prepareStatement(sqlPagination);
                                    //psPagination.setInt(1, Integer.parseInt(request.getParameter("AdmissionYear")));
                                    rsPagination = psPagination.executeQuery();
                                }
                                catch (Exception ex)
                                {
                                    out.print(ex);

                                }
                                Boolean notFound = false;
                                if (!rsPagination.next()) {
                                    notFound = true;
                                    message = " No Absentees Entered";
                                }

                                //// this will count total number of rows
                                String sqlRowCnt = "SELECT FOUND_ROWS() as cnt";

                                psRowCnt = con.prepareStatement(sqlRowCnt);
                                rsRowCnt = psRowCnt.executeQuery();

                                if (rsRowCnt.next())
                                {
                                    iTotalRows = rsRowCnt.getInt("cnt");
                                }
                    %>
                    <input type="hidden" name="iPageNo" value="<%=iPageNo%>"/>
                    <input type="hidden" name="cPageNo" value="<%=cPageNo%>"/>
                    <input type="hidden" name="iShowRows" value="<%=iShowRows%>"/>
<% if (message != null) {%>
                        <center>
                        <table><tr>
                            <td></td>
                            <td colspan="" class="textblack">  <span  class="Error" style="color:red">*<%=message%>*  </span></td>
                            </tr><br/></table> </center>
                        <%}else{%>
                        <fieldset><legend><span style="color:#a02a28; font-size: small"><b>List</b></span></legend>

                        <center>
                            <div align="center" class="textred"> <b><font class="textred">Total number of Records : <%=iTotalRows%></font></b></div>

                        <table BORDER="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack">

                    <tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>SlNo</th>
                         <th>Course</th>
                         <th>Subject</th>
                         <th>Status</th>
                         <th>Count</th>
                         <th>Edit</th>
                    </tr>
                            <%

                                        //CenterVerification verification = new CenterVerification();
                                        String Status= null;
                                        int j = 0;
                                        j = j + iPageNo;
                                        if (!notFound) {
                                            do {

                                                j++;

                                                if(rsPagination.getString("IsAbsentOrMal").equals("1"))
                                                    {
                                                    Status="Absent";
                                                    }
                                                if(rsPagination.getString("IsAbsentOrMal").equals("2"))
                                                    {
                                                    Status="Mal Practice";
                                                    }
                            %>


                    <tr>
                        <td class="textblack"><%=j%></td>
                        <td class="textblack"><%=rsPagination.getString("DisplayName") %></td>
                        <td class="textblack"><%=rsPagination.getString("SubjectName") %></td>
                        <td class="textblack"><%=Status %></td>
                        <td class="textblack"><%=rsPagination.getString("Count") %></td>
                        <td class="textblack"><a href="AbsenteesCount.jsp?ExamAbsenteeMalPracticeId=<%=rsPagination.getString("ExamAbsenteeMalPracticeId") %>">Edit</a> </td>
                    </tr>
                            <%  } while (rsPagination.next());
                                                                                            }%>




                            <%
                                        //// calculate next record start record  and end record
                                        try {
                                            if (iTotalRows < (iPageNo + iShowRows)) {
                                                iEndResultNo = iTotalRows;
                                            } else {
                                                iEndResultNo = (iPageNo + iShowRows);
                                            }

                                            iStartResultNo = (iPageNo + 1);
                                            iTotalPages = ((int) (Math.ceil((double) iTotalRows / iShowRows)));

             ;                           } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                            %>
                            <br/>
                            <tr>
                                <td colspan="8"  align="right">
                                    <div>
                                        <%
                                                    //// index of pages

                                                    i = 0;
                                                    int cPage = 0;
                                                    if (iTotalRows != 0) {
                                                        cPage = ((int) (Math.ceil((double) iEndResultNo / (iTotalSearchRecords * iShowRows))));

                                                        int prePageNo = (cPage * iTotalSearchRecords) - ((iTotalSearchRecords - 1) + iTotalSearchRecords);
                                                        if ((cPage * iTotalSearchRecords) - (iTotalSearchRecords) > 0) {
                                        %>
                                        <a href="AbsenteesCount.jsp?iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>"> << Previous</a>
                                        <%
                                                                                                                                                    }

                                                                                                                                                    for (i = ((cPage * iTotalSearchRecords) - (iTotalSearchRecords - 1)); i <= (cPage * iTotalSearchRecords); i++) {
                                                                                                                                                        if (i == ((iPageNo / iShowRows) + 1)) {
                                        %>
                                        <a href="AbsenteesCount.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: #a02a28"><b><%=i%></b></a>
                                        <%
                                                                                                                                                                                                                                                    } else if (i <= iTotalPages) {
                                        %>
                                        <a href="AbsenteesCount.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: blue"><%=i%></a>
                                        <%
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    if (iTotalPages > iTotalSearchRecords && i < iTotalPages) {
                                        %>
                                        <a href="AbsenteesCount.jsp?iPageNo=<%=i%>&cPageNo=<%=i%>"> >> Next</a>
                                        <%
                                                        }
                                                    }
                                        %>

                                    </div>
                                </td>
                            </tr>

                        </table></center><%}con.close();%></fieldset></fieldset>
                        </td>
                    </tr>
                    </table>
                     </center>
                    <BR>
                  <!-- Pagination Starts-->

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
