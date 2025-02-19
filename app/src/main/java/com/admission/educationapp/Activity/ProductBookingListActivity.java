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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.ProductBookingAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.ProductBookingModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductBookingListActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView booking_list;
    LinearLayout no_bookinglist,bookings;
    Button btn_referok;
    List<ProductBookingModel> bookinglist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_booking_list);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        booking_list = findViewById(R.id.booking_list);
        no_bookinglist = findViewById(R.id.no_bookinglist);
        btn_referok = findViewById(R.id.btn_referok);
        bookings = findViewById(R.id.bookings);

        btn_referok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductBookingListActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        toolbar();
        bookings();
    }

    private void bookings() {
        final Map<String, String> params = new HashMap<>();
        String para1 = "?customer_id=" + Bsession.getInstance().getUser_id(ProductBookingListActivity.this);
        String baseUrl = ProductConfig.product_bookinglist +para1 ;
        bookinglist = new ArrayList<>();
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
                            ProductBookingModel model = new ProductBookingModel();
                            model.setProd_name(jsonlist.getJSONObject(j).getString("product_name"));
                            model.setProd_price(jsonlist.getJSONObject(j).getString("price"));
                            model.setShop_name(jsonlist.getJSONObject(j).getString("shop_name"));
                            model.setShop_address(jsonlist.getJSONObject(j).getString("shop_address"));
                            bookinglist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductBookingListActivity.this, LinearLayoutManager.VERTICAL, false);
                        booking_list.setLayoutManager(layoutManager);
                        ProductBookingAdapter packageListAdapter = new ProductBookingAdapter(ProductBookingListActivity.this, bookinglist);
                        booking_list.setAdapter(packageListAdapter);
                        booking_list.setHasFixedSize(true);

                    } else {
                        Toast.makeText(ProductBookingListActivity.this, "Bookings list not found", Toast.LENGTH_SHORT).show();
                        no_bookinglist.setVisibility(View.VISIBLE);
                        bookings.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductBookingListActivity.this);
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
        getSupportActionBar().setTitle((CharSequence) "Product Bookings ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductBookingListActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}