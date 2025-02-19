package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admission.educationapp.Csession;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.SocialmediaAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.BannerModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailActivity extends AppCompatActivity {
    RecyclerView recycleimage;
    CardView add_cart;
    LinearLayout buy;
    TextView product_title,product_mrp,product_price;
    String productId,offer,nonOffer,name,description;
    String shopName,shopAddress;
    TextView product_desc,product_id,shopname,shopaddress,add;
    List<BannerModel> sliderlist = new ArrayList<>();
    private int currentPosition = 0;
    private Timer timer;
    String wallet;
    ProgressDialog progressDialog;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product_title= findViewById(R.id.product_title);
        product_mrp= findViewById(R.id.product_mrp);
        recycleimage= findViewById(R.id.recycleimage);
        product_desc= findViewById(R.id.product_desc);
        product_id= findViewById(R.id.product_id);
        shopname= findViewById(R.id.shopname);
        shopaddress= findViewById(R.id.shopaddress);
        add_cart= findViewById(R.id.add_cart);
        buy= findViewById(R.id.buy);
        add= findViewById(R.id.add);
        product_price= findViewById(R.id.product_price);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("product_id");
            product_id.setText("ID : BMA"+productId);

            name = bundle.getString("name");
            product_title.setText(name);

            offer = bundle.getString("offer");
            product_price.setText(" ₹ "+offer);

            nonOffer = bundle.getString("non_offer");
            product_mrp.setText("MRP ₹ "+nonOffer);
            product_mrp.setPaintFlags(product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            description = bundle.getString("product_desc");
            product_desc.setText(description);

            shopName = bundle.getString("shop_name");
            shopname.setText("ShopName : "+shopName);

            shopAddress = bundle.getString("shop_address");
            shopaddress.setText("ShopAddress : "+shopAddress);

        }

        toolbar();
        productBanner();

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wallet = Csession.getInstance().getWallet_amount(ProductDetailActivity.this);
                if (wallet.isEmpty()||wallet==null||wallet.equals("0")){
                    add.setError("* ");
                    Toast.makeText(ProductDetailActivity.this, "Your wallet is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                int walletAmount = Integer.parseInt(wallet);
                int totalPrice = Integer.parseInt(offer);
                if (walletAmount < totalPrice){
                    add.setError("*  ");
                    Toast.makeText(ProductDetailActivity.this, "Your total price is higher than wallet amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                showBottomSheetDialog();
            }
        });

    }

    private void showBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.product_booking);
        TextView txt1 = bottomSheetDialog.findViewById(R.id.stu_name);
        TextView txt2 = bottomSheetDialog.findViewById(R.id.mobile);
        TextView txt3 = bottomSheetDialog.findViewById(R.id.email);
        TextView txt4 = bottomSheetDialog.findViewById(R.id.shop_name);
        TextView txt5 = bottomSheetDialog.findViewById(R.id.shop_address);
        TextView txt6 = bottomSheetDialog.findViewById(R.id.prod_name);
        TextView txt7 = bottomSheetDialog.findViewById(R.id.prod_price);
        Button button = bottomSheetDialog.findViewById(R.id.btn_confirm);

        txt1.setText(Bsession.getInstance().getUser_name(ProductDetailActivity.this));
        txt2.setText(Bsession.getInstance().getUser_mobile(ProductDetailActivity.this));
        txt3.setText(Bsession.getInstance().getUser_email(ProductDetailActivity.this));
        txt4.setText(shopName);
        txt5.setText(shopAddress);
        txt6.setText(name);
        txt7.setText(" ₹ "+offer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, String> params = new HashMap<>();
                String para_str = "?customer_id=" + Bsession.getInstance().getUser_id(ProductDetailActivity.this);
                String para_str1 = "&product_id=" + productId;
                String para_str2 = "&name=" + name;
                String para_str3 = "&email=" + Bsession.getInstance().getUser_email(ProductDetailActivity.this);
                String para_str4 = "&mobile=" + Bsession.getInstance().getUser_mobile(ProductDetailActivity.this) ;
                String para_str5 = "&price=" + offer;
                String para_str6 = "&shop_name=" + shopName;
                String para_str7 = "&shop_address=" + shopAddress;

                progressDialog.show();
                bottomSheetDialog.dismiss();
                String baseUrl = ProductConfig.product_confirmation+para_str+para_str1+para_str2+para_str3+para_str4+para_str5+para_str6+para_str7;
                StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                Intent intent = new Intent(ProductDetailActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Profile Update Failed", Toast.LENGTH_LONG).show();
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
                Volley.newRequestQueue(ProductDetailActivity.this).add(jsObjRequest);
                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
            }
        });
        bottomSheetDialog.show();

    }
    private void productBanner() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?product_id="+productId;
        String baseUrl = ProductConfig.product_banner+para_str;
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

                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycleimage.setLayoutManager(linearLayoutManager2);
                        SocialmediaAdapter bestSaleAdapter = new SocialmediaAdapter(ProductDetailActivity.this, sliderlist);
                        recycleimage.setAdapter(bestSaleAdapter);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new ProductDetailActivity.AutoScrollTask(), 0, 2000);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Items records not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private class AutoScrollTask extends TimerTask {
        @Override
        public void run() {
            if (currentPosition < sliderlist.size() - 1) {
                currentPosition++;
            } else {
                currentPosition = 0;
            }
            recycleimage.smoothScrollToPosition(currentPosition);
        }
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
        getSupportActionBar().setTitle((CharSequence) "");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}