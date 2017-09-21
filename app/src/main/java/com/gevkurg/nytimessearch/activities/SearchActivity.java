package com.gevkurg.nytimessearch.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gevkurg.nytimessearch.R;
import com.gevkurg.nytimessearch.adapters.ArticlesAdapter;
import com.gevkurg.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.gevkurg.nytimessearch.models.Article;
import com.gevkurg.nytimessearch.models.ArticlesResponse;
import com.gevkurg.nytimessearch.network.NYTimesClient;
import com.gevkurg.nytimessearch.network.NYTimesService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    public static final NYTimesService NEW_YORK_TIMES_SERVICE = NYTimesClient.getInstance()
            .getNYTimesService();

    private static final String TOP_STORIES_SEARCH_QUERIY = "Top Stories";

    @BindView(R.id.rvArticles) RecyclerView rvArticles;
    private List<Article> articles;
    private ArticlesAdapter articlesAdapter;
    private String queryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        articles = new ArrayList<>();
        articlesAdapter = new ArticlesAdapter(this, articles);
        articlesAdapter.notifyDataSetChanged();
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setAdapter(articlesAdapter);
        rvArticles.setLayoutManager(gridLayoutManager);

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchArticles(page, queryText);
            }
        });

        fetchArticles(0, TOP_STORIES_SEARCH_QUERIY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText = query;
                fetchArticles(0, query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchArticles(int page, String query) {
        Call<ArticlesResponse> articlesCall = NEW_YORK_TIMES_SERVICE
                .getArticles(page, query);

        articlesCall.enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                articles.addAll(response.body().getResponse().getArticles());
                articlesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                String s = t.getMessage();
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}