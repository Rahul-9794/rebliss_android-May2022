package com.rebliss.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.domain.model.city.City;



import java.util.List;

public class CitySpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<City> allGroupsList;

    public CitySpinnerAdapter(Context context, List<City> allGroupsList) {
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
            convertView = inf.inflate(R.layout.stata_raw, null);
            holder = new ViewHolder();

            holder.textName = convertView.findViewById(R.id.textName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textName.setTypeface(App.LATO_REGULAR);

        try {
            holder.textName.setText(allGroupsList.get(position).getS_name());
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return convertView;
    }


    private class ViewHolder {

        private TextView textName;
    }
}
