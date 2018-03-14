package ru.anlim.rmatch.logic;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.fragments.FutureMatch;


public class JsoupHelper extends AsyncTask<Integer, Void, Context> {

    private Boolean noInternetException;
    private int typeLoad;
    private FutureMatch futureMatch;
    private MainActivity mainActivity;
    private Context context;

    public JsoupHelper(FutureMatch futureMatch){
        this.futureMatch = futureMatch;
    }

    public JsoupHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected Context doInBackground(Integer ... integers) {

        HashMap mapFutureMatch = new HashMap();
        HashMap mapLastMatch = new HashMap();
        typeLoad = integers[0];

        switch (integers[0]){
            case 101:
                context = futureMatch.getContext();
                break;
            case 100:
                context = mainActivity.getApplicationContext();
                break;
        }

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
            mapFutureMatch.put("Result","VS");
            mapLastMatch.put("Result", elementsResultLastMatch.get(1).text());

            dbHelper.dbWriteResult(mapLastMatch, "LastMatch");
            dbHelper.dbWriteResult(mapFutureMatch, "FutureMatch");

            noInternetException = false;
        } catch (IOException e) {
            e.printStackTrace();
            noInternetException = true;
        }

        return context;
    }

    @Override
    protected void onPostExecute(Context context) {
        super.onPostExecute(context);

        if(noInternetException){
            Toast.makeText(context, "Не удалось получить данные", Toast.LENGTH_SHORT).show();
        }

        if(typeLoad == 101){
            futureMatch.mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
