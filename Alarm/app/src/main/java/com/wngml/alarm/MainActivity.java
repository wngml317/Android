package com.wngml.alarm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txtTimer;
    EditText editTime;
    Button btnFinish;
    Button btnStart;

    CountDownTimer timer;

    // 타이머 전체 초
    long millisInFuture = 60000;
    // 감소시킬 초
    final int countDownIterval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        txtTimer = findViewById(R.id.txtTimer);
        editTime = findViewById(R.id.editTime);
        btnFinish = findViewById(R.id.btnFinish);
        btnStart = findViewById(R.id.btnStart);

        // SharedPreferences에서 데이터를 가져오는 방법
        SharedPreferences sp = getSharedPreferences("Alarm", MODE_PRIVATE);
        String saved_time = sp.getString("time", "");
        txtTimer.setText("남은 시간 : " + saved_time);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 타이머 취소
                timer.cancel();
                txtTimer.setText("남은 시간 : " + editTime.getText().toString().trim());
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeStr;

                // 1. 저장된 시간 데이터가 없으면
                // EditText 에서 유저가 입력한 초 정보를 가져온다.
                // 있으면 EditText가 null이여도 저장된 데이터로 타이머를  실행한다.
                if (saved_time.isEmpty()) {
                    timeStr = editTime.getText().toString().trim();
                } else {
                    timeStr = saved_time;
                }

                // SharedPreferences 를 이용해서
                // 앱 내의 저장소에 영구 저장하는 방법
                // 앱을 삭제하기 전까지는 영구적으로 저장된다.
                // 단, 앱을 삭제하면 당연히 같이 삭제된다.
                SharedPreferences sp = getSharedPreferences("Alarm", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("time", timeStr);
                // 이 함수를 실행해야 저장소에 데이터가 저장됨
                editor.apply();

                // 2. EditText의 내용이 있는지 확인해서 없으면, 실행 안시킨다.
                if (timeStr.isEmpty() || !isNumeric(timeStr)) {

                    Toast.makeText(getApplicationContext(), "시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;

                } else {

                    millisInFuture = Long.valueOf(timeStr).longValue() * 1000;

                    // 3. 이상이 없으면 타이머를 만들고, 실행시킨다.
                    timer = new CountDownTimer(millisInFuture, countDownIterval) {
                        @Override
                        public void onTick(long l) {
                            long remain = l / 1000;
                            txtTimer.setText("남은 시간 : " + remain + "초");
                        }

                        @Override
                        public void onFinish() {

                            // 1. 애니메이션 효과
                            YoYo.with(Techniques.Shake).duration(700).repeat(3).playOn(imageView);

                            // 2. 알람음 효과
                            MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);
                            mp.start();

                        }
                    };

                    timer.start();

                }

            }
        });
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // 기계의 back 버튼 눌렀을 때 실행되는 함수
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("앱을 종료하시겠습니까?");
        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.setNegativeButton("no", null);
        alert.show();

    }
}