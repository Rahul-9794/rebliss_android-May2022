package com.rebliss.view.activity.selfassessmenttest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.R;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.domain.model.selfassessmentquestions.AllGroup;
import com.rebliss.domain.model.selfassessmentquestions.SelfAssessmentResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityLogin;
import com.rebliss.view.activity.BaseActivity;
import com.rebliss.view.activity.TermsAndConditions.TermsAndConditionsScreenActivity;
import com.rebliss.view.adapter.multiviewadatper.MultiViewTypeAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfAssessmentQuestionsActivity extends BaseActivity {

    private RecyclerView recyclerQuestion;
    private MultiViewTypeAdapter selfAssessmentQuestionsAdapter;
    private Button btnNext;
    private KProgressHUD kProgressHUD;
    private List<AllGroup> allGroupList;
    String getQuestionID;
    private Network network;
    private DisplaySnackBar displaySnackBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assessment_questions);
        displaySnackBar = new DisplaySnackBar(this);
        kProgressHUD = new KProgressHUD(this);
        network = new Network();
        if (network.isNetworkConnected(SelfAssessmentQuestionsActivity.this)) {
            getTermsAndCondData();
        }
        else{
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
        recyclerQuestion = findViewById(R.id.recyclerQuestion);
        recyclerQuestion.setNestedScrollingEnabled(false);

        btnNext = findViewById(R.id.btnNext);
    }

    private void getTermsAndCondData() {
        kProgressHUD.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SelfAssessmentResponse> responseCall = apiService.getSelfAssessmentQuestionsData();
        responseCall.enqueue(new Callback<SelfAssessmentResponse>() {

            @Override
            public void onResponse(Call<SelfAssessmentResponse> call, Response<SelfAssessmentResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body().getData() != null && response.body().getData().getAllGroups().size() > 0) {
                            allGroupList = response.body().getData().getAllGroups();

                            selfAssessmentQuestionsAdapter = new MultiViewTypeAdapter(SelfAssessmentQuestionsActivity.this, allGroupList);
                            recyclerQuestion.setAdapter(selfAssessmentQuestionsAdapter);
                            PagerSnapHelper snapHelper = new PagerSnapHelper();
                            snapHelper.attachToRecyclerView(recyclerQuestion);
                            recyclerQuestion.setEnabled(false);

                            final int[] count = {0};
                            btnNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    int currentPosition = ((LinearLayoutManager) recyclerQuestion.getLayoutManager()).findFirstVisibleItemPosition();
                                    if (currentPosition >= recyclerQuestion.getAdapter().getItemCount() -1) {
                                        submitScreen(currentPosition);
                                    } else {
                                        submitAnswer(currentPosition);
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SelfAssessmentResponse> call, Throwable t) {
                kProgressHUD.dismiss();
                Log.e("selfassessment", "onFailure: "+t.getMessage() );
            }
        });
    }

    private void submitAnswer(int currentPosition) {

        if (checkAnswerValid(currentPosition)) {

            kProgressHUD.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<SuccessResponse> responseCall = apiService.postSelfAssessment(selfAssessmentQuestionsAdapter.selfAssessmentModelArrayList.get(currentPosition));
            responseCall.enqueue(new Callback<SuccessResponse>() {

                @Override
                public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                    kProgressHUD.dismiss();
                    if (response.isSuccessful()) {
                        if (response.code() >= 200 && response.code() < 700) {
                            Toast.makeText(SelfAssessmentQuestionsActivity.this, "Answer submitted successfully", Toast.LENGTH_SHORT).show();
                            nextQuestion(currentPosition);

                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessResponse> call, Throwable t) {
                    kProgressHUD.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Please enter the answer", Toast.LENGTH_SHORT).show();
        }

    }

    private void submitAnswers(int currentPosition) {

        if (checkAnswerValid(currentPosition)) {

            kProgressHUD.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<SuccessResponse> responseCall = apiService.postSelfAssessment(selfAssessmentQuestionsAdapter.selfAssessmentModelArrayList.get(currentPosition));
            responseCall.enqueue(new Callback<SuccessResponse>() {

                @Override
                public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                   kProgressHUD.dismiss();
                    if (response.isSuccessful()) {
                        if (response.code() >= 200 && response.code() < 700) {
                            Toast.makeText(SelfAssessmentQuestionsActivity.this, "Answer submitted successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessResponse> call, Throwable t) {
                    kProgressHUD.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Please enter the answer", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean checkAnswerValid(int currentPosition) {
        return !selfAssessmentQuestionsAdapter.selfAssessmentModelArrayList.get(currentPosition).getAnswer().isEmpty();
    }

    private void submitScreen(int currentPosition) {

        submitAnswers(currentPosition);
        btnNext.setText("Submit");

        startActivity(new Intent(SelfAssessmentQuestionsActivity.this, TermsAndConditionsScreenActivity.class));
        finishAffinity();
    }

    private void nextQuestion(int currentPosition) {
        btnNext.setText("Next");
        recyclerQuestion.smoothScrollToPosition(currentPosition+1);
    }

}