package com.wngml.mytestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면에서 만든 버튼을 가지고 와서 작업
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼 클릭했을 때 하고 싶은 코드는 여기에 작성
                Toast.makeText(getApplicationContext(), "안녕하세요.", Toast.LENGTH_LONG).show();
            }
        });


    }
}