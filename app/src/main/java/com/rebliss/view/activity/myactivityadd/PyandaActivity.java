package com.rebliss.view.activity.myactivityadd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;

import java.io.File;

public class PyandaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyanda3);


        if (MySingleton.getInstance(this).getBoolean(MyActivityConstants.IS_CALLED)) {
            int requestCode = getIntent().getIntExtra("taskCode", 0);
            ImagePicker.Companion.with(PyandaActivity.this).cameraOnly().start(requestCode);
            MySingleton.getInstance(this).saveBoolean(MyActivityConstants.IS_CALLED,false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        new Handler().postDelayed(() -> {
            MyActivityConstants.requestFile = ImagePicker.Companion.getFile(data);
            finish();

        },500);
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}
