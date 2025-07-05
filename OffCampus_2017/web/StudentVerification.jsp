
<%--
    Document   : ApprovedMarks
    Created on : Sep 29, 2011, 2:23:23 PM
    Author     : mgu
--%>

<%@page import="java.io.*,java.util.*,model.*,Entity.*"%>

<% if (request.getSession().getAttribute("UserName") == null) {
                response.sendRedirect(response.encodeRedirectURL("/OffCampus/LoginPage.jsp?message=Session Time Out.Login again"));
            }else{
    mCourseFeeDD dd=new mCourseFeeDD();

            java.util.Hashtable<String, String> PersonalData;
            java.util.Hashtable<String, String> QualificationData;
            CenterVerification verification = new CenterVerification();
           /* if(request.getSession().getAttribute("Re")!=null)
                {
                request.getSession().removeAttribute("Re");
                }
           if(!verification.IsStudentValid(request))
               {
                   response.sendRedirect(response.encodeRedirectURL("AppliedStudents.jsp"));
               }*/
            model.Document document=new model.Document();
            Boolean isApproved=false;
            if(!verification.IsMGUApproved(request))
            {
                isApproved=true;
            }
            String remarksError = null;
              int sid = 0;
            Boolean isFinal = false;
            Boolean Center = false;

            if (request.getParameter("StudentId") != null)
            {
                sid = Integer.parseInt(request.getParameter("StudentId"));
                request.getSession().setAttribute("StudentId", sid);
            }
            Integer i = (Integer) request.getSession().getAttribute("StudentId");
            sid=i.intValue();
            mCourseFee fee=new mCourseFee(sid);
            if (request.getParameter("Verified") != null && request.getParameter("Verified").equals("Verify the Application"))
            {
                int status = 1;
if(fee.IsFeePaid()){
                if (verification.VerifyApplication(request, status))
                {

                   if(request.getParameter("PrePage")!=null)
                    {
                    if(request.getParameter("PrePage").equals("PRN"))
                        {
                            response.sendRedirect("ApprovedStudents.jsp?AV=1");
                        }
                    else if(request.getParameter("PrePage").equals("AL"))
                        {
                            response.sendRedirect("AppliedStudents.jsp?AV=1");
                        }
                    else if(request.getParameter("PrePage").equals("RL"))
                        {
                            response.sendRedirect("RejectedStudents.jsp?AV=1");
                        }
                    else
                        {
                            response.sendRedirect("AppliedStudents.jsp?AV=1");
                        }
                    }
                }
                }else{ remarksError = "You can't approve this Student, the Student is not paid his/her Course fees ";}
            }
            else if (request.getParameter("Rejected") != null && request.getParameter("Rejected").equals("Reject"))
            {
                String Remarks = request.getParameter("Remarks");
                if(Remarks != null)
                    Remarks.trim();
                if (Remarks != null && !Remarks.equals(""))
                {
                    out.print(Remarks);
                    int status = -1;
                    if (verification.RejectApplication(request, status))
                    {

                        if(request.getParameter("PrePage")!=null)
                    {
                    if(request.getParameter("PrePage").equals("PRN"))
                        {
                            response.sendRedirect("ApprovedStudents.jsp?AV=1");
                        }
                    else if(request.getParameter("PrePage").equals("AL"))
                        {
                            response.sendRedirect("AppliedStudents.jsp?AV=1");
                        }
                    else if(request.getParameter("PrePage").equals("RL"))
                        {
                            response.sendRedirect("RejectedStudents.jsp?AV=1");
                        }
                    else
                        {
                            response.sendRedirect("AppliedStudents.jsp?AV=1");
                        }

                    }
                    }
                }
                else
                {
                    remarksError = "Enter your comments for rejecting this application";
                }
            }
            if(request.getParameter("Home")!=null && request.getParameter("Home").equals("1") )
                {
                if(request.getParameter("PrePage")!=null)
                    {
                    if(request.getParameter("PrePage").equals("PRN"))
                        {
                            response.sendRedirect("ApprovedStudents.jsp?AV=1");
                        }
                    else if(request.getParameter("PrePage").equals("AL"))
                        {
                            response.sendRedirect("AppliedStudents.jsp?AV=1");
                        }
                    else if(request.getParameter("PrePage").equals("RL"))
                        {
                            response.sendRedirect("RejectedStudents.jsp?AV=1");
                        }
                    else
                        {
                        response.sendRedirect("AppliedStudents.jsp?AV=1");
                        }
                    }
                }

            model.Registration PersonalDetails = null;
            PersonalDetails = new model.Registration();
            PersonalData = PersonalDetails.getPersonalDetails(sid, response);
            QualificationDetails qualificationDetails = null;
            qualificationDetails = new QualificationDetails(request, response);
            QualificationData = qualificationDetails.getQualificationDetails(sid);
            %>
<html><head>
		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                 <script type="text/javascript" src="./js/MarkEntry.js"></script>
                 <script type="text/javascript" src="./js/Absentee.js"></script>
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
<jsp:include page="Header_SO.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">

        <jsp:include page="Navigation_ExamSO.jsp"/>

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


            <form name="StudentVerification" id="StudentVerification" action="StudentVerification.jsp" method="post"  style="font-size: 13px">
                                    <input type="hidden" name="Final" value="Yes"/>

                                     <%if(request.getParameter("PrePage")!=null &&(!request.getParameter("PrePage").isEmpty()))
                  {%>
        <input type="hidden" name="PrePage" id="PrePage" value=<%=request.getParameter("PrePage")%>>
        <%}%>
                                        <div style="width:95%;color:#333333">

                                        <div id="myApplDiv" style="color:#333333">
                                            <fieldset>
                                                <legend style="color:#FF0000; font-size:14px; font-family:Georgia, 'Times New Roman', Times, serif; font-weight:bold">
                                                    <b>Applicant's Edit Profile(<a href="StudentVerification.jsp?Home=1&PrePage=<%= request.getParameter("PrePage")%>"> <font color="green">Back to Home</font> </a> )</b>	</legend>
                                                <!-- Reg Form 1 Start-->
                                                    <fieldset>

                                                    <legend >Personal Details    </legend>
                                                    <!-- Row Div 1 Open -->
                                                    <div id="div_row1" style="width:100%">
                                                        <!-- Photo Div Open -->
                                                        <div id="div_photo" style="float:left; width:30%">
                                                            <fieldset style="border:1px #CCCCCC ridge">
                                                                <legend>Profile Photo</legend>
                                                                <div style="height:126px" align="center">
                                                                        <div style="border:1px solid grey; padding:3px;width:115px; height:125px" id="photo_area">

                                                                        <img src="http://10.33.1.40:8080/images/<%=PersonalData.get("Image")%>"  width="115px" height="125px" />

                                                                    </div>

                                                                </div>
                                                            </fieldset>
                                                        </div>

                                                        <div id="div_personal_info"  >
                                                            <fieldset style="border:1px #CCCCCC ridge"><legend >Abstract Details</legend>
                                                                <table width="100%" align="center" style="font-size: 13px">
                                                                        <tr>

                                                                        <td width="26%">Name</td>
                                                                        <td width="5%"> : - </td>
                                                                        <td><%=PersonalData.get("Name")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Sex</td>
                                                                        <td> : - </td>

                                                                        <td><%=PersonalData.get("Sex")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Date of Birth</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("DOB")%></td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td>Nationality</td>
                                                                        <td> : - </td>
                                                                            <td><%=PersonalData.get("Nationality")%>

                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Religion</td>

                                                                        <td> : - </td>
                                                                        <td><%=PersonalDetails.getReligion(PersonalData.get("Religion"))%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Caste</td>
                                                                        <td> : - </td>

                                                                        <td>
                                                                            <%=PersonalDetails.getCaste(PersonalData.get("Caste"))%>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Contact Number</td>
                                                                        <td> : - </td>
                                                                        <td> <%=PersonalData.get("PhoneNo")%></td>
                                                                    </tr>
                                                                </table>

                                                            </fieldset>
                                                        </div>
                                                        <!-- Personal Div Closed  -->
                                                    </div>
                                                    <!-- Row Div 1 Closed -->

                                                    <!-- Row Div 2 Opened -->
                                                    <div id="div_row2" 	 >
                                                        <!-- Present Address Div Opend -->
                                                        <div id="div_p_address" style="float:left; width:50%">
                                                                <fieldset style="border:1px #CCCCCC ridge">

                                                                <legend >Permanent Address</legend>
                                                                <table width="90%" style="font-size: 13px">
                                                                    <tr>
                                                                        <td>HouseName/No</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("HouseName")%></td>

                                                                    </tr>
                                                                    <tr>
                                                                        <td>Place</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("Place")%></td>
                                                                    </tr>
                                                                        <tr>

                                                                        <td>State</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("State")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>City/District</td>

                                                                        <td> : - </td>
                                                                            <td><%=PersonalData.get("City")%>

                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Pincode</td>
                                                                        <td> : - </td>

                                                                        <td><%=PersonalData.get("Pincode")%></td>
                                                                    </tr>
                                                                </table>
                                                            </fieldset>
                                                        </div>
                                                        <!-- Present Address Div Closed -->
                                                        <!-- Communication Address Opend -->
                                                        <div id="div_c_address" >
                                                            <fieldset style="border:1px #CCCCCC ridge">
                                                                <legend >Communication Address</legend>

                                                                <table width="90%" style="font-size: 13px">
                                                                    <tr>
                                                                        <td>HouseName/No</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("CHouseName")%></td>
                                                                    </tr>
                                                                        <tr>

                                                                        <td>Place</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("CPlace")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>State</td>

                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("CState")%>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>City/District</td>
                                                                        <td> : - </td>

                                                                        <td>
                                                                            <%=PersonalData.get("CCity")%>								  </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Pincode</td>
                                                                        <td> : - </td>
                                                                        <td><%=PersonalData.get("CPincode")%></td>

                                                                    </tr>
                                                                </table>
                                                            </fieldset>
                                                        </div>
                                                        <!-- Communication Address Closed -->
                                                    </div>
                                                    <!-- Row Div 2 Closed -->
                                                </fieldset>
                                                <!-- Reg Form 1 End-->
                                               <!-- Reg Form 3 Start-->
                                                <fieldset>
                                                    <legend>Qualifications   </legend>
                                                    <div id="div_row6">
                                                        <fieldset style="border:1px #CCCCCC ridge">
                                                            <table width="100%" align="center" style="font-size: 12px" >
                                                                <thead>
                                                                    <tr  bgcolor="d6d2a1" >
                                                                        <th style="font-size: small"><div align="center">Name of Degrees</div></th>
                                                                        <th style="font-size: small"><div align="center">Name of Board/University </div></th>
                                                                        <th style="font-size: small"><div align="center">Name of School/College</div></th>
                                                                        <th style="font-size: small"><div align="center">Subject</div></th>
                                                                        <th style="font-size: small"><div align="center">Year</div></th>
                                                                        <th style="font-size: small"><div align="center">Registration No</div></th>
                                                                        <th style="font-size: small"><div align="center">Total Marks</div></th>
                                                                        <th style="font-size: small"><div align="center">Out of</div></th>
                                                                    </tr>

                                                                </thead>
                                                                <tbody>
                                                                    <tr bgcolor="#FFFFFFF">
                                                                        <td>10th</td>
                                                                        <td><%=QualificationData.get("sec_board")%></td>
                                                                        <td><%=QualificationData.get("sec_school")%></td>
                                                                        <td><%=QualificationData.get("sec_subject")%></td>
                                                                        <td align="center"><%=QualificationData.get("sec_passdate")%></td>
                                                                        <td align="center"><%=QualificationData.get("sec_regstno")%></td>
                                                                        <td align="center"><%=QualificationData.get("sec_marks")%></td>
                                                                        <td align="center"><%=QualificationData.get("sec_outof")%></td>
                                                                    </tr>

                                                                    <tr bgcolor="#FFFFFF">
                                                                        <td>12th</td>
                                                                        <td><%=QualificationData.get("hisec_board")%></td>
                                                                        <td><%=QualificationData.get("hisec_school")%></td>
                                                                        <td><%=QualificationData.get("hisec_subject")%></td>
                                                                        <td align="center"><%=QualificationData.get("hisec_passdate")%></td>
                                                                        <td align="center"><%=QualificationData.get("hisec_regstno")%></td>
                                                                        <td align="center"><%=QualificationData.get("hisec_marks")%></td>
                                                                        <td align="center"><%=QualificationData.get("hisec_outof")%></td>
                                                                    </tr>

                                                                    <% if (QualificationData.get("degree_university") != null) {%>

                                                                    <tr bgcolor="#FFFFFF">
                                                                        <td>Graduation</td>
                                                                        <td><%=QualificationData.get("degree_university")%></td>
                                                                        <td><%=QualificationData.get("degree_college")%></td>
                                                                        <td><%=QualificationData.get("degree_subject")%></td>
                                                                        <td align="center"><%=QualificationData.get("degree_passdate")%></td>
                                                                        <td align="center"><%=QualificationData.get("degree_regstno")%></td>
                                                                        <td align="center"><%=QualificationData.get("degree_marks")%></td>
                                                                        <td align="center"><%=QualificationData.get("degree_outof")%></td>
                                                                    </tr><%}%>


                                                                </tbody>
                                                            </table>

                                                        </fieldset>
                                                    </div>

                                                </fieldset>


                                                <fieldset >
                                                    <legend>Course & Center Details</legend>
                                                    <!-- Row Div 7 Opened -->
                                                        <div id="div_row7">

                                                        <div style="width:100%">
                                                                <fieldset style="border:1px #CCCCCC ridge">

                                                                    <table width="100%" border="0" style="font-size: 13px" >

                                                                    <tr>
                                                                        <td  width="50%">Course Applied</td>
                                                                        <td	width="5%"> : - </td>
                                                                        <td width="45%"><%if (QualificationData.get("BranchId") != null) {
                                                                                            out.print(qualificationDetails.getCourse(QualificationData.get("BranchId")));
                                                                                        }%></td>

                                                                    </tr>

                                                                    <tr>
                                                                        <td align="left">Names of the proposed Centers</td>

                                                                        <td >: - </td>
                                                                        <td><%if (QualificationData.get("CenterId") != null) {
                                                                                        if (QualificationData.get("CenterId").equals("-1")) {
                                                                                            out.print("Not Provided");

                                                                                        } else {
                                                                                            out.print(qualificationDetails.getCenter(QualificationData.get("CenterId")));
                                                                                        }
                                                                                    }%>

                                                                        </td>
                                                                    </tr>

                                                                </table>
                                                            </fieldset>
                                                        </div>
                                                    </div>

                                                    <!-- Row Div 7 Opened -->
                                                </fieldset>
                                                <!-- Reg Form 4 Ended-->

                                                <!-- Documents-->

                                                <fieldset>

                                                        <fieldset>
                                                        <div id="Documents">
                                                    <legend>Documents</legend>
                                                    <table width="100%" border="0" style="font-size: 13px" >
                                                        <% Document doc = new Document();
                                                                    ArrayList list = doc.getDocuments(sid);

                                                                    Iterator<String> itr = list.iterator();
                                                                    while (itr.hasNext()) {%>
                                                        <tr><td><%=itr.next()%></td></tr>
                                                        <%}%>
                                                        <tr>
                                                            <% if(document.getStudentDocumentMessage(i)!=null){%>
                                                            <td>
                                                        <span  class="Error" style="color:red"><%=document.getStudentDocumentMessage(i)%> </span></td>
                                                        <% }%>
                                                        </tr>

                                                    </table>
                                                        </div>
                                                        </fieldset>
                                                        <fieldset>
                                                            <legend>Course Fee</legend>
                                                            <%if(fee.IsFeePaid()){%>
                                                        <div id="CourseFee">
                                                            <table style="width:60%" align="left">
                                                                <tr bgcolor="d6d2a1">
                                                                    <th style="font-size: small"><div align="center">Year</div></th>
                                                                    <th style="font-size: small"><div align="center">DDNo</div></th>
                                                                    <th style="font-size: small"><div align="center">DDDate </div></th>
                                                                    <th style="font-size: small"><div align="center">Amount</div></th>
                                                                </tr>
                                                                <%
                                                                ArrayList<DD> StuDD=new ArrayList<DD>();
                                                                StuDD=dd.getDDForStudent(sid);
                                                                int Count=StuDD.size();
                                                                i=1;
                                                                %>

                                                                    <%
                                                                    if(Count>0)
                                                                        {
                                                                while(i<=Count)
                                                                        {
                                                                    DD FeeDD=StuDD.get(i-1);
                                                                %>
                                                                <tr bgcolor="#FFFFFFF">
                                                                    <td style="font-size: small" align="center"><%=i %></td>
                                                                    <td style="font-size: small" align="center"><%=FeeDD.getDDNumber() %></td>
                                                                    <td style="font-size: small" align="center"><%=FeeDD.getDDDate() %></td>
                                                                    <td style="font-size: small" align="right"><%=FeeDD.getAmountType()+" "+FeeDD.getAmount() %></td>
                                                                </tr>
                                                                <% i++;}%>
                                                                <%--<tr><td>DD No</td>
                                                                    <td><%=fee.getDDNo()%></td>
                                                                </tr>
                                                                <tr><td>DD Date</td>
                                                                    <td><%=fee.getDDDate() %></td>
                                                                </tr>
                                                                <tr><td>Amount</td>
                                                                    <td><%=fee.getDDAmount() %> <%if(fee.getAmountType().equals("INR")){out.print(" Rs");}else{out.print("$");}%> </td></tr> --%>
                                                            </table>
                                                        </div>
                                                        <%}}else{%>
                                                        <div id="CourseFee">
                                                            <table style="font-size: 13px">
                                                                <tr>
                                                                    <td colspan="">  <span  class="Error" style="color:red">Course Fee Not Paid  </span></td>
                                                                </tr>

                                                            </table>
                                                        </div>

                                                        <%}%>
                                                        </fieldset>

                                                </fieldset>
                                            <!-- Documents -->
                                              <fieldset>
                                                    <legend>Migration Details</legend>
                                                        <div id="div_row9">

                                                        <div style="width:100%">
                                                                <fieldset style="border:1px #CCCCCC ridge">

                                                                    <table width="100%" border="0" style="font-size: 13px" >

                                                                    <tr><td>Migration</td>
                                                                        <td><textarea name="Migration" rows="2" cols="80" ><%=verification.getMigration(sid)%></textarea>
                                                                        </td>

                                                                    </tr>

                                                                </table></fieldset></div></div>
                                                    <!-- Row Div 9 Opened -->

                                                </fieldset>
                                                <fieldset>
                                                    <legend>Remarks</legend>
                                                        <div id="div_row9">

                                                        <div style="width:100%">
                                                                <fieldset style="border:1px #CCCCCC ridge">

                                                                    <table width="100%" border="0" style="font-size: 13px">

                                                                    <tr><td>Remarks</td>
                                                                        <td><textarea name="Remarks" rows="2" cols="80" ><%=verification.getRemarks(sid)%></textarea>
                                                                        </td>

                                                                    </tr>
                                                                    <% if (remarksError != null) {%>
                                                                    <tr>
                                                                        <td></td>
                                                                        <td colspan="">  <span  class="Error" style="color:red">*<%=remarksError%>  </span></td>
                                                                    </tr><%}%>
                                                                </table></fieldset></div></div>
                                                    <!-- Row Div 9 Opened -->

                                                </fieldset>

                                                <div align="center" style="padding-top:10px;">
                                                    <table style="font-size: 13px">
                                                        <tr>
                                                            <%if(isApproved){%>
                                                            <td><input type="submit" id="btn_verify" name="Verified" value="Verify the Application"  class="schandbtn"/></td>



                                                            <td><input type="submit" id="btn_reject" name="Rejected" value="Reject"  class="schandbtn"/></td>
                                                             <%}%>
                                                        </tr>
                                                        <!--<tr><td align="right">
                                                                <a href="/DEMS_ADMIN/CentrePrint" target="_blank" style="font-size:22px" >Print Form</a></td></tr>-->
                                                    </table>
                                                </div>
                                            </fieldset>
                                        </div>
                                    </div>
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


