<%-- 
Document   : NominalRoll_CourseWise
Created on : Apr 18, 2012, 3:32:57 PM
Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<% if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}else{
  ExamStudentList_nominalRoll StuList=new ExamStudentList_nominalRoll(request,response);
 
QualificationDetails Qualdetails = new QualificationDetails(request, response);
CourseSelection  cs=new CourseSelection(request, response);
boolean IsSearch=true;
String CentreId=null;
String ExamId=null;
String CourseId=null;
String year=null;
String studenttype=null;
//CentreId=request.getSession().getAttribute("CenterId").toString();
if(request.getParameter("search_button")!=null && request.getParameter("search_button").equals("Search") )
{
if(request.getParameter("ExamId")!=null && (!request.getParameter("ExamId").equals("-1")))
    {
    ExamId=request.getParameter("ExamId");
    }
else
    {
    IsSearch=false;
    }
 if(request.getParameter("CentreId")!=null && (!request.getParameter("CentreId").equals("-1")))
    {
    CentreId=request.getParameter("CentreId");
    }
else
    {
    IsSearch=false;
    }
if(request.getParameter("course_applied")!=null && (!request.getParameter("course_applied").equals("-1")))
    {
    CourseId=request.getParameter("course_applied");
    }
else
    {
    IsSearch=false;
    }

if(request.getParameter("year_applied")!=null && (!request.getParameter("year_applied").equals("-1")))
    {
    year=request.getParameter("year_applied");
    }
else
    {
    IsSearch=false;
    }
if(request.getParameter("studenttype")!=null )
    {
    studenttype=request.getParameter("studenttype");
    }
else
    {
    IsSearch=false;
    }
}
if(request.getParameter("search_button")!=null && request.getParameter("search_button").equals("Search"))
    {
    if(IsSearch)
        response.sendRedirect("PrintCourseWiseNominalRolls.jsp?CentreId="+CentreId+"&year_applied="+year+"&course_applied="+CourseId+"&ExamId="+ExamId+"&studenttype="+studenttype);
    }
 model.mStudentList sList=new model.mStudentList();
Connection con=new DBConnection().getConnection();
mStudentList Student=new mStudentList();
        ForExamConfirmation examconfirm=new ForExamConfirmation();
mDD ddModel=new mDD();
%>
<html><head>


            <title>DEMS</title>
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
            <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
            <script type="text/javascript" src="./js/ExamRegistration.js"></script>
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
        body {margin:0; padding:0; width:100%;background:#fff; font-family:Arial, Helvetica, sans-serif;font-size:11px;}
        @media print {
        .break {page-break-before:always}
        input#btnPrint {
        display: none;
        }
        .header {display: none;}
        }
        input.printbutton {
            background-color:#91AF38;
            color:#fff;
            border:2px solid #590202;
            display: inline-block;
            outline: none;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
            font: 15px/100% Arial, Helvetica, sans-serif;
            padding: .4em 4em .4em 4em;
            text-shadow: 0 1px 1px rgba(0,0,0,.3);
            -webkit-border-radius: .2em;
            -moz-border-radius: .2em;
            -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
            -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
            box-shadow: 0 1px 2px rgba(0,0,0,.2)
        }
        input.printbutton:hover {
            text-decoration: none;
            background-color: #71AF68
        }
        input.printbutton:active {
            position: relative;
            border:1px solid #FFF;
            top: 1px
        }
    </style>
    <script type="text/javascript">
        function printtoken(){
            window.print();
        }
    </script>


        <style type="text/css">
<!--
.style1 {color: #0099CC}
-->
    </style>


        <style type="text/css">
<!--
.style1 {color: #0099CC}
-->
    </style>
        </head><body>
    <!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<jsp:include page="Header_Registration.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody><tr valign="top" align="left">
<td width="200" bgcolor="#e8dec3">
        <jsp:include page="Navigation_Registration.jsp"/>

       </td>

<td width="100%">
  <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr>
    <td colspan="5" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="100%"></td>
  </tr>
  <tr>

    <td>
<div class="textblack"></div>	</td>
    <td bgcolor="#ffffff">&nbsp;
         <%-- Content --%>
         <form name="NominalRoll_CourseWise" id="NominalRoll_CourseWise" action="NominalRoll_CourseWise.jsp" method="post">
 <b> <font color="#a02a28" size="2px"><u>Print Nominal Rolls</u></font></b>
             <div class="header"><table width="100%">
                 <tr>
                     <td>

                     </td>
                 </tr>
                 <tr>
                     <td>
                         <fieldset style="color:black" ><legend >Search </legend>
                                    <table width="90%">
                                         <tr>
                        <td class="textblack" align="left">Select Exam</td>
                        <td align="left">
                            <select name="ExamId" id="ExamId" style="width: 250px">
                                <option value="-1">.....Select.....</option>
                                <% ArrayList<ExamData> ExamList=StuList.getAllExams();
                                            int j=1;
                                            int count1=ExamList.size();
                                            if(count1>0){
                                                    while(j<=count1)
                                                        {
                                                        ExamData exam=ExamList.get(j-1);
                                            %>
                                           <option <%if(StuList.getExam()!=null && StuList.getExam().equals(exam.ExamId)) { out.print("selected");}  %>
                                                value="<%=exam.ExamId %>"><%=exam.ExamName %></option>

                                            <%j++; }}
                                            %>
                            </select>
                        </td>
                        <%if(StuList.ExamError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=StuList.ExamError()%></span> </td>
                        <% } %>
                        <td class="textblack">Course</td><td>
                                    <select name="course_applied" id="course_applied"   style="width: 215px;" title="Select your course" >

                                        <option value="-1" >--------Select------- </option>
                                        <%Set CourseList = cs.ListCourseNames();
                                                    for (Object obj : CourseList) {
                                                        Map.Entry<String, String> List = (Map.Entry<String, String>) obj;
                                        %>
                                        <option <%if (StuList.getCourseApplied()!=null && StuList.getCourseApplied().equals(List.getKey())) {
                                                                                                           out.print("selected");
                                                                                                       }
                                                                                                   %>
                                            value="<%=List.getKey()%>"><%=List.getValue()%></option>

                                        <% }
                                        %>
                                    </select></td>
                        </tr>
                                        <tr><td class="textblack">
                                    Centre</td><td>

                                     <% ArrayList<CentreDetails> CentreList=sList.getCentres();
                                          if(CentreList==null||CentreList.size()==0)
                                              {%>
                                              <span class="Error">No Centres Assigned to you </span>
                                          <%}else{
                                            int i=0;
                                           int count=CentreList.size();
                                            CentreDetails Centre;
                                         %>
          <select name="CentreId" id="CentreId"  style="width: 250px;" title="Select your centre">
<option value="-1" >--------Select-------</option>
<%while(i<count){
    Centre =CentreList.get(i);%>
    <option value="<%=Centre.CentreId %>" <%if (StuList.getCenterId()!=null && Integer.parseInt(StuList.getCenterId())==(Centre.CentreId)) {
                                                                                                           out.print("selected");
                                                                                                       }
                                                                                                   %>> <%=Centre.CentreName%></option>

     <%        i++;}//while
                                          }%>

</select></td><td class="textblack">Regular/Course Completed</td><td>
    <input type="radio" name="studenttype" value="0" checked="checked" ><font style="font-size: 11px;" >Regular
    <input type="radio" name="studenttype" value="1" >Course Completed
</td></tr>
                                        <tr><td class="textblack">

                                    Year/Semester</td><td>

                                    <select name="year_applied" id="year_applied"   style="width: 215px;" title="Select your year" >

                                        <option value="-1" >--------Select------- </option>
                                        <option value="1" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("1")) {%> selected="selected"<%}%> > First year</option>
                                         <option value="2" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("2")) {%> selected="selected"<%}%>> Second year</option>
                                <option value="3" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("3")) {%> selected="selected"<%}%>> Third year</option>
                                    </select></td><td>
                                    <input type="submit" name="search_button"id="search_button" value="Search"/></td>
                                        </tr>
                                       
                                        <%
                        if(StuList.getError()!=null)
                            { %>
                            <tr>
                                <td>
                                    <span class="textblack" style="color: red "><%=StuList.getError() %></span>
                                </td>
                            </tr><%}%>
                                    </table>
                                </fieldset>
                     </td>
                 </tr>
             </table></div>
                                     


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
<jsp:include page="footer.jsp"/><%}%>
