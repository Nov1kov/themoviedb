package ru.novikov.themoviedb.model.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.novikov.themoviedb.model.entity.Genre;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.entity.ProductionCountry;

/**
 * Created by Ivan on 08.10.2016.
 * parse json to object and resize bitmap
 */
public class ResponseAdapter {


    private static final String RESULTS_KEY = "results";

    public static Movie parseMovieDetail(JSONObject jsonObject) {
        Movie movie = new Movie();
        movie.id = jsonObject.optInt("id");
        movie.backdropPath = jsonObject.optString("backdrop_path");
        movie.title = jsonObject.optString("title");
        movie.tagline = jsonObject.optString("tagline");
        movie.overview = jsonObject.optString("overview");
        movie.voteAverage = jsonObject.optDouble("vote_average");
        movie.posterPath = jsonObject.optString("poster_path");

        {
            JSONArray genres = jsonObject.optJSONArray("genres");
            if (genres != null) {
                for (int i = 0; i < genres.length(); i++) {
                    JSONObject obj = genres.optJSONObject(i);
                    if (obj != null) {
                        movie.genres.add(parseGenre(obj));
                    }
                }
            }
        }

        {
            JSONArray productionCountries = jsonObject.optJSONArray("production_countries");
            if (productionCountries != null) {
                for (int i = 0; i < productionCountries.length(); i++) {
                    JSONObject obj = productionCountries.optJSONObject(i);
                    if (obj != null) {
                        movie.productionCountries.add(parseProductionCountrie(obj));
                    }
                }
            }
        }

        movie.homepage = jsonObject.optString("homepage");
        movie.releaseDate = jsonObject.optString("release_date");

        return movie;
    }

    public static ProductionCountry parseProductionCountrie(JSONObject jsonObject) {
        ProductionCountry productionCountry = new ProductionCountry();
        productionCountry.iso31661 = jsonObject.optString("iso_3166_1");
        productionCountry.name = jsonObject.optString("name");
        return productionCountry;
    }

    public static Genre parseGenre(JSONObject jsonObject) {
        Genre genre = new Genre();
        genre.id = jsonObject.optInt("id");
        genre.name = jsonObject.optString("name");
        return genre;
    }

    public static List<Movie> parseMoviesList(JSONObject jsonObject) {

        List<Movie> foundItems = new ArrayList<>();

        JSONArray items = jsonObject.optJSONArray(RESULTS_KEY);

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.optJSONObject(i);
            if (obj != null) {
                foundItems.add(parseMovieDetail(obj));
            }
        }

        return foundItems;
    }

    /**
     * Read the image from the stream and create a bitmap scaled to the desired
     * size.  Resulting bitmap will be at least as large as the
     * desired minimum specified dimensions and will keep the image proportions
     * correct during scaling.
     */
    public static Bitmap createScaledBitmapFromStream(InputStream s, int minimumDesiredBitmapWith, int minimumDesiredBitmapHeight) {

        final BufferedInputStream is = new BufferedInputStream(s, 32 * 1024);
        try {
            final BitmapFactory.Options decodeBitmapOptions = new BitmapFactory.Options();
            // For further memory savings, you may want to consider using this option
            // decodeBitmapOptions.inPreferredConfig = Config.RGB_565; // Uses 2-bytes instead of default 4 per pixel

            if (minimumDesiredBitmapWith > 0 || minimumDesiredBitmapHeight > 0) {
                final BitmapFactory.Options decodeBoundsOptions = new BitmapFactory.Options();
                decodeBoundsOptions.inJustDecodeBounds = true;
                is.mark(32 * 1024); // 32k is probably overkill, but 8k is insufficient for some jpgs
                BitmapFactory.decodeStream(is, null, decodeBoundsOptions);
                is.reset();

                final int originalWidth = decodeBoundsOptions.outWidth;
                final int originalHeight = decodeBoundsOptions.outHeight;
                float ratio;
                if (minimumDesiredBitmapWith <= 0) {
                    ratio = (float) originalHeight / (float) minimumDesiredBitmapHeight;
                } else if (minimumDesiredBitmapHeight <= 0) {
                    ratio = (float) originalWidth / (float) minimumDesiredBitmapWith;
                } else {
                    ratio = Math.min((float) originalWidth / (float) minimumDesiredBitmapWith,
                            (float) originalHeight / (float) minimumDesiredBitmapHeight);
                }

                // inSampleSize prefers multiples of 2, but we prefer to prioritize memory savings
                decodeBitmapOptions.inSampleSize = Math.max(1, Math.round(ratio));
            }

            return BitmapFactory.decodeStream(is, null, decodeBitmapOptions);

        } catch (IOException e) {
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }

    }
}
