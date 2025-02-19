package com.admission.educationapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.admission.educationapp.Model.CollegeModel;
import com.admission.educationapp.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends ArrayAdapter<CollegeModel> {
    private Context context;
    private int resourceId;
    private List<CollegeModel> items, tempItems, suggestions;

    public SuggestionAdapter(@NonNull Context context, int resourceId, ArrayList<CollegeModel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            final CollegeModel fruit = getItem(position);
            TextView pname = (TextView) view.findViewById(R.id.title);
            pname.setText(fruit.getCol_name());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public CollegeModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CollegeModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tempItems);
            } else {
                if( constraint.length() >1) {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (CollegeModel item : tempItems) {
                        if (item.getCol_name() .toLowerCase().startsWith(filterPattern)) {
                            System.out.println("item.getWeb_title().toLowerCase().contains(filterPattern)"+item.getCol_name().toLowerCase().contains(filterPattern));
                            filteredList.add(item);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

