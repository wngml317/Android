package com.wngml.memoapp.api;

import com.wngml.memoapp.model.Memo;
import com.wngml.memoapp.model.MemoList;
import com.wngml.memoapp.model.PostRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemoApi {

    // 메모 생성하는 API
    @POST("/memos")
    Call<PostRes> addMemo(@Header("Authorization") String token, @Body Memo memo);

    // 내 메모 가져오는 API
    @GET("/memos")
    Call<MemoList> getMemoList(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);

    // 메모 수정하는 API
    @PUT("/memos/{memoId}")
    Call<PostRes> updateMemo(@Header("Authorization") String token, @Path("memoId") int memoId, @Body Memo memo);

    // 메모  삭제하는 API
    @DELETE("/memos/{memoId}")
    Call<PostRes> deleteMemo(@Header("Authorization") String token, @Path("memoId") int memoId);

}
