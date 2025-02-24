package com.admission.educationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admission.educationapp.Adapter.SuggestionAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Adapter.CollegeAdapter;
import com.admission.educationapp.Adapter.SocialmediaAdapter;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.BannerModel;
import com.admission.educationapp.Model.CollegeModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    ImageView menu;
    ProgressDialog progressDialog;
    RecyclerView college_list;
    LinearLayout home_layout, referal, about, counselling;
    TextView home_txt, referal_txt, about_txt, counselling_txt;
    Button district, course;
    public AppUpdateManager mAppUpdateManager;
    List<CollegeModel> collegelist = new ArrayList<>();
    public ArrayList<CollegeModel> collegelists = new ArrayList<>();
    DrawerLayout drawer;
    RecyclerView recycleimage;
    List<BannerModel> sliderlist = new ArrayList<>();
    private int currentPosition = 0;
    private Timer timer;
    AutoCompleteTextView search_txt;
    SuggestionAdapter suggestionAdapter;
    private final InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                Toast.makeText(HomeActivity.this, "UPDATE STATUS", Toast.LENGTH_LONG).show();
                showCompletedUpdate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        search_txt = findViewById(R.id.search_txt);
        search_txt.requestFocus();
        menu = findViewById(R.id.menu);
        drawer = findViewById(R.id.content);
        home_layout = findViewById(R.id.home_layout);
        counselling = findViewById(R.id.counselling);
        college_list = findViewById(R.id.college_list);
        about = findViewById(R.id.about);
        referal = findViewById(R.id.referal);
        course = findViewById(R.id.course);
        recycleimage = findViewById(R.id.recycleimage);
        home_txt = findViewById(R.id.home_txt);
        referal_txt = findViewById(R.id.referal_txt);
        about_txt = findViewById(R.id.about_txt);
        counselling_txt = findViewById(R.id.counselling_txt);

        navigationView = findViewById(R.id.nav_view);
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                // If the keyboard is visible, hide BottomNavigationView
                if (keypadHeight > screenHeight * 0.15) {
                    navigationView.setVisibility(View.GONE);
                } else {
                    navigationView.setVisibility(View.VISIBLE);
                }
            }
        });


        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        if (savedInstanceState != null) {
            AppUpdateManager create = AppUpdateManagerFactory.create(this);
            this.mAppUpdateManager = create;
            create.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @SuppressLint("WrongConstant")
                public void onSuccess(AppUpdateInfo result) {
                    if (result.updateAvailability() == 2 && result.isUpdateTypeAllowed(0)) {
                        try {
                            // Toast.makeText(MainActivity.this, "UPDATESTATUS1", 1).show();
                            mAppUpdateManager.startUpdateFlowForResult(result, 0, (Activity) HomeActivity.this, 100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            this.mAppUpdateManager.registerListener(this.installStateUpdatedListener);
        }
        AppUpdateManager create2 = AppUpdateManagerFactory.create(this);
        this.mAppUpdateManager = create2;
        create2.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @SuppressLint("WrongConstant")
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == 2 && result.isUpdateTypeAllowed(0)) {
                    try {
                        //  Toast.makeText(MainActivity.this, "UPDATESTATUS1", 1).show();
                        mAppUpdateManager.startUpdateFlowForResult(result, 0, (Activity) HomeActivity.this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //this.mAppUpdateManager.registerListener(this.installStateUpdatedListener);


        DrawerLayout drawer = findViewById(R.id.content);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bannerList();
        collegelist();
        loadserach();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        home_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about_txt.setVisibility(View.GONE);
                about.setBackgroundResource(R.color.white);
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        referal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referal_txt.setVisibility(View.VISIBLE);
                referal.setBackgroundResource(R.drawable.round);
                home_txt.setVisibility(View.GONE);
                home_layout.setBackgroundResource(R.color.white);
                about_txt.setVisibility(View.GONE);
                about.setBackgroundResource(R.color.white);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "APP LINK : " + "https://play.google.com/store/apps/details?id=com.admission.educationapp" + "\n" + "Refer-ID = " + Bsession.getInstance().getUser_id(HomeActivity.this);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_txt.setVisibility(View.GONE);
                home_layout.setBackgroundResource(R.color.white);
                about_txt.setVisibility(View.GONE);
                about.setBackgroundResource(R.color.white);
                counselling_txt.setVisibility(View.VISIBLE);
                counselling.setBackgroundResource(R.drawable.round);
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_txt.setVisibility(View.GONE);
                home_layout.setBackgroundResource(R.color.white);
                referal_txt.setVisibility(View.GONE);
                referal.setBackgroundResource(R.color.white);
                about_txt.setVisibility(View.VISIBLE);
                about.setBackgroundResource(R.drawable.round);
                Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CourseSelectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showCompletedUpdate() {
        @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(findViewById(16908290), (CharSequence) "New App is Ready", -2);
        snackbar.setAction((CharSequence) "Install", (View.OnClickListener) new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "UPDATE STATUS", 1).show();
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    public void onStop() {
        AppUpdateManager appUpdateManager = this.mAppUpdateManager;
        if (appUpdateManager != null) {
            //appUpdateManager.registerListener(this.installStateUpdatedListener);
        }
        super.onStop();
    }

    private void loadserach() {
        String userid = Bsession.getInstance().getUser_id(getApplicationContext());
        collegelists = new ArrayList<>();
        final Map<String, String> params = new HashMap<>();
        String para1 = "?university_id=" + Bsession.getInstance().getuniversity_id(this);
        String para2 = "&course_id=" + Bsession.getInstance().getcourse_id(this);
        String para3 = "&district_id=" + Bsession.getInstance().getdistrict_id(this);
        String baseUrl = ProductConfig.collegelist + para1 + para2 + para3;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                        JSONArray jsonArray = jsonResponse.getJSONArray("storeList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);

                            CollegeModel model = new CollegeModel();
                            model.setCol_id(jsonObject.getString("id"));
                            model.setUni_name(jsonObject.getString("university"));
                            model.setCol_name(jsonObject.getString("name"));
                            model.setCol_code(jsonObject.getString("code"));
                            model.setCol_address(jsonObject.getString("address"));
                            model.setCol_weburl(jsonObject.getString("web_url"));
                            model.setCol_description(jsonObject.getString("description"));
                            model.setCol_image(jsonObject.getString("image"));
                            collegelists.add(model);
                            suggestionAdapter = new SuggestionAdapter(HomeActivity.this, R.layout.suggestion_items, collegelists);
                            search_txt.setAdapter(suggestionAdapter);

                            search_txt.setThreshold(1);
                            search_txt.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            search_txt.setText(model.getCol_name());
                                            CollegeModel model = collegelists.get(position);
                                            search_txt.setText("");
                                            String suggestion_title = collegelists.get(position).getCol_id();
                                            String name = collegelists.get(position).getCol_name();
                                            String address = collegelists.get(position).getCol_address();
                                            String code = collegelists.get(position).getCol_code();
                                            String univer = collegelists.get(position).getUni_name();
                                            String des = collegelists.get(position).getCol_description();
                                            String weburl = collegelists.get(position).getCol_weburl();

                                            Intent intent = new Intent(HomeActivity.this, CollegeDetailActivity.class);
                                            intent.putExtra("id", suggestion_title);
                                            intent.putExtra("name", name);
                                            intent.putExtra("address", address);
                                            intent.putExtra("code", code);
                                            intent.putExtra("univer", univer);
                                            intent.putExtra("des", des);
                                            intent.putExtra("weburl", weburl);
                                            startActivity(intent);
                                        }
                                    });

                        }
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
                        progressDialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();

                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_LONG).show();

                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();

                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_LONG).show();

                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();

                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_LONG).show();

                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);
    }

    private void bannerList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.bannerlist;
        sliderlist = new ArrayList<>();
        System.out.println("base==" + baseUrl);
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
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycleimage.setLayoutManager(linearLayoutManager2);
                        SocialmediaAdapter bestSaleAdapter = new SocialmediaAdapter(HomeActivity.this, sliderlist);
                        recycleimage.setAdapter(bestSaleAdapter);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new AutoScrollTask(), 0, 2000);
                    } else {
                        Toast.makeText(HomeActivity.this, "Items records not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_home) {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
        } else if (id == R.id.nav_scholar) {
            intent = new Intent(getApplicationContext(), ScholarshipActivity.class);
        } else if (id == R.id.nav_referal) {
            intent = new Intent(getApplicationContext(), ReferralListActivity.class);
        } else if (id == R.id.nav_loan) {
            intent = new Intent(getApplicationContext(), EducationLoanActivity.class);
        } else if (id == R.id.nav_abroad) {
            intent = new Intent(getApplicationContext(), AbroadActivity.class);
        } else if (id == R.id.nav_wallet) {
            intent = new Intent(getApplicationContext(), MyWalletActivity.class);
        } else if (id == R.id.nav_withdraw) {
            intent = new Intent(getApplicationContext(), WithdrawRequestActivity.class);
        } else if (id == R.id.nav_product) {
            intent = new Intent(getApplicationContext(), ProductlistActivity.class);
        } else if (id == R.id.nav_productbook) {
            intent = new Intent(getApplicationContext(), ProductBookingListActivity.class);
        } else if (id == R.id.nav_bookings) {
            intent = new Intent(getApplicationContext(), MyBookingsActivity.class);
        } else if (id == R.id.nav_privacy) {
            intent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
        } else if (id == R.id.nav_help) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
        }else if (id == R.id.nav_support) {
            intent = new Intent(getApplicationContext(), HelpAndSupportActivity.class);
        } else if (id == R.id.nav_logout) {
            logoutAlert();
        }
        if (intent != null) {
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void collegelist() {
        final Map<String, String> params = new HashMap<>();
        String para1 = "?college_id=" + Bsession.getInstance().getCollege_id(this);
        System.out.println("selectedIds==" + para1);
        String baseUrl = ProductConfig.collegelist_selected + para1 ;
        System.out.println("selectedIds==" + baseUrl);
        collegelist = new ArrayList<>();
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
                            CollegeModel model = new CollegeModel();
                            model.setCol_id(jsonlist.getJSONObject(j).getString("id"));
                            model.setUni_name(jsonlist.getJSONObject(j).getString("university"));
                            model.setCol_name(jsonlist.getJSONObject(j).getString("name"));
                            model.setCol_code(jsonlist.getJSONObject(j).getString("code"));
                            model.setCol_address(jsonlist.getJSONObject(j).getString("address"));
                            model.setCol_weburl(jsonlist.getJSONObject(j).getString("web_url"));
                            model.setCol_imag(jsonlist.getJSONObject(j).getString("web_url"));
                            model.setCol_description(jsonlist.getJSONObject(j).getString("description"));
                            model.setCol_image(jsonlist.getJSONObject(j).getString("image"));
                            collegelist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                        college_list.setLayoutManager(layoutManager);
                        CollegeAdapter packageListAdapter = new CollegeAdapter(HomeActivity.this, collegelist);
                        college_list.setAdapter(packageListAdapter);
                        college_list.setHasFixedSize(true);
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
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(jsObjRequest);

    }

    private void logoutAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage(getApplicationContext().getResources().getString(R.string.alert_want_logout))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.alert_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                logout();
                            }
                        })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.alert_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.content);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
            return;
        }
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

    private void logout() {
        Bsession.getInstance().destroy(HomeActivity.this);
        Toast.makeText(HomeActivity.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

}