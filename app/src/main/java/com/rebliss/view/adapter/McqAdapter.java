package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.domain.model.SelfAssessmentModel;
import com.rebliss.domain.model.selfassessmentquestions.MultichoiceOption;

import java.util.ArrayList;
import java.util.List;

public class McqAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    List<MultichoiceOption> list = new ArrayList<>();
    public int selectedPosition = -1;
    ArrayList<SelfAssessmentModel> selfAssessmentModelArrayList;
    int mainListPosition;

    public McqAdapter(Context context, List<MultichoiceOption> data, ArrayList<SelfAssessmentModel> selfAssessmentModelArrayList, int listPosition) {
        this.list = data;
        this.mContext = context;
        this.selfAssessmentModelArrayList = selfAssessmentModelArrayList;
        this.mainListPosition = listPosition;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mcq, parent, false);
        return new McqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((McqViewHolder) holder).manageData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class McqViewHolder extends RecyclerView.ViewHolder {
        CheckBox option;

        public McqViewHolder(View itemView) {
            super(itemView);
            this.option = (CheckBox) itemView.findViewById(R.id.checkbox);
        }

        public void manageData(int listPosition) {
            option.setText(list.get(listPosition).getOptions());

            if (listPosition == selectedPosition) {
                option.setChecked(true);
                selfAssessmentModelArrayList.get(mainListPosition).setAnswer(option.getText().toString());
            } else {
                option.setChecked(false);
            }

            option.setOnClickListener(view -> {
                selectedPosition = listPosition;
                notifyDataSetChanged();
            });
        }
    }

}
