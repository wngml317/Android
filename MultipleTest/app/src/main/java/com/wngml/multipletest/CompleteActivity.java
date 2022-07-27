package com.wngml.multipletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class CompleteActivity extends AppCompatActivity {

    TextView txtEmail;
    TextView txtSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        String email = getIntent().getStringExtra("email");

        txtEmail = findViewById(R.id.txtEmail);
        txtSP = findViewById(R.id.txtSP);

        txtEmail.setText(email + " 님\n 안녕하세요");

        // SharedPreferences에서 데이터를 가져오는 방법
        SharedPreferences sp = getSharedPreferences("MultipleTest", MODE_PRIVATE);
        String savedEmail = sp.getString("email", "nothing");
        txtSP.setText("저장되었던 이메일은 :\n " + savedEmail);

    };

    @Override
    public void onBackPressed() {
        finish();
    }


}