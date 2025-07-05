/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function ChangeYearOrSem()
{
    //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
         Ajax.open("POST","/OffCampus/YearOrSemester.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("YearOrSem").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ChangeYearOrSemForMarkEntry()
{
    //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
         Ajax.open("POST","/OffCampus/YearOrSemesterNew.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("YearOrSem").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}


function CalculateInternalForVerification(i)
{
    var Subject;var Mark;
    var Ajax=createXMLHttpRequest();
    if(document.MarkVerificationSO.external.length>1)
        {
            Subject=document.MarkVerificationSO.SubjectBranchId[i].value;
            Mark=document.MarkVerificationSO.external[i].value;
        }
        else
            {
                Subject=document.MarkVerificationSO.SubjectBranchId.value;
                Mark=document.MarkVerificationSO.external.value;
            }
     Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Mark="+ Mark+"&Subject="+ Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    if(document.MarkVerificationSO.external.length>1)
                        {
                            document.MarkVerificationSO.internal[i].readOnly=false;
                            document.MarkVerificationSO.internal[i].value="";
                        }
                   else
                       {
                           document.MarkVerificationSO.internal.readOnly=false;
                           document.MarkVerificationSO.internal.value="";
                       }
                    
                }
               else if(internalMark==-2)
                    {
                        if(document.MarkVerificationSO.external.length>1)
                            {
                                 document.MarkVerificationSO.internal[i].readOnly=true;
                                document.MarkVerificationSO.internal[i].value=0;
                            }
                            else
                                {
                                   document.MarkVerificationSO.internal.readOnly=true;
                                   document.MarkVerificationSO.internal.value=0;
                                }
                       
                    }
            else
                {
                    if(document.MarkVerificationSO.external.length>1)
                            {
                                document.MarkVerificationSO.internal[i].value=internalMark ;
                            }
                            else
                                {
                                    document.MarkVerificationSO.internal.value=internalMark ;
                                }
                }
        }
}

       Ajax.send("");
}


function CalculateInternalForVerificationPostCorrection(i)
{
    var Subject;var Mark;
    var Ajax=createXMLHttpRequest();
    if(document.PostCorrection.external.length>1)
        {
            Subject=document.PostCorrection.SubjectBranchId[i].value;
            Mark=document.PostCorrection.external[i].value;
        }
        else
            {
                Subject=document.PostCorrection.SubjectBranchId.value;
                Mark=document.PostCorrection.external.value;
            }
     Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Mark="+ Mark+"&Subject="+ Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    if(document.PostCorrection.external.length>1)
                        {
                            document.PostCorrection.internal[i].readOnly=false;
                            document.PostCorrection.internal[i].value="";
                        }
                   else
                       {
                           document.PostCorrection.internal.readOnly=false;
                           document.PostCorrection.internal.value="";
                       }

                }
               else if(internalMark==-2)
                    {
                        if(document.PostCorrection.external.length>1)
                            {
                                 document.PostCorrection.internal[i].readOnly=true;
                                document.PostCorrection.internal[i].value=0;
                            }
                            else
                                {
                                   document.PostCorrection.internal.readOnly=true;
                                   document.PostCorrection.internal.value=0;
                                }

                    }
            else
                {
                    if(document.PostCorrection.external.length>1)
                            {
                                document.PostCorrection.internal[i].value=internalMark ;
                            }
                            else
                                {
                                    document.PostCorrection.internal.value=internalMark ;
                                }
                }
        }
}

       Ajax.send("");
}
function CalculateInternalForVerificationRevaluation(i)
{
    var Subject;var Mark;
    var Ajax=createXMLHttpRequest();
    if(document.Revaluation.external.length>1)
        {
            Subject=document.Revaluation.SubjectBranchId[i].value;
            Mark=document.Revaluation.external[i].value;
        }
        else
            {
                Subject=document.Revaluation.SubjectBranchId.value;
                Mark=document.Revaluation.external.value;
            }
     Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Mark="+ Mark+"&Subject="+ Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    if(document.Revaluation.external.length>1)
                        {
                            document.Revaluation.internal[i].readOnly=false;
                            document.Revaluation.internal[i].value="";
                        }
                   else
                       {
                           document.Revaluation.internal.readOnly=false;
                           document.Revaluation.internal.value="";
                       }

                }
               else if(internalMark==-2)
                    {
                        if(document.Revaluation.external.length>1)
                            {
                                 document.Revaluation.internal[i].readOnly=true;
                                document.Revaluation.internal[i].value=0;
                            }
                            else
                                {
                                   document.Revaluation.internal.readOnly=true;
                                   document.Revaluation.internal.value=0;
                                }

                    }
            else
                {
                    if(document.Revaluation.external.length>1)
                            {
                                document.Revaluation.internal[i].value=internalMark ;
                            }
                            else
                                {
                                    document.Revaluation.internal.value=internalMark ;
                                }
                }
        }
}

       Ajax.send("");
}
function CalculateInternalForVerificationPostCorrectionAR(i)
{
    var Subject;var Mark;
    var Ajax=createXMLHttpRequest();
    if(document.PostCorrectionAR.external.length>1)
        {
            Subject=document.PostCorrectionAR.SubjectBranchId[i].value;
            Mark=document.PostCorrectionAR.external[i].value;
        }
        else
            {
                Subject=document.PostCorrectionAR.SubjectBranchId.value;
                Mark=document.PostCorrectionAR.external.value;
            }
     Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Mark="+ Mark+"&Subject="+ Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    if(document.PostCorrectionAR.external.length>1)
                        {
                            document.PostCorrectionAR.internal[i].readOnly=false;
                            document.PostCorrectionAR.internal[i].value="";
                        }
                   else
                       {
                           document.PostCorrectionAR.internal.readOnly=false;
                           document.PostCorrectionAR.internal.value="";
                       }

                }
               else if(internalMark==-2)
                    {
                        if(document.PostCorrectionAR.external.length>1)
                            {
                                 document.PostCorrectionAR.internal[i].readOnly=true;
                                document.PostCorrectionAR.internal[i].value=0;
                            }
                            else
                                {
                                   document.PostCorrectionAR.internal.readOnly=true;
                                   document.PostCorrectionAR.internal.value=0;
                                }

                    }
            else
                {
                    if(document.PostCorrectionAR.external.length>1)
                            {
                                document.PostCorrectionAR.internal[i].value=internalMark ;
                            }
                            else
                                {
                                    document.PostCorrectionAR.internal.value=internalMark ;
                                }
                }
        }
}

       Ajax.send("");
}
function CalculateInternalForVerificationRevaluationAR(i)
{
    var Subject;var Mark;
    var Ajax=createXMLHttpRequest();
    if(document.RevaluationByAR.external.length>1)
        {
            Subject=document.RevaluationByAR.SubjectBranchId[i].value;
            Mark=document.RevaluationByAR.external[i].value;
        }
        else
            {
                Subject=document.RevaluationByAR.SubjectBranchId.value;
                Mark=document.RevaluationByAR.external.value;
            }
     Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Mark="+ Mark+"&Subject="+ Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    if(document.RevaluationByAR.external.length>1)
                        {
                            document.RevaluationByAR.internal[i].readOnly=false;
                            document.RevaluationByAR.internal[i].value="";
                        }
                   else
                       {
                           document.RevaluationByAR.internal.readOnly=false;
                           document.RevaluationByAR.internal.value="";
                       }

                }
               else if(internalMark==-2)
                    {
                        if(document.RevaluationByAR.external.length>1)
                            {
                                 document.RevaluationByAR.internal[i].readOnly=true;
                                document.RevaluationByAR.internal[i].value=0;
                            }
                            else
                                {
                                   document.RevaluationByAR.internal.readOnly=true;
                                   document.RevaluationByAR.internal.value=0;
                                }

                    }
            else
                {
                    if(document.RevaluationByAR.external.length>1)
                            {
                                document.RevaluationByAR.internal[i].value=internalMark ;
                            }
                            else
                                {
                                    document.RevaluationByAR.internal.value=internalMark ;
                                }
                }
        }
}

       Ajax.send("");
}
function CalculateInternalNew()
{

   var Ajax=createXMLHttpRequest();
   var Mark=document.getElementById('Mark').value;;
   var Subject=document.getElementById('Subject').value;;
   var ExamId=document.getElementById('Exam').value;
   var BranchId=document.getElementById('Course').value;
   var Sem=document.getElementById('YearSem').value;
   var PRN=document.getElementById('PRN').value;
   var AcdemicYear=document.getElementById('AcdemicYear').value;
   //alert(PRN);
         Ajax.open("POST","/OffCampus/InternalCalculationNew.jsp?Mark="+ Mark+"&Subject="+ Subject+"&Exam="+ExamId+"&Course="+BranchId+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear+"&PRN="+PRN);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    //document.getElementById("InternalMark").value="";
                    document.getElementById("InternalMark").readOnly=false;
                    document.getElementById('btnEdit').style.visibility='hidden';
                }
               else if(internalMark==-2)
                    {
                        document.getElementById("InternalMark").value=0;
                        document.getElementById("InternalMark").readOnly=true;
                        document.getElementById('btnEdit').style.visibility='hidden';
                    }
            else
                {
                    document.getElementById("InternalMark").value =internalMark ;
                    document.getElementById('btnEdit').style.visibility='visible';
                }
        }
}

       Ajax.send("");

}

function CalculateExternal()
{
 
   var Ajax=createXMLHttpRequest();

   var Subject=document.getElementById('Subject').value;;
   var ExamId=document.getElementById('Exam').value;
   var BranchId=document.getElementById('Course').value;
   var Sem=document.getElementById('YearSem').value;
   var PRN=document.getElementById('PRN').value;
   var AcdemicYear=document.getElementById('AcdemicYear').value;

         Ajax.open("POST","/OffCampus/ExternalCalculation.jsp?Subject="+ Subject+"&Exam="+ExamId+"&Course="+BranchId+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear+"&PRN="+PRN);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var ExternalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(ExternalMark==-3)
                {
                    document.getElementById("Mark").readOnly=true;
                   document.getElementById("Mark").value=0;
                   document.getElementById("InternalMark").readOnly=false;
                }


        }
}

       Ajax.send("");

}
function CalculateInternal()
{
    
 var Ajax=createXMLHttpRequest();
   var Mark=document.MarkEntryForPRN.Mark.value;
   var Subject=document.MarkEntryForPRN.Subject.value;
   //alert(document.MarkEntryForPRN.Subject.value);
         Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Mark="+ Mark+"&Subject="+ Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    //document.getElementById("InternalMark").value="";
                    document.getElementById("InternalMark").readOnly=false;
                    document.getElementById('btnEdit').style.visibility='hidden';
                }
               else if(internalMark==-2)
                    {
                        document.getElementById("InternalMark").value=0;
                        document.getElementById("InternalMark").readOnly=true;
                        document.getElementById('btnEdit').style.visibility='hidden';
                    }
            else
                {
                    document.getElementById("InternalMark").value =internalMark ;
                    document.getElementById('btnEdit').style.visibility='visible';
                }
        }
}

       Ajax.send("");
}

 function EnableEditForInternal()
{
   //alert(document.MarkEntryForPRN.InternalMark.readOnly);
   document.MarkEntryForPRN.InternalMark.readOnly=false;
   document.getElementById("InternalEdit").value="Yes";
    return false;
}
 function EnableEditForPractical()
{
   //alert(document.MarkEntryForPRN.InternalMark.readOnly);
   document.MarkEntryForPRN.PracticalMark.readOnly=false;
   document.getElementById("PracticalEdit").value="Yes";
    return false;
}
function ChangeSubjectList()
{  
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;
  // alert(Course);
         Ajax.open("POST","/OffCampus/ViewSubjectList.jsp?Course="+ Course+"&YearSem="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ViewAllSubjectListForApprovedListSO()
{

 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
   //var Sem=document.MarkEntryForPRN.YearSem.value;

         Ajax.open("POST","/OffCampus/ViewAllSubjectsOfCourse.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ViewAllSubjectListForApprovedListAssistant()
{

 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
   //var Sem=document.MarkEntryForPRN.YearSem.value;

         Ajax.open("POST","/OffCampus/ViewAllSubjectsOfCourse.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function DisplayInternalCalculationDetails()
{
    var Ajax=createXMLHttpRequest();
    var Subject=document.MarkEntryForPRN.Subject.value;
   //alert(document.MarkEntryForPRN.Subject.value);
         Ajax.open("POST","/OffCampus/InternalCalculation.jsp?Subject="+ Subject);
         //alert(Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var internalMark=Ajax.responseText.trim();
            //alert(internalMark);
            if(internalMark==-1)
                {
                    document.getElementById("InternalMark").value="";
                    document.getElementById("InternalMark").readOnly=false;
                    document.getElementById('btnEdit').style.visibility='hidden';
                }
               else if(internalMark==-2)
                    {
                        //document.getElementById("InternalMark").value=0;
                        document.getElementById("InternalMark").readOnly=true;
                        document.getElementById('btnEdit').style.visibility='hidden';
                    }
                    else
                        {
                            document.getElementById('btnEdit').style.visibility='visible';
                            document.getElementById("InternalMark").readOnly=true;
                        }
        }
}

       Ajax.send("");
}
function DisplayPracticalDetails()
{
    var Ajax=createXMLHttpRequest();
    var Subject=document.MarkEntryForPRN.Subject.value;
   //alert(document.MarkEntryForPRN.Subject.value);
         Ajax.open("POST","/OffCampus/PracticalCalculation.jsp?Subject="+ Subject);
         //alert(Subject);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
            var PracticalMark=Ajax.responseText.trim();
           
            if(PracticalMark==-3)
                {
                      ///alert("aaaaaaa");
                    document.getElementById("PracticalMark").value="";
                    document.getElementById("PracticalMark").readOnly=false;
                    document.getElementById('PbtnEdit').style.visibility='hidden';
                }
               else
                    {
                        //document.getElementById("InternalMark").value=0;
                        document.getElementById("PracticalMark").readOnly=true;
                        document.getElementById('PbtnEdit').style.visibility='hidden';
                    }
                    
        }
}

       Ajax.send("");
}
function ViewSubjectTotalCountDetailsNew()
{

    try
    {
     var showalert=document.getElementById("showalert").value;
     if(showalert==1){
     alert( "Send this marks for approval");}
    }catch(Exception )
    {

    }

    
    var Exam=document.getElementById("Exam").value;
    var Subject=document.getElementById("Subject").value;
    var YearSem=document.getElementById("YearSem").value;
    var BranchId=document.getElementById('Course').value;
    var AcdemicYear=document.getElementById('AcdemicYear').value;
    //alert(AcdemicYear);
    var Ajax=createXMLHttpRequest();

         Ajax.open("POST","/OffCampus/SubjectDetailsNew.jsp?Exam="+ Exam+"&Subject="+Subject+"&YearSem="+YearSem+"&Course="+BranchId+"&AcdemicYear="+AcdemicYear);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectCount").innerHTML = Ajax.responseText;
    }
}


       Ajax.send("");
       DisplayInternalCalculationDetails();
     
      
}
function ViewSubjectTotalCountDetails()
{
    
    var Exam=document.getElementById("Exam").value;     
    var Subject=document.getElementById("Subject").value;
    var YearSem=document.getElementById("YearSem").value;
    var Ajax=createXMLHttpRequest();
   
         Ajax.open("POST","/OffCampus/SubjectDetails.jsp?Exam="+ Exam+"&Subject="+Subject+"&YearSem="+YearSem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectCount").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
       DisplayInternalCalculationDetails();
}

function ViewAllSubjectList()
{

 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
   //var Sem=document.MarkEntryForPRN.YearSem.value;

         Ajax.open("POST","/OffCampus/ViewAllSubjectsOfCourse.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}
function ViewSubjectList()
{

 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
   //var Sem=document.MarkEntryForPRN.YearSem.value;

         Ajax.open("POST","/OffCampus/ViewAllSubjectsForSearch.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectListSearch").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");

}
function ChangeSubjectListNew()
{

   var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
   var Sem=document.getElementById("YearSem").value;
   var AcdemicYear=document.getElementById("AcdemicYear").value;

   //alert(AcdemicYear);

    Ajax.open("POST","/OffCampus/ViewSubjectListMarkEntry.jsp?Course="+ Course+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}
       Ajax.send("");
}
function openNewWindow1(form)
{    
    var sl1=document.getElementsByName("Select");
       for(var i=0;i<document.getElementsByName("Select").length;i++)
                {          
    alert(sl1[i].checked);}
}
function openNewWindow(form)
{
    var cnt=0,markId=0,j=0;
    var arry=new Array();
         if(form.SelectAll.checked)
        {
             if(document.getElementsByName("Select").length>0)
             {
               // markId=form.Select[document.getElementsByName("Select").length-2].value;
                //alert(markId);
           
                 for(var i=0;i<document.getElementsByName("Select").length;i++)
                {
                    var sl1=document.getElementsByName("Select");
                    if(sl1[i].checked!=false)
                    {
                      markId=sl1[i].value;
                       arry[j]=sl1[i].value;
                       cnt++;
                       j++;
                      // if(cnt==7)break;
                    }

                }
                window.open('/OffCampus/VerificationSO.jsp?StudentSubjectMarkId='+markId+'&Select='+arry,'1349845259981','width=900,height=750,toolbar=0,menubar=0,location=0,status=1,scrollbars=1,resizable=1,left=0,top=0');
                return false;
             }
        }
        else
        {

             if(document.getElementsByName("Select").length>0)
             {
               for(var i=0;i<document.getElementsByName("Select").length;i++)
                {
                    var sl=document.getElementsByName("Select");
                    if(sl[i].checked!=false)
                    {
                       markId=sl[i].value;
                       arry[j]=sl[i].value;
                       cnt++;
                       j++;
                    }

                }
                if (cnt==0){
		alert("Please Select Atleast One Student");
		document.getElementById('SelectAll').focus();
		return false;
                }
             }

              if(cnt>0)
              {
                  window.open('/OffCampus/VerificationSO.jsp?StudentSubjectMarkId='+markId+'&Select='+arry,'1349845259981','width=800,height=700,toolbar=0,menubar=0,location=0,status=1,scrollbars=0,resizable=1,left=0,top=0');
                  return false;
              }
        }
}

function refreshParent()
{
    //alert('hi');
    // window.opener.ParentWindowFunction();
// to reload parent window
    parent.window.opener.document.location.reload();
    window.close();
}

function ViewSubjectList()
{

 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
   //var Sem=document.MarkEntryForPRN.YearSem.value;

         Ajax.open("POST","/OffCampus/ViewAllSubjectsForSearch.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectListSearch").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");

}

function createXMLHttpRequest(){
  // See http://en.wikipedia.org/wiki/XMLHttpRequest
  // Provide the XMLHttpRequest class for IE 5.x-6.x:
  if( typeof XMLHttpRequest == "undefined" ) XMLHttpRequest = function() {
    try {return new ActiveXObject("Msxml2.XMLHTTP.6.0")} catch(e) {}
    try {return new ActiveXObject("Msxml2.XMLHTTP.3.0")} catch(e) {}
    try {return new ActiveXObject("Msxml2.XMLHTTP")} catch(e) {}
    try {return new ActiveXObject("Microsoft.XMLHTTP")} catch(e) {}
    throw new Error( "This browser does not support XMLHttpRequest." )
  };
  return new XMLHttpRequest();
}

function chkNum(e)
{
	var key;
	var keychar;
	if (window.event){
		key = window.event.keyCode;
	}else if (e){
		key = e.which;
	}
	else
		return true;

	if((key == 8) || (key == 0)|| (key == 46)|| (key == 13))
		return true;

	keychar = String.fromCharCode(key);
	keychar = keychar.toUpperCase();
	key=keychar.charCodeAt(0);
	if((key >= 48) && (key <= 57))
	{
		return true;
	}
	else
            {

            alert("Alphabets or special characters are not allowed")
	    return false;
            }
}

function chkNumForInt(e)
{
	var key;
	var keychar;
	if (window.event){
		key = window.event.keyCode;
	}else if (e){
		key = e.which;
	}
	else
		return true;

	if((key == 8) || (key == 0)|| (key == 13))
		return true;

	keychar = String.fromCharCode(key);
	keychar = keychar.toUpperCase();
	key=keychar.charCodeAt(0);
	if((key >= 48) && (key <= 57))
	{
		return true;
	}
	else
            {

            alert("Alphabets or special characters are not allowed")
	    return false;
            }
}

function isCountReached()
{
    //var count=parseInt(document.MarkEntryForPRN.Count.value);
    
    ViewSubjectTotalCountDetails();
     var Ajax=createXMLHttpRequest();
    Ajax.open("POST","/OffCampus/RecorsdsEnteredandNotVerified.jsp");
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

        var count=parseInt(Ajax.responseText);
        if(count >=20)
            alert("Send Records for Approval");
              //document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function TotalRecordsVerifiedByAssistant()
{
    var Assistant=document.MarkVerificationSO.Assistant.value;
    var Ajax=createXMLHttpRequest();
    Ajax.open("POST","/OffCampus/CountOfMarksVerifiedByAssistant.jsp?Assistant="+Assistant);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4)
        {
         document.getElementById("recordCount").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function SelectAllRows(form)
{
 
    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    if(form.SelectAll.checked)
        {
            if(form.Select.length>1)
                {
            for(var i=0;i<form.Select.length;i++)
                {
                    if(document.MarkVerificationSO.external[i].readOnly==false)
                        {
                            form.Select[i].checked=false;
                            
                        }
                        else
                            {
                                form.Select[i].checked=true;
                            }
                }
                }
                else
                    {
                        if(document.MarkVerificationSO.external.readOnly==false)
                        {
                            form.Select.checked=false;

                        }
                        else
                            {
                                form.Select.checked=true;
                            }
                    }
        }
        else
            {
                if(form.Select.length>0)
                {
                for(var i=0;i<form.Select.length;i++)
                {
                    form.Select[i].checked=false;
                }
                }
                else
                    {
                        form.Select.checked=false;
                    }
            }
}

function SelectAllRowsForPRNList(form)
{
    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);

    if(form.SelectAll.checked)
        {
            if(form.Select.length>1)
                {
            for(var i=0;i<form.Select.length;i++)
                {
                   form.Select[i].checked=true;
                }
                }
                else
                    {
                         form.Select.checked=true;
                    }
        }
        else
            {
                if(form.Select.length>0)
                {
                for(var i=0;i<form.Select.length;i++)
                {
                    form.Select[i].checked=false;
                }
                }
                else
                    {
                        form.Select.checked=false;
                    }
            }

}

function OpenTextForEdit(i)
{  
    if(document.MarkVerificationSO.external.length>1)
        {
            if(document.MarkVerificationSO.SubmitEdit[i].value=='Edit')
                {
                    document.MarkVerificationSO.external[i].readOnly=false;
                    document.MarkVerificationSO.internal[i].readOnly=false;
                    document.MarkVerificationSO.practical[i].readOnly=false;
                    document.MarkVerificationSO.Select[i].checked=false;
                    document.MarkVerificationSO.external[i].focus();
                    document.MarkVerificationSO.SubmitEdit[i].value="Submit";
                    for(var j=0;j<document.MarkVerificationSO.external.length;j++)
                        {
                            if(j!=i)
                                {
                                    document.MarkVerificationSO.SubmitEdit[j].disabled=true;
                                }
                        }

                    return false;
                }
                else
                    {
                        UpdateMarkBySO(i);
                        return true;
                    }
        }
        else
            {
                
                if(document.MarkVerificationSO.SubmitEdit.value=='Edit')
                {
                    
                    document.MarkVerificationSO.external.readOnly=false;
                    document.MarkVerificationSO.internal.readOnly=false;
                    document.MarkVerificationSO.practical.readOnly=false;
                    document.MarkVerificationSO.Select.checked=false;
                    document.MarkVerificationSO.external.focus();
                    document.MarkVerificationSO.SubmitEdit.value='Submit';
                    return false;
                }
                else
                    {
                        //alert("ssssssss");
                        UpdateMarkBySO(i);
                        return true;
                    }
                 
            }
          
}

function UpdateMarkBySO(i)
{
    
    var Select;
    var external;
    var internal;
    var practical;
    
            //alert(document.MarkVerificationSO.SubmitEdit[i].value);
            //alert('pppppp');
            if(document.MarkVerificationSO.external.length>1)
                {
                    Select=document.MarkVerificationSO.Select[i].value;
                    external=document.MarkVerificationSO.external[i].value;
                    internal=document.MarkVerificationSO.internal[i].value;
                    practical=document.MarkVerificationSO.practical[i].value;
                }
                else
                    {

                        //alert(document.MarkVerificationSO.Select.value);
                        Select=document.MarkVerificationSO.Select.value;
                        external=document.MarkVerificationSO.external.value;
                        internal=document.MarkVerificationSO.internal.value;
                         practical=document.MarkVerificationSO.practical.value;
                    }
               var Ajax=createXMLHttpRequest();
               Ajax.open("POST","/OffCampus/UpdateMarkBySO.jsp?Select="+Select+"&external="+external+"&internal="+internal+"&practical="+practical);
               Ajax.onreadystatechange = function()
               {
                if (Ajax.readyState == 4)
                {
                    //alert(Ajax.responseText);
                   if(Ajax.responseText.trim()=="true"){
                        document.MarkVerificationSO.submit();
                     // window.location="/OffCampus/MarkVerificationSO.jsp";

                  return true;}
                   else
                    document.MarkVerificationSO.submit();
                }
            }

            Ajax.send("");
        
}

function ResetAll()
{
    document.MarkEntryForPRN.Exam.value=-1;
    document.MarkEntryForPRN.Course.value=-1
    document.MarkEntryForPRN.YearSem.value=-1;
    document.MarkEntryForPRN.Subject.value=-1;
    document.MarkEntryForPRN.PRN.value="";
    document.MarkEntryForPRN.Mark.value="";
    document.MarkEntryForPRN.InternalMark.value="";
    document.MarkEntryForPRN.Remarks.value='';
    document.MarkEntryForPRN.AcdemicYear.value=-1;
    
}

function OpenTextForEditPostCorrection(i)
{

    if(document.PostCorrection.external.length>1)
        {
            if(document.PostCorrection.SubmitEdit[i].value=='Edit')
                {
                    document.PostCorrection.external[i].readOnly=false;
                    document.PostCorrection.internal[i].readOnly=false;
                    document.PostCorrection.externalmod[i].readOnly=false;
                    document.PostCorrection.internalmod[i].readOnly=false;
                    document.PostCorrection.Select[i].checked=false;
                    document.PostCorrection.external[i].focus();
                    document.PostCorrection.SubmitEdit[i].value="Submit";
                    for(var j=0;j<document.PostCorrection.external.length;j++)
                        {
                            if(j!=i)
                                {
                                    document.PostCorrection.SubmitEdit[j].disabled=true;
                                }
                        }

                    return false;
                }
                else
                    {
                       
                        UpdateMarkBySOPostCorrection(i);
                        return true;
                    }
        }
        else
            {

                if(document.PostCorrection.SubmitEdit.value=='Edit')
                {

                    document.PostCorrection.external.readOnly=false;
                    document.PostCorrection.internal.readOnly=false;
                      document.PostCorrection.externalmod.readOnly=false;
                    document.PostCorrection.internalmod.readOnly=false
                    document.PostCorrection.Select.checked=false;
                    document.PostCorrection.external.focus();
                    document.PostCorrection.SubmitEdit.value='Submit';
                    return false;
                }
                else
                    {
                        //alert("ssssssss");
                       
                        if (UpdateMarkBySOPostCorrection(i))
                        return true;
                      else return false;
                    }

            }

}
function OpenTextForEditRevaluation(i)
{
        

   if(document.Revaluation.external.length>1)
        {
            if(document.Revaluation.SubmitEdit[i].value=='Edit')
                {  
               
                    document.Revaluation.external[i].readOnly=false;
                    document.Revaluation.internal[i].readOnly=false;
                    document.Revaluation.externalmod[i].readOnly=false;
                    document.Revaluation.internalmod[i].readOnly=false;
                    document.Revaluation.Select[i].checked=false;
                    document.Revaluation.external[i].focus();
                    document.Revaluation.SubmitEdit[i].value="Submit";
                    for(var j=0;j<document.Revaluation.external.length;j++)
                        {
                            if(j!=i)
                                {
                                    document.Revaluation.SubmitEdit[j].disabled=true;
                                }
                        }

                    return false;
                }
                else
                    {
                        UpdateMarkBySORevaluation(i);
                        return true;
                    }
        }
        else
            {

                if(document.Revaluation.SubmitEdit.value=='Edit')
                {
                     

                    document.Revaluation.external.readOnly=false;
                    document.Revaluation.internal.readOnly=false;
                     document.Revaluation.externalmod.readOnly=false;
                    document.Revaluation.internalmod.readOnly=false;
                    document.Revaluation.Select.checked=false;
                    document.Revaluation.external.focus();
                    document.Revaluation.SubmitEdit.value='Submit';
                    return false;
                }
                else
                    {
                        //alert("ssssssss");
                        UpdateMarkBySORevaluation(i);
                        return true;
                    }

            }

}

function  UpdateMarkBySOPostCorrection(i)
{

    var Select;
    var external;
    var internal;
    var externalmod;
    var internalmod;

            //alert(document.MarkVerificationSO.SubmitEdit[i].value);
            //alert('pppppp');
            if(document.PostCorrection.external.length>1)
                {
                    Select=document.PostCorrection.Select[i].value;
                    external=document.PostCorrection.external[i].value;
                    internal=document.PostCorrection.internal[i].value;
                     externalmod=document.PostCorrection.externalmod[i].value;
                     internalmod= document.PostCorrection.internalmod[i].value;
                }
                else
                    {

                        //alert(document.MarkVerificationSO.Select.value);
                        Select=document.PostCorrection.Select.value;
                        external=document.PostCorrection.external.value;
                        internal=document.PostCorrection.internal.value;
                        externalmod=document.PostCorrection.externalmod.value;
                     internalmod= document.PostCorrection.internalmod.value;
                    }
           
               var Ajax=createXMLHttpRequest();
               Ajax.open("POST","/OffCampus/PostCorrectionUpdateMarkBySO.jsp?Select="+Select+"&external="+external+"&internal="+internal+"&externalmod="+externalmod+"&internalmod="+internalmod);
               Ajax.onreadystatechange = function()
               {
                if (Ajax.readyState == 4)
                {
                    //alert("res*"+Ajax.responseText.trim()+"**");
                    if(Ajax.responseText.trim()=="true")
                        {
                          // alert("hai");
                        document.PostCorrection.submit();
                       return true;
                    }else{
                       // alert("no");
                      // alert("Error occured while updation");
                       return false;
                    }
                }
            }
            Ajax.send("");
              
}
function  UpdateMarkBySORevaluation(i)
{

    var Select;
    var external;
    var internal;
    var externalmod;
    var internalmod;

            //alert(document.MarkVerificationSO.SubmitEdit[i].value);
          
            if(document.Revaluation.external.length>1)
                {
                    Select=document.Revaluation.Select[i].value;
                    external=document.Revaluation.external[i].value;
                    internal=document.Revaluation.internal[i].value;
                     externalmod=document.Revaluation.externalmod[i].value;
                     internalmod= document.Revaluation.internalmod[i].value;
                }
                else
                    {

                        //alert(document.MarkVerificationSO.Select.value);
                        Select=document.Revaluation.Select.value;
                        external=document.Revaluation.external.value;
                        internal=document.Revaluation.internal.value;
                        externalmod=document.Revaluation.externalmod.value;
                     internalmod= document.Revaluation.internalmod.value;
                    }

               var Ajax=createXMLHttpRequest();
               
               Ajax.open("POST","/OffCampus/RevaluationUpdateMarkBySo.jsp?Select="+Select+"&external="+external+"&internal="+internal+"&externalmod="+externalmod+"&internalmod="+internalmod);
               Ajax.onreadystatechange = function()
               {
                if (Ajax.readyState == 4)
                {
                    //alert("res"+Ajax.responseText);
                    if(Ajax.responseText.trim()=="true")
                        {
                        document.Revaluation.submit();
                       return true;
                      
                    }else{                     
                       return false;
                    }
                }
            }

            Ajax.send("");

}
function SelectAllRowsPostCorrection(form)
{

    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    if(form.SelectAll.checked)
        {
            if(form.Select.length>1)
                {
            for(var i=0;i<form.Select.length;i++)
                {
                    if(document.PostCorrection.external[i].readOnly==false)
                        {
                            form.Select[i].checked=false;

                        }
                        else
                            {
                                form.Select[i].checked=true;
                            }
                }
                }
                else
                    {
                        if(document.PostCorrection.external.readOnly==false)
                        {
                            form.Select.checked=false;

                        }
                        else
                            {
                                form.Select.checked=true;
                            }
                    }
        }
        else
            {
                if(form.Select.length>0)
                {
                for(var i=0;i<form.Select.length;i++)
                {
                    form.Select[i].checked=false;
                }
                }
                else
                    {
                        form.Select.checked=false;
                    }
            }
}
function SelectAllRowsRevaluation(form)
{

    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    if(form.SelectAll.checked)
        {
            if(form.Select.length>1)
                {
            for(var i=0;i<form.Select.length;i++)
                {
                    if(document.Revaluation.external[i].readOnly==false)
                        {
                            form.Select[i].checked=false;

                        }
                        else
                            {
                                form.Select[i].checked=true;
                            }
                }
                }
                else
                    {
                        if(document.Revaluation.external.readOnly==false)
                        {
                            form.Select.checked=false;

                        }
                        else
                            {
                                form.Select.checked=true;
                            }
                    }
        }
        else
            {
                if(form.Select.length>0)
                {
                for(var i=0;i<form.Select.length;i++)
                {
                    form.Select[i].checked=false;
                }
                }
                else
                    {
                        form.Select.checked=false;
                    }
            }
}

function OpenTextForEditPostCorrectionByAR(i)
{

    if(document.PostCorrectionAR.external.length>1)
        {
      
            if(document.PostCorrectionAR.SubmitEdit[i].value=='Edit')
                {
                          
                    document.PostCorrectionAR.external[i].readOnly=false;
                    document.PostCorrectionAR.internal[i].readOnly=false;
                    document.PostCorrectionAR.externalmod[i].readOnly=false;
                    document.PostCorrectionAR.internalmod[i].readOnly=false;
                    document.PostCorrectionAR.Select[i].checked=false;
                    document.PostCorrectionAR.external[i].focus();
                    document.PostCorrectionAR.SubmitEdit[i].value="Submit";
                    for(var j=0;j<document.PostCorrectionAR.external.length;j++)
                        {
                            if(j!=i)
                                {                                  
                                    document.PostCorrectionAR.SubmitEdit[j].disabled=true;
                                }
                        }

                    return false;
                }
                else
                    {

                        UpdateMarkByARPostCorrection(i);
                        return true;
                    }
        }
        else
            {

                if(document.PostCorrectionAR.SubmitEdit.value=='Edit')
                {

                    document.PostCorrectionAR.external.readOnly=false;
                    document.PostCorrectionAR.internal.readOnly=false;
                      document.PostCorrectionAR.externalmod.readOnly=false;
                    document.PostCorrectionAR.internalmod.readOnly=false
                    document.PostCorrectionAR.Select.checked=false;
                    document.PostCorrectionAR.external.focus();
                    document.PostCorrectionAR.SubmitEdit.value='Submit';
                    return false;
                }
                else
                    {
                        //alert("ssssssss");

                        if (UpdateMarkByARPostCorrection(i))
                        return true;
                      else return false;
                    }

            }

}
function OpenTextForEditRevaluationByAR(i)
{

    if(document.RevaluationByAR.external.length>1)
        {

            if(document.RevaluationByAR.SubmitEdit[i].value=='Edit')
                {

                    document.RevaluationByAR.external[i].readOnly=false;
                    document.RevaluationByAR.internal[i].readOnly=false;
                    document.RevaluationByAR.externalmod[i].readOnly=false;
                    document.RevaluationByAR.internalmod[i].readOnly=false;
                    document.RevaluationByAR.Select[i].checked=false;
                    document.RevaluationByAR.external[i].focus();
                    document.RevaluationByAR.SubmitEdit[i].value="Submit";
                    for(var j=0;j<document.RevaluationByAR.external.length;j++)
                        {
                            if(j!=i)
                                {
                                    document.RevaluationByAR.SubmitEdit[j].disabled=true;
                                }
                        }

                    return false;
                }
                else
                    {

                        UpdateMarkByARRevaluation(i);
                        return true;
                    }
        }
        else
            {

                if(document.RevaluationByAR.SubmitEdit.value=='Edit')
                {

                    document.RevaluationByAR.external.readOnly=false;
                    document.RevaluationByAR.internal.readOnly=false;
                      document.RevaluationByAR.externalmod.readOnly=false;
                    document.RevaluationByAR.internalmod.readOnly=false
                    document.RevaluationByAR.Select.checked=false;
                    document.RevaluationByAR.external.focus();
                    document.RevaluationByAR.SubmitEdit.value='Submit';
                    return false;
                }
                else
                    {
                        //alert("ssssssss");

                        if (UpdateMarkByARRevaluation(i))
                        return true;
                      else return false;
                    }

            }

}

function  UpdateMarkByARPostCorrection(i)
{

    var Select;
    var external;
    var internal;
    var externalmod;
    var internalmod;

            //alert(document.MarkVerificationSO.SubmitEdit[i].value);
            //alert('pppppp');
            if(document.PostCorrectionAR.external.length>1)
                {
                    Select=document.PostCorrectionAR.Select[i].value;
                    external=document.PostCorrectionAR.external[i].value;
                    internal=document.PostCorrectionAR.internal[i].value;
                     externalmod=document.PostCorrectionAR.externalmod[i].value;
                     internalmod= document.PostCorrectionAR.internalmod[i].value;
                }
                else
                    {

                        //alert(document.MarkVerificationSO.Select.value);
                        Select=document.PostCorrectionAR.Select.value;
                        external=document.PostCorrectionAR.external.value;
                        internal=document.PostCorrectionAR.internal.value;
                        externalmod=document.PostCorrectionAR.externalmod.value;
                     internalmod= document.PostCorrectionAR.internalmod.value;
                    }

               var Ajax=createXMLHttpRequest();
               Ajax.open("POST","/OffCampus/PostCorrectionUpdateByAR.jsp?Select="+Select+"&external="+external+"&internal="+internal+"&externalmod="+externalmod+"&internalmod="+internalmod);
               Ajax.onreadystatechange = function()
               {
                if (Ajax.readyState == 4)
                {
                    //alert("res"+Ajax.responseText);
                    if(Ajax.responseText.trim()=="true")
                        {
                        document.MarkVerificationAR.submit();
                       return true;
                    }else{
                      // alert("Error occured while updation");
                       return false;
                    }
                }
            }

            Ajax.send("");

}
function  UpdateMarkByARRevaluation(i)
{

    var Select;
    var external;
    var internal;
    var externalmod;
    var internalmod;

            //alert(document.MarkVerificationSO.SubmitEdit[i].value);
            //alert('pppppp');
            if(document.RevaluationByAR.external.length>1)
                {
                    Select=document.RevaluationByAR.Select[i].value;
                    external=document.RevaluationByAR.external[i].value;
                    internal=document.RevaluationByAR.internal[i].value;
                     externalmod=document.RevaluationByAR.externalmod[i].value;
                     internalmod= document.RevaluationByAR.internalmod[i].value;
                }
                else
                    {

                        //alert(document.MarkVerificationSO.Select.value);
                        Select=document.RevaluationByAR.Select.value;
                        external=document.RevaluationByAR.external.value;
                        internal=document.RevaluationByAR.internal.value;
                        externalmod=document.RevaluationByAR.externalmod.value;
                     internalmod= document.RevaluationByAR.internalmod.value;
                    }

               var Ajax=createXMLHttpRequest();
               Ajax.open("POST","/OffCampus/RevaluationUpdateByAR.jsp?Select="+Select+"&external="+external+"&internal="+internal+"&externalmod="+externalmod+"&internalmod="+internalmod);
               Ajax.onreadystatechange = function()
               {
                if (Ajax.readyState == 4)
                {
                    //alert("res"+Ajax.responseText);
                    if(Ajax.responseText.trim()=="true")
                        {
                        document.RevaluationByAR.submit();
                       return true;
                    }else{
                      // alert("Error occured while updation");
                       return false;
                    }
                }
            }

            Ajax.send("");

}
function SelectAllRowsPostCorrectionByAR(form)
{

    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    if(form.SelectAll.checked)
        {
            if(form.Select.length>1)
                {
            for(var i=0;i<form.Select.length;i++)
                {
                    if(document.PostCorrectionAR.external[i].readOnly==false)
                        {
                            form.Select[i].checked=false;

                        }
                        else
                            {
                                form.Select[i].checked=true;
                            }
                }
                }
                else
                    {
                        if(document.PostCorrectionAR.external.readOnly==false)
                        {
                            form.Select.checked=false;

                        }
                        else
                            {
                                form.Select.checked=true;
                            }
                    }
        }
        else
            {
                if(form.Select.length>0)
                {
                for(var i=0;i<form.Select.length;i++)
                {
                    form.Select[i].checked=false;
                }
                }
                else
                    {
                        form.Select.checked=false;
                    }
            }
}
function SelectAllRowsRevaluationByAR(form)
{

    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    if(form.SelectAll.checked)
        {
            if(form.Select.length>1)
                {
            for(var i=0;i<form.Select.length;i++)
                {
                    if(document.RevaluationByAR.external[i].readOnly==false)
                        {
                            form.Select[i].checked=false;

                        }
                        else
                            {
                                form.Select[i].checked=true;
                            }
                }
                }
                else
                    {
                        if(document.RevaluationByAR.external.readOnly==false)
                        {
                            form.Select.checked=false;

                        }
                        else
                            {
                                form.Select.checked=true;
                            }
                    }
        }
        else
            {
                if(form.Select.length>0)
                {
                for(var i=0;i<form.Select.length;i++)
                {
                    form.Select[i].checked=false;
                }
                }
                else
                    {
                        form.Select.checked=false;
                    }
            }
}