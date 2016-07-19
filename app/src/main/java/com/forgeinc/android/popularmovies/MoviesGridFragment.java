package com.forgeinc.android.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Encapsulates fetching the forecast and displaying it as a {@link AutoFitRecyclerView} layout.
 */
public class MoviesGridFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";

    protected RecyclerView mRecyclerView;
    protected MovieInfo[] mDataset;
    protected MoviePosterAdapter mMoviePosterAdapter;

    public MoviesGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // specify an adapter
        mMoviePosterAdapter = new MoviePosterAdapter(mDataset);
        mRecyclerView.setAdapter(mMoviePosterAdapter);

        return rootView;
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        String sortType = PreferenceManager.getDefaultSharedPreferences(getActivity())
            .getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_rating));
        moviesTask.execute(sortType);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieInfo[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private MovieInfo[] getMoviesListFromJson(String moviesJsonStr, int numMovies)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_LIST = "results";
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "original_title";
            final String TMDB_PATH = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RELEASE = "release_date";
            final String TMDB_VOTE_AVG = "vote_average";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_LIST);

            MovieInfo[] resultMovies = new MovieInfo[numMovies];

            for(int i = 0; i < moviesArray.length(); i++) {
                // Get the JSON object representing the movie
                JSONObject movieInfo = moviesArray.getJSONObject(i);
                resultMovies[i] = new MovieInfo(movieInfo.getInt(TMDB_ID),
                    movieInfo.getString(TMDB_TITLE),
                    movieInfo.getString(TMDB_OVERVIEW),
                    movieInfo.getString(TMDB_RELEASE),
                    movieInfo.getString(TMDB_PATH),
                    movieInfo.getDouble(TMDB_VOTE_AVG));
            }
            return resultMovies;
        }

        @Override
        protected MovieInfo[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            int numMovies = 20;

            try {
                // Construct the URL for the TheMovieDatabase query
                // Possible parameters are available at TMDB's API page, at
                // http://docs.themoviedb.apiary.io/#reference
                final String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";

                SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortType = sharedPrefs.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_rating));

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendPath(sortType)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DATABASE_API_KEY)
                    .build();

                URL url = new URL(builtUri.toString());

                // Create the request to TheMovieDatabase, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesListFromJson(moviesJsonStr, numMovies);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }
    }
}
