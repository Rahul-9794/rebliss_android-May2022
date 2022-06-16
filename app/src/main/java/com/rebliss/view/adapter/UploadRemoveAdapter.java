package com.rebliss.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.view.activity.DashboardDocUpload;
import com.rebliss.view.activity.DashboardDocUploadEdit;
import com.rebliss.view.activity.DashboardFOSDocUpload;
import com.rebliss.view.activity.DashboardFOSDocUploadEdit;


public class UploadRemoveAdapter extends RecyclerView.Adapter<UploadRemoveAdapter.HomeViewHolder> {

    private Typeface nunito_regular;
    private MySingleton mySingleton;
    private int rowLayout;
    private Context context;

    String type;
    View view;
    String[] images;


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgRemoveAdhar;
        private TextView textAdharFileName;
        private LinearLayout lnAdharView;

        public HomeViewHolder(View v) {
            super(v);
            if (v != null) {
                textAdharFileName = v.findViewById(R.id.textAdharFileName);
                imgRemoveAdhar = v.findViewById(R.id.imgRemoveAdhar);
                lnAdharView = v.findViewById(R.id.lnAdharView);

            }
        }
    }

    public UploadRemoveAdapter(String[] images, int rowLayout, Context context, String type) {
        this.images = images;
        this.rowLayout = rowLayout;
        // this.upload_path = upload_path;
        this.context = context;
          this.type = type;

    }


    public void updateData(String[] images) {
        // items.clear();
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {

            view = LayoutInflater.from(context).inflate(R.layout.doc_upload_remove, parent, false);
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

        //  nunito_regular = Typeface.createFromAsset(context.getAssets(), Constant.Roboto_Regular);
        //App.imageLoader.displayImage(upload_path+Adhar_images[position], holder.imageProfile,App.defaultOptions);

        holder.imgRemoveAdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("uuu", position + "");
                // images[position] = null;
                String[] temp = new String[images.length - 1];
                for (int i = 0; i < images.length; i++) {
                    if (i < position) {
                        temp[i] = images[i];
                    } else if (i > position) {
                        temp[i - 1] = images[i];
                    }
                }
                if (context instanceof DashboardFOSDocUpload) {
                    ((DashboardFOSDocUpload) context).deleteUpload(type, images[position]);
                } else if (context instanceof DashboardDocUpload) {
                    ((DashboardDocUpload) context).deleteUpload(type, images[position]);
                }

                if (context instanceof DashboardFOSDocUploadEdit) {
                    ((DashboardFOSDocUploadEdit) context).deleteUpload(type, images[position]);
                } else if (context instanceof DashboardDocUploadEdit) {
                    ((DashboardDocUploadEdit) context).deleteUpload(type, images[position]);
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
