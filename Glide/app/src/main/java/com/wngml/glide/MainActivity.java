package com.wngml.glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wngml.glide.adapter.PhotoAdapter;
import com.wngml.glide.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;

    PhotoAdapter adapter;
    ArrayList<Photo> photoList = new ArrayList<Photo>();

    final String URL = "https://block1-image-test.s3.ap-northeast-2.amazonaws.com/photos.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("GlideApp", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        int id = data.getInt("id");
                        int albumId = data.getInt("albumId");
                        String title = data.getString("title");
                        String url = data.getString("url");
                        String thumbnailUrl = data.getString("thumbnailUrl");

                        Photo photo = new Photo(id, albumId, title, url
                                , thumbnailUrl);
                        photoList.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new PhotoAdapter(MainActivity.this, photoList);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GlideApp", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}