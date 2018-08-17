package ru.anlim.rmatch.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;

public class JsoupHelper extends AsyncTask<Context, Void, Context> {

    private boolean noInternetException;
    private MainActivity mainActivity;

    public JsoupHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected Context doInBackground(Context ... contexts) {

        HashMap <String, String> mapFutureMatch = new HashMap<>();
        HashMap <String, String> mapLastMatch = new HashMap<>();
        ArrayList<String> listLaliga= new ArrayList<>();
        ArrayList<String> listURLLiga = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(contexts[0]);

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

            //Если было дополнительное время в прошлом матче
            if(mapLastMatch.get("Guest").equals("ДВ")){

                mapLastMatch.put("Guest", elementsLastMatch.get(3).attr("alt"));
                mapLastMatch.put("GuestImage", elementsLastMatch.get(3).attr("src"));
                mapLastMatch.put("Tournir", elementsDataLastMatch.get(2).text());
            }

            //Получаем результат прошлого матча
            Elements elementsResultLastMatch = document.select("#match_box_1 span");
            mapFutureMatch.put("Result","VS");
            mapLastMatch.put("Result", elementsResultLastMatch.get(1).text());

            //Получение данных по таблице
            Document documentLiga = Jsoup.connect("http://football.sport-express.ru/foreign/spain/laleague").get();
            Elements elements = documentLiga.select(".m_all");
            for (int i = 0; i < elements.size(); i++) {
                listLaliga.add(elements.get(i).text());
            }


            Elements elements2 = documentLiga.select(".m_all a");

            // 100 для сезона, 60 для начала и конца сезона, 20 для межсезонья
            int countIndex = elements2.size() /20;

            for (int i = 0; i < elements2.size(); i = i + countIndex) {  // +3 для межсезонья +5 для сезона
                String urlTeam = String.valueOf(elements2.get(i).attr("href"));
                String numberTeam = urlTeam.replaceAll("[^0-9]", "");
                String urlImage = "http://ss.sport-express.ru/img/football/commands/" + numberTeam + ".png";
                listURLLiga.add(urlImage);
            }

            dbHelper.dbWriteLiga(listLaliga);
            dbHelper.dbWriteURLImageLaLiga(listURLLiga);
            dbHelper.dbWriteResult(mapLastMatch, "LastMatch");
            if(!mapFutureMatch.isEmpty())
            dbHelper.dbWriteResult(mapFutureMatch, "FutureMatch");
            noInternetException = false;

        } catch (IOException e) {

            e.printStackTrace();
            noInternetException = true;
        }
        return contexts[0];
    }

    @Override
    protected void onPostExecute(Context context) {
        super.onPostExecute(context);

        if (noInternetException){
            Toast.makeText(context, R.string.Error, Toast.LENGTH_SHORT).show();
        }

        mainActivity.setResult();
    }
}
