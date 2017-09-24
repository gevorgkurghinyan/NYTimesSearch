package com.gevkurg.nytimessearch.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gevkurg.nytimessearch.R;
import com.gevkurg.nytimessearch.adapters.ArticlesAdapter;
import com.gevkurg.nytimessearch.fragments.FilterFragment;
import com.gevkurg.nytimessearch.helper.Helper;
import com.gevkurg.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.gevkurg.nytimessearch.models.Article;
import com.gevkurg.nytimessearch.models.ArticlesResponse;
import com.gevkurg.nytimessearch.models.SearchFilter;
import com.gevkurg.nytimessearch.network.NYTimesClient;
import com.gevkurg.nytimessearch.network.NYTimesService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
        implements FilterFragment.SearchFilterUpdateListener {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    public static final NYTimesService NEW_YORK_TIMES_SERVICE = NYTimesClient.getInstance()
            .getNYTimesService();

    private static final String TOP_STORIES_SEARCH_QUERY = "Top Stories";
    public static final String FILENAME = "SearchFilter.txt";

    @BindView(R.id.rvArticles)
    RecyclerView mrvArticles;

    private List<Article> mArticles;
    private ArticlesAdapter mArticlesAdapter;
    private String mQueryText;
    private FilterFragment mFilterFragment;
    private SearchFilter mSearchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //mSearchFilter = loadSearchFilter();

        mSearchFilter = new SearchFilter();
        mArticles = new ArrayList<>();
        mArticlesAdapter = new ArticlesAdapter(this, mArticles);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mrvArticles.setAdapter(mArticlesAdapter);
        mrvArticles.setLayoutManager(gridLayoutManager);

        mrvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchArticles(page);
            }
        });

        mQueryText = TOP_STORIES_SEARCH_QUERY;
        fetchArticles(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Helper.isNetworkAvailable(this)) {
            Helper.showSnackBarForInternetConnection(mrvArticles, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                mArticlesAdapter.clear();
                fetchArticles(0);
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

        if (id == R.id.action_filter) {
            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFilterOptionsChanged(SearchFilter newSearchFilter) {
        mSearchFilter = newSearchFilter;
        //saveSearchFilter(mSearchFilter);
        mArticlesAdapter.clear();
        fetchArticles(0);
    }

    private void showFilterDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFilterFragment = FilterFragment.newInstance("Search Filter Options");
        Bundle args = new Bundle();
        args.putParcelable(FilterFragment.FILTER_OPTIONS_KEY, mSearchFilter);
        mFilterFragment.setArguments(args);
        mFilterFragment.show(fragmentManager, "search_filters_fragment");
    }

    private void fetchArticles(int page) {
        if (Helper.isNetworkAvailable(this)) {

            if (!mQueryText.isEmpty()) {

                String beginDate = mSearchFilter.getDateWithoutSeparator();
                String sortOrder = mSearchFilter.getSortOrder() != null ?
                        mSearchFilter.getSortOrder().name().toLowerCase() : null;
                final String newDeskValues = mSearchFilter.getNewDeskValues();
                String fq = newDeskValues != null ?
                        String.format("news_desk:(%s)", newDeskValues) : null;

                Call<ArticlesResponse> articlesCall = NEW_YORK_TIMES_SERVICE
                        .getArticles(mQueryText, page, beginDate, sortOrder, fq);

                articlesCall.enqueue(new Callback<ArticlesResponse>() {
                    @Override
                    public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                        if (response != null && response.body() != null) {
                            mArticles.addAll(response.body().getResponse().getArticles());
                            mArticlesAdapter.notifyDataSetChanged();
                        } else {
                            if(response.code() == 429) {
                                Helper.showSnackBar(mrvArticles, SearchActivity.this, R.string.request_failed_too_many_requests_text);
                            } else {
                                Helper.showSnackBar(mrvArticles, SearchActivity.this, R.string.request_failed_text);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                        Helper.showSnackBar(mrvArticles, SearchActivity.this, R.string.request_failed_text);
                    }
                });
            }
        } else {
            Helper.showSnackBarForInternetConnection(mrvArticles, this);
        }
    }

    //TODO: save SearchFilter into the file
    private void saveSearchFilter(SearchFilter filter) {
        try {
            FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(filter);
            os.close();
            fos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    //TODO: get saved SearchFilter from the file.
    private SearchFilter loadSearchFilter() {
        SearchFilter filter = new SearchFilter();
        try {
            FileInputStream fis = this.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            filter = (SearchFilter) is.readObject();
            is.close();
            fis.close();
            return (filter);
        } catch (ClassNotFoundException cnfe) {
            Log.e("Exception", "ClassNotFoundException: " + cnfe.toString());
        } catch (IOException e) {
            Log.e("Exception", "IOException: " + e.toString());
        }
        return filter;
    }
}