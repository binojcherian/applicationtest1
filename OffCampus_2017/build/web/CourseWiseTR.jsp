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
                ForExamConfirmation examconfirm = new ForExamConfirmation();
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
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0" >
                            <tbody><tr valign="top" align="left">
                                    <td width="">
                                        <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
                                            <tbody><tr>
                                                    <td  bgcolor="#ffffff"></td>
                                                </tr>
                                                <%
                                                    String Sem = "";

                                                    if (mark.YearOrSem().equals("Year")) {

                                                        Sem = "Year";
                                                    } else {
                                                        Sem = "Semester";
                                                    }
                                                    String Year = "";
                                                    if (Integer.parseInt(request.getParameter("YearSem")) == 1) {
                                                        Year = "First ";
                                                    } else if (Integer.parseInt(request.getParameter("YearSem")) == 2) {
                                                        Year = "Second ";
                                                    } else if (Integer.parseInt(request.getParameter("YearSem")) == 3) {
                                                        Year = "Third ";
                                                    } else if (Integer.parseInt(request.getParameter("YearSem")) == 4) {
                                                        Year = "Fourth  ";
                                                    }
                                                %>
                                                <tr>
                                                    <td bgcolor="#ffffff">
                                                        <%-- Content --%>
                                                        <p align="left"><a href="TabulationRegister.jsp" style="cursor:pointer;color: green;text-decoration: underline" class="header">Back to Search</a></p>
                                                        <center>
                                                            <form id="CourseWiseTR" name="CourseWiseTR" method="post" action="CourseWiseTR.jsp">
                                                                <input type="hidden" name="Centre" id="Centre" value="<%=request.getParameter("Centre")%>">
                                                                <input type="button" class="printbutton" id="btnPrint" value="PRINT" onclick="javascript:printtoken();"/><br>
                                                                <%
                                                                    Sem = "";
                                                                    String year = "1";
                                                                    int MaxSem = 2;
                                                                    ArrayList<TabulationData> TabData = mark.getStudentTRDetails_BcomPartIII(con);
                                                                    int MarksCount = TabData.size();
                                                                    int i = 0;
                                                                    ArrayList<model.StudentData> Student = mark.getStudents();
                                                                    int StudentCount = Student.size(), Stuindex = 0;
                                                                    String PreStudent = null;
                                                                %>

                                                                <% String stucenter = null; int tempStudentindex=Stuindex;
                                                                    for (Stuindex = 0; Stuindex < StudentCount; Stuindex++) {
                                                                        model.StudentData stu = Student.get(Stuindex);
                                                                        tempStudentindex++;
                                                                        //tempStudentindex++;
                                                                      if(stucenter != null && !stucenter.equals(stu.Centre) && Stuindex!=0)
                                                                        {
                                                                          tempStudentindex=0;
                                                                        }
                                                                %>
                                                                <center>
                                                                    <table width="100%" cellpadding="0"  >
                                                                        <tr>
                                                                        <div <%if (tempStudentindex % 5 == 0 && tempStudentindex > 1  || (stucenter != null && !stucenter.equals(stu.Centre))){%>class=break<% }%>>

                                                                            <%if ((tempStudentindex % 5 == 0 || Stuindex==0 ) || (stucenter != null && !stucenter.equals(stu.Centre))) {%>
                                                                            <center>
                                                                            <table>
                                                                                <tr>
                                                                                    <td></td>
                                                                                    <td align="center" >
                                                                                        <font size="2px"><b>Tabulation Register of <%=" " + Year + "" + Sem + " " + examconfirm.getCourseName(request.getParameter("Course"), con) + " "%>Off Campus Examination April-2011</b></font>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="left" class="textblack" colspan="2"><b> Centre:</b><%=stu.Centre%>  &nbsp;</td>
                                                                                </tr>
                                                                                <tr>
                                                                                <table width="100%" border="1" style="border-color: gray " cellpadding="3" cellspacing="0" >
                                                                                    <tr >

                                                                                        <td width=""  class="textforprint">&nbsp;</td>

                                                                                        <%
                                                                                            ArrayList<Subject> SubList = mark.getSubjectsForBranch();
                                                                                            int SubCount = SubList.size();
                                                                                            i = 0;
                                                                                            while (i < SubCount) {
                                                                                                Subject sub = SubList.get(i);
                                                                                        %>
                                                                                        <td  class="textforprint" width="115px" ><%=sub.SubjectName%></td>
                                                                                        <% i++;
                                                        }%>
                                                                                        <td rowspan="3" align="center" width="30px"class="textforprint">Grand Total</td>
                                                                                        <td rowspan="3" align="center" width="30px"class="textforprint">Pass/ Fail</td>
                                                                                        <td rowspan="3" align="center" width="60px"class="textforprint">Remarks</td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="textforprint" width="10px" >Max</td>
                                                                                        <%
                                                                                            i = 0;
                                                                                            String Max = "", Min = "";
                                                                                            while (i < SubCount) {
                                                                                                Subject sub1 = SubList.get(i);
                                                                                                if (sub1.SubjectBranchId.equals("160") || sub1.SubjectBranchId.equals("154")) {
                                                                                                    Max = "100";
                                                                                                    Min = "50";
                                                                                                } else {
                                                                                                    Max = sub1.MaxMark;
                                                                                                }
                                                                                        %>
                                                                                        <td class="textforprint"><div align="center"><%=Max%></div></td>
                                                                                        <% i++;
                                                        }%>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="textforprint" width="10px" >Min</td>
                                                                                        <%
                                                                                            i = 0;
                                                                                            while (i < SubCount) {
                                                                                                Subject sub2 = SubList.get(i);
                                                                                                if (sub2.SubjectBranchId.equals("160") || sub2.SubjectBranchId.equals("154")) {
                                                                                                    Max = "100";
                                                                                                    Min = "40";
                                                                                                } else {
                                                                                                    Min = sub2.MinMark;

                                                                                                }
                                                                                        %>
                                                                                        <td class="textforprint"><div align="center"><%=Min%></div></td>
                                                                                        <% i++;
                                                        }%>
                                                                                    </tr>
                                                                                </table></tr></table>
                                                                             </center>
                                                                                <%}%>
                                                                        <!--Mark Listing Starts here-->
                                                                        
                                                                        <table width="100%" border="1" style="border-color: gray " cellpadding="3" cellspacing="0">
                                                                            <tr>
                                                                                <td class="textforprint" width="" style="background-color: silver"><b><%=stu.PRN%></b></td>
                                                                                <td class="textforprint" width="80%" style="background-color: silver" ><b><%=stu.Name.toUpperCase()%></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                            <table width="100%" border="1" style="border-color: gray " cellpadding="3" cellspacing="0">
                                                                                <tr >

                                                                                    <td class="textforprint" width="10px"  >Ext</td>
                                                                                    <% String Total = " ";
                                                                                                                                            String Status = " ";
                                                                                                                                            boolean IsStudent = false;
                                                                                                                                            String str = " ";
                                                                                                                                            boolean flag = true;
                                                                                                                                            ;
                                                                                                                                            for (i = 0; i < MarksCount; i++) {
                                                                                                                                                Integer st = stu.StudentId;
                                                                                                                                                TabulationData Tab = TabData.get(i);
                                                                                                                                                if (Tab.StudentId.equals(st.toString())) {
                                                                                                                                                    Total = Tab.GrandTotal;

                                                                                                                                                    str = mark.getStudentWithheldDetails(Tab.StudentId);
                                                                                                                                                    flag = Tab.flag;
                                                                                                                                                    if (Tab.flag) {
                                                                                                                                                        Status = "Pass";
                                                                                                                                                        if (mark.getBranch().equals("13")) {
                                                                                                                                                            if (mark.getTotalPassMarkForBLiSc(con) > Integer.parseInt(Total)) {
                                                                                                                                                                Status = "Fail";
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    } else {
                                                                                                                                                        Status = "Fail";
                                                                                                                                                    }
                                                                                                                                                    if (str != null) {
                                                                                                                                                        Status = "Withheld";
                                                                                                                                                    }
                                                                                                                                                    IsStudent = true;
                                                                                    %>
                                                                                    <td  width="110px" class="textforprint" align="center"><% if (Tab.ExternalMark != null) {%><%=Tab.ExternalMark%><%} else {%> &nbsp;<% }%></td><% }
                                                                             }
                                                                             if (IsStudent) {%>
                                                                                    <td class="textforprint" rowspan="6" valign="bottom" width="30px"><b><%if (Total == null || (!flag)) {%>&nbsp; <% } else {%><%=Total%><%}%></b></td>
                                                                                    <td class="textforprint" rowspan="6" valign="bottom" width="30px"><b><%=Status%></b></td><% }%>
                                                                                    <td class="textforprint" rowspan="6" width="60px">&nbsp;</td> </tr>

                                                                                <tr>
                                                                                    <td class="textforprint" >Mod Ext</td>
                                                                                    <% for (i = 0; i < MarksCount; i++) {
                                                                                                                Integer st = stu.StudentId;
                                                                                                                TabulationData Tab = TabData.get(i);
                                                                                                                if (Tab.StudentId.equals(st.toString())) {%>
                                                                                    <td width="110px" class="textforprint" align="center"><%if (Tab.ModerationExt.equals(" ")) {%>&nbsp;<% } else {%><%=Tab.ModerationExt%><%}%></td><% }
                                                                                                            }%>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td class="textforprint"  >Int</td>
                                                                                    <% for (i = 0; i < MarksCount; i++) {
                                                                                                                Integer st = stu.StudentId;
                                                                                                                TabulationData Tab = TabData.get(i);
                                                                                                                if (Tab.StudentId.equals(st.toString())) {%>
                                                                                    <td width="110px" class="textforprint" align="center"><%if (Tab.InternalMark.equals(" ")) {%>&nbsp;<% } else {%><%=Tab.InternalMark%><%}%></td><% }
                                                                                                            }%>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td class="textforprint"  >Mod Int</td>
                                                                                    <% for (i = 0; i < MarksCount; i++) {
                                                                                                                Integer st = stu.StudentId;
                                                                                                                TabulationData Tab = TabData.get(i);
                                                                                                                if (Tab.StudentId.equals(st.toString())) {%>
                                                                                    <td width="110px" class="textforprint" align="center"><%if (Tab.ModerationInt.equals(" ")) {%>&nbsp;<% } else {%><%=Tab.ModerationInt%><%}%></td><% }
                                                                                                            }%>
                                                                                </tr>
                                                                                <tr >

                                                                                    <td class="textforprint"  ><b>Total</b></td>
                                                                                    <% for (i = 0; i < MarksCount; i++) {
                                                                                                                                                Integer st = stu.StudentId;
                                                                                                                                                TabulationData Tab = TabData.get(i);
                                                                                                                                                if (Tab.StudentId.equals(st.toString())) {
                                                                                                                                                    String Tot = Tab.TotalMark;
                                                                                                                                                    Integer Mark = mark.getTotalForEnglish(Tab.StudentId, con);
                                                                                                                                                    if (mark.getBranch().equals("5") || mark.getBranch().equals("9")) {
                                                                                                                                                        if (mark.IsStudentPassedForEnglish(Tab.StudentId, con)) {
                                                                                                                                                            if (Mark > 0) {
                                                                                                                                                                Tot = Tab.TotalMark;
                                                                                                                                                            } else {
                                                                                                                                                                Tot = " ";
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    if (Tab.SubjectBranchId.equals("160") || Tab.SubjectBranchId.equals("154")) {

                                                                                                                                                        if (mark.IsStudentPassedForEnglish(Tab.StudentId, con)) {
                                                                                                                                                            if (Mark > 0) {
                                                                                                                                                                Tot = Mark.toString();
                                                                                                                                                            } else {
                                                                                                                                                                Tot = " ";
                                                                                                                                                            }
                                                                                                                                                        } else {
                                                                                                                                                            Tot = " ";
                                                                                                                                                        }
                                                                                                                                                    }

                                                                                    %>
                                                                                    <td width="110px" class="textforprint" align="center"><b><%if (Tot.equals(" ")) {%>&nbsp;<% } else {%><%=Tot%><%}%></b></td><% }
                                                                                                            }%>

                                                                                </tr>

                                                                                <% stucenter = stu.Centre;%>

                                                                            </table>
                                                                            </tr>
                                                                        </table>
                                                                                </div>
                                                                        </tr>
                                                                    </table>
                                                                </center>
                                                                    <%}%>
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