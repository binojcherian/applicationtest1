<%--
    Document   : CourseWiseTR
    Created on : May 4, 2012, 4:40:42 PM
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
                if (request.getParameter("Exam") == null || request.getParameter("Exam").equals("-1") || request.getParameter("Course") == null || request.getParameter("Course").equals("-1")) {
                    response.sendRedirect("/OffCampus/TabulationRegister.jsp");
                }
                if (request.getParameter("Centre") == null && request.getParameter("Centre").equals("-1")) {
                    IsCentre = false;
                }
                Connection con = new DBConnection().getConnection();
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
                font-size: smaller;
            }
            -->
        </style>
        <link rel="Stylesheet" href="Style/style.css" type="text/css">

        <style type="text/css">
            body {margin:0; padding:0; width:100%;background:#fff; font-family:Arial, Helvetica, sans-serif;font-size:11px;}

            @media print {
                table#pgbrk {page-break-before:always}
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
        <style>


            .verticalText
            {
                /* IE-only DX filter */
                writing-mode: tb-rl;
                filter: flipv fliph;

                /* Safari/Chrome function */
                -webkit-transform: rotate(270deg);

                /* works in latest FX builds */
                -moz-transform: rotate(270deg);

            }

        </style>
        <style type="text/css">
            <!--
            .style1 {color: #0099CC}
            -->
        </style>
    </head><body>
        <%
            String Scheme = mark.YearOrSem();
        %>
        <!-- rpm find linux search -->
        <!-- <tr><td><table id="pgbrk"><tr><td></td></tr></table></td></tr> -->
        <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
            <tbody>
                <tr id="pgbrk"><td id="pgbrk"><table id="pgbrk"><tr><td></td></tr></table></td></tr>
                <tr><td>
                        <table width="95%" align="center" border="0" cellpadding="0" cellspacing="0" >
                            <tbody><tr valign="top" align="left">
                                    <td width="100%">
                                        <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
                                            <tbody><tr>
                                                    <td  bgcolor="#ffffff"></td>
                                                </tr>
                                                <tr>
                                                    <td bgcolor="#ffffff">
                                                        <%-- Content --%>
                                                        <center>
                                                            <form id="TRBCom" name="TRBCom" method="post" action="TRBCom.jsp">
                                                                <input type="hidden" name="Centre" id="Centre" value="">
                                                                <input type="button" class="printbutton" id="btnPrint" value="PRINT" onclick="javascript:printtoken();"/><br>
                                                                <table width="100%" border="1" style="border-color: gray " cellpadding="3" cellspacing="0" >
                                                                    <%
                                                                        String Sem = "", year = "1";
                                                                        int MaxSem = 2;
                                                                        ArrayList<TabulationData> TabData = mark.getStudentTRDetails(con);
                                                                        int MarksCount = TabData.size();
                                                                        int i = 0;
                                                                        ArrayList<model.StudentData> Student = mark.getStudents();
                                                                        int StudentCount = Student.size(), Stuindex=0;
                                                                        String PreStudent = null;
                                                                    %>
                                                                    <tr>
                                                                        <td  class="textforprint" width="75" rowspan="3" >Register Number</td>
                                                                        <td class="textforprint" width="85"  rowspan="3" style="border-bottom:none;">Name Of Candidate</td>
                                                                        <td width="35"  class="textforprint">&nbsp;</td>
                                                                        <%
                                                                            for (i = 1; i <= MaxSem; i++) {
                                                                                if (mark.YearOrSem().equals("Year")) {
                                                                                    MaxSem = 1;
                                                                                    Sem = "Year";
                                                                                } else {
                                                                                    Sem = "Semester";
                                                                                }
                                                                                String Year = "";
                                                                                if (i == 1) {
                                                                                    Year = "First ";
                                                                                } else if (i == 2) {
                                                                                    Year = "Second ";
                                                                                } else if (i == 3) {
                                                                                    Year = "Third ";
                                                                                } else if (i == 4) {
                                                                                    Year = "Fourth  ";
                                                                                }
                                                                        %>

                                                                        <%}%>
                                                                        <%
                                                                            ArrayList<Subject> SubList = mark.getSubjectsForBranch();
                                                                            int SubCount = SubList.size();
                                                                             i = 0;
                                                                            while (i < SubCount) {
                                                                                Subject sub = SubList.get(i);
                                                                        %>
                                                                        <td class="textforprint" style="width:30" ><%=sub.SubjectName%></td>
                                                                        <% i++;}%>
                                                                        <td rowspan="3" align="center" width="40px"class="textforprint">Grand Total</td>
                                                                        <td rowspan="3" align="center" width="40px"class="textforprint">Pass/Fail</td>
                                                                        <td rowspan="3" align="center" width="100px"class="textforprint">Remarks</td>
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
                            </tbody></table><%if (!con.isClosed()) {
        con.close();
    }%>
                    </td></tr>
            </tbody></table>

    </body></html><% }%>