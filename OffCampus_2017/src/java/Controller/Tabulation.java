/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.*;
import Entity.*;

public class Tabulation
{
    HttpServletRequest request;
    HttpServletResponse response;
    String ExamId="-1",BranchId="-1",Centre="-1",ExamError=null,BranchError=null,SemError=null,Sem="-1";
   
    Absentees absent=new Absentees();
    MarkEntry_old mark=new MarkEntry_old();
    TabulationRegister Tab=new TabulationRegister();


    public Tabulation(HttpServletRequest request,HttpServletResponse response) throws IOException
    {
        this.request=request;
        this.response=response;

        if(request.getParameter("Exam")!=null)
        {
            ExamId=request.getParameter("Exam");
        }
        if(request.getParameter("Course")!=null)
        {
            BranchId=request.getParameter("Course");
            request.getSession().setAttribute("BranchId", BranchId);
        }
        if(request.getParameter("Centre")!=null)
        {
            Centre=request.getParameter("Centre");
        }
        if(request.getParameter("YearSem")!=null)
        {
            Sem=request.getParameter("YearSem");
        }
        if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals("Submit"))
        {
            ExamError=getExamError();
            if(ExamError==null)
            {
                BranchError=getBranchError();
                if(BranchError==null)
                {
                    SemError=getSemError();
                    if(SemError==null)
                    {
                        
                            response.sendRedirect("CourseWiseTabulationRegister.jsp?Exam="+ExamId+"&Course="+BranchId+"&Centre="+Centre+"&YearSem="+Sem);
                        
                    }
                }
            }
        }
        
    }

    public ArrayList<Entity.CourseData> getBranchList() throws SQLException
    {
        return absent.getBranchList();
    }

    public ArrayList<ExamData> getExams() throws SQLException
    {
        return absent.getExams();
    }

    public String getExam()
    {
        return ExamId;
    }

    public String getSemError()
    {
        if(Sem.equals("-1"))
        {
            return "Select Semester/Year";
        }
        return null;
    }

    public String getBranch()
    {
    //    if(request.getSession().getAttribute("BranchId")!=null)
    //    return request.getSession().getAttribute("BranchId").toString();
    //   else
            return BranchId;
    }

    public String getSemYear()
    {
       /* if(!Sem.equals("-1"))
        return Sem.toString();
        else
            return "-1";*/
        return Sem;
    }

    public ArrayList<Centre> getAllCentresForCourse() throws SQLException
    {
         return Tab.getAllCentresForCourse(BranchId);
    }

    public String ExamError()
    {
        return ExamError;
    }

    public String BranchError()
    {
        return BranchError;
    }
    public String getExamError()
    {
        if(ExamId.equals("-1"))
        {
            return "Select Exam";
        }
        return null;
    }

    public String getBranchError()
    {
        if(BranchId.equals("-1"))
        {
            return "Select Course";
        }
        return null;
    }

    public String SemError()
    {
        return SemError;
    }
     public String getExamName() throws SQLException
     {
         return Tab.getExamName(ExamId);
     }

     public int getMaxYearOrSemForCourse() throws SQLException
    {
        return absent.getMaxYearOrSemForCourse(BranchId);
    }
     public ArrayList<Entity.StudentData> getStudentsOfCourse() throws SQLException
     {
         return Tab.getStudentsOfCourse(ExamId, BranchId, Centre,Sem.toString());
     }
     public ArrayList<Entity.StudentData> getStudentsOfCourse1() throws SQLException
     {
         return Tab.getStudentsOfCourse1(ExamId, BranchId, Centre,Sem.toString());
     }
     public ArrayList<Entity.StudentData> getStudentsOfCoursebcom2013() throws SQLException
     {
         return Tab.getStudentsOfCourse_Bcom2013(ExamId, BranchId, Centre,Sem.toString());
     }
     public ArrayList<Entity.StudentData> getStudentsOfCourse_BcomPartIII() throws SQLException
     {
        // return Tab.getStudentsOfCourse_BcomPartIII(ExamId, BranchId, Centre,Sem.toString());
         return Tab.getStudentsOfCourse_BcomPartIII(ExamId, BranchId, Centre,Sem.toString());
     }
     public ArrayList<Entity.StudentData> getStudentsOfCourse_BcomPartII() throws SQLException
     {
         //return Tab.getStudentsOfCourse_BcomPartIII(ExamId, BranchId, Centre,Sem.toString());
         return Tab.getStudentsOfCourse_BcomPartII(ExamId, BranchId, Centre,Sem.toString());
     }
     public String getStudentWithheldDetails(String StudentId) throws SQLException
     {
         return Tab.getStudentWithheldDetails(StudentId, ExamId);
     }
     public String YearOrSem() throws SQLException
     {
         return Tab.YearOrSem(BranchId);
     }

     public ArrayList<TabulationData> getStudentMarkDetails(String StudentId,Connection con)
     {
         return Tab.getStudentMarkDetails_old(StudentId,Sem, con);
     }
      public ArrayList<TabulationData> getStudentMarkDetails_LLM(String StudentId,Connection con)
     {
         return Tab.getStudentMarkDetails_LLM(StudentId,Sem, con);
     }
     public ArrayList<TabulationData> getStudentMarkDetails_BcomPartIII(String StudentId,Connection con)
     {
         return Tab.getStudentMarkDetails_BcomIII(StudentId);
         //return Tab.getStudentMarkDetails_BcomPartII(StudentId);
         //return Tab.getStudentTRDetails_BcomPartIII("3",2, con,"7","-1");
     }
     public ArrayList<TabulationData> getStudentMarkDetails_BcomPartII(String StudentId,Connection con)
     {
         return Tab.getStudentMarkDetails_BcomPartII(StudentId);
         // return Tab.getStudentTRDetails_BcomPartIII("3",2, con,"7","-1");
     }
     public ArrayList<TabulationData> getStudentMarkDetails_Bts(String StudentId,Connection con)
     {
         return Tab.getStudentMarkDetails_BTSPract(StudentId,Sem, con);
     }
      public ArrayList<TabulationData> getStudentMarkDetailsBcom(String StudentId)
     {
         return Tab.getStudentMarkDetails_Bcom(StudentId);
     }
      public ArrayList<TabulationData> getStudentMarkDetailsBcom2013(String StudentId)
     {
         return Tab.getStudentMarkDetails_Bcom2013(StudentId);
     }
      
 /*public ArrayList<TabulationData> getStudentMarkDetails(String StudentId,Connection con)
     {
         return Tab.getStudentMarkDetails(StudentId,ExamId,Sem, con);
     }*/
      /*public String getSemTotalForStudent(String StudentId,Connection con) throws SQLException
      {
          return Tab.getSemTotalForStudent(StudentId, ExamId, Sem, con);
      }*/
 public String getSemTotalForStudent(String StudentId,Connection con) throws SQLException
      {
          return Tab.getSemTotalForStudent_Old(StudentId,Sem, con);
      }

  public String getSemTotalForStudent1(String StudentId,Connection con,String sem) throws SQLException
      {
          return Tab.getSemTotalForStudent_Old(StudentId,sem, con);
      }
      public int getTotalForEnglish(String StudentId,Connection con)
      {
          return Tab.getTotalForEnglish(StudentId, ExamId, con);
      }
 public int getTotalForEnglishBcom(String StudentId)
      {
          return Tab.getTotalForEnglishBcom(StudentId, ExamId);
      }
 public int getSyllabusofSubject(String SubjectBranchId)
      {
          return Tab.getSyllabusofSubject(SubjectBranchId);
      }
       public boolean IsStudentPassedForEnglish(String StudentId,Connection con)
       {
           return Tab.IsStudentPassedForEnglish(StudentId, ExamId, con);
       }
         public boolean IsStudentPassedForEnglishBcom(String StudentId)
       {
           return Tab.IsStudentPassedForEnglishBcom(StudentId, ExamId);
       }
          public boolean IsStudentRalForEnglishBcom(String StudentId)
       {
           return Tab.IsStudentRalForEnglishBcom(StudentId, ExamId);
       }
         public int getTotalPassMarkForBLiSc(Connection con)
         {
            return Tab.getTotalPassMarkForBLiSc(BranchId, Sem, con);
         }
        public ArrayList<Subject> getSubjectsForBranch() throws SQLException
         {
             Subjects sub=new Subjects();
             return sub.getSubjectsForBranch(BranchId, Sem);
         }
        public ArrayList<TabulationData> getStudentTRDetails(Connection con)
        {
            return Tab.getStudentTRDetails(ExamId, Sem, con, BranchId,Centre);
        }
         public ArrayList<TabulationData> getStudentTRDetails_BcomPartIII(Connection con)
        {
            return Tab.getStudentTRDetails_BcomPartIII(ExamId, Sem, con, BranchId,Centre);
        }
        
        public ArrayList<model.StudentData> getStudents()
        {
            return Tab.getStudents();
        }
         //new Method added
       public boolean IsStudentPassedForSemester(int StudentId,Connection con,String Semester)
       {
            return Tab.IsStudentPassedForSemester(StudentId,Semester, BranchId, con);
            
            
       }
        public boolean IsBettermentStudent(int StudentId,Connection con)
       {
            return Tab.IsBettermentStudent(StudentId,ExamId, con);
       }
         public boolean IsAditionalDegreeStudent(int StudentId,Connection con)
       {
            return Tab.IsAditionalDegreeStudent(StudentId,ExamId, con);
       }
      
        public boolean IsStudentPassedForSemesterLLM(int StudentId,Connection con,String Semester)
       {
            return Tab.IsStudentPassedForSemesterLLM_Semester(StudentId, Semester, BranchId, con);
       }
       //new method added
       public String getSemTotal(int StudentId,Connection con,int Semester) throws SQLException
        {
            return Tab.getSemTotal(StudentId, Semester, con);
        }
       public boolean IsStudentPassedForBcomPartIII(int StudentId,Connection con,int Semester)
       {
            return Tab.IsStudentPassedForBcomPartIII(StudentId, Semester, BranchId, con);
       }
       //new method added
       public String getSemTotalofBcomPartIII(int StudentId,Connection con,int Semester) throws SQLException
        {
            return Tab.getSemTotalforBcomPartIII(StudentId, Semester, con);
        }
       public ArrayList<Entity.StudentData> getStudentsOfCourseConsolidate() throws SQLException
     {
         return Tab.getStudentsOfCourseConsolidate(ExamId, BranchId, Centre,Sem.toString());
     }
         public ArrayList<Entity.StudentData> getStudentsOfCourseConsolidateLLM() throws SQLException
     {
         return Tab.getStudentsOfCourseConsolidateLLM(ExamId, BranchId, Centre,Sem.toString());
     }
       public String getSemmaxExam(int StudentId,Connection con,int Semester) throws SQLException
        {
            return Tab.getSemmaxExam(StudentId, Semester, con);
        }
}
