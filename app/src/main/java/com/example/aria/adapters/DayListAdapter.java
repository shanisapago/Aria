package com.example.aria.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.aria.DayListItem;
import com.example.aria.R;
import java.util.List;

public class DayListAdapter extends BaseAdapter {

    List<DayListItem> dayList;

    private class ViewHolder {
        TextView time;
        TextView title;
        TextView description;
    }

    public DayListAdapter(List<DayListItem> dayList) {
        this.dayList=dayList;

    }

    @Override
    public int getCount(){
        return dayList.size();
    }
    @Override
    public Object getItem( int position){
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.item_day_list,parent,false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.time=convertView.findViewById(R.id.time);
            viewHolder.title=convertView.findViewById(R.id.title);
            viewHolder.description=convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        }

        DayListItem d=dayList.get(position);
        ViewHolder viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.time.setText(d.getTime());
        viewHolder.title.setText(d.getTitle());
        viewHolder.description.setText(d.getDescription());
        return convertView;
    }
}

