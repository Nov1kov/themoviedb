package ru.novikov.themoviedb.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.presenter.basepresenters.MoviesListPresenter;
import ru.novikov.themoviedb.presenter.MoviesListPresenterImpl;
import ru.novikov.themoviedb.view.adapters.PopulateMoviesListAdapter;
import ru.novikov.themoviedb.view.baseviews.BaseActivity;
import ru.novikov.themoviedb.view.baseviews.MoviesListView;
import ru.novikov.themoviedb.view.utils.UiTools;

public class MoviesListActivity extends BaseActivity<MoviesListPresenter> implements MoviesListView, PopulateMoviesListAdapter.OnClickListListener {

    private RecyclerView mListView;
    private PopulateMoviesListAdapter mListAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected MoviesListPresenterImpl createInstancePresenter() {
        return new MoviesListPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movies_list);
        mListView = (RecyclerView) findViewById(R.id.recycler_view);
        mListAdapter = new PopulateMoviesListAdapter();
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

                if (mListAdapter.needLoadedMore(totalItemCount, lastVisibleItem)){
                    mPresenter.loadMore();
                    mListAdapter.showProgressBar();
                }
            }
        });
        mPresenter.loadList();
    }

    @Override
    public void onListClick(Movie movie) {
        if (movie.id != null) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id);
            startActivity(intent);
        }
    }

    @Override
    public void updateList(List<Movie> movieList) {
        mListAdapter.updateList(movieList);
    }

    @Override
    public void showLoading(boolean shown) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearListeners();
    }
}
