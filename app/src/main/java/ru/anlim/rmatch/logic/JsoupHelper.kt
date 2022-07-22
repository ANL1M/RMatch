package ru.anlim.rmatch.logic

import ru.anlim.rmatch.MainActivity
import android.os.AsyncTask
import org.jsoup.Jsoup
import android.widget.Toast
import ru.anlim.rmatch.R
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class JsoupHelper : AsyncTask<Void, Void, String>() {
    private var noInternetException = false

    override fun doInBackground(vararg params: Void?): String? {
        val mapFutureMatch = HashMap<String, String>()
        val mapLastMatch = HashMap<String, String>()
        val listLaliga = ArrayList<String>()
        val listURLLiga = ArrayList<String>()
        val dbHelper = DBHelper(MainActivity().context)
        try {
            val document = Jsoup.connect("https://www.sport-express.ru/football/L/command/68").get()
            val elementsMatch = document.select("table a")

            //Будущий матч
            //Получаем названия команд и изображения для будущего матча
            mapFutureMatch["Home"] = elementsMatch[6].text()
            mapFutureMatch["Guest"] = elementsMatch[7].text()
            mapFutureMatch["MatchDate"] = elementsMatch[8].text()
            mapFutureMatch["Result"] = ""
            var numberTeam = elementsMatch[6].attr("href").replace("[^0-9]".toRegex(), "")
            var urlImage = "http://ss.sport-express.ru/img/football/commands/$numberTeam.png"
            mapFutureMatch["HomeImage"] = urlImage
            numberTeam = elementsMatch[7].attr("href").replace("[^0-9]".toRegex(), "")
            urlImage = "http://ss.sport-express.ru/img/football/commands/$numberTeam.png"
            mapFutureMatch["GuestImage"] = urlImage


            //Прошлый матч
            //Получаем названия команд и изображения для прошлого матча
            mapLastMatch["Home"] = elementsMatch[3].text()
            mapLastMatch["Guest"] = elementsMatch[4].text()
            mapLastMatch["MatchDate"] = ""
            mapLastMatch["Result"] = elementsMatch[5].text()
            numberTeam = elementsMatch[3].attr("href").replace("[^0-9]".toRegex(), "")
            urlImage = "http://ss.sport-express.ru/img/football/commands/$numberTeam.png"
            mapLastMatch["HomeImage"] = urlImage
            numberTeam = elementsMatch[4].attr("href").replace("[^0-9]".toRegex(), "")
            urlImage = "http://ss.sport-express.ru/img/football/commands/$numberTeam.png"
            mapLastMatch["GuestImage"] = urlImage

            //Получаем название лиги для будущего и прошлого матча
            val elementsTournament = document.select("td")
            mapFutureMatch["Tournament"] = elementsTournament[18].text()
            mapLastMatch["Tournament"] = elementsTournament[14].text()

            //Получение данных по таблице
            val documentLiga =
                Jsoup.connect("https://www.sport-express.ru/football/L/foreign/spain/laleague")
                    .get()
            val elements = documentLiga.select(".m_all")
            for (i in elements.indices) {
                listLaliga.add(elements[i].text())
            }
            val elements2 = documentLiga.select(".m_all a")

            // 100 для сезона, 60 для начала и конца сезона, 20 для межсезонья
            val countIndex = elements2.size / 20
            var i = 0
            while (i < elements2.size) {
                // +3 для межсезонья +5 для сезона
                val urlTeam = elements2[i].attr("href").toString()
                numberTeam = urlTeam.replace("[^0-9]".toRegex(), "")
                urlImage = "http://ss.sport-express.ru/img/football/commands/$numberTeam.png"
                listURLLiga.add(urlImage)
                i += countIndex
            }
            dbHelper.dbWriteLiga(listLaliga)
            dbHelper.dbWriteURLImageLaLiga(listURLLiga)
            dbHelper.dbWriteResult(mapLastMatch, "LastMatch")

            if (mapFutureMatch.isNotEmpty()) dbHelper.dbWriteResult(mapFutureMatch, "FutureMatch")
                noInternetException = false
        } catch (e: IOException) {
            e.printStackTrace()
            noInternetException = true
        }
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(null)
        if (noInternetException) {
            Toast.makeText(MainActivity().context, R.string.Error, Toast.LENGTH_SHORT).show()
        }
        MainActivity().setResult()
    }
}