package ru.novikov.themoviedb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.entity.Movie;

/**
 * Created by Ivan on 09.10.2016.
 */

public class PopulateMoviesListAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private static final int VISIBLE_THRESHOLD = 5;

    private List<Movie> mPopulateMovies = new ArrayList<>();
    private OnClickListListener mListItemClickListener;
    private boolean mIsLoading;
    private int mProgressBarItemPosition = -1;

    public void showProgressBar() {
        mIsLoading = true;
        mPopulateMovies.add(null);
        mProgressBarItemPosition = mPopulateMovies.size() - 1;
        notifyItemInserted(mProgressBarItemPosition);
    }

    public int getProgressItemPosition() {
        return mProgressBarItemPosition;
    }

    public interface OnClickListListener {
        void onListClick(Movie movie);
    }

    public void updateList(List<Movie> movieList){
        int oldPos = mPopulateMovies.size() - 1;
        mPopulateMovies.addAll(movieList);
        notifyItemRangeInserted(oldPos, movieList.size());
        //notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mPopulateMovies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new MovieViewHolder(v);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_list_item, parent, false);
            return new RecyclerView.ViewHolder(v) {};
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Movie movie = mPopulateMovies.get(position);
            movieViewHolder.bind(movie);
            movieViewHolder.setmListItemClickListener(new OnClickListListener() {
                @Override
                public void onListClick(Movie movie) {
                    mListItemClickListener.onListClick(movie);
                }
            });
        } /*else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }*/
    }

    public void setListItemClickListener(OnClickListListener listItemClickListener) {
        mListItemClickListener = listItemClickListener;
    }

    public boolean needLoadedMore(int totalItemCount, int lastVisibleItem) {
        return !mIsLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD);
    }

    public void setLoaded() {
        if (getItemViewType(mPopulateMovies.size() - 1) == VIEW_TYPE_LOADING){
            mPopulateMovies.remove(mPopulateMovies.size() - 1);
            notifyItemRemoved(mPopulateMovies.size());
            mProgressBarItemPosition = -1;
        }
        mIsLoading = false;
    }

    @Override
    public int getItemCount() {
        return mPopulateMovies != null ? mPopulateMovies.size() : 0;
    }
}
