package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admission.educationapp.Csession;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.FeesAdapter;
import com.admission.educationapp.Adapter.SocialmediaAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.BannerModel;
import com.admission.educationapp.Model.FeesModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FeesDetailActivity extends AppCompatActivity {
    RecyclerView rv_feescount;
    ProgressDialog progressDialog;
    List<FeesModel> feeslist = new ArrayList<>();
    RecyclerView recycleimage;
    List<BannerModel> sliderlist = new ArrayList<>();
    private int currentPosition = 0;
    private Timer timer;
    Button btn_viewseat;
    TextView phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_detail);

        rv_feescount = findViewById(R.id.rv_feescount);
        recycleimage= findViewById(R.id.recycleimage);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView );
        btn_viewseat = findViewById(R.id.btn_viewseat );

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        btn_viewseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_viewseat.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                Intent intent = new Intent(FeesDetailActivity.this, QuotaSeatActivity.class);
                startActivity(intent);
            }
        });

        phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhoneNumber((TextView) view);
            }
        });

        toolbar();
        colBanner();
        //feeStructure();

    }

    private void callPhoneNumber(TextView view) {
        String phoneNumber = phoneNumberTextView.getText().toString().trim();
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));

        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            Toast.makeText(this, "No app to handle phone calls", Toast.LENGTH_SHORT).show();
        }
    }


    private void colBanner() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?college_id=" + Csession.getInstance().getInd_colid(FeesDetailActivity.this);
        String baseUrl   = ProductConfig.college_banner + para_str;
        sliderlist = new ArrayList<>();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonResarray = jsonResponse.getJSONArray("storeList");
                        for (int i = 0; i < jsonResarray.length(); i++) {
                            JSONObject jsonObject1 = jsonResarray.getJSONObject(i);
                            BannerModel model1 = new BannerModel();
                            model1.setBannerimage(jsonObject1.getString("banner_image"));
                            sliderlist.add(model1);
                        }

                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(FeesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycleimage.setLayoutManager(linearLayoutManager2);
                        SocialmediaAdapter bestSaleAdapter = new SocialmediaAdapter(FeesDetailActivity.this, sliderlist);
                        recycleimage.setAdapter(bestSaleAdapter);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new FeesDetailActivity.AutoScrollTask(), 0, 2000);
                    } else {
                        Toast.makeText(FeesDetailActivity.this, "Items records not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FeesDetailActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private class AutoScrollTask extends TimerTask {
        @Override
        public void run() {
            // Scroll to the next item
            if (currentPosition < sliderlist.size() - 1) {
                currentPosition++;
            } else {
                currentPosition = 0; // Reset to the first item
            }
            recycleimage.smoothScrollToPosition(currentPosition);
        }
    }

    private void feeStructure() {
        final Map<String, String> params = new HashMap<>();
        String para_str1 = "?college_id=" + Csession.getInstance().getInd_colid(FeesDetailActivity.this);
        String para_str2 = "&department_id="+Bsession.getInstance().getDept_id(FeesDetailActivity.this);
        String baseUrl   = ProductConfig.fees + para_str1+para_str2;
        feeslist = new ArrayList<>();
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
                            FeesModel model = new FeesModel();

                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setQuota(jsonlist.getJSONObject(j).getString("quota"));
                            model.setHostel_fees(jsonlist.getJSONObject(j).getString("hostler_fees"));
                            model.setRoom_fees(jsonlist.getJSONObject(j).getString("day_scholar_fees"));
                            model.setPmss(jsonlist.getJSONObject(j).getString("pmss"));
                            model.setTution_fees(jsonlist.getJSONObject(j).getString("tution_fees"));
                            feeslist.add(model);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(FeesDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_feescount.setLayoutManager(layoutManager);
                        FeesAdapter packageListAdapter = new FeesAdapter(FeesDetailActivity.this, feeslist);
                        rv_feescount.setAdapter(packageListAdapter);
                        rv_feescount.setHasFixedSize(true);

                    } else {
                        Toast.makeText(FeesDetailActivity.this, "Fees list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FeesDetailActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CollegeDetailActivity.class));
                finish();
            }
        });
        getSupportActionBar().setTitle((CharSequence) "Quota Detail ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FeesDetailActivity.this, CollegeDetailActivity.class);
        startActivity(intent);
    }

}