package com.xiaoyaoworm.prolificlibrary.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.client.RestClient;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;
import com.xiaoyaoworm.prolificlibrary.ui.BooksAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String LIST_BOOKS_ERROR = "LIST_BOOKS_ERROR";
    public static final String DELETE_ALL_ERROR = "DELETE_ALL_ERROR";
    public static final String RESPONSE_FAILURE = "Response failure";
    public static final String RESPONSE_STATUS_CODE = "Response status code: ";
    public static final String LIST_BOOKS_RESPONSE_CODE = "listBooks response code";
    public static final String DELETE_ALL_SUCCESSFULLY = "Delete all books successfully.";
    public static final String DELETE_ALL_BOOKS = "Delete All books";
    public static final String ARE_YOU_SURE_YOU_WANT_TO_DELETE_ALL_BOOKS = "Are you sure you want to delete all books?";
    public static final String GETTING_BOOK_LIST = "Getting book list";
    public static final String PLEASE_WAIT = "Please wait...";
    public static final String DELETE_ALL_BOOKS1 = "Delete all books";
    public static final String PLEASE_WAIT1 = "Please wait...";
    public static final String DELETE_ALL_BOOKS_FAILED_PLEASE_CHECK_LOG = "Delete all books failed, please check log.";
    public static final String LIST_ALL_BOOKS_FAILED_PLEASE_CHECK_LOG = "List all books failed, please check log.";


    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.bookList);

        if (!isOnline()) {
            Toast.makeText(this, Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOnline()) {
            Toast.makeText(this, Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_LONG).show();
        } else {
            refreshBookList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            // Go into add Book Activity
            Intent addBookIntent = new Intent(this, AddBookActivity.class);
            this.startActivity(addBookIntent);
            return true;
        } else if (id == R.id.action_deleteAll) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle(DELETE_ALL_BOOKS)
                    .setMessage(ARE_YOU_SURE_YOU_WANT_TO_DELETE_ALL_BOOKS)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (!isOnline()) {
                                Toast.makeText(getParent(), Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_LONG).show();
                            } else {
                                deleteAll();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshBookList() {
        final ProgressDialog loading = ProgressDialog.show(this, GETTING_BOOK_LIST, PLEASE_WAIT, false, false);
        /********* Call get Book API to get all book list  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<ArrayList<Book>> listBooksCall = libraryServiceAPI.listBooks();
        listBooksCall.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                Log.d(LIST_BOOKS_RESPONSE_CODE, RESPONSE_STATUS_CODE + response.code());
                loading.dismiss();
                if (response.isSuccessful()) {
                    ArrayList<Book> books = response.body();
                    // Set response Books as listed layout
                    BooksAdapter booksAdapter = new BooksAdapter(getBaseContext(), R.layout.book_layout, books);
                    listView.setAdapter(booksAdapter);
                } else {
                    Log.d(LIST_BOOKS_ERROR, String.valueOf(response.code()));
                    Toast.makeText(getBaseContext(), LIST_ALL_BOOKS_FAILED_PLEASE_CHECK_LOG, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                loading.dismiss();
                Log.d(LIST_BOOKS_ERROR, RESPONSE_FAILURE);
                Toast.makeText(getBaseContext(), LIST_ALL_BOOKS_FAILED_PLEASE_CHECK_LOG, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteAll() {
        final ProgressDialog loading = ProgressDialog.show(this, DELETE_ALL_BOOKS1, PLEASE_WAIT1, false, false);
        /********* Call delete all API to delete all books from list  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Void> deleteAllCall = libraryServiceAPI.deleteAll();
        deleteAllCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(LIST_BOOKS_RESPONSE_CODE, RESPONSE_STATUS_CODE + response.code());
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getBaseContext(), DELETE_ALL_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                        refreshBookList();
                    }
                } else {
                    Log.d(DELETE_ALL_ERROR, String.valueOf(response.code()));
                    Toast.makeText(getBaseContext(), DELETE_ALL_BOOKS_FAILED_PLEASE_CHECK_LOG, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.dismiss();
                Log.d(DELETE_ALL_ERROR, RESPONSE_FAILURE);
                Toast.makeText(getBaseContext(), DELETE_ALL_BOOKS_FAILED_PLEASE_CHECK_LOG, Toast.LENGTH_LONG).show();
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
