package com.wngml.placesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wngml.placesapp.adapter.PlaceAdapter;
import com.wngml.placesapp.api.NetworkClient;
import com.wngml.placesapp.api.PlaceApi;
import com.wngml.placesapp.config.Config;
import com.wngml.placesapp.model.Place;
import com.wngml.placesapp.model.PlaceList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressBar;
    EditText editSearch;
    Button btnSearch;
    RecyclerView recyclerView;
    PlaceAdapter adapter;
    ArrayList<Place> placeList = new ArrayList<>();

    private String pagetoken;
    private String keyword = "";

    LocationManager locationManager;
    LocationListener locationListener;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progressBar);
        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            // 위도 경도 바꼈을 때 다시 가져옴
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    100);

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                -1,
                locationListener);

        progressBar.setVisibility(View.INVISIBLE);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();

                // 위도, 경도를 아직 못가져왔을 때
                if(lat == 0) {
                    Toast.makeText(MainActivity.this, "위치를 찾고 있으니 조금 후에 실행해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                getNetworkData();

            }
        });


    }

    private void getNetworkData() {
        placeList.clear();
        pagetoken = "";

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);
        Call<PlaceList> call = api.getplaceList(keyword, lat+","+lng, 2000, "ko", Config.MAPS_API_KEY);
        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()) {

                    pagetoken = response.body().getNext_page_token();
                    placeList.addAll(response.body().getResults());

                    adapter = new PlaceAdapter(MainActivity.this, placeList);
                    
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){

            if(ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION} ,
                        100);
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    -1,
                    locationListener);

        }

    }

}