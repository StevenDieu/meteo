package com.fges.meteo.data.service;

import com.fges.meteo.data.model.CategoriesFromServer;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Maxime on 23/03/2017.
 */

public interface BeerService {

    @GET("categories")
    Call<CategoriesFromServer> getCategories();
}
