package com.wngml.multipletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPwd;
    EditText editPwdCheck;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.editEmail);
        editPwd = findViewById(R.id.editPwd);
        editPwdCheck = findViewById(R.id.editPwdCheck);
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editEmail.getText().toString().trim();
                String pwd = editPwd.getText().toString().trim();
                String pwdCheck = editPwdCheck.getText().toString().trim();

                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

                // 1. 이메일, 비밀번호 모두 입력했는지 확인
                if (email.isEmpty() || pwd.isEmpty() || pwdCheck.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    // 2. 이메일 형식이 일치하는지 확인
                    if (email.isEmpty() == true || pattern.matcher(email).matches() == false) {
                        Toast.makeText(getApplicationContext(), "이메일을 바르게 입력하세요", Toast.LENGTH_SHORT).show();
                        return;

                    } else {
                        // 3. 비밀번호 길이가 적당한지 확인
                        if (pwd.length() < 6 || pwd.length() > 12) {
                            Toast.makeText(getApplicationContext(), " 비밀번호는 6자리 이상, 12자리 이하", Toast.LENGTH_SHORT).show();
                            return;

                        } else {
                            // 4. 먼저 입력한 비밀번호와 두번째로 입력한 비밀번호가 일치하는지 확인
                            if (!pwdCheck.equals(pwd)) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                // SharedPreferences 를 이용해서
                                // 앱 내의 저장소에 영구 저장하는 방법
                                // 앱을 삭제하기 전까지는 영구적으로 저장된다.
                                // 단, 앱을 삭제하면 당연히 같이 삭제된다.
                                SharedPreferences sp = getSharedPreferences("MultipleTest", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("email", email);
                                editor.putInt("number", 365);
                                // 이 함수를 실행해야 저장소에 데이터가 저장됨
                                editor.apply();

                                // 5. 모든 조건이 정상적으로 수행되었을 경우, 새로운 액티비티를 실행
                                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);

                                // 이 액티비티는 위의 새로운 액티비티를 실행시키고 나서 종료한다.
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }
}