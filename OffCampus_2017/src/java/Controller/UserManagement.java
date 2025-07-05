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
import Entity.*;

public class UserManagement
{
    HttpServletRequest request;
    HttpServletResponse response;
    String UserName,Password,ConfirmPassword,Category="-1",Privilege, Name,UserError,NameError,CategoryError,PasswordError;
    boolean IsSaved;
    UserCreation User=new UserCreation();

public UserManagement(HttpServletRequest request,HttpServletResponse response) throws SQLException
{
    this.request=request;
    this.response=response;
    UserName=request.getParameter("UserName");
    Password=request.getParameter("Password");
    ConfirmPassword=request.getParameter("CPassword");
    Category=request.getParameter("Category");
    Name=request.getParameter("Name");
    
    if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Submit"))
    {
      UserError=getUserError();
      if(UserError==null)
      {
          PasswordError=getPasswordError();
          if(PasswordError==null)
          {
              NameError=getNameError();
              if(NameError==null)
              {
                  CategoryError=getCategoryError();
                  if(CategoryError==null)
                  {
                      if(Category.equals("1"))
                      {
                          Privilege="32";
                      }
                      if(Category.equals("2"))
                      {
                          Privilege="33";
                      }
                      UserError=User.IsUserAlreadyExists(UserName);
                      if(UserError==null)
                      {
                        IsSaved=User.SaveUser(UserName, Password, Name, Privilege);
                        UserName="";
                        Password="";
                        ConfirmPassword="";
                        Category="-1";
                        Name="";
                      }
                  }
              }
          }
      }
    }

//    if(request.getParameter("Search")!=null && request.getParameter("Search").equals("Search"))
//    {
//        getMarkEntryCount(request.getParameter(Name))
//    }
}
public String UserError()
{
   return UserError;
}
public String NameError()
{
   return NameError;
}
public String PasswordError()
{
    return PasswordError;
}
public String CategoryError()
{
    return CategoryError;
}
public String getUserName()
{
    if(UserName==null)
        return "";
    return UserName;
}
public String getPassword()
{
    if(Password==null)
        return "";
    return Password;
}
public String getIsSaved()
{
    if(IsSaved==true)
        return "User Added Successfully";
    return null;
}
public String getName()
{
    if(Name==null)
        return "";
    return Name;
}
public String getCategory()
{
    if(Category==null)
        return "-1";
    return Category;
}
public String getUserError()
{
    if(UserName==null || UserName.trim().isEmpty())
    {
        return "Enter UserName";
    }
    else  if(UserName.length()<6)
    {
        return "Minimum six characters required for UserName";
    }
    else
    {
        return null;
    }
}
public String getNameError()
{
    if(Name==null || Name.trim().isEmpty())
    {
        return "Enter Name";
    }
    else
    {
        return null;
    }
}
public String getPasswordError()
{
    if(Password==null || Password.trim().isEmpty())
    {
        return "Enter Password";
    }
    else
    {
        if(ConfirmPassword==null || ConfirmPassword.isEmpty() )
        {
            return "Enter Confirm Password";
        }
        else if(!(Password.equals(ConfirmPassword)))
        {
            Password="";
            return "Password does not matches with Confirm Password";
        }
        else
            return ValidatePassword();
    }
}
public  String ValidatePassword()
{

            if (Password.length() < 6) {
                return "Minimum 6 characters Required";
            } else {
                String iChars = "!@#$%^&*()+=-[]\';,./{}|\":<>?";
                int count = 0;
                for (int i = 0; i < Password.length(); i++) {
                    if (iChars.indexOf(Password.charAt(i)) != -1) {
                        count++;

                    }
                }
                if (count < 1) {
                    return "Passwords must contain special characters";

                }
                return null;
            }
    }
public String getCategoryError()
{
    if(Category==null || Category.equals("-1"))
    {
        return "Select Category";
    }
    else
    {
        return null;
    }
}
public ArrayList<User>getAllUsersInDesignationOrder() throws SQLException
{
    return User.getAllUsersInDesignationOrder();
}

public ArrayList<User> getAllUsers() throws SQLException
{
    return User.getAllUsers();
}
public boolean Deleteuser(String UserName,String Privilege) throws SQLException
{
    return User.DeleteUser(UserName, Privilege);
}
public boolean DeleteUserFromAssistantSOMapping(String UserName,String Privilege) throws SQLException
{
    return User.DeleteUserFromAssistantSOMapping(UserName, Privilege);
}
public boolean IsAssignedUser(String UserName) throws SQLException
{
    return User.IsAssignedUser(UserName);
}
public String getSOofAssistant(String Assistant) throws SQLException
{
    return User.getSOofAssistant(Assistant);
}
public int getTotalAbsenteesEnteredByUser(String UserName) throws SQLException
{
    return User.TotalAbsMalEntryStatusOfUser(UserName, 2);
}
public int getTotalMalEnteredByUser(String UserName) throws SQLException
{
    return User.TotalAbsMalEntryStatusOfUser(UserName, 3);
}
public int getTotalMarkEnteredByUser(String UserName) throws SQLException
{
    return User.TotalMarkEntryStatusOfUser(UserName, 0);
}
public int getTotalMarkVerifiedByUser(String UserName) throws SQLException
{
    return User.TotalMarkEntryStatusOfUser(UserName, 4);
}
public int getTotalCountOfStudentsRegisteredForExam() throws SQLException
{
    return User.getTotalCountOfStudentsRegisteredForExam();
}
public ArrayList<UserWiseCount> getMarkEntryCount(String SubjectBranchId) throws SQLException
{ 
    return User.getUserWiseCountOfMark(SubjectBranchId);
}

}
