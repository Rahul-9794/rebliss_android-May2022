package com.rebliss.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.domain.constant.Constant;
import com.zhihu.matisse.internal.entity.SelectionSpec;

public class ActivityPreviewLIve extends AppCompatActivity {
    private ImageView image_view, video_play_button;
    private FrameLayout image_frame;
    private RelativeLayout video_frame, pdf_frame;
    private VideoView videoView;
    private TextView back;
    private Intent intent;
    private String path;
    private Uri uri;
    private Bitmap myBitmap;
    ProgressDialog pd;
    private KProgressHUD kProgressHUD;
    private Context context;
    private WebView webview;
    private static final String googleDocsUrl = "http://docs.google.com/viewer?url=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(SelectionSpec.getInstance().themeId);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_preview_item_app);
        intent = getIntent();
        path = intent.getStringExtra("url");
        initView();
        viewListener();
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);
        image_frame = findViewById(R.id.image_frame);
        video_frame = findViewById(R.id.video_frame);
        pdf_frame = findViewById(R.id.pdf_frame);

        image_view = findViewById(R.id.image_view);
        image_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        video_play_button = findViewById(R.id.video_play_button);
        if (path.contains(Constant.IMAGE_TYPE_JPG) || path.contains(Constant.IMAGE_TYPE_JPEG) ||
                path.contains(Constant.IMAGE_TYPE_PNG)) {
            image_frame.setVisibility(View.VISIBLE);
            video_frame.setVisibility(View.GONE);
            pdf_frame.setVisibility(View.GONE);
            App.imageLoader.displayImage(path, image_view, App.defaultOptions);

            video_play_button.setVisibility(View.GONE);
        } else if(path.contains("pdf")) {
            image_frame.setVisibility(View.GONE);
            video_frame.setVisibility(View.GONE);
            pdf_frame.setVisibility(View.VISIBLE);
            Log.i("MyPdf", path);
            webview = findViewById(R.id.webview);
            webview.loadUrl("http://docs.google.com/gview?embedded=true&url="+path);
        } else {

            kProgressHUD = KProgressHUD.create(ActivityPreviewLIve.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setLabel("Buffering video please wait...")
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .setWindowColor(getResources().getColor(R.color.transparent_color))
                    .show();
            image_frame.setVisibility(View.GONE);
            pdf_frame.setVisibility(View.GONE);
            video_frame.setVisibility(View.VISIBLE);
            MediaController media_Controller = new MediaController(this);
            videoView.setMediaController(media_Controller);
            Uri uri = Uri.parse(path);
            videoView.setVideoURI(uri);
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    kProgressHUD.dismiss();
                }
            });
        }
        back = findViewById(R.id.back);

    }

    private void viewListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView!=null) {
                 if(videoView.isPlaying())
                     videoView.stopPlayback();
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(videoView!=null) {
            if(videoView.isPlaying())
                videoView.stopPlayback();
        }
        finish();
    }
}
