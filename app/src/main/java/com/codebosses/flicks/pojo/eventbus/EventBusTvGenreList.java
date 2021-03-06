package com.codebosses.flicks.pojo.eventbus;

import com.codebosses.flicks.pojo.moviespojo.moviedetail.Genre;

import java.util.List;

public class EventBusTvGenreList {

    private List<Genre> genreList;

    public EventBusTvGenreList(List<Genre> genreList) {
        setGenreList(genreList);
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }
}
