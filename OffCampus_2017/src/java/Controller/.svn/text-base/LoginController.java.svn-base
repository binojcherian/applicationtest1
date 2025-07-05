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
import model.*;
import Entity.*;
public class LoginController
{
HttpServletRequest request;
HttpServletResponse response;
String UserName=null,Password=null,Message=null;
int Role=-1;
login loginobj=new login();
public LoginController(HttpServletRequest request,HttpServletResponse response) throws SQLException
{
    this.request=request;
    this.response=response;
    if(request.getParameter("UserName")!=null)
    {
        UserName=request.getParameter("UserName");
    }
    if(request.getParameter("Password")!=null)
    {
        Password=request.getParameter("Password");
    }
    if(request.getParameter("Submit")!=null)
    {
        if(UserName==null || UserName.isEmpty())
        {
            Message="Enter userName";
        }
        else if(Password==null || Password.isEmpty())
        {
            Message="Enter Password";
        }
        else
        {
            Role=loginobj.getPrivilege(UserName, Password);
            
        }
    }
}
public String getUsername()
{
    return UserName;
}
public String getPassword()
{
    return Password;
}
public String getMessage()
{
    return Message;
}
public int getRole()
{
    if(Role==-1)
    {
        return 0;
    }
    else
    {
        return Role;
    }
}

public int getPrivilege(String UserName) throws SQLException
{
    return loginobj.getPrivilege(UserName);
}
}
