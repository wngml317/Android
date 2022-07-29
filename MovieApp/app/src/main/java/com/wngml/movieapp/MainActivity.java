package com.wngml.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.api.UserApi;
import com.wngml.movieapp.config.Config;
import com.wngml.movieapp.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    String accessToken;

    BottomNavigationView navigationView;

    Fragment movieFragment;
    Fragment reviewFragment;
    Fragment recommendFragment;
    Fragment mypageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNavigationView);

        movieFragment = new FirstFragment();
        reviewFragment = new SecondFragment();
        recommendFragment = new ThirdFragment();
        mypageFragment = new ForthFragment();

        SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
        accessToken = sp.getString("accessToken", "");

        if (accessToken.isEmpty()) {


//            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//            startActivity(intent);
//
//            finish();
        }

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                Fragment fragment = null;

                if (itemId == R.id.movieFragment) {
                    fragment = movieFragment;
                } else if (itemId == R.id.reviewFragment) {
                    fragment = reviewFragment;
                } else if (itemId == R.id.recommendFragment) {
                    fragment = recommendFragment;
                } else if (itemId == R.id.mypageFragment) {
                    fragment = mypageFragment;
                }

                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        }
        return false;
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

//                                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                                startActivity(intent);
//                                finish();
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
        }
        return super.onOptionsItemSelected(item);
    }
}