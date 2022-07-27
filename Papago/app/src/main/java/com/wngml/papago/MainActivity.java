package com.wngml.papago;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wngml.papago.config.Config;
import com.wngml.papago.model.Papago;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioLang;
    EditText editText;
    Button button;
    TextView txtResult;
    ArrayList<Papago> papagoList = new ArrayList<Papago>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioLang = findViewById(R.id.radioLang);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        txtResult = findViewById(R.id.txtResult);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source = "ko";

                // 1. 라디오 버튼에서 무엇을 눌렀는지 데이터를 가져온다.
                int radioBtnId = radioLang.getCheckedRadioButtonId();
                String target;

                if(radioBtnId == R.id.radioBtn1) {
                    target = "en";
                } else if (radioBtnId == R.id.radioBtn2) {
                    target = "zh-CN";
                } else if (radioBtnId == R.id.radioBtn3) {
                    target = "zh-TW";
                } else if (radioBtnId == R.id.radioBtn4) {
                    target = "th";
                } else {
                    Toast.makeText(getApplicationContext(), "언어를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. 에디트텍스트에서 문장을 가져온다.
                String text = editText.getText().toString().trim();
                if(text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "변역할 문장을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. 네이버 API 호출해서
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                StringRequest request = new StringRequest(Request.Method.POST, Config.URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // 4. 결과를 텍스트뷰에 보여준다.

                                try {
                                    JSONObject result = new JSONObject(response);
                                    String translatedText = result.getJSONObject("message").getJSONObject("result").getString("translatedText");
                                    txtResult.setText(translatedText);

                                    // 히스토리 저장
                                    Papago papago = new Papago(text, translatedText);

                                    papagoList.add(0, papago);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                        params.put("X-Naver-Client-Id" , Config.CLIENT_ID);
                        params.put("X-Naver-Client-Secret", Config.CLIENT_SECRET);
                        return params;
                    }
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("source", source);
                        params.put("target", target);
                        params.put("text", text);
                        return params;
                    }

                };
                queue.add(request);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.menuHistory) {
            // 히스토리 액티비티를  실행
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("papagoList", papagoList);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}