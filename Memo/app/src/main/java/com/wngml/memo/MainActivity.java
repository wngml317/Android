package com.wngml.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wngml.memo.adpater.MemoAdapter;
import com.wngml.memo.data.DatabaseHandler;
import com.wngml.memo.model.Memo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MemoAdapter adapter;
    ArrayList<Memo> memoList;

    EditText editSearch;
    ImageView imgSearch;
    ImageView imgDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        imgDelete = findViewById(R.id.imgDelete);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. EditText 에서 검색어 가져온다.
                String keyword = editSearch.getText().toString().trim();

                // 2. 검색어로 DB 에 쿼리한다.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.searchMemo(keyword);

                // 3. 검색어로 매칭한 메모 결과들을 화면에 표시한다.
                adapter = new MemoAdapter(MainActivity.this, memoList);

                recyclerView.setAdapter(adapter);
                db.close();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. EditText 의 문자열을 지운다.
                editSearch.setText("");

                // 2. 디비에서 저장되어 있는 모든 메모를 가져와야 한다.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.getAllMemos();

                // 3. 가져온 메모를 화면에 표시
                adapter = new MemoAdapter(MainActivity.this, memoList);
                recyclerView.setAdapter(adapter);
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                // 키워드 검색 EditText 에 글자를 쓸때마다,
                // 자동으로 해당  검색어를 가져와서 디비에서 쿼리해서
                // 검색 결과를 화면에 표시해주는 기능 개발
                // 1. EditText 에서 검색어 가져온다.
                String keyword = editSearch.getText().toString().trim();

                // 검색 키워드가 2글자 미만일 때 return, 검색 불가능
               if (keyword.length() < 2) {
                   return;
                }

                // 2. 검색어로 DB 에 쿼리한다.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.searchMemo(keyword);

                // 3. 검색어로 매칭한 메모 결과들을 화면에 표시한다.
                adapter = new MemoAdapter(MainActivity.this, memoList);

                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        editSearch.setText("");

        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        memoList = db.getAllMemos();
        adapter = new MemoAdapter(MainActivity.this, memoList);
        recyclerView.setAdapter(adapter);
        db.close();
        printDBData();
    }

    // DB 에 있는 모든 메모 데이터를 디버깅용으로 로그캣 출력하는 함수
    void printDBData() {
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        ArrayList<Memo> memoList = db.getAllMemos();
        for (Memo data : memoList) {
            Log.i("MyMemo", "id :" + data.id + ", title :" + data.title + ", content : " + data.content);
        }
        Log.i("MyMemo", "========================================");
        db.close();
    }
}