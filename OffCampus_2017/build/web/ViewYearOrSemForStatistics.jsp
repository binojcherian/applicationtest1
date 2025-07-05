<%-- 
    Document   : ViewYearOrSemForStatistics
    Created on : Dec 6, 2011, 3:55:18 PM
    Author     : user
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*,java.lang.*" %>
<script type="text/javascript" src="./js/Absentee.js"></script>
<%
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
String BranchId=null;
String Sem=null;
if(request.getParameter("Course")!=null)
    {
    BranchId=request.getParameter("Course");
    }
 if(request.getParameter("YearSem")!= null)
        {
        Sem=request.getParameter("YearSem");
        }
%>
<div id="YearOrSem">
                                <select  name="YearSem" id="YearSem" style="width: 115px" >
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

