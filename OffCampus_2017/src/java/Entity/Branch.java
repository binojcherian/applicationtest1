/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

/**
 *
 * @author HP
 */
public class Branch {
int branchId;
String branchName;
String branchDisplay;

    public String getBranchDisplay() {
        return branchDisplay;
    }

    public void setBranchDisplay(String branchDisplay) {
        this.branchDisplay = branchDisplay;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
