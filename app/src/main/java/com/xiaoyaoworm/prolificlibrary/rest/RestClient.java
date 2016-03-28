package com.xiaoyaoworm.prolificlibrary.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiaoyaoworm on 3/20/16.
 */

public class RestClient {

    private static LibraryService libraryService;

    public static LibraryService getClient() {
        if (libraryService == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat(Constant.DATE_FORMAT)
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            libraryService = retrofit.create(LibraryService.class);
        }
        return libraryService;
    }

}
