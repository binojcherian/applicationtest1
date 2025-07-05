<%-- 
    Document   : CentreExamFee
    Created on : Apr 5, 2012, 6:32:48 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%if (request.getSession().getAttribute("UserName") == null)
{
 response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}
 else{
     CentreExamFee Fee=new CentreExamFee(request, response);
     String Message=null;
    /*String CentreId="54";
    int AttendingYear= Integer.parseInt(request.getSession().getAttribute("yearApplied").toString());
    int BranchId=Integer.parseInt(request.getSession().getAttribute("BranchId").toString());
    int CollegeId=Integer.parseInt(CentreId);
   
   */
   /* if(request.getParameter("confirm")!=null&&request.getParameter("confirm").equals("GO TO CONFIRMATION PAGE"))
    {
        response.sendRedirect("ExamConfirmation.jsp");
    }else{*/
    //if(Fee.isCenterVerificationOver())
       // {
       // if(Fee.IsAutomationFeeDateOver())
        //    {
        //    Message=" Fee Entering Date Closed: Please contact MG University for Further Details";
        //}
        //}
    %>
                <%
                       Connection con = new DBConnection().getConnection();
                       mDD ddModel=new mDD();
                       CourseSelection  cs=new CourseSelection(request, response);
                       //int amount=ddModel.getTotalDDAmountForCurrentExam(BranchId,AttendingYear,Integer.parseInt(CentreId),con);
                       mStudentList sl=new mStudentList();
                       ExamStudentsList StuList=new ExamStudentsList(request,response);
                       //int confirmed=sl.ConfirmedStudentsCount(BranchId,CollegeId, AttendingYear, con);
                       //int Unconfirmed=sl.UnconfirmedStudentsCount(BranchId, CollegeId, AttendingYear, con);
                       //int Registerd=sl.RegisteredStudentsCount(BranchId, CollegeId, AttendingYear, con);
                       mExamFeeNew examFeeNew=new mExamFeeNew();
                      // Amount ExamFeeAmount=examFeeNew.getTotalExamFeeForCentre(CollegeId, BranchId, AttendingYear, con);
                      // float ExamFee=ExamFeeAmount.getAmt();
                       //ArrayList<Student>  students=  sl.UnRegisteredStudents(BranchId,Integer.parseInt(CentreId) ,AttendingYear , con) ;
                       //String col=Fee.getCentreName(CentreId, con);
                       
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
             <form NAME="CentreExamFee" id="CentreExamFee" action="CentreExamFee.jsp" method="post">
                 <b> <font color="#a02a28" size="2px"><u>Verify DD Details</u></font></b>
                 <center>
                        <table  border="0"cellpadding="5%" width="90%">


                            <tr class="textblack">
                                <td align="left" >

                                    <fieldset style="color:black" style="width:95%"><legend >Search </legend>
                                        <table width="100%">
                                            <tr><td class="textblack">
                                        Centre</td><td>

                                         <% ArrayList<CentreDetails> CentreList=sl.getCentres();
                                              if(CentreList==null||CentreList.size()==0)
                                                  {%>
                                                  <span class="Error">No Centres Assigned to you </span>
                                              <%}else{
                                                int i=0;
                                               int count=CentreList.size();
                                                CentreDetails Centre;
                                             %>
              <select name="CentreId" id="CentreId"  style="width: 250px;" title="Select your centre">
    <option value="-1" >--------Select-------</option>
    <%while(i<count){
        Centre =CentreList.get(i);%>
        <option value="<%=Centre.CentreId %>" <%if (StuList.getCenterId()!=null && Integer.parseInt(StuList.getCenterId())==(Centre.CentreId)) {
                                                                                                               out.print("selected");
                                                                                                           }
                                                                                                       %>> <%=Centre.CentreName%></option>

         <%        i++;}//while
                                              }%>

</select></td><td class="textblack">Course</td><td>
                                        <select name="course_applied" id="course_applied"   style="width: 215px;" title="Select your course" >

                                            <option value="-1" >--------Select------- </option>
                                            <%Set CourseList = cs.ListCourseNames();
                                                        for (Object obj : CourseList) {
                                                            Map.Entry<String, String> List = (Map.Entry<String, String>) obj;
                                            %>
                                            <option <%if (StuList.getCourseApplied()!=null && StuList.getCourseApplied().equals(List.getKey())) {
                                                                                                               out.print("selected");
                                                                                                           }
                                                                                                       %>
                                                value="<%=List.getKey()%>"><%=List.getValue()%></option>

                                            <% }
                                            %>
                                        </select></td></tr>
                                            <tr><td class="textblack">

                                        Year/Semester</td><td>

                                        <select name="year_applied" id="year_applied"   style="width: 215px;" title="Select your year" >

                                            <option value="-1" >--------Select------- </option>
                                            <option value="1" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("1")) {%> selected="selected"<%}%> > First year</option>
                                             <option value="2" <%if (StuList.getYearApplied()!=null && StuList.getYearApplied().equals("2")) {%> selected="selected"<%}%>> Second year</option>

                                        </select></td><td>
                                        <input type="submit" name="search_button"id="search_button" value="Search"/></td>
                                            </tr>
                                             <%
                            if(StuList.getError()!=null)
                                { %>
                                <tr>
                                    <td>
                                        <span class="textblack" style="color: red "><%=StuList.getError() %></span>
                                    </td>
                                </tr><%}%>
                                        </table>
                                    </fieldset>
                                </td>

                            </tr>
                            <tr>
                                <td>
                                    <%if(Fee.getSearch()){ %>
                                    <fieldset style="width:97%"><legend ><font color="#e37a0a" size="2px" >Enter DD Details[Exam Fee]</font></legend>
                   
                 <table align="left" border="0" width="100%">

                    <% if(Fee.getisSaved()!=null){ %>
                    <tr>
                        <td colspan="2"><span class="error"><%=Fee.getisSaved()%></span> </td>

                    </tr> <% } %>
                    
                    <tr>
                        <td class="textblack">
                            DD No( DDNo & BranchNo).
                            <span class="Asterix">*</span>
                        </td>
                        <td>
                            <input type="text" tabindex="=1" name="DDNo" id="DDNo" value="<%=Fee.getDDNo()%>" style="width: 215px" maxlength="15" onkeypress="return chkNum(event);">
                        </td>
                        <% if(Fee.getDDNoError()!=null){ %>

                        <td><span class="Asterix"><%=Fee.getDDNoError()%></span> </td>
                     <% } %>


                    </tr>
                    <tr>
                        <td align="left"style="width:25%" class="textblack">DD Date.
                                    <span class="Asterix">*</span></td>

                                    <td>
                                        <div >
                                                <input  tabindex="2" type="text" name="ddDate" id="ddDate"  value="<%=Fee.getDDDate() %>"  style="width: 180px;"  readonly="true"/>
                                                <button type="button"  name="btndate" id="btndate"><img src="./img/iconCalendar.gif"></button>

                                            <script type="text/javascript">
                                                var cal = Calendar.setup({
                                                  onSelect: function(cal) { cal.hide() },
                                                  showTime: false
                                              });
                                              cal.manageFields("btndate", "ddDate", "%d-%m-%Y");
                                              document.getElementById("ddDate").readOnly=true;

                                            </script>

                                        </div></td>
                                        <% if(Fee.getDDDateError()!=null){ %>

                        <td><span class="Asterix"><%=Fee.getDDDateError()%></span> </td>
                     <% } %>
                    </tr>
                    <tr>
                        <td class="textblack">
                            Bank
                            <span class="Asterix">*</span>
                        </td>
                        <td>
                            <input type="text" tabindex="3" name="BankName" id="BankName" value="<%=Fee.getBank() %>" maxlength="40" style="width:215px">
                        </td>
                        <% if(Fee.getBankError()!=null){ %>

                        <td><span class="Asterix"><%=Fee.getBankError()%></span> </td>
                     <% } %>
                    </tr>
                    <tr>
                        <td class="textblack">
                            Branch
                            <span class="Asterix">*</span>
                        </td>
                        <td>
                            <input type="text" tabindex="4" name="BrName" id="BrName" value="<%=Fee.getBranch() %>" maxlength="40" style="width:215px" >
                        </td>
                        <% if(Fee.getBranchError()!=null){ %>

                        <td><span class="Asterix"><%=Fee.getBranchError()%></span> </td>
                     <% } %>
                    </tr>

                    <tr>
                        <td class="textblack">
                            Amount
                            <span class="Asterix">*</span>
                        </td>
                        <td class="textblack" colspan="2">
                            <input type="text" tabindex="4" name="Amount" id="Amount" value="<%=Fee.getAmount() %>" style="width:215px" tabindex="6" onkeypress="return chkNum(event);" maxlength="8">
                            <select name="AmountType" id="AmountType" >
                                <option value="INR">INR</option>
                                <option value="USD">USD</option>
                            </select>
                        </td>
                        <% if(Fee.getAmountError()!=null){ %>

                        <td><span class="Asterix"><%=Fee.getAmountError()%></span> </td>
                     <% } %>
                    </tr>
                    <tr>

                    </tr>
                    <tr>

                        <td colspan="2" align="center">
                            <%-- <input type="Submit" name="Save" id="Save" value="Submit" tabindex="7" onclick="return ValidateCentreExamFee()">--%>
                    <input type="button" name="Cancel" id="Cancel" value="Clear" onclick="ClearAll();">
                        </td>
                    </tr>

                </table>                   
                </fieldset>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <%
                                    //con=new DBConnection().getConnection();
                                     DD ddWithFine=ddModel.getDDWithFine(Integer.parseInt(Fee.getCourseApplied()), Fee.getYearApplied(), Integer.parseInt(Fee.getCenterId()), con);
                                     DD ddWithOutFine=ddModel.getDDWithOutFine(Integer.parseInt(Fee.getCourseApplied()), Fee.getYearApplied(), Integer.parseInt(Fee.getCenterId()), con);
                                    %>
                                    <table width="100%"><tr>
                                    <td>
                            <fieldset width="90%"><legend ><span style="color:#a02a28" class="textblack">DDDetails</span></legend>
                                <table width="90%">
                                    <tr>
                                        <td class="textblack">
                                            Total No of DDs with out Fine:<b><%=ddWithOutFine.Count %></b>&nbsp;&nbsp;&nbsp;&nbsp;

                                            Amounts To:<b><%=ddWithOutFine.getAmount() %></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textblack">
                                            Total No of DDs with Fine:<b><%=ddWithFine.Count %></b> &nbsp;&nbsp;&nbsp;&nbsp;

                                            Amounts To:<b><%=ddWithFine.getAmount() %></b>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                                    </td>
                                </tr></table>
                                </td>
                            </tr>
                             <tr>
                                <td colspan="6" align="center"> <%--<span><input type="submit"  name="submit" value="Approve Selected DDs"  onclick='return confirm("Are you sure you want to Approve  these DDs ?")' />
                                                                                            </span>--%>
                                </td>
                            </tr>
                            <tr>
                                <td>                                    
                    <%
                        //con = new DBConnection().getConnection();
                        ArrayList<DD> dds=ddModel.getDDsForCurrentExam(Integer.parseInt(Fee.getCourseApplied()),Fee.getYearApplied(),Integer.parseInt(Fee.getCenterId()),con);
                      
                      if(dds.size()>0)
                          {
                    %>
                    <table width="90%">
                                <tr align="center">
                                    <td colspan="2" class="textblack"><b> Centre Confirmed DDs : <%=ddModel.getConfirmedDDsCount(Integer.parseInt(Fee.getCourseApplied()), Fee.getYearApplied(), Integer.parseInt(Fee.getCenterId()), con)%>/<%=dds.size() %></b></td>
                                
                                <td colspan="2" class="textblack"><b>Mgu Approved DDs : <%=ddModel.getMguApprovedDDsCount(Integer.parseInt(Fee.getCourseApplied()), Fee.getYearApplied(), Integer.parseInt(Fee.getCenterId()), con)%>/<%=dds.size() %></b></td>
                            </tr>
                            </table>
                        <table  width="100%" border="1" cellspacing="0" cellpadding="3" >
                            <TR  bgcolor="dedede" align="left" >
                        <TH style="font-size: small">Select</TH>
                        <TH style="font-size: small">SLNo</TH>
                        <TH style="font-size: small">DD No</TH>
                        <TH style="font-size: small">Bank Name</TH>
                        <TH style="font-size: small">Branch Name</TH>
                        <TH style="font-size: small">DD Date</TH>
                        <TH style="font-size: small">Amount</TH>
                        <TH style="font-size: small">Status</TH>
                         <%--<TH style="font-size: small">Delete</TH>--%>
                            <tr>
                                 <td colspan="9" class="textblack" align="left"><input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="SelectAllRowsForDD(this.form);"><b>SelectAll</b> </td>

                            </tr>
                     <% int i=0;

                     while(i<dds.size())
                        {
                         String color="";
                         boolean IsCheck=true;
                           DD dd=dds.get(i);
                           if(dd.isConfirmed())
                               {
                                    color="orange";
                               }
                           else
                               {
                               color="blue";
                               }
                           if(dd.isMguApproved())
                               {
                               color="#19610f";
                               IsCheck=false;
                               }
                         %>
                    <tr>
                        <td class="textblack" align="left"><%if(IsCheck){ %><input type="checkbox" name="Select"id="Select" Value="<%=dd.getDDId() %>"><% }else {%> &nbsp; <%}%> </td>
                        <td class="textblack">&nbsp;<%=i+1%></td>
                        <td class="textblack">&nbsp;<%=dd.getDDNo() %></td>
                        <td class="textblack">&nbsp;<%=dd.getBank() %></td>
                        <td class="textblack">&nbsp;<%=dd.getBranch() %></td>
                        <td class="textblack">&nbsp;<%=Fee.convertDatetoddmmyyyy(dd.getDDDate())%></td>
                        <td class="textblack">&nbsp;<%=dd.getAmount()+" "+dd.getAmountType() %></td>
                        <td class="textblack">&nbsp;<font color="<%=color %>"><%=dd.Status %></font></td>
                        
                        <%-- <td class="textblack"><a href="ExamDDDelete.jsp?DDId=<%=dd.getDDId()%>">Delete</a></td>--%>
                        
                        </tr>
                 <% i++;} %>
                    
                        </table><% }else{%>
                        <center>
                            <table>
                                <tr>
                                    <td><span  class="Error" style="color:red">*No records found </span></td>
                                </tr>
                            </table>
                        </center>
                    <%}}%>
                                </td>
                            </tr>
                        </table> </center>
             </form><%if(!con.isClosed()){con.close();} %>
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