package com.xiaoyaoworm.prolificlibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksDetailActivity extends AppCompatActivity {

    public static final String GET_BOOK_INFO_ERROR = "GET_BOOK_INFO_ERROR";
    public static final String DELETE_BOOK_ERROR = "DELETE_BOOK_ERROR";
    public static final String RESPONSE_FAILURE = "Response failure";
    public static final String RESPONSE_STATUS_CODE = "Response status code: ";
    public static final String GET_BOOK_INFO_RESPONSE_CODE = "book info response code";
    public static final String DELETE_BOOK_RESPONSE_CODE = "delete response code";

    public TextView bookTitleText;
    public TextView bookAuthorText;
    public TextView bookPublisherText;
    public TextView bookTagText;
    public TextView bookLastCheckoutText;
    public TextView bookLastCheckoutByText;
    public Button checkoutButton;
    public Button deleteButton;

    public int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookTitleText = (TextView) findViewById(R.id.bookTitleText);
        bookAuthorText = (TextView) findViewById(R.id.bookAuthorText);
        bookPublisherText = (TextView) findViewById(R.id.bookPublisherText);
        bookTagText = (TextView) findViewById(R.id.bookTagText);
        bookLastCheckoutText = (TextView) findViewById(R.id.bookLastCheckoutText);
        bookLastCheckoutByText = (TextView) findViewById(R.id.bookLastCheckoutByText);
        checkoutButton = (Button) findViewById(R.id.checkoutButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Intent booksDetailIntent = getIntent();
        bookId = booksDetailIntent.getIntExtra("bookID", -1);
        if (bookId == -1) {
            Log.d(GET_BOOK_INFO_ERROR, "bookId is wrong, please check.");
            finish();
        } else {
            getBookInfo(bookId);
        }
    }

    public void delete(View view) {
        deleteBook(bookId);
    }

    public void getBookInfo(int bookId) {
        /********* Call get Book API to get all book list  ********/
        Gson gson = new GsonBuilder()
                .setDateFormat(Constant.DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LibraryService libraryServiceAPI = retrofit.create(LibraryService.class);
        Call<Book> checkBookInfoCall = libraryServiceAPI.getBookInfo(bookId);
        checkBookInfoCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Log.d(GET_BOOK_INFO_RESPONSE_CODE, RESPONSE_STATUS_CODE + response.code());
                if (response.isSuccessful()) {
                    Book book = response.body();
                    // Set response Books as listed layout
                    bookTitleText.setText(book.getTitle());
                    bookAuthorText.setText(book.getAuthor());
                    bookPublisherText.setText("Publisher: " + book.getPublisher());
                    bookTagText.setText("Tags: " + book.getCategories());
                    if (book.getLastCheckedOut() == null && book.getLastCheckedOutBy() == null) {
                        bookLastCheckoutText.setText("Last Checkout Out:");
                        bookLastCheckoutByText.setText("NULL");
                    } else {
                        bookLastCheckoutText.setText("Last Checkout Out:");
                        bookLastCheckoutByText.setText(String.valueOf(book.getLastCheckedOutBy()) + " @ " + String.valueOf(book.getLastCheckedOut()));
                    }


                } else {
                    Log.d(GET_BOOK_INFO_ERROR, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d(GET_BOOK_INFO_ERROR, RESPONSE_FAILURE);
            }
        });
    }


    public void deleteBook(int bookId) {
        /********* Call delete Book API to delete this book from list  ********/
        Gson gson = new GsonBuilder()
                .setDateFormat(Constant.DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LibraryService libraryServiceAPI = retrofit.create(LibraryService.class);
        Call<Void> deleteBookCall = libraryServiceAPI.deleteBook(bookId);
        deleteBookCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getBaseContext(), "Delete Book Successfully.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Log.d(DELETE_BOOK_ERROR, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(DELETE_BOOK_ERROR, RESPONSE_FAILURE);
            }
        });

    }
}
