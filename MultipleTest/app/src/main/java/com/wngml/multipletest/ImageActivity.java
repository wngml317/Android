package com.wngml.multipletest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    ImageView ImgSelect;
    Button btnRabbit;
    Button btnTurtle;
    Button btnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // 넘어오는 데이터 처리
        String email = getIntent().getStringExtra("email");

        ImgSelect = findViewById(R.id.ImgSelect);
        btnRabbit = findViewById(R.id.btnRabbit);
        btnTurtle = findViewById(R.id.btnTurtle);
        btnSelect = findViewById(R.id.btnSelect);

        btnRabbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgSelect.setImageResource(R.drawable.rabbit);
            }
        });

        btnTurtle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgSelect.setImageResource(R.drawable.turtle);
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ImageActivity.this);
                alert.setTitle("회원가입 완료");
                alert.setMessage("완료하시겠습니까?");
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(ImageActivity.this, CompleteActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);

                        // 현재의 이미지액티비
                        // 티를 종료
                        finish();

                    }
                });
                alert.show();
            }
        });
    }
}