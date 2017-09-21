package com.gevkurg.nytimessearch.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Article implements Parcelable {
    @SerializedName("_id")
    private String id;
    private Headline headline;
    private String snippet;
    @SerializedName("web_url")
    private String webUrl;
    @SerializedName("multimedia")
    private List<ArticleMedia> medias = new ArrayList<>();

    protected Article(Parcel in) {
        this.id = in.readString();
        String headline = in.readString();
        this.headline = new Headline();
        this.headline.setMain(headline);
        this.snippet = in.readString();
        this.webUrl = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getSnippet() {
        return snippet;
    }

    public Headline getHeadline() {
        return headline;
    }

    public String getWebUrl() {
        return webUrl;
    }

    @SerializedName("multimedia")
    public List<ArticleMedia> getMultimedia() {
        return medias;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.headline.getMain());
        parcel.writeString(this.snippet);
        parcel.writeString(this.webUrl);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}