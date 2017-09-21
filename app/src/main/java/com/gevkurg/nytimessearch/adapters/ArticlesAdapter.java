package com.gevkurg.nytimessearch.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gevkurg.nytimessearch.R;
import com.gevkurg.nytimessearch.activities.ArticleActivity;
import com.gevkurg.nytimessearch.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Article> mArticls;
    private Context mContext;

    public ArticlesAdapter(Context context, List<Article> articles) {
        mArticls = articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticls.get(position);

        // Set item views based on your views and data model
        TextView tvHeadline = viewHolder.tvHeadline;
        tvHeadline.setText(article.getHeadline().getMain());
        TextView tvSnippet = viewHolder.tvSnippet;
        tvSnippet.setText(article.getSnippet());
        ImageView ivArticleImage = viewHolder.ivArticleImage;
        ivArticleImage.setImageResource(0);

        String articleImageUrl = "";

        if (article.getMultimedia().size() > 0) {
            articleImageUrl = article.getMultimedia().get(0).getUrl();
        }

        if (!TextUtils.isEmpty(articleImageUrl)) {
            ivArticleImage.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(Uri.parse(articleImageUrl))
                    .placeholder(R.drawable.ic_nocover)
                    .into(ivArticleImage);
        } else {
            ivArticleImage.setVisibility(View.GONE);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticls.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @BindView(R.id.ivArticleImage)
        ImageView ivArticleImage;
        @BindView(R.id.tvHeadline)
        TextView tvHeadline;
        @BindView(R.id.tvSnippet)
        TextView tvSnippet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Article article = mArticls.get(position);

                Intent intent = new Intent(getContext(), ArticleActivity.class);
                intent.putExtra("article", article);
                getContext().startActivity(intent);
            }
        }
    }
}
