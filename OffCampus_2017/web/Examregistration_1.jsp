<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,java.sql.*,model.*,Entity.SubjectBranch " %>



<%if(request.getSession().getAttribute("UserName")==null||request.getSession().getAttribute("CenterId")==null){
 response.sendRedirect(response.encodeRedirectURL("Login.jsp?message=Session Time Out.Login again"));
}else{
             ExamStudentsList StuList=new ExamStudentsList(request,response);
            if(!StuList.isRedirected())
                {
            String message = null;
            String CenterId = (String) request.getSession().getAttribute("CenterId");
            QualificationDetails Qualdetails = new QualificationDetails(request, response);
            CourseSelection  cs=new CourseSelection(request, response);
            String BranchId=null;
           if( request.getSession().getAttribute("BranchId")!=null)
           BranchId=request.getSession().getAttribute("BranchId").toString();
            String yearApplied="0";int attendingYear=0;
           if( request.getSession().getAttribute("yearApplied")!=null){
            yearApplied=request.getSession().getAttribute("yearApplied").toString();
            attendingYear=Integer.parseInt(yearApplied);
          }
            int joinYear=0;
            if( request.getSession().getAttribute("joinYear")!=null){
            String temp=request.getSession().getAttribute("joinYear").toString();
            joinYear=Integer.parseInt(temp);
          }
            model.mStudentList sList=new model.mStudentList();

            %>

            <link rel="shortcut icon" href="../favicon.ico" type="image/png" />
           <script src="js/Course.js" type="text/javascript"></script>
            <html><head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
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

<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<jsp:include page="Header_StudentManagement.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
    <td width="200">


       </td>
    <td width="20" bgcolor="#ffffff"><img src="images_newStyle/spacer.gif" alt="" height="10" width="20"></td>
    <td width="90%">
      <table width="100%" bgcolor="#f9fbff" border="0" cellpadding="0" cellspacing="0">
      <tbody><tr>
        <td colspan="5" bgcolor="#f9fbff"><img src="images_newStyle/spacer.gif" alt="" height="10" width="609"></td>
      </tr>
      <tr>
        <td width="16" bgcolor="#f9fbff"><img src="images_newStyle/spacer.gif" alt="" height="18" width="8"></td>
	<td>
<div class="textblack"></div>	</td>
        <td bgcolor="#f9fbff" >&nbsp;  <%-- Content --%>

         <%-- Content --%>
         <form name="Examregistration"  id="Examregistration" action="Examregistration.jsp" method="post">

             <input type="hidden" name="isPostBack" value="Yes"/>
              <font face="verdana,geneva" size="2" color="blue"> <h4><b>For taking print out of Confirmed Students Click  </b><font face="verdana,geneva" size="3" color="green"><a href="ExamConfirmedList.jsp">Exam Confirmed List</a></font></h4> </font>

                 <center>

                        <table  border="0"cellpadding="5%" >

                                            <tr>
                                                <br>
                                                <br>
                                            </tr>
                            <tr class="textblack">
                                <td align="left" >

                                    <fieldset style="font-size: 14px;color:black;border-color: #0099CC "><legend style="font-size: 14px;font-weight:bold " >Search </legend>

                                        CourseName

                                        <select name="course_applied" id="course_applied"   style="width: 215px;" title="Select your course" >

                                            <option value="-1" >--------Select------- </option>
                                            <%Set CourseList = cs.ListCenterCourseNames();
                                                        for (Object obj : CourseList) {
                                                            Map.Entry<String, String> List = (Map.Entry<String, String>) obj;
                                            
                                            if(!List.getKey().equals("21") ){
                        %>
                                            <option <%if (StuList.getCourseApplied()!=null && StuList.getCourseApplied().equals(List.getKey())) {
                                                                                                               out.print("selected");
                                                                                                           }
                                                                                                       %>
                                                value="<%=List.getKey()%>"><%=List.getValue()%></option> <%}

                                             }
                                            %>
                                        </select>

                                        &nbsp;&nbsp;Year/Semester

                                        <select name="year_applied" id="year_applied"   style="width: 215px;" title="Select your year" >

                                            <option value="-1" >--------Select------- </option>
                                            <option value="1" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("1")) {%> selected="selected"<%}%> > First year</option>
                                             <option value="2" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("2")) {%> selected="selected"<%}%>> Second year</option>
                                              <option value="3" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("3")) {%> selected="selected"<%}%>> Third year</option>
                                        </select>
                                             
                                          &nbsp;&nbsp;    Admission Year

                                        <select name="join_year" id="join_year"   style="width: 100px;" title="Admission Year" >

                                           <option value="2010" <%if (joinYear==2010) {%> selected="selected"<%}%> >2010</option>
                                           <option value="2011" <%if (joinYear==2011) {%> selected="selected"<%}%>  >2011</option>
                                            <option value="2012" <%if (joinYear==2012) {%> selected="selected"<%}%> >2012</option>
                                           </select> &nbsp;&nbsp;
                                        <input type="submit" name="search_button"id="Search_button" value="Search"/>

                                    </fieldset>
                                </td>

                            </tr>
                        </table> </center>


                    <!-- Pagination Starts-->

                    <%if(request.getParameter("search_button")!=null){
                                Connection con = new DBConnection().getConnection();

                                ResultSet rsPagination = null;
                                ResultSet rsRowCnt = null;

                                PreparedStatement psPagination = null;
                                PreparedStatement psRowCnt = null;

                               try
                                {
                                    String sqlPagination = "SELECT StudentPersonal.*,StudentExamFeeStatus.isConfirmed "
                                            + " FROM StudentPersonal LEFT JOIN StudentExamFeeStatus on "
                                            + "StudentPersonal.StudentId = StudentExamFeeStatus.StudentId "
                                            + " and StudentExamFeeStatus.examId=(SELECT max(ExamId) FROM ExamMaster E) where StudentPersonal.CollegeId="+CenterId+"   and StudentPersonal.isMGUApproved=1  " + StuList.getSearchQuery()+" order by PRN";//limiting no of records
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
                                    message = " No Matching Records Found";
                                }


                    %>

                        <% if (message != null) {%>
                        <center>
                        <table><tr>
                            <td></td>
                            <td colspan="">  <span  class="Error" style="color:red">*<%=message%>  </span></td>
                            </tr><br/></table> </center>
                        <%}else{%>
                        <center>
                            <br>
                        <table BORDER="1" width="75%" cellspacing="1" cellpadding="5%" class="textblack">
                            <thead><font size="4" color="#2595CD"><%=Qualdetails.getBranchName(rsPagination.getInt(4))%></font></thead>
                             <TR bgcolor="#dedede" align="center">

                                 <TH style="font-size: small;">SLNo</TH>
                                 <TH style="font-size: small;">PRN</TH>
                                 <TH style="font-size: small;">Student Name</TH>
                                 <% if(BranchId!=null && yearApplied.equals("2") && ( BranchId.equals("21")|| BranchId.equals("17"))){%><TH style="font-size: small;">Option Selection</TH><%}
                                else if(StuList.getBranchOptionals().isEmpty()||StuList.getBranchOptionals().get(0).isEmpty()){%><TH style="font-size: small;">Select:<a href="javascript:selectAll(document.Examregistration.studentId)"> All, </a>
                                <a href="javascript:UnCheckAll(document.Examregistration.studentId)"> None </a>
                                 </TH>
                               <% }else{%><TH style="font-size: small;">Option Selection</TH><%}%>

                            </TR>
                            <%// if(StuList.getYearApplied()!=null && StuList.getYearApplied().equals("2") && )

                                        int j = 0;

                                        if (!notFound) {
                                            do {

                                                j++;

                            %>
                            <tr>
                                <%
                                int sid=rsPagination.getInt("StudentId");
                                boolean isSlected=false;
                                int branch=0;
                                if(BranchId!=null)
                                {
                                    branch=Integer.parseInt(BranchId);
                                    isSlected=StuList.getStudentList().contains(new Integer(sid));
                                }
                                ArrayList<SubjectBranch> branchs=sList.getSupplyOrImprovementPapers(sid,branch,joinYear,attendingYear);
                                boolean isSupply= false;
                                         String StuStatus=sList.getStudentWithheldDetails(sid);
                                if(branchs!=null&&!branchs.isEmpty())
                                        isSupply=true;
                               String  str=rsPagination.getString("StudentExamFeeStatus.isConfirmed");
                               boolean isConfirmed=false;
                               if(rsPagination.getString("StudentExamFeeStatus.isConfirmed")!=null&&rsPagination.getString("StudentExamFeeStatus.isConfirmed").equals("1"))
                                   {isConfirmed=true;                                  }else{isConfirmed=false;}
                                %>
                                <td>&nbsp;<%=j%></td>

                                <td><% if(rsPagination.getString("PRN")!=null){ out.print(rsPagination.getString("PRN"));}else{out.println("Not generated");}%></td>

                                <td style="text-transform: uppercase;"><%=rsPagination.getString(2)%></td>

                                <td><%
                                if(StuStatus!=null)
                                    {%>
                                     <span style="color: maroon" ><%=StuStatus%></span>
                                <%}else  if(isConfirmed){    %>
                                <span style="color: orange" > Exam Registration Completed</span>
                              <%  }
                               else if(isSlected){%>
                                    <input type="checkbox" name ="studentId" value="<%=rsPagination.getString("StudentId")%>" <%if(rsPagination.getString("isConfirmed")!=null && rsPagination.getString("isConfirmed").equals("0")){%>checked="checked"<%}%>> &nbsp;&nbsp;
                                    <a style="color: blue;"href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Edit Supply/Elective</a>

                                <%}else{
                                if(BranchId!=null && (yearApplied.equals("2") && ( BranchId.equals("21")|| BranchId.equals("17"))) || (yearApplied.equals("1") &&  BranchId.equals("26"))) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Opt Supply/Elective</a>  <%}
                                else if(BranchId!=null && yearApplied.equals("3") &&  BranchId.equals("18")) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Opt Supply/Elective</a> 
                                <%}
                                else if(BranchId!=null && yearApplied.equals("3") &&  BranchId.equals("20")) {%> <a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Opt Supply/Elective</a> 
                                <%}
                                
                                
                                else if((StuList.getBranchOptionals().isEmpty()||StuList.getBranchOptionals().get(0).isEmpty()) && !isSupply){%><input type="checkbox" name ="studentId" value="<%=rsPagination.getString("StudentId")%>" <%if(rsPagination.getString("isConfirmed")!=null && rsPagination.getString("isConfirmed").equals("0")){%>checked="checked"<%}%>>
                                    <%}else{%><a href="ElectivesupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>" style="color: green">Opt Supply/Elective</a><%}}%>
                                </td>




                            </tr>
                            <%  } while (rsPagination.next());
                                                                                            }%>




                            <br/>
                            <tr>
                                <td colspan="4" align="center"> <span><input type="submit"  name="submit" value="Register" onclick='return confirm("Are you sure you want to register  all these students for Exam?")' />
                                                                                            </span>
                                </td>
                            </tr>

                        </table></center>
                                                                                            <%}%> </form>
                        <% if(con!=null) con.close();}%>
        </td>
      </tr>
    </tbody></table></td>
  </tr>
</tbody></table>
</td></tr>
</tbody></table>
</body></html>

<jsp:include page="footer.jsp"/>
<%}}%>