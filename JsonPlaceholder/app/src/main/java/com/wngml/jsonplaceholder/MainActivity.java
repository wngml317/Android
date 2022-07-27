package com.wngml.jsonplaceholder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txtUserId;
    TextView txtId;
    TextView txtTitle;
    TextView txtBody;

    final String URL = "https://jsonplaceholder.typicode.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUserId = findViewById(R.id.txtUserId);
        txtId = findViewById(R.id.txtId);
        txtTitle = findViewById(R.id.txtTitle);
        txtBody = findViewById(R.id.txtBody);

        String apiUrl = URL + "posts/1";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        // 네트워크 통신을 위한 Request 를 만들어야 하는데
        // JSON 으로 통신하기 때문에
        // JsonObjectRequest 클래스를 객체 생성한다.
        // 생성자는 : (1) http 메소드, (2) API URL, (3) 전달할 데이터(파라미터), (4) 응답 받으면 실행할 코드, (5) 에러를 받으면 실행할 코드
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Placeholder :: ", response.toString());

                // JSON 객체에서 우리가 원하는 정보를 가져와야 된다.
                try {
                    int userId = response.getInt("userId");
                    txtUserId.setText("userId : " + userId);

                    int id = response.getInt("id");
                    txtId.setText("id : " + id);

                    String title = response.getString("title");
                    txtTitle.setText("title : \n" + title);

                    String body = response.getString("body");
                    txtBody.setText("body : \n" + body);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "파싱 에러", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "해당 키값이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 네트워크 통신 중 에러가 발생하면
                // 이 함수 처리
                Toast.makeText(MainActivity.this, "네트워크 에러", Toast.LENGTH_SHORT).show();
                Log.i("Placeholder Error :: ", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}