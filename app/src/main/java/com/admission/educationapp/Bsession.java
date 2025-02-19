package com.admission.educationapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.admission.educationapp.Activity.HomeActivity;


public class Bsession {
    public Bsession() {

    }

    public static Bsession   bsession;

    public static Bsession getInstance() {

        if (bsession == null) {
            synchronized (Bsession.class) {
                bsession = new Bsession();
            }
        }
        return bsession;
    }

    private static String TAG = Bsession.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;

    SharedPreferences prefs;

    private String dept_id;
    private String user_id;
    private String user_name;
    private String user_mobile;
    private String user_email;
    private String user_address;
    private String course_id;
    private String parent_name;
    private String university_id,district_id;
    private String college_id;
    private String fees_id;
    private String cq_count;
    private String mq_count;
    private String quota;
    private String mark_12th;

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public void initialize(Context context,
                           String user_id,
                           String user_name,
                           String user_mobile,
                           String user_address,
                           String user_email,
                           String parent_name,
                           String university_id,
                           String course_id,
                           String district_id,
                           String dept_id,
                           String fees_id,
                           String college_id,
                           String cq_count,
                           String mq_count,
                           String quota,
                           String mark_12th) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_name", user_name);
        editor.putString("user_mobile", user_mobile);
        editor.putString("user_address", user_address);
        editor.putString("district_id", district_id);
        editor.putString("parent_name", parent_name);
        editor.putString("user_email", user_email);
        editor.putString("university_id", university_id);
        editor.putString("course_id", course_id);
        editor.putString("dept_id", dept_id);
        editor.putString("college_id",college_id);
        editor.putString("fees_id",fees_id);
        editor.putString("cq_count",cq_count);
        editor.putString("mq_count",mq_count);
        editor.putString("quota",quota);
        editor.putString("mark_12th",mark_12th);

        editor.apply();

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_mobile = user_mobile;
        this.user_address = user_address;
        this.course_id = course_id;
        this.university_id = university_id;
        this.parent_name = parent_name;
        this.district_id=district_id;
        this.user_email=user_email;
        this.dept_id = dept_id;
        this.college_id = college_id;
        this.fees_id = fees_id;
        this.cq_count = cq_count;
        this.mq_count = mq_count;
        this.quota = quota;
        this.mark_12th = mark_12th;

    }

    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.user_id = null;
        this.user_name = null;
        this.user_mobile = null;
        this.user_address = null;
        this.user_email = null;
        this.course_id = null;
        this.university_id = null;
        this.parent_name = null;
        this.district_id = null;
        this.dept_id = null;
        this.college_id = null;
        this.fees_id = null;
        this.cq_count = null;
        this.mq_count = null;
        this.quota = null;
        this.mark_12th = null;

    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String[] sharedValues = new String[16];

        sharedValues[0] = this.user_id = prefs.getString("user_id", "");
        sharedValues[1] = this.user_name = prefs.getString("user_name", "");
        sharedValues[2] = this.user_mobile = prefs.getString("user_mobile", "");
        sharedValues[3] = this.user_address = prefs.getString("user_address", "");
        sharedValues[4] = this.user_email = prefs.getString("user_email", "");
        sharedValues[5] = this.district_id = prefs.getString("district_id", "");
        sharedValues[6] = this.university_id = prefs.getString("university_id", "");
        sharedValues[7] = this.parent_name = prefs.getString("parent_name", "");
        sharedValues[8] = this.course_id = prefs.getString("course_id", "");
        sharedValues[9] = this.dept_id = prefs.getString("dept_id", "");
        sharedValues[10] = this.college_id = prefs.getString("college_id", "");
        sharedValues[11] = this.fees_id = prefs.getString("fees_id", "");
        sharedValues[12] = this.cq_count = prefs.getString("cq_count", "");
        sharedValues[13] = this.mq_count = prefs.getString("mq_count", "");
        sharedValues[14] = this.quota = prefs.getString("quota", "");
        sharedValues[15] = this.mark_12th = prefs.getString("mark_12th", "");

        return sharedValues;
    }

    public String getdistrict_id(Context context) {
        getSession(context);
        return district_id;
    }

    public void setdistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public Boolean isAuthenticated(Context context) {

        if (this.dept_id == null || this.user_name == null || this.dept_id.isEmpty() || this.user_name.isEmpty() || this.dept_id.equals("NosessionId") || this.user_name.equals("Nouser_name")) {
            Intent intent = null;
            intent = new Intent(context.getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    public Boolean isApplicationExit(Context context) {

        getSession(context);

        System.out.println("====" + this.dept_id + "UN" + this.user_name);
        if (this.user_name == null || this.user_name.isEmpty() || this.user_name.equals("Nouser_name")) {
            return false;

        }
        System.out.println("====" + this.dept_id + "UN--true" + this.user_name);
        return true;
    }


    public String getDept_id(Context context) {
        getSession(context);
        return dept_id;
    }

    public void setDept_id(String sessionId) {
        this.dept_id = dept_id;
    }

    public String getUser_id(Context context) {
        getSession(context);
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name(Context context) {
        getSession(context);
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getParent_name(Context context) {
        getSession(context);
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getUser_mobile(Context context) {
        getSession(context);
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_address(Context context) {
        getSession(context);
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_email(Context context) {
        getSession(context);
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getcourse_id(Context context) {
        getSession(context);
        return course_id;
    }

    public void setcourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getFees_id(Context context) {
        getSession(context);
        return fees_id;
    }

    public void setFees_id(String fees_id) {
        this.fees_id = fees_id;
    }

    public String getuniversity_id(Context context) {
        getSession(context);
        return university_id;
    }

    public void setuniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getCollege_id(Context applicationContext) {
        return college_id;
    }

    public void setCollege_id(String type) {
        this.college_id = college_id;
    }

    public String getCq_count(Context context) {
        getSession(context);
        return cq_count;
    }

    public void setCq_count(String cq_count) {
        this.cq_count = cq_count;
    }

    public String getQuota(Context context) {
        getSession(context);
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getMq_count(Context context) {
        getSession(context);
        return mq_count;
    }

    public void setMq_count(String mq_count) {
        this.mq_count = mq_count;
    }
    public String getMark_12th(Context context) {
        getSession(context);
        return mark_12th;
    }

    public void setMark_12th(String mark_12th) {
        this.mark_12th = mark_12th;
    }
}



