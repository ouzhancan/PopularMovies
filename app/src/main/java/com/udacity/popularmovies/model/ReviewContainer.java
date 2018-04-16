package com.udacity.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class ReviewContainer {
    String id;
    ArrayList<Review> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }
}
