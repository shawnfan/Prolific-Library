package com.xiaoyaoworm.prolificlibrary.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.client.RestClient;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {

    public static final String PLEASE_TYPE_BOOK_TITLE_THERE = "Please type book title there.";
    public static final String PLEASE_TYPE_AUTHOR_THERE = "Please type author there.";
    public static final String PLEASE_TYPE_PUBLISHER_THERE = "Please type publisher there.";
    public static final String PLEASE_TYPE_CATEGORIES_THERE = "Please type categories there.";

    public static final String ADD_BOOK_ERROR = "ADD_BOOKS_ERROR";
    public static final String RESPONSE_FAILURE = "Response failure";
    public static final String RESPONSE_STATUS_CODE = "Response status code: ";
    public static final String ADD_BOOKS_RESPONSE_CODE = "addBook response code";
    public static final String ADD_BOOK = "Add book <";
    public static final String SUCCESSFULLY = "> successfully";
    public static final String ADD_BOOK_FAILED_PLEASE_CHECK_THE_LOG = "Add book failed, please check the log.";


    public EditText editBookTitle;
    public EditText editAuthor;
    public EditText editPublisher;
    public EditText editCategories;
    public Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(AddBookActivity.this);
            }
        });

        editBookTitle = (EditText) findViewById(R.id.editBookTitle);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        editPublisher = (EditText) findViewById(R.id.editPublisher);
        editCategories = (EditText) findViewById(R.id.editCategories);
        submitButton = (Button) findViewById(R.id.button_submit);
    }

    public void submit(View view) {
        if (!isOnline()) {
            Toast.makeText(this, Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_LONG).show();
        } else {
            if (editBookTitle.getText() == null || "".equals(editBookTitle.getText().toString().trim())) {
                Toast.makeText(this.getBaseContext(), PLEASE_TYPE_BOOK_TITLE_THERE, Toast.LENGTH_LONG).show();
            } else if (editAuthor.getText() == null || "".equals(editAuthor.getText().toString().trim())) {
                Toast.makeText(this.getBaseContext(), PLEASE_TYPE_AUTHOR_THERE, Toast.LENGTH_LONG).show();
            } else if (editPublisher.getText() == null || "".equals(editPublisher.getText().toString().trim())) {
                Toast.makeText(this.getBaseContext(), PLEASE_TYPE_PUBLISHER_THERE, Toast.LENGTH_LONG).show();
            } else if (editCategories.getText() == null || "".equals(editCategories.getText().toString().trim())) {
                Toast.makeText(this.getBaseContext(), PLEASE_TYPE_CATEGORIES_THERE, Toast.LENGTH_LONG).show();
            } else {
                Book book = new Book();
                book.setTitle(editBookTitle.getText().toString().trim());
                book.setAuthor(editAuthor.getText().toString().trim());
                book.setPublisher(editPublisher.getText().toString().trim());
                book.setCategories(editCategories.getText().toString().trim());

                /********* Call post Book API to add one book into list  ********/
                LibraryService libraryServiceAPI = RestClient.getClient();
                Call<Book> addBookCall = libraryServiceAPI.addBook(book);
                addBookCall.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        Log.d(ADD_BOOKS_RESPONSE_CODE, RESPONSE_STATUS_CODE + response.code());
                        if (response.isSuccessful()) {
                            Book added = response.body();
                            if (added != null) {
                                Toast.makeText(getBaseContext(), ADD_BOOK + added.getTitle() + SUCCESSFULLY, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            Log.e(ADD_BOOK_ERROR, String.valueOf(response.code()));
                            Toast.makeText(getBaseContext(), ADD_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(ADD_BOOK_ERROR, RESPONSE_FAILURE);
                        Log.e(ADD_BOOK_ERROR, "Exception: " + t.getMessage());
                        Toast.makeText(getBaseContext(), ADD_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void done(View view) {
        super.onBackPressed();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
