package com.garrett.flix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.garrett.flix.adapters.MovieAdapter;
import com.garrett.flix.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieActivity extends AppCompatActivity {
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    private MovieAdapter adapter;
    private List<Movie> movies;
    private Boolean inLandscape;

    private RecyclerView rvMovies;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        inLandscape = this.inLandscape(getResources().getConfiguration());

        rvMovies = findViewById(R.id.rvMovies);
        this.movies = new ArrayList<>();

        adapter = new MovieAdapter(getApplicationContext(), this.movies, this.getOrientationRid());
        rvMovies.setAdapter(adapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess (int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.get(0).getPosterPath());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure (int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    @Override
    public void onConfigurationChanged (@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean inLandscape = this.inLandscape(newConfig);

        if (inLandscape != this.inLandscape) {
            this.inLandscape = inLandscape;

            adapter = new MovieAdapter(getApplicationContext(),
                    movies, this.getOrientationRid());
            rvMovies.swapAdapter(adapter, false);
        }
    }

    private boolean inLandscape(Configuration cfg) {
        return cfg.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private Integer getOrientationRid() {
        return this.inLandscape ? R.layout.item_movie_land : R.layout.item_movie_port;
    }
}