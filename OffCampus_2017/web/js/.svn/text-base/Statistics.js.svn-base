function ChangeSubjectStatistics()
{
    //alert("dsgsdfg");
 var Ajax=createXMLHttpRequest();
   var Course=document.getElementById("Course").value;
   var YearSem=document.getElementById("YearSem").value;
   var ExamId=document.getElementById("Exam").value;
   //alert(ExamId);
         Ajax.open("POST","/OffCampus/StatisticsOfSubjects.jsp?Course="+ Course+"&YearSem="+YearSem+"&Exam="+ExamId);
    Ajax.onreadystatechange = function() {
        if (Ajax.readyState == 4) {

              document.getElementById("MarkStatistics").innerHTML = Ajax.responseText;
    }
}

       Ajax.send("");
}

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

function ChangeModerationType()
{
    var BranchRow=document.getElementById("BranchModeration");
    var SubjectRow=document.getElementById("SubjectModeration");
    var DisplayTable=document.getElementById("BranchM");
    var SubjectTable=document.getElementById("SubjectM");
    var SubjectList=document.getElementById("SubjectList");
    if(document.ResultStatistics.ModerationType[0].checked)
        {
            BranchRow.style.display='inline';
            SubjectRow.style.display='none';
            SubjectTable.style.display='none';
            SubjectList.style.display='none';
            DisplayTable.style.display='inline';
        }
   else if(document.ResultStatistics.ModerationType[1].checked)
        {
            BranchRow.style.display='none';
            SubjectRow.style.display='inline';
            SubjectTable.style.display='inline';
            SubjectList.style.display='inline';
            DisplayTable.style.display='none';
        }
        else
            {
                SubjectTable.style.display='none';
                SubjectList.style.display='none';
                SubjectRow.style.display='none';
            }
}

function ChechSubject(form)
{
    var From=document.getElementById("ModerationFrom").value;
    var To=document.getElementById("ModerationTo").value;

    if(form.ModerationType[0].checked)
        {
        if(From=='')
            {
                alert("Enter moderation Range :From")
                return false;
            }
            else if(To=='')
            {
                alert("Enter moderation Range :To")
                return false;
            }
            else
                {
                    return true;
                }

        }
   else if(form.ModerationType[1].checked)
        {
            var Subject=document.getElementById("Subject").value;
            if(Subject=='-1')
            {
                alert("select Subject");
                return false
            }
            else if(From=='')
            {
                alert("Enter moderation Range :From")
                return false;
            }
            else if(To=='')
            {
                alert("Enter moderation Range :To")
                return false;
            }
            else
                {
                    return true;
                }
        }
    
}

