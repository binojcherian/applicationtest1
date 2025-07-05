/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.UserCreation;
import Entity.User;

public class UserToCourse
{
HttpServletRequest request;
HttpServletResponse response;
UserCreation User=new UserCreation();
String Course="-1",SO="1",CourseError,AssignCourseError=" ";
boolean IsSaved=false;
public UserToCourse(HttpServletRequest request,HttpServletResponse response) throws SQLException
{
    this.request=request;
    this.response=response;
    Course=request.getParameter("Course");
    if(request.getParameter("SO")!=null && !(request.getParameter("SO").equals(" ")))
    {
        SO=request.getParameter("SO");
        if(SO!=null)
        request.getSession().setAttribute("SO", SO);
    }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Assign"))
    {
        CourseError=getCourseError();
        if(CourseError==null)
        {
            AssignCourseError=User.IsExistData(Course,SO);
                      if(AssignCourseError==null)
                      {
                        IsSaved=User.UserToCourse(Course, SO);}
        }
    }
    if(request.getParameter("AV")!=null && request.getParameter("AV").equals("1"))
    {
        if(request.getSession().getAttribute("SO")!=null)
        SO=request.getSession().getAttribute("SO").toString();

    }
    if(request.getParameter("AV")==null && request.getParameter("Submit")==null && request.getParameter("SO")==null)
    {
        request.getSession().removeAttribute("SO");
    }
}
public String getSO()
{
    if(request.getSession().getAttribute("SO")!=null)
        return request.getSession().getAttribute("SO").toString();
    return SO;
}
public String getCourse()
{
    return Course;
}
public String getSaved()
{
    if(IsSaved)
        return "User Map Successfully";
    return null;
}
public String CourseError()
{
    return CourseError;
}
public String AssignCourseError()
{
    return AssignCourseError;
}
public String getCourseError()
{
    if(Course==null || Course.equals("-1"))
    {
        return "Please Select Course";
    }
    return null;
}
public ArrayList<User> getAssignedCourses(String SO) throws SQLException
{
    return User.getAllCoursesofUser(SO);
}
public User getAssignedCourseRowToDelete(String UserCourseId) throws SQLException
{
    return User.getAssigedCourseRowToDelete(UserCourseId);
}
public boolean RemoveCourseFromUser(String UserCourseId) throws SQLException
{
    return User.RemoveCourseFromUser(UserCourseId);
}
}
