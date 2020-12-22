package com.agung.deteksikardiovaskular.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelCekPasien implements Parcelable {
    private String dateIssue, chestPT, restingEM, exerciseIA, sTSlope, thalassemia;
    private int restingBP, fastingBS, cholesterol, maxHeartRate, majorVessels;
    private float sTDepression;
    private String result;
    private String key;

    protected ModelCekPasien(Parcel in) {
        dateIssue = in.readString();
        chestPT = in.readString();
        restingEM = in.readString();
        exerciseIA = in.readString();
        sTSlope = in.readString();
        thalassemia = in.readString();
        restingBP = in.readInt();
        fastingBS = in.readInt();
        cholesterol = in.readInt();
        maxHeartRate = in.readInt();
        majorVessels = in.readInt();
        sTDepression = in.readFloat();
        result = in.readString();
        key = in.readString();
    }

    public static final Creator<ModelCekPasien> CREATOR = new Creator<ModelCekPasien>() {
        @Override
        public ModelCekPasien createFromParcel(Parcel in) {
            return new ModelCekPasien(in);
        }

        @Override
        public ModelCekPasien[] newArray(int size) {
            return new ModelCekPasien[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public ModelCekPasien(){

    }

    public ModelCekPasien(String dateIssue, String chestPT, String restingEM, String exerciseIA, String sTSlope, String thalassemia, int restingBP, int fastingBS, int cholesterol, int maxHeartRate, int majorVessels, float sTDepression, String result) {
        this.dateIssue = dateIssue;
        this.chestPT = chestPT;
        this.restingEM = restingEM;
        this.exerciseIA = exerciseIA;
        this.sTSlope = sTSlope;
        this.thalassemia = thalassemia;
        this.restingBP = restingBP;
        this.fastingBS = fastingBS;
        this.cholesterol = cholesterol;
        this.maxHeartRate = maxHeartRate;
        this.majorVessels = majorVessels;
        this.sTDepression = sTDepression;
        this.result = result;
    }


    public String getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue = dateIssue;
    }

    public String getChestPT() {
        return chestPT;
    }

    public void setChestPT(String chestPT) {
        this.chestPT = chestPT;
    }

    public String getRestingEM() {
        return restingEM;
    }

    public void setRestingEM(String restingEM) {
        this.restingEM = restingEM;
    }

    public String getExerciseIA() {
        return exerciseIA;
    }

    public void setExerciseIA(String exerciseIA) {
        this.exerciseIA = exerciseIA;
    }

    public String getsTSlope() {
        return sTSlope;
    }

    public void setsTSlope(String sTSlope) {
        this.sTSlope = sTSlope;
    }

    public String getThalassemia() {
        return thalassemia;
    }

    public void setThalassemia(String thalassemia) {
        this.thalassemia = thalassemia;
    }

    public int getRestingBP() {
        return restingBP;
    }

    public void setRestingBP(int restingBP) {
        this.restingBP = restingBP;
    }

    public int getFastingBS() {
        return fastingBS;
    }

    public void setFastingBS(int fastingBS) {
        this.fastingBS = fastingBS;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getMajorVessels() {
        return majorVessels;
    }

    public void setMajorVessels(int majorVessels) {
        this.majorVessels = majorVessels;
    }

    public float getsTDepression() {
        return sTDepression;
    }

    public void setsTDepression(float sTDepression) {
        this.sTDepression = sTDepression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateIssue);
        dest.writeString(chestPT);
        dest.writeString(restingEM);
        dest.writeString(exerciseIA);
        dest.writeString(sTSlope);
        dest.writeString(thalassemia);
        dest.writeInt(restingBP);
        dest.writeInt(fastingBS);
        dest.writeInt(cholesterol);
        dest.writeInt(maxHeartRate);
        dest.writeInt(majorVessels);
        dest.writeFloat(sTDepression);
        dest.writeString(result);
        dest.writeString(key);
    }
}
