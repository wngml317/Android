package com.wngml.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wngml.movieapp.adapter.MovieAdapter;
import com.wngml.movieapp.adapter.ReviewAdapter;
import com.wngml.movieapp.api.MovieApi;
import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.model.Movie;
import com.wngml.movieapp.model.MovieList;
import com.wngml.movieapp.model.Review;
import com.wngml.movieapp.model.ReviewList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewActivity extends AppCompatActivity {

    TextView txtTitle;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ReviewAdapter adapter;
    ArrayList<Review> reviewList = new ArrayList<>();

    int offset = 0;
    int limit = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(movie.getTitle()+" 리뷰");
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewActivity.this);
        MovieApi api = retrofit.create(MovieApi.class);
        Call<ReviewList> call = api.reviewList(movie.getId(), offset, limit);

        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    ReviewList data = response.body();
                    reviewList.addAll(data.getItems());

                    adapter = new ReviewAdapter(ReviewActivity.this, reviewList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}