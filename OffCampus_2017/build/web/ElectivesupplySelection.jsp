<%--
    Document   : OptionalStreamSelection
    Created on : Jan 4, 2012, 2:06:11 PM
    Author     : Aseena
--%>

<%@page import="Controller.StudentOptionSelection"%>
<%@page import="Entity.SubjectBranch"%>
<%@page import="model.CourseData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="model.ExamRegistration"%>
<%@page import="model.DBConnection"%>
<%@page import="model.mStudentList"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>

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
    //if(isConfirmed){response.sendRedirect(response.encodeRedirectURL("Examregistration.jsp"));}


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
             int examCentre=0;
            examCentre=supplyList.getRegisteredExamCentre(sid, branchId, con) ;
           
             ArrayList<SubjectBranch>  regstdOptional=new ArrayList<SubjectBranch>() ;
             regstdOptional=supplyList.getRegisteredOptionalPapers(sid,branchId,con,year) ;

    %>
<html><head>
        <script >
            
jQuery.fn.preventDoubleSubmit = function() {
    //    alert('asas');;
  jQuery(this).submit(function() {
    if (this.beenSubmitted)
      return false;
    else
      this.beenSubmitted = true;
  });
};
 function ComboChecking()
{ 
    // alert('asa');;
    
      
      var subBranch,BranchId;
      var optionalSub;
    try{    subBranch =document.ElectivesupplySelection.SubBranch.value;}catch(e){subBranch=-2}
     try{    BranchId =document.ElectivesupplySelection.BranchId.value;}catch(e){subBranch=-2}
     
    try{ optionalSub=document.ElectivesupplySelection.Optional.value;}catch(e){optionalSub=-2}

 try{examcentre =document.SupplySelection.examCentre.value;}catch(e){optionalSub=-2}
    if(subBranch=="-1")
        {
            alert("Select the Stream");
            document.ElectivesupplySelection.SubBranch.focus();
            return false;
        }
        else if(optionalSub=="-1")
            {
                alert("Select the Optional Subject");
                document.ElectivesupplySelection.Optional.focus();
                return false;
            }
          if(examcentre=="-1")
        {
            alert("Select the Exam centre");
            document.SupplySelection.examCentre.focus();
            return false;
        }
          //  alert(BranchId);
            if(BranchId==24 || BranchId==25 ||   BranchId==27 || BranchId==28 || BranchId==29 || BranchId==30 ||BranchId==31 || BranchId==32 )
            { 
              
                
                if(BranchId==24){
                  
                   var list1=document.getElementsByName('supplyList');
                 var selectedCount1=0;
                 
                 for (e=0;e<list1.length;e++) {
                   if (list1[e].checked) {
                        var sbm =list1[e].value;
                       // alert(sbm);
                        if(sbm==803 ||sbm==812 || sbm==958 || sbm==963){
                           strTry="supplyPaperTypeT"+sbm;
                           strpract="supplyPaperTypeP"+sbm;
                           chk=0;
                           if( (document.getElementById(strTry))&& (document.getElementById(strTry).checked) ){
                               chk=1;
                           }
             
                            if( (document.getElementById(strpract)) && document.getElementById(strpract).checked){
                                chk=2;
                            }
                            //alert(chk);
                            if(chk==0){
                               alert("Please select the Exam Paper Type(Theory /Practical)!");
                               return false;
                           }
                            
                        }
                
                    }}
                }
                
                
                
               var sem = ((document.ElectivesupplySelection.attendingYear.value)-1)*2;
               
               
               for (var s=sem;s>0;s--){ 
                  
                 var list=document.getElementsByName('ImproveList'+s);
                 var selectedCount=0;
                 for (e=0;e<list.length;e++) {
                   if (list[e].checked) {
                  selectedCount=selectedCount+1;
                
                    }}
                if(selectedCount>2){
                
                 alert("You can select only 2 improvement papers for each semester");
             return false;
         }
               }   }
           return true;
    //document.ElectivesupplySelection.submit();

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
                  <input type="hidden" name="attendingYear" id="attendingYear" value="<%=attendingYear %>">
                 
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

                                          
                                              
                                               <%if(branchId==21|| branchId==17 || (branchId==26 && attendingYear==1) || branchId== 18){

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
                                                                <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Option/Elective Selection</b></legend>
                                                <table>
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
                                                    </tr>
                                                    </table>
                                                </fieldset><%i++; }%>
                                                            
                                                       
                                                    
                                                <%}%>
                                               
                                           
                                            </div>
                                            </td>
                                        </tr>

                                         <tr>
                                            <td colspan="2">
                                           <div id="OptionSelection">
                                               <%

                                                                ArrayList<SubjectBranch>  branchSupplys=new ArrayList<SubjectBranch>() ;
                                                                ArrayList<SubjectBranch>  branchImprovements=new ArrayList<SubjectBranch>() ;
                                                               
                                                                 branchSupplys=supplyList.getSupplyOrImprovementPapers(sid, branchId,joinYear,attendingYear);
                                                                 branchImprovements=supplyList.getImprovementPapers(sid, branchId,attendingYear);
                                                                 
                                                                 ArrayList<SubjectBranch>  regstdSupplys=new ArrayList<SubjectBranch>() ;
                                                                 regstdSupplys=supplyList.getRegisteredSupplyOrImprovementPapers(sid,branchId,con,year) ;
                                                               if(!branchSupplys.isEmpty()){ %>
                                            <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Select supplementary/Re Examination Papers</b></legend>


                                                <table>                                                

                                                                                                                          
                                                                    
                                                                <%
                                                                boolean checked=false; boolean paperTypePchecked=false;boolean paperTypeTchecked=false;
                                                                 
                                                                    
                                                                    for(int j=0;j< branchSupplys.size();j++){
                                                                        
                                                                        SubjectBranch subjBranch=new SubjectBranch();
                                                                        subjBranch=branchSupplys.get(j)  ;
                                                                        /*if (branchId == 7 || branchId == 8) {
                                                                                isPassed = supplyList.isAlredayPassed(subjBranch, sid);
                                                                                    
                                                                            } else if (branchId == 24 || branchId == 25 || branchId == 26 || branchId ==27 ||branchId == 28 || branchId == 29 || branchId == 30 || branchId == 31) {
                                                                            isPassed = supplyList.isAlredayPassedForCBCSS(subjBranch, sid);
                                                                            }*/
                                                                                
                                                                         checked=false;paperTypeTchecked=false;paperTypePchecked=false;
                                                                        for(int k=0;k< regstdSupplys.size();k++)
                                                                        {
                                                                         
                                                                       if(subjBranch.getSubjectBranchId()!=0 && subjBranch.getSubjectBranchId()==regstdSupplys.get(k).getSubjectBranchId()){
                                                                        checked=true;
                                                                        
                                                                        if(regstdSupplys.get(k).getPaperType()!=null && (regstdSupplys.get(k).getPaperType().equals("B") ||regstdSupplys.get(k).getPaperType().equals("T"))){
                                                                            
                                                                            paperTypeTchecked=true;
                                                                        } if(regstdSupplys.get(k).getPaperType()!=null && (regstdSupplys.get(k).getPaperType().equals("B") ||regstdSupplys.get(k).getPaperType().equals("P"))){
                                                                            paperTypePchecked=true;
                                                                        }
                                                                        }
                                                                        //bgcolor="FEC56B"
                                                                       }
                                                                          
                                                                %>
                                                    <tr><td>
                                                            
                                                            <input type="checkbox" name="supplyList" id="supplyList" value="<%=subjBranch.getSubjectBranchId() %>" style="width: 230px" <%if(checked){%> checked="checked"<%}%>></td>
                                                                    
                                                        <td style="text-transform: uppercase;" ><%=subjBranch.getSubject().getSubjectName() %>
                                                            <%if(!subjBranch.getSubject().getPaperType().equals("N")){%> <label style="color:red ">  [ <label style="color:#00f ">
                                                    <spam><%  if((subjBranch.getSubject().getPaperType()=="B") || (subjBranch.getSubject().getPaperType()=="T")){ %>
                                                            <input type="checkbox" name="supplyPaperTypeT<%=subjBranch.getSubjectBranchId() %>" id="supplyPaperTypeT<%=subjBranch.getSubjectBranchId() %>" value="T" style="width: 30px" <%if(paperTypeTchecked){%> checked="checked"<%}%> >T
                                                        <%}if((subjBranch.getSubject().getPaperType()=="B")||subjBranch.getSubject().getPaperType()=="P"){ %>
                                                         <input type="checkbox" name="supplyPaperTypeP<%=subjBranch.getSubjectBranchId() %>" id="supplyPaperTypeP<%=subjBranch.getSubjectBranchId() %>" value="P" style="width: 30px" <%if(paperTypePchecked){%> checked="checked"<%}%>> P<%}%>
                                                    </spam> </label>  ] </label><% }%>
                                                
                                                </td>
                                                    </tr>
                                                                <% }%>

                                                        
                                                    
                                                </table>
                                                                
                                                                <br>
                                                
                                                

                                            </fieldset><%}
                                                                   if(!branchImprovements.isEmpty()){ %><br/>
                                            <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Select Improvement Papers</b></legend>
                                  
                                            <%
                                            if(branchId==24|| branchId==25 ||  branchId== 27 || branchId==28 || branchId==29 || branchId==30 ||  branchId== 31 ||  branchId== 32){

                                            int semCompleted= 0;
                                            semCompleted = (attendingYear-1)*2;
                                            for (int sem=semCompleted;sem>0;sem--){
                                               branchImprovements=supplyList.getCBCSSPassedPapersForImprovemntSemesterwise(sid, branchId,sem); 
                                              if(!branchImprovements.isEmpty()){
                                             %> <br/>
                                             <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack" style="background-color: burlywood;"><b> Semester <%=sem%></b></legend>
                                             <table>                                                

                                                
                                                                           
                                                                    
                                                                <% boolean checked=false;
                                                                    
                                                                    for(int j=0;j< branchImprovements.size();j++){
                                                                        
                                                                        SubjectBranch subjBranch=new SubjectBranch();
                                                                        subjBranch=branchImprovements.get(j)  ;
                                                                       
                                                                                
                                                                         checked=false;
                                                                        for(int k=0;k< regstdSupplys.size();k++)
                                                                        {
                                                                         
                                                                       if(subjBranch.getSubjectBranchId()!=0 && subjBranch.getSubjectBranchId()==regstdSupplys.get(k).getSubjectBranchId()){
                                                                        checked=true;
                                                                        //bgcolor="FEC56B"
                                                                            }}
                                                                %>
                                                    <tr><td>
                                                            
                                                            <input type="checkbox" name="ImproveList<%=sem%>" id="ImproveList<%=sem%>" value="<%=subjBranch.getSubjectBranchId() %>" style="width: 230px" <%if(checked){%> checked="checked"<%}%>></td>
                                                                    
                                                        <td style="text-transform: uppercase;" bgcolor="#C1EF7F" ><%=subjBranch.getSubject().getSubjectName() %>
                                                        
                                                        
                                                        </td>
                                                    </tr>
                                                                <% }%>

                                                        
                                                    
                                             </table></fieldset><br/><%}}}else{
                                            %>
                                            
                                           
                                             <table>                                                

                                                
                                                                           
                                                                    
                                                                <% boolean checked=false;
                                                                    
                                                                    for(int j=0;j< branchImprovements.size();j++){
                                                                        
                                                                        SubjectBranch subjBranch=new SubjectBranch();
                                                                        subjBranch=branchImprovements.get(j)  ;
                                                                       
                                                                                
                                                                         checked=false;
                                                                        for(int k=0;k< regstdSupplys.size();k++)
                                                                        {
                                                                         
                                                                       if(subjBranch.getSubjectBranchId()!=0 && subjBranch.getSubjectBranchId()==regstdSupplys.get(k).getSubjectBranchId()){
                                                                        checked=true;
                                                                        //bgcolor="FEC56B"
                                                                            }}
                                                                %>
                                                    <tr><td>
                                                            
                                                            <input type="checkbox" name="ImproveList" id="ImproveList" value="<%=subjBranch.getSubjectBranchId() %>" style="width: 230px" <%if(checked){%> checked="checked"<%}%>></td>
                                                                    
                                                        <td style="text-transform: uppercase;" bgcolor="#C1EF7F" ><%=subjBranch.getSubject().getSubjectName() %></td>
                                                    </tr>
                                                                <% }%>

                                                        
                                                    
                                             </table>
                                             <%   
                                             }%>
                                            
                                            </fieldset> <%}%>
                                            
                                            
                                            </div>
                                            </td>
                                        </tr>
                                        <tr>
                                                        <td colspan="2" align="center">
                                                           <input  name="SubmitOption" id="SubmitOption" type="Submit" value="Submit" onclick="return ComboChecking()"> 
                                                        </td>
                                                    </tr>
                                                   <%  if(!branchImprovements.isEmpty()){%>
                                                    <tr><td>
                                                    <div align="left" class="textblack" color="red">
                                                <p> <font style="font-size: 14px;color:red">* The Subjects listed  here are Supplementary/Improvement Papers of the Student.</font>
                                                </p> <p><font style="font-size: 14px;color:red"> * The highlighted subjects in green color are improvement papers. Improvement option is available only for B Com and CBCSS courses</font></p>
                                                    </div></td></tr><%} else{%>
                                                    <tr><td>
                                                    <div align="left" class="textblack" color="red">
                                                <p> <font style="font-size: 14px;color:red">* The Subjects listed  here are Supplementary Papers of the Student.</font>
                                                </p> <p><font style="font-size: 14px;color:red"> * Improvement option is available only for B Com and CBCSS courses</font></p>
                                                    </div></td></tr>
                                                    <%}%>
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