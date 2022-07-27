package com.wngml.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editYear;
    Button button;
    TextView txtAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editYear = findViewById(R.id.editYear);
        button = findViewById(R.id.button);
        txtAge = findViewById(R.id.txtAge);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. 유저가 입력한 년도를 가져온다.
                String yearStr = editYear.getText().toString();

                // 2. 현재 년도와 계산한다.
                // 현재년도 - 유저가 입력한 년도
                int this_year = Calendar.getInstance().get(Calendar.YEAR);
                int born_year = Integer.valueOf(yearStr).intValue();

                int age = this_year - born_year;

                // 3. 나이를 화면에 표시한다.
                txtAge.setText("" + age + "살 입니다.");
                txtAge.setBackgroundColor(Color.parseColor("#EEEEEE"));

                editYear.setText("");
            }
        });
    }
}