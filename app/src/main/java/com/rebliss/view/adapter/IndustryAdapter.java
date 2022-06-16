package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.domain.model.industrytype.IndustryType;

import java.util.List;

public class IndustryAdapter extends BaseAdapter{
    private Context context;
    private List<IndustryType> allGroupsList;

    public IndustryAdapter(Context context, List<IndustryType> allGroupsList) {
        this.context = context;
        this.allGroupsList = allGroupsList;
    }


    @Override
    public int getCount() {
        if(allGroupsList != null){
            return allGroupsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return allGroupsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.industry_row, null);
            holder = new ViewHolder();
            holder.textIndustryName = convertView.findViewById(R.id.textIndustryName);
            convertView.setTag(holder);
        } else {
            holder = (IndustryAdapter.ViewHolder) convertView.getTag();
        }
        holder.textIndustryName.setTypeface(App.LATO_REGULAR);
        holder.textIndustryName.setText(allGroupsList.get(position).getText());

        return convertView;
    }


    private class ViewHolder {
        TextView textIndustryName;
    }
}
