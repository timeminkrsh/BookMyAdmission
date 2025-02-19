package com.admission.educationapp.Model;

public class FeesModel {
    private String id;
    private String quota;
    private String hostel_fees;
    private String room_fees;
    private String pmss;
    private String tution_fees;

    public String getTution_fees() {
        return tution_fees;
    }

    public void setTution_fees(String tution_fees) {
        this.tution_fees = tution_fees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getHostel_fees() {
        return hostel_fees;
    }

    public void setHostel_fees(String hostel_fees) {
        this.hostel_fees = hostel_fees;
    }

    public String getRoom_fees() {
        return room_fees;
    }

    public void setRoom_fees(String room_fees) {
        this.room_fees = room_fees;
    }

    public String getPmss() {
        return pmss;
    }

    public void setPmss(String pmss) {
        this.pmss = pmss;
    }
}
