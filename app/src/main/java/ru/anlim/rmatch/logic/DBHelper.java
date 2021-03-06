package ru.anlim.rmatch.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rmMatchDB.db";
    private static final String TABLE_LAST_MATCH = "LastMatch";
    private static final String TABLE_FUTURE_MATCH = "FutureMatch";
    private static final String TABLE_LA_LIGA = "LaLiga";

    private static final String KEY_ID = "_id";
    private static final String Home         = "Home";
    private static final String Guest        = "Guest";
    private static final String HomeImage    = "HomeImage";
    private static final String GuestImage   = "GuestImage";
    private static final String MatchDate    = "MatchDate";
    private static final String Tournir      = "Tournir";
    private static final String Result       = "Result";
    private static final String Team         = "Team";
    private static final String Games        = "Games";
    private static final String Wins         = "Wins";
    private static final String Draw         = "Draw";
    private static final String Lose         = "Lose";
    private static final String Diff         = "Diff";
    private static final String Points       = "Points";
    private static final String ImageURL     = "ImageURL";

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

        db.execSQL("create table " + TABLE_LA_LIGA + "("
                + KEY_ID    + " integer primary key,"
                + Team      + " text,"
                + Games     + " text,"
                + Wins      + " text,"
                + Draw      + " text,"
                + Lose      + " text,"
                + Diff      + " text,"
                + Points    + " text,"
                + ImageURL    + " text"
                + ")");
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

    public void dbWriteLiga(ArrayList arrayList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        db.execSQL("delete from "+ TABLE_LA_LIGA);

        // 220 для сезона, 180 для начала и конца сезона, 140 для межсезонья
        int countIndex = arrayList.size() / 20;

        for (int i = 0; i < arrayList.size(); i = i + countIndex) {
            cv.put(Team   , (String) arrayList.get(i+1));
            cv.put(Games  , (String) arrayList.get(i+2));
            cv.put(Wins   , (String) arrayList.get(i+3));
            cv.put(Draw   , (String) arrayList.get(i+4));
            cv.put(Lose   , (String) arrayList.get(i+5));
            cv.put(Diff   , (String) arrayList.get(i+6));
            cv.put(Points , (String) arrayList.get(i+7));

            db.insert(TABLE_LA_LIGA, null,cv);
        }

        cv.clear();
        db.close();
    }

    public void dbWriteURLImageLaLiga(ArrayList arrayList){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            int j = i+1;
            db.execSQL("update LaLiga set ImageURL = \" " + arrayList.get(i)+ " \" where _id = " + j );
        }
        db.close();
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

    public Cursor dbReadLiga(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LA_LIGA, null, null, null, null, null, null);
    }

    private void dbUpdateName(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update LastMatch set Guest = \"Реал Мадрид\" where Guest = \"Реал\"");
        db.execSQL("update FutureMatch set Guest = \"Реал Мадрид\" where Guest = \"Реал\"");
        db.execSQL("update LastMatch set Home = \"Реал Мадрид\" where Home = \"Реал\"");
        db.execSQL("update FutureMatch set Home = \"Реал Мадрид\" where Home = \"Реал\"");

        db.execSQL("update LaLiga set Team = \"Реал Мадрид\" where Team = \"Реал\"");

        db.execSQL("update LastMatch set HomeImage = replace (HomeImage, \"https\", \"http\")");
        db.execSQL("update LastMatch set GuestImage = replace (GuestImage, \"https\", \"http\")");
        db.execSQL("update FutureMatch set HomeImage = replace (HomeImage, \"https\", \"http\")");
        db.execSQL("update FutureMatch set GuestImage = replace (GuestImage, \"https\", \"http\")");

        db.execSQL("update LastMatch set HomeImage = replace (HomeImage, \"_medium\", \"\")");
        db.execSQL("update LastMatch set GuestImage = replace (GuestImage, \"_medium\", \"\")");
        db.execSQL("update FutureMatch set HomeImage = replace (HomeImage, \"_medium\", \"\")");
        db.execSQL("update FutureMatch set GuestImage = replace (GuestImage, \"_medium\", \"\")");

        db.close();
    }
}
