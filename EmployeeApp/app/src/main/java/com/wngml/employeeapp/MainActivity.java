package com.wngml.employeeapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wngml.employeeapp.adapter.EmployeeAdapter;
import com.wngml.employeeapp.data.Share;
import com.wngml.employeeapp.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    // 어댑터, 어레이리스트
    EmployeeAdapter adapter;
    // 네트워크로부터 데이터를 받아올 때
    // 꼭 new 를 사용하여 객체 생성을 해주어야 한다.
    // ArrayList<Employee> employeeList = new ArrayList<Employee>();

    // employeeList 를 공유하기 위한 싱그톤 클래스
    Share share;

    // onResume 함수를 위한 flag
    boolean hasData = false;

    // 네트워크로 데이터 주고받을 때 표시용
    ProgressBar progressBar;

    final String URL = "http://dummy.restapiexample.com/api/v1/employees";

    // 내가 실행한 액티비티로부터 데이터를 다시 받아올 때 작성하는 코드
    /*
    public ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // 받아왔을 때 실행되는 코드
                    // result 는 EditActivity 에서 받아온 setResult(RESULT_OK, intent)
                    if(result.getResultCode() == RESULT_OK) {
                        int age = result.getData().getIntExtra("age", 0);
                        int salary = result.getData().getIntExtra("salary", 0);
                        int index = result.getData().getIntExtra("index", 0);

                        // 데이터를 저장하고 있는 리스트에서 해당 행에 매칭되는
                        // employee 객체를 가져와서, 데이터를 수정
                        Employee employee = employeeList.get(index);
                        employee.age = age;
                        employee.salary = salary;

                        adapter.notifyDataSetChanged();

                    }
                }
            });
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바를 가져오는 방법
//        ActionBar actionBar = getActionBar();
        getSupportActionBar().setTitle(R.string.title_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        share = Share.getInstance();

        // 네트워크를 통해서 데이터를 받아온다.

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("EmployeeApp", response.toString());

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for(int i=0; i < jsonArray.length(); i++) {
                        JSONObject emObj = jsonArray.getJSONObject(i);

                        int id = emObj.getInt("id");
                        String name = emObj.getString("employee_name");
                        int age = emObj.getInt("employee_age");
                        int salary = emObj.getInt("employee_salary");

                        // 클래스의 객체로 만들어서
                        Employee employee = new Employee(id, name, age, salary);

                        // 멤버변수인 리스트에 담아줘야 cpu가 일할 수 있다.
                        // employeeList.add(employee);
                        share.employeeList.add(employee);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 어댑터를 만들어서 실제 화면에 데이터를 표시하도록 한다.
                // adapter = new EmployeeAdapter(MainActivity.this, employeeList);
                adapter = new EmployeeAdapter(MainActivity.this, share.employeeList);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);

                hasData = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EmployeeApp", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasData) {
            adapter.notifyDataSetChanged();
        }
    }

}