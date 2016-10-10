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

public class MoviesListActivity extends BaseActivity<MoviesListPresenter> implements MoviesListView, PopulateMoviesListAdapter.OnClickListListener {

    private RecyclerView mListView;
    private PopulateMoviesListAdapter mListAdapter;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        mListView.setLayoutManager(gridLayoutManager);
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
