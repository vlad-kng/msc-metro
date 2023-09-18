import core.Line;
import core.Station;
import core.StationIndex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Parser {


//    public void createJsonFile()throws IOException
//    {
//        StationIndex stationIndex = new StationIndex();
//        try (FileWriter file = new FileWriter("data/map.json")) {
//            file.write(GSON).toJson(stationIndex);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String parseFile(String path)
    {
        StringBuilder builder = new StringBuilder();
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines.forEach(line -> builder.append(line + "\n"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return builder.toString();
    }

    public static void parseStation(String name, Line line){
        Station station = new Station(name, line);
        StationIndex.addStation(station);
        line.addStation(station);
    }
    public static void parseLine(String number, String name) {
        if(name != null && StationIndex.getLine(number) == null) {
        Line line = new Line(number, name);
        StationIndex.addLine(line);
        }
    }
    public static void parseConnection(){
        String htmlFile = parseFile("data/code.html");
        Document doc = Jsoup.parse(htmlFile);
        Element table = doc.select("table").get(2);//получить таблицу по индексу 2 (таблица со станицями метро)
        Elements rows = table.select("tr");//выделить строки таблицы по тегу "tr"
        rows.stream().skip(1).forEach((row) -> {//skip - пропустить и начать со строки № 1

            Elements cols = row.select("td"); //выбрать множество строк с тегом "td"
            String stationName = cols.get(1).firstElementChild().text(); //в строке с индексом 1 текст - название станции. firstElementChild - без дополнительного текста. Только 1 элемент.
            String lineName = cols.get(0).child(1).attr("title");// титул тега 1 строки 0 - название линии
            String lineNum = cols.get(0).child(0).text();
            String temp1 = cols.get(3).children().text();
            Station station = StationIndex.getStation(stationName, lineNum);
            List<String> connectionsLine = ((Element)cols.get(3)).children().eachAttr("title");
            List<String> connectionsNumber = ((Element)cols.get(3)).children().eachText();
            if(!connectionsNumber.isEmpty()) {
                ArrayList<Station> connections = new ArrayList<>();
                for (int i = 0; i < connectionsLine.size(); i++) {
                    String lineConnections = connectionsNumber.get(i);
                    String stationsConnect = connectionsLine.get(i);
                    //String[] tmp = temp1.split(" ");
                    //String lineConnections = tmp[0]; //только 1 номер, остальные и так присоеденятся
                    String[] temp = stationsConnect.split("«|»");
                    String stationConnection = temp[1];
                    if (StationIndex.getLine(lineConnections) != null) {
                        Station conectStation = new Station(stationConnection, StationIndex.getLine(lineConnections));
                        connections.add(conectStation);
                        //System.out.println(conectStation.getName() + " " + conectStation.getLine().getNumber()); //для проверки
                    }
                }
                if (!connections.isEmpty())
                    StationIndex.putConnetction(station, connections);
            }
        });
    }

    static void JsonParser() throws ParseException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(parseFile("data/map.json"));

        Map<String, List<String>> stations = (Map<String, List<String>>) jsonObject.get("stations");
        for (String lineId : stations.keySet()) {
            JSONArray stationsArray = (JSONArray) stations.get(lineId);
           System.out.println("Линия " + lineId + " -> количество станций: " + stationsArray.size());

            }

    }

//      static void JsonParser() throws ParseException {
//      JSONParser parser = new JSONParser();
//      JSONObject jsonObject = (JSONObject) parser.parse(parseFile("src\\main\\resources\\map.json"));
//
//      Map<String, List<String>> stations = (Map<String, List<String>>) jsonObject.get("stations");
//      for (String lineId : stations.keySet()) {
//        JSONArray stationsArray = (JSONArray) stations.get(lineId);
//        for (Line line : metro.getLines()) {
//            if (line.getId().equals(lineId)) {
//                System.out.println("Линия " + lineId + " " + line.getName() + " -> количество станций: " + stationsArray.size());
//
//            }
//        }
//    }
//}

}
