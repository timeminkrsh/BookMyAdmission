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

public class SupportActivity extends AppCompatActivity {
    EditText et_name,et_email,et_message;
    Button spt_submit;
    ProgressDialog progressDialog;
    String name;
    String email;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_message = findViewById(R.id.et_message);
        spt_submit = findViewById(R.id.spt_submit);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Submitting.....");

        String mobileobji = Bsession.getInstance(). getUser_name(SupportActivity.this);
        et_name.setText(mobileobji);
        String mobileobi =Bsession.getInstance().getUser_email(SupportActivity.this);
        et_email.setText(mobileobi);

        spt_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                spt_submit.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                SupportActivity registerActivity = SupportActivity.this;
                registerActivity.name = registerActivity.et_name.getText().toString().trim();
                SupportActivity registerActivity2 = SupportActivity.this;
                registerActivity2.email = registerActivity2.et_email.getText().toString().trim();
                SupportActivity registerActivity3 = SupportActivity.this;
                registerActivity3.message = registerActivity3.et_message.getText().toString();
                if (name.isEmpty()) {
                    et_name.setError("*Enter your name");
                    et_name.requestFocus();
                } else if (message.isEmpty()) {
                    et_message.setError("*Enter your message");
                    et_message.requestFocus();
                }else if (email.isEmpty()) {
                    et_email.setError("*Enter your email");
                    et_email.requestFocus();
                } else if (name == null || name == "" || email == ""|| email == null  || message == null || message == "") {
                    Toast.makeText(SupportActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setSupport();
                }
            }
        });

        toolbar();
    }

    public void setSupport() {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(SupportActivity.this);
        name = et_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        message = et_message.getText().toString();

        progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.support + ("?customer_id="+customer_id)+("&name=" + this.name) +  ("&message=" + this.message)+ ("&email="+this.email), new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(SupportActivity.this , "Message sent ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SupportActivity.this,HomeActivity.class);
                        startActivity(intent);
                    } else {

                        Toast.makeText(SupportActivity.this , "Message not sent", Toast.LENGTH_SHORT).show();
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
        getSupportActionBar().setTitle((CharSequence) "Support Centre ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SupportActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}