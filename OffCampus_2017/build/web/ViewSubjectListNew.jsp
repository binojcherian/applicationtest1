<%-- 
    Document   : ViewSubjectList
    Created on : Oct 15, 2011, 11:42:07 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<script type="text/javascript" src="./js/MarkEntry.js"></script>
<%
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
%>


<div id="SubjectList">
    <%
    String BranchId,AcdemicYear;int Sem;
    if(request.getParameter("Course")!= null)
        {
        BranchId=request.getParameter("Course");
        }
    if(request.getParameter("YearSem")!= null)
        {
        Sem=Integer.parseInt(request.getParameter("YearSem"));
        }
     if(request.getParameter("AcdemicYear")!= null)
        {
        AcdemicYear=request.getParameter("AcdemicYear");
        }
      %>
    <select name="SubjectSearch" id="SubjectSearch" style="width: 270px" onchange="ViewSubjectWiseMarkEntryStatus();">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubList=absentee.getSubjectsForCourseAcdemicYear();
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
                                           <option <%if(Integer.parseInt(absentee.getSubject())==Integer.parseInt(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>