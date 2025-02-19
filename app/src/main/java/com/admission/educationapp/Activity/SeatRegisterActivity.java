package com.admission.educationapp.Activity;

import static android.view.Gravity.CENTER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
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
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.BookingModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatRegisterActivity extends AppCompatActivity {
    Button btn_book;
    String mobile,merit="";
    String name,parent;
    String email,pmss="";
    String address,fg="";
    TextView txt_fg,txt_merit,txt_pmss;
    EditText edt_name,edt_parent,edt_phno;
    EditText edt_emailaddress,edt_address;
    RadioGroup radio_fg,radio_pmss,radio_merit;
    ProgressDialog progressDialog;
    String universityName,collegeName,quota,status;
    List<BookingModel> bookinglist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_register);

        btn_book = findViewById(R.id.btn_book);
        edt_name = findViewById(R.id.edt_name);
        edt_parent = findViewById(R.id.edt_parent);
        edt_phno = findViewById(R.id.edt_phno);
        edt_emailaddress = findViewById(R.id.edt_emailaddress);
        edt_address = findViewById(R.id.edt_address);
        radio_fg = findViewById(R.id.radio_fg);
        radio_pmss = findViewById(R.id.radio_pmss);
        radio_merit = findViewById(R.id.radio_merit);
        txt_fg = findViewById(R.id.txt_fg);
        txt_pmss = findViewById(R.id.txt_pmss);
        txt_merit = findViewById(R.id.txt_merit);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Use the correct method to retrieve an Integer value
            Integer above500Value = bundle.getInt("above_500", 0); // 0 is the default value if the key is not found
            // Check the value of above500Value and set the radio button accordingly
            if (above500Value != null) {
                if (above500Value > 500) {
                    radio_merit.check(R.id.merit_yes);
                    radio_merit.findViewById(R.id.merit_no).setEnabled(false);
                    merit = "Yes";
                } else {
                    radio_merit.check(R.id.merit_no);
                    radio_merit.findViewById(R.id.merit_yes).setEnabled(false);
                    merit = "No";
                }
            }
        }

        String above_500 = Bsession.getInstance().getMark_12th(SeatRegisterActivity.this);
        int mark_12 = Integer.parseInt(above_500);
        if (mark_12 > 500) {
            radio_merit.check(R.id.merit_yes);
            radio_merit.findViewById(R.id.merit_no).setEnabled(false);
            merit = "Yes";
        } else {
            radio_merit.check(R.id.merit_no);
            radio_merit.findViewById(R.id.merit_yes).setEnabled(false);
            merit = "No";
        }

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Registering.....");

        radio_fg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which RadioButton is checked
                if (checkedId == R.id.fg_yes) {
                    fg = "Yes";
                } else if (checkedId == R.id.fg_no) {
                    fg = "No";
                }
            }
        });

        radio_pmss.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which RadioButton is checked
                if (checkedId == R.id.pmss_yes) {
                    pmss = "Yes";
                } else if (checkedId == R.id.pmss_no) {
                    pmss = "No";
                }
            }
        });

        radio_merit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which RadioButton is checked
                if (checkedId == R.id.merit_yes) {
                    merit = "Yes";
                } else if (checkedId == R.id.merit_no) {
                    merit = "No";
                }
            }
        });

        name = Bsession.getInstance().getUser_name(SeatRegisterActivity.this);
        edt_name.setText(name);
        mobile = Bsession.getInstance().getUser_mobile(SeatRegisterActivity.this);
        edt_phno.setText(mobile);
        email = Bsession.getInstance().getUser_email(SeatRegisterActivity.this);
        edt_emailaddress.setText(email);
        address = Bsession.getInstance().getUser_address(SeatRegisterActivity.this);
        edt_address.setText(address);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_book.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                SeatRegisterActivity registerActivity = SeatRegisterActivity.this;
                registerActivity.name = registerActivity.edt_name.getText().toString().trim();
                SeatRegisterActivity registerActivity2 = SeatRegisterActivity.this;
                registerActivity2.mobile = registerActivity2.edt_phno.getText().toString().trim();
                SeatRegisterActivity registerActivit = SeatRegisterActivity.this;
                registerActivit.parent = registerActivit.edt_parent.getText().toString().trim();
                SeatRegisterActivity registerActivity3 = SeatRegisterActivity.this;
                registerActivity3.address = registerActivity3.edt_address.getText().toString();
                SeatRegisterActivity registerActivity4 = SeatRegisterActivity.this;
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
                }else if (parent.isEmpty()) {
                    edt_parent.setError("*Enter your parent name");
                    edt_parent.requestFocus();
                }else if (fg.isEmpty()) {
                    txt_fg.setError("*Enter your email");
                    txt_fg.requestFocus();
                }else if (pmss.isEmpty()) {
                    txt_pmss.setError("*Enter your email");
                    txt_pmss.requestFocus();
                } else if (merit.isEmpty()) {
                    txt_merit.setError("*Enter your email");
                    txt_merit.requestFocus();
                } else if (name == null || name == "" || email == ""|| email == null || mobile == null || mobile == "" || address == null || address == ""
                        || fg == null || fg == ""|| pmss == null || pmss == ""|| merit == null || merit == "") {
                    Toast.makeText(SeatRegisterActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setRegister();
                }
            }
        });

        toolbar();
    }

    public void setRegister() {
        final Map<String, String> params = new HashMap<>();
        String para1="?customer_id="+Bsession.getInstance().getUser_id(SeatRegisterActivity.this);
        String par1="&college_id="+ Csession.getInstance().getInd_colid(SeatRegisterActivity.this);
        String par2="&department_id="+Bsession.getInstance().getDept_id(SeatRegisterActivity.this);
        String par3="&fees_id="+Bsession.getInstance().getFees_id(SeatRegisterActivity.this);
        String para2="&student_name="+name;
        String para3="&parent_name="+parent;
        String para4="&mobile="+mobile;
        String para5="&email="+email;
        String para6="&address="+address;
        String para7="&first_graducate="+fg;
        String para8="&pmss="+pmss;
        String para9="&merit="+merit;
        String para10="&quota="+Bsession.getInstance().getQuota(SeatRegisterActivity.this);
        System.out.println("vvvvv"+Bsession.getInstance().getQuota(SeatRegisterActivity.this));
        progressDialog.show();

        String baseUrl = ProductConfig.seat_register+para1+par1+par2+par3+para2+para3+para4+para5+para6+para7+para8+para9+para10;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getApplicationContext(), "Seat Registered Success", Toast.LENGTH_LONG).show();
                        popupView(universityName,collegeName,quota,status);
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

    private void popupView(String universityName, String collegeName, String quota, String status) {
        LayoutInflater inflater = LayoutInflater.from(SeatRegisterActivity.this);
        View popupView = inflater.inflate(R.layout.booking_confirmation, null);
        TextView book_uniname = popupView.findViewById(R.id.book_uniname);
        TextView book_clname = popupView.findViewById(R.id.book_clname);
        TextView book_course = popupView.findViewById(R.id.book_course);
        TextView book_quota = popupView.findViewById(R.id.book_quota);
        TextView book_status = popupView.findViewById(R.id.book_status);
        Button book_ok = popupView.findViewById(R.id.btn_ok);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;
        PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);
        popupWindow.showAtLocation(popupView,CENTER,0,0);

        final Map<String, String> params = new HashMap<>();
        String para1 = "?customer_id=" + Bsession.getInstance().getUser_id(SeatRegisterActivity.this);
        String baseUrl = ProductConfig.booking_confirmation +para1 ;
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
                            BookingModel model = new BookingModel();
                            model.setBook_uniname(jsonlist.getJSONObject(j).getString("university_name"));
                            model.setBook_clgname(jsonlist.getJSONObject(j).getString("college_name"));
                            model.setBook_quota(jsonlist.getJSONObject(j).getString("quota"));
                            model.setBook_status(jsonlist.getJSONObject(j).getString("status"));
                            bookinglist.add(model);
                            book_uniname.setText(model.getBook_uniname());
                            book_clname.setText(model.getBook_clgname());
                            book_course.setText(model.getBook_course());  // Update this line with the actual course information
                            book_quota.setText(Bsession.getInstance().getQuota(SeatRegisterActivity.this));
                            book_status.setText(model.getBook_status());
                            book_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    book_ok.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                                    Intent intent = new Intent(SeatRegisterActivity.this,PaymentActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

                    } else {
                        Toast.makeText(SeatRegisterActivity.this, "Seat list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SeatRegisterActivity.this);
        requestQueue.add(jsObjRequest);

    }

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QuotaSeatActivity.class));
                finish();
            }
        });
        getSupportActionBar().setTitle((CharSequence) "Seat Register ");
        toolbar.setTitleTextColor(-1);
    }

   @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(this);
        BackAlertDialog.setTitle((CharSequence) "Activity Exit Alert");
        BackAlertDialog.setMessage((CharSequence) "Are you sure want to exit ?");
        BackAlertDialog.setIcon((int) R.drawable.logo1);
        BackAlertDialog.setPositiveButton((CharSequence) "NO", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        BackAlertDialog.setNegativeButton((CharSequence) "YES", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                finish();
            }

        });
        BackAlertDialog.show();
    }

}