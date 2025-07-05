

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<div id="MarkEntryStatusForCourse">
                                         <%
                                         model.Absentees abs=new model.Absentees();
                                            MarkEntryPRN mark=new MarkEntryPRN(request, response);
                                            Integer ExamId=abs.getCurrentExamId();
                                            ArrayList<Subject> Subj=mark.getAllSubjectsForCourse();
                                            int Count=Subj.size(),i=0;
                                            if(Count!=0)
                                                {
                                         %>
                                         <table border="1" style="border-style: groove" cellspacing="0" cellpadding="3" width="100%">
                                             <tr class="textblack" bgcolor="#e8dec3" align="center">
                                                 <th>Subject</th>
                                                 <th>Semester</th>
                                                 <th>Total Registration</th>
                                                 <th>Total Answer Scripts</th>
                                                 <th>Mark Entered</th>
                                                 <th>Mark Verified</th>
                                                 <th>MarkEntry Pending</th>
                                                 <th>Absentees (Registered) </th>
                                                 <th>Absentees(Not Registered) </th>
                                                 <th>MalPractice </th>
                                                 <th>Withheld (Unconfirmed)</th>
                                                 <th>Status</th>
                                            </tr>
                                            <%
                                            int PrevSem=0;
                                            while(i<Count)
                                                {
                                                Subject Sub=new Subject();
                                                
                                                Sub=Subj.get(i);
                                                String Status=null,bgcolor=null;
                                                int TotalStudents=abs.getTotalCountOfStudentsForSubject(Sub.SubjectBranchId,ExamId.toString());
                                                int MarkEntered=abs.getTotalCountOfMarkEnteredForSubject(Sub.SubjectBranchId,ExamId.toString());
                                                int AbsEntered=abs.getTotalCountOfAbsenteesEntered(Sub.SubjectBranchId);
                                                int Unconfirmed=abs.getTotalCountOfUnconfirmedStudents(Sub.SubjectBranchId);
                                                int MarkVer=0;
                                                //int TotalMalPractice=abs.getTotalCountOfMalPracticeForSubject(Sub.SubjectBranchId,ExamId.toString());
                                                int MalPracticeEntered=abs.getTotalCountOfMalPracticeEntered(Sub.SubjectBranchId);
                                                //int Abs=abs.getTotalCountOfAbsenteeForSubject(Sub.SubjectBranchId,ExamId.toString());
                                                 int TotalAbsWithOutFee=abs.getTotalAbsenteesEnteredForSubjectWithoutFee(Sub.SubjectBranchId, Integer.parseInt(Sub.YearSem), ExamId.toString());
                                                int TotalScripts=((TotalStudents-(AbsEntered-TotalAbsWithOutFee))-MalPracticeEntered);
                                                if(TotalScripts<0)
                                                    {
                                                        TotalScripts=0;
                                                    }
                                                int MarkEntryPending=TotalScripts-MarkEntered-Unconfirmed;
                                                if(MarkEntryPending<0)
                                                    {
                                                        MarkEntryPending=0;
                                                    }
                                                int MarkVerified=abs.getTotalCountOfMarkVerifiedForSubject(Sub.SubjectBranchId,ExamId.toString());
                                                
                                                
                                                if(TotalStudents==MarkEntered+(AbsEntered-TotalAbsWithOutFee)+MalPracticeEntered+Unconfirmed && MarkEntered==MarkVerified )
                                                    {
                                                    if(TotalStudents==0)
                                                        {
                                                         Status=" ";
                                                        }
                                                    else
                                                        {
                                                         Status="Completed";
                                                         bgcolor="#19610f";
                                                        }
                                                    }
                                                else
                                                    {
                                                     Status="Pending";
                                                     bgcolor="#d84937";
                                                    }
                                            %>
                                            <% if(PrevSem!=Integer.parseInt(Sub.YearSem)){%>
                                            <tr>
                                                <td colspan="12" align="center" valign="middle"><h5><font color="#7e1917"><%=Sub.YearSem %></font></h5></td>
                                            </tr><%}%>
                                            <tr>
                                                <td class="textblack"><%=Sub.SubjectName %></td>
                                                <td class="textblack"><%=Sub.YearSem %></td>
                                                <td class="textblack"><%=TotalStudents %></td>
                                                <td class="textblack"><%=TotalScripts %></td>
                                                <td class="textblack"><%=MarkEntered %></td>
                                                <td class="textblack"><%=MarkVerified %></td>
                                                <td class="textblack"><%=MarkEntryPending %></td>
                                                <td class="textblack"><%=AbsEntered-TotalAbsWithOutFee %></td>
                                                <td class="textblack"><%=TotalAbsWithOutFee %></td>
                                                <td class="textblack"><%=MalPracticeEntered %></td>
                                                <td class="textblack"><%=Unconfirmed %></td>
                                                <td class="textblack"  ><font color="<%=bgcolor %>"><%=Status %></font></td>
                                            </tr>
                                            <% i++; PrevSem=Integer.parseInt(Sub.YearSem);}%>
                                         </table><% }%>
                                     </div>