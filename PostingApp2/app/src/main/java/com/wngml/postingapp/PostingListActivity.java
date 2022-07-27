package com.wngml.postingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wngml.postingapp.adapter.FollowPostingAdapter;
import com.wngml.postingapp.api.NetworkClient;
import com.wngml.postingapp.api.PostingApi;
import com.wngml.postingapp.config.Config;
import com.wngml.postingapp.model.PostRes;
import com.wngml.postingapp.model.Posting;
import com.wngml.postingapp.model.PostingList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostingListActivity extends AppCompatActivity {

    String accessToken;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    FollowPostingAdapter adapter;
    ArrayList<Posting> postingList = new ArrayList<Posting>();

    // 좋아요 눌렀을 때 포스팅 정보
    private Posting selectedposting;

    // 페이징 처리 멤버 변수
    int count = 0;
    int offset = 0;
    int limit = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_list);

        SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
        accessToken = sp.getString("accessToken", "");

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostingListActivity.this));
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

    // 데이터를 처음 가져올때만 실행하는 함수
    // 데이터의 초기화도 필요하다.
    private void getNetworkData() {
        postingList.clear();
        count = 0;
        offset = 0;
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(PostingListActivity.this);
        PostingApi api = retrofit.create(PostingApi.class);
        Call<PostingList> call = api.getFollowPosting("Bearer " + accessToken, offset, limit);
        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                if (response.isSuccessful()) {
                    PostingList data = response.body();

                    count = data.getCount();
                    postingList.addAll(data.getItems());
                    offset = offset + count;

                    progressBar.setVisibility(View.INVISIBLE);
                    adapter = new FollowPostingAdapter(PostingListActivity.this, postingList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {

            }
        });
    }

    // 처음이 아니라 더 가져오는 경우
    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(PostingListActivity.this);
        PostingApi api = retrofit.create(PostingApi.class);

        Call<PostingList> call = api.getFollowPosting("Bearer " + accessToken, offset, limit);

        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()) {
                    PostingList data = response.body();

                    count = data.getCount();
                    postingList.addAll(data.getItems());
                    offset = offset + count;
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PostingListActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setLike(int index) {
        selectedposting = postingList.get(index);

        int postingId = selectedposting.getId();
        int likeCnt = selectedposting.getLikeCnt();
        int isLike = selectedposting.getIsLike();

        Retrofit retrofit  = NetworkClient.getRetrofitClient(PostingListActivity.this);
        PostingApi api = retrofit.create(PostingApi.class);

        Call<PostRes> call = null;

        if (isLike == 0) {
            call = api.setLike("Bearer " + accessToken, postingId);

        } else if (isLike == 1) {
            call = api.unsetLike("Bearer " + accessToken, postingId);
        }
        call.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                // 서버에 반영이 잘 된것이다.
                // 따라서 화면에 보여주는 방식은 2가지 방식이 있는데
                // 기획에 따라서 선택하면 된다.
                if (response.isSuccessful()) {
                    // 첫번째 방법은 전체 데이터를 다시 가져오는 방법
                    // 두번째 방법은 서버에는 반영이 되어있으니깐
                    // 클라이언트 쪽에만 따로 바뀐 부분을 반영해주는 방법
                    if (selectedposting.getIsLike() == 0) {
                        selectedposting.setIsLike(1);
                        selectedposting.setLikeCnt(likeCnt+1);
                    } else {
                        selectedposting.setIsLike(0);
                        selectedposting.setLikeCnt(likeCnt-1);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }
}
