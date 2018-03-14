package ru.anlim.rmatch.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rmMatchDB.db";
    public static final String TABLE_LAST_MATCH = "LastMatch";
    public static final String TABLE_FUTURE_MATCH = "FutureMatch";
    public static final String TABLE_LA_LIGA = "LaLiga";

    public static final String KEY_ID = "_id";
    public static final String KEY_RESULT = "result";

    public static final String Home         = "Home";
    public static final String Guest        = "Guest";
    public static final String HomeImage    = "HomeImage";
    public static final String GuestImage   = "GuestImage";
    public static final String MatchDate    = "MatchDate";
    public static final String Tournir      = "Tournir";
    public static final String Result       = "Result";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LAST_MATCH + "("
                + KEY_ID        + " integer primary key,"
                + Home          + " text,"
                + Guest         + " text,"
                + HomeImage     + " text,"
                + GuestImage    + " text,"
                + MatchDate     + " text,"
                + Tournir       + " text,"
                + Result        + " text"
                + ")");

        db.execSQL("create table " + TABLE_FUTURE_MATCH + "("
                + KEY_ID        + " integer primary key,"
                + Home          + " text,"
                + Guest         + " text,"
                + HomeImage     + " text,"
                + GuestImage    + " text,"
                + MatchDate     + " text,"
                + Tournir       + " text,"
                + Result        + " text"
                + ")");

        db.execSQL("create table " + TABLE_LA_LIGA + "(" + KEY_ID + " integer primary key," + KEY_RESULT + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void dbWriteResult(HashMap hashMap, String tableName){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Home      ,(String) hashMap.get(Home         ));
        cv.put(Guest     ,(String) hashMap.get(Guest        ));
        cv.put(HomeImage ,(String) hashMap.get(HomeImage    ));
        cv.put(GuestImage,(String) hashMap.get(GuestImage   ));
        cv.put(MatchDate ,(String) hashMap.get(MatchDate    ));
        cv.put(Tournir   ,(String) hashMap.get(Tournir      ));
        cv.put(Result    ,(String) hashMap.get(Result       ));

        db.execSQL("delete from "+ tableName);
        db.insert(tableName, null,cv);

        cv.clear();
        db.close();
        dbUpdateName();
    }

    public HashMap<String, String> dbReadResult(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName,null, null, null, null, null, null);
        cursor.moveToFirst();

        HashMap<String, String> hashMap = new HashMap<>();
        if (cursor.getCount() == 1){
            hashMap.put(Home      , cursor.getString(cursor.getColumnIndexOrThrow(Home       )));
            hashMap.put(Guest     , cursor.getString(cursor.getColumnIndexOrThrow(Guest      )));
            hashMap.put(HomeImage , cursor.getString(cursor.getColumnIndexOrThrow(HomeImage  )));
            hashMap.put(GuestImage, cursor.getString(cursor.getColumnIndexOrThrow(GuestImage )));
            hashMap.put(MatchDate , cursor.getString(cursor.getColumnIndexOrThrow(MatchDate  )));
            hashMap.put(Tournir   , cursor.getString(cursor.getColumnIndexOrThrow(Tournir    )));
            hashMap.put(Result    , cursor.getString(cursor.getColumnIndexOrThrow(Result     )));
        }

        cursor.close();
        db.close();
        return hashMap;
    }

    private void dbUpdateName(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update LastMatch set Guest = \"Реал Мадрид\" where Guest = \"Реал\"");
        db.execSQL("update FutureMatch set Guest = \"Реал Мадрид\" where Guest = \"Реал\"");
        db.execSQL("update LastMatch set Home = \"Реал Мадрид\" where Home = \"Реал\"");
        db.execSQL("update FutureMatch set Home = \"Реал Мадрид\" where Home = \"Реал\"");

        db.execSQL("update LastMatch set HomeImage = replace (HomeImage, \"_medium\", \"\")");
        db.execSQL("update LastMatch set GuestImage = replace (GuestImage, \"_medium\", \"\")");
        db.execSQL("update FutureMatch set HomeImage = replace (HomeImage, \"_medium\", \"\")");
        db.execSQL("update FutureMatch set GuestImage = replace (GuestImage, \"_medium\", \"\")");

        db.close();
    }
}
