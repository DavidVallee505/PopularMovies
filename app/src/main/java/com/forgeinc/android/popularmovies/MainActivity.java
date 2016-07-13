package com.forgeinc.android.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity {
    private String[] myDataset = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                                  "http://image.tmdb.org/t/p/w185//9yoY43fgCmuCv2tzIr77z9jCx1L.jpg",
                                  "https://image.tmdb.org/t/p/w185/aBBQSC8ZECGn6Wh92gKDOakSC8p.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // specify an adapter
        mRecyclerView.setAdapter(new MoviePosterAdapter(myDataset));
    }
}
