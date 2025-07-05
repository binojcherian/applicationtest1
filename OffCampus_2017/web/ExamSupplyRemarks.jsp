<%--
    Document   : OptionalStreamSelection
    Created on : Jan 4, 2012, 2:06:11 PM
    Author     : Aseena
--%>

<%@page import="model.CourseData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="model.ExamRegistration"%>
<%@page import="model.DBConnection"%>
<%@page import="model.mStudentList"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="Controller.*"%>
<%@page import="Entity.*"%>
<% if(request.getSession().getAttribute("UserName")==null){
response.sendRedirect(response.encodeRedirectURL("Login.jsp?message=Session Time Out.Login again"));
}else{
    int sid = 0;
    int attendingYear=0;boolean isConfirmed=false;
    int joinYear=0;
    java.util.Hashtable<String, String> PersonalData;
    java.util.Hashtable<String, String> QualificationData;
    if (request.getParameter("StudentId") != null)
            {
                sid = Integer.parseInt(request.getParameter("StudentId"));
                request.getSession().setAttribute("StudentId", sid);
            }
      if (request.getSession().getAttribute("yearApplied") != null){
          String temp = request.getSession().getAttribute("yearApplied").toString();
                attendingYear = Integer.parseInt(temp);

            }
     if (request.getSession().getAttribute("joinYear") != null){
          String temp = request.getSession().getAttribute("joinYear").toString();
                joinYear = Integer.parseInt(temp);

            }

    isConfirmed=new ExamRegistration().isExamRegstrnConfirmedStudent(sid, attendingYear);
   // if(isConfirmed){response.sendRedirect(response.encodeRedirectURL("Examregistration.jsp"));}


            Integer i = (Integer) request.getSession().getAttribute("StudentId");
            sid=i.intValue();
           
            model.Registration PersonalDetails = null;
            PersonalDetails = new model.Registration();
            PersonalData = PersonalDetails.getPersonalDetails(sid, response);
            model.QualificationDetails qualificationDetails = null;
            qualificationDetails = new model.QualificationDetails(request, response);
            QualificationData = qualificationDetails.getQualificationDetails(sid);
            int branchId=Integer.parseInt(QualificationData.get("BranchId"));
            int year=Integer.parseInt(PersonalData.get("AttendingYear"));
             mStudentList supplyList = new mStudentList();
             DBConnection DBcon=new DBConnection();
             Connection con=DBcon.getConnection();
              String remarksError = null;
              String Remarks = request.getParameter("Remarks");
                if(request.getParameter("SubmitOption") != null && request.getParameter("SubmitOption").equals("SubmitRemarks"))
                    {
                    if(Remarks != null){
                    Remarks.trim();      
                    supplyList.UpdateExamRemarks( Remarks,sid);
                response.sendRedirect(response.encodeRedirectURL("SupplyRegistration.jsp?search_button=ok"));
       
                    }
                    }
            

    %>
<html><head>

<link rel="shortcut icon" href="../favicon.ico" type="image/png" />

		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">

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
    <td width="130">


       </td>
    <td width="20" bgcolor="#ffffff"><img src="images/spacer.gif" alt="" height="10" width="20"></td>
    <td width="90%">
      <table width="100%" bgcolor="#f9fbff" border="0" cellpadding="0" cellspacing="0">
      <tbody><tr>
        <td colspan="5" bgcolor="#f9fbff"><img src="images/spacer.gif" alt="" height="10" width="609"></td>
      </tr>
      <tr>
        <td width="16" bgcolor="#f9fbff"><img src="images/spacer.gif" alt="" height="18" width="8"></td>
	<td>
<div class="textblack"></div>	</td>
        <td bgcolor="#f9fbff">&nbsp;
         <%-- Content --%>
        <center>
            <form id="ExamRemarks" name="ExamRemarks" action="ExamSupplyRemarks.jsp" method="post">
                <input type="hidden" name="BranchId" id="BranchId" value="<%=branchId %>">
                 <input type="hidden" name="StudentId" id="StudentId" value="<%=sid %>">
               
                                <fieldset style="width:90%"><legend class="textblack"><b>Remarks <a href="SupplyRegistration.jsp?search_button=ok" style="cursor:pointer;color: green">[Back]</a></b></legend>
                                    <table width="100%"   cellspacing="0" cellpadding="3" class="textblack">

                                        <tr>
                                             <td  ><textarea name="Remarks" id="Remarks" rows="5" cols="100" ><%=supplyList.getExamRemarks(sid)%></textarea>
                                                                        </td>
                                        </tr>

                                        <tr>
                                                        <td colspan="2" align="center">
                                                            <input  name="SubmitOption" id="SubmitOption" type="submit" value="SubmitRemarks" onclick="return confirm('Are you sure you want to update the Remarks?');">
                                                        </td>
                                                    </tr>
                                    </table>
                                </fieldset>


            </form> <% if(con!=null) con.close();%>
        </center>
         <%-- Content --%>
        </td>
      </tr>
    </tbody></table></td>
  </tr>
</tbody></table>
</td></tr>
</tbody></table>
</body></html>
<jsp:include page="footer.jsp"/>
    <% }%>