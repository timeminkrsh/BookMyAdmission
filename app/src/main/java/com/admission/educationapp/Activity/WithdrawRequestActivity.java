package com.admission.educationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Csession;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WithdrawRequestActivity extends AppCompatActivity {
    TextView withdraw_balance;
    TextView txt_reqamount;
    EditText wd_amount;
    String req_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_request);

        withdraw_balance = findViewById(R.id.withdraw_balance);
        txt_reqamount = findViewById(R.id.btn_reqamount);
        wd_amount = findViewById(R.id.wd_amount);

        txt_reqamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_reqamount.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                req_amount = wd_amount.getText().toString().trim();
                if (req_amount.isEmpty()||req_amount==null){
                    wd_amount.setError("* Enter the Withdraw Amount");
                    wd_amount.requestFocus();
                    return;
                }
                int withdrawalAmount = Integer.parseInt(req_amount);
                int currentBalance = Integer.parseInt(withdraw_balance.getText().toString().replace(" ₹ ", ""));

                if (withdrawalAmount > currentBalance) {
                    wd_amount.setError("* Enter a valid amount");
                    wd_amount.requestFocus();
                    return;
                }
                checkwithreq();
            }
        });

        String walletpoint = Csession.getInstance().getWallet_amount(WithdrawRequestActivity.this);
        withdraw_balance.setText(" ₹ "+walletpoint);

        toolbar();
    }

    private void checkwithreq() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?customer_id=" + Bsession.getInstance().getUser_id(this);
        String para1 =  "&amount=" + req_amount;
        String baseUrl   = ProductConfig.withdraw + para_str + para1;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(WithdrawRequestActivity.this , "Withdraw amount sent ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WithdrawRequestActivity.this,HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(WithdrawRequestActivity.this , "Withdraw amount not found", Toast.LENGTH_SHORT).show();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(WithdrawRequestActivity.this);
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
        getSupportActionBar().setTitle((CharSequence) "Withdraw Request ");
        toolbar.setTitleTextColor(-1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WithdrawRequestActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}