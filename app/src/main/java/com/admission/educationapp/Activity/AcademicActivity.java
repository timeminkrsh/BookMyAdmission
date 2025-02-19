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

import com.admission.educationapp.Csession;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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

public class AcademicActivity extends AppCompatActivity {
    String mark_10th,mark_11th,mark_12th,engg_cutoff,med_cutoff;
    String pcm_percent,pcb_percent,phy_mark,che_mark,math_mark;
    EditText edt_mark10th,edt_mark11th,edt_mark12th,edt_engg_cutoff,edt_med_cutoff;
    EditText edt_pcm_percent,edt_pcb_percent,edt_phy_mark,edt_che_mark,edt_math_mark;
    Button acad_submit;
    String above_500;
    int mark12Value;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        acad_submit = findViewById(R.id.acad_submit);
        edt_mark10th = findViewById(R.id.edt_mark10th);
        edt_mark11th = findViewById(R.id.edt_mark11th);
        edt_mark12th = findViewById(R.id.edt_mark12th);
        edt_engg_cutoff = findViewById(R.id.edt_engg_cutoff);
        edt_med_cutoff = findViewById(R.id.edt_med_cutoff);
        edt_pcm_percent = findViewById(R.id.edt_pcm_percent);
        edt_pcb_percent = findViewById(R.id.edt_pcb_percent);
        edt_phy_mark = findViewById(R.id.edt_phy_mark);
        edt_che_mark = findViewById(R.id.edt_che_mark);
        edt_math_mark = findViewById(R.id.edt_math_mark);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        acad_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                acad_submit.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                AcademicActivity registerActivity = AcademicActivity.this;
                registerActivity.mark_10th = registerActivity.edt_mark10th.getText().toString().trim();
                AcademicActivity registerActivity2 = AcademicActivity.this;
                registerActivity2.mark_11th = registerActivity2.edt_mark11th.getText().toString().trim();
                AcademicActivity registerActivity3 = AcademicActivity.this;
                registerActivity3.mark_12th = registerActivity3.edt_mark12th.getText().toString();
                AcademicActivity registerActivity4 = AcademicActivity.this;
                registerActivity4.engg_cutoff = registerActivity4.edt_engg_cutoff.getText().toString();
                AcademicActivity registerActivty5 = AcademicActivity.this;
                registerActivty5.med_cutoff = registerActivty5.edt_med_cutoff.getText().toString();
                AcademicActivity registerctivity = AcademicActivity.this;
                registerctivity.pcm_percent = registerctivity.edt_pcm_percent.getText().toString().trim();
                AcademicActivity registerctivity2 = AcademicActivity.this;
                registerctivity2.pcb_percent = registerctivity2.edt_pcb_percent.getText().toString().trim();
                AcademicActivity registerctivity3 = AcademicActivity.this;
                registerctivity3.phy_mark = registerctivity3.edt_phy_mark.getText().toString();
                AcademicActivity registerctivity4 = AcademicActivity.this;
                registerctivity4.che_mark = registerctivity4.edt_che_mark.getText().toString();
                AcademicActivity registerctivty5 = AcademicActivity.this;
                registerctivty5.math_mark = registerctivty5.edt_math_mark.getText().toString();

                if (mark_10th.isEmpty()) {
                    edt_mark10th.setError("*Enter your 10th mark");
                    edt_mark10th.requestFocus();
                } else {
                    int markValue = Integer.parseInt(mark_10th);
                    if (markValue >= 500) {
                        edt_mark10th.setError("Enter valid 10th mark");
                        edt_mark10th.requestFocus();
                        return;
                    }
                }
                if (mark_11th.isEmpty()) {
                    edt_mark11th.setError("*Enter your 11th mark");
                    edt_mark11th.requestFocus();
                    return;
                } else {
                    int markValue = Integer.parseInt(mark_11th);
                    if (markValue >= 600) {
                        edt_mark11th.setError("Enter valid 11th mark");
                        edt_mark11th.requestFocus();
                        return;
                    }
                }
                if (mark_12th.isEmpty()) {
                    edt_mark12th.setError("*Enter your 12th mark");
                    edt_mark12th.requestFocus();
                    return;
                } else {
                    mark12Value = Integer.parseInt(mark_12th);
                    if (mark12Value >= 600) {
                        edt_mark12th.setError("Enter valid 12th mark");
                        edt_mark12th.requestFocus();
                        return;
                    }else if (mark12Value >= 500 && mark12Value <= 600){
                        above_500 = String.valueOf(mark12Value);
                        System.out.println("aboveee500=="+above_500);
                    }else{

                    }
                }
                if (phy_mark.isEmpty()) {
                    edt_phy_mark.setError("*Enter your physics mark");
                    edt_phy_mark.requestFocus();
                    return;
                } else {
                    int phyMarkValue = Integer.parseInt(phy_mark);
                    if (phyMarkValue < 0 || phyMarkValue > 100) {
                        edt_phy_mark.setError("Enter valid physics mark (0-100)");
                        edt_phy_mark.requestFocus();
                        return;
                    }
                }
                if (che_mark.isEmpty()) {
                    edt_che_mark.setError("*Enter your chemistry mark");
                    edt_che_mark.requestFocus();
                    return;
                } else {
                    int cheMarkValue = Integer.parseInt(che_mark);
                    if (cheMarkValue < 0 || cheMarkValue > 100) {
                        edt_che_mark.setError("Enter valid chemistry mark (0-100)");
                        edt_che_mark.requestFocus();
                        return;
                    }
                }
                if (math_mark.isEmpty()) {
                    edt_math_mark.setError("*Enter your maths mark");
                    edt_math_mark.requestFocus();
                    return;
                } else {
                    int mathMarkValue = Integer.parseInt(math_mark);
                    if (mathMarkValue < 0 || mathMarkValue > 100) {
                        edt_math_mark.setError("Enter valid maths mark (0-100)");
                        edt_math_mark.requestFocus();
                        return;
                    }
                }
                setRegister();
            }
        });

        toolbar();

    }

    public void setRegister() {
        final Map<String, String> params = new HashMap<>();
        String para1= "?customer_id="+Bsession.getInstance().getUser_id(AcademicActivity.this);
        String para2= "&college_id="+ Csession.getInstance().getInd_colid(AcademicActivity.this);
        String para3="&department_id="+Bsession.getInstance().getDept_id(AcademicActivity.this);
        String para4="&fees_id="+Bsession.getInstance().getFees_id(AcademicActivity.this);
        String para5="&tenth_mark="+mark_10th;
        String para6="&eveleth_mark="+mark_11th;
        String para7="&twelfth_mark="+mark_12th;
        String para8="&physics="+phy_mark;
        String para9="&chemistry="+che_mark;
        String para10="&maths="+math_mark;
        progressDialog.show();

        String baseUrl = ProductConfig.academic+para1+para2+para3+para4+para5+para6+para7+para8+para9+para10;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (!jsonResponse.has(FirebaseAnalytics.Param.SUCCESS) || !jsonResponse.getString(FirebaseAnalytics.Param.SUCCESS).equals("1")) {
                        Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Details updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(AcademicActivity.this, SeatRegisterActivity.class);
                        intent.putExtra("above_500", mark12Value);
                        startActivity(intent);

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
                startActivity(new Intent(getApplicationContext(), QuotaSeatActivity.class));
                finish();
            }
        });
        getSupportActionBar().setTitle((CharSequence) "Academic Status ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AcademicActivity.this, QuotaSeatActivity.class);
        startActivity(intent);
    }
}