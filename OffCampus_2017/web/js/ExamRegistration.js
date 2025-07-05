/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function redirectToExamRegistration()
{
window.location = "CentreExamFee.jsp?AV=1";
}
function SelectAllRows(form)
{
    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    //alert("SDFsdf");
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
            // alert("SDFsdf");
}

function ViewSubBranches()
{

    var Ajax=createXMLHttpRequest();
    var BranchId=document.getElementById("course_applied").value;
    var SemOrYear=document.getElementById("year_applied").value;
    Ajax.open("POST","/OffCampus/ViewSubBranchs.jsp?BranchId="+BranchId+"&SemorYear="+SemOrYear);
 //alert("stdsrtgsdfgh");
    Ajax.onreadystatechange = function() {

        if (Ajax.readyState == 4) {

              document.getElementById("SubBranchRow").innerHTML = Ajax.responseText;

              //alert(Ajax.responseText);

    }
}
Ajax.send("");
}

function SelectAllRowsForDD(form)
{
    //var Select=document.MarkVerificationSO.SelectAll.value;
    //alert(form.SelectAll.checked);
    if(form.SelectAll.checked)
        {
            //alert('sdfdf');
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
function ChangeYearOrSemNominalRolls()
{
    //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("course_applied").value;
         Ajax.open("POST","/OffCampus/ViewSemestersForNominalRolls.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("YearOrSem").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

function ChangeSubjectList()
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


