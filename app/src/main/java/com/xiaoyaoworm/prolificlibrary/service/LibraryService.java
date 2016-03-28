package com.xiaoyaoworm.prolificlibrary.service;

import com.xiaoyaoworm.prolificlibrary.pojo.Book;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by xiaoyaoworm on 3/20/16.
 */
public interface LibraryService {

    @GET("books")
    Call<ArrayList<Book>> listBooks();

    @POST("books")
    Call<Book> addBook(@Body Book book);

    @GET("books/{id}")
    Call<Book> getBookInfo(@Path("id") int bookId);

    @DELETE("books/{id}")
    Call<Void> deleteBook(@Path("id") int bookId);

    @PUT("books/{id}")
    Call<Book> updateBook(@Path("id") int bookId , @Body Book book);
}
