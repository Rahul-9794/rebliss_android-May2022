package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rebliss.R;
import com.rebliss.domain.model.selfassessmentquestions.AllGroup;

import java.util.ArrayList;
import java.util.List;


public class SelfAssessmentQuestionsAdapter extends RecyclerView.Adapter<SelfAssessmentQuestionsAdapter.ViewHolder> {


    private Context mContext;
    private List<AllGroup> allGroupsList;
    ArrayAdapter adapter;
    String[] version = {"Aestro","Blender","CupCake"};
    ArrayList<String> list=new ArrayList<>();

    public SelfAssessmentQuestionsAdapter(Context mContext, List<AllGroup> allGroupsList) {
        this.mContext = mContext;
        this.allGroupsList = allGroupsList;

    }

    @NonNull
    @Override
    public SelfAssessmentQuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SelfAssessmentQuestionsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questions_assessment_items, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelfAssessmentQuestionsAdapter.ViewHolder viewHolder, int position) {
        list=new ArrayList<>();
        viewHolder.tvAnswers.setText((allGroupsList.get(position).getQuestion()));
        viewHolder.tvCount.setText((allGroupsList.get(position).getId()));

         viewHolder.listViewQuestions.setChoiceMode(viewHolder.listViewQuestions.CHOICE_MODE_SINGLE);
         adapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,list);
         viewHolder.listViewQuestions.setAdapter(adapter);

         getListData();
    }

    @Override
    public int getItemCount() {
        return allGroupsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAnswers;
        private TextView tvCount;
        private ListView listViewQuestions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

    }

    private void getListData(){
        list = new ArrayList<>();

        for (int i = 0;i<version.length;i++){

            list.add(version[i]);

        }
    }
}

