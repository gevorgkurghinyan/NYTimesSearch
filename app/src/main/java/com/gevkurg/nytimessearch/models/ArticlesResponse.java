package com.gevkurg.nytimessearch.models;


import com.google.gson.annotations.SerializedName;

public class ArticlesResponse {

    @SerializedName("response")
    private Docs docs;

    @SerializedName("response")
    public Docs getResponse() {
        return docs;
    }
}