package com.xiaoyaoworm.prolificlibrary.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.client.RestClient;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Leon Jiang(xiaoyaoworm) on 3/20/16.
 * https://github.com/xiaoyaoworm
 */

public class BooksDetailActivity extends AppCompatActivity {

    public static final String GET_BOOK_INFO_ERROR = "GET_BOOK_INFO_ERROR";
    public static final String DELETE_BOOK_ERROR = "DELETE_BOOK_ERROR";
    public static final String RESPONSE_FAILURE = "Response failure";
    public static final String RESPONSE_STATUS_CODE = "Response status code: ";
    public static final String GET_BOOK_INFO_RESPONSE_CODE = "book info response code";
    public static final String UPDATE_BOOK_ERROR = "UPDATE_BOOK_ERROR";
    public static final String PUBLISHER = "Publisher";
    public static final String TAGS = "Tags: ";
    public static final String LAST_CHECKOUT_OUT = "Last Check Out:";
    public static final String NULL = "NULL";
    public static final String DELETE_BOOK_SUCCESSFULLY = "Delete Book Successfully.";
    public static final String BOOK_ID_IS_WRONG_PLEASE_CHECK = "bookId is wrong, please check.";
    public static final String DELETE_BOOK = "Delete book";
    public static final String ARE_YOU_SURE_YOU_WANT_TO_DELETE_THIS_BOOK = "Are you sure you want to delete this book?";
    public static final String CHECK_OUT_SUCCESSFULLY_BY = "Check out successfully by ";
    public static final String CHECKOUT_BOOK_FAILED_PLEASE_CHECK_THE_LOG = "Checkout book failed, please check the log.";
    public static final String DELETE_BOOK_FAILED_PLEASE_CHECK_THE_LOG = "Delete Book failed, please check the log.";
    public static final String GET_BOOK_INFO_FAILED_PLEASE_CHECK_THE_LOG = "Get book info failed, please check the log.";

    public TextView bookTitleText;
    public TextView bookAuthorText;
    public TextView bookPublisherText;
    public TextView bookTagText;
    public TextView bookLastCheckoutText;
    public TextView bookLastCheckoutByText;
    public Button checkoutButton;
    public Button deleteButton;
    public int bookId;
    public Book book;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(BooksDetailActivity.this);
            }
        });

        bookTitleText = (TextView) findViewById(R.id.bookTitleText);
        bookAuthorText = (TextView) findViewById(R.id.bookAuthorText);
        bookPublisherText = (TextView) findViewById(R.id.bookPublisherText);
        bookTagText = (TextView) findViewById(R.id.bookTagText);
        bookLastCheckoutText = (TextView) findViewById(R.id.bookLastCheckoutText);
        bookLastCheckoutByText = (TextView) findViewById(R.id.bookLastCheckoutByText);
        checkoutButton = (Button) findViewById(R.id.checkoutButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        if (!isOnline()) {
            Toast.makeText(this, Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
        } else {
            book = new Book();
            Intent booksDetailIntent = getIntent();
            bookId = booksDetailIntent.getIntExtra("bookID", -1);
            if (bookId == -1) {
                Log.d(GET_BOOK_INFO_ERROR, BOOK_ID_IS_WRONG_PLEASE_CHECK);
                finish();
            } else {
                getBookInfo(bookId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void delete(View view) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(DELETE_BOOK)
                .setMessage(ARE_YOU_SURE_YOU_WANT_TO_DELETE_THIS_BOOK)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isOnline()) {
                            Toast.makeText(getParent(), Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
                        } else {
                            deleteBook(bookId);
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

    public void checkout(View view) {
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_checkout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.check_out_title);
        alert.setView(inflator);

        final EditText usernameText = (EditText) inflator.findViewById(R.id.username);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!isOnline()) {
                    Toast.makeText(getParent(), Constant.NO_INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
                } else {
                    String username = usernameText.getText().toString();
                    book.setLastCheckedOutBy(username);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat(Constant.DATE_FORMAT);
                    String current = format.format(calendar.getTime());
                    book.setLastCheckedOut(current);
                    updateBook(book);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }


    public void getBookInfo(int bookId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Reading book information", "Please wait...", false, false);
        /********* Call get Book API to get all book list  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Book> checkBookInfoCall = libraryServiceAPI.getBookInfo(bookId);
        checkBookInfoCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                loading.dismiss();
                Log.d(GET_BOOK_INFO_RESPONSE_CODE, RESPONSE_STATUS_CODE + response.code());
                if (response.isSuccessful()) {
                    book = response.body();
                    // Set response Books as listed layout
                    bookTitleText.setText(book.getTitle());
                    bookAuthorText.setText(book.getAuthor());
                    bookPublisherText.setText(PUBLISHER + ": " + book.getPublisher());
                    bookTagText.setText(TAGS + book.getCategories());
                    if (book.getLastCheckedOut() == null && book.getLastCheckedOutBy() == null) {
                        bookLastCheckoutText.setText(LAST_CHECKOUT_OUT);
                        bookLastCheckoutByText.setText(NULL);
                    } else {
                        bookLastCheckoutText.setText(LAST_CHECKOUT_OUT);
                        bookLastCheckoutByText.setText(String.valueOf(book.getLastCheckedOutBy()) + " @ " + String.valueOf(book.getLastCheckedOut()));
                    }

                } else {
                    Log.e(GET_BOOK_INFO_ERROR, String.valueOf(response.code()));
                    Toast.makeText(getBaseContext(), GET_BOOK_INFO_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                loading.dismiss();
                Log.e(GET_BOOK_INFO_ERROR, RESPONSE_FAILURE);
                Log.e(GET_BOOK_INFO_ERROR, "Exception: " + t.getMessage());
                Toast.makeText(getBaseContext(), GET_BOOK_INFO_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deleteBook(int bookId) {
        /********* Call delete Book API to delete this book from list  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Void> deleteBookCall = libraryServiceAPI.deleteBook(bookId);
        deleteBookCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 204) {
                        Toast.makeText(getBaseContext(), DELETE_BOOK_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.e(DELETE_BOOK_ERROR, String.valueOf(response.code()));
                    Toast.makeText(getBaseContext(), DELETE_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(DELETE_BOOK_ERROR, RESPONSE_FAILURE);
                Log.e(DELETE_BOOK_ERROR, "Exception: " + t.getMessage());
                Toast.makeText(getBaseContext(), DELETE_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateBook(Book book) {
        /********* Call update Book API to update book info  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Book> updateBookCall = libraryServiceAPI.updateBook(book.getId(), book);
        updateBookCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Log.d(RESPONSE_STATUS_CODE, RESPONSE_STATUS_CODE + response.code());
                if (response.isSuccessful()) {
                    Book updatedBook = response.body();
                    // Set response Books as listed layout
                    bookTitleText.setText(updatedBook.getTitle());
                    bookAuthorText.setText(updatedBook.getAuthor());
                    bookPublisherText.setText(PUBLISHER + ": " + updatedBook.getPublisher());
                    bookTagText.setText(TAGS + updatedBook.getCategories());
                    if (updatedBook.getLastCheckedOut() == null && updatedBook.getLastCheckedOutBy() == null) {
                        bookLastCheckoutText.setText(LAST_CHECKOUT_OUT);
                        bookLastCheckoutByText.setText(NULL);
                    } else {
                        bookLastCheckoutText.setText(LAST_CHECKOUT_OUT);
                        bookLastCheckoutByText.setText(String.valueOf(updatedBook.getLastCheckedOutBy()) + " @ " + String.valueOf(updatedBook.getLastCheckedOut()));
                        Toast.makeText(getBaseContext(), CHECK_OUT_SUCCESSFULLY_BY + updatedBook.getLastCheckedOutBy() + " at " + String.valueOf(updatedBook.getLastCheckedOut()), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.e(UPDATE_BOOK_ERROR, String.valueOf(response.code()));
                    Toast.makeText(getBaseContext(), CHECKOUT_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e(UPDATE_BOOK_ERROR, RESPONSE_FAILURE);
                Log.e(UPDATE_BOOK_ERROR, "Exception: " + t.getMessage());
                Toast.makeText(getBaseContext(), CHECKOUT_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_books_detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }


    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
