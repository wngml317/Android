package com.wngml.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wngml.memo.data.DatabaseHandler;
import com.wngml.memo.model.Memo;

public class AddActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. 유저가 입력한 제목과 내용 가져오기
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                // 1-1. 빈칸이 있으면 다시 return
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. Memo 객체로 만들기
                Memo memo = new Memo(title, content);

                // 3. 디비에 저장하기
                // 3-1. 디비핸들러 가져오기
                DatabaseHandler db = new DatabaseHandler(AddActivity.this);

                // 3-2. 메모 저장하는 함수 호출
                db.addMemo(memo);

                // 3-3. 저장 완료 메시지 보여주기
                Toast.makeText(getApplicationContext(), "잘 저장되었습니다.", Toast.LENGTH_SHORT).show();

                // 4. 액티비티 종료하기
                finish();


            }
        });
    }
}