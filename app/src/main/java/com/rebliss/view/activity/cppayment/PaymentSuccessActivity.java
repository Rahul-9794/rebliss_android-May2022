package com.rebliss.view.activity.cppayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.databinding.ActivityPaymentSuccessBinding;
import com.rebliss.domain.constant.Constant;
import com.rebliss.view.activity.ActivityDashboard;
import com.rebliss.view.fragment.FragmentDashboard;

public class PaymentSuccessActivity extends AppCompatActivity {
    ActivityPaymentSuccessBinding binding;
    String transNumber;
    private MySingleton mySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        mySingleton = new MySingleton(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_success);
        transNumber = getIntent().getStringExtra("trans_id");
        binding.tvTransactionNumber.setText(transNumber);
        binding.txtPhoneNumber.setText("Registered Number :"+mySingleton.getData("contactnum") );
        binding.txtNameofCP.setText("Hello Mr. "+mySingleton.getData(Constant.USER_FIRST_NAME) + " " + mySingleton.getData(Constant.USER_LAST_NAME));
        listener();
    }
    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, FragmentDashboard.class);
            intent.putExtra("frompayment","frompayment");
            startActivity(intent);
            finish();
        });
        binding.btnHome.setOnClickListener(view -> onBackPressed());
    }


}
