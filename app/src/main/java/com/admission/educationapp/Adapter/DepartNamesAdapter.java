package com.admission.educationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Activity.CourseSelectionActivity;
import com.admission.educationapp.Model.CollegeModel;
import com.admission.educationapp.Model.DepartModel;
import com.admission.educationapp.R;

import java.util.List;

public class DepartNamesAdapter extends RecyclerView.Adapter<DepartNamesAdapter.ViewHolder> {
    public Context context;
    public List<DepartModel> papularModelList;
    public DepartNamesAdapter(Context context, List<DepartModel> depart_names) {
        this.context = context;
        this.papularModelList = depart_names;
    }

    @NonNull
    @Override
    public DepartNamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartNamesAdapter.ViewHolder holder, int position) {
        final DepartModel model = papularModelList.get(position);
        holder.sno.setText(model.getId());
        holder.names.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return papularModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sno,names;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            names = itemView.findViewById(R.id.names);
        }
    }
}
