<%-- 
    Document   : Examregistration
    Created on : Apr 3, 2012, 2:42:35 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%if(request.getSession().getAttribute("UserName")==null){
 response.sendRedirect(response.encodeRedirectURL("LoginPage.jsp?message=Session Time Out.Login again"));
}else{
            SupplyStudentsList StuList=new SupplyStudentsList(request,response);
            
            String message = null;
            CourseSelection  cs=new CourseSelection(request, response);
            String BranchId=null;int branchId=0;
            if(request.getSession().getAttribute("BranchId")!=null){
            BranchId=request.getSession().getAttribute("BranchId").toString();
             Integer i = Integer.parseInt(BranchId);
            branchId=i.intValue();}
           
            model.mStudentList sList=new model.mStudentList();
            Connection con = new DBConnection().getConnection();
           
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
        <form name="Examregistration"  id="SupplyRegistration" action="SupplyRegistration.jsp" method="post">
             <input type="hidden" name="isPostBack" value="Yes"/>
             <b> <font color="#a02a28" size="2px"><u>Supplementary Registration For Course Completed Students</u></font></b>
                 <center>
                        <table  border="0"cellpadding="5%" width="90%">
                                           
                            <tr class="textblack">
                                <td align="left" >

                                    <fieldset style="font-size: 14px;color:black;border-color: #6c4303" ><legend style="font-size: 14px;font-weight:bold " >Search </legend>
                                        <table width="100%">
                                            <tr><td class="textblack">
                                        Centre</td><td>

                                         <% ArrayList<CentreDetails> CentreList=sList.getCentres();
                                              if(CentreList==null||CentreList.size()==0)
                                                  {%>
                                                  <span class="Error">No Centres Assigned to you </span>
                                              <%}else{
                                                int i=0;
                                               int count=CentreList.size();
                                                CentreDetails Centre;
                                             %>
              <select name="CentreId" id="CentreId"  style="width: 300px;" title="Select your centre">
    <option value="-1" >--------Select-------</option>
    <%while(i<count){
        Centre =CentreList.get(i);%>
        <option value="<%=Centre.CentreId %>" <%if (StuList.getCenterId()!=null && Integer.parseInt(StuList.getCenterId())==(Centre.CentreId)) {
                                                                                                               out.print("selected");
                                                                                                           }
                                                                                                       %>> <%=Centre.CentreName%></option>

         <%        i++;}//while
                                              }%>

</select></td></tr><tr><td class="textblack">Course</td><td>
                                        <select name="course_applied" id="course_applied"   style="width: 300px;" title="Select your course" >

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
                                            <br/>
                                            <tr >
                                                <td>&nbsp;</td><td align="left" >
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
                            
                        </table> </center>


                    <!-- Pagination Starts-->


                    <%if(StuList.getSearch()){
                                //Connection con = new DBConnection().getConnection();
                                int TotalRows=0,TotalAmt=0;
                                ResultSet rsPagination = null;
                                ResultSet rsRowCnt = null;

                                PreparedStatement psPagination = null;
                                PreparedStatement psRowCnt = null;

                               try
                                {
                                    String sqlPagination = "SELECT SQL_CALC_FOUND_ROWS StudentPersonal.*,StudentExamFeeStatus.isConfirmed,StudentExamFeeStatus.IsMguApproved as Approval,StudentExamFeeStatus.Remarks  FROM StudentPersonal LEFT JOIN StudentExamFeeStatus on StudentPersonal.StudentId = StudentExamFeeStatus.StudentId "
                                            + " and  StudentExamFeeStatus.examId=(SELECT max(ExamId) FROM ExamMaster E) where StudentPersonal.CollegeId="+StuList.getCenterId()+"  and  StudentPersonal.isMGUApproved=1 and  StudentPersonal.passedStudents=0 " + StuList.getSearchQueryForCourseCompltd()+" order by PRN";//limiting no of records

                                    psPagination = con.prepareStatement(sqlPagination);
                                  
                                    //psPagination.setInt(1, Integer.parseInt(request.getParameter("AdmissionYear")));
                                    rsPagination = psPagination.executeQuery();
                                }
                                catch (Exception ex)
                                {
                                    out.print(ex);
                                }
                                Boolean notFound = false;
                                if (!rsPagination.next())
                                {
                                    notFound = true;
                                     message = " No Matching Records Found";
                                }
                                String sqlRowCnt = "SELECT FOUND_ROWS() as cnt";

                                psRowCnt = con.prepareStatement(sqlRowCnt);
                                rsRowCnt = psRowCnt.executeQuery();

                                if (rsRowCnt.next()) {
                                    TotalRows = rsRowCnt.getInt("cnt");
                                }
                                 %>

                        <% if (message != null) {%>
                        <center>
                        <table><tr>
                            <td></td>
                            <td colspan="">  <span  class="Error" style="color:red">*<%=message%>  </span></td>
                            </tr><br/></table> </center>
                        <%}else{%>
                        <center>
                           
                            
                            
                            <br>

                        <table BORDER="1" width="95%" cellspacing="0" cellpadding="3" class="textblack" style="border-style: groove">
                            <thead><font size="4" color="#2595CD"></font></thead>
                             <TR bgcolor="#dedede" align="center">
                                <TH style="font-size: small;">Select</TH>
                                 <TH style="font-size: small;">SLNo</TH>
                                 <TH style="font-size: small;">PRN</TH>
                                 <TH style="font-size: small;">Student Name</TH>  
                                  <TH style="font-size: small;">Status</TH>  
                                 <TH style="font-size: small;">Supply Selection</TH>
                                <TH style="font-size: small;">Remarks</TH>

                            </TR>
                           
                            <%

                                        int j = 0; int slno=1;

                                        if (!notFound) {
                                            
                                            do {

                                                j++;

                            %>
                           

                                <%
                               
                                int sid=rsPagination.getInt("StudentId");
                                 boolean isSupply= false,isSupplyRegistered=false;
                                boolean isSlected=false;
                                boolean isConfirmed=false,IsCheck=false;
                                String rcolor = "#bd3d1b";
                                int branch=0;
                                if(BranchId!=null){
                                    branch=Integer.parseInt(BranchId);
                                    isSlected=StuList.getStudentList().contains(new Integer(sid));
                                    isSupply=true; 
                                }
                                
                                
                              /* ArrayList<SubjectBranch> branchs=null;
                               ArrayList<SubjectBranch> branchssupply=null;
                              
                                if(branch==24 || branch==25||  branch==27|| branch==28|| branch==29|| branch==30 || branch==31){
                                 branchs=sList.getSupplyOrImprovementPapersForCourseCompletedCBCSS(sid,branch);
                                 branchssupply=sList.getSupplyForCourseCompleted(sid,branch);
                                }else{
                                 branchs=sList.getSupplyOrImprovementPapersForCourseCompleted(sid,branch); 
                                }
                               
                                String StuStatus=sList.getStudentWithheldDetails(sid);
                               
                                if(branchs!=null&&!branchs.isEmpty())
                                        isSupply=true;
                                if(branchssupply!=null&&!branchssupply.isEmpty())
                                        isSupply=true;
                                */
                                 ArrayList<SubjectBranch>  regstdSupplys=new ArrayList<SubjectBranch>() ;
                                     regstdSupplys=sList.getRegisteredSupplyOrImprovementPapersForCourseComplted(sid,branchId,con) ;
                                     if(regstdSupplys!=null && !regstdSupplys.isEmpty()){
                                         isSupplyRegistered=true;IsCheck=true;}
                               String  str=rsPagination.getString("StudentExamFeeStatus.isConfirmed");
                               String color="",Status="";
                               
                               if(rsPagination.getString("StudentExamFeeStatus.isConfirmed")!=null&&rsPagination.getString("StudentExamFeeStatus.isConfirmed").equals("1"))
                                   {isConfirmed=true;                                  }else{isConfirmed=false;}
                                %>
                                <%
                                if(rsPagination.getString("StudentExamFeeStatus.isConfirmed")==null )
                                    {
                                    Status="Not Registered";
                                    color="#d84937";
                                   IsCheck=true; //comment removed on 10/07/2023
                                    }
                              else if(isSupplyRegistered)
                                    {
                                    Status="Supply Regsitered";
                                    color="blue";
                                     IsCheck=true;
                                    }
                               
                               
                               if(rsPagination.getInt("Approval")==1)
                                   {
                                   color="#19610f";
                                   Status="Mgu Approved";
                                    IsCheck=false;
                                   }
                               
                                            if (rsPagination.getString("StudentExamFeeStatus.Remarks") == null) {
                                                                                                                            rcolor = "#82bd1b";
                                                                                                                        }
                              
                                if(isSupply){

                                    

                                %>
                              <tr>
                                    <td class="textblack" align="left"><%if(IsCheck){ %><input type="checkbox" name="Select"id="Select" Value="<%=rsPagination.getString("StudentId") %>"><% }else{%>&nbsp;<%}%> </td>

                                <td>&nbsp;<%=slno%></td>

                                <td><% if(rsPagination.getString("PRN")!=null){ out.print(rsPagination.getString("PRN"));}else{out.println("Not generated");}%></td>

                                <td><%=rsPagination.getString(2)%></td>
                               
                                <td><font style="color: <%=color %>"><%=Status %></font><%if(Status.isEmpty()){ %>&nbsp;<% }%></td>
                                
                                <td>
                                    <%--
                                    if(Status!=null && !Status.equals("Mgu Approved")){
                                if (StuStatus != null) {--%>
                                    <!-- <span style="color: maroon" ><%--=StuStatus--%></span>-->
                                <%//} else
                                if (isSupplyRegistered) {%>
                                    
                                    <a style="color: <%=color%>" href="SupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>">Edit Supplementary Papers</a>
                                        
                                <% } else {%>
                                    <a href="SupplySelection.jsp?StudentId=<%=rsPagination.getString("StudentId")%>" style="color:<%=color%>">Opt Supplementary Papers</a><%
                                        }
                                 //}else {
                            %> <!--<span style="color: <%=color %>">Data Locked</span>--><%// }%>
                                </td>
                              <td><a  style="color:<%=rcolor%>" href="ExamSupplyRemarks.jsp?StudentId=<%=rsPagination.getString("StudentId")%>"> Update Remarks</a></td>
                              </tr>
                            <%slno++;
                            } } while (rsPagination.next());
                                                                                            }%>

                        
                             <tr>
                       <td colspan="7"class="textblack" align="left"><input type="checkbox" name="SelectAll"id="SelectAll" Value="SelectAll" onclick="SelectAllRows(this.form);"><b>SelectAll</b> </td>
                    </tr>
                    <tr>
                                <td colspan="7" align="center"> <span><input type="submit"  name="submit" value="Approve" onclick='return confirm("Are you sure you want to Approve  all these students for Exam?")' />
                                                                                            </span>
                                </td>
                            </tr>

                        </table></center><%}%>
                                                                                            <%}%> </form>
                        <% if(con!=null){ con.close();}%>
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