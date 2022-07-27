package com.wngml.memoapp;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wngml.memoapp.adapter.MemoAdapter;
import com.wngml.memoapp.api.MemoApi;
import com.wngml.memoapp.api.NetworkClient;
import com.wngml.memoapp.api.UserApi;
import com.wngml.memoapp.config.Config;
import com.wngml.memoapp.model.Memo;
import com.wngml.memoapp.model.MemoList;
import com.wngml.memoapp.model.PostRes;
import com.wngml.memoapp.model.UserRes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;

    // RecyclerView 관련 멤버변수 3개
    RecyclerView recyclerView;
    MemoAdapter adapter;
    ArrayList<Memo> memoList = new ArrayList<Memo>();
    ProgressBar progressBar;

    // 페이징에 필요한 변수
    int offset = 0;
    int limit = 10;
    int count = 0;

    String accessToken;
    private ProgressDialog dialog;
    private int index;

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

        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar);
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
                        // 네트워크 통해서, 데이터를 더 불러오면 된다.
                        addNetworkData();
                    }
                }

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    // 데이터를 처음 가져올 때만  실행하는 함수
    // 데이터의 초기화도 필요하다.
    private void getNetworkData() {

        memoList.clear();
        count = 0;
        offset = 0;

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        MemoApi api = retrofit.create(MemoApi.class);

        Call<MemoList> call = api.getMemoList("Bearer " + accessToken, offset, limit);

        call.enqueue(new Callback<MemoList>() {
            @Override
            public void onResponse(Call<MemoList> call, Response<MemoList> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    // 정상으로 데이터를 받아왔으니, 리사이클러뷰에 표시
                    MemoList data = response.body();

                    count = data.getCount();
                    memoList.addAll(data.getresult_list());
                    offset = offset + count;

                    adapter = new MemoAdapter(MainActivity.this, memoList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(MainActivity.this, "에러발생 : " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("MemoApp", "=== MainActivity === 에러발생 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MemoList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    // 처음이 아니라, 더 가져오는 경우
    private void addNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        MemoApi api = retrofit.create(MemoApi.class);

        Call<MemoList> call = api.getMemoList("Bearer " + accessToken, offset, limit);

        call.enqueue(new Callback<MemoList>() {
            @Override
            public void onResponse(Call<MemoList> call, Response<MemoList> response) {

                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    // 정상으로 데이터를 받아왔으니, 리사이클러뷰에 표시
                    MemoList data = response.body();

                    count = data.getCount();
                    memoList.addAll(data.getresult_list());
                    offset = offset + count;

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this, "에러발생 : " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("MemoApp", "=== MainActivity === 에러발생 : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<MemoList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.memuLogout) {
            // 로그아웃 처리해준다.
            // 쉐어드프리퍼런스에 억세스토큰을 가져온다.
            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
            accessToken = sp.getString("accessToken", "");

            Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
            UserApi api = retrofit.create(UserApi.class);

            Call<UserRes> call = api.logout("Bearer " + accessToken);

            showProgress("로그아웃 중입니다..");

            call.enqueue(new Callback<UserRes>() {
                @Override
                public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                    dismissProgress();
                    if(response.isSuccessful()) {
                        // HTTP 상태코드가 200 일때
                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                        // 네트춰크에서 잘 처리가 되었으면
                        // 클라이언트에서도 로그인 정보를 가지고 있는
                        // 억세스 토큰을 초기화 시켜야한다.
                        SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("accessToken", "");
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "에러발생" + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("MemoApp", "=== MainActivity === 에러발생 :"+response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserRes> call, Throwable t) {
                    dismissProgress();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteMemo(int index) {

        this.index = index;

        // 여기에 알러트다이얼로그 띄우고
        // 알러트다이얼로그에서 Yes 버튼 누르면
        // 네트워크 API 호출하여 해당 메모 삭제하고
        // 삭제하고 나면 화면에 반영해줘야 한다.

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(R.string.alert_main_title);
        alert.setMessage(R.string.alert_main_message);
        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

                MemoApi api = retrofit.create(MemoApi.class);

                // 헤더에 셋팅할 토큰을 가져온다.
                SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("accessToken", "");

                Memo memo = memoList.get(index);
                int memoId = memo.getId();

                Call<PostRes> call = api.deleteMemo("Bearer " + token, memoId);
                showProgress("메모 삭제중..");
                call.enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                        dismissProgress();
                        if(response.isSuccessful()) {
                            memoList.remove(index);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "삭제 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<PostRes> call, Throwable t) {
                        dismissProgress();
                    }
                });
            }
        });

        alert.setNegativeButton("no", null);
        alert.show();
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