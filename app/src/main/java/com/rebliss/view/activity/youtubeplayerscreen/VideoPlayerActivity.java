package com.rebliss.view.activity.youtubeplayerscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.rebliss.MainActivity;
import com.rebliss.R;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.opportunityvideo.AllGroup;
import com.rebliss.domain.model.opportunityvideo.OpportunityVideoRequest;
import com.rebliss.domain.model.opportunityvideo.OpportunityVideosResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityLogin;
import com.rebliss.view.adapter.OpportunityVideosAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayerActivity extends AppCompatActivity {

    private KProgressHUD kProgressHUD;
    private YouTubePlayerView youtube_player_view;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String videoId = null;
    private RecyclerView rvOpportunityVideos;
    private OpportunityVideosAdapter opportunityVideosAdapter;


    private  List<AllGroup> allGroupList;
    ArrayList<String> videoGroupArrayList;
    String youtubeLinks;
    private Network network;
    private DisplaySnackBar displaySnackBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        network = new Network();
        displaySnackBar = new DisplaySnackBar(this);
        if (network.isNetworkConnected(VideoPlayerActivity.this)) {
            getVideoDataApi();
        }
        else
            {
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
            }

        rvOpportunityVideos = findViewById(R.id.rvOpportunityVideos);
        rvOpportunityVideos.setLayoutManager(new LinearLayoutManager(this));


    }

    private void getVideoDataApi() {

        allGroupList=new ArrayList<>();

        OpportunityVideoRequest opportunityVideoRequest = new OpportunityVideoRequest();
        opportunityVideoRequest.setOpportunity_title("20% SALE");


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<OpportunityVideosResponse> call = apiService.getYoutubeDataApi(opportunityVideoRequest);
        call.enqueue(new Callback<OpportunityVideosResponse>() {
            @Override
            public void onResponse(Call<OpportunityVideosResponse> call, Response<OpportunityVideosResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {

                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {

                                if (response.body().getData() != null && response.body().getData().getAllGroups().size() > 0) {

                                    allGroupList = response.body().getData().getAllGroups();
                                    videoGroupArrayList = new ArrayList<>();

                                    for (int i = 0; i < allGroupList.size(); i++) {
                                        youtubeLinks = response.body().getData().getAllGroups().get(0).getVideoLink();
                                        videoGroupArrayList.add(youtubeLinks);
                                        Toast.makeText(VideoPlayerActivity.this, youtubeLinks, Toast.LENGTH_SHORT).show();
                                    }
                                    opportunityVideosAdapter = new OpportunityVideosAdapter(VideoPlayerActivity.this,allGroupList);
                                    rvOpportunityVideos.setAdapter(opportunityVideosAdapter);
                                }


                            }

                            else {

                                Log.e(TAG, "onResponse: else videoplayer" );                            }

                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<OpportunityVideosResponse> call, Throwable t) {
                kProgressHUD.dismiss();
                if ((t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    // displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

}