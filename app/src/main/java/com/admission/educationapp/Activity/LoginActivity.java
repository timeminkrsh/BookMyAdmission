package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class                                                                                                                                  LoginActivity extends AppCompatActivity {
    Button btn_login;
    EditText txt_mobile;
    String phone;
    String mobile;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        txt_mobile = findViewById(R.id.txt_mobile);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Logging in.....");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.phone = loginActivity.txt_mobile.getText().toString();
                if (phone.isEmpty()) {
                    txt_mobile.setError("*Enter The Mobile Number");
                    txt_mobile.requestFocus();
                }else if (phone == null ||phone.length() < 10 ) {
                    txt_mobile.setError("*Enter Valid Mobile Number");
                    txt_mobile.requestFocus();
                } else {
                    sendOTP();
                }
            }
        });
    }

    public void sendOTP() {
        String para1="?user_mobile="+txt_mobile.getText().toString().trim();
        String baseurl =ProductConfig.userlogin + para1;
        System.out.println("base====="+baseurl);
        final Map<String, String> params = new HashMap<>();
        progressDialog.show();
        Volley.newRequestQueue(this).add(new StringRequest(0,baseurl, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (!jsonResponse.has(FirebaseAnalytics.Param.SUCCESS) || !jsonResponse.getString(FirebaseAnalytics.Param.SUCCESS).equals("1")) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Bsession.getInstance().initialize(LoginActivity.this,"",
                            "",
                            jsonResponse.getString("user_mobile"),
                            "","", "","",
                            "", "", "","",
                            "","","","","");
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                    intent.putExtra("user_mobile", mobile);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                progressDialog.dismiss();
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() { return params; } });
    }

}