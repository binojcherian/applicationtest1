<%-- 
    Document   : SubjectListForAbsentees
    Created on : Nov 4, 2011, 1:01:39 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<%
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
                            <select name="Subject" id="Subject" style="width: 250px" onchange="getStatusOfAbsEntry();">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubList=absentee.getSubjectsForCourse();
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
                                           <option <%if(absentee.getSubject().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>