package zmq.com.model;

import java.io.Serializable;

/**

 * Created by zmq162 on 2/2/16.
 */

public class PatientInfo implements Serializable{
// By FZ
    private int rowID;
    private String fileName;
    private String serverDate;
    private String uploadStatus;

    private String saveStatus;
    private String UserId;
    private String Password;
    ////////////////////////////

    private String pId;
    private String pName;
    private String pAsha;
    private String pRegime;
    private String pDoseMode;
    private String DoseDate;

//    private String pState;
//    private String pCity;
//    private String pVillage;
//
//    private String pAddress;
//    private String pSex;
//    private String pAge;
//    private String pType;
//    private String pRegDate;
//    private String pCompliance;


    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(String saveStatus) {
        this.saveStatus = saveStatus;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }



    public String getpAsha() {
        return pAsha;
    }

    public void setpAsha(String pAsha) {
        this.pAsha = pAsha;
    }

    public String getpDoseMode() {
        return pDoseMode;
    }

    public void setpDoseMode(String pDoseMode) {
        this.pDoseMode = pDoseMode;
    }

    public String getpRegime() {
        return pRegime;
    }

    public void setpRegime(String pRegime) {
        this.pRegime = pRegime;
    }

    public String getDoseDate() {
        return DoseDate;
    }

    public void setDoseDate(String doseDate) {
        this.DoseDate = doseDate;
    }
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

   /* public String getpState() {
        return pState;
    }

    public void setpState(String pState) {
        this.pState = pState;
    }

    public String getpCompliance() {
        return pCompliance;
    }

    public void setpCompliance(String pCompliance) {
        this.pCompliance = pCompliance;
    }

    public String getpCity() {
        return pCity;
    }

    public void setpCity(String pCity) {
        this.pCity = pCity;
    }

    public String getpVillage() {
        return pVillage;
    }

    public void setpVillage(String pVillage) {
        this.pVillage = pVillage;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getpSex() {
        return pSex;
    }

    public void setpSex(String pSex) {
        this.pSex = pSex;
    }

    public String getpAge() {
        return pAge;
    }

    public void setpAge(String pAge) {
        this.pAge = pAge;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getpRegDate() {
        return pRegDate;
    }

    public void setpRegDate(String pRegDate) {
        this.pRegDate = pRegDate;
    }*/


    @Override
    public String toString() {
        return pId +" "+ pName +" Savestatus "+saveStatus;
    }
}
