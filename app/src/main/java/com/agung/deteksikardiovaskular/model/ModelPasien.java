package com.agung.deteksikardiovaskular.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelPasien implements Parcelable {
    private String name, dateBirth, address, noKtp, key;
    private int gender;

    public ModelPasien(String name, String dateBirth, String address, String noKtp, int gender) {
        this.name = name;
        this.dateBirth = dateBirth;
        this.address = address;
        this.noKtp = noKtp;
        this.gender = gender;
    }

    public ModelPasien(){

    }

    protected ModelPasien(Parcel in) {
        name = in.readString();
        dateBirth = in.readString();
        address = in.readString();
        noKtp = in.readString();
        key = in.readString();
        gender = in.readInt();
    }

    public static final Creator<ModelPasien> CREATOR = new Creator<ModelPasien>() {
        @Override
        public ModelPasien createFromParcel(Parcel in) {
            return new ModelPasien(in);
        }

        @Override
        public ModelPasien[] newArray(int size) {
            return new ModelPasien[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateBirth);
        dest.writeString(address);
        dest.writeString(noKtp);
        dest.writeString(key);
        dest.writeInt(gender);
    }
}
