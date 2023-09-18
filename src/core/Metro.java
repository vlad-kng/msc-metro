package core;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Metro {
    private Map<String, String> lines;
    @SerializedName("stations")
    private Map<String, List<String>> stations;
    @SerializedName("connections")
    private Map<Station, List<String>> connection;

    public Metro(Map<String, String> lines, Map<String, List<String>> stations, Map<Station, List<String>> connection) {
        this.lines = lines;
        this.stations = stations;
        this.connection = connection;
    }


}

