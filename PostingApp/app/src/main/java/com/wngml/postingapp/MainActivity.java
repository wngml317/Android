package com.wngml.postingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wngml.postingapp.adapter.PostingAdapter;
import com.wngml.postingapp.model.Posting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FloatingActionButton btnAdd;

    PostingAdapter adapter;

    ArrayList<Posting> postingList = new ArrayList<Posting>();

    final String URL = "https://block1-image-test.s3.ap-northeast-2.amazonaws.com/posting.json";

    public ActivityResultLauncher<Intent> laucher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        String title = result.getData().getStringExtra("title");
                        String body = result.getData().getStringExtra("body");

                        Posting posting = new Posting(1, title, body);

                        // 리스트 맨앞에 데이터를 넣어준다.
                        postingList.add(0, posting);

                        adapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 제목 바꾸기
        getSupportActionBar().setTitle(R.string.title_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAdd = findViewById(R.id.btnAdd);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("PostingAPP", response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        int id = data.getInt("id");
                        String title = data.getString("title");
                        String body = data.getString("body");

                        Posting posting = new Posting(id, title, body);
                        postingList.add(posting);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new PostingAdapter(MainActivity.this, postingList);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("EmployeeApp", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                startActivity(intent);
                laucher.launch(intent);
            }
        });


    }


    // xml 로 만든 메뉴를 액티비티의 화면에 나타나게 해주는 함수
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.itemAdd) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
//            startActivity(intent);
            laucher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}