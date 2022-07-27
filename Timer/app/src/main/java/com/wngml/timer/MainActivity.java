package com.wngml.timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtTimer;
    TextView txtCount;
    Button button;

    // 타이머를 사용하기 위한 멤버변수로 선언
    CountDownTimer timer;

    final int millisInFuture = 10000;
    final int countDownInterval = 1000;

    int count = 0;

    boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer = findViewById(R.id.txtTimer);
        txtCount = findViewById(R.id.txtCount);
        button = findViewById(R.id.button);

        timer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {
                // 남은 시간을  계산해서 텍스트뷰에 보여준다.
                // Log.i("MyTimer :: ", "남은 시간 : " + l);

                // long remain = l / 1000;
                int remain = (int)  l / 1000;
                txtTimer.setText("남은 시간 : " + remain);

            }

            @Override
            public void onFinish() {
                // Log.i("MyTimer :: ", "=================== 타이머 끝 ==================");
                isFinish = true;

                // Alert dialog (팝업) 를 띄어서
                // 한번 더 할것인지, 앱을 종료할 것인지 물어본다.
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("게임 종료!");
                alert.setMessage("한번 더 하시겠습니까?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer.start();
                        count = 0;
                        txtCount.setText("탭한 갯수");
                        isFinish = false;
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 액티비티 종료
                        finish();
                    }
                });
                // 이 alert dialog 의 버튼을
                // 안누르면 화면이 넘어가지 않도록 하는 방법
                alert.setCancelable(false);
                // alert dialog 를 화면에 표시
                alert.show();
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFinish == false) {
                    count += 1;
                    txtCount.setText("탭한 갯수 : " + count);
                }
            }
        });

        timer.start();

    }
}