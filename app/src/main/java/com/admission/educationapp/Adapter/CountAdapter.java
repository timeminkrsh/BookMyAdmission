package com.admission.educationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Activity.FeesDetailActivity;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.CountModel;
import com.admission.educationapp.R;

import java.util.List;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder>{
    public Context context;
    public List<CountModel> papularModelList;
    String dept_Id;
    String cq_Count,mq_Count;
    public CountAdapter(Context context, List<CountModel> countlist) {
        this.context = context;
        this.papularModelList = countlist;
    }

    @NonNull
    @Override
    public CountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intake_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CountModel model = papularModelList.get(position);

        holder.department.setText(model.getDepartment());
        holder.tot_count.setText(model.getCount());
        holder.cq_count.setText(model.getCq_count());
        holder.mq_count.setText(model.getMq_count());
        holder.seat_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.seat_view.setBackgroundResource(R.drawable.round);
                Intent intent = new Intent(context, FeesDetailActivity.class);
                dept_Id  = papularModelList.get(position).getId();
                cq_Count = papularModelList.get(position).getCq_count();
                mq_Count = papularModelList.get(position).getMq_count();
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
                        dept_Id,"",
                        Bsession.getInstance().getCollege_id(context),cq_Count,mq_Count,"",
                        Bsession.getInstance().getMark_12th(context));
            }
        });
    }

    @Override
    public int getItemCount() {
        List<CountModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView department,tot_count;
        TextView cq_count,mq_count;
        ImageView seat_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            department     = itemView.findViewById(R.id.department);
            tot_count     = itemView.findViewById(R.id.tot_count);
            cq_count     = itemView.findViewById(R.id.cq_count);
            mq_count     = itemView.findViewById(R.id.mq_count);
            seat_view     = itemView.findViewById(R.id.seat_view);
        }
    }
}
