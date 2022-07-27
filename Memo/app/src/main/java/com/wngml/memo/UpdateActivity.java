package com.wngml.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wngml.memo.data.DatabaseHandler;
import com.wngml.memo.model.Memo;

public class UpdateActivity extends AppCompatActivity {

    Memo memo;

    EditText editTitle;
    EditText editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // 다른 액티비티로 받아온 데이터 처리
        memo = (Memo) getIntent().getSerializableExtra("memo");

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        editTitle.setText(memo.title);
        editContent.setText(memo.content);

        // 버튼 클릭 시, 수정 내용을 DB에 저장하고, 액티비티 종료 후, 메인으로 돌아감
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHandler db = new DatabaseHandler(UpdateActivity.this);

                memo.title = title;
                memo.content = content;

                db.updateMemo(memo);

                // 현재 액티비티를 종료하면 자동으로 메인 액티비티가 나온다.
                finish();
            }
        });

    }
}