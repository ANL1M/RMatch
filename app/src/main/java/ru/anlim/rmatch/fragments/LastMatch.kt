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

class LastMatch : PlaceholderFragment() {
    private var tvHomeLast: TextView? = null
    private var tvGuestLast: TextView? = null
    private var tvDateLast: TextView? = null
    private var tvLigaLast: TextView? = null
    private var tvResultLast: TextView? = null
    private var imHomeLast: ImageView? = null
    private var imGuestLast: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

            val rootView = inflater.inflate(R.layout.fragment_last_match, container, false)

            //Инициализация сущностей
            tvHomeLast = rootView.findViewById(R.id.tvHomeLast)
            tvGuestLast = rootView.findViewById(R.id.tvGuestLast)
            tvDateLast = rootView.findViewById(R.id.tvDateLast)
            tvLigaLast = rootView.findViewById(R.id.tvLigaLast)
            tvResultLast = rootView.findViewById(R.id.tvResultLast)
            imHomeLast = rootView.findViewById(R.id.imHomeLast)
            imGuestLast = rootView.findViewById(R.id.imGuestLast)

            //Метод наполнения экрана данными
            setResult()
            return rootView
    }

    private fun setResult() {
        val picaccoHelper = PicaccoHelper()
        val dbHelper = DBHelper(activity)
        val hashMap = dbHelper.dbReadResult("LastMatch")
        tvHomeLast!!.text = hashMap["Home"]
        tvGuestLast!!.text = hashMap["Guest"]
        tvDateLast!!.text = hashMap["MatchDate"]
        tvLigaLast!!.text = hashMap["Tournament"]
        tvResultLast!!.text = hashMap["Result"]
        picaccoHelper.loadPic(hashMap["HomeImage"], imHomeLast)
        picaccoHelper.loadPic(hashMap["GuestImage"], imGuestLast)
    }
}