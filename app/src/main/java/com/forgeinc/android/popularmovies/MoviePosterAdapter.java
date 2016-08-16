package com.forgeinc.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MyViewHolder> {
    private MovieInfo[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public MyViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.poster_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoviePosterAdapter() {
        mDataset = new MovieInfo[] {};
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoviePosterAdapter(MovieInfo[] dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoviePosterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.poster_item, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        Uri uri = Uri.parse("https://image.tmdb.org/t/p/w185/" + mDataset[position].posterPath);
        Context context = holder.mImageView.getContext();
        // - replace the contents of the view with that element
        Picasso.with(context).load(uri).into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.mImageView.getContext(), "Test123", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.length;
        }
        return 0;
    }

    public MovieInfo[] getData() {
        return mDataset;
    }

    public void setData(MovieInfo[] dataset) {
        mDataset = dataset;
    }
}