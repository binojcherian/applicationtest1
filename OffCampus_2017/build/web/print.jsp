<%
if(request.getSession().getAttribute("CAPUSERID")==null){
    pageContext.forward("login.jsp");
}else if(Integer.parseInt(request.getSession().getAttribute("CAPUSERSTEP").toString())<4){
    pageContext.forward("bankpay.jsp");   
}else if(Integer.parseInt(request.getSession().getAttribute("CAPUSERSTEP").toString())==4){
    pageContext.forward("studentdetail.jsp");   
}else if(Integer.parseInt(request.getSession().getAttribute("CAPUSERSTEP").toString())==5){
    pageContext.forward("optionreg.jsp");   
}else if(Integer.parseInt(request.getSession().getAttribute("CAPUSERSTEP").toString())==6){
    pageContext.forward("finalsubmit.jsp");   
}
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="methods.StudentDetailMethod" %>
<%@page import="methods.QualificationMethod" %>
<%@page import="methods.accountmethods"%>
<%@page import="bin.*,action.*,bin.language.*"%>
<%@page import="entities.StudentPersonalDetailsEntities"%>
<%
    config configObj            = new config();
    english langObj             = new english();
    common commonobj=new common();
    StudentDetail studentDetail =   new StudentDetail(request , response);

    commonobj.activitylog(request.getSession().getAttribute("CAPUSERID").toString(), commonobj.getClientIP(request), "15");
%>
 <html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title><%=langObj.site_title%></title>
        <link rel="shortcut icon" href="favicon.ico" type="image/png"  />
        <style type="text/css">
            body {margin:0; padding:0; width:100%;background:#fff; font-family:Arial, Helvetica, sans-serif;font-size:11px;}
            @media print {
            .break {page-break-before:always}
            input#btnPrint {
            display: none;
            }
            }
            input.printbutton {
                background-color:#91AF38;
                color:#fff;
                border:2px solid #590202;
                display: inline-block;
                outline: none;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                font: 15px/100% Arial, Helvetica, sans-serif;
                padding: .4em 4em .4em 4em;
                text-shadow: 0 1px 1px rgba(0,0,0,.3);
                -webkit-border-radius: .2em; 
                -moz-border-radius: .2em;
                -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                box-shadow: 0 1px 2px rgba(0,0,0,.2)
            }
            input.printbutton:hover {
                text-decoration: none;
                background-color: #71AF68
            }
            input.printbutton:active {
                position: relative;
                border:1px solid #FFF;
                top: 1px
            } 
        </style>
        <script type="text/javascript">
            function printtoken(){
                window.print();
            }
        </script>
    </head>
<body>
    <table id="main_table" cellpadding="0" cellspacing="0" border="0" style="color: #0E385F;width: 100%">
        <tr><td  align="center">
        <input type="button" class="printbutton" id="btnPrint" value="PRINT APPLICATION" onclick="javascript:printtoken();" />
        </td></tr>
        <tr><td>
            <table border="0px">
            <tr><td align="right" width="5%"><img src="<%=configObj.getRootpath()%>images/mgulogo1.jpg" border="0" alt="" /></td>
                <td  align="center" style="font-size: 22px;color: black;" width="95%"><b>MAHATMA GANDHI UNIVERSITY, KOTTAYAM</b>
                <br/><font size="2">APPLICATION FOR ADMISSION TO M.Ed (2011-12) THROUGH CENTRALIZED ALLOTMENT PROCESS</font>
                </td></tr>
            </table>
        </td></tr>
        <tr><td align="right">
            <table bgcolor="#d9d6d6" cellpadding="0" cellspacing="0" border="0">
                <tr><td  align="right" style="font-size: 18px;color: black;" bgcolor="white">APPLICATION NUMBER : <b><%=studentDetail.studentdetailArray.get(0).getApplicationNo()%></b></td></tr>
            </table>
        </td></tr>
        <tr><td style="width: 100%">
            <fieldset style="border: solid 1px #CCCCCC;color: black"><legend><b>Personel Details of Applicant</b></legend>
            <table style="font-size: 14px ;" width="100%" border="0">
            <tr><td width="50%" align="left">Name of the Applicant</td><td width="3%"  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantName()%></td></tr>
            <tr><td  align="left">Name of Father/Guardian</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantGuardian()%></td></tr>
            <tr><td align="left">Sex</td><td align="left">:-</td>
                <td  align="left">
                <%
                String sex;
                sex=studentDetail.studentdetailArray.get(0).getApplicantSex();
                if(sex.equals("F"))
                out.print("Female");
                else
                out.print("Male");
                %>
            </td></tr>
           <tr><td  align="left">Date of Birth</td><td  align="left">:-</td><td  align="left"><%=commonobj.convertDatetoddmmyyyy(studentDetail.studentdetailArray.get(0).getApplicantDob())%></td></tr>
           <tr><td  align="left">Nationality</td><td  align="left">:-</td>
                <td  align="left"><%
                String nationality;
                nationality=studentDetail.studentdetailArray.get(0).getApplicantNationality();
                if(nationality.equals("I"))
                out.print("Indian");
                else
                out.print("Foreign");
                %>
                </td>
           </tr>
            <tr><td  align="left">Annual Income</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getAnnualincome()%></td></tr>
            <tr><td  align="left">Religion</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantReligion()%></td></tr>
            <tr><td  align="left">Caste</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantCaste()%></td></tr>
            <tr><td  align="left">Community</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantCommunity()%></td></tr>
           
         <tr><td  align="left">Reservation claim under Other Eligible Community(OEC)</td><td width="3%"  align="left">:-</td>
                <td  align="left">
                     <% String oec;
                  //  oec = studentDetail.accountinfo.get(0).getOec();
                   // if (oec.equals("0")) {
                   //     out.print("Not Applicable");
                   // } else if (oec.equals("1")) {
                    //    out.print("Yes");
                   // } %>
                </td>
       </tr>
             <tr><td  align="left">Selected caste under OEC</td><td  width="3%"  align="left">:-</td>
                <td  align="left">
              <%--
              if (studentDetail.accountinfo.get(0).getOeccastname()==null) {
                    out.print("Not Applicable");
              } else  {
              out.print(studentDetail.accountinfo.get(0).getOeccastname());
              } 
                --%>
             </td></tr>
            </table>
            </fieldset>
            </td></tr>
            <tr><td width="100%">
            <fieldset style="border: solid 1px #CCCCCC;">
            <legend><b><font color="black">Address for Communication </font></b></legend>
                <table style="font-size: 14px ;" width="100%" border="0">
                    <tr><td width="50%"  align="left">House Name/NO</td><td width="3%">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantHouseName()%></td></tr>
                    <tr><td  align="left">Place</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantPlace()%></td></tr>
                    <tr><td  align="left">District</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantDistrict()%></td></tr>
                    <tr><td  align="left">State</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantState()%></td></tr>
                    <tr><td  align="left">Pin code</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantPin()%></td></tr>
                    <tr><td  align="left">Land Phone With STD code</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantLandPhone()%></td></tr>
                    <tr><td  align="left">Mobile</td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantMobile()%></td></tr>
                    <tr><td  align="left">Email ID (If any) </td><td  align="left">:-</td><td  align="left"><%=studentDetail.studentdetailArray.get(0).getApplicantEmail()%></td></tr>
              </table>
            </fieldset>
           </td></tr>
            
                       <tr><td width="100%">
                        <fieldset style="border: solid 1px #CCCCCC;"><legend><b><font color="black">Weightage If Any </font></b></legend>
                        <table style="font-size: 14px ;" width="100%">
                        <tr><td width="50%"  align="left">Eligibility for consideration under other categories</td><td width="3%"  align="left">:-</td>
                            
                            <% 
                            String ph,tq,sq;
                            ph=studentDetail.studentdetailArray.get(0).getSpeclReservPH();
                             tq=studentDetail.studentdetailArray.get(0).getSpeclReservTQ();
                              sq=studentDetail.studentdetailArray.get(0).getSpeclReservSQ();
                            if(ph!=null && ph.equals("Y")){%><td  align="left"><%="Physically Handicapped"%></td><%}%>
                            <%  if(sq!=null && ph.equals("Y")){%><td  align="left"><%="Sports Person"%></td><%}%>
                             <%  if(tq!=null && tq.equals("Y")){%><td  align="left"><%="Teachers Quota"%></td><%}%>
                           
                            </tr>
                       <tr><td  align="left">Are you entitled to Bonus Marks of Ex-service men</td><td  align="left">:-</td>
                            <td  align="left">
                            <%
                            String exservice;
                            exservice=studentDetail.studentdetailArray.get(0).getExservice();
                            if(exservice.equals("Y"))
                            out.print("YES");
                            else
                            out.print("Not Applicable");
                            %>
                            </td></tr>
                        <tr><td  align="left">Teaching Experience(Completed in years and months)</td><td  align="left">:-</td>
                            <td  align="left"><%=studentDetail.studentdetailArray.get(0).getExpYears()%>&nbsp; Years
                            <%=studentDetail.studentdetailArray.get(0).getExpMonths()%>&nbsp;Months
                            </td></tr>
                        </table>
                        </fieldset>
                        </td></tr>
                       <tr class="break"><td colspan="3"></td></tr>
                      <tr><td colspan="3"> <br/> </td></tr>
                      <tr><td  align="right" style="font-size: 18px;color: black;" bgcolor="white">APPLICATION NUMBER : <b><%=studentDetail.studentdetailArray.get(0).getApplicationNo()%></b></td></tr>
                     
                   <tr><td width="100%">
                        <fieldset style="border: solid 1px #CCCCCC;"><legend><b><font color="black">Details of Qualifying Examination</font></b></legend>
                            <table width="100%" cellpadding="3" cellspacing="1" border="0" bgcolor="#c7cacd" style="font-size: 14px ;">
                                <tr bgcolor="#dcdcdc"><td><b>Sl No</b></td>
                                    <td><b>Qualifying Degree</b></td>
                                    <td  align="center"><b>Name of University</b></td>
                                    <td  align="center"><b>Year of Passing</b></td>
                                    <td  align="center"><b>Marks/CGPA Secured</b></td>
                                    <td  align="center"><b>Maximum Marks/CGPA </b></td></tr>
                             <%   int b;
                                  for (b = 0; b < studentDetail.qualArray.size(); b++) {%>
                <tr bgcolor="white"><td align="center"><%=b+1%></td><td align="left"><%=studentDetail.qualArray.get(b).getQualfyingDegreeName()%></td>
                    <td align="center"><%=studentDetail.qualArray.get(b).getQualuniversityName()%></td>
                    <td align="center"><%=studentDetail.qualArray.get(b).getQualYop()%></td>
                       <td align="center"><%=studentDetail.qualArray.get(b).getQualSecuredMarks()%></td>
                       <td align="center"><%=studentDetail.qualArray.get(b).getQualTotalMarks()%></td>
                </tr><%}%>
       
        </table>
        </fieldset>
        </td></tr>
        
        <tr><td>
                <table style="font-size: 14px ;" width="100%" border="0">
                    <tr><td colspan="2"><center><b><u>DECLARATION</u></b></center></td></tr>
                    <tr><td  align="justify" colspan="2">
                            <ol><li> I hereby declare that I have read the various clauses in the Prospectus for Admission to M.Ed Programmes, 2011-12 in the Affiliated Colleges of Mahatma Gandhi University and the instructions carefully and I agree to abide by them.</li>
                                <li> I also declare that all the statements made in this application are true, complete and correct to the best of my knowledge and    belief and that in the event of any  information being found false or incorrect or ineligibility being detected before or after the     admission , action can be taken against me by the Mahatma Gandhi University.</li>
                            </ol>
                    </td></tr>
                </table>
          </td></tr>
        <tr><td>
                <br/>
                <table width="100%" style="font-size: 14px ;">
                    <tr><td colspan="2" width="100%">Place .............................</td></tr>
                    <tr><td width="70%">Date   .............................</td>
                        <td width="30%">Name &amp; Signature of the Applicant</td></tr>
                    <tr><td colspan="2"></td></tr>
                    <tr><td colspan="2"></td></tr>
                    <tr><td colspan="2"></td></tr>
                    <tr><td colspan="2" align="center" >Signature of the Parent/Guardian</td></tr>
                   
                </table>
         </td></tr>
                </table>
</body>
</html>
