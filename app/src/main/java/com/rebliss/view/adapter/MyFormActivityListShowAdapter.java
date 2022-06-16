package com.rebliss.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rebliss.R;
import com.rebliss.domain.model.ActivitySelectModel;

import java.util.List;

public class MyFormActivityListShowAdapter extends RecyclerView.Adapter<MyFormActivityListShowAdapter.HomeViewHolder> {

    private Context context;
    private List<ActivitySelectModel> myActivityList;
    private int isEdit = 0;


    public MyFormActivityListShowAdapter(Context context, List<ActivitySelectModel> myActivityList, int isEdit) {

        // this.upload_path = upload_path;
        this.context = context;
        this.myActivityList = myActivityList;
        this.isEdit = isEdit;

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, btnRemove;

        public HomeViewHolder(View v) {
            super(v);

            tvName = v.findViewById(R.id.tvName);
            btnRemove = v.findViewById(R.id.btnRemove);
            if (isEdit == 1)
                btnRemove.setVisibility(View.GONE);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    myActivityList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                }
            });

        }
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {

        if (myActivityList.get(position).getSubCategory1() != null)
            holder.tvName.setText("Activity " + (position + 1));

        if (position == 0) {
            holder.btnRemove.setVisibility(View.GONE);
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
