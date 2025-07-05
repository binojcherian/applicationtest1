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
            int examCentre=0;
            examCentre=supplyList.getRegisteredExamCentre(sid, branchId, con) ;
            int centreId=0;
            if(request.getSession().getAttribute("CenterId")!=null){
            // request.getSession().getAttribute("CenterId").toString();
            centreId=Integer.parseInt(request.getSession().getAttribute("CenterId").toString());}
            

    %>
<html><head>
   
  <script >
 function ComboChecking()
{
     var examcentre;
     var supplySub=false;
     var len=0
     var BranchId=0;
     var  year=0;
    try{    examcentre =document.SupplySelection.examCentre.value;
             BranchId=document.getElementById('BranchId').value;
             year=document.getElementById('year').value;
            // alert(year);
     }catch(e)
    
    {examcentre =-2}
    
        if((document.getElementsByName('supplyList').length > 0)){
            len=1
        }
        
                for(var i=0;i<document.getElementsByName('supplyList').length;i++)
               { 
                        if(document.getElementsByName('supplyList')[i].checked){
                        supplySub=true;
                        break;
                        }
                }
    if(examcentre=="-1")
        {
            alert("Select the Exam centre");
            document.SupplySelection.examCentre.focus();
            return false;
        }
        else if(!supplySub && len==1)
        {
              
              
             
                alert("Select the Supplementary Subject");
                //document.SupplySelection.supplyList.focus();
                return false;
                
                
       }else if(BranchId==24 || BranchId==25 ||   BranchId==27 || BranchId==28 || BranchId==29 || BranchId==30 ||BranchId==31 || BranchId==32)
            { 
                
               
                if(BranchId==24){
                  
                   var list1=document.getElementsByName('supplyList');
                 var selectedCount1=0;
                 
                 for (e=0;e<list1.length;e++) {
                     
                   if (list1[e].checked) {
                        var sbm =list1[e].value;
                      
                        if(sbm==803 ||sbm==812 || sbm==958 || sbm==963){
                           
                           strTry="supplyPaperTypeT"+sbm;
                           strpract="supplyPaperTypeP"+sbm;
                           chk=0;
                          
                           if((document.getElementById(strTry)) && (document.getElementById(strTry).checked) ){
                               chk=1;
                           }
                           
                            if((document.getElementById(strpract)) && document.getElementById(strpract).checked){
                                chk=2;
                            }
                            if(chk==0){
                               alert("Please select the Exam Paper Type(Theory /Practical)!");
                               return false;
                           }
                            
                        }
                
                    }}
                }
               //alert(year);
               var sem = ((year))*2;
               
              
               for (var s=sem;s>0;s--){ 
                 // alert(s) ;
                 var list=document.getElementsByName('cbcssImproveList'+s);
                 var cnt=document.getElementById('hidcbcssImproveList'+s).value;
              
                 var selectedCount=0;
                 for (e=0;e<list.length;e++) {
                   if (list[e].checked) {
                  selectedCount=selectedCount+1;
                
                    }}
                //alert(selectedCount+"____"+cnt+":"+list.length);
                if(selectedCount>0 && selectedCount!=cnt){
                
                 alert("You should select all the papers for each semester");
                     return false;
                 }
                 }   
             } else{
                return true;
            }

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
            <form id="SupplySelection" name="SupplySelection" action="SupplySelection.jsp" method="post">
                <input type="hidden" name="BranchId" id="BranchId" value="<%=branchId %>">
                 <input type="hidden" name="StudentId" id="StudentId" value="<%=sid %>">
                 <input type="hidden" name="year" id="year" value="<%=year %>">
                <% if (Option.getMessage() != null) {%>
                        <center>
                        <table class="textblack"><tr>
                            <td></td>
                            <td colspan="">  <span  class="Error" style="color:red">*<%=Option.getMessage()%>  </span></td>
                            </tr><br></table> </center><% }%>
                                <fieldset style="width:90%"><legend class="textblack"><b>Option Selection <a href="SupplyRegistration.jsp?search_button=ok" style="cursor:pointer;color: green">[Back]</a></b></legend>
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
                                              
                                               
                                                <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Examination Centre Details</b></legend>
                                                <table>
                                                    <tr>
                                                        <td>Select the Exam Centre</td><td>

                                         <% ArrayList<CentreDetails> CentreList=supplyList.getCentres();

                                                int m=0;
                                               int count=CentreList.size();
                                                CentreDetails Centre;
                                             %>
              <select name="examCentre" id="examCentre"  style="width: 300px;" title="Select your Exam centre">
    <option value="-1" >--------Select-------</option>
    <%while(m<count){
        Centre =CentreList.get(m);%>
        <option value="<%=Centre.CentreId %>"
                <%if(examCentre!=0 && examCentre==Centre.CentreId){ %> selected="selected"
                
                <%}else if ( examCentre==0 && centreId!=0 &&  centreId==Centre.CentreId){
                    
    %>selected="selected"<%}%> > <%=Centre.CentreName%></option>

         <%        m++;}//while
                                              %>

</select></td>
                                                    </tr>
                                                            
                                                                                                             
                                                       
                                                    
                                                </table>
                                                </fieldset>
                                               
                                           
                                            </div>
                                            </td>
                                        </tr>

                                         <tr>
                                            <td colspan="2">
                                           <div id="OptionSelection">
                                               <%

                                                                ArrayList<SubjectBranch>  branchSupplys=new ArrayList<SubjectBranch>() ;
                                                                ArrayList<SubjectBranch>  semesterImprovements=new ArrayList<SubjectBranch>() ;
                                                               
                                                                 branchSupplys=supplyList.getSupplyOrImprovementPapersForCourseCompleted(sid, branchId);
                                                                 ArrayList<SubjectBranch>  regstdSupplys=new ArrayList<SubjectBranch>() ;
                                                                 regstdSupplys=supplyList.getRegisteredSupplyOrImprovementPapersForCourseComplted(sid,branchId,con) ;
                                                              
                  
                                                    
                                            if(branchId==24|| branchId==25 ||  branchId== 27 || branchId==28 || branchId==29 || branchId==30 ||  branchId== 31 ||  branchId== 32){

                                            int semCompleted= 0;
                                            
                                            semCompleted = (year)*2;
                                            
                                            for (int sem=semCompleted;sem>0;sem--){
                                               semesterImprovements=supplyList.getCBCSSCourseCompleredPapersForImprovemntSemesterwise(sid, branchId,sem); 
                                                
                                              if(!semesterImprovements.isEmpty()){
                                             %> <br/>
                                             <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack" style="background-color: burlywood;"><b> Semester <%=sem%></b></legend>
                                             <table>                                                

                                                
                                                                           
                                                                    
                                                                <%boolean checked=false;
                                                                    int cnt=semesterImprovements.size();
                                                                    for(int j=0;j< cnt;j++){
                                                                        
                                                                        SubjectBranch subjBranch=new SubjectBranch();
                                                                        subjBranch=semesterImprovements.get(j)  ;
                                                                        checked=false;
                                                                       for(int k=0;k< regstdSupplys.size();k++)
                                                                        {
                                                                          if(subjBranch.getSubjectBranchId()!=0 && subjBranch.getSubjectBranchId()==regstdSupplys.get(k).getSubjectBranchId()){      
                                                                         checked=true;
                                                                       
                                                                        }}%>
                                                    <tr><td>
                                                            
                                                            <input type="checkbox" name="cbcssImproveList<%=sem%>" id="cbcssImproveList<%=sem%>" value="<%=subjBranch.getSubjectBranchId() %>" style="width: 230px" <%if(checked){%> checked="checked"<%}%>>
                                                            <input type="hidden" name="hidcbcssImproveList<%=sem%>" id="hidcbcssImproveList<%=sem%>" value="<%=cnt %>" style="width: 230px" >
                                                           
                                                        </td>
                                                                    
                                                        <td style="text-transform: uppercase;" bgcolor="#C1EF7F" ><%=subjBranch.getSubject().getSubjectName() %></td>
                                                    </tr>
                                                                <% }%>

                                                        
                                                    
                                             </table></fieldset><br/><%}}
                                            
                                            branchSupplys=supplyList.getSupplyForCourseCompleted(sid,branchId);
                                            
                                            if(!branchSupplys.isEmpty()){ %>
                                            <fieldset style="border:1px #CCCCCC ridge"><legend class="textblack"><b>Select supplementary/Re Examination/Improvement Papers</b></legend>

                                                

                                                <table>                                                

                                                                                                                          
                                                                    
                                                                <%boolean checked=false;boolean paperTypePchecked=false;boolean paperTypeTchecked=false; 
                                                                    
                                                                    for(int j=0;j< branchSupplys.size();j++){
                                                                        
                                                                        SubjectBranch subjBranch=new SubjectBranch();
                                                                        subjBranch=branchSupplys.get(j)  ;
                                                                         checked=false;paperTypeTchecked=false;paperTypePchecked=false;
                                                                        for(int k=0;k< regstdSupplys.size();k++)
                                                                        {
                                                                         
                                                                            
                                                                        if(regstdSupplys.get(k).getPaperType()!=null && (regstdSupplys.get(k).getPaperType().equals("B") ||regstdSupplys.get(k).getPaperType().equals("T"))){
                                                                            
                                                                            paperTypeTchecked=true;
                                                                           
                                                                        } if(regstdSupplys.get(k).getPaperType()!=null && (regstdSupplys.get(k).getPaperType().equals("B") ||regstdSupplys.get(k).getPaperType().equals("P"))){
                                                                            paperTypePchecked=true;
                                                                        }
                                                                       if(subjBranch.getSubjectBranchId()!=0 && subjBranch.getSubjectBranchId()==regstdSupplys.get(k).getSubjectBranchId()){
                                                                        checked=true;
                                                                            }}
                                                                         
                                                                %>
                                                    <tr><td>
                                                            <input type="checkbox" name="supplyList" id="supplyList" value="<%=subjBranch.getSubjectBranchId() %>" style="width: 230px" <%if(checked){%> checked="checked"<%}%>></td>
                                                                    
                                                        <td style="text-transform: uppercase"><%=subjBranch.getSubject().getSubjectName() %>
                                                        
                                                        <%if(!subjBranch.getSubject().getPaperType().equals("N")){%> <label style="color:red ">  [ <label style="color:#00f ">
                                                    <spam><%  if((subjBranch.getSubject().getPaperType()=="B") || (subjBranch.getSubject().getPaperType()=="T")){ %>
                                                            <input type="checkbox" name="supplyPaperTypeT<%=subjBranch.getSubjectBranchId()%>" id="supplyPaperTypeT<%=subjBranch.getSubjectBranchId() %>" value="T" style="width: 30px" <%if(paperTypeTchecked){%> checked="checked"<%}%> >T
                                                        <%}if((subjBranch.getSubject().getPaperType()=="B")||subjBranch.getSubject().getPaperType()=="P"){ %>
                                                        <input type="checkbox" name="supplyPaperTypeP<%=subjBranch.getSubjectBranchId() %>" id="supplyPaperTypeP<%=subjBranch.getSubjectBranchId() %>" value="P" style="width: 30px" <%if(paperTypePchecked){%> checked="checked"<%}%>> P<%}%>
                                                    </spam> </label>  ] </label><% }%>
                                                        
                                                        
                                                        </td>
                                                    </tr>
                                                                <% }%>

                                                        
                                                    
                                                </table>
                                                                
                                                                <br>
                                                
                                                 <div align="left" class="textblack" color="red">
                                                     <font style="font-size: 14px;"color="#19610f">NB: The Subjects listed  here are Supplementary/Improvement Papers of the Student. Improvement option is available only for B Com Students</font>
             </div><br>

                                            </fieldset> <% }
                                            
                                            } else            
                  
                  
                  
                  
                  
                  
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
                                                            <input  name="SubmitOption" id="SubmitOption" type="submit" value="Register" onclick="return ComboChecking()">
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