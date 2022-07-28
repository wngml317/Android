package com.wngml.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wngml.movieapp.api.MovieApi;
import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.config.Config;
import com.wngml.movieapp.model.Movie;
import com.wngml.movieapp.model.MovieRes;
import com.wngml.movieapp.model.Review;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewActivity extends AppCompatActivity {

    TextView txtTitle;
    RatingBar ratingBar;
    Button btnSave;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        txtTitle = findViewById(R.id.txtTitle);
        ratingBar = findViewById(R.id.ratingBar);
        btnSave = findViewById(R.id.btnSave);

        txtTitle.setText(movie.getTitle() + " 리뷰 작성");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                int movieId = movie.getId();

                Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewActivity.this);
                MovieApi api = retrofit.create(MovieApi.class);

                SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                String accessToken = sp.getString("accessToken", "");

                Review review = new Review(movieId, rating);

                Call<MovieRes> call = api.review("Bearer " + accessToken, review);
                showProgress("별점 주는 중...");

                call.enqueue(new Callback<MovieRes>() {
                    @Override
                    public void onResponse(Call<MovieRes> call, Response<MovieRes> response) {
                        dismissProgress();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MovieRes> call, Throwable t) {
                        dismissProgress();
                    }
                });
            }
        });
    }
    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    void dismissProgress() {
        dialog.dismiss();
    }
}