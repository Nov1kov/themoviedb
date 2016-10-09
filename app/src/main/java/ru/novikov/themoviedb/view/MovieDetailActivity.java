package ru.novikov.themoviedb.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.Entity.Movie;
import ru.novikov.themoviedb.presenter.MovieDetailPresenter;
import ru.novikov.themoviedb.presenter.MovieDetailPresenterImpl;

public class MovieDetailActivity extends BaseActivity<MovieDetailPresenter> implements MovieDetailView{

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    @Override
    protected MovieDetailPresenterImpl createInstancePresenter() {
        return new MovieDetailPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter.loadMovie(movieId);
    }

    @Override
    public void updateInfo(Movie movie) {

    }

    @Override
    public void showLoading(boolean shown) {

    }

    @Override
    public void showError(String msg) {

    }
}
