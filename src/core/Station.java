package core;

public class Station implements Stations {
    private Line line;
    private String name;

    public Station(String name, Line line)
    {
        this.name = name;
        this.line = line;
    }

    public Line getLine()
    {
        return line;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(core.Station station) {
        if (this.getName().equalsIgnoreCase(station.getName()) && this.getLine().getNumber().equals(station.getLine().getNumber())) {
            return true;
        } else return false;


//        if (this == station) {
//            return true;
//        } else if (station != null && this.getClass() == station.getClass()) {
//            Line line = station.getLine();
//            return Objects.equals(this.getName(), line.getNumber());
//        } else {
//            return false;
//        }
    }

      @Override
    public String toString()
    {
        return name;
    }
}