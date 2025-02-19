package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.ReferAdapter;
import com.admission.educationapp.Adapter.TeamReferAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.ReferModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferralListActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView referal_list,teamrefer_list;
    LinearLayout no_referlist,referal;
    Button btn_referok;
    TextView stu_id,stu_name;
    List<ReferModel> referlist = new ArrayList<>();
    List<ReferModel> teamreferlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_list);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        referal_list = findViewById(R.id.referal_list);
        teamrefer_list = findViewById(R.id.teamrefer_list);
        referal = findViewById(R.id.referal);
        no_referlist = findViewById(R.id.no_referlist);
        btn_referok = findViewById(R.id.btn_referok);
        stu_id = findViewById(R.id.stu_id);
        stu_name = findViewById(R.id.stu_name);

        btn_referok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReferralListActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        stu_name.setText(Bsession.getInstance().getUser_name(ReferralListActivity.this));
        stu_id.setText(Bsession.getInstance().getUser_id(ReferralListActivity.this));

        toolbar();
        referList();
        teamreferList();
    }

    private void referList() {
        final Map<String, String> params = new HashMap<>();
        String para1 = "?customer_id=" + Bsession.getInstance().getUser_id(ReferralListActivity.this);
        String baseUrl = ProductConfig.refer + para1 ;
        referlist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ReferModel model = new ReferModel();
                            model.setId(jsonlist.getJSONObject(j).getString("user_id"));
                            model.setName(jsonlist.getJSONObject(j).getString("user_name"));
                            referlist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ReferralListActivity.this, LinearLayoutManager.VERTICAL, false);
                        referal_list.setLayoutManager(layoutManager);
                        ReferAdapter packageListAdapter = new ReferAdapter(ReferralListActivity.this, referlist);
                        referal_list.setAdapter(packageListAdapter);
                        referal_list.setHasFixedSize(true);

                    } else {
                        Toast.makeText(ReferralListActivity.this, "Referral list not found", Toast.LENGTH_SHORT).show();
                        no_referlist.setVisibility(View.VISIBLE);
                        referal.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ReferralListActivity.this);
        requestQueue.add(jsObjRequest);

    }

    private void teamreferList() {
        final Map<String, String> params = new HashMap<>();
        String para1 = "?customer_id=" + Bsession.getInstance().getUser_id(ReferralListActivity.this);
        String baseUrl = ProductConfig.team_refer+para1 ;
        teamreferlist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ReferModel model = new ReferModel();
                            model.setId(jsonlist.getJSONObject(j).getString("user_id"));
                            model.setName(jsonlist.getJSONObject(j).getString("user_name"));
                            teamreferlist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ReferralListActivity.this, LinearLayoutManager.VERTICAL, false);
                        teamrefer_list.setLayoutManager(layoutManager);
                        TeamReferAdapter packageListAdapter = new TeamReferAdapter(ReferralListActivity.this, teamreferlist);
                        teamrefer_list.setAdapter(packageListAdapter);
                        teamrefer_list.setHasFixedSize(true);

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ReferralListActivity.this);
        requestQueue.add(jsObjRequest);

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
        getSupportActionBar().setTitle((CharSequence) "Referral List ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReferralListActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}