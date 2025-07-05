/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Moderation
{
    public void ApplyModeration(String BranchId,String ExamId,String YearSem) throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0  group by StudentId ");
            //PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId inner join StudentPersonal p on p.StudentId=m.StudentId left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and p.JoiningYear!=2011 group by StudentId ");
           
            // PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  inner join StudentPersonal sp on sp.StudentId=m.StudentId and sp.JoiningYear=2011 left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.EndDate is null group by StudentId");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student1=FailedStudents.executeQuery();
            while(Student1.next())
            {
                if(!IsModerationAppliedForSemPass(BranchId, ExamId, YearSem, con))
                {//get the moderation categories defined for branch
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1)
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student1.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                System.out.println("amma1-"+Student1.getString("StudentId")+NeededMarks+TotalModeration);
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        int PassMin=0;
                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            PassMin=5;
                                        }
                                        else
                                        {
                                            PassMin=13;
                                        }
                                        IntModeration=PassMin-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);

                                       // int PassMin=0;

//                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
//                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
                                    }
                                    else
                                    {
                                        int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status
                                    System.out.println("amma1");
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student1.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark")+ExtModeration, Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student1.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }

                                }
                                else if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                {
                                    int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status
                                     System.out.println("amma2");
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student1.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        System.out.println("amma3");
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark")+ExtModeration, Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student1.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }

                            }
                        }
                        else
                        {
                            if((!IsStudentAbsentForAnySubjectOfSemester(Student1.getString("StudentId"), ExamId, YearSem, con)))
                            {
                                 int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student1.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                       /* IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");*/
                                        int PassMin=0;
                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            PassMin=5;
                                        }
                                        else
                                        {
                                            PassMin=13;
                                        }
                                        IntModeration=PassMin-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
                                    }
                                    else
                                    {
                                         int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status
                                        System.out.println("amma4");
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student1.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                       System.out.println("amma5");
                                        //Update internal  
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student1.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }

                                }
                            }
                            }
                        }
                    }
                }
                else
                {
                    PreparedStatement Pass=con.prepareStatement("select distinct StudentId,sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.IsPassed=0 and m.IsAbsent=0 and m.IsMalPractice=0 and m.StudentId=?");
                    Pass.setString(1, YearSem);
                    Pass.setString(2, Student1.getString("StudentId"));
                    ResultSet SemPass=Pass.executeQuery();
                    while(SemPass.next())
                    {
                        if(getTotalModerationForBranch(BranchId, ExamId, YearSem, con)+SemPass.getInt("Mark")>0 && (!IsStudentAbsentForAnySubjectOfSemester(Student1.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1 && (!IsStudentAbsentForAnySubjectOfSemester(Student1.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student1.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                               if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        //IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        /* IntModeration=13-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");*/
                                        int PassMin=0;
                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            PassMin=5;
                                        }
                                        else
                                        {
                                            PassMin=13;
                                        }
                                        IntModeration=PassMin-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
                                    }
                                    else
                                    {
                                        int Pass1;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass1=28;
                                        }
                                        else
                                        {
                                            Pass1=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass1 && Marks.getInt("ExternalMark")<Pass1)
                                            ExtModeration=Pass1-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status
                                    System.out.println("amma6");
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student1.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {System.out.println("amma7");
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student1.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                        }
                    }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
 public void ApplyModeration_BSc(String BranchId,String ExamId,String YearSem) throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0  group by StudentId ");
           // PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  inner join StudentPersonal sp on sp.StudentId=m.StudentId and sp.JoiningYear=2011 left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.EndDate is null group by StudentId");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student1=FailedStudents.executeQuery();
            while(Student1.next())
            {
                if(!IsModerationAppliedForSemPass(BranchId, ExamId, YearSem, con))
                {//get the moderation categories defined for branch
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1)
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student1.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        /*int PassMin=0;
                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            PassMin=5;
                                        }
                                        else
                                        {
                                            PassMin=13;
                                        }
                                        IntModeration=PassMin-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);

                                       // int PassMin=0;

//                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
//                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
*                                                  */
                                    }
                                    else
                                    {
                                        int Pass;
                                         if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            Pass=20;
                                        }
                                        else
                                        {
                                            Pass=37;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass){
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");}
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? , IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                  
                                    saveModeration.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                    saveModeration.setString(3, Student1.getString("StudentId"));
                                    saveModeration.setString(4, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(5, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=?,IsPassed=1,TotalMark=?  where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                         UpdateInternal.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                        UpdateInternal.setString(3, Student1.getString("StudentId"));
                                        UpdateInternal.setString(4, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(5, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }

                                }
                                else if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                {
                                    int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            Pass=20;
                                        }
                                        else
                                        {
                                            Pass=37;
                                        }
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass){
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");}
                                        }
                                    TotalModeration-=ExtModeration;
                                    //Update moderation,Pass status
if(ExtModeration!=0&&TotalModeration>0 ){
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? , IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    
                                    saveModeration.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                    saveModeration.setString(3, Student1.getString("StudentId"));
                                    saveModeration.setString(4, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(5, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                         UpdateInternal.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                        UpdateInternal.setString(3, Student1.getString("StudentId"));
                                        UpdateInternal.setString(4, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(5, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
}
                                }

                            }
                        }
                        else
                        {
                            if((!IsStudentAbsentForAnySubjectOfSemester(Student1.getString("StudentId"), ExamId, YearSem, con)))
                            {
                                 int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student1.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");
                                    }
                                    else
                                    {
                                         int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                           if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            Pass=16;
                                        }
                                        else
                                        {
                                            Pass=37;
                                        }
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status
if(ExtModeration!=0&&TotalModeration>0 ){
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student1.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                         UpdateInternal.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                        UpdateInternal.setString(3, Student1.getString("StudentId"));
                                        UpdateInternal.setString(4, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(5, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
}
                                }
                            }
                            }
                        }
                    }
                }
                else
                {
                    PreparedStatement Pass=con.prepareStatement("select distinct StudentId,sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.IsPassed=0 and m.IsAbsent=0 and m.IsMalPractice=0 and m.StudentId=?");
                    Pass.setString(1, YearSem);
                    Pass.setString(2, Student1.getString("StudentId"));
                    ResultSet SemPass=Pass.executeQuery();
                    while(SemPass.next())
                    {
                        if(getTotalModerationForBranch(BranchId, ExamId, YearSem, con)+SemPass.getInt("Mark")>0 && (!IsStudentAbsentForAnySubjectOfSemester(Student1.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1 && (!IsStudentAbsentForAnySubjectOfSemester(Student1.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student1.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                               if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");
                                    }
                                    else
                                    {
                                        int Pass1;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass1=28;
                                        }
                                        else
                                        {
                                           if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            Pass1=16;
                                        }
                                        else
                                        {
                                            Pass1=37;
                                        }
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass1 && Marks.getInt("ExternalMark")<Pass1)
                                            ExtModeration=Pass1-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status
if(ExtModeration!=0&&TotalModeration>0 ){
                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student1.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                        UpdateInternal.setString(3, Student1.getString("StudentId"));
                                        UpdateInternal.setString(4, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(5, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }
                                }
                            }
                        }
                    }
                        }
                    }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
    public void ApplyModerationToMBA(String BranchId,String ExamId,String YearSem) throws Exception
    {
         Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct StudentId,b.BranchId,sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and Studentid not in(select distinct StudentId from StudentExamWithheldDetails where Status='MalPractice' and EndDate is null) group by StudentId ");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                if(!IsModerationAppliedForSemPass(BranchId, ExamId, YearSem, con))
                {//get the moderation categories defined for branch
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {                        
                         int TotalModeration=0;
                         if(IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con))
                         {
                             TotalModeration=5;
                         }
                         else
                         {
                            TotalModeration=ModerationId.getInt("TotalModeration");
                         }
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=0;
                                 if(IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con))
                                 {
                                     PerPaperMax=5;
                                 }
                                 else
                                 {
                                    PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                 }
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
//                                        int PassMin=0;
//                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
//                                        {
//                                            PassMin=5;
//                                        }
//                                        else
//                                        {
//                                            PassMin=13;
//                                        }
//                                        IntModeration=PassMin-Marks.getInt("InternalMark");
//                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);

                                        int PassMin=0;

                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
                                    }
                                    else
                                    {
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                }
                            }
                    }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
    
    public void ApplyModerationToBLiSc_old(String BranchId,String ExamId,String YearSem) throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            int Total=getTotalPassMarkForBLiSc(BranchId, YearSem, con);
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct StudentId,b.BranchId,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark,b.PassMark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where m.ExamId=? and m.IsValid=1  group by StudentId ");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                if(!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"),ExamId,YearSem,con))
                {
                    System.out.println("-6"+Student.getInt("TotalMark")+'#'+Total);
                    if(Student.getInt("TotalMark")<Total || IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                    {
                        
                        int ReqMarkForSemPass=getTotalPassMarkForBLiSc(BranchId, YearSem, con)-Student.getInt("TotalMark");
                        System.out.println("-5"+ReqMarkForSemPass);
                        PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                        BranchModeration.setString(1, BranchId);
                        BranchModeration.setString(2, YearSem);
                        BranchModeration.setString(3, ExamId);
                        ResultSet ModerationId=BranchModeration.executeQuery();
                        while(ModerationId.next())
                        {
                            int TotalModerationApp=0;
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            int NeededMarks=0;
                            if(ReqMarkForSemPass > getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem))
                            {
                                NeededMarks=ReqMarkForSemPass;
                            }
                            else
                            {
                                NeededMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem);
                            }
                            if(ReqMarkForSemPass<=TotalModeration && getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem)<=TotalModeration && NeededMarks<=TotalModeration)
                            {
                                int ExtModeration=0;

                                String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                                PreparedStatement Subject=con.prepareStatement(Query);
                                Subject.setString(1, Student.getString("StudentId"));
                                Subject.setString(2, ExamId);
                                ResultSet Marks=Subject.executeQuery();
                                while(Marks.next())
                                {

                                    int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                    if(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")<Marks.getInt("PassMark") && Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"))<=PerPaperMax )
                                    {
                                         ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));

                                         PerPaperMax-=ExtModeration;
                                         TotalModeration-=ExtModeration;
                                         TotalModerationApp+=ExtModeration;
                                      //   system.out.printl
                                         PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? , IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                         saveModeration.setInt(1,ExtModeration);
                                         saveModeration.setInt(2, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration);
                                         saveModeration.setString(3, Student.getString("StudentId"));
                                         saveModeration.setString(4, Marks.getString("SubjectBranchId"));
                                         saveModeration.setString(5, ExamId);
                                         saveModeration.executeUpdate();
                                         //con.commit();
                                    }
                                }
                                int AppModeration=getTotalPassMarkForBLiSc(BranchId, YearSem, con)-(Student.getInt("TotalMark")+TotalModerationApp);
                                //Apply ModerationForSemPass
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    int RequiredModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                    int PassMark=0;

                                    PreparedStatement TotalPass=con.prepareStatement("select b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.StudentId=? and m.ExamId=? and  m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by b.PaperNo ");
                                    TotalPass.setString(1, YearSem);
                                    TotalPass.setString(2, Student.getString("StudentId"));
                                    TotalPass.setString(3, ExamId);
                                    ResultSet Pass=TotalPass.executeQuery();
                                    while(Pass.next())
                                    {
                                        int Count=1;
                                        if(YearSem.equals("1"))
                                        {
                                            Count=4;
                                        }
                                        if(YearSem.equals("2"))
                                        {
                                            Count=6;
                                        }
                                        if(Pass.isLast())
                                        {
                                            ExtModeration=AppModeration;
                                        }
                                        else
                                        {
                                            ExtModeration=RequiredModeration/Count;
                                        }
                                         AppModeration-=ExtModeration;
                                         PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? , IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                         saveModeration.setInt(1,ExtModeration);
                                         saveModeration.setInt(2, Pass.getInt("InternalMark")+Pass.getInt("ExternalMark")+ExtModeration);
                                         saveModeration.setString(3, Student.getString("StudentId"));
                                         saveModeration.setString(4, Pass.getString("SubjectBranchId"));
                                         saveModeration.setString(5, ExamId);
                                         saveModeration.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }

    private int getTotalPassMarkForIndividualSubjectsOfBLiSc(String StudentId,String YearSem)
    {
        Connection con2=null;
        try
        {

             con2=new DBConnection().getConnection();
            PreparedStatement st2=con2.prepareStatement("select sum(b.PassMark-(m.ExternalMark+m.InternalMark)) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and b.CurrentYearSem=?  where m.StudentId=? and m.IsPassed=0 and m.ExamId=?");
            st2.setString(1, YearSem);
            st2.setString(2, StudentId);
            st2.setInt(3, 5);
            ResultSet rs2=st2.executeQuery();
            while(rs2.next())
            {
                return rs2.getInt(1);
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        finally{
            try {

                 if(!con2.isClosed())
            {
                con2.close();
            }
            } catch (SQLException ex) {
                Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean IsStudentFailedForAnyPaper(String StudentId,String ExamId,String YearSem)
    {
        Boolean flag =false;
        Connection con1=null;
        try
        {

             con1=new DBConnection().getConnection();
            PreparedStatement st1=con1.prepareStatement("select b.PassMark-(ExternalMark+InternalMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.StudentId=? and m.ExamId=? ");
            st1.setString(1, YearSem);
            st1.setString(2, StudentId);
            st1.setString(3, ExamId);
            ResultSet rs1=st1.executeQuery();
            while(rs1.next())
            {
               int x= rs1.getInt("Mark");
                // System.out.println('%'+x+'%');
                if(x>=0)
                {
                    
                   flag=true;
                }
               
            }
            if(flag==true)
            {
                //System.out.println("%true%");
                return true;
            }
            return false;

        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }
        finally{
            try {

                 if(!con1.isClosed())
            {
                con1.close();
            }
            } catch (SQLException ex) {
                Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int getTotalModerationForBranch(String BranchId,String ExamId,String YearSem,Connection con)
    {
        try
        {
            PreparedStatement st=con.prepareStatement("select sum(TotalModeartion) from BranchModeration where BranchId=? and ExamId=? and YearSem=?");
            st.setString(1, BranchId);
            st.setString(2, ExamId);
            st.setString(3, YearSem);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    private boolean IsStudentAbsentForAnySubjectOfSemester(String StudentId,String ExamId,String YearSem,Connection con)
    {
        try
        {
            PreparedStatement st=con.prepareStatement("select count(*) as Count from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where (m.IsAbsent=1 or m.IsMalPractice=1 or TempUnconfirmed=1) and m.IsValid=1 and m.StudentId=? and m.ExamId=? ");
            st.setString(1, YearSem);
            st.setString(2, StudentId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                System.out.println("--8--"+rs.getInt("Count"));
                if(rs.getInt("Count")>0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean IsModerationAppliedForSemPass(String BranchId,String ExamId,String YearSem, Connection con)
    {
        try
        {
            PreparedStatement st=con.prepareStatement("select IsModeartonAppliedForSemPass from BranchModeration where BranchId=? and ExamId=? and YearOrSem=?");
            st.setString(1, BranchId);
            st.setString(2, ExamId);
            st.setString(3, YearSem);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt("IsModeartonAppliedForSemPass")==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String getSubjectBranchIdsForBranchModeration(String BranchModerationId,Connection con)
    {
            Connection con2=null;
             String SubjectBranchId="";
        try
        {
             con2=new DBConnection().getConnection();
       
            PreparedStatement st2=con2.prepareStatement("select SubjectBranchId from SubjectWiseModeration where BranchModerationId=?");
            st2.setString(1, BranchModerationId);
            ResultSet rs2=st2.executeQuery();

            while(rs2.next())
            {
                SubjectBranchId+=rs2.getString("SubjectBranchId")+",";
            }
            String Mark=SubjectBranchId.substring(0,SubjectBranchId.length()-1);
            return Mark;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
              finally{
            try {

                 if(!con2.isClosed())
            {
                con2.close();
            }
            } catch (SQLException ex) {
                Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private long CalculateInternal(int ExternalMark,String SubjectBranchId,Connection con) throws SQLException
    {
    
    long Internal;
        try
        {
           
            PreparedStatement st=con.prepareStatement("select InternalEvaluationValue from SubjectBranchMaster where SubjectBranchId=?");
            st.setString(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                Internal=Math.round(ExternalMark*rs.getDouble("InternalEvaluationValue"));
            }
            else
            {
                Internal=0;
            }
            return Internal;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
 
}
  private long CalculateInternalforBcom(int ExternalMark,int SubjectBranchId,Connection con) throws SQLException
    {

    long Internal;
        try
        {

            PreparedStatement st=con.prepareStatement("select InternalEvaluationValue from SubjectBranchMaster where SubjectBranchId=?");
            st.setInt(1, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                Internal=Math.round(ExternalMark*rs.getDouble("InternalEvaluationValue"));
            }
            else
            {
                Internal=0;
            }
            return Internal;

        }
        catch (SQLException ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

}
    private int getTotalPassMarkForBLiSc(String BranchId,String YearSem,Connection con)
    {
         try
        {
            PreparedStatement st=con.prepareStatement("select sum(TotalMark) from SubjectBranchMaster where Branchid=? and CurrentYearSem=?");
            st.setString(1, BranchId);
            st.setString(2, YearSem);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1)/2;
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
       private int getBcomM()
    {
           Connection con=null;
         try
        {
            con=new DBConnection().getConnection();

            PreparedStatement st=con.prepareStatement("select StudentId from tt");

            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                PreparedStatement BranchModeration=con.prepareStatement("select InternalMark from StudentSubjectMark where SubjectBranchId=207 and  ExamId=2 and StudentId=?");
                    BranchModeration.setString(1, rs.getString("StudentId"));
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {

                 int t=   Integer.parseInt( ModerationId.getString("InternalMark"))-1;

                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=207 and ExamId=2 and Isvalid=1 ");

                                saveModeration.setInt(1, t);
                                saveModeration.setString(2, rs.getString("StudentId"));

                                saveModeration.executeUpdate();
                    }
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
           finally{
            try {

                 if(!con.isClosed())
            {
                con.close();
            }
            } catch (SQLException ex) {
                Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            }}
    }
     public void ApplyModerationLLM(String BranchId,String ExamId,String YearSem) throws Exception
{
    Connection con=null;
    try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            //int Total=getTotalPassMarkForBLiSc(BranchId, YearSem, con);
            int Total=275;
            con.setAutoCommit(false);
           // PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark ,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark,count(m.SubjectBranchId) cnt from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.StudentExamWithheldId is null group by StudentId ");
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark ,Ifnull(a.TheoryMark,0) TheoryMark, " +
" sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark,count(m.SubjectBranchId) cnt" +
" from StudentSubjectMark m" +
" inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId " +
"left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId " +
"left join " +
"(select m1.StudentId,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TheoryMark from StudentSubjectMark m1 " +
"inner join SubjectBranchMaster b1 on b1.SubjectBranchId=m1.SubjectBranchId " +

"left join  StudentExamWithheldDetails d1 on m1.StudentId=d1.StudentId  where " +

" m1.ExamId=? and  m1.IsAbsent=0 and b1.BranchId=? and b1.CurrentYearSem=? " +
"and m1.IsValid=1 and m1.IsMalPractice=0 and d1.StudentExamWithheldId is null " +
" and m1.SubjectBranchId in(608,607,605,610,611,609,614,613,612,617,616,615) group by StudentId) a " +
"on a.StudentId=m.StudentId " +
"where  m.ExamId=? and b.BranchId=? and b.CurrentYearSem=? " +
"and  m.IsAbsent=0 " +
"and m.IsValid=1 and m.IsMalPractice=0 and d.StudentExamWithheldId is null  group by StudentId ");
            FailedStudents.setString(1, ExamId);
            FailedStudents.setString(2, BranchId);
            FailedStudents.setString(3, YearSem);
            FailedStudents.setString(4, ExamId);
              FailedStudents.setString(5, BranchId);
            FailedStudents.setString(6, YearSem);
            ResultSet Student=FailedStudents.executeQuery();

            while(Student.next())
            {
                if(Student.getInt("TotalMark")>=Total )                //Student Totalmark greater than 175 moderation for pass(40)
                {
                    if(Student.getInt("TheoryMark")>=150 ) {
                     if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        int TotalModeration=ModerationId.getInt("TotalModeration");
                        //get the mak details of the failed subjects in a selected moderation category for each student
                        String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                        PreparedStatement Subject=con.prepareStatement(Query);
                        Subject.setString(1, Student.getString("StudentId"));
                        Subject.setString(2, ExamId);
                        ResultSet Marks=Subject.executeQuery();
                        while(Marks.next())
                        {
                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                            int IntModeration=0,ExtModeration=0;
                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                            int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                            if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                            {
                                ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));

                                TotalModeration-=IntModeration+ExtModeration;
                                //Update moderation,Pass status
                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                saveModeration.setInt(1,ExtModeration);
                                saveModeration.setInt(2,IntModeration);
                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                saveModeration.setString(4, Student.getString("StudentId"));
                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                saveModeration.setString(6, ExamId);
                                saveModeration.executeUpdate();
                            }
                        }

                    }}}else{
                        int needforpassTheory=150-Student.getInt("TheoryMark");
                        if(16>=needforpassTheory)
                        {
                            if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                                int TotalModerationApp=0;
                                int  TotalModeration=needforpassTheory-TotalModerationApp;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                     TotalModeration=needforpassTheory-TotalModerationApp;
                                    //int TotalModeration=ModerationId.getInt("TotalModeration");
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                        {
                                           
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            TotalModeration-=IntModeration+ExtModeration;
                                            TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                            PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                            saveModeration.setInt(1,ExtModeration);
                                            saveModeration.setInt(2,IntModeration);
                                            saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                            saveModeration.setString(4, Student.getString("StudentId"));
                                            saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                            saveModeration.setString(6, ExamId);
                                            saveModeration.executeUpdate();
                                        }
                                      }
                                }
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                    BranchModeration1.setString(1, BranchId);
                                    BranchModeration1.setString(2, YearSem);
                                    BranchModeration1.setString(3, ExamId);
                                   ModerationId=BranchModeration1.executeQuery();
                                    while(ModerationId.next())
                                    {
                                          TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                        //get the mak details of the failed subjects in a selected moderation category for each student
                                        String Query="select distinct b.SubjectBranchId,(ExternalMark+ModerationExt) as ExternalMark,(InternalMark+ModerationInt) as InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                        PreparedStatement Subject=con.prepareStatement(Query);
                                        Subject.setString(1, Student.getString("StudentId"));
                                        Subject.setString(2, ExamId);
                                        ResultSet Marks1=Subject.executeQuery();
                                        while(Marks1.next())
                                        {
                                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                            int IntModeration=0,ExtModeration=0;
                                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                            int NeededMarks=Marks1.getInt("PMark")-(Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark"));
                                            if(NeededMarks<=PerPaperMax   && Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+PerPaperMax >=Marks1.getInt("PMark")  )
                                            {
                                                TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                                ExtModeration=Marks1.getInt("PMark")-(Marks1.getInt("ExternalMark")+Marks1.getInt("InternalMark"));
                                                if(ExtModeration>0)
                                                {
                                                    if(TotalModeration>=ExtModeration){
                                                        TotalModeration-=IntModeration+ExtModeration;
                                                    TotalModerationApp=TotalModerationApp+ExtModeration;}
                                                    else{
                                                          ExtModeration=TotalModeration;
                                                          TotalModerationApp=TotalModerationApp+ExtModeration;
                                                        }
                                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? ,ModerationInt=ModerationInt+?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                    saveModeration.setInt(1,ExtModeration);
                                                    saveModeration.setInt(2,IntModeration);
                                                    saveModeration.setInt(3, Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                    saveModeration.setString(4, Student.getString("StudentId"));
                                                    saveModeration.setString(5, Marks1.getString("SubjectBranchId"));
                                                    saveModeration.setString(6, ExamId);
                                                    saveModeration.executeUpdate();
                                                    }
                                            }

                                        }

                                    }

                                }
                            }else{
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpassTheory-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }}
                    
                    }

                }else{
                        int needforpass=Total-Student.getInt("TotalMark");
                        int needforpassTheory=150-Student.getInt("TheoryMark");
                        if(16>=needforpass)
                        {
                            if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                                int TotalModerationApp=0;
                                int  TotalModeration=needforpass-TotalModerationApp;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                     TotalModeration=needforpass-TotalModerationApp;
                                    //int TotalModeration=ModerationId.getInt("TotalModeration");
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                        {
                                           
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            TotalModeration-=IntModeration+ExtModeration;
                                            TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                            PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                            saveModeration.setInt(1,ExtModeration);
                                            saveModeration.setInt(2,IntModeration);
                                            saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                            saveModeration.setString(4, Student.getString("StudentId"));
                                            saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                            saveModeration.setString(6, ExamId);
                                            saveModeration.executeUpdate();
                                        }
                                      }
                                }
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                    BranchModeration1.setString(1, BranchId);
                                    BranchModeration1.setString(2, YearSem);
                                    BranchModeration1.setString(3, ExamId);
                                   ModerationId=BranchModeration1.executeQuery();
                                    while(ModerationId.next())
                                    {
                                          TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                        //get the mak details of the failed subjects in a selected moderation category for each student
                                        String Query="select distinct b.SubjectBranchId,(ExternalMark+ModerationExt) as ExternalMark,(InternalMark+ModerationInt) as InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                        PreparedStatement Subject=con.prepareStatement(Query);
                                        Subject.setString(1, Student.getString("StudentId"));
                                        Subject.setString(2, ExamId);
                                        ResultSet Marks1=Subject.executeQuery();
                                        while(Marks1.next())
                                        {
                                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                            int IntModeration=0,ExtModeration=0;
                                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                            int NeededMarks=Marks1.getInt("PMark")-(Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark"));
                                            if(NeededMarks<=PerPaperMax   && Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+PerPaperMax >=Marks1.getInt("PMark")  )
                                            {
                                                TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                                ExtModeration=Marks1.getInt("PMark")-(Marks1.getInt("ExternalMark")+Marks1.getInt("InternalMark"));
                                                if(ExtModeration>0)
                                                {
                                                    if(TotalModeration>=ExtModeration){
                                                        TotalModeration-=IntModeration+ExtModeration;
                                                    TotalModerationApp=TotalModerationApp+ExtModeration;}
                                                    else{
                                                          ExtModeration=TotalModeration;
                                                          TotalModerationApp=TotalModerationApp+ExtModeration;
                                                        }
                                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? ,ModerationInt=ModerationInt+?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                    saveModeration.setInt(1,ExtModeration);
                                                    saveModeration.setInt(2,IntModeration);
                                                    saveModeration.setInt(3, Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                    saveModeration.setString(4, Student.getString("StudentId"));
                                                    saveModeration.setString(5, Marks1.getString("SubjectBranchId"));
                                                    saveModeration.setString(6, ExamId);
                                                    saveModeration.executeUpdate();
                                                    }
                                            }

                                        }

                                    }

                                }
                            }else{
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpass-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(16>=needforpassTheory)
                        {
                            if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                                int TotalModerationApp=0;
                                int  TotalModeration=needforpassTheory-TotalModerationApp;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                     TotalModeration=needforpassTheory-TotalModerationApp;
                                    //int TotalModeration=ModerationId.getInt("TotalModeration");
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                        {
                                           
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            TotalModeration-=IntModeration+ExtModeration;
                                            TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                            PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                            saveModeration.setInt(1,ExtModeration);
                                            saveModeration.setInt(2,IntModeration);
                                            saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                            saveModeration.setString(4, Student.getString("StudentId"));
                                            saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                            saveModeration.setString(6, ExamId);
                                            saveModeration.executeUpdate();
                                        }
                                      }
                                }
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                    BranchModeration1.setString(1, BranchId);
                                    BranchModeration1.setString(2, YearSem);
                                    BranchModeration1.setString(3, ExamId);
                                   ModerationId=BranchModeration1.executeQuery();
                                    while(ModerationId.next())
                                    {
                                          TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                        //get the mak details of the failed subjects in a selected moderation category for each student
                                        String Query="select distinct b.SubjectBranchId,(ExternalMark+ModerationExt) as ExternalMark,(InternalMark+ModerationInt) as InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                        PreparedStatement Subject=con.prepareStatement(Query);
                                        Subject.setString(1, Student.getString("StudentId"));
                                        Subject.setString(2, ExamId);
                                        ResultSet Marks1=Subject.executeQuery();
                                        while(Marks1.next())
                                        {
                                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                            int IntModeration=0,ExtModeration=0;
                                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                            int NeededMarks=Marks1.getInt("PMark")-(Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark"));
                                            if(NeededMarks<=PerPaperMax   && Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+PerPaperMax >=Marks1.getInt("PMark")  )
                                            {
                                                TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                                ExtModeration=Marks1.getInt("PMark")-(Marks1.getInt("ExternalMark")+Marks1.getInt("InternalMark"));
                                                if(ExtModeration>0)
                                                {
                                                    if(TotalModeration>=ExtModeration){
                                                        TotalModeration-=IntModeration+ExtModeration;
                                                    TotalModerationApp=TotalModerationApp+ExtModeration;}
                                                    else{
                                                          ExtModeration=TotalModeration;
                                                          TotalModerationApp=TotalModerationApp+ExtModeration;
                                                        }
                                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? ,ModerationInt=ModerationInt+?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                    saveModeration.setInt(1,ExtModeration);
                                                    saveModeration.setInt(2,IntModeration);
                                                    saveModeration.setInt(3, Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                    saveModeration.setString(4, Student.getString("StudentId"));
                                                    saveModeration.setString(5, Marks1.getString("SubjectBranchId"));
                                                    saveModeration.setString(6, ExamId);
                                                    saveModeration.executeUpdate();
                                                    }
                                            }

                                        }

                                    }

                                }
                            }else{
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpassTheory-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }}else{
                            PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                            BranchModeration.setString(1, BranchId);
                            BranchModeration.setString(2, YearSem);
                            BranchModeration.setString(3, ExamId);
                            ResultSet ModerationId=BranchModeration.executeQuery();
                            while(ModerationId.next())
                            {
                                int TotalModeration=ModerationId.getInt("TotalModeration");
                                //get the mak details of the failed subjects in a selected moderation category for each student
                                String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                PreparedStatement Subject=con.prepareStatement(Query);
                                Subject.setString(1, Student.getString("StudentId"));
                                Subject.setString(2, ExamId);
                                ResultSet Marks=Subject.executeQuery();
                                while(Marks.next())
                                {
                                    int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                    int IntModeration=0,ExtModeration=0;
                                    //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                    int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                    if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PMark"))
                                    {
                                        ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        TotalModeration-=IntModeration+ExtModeration;
                                        //Update moderation,Pass status
                                        PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        saveModeration.setInt(1,ExtModeration);
                                        saveModeration.setInt(2,IntModeration);
                                        saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                        saveModeration.setString(4, Student.getString("StudentId"));
                                        saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                        saveModeration.setString(6, ExamId);
                                        saveModeration.executeUpdate();
                                    }
                                }
                            }
                        }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
            con.close();
            }
        }
}

     public void ApplyModerationforBLISc(String BranchId,String ExamId,String YearSem) throws Exception
{
   Connection con=null;
    try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            int NMarks=0;
            int Total=getTotalPassMarkForBLiSc(BranchId, YearSem, con);
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark ,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null where b.BranchId=? and b.CurrentYearSem=? and m.markstatus=1 and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0  group by StudentId ");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
           // FailedStudents.setString(3, ExamId);
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                System.out.println("1*"+Student.getString("StudentId")+'-'+Total+'-'+Student.getInt("TotalMark"));
                if(Student.getInt("TotalMark")>=Total )                //Student Totalmark greater than 175 moderation for pass(40)
                {
                     if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                         NMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem);
                          if(20>=NMarks){
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        int TotalModeration=ModerationId.getInt("TotalModeration");
                        //get the mak details of the failed subjects in a selected moderation category for each student
                        String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                        PreparedStatement Subject=con.prepareStatement(Query);
                        Subject.setString(1, Student.getString("StudentId"));
                        Subject.setString(2, ExamId);
                        ResultSet Marks=Subject.executeQuery();
                        while(Marks.next())
                        {
                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                            int IntModeration=0,ExtModeration=0;
                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                             int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                            if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                            {
                                ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));

                                TotalModeration-=IntModeration+ExtModeration;
                                //Update moderation,Pass status
                                
                                System.out.println("amma4");
                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                saveModeration.setInt(1,ExtModeration);
                                saveModeration.setInt(2,IntModeration);
                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                saveModeration.setString(4, Student.getString("StudentId"));
                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                saveModeration.setString(6, ExamId);
                                saveModeration.executeUpdate();
                            }
                        }

                    }

                          }
                     }

                }else{
                        int needforpass=Total-Student.getInt("TotalMark");
                        System.out.println("2*"+Student.getString("StudentId")+'-'+Total+'-'+Student.getInt("TotalMark")+'-'+needforpass);
                        if(20>=needforpass)
                        {
                           // System.out.println("amma");
                            if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                              //  System.out.println("0-amma");
                                NMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem);
                                 if(20>=NMarks){
                                  //   System.out.println("1-amma");
                                int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                    //int  TotalModeration=needforpass-TotalModerationApp;
                                    int TotalModeration=ModerationId.getInt("TotalModeration");
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                      //  System.out.println("2-amma");
                                        if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                        {
                                         //   System.out.println("3-amma");
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            TotalModeration-=IntModeration+ExtModeration;
                                            TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                            System.out.println("amma3");
                                            PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                            saveModeration.setInt(1,ExtModeration);
                                            saveModeration.setInt(2,IntModeration);
                                            saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                            saveModeration.setString(4, Student.getString("StudentId"));
                                            saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                            saveModeration.setString(6, ExamId);
                                            saveModeration.executeUpdate();
                                        }
                                      }
                                }
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                    BranchModeration1.setString(1, BranchId);
                                    BranchModeration1.setString(2, YearSem);
                                    BranchModeration1.setString(3, ExamId);
                                   ModerationId=BranchModeration1.executeQuery();
                                    while(ModerationId.next())
                                    {
                                        int  TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                        //get the mak details of the failed subjects in a selected moderation category for each student
                                        String Query="select distinct b.SubjectBranchId,(ExternalMark+ModerationExt) as ExternalMark,(InternalMark+ModerationInt) as InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                        PreparedStatement Subject=con.prepareStatement(Query);
                                        Subject.setString(1, Student.getString("StudentId"));
                                        Subject.setString(2, ExamId);
                                        ResultSet Marks1=Subject.executeQuery();
                                        while(Marks1.next())
                                        {
                                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                            int IntModeration=0,ExtModeration=0;
                                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                            int NeededMarks=Marks1.getInt("PMark")-(Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark"));
                                            if(NeededMarks<=PerPaperMax   && Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+PerPaperMax >=Marks1.getInt("PMark")  )
                                            {
                                                ExtModeration=Marks1.getInt("PMark")-(Marks1.getInt("ExternalMark")+Marks1.getInt("InternalMark"));
                                                if(ExtModeration>0)
                                                {
                                                    if(TotalModeration>=ExtModeration){
                                                        TotalModeration-=IntModeration+ExtModeration;}
                                                    else{
                                                          ExtModeration=TotalModeration;
                                                          TotalModeration-=IntModeration+ExtModeration;
                                                        }
                                                     System.out.println("amma1");
                                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? ,ModerationInt=ModerationInt+?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                    saveModeration.setInt(1,ExtModeration);
                                                    saveModeration.setInt(2,IntModeration);
                                                    saveModeration.setInt(3, Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                    saveModeration.setString(4, Student.getString("StudentId"));
                                                    saveModeration.setString(5, Marks1.getString("SubjectBranchId"));
                                                    saveModeration.setString(6, ExamId);
                                                    saveModeration.executeUpdate();
                                                    }
                                            }

                                        }

                                    }

                                }

                                 }

                            }else{
                                 System.out.println("3*"+Student.getString("StudentId")+'-'+Total+'-'+Student.getInt("TotalMark"));
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpass-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                 System.out.println("amma5");
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        /*else{
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                System.out.println('@'+Student.getString("StudentId")+'-'+Total+'-'+Student.getInt("TotalMark")+'-'+needforpass);
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpass-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                 System.out.println("amma2");
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }*/
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
            con.close();
            }
        }
}

     public void ApplyModerationforBLISc1(String BranchId,String ExamId,String YearSem) throws Exception
{
   Connection con=null;
    try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            int NMarks=0;
            int Total=getTotalPassMarkForBLiSc(BranchId, YearSem, con);
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark ,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0  group by StudentId ");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                System.out.println("--"+Student.getInt("TotalMark")+"#"+Total);
                if(Student.getInt("TotalMark")>=Total )                //Student Totalmark greater than 175 moderation for pass(40)
                {
                     if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                         NMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem);
                          if(20>=NMarks){
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        int TotalModeration=ModerationId.getInt("TotalModeration");
                        //get the mak details of the failed subjects in a selected moderation category for each student
                        String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                        PreparedStatement Subject=con.prepareStatement(Query);
                        Subject.setString(1, Student.getString("StudentId"));
                        Subject.setString(2, ExamId);
                        ResultSet Marks=Subject.executeQuery();
                        while(Marks.next())
                        {
                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                            int IntModeration=0,ExtModeration=0;
                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                             int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                            if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                            {
                                ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));

                                TotalModeration-=IntModeration+ExtModeration;
                                //Update moderation,Pass status
                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                saveModeration.setInt(1,ExtModeration);
                                saveModeration.setInt(2,IntModeration);
                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                saveModeration.setString(4, Student.getString("StudentId"));
                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                saveModeration.setString(6, ExamId);
                                saveModeration.executeUpdate();
                            }
                        }

                    }

                          }
                     }

                }else{
                    System.out.println("-1"+Student.getString("StudentId"));
                   
                        int needforpass=Total-Student.getInt("TotalMark");
                         System.out.println("-2"+needforpass+'#'+Total+'#'+Student.getInt("TotalMark"));
                        if(20>=needforpass)
                        {
                            if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                                NMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem);
                                 System.out.println("-3"+NMarks);
                                 if(20>=NMarks){
                                int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                    //int  TotalModeration=needforpass-TotalModerationApp;
                                    int TotalModeration=ModerationId.getInt("TotalModeration");
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            TotalModeration-=IntModeration+ExtModeration;
                                            TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status 
                                            System.out.println("&&&&&&&");
                                            PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                            saveModeration.setInt(1,ExtModeration);
                                            saveModeration.setInt(2,IntModeration);
                                            saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                            saveModeration.setString(4, Student.getString("StudentId"));
                                            saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                            saveModeration.setString(6, ExamId);
                                            saveModeration.executeUpdate();
                                        }
                                      }
                                }
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                    BranchModeration1.setString(1, BranchId);
                                    BranchModeration1.setString(2, YearSem);
                                    BranchModeration1.setString(3, ExamId);
                                   ModerationId=BranchModeration1.executeQuery();
                                    while(ModerationId.next())
                                    {
                                        int  TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                        //get the mak details of the failed subjects in a selected moderation category for each student
                                        String Query="select distinct b.SubjectBranchId,(ExternalMark+ModerationExt) as ExternalMark,(InternalMark+ModerationInt) as InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                        PreparedStatement Subject=con.prepareStatement(Query);
                                        Subject.setString(1, Student.getString("StudentId"));
                                        Subject.setString(2, ExamId);
                                        ResultSet Marks1=Subject.executeQuery();
                                        while(Marks1.next())
                                        {
                                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                            int IntModeration=0,ExtModeration=0;
                                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                            int NeededMarks=Marks1.getInt("PMark")-(Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark"));
                                            if(NeededMarks<=PerPaperMax   && Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+PerPaperMax >=Marks1.getInt("PMark")  )
                                            {
                                                ExtModeration=Marks1.getInt("PMark")-(Marks1.getInt("ExternalMark")+Marks1.getInt("InternalMark"));
                                                if(ExtModeration>0)
                                                {
                                                    if(TotalModeration>=ExtModeration){
                                                        TotalModeration-=IntModeration+ExtModeration;}
                                                    else{
                                                          ExtModeration=TotalModeration;
                                                          TotalModeration-=IntModeration+ExtModeration;
                                                        }
                                                 System.out.println("&&&&&&&");
                                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? ,ModerationInt=ModerationInt+?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                    saveModeration.setInt(1,ExtModeration);
                                                    saveModeration.setInt(2,IntModeration);
                                                    saveModeration.setInt(3, Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                    saveModeration.setString(4, Student.getString("StudentId"));
                                                    saveModeration.setString(5, Marks1.getString("SubjectBranchId"));
                                                    saveModeration.setString(6, ExamId);
                                                    saveModeration.executeUpdate();
                                                    }
                                            }

                                        }

                                    }

                                }

                                 }

                            }else{
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpass-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                System.out.println("&&&&&&&");
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
            con.close();
            }
        }
}

     
     
     public void ApplyClassModderation(String BranchId,String ExamId,String YearSem) throws Exception
{
  Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
          int ReqMark=0;
          int ExtModeration=0;
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("SELECT StudentId,sum(TotalMark) GrandTotal,Count(StudentId)   FROM `DEMS_db`.`StudentPassDetails`  group by StudentId having Count(StudentId) =4");
        
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                    if(Student.getInt("GrandTotal")>=1085 && Student.getInt("GrandTotal")<=1100)
                    {
                    ReqMark=1100-Student.getInt("GrandTotal");}
                    if(Student.getInt("GrandTotal")>=1305 && Student.getInt("GrandTotal")<=1320)
                    {
                     ReqMark=1320-Student.getInt("GrandTotal");}
                    if(Student.getInt("GrandTotal")>=1745 && Student.getInt("GrandTotal")<=1760)
                    {
                    ReqMark=1760-Student.getInt("GrandTotal");}

                       if(Student.getInt("GrandTotal")>=1085 && Student.getInt("GrandTotal")<=1100||Student.getInt("GrandTotal")>=1305 && Student.getInt("GrandTotal")<=1320||Student.getInt("GrandTotal")>=1745 && Student.getInt("GrandTotal")<=1760)
                       { PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                        BranchModeration1.setString(1, BranchId);
                        BranchModeration1.setString(2, YearSem);
                        BranchModeration1.setString(3, ExamId);
                        ResultSet ModerationId1=BranchModeration1.executeQuery();
                        while(ModerationId1.next())
                        {
                            int TotalModerationApp=0;
                            int TotalModeration=ReqMark;
                            int Count=6;

                            int NeededMarks=0;
                                String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,ModerationExt,b.PassMark,b.ExternalMin,b.InternalMin,b.ExternalMax from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId1.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                                PreparedStatement Subject=con.prepareStatement(Query);
                                Subject.setString(1, Student.getString("StudentId"));
                                Subject.setString(2, ExamId);
                                ResultSet Pass=Subject.executeQuery();
                                 while(Pass.next())
                                    {
                                       
                                        if(Pass.isLast())
                                        {
                                            ExtModeration=ReqMark;
                                        }
                                        else
                                        {
                                            ExtModeration=ReqMark/Count;
                                            Count=Count-1;
                                        }
                                         ReqMark-=ExtModeration;
                                         PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ClassModeration=? , IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                         saveModeration.setInt(1,ExtModeration);
                                         saveModeration.setInt(2, Pass.getInt("InternalMark")+Pass.getInt("ExternalMark")+ExtModeration);
                                         saveModeration.setString(3, Student.getString("StudentId"));
                                         saveModeration.setString(4, Pass.getString("SubjectBranchId"));
                                         saveModeration.setString(5, ExamId);
                                         saveModeration.executeUpdate();
                                    }
                        }

                       }

            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
     }
     
     
     public void ApplyClassModderation1(String BranchId,String ExamId,String YearSem) throws Exception
{
  Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
          int ReqMark=0;
          int ExtModeration=0;
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("SELECT StudentId,sum(TotalMark) GrandTotal,Count(StudentId)   FROM `DEMS_db`.`StudentPassDetails`  where Branchid=4 group by StudentId having Count(StudentId) =4");
        
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                    if(Student.getInt("GrandTotal")>=1085 && Student.getInt("GrandTotal")<=1100)
                    {
                    ReqMark=1100-Student.getInt("GrandTotal");}
                    if(Student.getInt("GrandTotal")>=1305 && Student.getInt("GrandTotal")<=1320)
                    {
                     ReqMark=1320-Student.getInt("GrandTotal");}
                    if(Student.getInt("GrandTotal")>=1745 && Student.getInt("GrandTotal")<=1760)
                    {
                    ReqMark=1760-Student.getInt("GrandTotal");}

                       if(Student.getInt("GrandTotal")>=1085 && Student.getInt("GrandTotal")<=1100||Student.getInt("GrandTotal")>=1305 && Student.getInt("GrandTotal")<=1320||Student.getInt("GrandTotal")>=1745 && Student.getInt("GrandTotal")<=1760)
                       { PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                        BranchModeration1.setString(1, BranchId);
                        BranchModeration1.setString(2, YearSem);
                        BranchModeration1.setString(3, ExamId);
                        ResultSet ModerationId1=BranchModeration1.executeQuery();
                        while(ModerationId1.next())
                        {
                            int TotalModerationApp=0;
                            int TotalModeration=ReqMark;
                            int Count=6;

                            int NeededMarks=0;
                                String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,ModerationExt,b.PassMark,b.ExternalMin,b.InternalMin,b.ExternalMax from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId1.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                                PreparedStatement Subject=con.prepareStatement(Query);
                                Subject.setString(1, Student.getString("StudentId"));
                                Subject.setString(2, ExamId);
                                ResultSet Pass=Subject.executeQuery();
                                 while(Pass.next())
                                    {
                                       
                                        if(Pass.isLast())
                                        {
                                            ExtModeration=ReqMark;
                                        }
                                        else
                                        {
                                            ExtModeration=ReqMark/Count;
                                            Count=Count-1;
                                        }
                                         ReqMark-=ExtModeration;
                                         PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ClassModeration=? , IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                         saveModeration.setInt(1,ExtModeration);
                                         saveModeration.setInt(2, Pass.getInt("InternalMark")+Pass.getInt("ExternalMark")+ExtModeration);
                                         saveModeration.setString(3, Student.getString("StudentId"));
                                         saveModeration.setString(4, Pass.getString("SubjectBranchId"));
                                         saveModeration.setString(5, ExamId);
                                         saveModeration.executeUpdate();
                                    }
                        }

                       }

            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
     }
     public void ApplyModerationBcomCS() throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select V1.StudentId,V2.mark72,V3.mark213,(V2.mark72+V3.mark213) as Total, (56-(V2.mark72+V3.mark213)) as M ,(V3.mark213+(56-(V2.mark72+V3.mark213))) newExt from"
+"(SELECT distinct(m.StudentId) FROM `DEMS_db`.`StudentSubjectMark` m"
+" where m.IsValid=1 and m.SubjectBranchId in (61,207) and m.ExamId=7 group by m.StudentId) V1"
+" LEFT JOIN "
+"(SELECT distinct(m.StudentId),IF(max(m.ExternalMark) IS NULL,0,max(m.ExternalMark) )as mark72 FROM `DEMS_db`.`StudentSubjectMark` m"
+" where m.IsValid=1 and m.SubjectBranchId =61 and m.ExamId=7 group by m.StudentId) V2 "
+" ON V1.StudentId=V2.StudentId"
+" LEFT JOIN (SELECT distinct(m.StudentId),IF(m.ExternalMark IS NULL,0,m.ExternalMark) as mark213 FROM `DEMS_db`.`StudentSubjectMark` m"
+" where m.IsValid=1 and m.ExamId=7 and m.SubjectBranchId =207 group by m.StudentId) V3"
+" ON V1.StudentId=V3.StudentId"
+" where mark213>0 and (V2.mark72+V3.mark213)<56 and (V2.mark72+V3.mark213)>38 order by StudentId ");
            
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set ModerationExt=?,InternalMark=? where StudentId=? and SubjectBranchId=207  and Isvalid=1 and ExamId=7 ");
                                         UpdateInternal.setInt(1, Student.getInt("M"));
                                        UpdateInternal.setLong(2, CalculateInternalforBcom(Student.getInt("newExt"), 207, con));
                                        UpdateInternal.setString(3, Student.getString("StudentId"));
                                        UpdateInternal.executeUpdate();

                                }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
     
   public void ApplyModerationBcomTT() throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select V1.StudentId,V2.mark72,V3.mark213,(V2.mark72+V3.mark213) as Total, (56-(V2.mark72+V3.mark213)) as M ,(V3.mark213+(56-(V2.mark72+V3.mark213))) newExt from"
+"(SELECT distinct(m.StudentId) FROM `DEMS_db`.`StudentSubjectMark` m"
+" where m.IsValid=1 and m.SubjectBranchId in (72,213) group by m.StudentId) V1"
+" LEFT JOIN "
+"(SELECT distinct(m.StudentId),IF(max(m.ExternalMark) IS NULL,0,max(m.ExternalMark) )as mark72 FROM `DEMS_db`.`StudentSubjectMark` m"
+" where m.IsValid=1 and m.SubjectBranchId =72 group by m.StudentId) V2 "
+" ON V1.StudentId=V2.StudentId"
+" LEFT JOIN (SELECT distinct(m.StudentId),IF(m.ExternalMark IS NULL,0,m.ExternalMark) as mark213 FROM `DEMS_db`.`StudentSubjectMark` m"
+" where m.IsValid=1 and m.SubjectBranchId =213 group by m.StudentId) V3"
+" ON V1.StudentId=V3.StudentId"
+" where mark213>0 and (V2.mark72+V3.mark213)<56 and (V2.mark72+V3.mark213)>49 order by StudentId ");

            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set ModerationExt=?,InternalMark=? where StudentId=? and SubjectBranchId=213  and Isvalid=1 ");
                                         UpdateInternal.setInt(1, Student.getInt("M"));
                                        UpdateInternal.setLong(2, CalculateInternalforBcom(Student.getInt("newExt"), 213, con));
                                        UpdateInternal.setString(3, Student.getString("StudentId"));
                                        UpdateInternal.executeUpdate();

                                }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
   
   
   
      public void ApplyModerationBcomTT_2013() throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select b.ExternalMark as E,a.StudentId,a.cnt,a.t,a.m as M from (SELECT s.StudentId,count(*) cnt,Sum(ExternalMark) t, "
                    + " (56-Sum(ExternalMark) ) m  FROM StudentSubjectMark s "
                    + " where  s.SubjectBranchId in(72,213) and s.MarkStatus=1 and s.ExamId=7 and s.IsValid=1"
                   
                    + " group by StudentId having count(*)=2 and Sum(ExternalMark) between 40 and 56)a inner join "
                    + " (SELECT *  FROM StudentSubjectMark b where b.SubjectBranchId=213 and b.ExamId=7 and b.MarkStatus=1)b "
                    + " on a.StudentId=b.StudentId ");

            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set ModerationExt=?,InternalMark=? where StudentId=? and SubjectBranchId=213  and Isvalid=1 ");
                                         UpdateInternal.setInt(1, Student.getInt("M"));
                                        UpdateInternal.setLong(2, CalculateInternalforBcom(Student.getInt("M")+Student.getInt("E"), 207, con));
                                        UpdateInternal.setString(3, Student.getString("StudentId"));
                                        UpdateInternal.executeUpdate();

                                }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
 public void ApplyModerationBcomMAl() throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("SELECT distinct(StudentId),ExternalMark,(28-ExternalMark) M,(ExternalMark+(28-ExternalMark)) newExt FROM `DEMS_db`.`StudentSubjectMark` where IsValid=1 and SubjectBranchId=73 and ExamId=7 and ExternalMark<28 and ExternalMark>17");

            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set ModerationExt=?,InternalMark=? where StudentId=? and SubjectBranchId=73  and Isvalid=1 and ExamId=7 ");
                                         UpdateInternal.setInt(1, Student.getInt("M"));
                                        UpdateInternal.setInt(2, 7);
                                        UpdateInternal.setString(3, Student.getString("StudentId"));
                                        UpdateInternal.executeUpdate();

                                }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
  public void ApplyModerationBcomHindi() throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("SELECT distinct(StudentId),ExternalMark,(28-ExternalMark) M,(ExternalMark+(28-ExternalMark)) newExt FROM `DEMS_db`.`StudentSubjectMark` where IsValid=1 and SubjectBranchId=74 and ExamId=7 and ExternalMark<28 and ExternalMark>12");

            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set ModerationExt=?,InternalMark=? where StudentId=? and SubjectBranchId=74  and Isvalid=1 and ExamId=7 ");
                                         UpdateInternal.setInt(1, Student.getInt("M"));
                                        UpdateInternal.setInt(2, 7);
                                        UpdateInternal.setString(3, Student.getString("StudentId"));
                                        UpdateInternal.executeUpdate();

                                }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
 public void ApplyModerationBcomSpEng() throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("SELECT distinct(StudentId),ExternalMark,(28-ExternalMark) M,(ExternalMark+(28-ExternalMark)) newExt FROM `DEMS_db`.`StudentSubjectMark` where IsValid=1 and SubjectBranchId=101 and ExamId=7 and ExternalMark<28 and ExternalMark>12");

            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set ModerationExt=?,InternalMark=? where StudentId=? and SubjectBranchId=101  and Isvalid=1 and ExamId=7 ");
                                         UpdateInternal.setInt(1, Student.getInt("M"));
                                        UpdateInternal.setInt(2, 7);
                                        UpdateInternal.setString(3, Student.getString("StudentId"));
                                        UpdateInternal.executeUpdate();

                                }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
  public void ApplyModerationBcomPartIII(String BranchId,String ExamId,String YearSem) throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            //PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in (69,70,71,208,209,210,211,212,628,629,630,631,632,633) left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null  where b.BranchId=?  and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0  group by  m.StudentId ");
           PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in (80,81,82,214,215,216,217,218,531,533,534,535,536,537) left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId and d.EndDate is null  where b.BranchId=?  and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0  group by  m.StudentId ");
            FailedStudents.setString(1, BranchId);
            
            FailedStudents.setString(2, ExamId);
           
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                if(!IsModerationAppliedForSemPass(BranchId, ExamId, YearSem, con))
                {//get the moderation categories defined for branch
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1)
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=32-(Marks.getInt("ExternalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration &&  Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        int PassMin=0;
                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            PassMin=5;
                                        }
                                        else
                                        {
                                            PassMin=13;
                                        }
                                        IntModeration=PassMin-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);

                                       // int PassMin=0;

//                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
//                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
                                    }
                                    else
                                    {
                                        int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark")+ExtModeration, Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }
                               /*else if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                {
                                    int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark")+ExtModeration, Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }*/

                            }
                        }
                        else
                        {
                            if((!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con)))
                            {
                                 int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=32-(Marks.getInt("ExternalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");
                                    }
                                    else
                                    {
                                         int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }

                                }
                            }
                            }
                        }
                    }
                }
                /*else
                {
                    PreparedStatement Pass=con.prepareStatement("select distinct StudentId,sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.IsPassed=0 and m.IsAbsent=0 and m.IsMalPractice=0 and m.StudentId=?");
                    Pass.setString(1, YearSem);
                    Pass.setString(2, Student.getString("StudentId"));
                    ResultSet SemPass=Pass.executeQuery();
                    while(SemPass.next())
                    {
                        if(getTotalModerationForBranch(BranchId, ExamId, YearSem, con)+SemPass.getInt("Mark")>0 && (!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1 && (!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                               if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");
                                    }
                                    else
                                    {
                                        int Pass1;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass1=28;
                                        }
                                        else
                                        {
                                            Pass1=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass1 && Marks.getInt("ExternalMark")<Pass1)
                                            ExtModeration=Pass1-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                        }
                    }
                }*/
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }

 public void ApplyModerationBTSFourtSem(String BranchId,String ExamId,String YearSem) throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark+PracticalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.StudentExamWithheldId is null group by StudentId ");
           // PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  inner join StudentPersonal sp on sp.StudentId=m.StudentId and sp.JoiningYear=2011 left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.EndDate is null group by StudentId");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                if(!IsModerationAppliedForSemPass(BranchId, ExamId, YearSem, con))
                {//get the moderation categories defined for branch
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1)
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,PracticalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        int PassMin=0;
                                        if(Marks.getInt("SubjectBranchId")==45 || Marks.getInt("SubjectBranchId")==179)
                                        {
                                            PassMin=5;
                                        }
                                        else
                                        {
                                            PassMin=13;
                                        }
                                        IntModeration=PassMin-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);

                                       // int PassMin=0;

//                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
//                                        ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+IntModeration);
                                    }
                                    else
                                    {
                                        int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+Marks.getInt("PracticalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark")+ExtModeration, Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }

                                }
                                else if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                {
                                    int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark")+ExtModeration, Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }

                            }
                        }
                        else
                        {
                            if((!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con)))
                            {
                                 int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark"));
                                if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");
                                    }
                                    else
                                    {
                                         int Pass;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass=28;
                                        }
                                        else
                                        {
                                            Pass=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass && Marks.getInt("ExternalMark")<Pass)
                                            ExtModeration=Pass-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+Marks.getInt("PracticalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }

                                }
                            }
                            }
                        }
                    }
                }
                else
                {
                    PreparedStatement Pass=con.prepareStatement("select distinct StudentId,sum((ExternalMark+InternalMark+PracticalMark)-b.PassMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.IsPassed=0 and m.IsAbsent=0 and m.IsMalPractice=0 and m.StudentId=?");
                    Pass.setString(1, YearSem);
                    Pass.setString(2, Student.getString("StudentId"));
                    ResultSet SemPass=Pass.executeQuery();
                    while(SemPass.next())
                    {
                        if(getTotalModerationForBranch(BranchId, ExamId, YearSem, con)+SemPass.getInt("Mark")>0 && (!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        if(ModerationId.getInt("IsModerationAppliedIfStudentAbsent")==1 && (!IsStudentAbsentForAnySubjectOfSemester(Student.getString("StudentId"), ExamId, YearSem, con)))
                        {
                            int TotalModeration=ModerationId.getInt("TotalModeration");
                            //get the mak details of the failed subjects in a selected moderation category for each student
                            String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,PracticalMark,b.PassMark,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                            PreparedStatement Subject=con.prepareStatement(Query);
                            Subject.setString(1, Student.getString("StudentId"));
                            Subject.setString(2, ExamId);
                            ResultSet Marks=Subject.executeQuery();
                            while(Marks.next())
                            {
                                int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                int IntModeration=0,ExtModeration=0;
                                int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark"));
                               if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")<Marks.getInt("PassMark"))
                                {
                                    if(ModerationId.getInt("HasSeparateMinimumForInternal")==1)
                                    {
                                        IntModeration=Marks.getInt("InternalMin")-Marks.getInt("InternalMark");
                                        ExtModeration=Marks.getInt("ExternalMin")-Marks.getInt("ExternalMark");
                                    }
                                    else
                                    {
                                        int Pass1;
                                        if(Marks.getInt("IsOptionalSubject")==1)
                                        {
                                            Pass1=28;
                                        }
                                        else
                                        {
                                            Pass1=32;
                                        }
                                        if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                        {
                                            if(Marks.getInt("ExternalMark")+PerPaperMax >=Pass1 && Marks.getInt("ExternalMark")<Pass1)
                                            ExtModeration=Pass1-Marks.getInt("ExternalMark");
                                        }
                                        else
                                        {
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")+Marks.getInt("PracticalMark"));
                                        }
                                    }
                                    TotalModeration-=IntModeration+ExtModeration;
                                    //Update moderation,Pass status

                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                    saveModeration.setInt(1,ExtModeration);
                                    saveModeration.setInt(2,IntModeration);
                                    saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+Marks.getInt("PracticalMark")+ExtModeration+IntModeration);
                                    saveModeration.setString(4, Student.getString("StudentId"));
                                    saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                    saveModeration.setString(6, ExamId);
                                    saveModeration.executeUpdate();
                                    if(ModerationId.getInt("IsInternalCalculatedAfterModeration")==1)
                                    {
                                        //Update internal
                                        PreparedStatement UpdateInternal=con.prepareStatement("Update StudentSubjectMark set InternalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        UpdateInternal.setLong(1, CalculateInternal(Marks.getInt("ExternalMark"), Marks.getString("SubjectBranchId"), con));
                                        UpdateInternal.setString(2, Student.getString("StudentId"));
                                        UpdateInternal.setString(3, Marks.getString("SubjectBranchId"));
                                        UpdateInternal.setString(4, ExamId);
                                        UpdateInternal.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                        }
                    }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
                con.close();
            }
        }
    }
 public void ApplyModerationLLM_semester(String BranchId,String ExamId,String YearSem) throws Exception
{
    Connection con=null;
    try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            //int Total=getTotalPassMarkForBLiSc(BranchId, YearSem, con);
            int Total=150;
            con.setAutoCommit(false);
           // PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark ,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark,count(m.SubjectBranchId) cnt from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.StudentExamWithheldId is null group by StudentId ");
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark , " +
                                " sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark,count(m.SubjectBranchId) cnt" +
                                " from StudentSubjectMark m" +
                                " inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId " +
                                "left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId " +

                                "where  m.ExamId=? and b.BranchId=? and b.CurrentYearSem=? " +
                                "and  m.IsAbsent=0 " +
                                "and m.IsValid=1 and m.IsMalPractice=0 and d.StudentExamWithheldId is null  group by StudentId ");
            FailedStudents.setString(1, ExamId);
            FailedStudents.setString(2, BranchId);
            FailedStudents.setString(3, YearSem);

            ResultSet Student=FailedStudents.executeQuery();

            while(Student.next())
            {
                if(Student.getInt("TotalMark")>=Total )                //Student Totalmark greater than 175 moderation for pass(40)
                {
                    
                     if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                    PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                    BranchModeration.setString(1, BranchId);
                    BranchModeration.setString(2, YearSem);
                    BranchModeration.setString(3, ExamId);
                    ResultSet ModerationId=BranchModeration.executeQuery();
                    while(ModerationId.next())
                    {
                        int TotalModeration=ModerationId.getInt("TotalModeration");
                        //get the mak details of the failed subjects in a selected moderation category for each student
                        String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                        PreparedStatement Subject=con.prepareStatement(Query);
                        Subject.setString(1, Student.getString("StudentId"));
                        Subject.setString(2, ExamId);
                        ResultSet Marks=Subject.executeQuery();
                        while(Marks.next())
                        {
                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                            int IntModeration=0,ExtModeration=0;
                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                            int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                            if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                            {
                                ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));

                                TotalModeration-=IntModeration+ExtModeration;
                                //Update moderation,Pass status
                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                saveModeration.setInt(1,ExtModeration);
                                saveModeration.setInt(2,IntModeration);
                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                saveModeration.setString(4, Student.getString("StudentId"));
                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                saveModeration.setString(6, ExamId);
                                saveModeration.executeUpdate();
                            }
                        }

                    }}

                }else{
                        int needforpass=Total-Student.getInt("TotalMark");
                        
                        if(10>=needforpass)
                        {
                            if(IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem))
                            {
                                int TotalModerationApp=0;
                                int  TotalModeration=needforpass-TotalModerationApp;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                     TotalModeration=needforpass-TotalModerationApp;
                                    //int TotalModeration=ModerationId.getInt("TotalModeration");
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PassMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PassMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PassMark"))
                                        {
                                           
                                            ExtModeration=Marks.getInt("PassMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            TotalModeration-=IntModeration+ExtModeration;
                                            TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                            PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                            saveModeration.setInt(1,ExtModeration);
                                            saveModeration.setInt(2,IntModeration);
                                            saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                            saveModeration.setString(4, Student.getString("StudentId"));
                                            saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                            saveModeration.setString(6, ExamId);
                                            saveModeration.executeUpdate();
                                        }
                                      }
                                }
                                if(Student.getInt("TotalMark")+TotalModerationApp <Total)
                                {
                                    PreparedStatement BranchModeration1=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                    BranchModeration1.setString(1, BranchId);
                                    BranchModeration1.setString(2, YearSem);
                                    BranchModeration1.setString(3, ExamId);
                                   ModerationId=BranchModeration1.executeQuery();
                                    while(ModerationId.next())
                                    {
                                          TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                        //get the mak details of the failed subjects in a selected moderation category for each student
                                        String Query="select distinct b.SubjectBranchId,(ExternalMark+ModerationExt) as ExternalMark,(InternalMark+ModerationInt) as InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                        PreparedStatement Subject=con.prepareStatement(Query);
                                        Subject.setString(1, Student.getString("StudentId"));
                                        Subject.setString(2, ExamId);
                                        ResultSet Marks1=Subject.executeQuery();
                                        while(Marks1.next())
                                        {
                                            int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                            int IntModeration=0,ExtModeration=0;
                                            //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                            int NeededMarks=Marks1.getInt("PMark")-(Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark"));
                                            if(NeededMarks<=PerPaperMax   && Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+PerPaperMax >=Marks1.getInt("PMark")  )
                                            {
                                                TotalModeration=Total-(Student.getInt("TotalMark")+TotalModerationApp);
                                                ExtModeration=Marks1.getInt("PMark")-(Marks1.getInt("ExternalMark")+Marks1.getInt("InternalMark"));
                                                if(ExtModeration>0)
                                                {
                                                    if(TotalModeration>=ExtModeration){
                                                        TotalModeration-=IntModeration+ExtModeration;
                                                    TotalModerationApp=TotalModerationApp+ExtModeration;}
                                                    else{
                                                          ExtModeration=TotalModeration;
                                                          TotalModerationApp=TotalModerationApp+ExtModeration;
                                                        }
                                                    PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=ModerationExt+? ,ModerationInt=ModerationInt+?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                    saveModeration.setInt(1,ExtModeration);
                                                    saveModeration.setInt(2,IntModeration);
                                                    saveModeration.setInt(3, Marks1.getInt("InternalMark")+Marks1.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                    saveModeration.setString(4, Student.getString("StudentId"));
                                                    saveModeration.setString(5, Marks1.getString("SubjectBranchId"));
                                                    saveModeration.setString(6, ExamId);
                                                    saveModeration.executeUpdate();
                                                    }
                                            }

                                        }

                                    }

                                }
                            }else{
                               int TotalModerationApp=0;
                                PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                                BranchModeration.setString(1, BranchId);
                                BranchModeration.setString(2, YearSem);
                                BranchModeration.setString(3, ExamId);
                                ResultSet ModerationId=BranchModeration.executeQuery();
                                while(ModerationId.next())
                                {
                                   int  TotalModeration=needforpass-TotalModerationApp;
                                    //get the mak details of the failed subjects in a selected moderation category for each student
                                    String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                    PreparedStatement Subject=con.prepareStatement(Query);
                                    Subject.setString(1, Student.getString("StudentId"));
                                    Subject.setString(2, ExamId);
                                    ResultSet Marks=Subject.executeQuery();
                                    while(Marks.next())
                                    {
                                        int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                        int IntModeration=0,ExtModeration=0;
                                        //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                        int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                        if(NeededMarks<=PerPaperMax   && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark")  )
                                        {
                                            ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                            if(ExtModeration>0)
                                            {
                                                if(TotalModeration>=ExtModeration){
                                                    TotalModeration-=IntModeration+ExtModeration;}
                                                else{
                                                    ExtModeration=TotalModeration;
                                                    TotalModeration-=IntModeration+ExtModeration;
                                                    }
                                                TotalModerationApp+=ExtModeration;
                                            //Update moderation,Pass status
                                                PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                                saveModeration.setInt(1,ExtModeration);
                                                saveModeration.setInt(2,IntModeration);
                                                saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                                saveModeration.setString(4, Student.getString("StudentId"));
                                                saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                                saveModeration.setString(6, ExamId);
                                                saveModeration.executeUpdate();
                                            }
                                        }
                                    }
                                }
                            }
                        }else{
                            PreparedStatement BranchModeration=con.prepareStatement("select BranchModerationId,TotalModeration,PerPaperMaximum,HasSeparateMinimumForInternal,IsInternalCalculatedAfterModeration,IsModeartonAppliedForSemPass,IsModerationAppliedIfStudentAbsent from BranchModeration where BranchId=? and YearOrSem=? and ExamId=?");
                            BranchModeration.setString(1, BranchId);
                            BranchModeration.setString(2, YearSem);
                            BranchModeration.setString(3, ExamId);
                            ResultSet ModerationId=BranchModeration.executeQuery();
                            while(ModerationId.next())
                            {
                                int TotalModeration=ModerationId.getInt("TotalModeration");
                                //get the mak details of the failed subjects in a selected moderation category for each student
                                String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark,b.ExternalMin,b.InternalMin,b.IsOptionalSubject from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc,b.PartNo,b.PaperNo";
                                PreparedStatement Subject=con.prepareStatement(Query);
                                Subject.setString(1, Student.getString("StudentId"));
                                Subject.setString(2, ExamId);
                                ResultSet Marks=Subject.executeQuery();
                                while(Marks.next())
                                {
                                    int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                    int IntModeration=0,ExtModeration=0;
                                    //int NeededMarks=(Marks.getInt("InternalMark")-Marks.getInt("InternalMin"))+(Marks.getInt("ExternalMark")-Marks.getInt("ExternalMin"));
                                    int NeededMarks=Marks.getInt("PMark")-(Marks.getInt("InternalMark")+Marks.getInt("ExternalMark"));
                                    if(NeededMarks<=PerPaperMax  && NeededMarks<=TotalModeration && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+PerPaperMax >=Marks.getInt("PMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+TotalModeration >=Marks.getInt("PMark") && Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")<Marks.getInt("PMark"))
                                    {
                                        ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));
                                        TotalModeration-=IntModeration+ExtModeration;
                                        //Update moderation,Pass status
                                        PreparedStatement saveModeration=con.prepareStatement("Update StudentSubjectMark set ModerationExt=? ,ModerationInt=?, IsPassed=1,TotalMark=? where StudentId=? and SubjectBranchId=? and ExamId=? and Isvalid=1 ");
                                        saveModeration.setInt(1,ExtModeration);
                                        saveModeration.setInt(2,IntModeration);
                                        saveModeration.setInt(3, Marks.getInt("InternalMark")+Marks.getInt("ExternalMark")+ExtModeration+IntModeration);
                                        saveModeration.setString(4, Student.getString("StudentId"));
                                        saveModeration.setString(5, Marks.getString("SubjectBranchId"));
                                        saveModeration.setString(6, ExamId);
                                        saveModeration.executeUpdate();
                                    }
                                }
                            }
                        }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
        }
        finally
        {
            if(!con.isClosed())
            {
            con.close();
            }
        }
}

    public static void main(String[] args) throws Exception
    {
        Moderation moderationObj=new Moderation();
        moderationObj.ApplyModeration("21", "7", "4");
        
    } 
}
