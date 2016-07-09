package com.forgeinc.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MovieActivity extends Activity {
    private WebView movieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieView = new WebView(this);

        String url = getIntent().getStringExtra("movieUrl");
        if (url == null) {
            url = "http://google.com/";
        }
        movieView.loadUrl(url);
        movieView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                setContentView(movieView);
            }
        });

    }
}
