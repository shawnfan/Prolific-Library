package com.xiaoyaoworm.prolificlibrary.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.rest.RestClient;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksDetailActivity extends AppCompatActivity {

    public static final String GET_BOOK_INFO_ERROR = "GET_BOOK_INFO_ERROR";
    public static final String DELETE_BOOK_ERROR = "DELETE_BOOK_ERROR";
    public static final String RESPONSE_FAILURE = "Response failure";
    public static final String RESPONSE_STATUS_CODE = "Response status code: ";
    public static final String GET_BOOK_INFO_RESPONSE_CODE = "book info response code";
    public static final String UPDATE_BOOK_ERROR = "UPDATE_BOOK_ERROR";
    public static final String PUBLISHER = "Publisher";
    public static final String TAGS = "Tags: ";
    public static final String LAST_CHECKOUT_OUT = "Last Checkout Out:";
    public static final String NULL = "NULL";
    public static final String LAST_CHECKOUT_OUT1 = "Last Checkout Out:";
    public static final String DELETE_BOOK_SUCCESSFULLY = "Delete Book Successfully.";

    private ShareActionProvider mShareActionProvider;

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

        book = new Book();
        Intent booksDetailIntent = getIntent();
        bookId = booksDetailIntent.getIntExtra("bookID", -1);
        if (bookId == -1) {
            Log.d(GET_BOOK_INFO_ERROR, "bookId is wrong, please check.");
            finish();
        } else {
            getBookInfo(bookId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getBookInfo(bookId);
    }

    public void delete(View view) {
        deleteBook(bookId);
    }

    public void checkout(View view) {

        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_checkout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.check_out_title);
        alert.setView(inflator);

        final EditText usernameText = (EditText) inflator.findViewById(R.id.username);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String username = usernameText.getText().toString();
                book.setLastCheckedOutBy(username);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(Constant.DATE_FORMAT);
                String current = format.format(calendar.getTime());
                book.setLastCheckedOut(current);
                updateBook(book);
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
        /********* Call get Book API to get all book list  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Book> checkBookInfoCall = libraryServiceAPI.getBookInfo(bookId);
        checkBookInfoCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
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
                        bookLastCheckoutText.setText(LAST_CHECKOUT_OUT1);
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
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Void> deleteBookCall = libraryServiceAPI.deleteBook(bookId);
        deleteBookCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 204) {
                        Toast.makeText(getBaseContext(), DELETE_BOOK_SUCCESSFULLY, Toast.LENGTH_LONG).show();
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
                        bookLastCheckoutText.setText(LAST_CHECKOUT_OUT1);
                        bookLastCheckoutByText.setText(String.valueOf(updatedBook.getLastCheckedOutBy()) + " @ " + String.valueOf(updatedBook.getLastCheckedOut()));
                    }


                } else {
                    Log.d(UPDATE_BOOK_ERROR, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d(UPDATE_BOOK_ERROR, RESPONSE_FAILURE);
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


}
