package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.domain.model.group.AllGroups;

import java.util.List;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private List<AllGroups> allGroupsList;

    public GroupAdapter(Context context, List<AllGroups> allGroupsList) {
        this.context = context;
        this.allGroupsList = allGroupsList;
    }


    @Override
    public int getCount() {
        return allGroupsList.size();
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
            convertView = inf.inflate(R.layout.group_row, null);
            holder = new ViewHolder();
            holder.textGroupName = convertView.findViewById(R.id.textGroupName);
            holder.imgGroup = convertView.findViewById(R.id.imgGroup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textGroupName.setTypeface(App.LATO_REGULAR);
        holder.textGroupName.setText(allGroupsList.get(position).getName());


        if(holder.textGroupName.getText().toString().equals("Join as Partner/RTSM"))
        {
            holder.imgGroup.setImageResource(R.drawable.group_icon);
        }
        else{
            holder.imgGroup.setImageResource(R.drawable.ic_person);
        }

        return convertView;
    }


    private class ViewHolder {
        TextView textGroupName;
        ImageView imgGroup;
    }
}
