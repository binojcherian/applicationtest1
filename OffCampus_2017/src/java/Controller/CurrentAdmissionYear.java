/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import model.*;

public class CurrentAdmissionYear
{
    AdmissionYear year=new AdmissionYear();
    public int getCurrentAdmissionYear() throws SQLException
    {
        return year.getCurrentAdmissionYear();
    }
    public boolean IsAdmissionDateOver() throws SQLException
    {
        return year.IsAdmissionDateOver();
    }
    public String IsAdmissionDateOverForApplicationNumber(String ApplicationNumber) throws SQLException
    {
        return year.IsAdmissionDatesOverForApplicationNumber(ApplicationNumber);
    }
    public ArrayList<Integer> getAdmissionYears() throws SQLException
    {
        return year.getAdmissionYears();
    }
    public String IsReAdmissionDatesOver() throws SQLException
    {
        return year.IsReAdmissionDatesOver();
    }
}
