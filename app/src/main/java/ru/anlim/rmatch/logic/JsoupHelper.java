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
            Document document = Jsoup.connect("https://www.sport-express.ru/football/L/command/68").get();
            Elements elementsMatch = document.select("table a");

            //Будущий матч
            //Получаем названия команд и изображения для будущего матча
            mapFutureMatch.put("Home", elementsMatch.get(6).text());
            mapFutureMatch.put("Guest", elementsMatch.get(7).text());
            mapFutureMatch.put("MatchDate",elementsMatch.get(8).text());
            mapFutureMatch.put("Result","");

            String numberTeam = elementsMatch.get(6).attr("href").replaceAll("[^0-9]", "");
            String urlImage = "http://ss.sport-express.ru/img/football/commands/" + numberTeam + ".png";
            mapFutureMatch.put("HomeImage", urlImage);

            numberTeam = elementsMatch.get(7).attr("href").replaceAll("[^0-9]", "");
            urlImage = "http://ss.sport-express.ru/img/football/commands/" + numberTeam + ".png";
            mapFutureMatch.put("GuestImage", urlImage);


            //Прошлый матч
            //Получаем названия команд и изображения для прошлого матча
            mapLastMatch.put("Home", elementsMatch.get(3).text());
            mapLastMatch.put("Guest", elementsMatch.get(4).text());
            mapLastMatch.put("MatchDate", "");
            mapLastMatch.put("Result", elementsMatch.get(5).text());

            numberTeam = elementsMatch.get(3).attr("href").replaceAll("[^0-9]", "");
            urlImage = "http://ss.sport-express.ru/img/football/commands/" + numberTeam + ".png";
            mapLastMatch.put("HomeImage", urlImage);

            numberTeam = elementsMatch.get(4).attr("href").replaceAll("[^0-9]", "");
            urlImage = "http://ss.sport-express.ru/img/football/commands/" + numberTeam + ".png";
            mapLastMatch.put("GuestImage", urlImage);

            //Получаем название лиги для будущего и прошлого матча
            Elements elementsTournir = document.select("td");
            mapFutureMatch.put("Tournir", elementsTournir.get(18).text());
            mapLastMatch.put("Tournir", elementsTournir.get(14).text());

            /*//Если было дополнительное время в прошлом матче
            if(mapLastMatch.get("Guest").equals("ДВ")){

                //mapLastMatch.put("Guest", elementsLastMatch.get(3).attr("alt"));
                //mapLastMatch.put("GuestImage", elementsLastMatch.get(3).attr("src"));
                mapLastMatch.put("Tournir", elementsDataLastMatch.get(2).text());
            }*/


            //Получение данных по таблице
            Document documentLiga = Jsoup.connect("https://www.sport-express.ru/football/L/foreign/spain/laleague").get();
            Elements elements = documentLiga.select(".m_all");
            for (int i = 0; i < elements.size(); i++) {
                listLaliga.add(elements.get(i).text());
            }

            Elements elements2 = documentLiga.select(".m_all a");

            // 100 для сезона, 60 для начала и конца сезона, 20 для межсезонья
            int countIndex = elements2.size() /20;

            for (int i = 0; i < elements2.size(); i = i + countIndex) {  // +3 для межсезонья +5 для сезона
                String urlTeam = String.valueOf(elements2.get(i).attr("href"));
                numberTeam = urlTeam.replaceAll("[^0-9]", "");
                urlImage = "http://ss.sport-express.ru/img/football/commands/" + numberTeam + ".png";
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
