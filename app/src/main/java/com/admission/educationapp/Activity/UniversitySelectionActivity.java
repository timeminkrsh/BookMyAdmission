package com.admission.educationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.admission.educationapp.Model.UniversityModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversitySelectionActivity extends AppCompatActivity {
    RecyclerView university_list;
    ProgressDialog progressDialog;
    List<UniversityModel> universitylist = new ArrayList<>();
    String uni_name = " ", uni_id = " ",last_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_selection);
        university_list = findViewById(R.id.university_list);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            last_id = bundle.getString("lastId");
            System.out.println("lastid==="+last_id);
        }

        universityList();
    }

    private void universityList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.universitylist ;
        universitylist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            UniversityModel model = new UniversityModel();
                            model.setUni_id(jsonlist.getJSONObject(j).getString("id"));
                            model.setUni_name(jsonlist.getJSONObject(j).getString("name"));
                            model.setUni_image(jsonlist.getJSONObject(j).getString("image"));
                            universitylist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(UniversitySelectionActivity.this, LinearLayoutManager.VERTICAL, false);
                        university_list.setLayoutManager(layoutManager);
                        UniversityAdapter packageListAdapter = new UniversityAdapter(UniversitySelectionActivity.this, universitylist);
                        university_list.setAdapter(packageListAdapter);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(UniversitySelectionActivity.this, 2);
                        university_list.setLayoutManager(gridLayoutManager);
                        university_list.setHasFixedSize(true);

                    } else {
                        Toast.makeText(UniversitySelectionActivity.this, "University list not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(UniversitySelectionActivity.this);
        requestQueue.add(jsObjRequest);

    }

    public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.ViewHolder> {
        private Context context;
        private List<UniversityModel> papularModelList;
       // private int selectedPosition = -1;

        public UniversityAdapter(Context context, List<UniversityModel> universitylist) {
            this.context = context;
            this.papularModelList = universitylist;
        }

        @NonNull
        @Override
        public UniversityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.universitylist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UniversityAdapter.ViewHolder holder, int position) {
            final UniversityModel model = papularModelList.get(position);

            holder.univ_name.setText(model.getUni_name());
            String img = papularModelList.get(position).getUni_image();
            Glide.with(context)
                    .load(img)
                    .placeholder(R.drawable.logo1)
                    .into(holder.univ_image);
            holder.card_annauniv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uni_name = model.getUni_name();
                    uni_id = model.getUni_id();
                    System.out.println("Selected University=== " + uni_name);
                    System.out.println("Selected University=== " + uni_id);
                    Intent intent = new Intent(UniversitySelectionActivity.this, CourseSelectionActivity.class);
                    intent.putExtra("uniId",uni_id);
                    intent.putExtra("lastId",last_id);
                    System.out.println("lastid==="+last_id);
                    startActivity(intent);
                     
                }
            });
        }

        @Override
        public int getItemCount() {
            return papularModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView univ_name;
            CardView card_annauniv;
            ImageView univ_image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                univ_name = itemView.findViewById(R.id.univ_name);
                univ_image = itemView.findViewById(R.id.univ_image);
                card_annauniv = itemView.findViewById(R.id.card_annauniv);
            }
        }
    }
}