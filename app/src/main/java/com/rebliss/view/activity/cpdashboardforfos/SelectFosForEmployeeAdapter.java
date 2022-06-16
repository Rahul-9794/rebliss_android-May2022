package com.rebliss.view.activity.cpdashboardforfos;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.view.activity.employee.EmployeeAcivity;

import java.util.List;

public class SelectFosForEmployeeAdapter extends RecyclerView.Adapter<SelectFosForEmployeeAdapter.ViewHolder> {
    private Context context;
    private List<com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.AllGroup> allGroupsList;
    private Dialog dialog;

    public SelectFosForEmployeeAdapter(Dialog dialog, Context context, List<com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.AllGroup> allGroupsList) {
        this.context = context;
        this.allGroupsList = allGroupsList;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_activity_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tvId.setText("Id: " + allGroupsList.get(i).getId());
        viewHolder.textIndustryName.setText(allGroupsList.get(i).getFirstName());

    }

    @Override
    public int getItemCount() {
        return allGroupsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIndustryName, tvId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textIndustryName = itemView.findViewById(R.id.tvName);
            tvId = itemView.findViewById(R.id.btnResubmit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((EmployeeAcivity) context).fosid = allGroupsList.get(getAdapterPosition()).getId();
                    ((EmployeeAcivity) context).getdataWithselectedDate();
                    if (allGroupsList.get(getAdapterPosition()).getId() == 0)
                        ((EmployeeAcivity) context).selectfos.setText("Select FOS: All");
                    else
                        ((EmployeeAcivity) context).selectfos.setText("Select FOS: " + allGroupsList.get(getAdapterPosition()).getFirstName());
                    dialog.dismiss();
                }
            });
        }
    }
}
