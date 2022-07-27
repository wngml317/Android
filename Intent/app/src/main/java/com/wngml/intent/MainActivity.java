package com.wngml.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                selectContact();
//                openWebPage("http://naver.com");
//                composeSMS("010-1234-5678");
//                composeEmail(new String[]{"abc@naver.com"}, "안녕하세요");
            }
        });
    }

    // 연락처 가져오기
    void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivity(intent);
    }

    // 웹사이트 연결
    void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        startActivity(intent);
    }

   /*
    // 문자메시지 앱으로 이동, 문자 보낼 전화번호 세팅
    void composeSMS(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms_to : " + phoneNumber));
        startActivity(intent);
    }

    // 메일 보낼 주소 세팅
    void composeEmail(String[] address, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mail_to:"));
        intent.putExtra(intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);
    }
    */
}