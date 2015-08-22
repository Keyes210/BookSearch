package com.alexlowe.booksearch.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alex on 8/21/2015.
 */
public class Book implements Serializable { //we want to be able to pass the book object from the list to this detail view, so let's make our Book class serializable:
    private String openLibraryID;
    private String title;
    private String author;

    // Returns a Book given the expected JSON
    public static Book fromJson(JSONObject jsonObject){
        Book book = new Book();

        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            if(jsonObject.has("cover_edition_key")){
                book.openLibraryID = jsonObject.getString("cover_edition_key");
            }else if(jsonObject.has("edition_key")){
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryID = ids.getString(0);
            }
            book.author = getAuthor(jsonObject);
            book.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }


    // Decodes array of book json results into business model objects
    public static ArrayList<Book> fromJson(JSONArray jsonArray){
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Book book = fromJson(bookJson);
            if (book != null){
                books.add(book);
            }
        }
        return books;
    }


    // Return comma separated author list when there is more than one author
    private static String getAuthor(final JSONObject jsonObject){
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();

            final String[] authorStrings = new String[numAuthors];

            for (int i = 0; i < authorStrings.length; i++){
                authorStrings[i] = authors.getString(i);
            }

            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    public String getOpenLibraryID() {
        return openLibraryID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    // Get medium sized book cover from covers API
    public String getCoverUrl(){
        return "http://covers.openlibrary.org/b/olid/" + openLibraryID + "-M.jpg?default=false";
    }

    public String getLargeCoverUrl(){
        return "http://covers.openlibrary.org/b/olid/" + openLibraryID + "-L.jpg?default=false";
    }
}
