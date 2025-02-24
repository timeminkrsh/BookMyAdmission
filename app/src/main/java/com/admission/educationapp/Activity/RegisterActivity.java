package com.admission.educationapp.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.UniversityModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class RegisterActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION_PERMISSION = 123;
    Button btn_register;
    EditText edtAddress, edt_name,et_date, edt_phno;
    EditText edt_emailaddress,edt_parent;
    TextView tvCurrentLocation;
    String mobile;
    String name,parent,dob,refer="",phone;
    String email;
    String address, uni_name = " ", uni_id = " ";
    String selectedOption = " ";
    DatePickerDialog datePicker;
    TextView txt_mode, txt_unity;
    ProgressDialog progressDialog,progressDialog2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
        edtAddress = findViewById(R.id.edt_address);
        btn_register = findViewById(R.id.btn_register);
        edt_name = findViewById(R.id.edt_name);
        et_date = findViewById(R.id.et_date);
        edt_phno = findViewById(R.id.edt_phno);
        edt_emailaddress = findViewById(R.id.edt_emailaddress);
        edt_parent = findViewById(R.id.edt_parent);

        txt_mode = findViewById(R.id.txt_mode);
        txt_unity = findViewById(R.id.txt_unity);
        tvCurrentLocation = findViewById(R.id.tv_currentlocation);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            refer = bundle.getString("refer");
            phone = bundle.getString("mobile");
            edt_phno.setText(phone);
        }

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Registering.....");


        statusCheck();
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);

        datePicker = new DatePickerDialog(RegisterActivity.this);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        et_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                Calendar minAgeCalendar = Calendar.getInstance();
                minAgeCalendar.add(Calendar.YEAR, -17); // User should be at least 17 years old
                datePicker.getDatePicker().setMaxDate(minAgeCalendar.getTimeInMillis());

                // Show the dialog
                datePicker.show();
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btn_register.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));

                RegisterActivity registerActivity = RegisterActivity.this;
                registerActivity.name = registerActivity.edt_name.getText().toString().trim();
                RegisterActivity registerActivity2 = RegisterActivity.this;
                registerActivity2.mobile = registerActivity2.edt_phno.getText().toString().trim();
                RegisterActivity registerActivity3 = RegisterActivity.this;
                registerActivity3.address = registerActivity3.edtAddress.getText().toString();
                RegisterActivity registerActivity4 = RegisterActivity.this;
                email = registerActivity4.edt_emailaddress.getText().toString();
                dob = et_date.getText().toString();
                parent = edt_parent.getText().toString();
                System.out.println("bnbn" + refer);

                if (name.isEmpty()) {
                    edt_name.setError("*Enter your name");
                    edt_name.requestFocus();
                } else if (mobile.isEmpty()) {
                    edt_phno.setError("*Enter your mobile number");
                    edt_phno.requestFocus();
                } else if (mobile == null || mobile.length() < 6 || mobile.length() > 13) {
                    edt_phno.setError("*Enter Valid Mobile Number");
                    edt_phno.requestFocus();
                } else if (address.isEmpty()) {
                    edtAddress.setError("*Enter your address");
                    edtAddress.requestFocus();
                } else if (email.isEmpty()) {
                    edt_emailaddress.setError("*Enter your email");
                    edt_emailaddress.requestFocus();
                }  else if (!isValidEmail(email)) {
                    edt_emailaddress.setError("*Enter a valid email");
                    edt_emailaddress.requestFocus();
                } else if (selectedOption.isEmpty()) {
                    txt_mode.setError("*Enter");
                    txt_mode.requestFocus();
                } else if (uni_name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please select the University", Toast.LENGTH_SHORT).show();
                } else if (name == null || name == "" || email == "" || email == null || mobile == null || mobile == "" || address == null || address == ""
                        || selectedOption == null || selectedOption == "" || uni_name == null || uni_name == "") {
                    Toast.makeText(RegisterActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    setRegister();
                }
            }
        });

        tvCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tvCurrentLocation.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                if (checkLocationPermission()) {
                    // Request location updates
                    requestLocationUpdates();
                } else {
                    // Request location permissions
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
            }
        });
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationUpdates() {
        ProgressDialog progressDialogg = new ProgressDialog(this);
        this.progressDialog2 = progressDialogg;
        progressDialog2.setMessage("Searching Location.....");
        progressDialog2.show();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Use either NETWORK_PROVIDER or GPS_PROVIDER depending on your requirements
            String provider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?
                    LocationManager.NETWORK_PROVIDER :
                    LocationManager.GPS_PROVIDER;

            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Handle the received location data
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Convert latitude and longitude to address
                    Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
                    try {
                        progressDialog2.dismiss();
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String fullAddress = address.getAddressLine(0);
                            edtAddress.setText(fullAddress);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        progressDialog2.dismiss();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                    progressDialog2.dismiss();
                }
            }, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setRegister() {
        final Map<String, String> params = new HashMap<>();
        String para1="?user_mobile="+mobile;
        String para2="&user_name="+name;
        String para3="&user_email="+email;
        String para4="&user_address="+address;
        String para5="&dob="+dob;
        String para6="&parent_name="+parent;
        String para7="&refer_id="+refer;
        System.out.println("refer=="+refer);
        progressDialog.show();

        String baseUrl = ProductConfig.userregister+para1+para2+para3+para4+para5+para6+para7;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Bsession.getInstance().initialize(RegisterActivity.this,
                                jsonResponse.getString("id"),
                                jsonResponse.getString("user_name"),
                                jsonResponse.getString("user_mobile"),
                                jsonResponse.getString("user_address"),
                                jsonResponse.getString("user_email"),
                                parent, "","","", "","","",
                                "","","","");

                        AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(RegisterActivity.this);
                        BackAlertDialog.setTitle((CharSequence) "Register Alert");
                        BackAlertDialog.setMessage((CharSequence) "Registeration Completed Successfully");
                        BackAlertDialog.setIcon((int) R.drawable.logo1);
                        BackAlertDialog.setCancelable(false); // Prevent dialog from closing when clicking outside
                        BackAlertDialog.setNegativeButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogLayout = inflater.inflate(R.layout.mode, null);

                                RadioGroup radiogroup = dialogLayout.findViewById(R.id.radiogroup);
                                Button btn = dialogLayout.findViewById(R.id.spt_submit);

                                AlertDialog.Builder BackAlertDialog1 = new AlertDialog.Builder(RegisterActivity.this);
                                BackAlertDialog1.setView(dialogLayout);
                                BackAlertDialog1.setCancelable(false); // Prevent dialog from closing when clicking outside

                                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        // Check which RadioButton is checked
                                        if (checkedId == R.id.radioButtonOnline) {
                                            selectedOption = "Online";
                                            System.out.println("selected="+selectedOption);
                                        } else if (checkedId == R.id.radioButtonOffline) {
                                            selectedOption = "Offline";
                                            System.out.println("selected="+selectedOption);
                                        }
                                    }
                                });

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(RegisterActivity.this, CounsellingFormActivity.class);
                                        intent.putExtra("dob",dob);
                                        intent.putExtra("mode",selectedOption);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                BackAlertDialog1.show();
                            }

                        });
                        BackAlertDialog.show();
                        /*AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(RegisterActivity.this);
                        BackAlertDialog.setTitle((CharSequence) "Referral Alert");
                        BackAlertDialog.setMessage((CharSequence) "Refer minimum 10 friends(students) and get Rs 500 after the enrolment");
                        BackAlertDialog.setIcon((int) R.drawable.logo);
                        BackAlertDialog.setNegativeButton((CharSequence) "YES", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = "APP LINK : " + "https://play.google.com/store/apps/details?id=com.admission.educationapp"+ "\n" + "Sponser-ID = " + Bsession.getInstance().getUser_id(RegisterActivity.this);
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                            }
                        });
                        BackAlertDialog.show();*/

                    } else {
                        Toast.makeText(getApplicationContext(),jsonResponse.getString("text") , Toast.LENGTH_LONG).show();
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

    public void statusCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with location updates

        } else {
            // Request location permission from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable it to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        progressDialog.dismiss(); // Dismiss the progressDialog
        // or use Toast to display the location
    }

}