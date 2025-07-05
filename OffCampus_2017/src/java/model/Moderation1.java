/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Moderation1
{
    public void ApplyModeration(String BranchId,String ExamId,String YearSem) throws Exception
    {
        Connection con=null;
        try
        {//get all failed students for branch
            con=new DBConnection().getConnection();
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct m.StudentId,b.BranchId, sum((ExternalMark+InternalMark)-b.PassMark) as Mark from StudentSubjectMark m  inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId  left join  StudentExamWithheldDetails d on m.StudentId=d.StudentId where b.BranchId=? and b.CurrentYearSem=? and m.ExamId=? and  m.IsAbsent=0  and m.IsValid=1 and m.IsMalPractice=0 and d.StudentExamWithheldId is null group by StudentId ");
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
                else
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
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void ApplyModerationToBLiSc(String BranchId,String ExamId,String YearSem) throws SQLException
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
                    if(Student.getInt("TotalMark")<Total || IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem,con))
                    {
                        int ReqMarkForSemPass=getTotalPassMarkForBLiSc(BranchId, YearSem, con)-Student.getInt("TotalMark");
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
                            if(ReqMarkForSemPass > getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem, con))
                            {
                                NeededMarks=ReqMarkForSemPass;
                            }
                            else
                            {
                                NeededMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem, con);
                            }
                            if(ReqMarkForSemPass<=TotalModeration && getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem, con)<=TotalModeration && NeededMarks<=TotalModeration)
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
    public void ApplyModerationToLLM(String BranchId,String ExamId,String YearSem) throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            int Total=getTotalPassMarkForBLiSc(BranchId, YearSem, con);
            con.setAutoCommit(false);
            PreparedStatement FailedStudents = con.prepareStatement("select distinct StudentId,b.BranchId,sum(ExternalMark+InternalMark+ModerationExt+ModerationInt) as TotalMark,b.PassMark,b.TotalMark/2 as PMark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.BranchId=? and b.CurrentYearSem=? where m.ExamId=? and m.IsValid=1  group by StudentId ");
            FailedStudents.setString(1, BranchId);
            FailedStudents.setString(2, YearSem);
            FailedStudents.setString(3, ExamId);
            ResultSet Student=FailedStudents.executeQuery();
            while(Student.next())
            {
                if(!IsStudentMalpracticeForAnySubjectOfSemester(Student.getString("StudentId"),ExamId,YearSem,con))
                {
                    if(Student.getInt("TotalMark")<Total || IsStudentFailedForAnyPaper(Student.getString("StudentId"),ExamId,YearSem,con))
                    {
                        int ReqMarkForSemPass=getTotalPassMarkForBLiSc(BranchId, YearSem, con)-Student.getInt("TotalMark");
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
                            if(ReqMarkForSemPass > getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem, con))
                            {
                                NeededMarks=ReqMarkForSemPass;
                            }
                            else
                            {
                                NeededMarks=getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem, con);
                            }
                            if(ReqMarkForSemPass<=TotalModeration && getTotalPassMarkForIndividualSubjectsOfBLiSc(Student.getString("StudentId"), YearSem, con)<=TotalModeration && NeededMarks<=TotalModeration)
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
                                 
                             
                            }else{
                                   int ExtModeration=0;

                                String Query="select distinct b.SubjectBranchId,ExternalMark,InternalMark,b.PassMark,b.TotalMark/2 as PMark ,b.ExternalMin,b.InternalMin from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.SubjectBranchId in ("+getSubjectBranchIdsForBranchModeration(ModerationId.getString("BranchModerationId"), con) +") where m.StudentId=? and m.ExamId=? and m.IsAbsent=0 and m.IsValid=1 and m.IsMalPractice=0 and m.TempUnconfirmed=0 order by ExternalMark+InternalMark desc";
                                PreparedStatement Subject=con.prepareStatement(Query);
                                Subject.setString(1, Student.getString("StudentId"));
                                Subject.setString(2, ExamId);
                                ResultSet Marks=Subject.executeQuery();
                                while(Marks.next())
                                {

                                    int PerPaperMax=ModerationId.getInt("PerPaperMaximum");
                                    if(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark")<Marks.getInt("PMark") && Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"))<=PerPaperMax )
                                    {
                                         ExtModeration=Marks.getInt("PMark")-(Marks.getInt("ExternalMark")+Marks.getInt("InternalMark"));

                                         PerPaperMax-=ExtModeration;
                                         TotalModeration-=ExtModeration;
                                         TotalModerationApp+=ExtModeration;

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
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
    private int getTotalPassMarkForIndividualSubjectsOfBLiSc(String StudentId,String YearSem,Connection con)
    {
        try
        {
            PreparedStatement st=con.prepareStatement("select sum(b.PassMark-(m.ExternalMark+m.InternalMark)) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on m.SubjectBranchId=b.SubjectBranchId and b.CurrentYearSem=?  where m.StudentId=? and m.IsPassed=0 and m.ExamId=?");
            st.setString(1, YearSem);
            st.setString(2, StudentId);
            st.setInt(3, 1);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    private boolean IsStudentFailedForAnyPaper(String StudentId,String ExamId,String YearSem,Connection con)
    {
        Boolean flag =false;
        try
        {
            PreparedStatement st=con.prepareStatement("select b.PassMark-(ExternalMark+InternalMark) as Mark from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where m.StudentId=? and m.ExamId=? ");
            st.setString(1, YearSem);
            st.setString(2, StudentId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt("Mark")>=0)
                {
                   flag=true;
                }
               
            }
            if(flag==true)
            {
                return true;
            }
            return false;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
private boolean IsStudentMalpracticeForAnySubjectOfSemester(String StudentId,String ExamId,String YearSem,Connection con)
    {
        try
        {
            PreparedStatement st=con.prepareStatement("select count(*) as Count from StudentSubjectMark m inner join SubjectBranchMaster b on b.SubjectBranchId=m.SubjectBranchId and b.CurrentYearSem=? where ( m.IsMalPractice=1 or TempUnconfirmed=1) and m.IsValid=1 and m.StudentId=? and m.ExamId=? ");
            st.setString(1, YearSem);
            st.setString(2, StudentId);
            st.setString(3, ExamId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String getSubjectBranchIdsForBranchModeration(String BranchModerationId,Connection con)
    {
        String SubjectBranchId="";
        try
        {
            PreparedStatement st=con.prepareStatement("select SubjectBranchId from SubjectWiseModeration where BranchModerationId=?");
            st.setString(1, BranchModerationId);
            ResultSet rs=st.executeQuery();

            while(rs.next())
            {
                SubjectBranchId+=rs.getString("SubjectBranchId")+",";
            }
            String Mark=SubjectBranchId.substring(0,SubjectBranchId.length()-1);
            return Mark;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Moderation1.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }


    public static void main(String[] args) throws Exception
    {
        Moderation1 moderationObj=new Moderation1();
        moderationObj.ApplyModerationToLLM("18", "2", "2");
    } 
}

