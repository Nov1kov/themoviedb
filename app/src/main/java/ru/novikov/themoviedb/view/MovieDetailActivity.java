package ru.novikov.themoviedb.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.entity.Genre;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.entity.ProductionCountry;
import ru.novikov.themoviedb.presenter.basepresenters.MovieDetailPresenter;
import ru.novikov.themoviedb.presenter.MovieDetailPresenterImpl;
import ru.novikov.themoviedb.view.baseviews.BaseActivity;
import ru.novikov.themoviedb.view.baseviews.MovieDetailView;

public class MovieDetailActivity extends BaseActivity<MovieDetailPresenter> implements MovieDetailView {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private View mHomePageButton;
    private TextView mCountriesTextView;
    private TextView mRatingTextView;
    private TextView mTitleTextView;
    private TextView mTaglineTextView;
    private TextView mOverviewTextView;
    private TextView mGenresTextView;
    private View mGenresTitle;
    private View mCountriesTitle;
    private ImageView mBackdrop;
    private int mBackdropHeight;

    @Override
    protected MovieDetailPresenterImpl createInstancePresenter() {
        return new MovieDetailPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(null);

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBackdrop = (ImageView) findViewById(R.id.backdrop);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mTaglineTextView = (TextView) findViewById(R.id.tagline);
        mOverviewTextView = (TextView) findViewById(R.id.overview);
        mGenresTextView = (TextView) findViewById(R.id.genres);
        mGenresTitle = findViewById(R.id.genres_title);
        mCountriesTextView = (TextView) findViewById(R.id.countries);
        mCountriesTitle = findViewById(R.id.countries_title);
        mHomePageButton = findViewById(R.id.homepage);
        mHomePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter.getCurrentMovie() != null) {
                    String url = mPresenter.getCurrentMovie().homepage;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        mBackdropHeight = getResources().getDimensionPixelSize(R.dimen.movie_detail_backdrop_height);
        mPresenter.loadMovie(movieId);
    }

    @Override
    public void updateInfo(Movie movie) {

        mTitleTextView.setText(movie.title);
        mRatingTextView.setText(String.format("%.1f", movie.voteAverage));
        mTaglineTextView.setText(movie.tagline);
        mOverviewTextView.setText(movie.overview);

        if (movie.genres != null && movie.genres.size() > 0) {
            mGenresTitle.setVisibility(View.VISIBLE);
            mGenresTextView.setVisibility(View.VISIBLE);
            String genresNames = "";
            for (Genre genre : movie.genres) {
                genresNames += genre.name + "\n";
            }
            mGenresTextView.setText(genresNames);
        } else {
            mGenresTitle.setVisibility(View.GONE);
            mGenresTextView.setVisibility(View.GONE);
        }

        if (movie.productionCountries != null && movie.productionCountries.size() > 0) {
            mCountriesTextView.setVisibility(View.VISIBLE);
            mCountriesTitle.setVisibility(View.VISIBLE);
            String countriesNames = "";
            for (ProductionCountry country : movie.productionCountries) {
                countriesNames += country.name + "\n";
            }
            mCountriesTextView.setText(countriesNames);
        } else {
            mCountriesTextView.setVisibility(View.GONE);
            mCountriesTitle.setVisibility(View.GONE);
        }

        mHomePageButton.setVisibility(TextUtils.isEmpty(movie.homepage) ?
                        View.GONE :
                        View.VISIBLE);

        mPresenter.loadBackdrop(movie.posterPath, mBackdrop.getWidth(), mBackdropHeight);
    }

    @Override
    public void updateBackdrop(Bitmap bitmap) {
        mBackdrop.setImageBitmap(bitmap);
    }

    @Override
    public void showLoading(boolean shown) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachListener();
    }
}
