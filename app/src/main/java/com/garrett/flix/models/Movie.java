/*
 * author: Garrett
 * date: 2/13/2021
 * project: Flix
 * description:
 */
package com.garrett.flix.models;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private static final int OVERVIEW_MAX_WORDS_PORT = 30;
    private static final int OVERVIEW_MAX_WORDS_LAND = 65;

    String backdropPath;
    String posterPath;
    String title;
    String overview;

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public String getBackdropPath () {
        return String.format("https://image.tmdb.org/t/p/w300%s", backdropPath);
    }

    public String getPosterPath () {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getTitle () {
        return title;
    }

    public String getOverview (boolean isPortrait) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String[] words = overview.split(" ");
            int end = isPortrait ? Math.min(words.length, OVERVIEW_MAX_WORDS_PORT) :
                      Math.min(words.length, OVERVIEW_MAX_WORDS_LAND);

            String shortened = String.join(" ", Arrays.copyOfRange(words, 0, end));

            if (shortened.length() <= overview.length()) {
                shortened += shortened.charAt(shortened.length() - 1) == '.' ? ".." : "...";
            }

            return shortened;
        } else {
            return overview;
        }
    }
}
