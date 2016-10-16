package ru.novikov.themoviedb.view.baseviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.*;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.presenter.basepresenters.MoviesListPresenter;
import ru.novikov.themoviedb.view.MovieDetailActivity;
import ru.novikov.themoviedb.view.adapters.MoviesListAdapter;
import ru.novikov.themoviedb.view.utils.UiTools;

/**
 * Created by Ivan on 16.10.2016.
 */

abstract public class BaseListActivity extends BaseActivity<MoviesListPresenter>
        implements MoviesListAdapter.OnClickListListener, MoviesListView {

    protected RecyclerView mListView;
    protected MoviesListAdapter mListAdapter;
    protected GridLayoutManager mGridLayoutManager;
    protected TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        mListView = (RecyclerView) findViewById(R.id.recycler_view);
        mListAdapter = new MoviesListAdapter();
        mListAdapter.setListItemClickListener(this);
        mListView.setAdapter(mListAdapter);
        mGridLayoutManager = new GridLayoutManager(this,
                UiTools.getColumnCount(this, getResources().getDimensionPixelSize(R.dimen.movie_item_min_column_width)));
        mGridLayoutManager.setAutoMeasureEnabled(true);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == (mListAdapter.getProgressItemPosition())) ? mGridLayoutManager.getSpanCount() : 1;
            }
        });
        mListView.setLayoutManager(mGridLayoutManager);
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = mGridLayoutManager.getItemCount();
                int lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();

                if (mListAdapter.needLoadedMore(totalItemCount, lastVisibleItem) &&
                        mPresenter.loadMore()) {
                    mListAdapter.showProgressBar();
                }
            }
        });
        mEmptyTextView = (TextView) findViewById(R.id.emptyText);
    }

    @Override
    public void showLoading(boolean shown) {

    }

    @Override
    public void clearList() {
        mListAdapter.clear();
    }

    @Override
    public void updateList(List<Movie> movieList) {
        emptyList(mListAdapter.getItemCount() == 0 && movieList.size() == 0);
        mListAdapter.updateList(movieList);
    }

    protected void emptyList(boolean state) {
        mListView.setVisibility(!state ? View.VISIBLE : View.GONE);
        mEmptyTextView.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onListClick(Movie movie, android.view.View moviePoster, android.view.View ratingView) {
        if (movie.id != null) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id);
            Pair<android.view.View, String> p1 = Pair.create(moviePoster, mPosterTransitionName);
            Pair<android.view.View, String> p2 = Pair.create(ratingView, mRatingTransitionName);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, p1, p2);
            ActivityCompat.startActivity(this, intent, options.toBundle());

        }
    }
}
