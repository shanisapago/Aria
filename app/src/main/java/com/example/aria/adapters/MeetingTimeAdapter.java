package com.example.aria.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aria.R;
import com.example.aria.TimeMeeting;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MeetingTimeAdapter extends BaseAdapter {

    List<TimeMeeting> timeMeetingsList;
    Duration treatment_duration;
    private class ViewHolder{
        TextView date;
        TextView time;
        TextView time2;
        ImageButton minus;
        ImageView errorIcon;


    }

    public MeetingTimeAdapter(List<TimeMeeting> timeMeetings,Duration d) {
        this.timeMeetingsList=timeMeetings;
        this.treatment_duration=d;
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
            viewHolder.time2=convertView.findViewById(R.id.time2);
            viewHolder.minus=convertView.findViewById(R.id.minus);
            viewHolder.errorIcon= convertView.findViewById(R.id.errorIcon);
            convertView.setTag(viewHolder);

        }

        TimeMeeting p = timeMeetingsList.get(position);
        ViewHolder viewHolder=(ViewHolder) convertView.getTag();
        viewHolder.date.setText(p.getDateMeeting());
        viewHolder.time.setText(p.getTimeMeeting());
        viewHolder.time2.setText(p.getTimeMeeting2());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime timeparse=LocalTime.parse(viewHolder.time.getText().toString(), formatter);

        LocalTime timeparse2=LocalTime.parse(viewHolder.time2.getText().toString(), formatter);
        Duration durationBetween = Duration.between(timeparse,timeparse2);
        if (!(durationBetween.compareTo(treatment_duration) > 0))
        {
            viewHolder.errorIcon.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.errorIcon.setVisibility(View.INVISIBLE);
        }

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.errorIcon.setVisibility(View.INVISIBLE);
                timeMeetingsList.remove(position);
                notifyDataSetChanged();

            }});


        return convertView;
    }

    public void setTimeMeetingsList(List<TimeMeeting> timeMeetingsList) {
        this.timeMeetingsList = timeMeetingsList;
    }
    public void updateDuration(Duration duration) {
        this.treatment_duration=duration;
        notifyDataSetChanged();
    }
    public boolean isError() {
        for (TimeMeeting p : timeMeetingsList) {
            LocalTime timeparse = LocalTime.parse(p.getTimeMeeting(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime timeparse2 = LocalTime.parse(p.getTimeMeeting2(), DateTimeFormatter.ofPattern("HH:mm"));
            Duration durationBetween = Duration.between(timeparse, timeparse2);

            if (!(durationBetween.compareTo(treatment_duration) > 0)) {
                return true;
            }
        }
        return false;
    }
}