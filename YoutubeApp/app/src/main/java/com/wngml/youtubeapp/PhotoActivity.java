package com.wngml.youtubeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wngml.youtubeapp.model.Youtube;

public class PhotoActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        String imgUrl = getIntent().getStringExtra("imgUrl");

        imageView = findViewById(R.id.imageView);

        GlideUrl url = new GlideUrl(imgUrl, new LazyHeaders.Builder().addHeader("User_Agent", "Android").build());
        Glide.with(PhotoActivity.this).load(url).into(imageView);
    }
}