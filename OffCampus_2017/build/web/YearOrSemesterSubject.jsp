<%-- 
    Document   : newjsp
    Created on : Oct 14, 2011, 2:44:51 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*,java.lang.*;" %>
<script type="text/javascript" src="./js/Absentee.js"></script>
<%
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
Statistics stat=new Statistics(request, response);
String BranchId=null;
String Sem=null;
if(request.getParameter("Course")!=null)
    {
    BranchId=request.getParameter("Course");
    } if(request.getParameter("YearSem")!= null)
        {
        Sem=request.getParameter("YearSem");
        }

%><!-- Modified by Yadu -->
<div id="YearOrSem">
                                <select  name="YearSemSearch" id="YearSemSearch" style="width: 115px" onchange="ChangeSubjectListSearch();">
                                    <option value="-1">.....Select.....</option>
                                    <%
                                   int count=stat.getMaxYearOrSemForCourse();
                                   int i=1;
                                    while(i<=count)
                                        {%>
                                        <option <%if(Integer.parseInt(stat.getSemYear())==i){ out.print("selected");}  %> value=<%=i %>><%=i %></option>
                                                <% i++; } %>
                                </select>
</div>
