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

public class AssistantToSO
{
HttpServletRequest request;
HttpServletResponse response;
UserCreation User=new UserCreation();
String Assistant="-1",SO="1",AssistantError;
boolean IsSaved=false;
public AssistantToSO(HttpServletRequest request,HttpServletResponse response) throws SQLException
{
    this.request=request;
    this.response=response;
    Assistant=request.getParameter("Assistant");
    if(request.getParameter("SO")!=null && !(request.getParameter("SO").equals(" ")))
    {
        SO=request.getParameter("SO");
        if(SO!=null)
        request.getSession().setAttribute("SO", SO);
    }
    if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Assign"))
    {
        AssistantError=getAsstError();
        if(AssistantError==null)
        {
            IsSaved=User.AssignSOToAssistant(Assistant, SO);
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
public String getAssistant()
{
    return Assistant;
}
public String getSaved()
{
    if(IsSaved)
        return "User Assigned Successfully";
    return null;
}
public String AssistantError()
{
    return AssistantError;
}
public String getAsstError()
{
    if(Assistant==null || Assistant.equals("-1"))
    {
        return "Please Select Assistant";
    }
    return null;
}
public ArrayList<User> getAssignedAssistants(String SO) throws SQLException
{
    return User.getAllAssistantsofSO(SO);
}
public User getAssignedAssistantsgetAssigedAssistantRowToDelete(String AssistantSOId) throws SQLException
{
    return User.getAssigedAssistantRowToDelete(AssistantSOId);
}
public boolean RemoveAssistantFromSO(String AssistantSOId) throws SQLException
{
    return User.RemoveAssistantfromSO(AssistantSOId);
}
}
