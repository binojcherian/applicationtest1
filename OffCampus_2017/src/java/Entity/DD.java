/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

import java.util.ArrayList;

public class DD {
  private   int DDId,CentreId;
String DDNo;
public String Status;
public int Count;
    private  String  Branch,Bank,AmountType,DDDate,CentreName,DDNumber ;
    private float Amount;
private boolean SingleDD;
boolean Confirmed,MguApproved;
private     ArrayList<Student> StudentList=new ArrayList<Student>();

    public void setDDId(int DDId) {
        this.DDId=DDId;
    }

    public void setBranch(String Branch) {
        this.Branch=Branch;
    }

    public void setBank(String Bank) {
        this.Bank=Bank;
    }

    public void setAmount(float Amount) {
        this.Amount=Amount;
    }

    public void setAmountType(String AmountType) {
        this.AmountType=AmountType;
    }

    public void setDDDate(String DDDate) {
        this.DDDate=DDDate;
    }

    public void setCentreName(String CentreName) {
        this.CentreName=CentreName;
    }

    public void setDDNo(String DDNo) {
        this.DDNo=DDNo;
    }

    public void setDDNumber(String DDNo) {
        this.DDNumber=DDNo;
    }

    public void setCentreId(int CentreId) {
        this.CentreId=CentreId;
    }
    public void  addStudent(Student student)
    {
         StudentList.add(student);
    }
    //=======================get=================================
        public int getDDId() {
        return DDId;
    }

    public String getBranch() {
        return Branch;
    }

    public String getBank() {
        return Bank;
    }

    public float getAmount() {
        return Amount;
    }

    public String getAmountType() {
        return AmountType;
    }

    public String getDDDate() {
        return DDDate;
    }

    public String getCentreName() {
        return CentreName;
    }

    public String getDDNo()
    {
      return DDNo;
    }

    public String getDDNumber()
    {
      return DDNumber;
    }
    public int getCentreId() {
        return CentreId;
    }
    public ArrayList<Student> getStudentList()
    {
        return StudentList;
    }

    public boolean isConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(boolean Confirmed) {
        this.Confirmed = Confirmed;
    }
     public boolean isMguApproved() {
        return MguApproved;
    }

    public void setMguApproved(boolean MguApproved) {
        this.MguApproved = MguApproved;
    }

    
}
