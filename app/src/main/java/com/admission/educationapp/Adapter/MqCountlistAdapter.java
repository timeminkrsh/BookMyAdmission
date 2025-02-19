package com.admission.educationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.educationapp.Activity.AcademicActivity;
import com.admission.educationapp.Activity.SeatRegisterActivity;
import com.admission.educationapp.Bsession;
import com.admission.educationapp.Model.CountlistModel;
import com.admission.educationapp.R;

import java.util.List;

public class MqCountlistAdapter extends RecyclerView.Adapter<MqCountlistAdapter.ViewHolder> {
    public Context context;
    public List<CountlistModel> papularModelList;
    String student_name,Quota;
    public MqCountlistAdapter(Context context, List<CountlistModel> countlist) {
        this.context = context;
        this.papularModelList = countlist;
    }

    @NonNull
    @Override
    public MqCountlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MqCountlistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CountlistModel model = papularModelList.get(position);
        student_name = papularModelList.get(position).getStudent_name();
        Quota = papularModelList.get(position).getQuota();
        int status = Integer.parseInt(model.getStatus());
        if (status == 1) {
            // Set background color to green
            holder.box.setBackgroundColor(ContextCompat.getColor(context, R.color.Green));
        } else if (status == 2) {
            // Set background color to red
            holder.box.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));
        } else {
            // Handle other status values or set a default color
            holder.box.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }
        holder.box.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (status == 1) {
                    System.out.println("quotaa=="+Quota);
                    holder.box.setBackgroundResource(R.drawable.border_blue);
                    Intent intent = new Intent(context, SeatRegisterActivity.class);
                    context.startActivity(intent);
                }else if (status == 2) {
                    PopupMenu popupMenu = new PopupMenu(context,view);
                    popupMenu.getMenu().add(Menu.NONE, Menu.NONE, 0, papularModelList.get(position).getStudent_name());
                    popupMenu.show();
                }
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
                        Bsession.getInstance().getFees_id(context),
                        Bsession.getInstance().getCollege_id(context),
                        Bsession.getInstance().getCq_count(context),
                        Bsession.getInstance().getMq_count(context),Quota,
                        Bsession.getInstance().getMark_12th(context) );
            }
        });
    }

    @Override
    public int getItemCount() {
        List<CountlistModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView box;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            box = itemView.findViewById(R.id.box);
        }
    }
}
