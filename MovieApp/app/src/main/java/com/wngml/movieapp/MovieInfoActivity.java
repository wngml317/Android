package com.wngml.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wngml.movieapp.api.MovieApi;
import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.model.Movie;
import com.wngml.movieapp.model.MovieList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieInfoActivity extends AppCompatActivity {

    TextView txtTitle;
    TextView txtGenre;
    TextView txtYear;
    TextView txtAttendance;
    TextView txtAvg;

    ArrayList<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        txtTitle = findViewById(R.id.txtTitle);
        txtGenre = findViewById(R.id.txtGenre);
        txtYear = findViewById(R.id.txtYear);
        txtAttendance = findViewById(R.id.txtAttendance);
        txtAvg = findViewById(R.id.txtAvg);

//        txtTitle.setText(movie.getTitle());
//        txtGenre.setText(movie.getGenre());
//        txtYear.setText(movie.getYear());
//        txtAttendance.setText("" + movie.getAttendance());
//        txtAvg.setText("" + movie.getAvg());

        Retrofit retrofit = NetworkClient.getRetrofitClient(MovieInfoActivity.this);
        MovieApi api = retrofit.create(MovieApi.class);
        Call<MovieList> call = api.movieInfo(movie.getId());

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                Log.i("Movie", "response " + response.body().getItem());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i("Movie", "error" + t);
            }
        });

    }
}