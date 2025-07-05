<?php
exit;
session_start();
include("config.php");

$dbQrsObj	= new Dbqueries();
$examslogchk		= $dbQrsObj->selectFunction("exam_master EM INNER JOIN camp_exam_map CEM ON EM.exam_id=CEM.exam_id",array("EM.exam_id","EM.exam_title","CEM.MA_start_date","CEM.Msc_start_Date","CEM.Mcom_start_date"),"CEM.camp_status=1 ");

$exammm=$dbQrsObj->selectFunction("exam_master",array("exam_id")," semester=4 and year=2016");

$crntExam=$exammm[0]['exam_id'];
if(!$examslogchk)
{
	 header("location:index.php?module=universityAssistant&page=assistantHome");
}
if($_SESSION['type']==1){
    $dbQrsObj->_database="mgu_pg_cbcssprivate";
}
 
//$accessCamp	= trim($_SESSION['mgucap_userCamp']);
$accessZone=$dbQrsObj->selectFunction("ugcap_college_course_map u
inner join ugcap_master_course_specialisation sd on sd.specialisation_id=u.specialisation_id
 inner join  mgucap_employee_collegecourse_access ss on ss.college_course_map_id=u.college_course_map_id",
array("distinct  course_id"),"user_tbl_id=".$_SESSION['user_tbl_id']);
//$accessZone	= trim($_SESSION['mgucap_userZone']);
$crse="";
$crsenm="";
  if($accessZone[0]['course_id']==1)
{
   $courses=$dbQrsObj->selectFunction("ugcap_master_course_specialisation",array("specialisation_id,specialisation_display_name")," specialisation_id in(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)");
}else if($accessZone[0]['course_id']==2)
{
	 $courses=$dbQrsObj->selectFunction("ugcap_master_course_specialisation",array("specialisation_id,specialisation_display_name")," specialisation_id in(16,17,18,19)");
}
else {
	 $courses=$dbQrsObj->selectFunction("ugcap_master_course_specialisation",array("specialisation_id,specialisation_display_name")," specialisation_id in(20)");
}
                 $course		= trim($_REQUEST['course']);

 if($course==1)
 {
	 	$crse=1;
	$crsenm="M.A";
 }
 
  if($course==2)
 {
	 	$crse=2;
	$crsenm="MCom";
 }
  if($course==3)
 {
	 	$crse=3;
	$crsenm="MSc";
 }


//$crntCentre	= trim($_REQUEST['college_id']);
//$crntClg	= $_SESSION['mgupgcap_college_id'];//trim($_REQUEST['college']);
$successmsg	= trim($_REQUEST['succmsg']);
$errmsg		= trim($_REQUEST['errmsg']);
$msg			= $_REQUEST['msg'];
if($_SESSION['mgupgcap_userType']!='AST'){
	header('Location:unauthorisedAccess.php'); exit(0);
}

                    if($_SESSION['type']==1){
						// $crse=$_REQUEST['course'];
                   $qpcode=$_REQUEST['qp'];
				   $crntCentre=$_REQUEST['centre'];
				   $centre		= $dbQrsObj->selectFunction("ugcap2011_master_college",array("college_id","college_name"),"college_status='active'","college_name ASC");
                $subjects		= $dbQrsObj->selectFunction("exam_papers_master EPM INNER JOIN subject_master SM  ON SM.subject_id=EPM.subject_id ",array("EPM.subject_id","SM.subject_name","EPM.qp_code"),"EPM.exam_id='".$_SESSION['crntExam']."' and EPM.qp_code_status='F'","EPM.qp_code" );
  $selFields	= array("UD.user_tbl_id","UD.User_Type","UD.User_Name","UD.User_Id");
	$table="camp_examiner_user_details UD
	inner join camp_examiner_details CED ON UD.user_tbl_id=CED.user_id
	inner join user_type UT ON UT.User_Type=UD.User_Type ";
	$wherecon="UD.User_Type ='CEX' and UD.Status='Y'";
	// and UD.User_Id not in(select chief_code from camp_additional_chief_map where specialisation_id=74)
	$userDetls	= $dbQrsObj->selectFunction($table,$selFields,$wherecon,"UD.User_Name ASC");     
 
  $subjectteacherDtls=$dbQrsObj->selectFunction("exam_papers_master",array("subject_id"),"qp_code='".$qpcode."'","");
 
 }
 if($_POST['btn_go'])
 {
	 $stud_Det		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on er.account_id=msa.account_id
	inner join exam_papers ep on ep.exam_regn_id=er.exam_regn_id inner join student_papers sp on sp.account_id=msa.account_id and sp.stud_paper_id=ep.stud_paper_id
inner join subject_master sm on sm.subject_id=sp.subject_id ",array("distinct PRN","account_student_name","ep.exam_paper_id","ep.pract_ext_mark","pract_ext_grade_status","ep.pract_ext_grade_entered_user"),"er.exam_id='".$crntExam."' and msa.college_id=".$crntCentre." and msa.specialisation_id=".$course." and sm.subject_name like'VIVA VOCE' and ep.pract_ext_grade_entered_user!=".$_SESSION['user_tbl_id'],"msa.PRN" ); 
	//echo $dbQrsObj->_query;exit();
	 
/*	 if($qpcode!="0")
	 {
	 $qpfalsedet		= $dbQrsObj->selectFunction("qpcode_falseno_camp",array("falsestart","falseend"),"exam_id='".$_SESSION['crntExam']."' and qpcode='".$qpcode."'","qpcode" );
	 
	 
	$selregmapprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("count(msa.prn) as regprn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam'].$strspec,"" );
	 
	  $selabsmapprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("count(msa.prn) as absprn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and ep.prent_abscent!='P' and ep.bar_code is null".$strspec,"" );
	 
	   $selmapprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("count(msa.prn) as mapprn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and ep.prent_abscent='P' and ep.bar_code is not null and ep.bar_code_map_status in(0,1)".$strspec,"" );
	   
	  
	 
	 
	 
	 
 }*/}
 
 if($_POST['btn_saveex'])
 {
	 $qpverval=$_POST['lstQpver'];
	 //$ext_mark=$_POST['ext_mrk'];
	// echo count($qpverval);
	// exit;
	 for($i=0;$i<count($qpverval);$i++)
			{
				$exm_paper_id =$qpverval[$i];
				
				//$ext_mark=$_POST['ext_mrk_'.$exm_paper_id];
				
				//$data['pract_ext_mark']	= $ext_mark;
				//$data['pract_ext_grade_entered_user']	= $_SESSION['user_tbl_id'];
				//$data['pract_ext_grade_entered_date']	=date("Y-m-d H:i:s");					
				$data['pract_ext_grade_status']	=1;
				$data['theory_ext_grade_status']	=1;
				$data['grade_verification']	=1;
				//$data['pract_ext_mark']
				
				$upd_qry	= $dbQrsObj->update("exam_papers", $data, "exam_paper_id=".$exm_paper_id);
			//	echo $dbQrsObj->_query;exit();
			}
			 $stud_Det		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on er.account_id=msa.account_id
	inner join exam_papers ep on ep.exam_regn_id=er.exam_regn_id inner join student_papers sp on sp.account_id=msa.account_id and sp.stud_paper_id=ep.stud_paper_id
inner join subject_master sm on sm.subject_id=sp.subject_id ",array("distinct PRN","account_student_name","ep.exam_paper_id","ep.pract_ext_mark","pract_ext_grade_status","ep.pract_ext_grade_entered_user"),"er.exam_id='".$crntExam."' and msa.college_id=".$crntCentre." and msa.specialisation_id=".$course." and sm.subject_name like'VIVA VOCE'  and ep.pract_ext_grade_entered_user!=".$_SESSION['user_tbl_id'],"msa.PRN" ); 
 }
 
 
 
 
 
 
 
 // if($_POST['btn_saveex'])
/*  if($_POST && !$_POST['btn_go'])
 {
	 if($_REQUEST['centre'] && $_REQUEST['qp'] && $_REQUEST['txt_prn'] && $_REQUEST['txt_falseno'])
	 {
		 $strspec="";
		// $crse=$_REQUEST['course'];
		 if($crse==1)
		 {
			 $strspec=" and msa.specialisation_id  in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)";
		 }
		 if($crse==2)
		 {
			  $strspec=" and msa.specialisation_id in (16,17,18,19)";
		 }
		 if($crse==3)
		 {
			  $strspec=" and msa.specialisation_id in (20)";
		 }
	 $prn=$_REQUEST['txt_prn'];
	 	 $flsno=$_REQUEST['txt_falseno'];
		 $flno=$flsno;
	 $qpfalsedetchk		= $dbQrsObj->selectFunction("qpcode_falseno_camp",array("falsestart","falseend"),"exam_id='".$_SESSION['crntExam']."' and ".$flsno." between  falsestart and falseend and qpcode='".$qpcode."'","qpcode" );
	  if(!$qpfalsedetchk)
	 {
		 $msg='FLNOTRNGE';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0); 
	 }
	 $prnclgchk		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id",array("er.exam_id"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and msa.prn='".$prn."'".$strspec,"" );
	 if(!$prnclgchk)
	 {
		  $msg='PRNINV';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0); 
	 }
	 $prnfalsechk		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on sp.subject_id=epm.subject_id and epm.qp_code=".$qpcode." and epm.exam_id=".$_SESSION['crntExam'],array("er.exam_id"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and msa.prn='".$prn."' and ep.bar_code is not null".$strspec,"" );
	 if($prnfalsechk)
	 {
		  $msg='PRNFLSEALRDMAP';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0); 
	 }
	  $falsechk		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id",array("er.exam_id"),"er.exam_id=".$_SESSION['crntExam']."  and ep.bar_code=".$flsno,"" );
	    if($falsechk)
	 {
		  $msg='FLSEALRDMAP';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0); 
	 }
	 /* $falsechkchfadl		= $dbQrsObj->selectFunction("examiner_falseno_camp",array("exam_id"),"exam_id=".$_SESSION['crntExam']." and qpcode=".$qpcode." and zone_id=".$accessZone." and ".$flsno." between st_range and en_range","" );
	    if(!$falsechkchfadl)
	 {
		  $msg='FLNOTMAPEXM';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0); 
	 }
	 
	   $selabsprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode,array("msa.prn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and msa.prn='".$prn."' and ep.prent_abscent!='P' and ep.bar_code is null".$strspec,"msa.prn" );
	  if($selabsprn)
	 {
		 $msg='PRNABS';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0); 
	 }
	  $prncamp		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("msa.prn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']."  and msa.prn='".$prn."' and ep.prent_abscent='P' and ep.bar_code is null".$strspec,"msa.prn" );
	  //echo $dbQrsObj->_query;exit();
	 if(!$prncamp)
	 {
		 $msg='PAPRNOTINTHISZONE';
	header("Location:index.php?module=assistantCampOfficer&page=falsenoprnmapping&qp=".$qpcode."&course=".$crse."&centre=".$crntCentre."&msg=".$msg);
	exit(0);  
	 }
	 
	
	
	 
	 
	 
	 
	 $selexmpapid		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode,array("ep.exam_paper_id"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and msa.prn='".$prn."'".$strspec,"" );
	
	 $dt	= date("Y-m-d H:i:s");
	$upd1['bar_code']				= $flsno;
				
//echo "sss".$bundle;

$upd1['generated_bar_code']		= $qpcode."*".$flsno;
		$upd1['bar_code_mapped_user']	= $_SESSION['user_tbl_id'];
		$upd1['false_number']			= $qpcode."*".$flsno;
		$upd1['bar_code_map_status']	= 0;
		//$updField['bundled']				= $bundle;
		$upd1['bar_code_mapped_date']	= $dt;
					$upd	= $dbQrsObj->update("exam_papers", $upd1, "exam_paper_id=".$selexmpapid[0]['exam_paper_id']);
				
	if($upd){
$dt	= date("Y-m-d H:i:s"); 
						$trackInsField['action']	= 47;
						$trackInsField['id1']		= $prn;
						$trackInsField['id2']		= $qpcode."*".$flsno;
						$trackInsField['done_by']	= $_SESSION['user_tbl_id'];
						$trackInsField['ip']		= $_SERVER['REMOTE_ADDR'];
						$trackInsField['entry_date']= $dt;
						$insTrack	= $dbQrsObj->insert("camp_log",$trackInsField);
					}
	 $selnextprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("msa.prn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and ep.prent_abscent='P' and ep.bar_code is null".$strspec,"msa.prn" );


$selregmapprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("count(msa.prn) as regprn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam'].$strspec,"" );
	 
	  $selabsmapprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("count(msa.prn) as absprn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and ep.prent_abscent!='P' and ep.bar_code is null".$strspec,"" );
	 
	   $selmapprn		= $dbQrsObj->selectFunction("mgucap_student_account msa inner join exam_registration er on msa.account_id=er.account_id inner join exam_papers ep on er.exam_regn_id=ep.exam_regn_id inner join student_papers sp on ep.stud_paper_id=sp.stud_paper_id inner join exam_papers_master epm on epm.subject_id=sp.subject_id and epm.exam_id=".$_SESSION['crntExam'] ." and epm.qp_code=".$qpcode." inner join camp_college_qpcode_map ccqm on  epm.id=ccqm.exam_paper_master_id and ccqm.college_id=".$crntCentre." and ccqm.zone_id=".$accessZone,array("count(msa.prn) as mapprn"),"msa.college_id=".$crntCentre." and er.exam_id=".$_SESSION['crntExam']." and ep.prent_abscent='P' and ep.bar_code is not null and ep.bar_code_map_status in(0,1)".$strspec,"" );
	   
	  


	$flno++;
	$nxtprn=$selnextprn[0]['prn'];
	 
	 }
	 }
 if($qpcode)
 {
	  $qpfalsedet		= $dbQrsObj->selectFunction("qpcode_falseno_camp",array("falsestart","falseend"),"exam_id='".$_SESSION['crntExam']."' and qpcode='".$qpcode."'","qpcode" );
	$selZoneDtls= $dbQrsObj->selectFunctionSingle("zone_master","zone_id,zone_name,zone_code","zone_id=".$accessZone);
	$zoneCode	= $selZoneDtls['zone_code'];  
 
 }*/

 $body	= "falsenomarkver_viva.tpl";
?>