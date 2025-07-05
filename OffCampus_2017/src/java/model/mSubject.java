/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import Entity.Subject;
import Entity.SubjectBranch;
import Entity.SubjectExam;
import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgu
 */
public class mSubject {


//    SELECT S.`SubjectBranchId`, S.`SubjectId`,sm.SubjectName FROM SubjectBranchMaster S inner join SubjectMaster sm on S.`SubjectId`=sm.`SubjectId`
//WHERE S.`BranchId` =21 AND SubBranchId=6 And AcademicYear=(select max(AcademicYear) from SubjectBranchMaster)
//And  (S.`CurrentYearSem`=3 or S.`CurrentYearSem`=4  )
//


        public ArrayList<SubjectBranch> getAllSubjectsForMBAOrMCOM_3AND4_SEM(int  BranchId,int SubBranch) throws SQLException
{
   
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(" SELECT S.`SubjectBranchId`, S.`SubjectId`,sm.SubjectName FROM SubjectBranchMaster S"
                    + " inner join SubjectMaster sm on S.`SubjectId`=sm.`SubjectId` WHERE S.`BranchId` =? AND SubBranchId=? And "
                    + "AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=? and CurrentYearSem=3) And  (S.`CurrentYearSem`=3 or S.`CurrentYearSem`=4  ) ");
            st.setInt(1, BranchId);
            st.setInt(2, SubBranch);
            st.setInt(3, BranchId);
            ResultSet rs=st.executeQuery();
            ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
            int k=0;
            while(rs.next())
            {
                SubjectBranch subjectBranch=new SubjectBranch();
                SubjectExam sub=new SubjectExam();
                subjectBranch.setSubject(sub);
                subjectBranch.setSubjectBranchId(rs.getInt(1));
                System.out.println("---"+rs.getInt(1)+"***"+k++);
                sub.setSubjectId(rs.getInt(2));
                sub.setSubjectName(rs.getString(3));
                list.add(subjectBranch);
            }
           return list;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mSubject.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
 }




        public ArrayList<SubjectBranch> getAllSubjectsForLLM_1AND2_SEM(int  BranchId,int SubBranch) throws SQLException
{
   
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(" SELECT S.`SubjectBranchId`, S.`SubjectId`,sm.SubjectName FROM SubjectBranchMaster S"
                    + " inner join SubjectMaster sm on S.`SubjectId`=sm.`SubjectId` WHERE S.`BranchId` =? AND SubBranchId=? And "
                    + "AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=?) And  (S.`CurrentYearSem`=1 or S.`CurrentYearSem`=2  ) ");
            st.setInt(1, BranchId);
            st.setInt(2, SubBranch);
            st.setInt(3, BranchId);
            ResultSet rs=st.executeQuery();
            ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
            while(rs.next())
            {
                SubjectBranch subjectBranch=new SubjectBranch();
                SubjectExam sub=new SubjectExam();
                subjectBranch.setSubject(sub);
                subjectBranch.setSubjectBranchId(rs.getInt(1));
                sub.setSubjectId(rs.getInt(2));
                sub.setSubjectName(rs.getString(3));
                list.add(subjectBranch);
            }
           return list;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mSubject.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
 }


 public ArrayList<SubjectBranch> getAllSubjectsForLLM_3rdYEAR(int  BranchId,int SubBranch) throws SQLException
{
   
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(" SELECT S.`SubjectBranchId`, S.`SubjectId`,sm.SubjectName FROM SubjectBranchMaster S"
                    + " inner join SubjectMaster sm on S.`SubjectId`=sm.`SubjectId` WHERE S.`BranchId` =? AND SubBranchId=? And "
                    + "AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=?) And  S.`CurrentYearSem`=3 ");
            st.setInt(1, BranchId);
            st.setInt(2, SubBranch);
            st.setInt(3, BranchId);
            ResultSet rs=st.executeQuery();
            ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
            while(rs.next())
            {
                SubjectBranch subjectBranch=new SubjectBranch();
                SubjectExam sub=new SubjectExam();
                subjectBranch.setSubject(sub);
                subjectBranch.setSubjectBranchId(rs.getInt(1));
                sub.setSubjectId(rs.getInt(2));
                sub.setSubjectName(rs.getString(3));
                list.add(subjectBranch);
            }
           return list;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mSubject.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
 }


 public ArrayList<SubjectBranch> getAllSubjectsForMCA_5AND6_SEM(int  BranchId,int SubBranch) throws SQLException
{
   
    Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement(" SELECT S.`SubjectBranchId`, S.`SubjectId`,sm.SubjectName FROM SubjectBranchMaster S"
                    + " inner join SubjectMaster sm on S.`SubjectId`=sm.`SubjectId` WHERE S.`BranchId` =? AND SubBranchId=? And "
                    + "AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where BranchId=?) And  (S.`CurrentYearSem`=5 or S.`CurrentYearSem`=6  ) ");
            st.setInt(1, BranchId);
            st.setInt(2, SubBranch);
            st.setInt(3, BranchId);
            ResultSet rs=st.executeQuery();
            ArrayList<SubjectBranch> list=new ArrayList<SubjectBranch>();
            while(rs.next())
            {
                SubjectBranch subjectBranch=new SubjectBranch();
                SubjectExam sub=new SubjectExam();
                subjectBranch.setSubject(sub);
                subjectBranch.setSubjectBranchId(rs.getInt(1));
                sub.setSubjectId(rs.getInt(2));
                sub.setSubjectName(rs.getString(3));
                list.add(subjectBranch);
            }
           return list;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(mSubject.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    finally
    {
        if(con!=null)
        {
            con.close();
        }
    }
 }



}
