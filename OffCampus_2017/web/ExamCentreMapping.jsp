<%--
    Document   : ExamCentreMapping
    Created on : Apr 3, 2012, 2:42:35 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%if(request.getSession().getAttribute("UserName")==null){
 response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}else{
            ExamVenueMapping StuList=new ExamVenueMapping(request,response);

            String message = null;
            //String CenterId = "54";

            //QualificationDetails Qualdetails = new QualificationDetails(request, response);
            CourseSelection  cs=new CourseSelection(request, response);
            String BranchId=null;
            if(request.getSession().getAttribute("BranchId")!=null)
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
        <form name="Examregistration"  id="ExamCentreMapping" action="ExamCentreMapping.jsp" method="post">
             <input type="hidden" name="isPostBack" value="Yes"/>
             <b> <font color="#a02a28" size="2px"><u>Exam Venue Mapping</u></font></b>
                 <center>
                        <table  border="0"cellpadding="5%" width="90%">

                            <tr class="textblack">
                                <td align="left" >

                                    <fieldset style="font-size: 14px;color:black;border-color: #6c4303" ><legend style="font-size: 14px;font-weight:bold " >Search </legend>
                                        <table width="100%">
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

</select></td><td class="textblack">Course</td><td>
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
                                        </select></td></tr>
                                            <tr><td class="textblack">

                                        Year/Semester</td><td>

                                        <select name="year_applied" id="year_applied"   style="width: 215px;" title="Select your year" >

                                            <option value="-1" >--------Select------- </option>
                                            <option value="1" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("1")) {%> selected="selected"<%}%> > First year</option>
                                            <option value="2" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("2")) {%> selected="selected"<%}%>> Second year</option>
                                            <option value="3" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("3")) {%> selected="selected"<%}%>> Third year</option>

                                        </select></td>
                                        <td class="textblack">
                                             Admission Year</td><td>

                                        <select name="join_year" id="join_year"   style="width: 100px;" title="Admission Year" >

                                           <option value="2010" <%if (joinYear==2010) {%> selected="selected"<%}%> >2010</option>
                                           <option value="2011" <%if (joinYear==2011) {%> selected="selected"<%}%>  >2011</option>
                                            <option value="2012" <%if (joinYear==2012) {%> selected="selected"<%}%> >2012</option>
                                            <option value="2013" <%if (joinYear==2013) {%> selected="selected"<%}%> >2013</option>
                                            <option value="2014" <%if (joinYear==2014) {%> selected="selected"<%}%> >2014</option>
                                           </select>
                                        </td></tr><tr><td colspan="4" align="center">
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

                        </table> </center>


                    <!-- Pagination Starts-->


                    <%if(StuList.getSearch()){
                                //Connection con = new DBConnection().getConnection();
                                int TotalRows=0,TotalAmt=0;
                                ResultSet rsPagination = null;
                                ResultSet rsRowCnt = null;

                                PreparedStatement psPagination = null;
                                PreparedStatement psRowCnt = null;

                               try
                                {
                                    String sqlPagination = "SELECT SQL_CALC_FOUND_ROWS StudentPersonal.*,StudentExamFeeStatus.isConfirmed,StudentExamFeeStatus.IsMguApproved as Approval ,ifnull (e2.ExamCentreName,'') as ExamVenue FROM StudentPersonal LEFT JOIN StudentExamFeeStatus on StudentPersonal.StudentId = StudentExamFeeStatus.StudentId  left join ExamCentre2011  e2 on e2.ExamCentreId= StudentExamFeeStatus.ExamVenue "
                                            + " and  StudentExamFeeStatus.examId=(SELECT max(ExamId) FROM ExamMaster E) where StudentExamFeeStatus.ExamCentre="+StuList.getCenterId()+"  and  StudentExamFeeStatus.isMGUApproved=1  and StudentExamFeeStatus.ExamId= (SELECT max(ExamId) FROM ExamMaster E) " + StuList.getSearchQueryforCentreMapping()+" order by PRN";//limiting no of records

                                    psPagination = con.prepareStatement(sqlPagination);
                                    //psPagination.setInt(1, Integer.parseInt(request.getParameter("AdmissionYear")));
                                    //System.out.println("asads"+sqlPagination);
                                    rsPagination = psPagination.executeQuery();
                                }
                                catch (Exception ex)
                                {
                                    out.print(ex);
                                }
                                Boolean notFound = false;
                                if (!rsPagination.next())
                                {
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
                        <%}else{%>
                        <center>


                            <br>
                            <table width="90%">
                                <tr align="center">
                                    <td colspan="2" class="textblack"><b>Registered Students : <%=sList.RegisteredStudentsCount(Integer.parseInt(StuList.getCourseApplied()), Integer.parseInt(StuList.getCenterId()), Integer.parseInt(StuList.getYearApplied()), con) %>/<%=TotalRows%></b></td>
                                <td colspan="2" class="textblack"><b>Confirmed by Centre : <%=sList.ConfirmedStudentsCount(Integer.parseInt(StuList.getCourseApplied()), Integer.parseInt(StuList.getCenterId()), Integer.parseInt(StuList.getYearApplied()), con) %>/<%=TotalRows%></b></td>
                                <td colspan="2" class="textblack"><b>Mgu Approved Students : <%=sList.MguApprovedStudentsCount(Integer.parseInt(StuList.getCourseApplied()), Integer.parseInt(StuList.getCenterId()), Integer.parseInt(StuList.getYearApplied()), con) %>/<%=TotalRows%></b></td>
                            </tr>
                            </table>
                            <br>

                        <table BORDER="1" width="100%" cellspacing="0" cellpadding="3" class="textblack" style="border-style: groove">
                            <thead><font size="4" color="#2595CD"></font></thead>
                             <TR bgcolor="#dedede" align="center">
                                 <TH style="font-size: small;">Select</TH>
                                 <TH style="font-size: small;">SLNo</TH>
                                 <TH style="font-size: small;">PRN</TH>
                                 <TH style="font-size: small;">Student Name</TH>

                                 <TH style="font-size: small;">Status</TH>
                                 

                            <TH style="font-size: small;">Exam Venue</TH>
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
                                boolean isConfirmed=false,IsCheck=true;
                                int branch=0;
                                if(BranchId!=null){
                                    branch=Integer.parseInt(BranchId);
                                    isSlected=StuList.getStudentList().contains(new Integer(sid));

                                }
                                ArrayList<SubjectBranch> branchs=sList.getSupplyOrImprovementPapers(sid,branch,joinYear,attendingYear);
                                boolean isSupply= false;
                                String StuStatus=sList.getStudentWithheldDetails(sid);

                                if(branchs!=null&&!branchs.isEmpty())
                                        isSupply=true;
                               String  str=rsPagination.getString("StudentExamFeeStatus.isConfirmed");
                               String color="",Status="";

                               if(rsPagination.getString("StudentExamFeeStatus.isConfirmed")!=null&&rsPagination.getString("StudentExamFeeStatus.isConfirmed").equals("1"))
                                   {isConfirmed=true;                                  }else{isConfirmed=false;}
                                %>
                                <%
                                if(rsPagination.getString("StudentExamFeeStatus.isConfirmed")==null)
                                    {
                                    Status="Not Registered";
                                    color="#d84937";
                                    
                                    }
                              else if(isSlected || rsPagination.getString("StudentExamFeeStatus.isConfirmed").equals("0"))
                                    {
                                    Status="Centre Not Confirmed";
                                    color="blue";
                                     
                                    }
                              else if(!isSlected)
                                   {
                                     Status="Not Registered";
                                    color="#d84937";

                                  
                                   }
                                if(isConfirmed)
                                    {
                                    color="orange";
                                    Status="Centre Confirmed";
                                     
                                    }
                               if(rsPagination.getInt("Approval")==1)
                                   {
                                   color="#19610f";
                                   Status="Mgu Approved";
                                   
                                   }
                                if(StuStatus!=null)
                                    {
                                    IsCheck=true;
                                    }

                                %>
                                 <td class="textblack" align="left"><%if(IsCheck){ %><input type="checkbox" name="Select"id="Select" Value="<%=rsPagination.getString("StudentId") %>"><% }else{%>&nbsp;<%}%> </td>
                                <td>&nbsp;<%=j%></td>

                                <td><% if(rsPagination.getString("PRN")!=null){ out.print(rsPagination.getString("PRN"));}else{out.println("Not generated");}%></td>

                                <td ><%=rsPagination.getString(2)%></td>

                                <td><font style="color: <%=color %>"><%=Status %></font><%if(Status.isEmpty()){ %>&nbsp;<% }%></td>
                                <td ><font style="color: teal"><%=rsPagination.getString("ExamVenue")%></font></td>
                         </tr>
                            <%  } while (rsPagination.next());
                                                                                            }%>


                            <tr>
                       <td colspan="7"class="textblack" align="left"><input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="SelectAllRows(this.form);"><b>SelectAll</b> </td>
                    </tr>
                    <tr bgcolor="#016b82"><td colspan="3" align="left" > <FONT color="WHITE">Assign Exam Venue</FONT></td><td colspan="2" align="left" >

                                         <% ArrayList<ExamVenueDetails> VenueList=sList.getExamVenue();
                                              if(VenueList==null||VenueList.size()==0)
                                                  {%>
                                                  <span class="Error">No Centres Assigned to you </span>
                                              <%}else{
                                                int i=0;
                                               int count=VenueList.size();
                                                ExamVenueDetails Venue;
                                             %>
              <select name="ExamCentreId" id="ExamCentreId"  style="width: 450px;" title="Select your Exam Venue">
    <option value="-1" >--------Select-------</option>
    <%while(i<count){
        Venue =VenueList.get(i);%>
        <option value="<%=Venue.ExamCentreId %>" <%if (StuList.getCenterId()!=null && Integer.parseInt(StuList.getCenterId())==(Venue.ExamCentreId)) {
                                                                                                               out.print("selected");
                                                                                                           }
                                                                                                       %>> <%=Venue.ExamCentreName%></option>

         <%        i++;}//while
                                              }%>

</select></td>
                                <td colspan="1" align="right"> <span>  <input type="submit"  name="submit" value="Assign" onclick='return confirm("Are you sure you want to Assign  all these students for Exam Venue?")' />
                                                                                           </span>
                                </td>
                            </tr>

                        </table></center><%}%>
                                                                                            <%}%> </form>
                        <% if(con!=null){ con.close();}%>
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