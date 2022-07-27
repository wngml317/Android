package com.wngml.tab.api;

import com.wngml.tab.model.PostRes;
import com.wngml.tab.model.PostingList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostingApi {

    // 포스팅하는 API
    @Multipart
    @POST("/posting")
    Call<PostRes> addPosting(@Header("Authorization") String token, @Part MultipartBody.Part photo, @Part("content") RequestBody content);

    // 내 포스팅 가져오는 API
    @GET("/posting")
    Call<PostingList> getMyPosting(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);

    // 친구 포스팅 가져오는 API
    @GET("/posting/follow")
    Call<PostingList> getFollowPosting(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);

    // 해당 포스팅에 좋아요 하는 API
    @POST("/like/{postingId}")
    Call<PostRes> setLike(@Header("Authorization") String token, @Path ("postingId") int postingId);

    // 해당 포스팅에 좋아요 취소하는 API
    @DELETE("/like/{postingId}")
    Call<PostRes> unsetLike(@Header("Authorization") String token, @Path ("postingId") int postingId);

    // 포스팅 수정하는 API
    @PUT("/posting/{postingId}")
    Call<PostRes> updatePosting(@Header("Authorization") String token, @Path ("postingId") int postingId, @Part MultipartBody.Part photo, @Part("content") RequestBody content);

    // 포스팅 삭제하는 API
    @DELETE("/posting/{postingId}")
    Call<PostRes> deletePosting(@Header("Authorization") String token, @Path ("postingId") int postingId);
}
