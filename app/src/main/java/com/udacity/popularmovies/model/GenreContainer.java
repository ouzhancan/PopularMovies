package com.udacity.popularmovies.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GenreContainer {
    public static List<Genre> genreList = new ArrayList();

    static {
        genreList.add(new Genre(28, "Action"));
        genreList.add(new Genre(12, "Adventure"));
        genreList.add(new Genre(16, "Animation"));
        genreList.add(new Genre(35, "Comedy"));
        genreList.add(new Genre(80, "Crime"));
        genreList.add(new Genre(99, "Documentary"));
        genreList.add(new Genre(18, "Drama"));
        genreList.add(new Genre(10751, "Family"));
        genreList.add(new Genre(14, "Fantasy"));
        genreList.add(new Genre(36, "History"));
        genreList.add(new Genre(27, "Horror"));
        genreList.add(new Genre(10402, "Music"));
        genreList.add(new Genre(9648, "Mystery"));
        genreList.add(new Genre(10749, "Romance"));
        genreList.add(new Genre(878, "Science Fiction"));
        genreList.add(new Genre(10770, "TV Movie"));
        genreList.add(new Genre(53, "Thriller"));
        genreList.add(new Genre(10752, "War"));
        genreList.add(new Genre(37, "Western"));
    }


    public static List<Genre> getGenreList() {
        // generateGenreList();
        return genreList;
    }

    @Nullable
    public static Genre findGenre(String index) {
        Genre returnedGenre = null;
        for (Genre genre : genreList) {
            if (genre.getId() == Integer.parseInt(index)) {
                returnedGenre = genre;
                break;
            }
        }

        return returnedGenre;
    }

    @Nullable
    public static Genre findId(String genreName) {
        Genre returnedGenre = null;
        for (Genre genre : genreList) {
            if (genre.getName().equalsIgnoreCase(genreName.trim())) {
                returnedGenre = genre;
                break;
            }
        }

        return returnedGenre;
    }
}
