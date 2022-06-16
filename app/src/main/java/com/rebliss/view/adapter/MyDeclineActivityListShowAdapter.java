package com.rebliss.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.domain.model.myactivity.AllGroup;
import com.rebliss.view.activity.myactivityadd.MyActivityEditFormActivity;

import java.util.List;

public class MyDeclineActivityListShowAdapter extends RecyclerView.Adapter<MyDeclineActivityListShowAdapter.HomeViewHolder> {

    private Context context;
    private List<AllGroup> myActivityList;


    public MyDeclineActivityListShowAdapter(Context context, List<AllGroup> myActivityList) {

        // this.upload_path = upload_path;
        this.context = context;
        this.myActivityList = myActivityList;

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, btnResubmit, tvReason, tvId;

        public HomeViewHolder(View v) {
            super(v);

            tvName = v.findViewById(R.id.tvName);
            btnResubmit = v.findViewById(R.id.btnResubmit);
            tvReason = v.findViewById(R.id.tvReason);
            tvId = v.findViewById(R.id.tvId);

            btnResubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, MyActivityEditFormActivity.class);
                    intent.putExtra("id", myActivityList.get(getAdapterPosition()).getActivityDetailId());
                    context.startActivity(intent);
                }
            });

        }
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {

        holder.tvName.setText("Activity " + (position + 1));
        if (myActivityList.get(position).getActivityDetailId() != null) {
            holder.tvId.setText("Id: " + myActivityList.get(position).getActivityDetailId());
        }
        if (myActivityList.get(position).getReasonToDecline() != null) {
            holder.tvReason.setText("Reason: " + myActivityList.get(position).getReasonToDecline());
        }

    }

    @Override
    public int getItemCount() {
        return myActivityList.size();
    }

    public void removeAt(int position) {
        // images.remove(position);

//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
    }

}
