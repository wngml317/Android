package com.wngml.movieapp.api;

import com.wngml.movieapp.model.MovieList;
import com.wngml.movieapp.model.MovieRes;
import com.wngml.movieapp.model.Review;
import com.wngml.movieapp.model.ReviewAdd;
import com.wngml.movieapp.model.ReviewList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/movie")
    Call<MovieList> movieList(@Query("offset") int offset, @Query("limit") int limit, @Query("order") String order);

    @GET("/movie/{movieId}")
    Call<MovieList> movieInfo(@Path("movieId") int movieId);

    // 특정 영화 리뷰 보여주는 API
    @GET("/movie/{movieId}/rating")
    Call<ReviewList> reviewList(@Path("movieId") int movieId, @Query("offset") int offset, @Query("limit") int limit);

    // 영화검색 API
    @GET("/movie/search")
    Call<MovieList> search(@Query("keyword") String keyword,  @Query("offset") int offset, @Query("limit") int limit);

    // 리뷰작성 API
    @POST("/rating")
    Call<MovieRes> review(@Header("Authorization") String token, @Body ReviewAdd review);

    // 내 정보 가져오는 API
    @GET("/rating")
    Call<ReviewList> mypage(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);
}
