package model;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CourseSelection {

    HttpServletRequest request;
    HttpServletResponse response;
    String CenterId = null;
    String errorMessage=null;

    Hashtable<String,String> ApplicationNo=new Hashtable<String, String>();
     Hashtable<String,String> CourseNames=new Hashtable<String, String>();
    Hashtable<Integer,String> courseList=new Hashtable<Integer, String>();
    private ArrayList<String> CourseId=new ArrayList<String>();
    public CourseSelection(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
        CenterId = (String) request.getSession().getAttribute("CenterId");
    }

    
    public String getErrorMessage() {
        return errorMessage;
    }
        public String getCourses() throws IOException, SQLException{
            Connection con =null;
        try {
            con = new DBConnection().getConnection();
             PreparedStatement Branchquery = con.prepareStatement("select B.BranchId,CONCAT(CourseCode,' - ',BranchName) as CourseName from BranchMaster B ,CourseMaster C where BranchId in (select BranchId from CollegeBranchMap where CollegeId=? and IsDeleted=0) and B.CourseId=C.CourseId");
             Branchquery.setInt(1,Integer.parseInt(CenterId));
             ResultSet branch = Branchquery.executeQuery();
             while(branch.next())
             {
                 CourseId.add(branch.getString(1));

             }

             return null;
        } catch (SQLException ex) {
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
         {
             if (con != null)
             {
                con.close();
             }
         }
}

    public boolean Execute() throws IOException, SQLException {
        Connection con =null;
        try {
            response.getWriter().print("Execute");
            con = new DBConnection().getConnection();
            String select[] = request.getParameterValues("course");
            getCourses();
            if (select != null && select.length != 0) {
            if(select.length<11){
            if (CenterId != null) {
                int CollegeId=Integer.parseInt(CenterId);
                Calendar now = Calendar.getInstance();
                int dd = now.get(Calendar.DATE) ;
                int mm = now.get(Calendar.MONTH) + 1;
                int yy = now.get(Calendar.YEAR);
                String startYear = yy + "-" + mm + "-" + dd;
                PreparedStatement query = con.prepareStatement("insert into CollegeBranchMap(CollegeId,BranchId,ModifiedDate,OrderNo,IsDeleted) values(?,?,?,?,0)");
                    for (int i = 0; i < select.length; i++) {
                        response.getWriter().print("select "+select[i]);
                        if(!CourseId.contains(select[i])||CourseId.isEmpty()){
                        query.setInt(1,CollegeId );
                        query.setInt(2, Integer.parseInt(select[i]));
                        query.setString(3, startYear);
                        query.setString(4,"213");
                        query.executeUpdate();
                         response.getWriter().print("inserted="+select[i]+"<br>");
                        }
                    }

         for(int i=0;i<CourseId.size();i++){
             response.getWriter().print("Course "+CourseId.get(i) );
             query=con.prepareStatement("update CollegeBranchMap set Orderno=?,IsDeleted=1  where CollegeId=? and BranchId=? and IsDeleted=0");
                    if(!contains(select, CourseId.get(i)))
                    {

                  query.setString(1,"123");
                  query.setInt(2,CollegeId);
                  query.setInt(3, Integer.parseInt(CourseId.get(i)));
                  query.executeUpdate();
                  response.getWriter().print("deleted"+CollegeId+"<-Collegeid"+CourseId.get(i)+"*<br>");
                    }
                }
                }
           return true;
            }
            else{ errorMessage="You can opt Maximum 10 courses";
                        return false;}
        }else{ errorMessage="No Courses are selected";
                return false;
                
        } }catch (SQLException ex) {
            response.getWriter().print(ex);
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
         finally
         {
             if (con != null)
             {
                con.close();
             }
         }
    }
private boolean  contains(String[] array,String element)

{
     if(array==null)
         return true;
    for(int i=0;i<array.length;i++)
    {
          if(array[i].equals(element))
              return true;
    }
     return false;
}
    public Hashtable getCenterCourses() throws IOException, SQLException{
        Connection con =null;
        try {
            
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select BranchId from CollegeBranchMap where CollegeId=? and IsDeleted=0");
             query.setInt(1, Integer.parseInt(CenterId));
             ResultSet records=query.executeQuery();
             int i=0;
             while(records.next()){
                 i++;
                  courseList.put(i, records.getString(1));
                  
             }
             return courseList;
        } catch (SQLException ex) {
            response.getWriter().print(ex);
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
         {
             if (con != null)
             {
                con.close();
             }
         }
 
    }

    public Set ListCenterCourseNames() throws SQLException
    {
        Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select BranchId,DisplayName from BranchMaster where BranchId in(select BranchId from CollegeBranchMap where CollegeId=?)");
             query.setInt(1, Integer.parseInt(CenterId));
             ResultSet Records=query.executeQuery();
                while (Records.next()) {

                    CourseNames.put(Records.getString(1), Records.getString(2));
                }

             Set CourseList=CourseNames.entrySet();
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
         {
             if (con != null)
             {
                con.close();
             }
         }
}

        public Set ListCourseNames() throws SQLException
    {
        Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select BranchId,DisplayName from BranchMaster order by DisplayName   ");
             ResultSet Records=query.executeQuery();
                while (Records.next()) {

                    CourseNames.put(Records.getString(1), Records.getString(2));
                }

             Set CourseList=CourseNames.entrySet();
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
         {
             if (con != null)
             {
                con.close();
             }
         }
}
        public Set ListCourseNamesRegular() throws SQLException
    {
        Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select BranchId,DisplayName from BranchMaster where   BranchId not in(5,6,7,8,9,10,11,18,13) order by DisplayName   ");
             ResultSet Records=query.executeQuery();
                while (Records.next()) {

                    CourseNames.put(Records.getString(1), Records.getString(2));
                }

             Set CourseList=CourseNames.entrySet();
            return CourseList;
        } catch (SQLException ex) {
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
         {
             if (con != null)
             {
                con.close();
             }
         }
}
   public Set ListOfApplicationNo() throws IOException, SQLException
   {
       Connection con =null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select QuestionId from StudentLoginMaster where StudentId in (SELECT StudentId FROM StudentPersonal where CollegeId=? )");
            query.setInt(1, Integer.parseInt(CenterId));
               response.getWriter().print(CenterId);
             ResultSet Records=query.executeQuery();
                while (Records.next()) {
                    ApplicationNo.put(Records.getString(1), Records.getString(2));
                }

             Set StudentList=ApplicationNo.entrySet();
            return StudentList;

        } catch (SQLException ex) {
            Logger.getLogger(CourseSelection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
         finally
         {
             if (con != null)
             {
                con.close();
             }
         }
}

     public static void main(String[] args) throws Exception
    {


     
  

}
}

