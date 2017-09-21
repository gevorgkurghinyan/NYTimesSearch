package com.gevkurg.nytimessearch.network;


import com.gevkurg.nytimessearch.models.ArticlesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTimesService {
    @GET("svc/search/v2/articlesearch.json?api-key=c65383152e094f4883e22c5ccf21709a")
    Call<ArticlesResponse> getArticles(@Query("page") int page, @Query("q") String query);
}