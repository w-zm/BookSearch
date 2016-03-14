package com.example.wzm.booksearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class BookListActivity extends AppCompatActivity {

    private ListView lv;
    private ProgressBar progressBar;
    private BookAdapter bookAdapter;
    private BookClient client;

    public static final String BOOK_DETAIL_KEY = "book";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        lv = (ListView) findViewById(R.id.lvBooks);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        ArrayList<Book> aBooks = new ArrayList<Book>();
        bookAdapter = new BookAdapter(this, aBooks);
        lv.setAdapter(bookAdapter);
        fetchBooks();
        setupBookSelectedListener();
    }

    private void fetchBooks() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        client = new BookClient();
        client.getBooks("Ernest Mathijs", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    progressBar.setVisibility(ProgressBar.GONE);
                    JSONArray docs = null;
                    if (response != null) {
                        docs = response.getJSONArray("docs");
                        final ArrayList<Book> books = Book.fromJson(docs);
                        bookAdapter.clear();
                        for (Book book : books) {
                            bookAdapter.add(book);
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setupBookSelectedListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookListActivity.this, BookDetailActivity.class);
                intent.putExtra(BOOK_DETAIL_KEY, bookAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

}
