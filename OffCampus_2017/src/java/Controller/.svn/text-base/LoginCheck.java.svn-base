/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

/**
 *
 * @author mgu
 */
public class LoginCheck {
     public static String Password(String password)
    {
        return emptyCheck("Password", password);
    }
public static String newPassword(String password) {
        String error = emptyCheck("Password", password);
        if (error == null) {
            if (password.length() < 6) {
                return "Minimum 6 characters Required";
            } else {
                String iChars = "!@#$%^&*()+=-[]\';,./{}|\":<>?";
                int count = 0;
                for (int i = 0; i < password.length(); i++) {
                    if (iChars.indexOf(password.charAt(i)) != -1) {
                        count++;

                    }
                }
                if (count < 1) {
                    return "Passwords must contain special characters";

                }
                return null;
            }

        } else {
            return error;
        }
    }



    public static String ConfirmPassword(String password,String c_password)
    {
        if(password.equals(c_password))
            return null;
        else
           return "Passwords do not match";

    }
     public static String emptyCheck(String field,String str)
    {
       if(str==null)
           return field +" is Required";
        str=str.trim();
        if(str.isEmpty())
           return field +" is Required";
        else if((str.length())>60)
             return field+" sholud be less than 60 characters";
        else{

        return null;}
    }
}
