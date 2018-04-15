package com.udacity.popularmovies.model;

import java.util.List;

public class ReviewContainer {
    String id;
    List<Review> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
