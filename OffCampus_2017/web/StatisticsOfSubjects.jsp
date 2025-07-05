<%--
    Document   : StatisticsOfSubjects
    Created on : Dec 6, 2011, 3:42:49 PM
    Author     : sunandha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<link rel="Stylesheet" href="Style/style.css" type="text/css">
<div id="MarkStatistics">
<%
Statistics stat=new Statistics(request, response);
AbsenteesEntry absentee=new AbsenteesEntry(request, response);
if(stat.IsMarkVerificationCompletedForBranch())
                                                {

    ArrayList<ResultStatistics> Result=stat.getStatisticsForCourse();
    int Count=Result.size(),i=0;
    if(Count!=0){%>
    <table BORDER="1" width="98%" style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack">
        <tr align="center">
             <td height="20px" bgcolor="#d9bf79" > <b> <font color="#a02a28">Statistics of Tabulated Marks for the Conduct of Pass Board Meeting</font></b></td>
        </tr>
        <tr>
            <td align="center">
                <center>
        <table width="100%" cellspacing="0" cellpadding="3" class="textblack">
            <tr >
                <td height="20px" class="textblack">Number of Candidates : <b> <%=stat.getTotalCountOfStudentsForCourse() %></b></td>
                <td height="20px" class="textblack"> Number of Candidates Passed :<b> <%=stat.getTotalPassForCourse() %></b></td>
            </tr>
            <tr>
                <td height="20px"  class="textblack">Number of Candidates above 80% : <b><%=stat.getCountofStudentsAboveEightyPercentage() %></b></td>
                <td height="20px"  class="textblack">Number of Candidates above 75% : <b><%=stat.getCountofStudentsAboveSeventyFivePercentage() %></b></td>
            </tr>
            <tr>
                <td height="20px" class="textblack">Number of Candidates above 60% :<b><%=stat.getCountofStudentsAboveSixtyPercentage() %></b></td>
                <td height="20px" class="textblack">Number of Candidates above 50% :<b><%=stat.getCountofStudentsAboveFiftyPercentage() %></b></td>
            </tr>

        </table>
            </center>
                </td>
        </tr>
        <tr>
            <td>
                <br>
                <center>
    <table BORDER="1" width="95%" style="border-style: groove" cellspacing="0" cellpadding="3" class="textblack">
        <tr  bgcolor="#ddcccc" align="center">
            <th class="textblack">SlNo</th>
            <th class="textblack">Suject Code</th>
            <th class="textblack">Subject Name</th>
            <th class="textblack">Total Registration</th>
            <th class="textblack">Total Pass</th>
            <th class="textblack">Pass %</th>
        </tr>
    <% while(i<Count){
        ResultStatistics ReStat=Result.get(i);
                                        %>
                                        <tr>
                                            <td class="textblack"><%=i+1%></td>
                                            <td class="textblack"><%=ReStat.SubjectCode %></td>
                                            <td class="textblack"><%=ReStat.SubjectName %></td>
                                            <td class="textblack"><%=ReStat.TotalReg %></td>
                                            <td class="textblack"><%=ReStat.TotalPass %></td>
                                            <td class="textblack"><%=ReStat.PassPercentage %></td>
                                        </tr>


                                    <% i++;}}%>
                                    </table></center></td></tr><br>
                                    <tr >
                                                <td class="textblack" align="center" height="40px">
                                                    <input type="radio" name="ModerationType" id="ModerationType" checked="true" value="0" >Semester Moderation
                                                
                                                    <input type="radio" name="ModerationType" id="ModerationType" value="1" >Subject Moderation
                                                </td> 
                                    </tr>
                                    <tr>
                                        <td>
                                            <fieldset><legend><legend><span style="color:#a02a28; font-size: small"><b>Prepare For Moderation</b></span></legend></legend>
                                              <div id="BranchWiseModeration" >
                                                <table>
                                                    <tr>
                                                        <td class="textblack">Enter Moderation Range: From
                                                            <input type="text" id="ModerationFrom" name="ModerationFrom" value="" maxlength="2" style="width: 100px">
                                                        </td>
                                                        <td class="textblack">To
                                                            <input type="text" id="ModerationTo" name="ModerationTo" value="" maxlength="2" style="width: 100px">
                                                        </td>
                                                        <td>
                                                            <input type="submit" name="Submit"
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                                
                                            </fieldset>
                                        </td>
                                    </tr>
    </table>
                                    <% }else
                                        {%>
                                        <center>
                        <table>
                            <tr>
                            <td></td>
                            <td colspan="" class="textblack">  <span  class="Error" style="color:red">*Mark Verification has not yet Completed for this Course*  </span></td>
                            </tr><br/>
                        </table> </center><% }%>
</div>
