<%-- 
    Document   : PrintStatistics
    Created on : Nov 13, 2014, 4:02:36 PM
    Author     : Sruthy C N
--%>

<%@page import="Entity.StatisticsWithModeration"%>
<%@page import="Entity.ExamData"%>
<%@page import="Entity.ResultStatistics"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.DBConnection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="model.MarkEntry"%>
<%@page import="java.sql.*"%>
<%@page import="Controller.Statistics"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DEMS</title>
        <style type="text/css">
            body {margin:0; padding:0; width:100%;  background:#fff; font-family:Arial, Helvetica, sans-serif;font-size:11px;}
            @media print {
                input#btnPrint {
                    display: none;
                }
            }
            input.printbutton {
                background-color:#669999;
                color:#fff;
                border:1px solid #a65757;
                display: inline-block;
                outline: none;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                font: 14px/100% Arial, Helvetica, sans-serif;
                padding: .4em 2em .4em 2em;
                text-shadow: 0 1px 1px rgba(0,0,0,.3);
                -webkit-border-radius: .2em; 
                -moz-border-radius: .2em;
                -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                box-shadow: 0 1px 2px rgba(0,0,0,.2)
            }
            input.printbutton:hover {
                text-decoration: none;
                background-color: #99CCFF;
            }
            input.printbutton:active {
                position: relative;
                border:1px solid #FFF;
                top: 1px
            }
            div#watermark {
                position: relative;
                width: 100%;
                height: 100%;
                z-index: 99999;
                background: url('images/mgulogo_watermark.jpg') center center no-repeat ;

            }


        </style>

        <script type="text/javascript">
            function printNow(){
                //alert('h');
                window.print();
            }
        </script>

    </head>
    <body>
        <%
         Statistics stat=new Statistics(request, response);
                
                MarkEntry ms = new MarkEntry();
%>
        <table width="100%" boder="1">
            <tr><td align="center" colspan="2"><div align="center">
                        <input class="printbutton" type="button"  id="btnPrint" value="PRINT NOW" onClick="javascript:printNow();" />
                    </div></td>
            </tr>
            <tr>
                <td align="center">
                   <h3>MG University  Distance Education</h3></td>
                <!--<td align="right" width="7%"> 
                    <img src="http://chart.googleapis.com/chart?cht=qr&chs=100x100&choe=UTF-8&chld=H&chl=M G Univeristy Distance Education" alt="" align="right" />

                </td>-->


            </tr>
        </table>
        <%

    ArrayList<ResultStatistics> Result=stat.getStatisticsForCourse();
    int Count=Result.size();int i=0;
    if(Count!=0){%>
       

        <table border="0" width="50%" style="font-size: 12px;">
                <tr>
                    <td width="20%">Exam :</td><td width="3%">:</td>
                    <td width="30%"><b><% ArrayList<ExamData> ExamList=stat.getExams();
                                                          int j=1;
                                                            int count=ExamList.size();
                                                            if(count>0){
                                                                    while(j<=count)
                                                                        {
                                                                        ExamData exam=ExamList.get(j-1);
                                                            if(stat.getExam().equals(exam.ExamId)) 
                                                                    out.print(exam.ExamName);

                                                            j++; }}
                                                            %></b></td>
                </tr>
                <tr>
                    <td width="20%">Course :</td><td width="3%">:</td>
                    <td width="10%"><b><% ArrayList<Entity.CourseData> CourseList=stat.getBranchList();
                                                           int k=0;
                                                            count=CourseList.size();
                                                                  if(count>0){
                                                                    while(k<count)
                                                                        {
                                                                        Entity.CourseData Course=CourseList.get(k);
                                                          if(Integer.parseInt(stat.getBranch())==Course.BranchId)
                                                            out.print(Course.BranchName); 
                                                                

                                                            k++; }
                                                                  }
                                                            %></b></td>
                </tr>
                 <tr>
                    <td width="20%">Year/Semester: </td><td width="3%">:</td>
                    <td width="10%"><b><%=stat.getSemYear() %></b></td>
                </tr>
                 <tr>
                    <td width="20%">Number of Candidates : </td><td width="3%">:</td>
                    <td width="10%"><b><%=stat.getTotalCountOfStudentsForCourse() %></b></td>
                </tr>
                <tr>
                    <td width="20%">Number of Candidates Passed :</td><td width="3%">:</td>
                    <td width="10%"><b><%=stat.getTotalPassForCourse() %></b></td>
                </tr>
        </table>
     
    <table BORDER="1"  style="border-style: groove" cellspacing="0" cellpadding="3" width="80%" align="center">
        <tr  bgcolor="#ddcccc" align="center">
            <th class="textblack">SlNo</th>
             <th class="textblack">Syllabus </th>
           <!--<th class="textblack">Specialization </th>-->
            <th class="textblack">Subject Code</th>
            <th class="textblack">Subject Name</th>
            <th class="textblack">Total Registration</th>
            <th class="textblack">Total Pass</th>
            <!--<th class="textblack">Pass %</th>-->
        </tr>
    <% while(i<Count){
        ResultStatistics ReStat=Result.get(i);
                                        %>
                                        <tr>
                                            <td class="textblack"><%=i+1%></td>
                                            <td class="textblack"><%=ReStat.syllabus%></td>
                                          <!--<td class="textblack"><%=ReStat.SubBranchName%></td>-->
                                            <td class="textblack"><%=ReStat.SubjectCode %></td>
                                            <td class="textblack"><%=ReStat.SubjectName %></td>
                                            <td class="textblack"><%=ReStat.TotalReg %></td>
                                            <td class="textblack"><%=ReStat.TotalPass %></td>
                                           <!-- <td class="textblack"><%=ReStat.PassPercentage %></td>-->
                                        </tr>
                                    <% i++;}}%>
                                    </table>
                                    <br/>
                                    
                                    <table align="center" width="50%">
                                        <tr><td><b>Moderation Range:</b></td><td><b><%=stat.getModerationFrom() %> to <%=stat.getModerationTo() %></b></td></tr>
                                        <% if(!request.getParameter("Subject").equals("-1")) {%><tr><td><b>Subject:</b></td><td><b> <%=stat.getSubjectName()%></b></td></tr><%}%>
                                    </table>
                                      <br/><br/><br/>
                                    <% if(request.getParameter("ModerationType").equals("0"))
                                    {
                                    %>
                                     <%
                                                        ArrayList<StatisticsWithModeration> Statistics =stat.getStatisticsForBranchWithModeration();
                                                        Count=Statistics.size();i=0;
                                                        if(Count>0)
                                                            {%>
                                                        <table id="BranchM" align="center" style="border-style: groove" cellspacing="0" cellpadding="3" width="70%" border="1">
                                                          <tr bgcolor="#ddcccc">
                                                            <th class="textblack" >SlNo</th>
                                                            <th class="textblack" >Applied Moderation</th>
                                                            <th class="textblack" >Total Pass</th>
                                                            <th class="textblack" >Sem Pass</th>
                                                            <!--<th class="textblack" >Pass Percentage</th>-->
                                                            
                                                            </tr>
                                                          <% while(i<Count)
                                                            {
                                                               StatisticsWithModeration StatResult=Statistics.get(i);
                                                                %>
                                                          <tr>
                                                            <td class="textblack"><%=i+1 %></td>
                                                            <td class="textblack"><%=StatResult.AppliedModeration %></td>
                                                            <td class="textblack"><%=StatResult.TotalPass %></td>
                                                            <td class="textblack"><%=StatResult.SemPass %></td>
                                                            <!--<td class="textblack"><%=StatResult.PassPercentage %></td>-->
                                                            </tr>
                                                          <% i++;}%>
                                                          </table>
                                                        <% }%>
                                    
                                    <%} else if(request.getParameter("ModerationType").equals("1"))
                                    {%>
                                     <table id="SubjectM" style="border-style: groove" cellspacing="0" cellpadding="3" border="1" width="70%" align="center">
                                                             <%
                                                                ArrayList<StatisticsWithModeration> SubjectStatistics =stat.getStatisticsForSubjectWithModeration();
                                                                Count=SubjectStatistics.size();i=0;
                                                                if(Count>0)
                                                            {%>
                                                       
                                                        <tr bgcolor="#ddcccc">
                                                            <th class="textblack" width="20%">SlNo</th>
                                                            <th class="textblack" width="20%">Applied Moderation</th>
                                                            <th class="textblack" width="20%">Total Pass</th>
                                                            <th class="textblack" width="20%">Failed</th>
                                                            <th class="textblack" width="20%">Pass Percentage</th>
                                                            <th class="textblack" width="20%">Total Pass:<%=stat.getSubjectName() %></th>
                                                        </tr>
                                                         <% while(i<Count)
                                                            {
                                                               StatisticsWithModeration StatResult=SubjectStatistics.get(i);
                                                                %>
                                                        <tr>
                                                            <td class="textblack"><%=i+1 %></td>
                                                            <td class="textblack"><%=StatResult.AppliedModeration %></td>
                                                            <td class="textblack"><%=StatResult.TotalPass %></td>
                                                            <td class="textblack"><%=StatResult.TotalFail %></td>
                                                            <td class="textblack"><%=StatResult.PassPercentage %></td>
                                                            <td class="textblack"><%=StatResult.TotalSubjectPass %></td>
                                                        </tr>
                                                            <% i++;}%>
                                                        </table><% }%>
                                    <% } %>

    </body>
</html>
