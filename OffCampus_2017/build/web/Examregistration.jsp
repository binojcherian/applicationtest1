<%-- 
    Document   : Examregistration
    Created on : Apr 3, 2012, 2:42:35 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" language="java" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%if (request.getSession().getAttribute("UserName") == null) {
                response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
            } else {
                ExamStudentsList StuList = new ExamStudentsList(request, response);

                String message = null;
                //String CenterId = "54";

                //QualificationDetails Qualdetails = new QualificationDetails(request, response);
                CourseSelection cs = new CourseSelection(request, response);
                String BranchId = null;
                if (request.getSession().getAttribute("BranchId") != null) {
                    BranchId = request.getSession().getAttribute("BranchId").toString();
                }
                String yearApplied = "0";
                int attendingYear = 0;
                if (request.getSession().getAttribute("yearApplied") != null) {
                    yearApplied = request.getSession().getAttribute("yearApplied").toString();
                    attendingYear = Integer.parseInt(yearApplied);
                }
                int joinYear = 0;
                if (request.getSession().getAttribute("joinYear") != null) {
                    String temp = request.getSession().getAttribute("joinYear").toString();
                    joinYear = Integer.parseInt(temp);
                }
                model.mStudentList sList = new model.mStudentList();
                Connection con = new DBConnection().getConnection();

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
                                                        <form name="Examregistration"  id="Examregistration" action="Examregistration.jsp" method="post">
                                                            <input type="hidden" name="isPostBack" value="Yes"/>
                                                            <b> <font color="#a02a28" size="2px"><u>Verify Student Details</u></font></b>
                                                            <center>
                                                                <table  border="0"cellpadding="5%" width="90%">

                                                                    <tr class="textblack">
                                                                        <td align="left" >

                                                                            <fieldset style="font-size: 14px;color:black;border-color: #6c4303" ><legend style="font-size: 14px;font-weight:bold " >Search </legend>
                                                                                <table width="100%">
                                                                                    <tr><td class="textblack">
                                                                                            Centre</td><td>

                                                                                            <% ArrayList<CentreDetails> CentreList = sList.getCentres();
                                             if (CentreList == null || CentreList.size() == 0) {%>
                                                                                            <span class="Error">No Centres Assigned to you </span>
                                                                                            <%} else {
                                                                                                                                              int i = 0;
                                                                                                                                              int count = CentreList.size();
                                                                                                                                              CentreDetails Centre;
                                                                                            %>
                                                                                            <select name="CentreId" id="CentreId"  style="width: 250px;" title="Select your centre">
                                                                                                <option value="-1" >--------Select-------</option>
                                                                                                <%while (i < count) {
                                                          Centre = CentreList.get(i);%>
                                                                                                <option value="<%=Centre.CentreId%>" <%if (StuList.getCenterId() != null && Integer.parseInt(StuList.getCenterId()) == (Centre.CentreId)) {
                                                                                                        out.print("selected");
                                                                                                    }
                                                                                                        %>> <%=Centre.CentreName%></option>

                                                                                                <%        i++;
                 }//while
             }%>

                                                                                            </select></td><td class="textblack">Course</td><td>
                                                                                            <select name="course_applied" id="course_applied"   style="width: 215px;" title="Select your course" >

                                                                                                <option value="-1" >--------Select------- </option>
                                                                                                <%Set CourseList = cs.ListCourseNamesRegular();
                                                                                                    for (Object obj : CourseList) {
                                                                                                        Map.Entry<String, String> List = (Map.Entry<String, String>) obj;
                                                                                                %>
                                                                                                <option <%if (StuList.getCourseApplied() != null && StuList.getCourseApplied().equals(List.getKey())) {
                                                                                                                                                                out.print("selected");
                                                                                                                                                            }
                                                                                                    %>
                                                                                                    value="<%=List.getKey()%>"><%=List.getValue()%></option>

                                                                                                <% }
                                                                                                %>
                                                                                            </select></td></tr>
                                                                                    <tr><td class="textblack">

                                                                                            Year/Semester</td><td>

                                                                                            <select name="year_applied" id="year_applied"   style="width: 215px;" title="Select your year" >

                                                                                                <option value="-1" >--------Select------- </option>
                                                                                             <!--   <option value="1" <%if (StuList.getYearApplied() != null && StuList.getYearApplied().equals("1")) {%> selected="selected"<%}%> > First year</option>-->
                                                                                                <option value="2" <%if (StuList.getYearApplied() != null && StuList.getYearApplied().equals("2")) {%> selected="selected"<%}%>> Second year</option>
                                                                                                <option value="3" <%if (StuList.getYearApplied() != null && StuList.getYearApplied().equals("3")) {%> selected="selected"<%}%>> Third year</option>

                                                                                            </select></td>
                                                                                        <!--<td class="textblack">
                                                                                            Admission Year</td><td>

                                                                                            <select name="join_year" id="join_year"   style="width: 100px;" title="Admission Year" >

                                                                                                <option value="2010" <%--if (joinYear == 2010) {%> selected="selected"<%}%> >2010</option>
                                                                                                <option value="2011" <%if (joinYear == 2011) {%> selected="selected"<%}%>  >2011</option>
                                                                                                <option value="2012" <%if (joinYear == 2012) {%> selected="selected"<%}%> >2012</option>
                                                                                                <option value="2013" <%if (joinYear == 2013) {%> selected="selected"<%}--%> >2013</option>
                                                                                            </select>
                                                                                        </td>-->
                                                                                    </tr><tr><td colspan="4" align="center">
                                                                                            <input type="submit" name="search_button"id="search_button" value="Search"/></td>
                                                                                    </tr>
                                                                                    <%
                                                if (StuList.getError() != null) {%>
                                                                                    <tr>
                                                                                        <td>
                                                                                            <span class="textblack" style="color: red "><%=StuList.getError()%></span>
                                                                                        </td>
                                                                                    </tr><%}%>
                                                                                </table>
                                                                            </fieldset>
                                                                        </td>

                                                                    </tr>

                                                                </table> </center>


                                                            <!-- Pagination Starts-->


                                                            <%if (StuList.getSearch()) {
                                                                    //Connection con = new DBConnection().getConnection();
                                                                    int TotalRows = 0, TotalAmt = 0;
                                                                    ResultSet rsPagination = null;
                                                                    ResultSet rsRowCnt = null;

                                                                    PreparedStatement psPagination = null;
                                                                    PreparedStatement psRowCnt = null;

                                                                    try {
                                                                        String sqlPagination = "SELECT SQL_CALC_FOUND_ROWS StudentPersonal.*,StudentExamFeeStatus.isConfirmed,StudentExamFeeStatus.IsMguApproved as Approval,StudentExamFeeStatus.Remarks ,IFNULL(StudentFeeId,0) StudentFeeId   FROM StudentPersonal LEFT JOIN StudentExamFeeStatus on StudentPersonal.StudentId = StudentExamFeeStatus.StudentId "
                                                                                + " and  StudentExamFeeStatus.examId=(SELECT max(ExamId) FROM ExamMaster E) left join StudentFeeMap F on F.StudentId=StudentPersonal.StudentId and F.StudentAttendingYear=StudentPersonal.AttendingYear where StudentPersonal.CollegeId=" + StuList.getCenterId() + "  and  StudentPersonal.isMGUApproved=1  " + StuList.getSearchQuery() + " order by PRN";//limiting no of records

                                                                        psPagination = con.prepareStatement(sqlPagination);
                                                                        
                                                                        //psPagination.setInt(1, Integer.parseInt(request.getParameter("AdmissionYear")));
                                                                        rsPagination = psPagination.executeQuery();
                                                                    } catch (Exception ex) {
                                                                        out.print(ex);
                                                                    }
                                                                    Boolean notFound = false;
                                                                    if (!rsPagination.next()) {
                                                                        notFound = true;
                                                                        message = " No Matching Records Found";
                                                                    }
                                                                    String sqlRowCnt = "SELECT FOUND_ROWS() as cnt";

                                                                    psRowCnt = con.prepareStatement(sqlRowCnt);
                                                                    rsRowCnt = psRowCnt.executeQuery();

                                                                    if (rsRowCnt.next()) {
                                                                        TotalRows = rsRowCnt.getInt("cnt");
                                                                    }
                                                            %>

                                                            <% if (message != null) {%>
                                                            <center>
                                                                <table><tr>
                                                                        <td></td>
                                                                        <td colspan="">  <span  class="Error" style="color:red">*<%=message%>  </span></td>
                                                                    </tr><br/></table> </center>
                                                                    <%} else {%>
                                                            <center>


                                                                <br>
                                                                <table width="90%">
                                                                    <tr align="center">
                                                                        <td colspan="2" class="textblack"><b>Registered Students : <%=sList.RegisteredStudentsCount(Integer.parseInt(StuList.getCourseApplied()), Integer.parseInt(StuList.getCenterId()), Integer.parseInt(StuList.getYearApplied()), con)%>/<%=TotalRows%></b></td>
                                                                        <td colspan="2" class="textblack"><b>Confirmed by Centre : <%=sList.ConfirmedStudentsCount(Integer.parseInt(StuList.getCourseApplied()), Integer.parseInt(StuList.getCenterId()), Integer.parseInt(StuList.getYearApplied()), con)%>/<%=TotalRows%></b></td>
                                                                        <td colspan="2" class="textblack"><b>Mgu Approved Students : <%=sList.MguApprovedStudentsCount(Integer.parseInt(StuList.getCourseApplied()), Integer.parseInt(StuList.getCenterId()), Integer.parseInt(StuList.getYearApplied()), con)%>/<%=TotalRows%></b></td>
                                                                    </tr>
                                                                </table>
                                                                <br>

                                                                <table BORDER="1" width="95%" cellspacing="0" cellpadding="3" class="textblack" style="border-style: groove;font-size: 13px;">
                                                                    <thead><font size="4" color="#2595CD"></font></thead>
                                                                    <TR bgcolor="#dedede" align="center">
                                                                        <TH style="font-size: small;">Select</TH>
                                                                        <TH style="font-size: small;">SLNo</TH>
                                                                        <TH style="font-size: small;">PRN</TH>
                                                                        <TH style="font-size: small;">Student Name</TH>

                                                                        <TH style="font-size: small;">Status</TH>
                                                                        <% if (BranchId != null && yearApplied.equals("2") && (BranchId.equals("21") || BranchId.equals("17"))) {%><TH style="font-size: small;">Option Selection</TH><%} else if (StuList.getBranchOptionals().isEmpty() || StuList.getBranchOptionals().get(0).isEmpty()) {%><TH style="font-size: small;">Data
                                                                        </TH>
                                                                        <% } else {%><TH style="font-size: small;">Option Selection</TH><%}%>

                                                                        <TH style="font-size: small;">Remarks</TH>
                                                                    </TR>

                                                                    <%// if(StuList.getYearApplied()!=null && StuList.getYearApplied().equals("2") && )

                                                                         int j = 0;

                                                                         if (!notFound) {
                                                                             do {

                                                                                 j++;

                                                                    %>
                                                                    <tr>

                                                                        <%

                                                                                                                        int sid = rsPagination.getInt("StudentId");
                                                                                                                         int FeeId=rsPagination.getInt("StudentFeeId");

                                                                                                                        boolean isSlected = false;
                                                                                                                        boolean isConfirmed = false, IsCheck = false,isImprovement=false;
                                                                                                                        int branch = 0;
                                                                                                                        if (BranchId != null) {
                                                                                                                            branch = Integer.parseInt(BranchId);
                                                                                                                            isSlected = StuList.getStudentList().contains(new Integer(sid));

                                                                                                                        }
                                                                                                                        ArrayList<SubjectBranch> branchs = sList.getSupplyOrImprovementPapers(sid, branch, joinYear, attendingYear);
                                                                                                                        ArrayList<SubjectBranch> ImproveList=sList.getImprovementPapers(sid,branch,attendingYear);
                                                                                                                        boolean isSupply = false;
                                                                                                                        String StuStatus = sList.getStudentWithheldDetails(sid);

                                                                                                                        if (branchs != null && !branchs.isEmpty()) {
                                                                                                                            isSupply = true;
                                                                                                                        }
                                                                                                                         if(ImproveList!=null&&!ImproveList.isEmpty())
                                                                                                                         isImprovement=true;
                                                                                                                        String str = rsPagination.getString("StudentExamFeeStatus.isConfirmed");
                                                                                                                        String color = "", Status = "";
                                                                                                                        String rcolor = "#bd3d1b";
                                                                                                                        if (rsPagination.getString("StudentExamFeeStatus.isConfirmed") != null && rsPagination.getString("StudentExamFeeStatus.isConfirmed").equals("1")) {
                                                                                                                            isConfirmed = true;
                                                                                                                        } else {
                                                                                                                            isConfirmed = false;
                                                                                                                        }
                                                                                                                 if(FeeId==0){ 
                                                                                                                             Status = "Course Fee Pending";
                                                                                                                             color = "#B95835";
                                                                                                                             IsCheck = false;
                                                                                                                            }
                                                                                                                       else if (rsPagination.getString("StudentExamFeeStatus.isConfirmed") == null) {
                                                                                                                            Status = "Not Registered";
                                                                                                                            color = "#d84937";
                                                                                                                            IsCheck = true;
                                                                                                                        } else if (isSlected || rsPagination.getString("StudentExamFeeStatus.isConfirmed").equals("0")) {
                                                                                                                            Status = "Centre Not Confirmed";
                                                                                                                            color = "blue";
                                                                                                                            IsCheck = true;
                                                                                                                        }
                                                                                                                        if (!isSlected && FeeId != 0) {
                                                                                                                                Status = "Not Registered";
                                                                                                                                color = "#d84937";
                                                                                                                                    
                                                                                                                                if ((StuList.getBranchOptionals().isEmpty() || StuList.getBranchOptionals().get(0).isEmpty())) {
                                                                                                                                    if (yearApplied.equals("2") && (BranchId.equals("21") || BranchId.equals("17"))) {
                                                                                                                                        IsCheck = false;
                                                                                                                                    } else if (BranchId != null && yearApplied.equals("3") && BranchId.equals("18")) {
                                                                                                                                        IsCheck = false;
                                                                                                                                    }else if (BranchId != null && yearApplied.equals("1") && BranchId.equals("26")) {
                                                                                                                                        IsCheck = false;
                                                                                                                                    } else {
                                                                                                                                        IsCheck = true;
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    IsCheck = false;
                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (isConfirmed) {
                                                                                                                                color = "#FF7E1B";
                                                                                                                                Status = "Centre Confirmed";
                                                                                                                                IsCheck = true;
                                                                                                                        }
                                                                                                                        if (rsPagination.getInt("Approval") == 1) {
                                                                                                                            color = "#19610f";
                                                                                                                            Status = "Mgu Approved";
                                                                                                                            IsCheck = false;
                                                                                                                        }
                                                                                                                        if (StuStatus != null) {
                                                                                                                            //IsCheck = true;
                                                                                                                        }
                                                                                                                        if (rsPagination.getString("StudentExamFeeStatus.Remarks") == null) {
                                                                                                                            rcolor = "#82bd1b";
                                                                                                                        }
                                                                        %>
                                                                        <td class="textblack" align="left"><%if (IsCheck) {%><input type="checkbox" name="Select"id="Select" Value="<%=rsPagination.getString("StudentId")%>"><% } else {%>&nbsp;<%}%> </td>
                                                                        <td>&nbsp;<%=j%></td>

                                                                        <td><% if (rsPagination.getString("PRN") != null) {
                                                                                    out.print(rsPagination.getString("PRN"));
                                                                                } else {
                                                                                    out.println("Not generated");
                                                                                }%></td>

                                                                        <td><%=rsPagination.getString(2)%></td>

                                                                        <td><font style="color: <%=color%>"><%=Status%></font><%if (Status.isEmpty()) {%>&nbsp;<% }%></td>

                                                                        <td>
                                                                            <% if(Status!=null && !Status.equals("Mgu Approved")){
                                                                                    if (StuStatus != null) {%>
                                                                          <span style="color: maroon" ><%--=StuStatus--%></span>
                                                                            <%} else if ((isSlected && FeeId!=0 ) || (isSupply && isSlected)) {%>

                                                                            <a style="color: <%=color%>" href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Edit Supply/Improvement/Elective</a>

                                                                            <% } else {
                                                                    if (FeeId!=0 && BranchId != null && (yearApplied.equals("2") && (BranchId.equals("21") || BranchId.equals("17"))) || (yearApplied.equals("1") && BranchId.equals("26"))) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Opt Supply/Elective</a>  <%} else if (BranchId != null && yearApplied.equals("3") && BranchId.equals("18")) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Opt Supply/Elective</a>
                                                                    <%} else if (FeeId!=0 && BranchId != null && yearApplied.equals("3") && BranchId.equals("20")) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>"><span style="color: <%=color%>" >Opt Supply/Elective</span></a>
                                                                            <%} else if (FeeId!=0 && BranchId != null && yearApplied.equals("1") && (BranchId.equals("24") || BranchId.equals("25"))) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>"><span style="color: <%=color%>" > Opt Elective</span></a>
                                                                            <%} else if ((StuList.getBranchOptionals().isEmpty() || StuList.getBranchOptionals().get(0).isEmpty()) && !isSupply && FeeId!=0 &&  !isImprovement) {%> &nbsp;
                                                                          
                                                                            <%} else if(FeeId!=0) {%>
                                                                            <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>" style="color:<%=color%>">Opt Improvement/Supply/Elective</a><%}
                                                                                                                                                 else{%>
                                                                                                                                                 <span style="color: <%=color%>">Data Locked</span>
                                                                                                                                                 <%}  }
                                                                                  }else {%><span style="color: <%=color%>">Data Locked</span><% }%>
                                                                        </td>
                                                                        <td><a  style="color:<%=rcolor%>" href="ExamRemarks.jsp?StudentId=<%=rsPagination.getString("StudentId")%>"> Update Remarks</a></td>
                                                                    </tr>
                                                                    <%  } while (rsPagination.next());
                                 }%>


                                                                    <tr>
                                                                        <td colspan="7"class="textblack" align="left"><input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="SelectAllRows(this.form);"><b>SelectAll</b> </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="7" align="center"> <span><input type="submit"  name="submit" value="Approve" onclick='return confirm("Are you sure you want to Approve  all these students for Exam?")' />
                                                                            </span>
                                                                        </td>
                                                                    </tr>

                                                                </table></center><%}%>
                                                            <%}%> </form>
                                                            <% if (con != null) {
                                con.close();
                            }%>
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