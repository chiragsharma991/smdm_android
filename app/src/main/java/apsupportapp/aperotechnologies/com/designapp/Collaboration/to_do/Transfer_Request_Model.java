package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do;

/**
 * Created by pamrutkar on 08/03/17.
 */

public class Transfer_Request_Model {
    String caseNo;
    String reqStoreCode;
    String reqStoreDesc;
    String productCode;

    public String getProdAttribute2() {
        return prodAttribute2;
    }

    public void setProdAttribute2(String prodAttribute2) {
        this.prodAttribute2 = prodAttribute2;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProdAttribute4() {
        return prodAttribute4;
    }

    public void setProdAttribute4(String prodAttribute4) {
        this.prodAttribute4 = prodAttribute4;
    }

    String prodAttribute2;
    String prodAttribute4;

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getReqStoreCode() {
        return reqStoreCode;
    }

    public void setReqStoreCode(String reqStoreCode) {
        this.reqStoreCode = reqStoreCode;
    }

    public String getReqStoreDesc() {
        return reqStoreDesc;
    }

    public void setReqStoreDesc(String reqStoreDesc) {
        this.reqStoreDesc = reqStoreDesc;
    }

    public double getStkQtyAvl() {
        return stkQtyAvl;
    }

    public void setStkQtyAvl(double stkQtyAvl) {
        this.stkQtyAvl = stkQtyAvl;
    }

    public double getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(double optionCount) {
        this.optionCount = optionCount;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public double getStkOnhandQtyRequested() {
        return stkOnhandQtyRequested;
    }

    public void setStkOnhandQtyRequested(double stkOnhandQtyRequested) {
        this.stkOnhandQtyRequested = stkOnhandQtyRequested;
    }

    double stkOnhandQtyRequested;
    double stkQtyAvl;
    double optionCount;
    String noOfDays;

 //Test commit - 10-3-17

        String level;

    public double getStkOnhandQty() {
        return stkOnhandQty;
    }

    public void setStkOnhandQty(double stkOnhandQty) {
        this.stkOnhandQty = stkOnhandQty;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getStkGitQty() {
        return stkGitQty;
    }

    public void setStkGitQty(double stkGitQty) {
        this.stkGitQty = stkGitQty;
    }

    double stkOnhandQty;
    double stkGitQty;

}
