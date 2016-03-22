package com.xiaoyaoworm.prolificlibrary.service;

import com.xiaoyaoworm.prolificlibrary.pojo.Book;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by xiaoyaoworm on 3/20/16.
 */
public interface LibraryService {

    @GET("books")
    Call<ArrayList<Book>> listBooks();

    @POST("books")
    Call<Book> addBook(@Body Book book);

}
