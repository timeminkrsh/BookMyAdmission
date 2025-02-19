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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.BankModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EducationLoanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button loan_apply;
    Spinner banks;
    EditText edt_name,edt_phno;
    EditText edt_address,edt_emailaddress;
    String name,email,address,mobile;
    ProgressDialog progressDialog;
    String selectedValue="",selectedBankId="";
    ArrayList<String> pincodelist = new ArrayList<String>();
    ArrayList<String> bank_id = new ArrayList<String>();
    List<BankModel> bank_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_loan);

        loan_apply = findViewById(R.id.loan_apply);
        edt_name = findViewById(R.id.edt_name);
        edt_phno = findViewById(R.id.edt_phno);
        edt_address = findViewById(R.id.edt_address);
        edt_emailaddress = findViewById(R.id.edt_emailaddress);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        name = Bsession.getInstance().getUser_name(EducationLoanActivity.this);
        edt_name.setText(name);
        mobile = Bsession.getInstance().getUser_mobile(EducationLoanActivity.this);
        edt_phno.setText(mobile);
        address = Bsession.getInstance().getUser_address(EducationLoanActivity.this);
        edt_address.setText(address);
        email =Bsession.getInstance().getUser_email(EducationLoanActivity.this);
        edt_emailaddress.setText(email);

        loan_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loan_apply.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
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
                    Toast.makeText(EducationLoanActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setUpdate();
                }
            }
        });

        toolbar();
        bankList();
    }

    private void bankList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.bank_list;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        bank_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            BankModel model1 = new BankModel();
                            model1.setBank_id(jsonObject1.getString("id"));
                            model1.setBank_name(jsonObject1.getString("bank_name"));
                            pincodelist.add(jsonObject1.getString("bank_name"));
                            bank_id.add(jsonObject1.getString("id"));
                            bank_list.add(model1);
                            System.out.println("drpdoown=="+bank_list);
                            createDropdownList(pincodelist);
                            System.out.println("responee=="+response);
                        }
                    } else {
                        Toast.makeText(EducationLoanActivity.this, "District not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EducationLoanActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void createDropdownList(ArrayList<String> pincodelist) {
        banks = findViewById(R.id.banks);
        banks.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, pincodelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        banks.setAdapter(adapter);

        banks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue = parent.getItemAtPosition(position).toString();
                selectedBankId = bank_id.get(position);
                // Do something with the selected value
                System.out.println("selectvv=="+selectedValue);
                System.out.println("Selected District ID: " + selectedBankId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void setUpdate() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?customer_id=" + Bsession.getInstance().getUser_id(this);
        String para_str2 = "&name=" + name ;
        String para_str3 = "&email=" + email;
        String para_str4 = "&mobile=" + mobile;
        String para_str5 = "&address=" + address;
        String para_str6 = "&bank_name=" + selectedBankId;

        progressDialog.show();
        String baseUrl = ProductConfig.education_loan + para_str+para_str2+para_str3+para_str4+para_str5+para_str6;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        startActivity(new Intent(EducationLoanActivity.this, HomeActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Loan applied success", Toast.LENGTH_LONG).show();
                        System.out.println("Response=="+response);
                    } else {
                        Toast.makeText(getApplicationContext(), "Loan applied Failed", Toast.LENGTH_LONG).show();
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
        getSupportActionBar().setTitle((CharSequence) "Education Loan ");
        toolbar.setTitleTextColor(-1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EducationLoanActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}