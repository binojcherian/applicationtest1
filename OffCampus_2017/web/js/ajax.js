
var xmlhttp,elem_id='';
var null_feild1 = new Array();
function GetXmlHttpObject()
{
if (window.XMLHttpRequest)
  {
  // code for IE7+, Firefox, Chrome, Opera, Safari
  return new XMLHttpRequest();
  }
if (window.ActiveXObject)
  {
  // code for IE6, IE5
  return new ActiveXObject("Microsoft.XMLHTTP");
  }
return null;
}

//function ajaxCall(str,url,elem_id)
//ajaxCall('admn_yr='+this.value,'college_ajax.php','degree_container');
function ajaxCall(url,str,elem_id1,null_feild)
{//alert(elem_id1);
elem_id = elem_id1;
null_feild1 = null_feild;
xmlhttp=GetXmlHttpObject();
if (xmlhttp==null)
  {
  alert ("Your browser does not support AJAX!");
  return;
  }
//var url="getcustomer.asp";
url=url+str;
url=url+"&sid="+Math.random();//alert(url);
//alert(url);
xmlhttp.onreadystatechange=handler;
xmlhttp.open("GET",url,true);
xmlhttp.send(null);
}

function handler()
{
if (xmlhttp.readyState==4)
  {
	 // alert(elem_id+"dfdf");
	 // alert(document.getElementById(elem_id).innerHTML);
	 // alert(xmlhttp.responseText);
      document.getElementById(elem_id).innerHTML=xmlhttp.responseText;	
      var j;
      for (j = 0; j < null_feild1.length; ++j)
      {
    	  document.getElementById(null_feild1[j]).innerHTML='';
      }
    
  }
}

