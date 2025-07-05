<%-- 
    Document   : ViewAllSubjectsForSearch
    Created on : Nov 9, 2011, 11:06:07 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>

                <script type="text/javascript" src="./js/StatusHide.js"></script>
<%
MarkVerification mark=new MarkVerification(request, response);
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
%>


<div id="SubjectList">
    <%
    String BranchId;int Sem;
    if(request.getParameter("Course")!= null)
        {
        BranchId=request.getParameter("Course");
        }
    if(request.getParameter("YearSem")!= null)
        {
        Sem=Integer.parseInt(request.getParameter("YearSem"));
        }
    %>
                            <select name="SubjectSearch" id="SubjectSearch" style="width: 300px" onchange="ViewSubjectWiseMarkEntryStatus();">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubList=absentee.getAllSubjectsForCourse();
                                           int i=0;
                                           if(SubList!=null)
                                               {
                                           int count=SubList.size();
                                            if(count!=0)
                                                {
                                                    while(i<count)
                                                        {
                                                        Subject sub=SubList.get(i);
                                            %>
                                           <option <%if(absentee.getSubjectSearch().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>

