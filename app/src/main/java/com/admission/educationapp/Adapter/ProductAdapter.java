package com.admission.educationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.admission.educationapp.Activity.ProductDetailActivity;
import com.admission.educationapp.Model.ProductModel;
import com.admission.educationapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public Context context;
    public List<ProductModel> papularModelList;
    public ProductAdapter(Context context, List<ProductModel> productlist) {
        this.context = context;
        this.papularModelList = productlist;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ProductModel model = papularModelList.get(position);
        String img = papularModelList.get(position).getImage();
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.logo1)
                .into(holder.product_img);
        holder.product_name.setText(model.getName());
        holder.price.setText(" ₹ "+model.getOffer_price());
        holder.mrp.setText("MRP ₹ "+model.getNon_offer_price());
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.product_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product_id",papularModelList.get(position).getId());
                intent.putExtra("name",papularModelList.get(position).getName());
                intent.putExtra("offer",papularModelList.get(position).getOffer_price());
                intent.putExtra("non_offer",papularModelList.get(position).getNon_offer_price());
                intent.putExtra("product_desc",papularModelList.get(position).getDescription());
                intent.putExtra("shop_name",papularModelList.get(position).getShop_name());
                intent.putExtra("shop_address",papularModelList.get(position).getShop_address());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        List<ProductModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_name,price,mrp;
        LinearLayout product_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_img     = itemView.findViewById(R.id.product_img);
            product_name     = itemView.findViewById(R.id.product_name);
            product_view     = itemView.findViewById(R.id.product_view);
            price     = itemView.findViewById(R.id.price);
            mrp     = itemView.findViewById(R.id.mrp);
        }
    }
}
