package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {
    Button btn_verify;
    PinView pinview;
    String phone,otp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("user_mobile");
            System.out.println("refer=="+phone);
        }
        btn_verify = findViewById(R.id.btn_verify);
        pinview = findViewById(R.id.pinview);

        ProgressDialog progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setMessage("Loading....");

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_verify.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                String otpstr = String.valueOf(pinview.getText().toString());
                otp = otpstr;
                if (otpstr != null && otpstr != "" && otpstr.length() >= 4) {
                    final Map<String, String> params = new HashMap<>();
                    progressDialog.show();

                    String para_str = "?user_mobile=" + phone;
                    String para_str1 = "&otp=" + pinview.getText().toString();
                    String baseUrl = ProductConfig.userotpverify + para_str + para_str1;
                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("status") && jsonResponse.getString("status").equals("0")) {
                                    Toast.makeText(OTPActivity.this, "OTP Entered Successfully", Toast.LENGTH_SHORT).show();
                                    LayoutInflater inflater = getLayoutInflater();
                                    View dialogLayout = inflater.inflate(R.layout.refer, null);
                                    EditText referal = dialogLayout.findViewById(R.id.edt_referal);
                                    Button btn = dialogLayout.findViewById(R.id.spt_submit);
                                    Button btn1 = dialogLayout.findViewById(R.id.direct_submit);
                                    AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(OTPActivity.this);
                                    BackAlertDialog.setView(dialogLayout);
                                    BackAlertDialog.setCancelable(false); // Prevent dialog from closing when clicking outside
                                    btn1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(OTPActivity.this, ReferalActivity.class);
                                            i.putExtra("mobile", phone);
                                            System.out.println("refer=="+phone);
                                            startActivity(i);
                                        }
                                    });
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(OTPActivity.this, RegisterActivity.class);
                                            i.putExtra("refer", referal.getText().toString());
                                            i.putExtra("mobile", phone);
                                            System.out.println("refer=="+referal.getText().toString());
                                            System.out.println("refer=="+phone);
                                            startActivity(i);
                                        }
                                    });
                                    BackAlertDialog.show();
                                } else if (jsonResponse.has("status") && jsonResponse.getString("status").equals("1")) {
                                    Bsession.getInstance().initialize(OTPActivity.this,
                                            jsonResponse.getString("user_id"),
                                            jsonResponse.getString("user_name"),
                                            jsonResponse.getString("user_mobile"),
                                            jsonResponse.getString("user_address"),
                                            jsonResponse.getString("user_email"),
                                            jsonResponse.getString("university"), "", "","","", "",jsonResponse.getString("college_id")
                                            , "","","",jsonResponse.getString("12th_marks"));
                                    Toast.makeText(OTPActivity.this, "OTP Entered Successfully", Toast.LENGTH_SHORT).show();


                                    Intent i1 = new Intent(OTPActivity.this, HomeActivity.class);
                                    startActivity(i1);
                                    finish();
                                }
                                else {
                                    Toast.makeText(OTPActivity.this, "Incorrect OTP entered", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(OTPActivity.this, R.style.AlertDialogTheme);
                                    dialog2.setTitle("' "  + OTPActivity.this.pinview.getText().toString().trim() + " ' Incorrect OTP !");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("You have entered the wrong OTP. Please enter correct OTP");
                                    dialog2.setMessage(sb.toString()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("Error", error.toString());
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(OTPActivity.this);
                    requestQueue.add(jsObjRequest);
                } else {
                    Toast.makeText(OTPActivity.this, "Invalid OTP..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}