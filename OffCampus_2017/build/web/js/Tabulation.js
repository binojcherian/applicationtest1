/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function ChangeCentreList()
{
    ChangeYearOrSemForMarkEntry();
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
         Ajax.open("POST","/OffCampus/ViewCentreList.jsp?Course="+ Course);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("CentreList").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}
function ChangeYearOrSemForMarkEntry()
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


