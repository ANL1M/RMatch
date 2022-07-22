package ru.anlim.rmatch.fragments

import android.content.Context
import android.database.Cursor
import ru.anlim.rmatch.MainActivity.PlaceholderFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.ListView
import ru.anlim.rmatch.R
import ru.anlim.rmatch.logic.DBHelper
import ru.anlim.rmatch.logic.PicaccoHelper
import android.widget.TextView

class LaLiga : PlaceholderFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

            val rootView = inflater.inflate(R.layout.fragment_la_liga, container, false)

            //Инициализация сущностей
            val dbHelper = DBHelper(activity)
            val cursor = dbHelper.dbReadLiga()
            val lvLiga = rootView.findViewById<ListView>(R.id.lvLiga)

            //Метод наполнения экрана данными
            setResult(lvLiga, cursor)
            return rootView
    }

    private fun setResult(lvLiga: ListView, cursor: Cursor?) {
        val picaccoHelper = PicaccoHelper()
        lvLiga.adapter = object : CursorAdapter(activity, cursor, 1) {
            override fun newView(context: Context, cursor: Cursor, viewGroup: ViewGroup): View {
                return LayoutInflater.from(activity).inflate(R.layout.item, viewGroup, false)
            }

            override fun bindView(view: View, context: Context, cursor: Cursor) {
                val tvNumber = view.findViewById<TextView>(R.id.tvNumber)
                val tvTeam = view.findViewById<TextView>(R.id.tvTeam)
                val tvGames = view.findViewById<TextView>(R.id.tvGames)
                val tvWin = view.findViewById<TextView>(R.id.tvWin)
                val tvDraw = view.findViewById<TextView>(R.id.tvDraw)
                val tvLose = view.findViewById<TextView>(R.id.tvLose)
                val tvDiff = view.findViewById<TextView>(R.id.tvDiff)
                val tvPoints = view.findViewById<TextView>(R.id.tvPoints)
                val ivTeam = view.findViewById<ImageView>(R.id.imTeam)
                tvNumber.text = cursor.getInt(cursor.getColumnIndexOrThrow("_id")).toString()
                tvTeam.text = cursor.getString(cursor.getColumnIndexOrThrow("Team"))
                tvGames.text = cursor.getString(cursor.getColumnIndexOrThrow("Games"))
                tvWin.text = cursor.getString(cursor.getColumnIndexOrThrow("Wins"))
                tvDraw.text = cursor.getString(cursor.getColumnIndexOrThrow("Draw"))
                tvLose.text = cursor.getString(cursor.getColumnIndexOrThrow("Lose"))
                tvDiff.text = cursor.getString(cursor.getColumnIndexOrThrow("Diff"))
                tvPoints.text = cursor.getString(cursor.getColumnIndexOrThrow("Points"))
                picaccoHelper.loadPic(
                    cursor.getString(cursor.getColumnIndexOrThrow("ImageURL")),
                    ivTeam
                )
            }
        }
    }
}