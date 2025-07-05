/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function ChangeYearOrSem()
{
    //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
         Ajax.open("POST","/OffCampus/ViewYearOrSemForStatistics.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("YearOrSem").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}
function ViewAllSubjectListForSearch()
{
//ViewCourseWiseMarkEntryStatus();
 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("CourseSearch").value;
    var Sem=document.getElementById("YearSemSearch").value
    //alert(Sem);
    //MarkEntryForPRN.YearSem.value;

         Ajax.open("POST","/OffCampus/ViewAllSubjectsForSearch.jsp?Course="+ Course+"&YearSem="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectListSearch").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");

}

function ViewAllSubjectList()
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
function ViewCourseWiseMarkEntryStatus()
{

 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("CourseSearch").value;
   //alert(Course);
         Ajax.open("POST","/OffCampus/CourseWiseStatus.jsp?Course="+ Course);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
        //alert(Ajax.responseText);
              document.getElementById("MarkEntryStatusForCourse").innerHTML = Ajax.responseText;
        }
    }
       Ajax.send("");

}

function getStatusOfAbsEntry()
{

    //alert('sadadad');
    var Exam=document.getElementById("Exam").value;
    var Subject=document.getElementById("Subject").value;
    var Absent=document.getElementById("Absent").value;
    if(Subject !=-1 && Exam!=-1)
        {
     var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;

         Ajax.open("POST","/OffCampus/CountAbsenteesAndMalPractice.jsp?Subject="+ Subject+"&Exam="+Exam);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
           document.getElementById("Status").innerHTML = Ajax.responseText;

        }
      }
        Ajax.send("");
        }
}
function ChangeYearOrSemForCount()
{
  //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;

         Ajax.open("POST","/OffCampus/YearOrSemesterNew.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
       //alert(Ajax.responseText);
              document.getElementById("YearOrSem").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ChangeYearOrSemForAbsentee()
{
    //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
         Ajax.open("POST","/OffCampus/YearOrSemForAbsentee.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("YearOrSem").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ViewSubjectList()
{

 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;

         Ajax.open("POST","/OffCampus/SubjectListForAbsentees.jsp?Course="+ Course+"&YearSem="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}




function ChangeSubjectList()
{
//alert('sdfzdfgdz');
 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;
//alert(Course);
         Ajax.open("POST","/OffCampus/ViewSubjectList.jsp?Course="+ Course+"&YearSem="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}
       Ajax.send("");
}

function ChangeSubjectListForCount()
{

    var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;

         Ajax.open("POST","/OffCampus/ViewSubjectList.jsp?Course="+ Course+"&YearSem="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
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

function ResetAll()
{
    document.Absentees.Exam.value=-1;
    document.Absentees.Course.value=-1;
    document.Absentees.YearSem.value=-1;
    document.Absentees.Subject.value=-1;
    document.Absentees.PRN.value='';
    document.Absentees.AcdemicYear.value=-1;

    return false;

}
function ResetForAbsenteeCount()
{
    document.AbsenteesCount.Exam.value=-1;
    document.AbsenteesCount.Course.value=-1;
    document.AbsenteesCount.YearSem.value=-1;
    document.AbsenteesCount.Subject.value=-1;
    document.AbsenteesCount.AcdemicYear.value=-1;
    document.AbsenteesCount.Count.value="";
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

// Modified By Yadu

function redirectfunction()
{
    var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;

   // alert(Course);

    if((Course=='21') && (Sem=='3' ||Sem=='4'))
        {
           // alert(Course);
            document.getElementById("SubBranchDet").style.visibility="visible";
            ViewSubBranchList();
           // ChangeSubjectList();
        }
    else if((Course=='17') && (Sem=='4'))
        {
             //alert(Course);
           document.getElementById("SubBranchDet").style.visibility="visible";
            ViewSubBranchList();
           // ChangeSubjectList();
        }
    else
        {
           document.getElementById("SubBranchDet").style.visibility="collapse";
            //alert(Course);
            ChangeSubjectListNew();
        }
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

function ChangeSubjectListNew()
{

   var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
   var Sem=document.getElementById("YearSem").value;
   var AcdemicYear=document.getElementById("AcdemicYear").value;

  //alert(AcdemicYear);

    Ajax.open("POST","/OffCampus/ViewSubjectListAbsentees.jsp?Course="+ Course+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
        //alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ViewSubBranchList()
{

    var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;

    //alert(Sem);

         Ajax.open("POST","/OffCampus/ViewSubBranchs.jsp?BranchId="+ Course+"&SemorYear="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
      // alert(Ajax.responseText);
              document.getElementById("SubBranchList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ViewSubBranchListForSearch()
{

    var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("CourseSearch").value;
    var Sem=document.getElementById("YearSemSearch").value;

   //alert(Sem);

         Ajax.open("POST","/OffCampus/ViewSubjectListAbsentees.jsp?BranchId="+ Course+"&SemorYear="+Sem);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
       //alert(Ajax.responseText);
              document.getElementById("SubBranchListSearch").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ViewAllSubBranchList()
{
    var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("Course").value;
    var Sem=document.getElementById("YearSem").value;
    var SubBranch=document.getElementById("SubBranch").value;
    var AcdemicYear=document.getElementById("AcdemicYear").value;

   //alert(AcdemicYear);

         Ajax.open("POST","/OffCampus/ViewSubBranchList.jsp?SubBranch="+SubBranch+"&Course="+ Course+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
      // alert(Ajax.responseText);
              document.getElementById("SubjectList").innerHTML = Ajax.responseText;
        }
    }
       Ajax.send("");
}

function ViewAllSubBranchListForSearch()
{
    var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("CourseSearch").value;
    var Sem=document.getElementById("YearSemSearch").value;
    var SubBranch=document.getElementById("SubBranchSearch").value;
    var AcdemicYear=document.getElementById("AcdemicYearSearch").value;

    //alert(AcdemicYear);

    Ajax.open("POST","/OffCampus/ViewSubBranchList.jsp?SubBranch="+SubBranch+"&Course="+ Course+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
      // alert(Ajax.responseText);
              document.getElementById("SubjectListSearch").innerHTML = Ajax.responseText;
    }
}
       Ajax.send("");
}

function ChangeYearOrSemForSearch()
{
   // alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("CourseSearch").value;

         Ajax.open("POST","/OffCampus/YearOrSemesterSubject.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("YearOrSemSearch").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function redirectFunctionForSearch()
{
    var Course=document.getElementById("CourseSearch").value;
    var Sem=document.getElementById("YearSemSearch").value;

   //alert(Course);

    if((Course=='21') && (Sem=='3' ||Sem=='4'))
        {
            //alert(Course);
            document.getElementById("SubBranchDetSearch").style.visibility="visible";
            ViewSubBranchListForSearch();
           // ChangeSubjectList();
        }
    else if((Course=='17') && (Sem=='4'))
        {
           // alert(Course);
           document.getElementById("SubBranchDetSearch").style.visibility="visible";
            ViewSubBranchListForSearch();
            //ChangeSubjectList();
        }
    else
        {
           document.getElementById("SubBranchDetSearch").style.visibility="collapse";
            // alert(Course);
          // ViewAllSubjectListForSearch();
           ChangeSubjectListSearch();
        }
}

function ChangeSubjectListSearch()
{

   var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("CourseSearch").value;
   var Sem=document.getElementById("YearSemSearch").value;
   var AcdemicYear=document.getElementById("AcdemicYearSearch").value;

  // alert(Course);

         Ajax.open("POST","/OffCampus/ViewSubjectListAbsentees.jsp?Course="+ Course+"&YearSem="+Sem+"&AcdemicYear="+AcdemicYear);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {
     //   alert(Ajax.responseText);
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