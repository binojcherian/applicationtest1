<%-- 
    Document   : ViewAssistantsofSO
    Created on : Oct 3, 2011, 10:02:56 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.User,model.*" %>

    <%
    String SO=request.getParameter("SO");
    model.UserCreation User=new UserCreation();
AssistantToSO Map=new AssistantToSO(request, response);
                    ArrayList<User> UserMap=new ArrayList<User>();
                    UserMap=Map.getAssignedAssistants(SO);
                   int Count= UserMap.size();int i=0;
if(Count==0)
                        {
                   %>

                   <center>
                       <table>
                           <tr class="textblack"  align="center"><span style="color:#a02a28; font-size: small"><b>No Assistants Assigned</b></span></tr>
                       </table>
                   </center>

                   <% }else{%>
                    <table border="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">
<tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>Sl No</th>
                         <th>Assistant</th>
                         <th>UserName</th>
                         <th>Delete </th>
                    </tr>
                    <% while(i<Count)
                        {
                        User us=UserMap.get(i);
                    %>
                    


                    <tr>
                        <td class="textblack"><%=(i+1)%></td>
                        <td class="textblack"><%=us.Name %></td>
                        <td class="textblack"><%=us.UserName %></td>
                        <td class="textblack"><a href="DeleteAssignedAssistant.jsp?Id=<%=us.AssistantSOId %>">Delete</a> </td>
                    </tr>
<% i++;}}%>
                </table>
