package org.gptchurch.broadcast.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import org.gptchurch.broadcast.db.SheetsApi;

import java.io.IOException;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

public class Sermon implements Parcelable {

    private String id;
    public String title;
    public Date dateUploaded;
    private String link;

    public Sermon(String id, String title, Date dateUploaded) {
        this.id = id;
        this.title = title;
        this.dateUploaded = dateUploaded;
    }

    protected Sermon(Parcel in) {
        title = in.readString();
        dateUploaded = Date.from(Instant.ofEpochMilli(in.readLong()));
    }

    public String getDriveLink() { return link; };


    public void setDriveLink(String link) {
        this.link = link;
    }

    public String getPrettyDate() {
        return DateFormat.getDateInstance().format(dateUploaded);
    }

    public static final Creator<Sermon> CREATOR = new Creator<Sermon>() {
        @Override
        public Sermon createFromParcel(Parcel in) {
            return new Sermon(in);
        }

        @Override
        public Sermon[] newArray(int size) {
            return new Sermon[size];
        }
    };

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if(obj == null)
            return false;
        return (((Sermon) (obj)).title.equals(this.title)) ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(dateUploaded.toInstant().toEpochMilli());
    }

    public String getId() {
        return id;
    }

    public void update(Date sendDate) throws IOException {

        SheetsApi sheetsApi = new SheetsApi();
        sheetsApi.update(this, sendDate);

    }
}
