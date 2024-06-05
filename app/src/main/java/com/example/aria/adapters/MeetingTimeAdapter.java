package com.example.aria.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.aria.R;
import com.example.aria.TimeMeeting;
import java.util.List;

public class MeetingTimeAdapter extends BaseAdapter {

    List<TimeMeeting> timeMeetingsList;
    private class ViewHolder{
        TextView date;
        TextView time;
        ImageButton minus;

    }

    public MeetingTimeAdapter(List<TimeMeeting> timeMeetings) {
        this.timeMeetingsList=timeMeetings;
    }

    @Override
    public int getCount() { return timeMeetingsList.size(); }

    @Override
    public Object getItem(int position) { return timeMeetingsList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.timemeeting_layout, parent, false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.date=convertView.findViewById(R.id.date);
            viewHolder.time=convertView.findViewById(R.id.time);
            viewHolder.minus=convertView.findViewById(R.id.minus);
            convertView.setTag(viewHolder);

        }

            TimeMeeting p = timeMeetingsList.get(position);
            ViewHolder viewHolder=(ViewHolder) convertView.getTag();
            viewHolder.date.setText(p.getDateMeeting());
            viewHolder.time.setText(p.getTimeMeeting());


        return convertView;
    }

    public void setTimeMeetingsList(List<TimeMeeting> timeMeetingsList) {
        this.timeMeetingsList = timeMeetingsList;
    }
}







