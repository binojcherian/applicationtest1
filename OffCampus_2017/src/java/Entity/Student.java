/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

/**
 *
 * @author HP
 */
public class Student {
int StudentId;
String PRN;
String ApplicationNo,BranchName,CentreName;
String Name;
int BranchId;
int CentreId,JoiningYear;
String Erno;
public int getStudentId()
    {
    return StudentId;
}
public String getPRN()
    {
    return PRN;
}

public String getERNo()
    {
    return Erno;
}
public String getName()
    {
return Name;
}
public int getBranch()
    {
return BranchId;
}
public String getApplicationNo()
{
    return ApplicationNo;
}
public String getCentreName()
{
    return CentreName;
}
public String getBranchName()
{
    return BranchName;
}
public int getJoiningYear()
{
    return JoiningYear;
}
//===============Setter==============

public void setApplicationNo( String ApNo)
{
    this.ApplicationNo=ApNo;
}
public void setCentreName(String CName)
{
    this.CentreName=CName;
}
public void setBranchName(String BrName)
{
    this.BranchName=BrName;
}
public void setJoiningYear(int JYear)
{
    this.JoiningYear=JYear;
}
public void setStudentId(int StudentId )
    {
    this.StudentId=StudentId;
}
public void setPRN(String PRN)
    {
    this.PRN= PRN;
}
public void setERNo(String Erno)
    {
    this.Erno= Erno;
}
public void setName(String Name)
    {
this.Name= Name;
}
public void setBranch(int BranchId)
    {
this.BranchId=BranchId;
}
public void setCentreId(int CentreId)
    {
this.CentreId=CentreId;
}
}

