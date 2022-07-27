package com.wngml.contactmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.wngml.contactmanager.model.Contact;
import com.wngml.contactmanager.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 테이블 생성
        String CREATE_CONTACT_TABLE = "create table " + Util.TABLE_NAME + "("+
                Util.KEY_ID + " integer primary key, " +
                Util.KEY_NAME + " text, " +
                Util.KEY_PHONE + " text )";

        Log.i("MyContact", "테이블 생성문 : " + CREATE_CONTACT_TABLE);

        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 기존의 contact 테이블을 삭제하고,
        String DROP_TABLE = "drop table " +Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        // 새롭게 테이블을 다시 만든다.
        onCreate(sqLiteDatabase);
    }




    public void addContact(Contact contact){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getWritableDatabase();
        // 테이블의 컬럼이름과 해당 데이터를 매칭해서 넣어준다.
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.name);
        values.put(Util.KEY_PHONE, contact.phone);
        // 데이터베이스에, 위의 데이터를 insert
        db.insert(Util.TABLE_NAME, null, values);
        // db를 닫아줘야 한다.
        db.close();
    }

    // 주소록 데이터 가져오기
    //   - 1개의 주소록 데이터만 가져오기  :  id로 가져오기
    //     select * from contact where id = 3;
    public Contact getContact(int id){
        // 1. 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문 만든다.
        Cursor cursor = db.rawQuery("select * from contact where id = "+id, null);
//        Cursor curosr = db.rawQuery("select * from contact where id = ?", new String[]{""+id});

        if(cursor != null){
            cursor.moveToFirst();
        }

        // 데이터를 가져오려면, 컬럼의 인덱스를 넣어주면 된다.
        // id 를 가져오는 방법
        // cursor.getInt(0);
        // name 을 가져오는 방법
        // cursor.getString(1);
        // phone 을 가져오는 방법
        // cursor.getString(2);

        // DB 에 저장된 데이터를 메모리에다 만들어줘야, cpu가 처리할 수 있다.
        Contact contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

        db.close();

        return contact;
    }


    //   - 주소록 데이터 전체 가져오기
    //     select * from contact;
    //      1, 홍길동, 01022
    //      2, 김나나, 02122

    public ArrayList<Contact> getAllContacts() {

        // 1. 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문 만든다.
        Cursor cursor = db.rawQuery("select * from contact", null);

        ArrayList<Contact> contactList = new ArrayList<Contact>();

//    if(cursor.moveToFirst()){
//        for(int i = 0; i < cursor.getCount(); i++ ){
//            Contact contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//            contactList.add(contact);
//            cursor.moveToNext();
//        }
//    }

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                contactList.add(contact);
            }while(cursor.moveToNext());
        }

        db.close();

        return contactList;
    }

    // 데이터 수정하는 함수
    public void updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(Util.KEY_NAME, contact.name);
//        values.put(Util.KEY_PHONE, contact.phone);
//
//        db.update(Util.TABLE_NAME, values,
//                Util.KEY_ID + "=?", new String[]{contact.id+""});

//        db.execSQL("update contact set name = '"+contact.name+"' , phone = '"+contact.phone+"' where id = "+contact.id);
        db.execSQL("update contact set name = ? , phone = ? where id = ?" ,
                new String[]{contact.name, contact.phone, contact.id+""});

        db.close();

        // update contact set name = '홍길동' , phone = '010-2222-3333' where id = 1;

    }


    public void deleteContact(Contact contact){
        // delete from contact where id = 1;

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from contact where id = ?" , new String[]{ contact.id+""});

        db.close();

    }

}