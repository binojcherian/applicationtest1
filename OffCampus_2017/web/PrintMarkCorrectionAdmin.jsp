<%-- 
    Document   : PrintMarkCorrectionAdmin
    Created on : Nov 1, 2014, 1:01:55 PM
    Updated on : Nov 27,28 2014
    Author     : Sruthy C N
--%>

<%@page import="Entity.StudentMarkInfo"%>
<%@page import="Entity.StudentInfo"%>
<%@page import="model.MarkEntry"%>
<%@page import="model.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="Entity.StudentSubjectMark"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Controller.MarkVerification"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
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
        <!-- <div id="watermark">-->
        <%
            MarkVerification mark = new MarkVerification(request, response);

            ArrayList<StudentInfo> studList = new ArrayList<StudentInfo>();

            MarkEntry ms = new MarkEntry();
        %>
        <table width="100%" boder="1">
            <tr><td align="center" colspan="2"><div align="center">
                        <input class="printbutton" type="button"  id="btnPrint" value="PRINT NOW" onClick="javascript:printNow();" />
                    </div></td>
            </tr>
            <tr>
                <td align="center">
                    <img src="images/mgulogo1.jpg" alt="" border="0"/><h3>MG University  Distance Education</h3></td>
                <td align="right" width="7%"> 
                    <img src="http://chart.googleapis.com/chart?cht=qr&chs=100x100&choe=UTF-8&chld=H&chl=M G Univeristy Distance Education" alt="" align="right" />

                </td>


            </tr>
        </table>
        <%
        
            String exam = "", branch = "", sub = "", assi = "", pr = "", seme = "", aca = "";
            int e = Integer.parseInt(request.getParameter("ex"));
            int b = Integer.parseInt(request.getParameter("co"));
            int s = Integer.parseInt(request.getParameter("sub"));
            String prn = request.getParameter("prn");
            int ac = Integer.parseInt(request.getParameter("ac"));
            int sem = Integer.parseInt(request.getParameter("year"));
            seme = String.valueOf(sem);
            String a = request.getParameter("assi");
            Connection con = null;
            try {
                    //System.out.println("in try");
                con = new DBConnection().getConnection();
               // System.out.println("b-->"+b);
                if (b != -1) {
                    PreparedStatement st1 = con.prepareStatement("SELECT BranchName FROM BranchMaster where BranchId=" + b + "");
                    ResultSet Rs1 = st1.executeQuery();
                    if (Rs1.next()) {
                         System.out.println("In resultSet");
                        branch = Rs1.getString(1);
                    }
                } else {
                    branch = "Nil";
                }
                //System.out.println(" branch "+branch);
             /* 
                if (e != -1) {
                    PreparedStatement st = con.prepareStatement("SELECT ExamName from ExamMaster where ExamId=" + e + "");
                    ResultSet Rs = st.executeQuery();
                    if (Rs.next()) {
                        exam = Rs.getString(1);
                    }
                } else {
                    exam = "Nil";
                }
                
                if (s != -1) {
                    PreparedStatement st2 = con.prepareStatement("SELECT SubjectName FROM subjectmaster where SubjectId=" + s + "");
                    ResultSet Rs2 = st2.executeQuery();
                    if (Rs2.next()) {
                        sub = Rs2.getString(1);
                    }
                } else {
                    sub = "Nil";
                }
                if (!a.equals("-1")) {
                    PreparedStatement st3 = con.prepareStatement("SELECT Name FROM mguclientlogin where UserName='" + a + "'");
                    ResultSet Rs3 = st3.executeQuery();
                    if (Rs3.next()) {
                        assi = Rs3.getString(1);
                    }
                } else {
                    assi = "Nil";
                }
*/
                if (prn == null || (prn.isEmpty())) {
                    pr = "Nil";
                } else {
                    pr = request.getParameter("prn");
                }
                if (sem == -1) {
                    seme = "Nil";
                } else {
                    seme = request.getParameter("year");
                }
                if (ac == -1) {
                    aca = "Nil";
                } else {
                    aca = request.getParameter("ac");
                }
                //System.out.println("b4 query");
                //Outer Query
                String Querys = "Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,SUBSTRING_INDEX(em.ExamName,'-',-1) as ExamName ,sp.BranchId as Branch ,sb.CurrentYearSem as Sem,sb.PaperNo,sp.PRN,s.SubjectName,sm.ExamId as ExamID,sm.StudentId,sm.IsValid,sm.IsPassed,sm.MarkStatus,sm.ReMarks,sm.IsWithheld,"
                        + " sm.IsAbsent,sm.IsPracticalAbsent,sm.IsMalPractice,sm.IsReValuated,sm.IsPostCorrected,sb.SubjectBranchId ,sm.ExternalMark,"
                        + " sm.InternalMark,sm.ModerationExt,sm.ModerationInt,sm.ClassModeration,sm.PracticalMark,sm.TourReportMark,sm.StudentSubjectMarkId,c.CollegeName "
                        + " from StudentSubjectMark sm inner join StudentPersonal sp on sm.StudentId=sp.StudentId inner join  SubjectBranchMaster sb "
                        + " on sm.SubjectBranchId=sb.SubjectBranchId inner join  SubjectMaster s on sb.SubjectId=s.SubjectId "
                        + "inner join ExamMaster em on em.ExamId=sm.ExamId inner join CollegeMaster c on sp.CollegeId=c.CollegeId";
                Querys += " where sm.IsValid=1   ";

                if (e != -1) {
                    Querys += " and sm.ExamId=" + e;
                }
                if (b != -1) {
                    Querys += " and sp.BranchId=" + b;
                }
                if (s != -1) {
                    Querys += " and sb.SubjectId=" + s;
                }
                if (sem != -1) {
                    Querys += " and sb.CurrentYearSem=" + sem;
                }
                if (ac != -1) {
                    Querys += " and sb.AcademicYear=" + ac;
                }
                if (prn != null && (!prn.isEmpty())) {
                    Querys += " and sp.PRN='" + prn + "'";
                }
                if (a != null && (!a.equals("-1"))) {
                    Querys += " and sm.StudentSubjectMarkId in (" + ms.getStudentSubjectMarkIdsEnteredByUser(assi) + ")";
                    System.out.println(Querys);
                }
                Querys += "  group by sp.PRN";
                

                //System.out.println("----> "+Querys);
               PreparedStatement std = con.prepareStatement(Querys);
                ResultSet rss = std.executeQuery();
                while (rss.next()) {
                    StudentInfo stud = new StudentInfo();
                    stud.name = rss.getString("StudentName");
                    stud.PRN = rss.getString("PRN");
                    stud.examId = rss.getInt("ExamID");
                    stud.exam = rss.getString("examName");
                    stud.sem = rss.getInt("Sem");
                    stud.branch = rss.getInt("Branch");
                    stud.course = branch;
                    stud.centre = rss.getString("CollegeName");
                    studList.add(stud);
                }



            } catch (Exception es) {
                System.out.println(es);
            }
            finally
            {
             con.close();
            }
           
        %>


        <br/></br>

        <% for (int i = 0; i < studList.size(); i++) {%> 
        <table border="0" width="100%" cellspacing="3" cellpadding="3" style="font-size: 12px;">
            <tr><td>Name:</td><td><%=studList.get(i).getName()%></td><td>Course:</td><td><%=branch%></td><td></td><td></td></tr>
            <tr><td>PRN:</td><td><%=studList.get(i).getPRN()%></td><!--<td>Sem/Year</td><td><!--%=studList.get(i).getSem()%></td>--><td>Centre</td><td><%=studList.get(i).getCentre()%></td></tr>
        </table>
        <br/>
        <%
            // ArrayList<StudentSubjectMark> StudentList=null;
            ArrayList<StudentMarkInfo> studentmark = null;
            try {
                //StudentList = new ArrayList<StudentSubjectMark>();
                studentmark = new ArrayList<StudentMarkInfo>();
                con = new DBConnection().getConnection();
                String Query = "Select SQL_CALC_FOUND_ROWS distinct sp.StudentName,SUBSTRING_INDEX(em.ExamName,'-',-1) as ExamName ,sb.CurrentYearSem,sb.PaperNo,sp.PRN,s.SubjectName,sm.ExamId,sm.StudentId,sm.IsValid,sm.IsPassed,sm.MarkStatus,sm.ReMarks,sm.IsWithheld,"
                        + " sm.IsAbsent,sb.ExternalMin,sb.InternalMin,sb.PassMark,sm.IsPracticalAbsent,sm.IsMalPractice,sm.IsReValuated,sm.IsPostCorrected,sb.SubjectBranchId ,sm.ExternalMark,"
                        + " sm.InternalMark,sm.ModerationExt,sm.ModerationInt,sm.ClassModeration,sm.PracticalMark,sm.TourReportMark,sm.StudentSubjectMarkId,sm.ExternalGraceMark,sm.InternalGraceMark "
                        + " from StudentSubjectMark sm inner join StudentPersonal sp on sm.StudentId=sp.StudentId inner join  SubjectBranchMaster sb "
                        + " on sm.SubjectBranchId=sb.SubjectBranchId inner join  SubjectMaster s on sb.SubjectId=s.SubjectId "
                        + "inner join ExamMaster em on em.ExamId=sm.ExamId ";
               // Query += " where sm.IsValid=1  and sm.ExamId=" + studList.get(i).getExamId() + " and sp.BranchId=" + studList.get(i).getBranch() + "";
                Query += " where sm.IsValid=1 and sp.PRN='" + studList.get(i).getPRN() + "'";
                if (Integer.parseInt(request.getParameter("ex")) != -1) {
                     Query += " and sm.ExamId=" + e;
                }
                if (Integer.parseInt(request.getParameter("co")) != -1) {
                      Query += " and sp.BranchId=" + b;
                }
                if (Integer.parseInt(request.getParameter("sub")) != -1) {
                    Query += " and sb.SubjectId=" + s;
                }
                if (Integer.parseInt(request.getParameter("year")) != -1) {
                    Query += " and sb.CurrentYearSem=" + sem;
                }
                if (Integer.parseInt(request.getParameter("ac")) != -1) {
                    Query += " and sb.AcademicYear=" + ac;
                }
                if (a != null && (!a.equals("-1"))) {
                    Query += " and sm.StudentSubjectMarkId in (" + ms.getStudentSubjectMarkIdsEnteredByUser(request.getParameter("assi")) + ")";
                    // System.out.println(Query);
                }
                Query += "  order by sp.PRN,sb.CurrentYearSem,sb.PaperNo,sm.SubjectBranchId,sm.ExamId  ";
                //System.out.println(Query);
                PreparedStatement sts = con.prepareStatement(Query);
                ResultSet rs = sts.executeQuery();
                while (rs.next()) {
                    StudentMarkInfo sms = new StudentMarkInfo();
                    sms.subject = rs.getString("SubjectName");
                    sms.sem=rs.getString("CurrentYearSem");
                    StudentSubjectMark student = new StudentSubjectMark();
                    String IsPassed=null;
                    
                    
                    if (rs.getString("IsAbsent").equals("1")) {

                        sms.extMark = "";
                        sms.intMark = "";
                        sms.pract = "";
                        sms.intmin = "";
                        sms.intMod = "";
                        sms.extmin = "";
                        sms.extMode = "";
                        sms.extClassmod = "";
                        sms.tour = "";
                        //sms.totMark = "";
                        sms.pass = "";
                        sms.exam="";
                        sms.totMark="";

                    } else {
                        sms.extMark = rs.getString("ExternalMark");
                        sms.intMark = rs.getString("InternalMark");
                        sms.pract = rs.getString("PracticalMark");
                        sms.extClassmod = rs.getString("ClassModeration");
                        sms.extMode = rs.getString("ModerationExt");
                        sms.intMod = rs.getString("ModerationInt");
                        sms.extgrace = rs.getString("ExternalGraceMark");
                        sms.intgrace = rs.getString("InternalGraceMark");
                        if (rs.getInt("ExternalMin") == 0) {
                            sms.extmin = "";
                        } else {
                            sms.extmin = rs.getString("ExternalMin");
                        }
                        if (rs.getInt("InternalMin") == 0) {
                            sms.intmin = "";
                        } else {
                            sms.intmin = rs.getString("InternalMin");
                        }
                        sms.tour = rs.getString("TourReportMark");
                        //sms.totMark = rs.getString("ExternalMin");
                        sms.pass = rs.getString("PassMark");
                        sms.exam=rs.getString("ExamName");
                        double total=(Double.parseDouble(rs.getString("ExternalMark")) + Double.parseDouble(rs.getString("InternalMark")) + Double.parseDouble(rs.getString("ModerationExt")) + Double.parseDouble(rs.getString("ModerationInt")) + Double.parseDouble(rs.getString("ClassModeration")) + Double.parseDouble(rs.getString("PracticalMark")) + Double.parseDouble(rs.getString("TourReportMark"))+ Double.parseDouble(rs.getString("ExternalGraceMark"))+ Double.parseDouble(rs.getString("InternalGraceMark")));
                        double min=rs.getDouble("PassMark");
                        sms.totMark = String.valueOf((Double.parseDouble(rs.getString("ExternalMark")) + Double.parseDouble(rs.getString("InternalMark")) + Double.parseDouble(rs.getString("ModerationExt")) + Double.parseDouble(rs.getString("ModerationInt")) + Double.parseDouble(rs.getString("ClassModeration")) + Double.parseDouble(rs.getString("PracticalMark")) + Double.parseDouble(rs.getString("TourReportMark"))+ Double.parseDouble(rs.getString("ExternalGraceMark"))+ Double.parseDouble(rs.getString("InternalGraceMark"))));
                        if(total>=min)
                        {
                            IsPassed="1";
                        }
                        else
                        {
                            IsPassed="0";
                        }
                    }
                    if (rs.getString("ReMarks") == null) {
                        sms.remarks = "";
                    } else {
                        sms.remarks = rs.getString("ReMarks");
                    }
                    if (rs.getString("IsAbsent").equals("1")) {
                        sms.status = "Absent";
                    } else if (IsPassed.equals("1")) {
                        sms.status = "Passed";
                    } else if (IsPassed.equals("0")) {
                        sms.status = "Failed";
                    } else if (rs.getString("IsWithheld").equals("1")) {
                        sms.status = "WithHeld";
                    } else if (rs.getString("IsMalPractice").equals("1")) {
                        sms.status = "MalPractice";
                    } else {
                        sms.status = "Present";
                    }
                    

                    studentmark.add(sms);
                }

            } catch (Exception et) {
                System.out.println(et);
            } finally {
                con.close();
            }
        %>
        <table border="1" width="100%" style="border-style: groove" cellspacing="0" cellpadding="3">

            
            <tr class="textblack"  align="center">

                <td width="230" rowspan="2">Subject</td>
                <td width="50" rowspan="2">Semester</td>
                <td colspan="5">External</td>
                <td colspan="4"> Internal</td>
                <td width="107">Practical</td>
                <td width="107" rowspan="2">Tour report</td>
                <td colspan="2">Total mark</td>
                <td width="139" rowspan="2">Status</td>
                <td width="139" rowspan="2">Exam</td>
                <td width="435" rowspan="2">Remarks</td>
            </tr>
            <tr>
                <td width="42">Min</td>
                <td width="42">Mark</td>
                <td width="42">Mod</td>
                <td width="42">grace</td>
                <td width="42">ClassMod</td>
                <td width="64">Min</td>
                <td width="84">Mark</td>
                <td width="71">Mod</td>
                   <td width="71">grace</td>
                <td width="107">&nbsp;</td>
                <td width="50">Min</td>
                <td width="45">Mark</td>
            </tr>
            <% for (int j = 0; j < studentmark.size(); j++) {

            %>
            <tr>
                <td class="textblack" align="left"><%=studentmark.get(j).getSubject()%></td>
                  <td class="textblack" align="left"><%=studentmark.get(j).getSem()%></td>
                <td><%=studentmark.get(j).getExtmin()%></td>
                <td><%=studentmark.get(j).getExtMark()%></td>
                <td><%=studentmark.get(j).getExtMode()%></td>
                 <td><%=studentmark.get(j).getextgrace()%></td>
                <td><%=studentmark.get(j).getExtClassmod()%></td>
                <td><%=studentmark.get(j).getIntmin()%></td>
                <td><%=studentmark.get(j).getIntMark()%></td>
                <td><%=studentmark.get(j).getIntMod()%></td>
                <td><%=studentmark.get(j).getIntgrace()%></td>
                <td><%=studentmark.get(j).getPract()%></td>
                <td><%=studentmark.get(j).getTour()%></td>
                <td><%=studentmark.get(j).getPass()%></td>
                <td><%=studentmark.get(j).getTotMark()%></td>
                <td><%=studentmark.get(j).getStatus()%></td>
                <td><%=studentmark.get(j).getExam() %></td>
                <td><%=studentmark.get(j).getRemarks()%></td>
            </tr>
            <% }%>
        </table>
        <br/>
        <% }%>



    </body>
</html>
