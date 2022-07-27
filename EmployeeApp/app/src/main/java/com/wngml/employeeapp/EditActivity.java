package com.wngml.employeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wngml.employeeapp.data.Share;
import com.wngml.employeeapp.model.Employee;

public class EditActivity extends AppCompatActivity {

    Employee employee;

    EditText editAge;
    EditText editSalary;
    Button btnSave;

    int index;

    Share share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 액션바 타이틀 설정
        getSupportActionBar().setTitle(R.string.title_edit);

        // 액션바 back 버튼 세팅
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Share 클래스는 싱글톤이기 때문에, 메모리에 한군데만 있다.
        // 따라서 getInstance() 함수를 이용하면
        // 한군데만 존재하는 객체를 가져오는 것이다.
        Share share = Share.getInstance();

        // 다른 액티비티로부터 받아온 데이터를 저장
        // employee = (Employee) getIntent().getSerializableExtra("employee");
        index = getIntent().getIntExtra("index", 0);

        editAge = findViewById(R.id.editAge);
        editSalary = findViewById(R.id.editSalary);
        btnSave = findViewById(R.id.btnSave);

        // editAge.setText(""+employee.age);
        // editSalary.setText(""+employee.salary);

        // Share 클래스에 들어있는 employeeList 에 직접 억세스가 기능하므로
        // 바로 나이와 연봉 정보를 가져와서 화면에 보여줄 수 있다.
        editAge.setText(share.employeeList.get(index).age + "");
        editSalary.setText(share.employeeList.get(index).salary + "");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ageStr = editAge.getText().toString().trim();
                String salaryStr = editSalary.getText().toString().trim();

                if (ageStr.isEmpty() || salaryStr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "나이와 연봉을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age = Integer.valueOf(ageStr).intValue();
                int salary = Integer.valueOf(salaryStr).intValue();

                // 바뀐 내용은 바로 Share 클래스의 employeeList 에
                // 직접 바뀐 내용을 저장하고,
                // 이 액티비티는 종료하면 된다.
                share.employeeList.get(index).age = age;
                share.employeeList.get(index).salary = salary;

/*

                Intent intent = new Intent();
                intent.putExtra("age", age);
                intent.putExtra("salary", salary);

                // 이 두개 정보로는 정보가 부족하다
                intent.putExtra("index", index);

                setResult(RESULT_OK, intent);
*/

                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        // 1. finish() 이용
        finish();

        // 2. 기계의 백버튼 눌렀을때 호출되는 콜백함수를 이용
        // onBackPressed();

        return true;
    }
}