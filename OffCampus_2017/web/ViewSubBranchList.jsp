<%-- 
    Document   : ViewSubjectList
    Created on : Oct 15, 2011, 11:42:07 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>
<script type="text/javascript" src="./js/MarkEntry.js"></script>
<%
AbsenteesCountBckUp absentee=new AbsenteesCountBckUp(request, response);
%>


<div id="SubjectList">
    <%
    String BranchId,SubBranchId,AcdemicYear;int Sem;
    if(request.getParameter("Course")!= null)
        {
        BranchId=request.getParameter("Course");
        }
    if(request.getParameter("SubBranch")!= null)
        {
        SubBranchId=request.getParameter("SubBranch");
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
    <select name="Subject" id="Subject" style="width: 270px" onchange="">
                                 <option value="-1">.....Select.....</option>
                                            <% ArrayList<Subject> SubBranchList=absentee.getSubjectsForSubBranch();
                                           int i=0;
                                           if(SubBranchList!=null)
                                               {
                                           int count=SubBranchList.size();
                                            if(count!=0)
                                                {
                                                    while(i<count)
                                                        {
                                                        Subject sub=SubBranchList.get(i);
                                            %>
                                           <option <%if(absentee.getSubject().equals(sub.SubjectBranchId)) { out.print("selected");}%>
                                                value="<%=sub.SubjectBranchId %>"><%=sub.SubjectName %></option>

                                            <%
                                            i++; }}}
                                            %>
                                 %>
                            </select>
                                 </div>