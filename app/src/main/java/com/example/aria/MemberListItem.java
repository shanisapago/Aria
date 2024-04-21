package com.example.aria;

public class MemberListItem {
        String phone;
        String name;


        public MemberListItem(String phone, String name) {
            this.phone = phone;
            this.name = name;
        }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MemberListItem obj1 = (MemberListItem) obj;
        return this.phone.equals(obj1.phone);
    }
}
