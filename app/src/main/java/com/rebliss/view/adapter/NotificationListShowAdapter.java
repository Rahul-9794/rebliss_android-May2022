package com.rebliss.view.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.rebliss.R;
import com.rebliss.domain.model.notificationlist.Desc;
import com.rebliss.view.activity.notification.NotificationDetailActivity;
import com.rebliss.view.activity.notification.NotificationListActivity;

import java.util.List;

public class NotificationListShowAdapter extends RecyclerView.Adapter<NotificationListShowAdapter.HomeViewHolder> {

    private Context context;
    private List<Desc> myActivityList;


    public NotificationListShowAdapter(Context context, List<Desc> myActivityList) {

        // this.upload_path = upload_path;
        this.context = context;
        this.myActivityList = myActivityList;

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,btnViewDetail,tvReason;

        public HomeViewHolder(View v) {
            super(v);

            tvName = v.findViewById(R.id.tvName);
            btnViewDetail = v.findViewById(R.id.btnViewDetail);

            btnViewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, NotificationDetailActivity.class);
                    intent.putExtra("desc",myActivityList.get(getAdapterPosition()));
                    context.startActivity(intent);
                    ((NotificationListActivity)context).finish();
                }
            });


        }
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {

        Desc desc = myActivityList.get(position);
        holder.tvName.setText(desc.getPushMessage());

        if (desc.getReadStatus()==1)
            holder.tvName.setTextColor(context.getResources().getColor(R.color.loginButtonText));
        else holder.tvName.setTextColor(context.getResources().getColor(R.color.colorBlack));

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
