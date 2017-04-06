package com.fges.meteo.data.model;

import java.util.Date;

/**
 * Created by Maxime on 23/03/2017.
 */

public class Category {

    private int id;
    private String name;
    private String createDate;

    public Category() {}

    public Category(int id, String name, String createDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
