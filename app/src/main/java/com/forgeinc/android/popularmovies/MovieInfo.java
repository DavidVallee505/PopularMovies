package com.forgeinc.android.popularmovies;


public class MovieInfo {
    int id;
    String originalTitle;
    String posterPath;
    String synopsis;
    String releaseDate;
    Double voteAverage;

    public MovieInfo(int id, String originalTitle, String posterPath, String synopsis,
                     String releaseDate, Double voteAverage)
    {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }
}
