package com.rebliss.view.activity.cppayment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.rebliss.R;
import com.rebliss.databinding.ActivityTermsAndConditionBinding;

public class TermsAndConditionActivity extends AppCompatActivity {


    ActivityTermsAndConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_terms_and_condition);
        binding.webView.loadUrl("http://app.rebliss.in/site/privacy-policy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
