<%-- 
    Document   : ViewAllSubjectsOfCourse
    Created on : Oct 24, 2011, 12:55:47 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<%
MarkVerification mark=new MarkVerification(request, response);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

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
                            <select name="Subject" id="Subject" style="width: 270px" >
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubList=mark.getSubjectsForCourse();
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
                                           <option <%if(mark.getSubject().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>
