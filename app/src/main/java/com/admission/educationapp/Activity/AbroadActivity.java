package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.AbroadCourseModel;
import com.admission.educationapp.Model.CountryListModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbroadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText edt_address,edt_name,edt_phno,edt_emailaddress;
    TextView selectedCountry,selectedCourse;
    String name,mobile,email,address;
    Spinner courses,country;
    Button btn_abrapply;
    String selectedCountryId="",selectedCourseId="";
    ProgressDialog progressDialog;
    ArrayList<String> courselist = new ArrayList<String>();
    ArrayList<String> countrylist = new ArrayList<String>();
    ArrayList<String> course_id = new ArrayList<String>();
    List<AbroadCourseModel> course_list = new ArrayList<>();
    ArrayList<String> country_id = new ArrayList<String>();
    List<CountryListModel> country_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abroad);

        edt_name = findViewById(R.id.edt_name);
        edt_phno = findViewById(R.id.edt_phno);
        edt_emailaddress = findViewById(R.id.edt_emailaddress);
        edt_address = findViewById(R.id.edt_address);
        selectedCountry = findViewById(R.id.selectedCountry);
        selectedCourse = findViewById(R.id.selectedCourse);
        btn_abrapply = findViewById(R.id.btn_abrapply);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        name = Bsession.getInstance().getUser_name(AbroadActivity.this);
        edt_name.setText(name);
        mobile = Bsession.getInstance().getUser_mobile(AbroadActivity.this);
        edt_phno.setText(mobile);
        address = Bsession.getInstance().getUser_address(AbroadActivity.this);
        edt_address.setText(address);
        email =Bsession.getInstance().getUser_email(AbroadActivity.this);
        edt_emailaddress.setText(email);

        btn_abrapply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btn_abrapply.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                AbroadActivity registerActivity = AbroadActivity.this;
                registerActivity.name = registerActivity.edt_name.getText().toString().trim();
                AbroadActivity registerActivity2 = AbroadActivity.this;
                registerActivity2.mobile = registerActivity2.edt_phno.getText().toString().trim();
                AbroadActivity registerActivity3 = AbroadActivity.this;
                registerActivity3.address = registerActivity3.edt_address.getText().toString();
                AbroadActivity registerActivity4 = AbroadActivity.this;
                registerActivity4.email = registerActivity4.edt_emailaddress.getText().toString();
                if (name.isEmpty()) {
                    edt_name.setError("*Enter your name");
                    edt_name.requestFocus();
                } else if (mobile.isEmpty()) {
                    edt_phno.setError("*Enter your mobile number");
                    edt_phno.requestFocus();
                } else if (mobile == null ||mobile.length() < 6 || mobile.length() > 13) {
                    edt_phno.setError("*Enter Valid Mobile Number");
                    edt_phno.requestFocus();
                } else if (address.isEmpty()) {
                    edt_address.setError("*Enter your address");
                    edt_address.requestFocus();
                }else if (email.isEmpty()) {
                    edt_emailaddress.setError("*Enter your email");
                    edt_emailaddress.requestFocus();
                }else if (selectedCountryId.isEmpty()) {
                    selectedCountry.setError("*Enter");
                    selectedCountry.requestFocus();
                }else if (selectedCourseId.isEmpty()) {
                    selectedCourse.setError("*Enter");
                    selectedCourse.requestFocus();
                } else if (name == null || name == "" || email == ""|| email == null || mobile == null || mobile == "" || address == null || address == ""
                        || selectedCourseId == null || selectedCourseId == ""|| selectedCountryId == null || selectedCountryId == "") {
                    Toast.makeText(AbroadActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setRegister();
                }
            }
        });

        courseList();
        countryList();
        toolbar();

    }

    private void courseList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.abroad_courselist;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        course_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            AbroadCourseModel model1 = new AbroadCourseModel();
                            model1.setCourse_id(jsonObject1.getString("course_id"));
                            model1.setCourse_name(jsonObject1.getString("name"));

                            courselist.add(jsonObject1.getString("name"));
                            course_id.add(jsonObject1.getString("course_id"));
                            course_list.add(model1);

                            createDropdownList(courselist);
                            System.out.println("responeee=="+response);
                        }
                    } else {
                        Toast.makeText(AbroadActivity.this, "District not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AbroadActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void createDropdownList(ArrayList<String> courselist) {
        courses = findViewById(R.id.courses);
        courses.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, courselist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courses.setAdapter(adapter);

        courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseId = course_id.get(position);

                System.out.println("Selected District ID: " + selectedCourseId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void countryList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.abroad_countrylist;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        country_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            CountryListModel model1 = new CountryListModel();
                            model1.setCountry_id(jsonObject1.getString("id"));
                            model1.setCountry_name(jsonObject1.getString("name"));

                            countrylist.add(jsonObject1.getString("name"));
                            country_id.add(jsonObject1.getString("id"));
                            country_list.add(model1);

                            createDrop_downList(countrylist);
                            System.out.println("responeeee=="+response);
                        }
                    } else {
                        Toast.makeText(AbroadActivity.this, "District not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AbroadActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void createDrop_downList(ArrayList<String> countrylist) {
        country = findViewById(R.id.country);
        country.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, countrylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryId = country_id.get(position);

                System.out.println("Selected District ID: " + selectedCountryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
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
        getSupportActionBar().setTitle((CharSequence) "Abroad Study");
        toolbar.setTitleTextColor(-1);
    }

    public void setRegister() {
        final Map<String, String> params = new HashMap<>();
        String para1="?course_id="+selectedCourseId;
        String para2="&country_id="+selectedCountryId;
        String para3="&name="+name;
        String para4="&email="+email;
        String para5="&mobile="+mobile;
        String para6="&address="+address;
        progressDialog.show();

        String baseUrl = ProductConfig.abroad_register+para1+para2+para3+para4+para5+para6;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        startActivity(new Intent(AbroadActivity.this, HomeActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Successfully Applied", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_LONG).show();

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}