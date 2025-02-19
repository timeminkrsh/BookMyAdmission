package com.admission.educationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admission.educationapp.Adapter.DepartNamesAdapter;
import com.admission.educationapp.CollegeSelectionActivity;
import com.admission.educationapp.Model.DepartModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.CourseModel;
import com.admission.educationapp.Model.DistrictModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseSelectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner packages;
    Button course_submit;
    ProgressDialog progressDialog;
    RecyclerView course_list,select_college;
    String cours_id="",last_id;
    LinearLayout lyouts;
    ImageView univ_logo;
    String selectedValue="",selectedDistrictId="";
    ArrayList<String> pincodelist = new ArrayList<String>();
    ArrayList<String> dict_id = new ArrayList<String>();
    List<CourseModel> courselist = new ArrayList<>();
    List<DepartModel> depart_names = new ArrayList<>();
    List<DistrictModel> district_list = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);

        packages = findViewById(R.id.packages);
        course_list = findViewById(R.id.course_list);
        lyouts = findViewById(R.id.lyouts);
        univ_logo = findViewById(R.id.univ_logo);
        lyouts.setVisibility(View.GONE);
        select_college = findViewById(R.id.select_college);
        course_submit = findViewById(R.id.course_submit);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cours_id = bundle.getString("uniId");
            System.out.println("cours_id=="+cours_id);
            last_id = bundle.getString("lastId");
            courseList();
        }
        toolbar();

    }

    private void courseList() {
        final Map<String, String> params = new HashMap<>();
        String para1 = "?university_id="+cours_id;
        String baseUrl = ProductConfig.courselist+para1;
        courselist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        String img = jsonResponse.getString("image");
                        Glide.with(getApplicationContext())
                                .load(img)
                                .placeholder(R.drawable.logo1)
                                .into(univ_logo);
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            CourseModel model = new CourseModel();

                            model.setId(jsonlist.getJSONObject(j).getString("course_id"));
                            model.setRadio(jsonlist.getJSONObject(j).getString("name"));
                            courselist.add(model);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(CourseSelectionActivity.this, LinearLayoutManager.VERTICAL, false);
                        course_list.setLayoutManager(layoutManager);
                        CourseAdapter packageListAdapter = new CourseAdapter(CourseSelectionActivity.this, courselist);
                        course_list.setAdapter(packageListAdapter);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CourseSelectionActivity.this, 2);
                        course_list.setLayoutManager(gridLayoutManager);
                        course_list.setHasFixedSize(true);

                    } else {
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
        RequestQueue requestQueue = Volley.newRequestQueue(CourseSelectionActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void districtList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.districtlist;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        district_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            DistrictModel model1 = new DistrictModel();
                            model1.setDis_id(jsonObject1.getString("id"));
                            model1.setDis_name(jsonObject1.getString("name"));

                            pincodelist.add(jsonObject1.getString("name"));
                            dict_id.add(jsonObject1.getString("id"));

                            district_list.add(model1);
                            System.out.println("drpdoown=="+district_list);

                            createDropdownList(pincodelist);
                            System.out.println("responee=="+response);
                        }

                    } else {
                        Toast.makeText(CourseSelectionActivity.this, "District not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CourseSelectionActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void createDropdownList(ArrayList<String> pincodelist) {
        packages = findViewById(R.id.packages);
        packages.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, pincodelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packages.setAdapter(adapter);

        packages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue = parent.getItemAtPosition(position).toString();
                selectedDistrictId = dict_id.get(position);
                // Do something with the selected value
                System.out.println("selectvv=="+selectedValue);
                System.out.println("Selected District ID: " + selectedDistrictId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void checkss() {
        final Map<String, String> params = new HashMap<>();
        String para1="?user_id=" + Bsession.getInstance().getUser_id(this);
        String para2="&university_id=" + cours_id;
        String para3="&distic=" + selectedDistrictId;
        progressDialog.show();

        String baseUrl = ProductConfig.course_selection+para1+para2+para3;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getApplicationContext(), "Detail submitted successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CourseSelectionActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CourseSelectionActivity.this);
        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
        public Context context;
        public List<CourseModel> papularModelList;
        private int selectedPosition = -1;
        private List<String> pincode_list;
        private List<String> dict_id;
        String courseId;

        public CourseAdapter(Context context, List<CourseModel> courselist) {
            this.context = context;
            this.papularModelList = courselist;
            this.pincode_list = new ArrayList<>();
            this.dict_id = new ArrayList<>();
            departList();
        }

        private void departList() {
            final Map<String, String> params = new HashMap<>();
            String para1 = "?course_id="+courseId;
            String baseUrl = ProductConfig.departlist+para1;
            System.out.println("baseurldepart===="+baseUrl);
            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                            district_list = new ArrayList<>();
                            JSONArray jsonlist = jsonResponse.getJSONArray("list");
                            for (int j = 0; j < jsonlist.length(); j++) {
                                JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                                DistrictModel model1 = new DistrictModel();
                                model1.setDis_id(jsonObject1.getString("id"));
                                model1.setDis_name(jsonObject1.getString("name"));

                                pincode_list.add(jsonObject1.getString("name"));
                                dict_id.add(jsonObject1.getString("id"));

                                district_list.add(model1);
                                System.out.println("drpdoown=="+district_list);

                                //createDropdown_List(pincode_list);
                                System.out.println("responee=="+response);
                            }

                        } else {
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
            RequestQueue requestQueue = Volley.newRequestQueue(CourseSelectionActivity.this);
            requestQueue.add(jsObjRequest);
        }

        @NonNull
        @Override
        public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("MissingInflatedId")
        @Override
        public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            final CourseModel currentItem = papularModelList.get(position);

            holder.univ_name.setText(currentItem.getRadio());
            courseId = papularModelList.get(position).getId();
            System.out.println("course_id"+courseId);
            final List<DepartModel>[] departlist = new List[]{new ArrayList<>()};
            holder.depart_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    courseId = papularModelList.get(position).getId();
                    System.out.println("course_id=="+courseId);
                    LayoutInflater inflater = getLayoutInflater();

                    final String[] selectedIds = new String[1];
                    View dialogLayout = inflater.inflate(R.layout.depart_list, null);
                    RecyclerView departs_list = dialogLayout.findViewById(R.id.departs_list);
                    Button btn_update = dialogLayout.findViewById(R.id.btn_update);

                    AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(CourseSelectionActivity.this);
                    BackAlertDialog.setView(dialogLayout);
                    final AlertDialog alertDialog = BackAlertDialog.create();

                    final Map<String, String> params = new HashMap<>();
                    String para1="?course_id=" + courseId;
                    String baseUrl = ProductConfig.departlist + para1;
                    departlist[0] = new ArrayList<>();
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
                                    if (jsonlist != null && jsonlist.length() > 0) {
                                        for (int j = 0; j < jsonlist.length(); j++) {
                                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                                            DepartModel model = new DepartModel();
                                            model.setId(jsonlist.getJSONObject(j).getString("id"));
                                            model.setCollege_id(jsonlist.getJSONObject(j).getString("college_id"));
                                            model.setName(jsonlist.getJSONObject(j).getString("name"));
                                            departlist[0].add(model);
                                        }
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(CourseSelectionActivity.this, LinearLayoutManager.VERTICAL, false);
                                        departs_list.setLayoutManager(layoutManager);
                                        DepartAdapter packageListAdapter = new DepartAdapter(CourseSelectionActivity.this, departlist[0]);
                                        departs_list.setAdapter(packageListAdapter);
                                        departs_list.setHasFixedSize(true);

                                        btn_update.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                selectedIds[0] = packageListAdapter.getSelectedCourseIds();
                                                // Use the selectedIds as needed (e.g., send to server)
                                                Log.d("Selected Course IDs", selectedIds[0]);
                                                System.out.println("selectedIds==" + selectedIds[0]);
                                                checkCollege(selectedIds[0]);
                                                districtList();
                                                lyouts.setVisibility(View.VISIBLE);
                                                alertDialog.dismiss();
                                            }
                                        });
                                    }else{
                                        alertDialog.cancel();
                                        Toast.makeText(CourseSelectionActivity.this, "Course list not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //Toast.makeText(CourseSelectionActivity.this, "College list not found", Toast.LENGTH_SHORT).show();
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
                    alertDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(CourseSelectionActivity.this);
                    requestQueue.add(jsObjRequest);
                }

                class DepartAdapter extends RecyclerView.Adapter<DepartAdapter.ViewHolder> {
                    private Context context;
                    private List<DepartModel> collegelist;
                    private SparseBooleanArray selected_Items;
                    private static final int MAX_SELECTIONS = 3;

                    public DepartAdapter(Context context, List<DepartModel> departModels) {
                        this.context = context;
                        this.collegelist = departModels;
                        this.selected_Items = new SparseBooleanArray();
                    }

                    @NonNull
                    @Override
                    public DepartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_list, parent, false);
                        return new ViewHolder(view);
                    }

                    @Override
                    public void onBindViewHolder(@NonNull DepartAdapter.ViewHolder holder, int position) {
                        final DepartModel currentItems = collegelist.get(position);
                        holder.courseCheck.setText(currentItems.getName());
                        holder.courseCheck.setChecked(selected_Items.get(position, false));
                        holder.courseCheck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (holder.courseCheck.isChecked()) {
                                    if (getSelectedItemCount() >= MAX_SELECTIONS) {
                                        holder.courseCheck.setChecked(false);
                                        Toast.makeText(context, "You can select only 3 courses", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                selected_Items.put(holder.getAdapterPosition(),holder.courseCheck.isChecked());
                                notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return collegelist.size();
                    }

                    public String getSelectedCourseIds() {
                        StringBuilder selectedIds = new StringBuilder();
                        int count = 0;
                        for (int i = 0; i < collegelist.size(); i++) {
                            if (selected_Items.get(i, false)) {
                                if (count > 0) {
                                    selectedIds.append(",");
                                }
                                selectedIds.append(collegelist.get(i).getId());
                                count++;
                            }
                        }
                        return selectedIds.toString();
                    }

                    class ViewHolder extends RecyclerView.ViewHolder {
                        CheckBox courseCheck;
                        public ViewHolder(@NonNull View itemView) {
                            super(itemView);
                            courseCheck = itemView.findViewById(R.id.course_check);
                        }
                    }

                    private int getSelectedItemCount() {
                        int count = 0;
                        for (int i = 0; i < selected_Items.size(); i++) {
                            if (selected_Items.valueAt(i)) {
                                count++;
                            }
                        }
                        return count;
                    }
                }
            });

           /* holder.text_radio.setText(currentItem.getRadio());
            holder.text_radio.setChecked(position == selectedPosition);
            holder.text_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int previousSelectedPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(previousSelectedPosition);
                    notifyItemChanged(selectedPosition);

                    cours_id = currentItem.getId();
                    System.out.println("Selected University=== " + cours_id);
                }
            });*/
        }

        @Override
        public int getItemCount() {
             return papularModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView univ_name;
            LinearLayout depart_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                univ_name = itemView.findViewById(R.id.univ_name);
                depart_list = itemView.findViewById(R.id.depart_list);
            }
        }

    }

    private void checkCollege(String selectedId) {
        final Map<String, String> params = new HashMap<>();
        String para1 = "?department_id=" + selectedId;
        String baseUrl = ProductConfig.department_names + para1 ;
        depart_names = new ArrayList<>();
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
                            DepartModel model = new DepartModel();

                            model.setId(jsonlist.getJSONObject(j).getString("S.no"));
                            model.setCollege_id(jsonlist.getJSONObject(j).getString("id"));
                            model.setName(jsonlist.getJSONObject(j).getString("name"));
                            depart_names.add(model);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(CourseSelectionActivity.this, LinearLayoutManager.VERTICAL, false);
                        select_college.setLayoutManager(layoutManager);
                        DepartNamesAdapter packageListAdapter = new DepartNamesAdapter(CourseSelectionActivity.this, depart_names);
                        select_college.setAdapter(packageListAdapter);
                        select_college.setHasFixedSize(true);

                        course_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                course_submit.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                                if (selectedValue.isEmpty()){
                                    Toast.makeText(CourseSelectionActivity.this, "District is not selected", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Bsession.getInstance().initialize(CourseSelectionActivity.this,
                                        Bsession.getInstance().getUser_id(CourseSelectionActivity.this),
                                        Bsession.getInstance().getUser_name(CourseSelectionActivity.this),
                                        Bsession.getInstance().getUser_mobile(CourseSelectionActivity.this),
                                        Bsession.getInstance().getUser_address(CourseSelectionActivity.this),
                                        Bsession.getInstance().getUser_email(CourseSelectionActivity.this),
                                        Bsession.getInstance().getParent_name(CourseSelectionActivity.this),
                                        cours_id, selectedId,selectedDistrictId,"","","",
                                        "","","",
                                        Bsession.getInstance().getMark_12th(CourseSelectionActivity.this));
                                Intent intent = new Intent(CourseSelectionActivity.this,CollegeSelectionActivity.class);
                                intent.putExtra("lastId",last_id);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(CourseSelectionActivity.this, "Course list not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(CourseSelectionActivity.this);
        requestQueue.add(jsObjRequest);

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

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UniversitySelectionActivity.class));
                finish();
            }
        });
        toolbar.setTitleTextColor(-1);
    }

}