package com.rebliss.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.opportunitylist.Opportunity;
import com.rebliss.view.activity.OpportunityDetailsActivity;

import java.util.List;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.ViewHolder> {
    private Context context;
    private List<Opportunity> allGroupsList;

    public OpportunityAdapter(Context context, List<Opportunity> allGroupsList) {
        this.context = context;
        this.allGroupsList = allGroupsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_opportunity, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.textIndustryName.setText(allGroupsList.get(i).getOpportunityTittle());

    }

    @Override
    public int getItemCount() {
        return allGroupsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIndustryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textIndustryName = itemView.findViewById(R.id.tv_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDescDialog();
                }
            });
        }


        private void showDescDialog() {
            context.startActivity(new Intent(context, OpportunityDetailsActivity.class).putExtra(Constant.OPPORTUNITY_TITLE,
                    String.valueOf(allGroupsList.get(getAdapterPosition()).getOpportunityTittle())));
           /* final Dialog dialogIntern = new Dialog(context);
            dialogIntern.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogIntern.setContentView(R.layout.dialog_opportunity_detail);
            dialogIntern.setCancelable(true);
            dialogIntern.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogIntern.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dialogIntern.show();

            TextView tvTitle = dialogIntern.findViewById(R.id.tvSubject);
            TextView tvDescription = dialogIntern.findViewById(R.id.tvDescription);
            Button btnSubmit = dialogIntern.findViewById(R.id.btnSelect);
            Button btnCancel = dialogIntern.findViewById(R.id.btnCancel);

            tvTitle.setText(allGroupsList.get(getAdapterPosition()).getOpportunityTittle());
            tvDescription.setText(allGroupsList.get(getAdapterPosition()).getOpportunityDescription());

            btnSubmit.setOnClickListener(view -> {
                dialogIntern.dismiss();
                context.startActivity(new Intent(context, OpportunityDetailsActivity.class).putExtra(Constant.OPPORTUNITY_TITLE,
                        String.valueOf(allGroupsList.get(getAdapterPosition()).getOpportunityTittle())));

            });

            btnCancel.setOnClickListener(view -> {
                dialogIntern.dismiss();
            });*/
        }
    }

}
