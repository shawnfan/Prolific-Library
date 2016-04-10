package com.xiaoyaoworm.prolificlibrary.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.activity.BooksDetailActivity;
import com.xiaoyaoworm.prolificlibrary.client.RestClient;
import com.xiaoyaoworm.prolificlibrary.data.Constant;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;
import com.xiaoyaoworm.prolificlibrary.service.LibraryService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Leon Jiang(xiaoyaoworm) on 3/20/16.
 * https://github.com/xiaoyaoworm
 */

public class BooksAdapter extends ArraySwipeAdapter<Book> {

    public static final String DELETE_SUCCESSFULLY = "Delete successfully";
    public static final String DELETE_BOOK_FAILED = "DELETE BOOK FAILED";
    public static final String DELETE_BOOK_FAILED_PLEASE_CHECK_THE_LOG = "DELETE Book Failed, please check the log.";
    public static final String EXCEPTION = "Exception: ";

    private ArrayList<Book> books;
    private int layoutResource;

    public BooksAdapter(Context context, int layoutResource, ArrayList<Book> books) {
        super(context, android.R.layout.simple_list_item_2, books);
        this.layoutResource = layoutResource;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return books.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }
        Book book = getItem(position);
        if (book != null) {
            TextView title = (TextView) view.findViewById(R.id.bookName);
            TextView author = (TextView) view.findViewById(R.id.bookAuthor);

            title.setText(books.get(position).getTitle());
            author.setText(books.get(position).getAuthor());

            // Add swipe then delete feature.
            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bookID = (int)getItemId(position);
                    deleteBook(bookID, position);
                }
            });
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView parent = (ListView) v.getParent();
                int pos = parent.getPositionForView(v);
                int bookId = books.get(pos).getId();
                Context context = getContext();
                Intent booksDetailsIntent = new Intent(context, BooksDetailActivity.class);
                booksDetailsIntent.putExtra("bookID", bookId);
                booksDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(booksDetailsIntent);

            }
        });


        return view;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }


    // Position is important because we need to refresh the adapter.
    private void deleteBook(int bookId, final int position) {
        /********* Call delete Book API to delete this book from list  ********/
        LibraryService libraryServiceAPI = RestClient.getClient();
        Call<Void> deleteBookCall = libraryServiceAPI.deleteBook(bookId);
        deleteBookCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 204) {
                        Toast.makeText(getContext(), DELETE_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
                    }
                    books.remove(position);
                    notifyDataSetChanged();
                } else {
                    Log.e(DELETE_BOOK_FAILED, String.valueOf(response.code()));
                    Toast.makeText(getContext(), DELETE_BOOK_FAILED, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(DELETE_BOOK_FAILED, EXCEPTION + t.getMessage());
                Toast.makeText(getContext(), DELETE_BOOK_FAILED_PLEASE_CHECK_THE_LOG, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
