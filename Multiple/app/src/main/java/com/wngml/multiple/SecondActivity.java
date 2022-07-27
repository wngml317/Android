package com.wngml.multiple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        Button button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        Log.i("MyMultiple", "Second : onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("MyMultiple", "Second : onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("MyMultiple", "Second : onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("MyMultiple", "Second : onDestroy");
    }
}