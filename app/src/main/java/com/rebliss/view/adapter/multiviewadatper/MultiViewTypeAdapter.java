package com.rebliss.view.adapter.multiviewadatper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.SelfAssessmentModel;
import com.rebliss.domain.model.selfassessmentquestions.AllGroup;
import com.rebliss.domain.model.selfassessmentquestions.ViewTypeModel;
import com.rebliss.view.adapter.McqAdapter;

import java.util.ArrayList;
import java.util.List;


public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AllGroup> dataSet;
    Context mContext;
    int total_types;

    public ArrayList<SelfAssessmentModel> selfAssessmentModelArrayList = new ArrayList<>();
    private McqAdapter mcqAdapter;


    public class SingleLineViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        TextView tvCount;
        EditText etAnswer;

        public SingleLineViewHolder(View itemView) {
            super(itemView);

            this.tvQuestion = itemView.findViewById(R.id.tvQuestion);
            this.tvCount = itemView.findViewById(R.id.tvCount);
            this.etAnswer = itemView.findViewById(R.id.etAnswer);
        }

        public void manageData(int listPosition) {
            tvQuestion.setText(dataSet.get(listPosition).getQuestion());
            tvCount.setText((listPosition + 1) + "");

            etAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    selfAssessmentModelArrayList.get(listPosition).setAnswer(editable.toString());
                }
            });
        }
    }

    public class MultipleLineViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        TextView tvCount;
        EditText etAnswer;

        public MultipleLineViewHolder(View itemView) {
            super(itemView);

            this.tvQuestion = itemView.findViewById(R.id.tvQuestion);
            this.tvCount = itemView.findViewById(R.id.tvCount);
            this.etAnswer = itemView.findViewById(R.id.etAnswer);
        }

        public void manageData(int listPosition) {
            tvQuestion.setText(dataSet.get(listPosition).getQuestion());
            tvCount.setText((listPosition + 1) + "");

            etAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    selfAssessmentModelArrayList.get(listPosition).setAnswer(editable.toString());
                }
            });
        }
    }

    public class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvQuestion;
        private final TextView tvCount;
        private final RecyclerView listViewQuestions;

        public MultipleChoiceViewHolder(View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvCount = itemView.findViewById(R.id.tvCount);
            listViewQuestions = itemView.findViewById(R.id.rvOptions);
        }

        public void manageData(int listPosition) {
            tvQuestion.setText(dataSet.get(listPosition).getQuestion());
            tvCount.setText((listPosition + 1) + "");

            mcqAdapter = new McqAdapter(mContext, dataSet.get(listPosition).getMultichoiceOptions(),selfAssessmentModelArrayList,listPosition);
            listViewQuestions.setAdapter(mcqAdapter);

        }
    }

    public MultiViewTypeAdapter(Context context, List<AllGroup> data) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case ViewTypeModel.QUESTION_TYPE_SINGLE_LINE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_assessment_single_line_items, parent, false);
                return new SingleLineViewHolder(view);
            case ViewTypeModel.QUESTION_TYPE_MULTIPLE_LINE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_assessment_multiple_line_items, parent, false);

                return new MultipleLineViewHolder(view);
            case ViewTypeModel.QUESTION_TYPE_MULTIPLE_CHOICE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_assessment_items, parent, false);
                return new MultipleChoiceViewHolder(view);
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getQuestionType()) {
            case 1:
                return ViewTypeModel.QUESTION_TYPE_SINGLE_LINE;
            case 2:
                return ViewTypeModel.QUESTION_TYPE_MULTIPLE_LINE;
            case 3:
                return ViewTypeModel.QUESTION_TYPE_MULTIPLE_CHOICE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        AllGroup object = dataSet.get(listPosition);
        if (object != null) {

            selfAssessmentModelArrayList.add(listPosition, new SelfAssessmentModel(MySingleton.getInstance(mContext).getData(Constant.USER_ID), object.getId(), ""));

            switch (object.getQuestionType()) {
                case ViewTypeModel.QUESTION_TYPE_SINGLE_LINE:
                    ((SingleLineViewHolder) holder).manageData(listPosition);
                    break;
                case ViewTypeModel.QUESTION_TYPE_MULTIPLE_LINE:
                    ((MultipleLineViewHolder) holder).manageData(listPosition);
                    break;
                case ViewTypeModel.QUESTION_TYPE_MULTIPLE_CHOICE:
                    ((MultipleChoiceViewHolder) holder).manageData(listPosition);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}