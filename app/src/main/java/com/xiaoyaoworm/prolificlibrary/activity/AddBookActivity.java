package com.xiaoyaoworm.prolificlibrary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        editBookTitle = (EditText) findViewById(R.id.editBookTitle);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        editPublisher = (EditText) findViewById(R.id.editPublisher);
        editCategories = (EditText) findViewById(R.id.editCategories);
        submitButton = (Button) findViewById(R.id.button_submit);
    }

    public void submit(View view) {
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
            Gson gson = new GsonBuilder()
                    .setDateFormat(Constant.DATE_FORMAT)
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            LibraryService libraryServiceAPI = retrofit.create(LibraryService.class);
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
                        Log.d(ADD_BOOK_ERROR, String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Log.d(ADD_BOOK_ERROR, RESPONSE_FAILURE);
                }
            });
        }
    }
}
