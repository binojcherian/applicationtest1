<%-- 
    Document   : ViewSubBranchs
    Created on : Apr 8, 2011, 4:09:49 PM
    Author     : Sunandha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="Controller.*,model.*,java.util.*"%>
<div id="SubBranchRow">

                                   <%
                                    String CourseId=request.getParameter("BranchId");
                                    String year=request.getParameter("SemorYear");
                                    ForExamConfirmation examconfirm=new ForExamConfirmation();
                                    ArrayList<Entity.CourseData> SubBranch=examconfirm.getSubBranch(CourseId, year);
                                    if(SubBranch!=null)
                                        {
                                    int Size=SubBranch.size();
                                    int i=0;
                                    if(Size>0)
                                        {

                                    %>
                                    SubBranch
                                    <select  id="SubBranch" name="SubBranch" style="width: 300px;">
                                        <option value="-1">--------Select------- </option>
                                        <%
                                        while(i<Size)
                                            {
                                            Entity.CourseData Sub=SubBranch.get(i);
                                        %>
                                        <option value="<%=Sub.BranchId %>"><%=Sub.BranchName %></option>
                                    <%i++;}%>
                                    </select>


                                    <% }}%>
                                    </div>
                     
       
   