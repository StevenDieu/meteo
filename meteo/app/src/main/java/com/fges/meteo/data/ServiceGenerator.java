package com.fges.meteo.data;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by steven on 23/03/2017.
 */

public class ServiceGenerator {
    private static final String SECRET_KEY = "c0182117c391ba46c720bf9119d0b682";
    private static final String BASE_URL = "https://api.darksky.net/";
    private static final String COMMA = ",";

    private Double mLatitude;
    private Double mLongitude;
    private Long mDateTime;

    private Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(configureHttpClient())
                    .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();


    private OkHttpClient configureHttpClient() {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();

                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addPathSegment(SECRET_KEY)
                        .addPathSegment(mLatitude + COMMA + mLongitude + COMMA + mDateTime)
                        .addQueryParameter("exclude", "flags")
                        .addQueryParameter("lang", "fr")
                        .build();

                final Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    public <S> S createService(final Class<S> serviceClass, final Double latitude,
                               final Double longitude, final Long dateTime) {
        mLatitude = latitude;
        mLongitude = longitude;
        mDateTime = dateTime / 1000L;
        return retrofit.create(serviceClass);
    }

}
