package com.admission.educationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.admission.educationapp.Activity.CollegeDetailActivity;
import com.admission.educationapp.Csession;
import com.admission.educationapp.Model.CollegeModel;
import com.admission.educationapp.R;

import java.util.List;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.ViewHolder> {
    public Context context;
    public List<CollegeModel> papularModelList;
    String col_Id;
    String uni_Name,col_Name,col_Code,col_Address;
    String col_Weburl,col_Description;

    public CollegeAdapter( Context context, List<CollegeModel> collegelist) {
        this.context = context;
        this.papularModelList = collegelist;
    }

    @NonNull
    @Override
    public CollegeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CollegeModel model = papularModelList.get(position);
        String img = papularModelList.get(position).getCol_image();
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.logo1)
                .into(holder.col_logo);
        holder.cl_uniname.setText(model.getUni_name());
        holder.cl_clname.setText(model.getCol_name());
        holder.cl_code.setText(model.getCol_code());
        holder.cl_address.setText(model.getCol_address());
        holder.col_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CollegeDetailActivity.class);
                col_Id  = papularModelList.get(position).getCol_id();
                uni_Name = papularModelList.get(position).getUni_name();
                col_Name = papularModelList.get(position).getCol_name();
                col_Code = papularModelList.get(position).getCol_code();
                col_Address =papularModelList.get(position).getCol_address();
                col_Weburl =papularModelList.get(position).getCol_weburl();
                col_Description =papularModelList.get(position).getCol_description();
                context.startActivity(intent);
                /*Bsession.getInstance().initialize(context,
                        Bsession.getInstance().getUser_id(context),
                        Bsession.getInstance().getUser_name(context),
                        Bsession.getInstance().getUser_mobile(context),
                        Bsession.getInstance().getUser_address(context),
                        Bsession.getInstance().getUser_email(context),
                        Bsession.getInstance().getuniversity_id(context),
                        Bsession.getInstance().getcourse_id(context),
                        Bsession.getInstance().getdistrict_id(context),
                        "","",col_Id,"","","");*/
                Csession.getInstance().initialize(context,uni_Name,col_Name,
                        col_Code,col_Address,col_Weburl,col_Description,"",col_Id);
            }
        });
    }

    @Override
    public int getItemCount() {
        List<CollegeModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cl_uniname,cl_clname;
        TextView cl_code,cl_address;
        ImageView col_logo;
        LinearLayout col_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cl_uniname     = itemView.findViewById(R.id.cl_uniname);
            cl_clname     = itemView.findViewById(R.id.cl_clname);
            col_logo     = itemView.findViewById(R.id.col_logo);
            cl_code     = itemView.findViewById(R.id.cl_code);
            col_detail     = itemView.findViewById(R.id.col_detail);
            cl_address     = itemView.findViewById(R.id.cl_address);
        }
    }
}
