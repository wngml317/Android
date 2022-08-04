package com.wngml.placesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wngml.placesapp.model.Place;

import java.util.ArrayList;

public class PlaceActivity extends AppCompatActivity implements OnMapReadyCallback{

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        Log.i("PlaceApp", ""+getIntent().getSerializableExtra("placeList"));

        // 구글맵 사용하겠다는 코드 작성
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng myLocation = new LatLng(lat, lng);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));

        googleMap.addMarker(new MarkerOptions().position(myLocation).title("연희직업전문학교")).setTag(0);
//        }
    }
}