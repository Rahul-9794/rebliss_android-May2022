package com.rebliss.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.HomeViewHolder>{

    private Typeface nunito_regular;
    private MySingleton mySingleton;
    private int rowLayout;
    private Context context;

    int type;
    View view;
    String[] images;


    public  class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgRemoveAdhar;
        private TextView textAdharFileName;
        private LinearLayout lnAdharView;

        public HomeViewHolder(View v) {
            super(v);

            textAdharFileName = v.findViewById(R.id.textAdharFileName);
            imgRemoveAdhar = v.findViewById(R.id.imgRemoveAdhar);
            lnAdharView = v.findViewById(R.id.lnAdharView);

        }
    }

    public TestAdapter(String[] images, int rowLayout, Context context) {
        this.images = images;
        this.rowLayout = rowLayout;
        // this.upload_path = upload_path;
        this.context = context;

    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {
        mySingleton = new MySingleton(context);

            holder.textAdharFileName.setText(images[position]);
            holder.textAdharFileName.setTypeface(App.LATO_REGULAR);

        holder.imgRemoveAdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i("uuu", position + "");
               // images[position] = null;
                String[] temp = new String[images.length-1];
                for(int i=0; i<images.length; i++){
                    if(i < position){
                        temp[i] = images[i];
                    } else if(i > position){
                        temp[i-1] = images[i];
                    }
                }
                images = temp;
                 notifyDataSetChanged();

            }
        });




    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public void removeAt(int position) {
       // images.remove(position);

//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
    }

}
