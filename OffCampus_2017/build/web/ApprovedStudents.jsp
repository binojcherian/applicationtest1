<%--
    Document   : Support
    Created on : Oct 29, 2011, 12:19:07 PM
    Author     : user
--%>

<%@page import="Entity.CentreDetails"%>
<%@page import="model.CourseData"%>
<%@page import="java.sql.SQLException"%>
<%@page import="model.DBConnection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<% if (request.getSession().getAttribute("UserName") == null) {
                response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
            } else {%>



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
        <td bgcolor="#ffffff" >&nbsp;
            <%-- Content --%>



       <%
            CurrentAdmissionYear Year=new CurrentAdmissionYear();
            String message = null;
            boolean PostBack = false;
           ApprovedList Data=new ApprovedList(request, response);
         //out.print(Data.getCourse());
%>

<form name="ApprovedStudents"  id="ApprovedStudents" action="ApprovedStudents.jsp" method="post"  >
             <input type="hidden" name="isPostBack" value="Yes"/>
             

                  <table  border="1" cellpadding="5%"  style="height: 100%;font-size: 13px" >
                      <tr style=" font-weight: bolder">
                          <center>

                              Select Admission Year

                        <select name="AdmissionYear" id="AdmissionYear"  title="Select Admission Year">

                                                     <%

                                                     ArrayList<Integer> yearArray=Year.getAdmissionYears();
                                                     int Count=yearArray.size();int i=0;
                                                     while(i<Count)
                                                         {

                                                          %>
                                                          <option  <% if(yearArray.get(i)==Data.getAdmissionYear()){out.print("selected");}%> value=<%=yearArray.get(i)%>><%=yearArray.get(i)%></option>
                                                                 <% i++;
                                                         }%>
                                                    </select>


                          </center>
                      </tr>
                      <tr >

                          <td align="right"  style=" height: 100% ;font-size: 13px">
                              <fieldset style="color:black" ><legend >Search<span style="color:red">[Approved Students]</span></legend>



                                  <table id="SearchTable" style=" height: 100% ;font-size: 13px">
                                      <tr>
                                          <td >
                                  CourseName
                                          </td>
                                          <td>

                   <select name="CourseId" id="CourseId"    title="Select your course" >
                       <option value="-1"  <%if(Data.getCourse()==-1){out.print("selected");}%>> All</option>
                                            <% ArrayList<CourseData> CourseList=Data.getCourses();
                                             i=0;
                                            int count=CourseList.size();

                                                    while(i<count)
                                                        {
                                                        CourseData Course=CourseList.get(i);
                                            %>
                                           <option <%if(Data.getCourse()==Course.BranchId) { out.print("selected");}  %>
                                                value="<%=Course.BranchId %>"><%=Course.BranchName %></option>

                                            <%i++; }
                                            %>

                                        </select>
                                          </td>
                                          <td></td>
                                      </tr>
                                      <tr>
                                          <td>
                                              Select Centre
                                          </td>
                                          <td >
                                            <% ArrayList<CentreDetails> CentreList=Data.getCentres();
                                              if(CentreList==null||CentreList.size()==0)
                                                  {%>
                                                  <span class="Error">No Centres Assigned to you </span>
                                              <%}else{
                                                 i=0;
                                                count=CentreList.size();
                                                CentreDetails Centre;
                                             %>
                                             <select name="CentreId" id="CentreId" style="width: 90%" >
    <option value="-1" <%if(Data.getCentreId()==-1){out.print("selected");}%>>--ALL--</option>
    <%while(i<count){
        Centre =CentreList.get(i);%>
        <option value="<%=Centre.CentreId %>"<%if(Data.getCentreId()==Centre.CentreId){out.print("selected");}%> > <%=Centre.CentreName%></option>

         <%        i++;}//while
                                              }%>

</select>


                                              <p>

                                          </td>
                                          <td></td>
                                      </tr>
                                      <tr>
                                          <td  > Application No or Student Name</td>
                                          <td>
                                      <input type="text" name="SearchText" id="Search_text" value=""/>
                                          </td>

                                          <td>

                                          <input type="submit" name="Search" id="Search_button" value="Search"/>
                                          </td>
                                  </tr>

                                  </table>
                                              <%-- Search table End--%>
</fieldset>


                                </td>

                            </tr>
                        </table> 



                    <!-- Pagination Starts-->
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
                                ResultSet rsPagination = null;
                                ResultSet rsRowCnt = null;
                                PreparedStatement psPagination = null;
                                PreparedStatement psRowCnt = null;
                                int iShowRows = 25;  // Number of records show on per page
                                int iTotalSearchRecords = 50;  // Number of pages index shown
                                int iTotalRows = nullIntconv(request.getParameter("iTotalRows"));
                                int iTotalPages = nullIntconv(request.getParameter("iTotalPages"));
                                int iPageNo = nullIntconv(Data.getiPageNo());
                                int cPageNo = nullIntconv(Data.getcPageNo());
                                int iStartResultNo = 0;
                                int iEndResultNo = 0;
                                if(request.getParameter("Search")!=null)
                                    {
                                    iPageNo=0;
                                    }
                                if(iPageNo!=0)
                            iPageNo = Math.abs((iPageNo - 1) * iShowRows);
                                try {
                                    String sqlPagination = Data.getQuery()+ " limit " + iPageNo + "," + iShowRows + "";//limiting no of records
                                    psPagination = con.prepareStatement(sqlPagination);
                                    //psPagination.setString(1, session.getAttribute("UserName").toString());
                                    //out.print(psPagination);
                                    rsPagination = psPagination.executeQuery();
                                } catch (SQLException ex) {

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
                            <td colspan="">  <span  class="Error" style="color:red">*<%=message%>  </span></td>
                            </tr><br></table> </center>
                        <%}else{%>
                        <br>
                        <table style=" font-size: 13px"><tr><td> <table width="3"bgcolor="dedeef" border="1"><tr><td ></td><td></td></tr><tr><td></td></tr></table></td>
                <td>  Centre transfered students are shown in this colour</td></tr
                            <tr><td> <table width="3"bgcolor="ea6969" border="1"><tr><td ></td><td></td></tr><tr><td></td></tr></table></td>
                <td>  Discontined students are shown in this colour</td></tr>
        </table>
                        <br>

                            <div style="  height: 100% ;font-size: 13px"> <center><b><font color=#494839>Total number of Students : <%=iTotalRows%></font></b></center></div>
                            <table BORDER="1px"   style=" height: 100% ;font-size: 13px"   >

                            <TR bgcolor="d6d2a1" align="center">
                                 <TH style="font-size: small;">SLNo</TH>
                                 <TH style="font-size: small;">Application No</TH>
                                 <TH style="font-size: small;">PRN</TH>
                                 <TH style="font-size: small;">Student Name</TH>
                                 <TH style="font-size: small;">Centre</TH>
                                 <TH style="font-size: small;">Course</TH>
                                 <TH style="font-size: small;">Student Status</TH>
                                 <TH style="font-size: small;">View/Edit</TH>

                            </TR>
                            <%

                                        int j=0;
                                        j = j + iPageNo;
                                        if (!notFound) {
                                            do {

                                                j++;

                            %>

                            <tr <%if(Data.IsCentreTransfered(rsPagination.getInt(1))){%> bgcolor="#dedeef " <% }
                                                if(Data.IsDisContinueStudent(rsPagination.getInt(1))){%> bgcolor="#ea6969 " <% }
                            %>>
                                <td>&nbsp;<%=j%></td>
                                 <td><%=rsPagination.getInt(5)%></td>
                                 <td><%=rsPagination.getString(7)%></td>
                                <td><%=rsPagination.getString(2)%></td>
                                <td><%=rsPagination.getString(3)%></td>
                                <td><%= rsPagination.getString(4)%></td>
                                <td> <%if(Data.IsDisContinueStudent(rsPagination.getInt(1))){out.println("DisContinued");}else {out.println(Data.getStatus(rsPagination.getInt(6)));}%></td>
                                <td><a href="StudentVerification.jsp?StudentId=<%=rsPagination.getInt(1)%>&PrePage=PRN" style="cursor:pointer;color: blue">  View</a> </td>

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

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                            %>
                            <br>

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
                                        <a href="ApprovedStudents.jsp?iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>"> << Previous</a>
                                        <%
                                                                                                                                                    }

                                                                                                                                                    for (i = ((cPage * iTotalSearchRecords) - (iTotalSearchRecords - 1)); i <= (cPage * iTotalSearchRecords); i++) {
                                                                                                                                                        if (i == ((iPageNo / iShowRows) + 1)) {
                                        %>
                                        <a href="ApprovedStudents.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: red"><b><%=i%></b></a>
                                        <%
                                                                                                                                                                                                                                                    } else if (i <= iTotalPages) {
                                        %>
                                        <a href="ApprovedStudents.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: blue"><%=i%></a>
                                        <%
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    if (iTotalPages > iTotalSearchRecords && i < iTotalPages) {
                                        %>
                                        <a href="ApprovedStudents.jsp?iPageNo=<%=i%>&cPageNo=<%=i%>"> >> Next</a>
                                        <%
                                                        }
                                                    }
                                        %>

                                    </div>
                                </td>
                            </tr>

                        </table><%}%>  </form>
<% if(con!=null) con.close();%>

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
