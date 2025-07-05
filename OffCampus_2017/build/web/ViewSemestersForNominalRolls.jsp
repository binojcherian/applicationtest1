<%-- 
    Document   : ViewSemestersForNominalRolls
    Created on : Apr 20, 2012, 2:24:30 PM
    Author     : user
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*,java.lang.*;" %>
<script type="text/javascript" src="./js/ExamRegistration.js"></script>
<%
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
Statistics stat=new Statistics(request, response);
String BranchId=null;
if(request.getParameter("Course")!=null)
    {
    BranchId=request.getParameter("Course");
    }
%>
<div id="YearOrSem">
                                <select  name="year_applied" id="year_applied" style="width: 115px" onchange="ViewSubBranches();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                   int count=stat.getMaxYearOrSemForCourse();
                                   int i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(stat.getSemYear()==i){ out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++; } %>
                                </select>
</div>
