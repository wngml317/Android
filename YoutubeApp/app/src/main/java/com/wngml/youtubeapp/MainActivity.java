package com.wngml.youtubeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wngml.youtubeapp.adapter.YoutubeAdapter;
import com.wngml.youtubeapp.config.Config;
import com.wngml.youtubeapp.model.Youtube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editSearch;
    ImageView imgSearch;
    ProgressBar progressBar;

    YoutubeAdapter adapter;
    ArrayList<Youtube> youtubeList = new ArrayList<Youtube>();

    // 페이징에 필요한 변수
    String pageToken;
    String keyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // 리스트를 맨 밑에까지 가면 알 수 있는 방법
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                // 스크롤을 맨 끝까지 한 것
                if(lastPosition+1 == totalCount) {
                    // 네트워크를 통해서 데이터를 가져온다.
                    if(pageToken == null) {
                        return;
                    }

                    // 1. 프로그레스바를 돌린다.
                    progressBar.setVisibility(View.INVISIBLE);

                    // 2. URL 을 조합한다.
                    // ?part=snippet&key=[자신의 API KEY]&q=축구&maxResults=20
                    String url = Config.BASE_URL + "?part=snippet&key=" + Config.GOOGLE_API_KEY +
                            "&q=" + keyword + "&maxResults=20&pageToken=" + pageToken;

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            // 데이터를 받아오면 프로그레스바를 안보이게 한다.
                            progressBar.setVisibility(View.INVISIBLE);


                            try {

                                if (response.has("nextPageToken")) {
                                    pageToken = response.getString("nextPageToken");
                                } else {
                                    pageToken = null;
                                }

                                JSONArray dataList = response.getJSONArray("items");

                                for(int i = 0; i < dataList.length(); i++) {

                                    String title = dataList.getJSONObject(i).getJSONObject("snippet").getString("title");
                                    String description = dataList.getJSONObject(i).getJSONObject("snippet").getString("description");

                                    // 이미지 데이터
                                    String imgUrl = dataList.getJSONObject(i).getJSONObject("snippet").
                                            getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                                    // 비디오 데이터
                                    String videoId = dataList.getJSONObject(i).getJSONObject("id").getString("videoId");

                                    Youtube youtube = new Youtube(title, description, imgUrl, videoId);
                                    youtubeList.add(youtube);
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("YoutubeApp", error.toString());
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                }
            }
        });


        progressBar.setVisibility(View.INVISIBLE);

        // 검색 버튼을 누르면, 네트워크로 데이터를 요청해서 정보를 받아온다.
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // 1. EditText 로 부터 검색어를 가져온다.
                keyword = editSearch.getText().toString().trim();

                // 2. 검색어가 없으면 검색어를 넣으라고 한다.
                if(keyword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. progressBar 를 돌린다.
                progressBar.setVisibility(View.VISIBLE);

                youtubeList.clear();
                if(adapter != null) {
                    adapter.notifyDataSetChanged();
                }

                // 4. URL 을 조합한다.
                // ?part=snippet&key=[자신의 API KEY]&q=축구&maxResults=20
                String url = Config.BASE_URL + "?part=snippet&key=" + Config.GOOGLE_API_KEY + "&q=" + keyword + "&maxResults=20";

                // 5. 네트워크 통신한다.
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // 데이터를 받아오면 프로그레스바를 안보이게 한다.
                        progressBar.setVisibility(View.INVISIBLE);


                        try {

                            if (response.has("nextPageToken")) {
                                pageToken = response.getString("nextPageToken");
                            } else {
                                pageToken = null;
                            }

                            JSONArray dataList = response.getJSONArray("items");

                            for(int i = 0; i < dataList.length(); i++) {

                                String title = dataList.getJSONObject(i).getJSONObject("snippet").getString("title");
                                String description = dataList.getJSONObject(i).getJSONObject("snippet").getString("description");

                                // 이미지 데이터
                                String imgUrl = dataList.getJSONObject(i).getJSONObject("snippet").
                                        getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                                // 비디오 데이터
                                String videoId = dataList.getJSONObject(i).getJSONObject("id").getString("videoId");

                                Youtube youtube = new Youtube(title, description, imgUrl, videoId);
                                youtubeList.add(youtube);
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }

                        adapter = new YoutubeAdapter(MainActivity.this, youtubeList);
                        recyclerView.setAdapter(adapter);
                        editSearch.setText("");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("YoutubeApp", error.toString());
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}