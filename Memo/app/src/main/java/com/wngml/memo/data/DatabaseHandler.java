package com.wngml.memo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.wngml.memo.model.Memo;
import com.wngml.memo.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // 테이블 생성
        String CREATE_MEMO_TABLE = "create table " + Util.TABLE_NAME + " (" +
                Util.KEY_ID + " integer primary key, " +
                Util.KEY_TITLE + " text, " +
                Util.KEY_CONTENT + " text )";

        Log.i("MyMemo", "테이블 생성문 : " + CREATE_MEMO_TABLE);

        sqLiteDatabase.execSQL(CREATE_MEMO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // 기존의 memo 테이블을 삭제하고,
        String DROP_TABLE = "drop table " +Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        // 새롭게 테이블을 다시 만든다.
        onCreate(sqLiteDatabase);

    }

    // 우리가 앱 동작시크는데 필요한 SQL 문이 적용된 함수들을 만든다.
    // CRUD 관련 함수들을 만든다.

    // 메모를 생성하는 함수
    public void addMemo(Memo memo) {

        // 1. 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. 테이블의 컬럼이름과 해당 데이터를 매칭해서 넣어준다.
        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, memo.title);
        values.put(Util.KEY_CONTENT, memo.content);

        // 3. 데이터베이스에 위의 데이터를 insert 한다.
        db.insert(Util.TABLE_NAME, null, values);

        // 4. db 를 닫아준다.
        db.close();
    }

    // 모든 메모를 보여주는 함수
    public ArrayList<Memo> getAllMemos() {

        // 1. 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문을 만든다.
        Cursor cursor = db.rawQuery("select * from memo", null);


        // 3. 디비에 저장된 메모를 리스트에 저장한다.
        ArrayList<Memo> memoList = new ArrayList<Memo>();

        if (cursor.moveToFirst()) {
            do {
                Memo memo = new Memo(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                memoList.add(memo);
            } while (cursor.moveToNext());
        }

        // 4. db 를 닫아준다.
        db.close();

        return memoList;
    }

    // 특정 메모를 보여주는 함수
    public Memo getMemo(int id) {

        // 1. 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문을 만든다.
        Cursor cursor = db.rawQuery("select * from memo where id = " + id, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        // DB 에 저장된 데이터를 메모리에 만들어주어야 cpu가 처리할 수 있다.
        Memo memo = new Memo(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

        db.close();

        return memo;
    }

    // 메모를 수정하는 함수
    public void updateMemo(Memo memo) {

        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("update memo set title = ?, content = ? where id = ? ",
                new String[]{memo.title, memo.content, memo.id+""});

        db.close();
    }

    // 메모를 삭제하는 함수
    public void deleteMemo(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("delete from memo where id = ?", new String[]{id + ""});

        db.close();
    }

    // 메모 내용을 검색하는 함수
    public ArrayList<Memo> searchMemo(String keyword) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from memo where title like ? or content like ?", new String[]{"%" + keyword + "%", "%" + keyword + "%"});

        ArrayList<Memo> memoList = new ArrayList<Memo>();

        if (cursor.moveToFirst()) {
            do {
                Memo memo = new Memo(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                memoList.add(memo);
            } while (cursor.moveToNext());
        }

        db.close();

        return memoList;
    }
}
