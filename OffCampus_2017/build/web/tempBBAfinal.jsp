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
                                                            <form name="TabulationRegister" id="TabulationRegister" action="TabulationRegister.jsp" method="post" style="min-height: 300px">
                                                                <input type="button" class="printbutton" id="btnPrint" value="PRINT " onclick="javascript:printtoken();" /><br>
                                                                <table   width="100%" style=" min-height: 500px;" cellspacing="0" cellpadding="3" >
                                                                    <tr  align="center" >
                                                                        <td height="20px" colspan="2"> <b> <font color="BLACK">MAHATMA GANDHI UNIVERSITY</font></b></td>
                                                                    </tr>
                                                                    <tr align="center" >
                                                                        <td height="15px" colspan="2"><b><%=mark.getExamName()%></b></td>
                                                                    </tr>
                                                                    <tr>
                                                                    <br>
                                                                    </tr>
                                                                    <%
                                                                        String PrevCentre = "";
                                                                        ArrayList<Entity.StudentData> StudentList = mark.getStudentsOfCourse();
                                                                            //ArrayList<Entity.StudentData> StudentList = mark.getStudentsOfCourse_BcomPartIII(); 
                                                         
                                                                            int count = StudentList.size();
                                                                        int i = 0;
                                                                        if (count > 0) {
                                                                            Connection con = new DBConnection().getConnection();
                                                                    %>
                                                                    <tr>
                                                                        <td>

                                                                            <% int var =1;
                                                                                                        while (i < count) {
                                                                                                             Entity.StudentData Student =null;
                                                                                                             Student=StudentList.get(i);
                                                                                                             //out.println("***"+i+Student.StudentName );
                                                                                                            int Sem = 0;
                                                                                                           
                                                                                                            boolean flag = true, EngFailed = false;
                                                                            %>
                                                                             
                                                                                
                                                                            <table align="center" width="100%" >
                                                                                    <% if (((i + 1) % var == 0 )) {
                                                                             //if (((i + 1) % var == 0 || (!Student.CollegeId.equals(PrevCentre))) && i > 1)%>
                                                                             
                                                                                <%//out.println((i + 1) % var);
                                                                                //out.println(i);%>
                                                                                <div class="break" ></div><% }%>
                                                                                    <tr>
                                                                                        <td align="left" class="textblack" style="background-color: silver" ><b><%=i + 1%>. Name :</b><%=Student.StudentName%></td>
                                                                                        <td align="left" class="textblack"><b>Course :</b><%=Student.BranchName%></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td align="left" class="textblack" style="background-color: silver"><b>PRN :</b><%=Student.PRN%></td>

                                                                                        <td align="left" class="textblack"><b><!--<%=mark.YearOrSem()%> :</b><%=mark.getSemYear()%>--></td>
                                                                                        <td align="right" class="textblack"><b>Centre :</b><%=Student.CollegeName%></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td colspan="3" align="center" class="textblack">
                                                                                            <table border="1"  width="100%" style="border-style: groove; " cellspacing="0" cellpadding="3" >
                                                                                                <tr>
                                                                                                    <td class="textblack" rowspan="2"><div align="center">Subject</div></td>
                                                                                                    <td class="textblack" colspan="4"><div align="center">External </div></td>
                                                                                                    <td class="textblack" colspan="3"><div align="center">Internal </div></td>
                                                                                              <!-- <td class="textblack" colspan="1"><div align="center">Practical </div></td>-->
                                                                                                    <td class="textblack" colspan="2"><div align="center">Total Mark</div></td>
                                                                                                    <td class="textblack" rowspan="2"><div align="center">Status</div></td>
                                                                                                    <td class="textblack" rowspan="2"><div align="center">Exam</div></td>
                                                                                                    <td class="textblack" rowspan="2"><div align="center">Remarks</div></td>
                                                                                                </tr>
                                                                                                <tr>
                                                                                                    <td class="textblack"><div align="center">Min</div></td>
                                                                                                    <td class="textblack"><div align="center">Mark</div></td>
                                                                                                    <td class="textblack"><div align="center">Mod</div></td>
                                                                                                    <td class="textblack"><div align="center">ClassMod</div></td>                                                                                               
                                                                                                    <td class="textblack"><div align="center">Min</div></td>
                                                                                                    <td class="textblack"><div align="center">Mark</div></td>
                                                                                                    <td class="textblack"><div align="center">Mod</div></td>
                                                                                                   <!-- <td class="textblack"><div align="center">Pract</div></td>-->
                                                                                                    <td class="textblack"><div align="center">Min</div></td>                                              
                                                                                                    <td class="textblack"><div align="center">Mark</div></td>
                                                                                                </tr>
                                                                                                <%
                                                                                                                                boolean IsGPassed = true;
                                                                                                                                String bComPass = "Passed";
                                                                                                                                String bComFailed = "Failed";
                                                                                                                                String Status = mark.getStudentWithheldDetails(Student.StudentId);
                                                                                                                                if (Status != null) {
                                                                                                %>
                                                                                                <tr>
                                                                                                    <td class="textblack" colspan="10" align="center"><font color="#a02a28"><%=Status%></font></td>
                                                                                                    <td class="textblack" >&nbsp;</td>
                                                                                                </tr><% } else {
                                                                                                                                             // ArrayList<TabulationData> TabData = mark.getStudentMarkDetails_BcomPartIII(Student.StudentId, con);
                                                                                                                                    ArrayList<TabulationData> TabData = mark.getStudentMarkDetails(Student.StudentId,con);
                                                                                                                                              int Sub = TabData.size();
                                                                                                                                              int j = 0;
                                                                                                                                              while (j < Sub) {
                                                                                                                                                  //#####################################
                                                                                                                                                  String fontstyle = "";
                                                                                                                                                  //######################################
                                                                                                                                                  TabulationData Tab = TabData.get(j);
                                                                                                                                                  flag = Tab.flag;
                                                                                                                                                  EngFailed = Tab.IsFailedForEnglish;
                                                                                                                                                  if (!flag) {
                                                                                                                                                      if (Student.BranchId.equals("5") || Student.BranchId.equals("9")) {
                                                                                                                                                      }

                                                                                                                                                  }
                                                                                                                                                  // TO CHECK WHETHER MARK SCORED IN CURRENT EXAM
                                                                                                                                                  if (!request.getParameter("Exam").equals(Tab.oldExam)) {
                                                                                                                                                      fontstyle = "bold";
                                                                                                                                                      
                                                                                                                                                  }
                                                                                                                                                  //CHECKING ENDS HERE

                                                                                                %>
                                                                                                <tr>
                                                                                                    <td class="textblack"><%=Tab.SubjectName%></td>
                                                                                                    <td class="textblack"><%if (Tab.ExtMin.equals(" ")) {%>&nbsp;<% } else {%><%=Tab.ExtMin%><% }%></td>

                                                                                                    <td class="textblack"><%=Tab.ExternalMark%></td>
                                                                                                    <td class="textblack"><%if (Tab.ModerationExt.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.ModerationExt%></font><% }%></td>
                                                                                                    <td class="textblack"><%if (Tab.ClassModeration.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.ClassModeration%></font><% }%></td>
                                                                                                    
                                                                                                    <td class="textblack"><%if (Tab.IntMin.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.IntMin%></font><% }%></td>
                                                                                                    <td class="textblack"><%if (Tab.InternalMark.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.InternalMark%></font><% }%></td>
                                                                                                    <td class="textblack"><%if (Tab.ModerationInt.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.ModerationInt%></font><% }%></td>
                                                                                                  
                                                                                                    <td class="textblack"><%if (Tab.PassMark.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.PassMark%></font><% }%></td>
                                                                                                    <td class="textblack"><%if (Tab.TotalMark.equals(" ")) {%>&nbsp;<% } else {%><font style="font-weight:<%=fontstyle%>"><%=Tab.TotalMark%></font><% }%></td>
                                                                                                    <% if (Tab.SubjectBranchId.equals("61") || Tab.SubjectBranchId.equals("72") || Tab.SubjectBranchId.equals("207") || Tab.SubjectBranchId.equals("213")) {%>
                                                                                                    <td>&nbsp;</td><% } else {%>
                                                                                                    <td class="textblack"><%=Tab.Status%></td><% }%>
                                                                                                    <td class="textblack" ><%if (Tab.oldExam.equals("1")) {%>2011<% } else if (Tab.oldExam.equals("2")) {%>2012<% }else if (Tab.oldExam.equals("3")) {%>2013<% }else if (Tab.oldExam.equals("4")) {%>2014<% }%></td>
                                                                                                    <td class="textblack" width="30%">&nbsp;</td>

                                                                                                </tr>

                                                                                                <%
                                                                                          j++;
                                                                                      }%>
                                                                                                <%-- if (((!Student.BranchId.equals("7")) && (!Student.BranchId.equals("8")))) {%>
                                                                                                <tr>
                                                                                                    <td class="textblack" colspan="11" align="right" height="20px"><b>Grand Total</b></td>
                                                                                                    <%if (Student.BranchId.equals("13")) {%>
                                                                                                    <td class="textblack"><% if (flag && Integer.parseInt(mark.getSemTotalForStudent(Student.StudentId, con)) >= mark.getTotalPassMarkForBLiSc(con)) {%><%=mark.getSemTotalForStudent(Student.StudentId, con)%><% } else {
                                                 IsGPassed = false;%>&nbsp;<% }%></td>
                                                                                                    <% } else {%>
                                                                                                    <td class="textblack"><% if (flag) {%><%=mark.getSemTotalForStudent(Student.StudentId, con)%><% } else {
                                                 IsGPassed = false;%>&nbsp;<% }%></td>
                                                                                                    <% }%>
                                                                                                </tr>
                                                                                                <%}--%><%
                                                                        }%>

                                                                                                <%if (mark.getSemYear().equals("2")) {
                                                                            if (Student.BranchId.equals("5") || Student.BranchId.equals("9")) {%>
                                                                                                <tr>
                                                                                                    <td class="textblack" colspan="11" align="right"><b>Grand Total for English Paper I and II</b></td>
                                                                                                    <td class="textblack"><%if (mark.IsStudentPassedForEnglish(Student.StudentId, con)) {%><b><%=mark.getTotalForEnglish(Student.StudentId, con)%></b><% } else {%>&nbsp; <%}%></td>
                                                                                                </tr><% }
                                                                                        
                                                                        }%>
                                                                         <%if (mark.getSemYear().equals("2")) {
                                                                            if (Student.BranchId.equals("7") || Student.BranchId.equals("8")) {%>
                                                                                                <tr>
                                                                                                    <td class="textblack" colspan="11" align="right"><b>Grand Total for English Paper I and II</b></td>
                                                                                                    <td class="textblack"><%if (mark.IsStudentPassedForEnglishBcom(Student.StudentId)) {%><b><%=mark.getTotalForEnglishBcom(Student.StudentId)%></b><% } else {%>&nbsp; <%}%></td>
                                                                                                </tr><% }
                                                                                        
                                                                        }%>
                                                                                                <!-- NEW CODE FOR DISPLAYING SEM TOTALS FOR MCOM--->
                                                                                               <% int GTotal = 0;
                                                                                               String t ;
                                                                                                                                // if(mark.getSemYear().equals("4") && mark.getBranch().equals("17")){
                                                                                                                                if (mark.getSemYear().equals("6")) {
                                                                                                                                    for (int x = 2; x < 7; x++) {
                                                                                                                                        int num = 0;
                                                                                                                                        try {
                                                                                                                                            num = Integer.parseInt(mark.getSemTotal(Integer.parseInt(Student.StudentId), con, x));

                                                                                                                                        } catch (NumberFormatException e) {
                                                                                                                                            num = 0;
                                                                                                                                        }
                                                                                                                                        //if(mark.IsStudentPassedForSemester(Integer.parseInt(Student.StudentId), con,x))
                                                                                                                                        GTotal += num;
                                                                                                                                        if (x <7) {
                                                                                                %>
                                                                                               <tr>
                                                                                                    <td class="textblack" colspan="11" align="right"> Total for Semester <%=x%></td>
                                                                                                    <td class="textblack"><%if (mark.IsStudentPassedForSemester(Integer.parseInt(Student.StudentId), con, x) && mark.getSemTotalForStudent(Student.StudentId, con) != null) {%><%=mark.getSemTotalForStudent(Student.StudentId, con)%><% } else {
                                                IsGPassed = false;%>&nbsp; <%}%></td>

                                                                                                </tr>

                                                                                                <%}
                                                                                    }%>

                                                                                               <tr>
                                                                                                    <td class="textblack" colspan="11" align="right"><b>Grand Total </b></td>
                                                                                                    <td class="textblack"><%if (IsGPassed) {%><b><%=GTotal%><% } else {%>&nbsp; <%}%></b></td>
<td><%if (GTotal> 2174 ) {%>First Class with Distinction <%}else if (GTotal< 2175 && GTotal>1739){%>First Class<%}else if (GTotal< 1449 ){%>Second Class<%}%></td>
                                                                                                </tr>


                                                                                                <%}%>
                                                                              
                                                                                        <td>

                                                                                        </td>
                                                                                    </tr>
                                                                                    <% PrevCentre = Student.CollegeId;
                                                    i++;
                                                }%>
                                                                                </table>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <% if (!con.isClosed()) {
                                                con.close();
                                            }
                                                                                                        } else {%>
                                                                    <tr>
                                                                        <td height="15px" colspan="2"><b>No Records </b></td>
                                                                    </tr>
                                                                    <% }%>

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
    </body></html><% }%>

