package com.admission.educationapp.Model;

public class TransactionModel {
    private String sno;
    private String direct_refer;
    private String team_refer;
    private String date;
    private String id;
    private String withdraw_amount;
    private String order_price;
    private String balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(String withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDirect_refer() {
        return direct_refer;
    }

    public void setDirect_refer(String direct_refer) {
        this.direct_refer = direct_refer;
    }

    public String getTeam_refer() {
        return team_refer;
    }

    public void setTeam_refer(String team_refer) {
        this.team_refer = team_refer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
