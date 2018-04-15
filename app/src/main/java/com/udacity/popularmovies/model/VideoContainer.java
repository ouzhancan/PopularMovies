package com.udacity.popularmovies.model;

import java.util.List;

public class VideoContainer {

    String id;
    List<Video> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
