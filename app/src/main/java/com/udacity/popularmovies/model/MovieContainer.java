package com.udacity.popularmovies.model;

import java.util.List;

public class MovieContainer {

    int page;
    int total_pages;
    List<Movie> results;

    public MovieContainer() {
    }

    public MovieContainer(int page, int total_pages, List<Movie> results) {
        this.page = page;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
