package com.admission.educationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Model.TransactionModel;
import com.admission.educationapp.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    public Context context;
    public List<TransactionModel> papularModelList;
    public TransactionAdapter(Context context, List<TransactionModel> transactionlist) {
        this.context = context;
        this.papularModelList = transactionlist;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        TransactionModel model = papularModelList.get(position);
        holder.sno.setText(model.getSno());
        holder.date.setText(model.getDate());
        holder.balance.setText(model.getBalance());
        if (model.getTeam_refer().equals("0") && model.getWithdraw_amount().equals("0") && model.getOrder_price().equals("0")) {
            // All conditions are true, set the text as required
            holder.action.setText("Direct Price: " + model.getDirect_refer());
        } else {
        }
        if (model.getDirect_refer().equals("0") && model.getWithdraw_amount().equals("0") && model.getOrder_price().equals("0")) {
            // All conditions are true, set the text as required
            holder.action.setText("Team Price: " + model.getTeam_refer());
        } else {
        }
        if (model.getTeam_refer().equals("0") && model.getDirect_refer().equals("0") && model.getOrder_price().equals("0")) {
            // All conditions are true, set the text as required
            holder.action.setText("Withdraw Amount: " + model.getWithdraw_amount());
        } else {
        }
        if (model.getTeam_refer().equals("0") && model.getWithdraw_amount().equals("0") && model.getDirect_refer().equals("0")) {
            // All conditions are true, set the text as required
            holder.action.setText("Order Price: " + model.getOrder_price());
        } else {
        }
    }

    @Override
    public int getItemCount() {
        List<TransactionModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,action;
        TextView balance,sno;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            action = itemView.findViewById(R.id.action);
            balance = itemView.findViewById(R.id.balance);
            sno = itemView.findViewById(R.id.sno);
        }
    }
}
