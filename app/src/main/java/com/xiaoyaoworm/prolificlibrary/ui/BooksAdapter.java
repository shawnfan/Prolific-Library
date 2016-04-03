package com.xiaoyaoworm.prolificlibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoyaoworm.prolificlibrary.R;
import com.xiaoyaoworm.prolificlibrary.activity.BooksDetailActivity;
import com.xiaoyaoworm.prolificlibrary.pojo.Book;

import java.util.ArrayList;

/**
 * Created by Leon Jiang(xiaoyaoworm) on 3/20/16.
 * https://github.com/xiaoyaoworm
 */

public class BooksAdapter extends ArrayAdapter<Book> {

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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
}
