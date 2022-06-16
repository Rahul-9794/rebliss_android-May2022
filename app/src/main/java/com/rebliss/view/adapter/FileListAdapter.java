package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.view.activity.myactivityadd.MyActivityEditFormActivity;

import java.util.List;

public class FileListAdapter extends  RecyclerView.Adapter<FileListAdapter.HomeViewHolder> {

    private Context context;
    private Integer filestatus;
    private List<com.rebliss.domain.model.file.File> photolistList;

    public FileListAdapter(Context context, List<com.rebliss.domain.model.file.File> photolistList,Integer filestatus) {
        this.context = context;
        this.photolistList = photolistList;
        this.filestatus=filestatus;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileListAdapter.HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.showimage_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        if(photolistList.get(position).getDelstatus()==0) {
            if (photolistList.get(position).getStatus() == 0) {
                holder.btnInside.setText(photolistList.get(position).getFilemane());
            } else {
                holder.btnInside.setText(photolistList.get(position).getFile().getName());
            }
        }else {
           holder.hello.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return photolistList.size();
    }

    @Override
    public int getItemCount() {
        return photolistList.size();
    }

    private class ViewHolder {

        private TextView textName;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{

        private TextView btnInside;
        private ImageView canclebutton;
        private RelativeLayout hello;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            btnInside = itemView.findViewById(R.id.btnInside);
            canclebutton = itemView.findViewById(R.id.img);
            hello=itemView.findViewById(R.id.hello);

            canclebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(photolistList.get(getAdapterPosition()).getStatus()==0) {
                        if (filestatus == 0) {
                            ((MyActivityEditFormActivity) context).insidesideimage.set(photolistList.get(getAdapterPosition()).getIndex(), 1);
                        } else if (filestatus == 1) {
                            ((MyActivityEditFormActivity) context).acivityimage.set(photolistList.get(getAdapterPosition()).getIndex(), 1);
                        } else if (filestatus == 2) {
                            ((MyActivityEditFormActivity) context).outsideimage.set(photolistList.get(getAdapterPosition()).getIndex(), 1);
                        }
                    }
                    photolistList.get(getAdapterPosition()).setDelstatus(1);
                    notifyDataSetChanged();
                 }
            });



        }
    }
}
