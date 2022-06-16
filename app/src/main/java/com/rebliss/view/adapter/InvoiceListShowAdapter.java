package com.rebliss.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.notificationlist.Desc;
import com.rebliss.domain.model.payment.InvoiceListResponse;
import com.rebliss.view.activity.notification.NotificationDetailActivity;
import com.rebliss.view.activity.notification.NotificationListActivity;

import java.util.List;

public class InvoiceListShowAdapter extends RecyclerView.Adapter<InvoiceListShowAdapter.HomeViewHolder> {

    private Context context;
    private List<InvoiceListResponse.Datum> invoiceList;


    public InvoiceListShowAdapter(Context context, List<InvoiceListResponse.Datum> myActivityList) {

        // this.upload_path = upload_path;
        this.context = context;
        this.invoiceList = myActivityList;

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,btnViewDetail,tvReason;

        public HomeViewHolder(View v) {
            super(v);

            tvName = v.findViewById(R.id.tvName);
            btnViewDetail = v.findViewById(R.id.btnViewDetail);
            btnViewDetail.setText("Download");

            btnViewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (invoiceList.get(getAdapterPosition()).getInvoiceUrl()!=null) {
                     //   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.kBaseURL_Download_Invoice + invoiceList.get(getAdapterPosition()).getInvoiceUrl()));
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.kBaseURL_Download_Invoice +invoiceList.get(getAdapterPosition()).getOrderId()));
                        context.startActivity(browserIntent);
                    }
                    else {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.kBaseURL_Download_Invoice +invoiceList.get(getAdapterPosition()).getOrderId()));
                        context.startActivity(browserIntent);

                    }
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

        InvoiceListResponse.Datum desc = invoiceList.get(position);

        holder.tvName.setText(desc.getOrderTitle());



    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public void removeAt(int position) {
        // images.remove(position);

//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
    }






}
