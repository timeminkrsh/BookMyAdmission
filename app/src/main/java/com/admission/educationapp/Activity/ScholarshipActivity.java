package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScholarshipActivity extends AppCompatActivity {
    EditText edt_name,edt_phno,edt_emailaddress,edt_scheme,edt_ifsc;
    EditText edt_schtype,edt_schgrade,edt_schcategory,edt_bank;
    Button btn_schapply;
    ProgressDialog progressDialog;
    String mobile,scheme;
    String name,schtype;
    String email,schgrade;
    String ifsc,bank,schcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        edt_scheme = findViewById(R.id.edt_scheme);
        btn_schapply = findViewById(R.id.btn_schapply);
        edt_name = findViewById(R.id.edt_name);
        edt_phno = findViewById(R.id.edt_phno);
        edt_emailaddress = findViewById(R.id.edt_emailaddress);
        edt_schtype = findViewById(R.id.edt_schtype);
        edt_schgrade = findViewById(R.id.edt_schgrade);
        edt_schcategory = findViewById(R.id.edt_schcategory);
        edt_ifsc = findViewById(R.id.edt_ifsc);
        edt_bank = findViewById(R.id.edt_bank);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Registering.....");

        name = Bsession.getInstance().getUser_name(ScholarshipActivity.this);
        edt_name.setText(name);
        mobile = Bsession.getInstance().getUser_mobile(ScholarshipActivity.this);
        edt_phno.setText(mobile);
        email = Bsession.getInstance().getUser_email(ScholarshipActivity.this);
        edt_emailaddress.setText(email);

        btn_schapply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btn_schapply.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                ScholarshipActivity registerActivity = ScholarshipActivity.this;
                registerActivity.name = registerActivity.edt_name.getText().toString().trim();
                ScholarshipActivity registerActivity2 = ScholarshipActivity.this;
                registerActivity2.mobile = registerActivity2.edt_phno.getText().toString().trim();
                ScholarshipActivity registerActivity3 = ScholarshipActivity.this;
                registerActivity3.scheme = registerActivity3.edt_scheme.getText().toString();
                ScholarshipActivity registerActivity4 = ScholarshipActivity.this;
                registerActivity4.email = registerActivity4.edt_emailaddress.getText().toString();
                ScholarshipActivity registerActivity5 = ScholarshipActivity.this;
                registerActivity5.schtype = registerActivity5.edt_schtype.getText().toString();
                ScholarshipActivity registerActivity6 = ScholarshipActivity.this;
                registerActivity6.schgrade = registerActivity6.edt_schgrade.getText().toString();
                ScholarshipActivity registerActivity7 = ScholarshipActivity.this;
                registerActivity7.schcategory = registerActivity7.edt_schcategory.getText().toString();
                ScholarshipActivity registerActivity8 = ScholarshipActivity.this;
                registerActivity8.ifsc = registerActivity8.edt_ifsc.getText().toString();
                ScholarshipActivity registerActivity9 = ScholarshipActivity.this;
                registerActivity9.bank = registerActivity9.edt_bank.getText().toString();
                if (name.isEmpty()) {
                    edt_name.setError("*Enter your name");
                    edt_name.requestFocus();
                } else if (mobile.isEmpty()) {
                    edt_phno.setError("*Enter your mobile number");
                    edt_phno.requestFocus();
                } else if (mobile == null ||mobile.length() < 6 || mobile.length() > 13) {
                    edt_phno.setError("*Enter Valid Mobile Number");
                    edt_phno.requestFocus();
                } else if (scheme.isEmpty()) {
                    edt_scheme.setError("*Enter your address");
                    edt_scheme.requestFocus();
                }else if (email.isEmpty()) {
                    edt_emailaddress.setError("*Enter your email");
                    edt_emailaddress.requestFocus();
                }else if (schtype.isEmpty()) {
                    edt_schtype.setError("*Enter your scholarship type");
                    edt_schtype.requestFocus();
                }else if (schcategory.isEmpty()) {
                    edt_schcategory.setError("*Enter your scholarship type");
                    edt_schcategory.requestFocus();
                }else if (schgrade.isEmpty()) {
                    edt_schgrade.setError("*Enter your scholarship type");
                    edt_schgrade.requestFocus();
                }else if (ifsc.isEmpty()) {
                    edt_ifsc.setError("*Enter your scholarship type");
                    edt_ifsc.requestFocus();
                }else if (bank.isEmpty()) {
                    edt_bank.setError("*Enter your scholarship type");
                    edt_bank.requestFocus();
                } else if (name == null || name == "" || email == ""|| email == null || mobile == null || mobile == "" ||
                        scheme == null || scheme == "" || bank == null || bank == ""|| ifsc == null || ifsc == ""||
                        schcategory == null || schcategory == ""|| schtype == null || schtype == ""|| schgrade == null || schgrade == "") {
                    Toast.makeText(ScholarshipActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setScholarship();
                }
            }
        });

        toolbar();
    }

    public void setScholarship() {
        final Map<String, String> params = new HashMap<>();
        String para1="?customer_id="+Bsession.getInstance().getUser_id(ScholarshipActivity.this);
        String para2="&name="+name;
        String para4="&mobile="+mobile;
        String para3="&email="+email;
        String para5="&scheme_name="+scheme;
        String para6="&scholarship_type="+schtype;
        String para7="&scholarship_grade="+schgrade;
        String para8="&category="+schcategory;
        String para9="&bank_name="+bank;
        String para10="&bank_ifsc_code="+ifsc;
        progressDialog.show();

        String baseUrl = ProductConfig.scholarship+para1+para2+para3+para4+para5+para6+para7+para8+para9+para10;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getApplicationContext(), "Scholarship applied success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ScholarshipActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Scholarship applied failed", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("Response=="+response);
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
            public Map<String, String> getParams() {
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
    }

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
        getSupportActionBar().setTitle((CharSequence) "Scholarship ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScholarshipActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}