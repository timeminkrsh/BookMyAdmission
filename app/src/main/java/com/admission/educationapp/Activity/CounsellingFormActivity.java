package com.admission.educationapp.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admission.educationapp.Bsession;
import com.admission.educationapp.CollegeSelectionActivity;
import com.admission.educationapp.Model.CourseModel;
import com.admission.educationapp.Model.DistrictModel;
import com.admission.educationapp.ProductConfig;
import com.admission.educationapp.R;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CounsellingFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText edt_name,edt_phno,edt_emailaddress,edt_address,name_of_parent,et_date,tenthmark,leventhmark,twelthmark,
            physics_marks,chemistry_marks,maths_marks,csc_or_bio_marks,expected_institution;
    Button btn_update,btn_cutoff;
    EditText business_marks,economics_marks,accounts_marks,commerce_marks,bio_marks;
    RecyclerView courses_list;
    ProgressDialog progressDialog;
    String name,mobile,email,address,parent;
    String selectedIds,selecteddate,selectedOption,selectedFees,selectedMode;
    String mark_10th,mark_11th,mark_12th,phy_mark,che_mark,math_mark,csc_mark;
    String bio_mark,commerce_mark,account_mark,business_mark,economic_mark;
    String selectedValue="",selectedDistrictId="",selectedReligion="",selectedCommunity="";
    Spinner religionlist,higher_group,comunitylist;
    RadioGroup radiogroup;
    String cutOff="";
    TextView txt_cutoff,result_cutoff;
    LinearLayout ll_physics,ll_cutoff,ll_chemistry,ll_maths,ll_biology,ll_computer,ll_commerce,ll_account,ll_economics,ll_business;
    String [] religions ={"Hindu","Christian","Muslim","others"};
    String [] communties ={"GEN","EWS","OBC","SC","ST","PWD"};
    ArrayList<String> pincodelist = new ArrayList<String>();
    ArrayList<String> dict_id = new ArrayList<String>();
    List<CourseModel> courselist = new ArrayList<>();
    List<DistrictModel> district_list = new ArrayList<>();

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselling_form);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
        edt_name=findViewById(R.id.edt_name);
        physics_marks=findViewById(R.id.physics_marks);
        expected_institution=findViewById(R.id.expected_institution);
        maths_marks=findViewById(R.id.maths_marks);
        csc_or_bio_marks=findViewById(R.id.csc_or_bio_marks);
        chemistry_marks=findViewById(R.id.chemistry_marks);
        higher_group=findViewById(R.id.higher_group);
        edt_phno=findViewById(R.id.edt_phno);
        edt_emailaddress=findViewById(R.id.edt_emailaddress);
        edt_address=findViewById(R.id.edt_address);
        name_of_parent=findViewById(R.id.name_of_parent);
        comunitylist=findViewById(R.id.comunitylist);
        religionlist=findViewById(R.id.religionlist);
        et_date = findViewById(R.id.et_date);
        tenthmark=findViewById(R.id.tenthmark);
        leventhmark=findViewById(R.id.leventhmark);
        twelthmark=findViewById(R.id.twelthmark);
        btn_update=findViewById(R.id.btn_update);
        btn_cutoff=findViewById(R.id.btn_cutoff);
        radiogroup = findViewById(R.id.radiogroup);
        courses_list=findViewById(R.id.courses_list);
        ll_physics=findViewById(R.id.ll_physics);
        ll_chemistry=findViewById(R.id.ll_chemistry);
        ll_maths=findViewById(R.id.ll_maths);
        ll_biology=findViewById(R.id.ll_biology);
        ll_computer=findViewById(R.id.ll_computer);
        ll_commerce=findViewById(R.id.ll_commerce);
        ll_account=findViewById(R.id.ll_account);
        ll_economics=findViewById(R.id.ll_economics);
        ll_business=findViewById(R.id.ll_business);
        business_marks=findViewById(R.id.business_marks);
        economics_marks=findViewById(R.id.economics_marks);
        accounts_marks=findViewById(R.id.accounts_marks);
        commerce_marks=findViewById(R.id.commerce_marks);
        bio_marks=findViewById(R.id.bio_marks);
        txt_cutoff=findViewById(R.id.txt_cutoff);
        ll_cutoff=findViewById(R.id.ll_cutoff);
        result_cutoff=findViewById(R.id.result_cutoff);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        //coursesList();
        districtList();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            selecteddate = bundle.getString("dob");
            selectedMode = bundle.getString("mode");
            et_date.setText(selecteddate);
        }

        religionlist.setOnItemSelectedListener(CounsellingFormActivity.this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, religions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religionlist.setAdapter(adapter);

        religionlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReligion = religions[position];
                // Now you have the name of the selected item, you can use it as needed
                System.out.println("selected=="+selectedReligion);
                Log.d("SelectedItem", selectedReligion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        comunitylist.setOnItemSelectedListener(CounsellingFormActivity.this);
        ArrayAdapter<String> adapterr = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, communties);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comunitylist.setAdapter(adapterr);

        comunitylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCommunity = communties[position];
                // Now you have the name of the selected item, you can use it as needed
                System.out.println("selected=="+selectedCommunity);
                Log.d("SelectedItem", selectedCommunity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        edt_name.setText(Bsession.getInstance().getUser_name(CounsellingFormActivity.this));
        edt_phno.setText(Bsession.getInstance().getUser_mobile(CounsellingFormActivity.this));
        edt_address.setText( Bsession.getInstance().getUser_address(CounsellingFormActivity.this));
        edt_emailaddress.setText(Bsession.getInstance().getUser_email(CounsellingFormActivity.this));
        name_of_parent.setText(Bsession.getInstance().getParent_name(CounsellingFormActivity.this));

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt_name.getText().toString();
                mobile = edt_phno.getText().toString();
                address = edt_address.getText().toString();
                email = edt_emailaddress.getText().toString();
                selecteddate = et_date.getText().toString();
                parent = name_of_parent.getText().toString();
                mark_10th = tenthmark.getText().toString().trim();
                mark_11th = leventhmark.getText().toString().trim();
                mark_12th = twelthmark.getText().toString();
                phy_mark = physics_marks.getText().toString();
                che_mark = chemistry_marks.getText().toString();
                math_mark = maths_marks.getText().toString();
                csc_mark = csc_or_bio_marks.getText().toString();
                bio_mark = bio_marks.getText().toString();
                commerce_mark = commerce_marks.getText().toString();
                account_mark = accounts_marks.getText().toString();
                economic_mark = economics_marks.getText().toString();
                business_mark = business_marks.getText().toString();

                if (name.isEmpty()) {
                    edt_name.setError("*Enter your name");
                    edt_name.requestFocus();
                    return;
                }
                if (mobile.isEmpty()) {
                    edt_phno.setError("*Enter your mobile number");
                    edt_phno.requestFocus();
                } else if (mobile == null || mobile.length() < 6 || mobile.length() > 13) {
                    edt_phno.setError("*Enter Valid Mobile Number");
                    edt_phno.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    edt_emailaddress.setError("*Enter your email");
                    edt_emailaddress.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    edt_address.setError("*Enter your address");
                    edt_address.requestFocus();
                    return;
                }
                if (parent.isEmpty()) {
                    name_of_parent.setError("*Enter your parent name");
                    name_of_parent.requestFocus();
                }
                if (selecteddate.isEmpty()) {
                    et_date.setError("*Enter your D.O.B");
                    et_date.requestFocus();
                    return;
                }
                if (selectedOption == null || selectedOption.isEmpty()) {
                    Toast.makeText(CounsellingFormActivity.this, "Please select the gender", Toast.LENGTH_SHORT).show();
                    return;
                }  if (selectedReligion.isEmpty()) {
                    Toast.makeText(CounsellingFormActivity.this, "Please select religion", Toast.LENGTH_SHORT).show();
                    return;
                }if (selectedCommunity.isEmpty()) {
                    Toast.makeText(CounsellingFormActivity.this, "Please select community", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mark_10th.isEmpty()) {
                    tenthmark.setError("*Enter your 10th mark");
                    tenthmark.requestFocus();
                } else {
                    int markValue = Integer.parseInt(mark_10th);
                    if (markValue >= 501) {
                        tenthmark.setError("Enter valid 10th mark");
                        tenthmark.requestFocus();
                        return;
                    }
                }

                if (mark_11th.isEmpty()) {
                    leventhmark.setError("*Enter your 11th mark");
                    leventhmark.requestFocus();
                    return;
                } else {
                    int markValue = Integer.parseInt(mark_11th);
                    if (markValue >= 601) {
                        leventhmark.setError("Enter valid 11th mark");
                        leventhmark.requestFocus();
                        return;
                    }
                }
                if (mark_12th.isEmpty()) {
                    twelthmark.setError("*Enter your 12th mark");
                    twelthmark.requestFocus();
                    return;
                } else {
                    int mark12Value = Integer.parseInt(mark_12th);
                    if (mark12Value >= 601) {
                        twelthmark.setError("Enter valid 12th mark");
                        twelthmark.requestFocus();
                        return;
                    }
                }

                counsling();
            }
        });

        btn_cutoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cutoff.setVisibility(View.GONE);
                name = edt_name.getText().toString();
                mobile = edt_phno.getText().toString();
                address = edt_address.getText().toString();
                email = edt_emailaddress.getText().toString();
                selecteddate = et_date.getText().toString();
                parent = name_of_parent.getText().toString();
                mark_10th = tenthmark.getText().toString().trim();
                mark_11th = leventhmark.getText().toString().trim();
                mark_12th = twelthmark.getText().toString();
                phy_mark = physics_marks.getText().toString();
                che_mark = chemistry_marks.getText().toString();
                math_mark = maths_marks.getText().toString();
                csc_mark = csc_or_bio_marks.getText().toString();
                bio_mark = bio_marks.getText().toString();
                commerce_mark = commerce_marks.getText().toString();
                account_mark = accounts_marks.getText().toString();
                economic_mark = economics_marks.getText().toString();
                business_mark = business_marks.getText().toString();
                group1();
            }
        });

        //toolbar();
        RadioButton codRadioButton2 = findViewById(R.id.radio_male);
        codRadioButton2.setChecked(true);
        selectedOption = "Male";

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which RadioButton is checked
                if (checkedId == R.id.radio_male) {
                    selectedOption = "Male";
                } else if (checkedId == R.id.radio_female) {
                    selectedOption = "Female";
                }else if (checkedId == R.id.radio_others) {
                    selectedOption = "Others";
                }
            }
        });

        RadioButton codRadioButton = findViewById(R.id.radio_above2l);
        codRadioButton.setChecked(true);
        selectedFees = "Above 2L";

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which RadioButton is checked
                if (checkedId == R.id.radio_above2l) {
                    selectedFees = "Above 2L";
                } else if (checkedId == R.id.radio_above1_5l) {
                    selectedFees = "Above 1.5L";
                }else if (checkedId == R.id.radio_above1l) {
                    selectedFees = "Above 1L";
                }else if (checkedId == R.id.radio_below1l) {
                    selectedFees = "Below 1L";
                }
            }
        });


    }



    private void districtList() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.grouplist;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        district_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            DistrictModel model1 = new DistrictModel();
                            model1.setDis_id(jsonObject1.getString("id"));
                            model1.setDis_name(jsonObject1.getString("group"));

                            pincodelist.add(jsonObject1.getString("group"));
                            dict_id.add(jsonObject1.getString("id"));

                            district_list.add(model1);
                            System.out.println("drpdoown=="+district_list);

                            createDropdownList(pincodelist);
                            System.out.println("responee=="+response);
                        }

                    } else {
                        Toast.makeText(CounsellingFormActivity.this, "District not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CounsellingFormActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void createDropdownList(ArrayList<String> pincodelist) {
        higher_group = findViewById(R.id.higher_group);
        higher_group.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, pincodelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        higher_group.setAdapter(adapter);

        higher_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue = parent.getItemAtPosition(position).toString();
                selectedDistrictId = dict_id.get(position);
                // Do something with the selected value
                System.out.println("selectvv=="+selectedValue);
                System.out.println("Selected District ID: " + selectedDistrictId);

                if(selectedValue.equals("Physics/Chemistry/Comp.Science/Mathematics")){
                    ll_physics.setVisibility(View.VISIBLE);
                    ll_chemistry.setVisibility(View.VISIBLE);
                    ll_maths.setVisibility(View.VISIBLE);
                    ll_biology.setVisibility(View.GONE);
                    ll_commerce.setVisibility(View.GONE);
                    ll_business.setVisibility(View.GONE);
                    ll_account.setVisibility(View.GONE);
                    ll_economics.setVisibility(View.GONE);
                    ll_computer.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);

                    return;
                }else if (selectedValue.equals("Physics/Chemistry/Biology/Mathematics")){
                    ll_physics.setVisibility(View.VISIBLE);
                    ll_chemistry.setVisibility(View.VISIBLE);
                    ll_maths.setVisibility(View.GONE);
                    ll_biology.setVisibility(View.VISIBLE);
                    ll_commerce.setVisibility(View.GONE);
                    ll_business.setVisibility(View.GONE);
                    ll_account.setVisibility(View.GONE);
                    ll_economics.setVisibility(View.GONE);
                    ll_computer.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);


                } else if (selectedValue.equals("Physics/Chemistry/Botany/Zoology")){
                    ll_physics.setVisibility(View.VISIBLE);
                    ll_chemistry.setVisibility(View.VISIBLE);
                    ll_maths.setVisibility(View.GONE);
                    ll_biology.setVisibility(View.VISIBLE);
                    ll_commerce.setVisibility(View.GONE);
                    ll_business.setVisibility(View.GONE);
                    ll_account.setVisibility(View.GONE);
                    ll_economics.setVisibility(View.GONE);
                    ll_computer.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);


                }else if (selectedValue.equals("Computer Science/Econumbermics/Commerce/Accountancy")){
                    ll_physics.setVisibility(View.GONE);
                    ll_chemistry.setVisibility(View.GONE);
                    ll_maths.setVisibility(View.GONE);
                    ll_biology.setVisibility(View.GONE);
                    ll_commerce.setVisibility(View.VISIBLE);
                    ll_business.setVisibility(View.GONE);
                    ll_account.setVisibility(View.VISIBLE);
                    ll_economics.setVisibility(View.VISIBLE);
                    ll_computer.setVisibility(View.VISIBLE);
                    btn_cutoff.setVisibility(View.VISIBLE);


                }else if (selectedValue.equals("Econumbermics/Commerce/Accountancy/Busi.Maths")){
                    ll_physics.setVisibility(View.GONE);
                    ll_chemistry.setVisibility(View.GONE);
                    ll_maths.setVisibility(View.GONE);
                    ll_biology.setVisibility(View.GONE);
                    ll_commerce.setVisibility(View.VISIBLE);
                    ll_business.setVisibility(View.VISIBLE);
                    ll_account.setVisibility(View.VISIBLE);
                    ll_economics.setVisibility(View.VISIBLE);
                    ll_computer.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void group1() {
        ll_cutoff.setVisibility(View.VISIBLE);
        if(selectedValue.equals("Physics/Chemistry/Comp.Science/Mathematics")){
            if (phy_mark.isEmpty()) {
                physics_marks.setError("*Enter your physics mark");
                physics_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int phyMarkValue = Integer.parseInt(phy_mark);
                if (phyMarkValue < 0 || phyMarkValue > 101) {
                    physics_marks.setError("Enter valid physics mark (0-100)");
                    physics_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (che_mark.isEmpty()) {
                chemistry_marks.setError("*Enter your chemistry mark");
                chemistry_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int cheMarkValue = Integer.parseInt(che_mark);
                if (cheMarkValue < 0 || cheMarkValue > 101) {
                    chemistry_marks.setError("Enter valid chemistry mark (0-100)");
                    chemistry_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (math_mark.isEmpty()) {
                maths_marks.setError("*Enter your maths mark");
                maths_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(math_mark);
                if (mathMarkValue < 0 || mathMarkValue > 101) {
                    maths_marks.setError("Enter valid maths mark (0-100)");
                    maths_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }

        }else if (selectedValue.equals("Physics/Chemistry/Biology/Mathematics")){

            if (phy_mark.isEmpty()) {
                physics_marks.setError("*Enter your physics mark");
                physics_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int phyMarkValue = Integer.parseInt(phy_mark);
                if (phyMarkValue < 0 || phyMarkValue > 101) {
                    physics_marks.setError("Enter valid physics mark (0-100)");
                    physics_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (che_mark.isEmpty()) {
                chemistry_marks.setError("*Enter your chemistry mark");
                chemistry_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int cheMarkValue = Integer.parseInt(che_mark);
                if (cheMarkValue < 0 || cheMarkValue > 101) {
                    chemistry_marks.setError("Enter valid chemistry mark (0-100)");
                    chemistry_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (bio_mark.isEmpty()) {
                bio_marks.setError("*Enter your biology mark");
                bio_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(bio_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    bio_marks.setError("Enter valid biology mark (0-100)");
                    bio_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
        } else if (selectedValue.equals("Physics/Chemistry/Botany/Zoology")){

            if (phy_mark.isEmpty()) {
                physics_marks.setError("*Enter your physics mark");
                physics_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int phyMarkValue = Integer.parseInt(phy_mark);
                if (phyMarkValue < 0 || phyMarkValue > 101) {
                    physics_marks.setError("Enter valid physics mark (0-100)");
                    physics_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (che_mark.isEmpty()) {
                chemistry_marks.setError("*Enter your chemistry mark");
                chemistry_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int cheMarkValue = Integer.parseInt(che_mark);
                if (cheMarkValue < 0 || cheMarkValue > 101) {
                    chemistry_marks.setError("Enter valid chemistry mark (0-100)");
                    chemistry_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (bio_mark.isEmpty()) {
                bio_marks.setError("*Enter your biology mark");
                bio_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(bio_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    bio_marks.setError("Enter valid biology mark (0-100)");
                    bio_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }else if (selectedValue.equals("Computer Science/Econumbermics/Commerce/Accountancy")){
            if (economic_mark.isEmpty()) {
                economics_marks.setError("*Enter your economics mark");
                economics_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(economic_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    economics_marks.setError("Enter valid economics mark (0-100)");
                    economics_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (account_mark.isEmpty()) {
                accounts_marks.setError("*Enter your accounts mark");
                accounts_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(account_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    accounts_marks.setError("Enter valid accounts mark (0-100)");
                    accounts_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (commerce_mark.isEmpty()) {
                commerce_marks.setError("*Enter your commerce mark");
                commerce_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(commerce_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    commerce_marks.setError("Enter valid commerce mark (0-100)");
                    commerce_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (csc_mark.isEmpty()) {
                csc_or_bio_marks.setError("*Enter your computer science mark");
                csc_or_bio_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(csc_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    csc_or_bio_marks.setError("Enter valid  computer science mark (0-100)");
                    csc_or_bio_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }else if (selectedValue.equals("Econumbermics/Commerce/Accountancy/Busi.Maths")){
            if (economic_mark.isEmpty()) {
                economics_marks.setError("*Enter your economics mark");
                economics_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(economic_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    economics_marks.setError("Enter valid economics mark (0-100)");
                    economics_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (account_mark.isEmpty()) {
                accounts_marks.setError("*Enter your accounts mark");
                accounts_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(account_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    accounts_marks.setError("Enter valid accounts mark (0-100)");
                    accounts_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (business_mark.isEmpty()) {
                business_marks.setError("*Enter your business maths mark");
                business_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(business_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    business_marks.setError("Enter valid business maths mark (0-100)");
                    business_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (commerce_mark.isEmpty()) {
                commerce_marks.setError("*Enter your commerce mark");
                commerce_marks.requestFocus();
                ll_cutoff.setVisibility(View.GONE);
                btn_cutoff.setVisibility(View.VISIBLE);
                return;
            } else {
                int mathMarkValue = Integer.parseInt(commerce_mark);
                if (mathMarkValue < 0 || mathMarkValue > 100) {
                    commerce_marks.setError("Enter valid commerce mark (0-100)");
                    commerce_marks.requestFocus();
                    ll_cutoff.setVisibility(View.GONE);
                    btn_cutoff.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }
        if(selectedValue.equals("Physics/Chemistry/Comp.Science/Mathematics")){
            int phyMark = Integer.parseInt(phy_mark);
            int cheMark = Integer.parseInt(che_mark);
            int mathMark = Integer.parseInt(math_mark);
            float averagePhysicsChemistry = (phyMark + cheMark) / 2.0f;
            float result = averagePhysicsChemistry + mathMark;
            System.out.println("Result: " + result);
            cutOff = String.valueOf(result);
            txt_cutoff.setText("Engineering Cutoff");
            result_cutoff.setText(result +"/200");
        }else if (selectedValue.equals("Physics/Chemistry/Biology/Mathematics")){
            float b = (Integer.parseInt(phy_mark)+(Integer.parseInt(che_mark)))/2;
            float group_2 = b + Integer.parseInt(bio_mark);
            System.out.println("group11=="+group_2);
            cutOff = String.valueOf(group_2);
            txt_cutoff.setText("Medical Cutoff");
            result_cutoff.setText(group_2 +"/200");
        } else if(selectedValue.equals("Physics/Chemistry/Botany/Zoology")){
            float c = (Integer.parseInt(phy_mark)+(Integer.parseInt(che_mark)))/2;
            float group_3 = c + Integer.parseInt(bio_mark);
            System.out.println("group11=="+group_3);
            cutOff = String.valueOf(group_3);
            txt_cutoff.setText("Medical Cutoff");
            result_cutoff.setText(group_3 +"/200");
        } else if(selectedValue.equals("Computer Science/Econumbermics/Commerce/Accountancy")){
            int d = Integer.parseInt(commerce_mark)+Integer.parseInt(account_mark)+Integer.parseInt(csc_mark)+Integer.parseInt(economic_mark);
            System.out.println("group11=="+d);
            cutOff = String.valueOf(d);
            txt_cutoff.setText("Cutoff Marks");
            result_cutoff.setText(d +"/400");
        } else if(selectedValue.equals("Econumbermics/Commerce/Accountancy/Busi.Maths")){
            int e = Integer.parseInt(commerce_mark)+Integer.parseInt(account_mark)+Integer.parseInt(business_mark)+Integer.parseInt(economic_mark);
            System.out.println("group11=="+e);
            txt_cutoff.setText("Cutoff Marks");
            result_cutoff.setText(e +"/400");
            cutOff = String.valueOf(e);
        }

    }

    private void counsling() {
        final Map<String, String> params = new HashMap<>();
        String para1="?customer_id="+Bsession.getInstance().getUser_id(CounsellingFormActivity.this);
        String para2="&name="+name;
        String para3="&mobile="+mobile;
        String para4="&email="+email;
        String para5="&name_of_parent="+parent;
        String para6="&address="+address;
        String para7="&dob="+selecteddate;
        String para8="&gender="+selectedOption;
        String para9="&religion="+selectedReligion;
        String para10="&community="+selectedCommunity;
        String para11="&10th_marks="+mark_10th;
        String para12="&11th_marks="+mark_11th;
        String para13="&12th_marks="+mark_12th;
        String para14="&higher_sec_group="+selectedDistrictId;
        String para15="&physics_marks="+phy_mark;
        String para16="&chemistry_marks="+che_mark;
        String para17="&maths_marks="+math_mark;
        String para18="&csc_or_bio_marks="+csc_mark;
        String para19="&fees_pay="+selectedFees;
        String para20="&biology="+bio_mark;
        String para21="&accounts="+account_mark;
        String para22="&economics="+economic_mark;
        String para23="&commerce="+commerce_mark;
        String para24="&business_maths="+business_mark;
        String para25="&counseling_mode="+selectedMode;
        String para26="&cutoff_marks="+cutOff;

        String baseUrl = ProductConfig.counselling_form+para1+para2+para3+para4+para5+para6+para7
                +para8+para9+para10+para11+para12+para13+para14+para15+para16+para17+para18+para19+
                para20+para21+para22+para23+para24+para25+para26;
        System.out.println("vvvvv"+baseUrl);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Bsession.getInstance().initialize(CounsellingFormActivity.this,
                                Bsession.getInstance().getUser_id(CounsellingFormActivity.this),
                                Bsession.getInstance().getUser_name(CounsellingFormActivity.this),
                                Bsession.getInstance().getUser_mobile(CounsellingFormActivity.this),
                                Bsession.getInstance().getUser_address(CounsellingFormActivity.this),
                                Bsession.getInstance().getUser_email(CounsellingFormActivity.this),
                                Bsession.getInstance().getParent_name(CounsellingFormActivity.this),
                                Bsession.getInstance().getuniversity_id(CounsellingFormActivity.this),
                                selectedIds,selectedDistrictId,"","","",
                                "","","",mark_12th);

                        String last_id = jsonResponse.getString("last_id");

                        Intent intent = new Intent(CounsellingFormActivity.this, UniversitySelectionActivity.class);
                        intent.putExtra("lastId",last_id);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("Response=="+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
        private Context context;
        private List<CourseModel> courseList;
        private SparseBooleanArray selectedItems;
        private static final int MAX_SELECTIONS = 3;

        public CoursesAdapter(Context context, List<CourseModel> courseList) {
            this.context = context;
            this.courseList = courseList;
            this.selectedItems = new SparseBooleanArray();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final CourseModel currentItem = courseList.get(position);

            holder.courseCheck.setText(currentItem.getRadio());
            holder.courseCheck.setChecked(selectedItems.get(position, false));
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
                    selectedItems.put(holder.getAdapterPosition(), holder.courseCheck.isChecked());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox courseCheck;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                courseCheck = itemView.findViewById(R.id.course_check);
            }
        }

        public ArrayList<CourseModel> getSelectedItems() {
            ArrayList<CourseModel> selectedCourses = new ArrayList<>();
            for (int i = 0; i < courseList.size(); i++) {
                if (selectedItems.get(i, false)) {
                    selectedCourses.add(courseList.get(i));
                }
            }
            return selectedCourses;
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