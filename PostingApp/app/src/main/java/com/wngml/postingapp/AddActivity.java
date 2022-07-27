package com.wngml.postingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wngml.postingapp.model.Posting;

public class AddActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editBody;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle(R.string.title_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();

                if (title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("body", body);

                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // 액션바 버튼
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.itemSave) {
            String title = editTitle.getText().toString().trim();
            String body = editBody.getText().toString().trim();

            if (title.isEmpty() || body.isEmpty()) {
                Toast.makeText(getApplicationContext(), "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent intent = new Intent();
            intent.putExtra("title", title);
            intent.putExtra("body", body);

            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}