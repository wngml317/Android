package com.wngml.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wngml.movieapp.api.NetworkClient;
import com.wngml.movieapp.api.UserApi;
import com.wngml.movieapp.config.Config;
import com.wngml.movieapp.model.User;
import com.wngml.movieapp.model.UserRes;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    EditText editName;
    RadioGroup radioGroup;
    Button btnRegister;
    TextView txtLogin;

    String gender = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        radioGroup = findViewById(R.id.radioGroup);
        btnRegister = findViewById(R.id.btnLogin);
        txtLogin = findViewById(R.id.txtRegister);

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

                String email = editEmail.getText().toString().trim();
                Pattern pattern = Patterns.EMAIL_ADDRESS;

                // ????????? ???????????? ??????????????????, ????????? ????????? ?????????,
                // ???????????? ????????? ????????? ????????? ?????????. ????????????.

                if( email.isEmpty() == true || pattern.matcher(email).matches() == false){
                    Toast.makeText(RegisterActivity.this, "????????? ????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = editPassword.getText().toString().trim();
                if(password.length() < 4 || password.length() > 12){
                    Toast.makeText(RegisterActivity.this, "??????????????? 4????????? 12??? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = editName.getText().toString().trim();
                int checkedId = radioGroup.getCheckedRadioButtonId();

                if(checkedId == R.id.radioBtn1) {
                    gender = "Male";
                } else if(checkedId == R.id.radioBtn2) {
                    gender = "Female";
                } else {
                    Toast.makeText(getApplicationContext(), "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);
                UserApi api = retrofit.create(UserApi.class);
                User user = new User(email, password, name, gender);
                Call<UserRes> call = api.register(user);
                showProgress("???????????? ????????????..");

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {

                        dismissProgress();

                        if(response.isSuccessful()) {
                            UserRes userRes = response.body();
                            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("accessToken", userRes.getAccess_token());
                            editor.apply();

//                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                            startActivity(intent);

                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "????????????" + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
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