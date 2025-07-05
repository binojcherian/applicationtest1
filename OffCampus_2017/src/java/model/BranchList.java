/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;


import Entity.*;
import Entity.SubjectBranch;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author mgu
 */
public class BranchList {
    ArrayList<String> BranchId,BranchCode ,BranchName,BranchDisplayName;
    ArrayList<CourseData> Branches;
    String SearchFilter,SearchBranch,SearchCode;

    HttpServletRequest request;
    HttpServletResponse response;



//    public ArrayList<ArrayList<SubjectBranch>> getOptionalForBranch(int BranchId,int year) 
//    {
//        Connection con = null;
//        try {
//            
//            con = new DBConnection().getConnection();
//            PreparedStatement st = con.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? LIMIT 0,1000");
//            st.setInt(1, BranchId);
//            ResultSet rs = st.executeQuery();
//            if (rs.next()) {
//                if (rs.getInt(1) == 0) {
//                    st = con.prepareStatement("SELECT S.`SubjectBranchId` , S.`SubjectId`,sm.SubjectName ,paperNo FROM DEMS_db.SubjectBranchMaster S ,SubjectMaster sm " + "WHERE S.`BranchId`=? AND S.`IsOptionalSubject`=1 and  paperNo In( SELECT paperNo FROM DEMS_db.SubjectBranchMaster S " + "WHERE S.`BranchId`=? AND S.`IsOptionalSubject`=1 and (CurrentYearSem=? or CurrentYearSem=?) " + "group by paperNo having count(*)>1) and sm.SubjectId=S.`SubjectId` order by paperNo");
//                    st.setInt(1, BranchId);
//                    st.setInt(2, BranchId);
//                    if (year == 1) {
//                        st.setInt(3, 1);
//                        st.setInt(4, 2);
//                    } else if (year == 2) {
//                        st.setInt(3, 3);
//                        st.setInt(4, 4);
//                    }else if (year == 3) {
//                        st.setInt(3, 5);
//                        st.setInt(4, 6);
//                    }
//                } else {
//                    st = con.prepareStatement("SELECT S.`SubjectBranchId` , S.`SubjectId`,sm.SubjectName,paperNo FROM DEMS_db.SubjectBranchMaster S ,SubjectMaster sm " + "WHERE S.`BranchId`=? AND S.`IsOptionalSubject`=1 and  paperNo In( SELECT paperNo FROM DEMS_db.SubjectBranchMaster S " + "WHERE S.`BranchId`=? AND S.`IsOptionalSubject`=1 and  CurrentYearSem=? " + "group by paperNo having count(*)>1) and sm.SubjectId=S.`SubjectId` order by paperNo");
//                    st.setInt(1, BranchId);
//                    st.setInt(2, BranchId);
//                    st.setInt(3, year);
//                }
//                ResultSet Optionals = st.executeQuery();
//               String optionalChange = null;
//                ArrayList<ArrayList<SubjectBranch>> OptionalList = new ArrayList<ArrayList<SubjectBranch>>();
//                ArrayList<SubjectBranch> temp = new ArrayList<SubjectBranch>();
//                while (Optionals.next()) {
//                    if (optionalChange == null) {
//                        optionalChange = Optionals.getString(4);
//                    }
//                    SubjectBranch sb = new SubjectBranch();
//                    SubjectExam subject = new SubjectExam();
//                   subject.setSubjectId(Optionals.getInt(2));
//                    subject.setSubjectName(Optionals.getString(3));
//                    sb.setSubject(subject);
//                    sb.setSubjectBranchId(Optionals.getInt(1));
//                    //OptionalList.add(temp);
//                    if (optionalChange.equals(Optionals.getString(4)) ){
//                        temp.add(sb);
//                    } else {
//                        OptionalList.add(temp);
//                        temp = new ArrayList<SubjectBranch>();
//                        temp.add(sb);
//                    }
//                }
//                OptionalList.add(temp);
//                return OptionalList;
//            } // rs.next
//            return null;
//        } catch (SQLException ex) {
//            Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//        finally
//        {
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }

    public ArrayList<ArrayList<SubjectBranch>> getOptionalForBranch(int BranchId,int year)
    {
        try {
            Connection con = null;
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT B.`SemorYear` FROM BranchMaster B WHERE B.`BranchId`=? LIMIT 0,1000");
            st.setInt(1, BranchId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                    st = con.prepareStatement("SELECT S.`SubjectBranchId` , S.`SubjectId`,sm.SubjectName ,paperNo FROM DEMS_db.SubjectBranchMaster S ,SubjectMaster sm " + "WHERE "
                            + "S.`BranchId`=? AND S.AcademicYear=(select max(AcademicYear) from SubjectBranchMaster where"
                            + " BranchId=? and "
                            + "(CurrentYearSem=? or "
                            + "CurrentYearSem=?)) AND S.`IsOptionalSubject`=1 and  paperNo In( SELECT paperNo FROM DEMS_db.SubjectBranchMaster S " + "WHERE "
                            + "S.`BranchId`=? AND S.`IsOptionalSubject`=1 and "
                            + "(CurrentYearSem=? or "
                            + "CurrentYearSem=?) " + "group by paperNo having count(*)>1) and sm.SubjectId=S.`SubjectId` and "
                            + "(CurrentYearSem=? or"
                            + " CurrentYearSem=?)order by paperNo");
                    st.setInt(1, BranchId);
                    st.setInt(2, BranchId);
                    if (year == 1) {
                        st.setInt(3, 1);
                        st.setInt(4, 2);
                        st.setInt(5, BranchId);
                        st.setInt(6, 1);
                        st.setInt(7, 2);
                        st.setInt(8, 1);
                        st.setInt(9, 2);
                        
                        
                    } else if (year == 2) {
                        st.setInt(3, 3);
                        st.setInt(4, 4);
                        st.setInt(5, BranchId);
                        st.setInt(6, 3);
                        st.setInt(7, 4);
                        st.setInt(8, 3);
                        st.setInt(9, 4);
                    }else if (year == 3) {
                        st.setInt(3, 5);
                        st.setInt(4, 6);
                        st.setInt(5, BranchId);
                        st.setInt(6, 5);
                        st.setInt(7, 6);
                        st.setInt(8, 5);
                        st.setInt(9, 6);
                    }
                    else
                    {
                        st.setInt(3,0);
                        st.setInt(4, 0);
                    }
                } else {
                    st = con.prepareStatement("SELECT S.`SubjectBranchId` , S.`SubjectId`,sm.SubjectName,paperNo FROM DEMS_db.SubjectBranchMaster S ,SubjectMaster sm " + "WHERE S.`BranchId`=? AND S.`IsOptionalSubject`=1 and  paperNo In( SELECT paperNo FROM DEMS_db.SubjectBranchMaster S " + "WHERE S.`BranchId`=? AND S.`IsOptionalSubject`=1 and  CurrentYearSem=? " + "group by paperNo having count(*)>1) and sm.SubjectId=S.`SubjectId` order by paperNo");
                    st.setInt(1, BranchId);
                    st.setInt(2, BranchId);
                    st.setInt(3, year);
                }
                ResultSet Optionals = st.executeQuery();
               String optionalChange = null;
                ArrayList<ArrayList<SubjectBranch>> OptionalList = new ArrayList<ArrayList<SubjectBranch>>();
                ArrayList<SubjectBranch> temp = new ArrayList<SubjectBranch>();
                while (Optionals.next()) {
                    if (optionalChange == null) {
                        optionalChange = Optionals.getString(4);
                    }
                    SubjectBranch sb = new SubjectBranch();
                    SubjectExam subject = new SubjectExam();
                    subject.setSubjectId(Optionals.getInt(2));
                    subject.setSubjectName(Optionals.getString(3));
                    sb.setSubject(subject);
                    sb.setSubjectBranchId(Optionals.getInt(1));
                    //OptionalList.add(temp);
                    if (optionalChange.equals(Optionals.getString(4)) ){
                        temp.add(sb);
                    } else {
                        OptionalList.add(temp);
                        temp = new ArrayList<SubjectBranch>();
                        temp.add(sb);
                    }
                }
                OptionalList.add(temp);
                return OptionalList;
            } // rs.next
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<CourseData> BranchList()
    {
         Connection con=null ;
             Branches=new ArrayList<CourseData>();
      try {
           con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT Distinct C.BranchId ,Displayname FROM CollegeBranchMap C ,BranchMaster B where C.BranchId=B.BranchId and IsDeleted=0 order by DisplayName ");
            ResultSet Rs = st.executeQuery();
           CourseData Course=null;
       
            while (Rs.next())
            {
                Course=new CourseData();
                Course.BranchId=Rs.getInt(1);
                Course.BranchName=Rs.getString(2);
                Branches.add(Course);
            }
             Rs.close();
             con.close();
          return  Branches;

           } catch (SQLException ex)
           {
            Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if(con!=null)
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex1);
            }
           return Branches;
          }
       
    }

    public String Branchs()
    {   try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT BranchMaster.BranchId,BranchCode,BranchName,DisplayName,CourseId FROM BranchMaster order by DisplayName");
            ResultSet Rs = st.executeQuery();
            BranchId = new ArrayList<String>();
            BranchCode = new ArrayList<String>();
            BranchName = new ArrayList<String>();
            BranchDisplayName = new ArrayList<String>();

            while (Rs.next()) {
               BranchId.add(Rs.getString(1));
                BranchCode.add(Rs.getString(2));
                BranchName.add(Rs.getString(3));
                 BranchDisplayName.add(Rs.getString(4));
                            }
            con.close();
          return null;
        } catch (SQLException ex) {
            Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }

    }

public int  getCount()
{
    return BranchId.size();
}
public String getBranchId(int index)
{
    return BranchId.get(index);
}
public String getBranchCode(int index)
{
 return BranchCode.get(index);
}
public String getBranchName(int index)
{
    return BranchName.get(index);
}
public String getBranchDisplayName(int index)
{
    return BranchDisplayName.get(index);
}
public ArrayList<CourseData> getBranchListOfCentre(int CentreId)
{
    try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT C.BranchId ,Displayname FROM CollegeBranchMap C ,BranchMaster B where C.BranchId=B.BranchId and IsDeleted=0 and CollegeId=? order by DisplayName");
         st.setInt(1, CentreId);
            ResultSet Rs = st.executeQuery();
           CourseData Course=null;
           Branches=new ArrayList<CourseData>();
            while (Rs.next()) {
                Course=new CourseData();
                Course.BranchId=Rs.getInt(1);
                Course.BranchName=Rs.getString(2);
                Branches.add(Course);
            }
                 con.close();
           return  Branches;
      
           } catch (SQLException ex) {
            Logger.getLogger(BranchList.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }

}

public static void main(String args[]) {

BranchList b= new BranchList();

b.getOptionalForBranch(7, 1) ;


}
}
