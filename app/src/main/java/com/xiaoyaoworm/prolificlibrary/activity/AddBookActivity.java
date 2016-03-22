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
import com.xiaoyaoworm.prolificlibrary.ui.BooksAdapter;

import java.util.ArrayList;

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
                        if(added!=null) {
                            Toast.makeText(getBaseContext(), ADD_BOOK +added.getTitle()+ SUCCESSFULLY,Toast.LENGTH_LONG).show();
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





//    Call<HttpBinResponse> call = service.postWithJson(new LoginData("username", "secret"));
//
//    // Asynchronously execute HTTP request
//    call.enqueue(new Callback<HttpBinResponse>() {
//        /**
//         * onResponse is called when any kind of response has been received.
//         */
//        @Override
//        public void onResponse(Response<HttpBinResponse> response, Retrofit retrofit) {
//            // http response status code + headers
//            System.out.println("Response status code: " + response.code());
//
//            // isSuccess is true if response code => 200 and <= 300
//            if (!response.isSuccess()) {
//                // print response body if unsuccessful
//                try {
//                    System.out.println(response.errorBody().string());
//                } catch (IOException e) {
//                    // do nothing
//                }
//                return;
//            }
//
//            // if parsing the JSON body failed, `response.body()` returns null
//            HttpBinResponse decodedResponse = response.body();
//            if (decodedResponse == null) return;
//
//            // at this point the JSON body has been successfully parsed
//            System.out.println("Response (contains request infos):");
//            System.out.println("- url:         " + decodedResponse.url);
//            System.out.println("- ip:          " + decodedResponse.origin);
//            System.out.println("- headers:     " + decodedResponse.headers);
//            System.out.println("- args:        " + decodedResponse.args);
//            System.out.println("- form params: " + decodedResponse.form);
//            System.out.println("- json params: " + decodedResponse.json);
//        }
//
//        /**
//         * onFailure gets called when the HTTP request didn't get through.
//         * For instance if the URL is invalid / host not reachable
//         */
//        @Override
//        public void onFailure(Throwable t) {
//            System.out.println("onFailure");
//            System.out.println(t.getMessage());
//        }
//    });

}
