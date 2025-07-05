<%-- 
    Document   : PrintSubjectWiseRolls
    Created on : Apr 20, 2012, 2:45:30 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<% if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}else{
  ExamStudentsList StuList=new ExamStudentsList(request,response);
QualificationDetails Qualdetails = new QualificationDetails(request, response);
CourseSelection  cs=new CourseSelection(request, response);
boolean IsSearch=true;
String CentreId=null;
String CourseId=null;
String year=null;
String SubBranch=null;
//CentreId=request.getSession().getAttribute("CenterId").toString();

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
if(request.getParameter("SubBranch")!=null )
    {
    if(!request.getParameter("SubBranch").equals("-1") )
        {
            SubBranch=request.getParameter("SubBranch");
        }
    else
        {
        IsSearch=false;
        }
    }


if(IsSearch==false)
    {
    response.sendRedirect("NominalRolls_SubjectWise.jsp");
    }
else
    {
 model.mStudentList sList=new model.mStudentList();
 model.Subjects subject=new model.Subjects();
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
    background-image:  url(images/mgulogo1.jpg);
    background-repeat: repeat-x;
    background-size: 100%;
    margin-left: 0px;
    margin-top: 0px;
    margin-right: 0px;
    margin-bottom: 0px;

}
-->
    </style>
    <style type="text/css">

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
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody><tr valign="top" align="left">
<td width="100%">
  <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
  <tbody>
  <tr>
    <td bgcolor="#ffffff">
         <%-- Content --%>
         <form name="PrintSubjectWiseRolls" id="PrintSubjectWiseRolls" action="PrintSubjectWiseRolls.jsp" method="post">

           <p align="left"><a href="NominalRolls_SubjectWise.jsp?search_button=ok" style="cursor:pointer;color: green;text-decoration: underline" class="header">Back to Search</a></p>

                                        
     <center>
     <input type="button" class="printbutton" id="btnPrint" value="PRINT " onclick="javascript:printtoken();" />
     
     </center>
         

             <%
             ArrayList<Subject> subList=subject.getAllSubjectsForNominalRoll(CourseId, year, SubBranch);
             String Scheme="",Streme="",Sem="";
             int Length=subList.size(),n=0;
             while(n<Length)
                 {
                 Subject sub=subList.get(n);
                 if(sub.AcademicYear==2011)
                     {
                     Scheme="2011 Admn Onwards";
                     }
                 if(sub.semOryear==0)
                     {
                        Sem="Semester";
                     }
                 else
                     {
                        Sem="Year";
                     }
                 String Year="";
                 if(year!=null)
                     {
                 if(year.equals("1"))
                     {
                        Year="First ";
                     }
                 else if(year.equals("2"))
                     {
                        Year="Second ";
                     }
                  else if(year.equals("3"))
                     {
                        Year="Third ";
                     }
                  else if(year.equals("4"))
                     {
                        Year="Fourth  ";
                     }
                 }

                 ArrayList<StudentsForExam> StudentList=examconfirm.MguApprovedStudentListForSubject(sub.SubjectBranchId,CentreId,con);
                                       int  i=0;
                                       int var=6;
                                        int count=StudentList.size();
                                        if(count>0){
             %>
                <center >
                     <table width="100%" cellpadding="0" <%if(n!=0){ %>class="break"<% }%>>
                         <tr>
                             <td align="center" valign="bottom">
                                 <img src="images/mgulogo1.jpg" alt="" border="0" width="45" height="45">
                             </td>
                             <td align="center" >
                                 <font size="3.5px"><b> Mahatma Gandhi University</b></font>
                             </td>
                         </tr>
                         <tr>
                             <td></td>
                              <td align="center" >
                                  <font size="2px">Nominal Roll Of Candidates Registered For Off Campus Examination April-2012</font>
                             </td>
                         </tr>
                         <tr>
                             <td align="left" class="textblack" colspan="2"><b> Centre:</b><%=examconfirm.getCentreName(CentreId, con) %><br><b>Course:</b> <%=examconfirm.getCourseName(CourseId, con) %>&nbsp; <%=Year+Sem %> &nbsp;&nbsp;<%=subject.getSubBranchName(SubBranch, con) %></td>
                         </tr>                         
                         <tr>
                             <td colspan="2">
                                 <hr width="100%" style="border-style: solid; height:6">
                             </td>
                         </tr>
                     </table></center>
                         <table width="100%" align="center">
             <tr>
                 <td class="textblack" colspan="4">
                   
                     <font size="2px"> <b><%=sub.SubjectName %><%if(!Scheme.isEmpty()){ %>(<%=Scheme %>) <% }%>&nbsp;&nbsp; Total Students: <%=count %> &nbsp;&nbsp; Present :  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; Absent :</b></font>
                 </td>
             </tr>
             <tr>
             <td colspan="2">
                 <hr width="100%" style="border-style: solid;">
             </td>
         </tr>
             <tr><td>
              <table width="100%" cellspacing="0" cellpadding="0"  cellspacing="0" >
                  <tr>
                      <th class="textblack" align="left">Sl No:</th>
                      <th class="textblack" align="left">PRN</th>
                      <th class="textblack" align="left">Name</th>
                      <th class="textblack" align="left">Signature of Candidate</th>
                  </tr>
                   <tr>
             <td colspan="4">
                 <hr width="100%" style="border-style: dotted;">
             </td>
         </tr>
                                               <% while(i<count)
                                                    {
                                                    StudentsForExam stud=StudentList.get(i);
                                        %>
                                        <tr>
                                           <td class="textblack"><%=i+1%></td>
                                            <td class="textblack"><%=stud.PRN%></td>
                                             <td class="textblack"><%=stud.StudentName %></td>
                                             <td>&nbsp;&nbsp;</td>
                                        
                                        <tr>
                                             <td colspan="4">
                                                 <hr width="100%" style="border-style: dotted;">
                                             </td>
                                         </tr>

                                            <%i++;}%>
 <tr>
             <td colspan="4">
                 <hr width="100%" style="border-style: solid;">
             </td>
         </tr>
                       
         <%}%> 

              </table></td></tr>
            
             
             <% n++;}%>
              <tr>
                            <td class="textblack" align="center">
                                All Rights Reserved.Powered by System Administration Team,Mahatma Gandhi University
                            </td>
                        </tr>
         </table>
         </form>
         <%-- Content --%>
    </td>
  </tr>
</tbody></table></td>
</tr>
<TR> <TD><br/></TD></TR><%con.close();%>
</tbody></table>
</td></tr>
</tbody></table>
</body></html>
<div class="header">
    <jsp:include page="footer.jsp"/></div><%}}%>

