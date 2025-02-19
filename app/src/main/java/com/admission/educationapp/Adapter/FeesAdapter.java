package com.admission.educationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.FeesModel;
import com.admission.educationapp.Activity.QuotaSeatActivity;
import com.admission.educationapp.R;

import java.util.List;

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.ViewHolder> {
    public Context context;
    public List<FeesModel> papularModelList;
    String fees_Id;

    public FeesAdapter(Context context, List<FeesModel> feeslist) {
        this.context = context;
        this.papularModelList = feeslist;
    }

    @NonNull
    @Override
    public FeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fees_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FeesModel model = papularModelList.get(position);

        holder.quota.setText(model.getQuota());
        holder.fee_fg.setText(model.getHostel_fees());
        holder.fee_mq.setText(model.getRoom_fees());
        holder.fee_general.setText(model.getTution_fees());
        holder.fee_pmss.setText(model.getPmss());
        holder.fee_department.setText(model.getId());
        holder.btn_viewseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuotaSeatActivity.class);
                fees_Id=papularModelList.get(position).getId();
                context.startActivity(intent);
                Bsession.getInstance().initialize(context,
                        Bsession.getInstance().getUser_id(context),
                        Bsession.getInstance().getUser_name(context),
                        Bsession.getInstance().getUser_mobile(context),
                        Bsession.getInstance().getUser_address(context),
                        Bsession.getInstance().getUser_email(context),
                        Bsession.getInstance().getParent_name(context),
                        Bsession.getInstance().getuniversity_id(context),
                        Bsession.getInstance().getcourse_id(context),
                        Bsession.getInstance().getdistrict_id(context),
                        Bsession.getInstance().getDept_id(context),
                        fees_Id,Bsession.getInstance().getCollege_id(context),
                        Bsession.getInstance().getCq_count(context),
                        Bsession.getInstance().getMq_count(context),"",
                        Bsession.getInstance().getMark_12th(context));
            }
        });
    }

    @Override
    public int getItemCount() {
        List<FeesModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fee_department,fee_pmss,fee_fg;
        TextView fee_general,fee_mq,quota;
        Button btn_viewseat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fee_department = itemView.findViewById(R.id.fee_department);
            fee_pmss = itemView.findViewById(R.id.fee_pmss);
            fee_fg = itemView.findViewById(R.id.fee_fg);
            fee_general = itemView.findViewById(R.id.fee_general);
            fee_mq = itemView.findViewById(R.id.fee_mq);
            quota = itemView.findViewById(R.id.quota);
            btn_viewseat = itemView.findViewById(R.id.btn_viewseat);

        }
    }
}
