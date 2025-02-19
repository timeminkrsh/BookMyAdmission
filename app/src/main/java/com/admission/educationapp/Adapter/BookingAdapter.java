package com.admission.educationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.BookingModel;
import com.admission.educationapp.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    public Context context;
    public List<BookingModel> papularModelList;

    public BookingAdapter(Context context, List<BookingModel> bookinglist) {
        this.context = context;
        this.papularModelList = bookinglist;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        BookingModel model = papularModelList.get(position);
        holder.book_uniname.setText(model.getBook_uniname());
        holder.book_clname.setText(model.getBook_clgname());
        holder.book_course.setText(model.getBook_course());
        holder.book_quota.setText(Bsession.getInstance().getQuota(context));
        holder.book_status.setText(model.getBook_status());
    }

    @Override
    public int getItemCount() {
        List<BookingModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_uniname,book_clname,book_course;
        TextView book_quota,book_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            book_uniname    = itemView.findViewById(R.id.book_uniname);
            book_clname     = itemView.findViewById(R.id.book_clname);
            book_course     = itemView.findViewById(R.id.book_course);
            book_quota      = itemView.findViewById(R.id.book_quota);
            book_status     = itemView.findViewById(R.id.book_status);
        }
    }
}
