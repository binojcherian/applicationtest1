/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;



/**
 *
 * @author HP
 */
public class SubjectBranch {
int subjectBranchId,subBranchId;
Branch branch;
SubjectExam subject;
boolean isOptional;
boolean isMain;
int SemOrYear;
String paperNo;
String paperType;

    public String getPaperNo() {
        return paperNo;
    }

    public void setPaperNo(String paperNo) {
        this.paperNo = paperNo;
    }

    public int getSemOrYear() {
        return SemOrYear;
    }

    public void setSemOrYear(int SemOrYear) {
        this.SemOrYear = SemOrYear;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isIsMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public boolean isIsOptional() {
        return isOptional;
    }

    public void setIsOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public SubjectExam getSubject() {
        return subject;
    }

    public void setSubject(SubjectExam subject) {
        this.subject = subject;
    }

    public int getSubjectBranchId() {
        return subjectBranchId;
    }

    public void setSubjectBranchId(int subjectBranchId) {
        this.subjectBranchId = subjectBranchId;
    }

    public int getSubBranchId() {
        return subBranchId;
    }

    public void setSubBranchId(int subBranchId) {
        this.subBranchId = subBranchId;
    }
      public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

}
