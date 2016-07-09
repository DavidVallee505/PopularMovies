package com.forgeinc.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                                  "http://i.imgur.com/DvpvklR.png"};
    protected static MyOnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myOnClickListener = new MyOnClickListener();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a grid layout manager
        mLayoutManager = new GridLayoutManager(this, 20);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new MyRecyclerViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String selectedMovieUrl = getSelectedMovieUrl(v);
            showSelectedMovie(selectedMovieUrl);
        }
        private String getSelectedMovieUrl(View view) {
            int selectedItemPosition = mRecyclerView.getChildLayoutPosition(view);
            String url = myDataset[selectedItemPosition];
            return url;
        }

        private void showSelectedMovie(String movieUrl) {
            Intent intent = new Intent(getParent(), MovieActivity.class);
            intent.putExtra("movieUrl", movieUrl);
            startActivity(intent);
        }
    }


}
