package com.example.aria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aria.ClosedEventsListItem;
import com.example.aria.OpenEventsListItem;
import com.example.aria.R;

import org.w3c.dom.Text;

import java.util.List;

public class ClosedEventsAdapter extends BaseAdapter {
    List<ClosedEventsListItem> closedEventsList;

    private class ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        TextView time;


    }

    public ClosedEventsAdapter(List<ClosedEventsListItem> closedEventsList) {
        this.closedEventsList=closedEventsList;

    }

    @Override
    public int getCount(){
        return closedEventsList.size();
    }
    @Override
    public Object getItem( int position){
        return closedEventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.item_closed_list,parent,false);
            com.example.aria.adapters.ClosedEventsAdapter.ViewHolder viewHolder=new com.example.aria.adapters.ClosedEventsAdapter.ViewHolder();
            viewHolder.title=convertView.findViewById(R.id.title);
            viewHolder.description=convertView.findViewById(R.id.description);
            viewHolder.date=convertView.findViewById(R.id.date);
            viewHolder.time=convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        }

        ClosedEventsListItem m=closedEventsList.get(position);
        com.example.aria.adapters.ClosedEventsAdapter.ViewHolder viewHolder= (com.example.aria.adapters.ClosedEventsAdapter.ViewHolder) convertView.getTag();
        viewHolder.title.setText(m.getTitle());
        viewHolder.description.setText(m.getDescription());
        viewHolder.date.setText(m.getDate());
        viewHolder.time.setText(m.getTime());
        return convertView;
    }
}