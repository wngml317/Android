package com.wngml.multiple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 누르면,
                // 다른 액티비티가 화면에 나타나도록 한다.
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        Log.i("MyMultiple", "Main : onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("MyMultiple", "Main : onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("MyMultiple", "Main : onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("MyMultiple", "Main : onDestroy");
    }
}