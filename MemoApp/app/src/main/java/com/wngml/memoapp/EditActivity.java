package com.wngml.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wngml.memoapp.api.MemoApi;
import com.wngml.memoapp.api.NetworkClient;
import com.wngml.memoapp.config.Config;
import com.wngml.memoapp.model.Memo;
import com.wngml.memoapp.model.PostRes;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditActivity extends AppCompatActivity {

    Memo memo;

    EditText editTitle;
    Button btnDate;
    Button btnTime;
    EditText editContent;
    Button btnSave;

    String date = "";
    String time = "";
    int id = 0;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        memo = (Memo) getIntent().getSerializableExtra("memo");
        id = memo.getId();

        editTitle = findViewById(R.id.editTitle);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        // 데이터를 화면에 세팅
        editTitle.setText(memo.getTitle());

        // "2022-07-22T11:00:00"
        String[] dateArray = memo.getDate().substring(0, 16).split("T");
        date = dateArray[0];
        time = dateArray[1];
        btnDate.setText(date);
        btnTime.setText(time);

        editContent.setText(memo.getContent());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                if(title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "제목은 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(date.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "날짜는 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(time.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "시간은 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = editContent.getText().toString().trim();

                // 네트워크로 데이터를 보낸다.
                Retrofit retrofit = NetworkClient.getRetrofitClient(EditActivity.this);

                MemoApi api = retrofit.create(MemoApi.class);

                // 헤더에 셋팅할 토큰을 가져온다.
                SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("accessToken", "");

                // 보낼 데이터를 만든다.
                Memo memo = new Memo(title, date+" " +time, content);
//                memo.setDate(title);
//                memo.setDate(date+" " +time);
//                memo.setContent(content);

                Call<PostRes> call = api.updateMemo("Bearer " + token, id, memo);

                showProgress("메모 수정중...");

                call.enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                        dismissProgress();

                        if(response.isSuccessful()) {
                            // HTTP 상태코드가 200 일때
                            Toast.makeText(EditActivity.this, "수정 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditActivity.this, "에러발생" + response.code(), Toast.LENGTH_SHORT).show();
                            Log.e("MemoApp", "=== EditActivity === 에러발생 :"+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<PostRes> call, Throwable t) {
                        dismissProgress();
                    }
                });
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance();
                new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        int month = i1 + 1;

                        String monthStr;
                        String dayStr;

                        if (month < 10) {
                            monthStr = "0" + month;
                        } else {
                            monthStr = "" + month;
                        }

                        if (i2 < 10) {
                            dayStr = "0" + i2;
                        } else {
                            dayStr = "" + i2;
                        }

                        date = i + "-" + monthStr + "-" + dayStr;
                        btnDate.setText(date);
                    }
                },
                        current.get(Calendar.YEAR),
                        current.get(Calendar.MONTH),
                        current.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance();

                new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        String hourStr;
                        String minuteStr;

                        if(i < 10) {
                            hourStr = "0" + i;
                        } else {
                            hourStr = "" + i;
                        }

                        if(i1 < 10) {
                            minuteStr = "0" + i1;
                        } else {
                            minuteStr = "" + i1;
                        }

                        time = hourStr + ":" + minuteStr;
                        btnTime.setText(time);

                    }
                },
                        current.get(Calendar.HOUR_OF_DAY),
                        current.get(Calendar.MINUTE),
                        true).show();
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