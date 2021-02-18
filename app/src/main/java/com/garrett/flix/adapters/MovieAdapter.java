/*
 * author: Garrett
 * date: 2/13/2021
 * project: Flix
 * description:
 */
package com.garrett.flix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.garrett.flix.R;
import com.garrett.flix.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;
    Integer orientationRid;
    boolean usePoster;

    public MovieAdapter (Context context, List<Movie> movies, Integer rid) {
        this.context = context;
        this.movies = movies;
        this.orientationRid = rid;

        usePoster = rid == R.layout.item_movie_port;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(this.context).inflate(this.orientationRid, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        Movie movie = this.movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount () {
        return this.movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind (Movie movie) {
            String img = usePoster ? movie.getPosterPath() : movie.getBackdropPath();

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview(usePoster));
            Glide.with(context).load(img).placeholder(R.drawable.placeholder).into(ivPoster);
        }
    }
}