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
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.*;
public class SubjectWisePRN
{

    public ArrayList<Entity.StudentData> getStudentsRegisteredForSubject(String SubjectBranchId) throws SQLException
    {
         Connection con=null;
    try {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("select p.StudentName,p.PRN from StudentPersonal p inner join ExamRegistrationMaster e  on e.StudentId=p.StudentId and e.SubjectBranchId=? and e.ExamId=7");
            st.setString(1, SubjectBranchId);

            ResultSet Rs = st.executeQuery();
            Entity.StudentData Course=null;
            ArrayList<Entity.StudentData> Branches=new ArrayList<Entity.StudentData>();
            while (Rs.next()) {
                Course=new Entity.StudentData();
                Course.StudentName=Rs.getString(1);
                Course.PRN=Rs.getString(2);
                Branches.add(Course);
            }

           return  Branches;

           } catch (SQLException ex) {
            Logger.getLogger(Absentees.class.getName()).log(Level.SEVERE, null, ex);

           return null;
        }
    finally{
        if(con!=null)
                 con.close();
             }

    }

public String getQuery()
{
    return "SELECT SQL_CALC_FOUND_ROWS p.PRN ,p.StudentName FROM `DEMS_db`.`ExamRegistrationMaster`  e  inner join StudentPersonal p on e.StudentID=p.Studentid  and e.ExamId=7 inner join SubjectBranchMaster b on b.SubjectBranchID=e.SubjectBranchId  inner join SubjectMaster s on s.SubjectId=b.SubjectId and  b.SubjectId=?";
}
}

