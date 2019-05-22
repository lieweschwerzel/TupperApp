package com.example.tupperapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "tuppermeal")
public class TupperMeal implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int id;
    private int ImageId;
    public String title;
    public String status;
    public String date;
    public String url;


    public TupperMeal(String title, int ImageId, String status, String date, String url) {
        this.title = title;
        this.ImageId = ImageId;
        this.status = status;
        this.date = date;
        this.url = url;
    }

    @Override
    public String toString() {
        return "TupperMeal{" +
                "id=" + id +
                ", ImageId=" + ImageId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    protected TupperMeal(Parcel in) {
        id = in.readInt();
        title = in.readString();
        ImageId = in.readInt();
        status = in.readString();
        date = in.readString();
        url = in.readString();
    }

    public static final Creator<TupperMeal> CREATOR = new Creator<TupperMeal>() {
        @Override
        public TupperMeal createFromParcel(Parcel in) {
            return new TupperMeal(in);
        }

        @Override
        public TupperMeal[] newArray(int size) {
            return new TupperMeal[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId (int ImageId) {
        this.ImageId = ImageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(ImageId);
        dest.writeString(status);
        dest.writeString(date);
        dest.writeString(url);
    }
}
