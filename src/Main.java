import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Metro;
import core.StationIndex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static core.StationIndex.getLine;

public class Main {

    //public static String htmlFile = Parser.parseFile("data/code.html");
    private static String dataFile = "data/map.json";

    public static void main(String[] args) throws ParseException, org.json.simple.parser.ParseException {

        String htmlFile = Parser.parseFile("data/code.html");
        //FileOutputStream fout = null;
        //BufferedInputStream in = null;
        StationIndex stationIndex = new StationIndex();
        Document doc = Jsoup.parse(htmlFile);
        Element table = doc.select("table").get(2);//получить таблицу по индексу 2 (таблица со станицями метро)
        Elements rows = table.select("tr");//выделить строки таблицы по тегу "tr"
        //rows.forEach(System.out::println); //распечатать таблицу
        rows.stream().skip(1).forEach((row) -> {//skip - пропустить и начать со строки № 1

            Elements cols = row.select("td"); //выбрать множество строк с тегом "td"
            String stationName = cols.get(1).firstElementChild().text(); //в строке с индексом 1 текст - название станции. firstElementChild - без дополнительного текста. Только 1 элемент.
            String lineName = cols.get(0).child(1).attr("title");// титул тега 1 строки 0 - название линии
            String lineNum = cols.get(0).child(0).text();
            String temp1 = cols.get(3).children().text();
            //System.out.println(temp1);
            String stationConnections = cols.get(3).children().attr("title");
            List<String> connectionsLine = cols.get(3).children().eachAttr("title");
            List<String> connectionsNumber = cols.get(3).children().eachText();
            //System.out.println(connectionsLine + " " + connectionsLine.size());
            //System.out.println(connectionsNumber);
            //System.out.println(stationConnections);
            Parser.parseLine(lineNum, lineName); //отпарсили все линии, получилась сказка
            Parser.parseStation(stationName, getLine(lineNum));
        });
        Parser.parseConnection();
        //StationIndex.printConnection(); // разобраться, почему не добавляются коннекшены!
        //System.out.println(StationIndex.getSizeConnections());
        //StationIndex.printLines();
        //List<Station> kievskaya = stationIndex.getStations("Киевская"); // проверка работы getStations
        //StationIndex.printLines(); //создал метод, для проверки содержания списка линий
        //StationIndex.printStations(); // распечатал станции


        Metro metro = new Metro(stationIndex.linesToWrite(), stationIndex.stationsToWrite(), stationIndex.connectionsToWrite());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter file = new FileWriter("data/map.json")) {
              file.write(gson.toJson(metro));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parser.JsonParser();


    }

}


