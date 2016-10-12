package ru.novikov.themoviedb.model.network;

/**
 * Created by inovikov on 12.10.2016.
 */

public class RequestFactory {

    private static final String API_KEY = "72b56103e43843412a992a8d64bf96e9";

    private static final String SERVICE_URL = "https://api.themoviedb.org/";
    private static final String IMAGES_URL = "https://image.tmdb.org/";
    private static final String IMAGE_PARAMS_QUERY = "t/p/w500";
    private static final String REST_VERSION = "3/";

    private static final String REST_MOVIE_KEY = "movie/";
    private static final String REST_MOVIE_POPULAR_KEY = "popular";
    private static final String REST_PAGE_QUERY = "&page=";
    private static final String REST_LANGUAGE_QUERY = "&language=";
    private static final String REST_API_KEY = "?api_key=";

    private static final String LANFUAGE_QUERY_EN = "en-US";

    private final String mLanguage;

    public RequestFactory() {
        this.mLanguage = LANFUAGE_QUERY_EN;
    }

    public String createUrlPopularMovies(String pageId) {
        return SERVICE_URL + REST_VERSION + REST_MOVIE_KEY + REST_MOVIE_POPULAR_KEY +
                REST_API_KEY + API_KEY + REST_LANGUAGE_QUERY + mLanguage +
                REST_PAGE_QUERY + pageId;
    }

    public String createUrlMovie(String movieId) {
        return SERVICE_URL + REST_VERSION + REST_MOVIE_KEY + movieId + REST_API_KEY + API_KEY;
    }

    public String createUrlImage(String imageUrl) {
        return IMAGES_URL + IMAGE_PARAMS_QUERY + imageUrl;
    }
}
