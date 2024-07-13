package com.example.aria.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aria.MemberListItem;
import com.example.aria.R;
import com.example.aria.RetroFitClasses.NotUser;

import java.util.List;

public class MembersAdapter extends BaseAdapter {
        List<MemberListItem> membersList;
        NotUser[] notUsers;

        private class ViewHolder {
            TextView name;
            TextView phone;

            ImageView minus;
            ImageView errorIcon;
        }

        public MembersAdapter(List<MemberListItem> membersList,NotUser [] notUsers) {
            this.membersList=membersList;
            this.notUsers=notUsers;


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
                convertView=inflater.inflate(R.layout.item_friends_list,parent,false);
                com.example.aria.adapters.MembersAdapter.ViewHolder viewHolder=new com.example.aria.adapters.MembersAdapter.ViewHolder();
                viewHolder.name=convertView.findViewById(R.id.name);
                viewHolder.phone=convertView.findViewById(R.id.phone);
                viewHolder.minus=convertView.findViewById(R.id.minus);
                viewHolder.errorIcon= convertView.findViewById(R.id.errorIcon);
                convertView.setTag(viewHolder);
            }

            MemberListItem m=membersList.get(position);
            com.example.aria.adapters.MembersAdapter.ViewHolder viewHolder= (com.example.aria.adapters.MembersAdapter.ViewHolder) convertView.getTag();
            viewHolder.phone.setText(m.getPhone());
            viewHolder.name.setText(m.getName());

            for(NotUser notUser:notUsers)
            {
                if(viewHolder.phone.getText().toString().equals(notUser.getPhone())){
                    System.out.println("in adapter");
                    System.out.println(viewHolder.phone.getText().toString());
                    System.out.println(notUser.getPhone());
                    viewHolder.errorIcon.setVisibility(View.VISIBLE);}
                else{
                    viewHolder.errorIcon.setVisibility(View.INVISIBLE);
                }

            }
            viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.errorIcon.setVisibility(View.INVISIBLE);
                    membersList.remove(position);
                    notifyDataSetChanged();

                }});
            return convertView;
        }

    public void updateNotUsers(NotUser [] newNotUsers) {
        this.notUsers = newNotUsers;
        notifyDataSetChanged();
    }
}
