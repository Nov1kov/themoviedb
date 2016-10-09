package ru.novikov.themoviedb.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 08.10.2016.
 */

public class Movie {

    public Boolean adult;
    public String backdropPath;
    public BelongsToCollection belongsToCollection;
    public Integer budget;
    public List<Genre> genres = new ArrayList<>();
    public String homepage;
    public Integer id;
    public String imdbId;
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public Double popularity;
    public String posterPath;
    public List<ProductionCompany> productionCompanies = new ArrayList<>();
    public List<ProductionCountry> productionCountries = new ArrayList<>();
    public String releaseDate;
    public Integer revenue;
    public Integer runtime;
    public List<SpokenLanguage> spokenLanguages = new ArrayList<>();
    public String status;
    public String tagline;
    public String title;
    public Boolean video;
    public Double voteAverage;
    public Integer voteCount;

}