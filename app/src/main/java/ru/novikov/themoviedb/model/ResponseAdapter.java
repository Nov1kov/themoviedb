package ru.novikov.themoviedb.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.themoviedb.model.Entity.Genre;
import ru.novikov.themoviedb.model.Entity.Movie;
import ru.novikov.themoviedb.model.Entity.ProductionCountry;

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

}
