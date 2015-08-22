package com.alexlowe.booksearch.model;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Alex on 8/21/2015.
 */
public class BookClient {
    private static final String API_BASE_CLIENT = "http://openlibrary.org/";
    private AsyncHttpClient client;

    public BookClient(){
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl){
        return API_BASE_CLIENT + relativeUrl;
    }

    // Method for accessing the search API
    public void getBooks(final String query, JsonHttpResponseHandler handler){
        try {
            String url = getApiUrl("search.json?q=");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Method for accessing books API to get publisher and no. of pages in a book.
    public void getExtraBookDetails(String openLibraryID, JsonHttpResponseHandler handler){
        String url = getApiUrl("books/");
        client.get(url + openLibraryID + ".json", handler);
    }
}
