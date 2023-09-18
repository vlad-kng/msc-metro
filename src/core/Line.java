
package core;

import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line>
{
    private String number;
    private String name;
    private List<core.Station> stations;

    public Line(String number, String name)
    {
        this.number = number;
        this.name = name;
        stations = new ArrayList<>();
    }

    public Line getLine(String number)
    {
      this.number = number;
      for (Line line  : StationIndex.getNumber2line().values())
      {
          if(number.equals(line.getNumber()))
          return line;
      }
      return null;
    }



    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public void addStation(Station station)
    {
        //Station query = StationIndex.getStation(station.getName(), station.getLine().getNumber());
        if (StationIndex.getStation(station.getName(), station.getLine().getNumber()) != null)
        stations.add(station);
    }

    public List<core.Station> getStations()
    {
        return stations;
    }

    @Override
    public int compareTo(Line line)
    {
        int result = line.getNumber()==number? 1 : 0;
        return result;
    }

}