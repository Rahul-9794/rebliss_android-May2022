package com.rebliss.view.activity.cpdashboardforfos;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rebliss.R;

import java.util.List;

public class SelectFosAdapter extends RecyclerView.Adapter<SelectFosAdapter.ViewHolder> {
    private Context context;
    private List<com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.AllGroup> allGroupsList;
    private Dialog dialog;

    public SelectFosAdapter(Dialog dialog, Context context, List<com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.AllGroup> allGroupsList) {
        this.context = context;
        this.allGroupsList = allGroupsList;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.acivityadapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.textIndustryName.setText(allGroupsList.get(i).getFirstName());
        viewHolder.tvId.setText("Id: "+allGroupsList.get(i).getId());

    }

    @Override
    public int getItemCount() {
        return allGroupsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIndustryName, tvId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textIndustryName = itemView.findViewById(R.id.tv_text);
            tvId = itemView.findViewById(R.id.tv_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CpAcivity) context).fosId = allGroupsList.get(getAdapterPosition()).getId();
                    ((CpAcivity) context).getacivityliastlist();
                    if (allGroupsList.get(getAdapterPosition()).getId() == 0)
                        ((CpAcivity) context).btnSelectFos.setText("Select FOS: All");
                    else
                        ((CpAcivity) context).btnSelectFos.setText("Select FOS: " + allGroupsList.get(getAdapterPosition()).getFirstName());
                    dialog.dismiss();
                }
            });
        }
    }
}
