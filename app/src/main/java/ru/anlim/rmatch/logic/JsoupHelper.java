package ru.anlim.rmatch.logic;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import ru.anlim.rmatch.R;

public class JsoupHelper extends AsyncTask<Context, Void, Void> {

    HashMap mapFutureMatch, mapLastMatch, mapLaLiga;


    @Override
    protected Void doInBackground(Context... contexts) {

        mapFutureMatch = new HashMap();
        mapLastMatch = new HashMap();
        Context context = contexts[0];
        DBHelper dbHelper = new DBHelper(context);

        try {
            Document document = Jsoup.connect("http://football.sport-express.ru/command/68").get();

            //Будущий матч
            //Получаем названия команд и изображения для будущего матча
            Elements elementsNextMatch = document.select("#match_box_2 img");
            mapFutureMatch.put("Home", elementsNextMatch.get(1).attr("alt"));
            mapFutureMatch.put("Guest", elementsNextMatch.get(2).attr("alt"));
            mapFutureMatch.put("HomeImage", elementsNextMatch.get(1).attr("src"));
            mapFutureMatch.put("GuestImage", elementsNextMatch.get(2).attr("src"));

            //Получаем дату и название лиги для будущего матча
            Elements elementsDataNextMatch = document.select("#match_box_2 div");
            mapFutureMatch.put("MatchDate",elementsDataNextMatch.get(0).text());
            mapFutureMatch.put("Tournir", elementsDataNextMatch.get(1).text());

            //Прошлый матч
            //Получаем названия команд и изображения для прошлого матча
            Elements elementsLastMatch = document.select("#match_box_1 img");
            mapLastMatch.put("Home", elementsLastMatch.get(1).attr("alt"));
            mapLastMatch.put("Guest", elementsLastMatch.get(2).attr("alt"));
            mapLastMatch.put("HomeImage", elementsLastMatch.get(1).attr("src"));
            mapLastMatch.put("GuestImage", elementsLastMatch.get(2).attr("src"));

            //Получаем дату и название лиги для прошлого матча
            Elements elementsDataLastMatch = document.select("#match_box_1 div");
            mapLastMatch.put("MatchDate", elementsDataLastMatch.get(0).text());
            mapLastMatch.put("Tournir", elementsDataLastMatch.get(1).text());

            //Если было дополниительное время в прошлом матче
            if(mapLastMatch.get("Guest").equals("ДВ")){
                //mapLastMatch.remove("nameGuestLast");
                //mapLastMatch.remove("hrefGuestImageLM");
                //mapLastMatch.remove("tournirResLast");

                mapLastMatch.put("Guest", elementsLastMatch.get(3).attr("alt"));
                mapLastMatch.put("GuestImage", elementsLastMatch.get(3).attr("src"));
                mapLastMatch.put("Tournir", elementsDataLastMatch.get(2).text());
            }

            //Получаем результат прошлого матча
            Elements elementsResultLastMatch = document.select("#match_box_1 span");
            mapLastMatch.put("Result", elementsResultLastMatch.get(1).text());

            dbHelper.dbWriteResult(mapLastMatch, 0);
            dbHelper.dbWriteResult(mapFutureMatch, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
