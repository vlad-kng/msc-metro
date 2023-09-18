package core;

import com.google.gson.annotations.SerializedName;

import java.util.*;
import java.util.stream.Collectors;

public class StationIndex
{
    @SerializedName("lines")
    private static HashMap<String, Line> number2line;
    @SerializedName("stations")
    static List<Station> stations;
    @SerializedName("connections")
    static HashMap <Station, List<Station>> connections; // наверное проще TreeMap<TreeSet<Station>>, но надо проверить

    public StationIndex()
    {
        number2line = (new HashMap<>());
        stations = new ArrayList<>();
        connections = new HashMap<>();
    }


    public static HashMap<String, Line> getNumber2line() {
        return number2line;
    }

    public static void addStation(Station station)
    {
        stations.add(station);
    }

    public static void addLine(Line line)
    {
        //if(number2line.containsKey(line.getNumber()))
        //{}else
            getNumber2line().put((line.getNumber()), line);
    }

    public static void printLines(){
        for(Map.Entry<String, Line> line : getNumber2line().entrySet())
        {
            String number = line.getKey();
            Line line1 = line.getValue();
            String name = line1.getName();
            System.out.println(number + " : " + name);
        }
    }

    public static void printStations(){
        for(Station station : stations)
        {
            Line line1 = station.getLine();
            String name = station.getName();
            String number = line1.getNumber();
            System.out.println(number + " : " + name);
        }
    }

    public static void printConnection()
    {
        for (Map.Entry entry : connections.entrySet())
        {
            Station station1 = (Station) entry.getKey();
            List<Station> stations = (List<Station>) entry.getValue();
            System.out.print("Станция: " + station1.getName() + ", линия " + station1.getLine().getNumber() + ", пересадка на станции: ");
            for (Station station : stations)
            {
                System.out.print(station.getName() + " "+ station.getLine().getNumber() + "; ");
            }
            System.out.println();

//            Station station2 = stations2.get(0);
//            System.out.println("Станция: " + station1.getName() + ", линия " + station1.getLine().getNumber() + ", пересадка на станцию: " + station2.getName() + " линия " + station2.getLine().getNumber() + " size = " + stations2.size());
        }


//        for (List <Station> temp : connections)
//        {
//            for (Station st : temp)
//            {
//                System.out.println("Станция: " + st.getName() + ", линия: " + st.getLine());
//            }
//        }
    }

//    public static void addConnection(List<Station> stations)
//    {
//        List<Station> temp = new ArrayList<>();
//        for(Station station : stations) {
//            if (number2line.containsValue(station.getLine())) {
//                temp.add(station);
//            } else temp.clear();
//        } if (!temp.isEmpty()) {StationIndex.addConnection(temp);}
//    }

    public void addConnection(List<Station> stations)
    {
        for(Station station : stations)
        {
            if(!connections.containsKey(station)) {
                connections.put(station, new ArrayList<>());
            }
//            } else if (connections.containsKey(query)) {
//                connections.compute(query, (k,v) -> v.add((Station) stations.stream()
//                        .filter(s -> !s.equals(query)).collect(Collectors.toList()))); //проверить не херню ли написал!
//
//            }
            ArrayList<Station> connectedStations = (ArrayList<Station>) connections.get(station);
            connectedStations.addAll(stations.stream()
                    .filter(s -> !s.equals(station)).collect(Collectors.toList()));
            connections.put(station, connectedStations);
            }; //добавил, чтобы все переходы добавились к ключу
        }
        public static void putConnetction(Station station, List<Station> stations)
        {
            connections.put(station, stations);
        }


    public static int getSizeConnections()
    {
        return connections.size();
    }


    public static Line getLine(String number)
    {
        return getNumber2line().get(number);
    }

    public HashMap<String, Line> getAllLines()
    {
        return number2line;
    }



//    public static void setNumber2line(HashMap<String, Line> number2line) {
//        StationIndex.number2line = number2line;
//    }

    public Station getStation(String name)
    {

        for(Station station : stations)
        {
            if(station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        Exception ex = new Exception("Wrong station name!");
        ex.printStackTrace();

        return null;
    }

    public List<Station> getAllStatoions()
    {
        return stations;
    }
    public HashMap<Station, List<Station>> getAllConnections()
    {
        return connections;
    }

    public HashMap<String, List<String>> stationsToWrite()
    {
        HashMap<String, List<String>> stations = new HashMap<>();
        for (Line line : number2line.values())
        {
            List<Station> st = new ArrayList<>(line.getStations());
            List<String> names = new ArrayList<>();
            for (Station station : st) {
                names.add(station.getName());
            }
            stations.put(line.getNumber(), names);
        }
        return stations;
    }
    public HashMap<String, String> linesToWrite()
    {
        HashMap<String, String> lines = new HashMap<>();
        for (Line line : number2line.values())
        {
            lines.put(line.getNumber(), line.getName());
        }
        return lines;
    }
    public HashMap<Station, List<String>> connectionsToWrite()
    {
        HashMap<Station, List<String>> connection = new HashMap<>();

        for(Map.Entry entry : connections.entrySet())
        {
            List<String> connects = new ArrayList<>();
            Station station1 = (Station) entry.getKey();
            List<Station> stations = (List<Station>) entry.getValue();
            for (Station station : stations)
            {
                String temp = station.getName() + " : " + station.getLine().getNumber();
                connects.add(temp);
            }
            connection.put(station1, connects);
        }
        return connection;
    }


    public List <Station> getStations(String name) //получить список станций с одинаковым названием, например Киевская, Кунцевская
    {
        List<Station> stm = new ArrayList<>();
        for (Station station : stations)
        {
            if(station.getName().equalsIgnoreCase(name) && !stm.contains(station))
                stm.add(station);
        }
        return stm;
    }

    public static Station getStation(String name, String lineNumber)
    {
        Station query = new Station(name, getLine(lineNumber));
        Station result = null;
        for (Station station : stations) {
            if (station.getName().equalsIgnoreCase(query.getName()) && station.getLine().getNumber().equals(query.getLine().getNumber())) {
                result = query;
            }
        } return result;
    }

    public List<Station> getConnectedStations(Station station)
    {
        for (Station key : connections.keySet())
        {
            if(key.getName().equals(station.getName()) && key.getLine().getNumber().equals(station.getLine().getNumber()))
                return connections.get(key);
        }

//        if(connections.containsKey(station)) {
//            return connections.get(station);
        return null;
        //return new ArrayList<>();
    }
}
