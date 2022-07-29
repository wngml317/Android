package com.wngml.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wngml.movieapp.adapter.ReviewAdapter;
import com.wngml.movieapp.api.MovieApi;
import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.model.Movie;
import com.wngml.movieapp.model.Review;
import com.wngml.movieapp.model.ReviewList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewListActivity extends AppCompatActivity {

    TextView txtTitle;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ReviewAdapter adapter;
    ArrayList<Review> reviewList = new ArrayList<>();
    Movie movie;

    int count = 0;
    int offset = 0;
    int limit = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        movie = (Movie) getIntent().getSerializableExtra("movie");

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(movie.getTitle()+" 리뷰");
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewListActivity.this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(  lastPosition+1  == totalCount  ){

                    if(count == limit){
                        // 네트워크 통해서, 남아있는 데이터를 추가로 가져오기
                        addNetworkData();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    private void getNetworkData() {

        reviewList.clear();
        count = 0;
        offset = 0;
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewListActivity.this);
        MovieApi api = retrofit.create(MovieApi.class);
        Call<ReviewList> call = api.reviewList(movie.getId(), offset, limit);

        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    count = response.body().getCount();
                    ReviewList data = response.body();
                    reviewList.addAll(data.getItems());
                    offset = offset + count;
                    adapter = new ReviewAdapter(ReviewListActivity.this, reviewList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // 처음이 아니라 더 가져오는 경우
    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewListActivity.this);
        MovieApi api = retrofit.create(MovieApi.class);
        Call<ReviewList> call = api.reviewList(movie.getId(), offset, limit);

        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()) {
                    ReviewList data = response.body();

                    count = data.getCount();
                    reviewList.addAll(data.getItems());
                    offset = offset + count;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}