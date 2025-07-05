<%-- 
    Document   : SubjectDetails
    Created on : Nov 10, 2011, 12:31:53 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="Controller.*,java.util.ArrayList,Entity.*" %>

<div id="SubjectCount">
                                    <%
                                       int TotalCount=0,AbsCount=0,MalCount=0,AbsEntered=0,MalWithOutFee=0,MalEntered=0,MarkEntered=0,AbsWithOutFee=0,Unconfirmed=0,Pending=0,totalentered=0;
                                        String SubjectBranchId=null;
                                        if(request.getParameter("Subject")!=null && (!request.getParameter("Subject").equals("-1")))
                                        {
                                            MarkEntryPRNBckUp mark=new MarkEntryPRNBckUp(request, response);
                                            AbsenteesEntryBckUp abs=new AbsenteesEntryBckUp(request, response);
                                            model.Absentees absent=new model.Absentees();
                                            TotalCount=abs.getTotalCountOfStudentsForSubject();
                                            AbsCount=abs.getTotalCountOfAbsenteeForSubject();
                                            MalCount=abs.getTotalCountOfMalPracticeForSubject();
                                            AbsEntered=abs.getTotalCountOfAbsenteesEntered();
                                            AbsWithOutFee=mark.getTotalAbsenteesEnteredForSubjectWithoutFee();
                                            MalEntered=abs.getTotalCountOfMalPracticeEntered();
                                            MarkEntered=abs.getTotalCountOfMarkEnteredForSubject();
                                            MalWithOutFee=mark.getTotalMalpracteesEnteredForSubjectWithoutFee();
                                            //SubjectBranchId=absent.getStudentSubjectBranchId(request.getParameter("Exam"), absent.getStudentIdFromPRN(request.getParameter("PRN")), request.getParameter("Subject"), Integer.parseInt(request.getParameter("YearSem")));
                                            //Unconfirmed=absent.getTotalCountOfUnconfirmedStudents(SubjectBranchId);
                                            //Pending=absent.getTotalCountOfUnconfirmedStudentsPending(SubjectBranchId);
                                       Pending=TotalCount-(MarkEntered+AbsEntered);
                                       totalentered=MarkEntered+AbsEntered+MalEntered;
                                            String SubjectName=mark.getSubjectNameFromSubjectId();
                                        }
                                    %>
                                 <table width="100%" >
                                 <tr>
                                     <td colspan="3" class="textblack" bgcolor="#d4c7a4" align="center" height="25px" width="100%"><b>Count</b></td>
                                        </tr>
                                        
                                        <tr>
                                        <table border="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">
                                        <tr>
                                            <th class="textblack">Item</th>
                                            <th class="textblack">Total</th>
                                            <th class="textblack">Entered</th>
                                        </tr>
                                        <tr>
                                            <td class="textblack"> Regisration:</td>
                                            <td class="textblack"><%if(request.getParameter("Subject")!=null){ %><a href="SubjectWisePRN.jsp?SubjectBranchId=<%=request.getParameter("Subject")%>&Course=<%=request.getParameter("Course")%>"> <%=TotalCount %></a><% }else{%><%=TotalCount %><% } %></td>
                                            <td class="textblack"><%=MarkEntered %></td>

                                        </tr>
                                        <%--tr>
                                            <td class="textblack">Marks To be Entered:</td>
                                            <td class="textblack"><%=(TotalCount-AbsCount) %></td>
                                        </tr--%>
                                        <tr>
                                             <td class="textblack">Absentees (Registered)</td>
                                             <td class="textblack"><%=AbsCount %></td>
                                             <td class="textblack"><%=AbsEntered %></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">Absentees (Not Registered)</td>
                                             <td class="textblack"><%--AbsCount --%></td>
                                             <td class="textblack"><%=AbsWithOutFee %></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">MalPractice (Registered) </td>
                                             <td class="textblack"><%=MalCount %></td>
                                             <td class="textblack"><%=MalEntered%></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">MalPractice (Not Registered) </td>
                                             <td class="textblack"><%--MalCount --%></td>
                                             <td class="textblack"><%=MalWithOutFee %></td>
                                        </tr>
                                        
                                        <!--
                                        <tr>
                                             <td class="textblack">Withheld (Aproved)</td>
                                             <td class="textblack">_</td>
                                             <td class="textblack"><%=Unconfirmed %></td>
                                        </tr>
                                        <tr>
                                             <td class="textblack">Withheld (Pending for Approval)</td>
                                             <td class="textblack">_</td>
                                             <td class="textblack"><%=Pending %></td>
                                        </tr>
                                        -->
                                         <tr>
                                             <td class="textblack">Mark Entry Pending</td>
                                             
                                             <td class="textblack" style="color:#a02a28;font-weight:bold;text-align: center;"><%=Pending %></td>
                                             <td class="textblack" style="text-align: center;"><%=totalentered %></td>
                                        </tr>
                                        </table>
                                        </tr>
                                    </table>
                                    </div>
