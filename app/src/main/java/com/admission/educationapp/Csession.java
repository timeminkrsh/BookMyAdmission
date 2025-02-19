package com.admission.educationapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Csession {
    public Csession(){

    }
    public static Csession csession;

    public static Csession getInstance() {

        if (csession == null) {
            synchronized (Csession.class) {
                csession = new Csession();
            }
        }
        return csession;
    }

    private String uni_name;
    private String col_name;
    private String col_code;
    private String col_address;
    private String col_weburl;
    private String col_description;
    private String wallet_amount;
    private String ind_colid;


    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public void initialize(Context context,
                           String uni_name,
                           String col_name,
                           String col_code,
                           String col_address,
                           String col_weburl,
                           String col_description,
                           String wallet_amount,
                           String ind_colid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("uni_name", uni_name);
        editor.putString("col_name", col_name);
        editor.putString("col_code", col_code);
        editor.putString("col_address", col_address);
        editor.putString("col_weburl", col_weburl);
        editor.putString("col_description", col_description);
        editor.putString("wallet_amount", wallet_amount);
        editor.putString("ind_colid", ind_colid);

        editor.apply();

        this.uni_name = uni_name;
        this.col_name = col_name;
        this.col_code = col_code;
        this.col_address = col_address;
        this.col_weburl = col_weburl;
        this.col_description = col_description;
        this.wallet_amount = wallet_amount;
        this.ind_colid = ind_colid;

    }

    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.uni_name = null;
        this.col_name = null;
        this.col_code = null;
        this.col_address = null;
        this.col_weburl = null;
        this.col_description = null;
        this.wallet_amount = null;
        this.ind_colid = null;

    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String[] sharedValues = new String[8];

        sharedValues[0] = this.uni_name = prefs.getString("uni_name", "");
        sharedValues[1] = this.col_name = prefs.getString("col_name", "");
        sharedValues[2] = this.col_code = prefs.getString("col_code", "");
        sharedValues[3] = this.col_address = prefs.getString("col_address", "");
        sharedValues[4] = this.col_weburl = prefs.getString("col_weburl", "");
        sharedValues[5] = this.col_description = prefs.getString("col_description", "");
        sharedValues[6] = this.wallet_amount = prefs.getString("wallet_amount", "");
        sharedValues[7] = this.ind_colid = prefs.getString("ind_colid", "");

        return sharedValues;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public String getUni_name(Context context) {
        getSession(context);
        return uni_name;
    }

    public void setUni_name(String uni_name) {
        this.uni_name = uni_name;
    }

    public String getCol_name(Context context) {
        getSession(context);
        return col_name;
    }

    public void setCol_name(String col_name) {
        this.col_name = col_name;
    }

    public String getCol_weburl(Context context) {
        getSession(context);
        return col_weburl;
    }

    public void setCol_weburl(String col_weburl) {
        this.col_weburl = col_weburl;
    }

    public String getCol_code(Context context) {
        getSession(context);
        return col_code;
    }

    public void setCol_code(String col_code) {
        this.col_code = col_code;
    }

    public String getCol_address(Context context) {
        getSession(context);
        return col_address;
    }

    public void setCol_address(String col_address) {
        this.col_address = col_address;
    }

    public String getCol_description(Context context) {
        getSession(context);
        return col_description;
    }

    public void setCol_description(String col_description) {
        this.col_description = col_description;
    }

    public String getWallet_amount(Context context) {
        getSession(context);
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getInd_colid(Context context) {
        getSession(context);
        return ind_colid;
    }

    public void setInd_colid(String ind_colid) {
        this.ind_colid = ind_colid;
    }

}



