package ru.novikov.themoviedb.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.DataProviderCallBacks;
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
    private int mMovieId;
    private Scene scene2;
    private View mTitleContainer;
    private View mProgressBar;

    @Override
    protected MovieDetailPresenterImpl createInstancePresenter() {
        return new MovieDetailPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setTypeInfoReceiver(DataProviderCallBacks.TYPE_INFO_MOVIE_DETAIL);
        setContentView(R.layout.activity_movie_detail);
        setTitle(null);

        mMovieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleContainer = findViewById(R.id.title_container);
        mProgressBar = findViewById(R.id.progress_bar);

        mBackdrop = (ImageView) findViewById(R.id.backdrop);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mTaglineTextView = (TextView) findViewById(R.id.tagline);
        mOverviewTextView = (TextView) findViewById(R.id.overview);

        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.root_scene);
        View scene2View = getLayoutInflater().inflate(R.layout.detailed_movie_scene, sceneRoot, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scene2 = new Scene(sceneRoot, scene2View);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            scene2 = new Scene(sceneRoot, (ViewGroup) scene2View);
        } else {
            ViewGroup parent = (ViewGroup) sceneRoot.getParent();
            int index = parent.indexOfChild(sceneRoot);
            parent.removeView(sceneRoot);
            parent.addView(scene2View, index);
        }

        mGenresTextView = (TextView) scene2View.findViewById(R.id.genres);
        mGenresTitle = scene2View.findViewById(R.id.genres_title);
        mCountriesTextView = (TextView) scene2View.findViewById(R.id.countries);
        mCountriesTitle = scene2View.findViewById(R.id.countries_title);
        mHomePageButton = scene2View.findViewById(R.id.homepage);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.loadMovie(mMovieId);
    }

    @Override
    public void updateInfo(Movie movie) {

        mTitleContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

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

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        mHomePageButton.startAnimation(logoMoveAnimation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            TransitionSet set = new TransitionSet();
            set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
            Fade fadeAnim = new Fade();
            fadeAnim.addTarget(R.id.genres).addTarget(R.id.genres_title).
                    addTarget(R.id.countries).addTarget(R.id.countries_title);
            set.addTransition(fadeAnim);
            set.setDuration(1000);
            TransitionManager.go(scene2, set);
        }

        mPresenter.loadBackdrop(movie.backdropPath, mBackdrop.getWidth(), mBackdropHeight);
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
                ActivityCompat.finishAfterTransition(this); //finish(); //NavUtils.navigateUpFromSameTask(this);
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
