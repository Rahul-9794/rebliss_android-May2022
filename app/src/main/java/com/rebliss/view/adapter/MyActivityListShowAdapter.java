package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.domain.model.myactivity.AllGroup;
import com.rebliss.onclickinterfaces.RecyclerViewClickInterface;

import java.util.ArrayList;
import java.util.List;

public class MyActivityListShowAdapter extends RecyclerView.Adapter<MyActivityListShowAdapter.HomeViewHolder> implements Filterable {

    private Context context;
    private List<AllGroup> myActivityList;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    private List<AllGroup> filteredNameList;


    public MyActivityListShowAdapter(Context context, List<AllGroup> myActivityList, RecyclerViewClickInterface recyclerViewClickInterface) {

        // this.upload_path = upload_path;
        this.context = context;
        this.myActivityList = myActivityList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.filteredNameList = myActivityList;
    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, btnResubmit, tvReason;
        private TextView tvShopName, tvPhoneNo,tvCategory;
        private CheckBox cbAcceptOrder;
        private TextView tvOrderAccepted;

        public HomeViewHolder(View v) {
            super(v);

            tvName = v.findViewById(R.id.tvName);
            tvCategory = v.findViewById(R.id.tvCategory);
            btnResubmit = v.findViewById(R.id.btnResubmit);
            tvReason = v.findViewById(R.id.tvReason);
            btnResubmit.setVisibility(View.VISIBLE);
            tvReason.setVisibility(View.VISIBLE);

            tvShopName = v.findViewById(R.id.tvShopName);
            tvPhoneNo = v.findViewById(R.id.tvPhoneNo);

            cbAcceptOrder = v.findViewById(R.id.cbAcceptOrder);
            tvOrderAccepted = v.findViewById(R.id.tvOrderAccepted);

            //  cbAcceptOrder.setOnClickListener(this);
            cbAcceptOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewClickInterface != null)
                        recyclerViewClickInterface.onItemClick(getAdapterPosition());
                    // Is the view now checked?
                    boolean checked = ((CheckBox) view).isChecked();
                    if (checked) {
                        Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
                        Toast.makeText(context, "unchecked", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_activity_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {

        //  AllGroup allGroup = myActivityList.get(position);
        AllGroup allGroup = filteredNameList.get(position);


        holder.btnResubmit.setText("Id: " + allGroup.getActivityDetailId());
        holder.tvName.setText("Activity " + (position + 1));

        if (allGroup.getStatus() != null) {
            holder.tvReason.setText("Status: " + allGroup.getStatus());
        }
if(allGroup.getSubCategory1Id()==59)
{
    holder.tvCategory.setText("JIO Mart");
}
if(allGroup.getSubCategory1Id()==52)
{
    holder.tvCategory.setText("Airtel");
}
        if (allGroup.getActivity_status() == null) {
            holder.tvShopName.setText("Bussiness Name: " + allGroup.getShopName());
        } else {
            holder.tvShopName.setText("Shop Name: " + allGroup.getShopName());
        }
        holder.tvPhoneNo.setText("Phone No: " + allGroup.getMobile());

        if (allGroup.getStatus().contains("Pending")) {

                if (allGroup.getActivity_status() != null) {
                    if (allGroup.getActivity_status().contains("1")) {
                        holder.tvOrderAccepted.setVisibility(View.VISIBLE);
                        holder.cbAcceptOrder.setVisibility(View.GONE);
                    } else {
                        if (allGroup.getSubCategory1Id() == 59) {
                            holder.tvOrderAccepted.setVisibility(View.GONE);
                            holder.cbAcceptOrder.setVisibility(View.VISIBLE);
                        }
                        else
                            {
                                holder.cbAcceptOrder.setVisibility(View.GONE);
                            }
                    }
                } else {
                    holder.cbAcceptOrder.setVisibility(View.GONE);
                }
                // holder.cbAcceptOrder.setVisibility(View.VISIBLE);
            }
        else {
            holder.cbAcceptOrder.setVisibility(View.GONE);
        }

    }

    /*    @Override
        public int getItemCount() {
            return myActivityList.size();
        }*/
    @Override
    public int getItemCount() {
        return filteredNameList.size();
    }

    public void removeAt(int position) {
        // images.remove(position);

//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
    }


    //get filter activity with respect to mobile number and shop name
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredNameList = myActivityList;
                } else {
                    List<AllGroup> filteredList = new ArrayList<>();
                    for (AllGroup name : myActivityList) {
                        if (name.getMobile().toLowerCase().contains(charSequenceString.toLowerCase())
                                || name.getShopName().toLowerCase().contains(charSequenceString.toLowerCase())
                                || String.valueOf(name.getActivityDetailId()).toLowerCase().contains(charSequenceString.toLowerCase())) {
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
                recyclerViewClickInterface.filterItemCount(filteredNameList.size());
                notifyDataSetChanged();
            }
        };
    }
}