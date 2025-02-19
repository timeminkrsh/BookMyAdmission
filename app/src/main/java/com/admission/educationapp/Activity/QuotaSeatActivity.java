package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.CountlistAdapter;
import com.admission.educationapp.Adapter.MqCountlistAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.CountlistModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotaSeatActivity extends AppCompatActivity {
    TextView txt_man_count,txt_coun_count;
    RecyclerView counselling_seatlist,management_seatlist;
    ProgressDialog progressDialog;
    List<CountlistModel> counsellinglist = new ArrayList<>();
    List<CountlistModel> managementlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quota_seat);

        txt_man_count = findViewById(R.id.txt_man_count);
        txt_coun_count = findViewById(R.id.txt_coun_count);
        counselling_seatlist = findViewById(R.id.counselling_seatlist);
        management_seatlist = findViewById(R.id.management_seatlist);

        txt_coun_count.setText("Counselling Quota : "+ Bsession.getInstance().getCq_count(QuotaSeatActivity.this));
        txt_man_count.setText("Management Quota : "+ Bsession.getInstance().getMq_count(QuotaSeatActivity.this));

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        toolbar();
        managementCount();
        counsellingCount();

    }

    private void counsellingCount() {
        final Map<String, String> params = new HashMap<>();
        String para_str2 = "?department_id="+ Bsession.getInstance().getDept_id(QuotaSeatActivity.this);
        String baseUrl   = ProductConfig.counselling_count + para_str2;
        counsellinglist = new ArrayList<>();
        progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            CountlistModel model = new CountlistModel();

                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setQuota(jsonlist.getJSONObject(j).getString("quota"));
                            model.setStatus(jsonlist.getJSONObject(j).getString("status"));
                            model.setStudent_name(jsonlist.getJSONObject(j).getString("student_name"));
                            counsellinglist.add(model);
                        }

                        counselling_seatlist.setLayoutManager(new GridLayoutManager(QuotaSeatActivity.this, 5));
                        CountlistAdapter packageListAdapter = new CountlistAdapter(QuotaSeatActivity.this, counsellinglist);
                        counselling_seatlist.setAdapter(packageListAdapter);
                        counselling_seatlist.setHasFixedSize(true);

                    } else {
                        Toast.makeText(QuotaSeatActivity.this, "Count not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(QuotaSeatActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void managementCount() {
        final Map<String, String> params = new HashMap<>();
        String para_str2 = "?department_id=" +Bsession.getInstance().getDept_id(QuotaSeatActivity.this);
        String baseUrl   = ProductConfig.management_count+para_str2;
        managementlist = new ArrayList<>();
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            CountlistModel model = new CountlistModel();

                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setQuota(jsonlist.getJSONObject(j).getString("quota"));
                            model.setStatus(jsonlist.getJSONObject(j).getString("status"));
                            model.setStudent_name(jsonlist.getJSONObject(j).getString("student_name"));
                            managementlist.add(model);
                        }

                        management_seatlist.setLayoutManager(new GridLayoutManager(QuotaSeatActivity.this, 5));
                        MqCountlistAdapter packageListAdapter = new MqCountlistAdapter(QuotaSeatActivity.this, managementlist);
                        management_seatlist.setAdapter(packageListAdapter);
                        management_seatlist.setHasFixedSize(true);

                    } else {
                        Toast.makeText(QuotaSeatActivity.this, "Count not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(QuotaSeatActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FeesDetailActivity.class));
                finish();
            }
        });
        getSupportActionBar().setTitle((CharSequence) "Seat Availability ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuotaSeatActivity.this, FeesDetailActivity.class);
        startActivity(intent);
    }

}