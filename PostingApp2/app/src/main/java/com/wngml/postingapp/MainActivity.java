package com.wngml.postingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wngml.postingapp.adapter.FollowPostingAdapter;
import com.wngml.postingapp.adapter.MyPostingAdapter;
import com.wngml.postingapp.api.NetworkClient;
import com.wngml.postingapp.api.PostingApi;
import com.wngml.postingapp.api.UserApi;
import com.wngml.postingapp.config.Config;
import com.wngml.postingapp.model.PostRes;
import com.wngml.postingapp.model.Posting;
import com.wngml.postingapp.model.PostingList;
import com.wngml.postingapp.model.UserRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    String accessToken;

    Button btnAdd;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    MyPostingAdapter adapter;
    ArrayList<Posting> postingList = new ArrayList<>();
    private ProgressDialog dialog;

    // 페이징에 필요한 변수
    int offset = 0;
    int limit = 5;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 쉐어드프리퍼런스에 억세스토큰을 가져온다.
        SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
        accessToken = sp.getString("accessToken", "");

        // 2. 만약 억세스토큰이 없으면 회원가입 액티비티를 실행하고,
        // 그렇지 않으면, 메모 가져오는 API 를 호출해서
        // 리사이클러뷰로 화면에 내 메모를 보여준다.
        if (accessToken.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        progressBar = findViewById(R.id.progressBar);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PostingApi api = retrofit.create(PostingApi.class);
        Call<PostingList> call = api.getMyPosting("Bearer " + accessToken, offset, limit);
        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    PostingList data = response.body();

                    count = data.getCount();
                    postingList.addAll(data.getItems());
                    offset = offset + count;

                    adapter = new MyPostingAdapter(MainActivity.this, postingList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // 처음이 아니라 더 가져오는 경우
    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PostingApi api = retrofit.create(PostingApi.class);

        Call<PostingList> call = api.getMyPosting("Bearer " + accessToken, offset, limit);

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
                    Toast.makeText(MainActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void deletePosting(int index) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("포스팅 삭제");
        alert.setMessage("정말 삭제하시겠습니까?");
        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Posting slectedPosting = postingList.get(index);
                Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
                PostingApi api = retrofit.create(PostingApi.class);
                Call<PostRes> call = api.deletePosting("Bearer " + accessToken, slectedPosting.getId());
                call.enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                        postingList.remove(index);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<PostRes> call, Throwable t) {

                    }
                });
            }
        });
        alert.setNegativeButton("No", null);
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.menuLogout) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("로그아웃");
            alert.setMessage("로그아웃 하시겠습니까?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                    accessToken = sp.getString("accessToken", "");

                    Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
                    UserApi api = retrofit.create(UserApi.class);

                    Call<UserRes> call = api.logout("Bearer " + accessToken);

                    call.enqueue(new Callback<UserRes>() {
                        @Override
                        public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                            if (response.isSuccessful()) {
                                SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("accessToken", "");
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRes> call, Throwable t) {

                        }
                    });
                }

            });
            alert.setNegativeButton("No", null);
            alert.show();
        } else if (itemId == R.id.menuPosting) {
            Intent intent = new Intent(MainActivity.this, PostingListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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