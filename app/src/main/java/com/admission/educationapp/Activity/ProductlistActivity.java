package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.ProductAdapter;
import com.admission.educationapp.Model.ProductModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductlistActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView product_list;
    List<ProductModel> productlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        product_list = findViewById(R.id.product_list);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        toolbar();
        productList();
    }

    private void productList() {
        final Map<String, String> params = new HashMap<>();

        String baseUrl = ProductConfig.product_list ;
        productlist = new ArrayList<>();
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
                            ProductModel model = new ProductModel();
                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setName(jsonlist.getJSONObject(j).getString("name"));
                            model.setOffer_price(jsonlist.getJSONObject(j).getString("offer_price"));
                            model.setNon_offer_price(jsonlist.getJSONObject(j).getString("non_offer_price"));
                            model.setDescription(jsonlist.getJSONObject(j).getString("description"));
                            model.setShop_name(jsonlist.getJSONObject(j).getString("shop_name"));
                            model.setShop_address(jsonlist.getJSONObject(j).getString("shop_address"));
                            model.setImage(jsonlist.getJSONObject(j).getString("image"));
                            productlist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductlistActivity.this, LinearLayoutManager.VERTICAL, false);
                        product_list.setLayoutManager(layoutManager);
                        ProductAdapter packageListAdapter = new ProductAdapter(ProductlistActivity.this, productlist);
                        product_list.setAdapter(packageListAdapter);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductlistActivity.this, 2);
                        product_list.setLayoutManager(gridLayoutManager);
                        product_list.setHasFixedSize(true);
                    } else {
                        Toast.makeText(ProductlistActivity.this, "Product list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductlistActivity.this);
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
        getSupportActionBar().setTitle((CharSequence) "Product List ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductlistActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}