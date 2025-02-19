package com.admission.educationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Model.ProductBookingModel;
import com.admission.educationapp.R;

import java.util.List;

public class ProductBookingAdapter extends RecyclerView.Adapter<ProductBookingAdapter.ViewHolder> {
    public Context context;
    public List<ProductBookingModel> papularModelList;
    public ProductBookingAdapter(Context context, List<ProductBookingModel> bookinglist) {
        this.context = context;
        this.papularModelList = bookinglist;
    }

    @NonNull
    @Override
    public ProductBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_bookinglist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductBookingAdapter.ViewHolder holder, int position) {
        ProductBookingModel model = papularModelList.get(position);
        holder.product_name.setText(model.getProd_name());
        holder.product_price.setText(model.getProd_price());
        holder.shop_name.setText(model.getShop_name());
        holder.shop_address.setText(model.getShop_address());
    }

    @Override
    public int getItemCount() {
        List<ProductBookingModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name,product_price,shop_name,shop_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            shop_name = itemView.findViewById(R.id.shop_name);
            shop_address = itemView.findViewById(R.id.shop_address);
        }
    }
}
