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
import Entity.User;

public class FalseNoTOAssistant
{
    HttpServletRequest request;
    HttpServletResponse response;
    String FalseNo_From=null, FalseNo_To=null,Assistant="-1",Count="0",FromError=null,ToError=null, AssistantError=null;
    boolean IsSaved;
    FalseNumberAssistantMapping FalseNo=new FalseNumberAssistantMapping();
    public FalseNoTOAssistant(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
        this.request=request;
        this.response=response;

        FalseNo_From=request.getParameter("From");
        FalseNo_To=request.getParameter("To");
        Count=request.getParameter("Count");
        Assistant=request.getParameter("Assisteant");
        if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Asign"))
        {
            FromError=getFalseNoFromError();
            if(FromError==null)
            {
                ToError=getFalseNoToError();
                if(ToError==null)
                {
                    AssistantError=getAssistantError();
                    if(AssistantError==null)
                    {
                    IsSaved=FalseNo.AssignFalseNoToAssistant(Assistant, FalseNo_From, FalseNo_To, Count);
                    FalseNo_From="";
                    FalseNo_To="";
                    Count="";
                    Assistant="-1";
                    }
                }
            }
        }
    }

    public String getFalseNoFromError()
    {
        if(FalseNo_From==null || FalseNo_From.isEmpty())
        {
            return "Enter False No From";
        }
        return null;
    }
    public String getFalseNoToError()
    {
        if(FalseNo_To==null || FalseNo_To.isEmpty())
        {
            return "Enter False No To";
        }
        return null;
    }
    public String getAssistantError()
    {
        if(Assistant==null || Assistant.equals("-1"))
        {
            return "Select Assistant";
        }
        return null;
    }
    public String getAssistant()
    {
        return Assistant;
    }
    public String getFrom()
    {
        if(FalseNo_From==null)
            return "";
        else
            return FalseNo_From;
    }
    public String getTo()
    {
        if(FalseNo_To==null)
            return "";
        else
            return FalseNo_To;
    }
    public String AssistantError()
    {
        return AssistantError;
    }
    public String FromError()
    {
        return FromError;
    }
    public String ToError()
    {
        return ToError;
    }
    public String getIsSaved()
    {
        if(IsSaved)
            return "False No Range Assigned to Assistant";
        return null;
    }
}
