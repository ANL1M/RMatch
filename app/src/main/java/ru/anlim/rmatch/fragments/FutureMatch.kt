package ru.anlim.rmatch.fragments

import ru.anlim.rmatch.MainActivity.PlaceholderFragment
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import ru.anlim.rmatch.R
import ru.anlim.rmatch.logic.PicaccoHelper
import ru.anlim.rmatch.logic.DBHelper

class FutureMatch : PlaceholderFragment() {
    private var tvHomeFuture: TextView? = null
    private var tvGuestFuture: TextView? = null
    private var tvDateFuture: TextView? = null
    private var tvLigaFuture: TextView? = null
    private var tvVSFuture: TextView? = null
    private var imHomeFuture: ImageView? = null
    private var imGuestFuture: ImageView? = null

    override fun onCreateView (

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

            val rootView = inflater.inflate(R.layout.fragment_future_match, container, false)

            //Инициализация сущностей
            tvHomeFuture = rootView.findViewById(R.id.tvHomeFuture)
            tvGuestFuture = rootView.findViewById(R.id.tvGuestFuture)
            tvDateFuture = rootView.findViewById(R.id.tvDateFuture)
            tvLigaFuture = rootView.findViewById(R.id.tvLigaFuture)
            tvVSFuture = rootView.findViewById(R.id.tvVSFuture)
            imHomeFuture = rootView.findViewById(R.id.imHomeFuture)
            imGuestFuture = rootView.findViewById(R.id.imGuestFuture)

            //Метод наполнения экрана данными
            setResult()
            return rootView
    }

    private fun setResult() {
        val picaccoHelper = PicaccoHelper()
        val dbHelper = DBHelper(activity)
        val hashMap = dbHelper.dbReadResult("FutureMatch")
        tvHomeFuture!!.text = hashMap["Home"]
        tvGuestFuture!!.text = hashMap["Guest"]
        tvDateFuture!!.text = hashMap["MatchDate"]
        tvLigaFuture!!.text = hashMap["Tournament"]
        tvVSFuture!!.text = hashMap["Result"]
        picaccoHelper.loadPic(hashMap["HomeImage"], imHomeFuture)
        picaccoHelper.loadPic(hashMap["GuestImage"], imGuestFuture)
    }
}