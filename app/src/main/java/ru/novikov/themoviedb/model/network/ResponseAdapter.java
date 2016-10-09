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
 */

public class ResponseAdapter {


    private static final String RESULTS_KEY = "results";

    public Movie parseMovieDetail(JSONObject jsonObject) {
        Movie movie = new Movie();
        movie.id = jsonObject.optInt("id");
        movie.backdropPath = jsonObject.optString("backdrop_path");
        movie.title = jsonObject.optString("title");
        movie.tagline = jsonObject.optString("tagline");
        movie.overview = jsonObject.optString("overview");
        movie.voteAverage = jsonObject.optDouble("vote_average");

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

    public ProductionCountry parseProductionCountrie(JSONObject jsonObject) {
        ProductionCountry productionCountry = new ProductionCountry();
        productionCountry.iso31661 = jsonObject.optString("iso_3166_1");
        productionCountry.name = jsonObject.optString("name");
        return productionCountry;
    }

    public Genre parseGenre(JSONObject jsonObject) {
        Genre genre = new Genre();
        genre.id = jsonObject.optInt("id");
        genre.name = jsonObject.optString("name");
        return genre;
    }

    public List<Movie> parseMoviesList(JSONObject jsonObject) {

        List<Movie> foundItems = new ArrayList<Movie>();

        JSONArray items = jsonObject.optJSONArray(RESULTS_KEY);

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.optJSONObject(i);
            if (obj != null) {
                foundItems.add(parseMovieDetail(obj));
            }
        }

        return foundItems;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap resizeBitmap(InputStream networkInputStream, int reqWidth, int reqHeight) {

        final BufferedInputStream is = new BufferedInputStream(networkInputStream);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(networkInputStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
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
                float ratio = 0;
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
            throw new RuntimeException(e); // this shouldn't happen
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }

    }
}
