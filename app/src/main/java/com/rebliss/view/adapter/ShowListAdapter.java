package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;

import java.io.File;
import java.util.List;

public class ShowListAdapter extends  RecyclerView.Adapter<ShowListAdapter.HomeViewHolder> {

    private Context context;
    private List<File> photolistList;

    public ShowListAdapter(Context context, List<File> photolistList) {
        this.context = context;
        this.photolistList = photolistList;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShowListAdapter.HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.showimage_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        if(photolistList.get(position)!=null) {
            holder.btnInside.setText(photolistList.get(position).getName());
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
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            btnInside = itemView.findViewById(R.id.btnInside);
            canclebutton = itemView.findViewById(R.id.img);

            canclebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ((MyActivityFormActivity)context).outsideimage.remove(getAdapterPosition());
                    photolistList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                 }
            });



        }
    }
}
