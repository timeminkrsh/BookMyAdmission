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

public class ProfileActivity extends AppCompatActivity {
    Button btn_update;
    EditText edt_name,edt_phno;
    EditText edt_address,edt_emailaddress;
    String name,email,address,mobile;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn_update = findViewById(R.id.btn_update);
        edt_name = findViewById(R.id.edt_name);
        edt_phno = findViewById(R.id.edt_phno);
        edt_address = findViewById(R.id.edt_address);
        edt_emailaddress = findViewById(R.id.edt_emailaddress);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Updating.....");

        name = Bsession.getInstance().getUser_name(ProfileActivity.this);
        edt_name.setText(name);
        mobile = Bsession.getInstance().getUser_mobile(ProfileActivity.this);
        edt_phno.setText(mobile);
        address = Bsession.getInstance().getUser_address(ProfileActivity.this);
        edt_address.setText(address);
        email =Bsession.getInstance().getUser_email(ProfileActivity.this);
        edt_emailaddress.setText(email);

        btn_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btn_update.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                if (name.isEmpty()) {
                    edt_name.setError("*Enter your name");
                    edt_name.requestFocus();
                } else if (email.isEmpty()) {
                    edt_emailaddress.setError("*Enter your email");
                    edt_emailaddress.requestFocus();
                } else if (address.isEmpty()) {
                    edt_address.setError("*Enter your address");
                    edt_address.requestFocus();
                } else if (name == null || name == "" || email == ""|| email == null || address == null || address == "" ) {
                    Toast.makeText(ProfileActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setUpdate();
                }
            }
        });

        toolbar();
    }

    public void setUpdate() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?customer_id=" + Bsession.getInstance().getUser_id(this);
        String para_str2 = "&user_name=" + name ;
        String para_str3 = "&user_email=" + email;
        String para_str4 = "&user_address=" + address;

        progressDialog.show();
        String baseUrl = ProductConfig.profile + para_str+para_str2+para_str3+para_str4;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Bsession.getInstance().initialize(ProfileActivity.this,
                                jsonResponse.getString("user_id"),
                                jsonResponse.getString("user_name"),
                                jsonResponse.getString("user_mobile"),
                                jsonResponse.getString("user_address"),
                                jsonResponse.getString("user_email"),
                                Bsession.getInstance().getParent_name(ProfileActivity.this),
                                Bsession.getInstance().getuniversity_id(ProfileActivity.this),
                                Bsession.getInstance().getcourse_id(ProfileActivity.this),
                                Bsession.getInstance().getdistrict_id(ProfileActivity.this),
                                "", "","","","","",
                                Bsession.getInstance().getMark_12th(ProfileActivity.this));
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Profile Update Success", Toast.LENGTH_LONG).show();
                        System.out.println("Response=="+response);
                    } else {
                        Toast.makeText(getApplicationContext(), "Profile Update Failed", Toast.LENGTH_LONG).show();
                    }

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
        getSupportActionBar().setTitle((CharSequence) "My Profile ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}