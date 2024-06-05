package com.example.aria.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aria.MemberListItem;
import com.example.aria.R;
import java.util.List;

public class MembersAdapter extends BaseAdapter {
        List<MemberListItem> membersList;

        private class ViewHolder {
            TextView name;
            TextView phone;

            ImageView minus;
        }

        public MembersAdapter(List<MemberListItem> membersList) {
            this.membersList=membersList;

        }

        @Override
        public int getCount(){
            return membersList.size();
        }
        @Override
        public Object getItem( int position){
            return membersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null){
                LayoutInflater inflater= LayoutInflater.from(parent.getContext());
                convertView=inflater.inflate(R.layout.item_member,parent,false);
                com.example.aria.adapters.MembersAdapter.ViewHolder viewHolder=new com.example.aria.adapters.MembersAdapter.ViewHolder();
                viewHolder.name=convertView.findViewById(R.id.name);
                viewHolder.phone=convertView.findViewById(R.id.phone);
                viewHolder.minus=convertView.findViewById(R.id.minus);
                convertView.setTag(viewHolder);
            }

            MemberListItem m=membersList.get(position);
            com.example.aria.adapters.MembersAdapter.ViewHolder viewHolder= (com.example.aria.adapters.MembersAdapter.ViewHolder) convertView.getTag();
            viewHolder.phone.setText(m.getPhone());
            viewHolder.name.setText(m.getName());
            return convertView;
        }
}
