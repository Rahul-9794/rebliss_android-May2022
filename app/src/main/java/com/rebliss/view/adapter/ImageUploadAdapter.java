package com.rebliss.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.view.activity.ActivityPreviewLIve;


import java.util.List;


public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.HomeViewHolder> {

    private int rowLayout;
    private FragmentActivity context;
    private String upload_path;

    private ImageLoader imageLoader;
    public ImageLoaderConfiguration config;
    private Typeface nunito_regular;
    private MySingleton mySingleton;
    private List<Integer> color;
    // this class is called feom summery page and wrigle list page, Type is used for identifiy that
    // 1=Summery
    // 2 = FragmentWriglList
    int type;
    View view;
    String[] Adhar_images;

       public static class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageProfile;



        public HomeViewHolder(View v) {
            super(v);

            imageProfile = v.findViewById(R.id.aadharimageView);

        }
    }

    public ImageUploadAdapter( String[] Adhar_images, int rowLayout, Context context, String upload_path) {
        this.Adhar_images = Adhar_images;
        this.rowLayout = rowLayout;
        this.upload_path = upload_path;
        this.context = (FragmentActivity) context;

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
    public void onBindViewHolder(HomeViewHolder holder, final int position) {
        mySingleton = new MySingleton(context);

        //  nunito_regular = Typeface.createFromAsset(context.getAssets(), Constant.Roboto_Regular);
        if(Adhar_images[position].contains(".pdf")) {
            holder.imageProfile.setImageResource(R.drawable.pdf);
        }

//        App.imageLoader.displayImage(upload_path+Adhar_images[position], holder.imageProfile,App.defaultOptions);
        //App.imageLoader.displayImage(" http://192.168.1.103/vendor/codefire/cfusermgmt/web/images/docs/"+Adhar_images[position], holder.imageProfile,App.defaultOptions);

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,ActivityPreviewLIve.class);
                // Log.i()
                intent.putExtra("url",upload_path+Adhar_images[position]);
                //intent.putExtra("context", (Parcelable) context);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return Adhar_images.length;
    }




}