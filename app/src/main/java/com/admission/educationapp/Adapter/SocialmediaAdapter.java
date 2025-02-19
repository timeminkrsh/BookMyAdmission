package com.admission.educationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.admission.educationapp.Model.BannerModel;
import com.admission.educationapp.R;

import java.util.List;

public class SocialmediaAdapter extends RecyclerView.Adapter<SocialmediaAdapter.ViewHolder> {
    private List<BannerModel> itemList;
    private Context mContext;
    public SocialmediaAdapter(Context context, List<BannerModel> sliderlist) {
        this.mContext = context;
        this.itemList = sliderlist;
    }

    @NonNull
    @Override
    public SocialmediaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.social, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialmediaAdapter.ViewHolder holder, int position) {
        BannerModel model = itemList.get(position);
        String imgs = itemList.get(position).getBannerimage();
        Glide.with(mContext)
                .load(imgs)
                .placeholder(R.drawable.logo1)
                .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
        }
    }
}
