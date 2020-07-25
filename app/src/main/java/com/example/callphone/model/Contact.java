package com.example.callphone.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private int id;
    private String name;
    private String phoneNum;
    private String telephoneNum;
    private String E_meil;
    private String iconUri;

    public Contact(int id,String name, String phoneNum) {
        this.id = id;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNum = in.readString();
        telephoneNum = in.readString();
        E_meil = in.readString();
        iconUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phoneNum);
        dest.writeString(telephoneNum);
        dest.writeString(E_meil);
        dest.writeString(iconUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getE_meil() {
        return E_meil;
    }

    public void setE_meil(String e_meil) {
        E_meil = e_meil;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }
}
