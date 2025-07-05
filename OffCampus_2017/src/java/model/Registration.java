

package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;


public class Registration {
    Hashtable<String,String> Personal=new Hashtable<String, String>();
    ArrayList<String> ReligionId=new ArrayList<String>();
    ArrayList<String> ReligionCode=new ArrayList<String>();
    ArrayList<String> CasteId=new ArrayList<String>();
    ArrayList<String> CasteCode=new ArrayList<String>();
int StudentId;
HttpServletResponse res;
public static String convertDatetoyyyymmdd(String date) {
        String dd = date.substring(0, 2);
        String mm = date.substring(3, 5);
        String yy = date.substring(6, 10);
        String ymd = yy + "-" + mm + "-" + dd;
        return ymd;
    }

     public static String convertDatetoddmmyyyy(String date) {
        String dd = date.substring(8, 10);
        String mm = date.substring(5, 7);
        String yy = date.substring(0, 4);
        String ymd = dd + "-" + mm + "-" + yy;
        return ymd;
    }
    public  boolean SaveDetails( com.oreilly.servlet.MultipartRequest Request) throws IOException, SQLException
    { Connection con = null;
        try {
          AdmissionYear Year=new AdmissionYear();
             con = new DBConnection().getConnection();
          //  con.rollback();
           // con.setAutoCommit(false);
          //  con.commit();
            PreparedStatement QueryPersonal = con.prepareStatement("update StudentPersonal set StudentName=?,Gender=?,Dob=?,Nationality=?,ReligionId=?,CastId=?,EmployementStatus=?, Guardian=? ,Relationship_Guardian=? where StudentId=?");
             QueryPersonal.setString(1,Request.getParameter("Name").trim() );
            QueryPersonal.setString(2, Request.getParameter("Sex").trim());
            QueryPersonal.setString(3,convertDatetoyyyymmdd(Request.getParameter("DOB").trim()));
            QueryPersonal.setString(4, Request.getParameter("Nationality").trim());
            QueryPersonal.setInt(5, Integer.parseInt(Request.getParameter("Religion").trim()));
            QueryPersonal.setInt(6,Integer.parseInt(Request.getParameter("Caste").trim()));
            QueryPersonal.setInt(7,Integer.parseInt(Request.getParameter("Employed").trim()));
            QueryPersonal.setString(8,Request.getParameter("Guardian"));
            QueryPersonal.setString(9, Request.getParameter("Relation"));
            //QueryPersonal.setInt(10, Year.getCurrentAdmissionYear());
            QueryPersonal.setInt(10, StudentId);
            QueryPersonal.executeUpdate();
            
            QueryPersonal=con.prepareStatement("update StudentAddress set PermenentHouseNo=?,PermenentPincode=?,PermenentCity=?,PermenentPlace=?,PermenentState=?,CommunicationHouseNo=?,CommunicationPincode=?,CommunicationCity=?,CommunicationPlace=?,CommunicationState=?,PhoneNo=?,EmailId=? ,SameAddress=? where StudentId=?");
            QueryPersonal.setString(1, Request.getParameter("HouseName").trim());
            QueryPersonal.setInt(2,Integer.parseInt(Request.getParameter("Pincode").trim()));
            QueryPersonal.setString(3,Request.getParameter("City").trim());
            QueryPersonal.setString(4,Request.getParameter("Place").trim());
            QueryPersonal.setString(5,Request.getParameter("State").trim());
            QueryPersonal.setString(6, Request.getParameter("P_HouseName").trim());
            QueryPersonal.setString(7,Request.getParameter("P_Pincode").trim());
            QueryPersonal.setString(8,Request.getParameter("P_City").trim());
            QueryPersonal.setString(9,Request.getParameter("P_Place").trim());
            QueryPersonal.setString(10,Request.getParameter("P_State").trim());
            QueryPersonal.setLong(11, Long.parseLong(Request.getParameter("PhoneNo").trim()));
            QueryPersonal.setString(12,Request.getParameter("EmailID").trim());
            if(Request.getParameter("chk_same")!=null)
                QueryPersonal.setInt(13, 1);
            else
                QueryPersonal.setInt(13,0);

            QueryPersonal.setInt(14, StudentId);
            QueryPersonal.executeUpdate();
          //  con.commit();
                  
           con.close();
            return true;
        } catch (SQLException ex) {
            if(con!=null)
            //con.rollback();
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public Hashtable getPersonalDetails(int StudentId,HttpServletResponse res) throws IOException
    {
        this.res=res;
        this.StudentId=StudentId;
        if(FillPersonalDetails())
        {
         
        return Personal;
        }
         
        return null;
    }
private  boolean FillPersonalDetails() throws IOException
{
        try {
            Connection con = new DBConnection().getConnection();
            String str="select StudentName ,PRN,Gender ,Dob,Nationality,ReligionId,CommunicationState,PermenentState,CastId,EmployementStatus,PhoneNo,EmailId,PermenentHouseNo,PermenentPlace,PermenentCity,PermenentPincode,SameAddress,CommunicationHouseNo,CommunicationPlace,CommunicationCity,CommunicationPincode,Image,Guardian,Relationship_Guardian,AttendingYear from StudentPersonal,StudentAddress where StudentAddress.StudentId=? and StudentPersonal.StudentId=?";
            PreparedStatement query = con.prepareStatement(str);
            query.setInt(1, StudentId);
            query.setInt(2, StudentId);
            ResultSet data = query.executeQuery();
              while (data.next()) {
                Personal.put("StudentId", new Integer(StudentId).toString());
                Personal.put("Name", IfNull(data.getString("StudentName")));
                Personal.put("Sex", IfNull(data.getString("Gender")));
                 String Dob=data.getString("Dob");
                if(Dob!=null)
                     Dob=convertDatetoddmmyyyy(Dob);
                Personal.put("DOB", IfNull(Dob));
                Personal.put("Nationality",IfNull(data.getString("Nationality"))); //ReligionId
                Personal.put("Religion", IfNull(data.getString("ReligionId")));
                Personal.put("Caste", IfNull(data.getString("CastId")));
                Personal.put("Employed", IfNull(data.getString("EmployementStatus")));
                Personal.put("PhoneNo", IfNull(data.getString("PhoneNo")));
                Personal.put("EmailID", IfNull(data.getString("EmailId")));
                Personal.put("HouseName",IfNull(data.getString("PermenentHouseNo")));
                Personal.put("Place", IfNull(data.getString("PermenentPlace")));
                Personal.put("City", IfNull(data.getString("PermenentCity")));
                Personal.put("Pincode", IfNull(data.getString("PermenentPincode")));
                Personal.put("State",IfNull(data.getString("PermenentState")));
                Personal.put("CState",IfNull(data.getString("CommunicationState")));
                Personal.put("Same", IfNull(data.getString("SameAddress")));
                Personal.put("CHouseName", IfNull(data.getString("CommunicationHouseNo")));
                Personal.put("CPlace", IfNull(data.getString("CommunicationPlace")));
                Personal.put("CCity", IfNull(data.getString("CommunicationCity")));
                Personal.put("CPincode", IfNull(data.getString("CommunicationPincode")));
                Personal.put("Image",IfNull(data.getString("Image"))) ;
                Personal.put("Guardian",IfNull(data.getString("Guardian")));
                Personal.put("Relation",IfNull(data.getString("Relationship_Guardian")));
                Personal.put("AttendingYear", IfNull(data.getString("AttendingYear")));
                Personal.put("PRN", IfNull(data.getString("PRN")));
            }
             con.close();
            return true;
        } catch (SQLException ex) {
         
           return  false;
        }
}
public  boolean getReligions()
{
    
        try {
            Connection con = new DBConnection().getConnection();
            Statement query = con.createStatement();
            ResultSet Records = query.executeQuery("SELECT ReligionId ,ReligionName FROM ReligionMaster");
            while (Records.next()) {
                ReligionId.add(Records.getString(1));
                ReligionCode.add(Records.getString(2));
            }
            con.close();
            return  true;
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }
}

public  String getReligion(String ReligionId)
{
        try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select ReligionName from ReligionMaster where ReligionId=?");
            query.setInt(1, Integer.parseInt(ReligionId));
            ResultSet Data = query.executeQuery();
            Data.next();
            String Reg=Data.getString(1);
            con.close();
            return Reg;

        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
           return null;
        }
}

public  String getCaste(String CasteId)
{
        try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select CasteName from CasteMaster where CasteId=?");
            query.setInt(1,Integer.parseInt(CasteId));
            ResultSet Rs=query.executeQuery();
            Rs.next();
            String Caste=Rs.getString("CasteName");
            con.close();
            return Caste;
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
           return null;
        }
}


public String getReligionId(int index)
{
    return ReligionId.get(index);
}
public String getReligionCode(int index)
{
     return ReligionCode.get(index);
}
public int getSize()
{
     return  ReligionId.size();
}
private String IfNull(String value )
{
      if(value==null)
           return " ";
      return value;
}
public boolean  getCastes(String  Religion)
{
        try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement query = con.prepareStatement("select CasteId,CasteName from CasteMaster where CasteId in (select CasteId from ReligionCasteMap where ReligionId=?)");
            query.setInt(1, Integer.parseInt(Religion));
            ResultSet Data = query.executeQuery();
            while (Data.next()) {
                CasteId.add(Data.getString(1));
                CasteCode.add(Data.getString(2));


            }
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }

}
public String getCasteId(int index)
{
     return CasteId.get(index);
}
public String getCasteCode(int index)
{
     return CasteCode.get(index);
}
public int getCasteCount()
{
    return CasteId.size();
}
public static  boolean UpdateImage(int StudentId,String ImageName,String path)
{
        try {

            Connection con = new DBConnection().getConnection();
            PreparedStatement ps=con.prepareStatement("SELECT Image FROM `StudentPersonal` where StudentID=? ");
            ps.setInt(1, StudentId);
            ResultSet rs=ps.executeQuery();
            String preImage=null;
            while (rs.next()) {
                preImage=rs.getString(1);
            }
            PreparedStatement Query = con.prepareStatement("update  StudentPersonal set Image=?  where StudentId=?");
            Query.setString(1, ImageName);
            Query.setInt(2, StudentId);
            Query.executeUpdate();
            con.close();
            if(preImage!=null)
             {
                 File file=new File(path+"images/"+preImage);
                 file.delete();
             }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }

}
public static  boolean UpdateImageforCentre(int StudentId,String ImageName)
{
        try {
            Connection con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("update  StudentPersonal set Image=?  where StudentId=?");
            Query.setString(1, ImageName);
            Query.setInt(2, StudentId);
            Query.executeUpdate();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }

}
}
