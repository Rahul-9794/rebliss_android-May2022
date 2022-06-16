package com.rebliss.view.activity.cpdashboardforfos;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rebliss.R;


import java.util.ArrayList;
import java.util.List;

public class MyAcivityAdapter extends RecyclerView.Adapter<MyAcivityAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<AllGroup> allGroupsList;
    private List<AllGroup> filteredNameList;


    public MyAcivityAdapter(Context context, List<AllGroup> allGroupsList) {
        this.context = context;
        this.allGroupsList = allGroupsList;
        this.filteredNameList=allGroupsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_activity_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        AllGroup allGroup = filteredNameList.get(i);
        viewHolder.btnResubmit.setText("Id: "+allGroup.getActivityDetailId());
        viewHolder.tvName.setText("Activity "+(i+1));
        viewHolder.tvReason.setText("Status: "+allGroup.getStatus());

        if (allGroup.getStatus().equals("Resubmitted"))
        {
            viewHolder.tvReason.setText("Status: Pending");
        }


    }

    @Override
    public int getItemCount() {
        return allGroupsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredNameList = allGroupsList;
                } else {
                    List<AllGroup> filteredList = new ArrayList<>();
                    for (AllGroup name : allGroupsList) {
                        if (name.getMobile().toLowerCase().contains(charSequenceString.toLowerCase())
                                || name.getShopName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        filteredNameList = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredNameList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredNameList = (List<AllGroup>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    class ViewHolder extends RecyclerView.ViewHolder{
         private TextView tvName,btnResubmit,tvReason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnResubmit = itemView.findViewById(R.id.btnResubmit);
            tvReason = itemView.findViewById(R.id.tvReason);
            btnResubmit.setVisibility(View.VISIBLE);
            tvReason.setVisibility(View.VISIBLE);

        }
    }
}
