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
function chkNumberOnly(e)
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

	if((key == 8) || (key == 0))
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
function EnableModeration(i)
{
    
    if(document.ModerationPreperation.Select[i].checked)
        {
            document.ModerationPreperation.SeparateModeration[i].readOnly=false;
             document.ModerationPreperation.SeparateModeration[i].focus();
        }
        else
            {
                 document.ModerationPreperation.SeparateModeration[i].readOnly=true;
                 document.ModerationPreperation.SeparateModeration[i].value="";
            }
}

function EnableClassModeration(form)
{
    if(form.ForDistinction.checked)
        {
            form.Distinction.readOnly=false;
            form.Distinction.focus();
        }
        else
            {
                form.Distinction.readOnly=true;
                form.Distinction.value="";
            }
       if(form.ForFirst.checked)
        {
            form.First.readOnly=false;
            form.First.focus();
        }
        else
            {
                form.First.readOnly=true;
                form.First.value="";
            }
       if(form.ForSecond.checked)
        {
            form.Second.readOnly=false;
            form.Second.focus();
        }
        else
            {
                form.Second.readOnly=true;
                form.Second.value="";
            }
}