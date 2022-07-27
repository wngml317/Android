package com.wngml.pcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editPer;
    EditText editNum;
    Button button;
    TextView txtResult;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면과 연결
        editPer = findViewById(R.id.editPer);
        editNum = findViewById(R.id.editNum);
        button = findViewById(R.id.button);
        txtResult = findViewById(R.id.txtResult);

        // 버튼 눌렀을때 동작하는 코드 정의
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. 에디트 텍스트에 적힌 문자열을 가져온다.
                String perStr = editPer.getText().toString();
                String numStr = editNum.getText().toString();

                // 2. 계산한다.
                // 먼저 문자열을 double 형으로 변환
                double percent = Double.valueOf(perStr).doubleValue();
                double number = Double.valueOf(numStr).doubleValue();

                // 결과를 다시
                //String result = Double.toString(percent * number / 100);

                double result = percent * number / 100;
                // 3. 결과를 텍스트 뷰에 보여준다.
                txtResult.setText(numStr + "를 " + perStr + "% 계산 = " + result);

                // 4. 다음 입력을 위해서 이전에 입력한 글자들은 지운다.
                editNum.setText("");
                editPer.setText("");

                // 5. 버튼을 클릭한 횟수를 저장한다.
                count += 1;

                // 6. 계산한 횟수가 5의 배수이면 토스트를 보여준다.
                if(count % 5 == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "현재까지 " + count+ "번 계산하였습니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }

            }
        });
    }
}