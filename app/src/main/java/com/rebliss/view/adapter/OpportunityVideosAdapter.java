package com.rebliss.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.rebliss.R;
import com.rebliss.domain.model.opportunityvideo.AllGroup;
import com.rebliss.view.activity.YoutubeVideoPlayerActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpportunityVideosAdapter extends RecyclerView.Adapter<OpportunityVideosAdapter.ViewHolder> {


    private Context mContext;
    private List<AllGroup> allGroupsList;
    private String videoId = null;
    DisplayMetrics displayMetrics = new DisplayMetrics();


    public OpportunityVideosAdapter(Context mContext, List<AllGroup> allGroupsList) {
        this.mContext = mContext;
        this.allGroupsList = allGroupsList;

    }

    @NonNull
    @Override
    public OpportunityVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OpportunityVideosAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_opportunity_items, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityVideosAdapter.ViewHolder viewHolder, int i) {

        videoId=getVideoIdFromYoutubeUrl(allGroupsList.get(0).getVideoLink());
       // viewHolder.videoLink.setText(getVideoIdFromYoutubeUrl(videoId));
      /*  viewHolder.youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        });*/

        Glide.with(mContext).load(allGroupsList.get(i).getImage()).into(viewHolder.ivVideoThumbnails);

        viewHolder.ivVideoThumbnails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YoutubeVideoPlayerActivity.class);
                intent.putExtra("videoId",videoId);
                Log.e("TAG", "onClick: "+videoId );
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allGroupsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView videoLink;
        private YouTubePlayerView youtube_player_view;
        private ImageView btnPlay;
        private ImageView ivVideoThumbnails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoLink = itemView.findViewById(R.id.videoLink);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            youtube_player_view = itemView.findViewById(R.id.youtube_player_view);
            ivVideoThumbnails = itemView.findViewById(R.id.ivVideoThumbnail);

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.cueVideo(allGroupsList.get(getAdapterPosition()).getVideoLink(), 0);
                            Toast.makeText(mContext, allGroupsList.get(getAdapterPosition()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }

    }

    public String getVideoIdFromYoutubeUrl(String url){
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            videoId = matcher.group(1);
        }
        return videoId;
    }
}
