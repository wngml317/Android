package com.wngml.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.wngml.movieapp.model.ReviewAdd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewAddActivity extends AppCompatActivity {

    TextView txtTitle;
    RatingBar ratingBar;
    Button btnSave;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

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

                Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewAddActivity.this);
                MovieApi api = retrofit.create(MovieApi.class);

                SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                String accessToken = sp.getString("accessToken", "");

                ReviewAdd reviewAdd = new ReviewAdd(movieId, rating);
                reviewAdd.setMovieId(movieId);
                reviewAdd.setRating(rating);

                Call<MovieRes> call = api.review("Bearer " + accessToken, reviewAdd);
                showProgress("별점 주는 중...");

                call.enqueue(new Callback<MovieRes>() {
                    @Override
                    public void onResponse(Call<MovieRes> call, Response<MovieRes> response) {
                        dismissProgress();
                        if (response.isSuccessful()) {
                            finish();
                        } else {
                            if(response.code() == 401) {
                                // 회원가입 액티비티 띄운다.
                                Intent intent = new Intent(ReviewAddActivity.this, RegisterActivity.class);
                                startActivity(intent);
                            }
                        }
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