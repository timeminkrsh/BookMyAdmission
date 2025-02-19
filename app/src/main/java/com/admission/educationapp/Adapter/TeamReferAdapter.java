package com.admission.educationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Model.ReferModel;
import com.admission.educationapp.R;

import java.util.List;

public class TeamReferAdapter extends RecyclerView.Adapter<TeamReferAdapter.ViewHolder> {
    public Context context;
    public List<ReferModel> papularModelList;

    public TeamReferAdapter(Context context, List<ReferModel> teamreferlist) {
        this.context = context;
        this.papularModelList = teamreferlist;
    }

    @NonNull
    @Override
    public TeamReferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teamrefer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamReferAdapter.ViewHolder holder, int position) {
        ReferModel model = papularModelList.get(position);
        holder.refer_sno.setText(model.getId());
        holder.refer_name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        List<ReferModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView refer_sno,refer_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            refer_sno = itemView.findViewById(R.id.refer_sno);
            refer_name = itemView.findViewById(R.id.refer_name);
        }
    }
}
