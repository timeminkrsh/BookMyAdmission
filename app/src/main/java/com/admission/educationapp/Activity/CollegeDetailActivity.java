package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
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
import com.admission.educationapp.Adapter.CountAdapter;
import com.admission.educationapp.Adapter.SocialmediaAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Csession;
import com.admission.educationapp.Model.BannerModel;
import com.admission.educationapp.Model.CountModel;
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

public class CollegeDetailActivity extends AppCompatActivity {
    RecyclerView rv_teamcount;
    TextView abt_clg,col_code,col_location,col_url;
    List<CountModel> countlist = new ArrayList<>();
    ProgressDialog progressDialog;
    String uni_Name,col_Name;
    RecyclerView recycleimage;
    TextView tool_name,un_name;
    List<BannerModel> sliderlist = new ArrayList<>();
    private int currentPosition = 0;
    private Timer timer;
    String des="",weburl="",id="",name="",address="",code="",univer="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_detail);

        abt_clg= findViewById(R.id.abt_clg);
        col_code= findViewById(R.id.col_code);
        col_location= findViewById(R.id.col_location);
        col_url= findViewById(R.id.col_url);
        un_name= findViewById(R.id.un_name);
        tool_name= findViewById(R.id.tool_name);
        rv_teamcount= findViewById(R.id.rv_teamcount);
        recycleimage= findViewById(R.id.recycleimage);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getString("id");
            name = bundle.getString("name");
            address = bundle.getString("address");
            code = bundle.getString("code");
            univer = bundle.getString("univer");
            des = bundle.getString("des");
            weburl = bundle.getString("weburl");
            System.out.println("bbb="+id);
            deptCounts();
            colBanners();
            un_name.setText(univer);
            col_Name=name;
            col_code.setText(code);
            col_location.setText(address);
            abt_clg.setText(des);
            col_url.setText(weburl);
            Linkify.addLinks(col_url, Linkify.WEB_URLS);
        } else {
            String name = Csession.getInstance().getUni_name(CollegeDetailActivity.this);
            un_name.setText(name);
            col_Name =  Csession.getInstance().getCol_name(CollegeDetailActivity.this);
            String clg_code =Csession.getInstance().getCol_code(CollegeDetailActivity.this);
            col_code.setText(clg_code);
            String clg_address =Csession.getInstance().getCol_address(CollegeDetailActivity.this);
            col_location.setText(clg_address);
            String clg_weburl =Csession.getInstance().getCol_weburl(CollegeDetailActivity.this);
            col_url.setText(clg_weburl);
            Linkify.addLinks(col_url, Linkify.WEB_URLS);
            String clg_desc =Csession.getInstance().getCol_description(CollegeDetailActivity.this);
            abt_clg.setText(clg_desc);
            deptCount();
            colBanner();
        }
        toolbar();


    }

    private void colBanner() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?college_id=" + Csession.getInstance().getInd_colid(CollegeDetailActivity.this);
        String baseUrl  = ProductConfig.college_banner + para_str;
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
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(CollegeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycleimage.setLayoutManager(linearLayoutManager2);
                        SocialmediaAdapter bestSaleAdapter = new SocialmediaAdapter(CollegeDetailActivity.this, sliderlist);
                        recycleimage.setAdapter(bestSaleAdapter);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new CollegeDetailActivity.AutoScrollTask(), 0, 2000);
                    } else {
                        Toast.makeText(CollegeDetailActivity.this, "Items records not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeDetailActivity.this);
        requestQueue.add(jsObjRequest);
    }
    private void colBanners() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?college_id=" +id;
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

                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(CollegeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycleimage.setLayoutManager(linearLayoutManager2);
                        SocialmediaAdapter bestSaleAdapter = new SocialmediaAdapter(CollegeDetailActivity.this, sliderlist);
                        recycleimage.setAdapter(bestSaleAdapter);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new CollegeDetailActivity.AutoScrollTask(), 0, 2000);
                    } else {
                        Toast.makeText(CollegeDetailActivity.this, "Items records not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeDetailActivity.this);
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

    private void deptCount() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?college_id=" + Csession.getInstance().getInd_colid(CollegeDetailActivity.this);
        String baseUrl = ProductConfig.dept_count+para_str ;
        countlist = new ArrayList<>();
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
                            CountModel model = new CountModel();
                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setCol_id(jsonlist.getJSONObject(j).getString("college_id"));
                            model.setDepartment(jsonlist.getJSONObject(j).getString("name"));
                            model.setCount(jsonlist.getJSONObject(j).getString("total"));
                            model.setCq_count(jsonlist.getJSONObject(j).getString("counselling_quota"));
                            model.setMq_count(jsonlist.getJSONObject(j).getString("management_quota"));
                            countlist.add(model);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(CollegeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_teamcount.setLayoutManager(layoutManager);
                        CountAdapter packageListAdapter = new CountAdapter(CollegeDetailActivity.this, countlist);
                        rv_teamcount.setAdapter(packageListAdapter);
                        rv_teamcount.setHasFixedSize(true);

                    } else {
                        Toast.makeText(CollegeDetailActivity.this, "Count list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeDetailActivity.this);
        requestQueue.add(jsObjRequest);

    }
    private void deptCounts() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?college_id=" + id;
        String baseUrl = ProductConfig.dept_count+para_str ;
        countlist = new ArrayList<>();
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
                            CountModel model = new CountModel();
                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setCol_id(jsonlist.getJSONObject(j).getString("college_id"));
                            model.setDepartment(jsonlist.getJSONObject(j).getString("name"));
                            model.setCount(jsonlist.getJSONObject(j).getString("total"));
                            model.setCq_count(jsonlist.getJSONObject(j).getString("counselling_quota"));
                            model.setMq_count(jsonlist.getJSONObject(j).getString("management_quota"));
                            countlist.add(model);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(CollegeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_teamcount.setLayoutManager(layoutManager);
                        CountAdapter packageListAdapter = new CountAdapter(CollegeDetailActivity.this, countlist);
                        rv_teamcount.setAdapter(packageListAdapter);
                        rv_teamcount.setHasFixedSize(true);

                    } else {
                        Toast.makeText(CollegeDetailActivity.this, "Count list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeDetailActivity.this);
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
        getSupportActionBar().setTitle((CharSequence) col_Name);
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CollegeDetailActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}