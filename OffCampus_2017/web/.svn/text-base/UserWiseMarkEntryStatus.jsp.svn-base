<%-- 
    Document   : UserWiseMarkEntryStatus
    Created on : Nov 17, 2011, 3:47:56 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%
UserManagement User=new UserManagement(request, response);
%>
<table border="1" style="border-style: groove" cellspacing="0" cellpadding="3" >
                    <tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>UserName</th>
                         <th>Name</th>
                         <th>Designation</th>
                    </tr>
                    <%
                    ArrayList<User> UserList=new ArrayList<User>();
                    UserList=User.getAllUsers();
                    int Count= UserList.size(),i=0;
                    while(i<Count)
                        {
                        User us=UserList.get(i);
                        String User_Assign=null;

                    %>
                    <tr >
                        <td class="textblack"><%=us.UserName %></td>
                        <td class="textblack"><%=us.Name %></td>
                        <td class="textblack"><%=us.Category %></td>
                    </tr>
                    <% i++;} %>
                    <tr></tr>
                </table>
