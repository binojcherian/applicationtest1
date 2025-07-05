function showonlyonev2(thechosenone) 
{
      var newboxes = document.getElementsByTagName("div");
      for(var x=0; x<newboxes.length; x++)
      {
            name = newboxes[x].getAttribute("name");
            //newboxes[x].style.display = 'none';
            if (name == 'newboxes-2')
            {
                  if (newboxes[x].id == thechosenone)
                  {
                        if (newboxes[x].style.display == 'block')
                        {
                              newboxes[x].style.display = 'none';
                        }
                        else
                        {
                              newboxes[x].style.display = 'block';
                        }
                  }
                  else
                  {
                        newboxes[x].style.display = 'none';
                  }
            }
               
      }
}

function ViewSubjectWiseMarkEntryStatus()
{
    
 var Ajax=createXMLHttpRequest();
    var Course=document.getElementById("SubjectSearch").value;
   //alert(Course);
         Ajax.open("POST","/OffCampus/SubjectWiseMarkEntryStatus.jsp?Subject="+ Course);
    Ajax.onreadystatechange = function()
    {
        if (Ajax.readyState == 4)
        {
        //alert(Ajax.responseText);
              document.getElementById("MarkEntryStatusForSubject").innerHTML = Ajax.responseText;
        }
    }
       Ajax.send("");

}


