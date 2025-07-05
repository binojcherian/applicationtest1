<%-- 
    Document   : AssistantToSOMapping
    Created on : Sep 27, 2011, 5:50:28 PM
    Author     : mgu
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.User,model.*" %>
<%
if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
}else{
    LoginController login=new LoginController(request, response);
int Role=login.getPrivilege(request.getSession().getAttribute("UserName").toString());
if(Role!=31)
    {
        response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=YOU ARE NOT PERMITTED TO ACCESS THE PAGE"));
    }
model.UserCreation User=new UserCreation();
AssistantToSO Map=new AssistantToSO(request, response);
%>
<html><head>
		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/UserMapping.js"></script>
		<style type="text/css">
<!--
body {
        background-image:  url();
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;
        background-color: #FFFFFF;
}
-->
        </style>
<link rel="Stylesheet" href="Style/style.css" type="text/css">



	    <style type="text/css">
<!--
.style1 {color: #0099CC}
-->
        </style>
	    </head><body>
	<!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
    <jsp:include page="Header_Home.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">

        <jsp:include page="Navigation_ExamAR.jsp"/>

       </td>
    <td width="20" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="20"></td>
    <td width="90%">
      <table width="100%" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
      <tbody><tr>
        <td colspan="5" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="609"></td>
      </tr>
      <tr>
        <td width="16" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="18" width="8"></td>
	<td>
<div class="textblack"></div>	</td>
        <td bgcolor="#ffffff">&nbsp;
            <%-- Content --%>
            <form id="AssistantToSOMapping" name="AssistantToSOMapping" action="AssistantToSOMapping.jsp" method="post">

                <center>
                    <table  border="1" width="80%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Assistant Management</font></b></td>
                    </tr>
                    <tr> <td align="center">
                            <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Enter[ <a href="UserCreation.jsp">Back</a> ]</b></span></legend>
                <table>

                    <tr>
                        <td class="textblack">Section Officer</td>
                        <td>
                            <select name="SO" id="SO" style="width: 215px" onchange="ChangeAssistants();">
                                <%
                                ArrayList<User> UserList=new ArrayList<User>();
                                UserList=User.getSectionOfficers();
                                int Count= UserList.size(),i=0;
                                if(Count==0)
                                    {%>
                                    <option value="-1">.....Select.....</option>
                                   <% }
                                while(i<Count)
                                {
                                User us=UserList.get(i);
                                %>
                                <option <%if(Map.getSO()!=null){if(Map.getSO().equals(us.UserName)){out.print("selected");} }%>  value="<%=us.UserName %>"><%=us.Name %></option>
                                <%i++;
                                }%>
                            </select>
                        </td>
                        <td class="textblack">Assistant</td>
                        <td>
                            <select name="Assistant" id="Assistant" style="width: 215px">
                                <option value="-1">.....Select.....</option>
                                <%
                                ArrayList<User> List=new ArrayList<User>();
                     List=User.getAssistantsNotAssigned();
                     Count= List.size();i=0;
                     while(i<Count)
                        {
                        User us=List.get(i);
                                %>
                                <option  value="<%=us.UserName %>"><%=us.Name %></option>
                                <% i++; }%>
                            </select>
                        </td>

                    </tr>
                    <%if(Map.AssistantError()!=null){ %>
                    <tr>
                        <td colspan="3" align="right"><span style="color:#a02a28; font-size: small"><b><%=Map.AssistantError() %></b></span> </td>
                    </tr>
                    <% } %>
                    <tr>
                        <td colspan="4" align="right">
                            <input type="submit" id="Submit" name="Submit" value="Assign">
                        </td>
                    </tr>
                    <tr>

                        <%if(Map.getSaved()!=null) {%>
                        <td colspan="3" align="right"><span style="color:#a02a28; font-size: small"><b><%=Map.getSaved() %></b></span> </td>
                        <% } %>
                    </tr>

                </table>
                    </fieldset>
                             <BR>
                             
                             <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>User List</b></span></legend>
                       <div id="UserList">
                       <%
                       String UserSO=null;
                    ArrayList<User> UserMap=new ArrayList<User>();
                    if(request.getSession().getAttribute("SO")!=null  && (!request.getSession().getAttribute("SO").equals("null")))
                        {
                         UserSO=request.getSession().getAttribute("SO").toString();
                        }
                    else
                        {
                        if(UserList.size()>0)
                            {
                          UserSO=UserList.get(0).UserName;
                          }
                        }
                    UserMap=Map.getAssignedAssistants(UserSO);
                    Count= UserMap.size();i=0;
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
<%i++; }}%>
                </table>
                            </div></fieldset>
        </td></tr>
                </table>
                     </center>
                <br>
                <br>
            </form>

         <%-- Content --%>
        </td>
      </tr>
    </tbody></table></td>
  </tr>
  <TR> <TD><br/></TD></TR>
</tbody></table>
</td></tr>
</tbody></table>
</body></html>
<jsp:include page="footer.jsp"/><% }%>
