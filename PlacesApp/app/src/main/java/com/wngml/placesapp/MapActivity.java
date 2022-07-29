package com.wngml.placesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wngml.placesapp.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    double lat = 37.5428428;
    double lng = 126.6772096;

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        place = (Place) getIntent().getSerializableExtra("place");

        // 구글맵 사용하겠다는 코드 작성
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // 내 위치가 지도의 중심이 되도록
        LatLng myLocation = new LatLng(lat, lng);

        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));

        googleMap.addMarker(new MarkerOptions().position(myLocation).title(place.getName())).setTag(0);


    }

}