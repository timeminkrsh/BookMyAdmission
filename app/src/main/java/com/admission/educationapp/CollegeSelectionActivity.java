package com.admission.educationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.admission.educationapp.Activity.CourseSelectionActivity;
import com.admission.educationapp.Activity.HomeActivity;
import com.admission.educationapp.Adapter.CollegeAdapter;
import com.admission.educationapp.Model.CollegeModel;
import com.admission.educationapp.Model.CourseModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollegeSelectionActivity extends AppCompatActivity {
    RecyclerView colleges_list;
    Button btn_update;
    String selectedIds,last_id;
    ProgressDialog progressDialog;
    List<CollegeModel> collegelist = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_selection);

        colleges_list = findViewById(R.id.colleges_list);
        btn_update = findViewById(R.id.btn_update);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            last_id = bundle.getString("lastId");
            System.out.println("lastid==="+last_id);
        }
        collegelist();

    }

    private void collegelist(){
        final Map<String, String> params = new HashMap<>();
        String para2="?department_id=" +Bsession.getInstance().getcourse_id(this);
        String para3="&district_id=" + Bsession.getInstance().getdistrict_id(this);
        String para4="&university_id=" + Bsession.getInstance().getuniversity_id(this);
        String baseUrl = ProductConfig.collegelist + para2 +para3 +para4 ;
        collegelist = new ArrayList<>();
        progressDialog.show();
        System.out.println("lastid===-"+baseUrl);
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
                            model.setCol_description(jsonlist.getJSONObject(j).getString("description"));
                            model.setCol_image(jsonlist.getJSONObject(j).getString("image"));
                            collegelist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(CollegeSelectionActivity.this, LinearLayoutManager.VERTICAL, false);
                        colleges_list.setLayoutManager(layoutManager);
                        CollegesAdapter packageListAdapter = new CollegesAdapter(CollegeSelectionActivity.this, collegelist);
                        colleges_list.setAdapter(packageListAdapter);
                        colleges_list.setHasFixedSize(true);
                        btn_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectedIds = packageListAdapter.getSelectedCourseIds();
                                // Use the selectedIds as needed (e.g., send to server)
                                Log.d("Selected Course IDs", selectedIds);
                                System.out.println("selectedIds==" + selectedIds);
                                insertCollege(selectedIds);
                                checkCollege(selectedIds);

                            }
                        });

                    } else {
                        Toast.makeText(CollegeSelectionActivity.this, "College list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeSelectionActivity.this);
        requestQueue.add(jsObjRequest);

    }

    private void insertCollege(String selectedIds) {

        final  Map<String,String> para=new HashMap<>();
        String para2="?last_id=" + last_id;
        String para1="&course_id="+Bsession.getInstance().getcourse_id(getApplicationContext());
        String para3="&college_id=" + selectedIds;

        progressDialog.show();

        String baseUrl = ProductConfig.counselling_update+para2+para1+para3;
        System.out.println("vvvvv===-"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("message") && jsonResponse.getString("message").equals("Successfully")) {
                    } else {
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
                return para;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeSelectionActivity.this);
        requestQueue.add(jsObjRequest);

    }

    private void checkCollege(String selectedIds) {
        final Map<String, String> params = new HashMap<>();
        String para2="?last_id=" + last_id;
        String para3="&college_id1=" + selectedIds;
        progressDialog.show();

        String baseUrl = ProductConfig.update_counselling+para2+para3;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Bsession.getInstance().initialize(CollegeSelectionActivity.this,
                                Bsession.getInstance().getUser_id(CollegeSelectionActivity.this),
                                Bsession.getInstance().getUser_name(CollegeSelectionActivity.this),
                                Bsession.getInstance().getUser_mobile(CollegeSelectionActivity.this),
                                Bsession.getInstance().getUser_address(CollegeSelectionActivity.this),
                                Bsession.getInstance().getUser_email(CollegeSelectionActivity.this),
                                Bsession.getInstance().getParent_name(CollegeSelectionActivity.this),
                                Bsession.getInstance().getuniversity_id(CollegeSelectionActivity.this),
                                Bsession.getInstance().getcourse_id(CollegeSelectionActivity.this),
                                Bsession.getInstance().getdistrict_id(CollegeSelectionActivity.this),
                                "","",selectedIds, "","","",
                                Bsession.getInstance().getMark_12th(CollegeSelectionActivity.this));
                        System.out.println("selectedIds=="+Bsession.getInstance().getMark_12th(CollegeSelectionActivity.this));
                        Toast.makeText(getApplicationContext(), "Detail submitted successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CollegeSelectionActivity.this, HomeActivity.class);
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
        RequestQueue requestQueue = Volley.newRequestQueue(CollegeSelectionActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private class CollegesAdapter extends RecyclerView.Adapter<CollegesAdapter.Viewholder>{
        private Context context;
        private List<CollegeModel> collegelist;
        private SparseBooleanArray selectedItems;
        private static final int MAX_SELECTIONS = 5;
        public CollegesAdapter(Context context, List<CollegeModel> collegelist) {
            this.context = context;
            this.collegelist = collegelist;
            this.selectedItems = new SparseBooleanArray();
        }

        @NonNull
        @Override
        public CollegesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_list, parent, false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CollegesAdapter.Viewholder holder, int position) {
            final CollegeModel currentItem = collegelist.get(position);
            holder.courseCheck.setText(currentItem.getCol_name());
            holder.courseCheck.setChecked(selectedItems.get(position, false));
            holder.courseCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.courseCheck.isChecked()) {
                        if (getSelectedItemCount() >= MAX_SELECTIONS) {
                            holder.courseCheck.setChecked(false);
                            Toast.makeText(context, "You can select only 5 college", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    selectedItems.put(holder.getAdapterPosition(), holder.courseCheck.isChecked());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return collegelist.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            CheckBox courseCheck;
            public Viewholder(@NonNull View itemView) {
                super(itemView);
                courseCheck = itemView.findViewById(R.id.course_check);
            }
        }

        public ArrayList<CollegeModel> getSelectedItems() {
            ArrayList<CollegeModel> selectedCourses = new ArrayList<>();
            for (int i = 0; i < collegelist.size(); i++) {
                if (selectedItems.get(i, false)) {
                    selectedCourses.add(collegelist.get(i));
                }
            }
            return selectedCourses;
        }

        public String getSelectedCourseIds() {
            StringBuilder selectedIds = new StringBuilder();
            int count = 0;
            for (int i = 0; i < collegelist.size(); i++) {
                if (selectedItems.get(i, false)) {
                    if (count > 0) {
                        selectedIds.append(",");
                    }
                    selectedIds.append(collegelist.get(i).getCol_id());
                    count++;
                }
            }
            return selectedIds.toString();
        }

        private int getSelectedItemCount() {
            int count = 0;
            for (int i = 0; i < selectedItems.size(); i++) {
                if (selectedItems.valueAt(i)) {
                    count++;
                }
            }
            return count;
        }
    }
}