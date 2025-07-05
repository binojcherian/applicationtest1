<%-- 
    Document   : ExamDDDelete
    Created on : Apr 6, 2012, 11:29:00 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>

<%if (request.getSession().getAttribute("UserName") == null) {
                response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
            }
            boolean PostBack = false;
            boolean isEdit = false;
            boolean valid = false;
            String DDId = null;
            DD dd=null;
            mDD ddModel=new mDD();
            int id =0;
            boolean redirected=false;
            DDId = request.getParameter("DDId");
            if (DDId != null && !DDId.equals("null")) {
                 id = Integer.parseInt(DDId);

               Connection connection=new DBConnection().getConnection();
               dd=ddModel.getExamDDetails(id, connection);
               connection.close();
             

            }

              if ((request.getParameter("isPostBack") != null) && request.getParameter("isPostBack").equals("Yes")) {

              if (ddModel.deleteDD(id,request) )
              {
                   redirected=true;
                              response.sendRedirect("CentreExamFee.jsp?AV=1");
              }
              }
           
if(!redirected){
%>

<html><head>


		<title>DEMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
                <link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
                <script type="text/javascript" src="./js/ExamRegistration.js"></script>
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
<script src="./js/ExamRegistration.js"></script>
<script src="./js/jscal2.js"></script>
<script src="./js/en.js"></script>
<link rel="stylesheet" type="text/css" href="./Style/jscal2.css"/>
<link rel="stylesheet" type="text/css" href="./Style/border-radius.css" />


	    <style type="text/css">
<!--
.style1 {color: #0099CC}
-->
        </style>
	    </head><body>
	<!-- rpm find linux search -->
<table width="90%" align="center" border="0" cellpadding="0" cellspacing="0">
<tbody>
<jsp:include page="Header_Registration.jsp"/>
<tr><td>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr valign="top" align="left">
          <td width="200" bgcolor="#e8dec3">
        <jsp:include page="Navigation_Registration.jsp"/>

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
                 <form name="DeleteDD" id="DeleteDD" method="post" action="ExamDDDelete.jsp">
    <input type="hidden" name="isPostBack" value="Yes"/>
    <input type="hidden" name="operation" value="Update"/>
    <input type="hidden" name="DDId" value="<%=DDId%>"/>
    <div align="left" id="MainDiv"><br><br>

        <table id="AppTable" width="60%" align="left" border="0">
            <tbody>
                <tr>
                    <td colspan="2">
                        <div align="center">
                            <table width="100%" cellpadding="4" >
                                <tr>
                                    <td  class="textblack"align="left">DD No.
                                    <span class="Asterix">*</span></td>
                                    <td class="textblack">

                                        <input tabindex="1" name="DDNo" id="DDNo" value="<%=dd.getDDNo()%>" readonly="true" style="width: 205px;" type="text" maxlength="30" onkeypress="return chkNum(event);"/> </td>


                                </tr>

                                <tr>
                                    <td class="textblack" align="left"style="width:25%">DD Date.
                                    <span class="Asterix">*</span></td>

                                    <td class="textblack">
                                        <div >
                                                <input  tabindex="2" type="text" name="ddDate" id="ddDate"  value="<%=dd.getDDDate() %>"  style="width: 180px;"  readonly="true"/>

                                            <script language="javascript" type="text/javascript">Calendar.setup({
                                                inputField : "ddDate", // id of the input field
                                                ifFormat: "%d-%m-%Y", // format of the input field
                                                button : "btndate", // trigger	for the calendar (button ID)
                                                align : "Bl", // alignment (defaults to "Bl")
                                                singleClick : true });
                                            </script>
                                        </div></td>
                                                        </tr>

                                <tr>
                                    <td class="textblack" style="width:25%"align="left">Name of the Bank
                                    <span class="Asterix">*</span></td>
                                    <td>
                                           <input tabindex="3" name="BankName" id="BankName" readonly="true" value="<%=dd.getBank() %>"  title="Enter Bank Name" style="width: 205px;" type="text" maxlength="100"/></td>


                                </tr>
                                <tr>
                                    <td  class="textblack"style="width:25%"align="left">Name of the Branch
                                    <span class="Asterix">*</span></td>
                                    <td class="textblack">
                                        <input tabindex="4" name="BrName" id="BrName" readonly="true" value="<%= dd.getBranch() %>"  title="Enter Branch Name" style="width: 205px;" type="text" maxlength="100"/>
                                    </td>

                                </tr>
                                <tr>

                                    <td class="textblack" style="width:25%"align="left">Amount
                                    <span class="Asterix">*</span></td>
                                    <td class="textblack">
                                        <input tabindex="5" name="Amount" id="Amount" readonly="true" value="<%=dd.getAmount()%>"  title="Enter Amount" style="width: 205px;" type="text"  onkeypress="return chkNum(event);" maxlength="5"/></td>
                                                                      </tr>
                                <tr>
                <td class="textblack" style="color: red ">
                    <input type="hidden" name="Operation" value="<%=request.getParameter("Operation") %>" >
                    Do You Want to Delete this DD Details
                </td>
                <td> <input type="submit" align="right" value=" Yes"  > &nbsp;<input type="button" align="right"value="No" onclick="redirectToExamRegistration();"></td>
            </tr>
                            </table>

                        </div></td>
                </tr>
            </tbody>
            <tr>

            </tr>
            <tr></tr><tr></tr><tr></tr>
        </table>
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
<jsp:include page="footer.jsp"/><%}%>
