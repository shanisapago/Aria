package com.example.aria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.aria.OpenEventsListItem;
import com.example.aria.R;
import java.util.List;

public class OpenEventsAdapter extends BaseAdapter {
    List<OpenEventsListItem> openEventsList;

    private class ViewHolder {
        TextView title;
        TextView description;


    }

    public OpenEventsAdapter(List<OpenEventsListItem> openEventsList) {
        this.openEventsList=openEventsList;

    }

    @Override
    public int getCount(){
        return openEventsList.size();
    }
    @Override
    public Object getItem( int position){
        return openEventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.item_open_list,parent,false);
            com.example.aria.adapters.OpenEventsAdapter.ViewHolder viewHolder=new com.example.aria.adapters.OpenEventsAdapter.ViewHolder();
            viewHolder.title=convertView.findViewById(R.id.title);
            viewHolder.description=convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        }

        OpenEventsListItem m=openEventsList.get(position);
        com.example.aria.adapters.OpenEventsAdapter.ViewHolder viewHolder= (com.example.aria.adapters.OpenEventsAdapter.ViewHolder) convertView.getTag();
        viewHolder.title.setText(m.getTitle());
        viewHolder.description.setText(m.getDescription());
        return convertView;
    }
}