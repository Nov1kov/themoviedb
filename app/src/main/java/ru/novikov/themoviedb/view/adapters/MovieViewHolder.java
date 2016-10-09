package ru.novikov.themoviedb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.BreakIterator;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.Entity.Movie;

/**
 * Created by Ivan on 09.10.2016.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mBackDrop;
    private final TextView mTitle;
    private final TextView mOverview;
    private final TextView mYear;
    private final TextView mRating;

    private Movie mMovieEntity;

    private PopulateMoviesListAdapter.OnClickListListener mListItemClickListener;

    public MovieViewHolder(View itemView) {
        super(itemView);

        mBackDrop = (ImageView) itemView.findViewById(R.id.backdrop);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mOverview = (TextView) itemView.findViewById(R.id.overview);
        mYear = (TextView) itemView.findViewById(R.id.year);
        mRating = (TextView) itemView.findViewById(R.id.rating);
    }

    public void bind(Movie movie) {
        mMovieEntity = movie;
        mTitle.setText(movie.title);
        mYear.setText(movie.releaseDate);
        mOverview.setText(movie.overview);
        mRating.setText(String.format("%.1f", movie.voteAverage));
    }

    public void setmListItemClickListener(PopulateMoviesListAdapter.OnClickListListener listItemClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovieEntity != null) {
                    mListItemClickListener.onListClick(mMovieEntity);
                }
            }
        });
        mListItemClickListener = listItemClickListener;
    }

}
