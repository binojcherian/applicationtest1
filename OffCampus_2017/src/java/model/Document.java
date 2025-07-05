

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Document
{

    ArrayList<String> DocId =new ArrayList<String>();
    ArrayList<String> DocName=new ArrayList<String>();
    ArrayList<String> DocDesc=new ArrayList<String>();
    ArrayList<String> DocPath=new ArrayList<String>();
    Hashtable<String ,DocData> Table=new Hashtable<String, DocData>();
    Messages msg=new Messages();
    public boolean fetchData(String StudentId) throws SQLException
    {  Connection con=null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT * FROM StudentDocumentMaster where StudentId=? order by DocumentId");
            int SId = Integer.parseInt(StudentId);
            Query.setInt(1, SId);
            ResultSet Records = Query.executeQuery();
            while (Records.next()) {
                DocId.add(Records.getString(2));

       Table.put(Records.getString(2),new DocData(Records.getString(3),Records.getString(4),Records.getString(5)));
                DocName.add(Records.getString(3));
                DocPath.add(Records.getString(4));
                DocDesc.add(Records.getString(5));
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();

            return false;
        }
       finally
           {
               if(con!=null)
               {
                   con.close();
               }
           }
    }
 public boolean IsExistData(int StudentId) throws SQLException
    {  Connection con=null;
        try {
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT * FROM StudentDocumentMaster where StudentId=?");

            Query.setInt(1, StudentId);
            Query.executeQuery();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();

            return false;
        }
       finally
           {
               if(con!=null)
               {
                   con.close();
               }
           }
    }

    public String DocDesc(String index)
    {
        String str;
          try{
                str  =Table.get(index).DocDesc;
          }catch(NullPointerException i)
          {
              return null;
          }
          return str;
    }
    public  boolean SaveToDB(int StudentID,String DocType,String Name,String path) throws SQLException
    {       Connection con = null;
        try {
            String DocName=null;
            DocType=DocType.trim();
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("insert into StudentDocumentMaster values(?,?,?,?,?)");
            if(DocType.equals("1"))
            {
                DocName="SSLC Certificate";
            }else if(DocType.equals("2"))
            {
                DocName="Pre-Degree/Higher Secondary Certificate";
            }
            else if(DocType.equals("3"))
            {
                DocName="Transfer Certificate ";
            }
            else if(DocType.equals("4"))
            {
                if(Name.equals("D"))
                DocName="Degree Certificate";
                if(Name.equals("P"))
                DocName="Provisional Certificate";
                if(Name.equals("F"))
                DocName="Final Mark List";
                SaveStudentDocumentMessage(StudentID,Name);
            }
            else if(DocType.equals("5"))
            {
                DocName="Migration Certificate ";
            }
            else if(DocType.equals("6"))
            {
                DocName="Community Certificate";
            }
            Query.setInt(1, StudentID);
            Query.setInt(2, Integer.parseInt(DocType.trim()));
            Query.setString(3, Name.trim());
            Query.setString(4, path.trim());
            Query.setString(5, DocName);
            Query.executeUpdate();
            con.close();

            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
           return false;
       }
    }


    public  boolean SaveToDBSelectedDocs(int StudentID,ArrayList<String> Doc,String Name) throws SQLException
    {       Connection con = null;
        try {
            String DocName=null;
            String DocType="0";
            String DName="";
            con = new DBConnection().getConnection();
            if(IsExistData(StudentID)){
                 PreparedStatement Query = con.prepareStatement("delete from  StudentDocumentMaster where StudentId = ?");
                Query.setInt(1, StudentID);
                Query.executeUpdate();
             }
            PreparedStatement Query = con.prepareStatement("insert into StudentDocumentMaster values(?,?,?,?,?)");
            int size=Doc.size(),i=0;
            while(i<size)
            {
                DocType=Doc.get(i);
                if(DocType.equals("1"))
                {
                    DocName="SSLC Certificate";
                }else if(DocType.equals("2"))
                {
                    DocName="Pre-Degree/Higher Secondary Certificate";
                }
                else if(DocType.equals("3"))
                {
                    DocName="Transfer Certificate ";
                }
                else if(DocType.equals("4"))
                {
                    if(Name.equals("D"))
                    DocName="Degree Certificate";
                    if(Name.equals("P"))
                    DocName="Provisional Certificate";
                    if(Name.equals("F"))
                    DocName="Final Mark List";
                    DName=Name;
                    SaveStudentDocumentMessage(StudentID,DName);
                }
                else if(DocType.equals("5"))
                {
                    DocName="Migration Certificate ";
                }
                else if(DocType.equals("6"))
                {
                    DocName="Community Certificate";
                }
                Query.setInt(1, StudentID);
                Query.setInt(2, Integer.parseInt(DocType.trim()));
                Query.setString(3, DName);
                Query.setString(4, "");
                Query.setString(5, DocName);
                Query.executeUpdate();
                i++;
            }
            con.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
           return false;
       }
    }
     public void SaveStudentDocumentMessage(int StudentId,String name) throws SQLException
    {
        boolean flag=false;
        String message=null;
        if(name.equals("P")||name.equals("F"))
        {
            message="* "+msg.getMessageForStudentDocument();
        }
        else if(name.equals("D"))
        {
            flag=true;
        }
        Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st= con.prepareStatement("select Message from StudentDocumentMessage where StudentId=?");
            st.setInt(1, StudentId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {

                st=con.prepareStatement("delete from StudentDocumentMessage where StudentId=?");
                st.setInt(1, StudentId);
                st.execute();
            }
            if(flag==false)
            {
                st=con.prepareStatement("insert into StudentDocumentMessage(StudentId,Message) values(?,?)");
                st.setInt(1, StudentId);
                st.setString(2, message);
                st.execute();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);

        }
        finally
        {
            if(con!=null)
            {
                con.close();
            }
        }
    }
    public boolean Edit(int StudentID,String DocType,String Name,String path) throws SQLException
    {
        String Desc=Name;
           Connection con = null;
        try {


           if(DocType.equals("1"))
            {
                Name="SSLC Certificate";
            }else if(DocType.equals("2"))
            {
                Name="Pre-Degree/Higher Secondary Certificate";
            }
            else if(DocType.equals("3"))
            {
                Name="Transfer Certificate ";
            }
            else if(DocType.equals("4"))
            {
                SaveStudentDocumentMessage(StudentID,Name);
                if(Name.equals("D"))
                Name="Degree Certificate";
                if(Name.equals("P"))
                Name="Provisional Certificate";
                if(Name.equals("F"))
                Name="Final Mark List";
            }
            else if(DocType.equals("5"))
            {
                Name="Migration Certificate ";
            }
            else if(DocType.equals("6"))
            {
                Name="Community Certificate";
            }
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("update StudentDocumentMaster set DocumentName=? ,DocumentPath=?, DocumentDescription=? where StudentId=? and DocumentId=? ");
            Query.setString(1,Desc);
            Query.setString(2, path);
            Query.setInt(4, StudentID);
            Query.setInt(5, Integer.parseInt(DocType.trim()));
            Query.setString(3,Name);

            Query.executeUpdate();
            con.close();

            return true;
         } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return false;
        }
    }
     public ArrayList getDocuments(int StudentId) throws SQLException
    {
         Connection con=null;
         ArrayList documents = new ArrayList();
        try {
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement("SELECT DocumentDescription FROM StudentDocumentMaster where StudentId=? order by DocumentId");

            Query.setInt(1,StudentId);
            ResultSet Records = Query.executeQuery();
            while (Records.next()) {
                documents.add(Records.getString("DocumentDescription"));
            }
            return documents;
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return null;
        }
          finally
           {
               if(con!=null)
               {
                   con.close();
               }
           }
    }

    public String getNameOfDoc(String index)
    {  String str;
          try{
                str  =Table.get(index).DocName;
          }catch(NullPointerException i)
          {
              return null;
          }
          return str;
    }
    public String getDocPathOfDoc(String index)
    {
        String str;
          try{
                str  =Table.get(index).DocPath;
          }catch(NullPointerException i)
          {
              return null;
          }
          return str;
    }
    public String getDocId(int index)
    {
        return DocId.get(1);
    }
    public String getDocName(int index)
    {

         return DocName.get(index);
    }
    public String getDocpath(int index)
    {
        return DocPath.get(index);
    }
   public String DocumentValidation(int StudentId) throws SQLException
   {
       Connection con=null;
         ArrayList documents = new ArrayList();
        try {
            con = new DBConnection().getConnection();
            PreparedStatement Query = con.prepareStatement(" select count(*) from StudentDocumentMaster where studentid=? and (Documentid=1 or Documentid=2 or Documentid=3 )");
            Query.setInt(1,StudentId);

            ResultSet Records = Query.executeQuery();

            if(Records.next()&&Records.getInt(1)<3)
            return "All the Documents marked with a red asterisk * are  mandatory";
            else
            {
                Query=con.prepareStatement("select BasicQualification from CourseMaster where CourseId=(select Courseid from BranchMaster where BranchId = (select  Branchid FROM StudentPersonal where StudentId=? ))");
                Query.setInt(1,StudentId);
                 Records = Query.executeQuery();

                if( Records.next()&&Records.getString(1).equals("Degree"))
                {
                     Query=con.prepareStatement("SELECT count(*) FROM StudentDocumentMaster  where Documentid=4 and Studentid=?");
                     Query.setInt(1, StudentId);
                     Records=Query.executeQuery();
                     if(Records.next()&&Records.getInt(1)==0)
                     {
                          return "Degree Certificate is Requried";
                     }
                     else
                     {
                         return null;
                     }
                }
                 return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
            return "Data can not be fetched contact MG university for technical support";
        }
          finally
           {
               if(con!=null)
               {
                   con.close();
               }
           }

   }
   public String getStudentDocumentMessage(int StudentId) throws SQLException
    {
       Connection con=null;
        try
        {
            con = new DBConnection().getConnection();
            PreparedStatement st = con.prepareStatement("Select Message from StudentDocumentMessage where StudentId=?");
            st.setInt(1, StudentId);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
       {
           if(con!=null)
           {
               con.close();
           }
       }
    }

}

class DocData
{
    String DocName;
    String DocPath;
    String DocDesc;
    DocData(String DocName,String DocPat,String DocDesc)
    {
        this.DocName=DocName;
        this.DocPath=DocPat;
        this.DocDesc=DocDesc;
    }
}