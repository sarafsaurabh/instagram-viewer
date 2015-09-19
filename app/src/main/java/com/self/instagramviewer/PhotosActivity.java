package com.self.instagramviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.self.instagramviewer.rest.model.InstagramPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private static final String INSTAGRAM_CLIENT_ID = "9aac95c7addd4975854a9cdd13a40c69";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);

        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + INSTAGRAM_CLIENT_ID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(
                    int statusCode, Header[] headers, JSONObject response) {

                JSONArray photosJSON;
                Log.d(getClass().toString(), "Instagram Response: " + response.toString());
                try {
                    photosJSON = response.getJSONArray("data");
                    for(int i=0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.optJSONObject("caption") == null ?
                                null: photoJSON.optJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images")
                                .getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images")
                                .getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.createdTime = photoJSON.getLong("created_time");
                        photo.profilePicUrl =
                                photoJSON.getJSONObject("user").optString("profile_picture");
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    Log.e(getClass().toString(), "Error encountered while parsing JSON", e);
                }

                aPhotos.notifyDataSetChanged();
            }
        });
    }
}
