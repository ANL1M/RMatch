package ru.anlim.rmatch.logic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.text.TextUtils.replace
import java.util.ArrayList
import java.util.HashMap

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + TABLE_LAST_MATCH + "("
                    + KEY_ID + " integer primary key,"
                    + Home + " text,"
                    + Guest + " text,"
                    + HomeImage + " text,"
                    + GuestImage + " text,"
                    + MatchDate + " text,"
                    + Tournament + " text,"
                    + Result + " text"
                    + ")"
        )
        db.execSQL(
            "create table " + TABLE_FUTURE_MATCH + "("
                    + KEY_ID + " integer primary key,"
                    + Home + " text,"
                    + Guest + " text,"
                    + HomeImage + " text,"
                    + GuestImage + " text,"
                    + MatchDate + " text,"
                    + Tournament + " text,"
                    + Result + " text"
                    + ")"
        )
        db.execSQL(
            "create table " + TABLE_LA_LIGA + "("
                    + KEY_ID + " integer primary key,"
                    + Team + " text,"
                    + Games + " text,"
                    + Wins + " text,"
                    + Draw + " text,"
                    + Lose + " text,"
                    + Diff + " text,"
                    + Points + " text,"
                    + ImageURL + " text"
                    + ")"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
    fun dbWriteResult(hashMap: HashMap<*, *>, tableName: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Home, hashMap[Home] as String?)
        cv.put(Guest, hashMap[Guest] as String?)
        cv.put(HomeImage, hashMap[HomeImage] as String?)
        cv.put(GuestImage, hashMap[GuestImage] as String?)
        cv.put(MatchDate, hashMap[MatchDate] as String?)
        cv.put(Tournament, hashMap[Tournament] as String?)
        cv.put(Result, hashMap[Result] as String?)
        db.execSQL("delete from $tableName")
        db.insert(tableName, null, cv)
        cv.clear()
        db.close()
        dbUpdateName()
    }

    fun dbWriteLiga(arrayList: ArrayList<*>) {
        val db = this.writableDatabase
        val cv = ContentValues()
        db.execSQL("delete from $TABLE_LA_LIGA")

        // 220 для сезона, 180 для начала и конца сезона, 140 для межсезонья
        val countIndex = arrayList.size / 20
        var i = 0
        while (i < arrayList.size) {
            cv.put(Team, arrayList[i + 1] as String)
            cv.put(Games, arrayList[i + 2] as String)
            cv.put(Wins, arrayList[i + 3] as String)
            cv.put(Draw, arrayList[i + 4] as String)
            cv.put(Lose, arrayList[i + 5] as String)
            cv.put(Diff, arrayList[i + 6] as String)
            cv.put(Points, arrayList[i + 7] as String)
            db.insert(TABLE_LA_LIGA, null, cv)
            i += countIndex
        }
        cv.clear()
        db.close()
    }

    fun dbWriteURLImageLaLiga(arrayList: ArrayList<*>) {
        val db = this.writableDatabase
        for (i in arrayList.indices) {
            val j = i + 1
            db.execSQL("update LaLiga set ImageURL = \" " + arrayList[i] + " \" where _id = " + j)
        }
        db.close()
    }

    fun dbReadResult(tableName: String?): HashMap<String, String> {
        val db = this.readableDatabase
        val cursor = db.query(tableName, null, null, null, null, null, null)
        cursor.moveToFirst()
        val hashMap = HashMap<String, String>()
        if (cursor.count == 1) {
            hashMap[Home] =
                cursor.getString(cursor.getColumnIndexOrThrow(Home))
            hashMap[Guest] =
                cursor.getString(cursor.getColumnIndexOrThrow(Guest))
            hashMap[HomeImage] =
                cursor.getString(cursor.getColumnIndexOrThrow(HomeImage))
            hashMap[GuestImage] =
                cursor.getString(cursor.getColumnIndexOrThrow(GuestImage))
            hashMap[MatchDate] =
                cursor.getString(cursor.getColumnIndexOrThrow(MatchDate))
            hashMap[Tournament] = cursor.getString(cursor.getColumnIndexOrThrow(Tournament))
            hashMap[Result] = cursor.getString(cursor.getColumnIndexOrThrow(Result))
        }
        cursor.close()
        db.close()
        return hashMap
    }

    fun dbReadLiga(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_LA_LIGA, null, null, null, null, null, null)
    }

    private fun dbUpdateName() {
        val db = this.writableDatabase
        db.execSQL("update LastMatch set Guest = \"Реал Мадрид\" where Guest = \"Реал\"")
        db.execSQL("update FutureMatch set Guest = \"Реал Мадрид\" where Guest = \"Реал\"")
        db.execSQL("update LastMatch set Home = \"Реал Мадрид\" where Home = \"Реал\"")
        db.execSQL("update FutureMatch set Home = \"Реал Мадрид\" where Home = \"Реал\"")
        db.execSQL("update LaLiga set Team = \"Реал Мадрид\" where Team = \"Реал\"")
        db.execSQL("update LastMatch set HomeImage = " +  replace (HomeImage, arrayOf("https"), arrayOf("http")) + ")")
        db.execSQL("update LastMatch set GuestImage = " +  replace (GuestImage, arrayOf("https"), arrayOf("http")) + ")")
        db.execSQL("update FutureMatch set HomeImage " +  replace (HomeImage, arrayOf("https"), arrayOf("http")) + ")")
        db.execSQL("update FutureMatch set GuestImage " +  replace (GuestImage, arrayOf("https"), arrayOf("http")) + ")")
        db.execSQL("update LastMatch set HomeImage = " + replace (HomeImage, arrayOf("_medium"), arrayOf("")) + ")")
        db.execSQL("update LastMatch set GuestImage = " + replace (GuestImage, arrayOf("_medium"), arrayOf("")) + ")")
        db.execSQL("update FutureMatch set HomeImage = " + replace (HomeImage, arrayOf("_medium"), arrayOf("")) + ")")
        db.execSQL("update FutureMatch set GuestImage = " + replace (GuestImage, arrayOf("_medium"), arrayOf("")) + ")")
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "rmMatchDB.db"
        private const val TABLE_LAST_MATCH = "LastMatch"
        private const val TABLE_FUTURE_MATCH = "FutureMatch"
        private const val TABLE_LA_LIGA = "LaLiga"
        private const val KEY_ID = "_id"
        private const val Home = "Home"
        private const val Guest = "Guest"
        private const val HomeImage = "HomeImage"
        private const val GuestImage = "GuestImage"
        private const val MatchDate = "MatchDate"
        private const val Tournament = "Tournament"
        private const val Result = "Result"
        private const val Team = "Team"
        private const val Games = "Games"
        private const val Wins = "Wins"
        private const val Draw = "Draw"
        private const val Lose = "Lose"
        private const val Diff = "Diff"
        private const val Points = "Points"
        private const val ImageURL = "ImageURL"
    }
}