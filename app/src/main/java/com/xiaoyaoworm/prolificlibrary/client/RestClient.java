package com.xiaoyaoworm.prolificlibrary.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Leon Jiang(xiaoyaoworm) on 3/20/16.
 * https://github.com/xiaoyaoworm
 */

public class RestClient {

    private static LibraryService libraryService;

    public static LibraryService getClient() {
        if (libraryService == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat(Constant.DATE_FORMAT)
                    .create();

            // Add logging into retrofit 2.0
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.interceptors().add(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build()).build();

            libraryService = retrofit.create(LibraryService.class);
        }
        return libraryService;
    }

}
