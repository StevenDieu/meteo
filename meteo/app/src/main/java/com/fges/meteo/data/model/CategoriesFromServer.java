package com.fges.meteo.data.model;

import java.util.List;

/**
 * Created by Maxime on 23/03/2017.
 */

public class CategoriesFromServer {

    private String message;
    private List<Category> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public CategoriesFromServer() {}

    public CategoriesFromServer(String message, List<Category> data) {
        this.message = message;
        this.data = data;
    }
}
