package com.wngml.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wngml.memoapp.api.NetworkClient;
import com.wngml.memoapp.api.UserApi;
import com.wngml.memoapp.config.Config;
import com.wngml.memoapp.model.UserRes;
import com.wngml.memoapp.model.User;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    EditText editName;
    Button btnRegister;
    TextView txtLogin;

    // 네트워크 처리를 보여주는 프로그레스 다이얼로그
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 이메일 가져온다. 이메일 형식 체크
                String email = editEmail.getText().toString().trim();
                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

                // 유저가 아무것도 입력안했거나, 이메일 형식이 틀리면 return
                if( email.isEmpty() == true || pattern.matcher(email).matches() == false){
                    Toast.makeText(RegisterActivity.this, "이메일 제대로 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 가져온다.
                String password = editPassword.getText().toString().trim();

                // 비밀번호 길이 체크
                if (password.length() < 4 || password.length() > 12) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 4자 이상 12자 이하로 만들어 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 닉네임을 가져와서 빈 문자열인지 체크
                String name = editName.getText().toString().trim();

                if(name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 이 데이터를 API 로 호출
                // 네트워크 데이터를 보내고 있다는
                // progress dialog 를 먼저 띄운다.
                showProgress(getString(R.string.dialog_register));

                Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);

                UserApi api = retrofit.create(UserApi.class);

                User user = new User(email, password, name);

                Call<UserRes> call = api.register(user);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();

                        // 200 OK 일때,
                        if(response.isSuccessful()){

                            UserRes registerRes = response.body();

                            // 억세스토큰은, 이제 앱에서, api 호출할때마다 헤더에 넣어서 보내야한다.
                            // 따라서 억세스토큰은, 쉐어드프리퍼런스에 저장해 놓는다.
                            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("accessToken", registerRes.getAccess_token());
                            editor.apply();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "에러발생" + response.code(), Toast.LENGTH_SHORT).show();
                            Log.e("MemoApp", "=== RegisterActivity === 에러발생 :"+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        // 네트워크 자체 문제로 실패
                        dismissProgress();
                    }
                });

            }
        });
    }

    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    void dismissProgress() {
        dialog.dismiss();
    }
}
