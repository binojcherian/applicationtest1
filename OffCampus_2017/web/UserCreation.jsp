<%-- 
    Document   : UserCreation
    Created on : Sep 27, 2011, 1:00:40 PM
    Author     : mgu
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.User" %>
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
UserManagement User=new UserManagement(request, response);
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
            <form id="UserCreation" name="UserCreation" action="UserCreation.jsp" method="post">
               
                  <center>
                    <table  border="1" width="70%" >
                    <tr  align="center">
                        <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">User Creation</font></b></td>
                    </tr>
                    <tr> <td align="center">
                    <fieldset ><legend><span style="color:#a02a28; font-size: small"><b>Please Enter</b></span></legend>
                <table>
                    <tr>
                        <td class="textblack">User Name</td>
                        <td>
                            <input type="text" id="UserName" name="UserName" value="<%=User.getUserName() %>" maxlength="30" style="width:215 px">
                        </td>
                        <%if(User.UserError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=User.UserError()%></span> </td>
                        <% } %>
                    </tr>
                    <tr>
                        <td class="textblack">Password</td>
                        <td>
                            <input type="Password" id="Password" name="Password" value="<%=User.getPassword() %>"  maxlength="30" style="width:215 px">
                        </td>
                        <%if(User.PasswordError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=User.PasswordError()%></span> </td>
                        <% } %>
                    </tr>
                    <tr>
                        <td class="textblack">Confirm Password</td>
                        <td>
                            <input type="Password" id="CPassword" name="CPassword" maxlength="30" style="width:215 px">
                        </td>
                    </tr>
                    <tr>
                        <td class="textblack">Name</td>
                        <td>
                            <input type="text" id="Name" name="Name" maxlength="30" value="<%=User.getName() %>" style="width:215 px">
                        </td>
                         <%if(User.NameError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=User.NameError()%></span> </td>
                        <% } %>
                    </tr>
                    <tr>
                        <td class="textblack">Category</td>
                        <td>
                            <select id="Category" name="Category" style="width:215 px">
                                <option value="-1" <%if(User.getCategory().equals("-1")){out.print("Selected");}%> >.....Select.....</option>
                                <option value="1" <%if(User.getCategory().equals("1")){out.print("Selected");}%>>Section Officer</option>
                                <option value="2" <%if(User.getCategory().equals("2")){out.print("Selected");}%>>Assistant</option>
                            </select>
                        </td>
                         <%if(User.CategoryError()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><%=User.CategoryError() %></span> </td>
                        <% } %>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="submit" name="submit" id="submit" value="Submit">
                            <input type="submit" name="submit" id="submit" value="Reset" onclick="return ResetAllFields();">
                        </td>
                        <%if(User.getIsSaved()!=null) {%>
                        <td><span style="color:#a02a28; font-size: small"><b><%=User.getIsSaved() %></b></span> </td>
                        <% } %>
                    </tr>
                </table>
                        </fieldset>
                            <br>
                                 <fieldset ><legend><span style="color:#a02a28; font-size: small"><b> User List</b></span></legend>
                                     <table align="left"><tr><td> <table width="3"bgcolor="#dedede" ><tr><td ></td><td></td></tr><tr><td></td></tr></table></td>
                <td class="textblack">  Un assigned Assistants are shown in this colour</td></tr>
        </table>
                                     <table border="1"  width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">
                    <tr class="textblack" bgcolor="#ddcccc" align="center">
                         <th>UserName</th>
                         <th>Name</th>
                         <th>Designation</th>
                         <th>Delete </th>
                    </tr>
                    <%
                    ArrayList<User> UserList=new ArrayList<User>();
                    UserList=User.getAllUsers();
                    int Count= UserList.size(),i=0;
                    while(i<Count)
                        {
                        User us=UserList.get(i);
                        String User_Assign=null;
                        if(us.Privilege.equals("32") )
                            {
                                User_Assign=us.UserName;
                            }
                        if(us.Privilege.equals("33") )
                            {
                            User_Assign=User.getSOofAssistant(us.UserName);
                            }
                    %>
                    <tr <%if(us.Privilege.equals("33")){if(User.IsAssignedUser(us.UserName)){ %> bgcolor="#dedede"<%}} %>>
                        <td class="textblack"><%=us.UserName %></td>
                        <td class="textblack"><a href="AssistantToSOMapping.jsp?SO=<%=User_Assign%>"><%=us.Name %></a></td>
                        <td class="textblack"><%=us.Category %></td>
                        <td class="textblack"><a href="DeleteUser.jsp?Username=<%=us.UserName%>&Privilege=<%=us.Privilege %>">Disable</a> </td>
                    </tr>
                    <% i++;} %>
                    <tr></tr>
                </table>
                        </fieldset>
                        </td></tr>
                </table>
                     </center>
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
