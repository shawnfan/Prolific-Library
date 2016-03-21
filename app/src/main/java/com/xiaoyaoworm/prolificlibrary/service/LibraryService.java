package com.xiaoyaoworm.prolificlibrary.service;

import com.xiaoyaoworm.prolificlibrary.pojo.Book;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by xiaoyaoworm on 3/20/16.
 */
public interface LibraryService {

    @GET("/books")
    Call<ArrayList<Book>> listBooks();

}
