/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

/**
 *
 * @author user
 */
public class SubjectExam {



int subjectId;
String subjectName;
int currentYearSem;
String paperType;




    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    public int getCurrentYearSem() {
        return currentYearSem;
    }

    public void setCurrentYearSem(int currentYearSem) {
        this.currentYearSem = currentYearSem;
    }
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

}
