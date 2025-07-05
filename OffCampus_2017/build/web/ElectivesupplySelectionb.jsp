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
            StudentOptionSelection Option=new StudentOptionSelection(request, response);
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
             ArrayList<SubjectBranch>  regstdOptional=new ArrayList<SubjectBranch>() ;
             regstdOptional=supplyList.getRegisteredOptionalPapers(sid,branchId,con,year) ;

    %>
<html><head>
        <script >
 function ComboChecking()
{
    var subBranch;
      var optionalSub;
    try{    subBranch =document.ElectivesupplySelection.SubBranch.value;}catch(e){subBranch=-2}
    try{ optionalSub=document.ElectivesupplySelection.Optional.value;}catch(e){optionalSub=-2}


   if(optionalSub=="-1")
            {
                alert("Select Option");
                document.ElectivesupplySelection.Optional.focus();
                return false;
            }
            else
                {
                    return confirm("Are you sure you want to Edit the Optional/Supply/Improvement Papers?");
                }
return true;
return true;

}
        </script>
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
            <form id="ElectivesupplySelection" name="ElectivesupplySelection" action="ElectivesupplySelection.jsp" method="post">
                <input type="hidden" name="BranchId" id="BranchId" value="<%=branchId %>">
                 <input type="hidden" name="StudentId" id="StudentId" value="<%=sid %>">
                <% if (Option.getMessage() != null) {%>
                        <center>
                        <table class="textblack"><tr>
                            <td></td>
                            <td colspan="">  <span  class="Error" style="color:red">*<%=Option.getMessage()%>  </span></td>
                            </tr><br></table> </center><% }%>
                                <fieldset style="width:90%"><legend class="textblack"><b>Option Selection <a href="Examregistration.jsp?search_button=ok" style="cursor:pointer;color: green">[Back]</a></b></legend>
                                    <table width="100%"   cellspacing="0" cellpadding="3" class="textblack">
                                        <tr>
                                            
                                            <td>
                                                   <div id="div_personal_info"  >
                                                    <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Abstract Details</b></legend>
                                                        <table width="100%" align="center">
                                                            <tr>
                                                                <td width="26%" class="textblack">Name</td>
                                                                <td width="5%"> : - </td>
                                                                <td class="textblack"><%=PersonalData.get("Name")%></td>
                                                            </tr>
                                                            <tr>
                                                                <td width="26%" class="textblack">PRN</td>
                                                                <td width="5%"> : - </td>
                                                                <td class="textblack"><%=PersonalData.get("PRN")%></td>
                                                            </tr>
                                                            <tr>
                                                                <td  width="26%" class="textblack">Course</td>
                                                                <td	width="5%"> : - </td>
                                                                <td  class="textblack"><%if(QualificationData.get("BranchId")!=null)
                                                                {out.print(qualificationDetails.getCourse(QualificationData.get("BranchId")));}%></td>

                                                            </tr>
                                                            <tr>
                                                                <td width="26%" class="textblack">Attending Year</td>
                                                                <td width="5%"> : - </td>
                                                                <td class="textblack"><%=PersonalData.get("AttendingYear")%></td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textblack">Contact Number</td>
                                                                <td> : - </td>
                                                                <td class="textblack"> <%=PersonalData.get("PhoneNo")%></td>
                                                            </tr>
                                                        </table>

                                                    </fieldset>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                           <div id="OptionSelection">

                                          
                                              
                                               <%if(branchId==21|| branchId==17 || branchId==26 || branchId== 18){

                                                %>  <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Option/Elective Selection</b></legend>
                                                <table>
                                                    <tr>
                                                        <td class="textblack">
                                                            Select Stream
                                                        </td>
                                                        <td>
                                                            <select name="SubBranch" id="SubBranch" style="width: 300px">
                                                                <option value="-1">...Select...</option>
                                                                <%
                                                                model.PRNListforCentres List=new model.PRNListforCentres();
                                                                int Count;i=0;
                                                                ArrayList<model.CourseData> StreamList=List.getSubBranch(QualificationData.get("BranchId"));
                                                                Count=StreamList.size();
                                                                while(i<Count)
                                                                   {
                                                                    boolean selected=false;
                                                                    model.CourseData Stream=StreamList.get(i);
                                                                    for(int k=0;k< regstdOptional.size();k++)
                                                                        {

                                                                       if(Stream.BranchId!=0 && Stream.BranchId==regstdOptional.get(k).getSubBranchId()){
                                                                        selected=true;
                                                                            }}
                                                                %>
                                                                <option value="<%=Stream.BranchId %>" <%if(selected){%>selected="selected"<%}%>><%=Stream.BranchName %></option>
                                                                <% i++;}%>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                   
                                                </table></fieldset><%}else{%>
                                                <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Option/Elective Selection</b></legend>
                                                <table>
                                                    
                                                            
                                                                <%
                                                                model.BranchList List=new model.BranchList();
                                                                ArrayList<ArrayList<SubjectBranch>>  branchOptionals=new ArrayList<ArrayList<SubjectBranch>>() ;
                                                                
                                                                branchOptionals=List.getOptionalForBranch(branchId,year);
                                                                int Count=0;i=0;
                                                                Count=branchOptionals.size();
                                                                if(Count==1&&branchOptionals.get(0).isEmpty())
                                                                    {
                                                                        Count=0;
                                                                     }
                                                                while(i<Count)       {%>
                                                       <tr>
                                                        <td class="textblack">Select Optional Subject</td>
                                                        <td>
                                                          
                                                                    <select name="Optional" id="Optional" style="width: 230px">
                                                                    <option value="-1">...Select...</option>
                                                                <%
                                                                   

                                                                    ArrayList<SubjectBranch> subjectBranchs=new ArrayList<SubjectBranch>();
                                                                    subjectBranchs=branchOptionals.get(i)  ;
                                                                    for(int j=0;j<subjectBranchs.size();j++){
                                                                         boolean selected=false;
                                                                        for(int k=0;k< regstdOptional.size();k++)
                                                                        {
                                                                          
                                                                       if(subjectBranchs.get(j).getSubjectBranchId()!=0 && subjectBranchs.get(j).getSubjectBranchId()==regstdOptional.get(k).getSubjectBranchId()){
                                                                        selected=true;
                                                                            }}
                                                                %>
                                                                <option value="<%=subjectBranchs.get(j).getSubjectBranchId() %>" <% if(selected){%> selected="selected" <%}%> ><%=subjectBranchs.get(j).getSubject().getSubjectName() %></option>
                                                                <% }%></select></td>
                                                    </tr><%i++; }%>
                                                            
                                                       
                                                    
                                                </table>
                                                </fieldset><%}%>
                                               
                                           
                                            </div>
                                            </td>
                                        </tr>

                                         <tr>
                                            <td colspan="2">
                                           <div id="OptionSelection">
                                               <%

                                                                ArrayList<SubjectBranch>  branchSupplys=new ArrayList<SubjectBranch>() ;
                                                                 branchSupplys=supplyList.getSupplyOrImprovementPapers(sid, branchId,joinYear,attendingYear);
                                                                 ArrayList<SubjectBranch>  regstdSupplys=new ArrayList<SubjectBranch>() ;
                                                                 regstdSupplys=supplyList.getRegisteredSupplyOrImprovementPapers(sid,branchId,con,year) ;
                                                               if(!branchSupplys.isEmpty()){ %>
                                            <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Select supplementary/Re Examination/Improvement Papers</b></legend>


                                                <table>                                                

                                                                                                                          
                                                                    
                                                                <%boolean checked=false;
                                                                    
                                                                    for(int j=0;j< branchSupplys.size();j++){
                                                                        
                                                                        SubjectBranch subjBranch=new SubjectBranch();
                                                                        subjBranch=branchSupplys.get(j)  ;
                                                                         checked=false;
                                                                        for(int k=0;k< regstdSupplys.size();k++)
                                                                        {
                                                                         
                                                                       if(subjBranch.getSubjectBranchId()!=0 && subjBranch.getSubjectBranchId()==regstdSupplys.get(k).getSubjectBranchId()){
                                                                        checked=true;
                                                                            }}
                                                                %>
                                                    <tr><td>
                                                            <input type="checkbox" name="supplyList" id="supplyList" value="<%=subjBranch.getSubjectBranchId() %>" style="width: 230px" <%if(checked){%> checked="checked"<%}%>></td>
                                                                    
                                                        <td style="text-transform: uppercase"><%=subjBranch.getSubject().getSubjectName() %></td>
                                                    </tr>
                                                                <% }%>

                                                        
                                                    
                                                </table>
                                                                
                                                                <br>
                                                
                                                 <div align="left" class="textblack" color="red">
                                                     <font style="font-size: 14px;"color="#19610f">NB: The Subjects listed  here are Supplementary/Improvement Papers of the Student. Improvement option is available only for B Com Students</font>
             </div><br>

                                            </fieldset><%}%>
                                            </div>
                                            </td>
                                        </tr>
                                        <tr>
                                                        <td colspan="2" align="center">
                                                            <input  name="SubmitOption" id="SubmitOption" type="submit" value="Submit" onclick="return ComboChecking()">
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