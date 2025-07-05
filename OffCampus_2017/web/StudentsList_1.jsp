<%--
    Document   : StudentList
    Created on : Oct 17, 2010, 8:06:28 AM
    Author     : HP
--%>
<%@page import="Entity.CentreDetails"%>
<%@page import="model.CourseData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="model.DBConnection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="model.*"%>
<%if(request.getSession().getAttribute("UserName")==null)
{
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}
else
{%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="Controller.*"%>


<link rel="stylesheet" href="Style/UserCSS.css" type="text/css" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>School Distance Education</title>
    </head>
    <body>
        <table width="100%" style="height: 100%">
            <tr>
                <td colspan="2"><jsp:include page="StudentManagementHeader.jsp"/></td>
            </tr>
            <tr>

            </tr>
            <tr style="height: 100%">
                <td width="20%" height="100%" valign="top" bgcolor="#b87b1a" style="padding: 0">
                    <jsp:include page="StudentManagementNavigation.jsp"/>
                </td>
                <td bgcolor="#f8e4c7" height="100%" >
                    <%-- Content --%>

       <%
            CurrentAdmissionYear Year=new CurrentAdmissionYear();
            String message = null;
            boolean PostBack = false;
           ApprovedList Data=new ApprovedList(request, response);
         //out.print(Data.getCourse());
%>

<form name="StudentList"  id="StudentList" action="StudentList.jsp" method="post" style="min-height: 560px">
             <input type="hidden" name="isPostBack" value="Yes"/>
              <center>

                  <table  border="1" cellpadding="5%"  style="height: 100%; width:95%">
                      <tr style=" font-weight: bolder">
                          <center>

                              Select Admission Year

                        <select name="AdmissionYear" id="AdmissionYear" style="width: 215px;" title="Select Admission Year">

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
                      <tr>

                          <td align="right" >
                              <fieldset style="color:black"><legend >Search<span style="color:red">[Approved Students]</span></legend>



                                  <table id="SearchTable">
                                      <tr>
                                          <td>
                                  CourseName
                                          </td>
                                          <td>

                   <select name="CourseId" id="CourseId"   style="width: 215px;" title="Select your course" >
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
                                          <td>
                                              Select Centre
                                          </td>
                                          <td>
                                            <% ArrayList<CentreDetails> CentreList=Data.getCentres();
                                              if(CentreList==null||CentreList.size()==0)
                                                  {%>
                                                  <span class="Error">No Centres Assigned to you </span>
                                              <%}else{
                                                 i=0;
                                                count=CentreList.size();
                                                CentreDetails Centre;
                                             %>
               <select name="CentreId" id="CentreId">
    <option value="-1" <%if(Data.getCentreId()==-1){out.print("selected");}%>>--ALL--</option>
    <%while(i<count){
        Centre =CentreList.get(i);%>
        <option value="<%=Centre.CentreId %>"<%if(Data.getCentreId()==Centre.CentreId){out.print("selected");}%> > <%=Centre.CentreName%></option>

         <%        i++;}//while
                                              }%>

</select>


                                              <p>

                                          </td>
                                      </tr>
                                      <tr>
                                          <td> Application No or  Student Name</td>
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
                        </table> </center>



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
                                    psPagination.setString(1, session.getAttribute("User").toString());
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
                        <table><tr><td> <table width="3"bgcolor="dedeef" border="1"><tr><td ></td><td></td></tr><tr><td></td></tr></table></td>
                <td>  Centre transfered students are shown in this colour</td></tr
                            <tr><td> <table width="3"bgcolor="ea6969" border="1"><tr><td ></td><td></td></tr><tr><td></td></tr></table></td>
                <td>  Discontined students are shown in this colour</td></tr>
        </table>
                        <center><br>

                            <div align="center"> <b><font color=#494839>Total number of Students : <%=iTotalRows%></font></b></div>
                            <table BORDER="1px"  align="center"  style="width:90%; height: 100%"  >

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
                                        <a href="StudentList.jsp?iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>"> << Previous</a>
                                        <%
                                                                                                                                                    }

                                                                                                                                                    for (i = ((cPage * iTotalSearchRecords) - (iTotalSearchRecords - 1)); i <= (cPage * iTotalSearchRecords); i++) {
                                                                                                                                                        if (i == ((iPageNo / iShowRows) + 1)) {
                                        %>
                                        <a href="StudentList.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: red"><b><%=i%></b></a>
                                        <%
                                                                                                                                                                                                                                                    } else if (i <= iTotalPages) {
                                        %>
                                        <a href="StudentList.jsp?iPageNo=<%=i%>" style="cursor:pointer;color: blue"><%=i%></a>
                                        <%
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    if (iTotalPages > iTotalSearchRecords && i < iTotalPages) {
                                        %>
                                        <a href="StudentList.jsp?iPageNo=<%=i%>&cPageNo=<%=i%>"> >> Next</a>
                                        <%
                                                        }
                                                    }
                                        %>

                                    </div>
                                </td>
                            </tr>

                        </table></center><%}%>  </form>
<% if(con!=null) con.close();%>

                    <%-- End of Content --%>
                 </td>
            </tr>
            <tr >
                <td colspan="2" align="center"><jsp:include page="Footer.jsp" />
                </td>
            </tr>
        </table>
    </body>
</html>
<%}%>