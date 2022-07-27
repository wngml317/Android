package com.wngml.glide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wngml.glide.model.Photo;

public class PhotoActivity extends AppCompatActivity {

    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        String imgUrl = getIntent().getStringExtra("url");

        imgPhoto = findViewById(R.id.imgPhoto);

        // Glide 라이브러리  사용
        GlideUrl url = new GlideUrl(imgUrl, new LazyHeaders.Builder().addHeader("User-Agent", "Android").build());
        Glide.with(PhotoActivity.this).load(url).placeholder(R.drawable.ic_defalut).into(imgPhoto);

    }
}