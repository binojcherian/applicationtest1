<%-- 
    Document   : SubjectWiseMarkEntryStatus
    Created on : Nov 21, 2011, 10:17:50 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%
UserManagement User=new UserManagement(request, response);
AbsenteesCount absentee=new AbsenteesCount(request,response);
String Subject=null;
if(request.getParameter("Subject")!=null)
    {
       Subject=request.getParameter("Subject");
    }
%>
 <div id="MarkEntryStatusForSubject">
                                     <%
                                      ArrayList<UserWiseCount> UserCount=User.getMarkEntryCount(Subject);
                   int  Count= UserCount.size();int i=0;
                    if(Count!=0)
                        {%>
               <table border="1" style="border-style: groove" cellspacing="0" cellpadding="3" >
                    <tr class="textblack" bgcolor="#e8dec3" align="center">
                         <th>Name</th>
                         <th>Designation</th>
                         <th>Mark Entry</th>
                         <th>Mark Verification</th>
                    </tr>
                    <%
                    int TotalMark=0,TotalVerified=0;
                    while(i<Count)
                        {
                        UserWiseCount us=UserCount.get(i);
                        String User_Assign=null;
                        int Abs=User.getTotalMarkEnteredByUser(us.UserName);
                        int Mal=User.getTotalMarkVerifiedByUser(us.UserName);
                        int MarkE=User.getTotalAbsenteesEnteredByUser(us.UserName);
                        int MarkVer=User.getTotalMalEnteredByUser(us.UserName) ;

                        TotalMark+=us.MarkEntryCount ;
                        TotalVerified+=us.MarkVerificationCount;
                    %>
                    <tr>
                       <td class="textblack"><%=us.Name %></td>
                        <td class="textblack"><%=us.Designation %></td>
                         <%if(us.Designation.equals("Section Officer")) {%>
                        <td class="textblack"><font color="#19610f">Not Applicable</font></td>
                        <% } else{%>
                        <td class="textblack"><%=us.MarkEntryCount %></td><% }%>
                        <%if(us.Designation.equals("Assistant")) {%>
                        <td class="textblack"><font color="#19610f">Not Applicable</font></td>
                        <% } else{%>
                        <td class="textblack"><%=us.MarkVerificationCount %></td><% }%>
                       
                    </tr>
                    <% i++;} %>
                    <tr>
                        <td colspan="2" align="right" class="textblack"><b> Total :</b></td>
                        <td class="textblack"><b><%=TotalMark%></b></td>
                        <td class="textblack"><b><%=TotalVerified%></b></td>
                    </tr>
                    
                </table><% }else{ %>
                <table>
                          <tr class="textblack"  align="center"><span style="color:#a02a28; font-size: small"><b>No Records to Display</b></span></tr>
                       </table><% } %></div>
