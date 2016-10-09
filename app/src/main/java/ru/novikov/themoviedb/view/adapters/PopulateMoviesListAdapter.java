package ru.novikov.themoviedb.view.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.Entity.Movie;

/**
 * Created by Ivan on 09.10.2016.
 */

public class PopulateMoviesListAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    @Nullable
    private List<Movie> mPopulateMovies;

    private OnClickListListener mListItemClickListener;

    public interface OnClickListListener {
        void onListClick(Movie movie);
    }

    public void updateList(List<Movie> movieList){
        mPopulateMovies = movieList;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mPopulateMovies != null) {
            Movie movie = mPopulateMovies.get(position);
            holder.bind(movie);
            holder.setmListItemClickListener(new OnClickListListener() {
                @Override
                public void onListClick(Movie movie) {
                    mListItemClickListener.onListClick(movie);
                }
            });
        }
    }

    public void setListItemClickListener(OnClickListListener listItemClickListener) {
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mPopulateMovies != null ? mPopulateMovies.size() : 0;
    }
}
