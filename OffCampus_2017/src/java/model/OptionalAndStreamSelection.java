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

public class OptionalAndStreamSelection
{
    public boolean IsStudentOptionalAlreadyEntered(int StudentId,String SubjectBranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(*) from StudentSubjectBranchMap where StudentId=? and SubjectBranchId=?");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

    public boolean SaveOptionalSubject(int StudentId,String SubjectBranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into StudentSubjectBranchMap (StudentId,SubjectBranchId) values(?,?)");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            st.execute();
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }
     public boolean SaveOptionalSubjectMBAorMcom(int StudentId,String SubBranchId,int subjectBranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into StudentSubjectBranchMap (StudentId,SubjectBranchId,SubBranchId) values(?,?,?)");
            st.setInt(1, StudentId);
            st.setInt(2, subjectBranchId);
             st.setString(3, SubBranchId);
            st.execute();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }
    public boolean SaveAllSubjectForBComStudent(int StudentId,String BranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st1 = con.prepareStatement("select SubjectBranchId from SubjectBranchMaster where BranchId=? and IsElective=0 and IsOptionalSubject=0");
            st1.setString(1, BranchId);
            ResultSet Subject = st1.executeQuery();
            while(Subject.next())
            {
                PreparedStatement st2 = con.prepareStatement("insert into StudentSubjectBranchMap (StudentId,SubjectBranchId) values(?,?)");
                st2.setInt(1, StudentId);
                st2.setString(2, Subject.getString("SubjectBranchId"));
                st2.execute();
            }
            return true;
        }
        catch(SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

    public CourseData getOptionalSubjectOfStudent(int StudentId) throws SQLException
    {
        Connection con=null;
        CourseData Course=new CourseData();
        try
        {
            con=new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("select s.SubjectName,b.SubjectBranchId from SubjectMaster s,SubjectBranchMaster b,StudentSubjectBranchMap sb where s.SubjectId=b.SubjectId and b.SubjectBranchId=sb.SubjectBranchId and sb.StudentId=? and b.IsElective=1 and b.IsOptionalSubject=1");
            st.setInt(1, StudentId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                Course.BranchId=rs.getInt("SubjectBranchId");
                Course.BranchName=rs.getString("SubjectName");
                return Course;
            }
            return null;
        }
        catch(SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean SaveSupplementarySubject(int StudentId,String subjectBranchId,String Type) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId,RegistrationType,ExamType) values(?,?,(select max(ExamId) from ExamMaster),1,?)");
            st.setInt(1, StudentId);
            st.setString(2, subjectBranchId);
            st.setString(3, Type);
            //st.setInt(3, 2);// select max for examId
            //System.out.println(st);
            st.execute();
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }
    
     public boolean SaveSupplementarySubjectForCourseCompleted(int StudentId,String subjectBranchId,String examCentre,String PaperType) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId,RegistrationType,ExamCentre,ExamType) values(?,?,(select max(ExamId) from ExamMaster),1,?,?)");
            st.setInt(1, StudentId);
            st.setString(2, subjectBranchId);
            st.setString(3, examCentre);
             st.setString(4, PaperType);
            st.execute();
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

     public boolean IsStudentSupplementaryAlreadyEntered(int StudentId,String SubjectBranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("Select count(*) from ExamRegistrationMaster where StudentId=? and SubjectBranchId=?");
            st.setInt(1, StudentId);
            st.setString(2, SubjectBranchId);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1)>0)
                {
                    return true;
                }
            }
            return false;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }



     public boolean SaveOptionaltoExamRegistration(int StudentId,String subjectBranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into ExamRegistrationMaster (StudentId,SubjectBranchId,ExamId,RegistrationType) values(?,?,(select max(ExamId) from ExamMaster),0)");
            st.setInt(1, StudentId);
            st.setString(2, subjectBranchId);
            
            //st.setInt(3, 2);// select max for examId
           
            st.execute();
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }



     public boolean SaveOptionalselectionStatus(int StudentId,String BranchId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("insert into StudentoptionSelected (StudentId,OptionsupplySelection,Selectedon,BranchId,examId) values(?,?,sysdate(),?,(SELECT max(ExamId) FROM ExamMaster E))");
            st.setInt(1, StudentId);
            st.setInt(2,1);
            st.setInt(3,Integer.parseInt(BranchId));
            st.execute();
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }

     public boolean RevertMguApproval(int StudentId) throws SQLException
    {
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st=con.prepareStatement("update StudentExamFeeStatus set IsMguApproved=0 where StudentId=? and examId=(select max(ExamId) from ExamMaster)");
            st.setInt(1, StudentId);
                    st.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(OptionalAndStreamSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
