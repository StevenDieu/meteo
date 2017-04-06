package com.fges.meteo.data.manager;

import android.util.Log;

import com.fges.meteo.data.ServiceGenerator;
import com.fges.meteo.data.model.CategoriesFromServer;
import com.fges.meteo.data.model.Category;
import com.fges.meteo.data.service.BeerService;
import com.fges.meteo.ui.activity.HomeActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maxime on 23/03/2017.
 */

public class BeerManagerV2 {

    private static final BeerManagerV2 ourInstance = new BeerManagerV2();

    public static BeerManagerV2 getInstance() {
        return ourInstance;
    }

    private BeerManagerV2() {}

    public void getCategories(final HomeActivity.MyCallback myCallback) {

        BeerService beerService = ServiceGenerator.createService(BeerService.class);

        Call<CategoriesFromServer> call = beerService.getCategories();

        call.enqueue(new Callback<CategoriesFromServer>() {
            @Override
            public void onResponse(Call<CategoriesFromServer> call, Response<CategoriesFromServer> response) {

                if (response.isSuccessful()) {
                    CategoriesFromServer categoriesFromServer = response.body();

                    List<Category> categories = categoriesFromServer.getData();

                    for (int i = 0; i < categories.size(); i++) {
                        Log.d("Categorie", categories.get(i).getName());
                    }
                    myCallback.onSuccess(categories);

                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<CategoriesFromServer> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }
}
