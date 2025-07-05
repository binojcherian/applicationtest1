<%-- 
    Document   : YearOrSemForAbsentee
    Created on : Nov 4, 2011, 1:05:24 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*,java.lang.*;" %>
<script type="text/javascript" src="./js/Absentee.js"></script>
<%
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
String BranchId=null;
if(request.getParameter("Course")!=null)
    {
    BranchId=request.getParameter("Course");
    }
%>
<div id="YearOrSem">
                                <select  name="YearSem" id="YearSem" style="width: 115px" onchange="ViewSubjectList();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                   int count=absentee.getMaxYearOrSemForCourse();
                                   int i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(Integer.parseInt(absentee.getSemYear())==i){ out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++; } %>
                                </select>
</div>
