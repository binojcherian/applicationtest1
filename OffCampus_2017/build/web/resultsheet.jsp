<%-- 
    Document   : CourseWiseTabulationRegister
    Created on : Mar 7, 2012, 2:22:51 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>

<%
            if (request.getSession().getAttribute("UserName") == null) {
                response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
            } else {
                String PrivilegeMessage = null;
                LoginController login = new LoginController(request, response);
                int Role = login.getPrivilege(request.getSession().getAttribute("UserName").toString());
                if (Role != 33) {
                    PrivilegeMessage = "YOU ARE NOT PERMITTED TO ACCESS THIS PAGE";
                    response.sendRedirect("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE");
                }
                Tabulation mark = new Tabulation(request, response);
                boolean IsCentre = true;
                if (request.getParameter("Exam") == null || request.getParameter("Exam").equals("-1") || request.getParameter("Course") == null || request.getParameter("Course").equals("-1") || request.getParameter("YearSem").equals("-1")) {
                    response.sendRedirect("/OffCampus/TabulationRegister.jsp");
                }
                if (request.getParameter("Centre") == null && request.getParameter("Centre").equals("-1")) {
                    IsCentre = false;
                }
%>
<html><head>


        <title>DEMS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
        <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
        <script type="text/javascript" src="./js/Tabulation.js"></script>
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
            body {margin:0; padding:0; width:100%;background:#fff; font-family:Arial, Helvetica, sans-serif;font-size:6px;}
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
    </head><body>
        <!-- rpm find linux search -->
        <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
            <tbody>
                <tr><td>
                        <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                            <tbody><tr valign="top" align="left">

                                    <td width="100%">
                                        <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
                                            <tbody><tr>
                                                    <td  bgcolor="#ffffff"></td>
                                                </tr>
                                                <tr>
                                                    <td bgcolor="#ffffff">&nbsp;
                                                        <%-- Content --%>
                                                        <center>
                                                            <form name="TabulationRegister" id="TabulationRegister" action="resultsheet.jsp" method="post" style="min-height: 300px">
                                                                <input type="button" class="printbutton" id="btnPrint" value="PRINT " onclick="javascript:printtoken();" /><br>
                                                                <table   width="100%" style=" min-height: 100px;" cellspacing="0" cellpadding="3" >
                                                                    <tr  align="center" >
                                                                        <td height="20px" colspan="2"> <b> <font color="BLACK">MAHATMA GANDHI UNIVERSITY</font></b></td>
                                                                    </tr>
                                                                    <tr align="center" >
                                                                        <td height="15px" colspan="2"><b><%=mark.getExamName()%></b></td>
                                                                    </tr>
                                                                       <%
                                                                        String PrevCentre = "";
                                                                        int c=0;
                                                                        ArrayList<Entity.StudentData> StudentList = mark.getStudentsOfCourse1();
                                                                           //forBcom 2015 ArrayList<Entity.StudentData> StudentList = mark.getStudentsOfCourse_BcomPartIII();  
                                                                            int count = StudentList.size();
                                                                            
                                                                         int i = 0;
                                                                        if (count > 0) {
                                                                            Connection con = new DBConnection().getConnection();
                                                                    %>
                                                                </td> </tr>
                                                                </table>>
                                                                     
                                                                                    <table border="1">
                                                                                        <tr><th>SL</th><th>PRN</th><th>NAME</th><th>CENTRE</th><th>SEMESTER1</th><th>RESULT</th><th>SEMESTER2</th><th>RESULT</th></tr>
                                                                                        

                                                                            <% int var =2;
                                                                                                        while (i < count) {
                                                                                                             Entity.StudentData Student =null;
                                                                                                             Student=StudentList.get(i);
                                                                                                            // out.println("***"+i+Student.StudentName );
                                                                                                            int Sem = 0;
                                                                                                           
                                                                                                            boolean flag = true, EngFailed = false;
                                                                            %>
                                                                             
                                                                                
                                                                            
                                                                                    <% if (((i + 1) % var == 0 )) {
                                                                             //if (((i + 1) % var == 0 || (!Student.CollegeId.equals(PrevCentre))) && i > 1)%>
                                                                             
                                                                                <%//out.println((i + 1) % var);
                                                                                //out.println(i);%>
                                                                                <div class="break" ></div><% }%>
                                                                                   
                                                                                  
                                                                                    <tr>
                                                                                            <td><b><%=i + 1%></b></td>
                                                                                        <td align="left" class="textblack" style="background-color: silver"><%=Student.PRN%></td>
                                                                                        <td align="left" class="textblack" style="background-color: silver" ><%=Student.StudentName%></td>
                                                                                         <td align="left" class="textblack"><%=Student.CollegeName%></td>
                                                                                        
                                                                                    
                                                                                                <%   boolean IsGPassed = true;
                                                                                                       int GTotal = 0;
                                                                                                                                // if(mark.getSemYear().equals("4") && mark.getBranch().equals("17")){
                                                                                                                                if (mark.getSemYear().equals("2")) {
                                                                                                                                    for (int x = 1; x < 3; x++) {
                                                                                                                                        int num = 0;
                                                                                                                                        try {
                                                                                                                                            num = Integer.parseInt(mark.getSemTotal(Integer.parseInt(Student.StudentId), con, x));
                                                                                                                                            System.out.println("--"+Student.StudentId+'-'+num+"--");

                                                                                                                                        } catch (NumberFormatException e) {
                                                                                                                                            num = 0;
                                                                                                                                        }
                                                                                                                                        //if(mark.IsStudentPassedForSemester(Integer.parseInt(Student.StudentId), con,x))
                                                                                                                                        GTotal += num;
                                                                                                                                        System.out.println("--"+Student.StudentId+'-'+GTotal+"++");
                                                                                                                                        if (x <3) {
                                                                                                %>
                                                                                           
                                                                                                   
                                                                                                 
                                                
                                               
                                                                                                    <td class="textblack"> 
                                                                                                            <%if (mark.IsStudentPassedForSemesterLLM(Integer.parseInt(Student.StudentId), con,""+x) ) { c=c+1;%>
                                                                                                        <%=mark.getSemTotalForStudent1(Student.StudentId, con,""+x)%><% } else { 
                                                 IsGPassed = false;%> <%=mark.getSemTotalForStudent1(Student.StudentId, con,""+x)%>&nbsp;<% }%></td>
                                                                                                        
                                                                                                    <td class="textblack"><%if (mark.IsStudentPassedForSemesterLLM(Integer.parseInt(Student.StudentId), con,""+x) )  {%><b><%="PASS"%><% } else {%><%="FAIL"%> <%}%></b></td>              

                                                                                                <%}
                                                                                    }%>

                                                                                              
                                                                                                 <!--   <td class="textblack"><%if (IsGPassed) {%><b><%=GTotal%><%=IsGPassed%><% } else {%>&nbsp; <%}%></b></td> -->

                                                                                                
                                                                                                 

                                                                                                <%}
                                             PrevCentre = Student.CollegeId;
                                                    i++;
                                                                                                        
        
        }}%>   
                                                                                                </tr> </table> 
                                                            </form>
                                                        </center>
                                                   </td>
                                </tr>
                                <TR> <TD><br/></TD></TR>
                            </tbody></table>
                    </td></tr>
            </tbody></table>
    </body></html><% }%>

