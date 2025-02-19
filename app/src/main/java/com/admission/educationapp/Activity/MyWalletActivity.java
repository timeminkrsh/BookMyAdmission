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
import com.admission.educationapp.Adapter.TransactionAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Csession;
import com.admission.educationapp.Model.TransactionModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWalletActivity extends AppCompatActivity {
    TextView wallet_amount;
    RecyclerView rv_transaction_list;
    ProgressDialog progressDialog;
    LinearLayout trans_list,no_translist;
    Button btn_referok;
    List<TransactionModel> transactionlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        wallet_amount = findViewById(R.id.wallet_amount);
        trans_list = findViewById(R.id.trans_list);
        btn_referok = findViewById(R.id.btn_referok);
        no_translist = findViewById(R.id.no_translist);
        rv_transaction_list = findViewById(R.id.rv_transaction_list);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        toolbar();
        walletAmount();
        transactionList();

        btn_referok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void walletAmount() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?customer_id="+Bsession.getInstance().getUser_id(this);
        String baseUrl = ProductConfig.wallet + para_str;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        wallet_amount.setText(" ₹ "+jsonResponse.getString("walletpoint"));
                        Csession.getInstance().initialize(MyWalletActivity.this,"","",
                                "","","","",jsonResponse.getString("walletpoint"),"");
                    } else {
                        Toast.makeText(MyWalletActivity.this , "No details found", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("Reponse=="+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyWalletActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void transactionList() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?customer_id=" + Bsession.getInstance().getUser_id(this);
        System.out.println("custid==="+Bsession.getInstance().getUser_id(this));
        String baseUrl = ProductConfig.transcation_list + para_str;
        transactionlist = new ArrayList<>();
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
                            TransactionModel model = new TransactionModel();
                            model.setSno(jsonlist.getJSONObject(j).getString("S.no"));
                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                            model.setDirect_refer(jsonlist.getJSONObject(j).getString("direct_refer"));
                            model.setTeam_refer(jsonlist.getJSONObject(j).getString("team_refer"));
                            model.setWithdraw_amount(jsonlist.getJSONObject(j).getString("withdraw_amount"));
                            model.setOrder_price(jsonlist.getJSONObject(j).getString("order_price"));
                            model.setBalance(jsonlist.getJSONObject(j).getString("current_balance"));
                            model.setDate(jsonlist.getJSONObject(j).getString("date"));
                            transactionlist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MyWalletActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_transaction_list.setLayoutManager(layoutManager);
                        TransactionAdapter packageListAdapter = new TransactionAdapter(MyWalletActivity.this, transactionlist);
                        rv_transaction_list.setAdapter(packageListAdapter);
                        rv_transaction_list.setHasFixedSize(true);

                    } else {
                        Toast.makeText(MyWalletActivity.this, "No payout history found", Toast.LENGTH_SHORT).show();
                        no_translist.setVisibility(View.VISIBLE);
                        trans_list.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MyWalletActivity.this);
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
        getSupportActionBar().setTitle((CharSequence) "My Wallet ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyWalletActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}